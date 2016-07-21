<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="LeftMenu_Game.aspx.cs" Inherits="AppStore.Web.LeftMenu_Game" %>

<!DOCTYPE html>

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
    <form id="form2" runat="server">

        <div>
            <a href="Default_Mobile.aspx" target="_parent">应用商店-手机</a>
        </div>
        <div>
            <a href="Default.aspx" target="_parent">应用商店-平板</a>
        </div>
        <div class="leftmenu">
            <p>应用商店-游戏中心</p>
            <ul>


                <li><a href="GameInfoList.aspx?MaxAppID=100000" target="main">合作的游戏配置</a></li>
                <%--<%--<li><a href="AppInfoList.aspx" target="main">应用配置</a></li>--%>
                <%--<li><a href="GameRecommendationList.aspx?GroupTypeID=5102&SchemeID=103" target="main">桌面游戏推荐</a></li>--%>
                <li><a href="GameInfoList.aspx" target="main">游戏配置</a></li>
                <%--<li><a href="HomePageRecommendByAppCenterList.aspx?acttype=4106,104" target="main">首页推荐-游戏中心 </a></li>--%>
                <li><a href="HomePageRecommendByAppCenterList.aspx?acttype=4102,104" target="main">游戏推荐-游戏中心</a></li>
                <%--<li><a href="LauncherRecommendList.aspx?acttype=4107,104" target="main">应用游戏精品推荐-游戏中心</a></li>--%>
                <li><a href="LauncherRecommendList.aspx?acttype=4108,104" target="main">游戏精品推荐-游戏中心</a></li>
                <li><a href="BeginnerRecommendList.aspx?acttype=4101,104" target="main">新手推荐-游戏中心</a></li>
                <%--<li><a href="LinkInfoList.aspx" target="main">跳转链接配置</a></li>--%>
                <%--<li><a href="SpecialTopicList.aspx?acttype=3100,104" target="main">专题管理-游戏中心</a></li>--%>
                <li><a href="SpecialTopicList.aspx?acttype=3200,104" target="main">游戏专题管理-游戏中心</a></li>
                <li><a href="PopularList.aspx?acttype=104" target="main">搜索/热门配置-游戏中心</a></li>
                <li><a href="GroupInfo.aspx" target="main">分组管理</a></li>
                <li><a href="GroupSchemesList.aspx?acttype=104" target="main">分组方案管理--游戏中心</a></li>
                <li><a href="SyncManager.aspx" target="main">同步</a></li>
                <li><a href="ChannelList.aspx" target="main">渠道管理</a></li>
                <li><a href="CpsList.aspx" target="main">Cps管理</a></li>
                <li><a href="AutoCollectList.aspx" target="main">自动获取信息</a></li>

                <li><a href="AppInfoListNew.aspx?Action=1" target="main">已上架游戏</a></li>

                <li><a href="AppInfoListNew.aspx?Action=2" target="main">待审核游戏</a></li>

                <li><a href="AppInfoListNew.aspx?Action=3" target="main">游戏接入情况</a></li>

                <li><a href="AppInformList.aspx" target="main">举报管理</a></li>

                <li><a href="FeedBack.aspx?ClientId=12" target="main">用户反馈管理</a></li>



            </ul>
        </div>
    </form>
</body>
</html>
