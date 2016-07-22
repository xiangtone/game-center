using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.UI;



namespace AppStore.Web
{
    public class JsUtils
    {
        /// <summary>
        /// 与JS配合弹出显示一个确认框
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="msg">要显示的消息</param>
        public static void DlgConfirm(Page page, string msg)
        {
            DlgConfirm(page, msg, "", "");
        }

        /// <summary>
        /// 与JS配合弹出显示一个确认框
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="msg">要显示的消息</param>
        /// <param name="callbackFunc">用户确认后的回调函数</param>
        /// <param name="args">用户确认后要传弟给回调函数的参数</param>
        public static void DlgConfirm(Page page, string msg, string callbackFunc, string args)
        {
            if (callbackFunc.Length < 1)
                callbackFunc = "false";
            Exec(page, string.Format("DlgConfirm(\"{0}\",{1},\"{2}\");", msg, callbackFunc, args));
        }

        /// <summary>
        /// 与JS配合弹出显示一个警告框
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="msg">要显示的消息</param>
        public static void DlgAlert(Page page, string msg)
        {
            DlgAlert(page, msg, "", "");
        }

        /// <summary>
        /// 与JS配合弹出显示一个警告框
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="msg">要显示的消息</param>
        /// <param name="callbackFunc">关闭IFRAME框时需要执行的JS方法名</param>
        /// <param name="args">用户确认后要传弟给回调函数的参数</param>
        public static void DlgAlert(Page page, string msg, string callbackFunc, string args)
        {
            if (callbackFunc.Length < 1)
                callbackFunc = "false";
            Exec(page, string.Format("DlgAlert(\"{0}\", {1}, \"{2}\");", msg, callbackFunc, args));
        }

        /// <summary>
        /// 与JS配合弹出显示一个消息框
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="msg">要显示的消息</param>
        public static void DlgInfo(Page page, string msg)
        {
            DlgInfo(page, msg, "", "");
        }

        /// <summary>
        /// 与JS配合弹出显示一个消息框
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="msg">要显示的消息</param>
        /// <param name="callbackFunc">关闭IFRAME框时需要执行的JS方法名</param>
        /// <param name="args">用户确认后要传弟给回调函数的参数</param>
        public static void DlgInfo(Page page, string msg, string callbackFunc, string args)
        {
            if (callbackFunc.Length < 1)
                callbackFunc = "false";
            Exec(page, string.Format("DlgInfo(\"{0}\", {1}, \"{2}\");", msg, callbackFunc, args));
        }

        /// <summary>
        /// 与JS配合弹出显示一个IFRAME框
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="srcId">Iframe的ID</param>
        public static void DlgIfrm(Page page, string srcId)
        {
            DlgIfrm(page, srcId, "", "");
        }

        /// <summary>
        /// 与JS配合弹出显示一个IFRAME框
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="srcId">Iframe的ID</param>
        /// <param name="callbackFunc">关闭IFRAME框时需要执行的JS方法名</param>
        /// <param name="args">用户确认后要传弟给回调函数的参数</param>
        public static void DlgIfrm(Page page, string srcId, string callbackFunc, string args)
        {
            if (callbackFunc.Length < 1)
                callbackFunc = "false";
            Exec(page, string.Format("DlgIfrm(\"{0}\", {1}, \"{2}\");", srcId, callbackFunc, args));
        }

        /// <summary>
        /// 与JS配合弹出显示一个IFRAME框
        /// </summary>
        /// <param name="page">当前页面</param>
        public static void DlgIfrmCloseInSubwin(Page page)
        {
            DlgIfrmCloseInSubwin(page, "", "");
        }

        /// <summary>
        /// 与JS配合弹出显示一个IFRAME框
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="callbackFunc">关闭IFRAME框时需要执行的JS方法名</param>
        /// <param name="args">执行JS方法时需要传入的参数</param>
        public static void DlgIfrmCloseInSubwin(Page page, string callbackFuncName, string args)
        {
            Exec(page, string.Format("DlgIfrmCloseInSubwin(\"{0}\", \"{1}\");", callbackFuncName, args));
        }

        /// <summary>
        /// 与JS配合弹出显示一个弹出框
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="srcId">要在弹出框中显示的内容的对象ID</param>
        public static void DlgBox(Page page, string srcId)
        {
            Exec(page, string.Format("DlgBox(\"{0}\");", srcId));
        }

        /// <summary>
        /// 与页面布局与JS配置，显示一个提示的Label，并指定其样式与提示内容（暂停使用）
        /// </summary>
        /// <param name="page"></param>
        /// <param name="labelId"></param>
        /// <param name="msg"></param>
        /// <param name="isGoodMsg"></param>
        public static void Prompt(Page page, string labelId, string msg, bool isGoodMsg)
        {
            string cn = isGoodMsg ? "succlbl" : "errlbl";
            Exec(page, string.Format("var e = $(\"#{0}\");e.removeClass(\"{1}\");e.addClass(\"{1}\");e.html(\"{2}\");", labelId, cn, msg));
        }

        /// <summary>
        /// 与JS配合使页面重定向至新的URL地址
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="url">URL地址</param>
        public static void Redirect(Page page, string url)
        {
            page.ClientScript.RegisterStartupScript(page.GetType(), "Redirect",
                "<script lanuage='javascript'>window.location.href='" + url + "';</script>");
        }

        /// <summary>
        /// 执行一段Javascript脚本
        /// </summary>
        /// <param name="page">当前页面</param>
        /// <param name="script">Javascript脚本</param>
        public static void Exec(Page page, string script)
        {
            page.ClientScript.RegisterClientScriptBlock(page.GetType(), "Exec",
                "<script lanuage='javascript'>" + script + "</script>");
        }
    }

}