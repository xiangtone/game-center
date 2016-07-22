<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="F_GameInfoList.aspx.cs" Inherits="AppStore.Web.FindBack.F_GameInfoList" %>


<%@ Register Assembly="AspNetPager" Namespace="Wuqi.Webdiyer" TagPrefix="webdiyer" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
        .packcount {
            display: block;
            height: 20px;
            width: 20px;
            background-color: #ce0000;
            border: 2px solid #FFF;
            border-radius: 20px;
            text-align: center;
            position: relative;
            top: 15px;
            right: -45px;
            color: #FFF;
            font-weight: bold;
            line-height: 20px;
        }
    </style>
    <script type="text/javascript">

        ShareData = function (data) {
            DlgIfrmCloseInSubwin("GetShareData", data);
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div id="background" class="background" style="display: none;">
    </div>
    <div id="progressBar" class="progressBar" style="display: none;">
        数据处理中，请稍等...
    </div>
    <div class="wrap">
        搜索方式：
    <asp:DropDownList ID="SearchType" runat="server">
        <asp:ListItem Value="0">游戏名称</asp:ListItem>
        <asp:ListItem Value="1">开发者名称</asp:ListItem>
        <asp:ListItem Value="2">游戏ID</asp:ListItem>
    </asp:DropDownList>


        搜索内容：<input runat="server" id="Keyword_2" type="text" />

        <asp:DropDownList ID="IsNetGame" runat="server" AutoPostBack="true">
            <asp:ListItem>全部游戏</asp:ListItem>
            <asp:ListItem Value="2101">网游</asp:ListItem>
            <asp:ListItem Value="2102">单机</asp:ListItem>
        </asp:DropDownList>
        <asp:DropDownList ID="AppType" runat="server" AutoPostBack="true" Visible="false"></asp:DropDownList>

        <asp:Button ID="btnSearch" runat="server" Text="搜索" CssClass="btn" OnClick="btnSearch_Click" />｜排序方式：
        <asp:DropDownList ID="OrderType" runat="server" AutoPostBack="true">
            <asp:ListItem Value="CreateTime">添加时间</asp:ListItem>
            <asp:ListItem Value="UpdateTime">更新时间</asp:ListItem>
            <asp:ListItem Value="DownTimes">下载次数</asp:ListItem>
        </asp:DropDownList>
    </div>
    <asp:Repeater ID="objRepeater" runat="server">
        <HeaderTemplate>
            <table style="width: 100%" class="grid">
                <thead>
                    <tr>
                        <th>应用ID</th>
                        <th style="width: 70px;">图标及名称</th>
                        <th>应用状态</th>
                        <th>开发者</th>
                        <th>应用类型</th>
                        <th>是否为网游</th>
                        <th>建档日期</th>
                        <th>更新日期</th>
                        <th>应用操作</th>

                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                <td><%#Eval("AppID") %>
                </td>

                <td style="width: 60px;">
                    <span class="packcount"><%#Eval("PackCount") %></span>
                    <img src='<%#Eval("MainIconUrl") %>' width="60" height="60" />
                    <br style="clear: both;" />
                    <span style="width: 60px; overflow: hidden"><%#Eval("ShowName") %></span>
                </td>
                <td><%#Convert.ToInt32(Eval("Status")) == 1 ? "启用" : "<span style='color:red;'>禁用</span>" %></td>
                <td><%# this.dic_DevList[Convert.ToInt32(Eval("CPID"))]%></td>
                <td><%#Eval("AppTypeName") %></td>
                <td><%#Convert.ToInt32( Eval("IsNetGame"))==1?"网游":"单机" %></td>
                <td><%#((DateTime) Eval("CreateTime")).ToString("yyyy-MM-dd") %></td>
                <td><%#((DateTime) Eval("UpdateTime")).ToString("yyyy-MM-dd") %></td>
                <td>
                    <a class="btnSelect" href="javascript:ShareData({AppID:'<%#Eval("AppID") %>',RecommTag:'<%#Eval("RecommTag")%>', ShowName:'<%#Eval("ShowName")%>', MainIconPicUrl:'<%#Eval("MainIconUrl") %>',TypeID:'<%#Eval("AppType") %>' ,TypeName :'<%#Eval("AppTypeName") %>',RecommTag:'<%#Eval("RecommTag") %>' })"
                        title="查找带回" style="margin-top: 20px;"><%#Convert.ToInt32(Eval("Status")) == 1 ? "选择" : "" %></a>
                </td>
            </tr>

        </ItemTemplate>
        <FooterTemplate>
            </table>
        </FooterTemplate>

    </asp:Repeater>
    <div class="wrap">
        <webdiyer:AspNetPager runat="server" ID="pagerList" OnPageChanged="pagerList_PageChanged" CssClass="pages" CurrentPageButtonClass="cpb"
            PagingButtonSpacing="0px" NumericButtonCount="10" PageSize="20" AlwaysShow="true">
        </webdiyer:AspNetPager>
    </div>
</asp:Content>
