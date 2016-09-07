<%@ Page Title="" Language="C#" MasterPageFile="~/CommonMaster.Master" AutoEventWireup="true" CodeBehind="TestScheme.aspx.cs" Inherits="updatesys_cms.Web.TestScheme" %>
<asp:Content ID="Content1" ContentPlaceHolderID="Header" runat="server">
    <style type="text/css">
        #content #add { margin:10px 5px;}
        #content #add input[type=text] { margin-left:8px; margin-right:5px; width:250px;}
        #content #add input[type=submit] { margin-right:3px; cursor:pointer;}
        #content #result { padding-top:10px;}
        #content #result p { margin-bottom:8px; padding:5px 5px;}
        #content #result p:hover {background-color:#F8F8F8;}
        .del { margin:3px; font-weight:900; font-size:20px; text-decoration:none; color:#F00;}
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="Body" runat="server">
    <div class="nav">
        <span class="nav_title">更新管理</span>
        <span class="nav_desc">首页 &raquo; 测试方案配置</span>
    </div>
    <div id="content">
        <div id="add">设备信息<asp:TextBox runat="server" ID="txtIMEI"></asp:TextBox><asp:Button runat="server" Text="添加" OnClick="OnAdd" /></div>
        <div id="result">
            <asp:Repeater runat="server" ID="rpResultList">
                <ItemTemplate><p><asp:LinkButton runat="server" OnCommand="OnDel" CommandArgument="<%#Container.DataItem %>" CssClass="del">x</asp:LinkButton> <%#Container.DataItem %></p></ItemTemplate>
            </asp:Repeater>
        </div>
    </div>
</asp:Content>
