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
    public partial class GameRecommendationPostList : BasePage
    {
        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.Request<int>("GroupTypeID", 5102); } }

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.Request<int>("SchemeID", 103); } }


        protected int PosId = -1;
        protected int OrderNo = -1;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
                Bind();
        }


        private void Bind()
        {
            int posId = Tools.GetRequestVal("PosID", -1);
            int orderNo = Tools.GetRequestVal("order", -1);
            PosId = posId;
            OrderNo = orderNo;
            if (posId < 0) return;
            var allList = new GroupBLL().HomePageRecommendGetElemsByPosId(posId, this.GroupTypeID, this.SchemeID);

            var runningList = new List<GroupElemsEntity>();
            var toRunList = new List<GroupElemsEntity>();
            var expiredList = new List<GroupElemsEntity>();
            DateTime currentTime = DateTime.Now;
            foreach (var eachItem in allList)
            {
                if (eachItem.EndTime < currentTime)
                {
                    //已过期
                    expiredList.Add(eachItem);
                }
                else if (eachItem.StartTime > currentTime)
                {
                    //即将启用
                    toRunList.Add(eachItem);
                }
                else
                {
                    //启用中
                    runningList.Add(eachItem);
                }
            }

            RunningList.DataSource = runningList;
            ToRunList.DataSource = toRunList;
            ExpiredList.DataSource = expiredList;

            RunningList.DataBind();
            ToRunList.DataBind();
            ExpiredList.DataBind();
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

        protected void DelItem(object s, CommandEventArgs e)
        {
            int id = Tools.GetInt(e.CommandArgument, 0);
            if (new GroupBLL().DeleteElem(id))
            {
                this.Alert("删除成功！");
                Bind();
            }
            else
            {
                this.Alert("删除失败");
            }
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

                        Bind(); // 重新绑定页面
                    }
                }
            }
        }
    }
}