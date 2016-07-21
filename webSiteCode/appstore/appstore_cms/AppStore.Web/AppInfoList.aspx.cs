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
    public partial class AppInfoList : BasePage
    {
        public List<AppInfoEntity> CurrentList { get; set; }
        public Dictionary<int, string> dic_DevList { get; set; }
        public int AppID { get { return this.Request<int>("AppID", 0); } }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (this.AppID != 0)
                {

                    
                    if (new AppInfoBLL().IsExistGroupElems(this.AppID))
                    {
                        this.Alert("推荐中存在该应用，请先处理再删除！");
                    }
                    else
                    {
                        if (new AppInfoBLL().Delete(this.AppID))
                        {
                            //AppInfoEntity entity = new AppInfoBLL().GetSingle(AppID);
                            //OperateRecordEntity info = new OperateRecordEntity()
                            //{
                            //    ElemId = AppID,
                            //    reason = "",
                            //    Status = 1,
                            //    OperateFlag = "13",
                            //    OperateType = "2",
                            //    OperateExplain = "删除游戏",
                            //    OperateContent =entity.ShowName,
                            //    UserName = GetUserName(),
                            //};
                            //new OperateRecordBLL().Insert(info);
                            this.Alert("删除成功");
                        }
                        else
                        {
                            this.Alert("删除失败");
                        }
                    }
                }
                this.BindAppType();
            }

            BindData();


        }


        /// <summary>
        /// 判定状态显示
        /// </summary>
        /// <param name="val"></param>
        /// <returns></returns>
        public string BindStatus(object val)
        {
            int Status = Convert.ToInt32(val);
            string status_val = "";
            if (Status == 1)
            {
                status_val = "启用";
            }
            else if (Status == 2)
            {
                status_val = "禁用";
            }
            else if (Status == 12)
            {
                status_val = "数据异常";
            }
            else
            {
                status_val = "控制异常";
            }
            return status_val;
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
                AppType = AppType.SelectedValue.Convert<int>(0),
                OrderType = OrderType.SelectedValue,
                StartIndex = pagerList.StartRecordIndex - 1,
                EndIndex = pagerList.PageSize,
                Status = 1
            };

            if (SearchType.SelectedValue == "1")
            {
                entity.SearchKeys = new B_DevBLL().GetDevIDByName(this.Keyword_2.Value);
            }

            dic_DevList = new B_DevBLL().GetDevListDic();
            dic_DevList[0] = "";

            List<AppInfoEntity> list = new AppInfoBLL().GetDataList(entity, ref totalCount);
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