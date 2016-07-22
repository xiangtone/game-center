<%@ Page Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="GameRecommendationPostList.aspx.cs" Inherits="AppStore.Web.GameRecommendationPostList" %>

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
            border-bottom: 1px solid #999;
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
    </style>

</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <script type="text/javascript">
        function openWin(obj) {
            //obj.target="_blank";
            window.location.href = "GameRecommendPosEdit.aspx?Action=Add&PosID=<%=PosId %>&order=<%=OrderNo %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>";
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
    <div class="nav">
         <%-- <%--<span class="nav_title">游戏中心管理</span> --%> --%> &raquo;<a href="GameRecommendationList.aspx?GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>">推荐管理</a> &raquo;推荐位管理
    </div>
    <div class="wrap">
        <input type="button" value="新增推荐" onclick="openWin(this)" />
    </div>
    <div id="content">
        <div class="group">
            <p class="title">正在启用</p>
            <asp:Repeater runat="server" ID="RunningList">
                <ItemTemplate>
                    <div class="item">
                        <div class="pic">
                            <img src="<%#Eval("RecommPicUrl") %>" /></div>
                        <div class="info">
                            <p><%#Eval("RecommTitle")%></p>
                            <p>状态：<%#BindStatus(this.GetDataItem()) %></p>
                            <p>推荐时间：<%#BindTime(this.GetDataItem()) %></p>
                            <p>
                                <asp:LinkButton ID="lbtnUp" runat="server" OnCommand="lbtnMoveIndex_Command" CommandName='<%#Eval("GroupElemID") %>' CommandArgument="up">上移</asp:LinkButton>&nbsp;<asp:LinkButton ID="lbtnDown" runat="server"  OnCommand="lbtnMoveIndex_Command"  CommandName='<%#Eval("GroupElemID") %>' CommandArgument="down">下移</asp:LinkButton>
                            </p>
                            <p><a href="GameRecommendPosEdit.aspx?Id=<%#Eval("GroupElemID") %>&Action=Edit&OrderNo=<%#Eval("OrderNo") %>&PosID=<%=PosId %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>">编辑</a>&nbsp;<asp:LinkButton runat="server" OnCommand="DelItem" CommandArgument=<%#Eval("GroupElemID") %> OnClientClick="return CheckDel(this);" Text="删除" /> </p>
                        </div>
                        <div class="clear"></div>
                    </div>
                </ItemTemplate>
            </asp:Repeater>
        </div>
        <div class="group">
            <p class="title">即将启用</p>
            <asp:Repeater runat="server" ID="ToRunList">
                <ItemTemplate>
                    <div class="item">
                        <div class="pic">
                            <img src="<%#Eval("RecommPicUrl") %>" /></div>
                        <div class="info">
                            <p><%#Eval("RecommTitle")%></p>
                            <p>状态：<%#BindStatus(this.GetDataItem()) %></p>
                            <p>推荐时间：<%#BindTime(this.GetDataItem()) %></p>
                            <p><a href="GameRecommendPosEdit.aspx?Id=<%#Eval("GroupElemID") %>&Action=Edit&OrderNo=<%#Eval("OrderNo") %>&PosID=<%=PosId %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>">编辑</a>&nbsp;<a href="#">删除</a></p>
                        </div>
                        <div class="clear"></div>
                    </div>
                </ItemTemplate>
            </asp:Repeater>
        </div>
        <div class="group">
            <p class="title">已过期</p>
            <asp:Repeater runat="server" ID="ExpiredList">
                <ItemTemplate>
                    <div class="item">
                        <div class="pic">
                            <img src="<%#Eval("RecommPicUrl") %>" /></div>
                        <div class="info">
                            <p><%#Eval("RecommTitle")%></p>
                            <p>状态：<%#BindStatus(this.GetDataItem()) %></p>
                            <p>推荐时间：<%#BindTime(this.GetDataItem()) %></p>
                            <p><a href="GameRecommendPosEdit.aspx?Id=<%#Eval("GroupElemID") %>&Action=Edit&OrderNo=<%#Eval("OrderNo") %>&PosID=<%=PosId %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>">编辑</a>&nbsp;<a href="#">删除</a></p>
                        </div>
                        <div class="clear"></div>
                    </div>
                </ItemTemplate>
            </asp:Repeater>
        </div>
    </div>
</asp:Content>
