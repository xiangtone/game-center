<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="BeginnerRecommendEdit.aspx.cs" Inherits="AppStore.Web.BeginnerRecommendEdit" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
        body, div, table, tr, td, p {
            margin: 0;
            padding: 0;
            border: 0;
        }

        #main_form {
            margin-left: 50px;
            margin-top: 25px;
        }

            #main_form tr {
            }

                #main_form tr td {
                    padding-bottom: 10px;
                    padding-right: 10px;
                    vertical-align: top;
                }

            #main_form td.form_text {
                text-align: right;
                line-height: 1.5em;
            }

            #main_form td .multi_text {
                width: 250px;
                height: 100px;
            }

            #main_form td .form_inputbox {
                height: 20px;
            }
        .auto-style1 {
            height: 114px;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <script type="text/javascript">
        $(function () {
            if ($("#SchemeID").attr("data-value") == "104") {
                $("#find_app").attr("style", "display: none;");
            }
        });
        var FindBack = function (type) {
            if (type == "game")
                DlgIfrm("ifrmModify");
            else
                DlgIfrm("ifrmModify2");
        }

        function GetShareData(args) {
            $("#<%=hfAppID.ClientID %>").val(args.AppID);
        $("#<%=txtShowName.ClientID %>").val(args.ShowName);
        $("#<%=hfIconUrl.ClientID %>").val(args.MainIconPicUrl);

        //$("#ShowName").val(args.ShowName);
    }

        function OnClickTime(id) {
        WdatePicker({ el: id, dateFmt: 'yyyy-MM-dd HH:mm' });
    }
    </script>
       <input type="hidden" id="SchemeID" data-value="<%=this.SchemeID %>"/>
    <div id="background" class="background" style="display: none;">
    </div>
    <div id="progressBar" class="progressBar" style="display: none;">
        数据处理中，请稍等...W
    </div>
    <div class="nav">
         <%-- <%--<span class="nav_title">游戏中心管理</span> --%> --%> <span class="nav_desc">推荐位管理 &raquo; <a href="BeginnerRecommendList.aspx?acttype=<%=this.GroupTypeID %>,<%=this.SchemeID %>">新用户推荐管理</a>&raquo; 新增</span>
    </div>
    <div id="container">
        <input type="hidden" id="hidOrderNo" value="<%=this.OrderNo %>" runat="server" />
        <table id="main_form">
            <tr>
                <td class="form_text">选择游戏</td>
                <td>
                    <asp:TextBox runat="server" CssClass="form_inputbox required" ID="txtShowName" onclick="FindBack('game');" txt_name="游戏" />&nbsp;<a href="javascript:FindBack('game')">查找游戏</a>&nbsp;<a id="find_app" href="javascript:FindBack('app')">查找应用</a><input runat="server" type="hidden" id="hfAppID" class="required" txt_name="游戏" /><input runat="server" type="hidden" id="hfIconUrl" class="required" txt_name="游戏图标" /></td>
            </tr>
            <tr>
                <td class="form_text" style="height: 114px">推荐语</td>
                <td class="auto-style1">
                    <asp:TextBox ID="txtRecommWord" TextMode="MultiLine" runat="server" CssClass="multi_text required" txt_name="推荐语" /></td>
            </tr>
            <tr>
                <td class="form_text">开始时间</td>
                <td>
                    <asp:TextBox ID="txtStartTime" runat="server" CssClass="form_inputbox required" onclick="OnClickTime(this);"  txt_name="开始时间" /></td>
            </tr>
            <tr>
                <td class="form_text">结束时间</td>
                <td>
                    <asp:TextBox ID="txtEndTime" runat="server" CssClass="form_inputbox required" onclick="OnClickTime(this);" txt_name="结束时间" /></td>
            </tr>
            <tr>
                <td class="form_text">位置编号</td>
                <td>
                    <asp:TextBox ID="txtPosID" runat="server" CssClass="form_inputbox required"  txt_name="位置编号" />
                    <asp:Label ID="labelPosIDTips" runat="server" Text=""></asp:Label>
                </td>
            </tr>
            <tr>
                <td class="form_text">状态</td>
                <td>
                    <asp:DropDownList ID="ddlStatus" runat="server">
                        <asp:ListItem Value="1">启用</asp:ListItem>
                        <asp:ListItem Value="0">禁用</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <asp:Button runat="server" Text="保存" OnClick="OnSave"/><span id="err_msg"></span></td>
            </tr>
        </table>

        <iframe id="ifrmModify" title="查找游戏" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_GameInfoList.aspx?SchemeID=<%=this.SchemeID %>"></iframe>
        <iframe id="ifrmModify2" title="查找应用" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_AppInfoList.aspx?SchemeID=<%=this.SchemeID %>"></iframe>
    </div>

</asp:Content>
