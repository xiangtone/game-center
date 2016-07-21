using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;
using nwbase_sdk;
namespace AppStore.Web
{
    public partial class FlashPageEdit : BasePage
    {
        protected GroupInfoEntity _CurrentEntity = null;
        protected int _Id = 0;
        protected void Page_Load(object sender, EventArgs e)
        {
            _Id = nwbase_sdk.Tools.GetRequestVal("id", 0);
            if (!IsPostBack)
                Bind();
        }

        protected void OnSave(object sender, EventArgs e)
        {
            var currentEntity = new GroupInfoEntity();
            currentEntity.GroupID = _Id;
            currentEntity.GroupTypeID = 4105;
            currentEntity.OrderType = 0;
            currentEntity.OrderNo = 0;
            currentEntity.GroupName = "闪屏";
            currentEntity.GroupDesc = txtDesc.Text;
            currentEntity.GroupTips = "";
            currentEntity.GroupPicUrl = ThumbPicUrl.Value;
            currentEntity.Remarks = "";
            currentEntity.StartTime = DateTime.ParseExact(txtStartTime.Text, "yyyy-MM-dd HH:mm", System.Globalization.CultureInfo.CurrentCulture);
            currentEntity.EndTime = DateTime.ParseExact(txtEndTime.Text, "yyyy-MM-dd HH:mm", System.Globalization.CultureInfo.CurrentCulture);
            currentEntity.Status = nwbase_sdk.Tools.GetInt(ddlStatus.SelectedValue, 1);
            currentEntity.UpdateTime = DateTime.Now;
            currentEntity.Remarks = string.Empty;

            bool result = false;
            if (_Id <= 0)
            {
                //新增
                currentEntity.CreateTime = DateTime.Now;
                result = new GroupBLL().FlashPageInsert(currentEntity);
            }
            else
            {
                result = new GroupBLL().FlashPageUpdate(currentEntity);
            }
            if (result)
            {
                this.Alert("保存成功");
                Bind();
            }
            else
                this.Alert("保存失败！");
        }

        private void Bind()
        {
            if (_Id > 0)
            {
                _CurrentEntity = new GroupBLL().GetGroupInfoByID(_Id);
                if (_CurrentEntity != null)
                {
                    txtDesc.Text = _CurrentEntity.GroupDesc;
                    ThumbPicUrl.Value = _CurrentEntity.GroupPicUrl;
                    ShowThumbPic.ImageUrl = _CurrentEntity.GroupPicUrl;
                    txtStartTime.Text = _CurrentEntity.StartTime.ToString("yyyy-MM-dd HH:mm");
                    txtEndTime.Text = _CurrentEntity.EndTime.ToString("yyyy-MM-dd HH:mm");
                    ddlStatus.SelectedValue = _CurrentEntity.Status.ToString();
                }
            }
        }
    }
}