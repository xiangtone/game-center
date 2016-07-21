<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="GroupElementAdd.aspx.cs" Inherits="AppStore.Web.GroupElementAdd" %>

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
              .nav {
            padding: 15px 0 2px 10px;
        }
                       a {
            text-decoration: none;
        }input[type="submit"], input[type="button"] {
        height:30px;  color: #fff;}

    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <script type="text/javascript">
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
        $(function () {

            <%if (PageType == "new")
              {%>
            $(".nav_title").hide();
            //$(".nav").prepend('<a href="AppInfoListNew.aspx?Action=1" style="font-size: 13px;">首页</a> &raquo;');
            $(".nav a").css("color", "#1e74c9");
            $(".nav a").css("font-size", "13px");
            $(".tr_RecommWord").hide();
            $("input[type='button']").css("background-color", "#49aef5");
            $("input[type='submit']").css("background-color", "#49aef5");
            $("input[type='button']").css("font-weight", "normal");
            $("input[type='submit']").css("font-weight", "normal");
            <%
              }%>

        });
    </script>
    <div id="background" class="background" style="display: none;">
    </div>
    <div id="progressBar" class="progressBar" style="display: none;">
        数据处理中，请稍等...W
    </div>
    <div class="nav">
         <%--<span class="nav_title">游戏中心管理</span> --%> <span class="nav_desc"><%--<a href="GroupInfo.aspx?SchemeID=<%=SchemeID %>&page=<%=PageType %>">分组管理 &raquo; </a>--%>
            <a href="GroupElement.aspx?GroupID=<%=this.GroupID %>&SchemeID=<%=SchemeID %>&page=<%=PageType %>&GroupName=<%=this.GroupName %>"><%=this.GroupName %> </a>&raquo;&nbsp;&nbsp;<a><%=this.NavShowStatus %></a></span>
    </div>
    <div id="container">
        <table id="main_form">
            <tr>
                <td class="form_text">选择游戏</td>
                <td>
                    <asp:TextBox runat="server" CssClass="form_inputbox required" ID="txtShowName" onclick="FindBack('game');" txt_name="游戏" />&nbsp;
                    <a href="javascript:FindBack('game')">查找游戏</a>&nbsp;
                    <%if (PageType != "new")
                      {%>
                    <a href="javascript:FindBack('app')">查找应用</a>
                    <%} %>
                    <input runat="server" type="hidden" id="hfAppID" class="required" txt_name="游戏" />
                    <input runat="server" type="hidden" id="hfIconUrl" class="required" txt_name="游戏图标" /></td>
            </tr>
            <tr class="tr_RecommWord" style="display:none;">
                <td class="form_text">推荐语</td>
                <td>
                    <asp:TextBox ID="txtRecommWord" TextMode="MultiLine" runat="server" CssClass="multi_text required" txt_name="推荐语" Text=" " /></td>
            </tr>
            <tr>
                <td class="form_text">开始时间</td>
                <td>
                    <asp:TextBox ID="txtStartTime" runat="server" CssClass="form_inputbox required" onclick="OnClickTime(this);" txt_name="开始时间" /></td>
            </tr>
            <tr>
                <td class="form_text">结束时间</td>
                <td>
                    <asp:TextBox ID="txtEndTime" runat="server" CssClass="form_inputbox required" onclick="OnClickTime(this);" txt_name="结束时间" /></td>
            </tr>
            <tr>
                <td class="form_text">位置编号</td>
                <td>
                    <asp:TextBox ID="txtPosID" runat="server" CssClass="form_inputbox required" txt_name="位置编号" /></td>
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
                    <asp:Button ID="btnSave" runat="server" Text="保存" OnClick="OnSave_Click" CssClass="check_form" /><span id="err_msg"></span></td>
            </tr>
        </table>

        <iframe id="ifrmModify" title="查找游戏" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_GameInfoList.aspx?SchemeID=<%=SchemeID %>"></iframe>
        <iframe id="ifrmModify2" title="查找应用" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_AppInfoList.aspx?SchemeID=<%=SchemeID %>"></iframe>
    </div>

</asp:Content>
