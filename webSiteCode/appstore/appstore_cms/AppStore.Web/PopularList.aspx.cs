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
using nwbase_utils;
namespace AppStore.Web
{
    public partial class PopularList : BasePage
    {


        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.Request<int>("acttype", 1); } }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
                Bind();
        }

        private void Bind()
        {
            PopularGameList.DataSource = new GroupBLL().PopularGameGetList(this.SchemeID);
            PopularGameList.DataBind();
            SearchWordsList.DataSource = new GroupBLL().SearchWordsGetList(this.SchemeID);
            SearchWordsList.DataBind();
        }

        protected string BindStatus(object entity)
        {
            GroupElemsEntity obj = (GroupElemsEntity)entity;
            DateTime currentTime = DateTime.Now;
            if (obj.EndTime < currentTime)
            {
                return "<span class=\"red\">已过期</span>";
            }
            //else if (obj.StartTime > currentTime)
            //{
            //    var timeSpan = obj.StartTime - currentTime;

            //    return string.Format("<span class=\"blue\">开启&nbsp;&nbsp;/&nbsp;&nbsp;{0}后显示</span>", timeSpan.Days > 0 ? timeSpan.Days.ToString() + "天" : timeSpan.Hours.ToString() + "小时");
            //}
            else
            {
                return "开启";
            }
        }

        protected string BindTime(object entity)
        {
            GroupElemsEntity obj = (GroupElemsEntity)entity;
            string timePart = string.Format("{0:yyyy.MM.dd HH:mm} ~<br/> {1:yyyy.MM.dd HH:mm}", obj.StartTime, obj.EndTime);
            DateTime currentTime = DateTime.Now;

            if (obj.EndTime < currentTime)
            {
                return "" + timePart + "";
            }
            else if (obj.StartTime > currentTime)
            {
                return "" + timePart + "";
            }
            else
            {
                return "" + timePart + "";
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
            new GroupBLL().UpdateElemPos(items);
        }   
    }
}