using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;

namespace AppStore.Web
{
    public partial class GroupSchemesList : BasePage
    {
        public int SchemeID { get { return this.Request<int>("SchemeID"); } }

        public int GroupID { get { return this.Request<int>("GroupID"); } }

        public int ActType { get { return this.Request<int>("acttype"); } }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (this.SchemeID != 0 && this.GroupID != 0)
                {
                    bool result = new GroupSchemesBLL().Delete(new GroupSchemesEntity() { SchemeID = this.SchemeID, GroupID = this.GroupID });

                    this.Alert(result == true ? "删除成功" : "删除失败");
                }
                this.BindData();
            }
        }

        protected void pagerList_PageChanged(object sender, EventArgs e)
        {
            BindData();
        }

        private void BindData()
        {
            int totalCount = 0;

            List<GroupSchemesEntity> list = new GroupSchemesBLL().GetGroupSchemesList(this.ActType, pagerList.StartRecordIndex - 1, pagerList.PageSize, ref totalCount);
            this.objRepeater.DataSource = list;
            this.objRepeater.DataBind();

            pagerList.RecordCount = totalCount;
            pagerList.DataBind();
        }
    }
}