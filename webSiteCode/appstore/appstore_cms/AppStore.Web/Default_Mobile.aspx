<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Default_Mobile.aspx.cs" Inherits="AppStore.Web.Default_Mobile" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1" runat="server">
    <title>应用商店后台</title>
        <link href="<%=ResolveUrl("~") + "css/frame.css"%>" rel="stylesheet" />

    <style type="text/css">
        frameset
        {
	        text-align:center;
        }
        #leftmenu
        {
            border-right: 1px solid #333;
        }
    </style>
</head>

       
  <frameset cols="200,*" border="0">
<%--   <frame src="top.aspx" frameborder="1" name="top" id="frame_top" />--%>
  <frame src="LeftMenu_Mobile.aspx" frameborder="1" name="left" noresize="noresize" scrolling="no" id="leftmenu" />
  <frame src="GameInfoList.aspx" frameborder="0" name="main" noresize="noresize" />
</frameset>
      
</html>