<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="LeftMenu_Mobile.aspx.cs" Inherits="AppStore.Web.LeftMenu_Mobile" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <link href="<%=ResolveUrl("~") + "css/frame.css"%>" rel="stylesheet" />

    <style type="text/css">
        div, ul, li, p {
            margin: 0;
            padding: 0;
            border: 0;
        }

            div.leftmenu {
                margin: 20px 0px 0 0px;
            }

        .leftmenu p {
            font-weight: bold;
            line-height: 2em;
        }

        .leftmenu ul {
            list-style: none;
            /*width: 80%;*/
        }

            .leftmenu ul li {
                float: left;
                width: 100%;
            }

        .leftmenu li a {
            text-decoration: none;
            line-height: 1.5em;
            display: block;
            padding-left: 5px;
        }

            .leftmenu li a:hover {
                background-color: #BBB;
            }
    </style>
</head>
<body>
    <form id="form1" runat="server">
        <div>
            <a href="Default.aspx" target="_parent">应用商店-平板</a>
        </div>
        <div>
            <a href="Default_Game.aspx" target="_parent">应用商店-游戏中心</a>
        </div>
        <div class="leftmenu">
            <p>应用商店-手机</p>
            <ul>
                <li><a href="AppInfoList.aspx" target="main">应用配置</a></li>
                <li><a href="GameInfoList.aspx" target="main">游戏配置</a></li>
                <li><a href="HomePageRecommendByAppCenterList.aspx?acttype=4106,102" target="main">首页推荐-手机版 </a></li>
                <li><a href="HomePageRecommendByAppCenterList.aspx?acttype=4102,102" target="main">游戏推荐-手机版</a></li>
                <li><a href="LauncherRecommendList.aspx?acttype=4107,102" target="main">应用游戏精品推荐-手机版</a></li>
                <li><a href="LauncherRecommendList.aspx?acttype=4108,102" target="main">游戏精品推荐-手机版</a></li>
                <li><a href="BeginnerRecommendList.aspx?acttype=4101,102" target="main">新手推荐-手机版</a></li>
                <li><a href="LinkInfoList.aspx" target="main">跳转链接配置</a></li>
                <li><a href="SpecialTopicList.aspx?acttype=3100,102" target="main">应用专题管理-手机版</a></li>
                <li><a href="SpecialTopicList.aspx?acttype=3200,102" target="main">游戏专题管理-手机版</a></li>
                <li><a href="PopularList.aspx?acttype=102" target="main">搜索/热门配置-应用</a></li>
                <li><a href="GroupInfo.aspx" target="main">分组管理</a></li>
                <li><a href="GroupSchemesList.aspx?acttype=102" target="main">分组方案管理--应用</a></li>
                <li><a href="SyncManager.aspx" target="main">同步</a></li>
                <li><a href="ChannelList.aspx" target="main">渠道管理</a></li>
                <li><a href="CpsList.aspx" target="main">Cps管理</a></li>
                <li><a href="AutoDownload.aspx" target="main">批量下载apk</a></li>

            </ul>
        </div>
    </form>
</body>
</html>
