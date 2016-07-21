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
    public partial class SearchWordsEdit : BasePage
    {

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.Request<int>("SchemeID", 1); } }

        protected GroupElemsEntity _CurrentEntity = null;
        protected int _Id = 0;
        protected void Page_Load(object sender, EventArgs e)
        {
            _Id = nwbase_sdk.Tools.GetRequestVal("id", 0);
            if (!IsPostBack)
                Bind();
        }

        protected void OnSave(object sender, EventArgs e)
        {
            int maxPosId = new GroupBLL().MaxPisId(this.SchemeID);

            var currentEntity = new GroupElemsEntity();
            currentEntity.GroupElemID = _Id;
            currentEntity.PosID = maxPosId+1;
            currentEntity.ElemID = 0;
            currentEntity.ElemType = 6;
            currentEntity.RecommPicUrl = "";
            currentEntity.RecommTitle = txtRecommTitle.Text;
            currentEntity.RecommWord = "";
            currentEntity.StartTime = DateTime.ParseExact(txtStartTime.Text, "yyyy-MM-dd HH:mm", System.Globalization.CultureInfo.CurrentCulture);
            currentEntity.EndTime = DateTime.ParseExact(txtEndTime.Text, "yyyy-MM-dd HH:mm", System.Globalization.CultureInfo.CurrentCulture);
            currentEntity.Status = nwbase_sdk.Tools.GetInt(ddlStatus.SelectedValue, 1);
            currentEntity.UpdateTime = DateTime.Now;
            currentEntity.Remarks = string.Empty;
            currentEntity.GroupID = new GroupBLL().SearchWordsGetGroupId(this.SchemeID);
            bool result = false;
            if (_Id <= 0)
            {
                //新增
                currentEntity.CreateTime = DateTime.Now;
                result = new GroupBLL().SearchWordsInsert(currentEntity);
            }
            else
            {
                result = new GroupBLL().SearchWordsUpdate(currentEntity);
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
                _CurrentEntity = new GroupBLL().GetGroupElemByID(_Id);
                if (_CurrentEntity != null)
                {
                    txtRecommTitle.Text = _CurrentEntity.RecommTitle;
                    txtStartTime.Text = _CurrentEntity.StartTime.ToString("yyyy-MM-dd HH:mm");
                    txtEndTime.Text = _CurrentEntity.EndTime.ToString("yyyy-MM-dd HH:mm");
                    ddlStatus.SelectedValue = _CurrentEntity.Status.ToString();
                }

            }
            else { txtStartTime.Text = DateTime.Now.ToString("yyyy-MM-dd HH:mm");
            txtEndTime.Text = "2025-01-01 00:00";
            }
        }
    }
}