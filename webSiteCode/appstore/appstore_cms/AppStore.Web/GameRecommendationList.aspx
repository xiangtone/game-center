<%@ Page Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="GameRecommendationList.aspx.cs" Inherits="AppStore.Web.GameRecommendation" %>

<%@ Import Namespace="AppStore.Model" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <script src="nivo-slider/jquery.nivo.slider.js" type="text/javascript"></script>
    <link href="nivo-slider/themes/default/default.css" rel="stylesheet" />
    <link href="nivo-slider/nivo-slider.css" rel="stylesheet" />

    <link href="theme/css/isotope.css" rel="stylesheet" />
    <link href="theme/css/progressBar.css" rel="stylesheet" />
    <script type="text/javascript" src="javascript/jquery.isotope.min.js"></script>
    <script type="text/javascript" src="javascript/Sortable.js"></script>
    <style type="text/css">
        ul li {
            list-style: none;
        }

        .list_banner {
            cursor: move;
        }

            .list_banner i {
                color: #c00;
                cursor: pointer;
                display: block;
                opacity: 0;
                right: 0;
            }

            .list_banner:hover i {
                opacity: 1;
            }

        .column {
            margin: 5px;
            width: 1100px;
        }

        #list-1 {
            margin-left: 0;
        }

        .list {
            margin: 0;
            padding: 0;
            list-style: none;
        }

        .adv {
            height: 45px;
            position: absolute;
            left: 1px;
            top: 1px;
            width: 45px;
        }

        .group {
            margin-bottom: 20px;
            /*border-bottom: 1px solid #999;*/
        }

            .group .item {
                margin-bottom: 15px;
                vertical-align: bottom;
                height: 200px;
                width: 800px;
                border: 1px solid #808080;
            }

            .group .pic {
                float: left;
                margin-right: 10px;
            }

            .group img {
                height: 200px;
            }

            .group .info {
                float: left;
                /*margin-left: 20px;*/
            }

            .group p {
                line-height: 2em;
            }

            .group .list li {
                float: left;
                width: 500px;
                height: 80px;
                border: 1px solid #808080;
                padding: 1px;
                margin-right: 10px;
                margin-bottom: 10px;
            }

                .group .list li p {
                    line-height: 1.5em;
                }


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
            width: 1100px;
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
                line-height: 1.5em;
            }

            #container ul li.add {
                text-align: center;
            }

                #container ul li.add img {
                    width: 50px;
                    margin-top: 20px;
                }

            #container ul li {
                height: 90px;
                width: 500px;
                margin: 5px;
                border: 1px solid #999;
                -webkit-border-radius: 1.2em;
                -moz-border-radius: 1.2em;
                border-radius: 1.2em;
                background: #fff;
                line-height: 10px;
                list-style: none;
                padding: 5px;
                cursor: pointer;
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

        #container ul li.selected {
            background-color: #d6e3e1;
        }

        .theme-default .nivoSlider img {
            max-width: 750px;
            max-height: 244px;
        }
    </style>
    <script type="text/javascript">
        function openWin(obj) {
            var posid = $(".list_banner").length
            //obj.target="_blank";
            window.location.href = "GameRecommendPosEdit.aspx?Action=Add&showtype=1&PosID=" + posid + "&order=<%=OrderNo %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>";
            //obj.click();
        }
        function CheckDel(obj) {
            var items = $(obj).parentsUntil(".group").parent().find(".item");
            if (items.length == 1) {
                return confirm("仅剩次一项有效推荐，确定删除吗？");
            }
            else {
                return confirm("确定删除吗？");;
            }
        }
        function openAdd(obj) {
            var all_order = $("#container li").not(".add").find("input.orderno");
            var max_val = parseInt($(".list_game").length) + 1;
            //for (var i = 0; i < all_order.length; i++) {
            //    var current_val = parseInt($(all_order[i]).val());
            //    if (current_val > max_val) {
            //        max_val = current_val;
            //    }
            //}
            //max_val = max_val + 1;
            obj.href = "GameRecommendPosEdit.aspx?GroupTypeID=<%=this.GroupTypeID%>&SchemeID=<%=this.SchemeID%>&Action=Add&order=0&PosID=" + max_val;
            obj.click();
        }
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
            if ($(".list_banner").length < 2) {
                $("#btnSave").hide();
            }
            else {
                $("#btnSave").show();
            }

            $("#btnSave").click(function () {
                var elemId = "";
                var OrderNo = "";
                $(".list_game").each(function (index, item) {
                    elemId += ($(this).data("id")) + ",";
                    OrderNo += (index+1) + ",";
                })
                $.ajax({
                    type: "POST",
                    url: 'GameRecommendationList.aspx?action=ChangeOrdeNo',
                    data: {
                        "elemId": elemId,
                        "OrderNo": OrderNo,
                    },
                    succe function (data) {
                        alert(data);
                        location.reload();
                    }
                });
            });

            var ajaxbg = $("#background,#progressBar");
            $(document).ajaxStart(function () {
                ajaxbg.show();
            }).ajaxStop(function () {
                ajaxbg.hide();
            });

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
            //        $("body").keydown(function (event) {
            //            if (event.keyCode == 46) {
            //                //DEL
            //                var seleced_item = $('#container').find('.selected');
            //                del_item(seleced_item);
            //            }
            //        });
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
        function get_all_items() {
            var items = $('#container li').not('.add');
            //var result = new Array();
            var result = '';
            for (var i = 0; i < items.length; i++) {
                var cur_item = $(items[i]);
                result += cur_item.find('.id').val() + ":" + cur_item.find('.orderno').val() + ",";
            }
            return "{\"orderinfo\": \"" + result + "\"}";
        }
        function update_order(data) {
            $.ajax({
                type: "Post",
                url: "HomePageRecommendList.aspx/UpdateOrder",
                contentType: "application/json; charset=utf-8",
                data: data,
                dataType: "json",
                succe function (resdata) {

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
        $(function () {
            $('#slider').nivoSlider();

            $("#Add").click(function () { alert("T"); });
        });
    </script>

</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div class="nav">
        <span class="nav_title">桌面游戏推荐管理</span> <%--<span class="nav_desc">推荐位管理 &raquo; <a href="HomePageRecommendList.aspx?GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>">游戏推荐管理</a></span>--%>
    </div>


    <div class="column">
        <div style="float: left">
            <div class="group">
                <div class="info">
                    <ul id="list-1">

                        <asp:Repeater runat="server" ID="RunningList">
                            <ItemTemplate>
                                <li class="list_banner list_game" data-id="<%#Eval("GroupElemID") %>">
                                    <div class="item">
                                        <div class="pic">
                                            <img src="<%#Eval("RecommPicUrl") %>" />
                                        </div>

                                        <p><%#Eval("RecommTitle")%></p>
                                        <p>状态：<%#BindStatus(this.GetDataItem()) %></p>
                                        <p>推荐时间：<%#BindTime(this.GetDataItem()) %></p>
                                        <%--           <p>
                                            <asp:LinkButton ID="lbtnUp" runat="server" OnCommand="lbtnMoveIndex_Command" CommandName='<%#Eval("GroupElemID") %>' CommandArgument="up">上移</asp:LinkButton>&nbsp;&nbsp;<asp:LinkButton ID="lbtnDown" runat="server" OnCommand="lbtnMoveIndex_Command" CommandName='<%#Eval("GroupElemID") %>' CommandArgument="down">下移&nbsp;&nbsp;</asp:LinkButton>
                                        </p>--%>
                                        <p>
                                            <a href="GameRecommendPosEdit.aspx?Id=<%#Eval("GroupElemID") %>&Action=Edit&OrderNo=<%#Eval("OrderNo") %>&PosID=<%=PosId %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>">编辑</a>&nbsp;&nbsp;<asp:LinkButton runat="server" OnCommand="DelItem" CommandArgument='<%#Eval("GroupElemID") %>' OnClientClick="return CheckDel(this);" Text="删除" />
                                        </p>

                                    </div>
                                    <%-- <i class="js-remove">✖</i>--%>
                                </li>
                            </ItemTemplate>

                        </asp:Repeater>

                    </ul>
                </div>
                <br />
                <input type="button" id="btnSave" value="保存广告位置" style="height: 30px;" />
                <script type="text/javascript">
                    var el = document.getElementById('list-1');
                    var sortable = Sortable.create(el, {
                        animation: 150,
                    });
                    
                    function change() {
                        alert();
                        var elemId = "";
                        var OrderNo = "";
                        $(".list_game").each(function (index, item) {
                            elemId += ($(this).data("id")) + ",";
                            OrderNo += (index + 1) + ",";
                        })
                        $.ajax({
                            type: "POST",
                            url: 'GameRecommendationList.aspx?action=ChangeOrdeNo',
                            data: {
                                "elemId": elemId,
                                "OrderNo": OrderNo,
                            },
                            succe function (data) {
                                alert(data);
                                location.reload();
                            }
                        });
                    }
                </script>

            </div>
        </div>
        <input type="button" style="float: right;" value="新增推荐" onclick="openWin(this)" />
        <div style="clear: both;"></div>

    </div>
    <hr style="margin-bottom: 10px;" />
    <div id="container" style="border: 0;">
        <ul>

            <%
                foreach (var item in this.RandomRecommList)
                {
            %>
            <li class="show-app-pos list_game" data-id="<%=item.GroupElemID %>">
                <input class="id" type="hidden" value="<%=item.GroupElemID %>" />
                <input class="orderno" type="hidden" value="<%=item.PosID%>" />
                <div style="float: left;">
                    <img src="<%=item.RecommPicUrl%>" width="80" height="80" alt="" />
                </div>
                <div style="float: left; margin-left: 10px; width: 400px;">
                    <a href="GameRecommendPosEdit.aspx?Action=Editapp&Id=<%=item.GroupElemID%>&PosID=<%=item.PosID %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>"><span style="margin: 5px; position: absolute; right: 5px; bottom: 5px;">编辑</span></a>

                    <p><%=item.RecommTitle %></p>
                    <p>状态：<%= this.BindStatus(item) %></p>
                    <p><%= this.BindTime(item) %></p>
                    <p style="overflow: hidden;">推荐语:<%=item.RecommWord %></p>
                    <%if (Convert.ToInt32(item.RecommTag) == 1)
                      { %>
                    <span class="adv" style="background: url('Images/RecommTag1.png') no-repeat"></span>
                    <%} %>
                    <%if (Convert.ToInt32(item.RecommTag) == 2)
                      { %>
                    <span class="adv" style="background: url('Images/RecommTag2.png') no-repeat"></span>
                    <%} %>
                    <%if (Convert.ToInt32(item.RecommTag) == 4)
                      { %>
                    <span class="adv" style="background: url('Images/RecommTag4.png') no-repeat"></span>
                    <%} %>
                    <%if (Convert.ToInt32(item.RecommTag) == 8)
                      { %>
                    <span class="adv" style="background: url('Images/RecommTag8.png') no-repeat"></span>
                    <%} %>
                </div>
                <div style="clear: both;"></div>
            </li>

            <%
                }
            %>
            <li class="show-app-pos add">
                <input class="id" type="hidden" value="0" /><input class="orderno" type="hidden" value="99999" /><a href="javascript:void(0)" onclick="openAdd(this);"><img src="Images/plus2.png" /></a>
            </li>
        </ul>
    </div>
</asp:Content>
