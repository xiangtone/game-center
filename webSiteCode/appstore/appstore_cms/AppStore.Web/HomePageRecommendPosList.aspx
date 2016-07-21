<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="HomePageRecommendPosList.aspx.cs" Inherits="AppStore.Web.HomePageRecommendPosList" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
        body {
            padding: 10px;
        }

        #content {
            padding: 10px 10px 10px 30px;
        }

        .clear {
            clear: both;
        }

        div.group {
            margin-bottom: 20px;
            /*border-bottom: 1px solid #999;*/
        }

            div.group p.title {
                font-weight: bold;
                font-size: 16px;
                line-height: 1.5em;
                margin-bottom: 10px;
            }

        div.item {
            margin-bottom: 15px;
            vertical-align: bottom;
            height: 200px;
        }

            div.item div.pic {
                float: left;
            }

                div.item div.pic img {
                    height: 200px;
                }

            div.item div.info {
                float: left;
                margin-left: 20px;
            }

                div.item div.info p {
                    line-height: 2em;
                }

        /*input[type="submit"], input[type="button"]*/
        .newinput {
            background-color: #49aef5;
            color: #FFF;
            cursor: pointer;
            font-size: 12px;
            font-weight: inherit;
            height: 2em;
            line-height: 1.5em;
            padding: 2px 10px;
            vertical-align: middle;
        }

        .newa {
            color: #1e74c9;
            text-decoration: none;
        }

        .new {
            background-color: #f7f7f7;
            margin: 0;
            border: 1px solid #e5e5e5;
        }

        .newbody {
            background-color: #edeef0;
            padding: 20px;
        }

            .newbody a {
                text-decoration: none;
            }
    </style>
    <script type="text/javascript">
        $(function () {
            <%if (PageType == "new")
              {%>
            $("body").addClass("newbody");
            $("#content").addClass("new");
            $("input[type='button']").css("background-color", "#49aef5");
            $("input[type='submit']").css("background-color", "#49aef5");
            <%}%>

        });
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <script type="text/javascript">
        function openWin(obj) {
            //obj.target="_blank";
            window.location.href = "HomePageRecommendPosEdit.aspx?Action=Add&PosID=<%=PosId %>&order=<%=OrderNo %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>&page=<%= this.PageType%>";

            //obj.click();
        }
        function CheckDel(obj) {
            var items = $(obj).parentsUntil(".group").parent().find(".item");
            if (items.length == 1) {
                return confirm("仅剩次一项有效推荐，确定删除吗？");
            }
            else {
                return confirm("确定删除吗？");;
            }
        }
    </script>
    <%if (PageType == "new")
      {%>
    <span class="nav_desc" style="color: #1e74c9;"><a href="AppInfoListNew.aspx" style="color: #1e74c9; font-size: 13px;">首页</a>

        &raquo; <a href="HomePageRecommendByAppCenterList.aspx?acttype=4102,104&page=<%= this.PageType%>" style="color: #1e74c9;">首页推荐管理</a></span>

    <%}
      else
      {%>
    <div class="nav">
        <span class="nav_title">桌面游戏推荐管理</span> <span class="nav_desc">推荐管理 </span>
    </div>
    <%} %>
    <div style="clear: both;"></div>
    <br />
    <div class="wrap">
        <input type="button" value="新增推荐" onclick="openWin(this)" />
    </div>
    <div id="content">
        <div class="group">
            <p class="title" style="display: none">正在启用</p>
            <asp:Repeater runat="server" ID="RunningList">
                <ItemTemplate>
                    <div class="item">
                        <div class="pic">
                            <img src="<%#Eval("RecommPicUrl") %>" />
                        </div>
                        <div class="info">
                            <p><%#Eval("RecommTitle")%></p>
                            <p>状态：<%#BindStatus(this.GetDataItem()) %></p>
                            <p>推荐标签：<%#new AppStore.Web.HomePageRecommendByAppCenterList().BindTag(Eval("RecommTag")) %></p>
                            <p>
                                <asp:LinkButton ID="lbtnUp" runat="server" OnCommand="lbtnMoveIndex_Command" CommandName='<%#Eval("GroupElemID") %>' CommandArgument="up">上移</asp:LinkButton>&nbsp;<asp:LinkButton ID="lbtnDown" runat="server" OnCommand="lbtnMoveIndex_Command" CommandName='<%#Eval("GroupElemID") %>' CommandArgument="down">下移</asp:LinkButton>
                            </p>
                            <p>
                                <a href="HomePageRecommendPosEdit.aspx?Id=<%#Eval("GroupElemID") %>&Action=Edit&OrderNo=<%#Eval("OrderNo") %>&PosID=<%=PosId %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>&page=<%= this.PageType%>">编辑</a>
                                &nbsp;<asp:LinkButton runat="server" OnCommand="DelItem" CommandArgument='<%#Eval("GroupElemID") %>' OnClientClick="return CheckDel(this);" Text="删除" />
                            </p>
                        </div>
                        <div class="clear"></div>
                    </div>
                </ItemTemplate>
            </asp:Repeater>
        </div>
        <div class="group" style="display: none">
            <p class="title">即将启用</p>
            <asp:Repeater runat="server" ID="ToRunList">
                <ItemTemplate>
                    <div class="item">
                        <div class="pic">
                            <img src="<%#Eval("RecommPicUrl") %>" />
                        </div>
                        <div class="info">
                            <p><%#Eval("RecommTitle")%></p>
                            <p>状态：<%#BindStatus(this.GetDataItem()) %></p>
                            <p>推荐时间：<%#BindTime(this.GetDataItem()) %></p>
                            <p>推荐标签：<%#new AppStore.Web.HomePageRecommendByAppCenterList().BindTag(Eval("RecommTag")) %></p>
                            <p><a href="HomePageRecommendPosEdit.aspx?Id=<%#Eval("GroupElemID") %>&Action=Edit&OrderNo=<%#Eval("OrderNo") %>&PosID=<%=PosId %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>">编辑</a>&nbsp;<a href="#">删除</a></p>
                        </div>
                        <div class="clear"></div>
                    </div>
                </ItemTemplate>
            </asp:Repeater>
        </div>
        <div class="group" style="display: none">
            <p class="title">已过期</p>
            <asp:Repeater runat="server" ID="ExpiredList">
                <ItemTemplate>
                    <div class="item">
                        <div class="pic">
                            <img src="<%#Eval("RecommPicUrl") %>" />
                        </div>
                        <div class="info">
                            <p><%#Eval("RecommTitle")%></p>
                            <p>状态：<%#BindStatus(this.GetDataItem()) %></p>
                            <p>推荐时间：<%#BindTime(this.GetDataItem()) %></p>
                            <p>推荐标签：<%#new AppStore.Web.HomePageRecommendByAppCenterList().BindTag(Eval("RecommTag")) %></p>
                            <p><a href="HomePageRecommendPosEdit.aspx?Id=<%#Eval("GroupElemID") %>&Action=Edit&OrderNo=<%#Eval("OrderNo") %>&PosID=<%=PosId %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>">编辑</a>&nbsp;<a href="#">删除</a></p>
                        </div>
                        <div class="clear"></div>
                    </div>
                </ItemTemplate>
            </asp:Repeater>
        </div>
    </div>
</asp:Content>
