<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="F_DevInfoList.aspx.cs" Inherits="AppStore.Web.FindBack.F_DevInfoList" %>

<%@ Register Assembly="AspNetPager" Namespace="Wuqi.Webdiyer" TagPrefix="webdiyer" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
     <script type="text/javascript">
         ShareData = function (data) {
             DlgIfrmCloseInSubwin("GetShareData", data);
         }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
     <div class="nav">
            <span class="nav_title">应用商店管理</span>
            <span class="nav_desc">首页 &raquo; <a href="/FindBack/F_AppInfoList.aspx">开发者查找带回</a></span>
        </div>
        <div class="wrap">
            应用名称：<input name="Keyword_1" type="text" value="<%=base.Keyword_1 %>" />

            <input runat="server" id="btnSearch" type="button" value="搜索" onserverclick="btnSearch_Click" />
        </div>
        <asp:Repeater ID="objRepeater" runat="server">
            <HeaderTemplate>
                <table style="width: 100%" class="grid">
                    <thead>
                        <tr>
                            <th>开发者ID</th>
                            <th>开发者名称</th>
                            <th>备注信息</th>
                            <th>建档日期</th>
                            <th>操作</th>
                        </tr>
                    </thead>
            </HeaderTemplate>
            <ItemTemplate>
                <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                    <td><%#Eval("CPID") %></td>
                    <td><%#Eval("CPName") %></td>
                    <td><%#Eval("Remarks") %></td>
                    <td><%#((DateTime) Eval("CreateTime")).ToString("yyyy-MM-dd") %></td>
                    <td>
                        <a class="btnSelect" href="javascript:ShareData({DevID:'<%#Eval("CPID") %>', DevName:'<%#Eval("CPName")%>'})"
                            title="查找带回" style="margin-top: 20px;">选择</a>
                    </td>
                </tr>

            </ItemTemplate>
            <FooterTemplate>
                </table>
            </FooterTemplate>

        </asp:Repeater>
        <div class="wrap">
            <webdiyer:AspNetPager runat="server" ID="pagerList" OnPageChanged="pagerList_PageChanged" CssClass="pages" CurrentPageButtonClass="cpb"
                PagingButtonSpacing="0px" NumericButtonCount="10" PageSize="11" AlwaysShow="true">
            </webdiyer:AspNetPager>
        </div>
</asp:Content>
