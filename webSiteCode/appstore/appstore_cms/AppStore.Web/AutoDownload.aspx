<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="AutoDownload.aspx.cs" Inherits="AppStore.Web.AutoDownload" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <script type="text/javascript">

        $(function () {
            $("#btnDownload").click(function () {
                var ids = $("#txtAppIds").val().split(',');
                    $.ajax({
                        type: "POST",
                        url: 'AutoDownload.aspx?action=download&id=' + $("#txtAppIds").val(),
                        succe function (data) {
                            var datas=data.split(',');
                            for (var i = 0; i < datas.length ; i++) {
                                //$("#content").html($("#content").html() + datas[i]);
                                window.open(datas[i]);
                            }
                        }
                    });
            });
        });
    </script>

</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">

   请输入appid：  <input type="text" id="txtAppIds" style="width:300px;" />   多个的时候使用,隔开
     
<br />
    <br />
    <input type="button" id="btnDownload" value="下载" />
    <div id="content"></div>
</asp:Content>
