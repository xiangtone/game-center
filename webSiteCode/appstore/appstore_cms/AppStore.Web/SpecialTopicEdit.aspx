<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="SpecialTopicEdit.aspx.cs" Inherits="AppStore.Web.SpecialTopicEdit" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <link href="theme/css/isotope.css" rel="stylesheet" />
    <link href="theme/css/progressBar.css" rel="stylesheet" />
    <script type="text/javascript" src="javascript/jquery.isotope.min.js"></script>
    <style type="text/css">
        body, div, table, tr, td, p {
            margin: 0;
            padding: 0;
            border: 0;
        }

        a:active {
            background-color: transparent;
        }

        #main_form {
            margin-left: 50px;
            margin-top: 25px;
        }

        .nav {
            padding: 15px 0 2px;
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

        #container {
            padding-left: 50px;
        }

            #container .red {
                color: Red;
            }

            #container .blue {
                color: Blue;
            }

            #container .green {
                color: green;
            }

            #container .white {
                color: white;
            }

            #container .black {
                color: #333;
            }

            #container ul li p {
                text-align: center;
            }

            #container ul li.add {
                text-align: center;
            }

                #container ul li.add img {
                    width: 50px;
                    margin-top: 20px;
                    border: 1px solid #000;
                }

            #container ul li {
                height: 100px;
                width: 100px;
                margin: 5px;
                -webkit-border-radius: 1.2em;
                -moz-border-radius: 1.2em;
                border-radius: 1.2em;
                background: #fff;
                line-height: 10px;
                list-style: none;
                padding: 5px;
            }

                #container ul li .name {
                    margin-top: 5px;
                }

                #container ul li .pic {
                    text-align: center;
                }

                    #container ul li .pic img {
                        margin: 0 auto;
                        max-width: 100px;
                        height: 80px;
                        cursor: pointer;
                    }

                #container ul li .desc {
                    left: 0.5em;
                    bottom: 1.6em;
                    font-size: 1.05em;
                    line-height: 1.5em;
                    color: white;
                    margin-bottom: 3px;
                }

                #container ul li .weight {
                    font-size: 0.9em;
                    left: 0.5em;
                    bottom: 0.5em;
                    color: white;
                    line-height: 1.5em;
                    margin-bottom: 3px;
                }

            #container table.list_info {
                width: 100%;
                bottom: 0.5em;
                position: absolute;
            }

                #container table.list_info td {
                    line-height: 1.5em;
                    padding-top: 3px;
                    padding-right: 5px;
                }

        .show-app-pos a {
            color: hsla(0, 100%, 100%, 1);
            color: white;
            text-decoration: none;
        }

            .show-app-pos a:active {
                background-color: transparent;
            }

        .wrap {
            vertical-align: middle;
            line-height: 1.5em;
            padding-left: 5px;
        }

        input[type="submit"], input[type="button"] {
            color: #fff;
            font-weight: normal;
        }

        .selected {
            border: 1px solid #000;
        }
    </style>

    <script type="text/javascript">
        function get_item(orderno) {
            var $obj = $("#container li input.orderno[value='" + orderno + "']").parent()
            return $obj;
        }
        function change_obj_order(old_order, order) {
            var key = '.orderno';
            var $obj = get_item(old_order);
            $obj.find(key).val(order);
        }
        function on_item_click(obj, val_key) {
            var parent = obj.data('parent');
            if (parent.data('last_obj') == undefined || parent.data('last_obj') == false) {
                parent.data('last_obj', obj);
                parent.data('last_val', obj.find(val_key).val());
                //$(this).css('border','1px solid #000');
                obj.attr('class', obj.attr('class') + ' ' + parent.data('selectedStyle'));
            }
            else {
                var current_val = parseInt(obj.find(val_key).val());
                var last_val = parseInt(parent.data('last_val'));
                var i = 0;

                if (current_val > last_val) {
                    //往后移动
                    change_obj_order(last_val, 'tmp');
                    for (var i = last_val + 1; i < current_val; i++) {
                        change_obj_order(i, i - 1);
                    }
                    change_obj_order('tmp', current_val - 1);
                }
                else if (last_val > current_val) {
                    //往前移动
                    change_obj_order(last_val, 'tmp');
                    for (var i = last_val - 1; i >= current_val; i--) {
                        change_obj_order(i, i + 1);
                    }
                    change_obj_order('tmp', current_val);
                }

                parent.data('last_obj').attr('class', parent.data('last_obj').attr('class').replace(' ' + parent.data('selectedStyle'), ''));
                parent.data('last_obj', false);

                if (last_val != current_val)
                    parent.data('callback')();
            }
        }
        $.fn.extend({
            exchange: function (param) {
                var children_obj = $(this).find(param.item).not(param.exclude);
                var clickObj = children_obj;
                var data_obj = children_obj.find(param.key);
                clickObj.data('parent', $(this));
                $(this).data('callback', param.callback);
                $(this).data('selectedStyle', param.selectedStyle);
                clickObj.click(function () {
                    return on_item_click($(this), param.key);
                });
            }
        });
        $(function () {
            $("body").keydown(function (event) {
                if (event.keyCode == 46) {
                    //DEL
                    var seleced_item = $('#container').find('.selected');
                    del_item(seleced_item);
                }
            });
          
            var ajaxbg = $("#background,#progressBar");
            $(document).ajaxStart(function () {
                ajaxbg.show();
            }).ajaxStop(function () {
                ajaxbg.hide();
            });
            var id = $.getUrlParam("id");
            if (id == null) {
                $('#container').css("display", "none");
                $('#container').prev(".wrap").css("display", "none");
            }
            var $container = $('#container');

            $container.isotope({
                itemSelector: '.show-app-pos',
                layoutMode: 'fitRows',
                getSortData: {
                    id: function ($elem) {
                        return parseInt($elem.find('.orderno').val(), 10);
                    }
                },
                sortBy: 'orderno',
                sortAscending: true
            });
            $('#container').exchange({
                item: 'li',
                key: '.orderno',
                exclude: '.add',
                selectedStyle: 'selected',
                callback: function () {
                    $('#container').isotope('updateSortData', $('#container').find('.show-app-pos')).isotope({ sortBy: 'id', sortAscending: true });
                    var orderInfo = get_all_items();
                    update_order(orderInfo);
                }
            });

        });
        function GetNewElem(app_id, app_name, app_icon, order_no) {
            var template = "<li class=\"show-app-pos new\">" +
                               "<input class=\"id\" type=\"hidden\" value=\"" + app_id + "\" />" +
                               "<input class=\"orderno\" type=\"hidden\" value=\"" + order_no + "\" />" +
                               "<p class=\"pic\"><img src=\"" + app_icon + "\" /></p>" +
                               "<p class=\"name\">" + app_name + "</p>" +
                            "</li>";
            return template;
        }

        function GetShareData(args) {
            var key = '.orderno';
            var parentId = '#container';
            var order_no = $(parentId + ' ul').children().length;
            var $newEle = $(GetNewElem(args.AppID, args.ShowName, args.MainIconPicUrl, order_no));
            $newEle.data('parent', $(parentId));
            $newEle.click(function () {
                return on_item_click($(this), key);
            });
            //$newEle.find(key).val();
            $(parentId + ' ul').append($newEle);
            $(parentId).isotope('appended', $newEle);
            add_item(parse_to_json($newEle));
            //$("#ShowName").val(args.ShowName);
        }
        function parse_to_json(item) {
            //var cur_item = $(items[i]);
            var gid = $.getUrlParam("id");
            var data = { 'appid': item.find('.id').val(), 'name': item.find('.name').text(), 'pic': item.find('.pic img').attr('src'), 'gid': gid };
            return JSON.stringify(data);
        }
        function del_item(item) {
            var id = item.find('.id').val();
            $.ajax({
                type: "Post",
                url: "SpecialTopicEdit.aspx/Delete",
                contentType: "application/json; charset=utf-8",
                data: "{\"id\": " + id + ",\"SchemeID\": <%=SchemeID%> }",
                dataType: "json",
                success: function (data) {
                    var result = data.d;
                    var parentId = '#container';
                    $(parentId).isotope('remove', item);

                    var parent = item.data('parent');
                    parent.data('last_obj', false);
                    item.remove();
                },
                error: function (err) {
                }
            });

            //$('#container').isotope('updateSortData', $('#container').find('.show-app-pos')).isotope({ sortBy: 'id', sortAscending: true });
        }
        function get_all_items() {
            var items = $('#container li').not('.add');
            //var result = new Array();
            var result = '';
            for (var i = 0; i < items.length; i++) {
                var cur_item = $(items[i]);
                result += cur_item.find('.id').val() + ":" + cur_item.find('.orderno').val() + ",";
            }
            return "{\"orderinfo\": \"" + result + "\",\"SchemeID\": <%=SchemeID%> }";
        }

        function add_item(json_data) {
            $.ajax({
                type: "Post",
                url: "SpecialTopicEdit.aspx/Add",
                contentType: "application/json; charset=utf-8",
                data: json_data,
                dataType: "json",
                success: function (data) {
                    var new_id = data.d;
                    var $elem = $('#container li').last();
                    $elem.find('.id').val(new_id);
                    var orderInfo = get_all_items();
                    update_order(orderInfo);
                },
                error: function (err) {
                }
            });
        }

        function update_order(data) {
            $.ajax({
                type: "Post",
                url: "SpecialTopicEdit.aspx/UpdateOrder",
                contentType: "application/json; charset=utf-8",
                data: data,
                dataType: "json",
                success: function (resdata) {

                },
                error: function (err) {
                }
            });
        }
        function test() {
            alert($.getUrlParam('id'));
        }
    </script>
    <script type="text/javascript">
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <script type="text/javascript">
        var FindBack = function (type) {
            if (type == "game")
                DlgIfrm("ifrmModify");
            else if (type == 'app')
                DlgIfrm("ifrmModify2");
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
        <%--<span class="nav_title">游戏中心管理</span> --%> <span class="nav_desc"><a href="SpecialTopicList.aspx?acttype=<%=this.GroupTypeID %>,<%=this.SchemeID %>&page=<%=PageType %>">专题管理</a>&raquo;</span><a> 新增</a>
    </div>
    <div>
        <table id="main_form">
            <tr>
                <td class="form_text">专题名称</td>
                <td>
                    <asp:TextBox ID="txtGroupName" runat="server" CssClass="required" txt_name="专题名称" /></td>
            </tr>
            <tr>
                <td class="form_text">开始时间</td>
                <td>
                    <asp:TextBox ID="txtStartTime" runat="server" CssClass="required" txt_name="开始时间" onclick="OnClickTime(this)" /></td>
            </tr>
            <tr>
                <td class="form_text">结束时间</td>
                <td>
                    <asp:TextBox ID="txtEndTime" runat="server" CssClass="required" txt_name="结束时间" onclick="OnClickTime(this)" /></td>
            </tr>
            <tr>
                <td class="form_text">专题说明</td>
                <td>
                    <asp:TextBox ID="txtGroupDesc" TextMode="MultiLine" runat="server" CssClass="multi_text required" txt_name="专题说明" /></td>
            </tr>
            <tr>
                <td class="form_text">专题图片</td>
                <td>
                    <input type="hidden" runat="server" id="ThumbPicUrl" class="required" txt_name="专题图片" />
                    <object type="application/x-shockwave-flash" data="UploadControl/ResourceUpload.swf" id="UploaderThumbPicUrl" style="visibility: visible; height: 80px; min-width: 300px;">
                        <param name="quality" value="high" />
                        <param name="wmode" value="opaque" />

                        <param name="flashVars" value="config=UploadControl/configuration.xml&ctrlid=ThumbPicUrl&token=123456&thumb=true&url=<%=this.UploadUrl %>&appid=2&subid=31&extension=,jpg,png,gif," />
                        <param name="allowScriptAccess" value="sameDomain" />
                    </object>
                </td>
            </tr>
            <tr>
                <td class="form_text">专题图片预览</td>
                <td>
                    <asp:Image runat="server" ID="ShowThumbPic" CssClass="thumbPic" ImageUrl="Theme/Images/empty.png" />
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <asp:Button ID="Button1" runat="server" Text="保存" CssClass="check_form" OnClick="OnSave" /><span id="err_msg"></span></td>
            </tr>
        </table>
    </div>
    <div class="wrap" style="text-align: left;">
        选择游戏&nbsp;  <span style="float: right">
            <asp:Button ID="Button2" runat="server" Text="导出列表" CssClass="btn" OnClick="Button2_Click" Visible="false" /></span>
    </div>
    <div id="container">
        <ul>
            <li class="show-app-pos add" runat="server" id="addGameLi">
                <input class="id" type="hidden" value="0" /><input class="orderno" type="hidden" value="0" /><a href="javascript:FindBack('game')"><img src="Images/plus2.png" /></a><p style="margin-top: 5px;">添加游戏</p>
            </li>
            <li class="show-app-pos add" runat="server" id="addAppLi">
                <input class="id" type="hidden" value="0" /><input class="orderno" type="hidden" value="0" /><a href="javascript:FindBack('app')"><img src="Images/plus2.png" /></a><p style="margin-top: 5px;">添加应用</p>
            </li>


            <asp:Repeater runat="server" ID="DataList">
                <ItemTemplate>
                    <li class="show-app-pos">
                        <input class="id" type="hidden" value="<%#Eval("GroupElemID") %>" />
                        <input class="orderno" type="hidden" value="<%#Eval("OrderNo") %>" />
                        <p class="pic">
                            <img src="<%#Eval("RecommPicUrl") %>" />
                        </p>
                        <p class="name"><%#Eval("RecommTitle")%></p>
                    </li>
                </ItemTemplate>
            </asp:Repeater>
        </ul>
        <iframe id="ifrmModify" title="查找游戏" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_GameInfoList.aspx?SchemeID=<%=SchemeID %>"></iframe>
        <iframe id="ifrmModify2" title="查找应用" frameborder="0" scrolling="yes" style="display: none; width: 800px; height: 480px;" src="FindBack/F_AppInfoList.aspx?SchemeID=<%=SchemeID %>"></iframe>
    </div>
</asp:Content>
