<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Default_GameCenter.aspx.cs" Inherits="AppStore.Web.Default_GameCenter" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1" runat="server">
    <title>游戏中心后台</title>
    <link rel="icon" href="images/icon-tubiao.png">
    <link href="<%=ResolveUrl("~") + "css/frame.css"%>" rel="stylesheet" />
    <script src="http://cms.niuwan.cc/js/jquery-1.9.1.js" type="text/javascript"></script>

    <style type="text/css">
        frameset {
            text-align: center;
        }

        #leftmenu {
            border-right: 1px solid #cccccc;
        }
    </style>

   <%-- <script type="text/javascript">
        //document.domain = 'cms.niuwan.cc';
        // ====================================================
        // 用户登录状态检查与处理
        // ====================================================
        var userLoginTimer = null;
        var userLoginShowed = false;

        $(window).ready(function () {
            UserLoginCheck();
        });

        function UserLoginCheck() {
            userLoginTimer = setTimeout("UserLoginCheckEx()", 30000);
        }

        function UserLoginCheckEx() {
            if (userLoginShowed == false)
                $.get("logincheck.aspx", null, function (data) { if (data == "false") UserLogin(); else UserLoginCheck(); });
        }

        function UserLogin() {
            userLoginShowed = true;
            $("#ifrmLogin").attr("src", "/login.aspx?msg=您的登录已过期，请重新登录。");
            
       ("ifrmLogin", UserLoginClosed, "");
            //window.location.href = "login.aspx?msg=您的登录已过期，请重新登录。";
        }

        function UserLoginClosed() {
            userLoginShowed = false;
            UserLoginCheck();
        }

    </script>--%>
</head>
<frameset rows="70,*" cols="*" bordercolor='#006699' border="0" frameborder="1" bordercolor="#cccccc">
            <FRAME id=topFrame name=topFrame src="top.aspx" noResize scrolling=no border="0"  />

           <FRAMESET rows=* cols=200,*>
                <FRAME name=left  src="LeftMenu_GameCenter.aspx" id="leftmenu" target="main"  />
              <FRAME name=main src="http://gcstatsys.cms.niuwan.cc/Index.aspx" border="0" />
         </FRAMESET>
     
</frameset>

<%--<iframe id="ifrmLogin" title="重新登录" frameborder="0" scrolling="yes" style="display: none; width: 380px; height: 250px;" src="about:blank"></iframe>--%>

</html>
