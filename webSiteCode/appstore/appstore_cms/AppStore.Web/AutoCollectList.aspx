<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="AutoCollectList.aspx.cs" Inherits="AppStore.Web.AutoCollectList" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <script type="text/javascript">
        $(function () {
            $(".del").click(function () {
                if (confirm("确认要删除吗?")) {
                    $.ajax({
                        type: "POST",
                        url: 'AutoCollectList.aspx?action=del&id=' + $(this).attr("data-id"),
                        succe function (data) {
                            window.location.reload();
                        }
                    });
                };
            });
        });
    </script>
    <style type="text/css">
        .del {
            cursor: pointer;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <%--    <asp:GridView CssClass="grid" ID="GridView1" runat="server" AutoGenerateColumns="False" OnRowCommand="GridView1_RowCommand" EmptyDataText="Data Is Empty">
         
        <Columns>
            <asp:ImageField DataImageUrlField="MainIconUrl" HeaderText="Ico">
                <ControlStyle Height="70px" Width="70px"></ControlStyle>
            </asp:ImageField>
            <asp:BoundField DataField="AppID" HeaderText="ID"></asp:BoundField>
            <asp:BoundField DataField="AppName" HeaderText="游戏名"></asp:BoundField>
            <asp:BoundField DataField="MainPackSize" HeaderText="大小"></asp:BoundField>
            <asp:BoundField DataField="PackName" HeaderText="包名"></asp:BoundField>
            <asp:BoundField DataField="AppDesc" HeaderText="描述"></asp:BoundField>
            <asp:TemplateField ShowHeader="False">
                <ItemTemplate>
                    <asp:LinkButton ID="LinkButton1" runat="server" CausesValidation="False" CommandArgument=' <%# Eval("AppID") %> ' CommandName="Delete"
                        Text="删除" OnClientClick='<%# "if (!confirm(\"你确定要删除" + Eval("AppName").ToString() + "吗?\")) return false;"%>'></asp:LinkButton>
                </ItemTemplate>
            </asp:TemplateField>
        </Columns>
    </asp:GridView>--%>
    <div>
        <a href="AutoCollect.aspx">
            <input type="button" class="addnew" value="新增" /></a>
        <table class="grid">
            <tr>
                <th>ico</th>
                <th>游戏名</th>
                <th>大小</th>
                <th>包名</th>
                <th>创建时间</th>
                <th>描述</th>
                <th>操作</th>
            </tr>
            <%if (list != null)
              {
                  foreach (var item in list)
                  {
            %>
            <tr>
                <td><img src="<%=item.MainIconUrl%>"  width="70" height="70"/></td>
                <td><%=item.AppName%></td>
                <td><%=item.MainPackSize%></td>
                <td><%=item.PackName %></td>
                <td><%=item.UpdateTime%></td>
                <td><%=item.AppDesc%></td>
                <td>
                    <span class="del" data-id="<%=item.AppID%>">删除</span> </td>
            </tr>
            <%}
              }%>
        </table>
    </div>
</asp:Content>
