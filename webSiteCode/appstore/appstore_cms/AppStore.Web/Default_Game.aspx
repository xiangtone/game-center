<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Default_Game.aspx.cs" Inherits="AppStore.Web.Default_Game" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1" runat="server">
    <title>游戏中心后台</title>
    <link rel="icon" href="images/icon-tubiao.png" >
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
  <frameset cols="10%,*" cols="840px" border="0">
     
        <frame src="LeftMenu_Game.aspx" frameborder="1" name="left" noresize="noresize" scrolling="no" id="leftmenu" />
         <frame src="GameInfoList.aspx" frameborder="0" name="main" noresize="noresize" />
    </frameset>


<%--<frameset  cols="100%,10%" border="0">
      <frameset cols="100%,10%">
               <frame src="top.aspx" frameborder="2" name="top" id="frame_top" />
      </frameset>
      <frameset  cols="200,*">
              <frame src="LeftMenu_Game.aspx" frameborder="1" name="left" noresize="noresize" scrolling="no" id="leftmenu" />
              <frame src="GameInfoList.aspx" frameborder="0" name="main" noresize="noresize" />
      </frameset>
</frameset>--%>
</html>


<%--   <frame src="top.aspx" frameborder="2" name="top" id="frame_top" />--%>
