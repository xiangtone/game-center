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
    public partial class IosAppInfoList : BasePage
    {
        public List<AppInfoiosEntity> CurrentList { get; set; }
        public Dictionary<int, string> dic_DevList { get; set; }
        public int AppID { get { return this.Request<int>("AppID", 0); } }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
               
                BindData();
            }

        }

        private void BindData()
        {
            int totalCount = 0;

            AppInfoiosEntity entity = new AppInfoiosEntity()
            {
                SearchType = SearchType.SelectedValue,
                StartIndex = pagerList.StartRecordIndex - 1,
                EndIndex = pagerList.PageSize,
                Status = 1
            };

           
            entity.SearchKeys = this.SearchKeys.Value;

            List<AppInfoiosEntity> list = new AppInfoiosBLL().GetDataList(entity,ref totalCount);
            this.objRepeater.DataSource = list;
            this.objRepeater.DataBind();

            pagerList.RecordCount = totalCount;
            pagerList.DataBind();
        }

        protected void pagerList_PageChanged(object sender, EventArgs e)
        {

            BindData();

        }

        protected void btnSearch_Click(object sender, EventArgs e)
        {

            BindData();

        }

        protected void objRepeater_ItemCommand(object source, RepeaterCommandEventArgs e)
        {

        }

        protected void SearchType_SelectedIndexChanged(object sender, EventArgs e)
        {

        }
    }
}