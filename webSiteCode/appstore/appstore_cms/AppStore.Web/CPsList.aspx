<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="CPsList.aspx.cs" Inherits="AppStore.Web.CPsList" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
        .del {
            cursor: pointer;
        }

        .UpdateStatus {
            cursor: pointer;
        }

        .grid td {
            text-align: center;
        }

        .addnew {
            font-size: 20px;
            margin: 5px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $(".del").click(function () {
                if (confirm("确定要删除吗》")) {
                    $.ajax({
                        type: "POST",
                        url: 'CPsList.aspx?action=del&id=' + $(this).attr("data-id"),
                        success: function (data) {
                            window.location.reload();
                        }
                    });
                }
            });
            $(".UpdateStatus").click(function () {
                var str = "";
                if ($(this).attr("data-status") == "2") {
                    str = "确认禁用?";
                }
                else {
                    str = "确认启用?";
                }
                if (confirm(str)) {
                    $.ajax({
                        type: "POST",
                        url: 'CPsList.aspx?action=UpdateStatus&id=' + $(this).attr("data-id") + "&status=" + $(this).attr("data-status"),
                        success: function (data) {
                            window.location.reload();
                        }
                    });
                };
            });
        });
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div>
        <a href="CPsEdit.aspx">
            <input type="button" class="addnew" value="新增" /></a>
        <table class="grid">
            <tr>
                <th>ID</th>
                <th>类型</th>
                <th>是否开发者</th>
                <th>CP名</th>
                <th>全名</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>更新时间</th>
                <th>操作</th>
            </tr>
            <%if (CpsInfoList != null)
              {
                  foreach (var item in CpsInfoList)
                  {
            %>
            <tr>
                <td><%=item.CPID%></td>
                <td><%=item.CPType  == 1 ? "企业开发者" : "个人开发者"%></td>
                <td><%=item.IsDeveloper == 1 ? "开发者" : item.IsDeveloper == 2 ? "cp" : "开发者+CP"%></td>
                <td><%=item.CPName%></td>
                <td><%=item.FullName%></td>
                <td>
                    <%if (item.Status == 1)
                      {%>
                    启用
                    <%}
                      else
                      {
                    %>  禁用
                    <%} %>
                </td>
                <td><%=item.CreateTime%></td>
                <td><%=item.UpdateTime%></td>
                <td>
                    <%if (item.Status == 1)
                      { 
                    %>
                    <span class="UpdateStatus" data-id="<%=item.CPID%>" data-status="2">禁用</span>
                    <%}
                      else
                      {
                    %>
                    <span class="UpdateStatus" data-id="<%=item.CPID%>" data-status="1">启用</span>
                    <%} %>
                    <a href="CpsEdit.aspx?action=update&id=<%=item.CPID%>" style="margin: 5px;">修改</a>
                    <span class="del" data-id="<%=item.CPID%>">删除</span>

                </td>
            </tr>
            <%}
              }%>
        </table>
    </div>
</asp:Content>
