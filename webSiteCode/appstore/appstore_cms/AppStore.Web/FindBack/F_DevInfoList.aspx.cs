using AppStore.BLL;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web.FindBack
{
    public partial class F_DevInfoList : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                Bind();
            }
        }

        protected void btnSearch_Click(object sender, EventArgs e)
        {
            Bind();
        }


        protected void Bind()
        {
            int total = 0;
            this.objRepeater.DataSource = new B_DevBLL().GetDataListByPager(pagerList.StartRecordIndex - 1, pagerList.PageSize, ref total, base.Keyword_1);

            this.objRepeater.DataBind();

            pagerList.RecordCount = total;
            pagerList.DataBind();
        }

        protected void pagerList_PageChanged(object sender, EventArgs e)
        {
            Bind();
        }
    }
}