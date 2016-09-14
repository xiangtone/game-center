<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="IosAppInfoEdit.aspx.cs" Inherits="AppStore.Web.IosAppInfoEdit" %>

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

            if (id == "IconUrl") {
                $("#ShowIconPic").attr("src", filename);
                $("#IconUrl").val(filename);
                $("#cropType").show();
            } else if (id == "AppPicUrl") {
                imgArray.push(filename);
                $("#ShowAppPicUrl").attr("src", filename).Jcrop({
                    aspectRatio: 4 / 3,
                    onChange: showCoords,
                    onSelect: showCoords,
                    onRelease: clearCoords
                });
                $("#cropType").show();
            } else if (id == "AdsPicUrl") {
                $("#<%=AdsPicUrl.ClientID %>").val(filename);
                $("#ShowAdsPic").attr("src", filename);
            }else if (id == "ThumbPicUrl") {
                $("#<%=ThumbPicUrl.ClientID%>").val(filename)
                $("#ShowThumbPic").attr("src", filename);
            }else {
                return;
            }
        }

        //上传队列结束
        function upload_end(id) {
            if (id == "AppPicUrl") {
                $("#AppPicUrl").val(imgArray.toString());

                var html = "<td class='form_text'>截图展示</td>";
                html += "<td>";
                for (var i = 0; i < imgArray.length; i++) {
                    html += "<img src='" + imgArray[i] + "' width='220' style='border: none; margin-right: 3px;' />";
                    html += "<input type='hidden' name='AppPicUrls' value='" + imgArray[i] + "' />";
                }

                html += "</td>";
                $("#ShowAppPicUrl").html("").html(html);

                imgArray.splice(0, imgArray.length);
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
                $("#IconUrl").Jcrop({
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
        <span class="nav_title">应用中心管理</span> <span class="nav_desc">首页 &raquo;
        <a href="IosAppInfoList.aspx">IOS游戏管理</a> &raquo; 修改</span>
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
                <td class="form_text">开发者名称</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="DevName" txt_name="开发者" />
                </td>
            </tr>
             <tr>
                <td class="form_text">应用类型</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="AppType" txt_name="应用类型" Width="200px" />
                </td>
            </tr>

            <tr>
                <td class="form_text">应用大小</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="AppSize" txt_name="应用大小" />
                </td>
            </tr>

             <tr>
                <td class="form_text">应用版本</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="AppVersion" txt_name="应用版本" />
                </td>
            </tr>

            <tr>
                <td class="form_text">应用价格</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="AppPrice" txt_name="应用价格" />
                </td>
            </tr>

            <tr>
                <td class="form_text">应用状态</td>
                <td>
                    <asp:DropDownList ID="Status" runat="server">
                        <asp:ListItem Value="1" Selected="True">启用</asp:ListItem>
                        <asp:ListItem Value="0">禁用</asp:ListItem>
                    </asp:DropDownList>
                </td>
            </tr>
              <tr>
                <td class="form_text">推荐标示语</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="RecommFlagWord" txt_name="推荐标示语" Width="200px"/>
                </td>
            </tr>

            <tr>
                <td class="form_text">推荐语</td>
                <td><asp:TextBox ID="RecommWord" runat="server" TextMode="MultiLine" runat="server" CssClass="multi_text" Width="200" ></asp:TextBox>
                    &nbsp;</td>
            </tr>

            <tr>

            <td class="form_text">备注</td>

            <td>
                    <asp:TextBox ID="Remarks" TextMode="MultiLine" runat="server" CssClass="multi_text" Width="256" /></td>
            </tr>
            <tr>
                <td class="form_text">游戏描述</td>
                <td>
                    <asp:TextBox ID="AppDesc" TextMode="MultiLine" runat="server" CssClass="multi_text" Width="325" /></td>
            </tr>
            <tr>
                <td class="form_text">游戏URL地址</td>
                <td>
                    <asp:TextBox ID="AppUrl" TextMode="MultiLine" runat="server" CssClass="multi_text" Width="325" /></td>
            </tr>

                        <tr>
                <td class="form_text">上传缩略图</td>
                <td>
                    <asp:HiddenField ID="ThumbPicUrl" runat="server"/>
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploadThumbPicUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=ThumbPicUrl&token=123456&thumb=false&url=<%=this.UploadUrl %>&appid=2&subid=11&extension=,jpg,png,gif," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                </td>
            </tr>
            <tr >
                <td class="form_text">缩略图展示</td>
                <td>
                    <asp:Image ID="ShowThumbPic" ClientIDMode="Static" CssClass="thumbPic" runat="server"/>
                </td>
            </tr>
             <tr>
                <td class="form_text">上传广告图</td>
                <td>
                    <asp:HiddenField ID="AdsPicUrl" runat="server" />
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderAdvUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=AdsPicUrl&token=123456&thumb=false&url=<%=this.UploadUrl %>&appid=2&subid=21&extension=,jpg,png,gif" />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                </td>
            </tr>
                <tr>
                <td class="form_text">广告图展示</td>
                <td>
                    <asp:Image ID="ShowAdsPic" ClientIDMode="Static" CssClass="thumbPic" runat="server"/>
                </td>
            </tr>
            <tr>
                <td class="form_text">上传ICON</td>
                <td>
                    <asp:HiddenField ID="IconUrl" runat="server" />
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderIconUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=IconUrl&token=123456&thumb=true&url=<%=this.UploadUrl %>&appid=2&subid=11&extension=,jpg,png,gif" />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                </td>
            </tr>
            <tr>
                <td class="form_text">ICON展示</td>
                <td>
                    <asp:Image ID="ShowIconPic" ClientIDMode="Static" CssClass="thumbPic" runat="server"  Height="60px" Width="60px"/>
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
                <td class="form_text">上传截图</td>
                <td>
                    <input type="hidden" id="AppPicUrl" name="AppPicUrl" value="<% =AppPicUrl %>" />
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderAppPicUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=AppPicUrl&appid=2&subid=21&token=123456&thumb=false&url=<%=this.UploadUrl %>&extension=,jpg,JPG,png,PNG,gif,GIF," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>

                </td>
            </tr>
            <tr id="ShowAppPicUrl">
                <td class="form_text">截图展示</td>
                <td>
                    <%if (AppPicUrl.Length>0)
                      {
                          if (AppPicUrl.IndexOf(',') > 0)
                          { 
                              string[] appPicUrl = AppPicUrl.Split(',');
                          
                                  foreach (var item in appPicUrl)
                                  {%>
                                      <img src="<%=item%>" style="border: 1px dotted #b8d0d6;"/>
                                  <%} 
                          }
                          else{%>
                                  <img src="<%=AppPicUrl%>" style="border: 1px dotted #b8d0d6;"/>
                           <%}
                      }
                      else
                      {%>
                           <img src="Theme/Images/empty.png" style="border: 1px dotted #b8d0d6;" />
                      <% } %>        
                </td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <asp:Button ID="Save_Btn1" runat="server" Text="保存" CssClass="btn check_form" OnClick="btnSave_Click"  />
                    <span id="err_msg"></span>
                </td>
            </tr>
            </table>
        <iframe id="ifrmModify" title="查找带回" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_DevInfoList.aspx"></iframe>
    </div>
</asp:Content>
