<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true"   Inherits="AppStore.Web.LinkInfoList" %>


<%@ Register Assembly="AspNetPager" Namespace="Wuqi.Webdiyer" TagPrefix="webdiyer" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <script type="text/javascript">
        ShareData = function (data) {
            DlgIfrmCloseInSubwin("GetShareLinkInfo", data);
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div class="wrap">
        网站名称：<input runat="server" id="Keyword_2" type="text" />
        <asp:Button ID="btnSearch" runat="server" Text="搜索" CssClass="btn" OnClick="btnSearch_Click" />
    </div>
    <asp:Repeater ID="objRepeater" runat="server">
        <HeaderTemplate>
            <table style="width: 100%" class="grid">
                <thead>
                    <tr>
                        <th>跳转链接ID</th>
                        <th style="width: 70px;">图标及名称</th>
                        <th>状态</th>
                        <th>开发者</th>
                        <th>实际链接地址</th>
                        <th>建档日期</th>
                        <th>更新日期</th>
                        <th>应用操作</th>

                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                <td><%#Eval("LinkID") %>
                </td>

                <td style="width: 60px;">
                    <img src='<%#Eval("IconUrl") %>' width="60" height="60" class="icon" />
                    <br style="clear: both;" />
                    <span style="width: 60px; overflow: hidden"><%#Eval("ShowName") %></span>
                </td>
                <td><%#Convert.ToInt32(Eval("Status")) == 1 ? "启用" : "禁用" %></td>
                <td><%# this.dic_DevList[Convert.ToInt32(Eval("CPID"))]%></td>
                <td><%#Eval("LinkUrl") %></td>
                <td><%#((DateTime) Eval("CreateTime")).ToString("yyyy-MM-dd") %></td>
                <td><%#((DateTime) Eval("UpdateTime")).ToString("yyyy-MM-dd") %></td>
                <td>
                     <a class="btnSelect" href="javascript:ShareData({LinkID:'<%#Eval("LinkID") %>', ShowName:'<%#Eval("ShowName")%>',IconPicUrl:'<%#Eval("IconUrl") %>'})"
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
            PagingButtonSpacing="0px" NumericButtonCount="10" PageSize="20" AlwaysShow="true">
        </webdiyer:AspNetPager>
    </div>
</asp:Content>
