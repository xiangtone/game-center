using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using System.IO;
using System.Text;
using System.Runtime.Serialization.Json;
using System.Text.RegularExpressions;
using System.Web.Script.Serialization;

namespace AppStore.Web
{
    public partial class LeftMenu_GameCenter : System.Web.UI.Page
    {

        public string Action ="";

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {//
            //    Action = Request.QueryString["Action"];
            //    nwbase_utils.TextLog.Default.Info("action111:"+Action);
            }
        }
    }
}