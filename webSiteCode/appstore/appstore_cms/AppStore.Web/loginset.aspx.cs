using nwbase_utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class loginset : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (Request.ContentLength > 0)
            {
                var requestData = Request.BinaryRead((int)Request.ContentLength);
                try
                {
                    var requestJson = JsonSerializer.Deserialize<LoginResultObj>(System.Text.Encoding.UTF8.GetString(requestData));

                    //BasePage.IsLogin = true;
                    //BasePage.uid = requestJson.uid;
                    //BasePage.uname = requestJson.user_name;
                    //BasePage.UserShowName = requestJson.user_name;

                    if (Request.Cookies["user"] != null)
                    {
                        HttpCookie cookies = Request.Cookies["user"];
                        cookies["name"] = requestJson.user_name;
                        cookies["id"] = requestJson.uid.ToString();
                        cookies.Domain = "niuwan.cc";
                        //Response.Cookies["user"].Expires = DateTime.Now.AddMinutes(-1);  
                    }
                    else
                    {
                        HttpCookie cookies = new HttpCookie("user");
                        cookies["name"] = requestJson.user_name;
                        cookies["id"] = requestJson.uid.ToString();
                        cookies.Domain = "niuwan.cc";
                        //cookies.Expires = DateTime.Now.AddMinutes(30);
                        Response.Cookies.Add(cookies);
                    }

                }
                catch (Exception ex)
                {
                    Response.Write("error");
                    nwbase_utils.TextLog.Error("error", "login_exception", ex);
                }
            }
        }
    }
}