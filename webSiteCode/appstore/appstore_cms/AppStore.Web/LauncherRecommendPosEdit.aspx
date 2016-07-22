<%@ Page Language="C#" AutoEventWireup="true" MasterPageFile="~/MasterPage.Master" CodeBehind="LauncherRecommendPosEdit.aspx.cs" Inherits="AppStore.Web.LauncherRecommendPosEdit" %>



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


        }

        //查找应用，查找游戏
        function GetShareData(args) {
            $("#<%=ElemID.ClientID %>").val(args.AppID);
            $("#<%=txtShowName.ClientID %>").val(args.ShowName);
            $("#<%=RecommTitle.ClientID%>").val(args.ShowName);
            $("#<%=MainIconPicUrl.ClientID%>").val(args.MainIconPicUrl);
            $("#<%=GroupType.ClientID%>").val(args.TypeID);

        }

    </script>

    <!-- 星级 开始 -->
    <style type="text/css">
        .recomm-val-star-bg {
            background: url('Images/blank-star.png') repeat-x;
            width: 80px;
            height: 16px;
        }

        .recomm-val-star {
            background: url('Images/blue-star.png') repeat-x;
            width: 32px;
            height: 16px;
        }

        .recomm-val-star-bg:hover {
            cursor: pointer;
        }     .nav {
            padding: 15px 0 2px 10px;
        }
                a {
            text-decoration: none;
        }
    </style>
    <script type="text/javascript">
        $(function () {
              <%if (PageType == "new")
                    {%>
            //$(".nav").prepend('<a href="AppInfoListNew.aspx?Action=1" style="font-size: 13px;">首页</a> &raquo;');
            $(".nav a").css("color", "#1e74c9");
            $(".nav span").css("color", "#1e74c9");
            $(".nav a").css("font-size", "13px");
            $("input[type='button']").css("background-color", "#49aef5");
            $("input[type='submit']").css("background-color", "#49aef5");
            $("input[type='button']").css("font-weight", "normal");
            $("input[type='submit']").css("font-weight", "normal");
            <%} %>
            var recommVal = $("#<%=this.RecommVal.Value%>");
            $('.recomm-val-star').width(recommVal * 8);
            var starWidth = $('.recomm-val-star').width();
            $('.recomm-val-star-bg').mousemove(function (e) {
                var x = e.pageX - 110,
                    numOfHalf = parseInt(x / 8),
                    mod = x % 8;
                if (mod > 4) { numOfHalf++; }

                $('.recomm-val-star').width(numOfHalf * 8);
            }).mouseout(function () {
                $('.recomm-val-star').width(starWidth);
            }).click(function (e) {
                starWidth = $('.recomm-val-star').width();
                $("#<%= RecommVal.ClientID %>").val(starWidth / 8);
            })
        })
    </script>
    <!-- 星级 结束 -->

</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
        <div class="nav">
            <%--<span class="nav_title">应用商店管理</span>--%>
            <span class="nav_desc"><%--首页 &raquo;--%> <a href="LauncherRecommendList.aspx?acttype=<%=this.GroupTypeID %>,<%=this.SchemeID %>&page=<%=this.PageType %>">
                <asp:Label ID="literalNavName" runat="server" Text="Label"></asp:Label></a> &raquo; <a>编辑</a></span>
        </div>
    
    <div id="container">
        <table id="main_form">
            <tr>
                <td class="form_text">推荐类型</td>
                <td>
                    <asp:DropDownList ID="ElemType" runat="server" Width="132">
<%--                        <asp:ListItem Value="1" Selected="True">跳转至应用或游戏</asp:ListItem>--%>
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
                    <%if (SchemeID == 104)
                      {%>
                    <a href="javascript:FindBack('game')">查找游戏</a>&nbsp;
                      <%}
                      else
                      {
                      %>
                    <a href="javascript:FindBack('game')">查找游戏</a>&nbsp;
                    <a href="javascript:FindBack('app')">查找应用</a>
                    <%} %>

                    <asp:HiddenField ID="GroupType" runat="server" />
                    <asp:HiddenField ID="MainIconPicUrl" runat="server" />
                </td>
            </tr>
            <asp:HiddenField ID="RecommTitle" runat="server" />
            <tr style="display:none;">
                <td class="form_text">推荐语</td>
                <td>
                    <asp:TextBox runat="server" ID="RecommWord" txt_name="推荐语" Rows="5" TextMode="multiline" />
                </td>
            </tr>
            <tr style="display:none;">
                <td class="form_text">推荐星级</td>
                <td>
                    <div class="recomm-val-star-bg">
                        <div class="recomm-val-star"></div>
                    </div>
                    <asp:HiddenField runat="server" ID="RecommVal" />
                </td>
            </tr>
            <tr>
                <td class="form_text">备注</td>
                <td>
                    <asp:TextBox runat="server" CssClass="form_inputbox" ID="Remarks" />
                </td>
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
                <td></td>
                <td>
                    <asp:Button ID="btnSave" runat="server" Text="保存" CssClass="btn check_form" OnClick="btnSave_Click" />
                    <span id="err_msg"></span>
                </td>
            </tr>
        </table>

        <iframe id="ifrmModify" title="查找游戏" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_GameInfoList.aspx?SchemeID=<%=this.SchemeID %>"></iframe>
        <iframe id="ifrmModify2" title="查找应用" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_AppInfoList.aspx?SchemeID=<%=this.SchemeID %>"></iframe>
        <iframe id="ifrmModify3" title="查找链接" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_LinkInfoList.aspx"></iframe>
        <iframe id="ifrmModify4" title="查找分类" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_AllCategoryList.aspx"></iframe>
        <iframe id="ifrmModify5" title="查找专题" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_SpecialTopicList.aspx?SchemeID=<%=this.SchemeID %>"></iframe>
    </div>
</asp:Content>
