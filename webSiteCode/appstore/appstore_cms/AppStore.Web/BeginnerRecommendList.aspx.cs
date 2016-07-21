using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;
using System.Web.Services;
using nwbase_sdk;
namespace AppStore.Web
{
    public partial class BeginnerRecommendList : BasePage
    {
        public string Action { get { return this.Request<string>("acttype", "4101,1"); } }

        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.Action.Split(',')[0].Convert<int>(0); } }

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.Action.Split(',')[1].Convert<int>(0); } }

        protected List<GroupElemsEntity> RecommPosLists;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
                Bind();
        }

        private void Bind()
        {
            DataList.DataSource = new GroupBLL().BeginnerRecommendGetList(this.GroupTypeID, this.SchemeID);
            DataList.DataBind();
        }

        protected string BindStatus(object entity)
        {
            GroupElemsEntity obj = (GroupElemsEntity)entity;
            DateTime currentTime = DateTime.Now;
            if (obj.StartTime > currentTime || obj.EndTime < currentTime)
            {
                return "<span class=\"red\">已过期</span>";
            }
            else if (obj.EndTime.AddHours(-24) < currentTime)
            {
                return "<span class=\"blue\">启用中</span>";
            }
            else
            {
                return "<span class=\"white\">启用中</span>";
            }
        }

        protected string BindTime(object entity)
        {
            GroupElemsEntity obj = (GroupElemsEntity)entity;
            string timePart = string.Format("{0:yyyy.MM.dd HH:mm} ~ {1:yyyy.MM.dd HH:mm}", obj.StartTime, obj.EndTime);
            DateTime currentTime = DateTime.Now;
            if (obj.StartTime > currentTime || obj.EndTime < currentTime)
            {
                return "<span class=\"red\">" + timePart + "</span>";
            }
            else if (obj.EndTime.AddHours(-24) < currentTime)
            {
                return "<span class=\"blue\">" + timePart + "</span>";
            }
            else
            {
                return "<span class=\"white\">" + timePart + "</span>";
            }
        }

        protected void OnDel(object s, CommandEventArgs e)
        {
            int id = nwbase_sdk.Tools.GetInt(e.CommandArgument, 0);
            if (id > 0)
            {
                var result = new GroupBLL().DeleteElem(id);
                if (result)
                {
                    this.Alert("删除成功");
                    Bind();
                }
                else
                    this.Alert("删除失败");
            }
            else
                this.Alert("删除参数无效");
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
            new GroupBLL().UpdateElemOrder(items);
        }
    }
}