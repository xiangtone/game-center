<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="ChannelEdit.aspx.cs" Inherits="AppStore.Web.ChannelEdit" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">

    <div>
        <table class="edit-grid">
            <tr>
                <td class="title">渠道号</td>
                <td class="content">
                    <asp:TextBox ID="txtChannelNO" runat="server"></asp:TextBox>
                </td>
            </tr>
            <tr>
                <td class="title">渠道名</td>
                <td class="content">
                    <asp:TextBox ID="txtChannelName" runat="server"></asp:TextBox></td>
            </tr>
            <tr>
                <td class="title">渠道标识</td>
                <td class="content">
                    <asp:TextBox ID="txtChannelFlag" runat="server"></asp:TextBox></td>
            </tr>

        </table>
        <asp:HiddenField ID="hidID" Value="0" runat="server" />
        <asp:Button ID="Button1" runat="server" Text="保存" OnClick="Button1_Click" />

    </div>
</asp:Content>
