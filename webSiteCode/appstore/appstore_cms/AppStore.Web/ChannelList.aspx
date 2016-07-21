<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="ChannelList.aspx.cs" Inherits="AppStore.Web.ChannelList" %>

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
                if (confirm("确认要删除吗?")) {
                    $.ajax({
                        type: "POST",
                        url: 'ChannelList.aspx?action=del&id=' + $(this).attr("data-id"),
                        succe: function (data) {
                            window.location.reload();
                        }
                    });
                };
            });

            $(".UpdateStatus").click(function () {
                var str = "";
                if ($(this).attr("data-status") == "2") {
                    str = "确认禁用该渠道?";
                }
                else {
                    str = "确认启用该渠道?";
                }
                if (confirm(str)) {
                    $.ajax({
                        type: "POST",
                        url: 'ChannelList.aspx?action=UpdateStatus&id=' + $(this).attr("data-id") + "&status=" + $(this).attr("data-status"),
                        succe: function (data) {
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
        <a href="ChannelEdit.aspx">
            <input type="button" class="addnew" value="新增" /></a>
        <table class="grid">
            <tr>
                <th>渠道号</th>
                <th>渠道名</th>
                <th>渠道标识</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>更新时间</th>
                <th>操作</th>
            </tr>
            <%if (ChannelInfoList != null)
              {
                  foreach (var item in ChannelInfoList)
                  {
            %>
            <tr>
                <td><%=item.ChannelNO%></td>
                <td><%=item.ChannelName%></td>
                <td><%=item.ChannelFlag%></td>
                <td><%=BindStatus(item.Status) %></td>
                <td><%=item.CreateTime%></td>
                <td><%=item.UpdateTime%></td>
                <td>
                    <%if (item.Status == 1)
                      { 
                    %>
                    <span class="UpdateStatus" data-id="<%=item.ChannelNO%>" data-status="2">禁用</span>
                    <%}
                      else
                      {
                    %>
                    <span class="UpdateStatus" data-id="<%=item.ChannelNO%>"  data-status="1">启用</span>
                    <%} %>
                    <a href="ChannelEdit.aspx?action=update&id=<%=item.ChannelNO%>" style="margin:5px;">修改</a>
                    <span class="del" data-id="<%=item.ChannelNO%>">删除</span> </td>
            </tr>
            <%}
              }%>
        </table>
    </div>
</asp:Content>
