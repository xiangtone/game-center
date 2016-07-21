using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web.FindBack
{
    public partial class F_GroupTypeList : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                this.BindData();
            }
        }

        private void BindData()
        {
            int totalCount = 0;

            this.objRepeater.DataSource = new GroupTypeBLL().GetGroupTypeList(pagerList.StartRecordIndex - 1, pagerList.PageSize, ref totalCount);
            this.objRepeater.DataBind();
            this.pagerList.RecordCount = totalCount;
        }


        protected void pagerList_PageChanged(object sender, EventArgs e)
        {
            BindData();
        }
    }
}