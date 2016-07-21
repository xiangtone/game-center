using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using AppStore.BLL;
using AppStore.Model;

namespace AppStore.Web.FindBack
{
    public partial class F_GameInfoList : BasePage
    {
        public int AppID { get { return this.Request<int>("AppID", 0); } }
        public int SchemeID { get { return this.Request<int>("SchemeID", 1); } }
        public Dictionary<int, string> dic_DevList { get; set; }

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
            List<GroupTypeEntity> list = new GroupTypeBLL().GetDataList(12);
            ListItem objItem = new ListItem("分类筛选", "");
            this.AppType.Items.Add(objItem);

            foreach (GroupTypeEntity item in list)
            {
                if (item.TypeID == 1200)
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
                IsNetGame = IsNetGame.SelectedValue.Convert<int>(0),
                AppClass = 12,
                TypeID = AppType.SelectedValue,
                OrderType = OrderType.SelectedValue,
                StartIndex = pagerList.StartRecordIndex - 1,
                EndIndex = pagerList.PageSize,
                Status = 1,
            };

            if (SearchType.SelectedValue == "1")
            {
                entity.SearchKeys = new B_DevBLL().GetDevIDByName(this.Keyword_2.Value);
            }

            dic_DevList = new B_DevBLL().GetDevListDic();
            dic_DevList[0] = "";
            List<AppInfoEntity> list;
            list = new AppInfoBLL().GetDataList(entity, ref totalCount, SchemeID).ToList();

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