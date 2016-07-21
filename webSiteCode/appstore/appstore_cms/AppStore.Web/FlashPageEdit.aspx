<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="FlashPageEdit.aspx.cs" Inherits="AppStore.Web.FlashPageEdit" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
<style type="text/css">
    body,div,table,tr,td,p
    {
        margin:0;
        padding:0;
        border:0;
    }
    #main_form{ margin-left: 50px; margin-top:25px; }
    #main_form tr{}
    #main_form tr td{ padding-bottom: 10px; padding-right: 10px; vertical-align:top;}
    #main_form td.form_text{text-align:right;line-height:1.5em;}
    #main_form td .multi_text {width: 250px; height:100px;}
    #main_form td .form_inputbox{ height: 20px;}
    .thumbPic {border: 1px dotted #b8d0d6; max-width:900px; max-height:500px;}
</style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
<script type="text/javascript">
    function OnClickTime(id) {
        WdatePicker({ el: id, dateFmt: 'yyyy-MM-dd HH:mm' });
    }
    //上传成功
    function upload_suc(id, filename, filesize, hash) {
        if (id == "ThumbPicUrl") {
            //imgArray.push(filename);
            $("#<%=ThumbPicUrl.ClientID %>").val(filename);
            $("#<%=ShowThumbPic.ClientID %>").attr("src", filename).Jcrop({
                aspectRatio: 4 / 3,
                onChange: showCoords,
                onSelect: showCoords,
                onRelease: clearCoords
            });
        }
    }

    //上传失败
    function upload_fail(id, msg) {
        alert("upload fail:" + filename);
    }
</script>
<div id="background" class="background" style="display: none;">
</div>
<div id="progressBar" class="progressBar" style="display: none;">
    数据处理中，请稍等...W
</div>
<div class="nav">
     <%-- <%--<span class="nav_title">游戏中心管理</span> --%> --%> <a href="FlashPageList.aspx"><span class="nav_desc">闪屏界面管理</span></a>&raquo; 新增
</div>
<div id="container">
    <table id="main_form">
        <tr><td class="form_text">闪屏说明</td><td><asp:TextBox ID="txtDesc" runat="server" CssClass="required" txt_name="闪屏说明" /></td></tr>
        <tr><td class="form_text">开始时间</td><td><asp:TextBox ID="txtStartTime" runat="server" CssClass="form_inputbox required" onclick="OnClickTime(this);" txt_name="开始时间"   /></td></tr>
        <tr><td class="form_text">结束时间</td><td><asp:TextBox ID="txtEndTime" runat="server" CssClass="form_inputbox required" onclick="OnClickTime(this);" txt_name="结束时间" /></td></tr>
        <tr><td class="form_text">状态</td><td><asp:DropDownList ID="ddlStatus" runat="server" CssClass="required" txt_name="状态" valid_type="list"><asp:ListItem Value="1">开启</asp:ListItem><asp:ListItem Value="0">禁用</asp:ListItem></asp:DropDownList></td></tr>
        <tr><td class="form_text">上传闪屏图</td>
            <td>
                <input type="hidden" runat="server" id="ThumbPicUrl" class="required" txt_name="闪屏图" />
                <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderThumbPicUrl" style="visibility: visible; height: 80px; min-width:300px;">
                    <param name="quality" value="high" />
                    <param name="wmode" value="opaque" />
                    <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=ThumbPicUrl&token=123456&thumb=false&url=<%=this.UploadUrl %>&appid=2&subid=41&extension=,jpg,png,gif," />
                    <param name="allowScriptAccess" value="sameDomain" />
                </object>
            </td>
        </tr>
        <tr><td class="form_text">闪屏图预览</td>
            <td>
                <asp:Image runat="server" ID="ShowThumbPic" CssClass="thumbPic" ImageUrl="Theme/Images/empty.png" />
            </td>
        </tr>
        <tr><td></td><td><asp:Button ID="Button1" runat="server" Text="保存" CssClass="check_form" OnClick="OnSave" /><span id="err_msg" ></span></td></tr>
    </table>
</div>
</asp:Content>
