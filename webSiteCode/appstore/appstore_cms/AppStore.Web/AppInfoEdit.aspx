<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="AppInfoEdit.aspx.cs" Inherits="AppStore.Web.AppInfoEdit" %>

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

        .thumbPic {
            border: 1px dotted #b8d0d6;
            max-width: 900px;
            max-height: 500px;
        }
    </style>

    <link href="Theme/css/jquery.Jcrop.css" rel="stylesheet" />
    <script src="Javascript/jquery.Jcrop.js" type="text/javascript"></script>
    <script type="text/javascript">
        var imgArray = new Array();

        var FindBack = function (type) {
            var randomnumber = Math.floor(Math.random() * 100000);

            if (type == "DevName") {
                $("#ifrmModify").attr("src", "FindBack/F_DevInfoList.aspx?randomnumber=" + randomnumber);

            } else if (type == "UAppID") {
                $("#ifrmModify").attr("src", "FindBack/F_UAppInfoList.aspx?randomnumber=" + randomnumber);
            }
            DlgIfrm("ifrmModify");
        }

        function GetShareData(args) {
            $("#<%=CPID.ClientID %>").val(args.DevID);
            $("#<%=DevID.ClientID%>").val(args.DevID);
            $("#<%=CPName.ClientID%>").val(args.DevName);
        }

        function GetUAppList(args) {
            $("#<%=hidUAppID.ClientID %>").val(args.AppID);
            $("#<%=UAppName.ClientID %>").val(args.AppName);
        }

        function showCoords(c) {
            $('#x1').val(c.x);
            $('#y1').val(c.y);
            $('#x2').val(c.x2);
            $('#y2').val(c.y2);
            $('#w').val(c.w);
            $('#h').val(c.h);

        };
        function clearCoords() {
            $('#coords input').val('');
        };
        //上传成功
        function upload_suc(id, filename, filesize, hash) {

            if (id == "ThumbPicUrl") {
                imgArray.push(filename);
                $("#<%=ThumbPicUrl.ClientID %>").val(filename);
                $("#ShowThumbPic").attr("src", filename).Jcrop({
                    aspectRatio: 4 / 3,
                    onChange: showCoords,
                    onSelect: showCoords,
                    onRelease: clearCoords
                });
                $("#cropType").show();
            } else {
                $("#PackUrl").val(filename);
                $("#PackDownLoadUrl").text("【 点击下载 】").attr("href", filename);
                $("#PackSize").val(filesize);
                $("#PackMD5").val(hash);
            }
        }

        //上传失败
        function upload_fail(id, msg) {
            alert("upload fail:" + filename);
        }

        $(function () {

            $("#hengping").click(function () {
                $("#ShowThumbPic").Jcrop({
                    aspectRatio: 4 / 3,
                    onChange: showCoords,
                    onSelect: showCoords,
                    onRelease: clearCoords
                });
            });

            $("#shuping").click(function () {
                $("#ShowThumbPic").Jcrop({
                    aspectRatio: 3 / 4,
                    onChange: showCoords,
                    onSelect: showCoords,
                    onRelease: clearCoords
                });
            });
        });
        function clientClick() {
            var checkbox = document.getElementById('<%=CheckBox1.ClientID %>');
            //是否被选中
            if (checkbox.checked) {
                if (!confirm('确定要随机生成应用评星吗？')) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div class="nav">
        <span class="nav_title">应用中心管理</span> <span class="nav_desc">首页 &raquo;
            <%if (AppClass == "12")
              { %>
            <a href="GameInfoList.aspx">游戏管理</a>
            <%}
              else
              {%>
            <a href="AppInfoList.aspx">应用管理</a>
            <%  } %>
            
             &raquo;
            <a href="PackInfoList.aspx?AppID=<%=this.AppID %>&ShowName=<%=this.ShowName.Text %>&type=app"><%=this.ShowName.Text %></a> &raquo; 修改</span>
    </div>
    <div id="container">
        <table id="main_form">
            <tr>
                <td class="form_text">应用名称</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" txt_name="应用名称" ID="AppName" />
                </td>
            </tr>

            <tr>
                <td class="form_text">显示名称</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="ShowName" txt_name="显示名称" />
                </td>
            </tr>
            <tr>
                <td class="form_text">适用设备类型</td>
                <td>
                    <asp:DropDownList ID="ForDeviceTypeList" runat="server" Width="132">
                        <asp:ListItem Value="4" Selected="True">所有设备</asp:ListItem>
                        <asp:ListItem Value="1">手机</asp:ListItem>
                        <asp:ListItem Value="2">平板</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td class="form_text">CPID</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="CPID" ReadOnly="true" txt_name="开发者" />&nbsp;<a href="javascript:FindBack('CPName')">查找</a><asp:HiddenField runat="server" ID="DevID" />
                </td>
            </tr>
            <tr style="display: none;">
                <td class="form_text">渠道</td>
                <td>

                    <asp:DropDownList ID="drpIssueType" runat="server" OnSelectedIndexChanged="drpIssueType_SelectedIndexChanged" Width="132" AutoPostBack="True">
                        <asp:ListItem Value="1">不分渠道</asp:ListItem>
                        <asp:ListItem Value="2">分渠道分发</asp:ListItem>
                    </asp:DropDownList>
                    <br />
                    <br />
                    <asp:CheckBoxList ID="cbChannel" runat="server" RepeatDirection="Horizontal" Visible="False">
                    </asp:CheckBoxList>
                </td>
            </tr>
            <tr>
                <td class="form_text">渠道适配性</td>
                <td>
                    <asp:CheckBoxList ID="cbChannel2" runat="server" RepeatDirection="Horizontal">
                    </asp:CheckBoxList>
                </td>
            </tr>

            <tr>
                <td class="form_text">架构适配</td>
                <td>
                    <asp:CheckBoxList ID="cbArchitecture" runat="server" RepeatDirection="Horizontal">
                        <asp:ListItem Value="1">arm</asp:ListItem>
                        <asp:ListItem Value="2">x86</asp:ListItem>
                    </asp:CheckBoxList>

                </td>
            </tr>
            <tr>
                <td class="form_text">应用标签</td>
                <td>
                    <asp:CheckBoxList ID="chbIsSafe" runat="server" RepeatDirection="Horizontal">
                        <asp:ListItem Value="1">安全</asp:ListItem>
                        <asp:ListItem Value="2">无广告</asp:ListItem>
                        <asp:ListItem Value="4">道具收费</asp:ListItem>
                    </asp:CheckBoxList>
                </td>
            </tr>
            <tr>
                <td class="form_text">开发者名称</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="CPName" txt_name="开发者" />
                </td>
            </tr>
            <tr>
                <td class="form_text">关键词</td>
                <td>
                    <asp:TextBox runat="server" CssClass="form_inputbox" ID="SearchKeys" /></td>
            </tr>
            <tr>
                <td class="form_text">下载次数</td>
                <td>
                    <asp:TextBox runat="server" CssClass="num form_inputbox" ID="DownTimes" />
                    <asp:CheckBox ID="CheckBox1" runat="server" />是否随机生成应用评星

                </td>
            </tr>
            <tr>
                <td class="form_text">推荐语</td>
                <td>
                    <asp:TextBox runat="server" CssClass="form_inputbox" ID="RecommWord" /></td>
            </tr>
            <tr>
                <td class="form_text">应用分类</td>
                <td>
                    <asp:DropDownList ID="AppType" runat="server" Width="132" CssClass="required" valid_type="list" txt_name="应用分类"></asp:DropDownList>
                    <asp:HiddenField ID="OldAppType" runat="server" />
                </td>
            </tr>
            <tr style="display: none;">
                <td class="form_text">联运游戏ID</td>
                <td>
                    <asp:TextBox ID="UAppName" runat="server"></asp:TextBox>
                    &nbsp;  <a href="javascript:FindBack('UAppID')">查找</a><asp:HiddenField runat="server" ID="hidUAppID" />
                    &nbsp;<span style="color: #f00">联运游戏必填！非联运可忽略此项</span>
                </td>
            </tr>
            <tr>
                <td class="form_text">黄暴等级</td>
                <td>
                    <asp:DropDownList ID="EvilLevel" runat="server" Width="132">
                        <asp:ListItem Value="0">0</asp:ListItem>
                        <asp:ListItem Value="1">1</asp:ListItem>
                        <asp:ListItem Value="2">2</asp:ListItem>
                        <asp:ListItem Value="3">3</asp:ListItem>
                        <asp:ListItem Value="4">4</asp:ListItem>
                        <asp:ListItem Value="5" Selected="True">5</asp:ListItem>
                        <asp:ListItem Value="6">6</asp:ListItem>
                        <asp:ListItem Value="7">7</asp:ListItem>
                        <asp:ListItem Value="8">8</asp:ListItem>
                        <asp:ListItem Value="9">9</asp:ListItem>
                        <asp:ListItem Value="10">10</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td class="form_text">推荐等级</td>
                <td>
                    <asp:DropDownList ID="RecommLevel" runat="server" Width="132">
                        <asp:ListItem Value="0">0</asp:ListItem>
                        <asp:ListItem Value="1">1</asp:ListItem>
                        <asp:ListItem Value="2">2</asp:ListItem>
                        <asp:ListItem Value="3">3</asp:ListItem>
                        <asp:ListItem Value="4">4</asp:ListItem>
                        <asp:ListItem Value="5" Selected="True">5</asp:ListItem>
                        <asp:ListItem Value="6">6</asp:ListItem>
                        <asp:ListItem Value="7">7</asp:ListItem>
                        <asp:ListItem Value="8">8</asp:ListItem>
                        <asp:ListItem Value="9">9</asp:ListItem>
                        <asp:ListItem Value="10">10</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr >
                <td class="form_text">推荐标识</td>
                <td>
                    <asp:CheckBoxList ID="cbRecommFlag" runat="server" RepeatDirection="Horizontal">
                        <asp:ListItem Value="1">官方</asp:ListItem>
                        <asp:ListItem Value="2">推荐</asp:ListItem>
                        <asp:ListItem Value="4">首发</asp:ListItem>
                        <asp:ListItem Value="8">免费</asp:ListItem>
                        <asp:ListItem Value="16">礼包</asp:ListItem>
                        <asp:ListItem Value="32">活动</asp:ListItem>
                        <asp:ListItem Value="64">内测</asp:ListItem>
                        <asp:ListItem Value="128">热门</asp:ListItem>
                    </asp:CheckBoxList>
                </td>
            </tr>


            <tr>
                <td class="form_text">是否网游</td>
                <td>
                    <asp:DropDownList ID="IsNetGame" runat="server" Width="132">
                        <asp:ListItem Value="1">是</asp:ListItem>
                        <asp:ListItem Value="2" Selected="True">否</asp:ListItem>
                    </asp:DropDownList></td>
                <asp:HiddenField ID="OldIsNetGame" runat="server" />
            </tr>

            <tr style="display: none">
                <!--新表结构中不存在此字段 2014-10-24 momo-->
                <td class="form_text">分发类型</td>
                <td>
                    <asp:DropDownList ID="IssueType" runat="server" Width="132">
                        <asp:ListItem Value="1" Selected="True">不分渠道</asp:ListItem>
                        <asp:ListItem Value="2">分渠道分发</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td class="form_text">状态</td>
                <td>
                    <asp:DropDownList ID="Status" runat="server" Width="132">
                        <asp:ListItem Value="1" Selected="True">启用</asp:ListItem>
                        <asp:ListItem Value="2">禁用</asp:ListItem>
                        <asp:ListItem Value="4">接入中</asp:ListItem>
                        <asp:ListItem Value="5">测试中</asp:ListItem>
                        <asp:ListItem Value="6">待审核</asp:ListItem>
                        <asp:ListItem Value="7">审核不通过</asp:ListItem>
                        <asp:ListItem Value="12">数据异常</asp:ListItem>
                        <asp:ListItem Value="22">控制异常</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td class="form_text">备注</td>
                <td>
                    <asp:TextBox runat="server" CssClass="form_inputbox" ID="Remarks" /></td>
            </tr>
            <tr>
                <td class="form_text">游戏描述</td>
                <td>
                    <asp:TextBox ID="AppDesc" TextMode="MultiLine" runat="server" CssClass="multi_text" Width="325" /></td>
            </tr>
            <tr style="display: none;">
                <td class="form_text">上传缩略图</td>
                <td>
                    <asp:HiddenField ID="ThumbPicUrl" runat="server" />
                    <asp:HiddenField ID="OldThumbPicUrl" runat="server" />
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderThumbPicUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=ThumbPicUrl&token=123456&thumb=true&url=<%=this.UploadUrl %>&appid=2&subid=11&extension=,jpg,png,gif,apk," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                </td>
            </tr>
            <tr style="display: none;">
                <td class="form_text">缩略图展示</td>
                <td>
                    <asp:Image ID="ShowThumbPic" ClientIDMode="Static" CssClass="thumbPic" runat="server" />
                </td>
            </tr>

            <tr id="cropType" style="display: none;">
                <td class="form_text">裁剪方式</td>
                <td style="line-height: 20px;">
                    <input type="radio" name="cropType" checked="checked" id="hengping" value="hengping" />横屏&nbsp;&nbsp;
                    <input type="radio" name="cropType" value="shuping" id="shuping" />竖屏

                    <input type="hidden" id="x1" name="x1" />
                    <input type="hidden" id="y1" name="y1" />
                    <input type="hidden" id="x2" name="x2" />
                    <input type="hidden" id="y2" name="y2" />
                    <input type="hidden" id="w" name="w" />
                    <input type="hidden" id="h" name="h" />

                </td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <asp:Button ID="btnSave" runat="server" Text="保存" CssClass="btn check_form" OnClick="btnSave_Click" OnClientClick="javascript:return clientClick();" />
                    <span id="err_msg"></span>
                </td>
            </tr>
        </table>
        <iframe id="ifrmModify" title="查找带回" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_DevInfoList.aspx"></iframe>
        <asp:HiddenField ID="MainPackName" runat="server" />
    </div>
</asp:Content>
