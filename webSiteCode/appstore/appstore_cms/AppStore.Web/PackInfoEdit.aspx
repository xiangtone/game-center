<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" ValidateRequest="false" CodeBehind="PackInfoEdit.aspx.cs" Inherits="AppStore.Web.PackInfoEdit" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">

    <script type="text/javascript">


        $(function () {

<%--            $("#<%=btnSave.ClientID %>").click(function () {
                $(this).attr('disabled', true);
                setTimeout(function () {
                    alert()
                    $("#<%=btnSave.ClientID %>").attr('disabled', false);
                 }, 3000);
          })--%>
        });
    var imgArray = new Array();

    //上传成功
    function upload_suc(id, filename, filesize, hash) {

        if (id == "AppPicUrl") {
            // 是否续传
            //if (imgArray.length == 0){
            //    // 初始化
            //    $("input[name='AppPicUrls']").each(function () {
            //        imgArray.push(this.value);
            //    });
            //}
            imgArray.push(filename);
        } else if (id == "IconPicUrl") {
            $("#ShowIcon").attr("src", filename);
            $("#IconPicUrl").val(filename);
        }
        else {
            $("#PackUrl").val(filename);
            $("#PackSize").val(filesize);
            $("#PackMD5").val(hash);

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

    //解包成功
    function unpack_suc(id, verCode, verName, packName, icolUrl, signCode, permission) {
        // document.getElementById().value = verCode;
        $("#VerCode").val(verCode);
        $("#VerName").val(verName);
        $("#PackName").val(packName);
        $("#ShowIcon").attr("src", icolUrl);
        $("#IconPicUrl").val(icolUrl);
        $("#permission").val(permission);

        if (icolUrl == null || icolUrl == "") {
            $("#ShowUploadIcon").show();
            alert("ICON解析失败，请手动上传");
        }
    }
    //解包失败
    function unpack_fail(id, msg) {
        alert(id + ":Error " + msg);
    }


    </script>

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

        #ShowAppPicUrl img {
            width: 50%;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div class="nav">
        <span class="nav_title">应用中心管理</span> <span class="nav_desc">首页 &raquo; <%if (this.Type == "game")
                                                                                  {%> <a href="GameInfoList.aspx">游戏管理</a> <%}
                                                                                  else
                                                                                  { %><a href="AppInfoList.aspx"> 应用管理</a> <% } %> &raquo; <a href="PackInfoList.aspx?AppID=<%=this.AppID %>&ShowName=<%=this.ShowName %>&type=<%=this.Type %>"><%=this.ShowName %></a> &raquo; 新增安装包</span>
    </div>
    <div id="container">
        <table id="main_form">
            <tr>
                <td class="form_text" style="width: 10%;">下载次数</td>
                <td style="width: 90%;">
                    <input type="text" class="num form_inputbox" id="DownTimes" name="DownTimes" value="<%=this.CurrentEntity.DownTimes %>" /></td>
            </tr>
            <tr style="display: none;">
                <!--新数据结构中不存在此字段  2004-10-27 momo-->
                <td class="form_text">分发标识</td>
                <td>
                    <input type="text" class="form_inputbox" id="IssueFlag" name="IssueFlag" value="<%=this.CurrentEntity.IssueFlag %>" /></td>
            </tr>
            <tr>
                <td class="form_text">签名特征码</td>
                <td>
                    <input type="text" class="form_inputbox" id="SignCode" name="SignCode" value="<%=this.CurrentEntity.PackSign %>" /></td>
            </tr>
            <tr>
                <td class="form_text">适用性说明</td>
                <td>
                    <input type="text" class="form_inputbox" id="CompDesc" name="CompDesc" value="<%=this.CurrentEntity.CompDesc %>" /></td>
            </tr>
            <tr>
                <td class="form_text">来源</td>
                <td>
                    <asp:DropDownList ID="PackForm" runat="server" Width="132">
                        <asp:ListItem Value="0">其他</asp:ListItem>
                        <asp:ListItem Value="1">直接合作</asp:ListItem>
                        <asp:ListItem Value="2">自由市场</asp:ListItem>
                        <asp:ListItem Value="3">豌豆荚</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td class="form_text">合作类型</td>
                <td>
                    <asp:DropDownList ID="CoopType" runat="server" Width="132">
                        <asp:ListItem Value="99">未合作</asp:ListItem>
                        <asp:ListItem Value="1">联运</asp:ListItem>
                        <asp:ListItem Value="2">CPS</asp:ListItem>
                        <asp:ListItem Value="3">CPA</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>

            <tr>
                <td class="form_text">是否主版本</td>
                <td>
                    <asp:DropDownList ID="IsMainVer" runat="server" Width="132">
                        <asp:ListItem Value="1">是</asp:ListItem>
                        <asp:ListItem Value="0">否</asp:ListItem>
                    </asp:DropDownList></td>
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
                    <input type="text" class="form_inputbox" id="Remarks" name="Remarks" value="<%=this.CurrentEntity.Remarks %>" /></td>
            </tr>

            <tr>
                <td class="form_text">包名</td>
                <td>
                    <input type="text" class="required form_inputbox" txt_name="包名" id="PackName" name="PackName" readonly="readonly" value="<%=this.CurrentEntity.PackName %>" />
                </td>
            </tr>

            <tr>
                <td class="form_text">版本代码</td>
                <td>
                    <input type="text" class="required form_inputbox" id="VerCode" name="VerCode" txt_name="版本代码" readonly="readonly" value="<%=this.CurrentEntity.VerCode %>" />
                </td>
            </tr>
            <tr>
                <td class="form_text">版本号</td>
                <td>
                    <input type="text" class="required form_inputbox" id="VerName" name="VerName" txt_name="版本号" readonly="readonly" value="<%=this.CurrentEntity.VerName %>" />&nbsp;</td>
            </tr>
            <tr>
                <td class="form_text">MD校验</td>
                <td>
                    <input type="text" class="form_inputbox" id="PackMD5" readonly="readonly" name="PackMD5" value="<%=this.CurrentEntity.PackMD5 %>" /></td>
            </tr>
            <tr>
                <td class="form_text">文件大小</td>
                <td>
                    <input type="text" class="num form_inputbox" id="PackSize" readonly="readonly" name="PackSize" value="<%=this.CurrentEntity.PackSize %>" /></td>
            </tr>
            <tr>
                <td class="form_text">更新说明</td>
                <td>
                    <textarea rows="5" cols="10" id="UpDateDesc" name="UpDateDesc" class="multi_text" width="325"><%=this.CurrentEntity.UpdateDesc %></textarea>
                </td>
            </tr>
            <tr>
                <td class="form_text">上传安装包</td>
                <td>
                    <input type="hidden" id="PackUrl" name="PackUrl" value="<%=this.CurrentEntity.PackUrl %>" />
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderPackUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=PackUrl&token=123456&thumb=false&url=<%=this.UploadUrl %>&appid=2&subid=72&extension=,apk," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                    <input type="hidden" id="permission" name="permission" value="<%=this.CurrentEntity.permission %>" />

                </td>
            </tr>
            <tr class="picshow" id="ShowUploadIcon" style="display: none;">
                <td class="form_text">上传ICON</td>
                <td>
                    <object type="application/x-shockwave-flash" data="uploadify/ResourceUpload.swf" id="Object1" style="visibility: visible; min-width: 400px; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=IconPicUrl&token=123456&thumb=false&url=<%=this.UploadUrl %>&appid=2&subid=71&extension=,jpg,png,gif," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                </td>
            </tr>
            <tr>
                <td class="form_text">上传Icon</td>
                <td>

                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderAppIconPicUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=IconPicUrl&appid=2&subid=21&token=123456&thumb=false&url=<%=this.UploadUrl %>&extension=,jpg,JPG,png,PNG,gif,GIF," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                </td>
            </tr>
            <tr>
                <td class="form_text">Icon展示</td>
                <td>
                    <input id="IconPicUrl" type="hidden" name="IconPicUrl" value="<%=this.CurrentEntity.IconUrl %>" />
                    <% if (string.IsNullOrEmpty(this.CurrentEntity.IconUrl))
                       {
                    %>
                    <img id="ShowIcon" src="Theme/Images/empty.png" width="60" height="60" style="border: 1px dotted #b8d0d6;" />
                    <%
                       }
                       else
                       {
                    %>
                    <img id="ShowIcon" src="<%=this.CurrentEntity.IconUrl %>" width="60" height="60" style="border: 1px dotted #b8d0d6;" />
                    <%
                       } %>
                   
                </td>
            </tr>
            <tr>
                <td class="form_text">上传截图</td>
                <td>
                    <input type="hidden" id="AppPicUrl" name="AppPicUrl" />
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderAppPicUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=AppPicUrl&appid=2&subid=21&token=123456&thumb=false&url=<%=this.UploadUrl %>&extension=,jpg,JPG,png,PNG,gif,GIF," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>

                    <%--新数据结构中不存在此字段  2004-10-27 momo<%
                        if (this.AppPicList != null || this.AppPicList.Count != 0)
                        {
                            foreach (AppStore.Model.AppPicListEntity item in this.AppPicList)
                            {      
                           
                    %>--%>
                    <%
                        if (this.CurrentEntity.AppPicUrl != null || this.CurrentEntity.AppPicUrl != "")
                        {
                            var item = this.CurrentEntity.AppPicUrl.Split(',');
                            for (int i = 0; i < item.Length; i++)
                            {
                    %>
                    <input type="hidden" name="OldAppPicID" value="<%=item[i]%>" />
                    <%
                            }
                        }
                        
                    %>
                </td>
            </tr>
            <tr id="ShowAppPicUrl">
                <td class="form_text">截图展示</td>
                <td>

                    <%
                        if (this.CurrentEntity.AppPicUrl == null || this.CurrentEntity.AppPicUrl == "")
                        {
                    %>
                    <img src="Theme/Images/empty.png" style="border: 1px dotted #b8d0d6;" />
                    <%
                        }
                        else
                        {
                            var list = this.CurrentEntity.AppPicUrl.Split(',');
                            for (int j = 0; j < list.Length; j++)
                            {
                    %>
                    <img src="<%=list[j] %>" style="border: 1px dotted #b8d0d6;" />
                    <input type="hidden" name="AppPicUrls" value="<%=list[j]%>" />
                    <%
                            }
                        }
                    %>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <asp:Button ID="btnSave" runat="server" Text="保存" CssClass="btn check_form" OnClick="btnSave_Click"  />
                    <span id="err_msg"></span>
                </td>
            </tr>
        </table>
    </div>
</asp:Content>
