<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="AppInfoList.aspx.cs" Inherits="AppStore.Web.AppInfoList" %>

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
        <span class="nav_desc">首页 &raquo; <a href="AppInfoList.aspx">应用管理</a></span>
    </div>
    <div class="wrap">
        搜索方式：
            <asp:DropDownList ID="SearchType" runat="server">
                <asp:ListItem Value="0">应用名称</asp:ListItem>
                <asp:ListItem Value="1">开发者名称</asp:ListItem>
                <asp:ListItem Value="2">应用ID</asp:ListItem>
            </asp:DropDownList>


        搜索内容：<input runat="server" id="Keyword_2" type="text" />

        <asp:DropDownList ID="AppType" runat="server" AutoPostBack="true"></asp:DropDownList>

        <asp:Button ID="btnSearch" runat="server" Text="搜索" CssClass="btn" OnClick="btnSearch_Click" />｜排序方式：
               <asp:DropDownList ID="OrderType" runat="server" AutoPostBack="true">
                   <asp:ListItem Value="CreateTime">添加时间</asp:ListItem>
                   <asp:ListItem Value="UpdateTime">更新时间</asp:ListItem>
                   <asp:ListItem Value="DownTimes">下载次数</asp:ListItem>
               </asp:DropDownList>
        ｜
            <input
                runat="server" id="btnAdd" type="button" value="新增" onclick="return window.location.href = 'AppInfoAdd.aspx?AppClass=11';" />


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
                        <th>下载次数</th>
                        <th>评论次数</th>
                        <%--<th>用户评分</th>--%>
                        <th>安装包管理</th>
                        <th>应用操作</th>
                        <th>建档日期</th>
                        <th>更新日期</th>
                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                <td><%#Eval("AppID") %>
                </td>
                <td style="width: 60px;">
                    <span class="packcount"><%#Eval("PackCount") %></span>
                    <img src='<%#Eval("MainIconUrl") %>' width="60" height="60" class="icon" />
                    <br style="clear: both;" />
                    <span style="width: 60px; overflow: hidden"><%#Eval("ShowName") %></span>
                </td>
                <td><%# BindStatus(Eval("status")) %></td>
                <td><%# Eval("DevName")%></td>
                <td><%#Eval("AppTypeName") %></td>
                <td>否</td>

                <td>实际下载:<%#Eval("DownTimesReal") %>次
                        <br />
                    显示下载：<%#Eval("DownTimes") %>次</td>

                <td><%#Eval("CommentTimes") %></td>
                <%--<td><%#Eval("CommentScore") %></td>--%>
                <td>
                    <a href="PackInfoList.aspx?AppID=<%#Eval("AppID") %>&ShowName=<%#Eval("ShowName") %>&type=app" style="line-height: 60px;">详情</a>
                    <a href="PackInfoAdd.aspx?AppID=<%#Eval("AppID") %>&ShowName=<%#Eval("ShowName") %>&type=app" style="line-height: 60px;">更新版本</a>
                </td>
                <td>
                    <a href="AppInfoAdd.aspx" style="line-height: 60px;">添加</a>

                    <a href="AppInfoEdit.aspx?AppID=<%#Eval("AppID") %>&ShowName=<%#Eval("ShowName") %>&type=app&AppClass=11" style="line-height: 60px;">修改</a>

                    <a class="del_appInfo" href="AppInfoList.aspx?AppID=<%#Eval("AppID") %>" style="cursor: pointer" onclick="javascript:void(0)">删除</a>
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
