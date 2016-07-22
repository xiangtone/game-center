using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class ChannelList : BasePage
    {
        public List<ChannelEntity> ChannelInfoList;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                Bind();
                var a = Request.QueryString["action"];
                if (Request.QueryString["action"] == "del")
                {
                    bool rult = new ChannelBLL().Delete(Convert.ToInt32(Request.QueryString["id"]));
                }
                else if (Request.QueryString["action"] == "UpdateStatus")
                {
                    int id = Convert.ToInt32(Request.QueryString["id"]);
                    int status = Convert.ToInt32(Request.QueryString["status"]);
                    bool rult = new ChannelBLL().UpdateStatus(id, status);
                }
            }
        }
        public void Bind()
        {
            ChannelInfoList = new ChannelBLL().Select();
        }
        public string BindStatus(int status)
        {
            string sult = "";
            if (status == 1)
            {
                sult = "启用";
            }
            else
            {
                sult = "禁用";
            }
            return sult;
        }
    }
}