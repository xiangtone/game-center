using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;


using AppStore.Model;
using AppStore.BLL;
using nwbase_sdk;

namespace AppStore.Web
{
    public partial class CommentList : BasePage
    {
        private string searchType {get;set;}
        private string searchKey { get; set; }
        private string orderType { get; set; }
        private int pageIndex { get; set; }
        private int pageSize { get; set; }

        private AppInfoBLL appInfoBll = new AppInfoBLL();

        /// <summary>
        /// 评论列表
        /// </summary>
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                ResetPager();
                BindData();
            }
        }

        public void BindData()
        {
            this.searchType = SearchType.Text;
            this.searchKey = SearchKey.Text;
            this.orderType = OrderType.SelectedValue;
            
            BindPageComponents();
        }

        private void BindPageComponents()
        {

            DataList.DataSource = new AppCommentsBLL().GetDataList(searchType, searchKey, orderType, pageIndex, pageSize );
            DataList.DataBind(); 

            pagerList.RecordCount = new AppCommentsBLL().GetTotalCount(searchType, searchKey, orderType);
            pagerList.DataBind();
        }

        protected void pagerList_PageChanged(object sender, EventArgs e)
        {
            Wuqi.Webdiyer.AspNetPager pager = (Wuqi.Webdiyer.AspNetPager)sender;
            pageIndex = pager.CurrentPageIndex;
            pageSize = pager.PageSize;
            BindData();
        }

        protected void SearchBtn_Click(object sender, EventArgs e)
        {
            ResetPager();
            BindData();
        }

        protected string AppName(object appId)
        {
            AppInfoEntity info = appInfoBll.GetSingle((int)appId);
            return info.AppName;
        }
        protected string AppType(object appId)
        {
            AppInfoEntity info = appInfoBll.GetSingle((int)appId);
            int appClass = info.AppClass;
            if (appClass == 1 || appClass == 11)
            {
                return "应用";
            }
            else if (appClass == 2 || appClass == 12)
            {
                return "游戏";
            }
            else
            {
                return "";
            }
        }
        private void ResetPager()
        {
            this.pageIndex = 1;
            this.pageSize = 20;
            pagerList.CurrentPageIndex = 1;
        }
    }
}