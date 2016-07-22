using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.Services;

using AppStore.BLL;
using AppStore.Model;
using nwbase_sdk;
using AppStore.Common;
namespace AppStore.Web
{
    public partial class GameRecommendation : BasePage
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

        public string ActType { get { return this.Request<string>("acttype", "5102,103"); } }

        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.ActType.Split(',')[0].Convert<int>(0); } }

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.ActType.Split(',')[1].Convert<int>(0); } }

        protected int PosId = 1;
        protected int OrderNo = 1;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                BindData();
                if (Request.QueryString["action"] == "ChangeOrdeNo")
                {
                    string[] elemIdArray = SubStr(Request.Form["elemId"]);
                    string[] OrderNoArray = SubStr(Request.Form["OrderNo"]);
                    //修改OrderNO
                    bool rult = true;
                    for (int i = 0; i < elemIdArray.Length; i++)
                    {
                        if (new GroupBLL().UpdateOrderNoById(Convert.ToInt32(elemIdArray[i]), Convert.ToInt32(OrderNoArray[i])) > 0)
                        {
                            rult = true;
                        }
                        else { rult = false; }
                    }
                    if (rult == true)
                    {
                        Response.Write("保存成功！");
                        Response.End();
                    }
                    else
                    {
                        Response.Write("保存失败！");
                        Response.End();
                    }
                }
                
            }
        }
        public string[] SubStr(string data)
        {
            return data.Substring(0, data.Length - 1).Split(',');
        }
        //private void Bind()
        //{
        //    int posId = Tools.GetRequestVal("PosID", 1);
        //    int orderNo = Tools.GetRequestVal("order", 1);
        //    PosId = posId;
        //    OrderNo = orderNo;
        //    //if (posId < 0) return;
        //    var allList = new GroupBLL().HomePageRecommendGetElemsByPosId(this.GroupTypeID, this.SchemeID);

        //    var runningList = new List<GroupElemsEntity>();
        //    var toRunList = new List<GroupElemsEntity>();
        //    var expiredList = new List<GroupElemsEntity>();
        //    DateTime currentTime = DateTime.Now;
        //    foreach (var eachItem in allList)
        //    {
        //        if (eachItem.EndTime < currentTime)
        //        {
        //            //已过期
        //            expiredList.Add(eachItem);
        //        }
        //        else if (eachItem.StartTime > currentTime)
        //        {
        //            //即将启用
        //            toRunList.Add(eachItem);
        //        }
        //        else
        //        {
        //            //启用中
        //            runningList.Add(eachItem);
        //        }
        //    }

        //    RunningList.DataSource = runningList;
        //    //ToRunList.DataSource = toRunList;
        //    //ExpiredList.DataSource = expiredList;

        //    RunningList.DataBind();
        //    //ToRunList.DataBind();
        //    //ExpiredList.DataBind();
        //}

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

        protected void DelItem(object s, CommandEventArgs e)
        {
            int id = Tools.GetInt(e.CommandArgument, 0);
            if (new GroupBLL().DeleteElem(id))
            {
                this.Alert("删除成功！");
                BindData();
            }
            else
            {
                this.Alert("删除失败");
            }
        }
        public void BindData()
        {
            //Bind();
            this.HomePageRecommList = new GroupBLL().GetHomePageRecommend(this.GroupTypeID, this.SchemeID);

            if (this.HomePageRecommList != null)
            {
                this.FlashRecommList = this.HomePageRecommList.Where(p => p.ShowType ==1).ToList();
                //this.FixedRecommList = this.HomePageRecommList.Where(p => p.PosID >= 2 && p.PosID <= 3).ToList();
                this.RandomRecommList = this.HomePageRecommList.Where(p => p.ShowType != 1).ToList();

                for (int j = 0; j < this.FlashRecommList.Count; j++)
                {
                    this.FlashRecommList[j].PosID = j;
                }
                RunningList.DataSource = FlashRecommList;
                RunningList.DataBind();

                //if (this.FlashRecommList.Count == 0)
                //{
                //    this.FlashRecommList = new List<GroupElemsEntity>();
                //    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 1, ElemType = 1, OrderNo = 1, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                //    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 2, ElemType = 1, OrderNo = 2, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                //    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 3, ElemType = 1, OrderNo = 3, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                //    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 4, ElemType = 1, OrderNo = 4, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                //    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 5, ElemType = 1, OrderNo = 5, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                //}

                //if (this.FixedRecommList.Count < 2)
                //{
                //    this.FixedRecommList = new List<GroupElemsEntity>();


                //    this.FixedRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 2, ElemType = 1, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                //    this.FixedRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 3, ElemType = 1, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                //}

                for (int i = 0; i < this.RandomRecommList.Count; i++)
                {
                    this.RandomRecommList[i].PosID = i + 4;
                }

            }
        }

       
        [WebMethod]
        public static void UpdateOrder(string orderinfo)
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
        }
        protected void lbtnMoveIndex_Command(object sender, CommandEventArgs e)
        {
            int groupElemID = Convert.ToInt32(e.CommandName);
            if (groupElemID > 0)
            {
                GroupElemsEntity groupElem = new GroupBLL().GetGroupElemByID(groupElemID);
                if (groupElem != null)
                {
                    string strWhere = "";
                    string strOrder = "OrderNo";
                    int posId = Tools.GetRequestVal("PosID", -1);
                    string cmd = e.CommandArgument.ToString();
                    if (cmd == "up") // 上移
                    {
                        strWhere = string.Format("OrderNo < {0} and status=1", groupElem.OrderNo);
                        strOrder += " desc";
                    }
                    else // 下移
                    {
                        strWhere = string.Format("OrderNo > {0} and status=1", groupElem.OrderNo);
                    }
                    strWhere += string.Format(" and GroupID={0} and PosID={1}", groupElem.GroupID, posId);

                    List<GroupElemsEntity> groupElemList = new GroupBLL().GetList(1, strWhere, strOrder);

                    if (groupElemList != null && groupElemList.Count > 0)
                    {
                        Dictionary<int, int> orderNoDic = new Dictionary<int, int>();
                        orderNoDic.Add(groupElem.GroupElemID, groupElemList[0].OrderNo);
                        orderNoDic.Add(groupElemList[0].GroupElemID, groupElem.OrderNo);
                        new GroupBLL().UpdateElemOrder(orderNoDic);
                        BindData(); // 重新绑定页面
                    }
                    BindData(); // 重新绑定页面
                }
            }
           
        }
    }
}