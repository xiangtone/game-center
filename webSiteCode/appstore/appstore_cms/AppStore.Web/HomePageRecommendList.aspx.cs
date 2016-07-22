using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.UI.WebControls;
using System.Web.Services;
using AppStore.Common;
using AppStore.BLL;
using AppStore.Model;
using nwbase_sdk;

namespace AppStore.Web
{
    public partial class HomePageRecommendList : BasePage
    {
        /// <summary>
        /// 首页推荐全部数据
        /// </summary>
        public List<GroupElemsEntity> HomePageRecommList { get; set; }

        /// <summary>
        /// 首页轮播数据
        /// </summary>
        public List<GroupElemsEntity> FlashRecommList { get; set; }

        /// <summary>
        /// 固定推荐位数据
        /// </summary>
        public List<GroupElemsEntity> FixedRecommList { get; set; }

        /// <summary>
        /// 随机推荐位数据
        /// </summary>
        public List<GroupElemsEntity> RandomRecommList { get; set; }

        public string Action { get { return this.Request<string>("Action", "4102,1"); } }

        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.Action.Split(',')[0].Convert<int>(0); } }

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.Action.Split(',')[1].Convert<int>(0); } }

        public string PageType { get { return this.Request<string>("page", ""); } }

        protected void Page_Load(object sender, EventArgs e)
        {

            if (!IsPostBack)
            {
                BindData();
            }
        }

        public void BindData()
        {
            this.HomePageRecommList = new GroupBLL().GetHomePageRecommend(this.GroupTypeID, this.SchemeID);

            if (this.HomePageRecommList != null)
            {
                this.FlashRecommList = this.HomePageRecommList.Where(p => p.PosID == 1).ToList();
                this.FixedRecommList = this.HomePageRecommList.Where(p => p.PosID >= 2 && p.PosID <= 3).ToList();
                this.RandomRecommList = this.HomePageRecommList.Where(p => p.PosID > 3).ToList();


                if (this.FlashRecommList.Count == 0)
                {
                    this.FlashRecommList = new List<GroupElemsEntity>();
                    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 1, ElemType = 1, OrderNo = 1, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 1, ElemType = 1, OrderNo = 2, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 1, ElemType = 1, OrderNo = 3, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 1, ElemType = 1, OrderNo = 4, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 1, ElemType = 1, OrderNo = 5, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                }

                if (this.FixedRecommList.Count < 2)
                {
                    this.FixedRecommList = new List<GroupElemsEntity>();


                    this.FixedRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 2, ElemType = 1, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                    this.FixedRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 3, ElemType = 1, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                }

                for (int i = 0; i < this.RandomRecommList.Count; i++)
                {
                    this.RandomRecommList[i].PosID = i + 4;
                }

                DataList.DataSource = this.RandomRecommList;
                DataList.DataBind();
            }
        }

        protected string BindStatus(object entity)
        {
            GroupElemsEntity obj = (GroupElemsEntity)entity;
            DateTime currentTime = DateTime.Now;
            if (obj.EndTime < currentTime)
            {
                return "<span class=\"red\">已过期</span>";
            }
            else if (obj.StartTime > currentTime)
            {
                var timeSpan = obj.StartTime - currentTime;

                return string.Format("<span class=\"blue\">即将启用</span>");
            }
            else
            {
                return "<span class=\"black\">开启</span>";
            }
        }

        protected string BindTime(object entity)
        {
            GroupElemsEntity obj = (GroupElemsEntity)entity;
            string timePart = string.Format("{0:yyyy.MM.dd HH:mm} ~ {1:yyyy.MM.dd HH:mm}", obj.StartTime, obj.EndTime);
            return timePart;
        }

        [WebMethod]
        public static void UpdateOrder(string orderinfo, int SchemeID,string name)
        {
            Dictionary<int, int> items = new Dictionary<int, int>();
            foreach (string eachInfo in orderinfo.Split(','))
            {
                if (string.IsNullOrEmpty(eachInfo))
                    break;
                int appId = Tools.GetInt(eachInfo.Split(':')[0], 0);
                int order = Tools.GetInt(eachInfo.Split(':')[1], 0);
                items.Add(appId, order);
            }
            new GroupBLL().UpdateElemPos(items);
            new HomePageRecommendList().UpdateLog(SchemeID,name);
        }

        /// <summary>
        /// 操作日志
        /// </summary>
        public void UpdateLog(int SchemeID,string name)
        {
            if (SchemeID == 104)
            {
                OperateRecordEntity info = new OperateRecordEntity()
                {
                    ElemId = 0,
                    reason = "",
                    Status = 1,
                    OperateFlag = "3",
                    OperateType = "1",
                    OperateExplain = "首页推荐位排序",
                    OperateContent = "首页推荐位排序",
                    SourcePage = 60,
                    UserName = name
                };
                new OperateRecordBLL().Insert(info);
            }

        }

    }
}