using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// AdminBasePage 的摘要说明
/// </summary>
/// 
namespace AppStore.Web
{
    public class AdminBasePage : nwbase_auth.AuthPageBase
    {
        //protected override void OnError(EventArgs e)
        //{
        //    //base.OnError(e);
        //    var ex = Server.GetLastError();

        //    nwbase_utils.TextLog.Error("error", "PageError", ex);

        //    Server.ClearError();

        //    Response.Write("出现异常了！");

        //    Response.End();
        //}

        protected void Page_Error(object s, EventArgs e)
        {
            Exception ex = Server.GetLastError();
            nwbase_utils.TextLog.Error("error", "page_error", ex);

            new ActionLogBll().Add(new ActionLogModel()
            {
                UserId = IsLogin ? AuthUser.UserId : 0,
                Action = "【页面异常】",
                UserIP = Request.UserHostAddress,
                Content = this.Page.AppRelativeVirtualPath,
                Level = 3
            });

            Server.ClearError();
            Response.Write("发生错误了！");
            Response.End();
        }

        protected override void OnLoad(EventArgs e)
        {
            base.OnLoad(e);
            Response.AddHeader("Access-Control-Allow-Origin", ".cms.niuwan.cc");
        }

    }
}