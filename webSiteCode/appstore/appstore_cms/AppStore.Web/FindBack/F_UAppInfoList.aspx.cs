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
    public partial class F_UAppInfoList : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            BindData();
        }


        private void BindData()
        {
            int totalCount = 0;
            pagerList.PageSize = 20;

            AppInfoEntity entity = new AppInfoEntity();

            entity.SearchKeys = this.Keyword_2.Value.Trim();
            entity.StartIndex = pagerList.StartRecordIndex - 1;
            if (pagerList.StartRecordIndex == 1)
            {
                entity.EndIndex = pagerList.PageSize;
            }
            else
            {
                entity.EndIndex = pagerList.EndRecordIndex;
            }

            List<AppInfoEntity> list = new AppInfoBLL().GetUAppInfoList(entity, ref totalCount);
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
    }
}