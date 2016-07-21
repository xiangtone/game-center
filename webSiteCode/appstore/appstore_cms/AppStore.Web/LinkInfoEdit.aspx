<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="LinkInfoEdit.aspx.cs" Inherits="AppStore.Web.LinkInfoEdit" %>


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

        .w300 {
            width: 300px;
        }

        .Child .jcrop-holder {
            margin-left: 130px;
        }

        .Child p {
            margin: 10px;
            height: 21px;
            line-height: 21px;
        }

            .Child P span {
                float: left;
                width: 100px;
                text-align: right;
                margin-right: 10px;
            }

            .Child p input {
                height: 21px;
                min-height: 21px;
                padding: 0 0px;
            }

        .Child .picshow {
            margin: 10px;
        }

            .Child .picshow span {
                float: left;
                width: 100px;
                text-align: right;
                margin-right: 10px;
            }

        .Child .error {
            color: #f00;
        }

        .nav-new {
            height: 30px;
            padding: 5px 0px 4px 0px;
            border-bottom: solid 3px #aaa;
            line-height: 30px;
        }

            .nav-new .nav_title {
                margin-left: 0px;
                font-size: 15pt;
                font-weight: bold;
                line-height: 30px;
                color: #006666;
            }
    </style>

    <script type="text/javascript">

        var FindBack = function () {
            var randomnumber = Math.floor(Math.random() * 100000);
            $("#ifrmModify").attr("src", "FindBack/F_DevInfoList.aspx?randomnumber=" + randomnumber);
            DlgIfrm("ifrmModify");

        }

        function GetShareData(args) {
            $("#<%=DevName.ClientID %>").val(args.DevName);
            $("#<%=DevID.ClientID%>").val(args.DevID);
            $("#<%=CPName.ClientID%>").val(args.DevName);
        }




        //上传成功
        function upload_suc(id, filename, filesize, hash) {

            if (id == "ThumbPicUrl") {
                $("#<%=ThumbPicUrl.ClientID %>").val(filename);
                $("#ShowThumbPic").attr("src", filename);

            } else if (id == "IconPicUrl") {
                $("#<%=IconPicUrl.ClientID%>").val(filename);
                    $("#ShowIconPic").attr("src", filename);
                }
        }

        //上传失败
        function upload_fail(id, msg) {
            alert("upload fail:" + filename);
        }

        $(function () {
            var $siteName = $("#<%=SiteName.ClientID%>");
            $siteName.on("change", function () {
                $("#<%=ShowName.ClientID%>").val($siteName.val());
            });
        });

    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div class="nav">
        <div class="nav">
            <span class="nav_title">应用商店管理</span>
            <span class="nav_desc">首页 &raquo; <a href="LinkInfoList.aspx">跳转链接管理</a> &raquo; 新增</span>
        </div>
    </div>
    <div id="container">
        <table id="main_form">
            <tr>
                <td class="form_text">站点名称</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" txt_name="站点名称" ID="SiteName" />
                </td>
            </tr>

            <tr>
                <td class="form_text">显示名称</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="ShowName" txt_name="显示名称" />
                </td>
            </tr>
            <tr>
                <td class="form_text">开发者</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="DevName" ReadOnly="true" txt_name="开发者" />&nbsp;<a href="javascript:FindBack()">查找</a><asp:HiddenField runat="server" ID="DevID" /><asp:HiddenField runat="server" ID="CPName" />
                </td>
            </tr>


            <tr>
                <td class="form_text">实际URL地址</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="LinkUrl" txt_name="实际URL地址" />
                </td>
            </tr>
            <!--
            <tr style="display:none;">
                <td class="form_text">跳转URL地址</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="JumpUrl" txt_name="跳转URL地址" />
                </td>
            </tr>
            -->
            <tr>
                <td class="form_text">合作类型</td>
                <td>
                    <asp:DropDownList ID="CoopType" runat="server" Width="132">
                        <asp:ListItem Value="1">CPC</asp:ListItem>
                        <asp:ListItem Value="2">CPL</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td class="form_text">外链标签</td>
                <td>
                    <asp:DropDownList ID="LinkTag" runat="server" Width="132" CssClass="required" txt_name="外链标签">
                        <asp:ListItem Value="1">独家</asp:ListItem>
                        <asp:ListItem Value="2">首发</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td class="form_text">外链描述</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="LinkDesc" txt_name="外链描述" /></td>
            </tr>


            <tr>
                <td class="form_text">状态</td>
                <td>
                    <asp:DropDownList ID="Status" runat="server" Width="132">
                        <asp:ListItem Value="1" Selected="True">启用</asp:ListItem>
                        <asp:ListItem Value="2">禁用</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td class="form_text">备注</td>
                <td>
                    <asp:TextBox runat="server" CssClass="form_inputbox" ID="Remarks" /></td>
            </tr>

            <tr>
                <td class="form_text">上传Icon</td>
                <td>
                    <asp:HiddenField ID="IconPicUrl" runat="server" />
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderIconPicUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=IconPicUrl&token=123456&thumb=false&url=<%=this.UploadUrl %>" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=IconPicUrl&token=123456&thumb=false&url=<%=this.UploadUrl %>&appid=2&subid=62&extension=,jpg,png,gif," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                </td>
            </tr>
            <tr>
                <td class="form_text">Icon展示</td>
                <td>
                    <img src="Theme/Images/empty.png" width="60" height="60" id="ShowIconPic" style="border: 1px dotted #b8d0d6;"  />
                </td>
            </tr>

            <tr style="display:none;">
                <td class="form_text">上传缩略图</td>
                <td>
                    <asp:HiddenField ID="ThumbPicUrl" runat="server" />
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderThumbPicUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=ThumbPicUrl&token=123456&thumb=true&url=<%=this.UploadUrl %>&appid=2&subid=61&extension=,jpg,png,gif," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                </td>
            </tr>
            <tr style="display:none;">
                <td class="form_text">缩略图展示</td>
                <td>
                    <img src="Theme/Images/empty.png" id="ShowThumbPic" style="border: 1px dotted #b8d0d6;" runat="server"/>
                </td>
            </tr>



            <tr>
                <td></td>
                <td>
                    <asp:Button ID="btnSave" runat="server" Text="保存" CssClass="btn check_form" OnClick="btnSave_Click" />
                    <span id="err_msg"></span>
                </td>
            </tr>
        </table>

        <iframe id="ifrmModify" title="查找带回" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_DevInfoList.aspx"></iframe>
    </div>
</asp:Content>
