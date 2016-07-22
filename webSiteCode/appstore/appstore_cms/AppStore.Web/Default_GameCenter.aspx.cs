using nwbase_sdk;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class Default_GameCenter : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }
    }

    public class LoginResultObj
    {
        public int uid { get; set; }

        public string user_name { get; set; }

        public string msg { get; set; }
    }
}