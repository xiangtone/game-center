using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.UI;

namespace AppStore.Common
{
    /// <summary>
    /// 页面类
    /// </summary>
    public static class PageExtension
    {
        /// <summary>
        /// Javascript中的Alert方法
        /// </summary>
        /// <param name="objPage"></param>
        /// <param name="message"></param>
        public static void Alert(this Page objPage, string message)
        {
            string key = "AlertMessage";
            string script = string.Format("alert('{0}')", message);
            objPage.ClientScript.RegisterStartupScript(typeof(Page), key, script, true);
        }

        /// <summary>
        /// Javascript中的Alert方法
        /// </summary>
        /// <param name="objPage"></param>
        /// <param name="message"></param>
        /// <param name="url"></param>
        public static void Alert(this Page objPage, string message, string url)
        {
            string key = "AlertMessage";
            string script = String.Format("alert('{0}');window.location='{1}';", message, url);
            objPage.ClientScript.RegisterStartupScript(typeof(Page), key, script, true);
        }

        /// <summary>
        /// 获取请求参数
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="objPage"></param>
        /// <param name="key"></param>
        /// <param name="defaultValue"></param>
        /// <param name="needUrlEncode"></param>
        /// <returns></returns>
        public static T Request<T>(this Page objPage, string key, T defaultValue = default(T), bool needUrlEncode = false)
        {
            T result = (T)defaultValue;

            if (needUrlEncode)
            {
                return objPage.Request.Params[key].UrlEncode().Convert<T>(result);
            }

            return objPage.Request.Params[key].Convert<T>(defaultValue);
        }

        /// <summary>
        /// 获取请求参数
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="objHandler"></param>
        /// <param name="key"></param>
        /// <param name="defaultValue"></param>
        /// <param name="needUrlEncode"></param>
        /// <returns></returns>
        public static T Request<T>(this IHttpHandler objHandler, string key, object defaultValue, bool needUrlEncode = false)
        {
            T result = (T)defaultValue;

            if (needUrlEncode)
            {
                return HttpContext.Current.Request.Params[key].UrlEncode().Convert<T>(result);

            }

            return HttpContext.Current.Request.Params[key].Convert<T>(result);
        }
    }
}
