<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="FeedBack.aspx.cs" Inherits="AppStore.Web.FeedBack" %>
<%@ Register Assembly="AspNetPager" Namespace="Wuqi.Webdiyer" TagPrefix="webdiyer" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
        td {
        text-align:center;
        }

    </style>
    <script type="text/ecmascript">
        $(function () {
           
        });
        function handle(id,val) {
            $("#<%=HidFBID.ClientID %>").val(id);
            $("#<%=txtRemarks.ClientID %>").val(val);
            DlgIfrm("Remarks");
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div>
        <table class="grid">
            <tr>
                <th>时间日期</th>
                <th>内容</th>
                <th>联系方式</th>
                <th>品牌</th>
                <th>手机型号</th>
                <th>版本号</th>
                <th>用户ID</th>
                <th>手机号码</th>
                <th>操作</th>
                <th>备注</th>
            </tr>
            <%if (FeedBackList != null)
              {
                  foreach (var item in FeedBackList)
                  {
            %>
            <tr>
                <td style="width:10%;"><%=item.CreateTime.ToString("yyyy-MM-dd")%></td>
                <td style="width:auto"><%=item.Content%></td>
                <td style="width:10%;"><%=item.UserContact %></td>
                <td style="width:8%;"><%=item.BrandFlag%></td>
                <td style="width:8%;"><%=item.ModelFlag%></td>
                 <td style="width:8%;"><%=item.Version%></td>
                <td style="width:10%;"><%=item.OpenId%></td>
                <td style="width:8%;"><%=item.UserContact %></td>
                <td style="width:5%;"><span style="cursor: pointer; color: #3820a0;" onclick="handle(<%=item.FBId%>,'<%=item.Remarks%>');">备注</span></td>
                <td style="width:10%;"><%=item.Remarks%></td>
            </tr>
            <%}
              }%>
        </table>

         <div class="wrap">
        <webdiyer:AspNetPager runat="server" ID="pagerList" OnPageChanged="pagerList_PageChanged" CssClass="pages" CurrentPageButtonClass="cpb"
            PagingButtonSpacing="0px" NumericButtonCount="10" PageSize="20" AlwaysShow="true">
        </webdiyer:AspNetPager>
    </div>
    </div>
    <div id="Remarks" style="display:none;width: 500px;height:230px; padding: 20px;text-align:center;" title="添加备注">
        <p style="float:left;margin:20px;">填写备注:</p>
        <br />
        <asp:TextBox ID="txtRemarks" runat="server"  TextMode="MultiLine" Width="347" Height="121"></asp:TextBox>
        <br />
        <br />
        <asp:HiddenField ID="HidFBID" runat="server" />
        <asp:Button ID="btnSave" runat="server" Text="保存" OnClick="btnSave_Click" />
    </div>

</asp:Content>
