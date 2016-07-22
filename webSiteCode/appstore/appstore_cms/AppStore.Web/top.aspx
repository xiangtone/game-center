<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="top.aspx.cs" Inherits="AppStore.Web.top" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=ResolveUrl("~") + "css/frame.css"%>" rel="stylesheet" />

</head>
<body>
    <form id="form3" runat="server">
        <div id="frame_top" style="height: 70px;">
           <div id="frame_logo" <%--style="background:url(images/huashuo.jpg) no-repeat;"--%>>  <img src="Images/huashuo.jpg" style="height: 66px; width: 313px" /></div>
<%--            <img src="Images/huashuo.jpg" />--%>
            <div id="frame_appname"></div>
            <div id="frame_useroprs">
                <span id="frame_username" title="管理员"><%=GetUserName() %></span><span id="frame_welcome">，欢迎您  </span>
                <span id="frame_usertools">
                    <a id="frame_usertools_home" href="http://gcstatsys.cms.niuwan.cc/Index.aspx" target="main">首页</a> |
                <a id="frame_usertools_conf" href="http://cms.niuwan.cc/settings.aspx"  target="main">个人设定</a> |
<%--                <a id="frame_usertools_help" href="http://cms.niuwan.cc/help.aspx"  target="main">帮助</a> |--%>
                <a id="frame_usertools_logout" href="<%=ResolveUrl("~")%>logout.aspx"  target="_parent">注销</a>
                </span>
            </div>
        </div>
    </form>
</body>
</html>
