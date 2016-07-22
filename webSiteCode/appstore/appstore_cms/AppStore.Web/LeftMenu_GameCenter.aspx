<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="LeftMenu_GameCenter.aspx.cs" Inherits="AppStore.Web.LeftMenu_GameCenter" %>


<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <link href="<%=ResolveUrl("~") + "css/frame.css"%>" rel="stylesheet" />
    <script src="http://cms.niuwan.cc/js/jquery-1.9.1.js" type="text/javascript"></script>

    <style type="text/css">
        body {
            background-color: #F1F1F1;
        }

        div, ul, li, p {
            margin: 0;
            padding: 0;
            border: 0;
        }

            div.leftmenu {
                margin: 0px 0px 1px 0px;
            }

        .leftmenu p {
            font-weight: bold;
            line-height: 2em;
            font-size: 15px;
            cursor: pointer;
            background-color: #e1e1e1;
            color: #555;
            font-size: 10pt;
            font-weight: bold;
            height: 30px;
            line-height: 30px;
            margin-bottom: 1px;
            padding-left: 15px;
            padding-top: 3px;
        }

        .leftmenu ul {
            list-style: none;
            /*width: 80%;*/
            height: auto;
        }

            .leftmenu ul li {
                float: left;
                width: 100%;
                line-height: 25px;
            }

        .leftmenu li a {
            text-decoration: none;
            line-height: 1.5em;
            display: block;
            padding-left: 5px;
            background-color: transparent;
            /*color: #555;*/
            font-size: 10pt;
            font-weight: normal;
            height: 25px;
            padding-left: 30px;
        }

            .leftmenu li a:hover {
                background-color: #7cbae6;
                color: #ffffff;
            }

        .click {
            background-color: #7cbae6;
            color: #ffffff;
        }

            .click a {
                color: #ffffff;
            }

        .dis {
            display: none;
        }
    </style>
    <script type="text/javascript">
        $(function () {

            $(".leftmenu_p").click(function () {
                if ($(this).parent().find(".leftmenu_ul").attr("data-dis") == "dis") {
                    $(this).parent().find(".leftmenu_ul").show();
                    $(this).parent().find(".leftmenu_ul").attr("data-dis", "none");
                }
                else {
                    $(this).parent().find(".leftmenu_ul").hide();
                    $(this).parent().find(".leftmenu_ul").attr("data-dis", "dis");
                }
            });
            $(".leftmenu_ul li").click(function () {
                $(".leftmenu_ul li").attr("class", "");
                $(this).attr("class", "click");

            });


            <%--               <% if (Action == "2")
                  {%>
            alert(123);
            $(".leftmenu_ul li").attr("class", "");
            $("#action3").attr("class", "click");
            <%}%>--%>
        });
    </script>
</head>
<body>
    <form id="form2" runat="server">

        <div class="leftmenu">
            <p class="leftmenu_p">数据统计</p>
            <ul class="leftmenu_ul" data-dis="none">
                <li><a href="http://gcstatsys.cms.niuwan.cc/Index.aspx" target="main">数据统计</a></li>
                <li><a href="http://gcstatsys.cms.niuwan.cc/BaseData.aspx" target="main">基础数据</a></li>
                <li><a href="http://gcstatsys.cms.niuwan.cc/DownLoad.aspx" target="main">下载相关</a></li>
                <li><a href="http://gcstatsys.cms.niuwan.cc/PageVisit.aspx" target="main">访问页面</a></li>
                <li><a href="http://gcstatsys.cms.niuwan.cc/VersionsData.aspx" target="main">版本情况</a></li>
                <li><a href="http://gcstatsys.cms.niuwan.cc/AccRecharge.aspx" target="main">账户充值</a></li>
                <li><a href="http://gcstatsys.cms.niuwan.cc/Consumer.aspx" target="main">游戏消费</a></li>
                <li><a href="http://gcstatsys.cms.niuwan.cc/AccRegister.aspx" target="main">账户注册</a></li>
                <li><a href="http://gcstatsys.cms.niuwan.cc/UserLocation.aspx" target="main">用户分析</a></li>
            </ul>
        </div>
        <div style="clear: both;"></div>

        <div class="leftmenu">

            <p class="leftmenu_p">华硕游戏中心</p>
            <ul class="leftmenu_ul" data-dis="none">
                <li><a href="<%=ResolveUrl("~")%>AppInfoListNew.aspx?Action=1" target="main">已上架游戏</a></li>
                <li><a href="<%=ResolveUrl("~")%>AppInfoListNew.aspx?Action=4" target="main">已下架游戏</a></li>
                <li><a href="<%=ResolveUrl("~")%>AppInfoListNew.aspx?Action=2" target="main">待审核游戏</a></li>
                <li><a href="<%=ResolveUrl("~")%>AppInfoListNew.aspx?Action=5" target="main">审核不通过游戏</a></li>
                <li><a href="<%=ResolveUrl("~")%>HomePageRecommendByAppCenterList.aspx?acttype=4102,104&page=new" target="main">首页游戏配置</a></li>
                <li><a href="<%=ResolveUrl("~")%>SpecialTopicList.aspx?acttype=3200,104&page=new" target="main">游戏专题配置</a></li>
                <li><a href="<%=ResolveUrl("~")%>GroupInfo.aspx?SchemeID=104&page=new" target="main">游戏分类管理</a></li>
                
                
                <%--<li><a href="<%=ResolveUrl("~")%>AppInfoListNew.aspx?Action=3" target="main">游戏接入情况</a></li>--%>
                <li><a href="<%=ResolveUrl("~")%>GroupElement.aspx?GroupID=93&GroupName=游戏排行管理&orderNo=0&SchemeID=104&page=new" target="main">游戏排行管理</a></li>
                <li><a href="<%=ResolveUrl("~")%>GroupElement.aspx?GroupID=94&GroupName=最新游戏管理&orderNo=2&SchemeID=104&page=new" target="main">最新游戏管理</a></li>
                <li><a href="<%=ResolveUrl("~")%>LauncherRecommendList.aspx?acttype=4108,104&page=new" target="main">热门游戏管理</a></li>
                <li><a href="<%=ResolveUrl("~")%>SyncManager.aspx?page=new" target="main">缓存同步</a></li>

            </ul>
        </div>
        <div style="clear: both;"></div>
    </form>
</body>
</html>
