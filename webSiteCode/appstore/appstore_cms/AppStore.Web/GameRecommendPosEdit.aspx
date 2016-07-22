<%@ Page Language="C#" AutoEventWireup="true" MasterPageFile="~/MasterPage.Master"  CodeBehind="GameRecommendPosEdit.aspx.cs" Inherits="AppStore.Web.GameRecommendPosEdit" %>

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

        #main_form img {
            max-width: 900px;
            max-height: 500px;
        }
    </style>
    <script type="text/javascript">

        function OnClickTime(id) {
            WdatePicker({ el: id, dateFmt: 'yyyy-MM-dd HH:mm' });
        }

        var FindBack = function (type) {
            if (type == "game")
                DlgIfrm("ifrmModify");
            else if (type == "app")
                DlgIfrm("ifrmModify2");
            else if (type == "link")
                DlgIfrm("ifrmModify3");
            else if (type == "category")
                DlgIfrm("ifrmModify4");
            else if (type == "specialtopic")
                DlgIfrm("ifrmModify5");
            else if (type == "specialtopic2")
                DlgIfrm("ifrmModify6");

        }

        //查找应用，查找游戏
        function GetShareData(args) {
            $("#<%=ElemID.ClientID %>").val(args.AppID);
            $("#<%=txtShowName.ClientID %>").val(args.ShowName);
            $("#<%=RecommTitle.ClientID%>").val(args.ShowName);
            $("#<%=RecommTag.ClientID%>").val(args.RecommTag);
            $("#<%=MainIconPicUrl.ClientID%>").val(args.MainIconPicUrl);
            $("#<%=GroupType.ClientID%>").val(args.TypeID);

        }

        //查找链接
        function GetShareLinkInfo(args) {
            $("#<%=ElemID.ClientID %>").val(args.LinkID);
            $("#<%=txtShowName.ClientID %>").val(args.ShowName);
            $("#<%=RecommTitle.ClientID%>").val(args.ShowName);
            $("#<%=RecommTag.ClientID%>").val(args.RecommTag);
            $("#<%=MainIconPicUrl.ClientID%>").val(args.IconPicUrl);
        }

        //查找分类
        function GetShareCategory(args) {
            $("#<%=ElemID.ClientID %>").val("0");
            $("#<%=GroupType.ClientID%>").val(args.TypeID);
            $("#<%=txtShowName.ClientID %>").val(args.TypeName);
            $("#<%=RecommTitle.ClientID%>").val(args.TypeName);
            $("#<%=RecommTag.ClientID%>").val(args.RecommTag);
            $("#<%=MainIconPicUrl.ClientID%>").val(args.TypePicUrl);
        }

        //查找专题
        function GetShareSpecialTopic(args) {
            $("#<%=ElemID.ClientID %>").val(args.GroupID);
            $("#<%=GroupType.ClientID%>").val(args.GroupTypeID);
            $("#<%=txtShowName.ClientID %>").val(args.GroupName);
            $("#<%=RecommTitle.ClientID%>").val(args.GroupName);
            $("#<%=RecommTag.ClientID%>").val(args.RecommTag);
            $("#<%=MainIconPicUrl.ClientID%>").val(args.GroupPicUrl);
        }

        //上传成功
        function upload_suc(id, filename, filesize, hash) {

            if (id == "RecommPicUrl") {
                $("#<%=RecommPicUrl.ClientID %>").val(filename);
                $("#ShowRecommPicUrl").attr("src", filename);
            }

            if (filesize >= 102400) {
                alert("上传的文件大于或等于100KB，可能会影响客户端展示，是否重新上传？");
            }
        }

        //上传失败
        function upload_fail(id, msg) {
            alert("upload fail:" + filename);
        }

        $(function () {

            var $elemType = $("#<%=ElemType.ClientID%>");

            //选择元素类型
            $elemType.on("change", function () {

                $("#<%=txtShowName.ClientID%>").siblings().hide();
                $("#<%=ElemID.ClientID %>").val("");
                $("#<%=txtShowName.ClientID %>").val("");
                $("#<%=RecommTitle.ClientID%>").val("");
                $("#<%=RecommTag.ClientID%>").val("");
                $("#<%=MainIconPicUrl.ClientID%>").val("");
                $("#<%=GroupType.ClientID%>").val("");

                var $txtShowName = $("#<%=txtShowName.ClientID%>");
                switch ($(this).val()) {
                    case "1":
                        $("#find_game").show("fast");
                        $("#find_app").show("fast");
                        $txtShowName.show();
                        break;
                    case "2":
                        $("#find_link").show("fast");
                        $txtShowName.show();
                        break;
                    case "3":
                        $("#find_category").show("fast");
                        $txtShowName.show();
                        break;
                    case "4":
                        $("#<%=txtShowName.ClientID%>").hide().removeClass("required");
                        $("#onlinegames").show();
                        $("#singlegames").show();
                        $("#");
                        break;
                    case "5":
                        $("#find_specialtopic").show("fast");
                        $("#find_specialtopic2").show("fast");
                        $txtShowName.show();
                        break;
                }

            });

            switch ($elemType.val()) {
                case "1":
                    $("#find_game").show().siblings("a").hide();
                    $("#find_app").show();
                    break;
                case "2":
                    $("#find_link").show().siblings("a").hide();
                    break;
                case "3":
                    $("#find_category").show().siblings("a").hide();
                    break;
                case "4":
                    $("#<%=txtShowName.ClientID%>").hide();
                    $("#find_game").hide().siblings("a").hide();
                    $("#singlegames").show();
                    $("#onlinegames").show();
                    <% 
        if (this.Action == "Edit")
        {
            if (this.CurrentEntity.GroupType == 2101)
            {
                %>
                    $("#online_game").attr("checked", true);
                    <%
            }
            else
            {
                %>
                    $("#single_game").attr("checked", true);;
                    <%
            }
        }
                   %>
                    break;
                case "5":
                    $("#find_specialtopic").show().siblings("a").hide();
                    $("#find_specialtopic2").show();
                    break;
            }
            if ($("#SchemeID").attr("data-value") == "104") {
                $("#find_app").attr("style", "display: none;");
            }
        });

    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <input type="hidden" id="SchemeID" data-value="<%=this.SchemeID %>"/>
    <div class="nav">
        <div class="nav">
            <span class="nav_title">应用商店管理</span>
            <span class="nav_desc">首页 &raquo; <a href="GameRecommendationList.aspx?GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>">推荐管理</a> &raquo; 新增</span>
        </div>
    </div>
    <div id="container">
        <table id="main_form">
            <tr>
                <td class="form_text">推荐类型</td>
                <td>
                    <asp:DropDownList ID="ElemType" runat="server" Width="132">
                        <asp:ListItem Value="1" Selected="True">跳转至应用或游戏</asp:ListItem>
                        <asp:ListItem Value="2">跳转至指定链接</asp:ListItem>
                        <asp:ListItem Value="3">跳转至指定分类</asp:ListItem>
                        <asp:ListItem Value="4">跳转至网游或单机</asp:ListItem>
                        <asp:ListItem Value="5">跳转至指定专题</asp:ListItem>
                    </asp:DropDownList>
                </td>
            </tr>
            <tr>
                <td class="form_text">选择元素</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" txt_name="选择元素" ID="txtShowName" ReadOnly="true" />&nbsp;
                   <span id="singlegames" style="display: none;">
                       <input type="radio" name="gameType" value="2102" id="single_game" style="vertical-align: middle;" checked="checked" /><label for="single_game">单机</label>&nbsp;</span>
                    <span id="onlinegames" style="display: none;">
                        <input type="radio" name="gameType" value="2101" id="online_game" style="vertical-align: middle;" /><label for="online_game">网游</label>&nbsp;</span>


                    <asp:HiddenField runat="server" ID="ElemID" />

                    <a id="find_game" href="javascript:FindBack('game')">查找游戏</a>&nbsp;
