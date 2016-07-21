using AppStore.Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class MasterPage : System.Web.UI.MasterPage
    {
        public string Docdomain { get { return Extensions.AppSettings("Docdomain", ""); } } 
        protected void Page_Load(object sender, EventArgs e)
        {

        }
    }
}