using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using updatesys_cms.BLL;
using updatesys_cms.Model;
using System.Configuration;
using nwbase_utils;
using ServiceStack.Redis;
namespace updatesys_cms.Web
{
    public partial class PackList : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                Bind();
            }
        }

        private void Bind()
        {
            int schemeId = nwbase_utils.Tools.GetInt(ddlSchemeId.SelectedValue, 0);
            PackListRpt.DataSource = new updatesys_cms.BLL.UpdateInfo().GetAll(0, schemeId);
            PackListRpt.DataBind();
        }

        protected string GetType(object typeId)
        {
            int type = Convert.ToInt32(typeId);
            switch (type)
            {
                case 1:
                    return "提示升级";
                case 2:
                    return "标识-小红点";
                case 3:
                    return "强制升级";
            }
            return "未知";
        }

        protected void OnSchemeChanged(object s, EventArgs e)
        {
            Bind();
        }
         
        protected void Del_Click(object s, CommandEventArgs e)
        {
            try
            {
                string[] param = e.CommandArgument.ToString().Split('|');
                int id = Convert.ToInt32(param[0]);
                string packName = param[1];
                string schemeId = param[2];
                string channelNo = param[3];
                var result = new BLL.UpdateInfo().Del(id);
                if (result)
                {
                    string keyName = string.Format("hPackList:{0}_{1}_{2}", schemeId, packName, channelNo);
                    var client = _GetRedisClient();
                    client.Del(keyName);
                    UpdateCache();
                    Bind();
                }
                Alert("删除" + (result ? "成功" : "失败"));
            }
            catch (Exception ex)
            {
                Alert("删除失败！");
                //异常
            }
        }

        protected string BindItem(object s)
        {
            var obj = s as Model.UpdateInfo;
            return string.Format("{0}|{1}|{2}|{3}", obj.UpdateId, obj.PackName, obj.SchemeId, obj.ChannelNo);
        }
    }
}