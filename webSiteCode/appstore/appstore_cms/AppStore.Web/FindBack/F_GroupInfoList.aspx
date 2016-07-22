<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" Inherits="AppStore.Web.GroupInfo" %>

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

    <script type="text/javascript">

        ShareData = function (data) {
            DlgIfrmCloseInSubwin("GetGroupInfoData", data);
        }

    </script>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div id="background" class="background" style="display: none;">
    </div>
    <div id="progressBar" class="progressBar" style="display: none;">
        数据处理中，请稍等...
    </div>
    <div class="nav">
        <span class="nav_title">应用商店管理</span>
        <span class="nav_desc">首页 &raquo; <a href="GroupInfo.aspx">分组管理</a></span>
    </div>
    <div class="wrap">
        搜索类型：
           <asp:DropDownList ID="GroupType" runat="server" AutoPostBack="false">
               <asp:ListItem Value="0" Selected="True">全部</asp:ListItem>
               <asp:ListItem Value="10">应用游戏</asp:ListItem>
               <asp:ListItem Value="11">应用分类</asp:ListItem>
               <asp:ListItem Value="12">游戏分类</asp:ListItem>
               <asp:ListItem Value="21">网游单机</asp:ListItem>
               <asp:ListItem Value="31">专题</asp:ListItem>
               <asp:ListItem Value="41">推荐</asp:ListItem>
               <asp:ListItem Value="51">分发</asp:ListItem>
           </asp:DropDownList>
        ｜分组状态：
               <asp:DropDownList ID="dropStatus" runat="server" AutoPostBack="false">
                   <asp:ListItem Value="">全部</asp:ListItem>
                   <asp:ListItem Value="1">启用</asp:ListItem>
                   <asp:ListItem Value="0">禁用</asp:ListItem>
               </asp:DropDownList>

        搜索内容：<input runat="server" id="Keyword_2" type="text" />

        <asp:Button ID="btnSearch" runat="server" Text="搜索" CssClass="btn" OnClick="btnSearch_Click" />｜排序方式：
               <asp:DropDownList ID="OrderType" runat="server" AutoPostBack="false">
                   <asp:ListItem Value="CreateTime">添加时间</asp:ListItem>
                   <asp:ListItem Value="UpdateTime">更新时间</asp:ListItem>
               </asp:DropDownList>
        ｜
            <input
                runat="server" id="btnAdd" type="button" value="新增" onclick="return window.location.href = 'GroupInfoEdit.aspx?PageType=addGroup&GroupID=0';" />


    </div>
    <asp:Repeater ID="objRepeater" runat="server">
        <HeaderTemplate>
            <table style="width: 100%" class="grid">
                <thead>
                    <tr>
                        <th>分组ID</th>
                        <th style="width: 70px;">分组图标</th>
                        <th>分组名称</th>
                        <th>分组类型名称</th>
                        <th>分组状态</th>
                        <th>排序类型</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>查找带回</th>

                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                <td><%#Eval("GroupID") %>
                </td>

                <td style="width: 60px;">
                    <span class="packcount"><%#Eval("ElementCount") %></span>
                    <img src='<%#Eval("GroupPicUrl") %>' width="60" height="60" class="icon" />

                </td>
                <td>
                    <a href="GroupElement.aspx?GroupID=<%#Eval("GroupID") %>&GroupName=<%#(Eval("GroupName"))%>"><%#(Eval("GroupName"))%></a>
                </td>
                <td>
                    <%#Eval("TypeName") %>
                </td>
                <td><%#Convert.ToInt32(Eval("Status")) == 1 ? "启用" : "<span style='color:red;'>禁用</span>" %></td>
                <td><%#Convert.ToInt32(Eval("OrderType")) == 0 ? "默认" : "时间" %></td>

                <td><%#((DateTime)Eval("StartTime")).ToString("yyyy-mm-dd")%></td>
                <td><%#((DateTime)Eval("EndTime")).ToString("yyyy-mm-dd") %></td>
                <td><a class="btnSelect" href="javascript:ShareData({GroupID:'<%#Eval("GroupID") %>', GroupName:'<%#Eval("GroupName")%>'})"
                    title="查找带回" style="margin-top: 20px;">选择</a></td>
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
