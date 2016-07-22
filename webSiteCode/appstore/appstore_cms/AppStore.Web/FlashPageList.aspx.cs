using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;

namespace AppStore.Web
{
    public partial class FlashPageList : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
                Bind();
        }

        private void Bind()
        {
            DataList.DataSource = new GroupBLL().FlashPageGetList();
            DataList.DataBind();
        }

        protected string BindStatus(object entity)
        {
            GroupInfoEntity obj = (GroupInfoEntity)entity;
            DateTime currentTime = DateTime.Now;
            if (obj.EndTime < currentTime)
            {
                return "<span class=\"red\">已过期</span>";
            }
            else if (obj.StartTime > currentTime)
            {
                var timeSpan = obj.StartTime - currentTime;

                return string.Format("<span class=\"blue\">开启&nbsp;&nbsp;/&nbsp;&nbsp;{0}后显示</span>", timeSpan.Days > 0 ? timeSpan.Days.ToString() + "天" : timeSpan.Hours.ToString() + "小时");
            }
            else
            {
                return "<span class=\"black\">开启</span>";
            }
        }

        protected string BindTime(object entity)
        {
            GroupInfoEntity obj = (GroupInfoEntity)entity;
            string timePart = string.Format("{0:yyyy.MM.dd HH:mm} ~ {1:yyyy.MM.dd HH:mm}", obj.StartTime, obj.EndTime);
            DateTime currentTime = DateTime.Now;
            
            if (obj.EndTime < currentTime)
            {
                return "<span class=\"black\">显示时间：" + timePart + "</span>";
            }
            else if (obj.StartTime > currentTime)
            {
                return "<span class=\"black\">显示时间：" + timePart + "</span>";
            }
            else
            {
                return "<span class=\"black\">显示时间：" + timePart + "</span>";
            }
        }

        protected void OnDel(object s, CommandEventArgs e)
        {
            int id = nwbase_sdk.Tools.GetInt(e.CommandArgument, 0);
            if (id > 0)
            {
                var result = new GroupBLL().DeleteInfo(id);
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
    }
}