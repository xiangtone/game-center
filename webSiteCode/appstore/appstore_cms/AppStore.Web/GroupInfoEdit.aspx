<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="GroupInfoEdit.aspx.cs" Inherits="AppStore.Web.GroupInfoEdit" %>

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


       var FindBack = function () {
           var randomnumber = Math.floor(Math.random() * 100000);
           $("#ifrmModify").attr("src", "FindBack/F_DevInfoList.aspx?randomnumber=" + randomnumber);
           DlgIfrm("ifrmModify");

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

    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div class="nav">
        <span class="nav_title">应用中心管理</span> <span class="nav_desc">分组管理 &raquo; <a href="GroupElement.aspx?GroupID=<%=this.GroupID %>"><%=this.GroupName.Text %></a> &raquo;<%=this.NavShowStatus%> </span>
    </div>
    <div id="container">
        <table id="main_form">
            <tr>
                <td class="form_text">分组名称</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" txt_name="分组名称" ID="GroupName" />
                </td>
            </tr>
            <tr>
                <td class="form_text">排序类型</td>
                <td>
                    <asp:DropDownList ID="dropOrderType" runat="server" Width="132">
                    <asp:ListItem Value="0">默认</asp:ListItem>
                    <asp:ListItem Value="2">时间</asp:ListItem>
                    </asp:DropDownList>
                </td>
            </tr>
             <tr>
                <td class="form_text">分组类型</td>
                <td>
                    <asp:DropDownList ID="dropGroupType" runat="server" Width="132" CssClass="required" valid_type="list" txt_name="分组类型"></asp:DropDownList>
                </td>
            </tr>
            <tr>
                <td class="form_text">推荐语</td>
                <td>
                    <asp:TextBox runat="server" CssClass="form_inputbox" ID="RecommWord" /></td>
            </tr>
            <tr>
                <td class="form_text">状态</td>
                <td>
                    <asp:DropDownList ID="Status" runat="server" Width="132">
                        <asp:ListItem Value="1">启用</asp:ListItem>
                        <asp:ListItem Value="0">禁用</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td class="form_text">备注</td>
                <td>
                    <asp:TextBox runat="server" CssClass="form_inputbox" ID="Remarks" /></td>
            </tr>
             <tr>
                <td class="form_text">开始时间</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="StartTime" onclick="OnClickTime(this);" txt_name="开始时间" />
                </td>
            </tr>
            <tr>
                <td class="form_text">结束时间</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="EndTime" onclick="OnClickTime(this);" txt_name="结束时间" />
                </td>
            </tr>
            <tr>
                <td class="form_text">分组描述</td>
                <td>
                    <asp:TextBox ID="GroupDesc" TextMode="MultiLine" runat="server" CssClass="multi_text" Width="325" /></td>
            </tr>
          <tr>
                <td class="form_text">上传缩略图</td>
                <td>
                    <asp:HiddenField ID="ThumbPicUrl" runat="server" />
                    <asp:HiddenField ID="OldThumbPicUrl" runat="server" />
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderThumbPicUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=ThumbPicUrl&token=123456&thumb=true&url=<%=this.UploadUrl %>&appid=2&subid=41&extension=,jpg,png,gif,apk," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                </td>
            </tr>
            <tr>
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
                    <asp:Button ID="btnSave" runat="server" Text="保存" CssClass="btn check_form" OnClick="btnSave_Click" />
                    <span id="err_msg"></span>
                </td>
            </tr>
           
        </table>
        <iframe id="ifrmModify" title="查找带回" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_DevInfoList.aspx"></iframe>
        <asp:HiddenField ID="MainPackName" runat="server" />
    </div>
</asp:Content>
