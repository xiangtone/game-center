<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="F_GroupTypeList.aspx.cs" Inherits="AppStore.Web.FindBack.F_GroupTypeList" %>


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
            DlgIfrmCloseInSubwin("GetGroupTypeData", data);
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
        <span class="nav_desc">首页 &raquo; <a href="GroupInfo.aspx">分类管理</a></span>
    </div>
    <asp:Repeater ID="objRepeater" runat="server">
        <HeaderTemplate>
            <table style="width: 100%" class="grid">
                <thead>
                    <tr>
                        <th>分类ID</th>
                        <th>分型名称</th>
                        <th>排序编号</th>
                        <th>状态</th>
                        <th>查找带回</th>

                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                <td><%#Eval("TypeID") %>
                </td>
                <td>
                    <%#Eval("TypeName") %>
                </td>
                <td>
                    <%#Eval("OrderNo") %>
                </td>
                <td><%#Convert.ToInt32(Eval("Status")) == 1 ? "启用" : "<span style='color:red;'>禁用</span>" %></td>
            
                <td><a class="btnSelect" href="javascript:ShareData({TypeID:'<%#Eval("TypeID") %>', TypeName:'<%#Eval("TypeName")%>'})"
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
