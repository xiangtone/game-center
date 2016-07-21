<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="CpsEdit.aspx.cs" Inherits="AppStore.Web.CpslEdit" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">

    <div>
        <table class="edit-grid">
            <tr>
                <td class="title">CP类型</td>
                <td class="content">
                    <asp:DropDownList ID="drpCPType" runat="server">
                        <asp:ListItem Value="1">企业开发者</asp:ListItem>
                        <asp:ListItem Value="2">个人开发者</asp:ListItem>
                    </asp:DropDownList>
                </td>
            </tr>
            <tr>
                <td class="title">是否开发者</td>
                <td class="content">
                    <asp:DropDownList ID="drpIsDeveloper" runat="server">
                        <asp:ListItem Value="1">开发者</asp:ListItem>
                        <asp:ListItem Value="2">CP</asp:ListItem>
                        <asp:ListItem Value="3">开发者+CP</asp:ListItem>
                    </asp:DropDownList>
                </td>
            </tr>
            <tr>
                <td class="title">CP名</td>
                <td class="content">
                    <asp:TextBox ID="txtCpsName" runat="server"></asp:TextBox></td>
            </tr>
            <tr>
                <td class="title">全名</td>
                <td class="content">
                    <asp:TextBox ID="txtFullName" runat="server"></asp:TextBox></td>
            </tr>

        </table>
        <asp:HiddenField ID="hidID" Value="0" runat="server" />
        <asp:Button ID="Button1" runat="server" Text="保存" OnClick="Button1_Click" />

    </div>

</asp:Content>
