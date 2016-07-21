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
    public partial class GameInfoList : BasePage
    {
        public int AppID { get { return this.Request<int>("AppID", 0); } }
        public int MaxAppID { get { return this.Request<int>("MaxAppID", 0); } }

        public Dictionary<int, string> dic_DevList { get; set; }

        public string BindDev(object val)
        {
            int CPID = Convert.ToInt32(val);
            if (dic_DevList.ContainsKey(CPID))
            {
                return dic_DevList[CPID];
            }
            return "同步中";
        }

        /// <summary>
        /// 判定状态显示
        /// </summary>
        /// <param name="val"></param>
        /// <returns></returns>
        public string BindStatus(object val)
        {
            //状态，定义：1=正常，2=禁用，3=删除，4=接入中 ，5=测试中，6=待审核，7=审核不通过，12=数据异常，22=控制禁用  ，99=自动获取待上传的， 98=自动获取后删除的
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
            else if (Status == 3)
            {
                status_val = "删除";
            }
            else if (Status == 4)
            {
                status_val = "接入中";
            }
            else if (Status == 5)
            {
                status_val = "测试中";
            }
            else if (Status == 6)
            {
                status_val = "待审核";
            }
            else if (Status == 7)
            {
                status_val = "审核不通过";
            }
            else if (Status == 12)
            {
                status_val = "数据异常";
            }
            else if (Status == 22)
            {
                status_val = "控制异常";
            }
            return status_val;
        }


        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (this.AppID != 0)
                {
                    if (new AppInfoBLL().Delete(this.AppID))
                    {
                        AppInfoEntity entity = new AppInfoBLL().GetSingle(AppID);
                        OperateRecordEntity info = new OperateRecordEntity()
                        {
                            ElemId = AppID,
                            reason = "",
                            Status = 1,
                            OperateFlag = "3",
                            OperateType = "2",
                            OperateExplain = "删除游戏",
                             SourcePage=1,
                            OperateContent = entity.ShowName,
                            UserName = GetUserName(),
                        };
                        new OperateRecordBLL().Insert(info);
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
            if (MaxAppID > 0)
            {
                list = list.Where(a => a.AppID < MaxAppID).ToList();
            }
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