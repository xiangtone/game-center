<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="GroupSchemesList.aspx.cs" Inherits="AppStore.Web.GroupSchemesList" %>

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
        <span class="nav_desc">首页 &raquo; <a href="GroupSchemesList.aspx?acttype=<%=this.SchemeID%>">方案管理</a></span>
    </div>
    <div class="wrap">
        <input runat="server" id="btnAdd" type="button" value="新增" onclick="return window.location.href = 'GroupSchemesEdit.aspx?Action=Add';" />
    </div>

    <asp:Repeater ID="objRepeater" runat="server">
        <HeaderTemplate>
            <table style="width: 100%" class="grid">
                <thead>
                    <tr>
                        <th>方案ID_分组ID</th>
                        <th>分组名称</th>
                        <th>类别名称</th>
                        <th>排序类型</th>
                        <th>建档日期</th>
                        <th>更新日期</th>
                        <th>状态</th>
                        <th>备注</th>
                        <th>操作</th>

                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                <td><%#Eval("SchemeID") %>_<%#Eval("GroupID") %>
                </td>
                <td>
                     <a href="GroupElement.aspx?GroupID=<%#Eval("GroupID") %>&GroupName=<%#(Eval("GroupName"))%>"><%#(Eval("GroupName"))%></a>
                 <%--   <%#Eval("GroupName") %>--%>
                </td>
                <td><%#Eval("TypeName") %></td>
                <td><%#Convert.ToInt32(Eval("OrderType")) == 0 ? "默认" : "时间" %></td>
                <td><%#((DateTime) Eval("CreateTime")).ToString("yyyy-MM-dd") %></td>
                <td><%#((DateTime) Eval("UpdateTime")).ToString("yyyy-MM-dd") %></td>
                <td><%#Convert.ToInt32(Eval("Status")) == 1 ? "启用" : "<span style='color:red;'>禁用</span>" %></td>
                <td><%#Eval("Remarks") %></td>
                <td>
                    <%--<a href="GroupSchemesEdit.aspx?GroupID=<%#Eval("GroupID") %>&GroupTypeID=<%#Eval("GroupTypeID") %>&OrderType=<%#Eval("OrderType") %>&Action=Add" style="line-height: 60px;">添加</a>

                    <a href="GroupSchemesEdit.aspx?SchemeID=<%#Eval("SchemeID") %>&GroupID=<%#Eval("GroupID") %>&GroupTypeID=<%#Eval("GroupTypeID") %>&OrderType=<%#Eval("OrderType") %>&Action=Edit" style="line-height: 60px;">修改</a>--%>

                    <a class="del_appInfo" href="GroupSchemesList.aspx?acttype=2&SchemeID=<%#Eval("SchemeID") %>&GroupID=<%#Eval("GroupID") %>" style="cursor: pointer" onclick="javascript:void(0)">删除</a>
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
