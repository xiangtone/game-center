<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="SearchWordsEdit.aspx.cs" Inherits="AppStore.Web.SearchWordsEdit" %>

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

        .thumbPic {
            border: 1px dotted #b8d0d6;
            max-width: 900px;
            max-height: 500px;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div id="background" class="background" style="display: none;">
    </div>
    <div id="progressBar" class="progressBar" style="display: none;">
        数据处理中，请稍等...W
    </div>
    <div class="nav">
         <%--<span class="nav_title">游戏中心管理</span> --%> <a href="PopularList.aspx?acttype=<%=this.SchemeID %>"><span class="nav_desc">热门搜索词管理</span></a>&raquo; 新增
    </div>
    <div id="container">
        <table id="main_form">
            <tr>
                <td class="form_text">搜索词</td>
                <td>
                    <asp:TextBox ID="txtRecommTitle" runat="server" CssClass="required" txt_name="搜索词" /></td>
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
                <td class="form_text">状态</td>
                <td>
                    <asp:DropDownList ID="ddlStatus" runat="server" CssClass="required" txt_name="状态" valid_type="list">
                        <asp:ListItem Value="1">开启</asp:ListItem>
                        <asp:ListItem Value="0">禁用</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <asp:Button ID="Button1" runat="server" Text="保存" CssClass="check_form" OnClick="OnSave" /><span id="err_msg"></span></td>
            </tr>
        </table>
    </div>
</asp:Content>
