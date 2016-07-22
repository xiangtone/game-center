<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="GroupSchemesEdit.aspx.cs" Inherits="AppStore.Web.GroupSchemesEdit" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
        body, div, table, tr, td, p {
            margin: 0;
            padding: 0;
            border: 0;
        }

        body {
            padding: 10px;
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
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <script type="text/javascript">
        var FindBack = function (type) {
            if (type == "FindGroupID")
                DlgIfrm("ifrmModify");
            else
                DlgIfrm("ifrmModify2");
        }

        function GetGroupInfoData(args) {
            $("#<%=txtGroupID.ClientID %>").val(args.GroupID);
        }

        function GetGroupTypeData(args) {
            $("#<%=txtGroupTypeID.ClientID %>").val(args.TypeID);
        }

    </script>


    <div id="background" class="background" style="display: none;">
    </div>
    <div id="progressBar" class="progressBar" style="display: none;">
        数据处理中，请稍等...W
    </div>
    <div class="nav">
        <span class="nav_title">应用商店管理</span> <span class="nav_desc">首页 &raquo; <a href="GroupSchemesList.aspx?acttype=<%=this.SchemeID %>">方案管理</a>&raquo; 编辑</span>
    </div>
    <div id="container">
        <table id="main_form">
            <tr>
                <td class="form_text">方案ID</td>
                <td>
                    <asp:TextBox ID="txtSchemeID" runat="server" CssClass="form_inputbox required" txt_name="方案ID" /><span style="margin-left: 10px; color: red;">1=游戏中心，2=应用中心，3=桌面分发</span>
                    <asp:HiddenField ID="hidOldSchemeID" runat="server" />
                </td>
            </tr>
            <tr>
                <td class="form_text">分组ID</td>
                <td>
                    <asp:TextBox ID="txtGroupID" runat="server" CssClass="form_inputbox required" txt_name="分组ID" /><span style="margin-left: 10px;"><a href="javascript:FindBack('FindGroupID')">查找</a></span>
                    <asp:HiddenField ID="hidOldGroupID" runat="server" />
                </td>
            </tr>
            <tr>
                <td class="form_text">分组类型ID</td>
                <td>
                    <asp:TextBox ID="txtGroupTypeID" runat="server" CssClass="form_inputbox required" txt_name="分组类型ID" /><span style="margin-left: 10px;"><a href="javascript:FindBack('FindGroupTypeID')">查找</a></span>
                </td>
            </tr>
            <tr>
                <td class="form_text">排序类型</td>
                <td>
                    <asp:TextBox ID="txtOrderType" runat="server" CssClass="form_inputbox required" txt_name="排序类型" />
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
                    <asp:Button ID="btnSave" runat="server" Text="保存" CssClass="check_form" OnClick="btnSave_Click" /><span id="err_msg"></span></td>
            </tr>
        </table>

        <iframe id="ifrmModify" title="查找分组" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_GroupInfoList.aspx"></iframe>
        <iframe id="ifrmModify2" title="查找类别" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_GroupTypeList.aspx"></iframe>
    </div>
</asp:Content>
