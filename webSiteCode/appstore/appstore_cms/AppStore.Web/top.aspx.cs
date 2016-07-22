using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class top : BasePage
    {
        //public top()
        //    : base(false)
        //{

        //}

        protected void Page_Load(object sender, EventArgs e)
        {

        }
        public string GetUserName()
        {
            try
            {
                return AuthUser.UserName;
            }
            catch (Exception e)
            {

                return "";
            }
           
        }

    }
}