<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="IosAppInfoList.aspx.cs" Inherits="AppStore.Web.IosAppInfoList" %>

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

        .icon {
            border-radius: 10px;
        }
    </style>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div id="background" class="background" style="display: none;">
    </div>
    <div id="progressBar" class="progressBar" style="display: none;">
        数据处理中，请稍等...
    </div>
    <div class="nav">
        <span class="nav_title">应用商店管理</span>
        <span class="nav_desc">首页 &raquo; <a href="IosAppInfoList.aspx">应用管理</a></span>
    </div>
    <div class="wrap">
        搜索方式：
            <asp:DropDownList ID="SearchType" runat="server" OnSelectedIndexChanged="SearchType_SelectedIndexChanged">
                <asp:ListItem Value="0">应用名称</asp:ListItem>
                <asp:ListItem Value="1">开发者名称</asp:ListItem>
                <asp:ListItem Value="2">应用ID</asp:ListItem>
            </asp:DropDownList>


        搜索内容：<input runat="server" id="SearchKeys" type="text" />

        <asp:Button ID="btnSearch" runat="server" Text="搜索" CssClass="btn" OnClick="btnSearch_Click" />｜排序方式：
               <asp:DropDownList ID="OrderType" runat="server" AutoPostBack="true">
                   <asp:ListItem Value="CreateTime">添加时间</asp:ListItem>
                   <asp:ListItem Value="UpdateTime">更新时间</asp:ListItem>
               </asp:DropDownList>
        ｜
            <input
                runat="server" id="btnAdd" type="button" value="新增" onclick="return window.location.href = 'IosAppInfoAdd.aspx?AppClass=23';" />


    </div>

    <asp:Repeater ID="objRepeater" runat="server" OnItemCommand="objRepeater_ItemCommand">
        <HeaderTemplate>
            <table style="width: 100%" class="grid">
                <thead>
                    <tr>
                        <th>图标</th>
                        <th style="width: 70px;">应用名称</th>
                        <th>应用状态</th>
                        <th>开发者</th>
                        <th>应用操作</th>
                        <th>建档日期</th>
                        <th>更新日期</th>
                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                 <td style="width: 70px;">
                    <img src='<%#Eval("IconPicUrl") %>' width="60" height="60" class="icon" /><br />
                    <%#Eval("AppID") %>
                </td>
                <td>
                    <span style="width: 60px; overflow: hidden"><%#Eval("ShowName") %></span>
                </td>
                <td><%#Convert.ToInt32( Eval("Status")) == 1 ? "启用" : "禁用" %></td>
                <td><%# Eval("DevName")%></td>

                <td>
                    <a href="IosAppInfoEdit.aspx?AppID=<%#Eval("AppID") %>&ShowName=<%#Eval("ShowName") %>&type=app&AppClass=23" style="line-height: 60px;">修改</a>

                    <a class="del_appInfo" href="IosAppInfoList.aspx?AppID=<%#Eval("AppID") %>" style="cursor: pointer" onclick="javascript:void(0)">删除</a>
                </td>
                <td><%#((DateTime) Eval("CreateTime")).ToString("yyyy-MM-dd") %></td>
                <td><%#((DateTime) Eval("UpdateTime")).ToString("yyyy-MM-dd") %></td>
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
