<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="AppInformList.aspx.cs" Inherits="AppStore.Web.AppInformList" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
        td {
        text-align:center;
        }

    </style>
    <script type="text/ecmascript">
        $(function () {

        });
        function handle(id) {
            $("#<%=HidID.ClientID %>").val(id);
            DlgIfrm("Remarks");
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div>
        <table class="grid">
            <tr>
                <th>时间日期</th>
                <th>举报游戏</th>
                <th>举报内容</th>
                <th>更多描述</th>
                <th>手机型号</th>
                <th>用户ID</th>
                <th>操作</th>
                <th>备注</th>
            </tr>
            <%if (Appinformt != null)
              {
                  foreach (var item in Appinformt)
                  {
            %>
            <tr>
                <td  style="width:10%;"><%=item.CreateTime.ToString("yyyy-MM-dd HH:mm:ss")%></td>
                <td  style="width:10%;"> <%= GetAppName(int.Parse(item.AppId.ToString()))%></td>
                 <td  style="width:10%;"><%= BindInform(item.InformType) %></td>
                <td  style="width:20%;"><%=item.InformDetail %></td>
                <td  style="width:5%;"><%=item.DeviceFlag %></td>
                <td  style="width:10%;"><%=item.OpenId%></td>
                <td  style="width:5%;"><%if (item.Status == 1)
                      {%>
                    <span style="cursor: pointer; color: #3820a0;" onclick="handle(<%=item.InformId%>);">添加备注</span>
                    <% }
                      else if (item.Status == 2)
                      {%>
                    <span>已跟进</span>
                    <%}%></td>
                <td  style="width:10%;"><%=item.Remarks%></td>
            </tr>
            <%}
              }%>
        </table>
    </div>
    <div id="Remarks" style="display:none;width: 500px;height:230px; padding: 20px;text-align:center;" title="添加备注">
        <p style="float:left;margin:20px;">填写备注:</p>
        <br />
        <asp:TextBox ID="txtRemarks" runat="server"  TextMode="MultiLine" Width="347" Height="121"></asp:TextBox>
        <br />
        <br />
        <asp:HiddenField ID="HidID" runat="server" />
        <asp:Button ID="btnSave" runat="server" Text="保存" OnClick="btnSave_Click" />
    </div>
</asp:Content>
