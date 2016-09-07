using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using updatesys_cms.BLL;
using updatesys_cms.Model;
using nwbase_utils;

namespace updatesys_cms.Web
{
    public partial class PackEdit : BasePage
    {
        private int _UpdateId = 0;
        protected void Page_Load(object sender, EventArgs e)
        {
            _UpdateId = string.IsNullOrEmpty(Request.QueryString["id"]) ? 0 : Convert.ToInt32(Request.QueryString["id"]);
            if (!IsPostBack)
                Bind();
        }
        private void Bind()
        {
            var appList = new updatesys_cms.BLL.AppInfo().GetAll();
            string act = Request.QueryString["act"];

            ddlAppList.DataTextField = "AppName";
            ddlAppList.DataValueField = "AppID";
            ddlAppList.DataSource = appList;
            ddlAppList.DataBind();

            // 初始化
            txtForceUpdateVerCode.Text = "0";

            if (act == "edit" && _UpdateId > 0)
            {
                Model.UpdateInfo updateInfo = new BLL.UpdateInfo().GetOne(_UpdateId);
                if (updateInfo != null)
                {
                    ddlAppList.SelectedValue = updateInfo.AppId.ToString();
                    txtChannelNo.Text = updateInfo.ChannelNo;
                    txtVerName.Text = updateInfo.VerName;
                    txtVerCode.Text = updateInfo.VerCode.ToString();
                    txtPackMD5.Text = updateInfo.PackMD5;
                    txtPackName.Text = updateInfo.PackName;
                    txtPackSize.Text = updateInfo.PackSize.ToString();
                    txtPackUrl.Text = updateInfo.PackUrl;
                    txtUpdateDesc.Text = updateInfo.UpdateDesc.Replace("\n", "\r\n");
                    txtUpdatePrompt.Text = updateInfo.UpdatePrompt.Replace("\n", "\r\n");
                    ddlUpdateType.SelectedValue = updateInfo.UpdateType.ToString();
                    ddlSchemeId.SelectedValue = updateInfo.SchemeId.ToString();
                    ddlStatus.SelectedValue = updateInfo.Status.ToString();
                    txtForceUpdateVerCode.Text = updateInfo.ForceUpdateVerCode.ToString();
                }
            }
        }

        protected void Save_Click(object s, EventArgs e)
        {
            Model.UpdateInfo updateInfo = new Model.UpdateInfo();
            updateInfo.AppId = Convert.ToInt32(ddlAppList.SelectedValue);
            updateInfo.ChannelNo = txtChannelNo.Text;
            updateInfo.VerName = txtVerName.Text;
            updateInfo.VerCode = Convert.ToInt32(txtVerCode.Text);
            updateInfo.PackMD5 = txtPackMD5.Text;
            updateInfo.PackName = txtPackName.Text;
            updateInfo.PackSize = Convert.ToInt32(txtPackSize.Text);
            updateInfo.PackUrl = txtPackUrl.Text;
            updateInfo.PubTime = DateTime.Now;
            updateInfo.UpdateDesc = txtUpdateDesc.Text;
            updateInfo.UpdatePrompt = txtUpdatePrompt.Text;
            updateInfo.UpdateType = Convert.ToInt32(ddlUpdateType.SelectedValue);
            updateInfo.Status = Convert.ToInt32(ddlStatus.SelectedValue);
            updateInfo.SchemeId = nwbase_utils.Tools.GetInt(ddlSchemeId.SelectedValue, 0);
            updateInfo.UpdateDesc = updateInfo.UpdateDesc.Replace("\r\n", "\n");
            updateInfo.UpdatePrompt = updateInfo.UpdatePrompt.Replace("\r\n", "\n");
            updateInfo.ForceUpdateVerCode = Convert.ToInt32(txtForceUpdateVerCode.Text);

            bool result = false;

            if (_UpdateId <= 0)
            {
                //新增
                //appinfo.AppID = Convert.ToInt32(txtAppId.Text);
                result = Add(updateInfo);
            }
            else
            {
                //修改
                updateInfo.UpdateId = _UpdateId;
                result = Update(updateInfo);
            }
            if (result)
            {
                string keyName = string.Format("hPackList:{0}_{1}_{2}", updateInfo.SchemeId, updateInfo.PackName, updateInfo.ChannelNo);
                var client = _GetRedisClient();
                client.Del(keyName);

                UpdateCache();
                ExecJS( "DlgIfrmCloseInSubwin(\"Reload\", true);");
                //string cache_key = "hApp:" + appinfo.AppID;

                //Dictionary<string, string> cache_app_info = new Dictionary<string, string>();
                //cache_app_info.Add("uid", "0");
                //cache_app_info.Add("devid", "0");
                //cache_app_info.Add("name", appinfo.AppName);
                //cache_app_info.Add("packflag", appinfo.PackFlag);
                //cache_app_info.Add("apptoken", appinfo.AppToken);
                //cache_app_info.Add("authlevel", "0");
                //cache_app_info.Add("authtime", appinfo.CreateTime.ToString());
                //cache_app_info.Add("status", appinfo.Status.ToString());
                //rc.SetRangeInHash(cache_key, cache_app_info);
                //rc.Del("payua_Appinfo");
                //
                //
            }

            Alert("保存" + (result ? "成功" : "失败"));
        }

        private bool Update(Model.UpdateInfo updateInfo)
        {
            bool update_result = new BLL.UpdateInfo().Update(updateInfo);
            return update_result;
        }

        private bool Add(Model.UpdateInfo updateInfo)
        {
            bool add_result = new BLL.UpdateInfo().Add(updateInfo);
            return add_result;
        }
    }
}