<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="AppStore.Web.Default" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1" runat="server">
    <title>应用商店后台</title>
    <link href="<%=ResolveUrl("~") + "css/frame.css"%>" rel="stylesheet" />
    <style type="text/css">
        frameset {
            text-align: center;
        }

        #leftmenu {
            border-right: 1px solid #333;
        }
    </style>
</head>

<%--<frameset rows="*,90%" cols="840px" border="0" >
            <frame src="top.aspx"  frameborder="0" name="top" id="frame_top" scrolling="no" marginwidth="0" marginheight="0"  noresize="noresize"/>
            <frame src="LeftMenu.aspx" frameborder="1" name="left" scrolling="no" marginwidth="0" marginheight="0"  noresize="noresize" id="leftmenu"/>
            <frame src="GameInfoList.aspx" frameborder="0" name="main" scrolling="no" marginwidth="0" marginheight="0"  noresize="noresize" />
   </frameset>--%>
<%--            <frame src="top.aspx"  frameborder="0" name="top" id="frame_top" scrolling="no" marginwidth="0" marginheight="0"  noresize="noresize"/>--%>
    
    <frameset cols="10%,*" cols="840px" border="0">
     
        <frame src="LeftMenu.aspx" frameborder="1" name="left" noresize="noresize" scrolling="no" id="leftmenu" />
         <frame src="GameInfoList.aspx" frameborder="0" name="main" noresize="noresize" />
    </frameset>
</html>
