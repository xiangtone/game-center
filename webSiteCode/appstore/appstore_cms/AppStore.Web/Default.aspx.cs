//using AppStore.BLL.Redis;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class Default : System.Web.UI.Page
    {
        protected string UserShowName = "";

        protected void Page_Load(object sender, EventArgs e)
        {
            //if (!IsPostBack)
            //{
            //    Response.Write(new RedisBLL().SAppScheme());

            //    Response.Write(new RedisBLL().HAppInfo());

            //    Response.Write(new RedisBLL().HSchemeGroups());

            //    Response.Write(new RedisBLL().SsGroups());

            //    Response.Write(new RedisBLL().HNewestAppVer());

            //    Response.Write(new RedisBLL().HGroupElems());

            //}
        }


    }
}