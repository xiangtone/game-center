using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class logincheck : BasePage
    {

        protected void Page_Load(object sender, EventArgs e)
        {
            //RightBase rb = new RightBase();
            //g_uri = rb.GetUserRights(this.Request, 1);
            //if (g_uri == null || g_uri.ErrorCode != 0)
            //{
            //    Response.Write("false");
            //}
            //else
            //{
            //    Response.Write("true");
            //}
            if (IsLogin)
                Response.Write("true");
            else
                Response.Write("false");
            Response.End();
        }

        protected override void OnUnAuth()
        {
            //base.OnUnAuth();
        }
    }
}