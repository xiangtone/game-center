<%@ Page Title="" Language="C#" MasterPageFile="~/CommonMaster.Master" AutoEventWireup="true" CodeBehind="PackEdit.aspx.cs" Inherits="updatesys_cms.Web.PackEdit" %>
<asp:Content ID="Content1" ContentPlaceHolderID="Header" runat="server">
    <style type="text/css">
    .litinput
    {
        width: 100px;
    }
    .midinput
    {
        width: 200px;
    }
    .biginput
    {
        width: 300px;
    }
</style>
<script type="text/javascript">
    function upload_suc(id, filename, fileSize, hash) {
        //alert(filename);
        //alert(hash);
        $("#<%=txtPackUrl.ClientID %>").val(filename);
        $("#<%=txtPackMD5.ClientID %>").val(hash);
        $("#<%=txtPackSize.ClientID %>").val(fileSize);
    }
    function upload_fail(id, msg) {
        alert("upload fail:" + filename);
    }
    function unpack_suc(id, versioncode, versionname, packname, iconurl) {
        //alert("unpack: " + versioncode + "," + versionname + "," + packname + "," + iconurl);
        $("#<%=txtVerCode.ClientID %>").val(versioncode);
        $("#<%=txtVerName.ClientID %>").val(versionname);
        $("#<%=txtPackName.ClientID %>").val(packname);
        var versionNameArray = versionname.split(".");
        var channelNo = versionNameArray[versionNameArray.length - 1];
        $("#<%=txtChannelNo.ClientID %>").val(channelNo);
    }
</script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="Body" runat="server">
    <div>
    <table cellspacing="0px" class="form">
        <tr runat="server" id="trModuleId">
            <td class="tit">应用</td>
            <td>
                <asp:DropDownList runat="server" ID="ddlAppList"></asp:DropDownList>
            </td>
        </tr>

        <tr runat="server" id="tr12">
            <td class="tit">方案</td>
            <td>
                <asp:DropDownList runat="server" ID="ddlSchemeId">
                    <asp:ListItem Value="0">通用方案</asp:ListItem>
                    <asp:ListItem Value="1">测试方案</asp:ListItem>
                </asp:DropDownList>
            </td>
        </tr>
        <tr runat="server" id="tr11">
            <td class="tit">上传安装包</td>
            <td>
            <object type="application/x-shockwave-flash" data="ResourceUpload.swf"  id="uploadifyUploader" style="visibility: visible;" height="80px" width="300px">
            <param name="quality" value="high" />
            <param name="wmode" value="opaque" />
            <param name="flashVars" value="config=configuration.xml&appid=8&subid=0&ctrlid=1&thumb=false&url=http://cms.huashenggame.com/upload/UploadFile.asmx?wsdl&extension=,rar,zip,apk," />
            <param name="allowScriptAccess" value="sameDomain" />
            </object>
            </td>
        </tr>
        <tr runat="server" id="tr1">
            <td class="tit">渠道号</td>
            <td>
                <asp:TextBox runat="server" ID="txtChannelNo" CssClass="litinput"></asp:TextBox>
            </td>
        </tr>
        <tr runat="server" id="tr3">
            <td class="tit">包名</td>
            <td>
                <asp:TextBox runat="server" ID="txtPackName" CssClass="biginput"></asp:TextBox>
            </td>
        </tr>
        <tr runat="server" id="tr2">
            <td class="tit">版本号</td>
            <td>
                <asp:TextBox runat="server" ID="txtVerName" CssClass="litinput"></asp:TextBox>
            </td>
        </tr>
        <tr runat="server" id="tr4">
            <td class="tit">版本代码</td>
            <td>
                <asp:TextBox runat="server" ID="txtVerCode" CssClass="litinput"></asp:TextBox>
            </td>
        </tr>
        <tr runat="server" id="tr5">
            <td class="tit">包地址</td>
            <td>
                <asp:TextBox runat="server" ID="txtPackUrl" CssClass="biginput"></asp:TextBox>
            </td>
        </tr>
        <tr runat="server" id="tr6">
            <td class="tit">包大小</td>
            <td>
                <asp:TextBox runat="server" ID="txtPackSize" CssClass="litinput"></asp:TextBox>&nbsp;Bytes
            </td>
        </tr>
        <tr runat="server" id="tr7">
            <td class="tit">包MD5值</td>
            <td>
                <asp:TextBox runat="server" ID="txtPackMD5" CssClass="biginput"></asp:TextBox>
            </td>
        </tr>
        <tr runat="server" id="tr8">
            <td class="tit">更新类型</td>
            <td>
                <asp:DropDownList runat="server" ID="ddlUpdateType" AutoPostBack="true" class="mustval">
                    <asp:ListItem Value="1">提示升级</asp:ListItem>
                    <asp:ListItem Value="2">标识-小红点</asp:ListItem>
                    <asp:ListItem Value="3">强制升级</asp:ListItem>
                </asp:DropDownList>
            </td>
        </tr>
        <tr runat="server" id="tr13">
            <td class="tit">强制更新的版本代码</td>
            <td>
                <asp:TextBox runat="server" ID="txtForceUpdateVerCode" CssClass="litinput"></asp:TextBox>
                <span style="color:black">&nbsp;低于此版本码强制更新，大于0才生效</span>
            </td>
        </tr>
        <tr runat="server" id="tr9">
            <td class="tit">更新提示语</td>
            <td>
                <asp:TextBox runat="server" ID="txtUpdatePrompt" CssClass="biginput" TextMode="MultiLine" Rows="5" Columns="15"></asp:TextBox>
            </td>
        </tr>
        <tr runat="server" id="tr10">
            <td class="tit">更新描述语</td>
            <td>
                <asp:TextBox runat="server" ID="txtUpdateDesc" CssClass="biginput" TextMode="MultiLine" Rows="5" Columns="15"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td class="tit">状态</td>
            <td>
                <asp:DropDownList runat="server" ID="ddlStatus" AutoPostBack="true" class="mustval">
                    <asp:ListItem Value="1">启用</asp:ListItem>
                    <asp:ListItem Value="2">禁用</asp:ListItem>
                </asp:DropDownList></td>
        </tr>
        <tr>
            <td class="tit"></td>
            <td>
                <input id="btnSave" type="button" value="保存" runat="server" onserverclick="Save_Click"  /><br />
                <asp:Label ID="lblPrompt" runat="server" Text="" CssClass="succlbl"></asp:Label></td>
        </tr>
</table>
</div>

</asp:Content>
