using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class LinkInfoList : BasePage
    {
        public Dictionary<int, string> dic_DevList { get; set; }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (this.Id != 0)
                {
                    new LinkInfoBLL().Delete(new LinkInfoEntity() { LinkID = this.Id , Status = 2});
                }

                this.BindData();
            }
        }

        protected void btnSearch_Click(object sender, EventArgs e)
        {
            BindData();
        }

        private void BindData()
        {
            int totalCount = 0;

            List<LinkInfoEntity> list = new LinkInfoBLL().GetDataList(pagerList.StartRecordIndex - 1, pagerList.PageSize, ref totalCount, this.Keyword_2.Value);

            dic_DevList = new B_DevBLL().GetDevListDic();
            dic_DevList[0] = "";

            this.objRepeater.DataSource = list;
            this.objRepeater.DataBind();



            pagerList.RecordCount = totalCount;
            pagerList.DataBind();
        }

        protected void pagerList_PageChanged(object sender, EventArgs e)
        {
            BindData();
        }
    }
}