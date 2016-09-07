<%@ Page Title="" Language="C#" MasterPageFile="~/CommonMaster.Master" AutoEventWireup="true" CodeBehind="PackList.aspx.cs" Inherits="updatesys_cms.Web.PackList" %>
<asp:Content ID="Content1" ContentPlaceHolderID="Header" runat="server">
<script type="text/javascript">
    function OpenModify(id) {
        if (id <= 0)
            $("#ifrmModify").attr("src", "PackEdit.aspx?act=add&id=" + id);
        else
            $("#ifrmModify").attr("src", "PackEdit.aspx?act=edit&id=" + id);
        DlgIfrm("ifrmModify");

    }
    function Confirm_Del() {
        return confirm("确定要删除吗？");
    }
    function Reload(isReload) {
        if (isReload) {
            document.location.reload();
        }
    }
    $(document).ready(function () {
        var dataListLastTd = '';
        var colors = new Array();
        colors[0] = "#f4f4f4";
        colors[1] = "#dfdfdf";

        var colorIndex = -1;
        $("#datalist tr").each(function () {
            var eachTd = $(this).children("td");
            if (eachTd.length > 0) {
                var curTd = eachTd.eq(1).html() + "|" + eachTd.eq(2).html();
                if (curTd != dataListLastTd) {
                    colorIndex += 1;
                }
                var curColor = colors[colorIndex % 2];
                eachTd.css("background-color", curColor);
                dataListLastTd = curTd;
            }
        });
    });
</script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="Body" runat="server">
<div class="nav">
    <span class="nav_title">更新管理</span>
    <span class="nav_desc">首页 &raquo; 更新管理</span>
</div>
<div class="wrap">
    <input type="hidden" runat="server" id="hidParentId" value="1" />
    <asp:Label runat="server" ID="lblParentDesc" Text=""></asp:Label>
    <asp:DropDownList runat="server" ID="ddlSchemeId" OnSelectedIndexChanged="OnSchemeChanged" AutoPostBack="true">
        <asp:ListItem Value="0">通用方案</asp:ListItem>
        <asp:ListItem Value="1">测试方案</asp:ListItem>
    </asp:DropDownList>
    <asp:Button runat="server" UseSubmitBehavior="false" ID="btnToParent" Text="返回父级" />
    <input type="button" runat="server" id="btnAdd" value="新增" onclick="OpenModify(0)" />
</div>
<div>
<table class="grid" id="datalist">
    <tr>
        <th>应用名称</th><th>包名</th><th>版本代码</th><th>版本号</th><th>渠道号</th><th>更新类型</th><th>包地址</th><th>发布时间</th><th>状态</th><th>管理</th>
    </tr>
    <asp:Repeater ID="PackListRpt" runat="server">
    <ItemTemplate>
        <tr>
            <td><%#Eval("AppName")%></td>
            <td><%#Eval("PackName")%></td>
            <td><%#Eval("VerCode")%></td>
            <td><%#Eval("VerName")%></td>
            <td><%#Eval("ChannelNo")%></td>
            
            
            <td><%#GetType(Eval("UpdateType"))%></td>
            <td><a href="<%#Eval("PackUrl")%>">下载</a></td>
            <td><%#Eval("PubTime")%></td>
            
            <td><%#string.Format("{0}",(int)Eval("Status")==1?"启用":"禁用")%></td>
            <td><a href='javascript:OpenModify(<%#Eval("UpdateId") %>);'>编辑</a>&nbsp;<asp:LinkButton ID="LinkButton1" runat="server" Text="删除" OnCommand="Del_Click" CommandArgument='<%#BindItem(Container.DataItem) %>' OnClientClick="return Confirm_Del();"></asp:LinkButton></td>
        </tr>
    </ItemTemplate>
    </asp:Repeater>
</table>
</div>
<iframe id="ifrmModify" title="更新编辑" frameborder="0" scrolling="yes" style="display:none; width:600px; height:640px;" src="about:blank"></iframe>
</asp:Content>