<%--                    <a id="find_app" href="javascript:FindBack('app')">查找应用</a>--%>
                    <a id="find_link" href="javascript:FindBack('link')" style="display: none;">查找链接</a>
                    <a id="find_category" href="javascript:FindBack('category')" style="display: none;">查找分类</a>
<%--                    <a id="find_specialtopic" href="javascript:FindBack('specialtopic')" style="display: none;">查找应用专题</a>--%>
                    <a id="find_specialtopic2" href="javascript:FindBack('specialtopic2')" style="display: none;">查找游戏专题</a>
                    <asp:HiddenField ID="GroupType" runat="server" />
                </td>
            </tr>

            <tr>
                <td class="form_text">推荐标题</td>
                <td>
                    <asp:TextBox runat="server" CssClass="required form_inputbox" ID="RecommTitle" txt_name="推荐标题" />
                </td>
            </tr>
              <tr>
                <td class="form_text">推荐标签</td>
                <td>
                     <asp:DropDownList ID="RecommTag" runat="server" Width="132">
                        <asp:ListItem Value="0" Selected="True">无</asp:ListItem>
<%--                        <asp:ListItem Value="1">推荐</asp:ListItem>--%>
                          <asp:ListItem Value="2">热门</asp:ListItem>
<%--                          <asp:ListItem Value="4">官方</asp:ListItem>--%>
                          <asp:ListItem Value="8">最新</asp:ListItem>
                    </asp:DropDownList>
                </td>
                  <%--<asp:TextBox runat="server" CssClass="form_inputbox" ID="RecommTag" txt_name="推荐标签" /> (0=无,1=推荐，2=热门，4=官方，8=最新)--%>
            </tr>
            <tr>
                <td class="form_text">推荐语</td>
                <td>
                    <asp:TextBox runat="server" CssClass="form_inputbox" ID="RecommWord" txt_name="推荐语" />
                </td>
            </tr>
            <tr>
                <td class="form_text">备注</td>
                <td>
                    <asp:TextBox runat="server" CssClass="form_inputbox" ID="Remarks" />
                </td>
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
                <td class="form_text">展示方式</td>
                <td>
                    <asp:DropDownList ID="ShowType" runat="server" Width="132" Enabled="False">
                        <asp:ListItem Value="0" Selected="True">默认</asp:ListItem>
                        <asp:ListItem Value="1">广告位</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td class="form_text">状态</td>
                <td>
                    <asp:DropDownList ID="Status" runat="server" Width="132">
                        <asp:ListItem Value="1" Selected="True">启用</asp:ListItem>
                        <asp:ListItem Value="0">禁用</asp:ListItem>
                    </asp:DropDownList></td>
            </tr>
            <tr>
                <td class="form_text">上传广告图</td>
                <td>
                    <asp:HiddenField ID="RecommPicUrl" runat="server" />
                    <asp:HiddenField ID="MainIconPicUrl" runat="server" />
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderRecommPicUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />
                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=RecommPicUrl&token=123456&thumb=false&url=<%=this.UploadUrl %>&appid=2&subid=31&extension=,jpg,png,gif," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                </td>
            </tr>
            <tr>
                <td class="form_text">广告图展示</td>
                <td>
                    <% if (this.Action == "Edit" && !string.IsNullOrEmpty(this.CurrentEntity.RecommPicUrl))
                       {
                    %>
                    <img src="<%=this.CurrentEntity.RecommPicUrl %>" id="ShowRecommPicUrl" style="border: 1px dotted #b8d0d6;" />

                    <%
                       }
                       else
                       {%>
                    <img src="Theme/Images/empty.png" id="ShowRecommPicUrl" style="border: 1px dotted #b8d0d6;" />
                    <%} %>
                    
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

        <iframe id="ifrmModify" title="查找游戏" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_GameInfoList.aspx"></iframe>
        <iframe id="ifrmModify2" title="查找应用" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_AppInfoList.aspx"></iframe>
        <iframe id="ifrmModify3" title="查找链接" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_LinkInfoList.aspx"></iframe>
        <iframe id="ifrmModify4" title="查找分类" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_AllCategoryList.aspx"></iframe>
        <iframe id="ifrmModify5" title="查找应用专题" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_SpecialTopicList.aspx?acttype=3100,<%=this.SchemeID %>&Action=3100,<%=this.SchemeID %>&SchemeID=<%=this.SchemeID %>&GroupTypeID=3100"></iframe>
        <iframe id="ifrmModify6" title="查找游戏专题" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_SpecialTopicList.aspx?acttype=3200,<%=this.SchemeID %>&Action=3200,<%=this.SchemeID %>&SchemeID=<%=this.SchemeID %>&GroupTypeID=3200"></iframe>

    </div>
</asp:Content>