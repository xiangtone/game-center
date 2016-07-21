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
    public partial class CPsList : BasePage
    {
        public List<CPsEntity> CpsInfoList;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                Bind();
                var a = Request.QueryString["action"];
                if (Request.QueryString["action"] == "del")
                {
                    bool rult = new CPsBLL().Delete(Convert.ToInt32(Request.QueryString["id"]));
                }
                else if (Request.QueryString["action"] == "UpdateStatus")
                {
                    int id = Convert.ToInt32(Request.QueryString["id"]);
                    int status = Convert.ToInt32(Request.QueryString["status"]);
                    bool rult = new CPsBLL().UpdateStatus(id, status);
                }
            }
        }
        public void Bind()
        {
            CpsInfoList = new CPsBLL().Select();
        }
    }
}