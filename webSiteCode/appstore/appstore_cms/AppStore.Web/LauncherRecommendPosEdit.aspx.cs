using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;
using nwbase_sdk;

namespace AppStore.Web
{
    public partial class LauncherRecommendPosEdit : BasePage
    {
        #region 全局属性

        /// <summary>
        /// 分组元素ID
        /// </summary>
        public int GroupElemID { get { return this.Request<int>("GroupElemID", 0); } }

        /// <summary>
        /// 推荐位ID
        /// </summary>
        public int PosID { get { return this.Request<int>("PosID", 0); } }

        /// <summary>
        /// 排序号
        /// </summary>
        public int OrderNo { get { return this.Request<int>("OrderNo", 0); } }

        public string PageType { get { return this.Request<string>("page", ""); } }


        public GroupElemsEntity CurrentEntity { get; set; }

        public string ActType { get { return this.Request<string>("acttype", "4102,1"); } }

        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.ActType.Split(',')[0].Convert<int>(0); } }

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.ActType.Split(',')[1].Convert<int>(0); } }

        #endregion

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                this.BindData();
                BindNavName();
            }
        }
        protected void btnSave_Click(object sender, EventArgs e)
        {
            if (this.Action == "Edit")
            {
                this.Update();
            }
            else
            {
                this.Add();
            }

        }

        private void Add()
        {
            GroupElemsEntity entity = new GroupElemsEntity();
            entity.GroupID = new GroupBLL().LauncherRecommendGetGroupId(SchemeID, GroupTypeID);
            entity.PosID = this.PosID;
            entity.ElemType = this.ElemType.SelectedValue.Convert<int>(1);
            entity.GroupType = this.GroupType.Value.Convert<int>(0);
            entity.OrderNo = this.OrderNo;
            entity.ElemID = this.ElemID.Value.Convert<int>(0);

            entity.RecommTitle = this.RecommTitle.Value.Trim();
            entity.RecommPicUrl = this.MainIconPicUrl.Value.Trim();
            entity.RecommWord = this.RecommWord.Text;
            entity.RecommVal = this.RecommVal.Value.Convert<int>(0);
            entity.Status = this.Status.SelectedValue.Convert<int>(0);
            entity.Remarks = this.Remarks.Text.Trim();

            entity.StartTime = DateTime.Now.AddYears(-100);
            entity.EndTime = DateTime.Now.AddYears(100);



            if (new GroupElemsBLL().IsExist(entity))
            {
                this.Alert("当前应用已存在，添加失败");
            }
            else
            {
                bool result = new GroupElemsBLL().Insert(entity);

                if (result)
                {
                    if (SchemeID == 104)
                    {
                        OperateRecordEntity info = new OperateRecordEntity()
                        {
                            ElemId = entity.ElemID,
                            reason = "",
                            Status = 1,
                            OperateFlag = "1",
                            SourcePage = 65,
                            OperateType = "9",
                            OperateExplain = "热门管理：新增游戏",
                            OperateContent = new AppInfoBLL().GetSingle(entity.ElemID).ShowName,
                            UserName = GetUserName(),
                        };
                        new OperateRecordBLL().Insert(info);
                    }
                    // this.Alert("添加成功", string.Format("LauncherRecommendList.aspx?acttype={0}",ActType));
                    Response.Redirect(string.Format("LauncherRecommendList.aspx?acttype={0}&page={1}", ActType, PageType));

                }
                else
                {
                    this.Alert("添加失败");
                }
            }
        }
        private void BindNavName()
        {
            string navName = "推荐";
            switch (GroupTypeID)
            {
                case 4107:
                    navName = "应用游戏精品推荐";
                    break;
                case 4108:
                    if (PageType == "new")
                    {
                        navName = "热门游戏管理";
                    }
                    else
                    {
                        navName = "精品游戏推荐";
                    }
                    break;
                case 5101:
                    navName = "桌面精品推荐";
                    break;

            }

            literalNavName.Text = navName;

            if (SchemeID == 104)
            {
                ElemType.Items.Add(new ListItem() { Text = "跳转至游戏", Value = "1", Selected = true });
            }
            else
            {
                ElemType.Items.Add(new ListItem() { Text = "跳转至应用或游戏", Value = "1", Selected = true });
            }
        }
        private void BindData()
        {
            if (this.Action == "Edit")
            {
                GroupElemsEntity entity = new GroupBLL().GetLauncherRecommendSingle(SchemeID, this.GroupElemID, GroupTypeID);
                this.ElemType.SelectedValue = entity.ElemType.ToString();
                this.GroupType.Value = entity.GroupType.ToString();
                this.ElemID.Value = entity.ElemID.ToString();
                this.txtShowName.Text = entity.ElemID.ToString();
                this.RecommTitle.Value = entity.RecommTitle;
                this.MainIconPicUrl.Value = entity.RecommPicUrl;
                this.RecommWord.Text = entity.RecommWord;
                this.RecommVal.Value = entity.RecommVal.ToString();
                this.Status.SelectedValue = entity.Status.ToString();
                this.Remarks.Text = entity.Remarks;
            }
        }

        private void Update()
        {
            GroupElemsEntity entity = new GroupElemsEntity();
            entity.GroupElemID = this.GroupElemID;
            entity.ElemID = this.ElemID.Value.Convert<int>(0);
            entity.RecommTitle = this.RecommTitle.Value.Trim();
            entity.RecommPicUrl = this.MainIconPicUrl.Value.Trim();
            entity.RecommWord = this.RecommWord.Text;
            entity.RecommVal = this.RecommVal.Value.Convert<int>(0);
            entity.Status = this.Status.SelectedValue.Convert<int>(0);
            entity.Remarks = this.Remarks.Text.Trim();



            bool result = new GroupBLL().UpdateLauncherRecommend(entity);

            if (result)
            {
                if (SchemeID == 104)
                {
                    OperateRecordEntity info = new OperateRecordEntity()
                    {
                        ElemId = entity.ElemID,
                        reason = "",
                        Status = 1,
                        OperateFlag = "2",
                        SourcePage = 65,
                        OperateType = "9",
                        OperateExplain = "热门游戏管理：编辑游戏",
                        OperateContent =   new AppInfoBLL().GetSingle(entity.ElemID).ShowName,
                        UserName = GetUserName(),
                    };
                    new OperateRecordBLL().Insert(info);
                }
                //this.Alert("更新成功"); Response.Redirect(string.Format("LauncherRecommendList.aspx?acttype={0}&page={1}", ActType, PageType));
                this.Alert("更新成功", Request.ApplicationPath + string.Format("LauncherRecommendList.aspx?acttype={0}&page={1}", ActType, PageType));
                //Response.Redirect(Request.Url. "/LauncherRecommendList.aspx");
                //Response.Redirect(Request.Url.AbsolutePath + string.Format("?acttype={0}", ActType));
            }
            else
            {
                this.Alert("更新失败");
            }

        }

    }
}