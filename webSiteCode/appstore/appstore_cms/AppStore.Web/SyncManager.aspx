<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="SyncManager.aspx.cs" Inherits="AppStore.Web.SyncManager" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
        .auto-style5 {
            width: 100px;
            height: 18px;
        }
        .auto-style6 {
            width: 200px;
            height: 18px;
        } input[type="submit"], input[type="button"] {
        height:30px;  color: #fff;font-weight:normal;}
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <%if (PageType=="new")
      {%>
        <asp:Button runat="server" Text="数据生效" OnClick="OnRsyncStart" BackColor="#49aef5" />

      <% }
      else
      {%>
    <asp:Button runat="server" Text="同步缓存" OnClick="OnRsyncStart" />
    <asp:Button runat="server" Text="立即生效" Visible="false" ID="SyncEffective" OnClick="SyncEffective_Click" />
    <asp:Button runat="server" Text="同步缓存Test" Visible="false" ID="Button1" OnClick="Button1_Click" />
      <% } %>
    
</asp:Content>
