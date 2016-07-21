using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class logout : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {

                if (IsLogin && LogoutUser(AuthUser.UserId, AuthUser.UserToken))
                {
                    IsLogin = false;
                    Response.Redirect("/login.aspx?msg=注销成功，请重新登录！");
                }
                else
                {
                    IsLogin = false;
                    Response.Redirect("/login.aspx?msg=注销成功，请重新登录！");
                    //Response.Write(string.Format("用户ID｛{0}｝/用户Token｛{1}｝注销失败！", g_uri.UserId.ToString(), g_uri.UserToken));
                }
                Response.End();
            }
        }

        private bool LogoutUser(int uid, string utoken)
        {
            DelCookies("user");
            DelCookies("userCert");
            string cacheName = string.Format("LoginUser_{0}", uid.ToString());
            if (((string)nwbase_utils.Cache.CacheHelper.GetCache(cacheName)) == utoken)
            {
                nwbase_utils.Cache.CacheHelper.DelCache(cacheName, true);
                Response.Cookies.Remove("userCert");

                return true;
            }
            return false;
        }

        /// <summary>
        /// 未授权时触发
        /// </summary>
        protected override void OnUnAuth()
        {
            DelCookies("user");
            DelCookies("userCert");
            Response.Redirect("/login.aspx?msg=注销成功，请重新登录！", true);
        }


        public void DelCookies(string name)
        {
            if (Request.Cookies.AllKeys.Contains(name))
            {


                HttpCookie cookies = Request.Cookies[name];
                cookies.Domain = ".niuwan.cc";
                if (cookies != null)
                {

                    cookies.Expires = DateTime.Today.AddDays(-1);
                    Response.Cookies.Add(cookies);
                    Request.Cookies.Remove(name);
                }
            }
        }
    }
}