<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="F_AllCategoryList.aspx.cs" Inherits="AppStore.Web.FindBack.F_AllCategoryList" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <script type="text/javascript">
        ShareData = function (data) {
            DlgIfrmCloseInSubwin("GetShareCategory", data);
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div class="wrap">
    </div>
    <asp:Repeater ID="objRepeater" runat="server">
        <HeaderTemplate>
            <table style="width: 100%" class="grid">
                <thead>
                    <tr>
                        <th>类别ID</th>
                        <th>分类ID</th>
                        <th>分类名称</th>
                        <th>建档日期</th>
                        <th>更新日期</th>
                        <th>应用操作</th>

                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                <td><%#Eval("TypeID") %>
                </td>
                <td><%#Eval("TypeClass") %></td>
                <td><%#Eval("TypeName")%></td>
                <td><%#((DateTime) Eval("CreateTime")).ToString("yyyy-MM-dd") %></td>
                <td><%#((DateTime) Eval("UpdateTime")).ToString("yyyy-MM-dd") %></td>
                <td>
                    <a class="btnSelect" href="javascript:ShareData({TypeID:'<%#Eval("TypeID") %>', TypeName:'<%#Eval("TypeName")%>',TypePicUrl:'<%#Eval("TypePicUrl") %>'})"
                        title="查找带回" style="margin-top: 20px;">选择</a>
                </td>
            </tr>

        </ItemTemplate>
        <FooterTemplate>
            </table>
        </FooterTemplate>

    </asp:Repeater>
</asp:Content>

