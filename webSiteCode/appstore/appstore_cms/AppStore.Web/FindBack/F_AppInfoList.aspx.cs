using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;

namespace AppStore.Web.FindBack
{
    public partial class F_AppInfoList : BasePage
    {
        public List<AppInfoEntity> CurrentList { get; set; }
        public Dictionary<int, string> dic_DevList { get; set; }
        public int AppID { get { return this.Request<int>("AppID", 0); } }
        public int SchemeID { get { return this.Request<int>("SchemeID", 1); } }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (this.AppID != 0)
                {
                    if (new AppInfoBLL().Delete(this.AppID))
                    {
                        this.Alert("删除成功");
                    }
                    else
                    {
                        this.Alert("删除失败");
                    }
                }
                this.BindAppType();
            }


            BindData();

        }


        /// <summary>
        /// 绑定应用类型
        /// </summary>
        private void BindAppType()
        {
            List<GroupTypeEntity> list = new GroupTypeBLL().GetDataList(11);
            ListItem objItem = new ListItem("分类筛选", "");
            this.AppType.Items.Add(objItem);

            foreach (GroupTypeEntity item in list)
            {
                if (item.TypeID == 1100)
                {
                    continue;
                }
                else
                {
                    this.AppType.Items.Add(new ListItem(item.TypeName, item.TypeID.ToString()));
                }
            }
        }

        private void BindData()
        {
            int totalCount = 0;

            AppInfoEntity entity = new AppInfoEntity()
            {
                SearchType = SearchType.SelectedValue,
                SearchKeys = this.Keyword_2.Value.Trim(),
                AppClass = 11,
                TypeID = AppType.SelectedValue,
                OrderType = OrderType.SelectedValue,
                StartIndex = pagerList.StartRecordIndex - 1,
                EndIndex = pagerList.PageSize,
                Status = 0
            };

            if (SearchType.SelectedValue == "1")
            {
                entity.SearchKeys = new B_DevBLL().GetDevIDByName(this.Keyword_2.Value);
            }

            dic_DevList = new B_DevBLL().GetDevListDic();
            dic_DevList[0] = "";

            List<AppInfoEntity> list = new AppInfoBLL().GetDataList(entity, ref totalCount, SchemeID);
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