<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="HomePageRecommendByAppCenterList.aspx.cs" Inherits="AppStore.Web.HomePageRecommendByAppCenterList" %>

<%@ Register Assembly="AspNetPager" Namespace="Wuqi.Webdiyer" TagPrefix="webdiyer" %>

<%@ Import Namespace="AppStore.Model" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <%--    <script src="nivo-slider/jquery.nivo.slider.js" type="text/javascript"></script>
    <link href="nivo-slider/themes/default/default.css" rel="stylesheet" />
    <link href="nivo-slider/nivo-slider.css" rel="stylesheet" />--%>

    <link href="theme/css/isotope.css" rel="stylesheet" />
    <link href="theme/css/progressBar.css" rel="stylesheet" />
    <%--  <script type="text/javascript" src="javascript/jquery.isotope.min.js"></script>--%>
    <style type="text/css">
        .column {
            width: 1100px;
            margin-left: 5px;
            margin-right: 5px;
            margin-bottom: 5px;
        }

        #Div_Log .grid tr td {
            text-align: center;
        }

        .list {
            margin: 0;
            padding: 0;
            list-style: none;
        }

            .list li {
                float: left;
                width: 500px;
                height: 90px;
                border: 1px solid #808080;
                padding: 1px;
                margin-right: 10px;
                margin-bottom: 10px;
            }

                .list li p {
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
            padding: 0;
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
            width: 750px;
            height: 244px;
        }

        .new {
            background-color: #f7f7f7;
            margin: 0;
            border: 1px solid #e5e5e5;
        }

        .newbody {
            background-color: #edeef0;
            padding: 20px;
        }

        .newinput {
            background-color: #49aef5;
            color: #FFF;
            cursor: pointer;
            font-size: 12px;
            font-weight: inherit;
            height: 2em;
            line-height: 1.5em;
            padding: 2px 10px;
            vertical-align: middle;
        }

        .bannerimg {
            display: none;
        }

        .banner_img {
            width: 650px;
            height: 244px;
        }

        .banner {
            position: relative;
        }

        .radiobanner {
            position: absolute;
            bottom: 10px;
            width: 650px;
            text-align: center;
        }


        .nivo-directionNav {
            position: absolute;
            top: 50%;
            width: 650px;
        }

            .nivo-directionNav a {
                z-index: 100;
                border: 0 none;
                display: block;
                height: 30px;
                opacity: 1;
                text-indent: -9999px;
                transition: all 200ms ease-in-out 0s;
                width: 30px;
            }

        .nivo-nextNav {
            z-index: 100;
            float: right;
            /*background-position: right;*/
            background-position: -30px 0;
            background-position-x: -50%;
            right: 0;
            background: rgba(0, 0, 0, 0) url("Images/arrows.png") no-repeat scroll -30px 0;
        }

        .nivo-prevNav {
            float: left;
            z-index: 100;
            /*background-position: -30px 0;*/
            left: 0;
            background: rgba(0, 0, 0, 0) url("Images/arrows.png") no-repeat scroll 0 0;
        }
           input[type="submit"], input[type="button"] {
        color: #fff;font-weight:normal;height:30px;}
    </style>
    <script type="text/javascript">

        function openAdd(obj) {
            var all_order = $("#container li").not(".add").find("input.orderno");
            var max_val = 3;
            for (var i = 0; i < all_order.length; i++) {
                var current_val = parseInt($(all_order[i]).val());
                if (current_val > max_val) {
                    max_val = current_val;
                }
            }
            max_val = max_val + 1;
            <%if (PageType == "new")
              {
                  %>
            obj.href = "HomePageRecommendPosEdit.aspx?GroupTypeID=<%=this.GroupTypeID%>&page=<%= this.PageType%>&SchemeID=<%=this.SchemeID%>&Action=Add&order=0&PosID=" + max_val;

              <%}
              else
              {%>
            obj.href = "HomePageRecommendPosEdit.aspx?GroupTypeID=<%=this.GroupTypeID%>&SchemeID=<%=this.SchemeID%>&Action=Add&order=0&PosID=" + max_val;

              <%}%>
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
            $(".bannerimg").first().show();
            $(".bannerimg").first().attr("data-type", "show")
            $(".bannerbtn").first().attr("checked", "checked")



            $(".bannerbtn").click(function () {
                var i = $(this).attr("data-value");
                $(".bannerimg").attr("data-type", "hide");
                $(".bannerimg").hide();
                $(".bannerimg").each(function (item, index) {
                    if (item == i) {
                        $(this).show();
                        $(this).attr("data-type", "show");
                        return false;
                    }
                });
            });
            $(".bannerbtn").each(function (item, index) {
                $(this).attr("data-value", item);
                $(this).attr("value", item);
            });
            $(".bannerimg").each(function (item, index) {
                $(this).attr("data-value", item);
                $(this).attr("value", item);
            });

            $(".nivo-prevNav").click(function () {
                var count = $(".bannerimg").length;
                $(".bannerimg").each(function (item, index) {
                    if ($(this).attr("data-type") == "show") {
                        $(this).hide();
                        $(this).attr("data-type", "hide");
                        if (item > 0) {
                            $(this).prev(".bannerimg").show();
                            $(this).prev(".bannerimg").attr("data-type", "show");
                            $(".bannerbtn").each(function (item2, index2) {
                                if (parseInt($(this).attr("value")) == item) {
                                    $(this).prev(".bannerbtn").prop('checked', true);
                                }
                            });
                            return false;
                        } else {
                            $(".bannerimg").last().show();
                            $(".bannerimg").last().attr("data-type", "show")
                            $(".bannerbtn").last().prop('checked', true);
                            return false;
                        }
                    }
                });
            });
            $(".nivo-nextNav").click(function () {
                var count = $(".bannerimg").length;
                $(".bannerimg").each(function (item, index) {
                    if ($(this).attr("data-type") == "show") {
                        $(this).hide();
                        $(this).attr("data-type", "hide");
                        if (item < count - 1) {
                            $(this).next(".bannerimg").show();
                            $(this).next(".bannerimg").attr("data-type", "show");
                            $(".bannerbtn").each(function (item2, index2) {
                                if (parseInt($(this).attr("value")) == item) {
                                    $(this).next(".bannerbtn").prop('checked', true);
                                }
                            });
                            return false;
                        } else {
                            $(".bannerimg").first().show();
                            $(".bannerimg").first().attr("data-type", "show")
                            $(".bannerbtn").first().prop('checked', true);
                        }
                    }
                });

            });
            window.setInterval(paly, 5000);

            $(".nivoSlider").find("img").css("width", "750px");
            $(".nivoSlider").find("img").css("height", "244px");
             <%if (PageType == "new")
               {%>
            $("body").addClass("newbody");
            $(".column").addClass("new");
            $("#container").addClass("new");
            $("input[type='button']").css("background-color", "#49aef5");
            $("input[type='submit']").css("background-color", "#49aef5");
            $("#Div_Log").show();
            <%}%>


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
        function del_item(item) {
            //        var id = item.find('.id').val();
            //        $.ajax({
            //            type: "Post",
            //            url: "SpecialTopicEdit.aspx/Delete",
            //            contentType: "application/json; charset=utf-8",
            //            data: "{\"id\": " + id + "}",
            //            dataType: "json",
            //            succe function (data) {
            //                var result = data.d;
            //                var parentId = '#container';
            //                $(parentId).isotope('remove', item);

            //                var parent = item.data('parent');
            //                parent.data('last_obj', false);
            //                item.remove();
            //            },
            //            error: function (err) {
            //            }
            //        });

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
            return "{\"orderinfo\": \"" + result + "\",\"SchemeID\": <%=SchemeID%> ,\"name\":\"<% =GetUserName() %>\" }";
        }

        function add_item(json_data) {
            //        $.ajax({
            //            type: "Post",
            //            url: "SpecialTopicEdit.aspx/Add",
            //            contentType: "application/json; charset=utf-8",
            //            data: json_data,
            //            dataType: "json",
            //            succe function (data) {
            //                var new_id = data.d;
            //                var $elem = $('#container li').last();
            //                $elem.find('.id').val(new_id);
            //                var orderInfo = get_all_items();
            //                update_order(orderInfo);
            //            },
            //            error: function (err) {
            //            }
            //        });
        }



        function paly() {
            var count = $(".bannerimg").length;
            $(".bannerimg").each(function (item, index) {
                if ($(this).attr("data-type") == "show") {
                    if (item == (count - 1)) {
                        $(".bannerimg").first().show();
                        $(".bannerimg").first().attr("data-type", "show");
                    }
                    else {
                        $(this).next(".bannerimg").show();
                        $(this).next(".bannerimg").attr("data-type", "show");
                    }
                    $(this).attr("data-type", "hide");
                    $(this).hide();

                    // $(".bannerbtn").removeAttr("checked");
                    var i = item;
                    $(".bannerbtn").each(function (item, index) {
                        if (item == i) {
                            $(this).attr("checked", false);
                            if (item == (count - 1)) {

                                $(".bannerbtn").first().prop('checked', true);
                            }
                            else {
                                $(this).next(".bannerbtn").prop('checked', true);
                            }
                            return false;
                        }

                    });
                    return false;
                }
            });
        }
        function update_order(data) {
            $.ajax({
                type: "Post",
                url: "HomePageRecommendList.aspx/UpdateOrder",
                contentType: "application/json; charset=utf-8",
                data: data,
                dataType: "json",
                succe: function (resdata) {

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
            //$('#slider').nivoSlider();

            $("#Add").click(function () { alert("T"); });
        });
    </script>

</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">

    <%if (PageType != "new")
      {%>
    <div class="nav">
         <%-- <%--<span class="nav_title">游戏中心管理</span> --%> --%> <span class="nav_desc">推荐位管理 &raquo; <a href="HomePageRecommendByAppCenterList.aspx?acttype=<%=this.GroupTypeID %>,<%=this.SchemeID %>">首页推荐管理</a></span>
    </div>


    <% } %>
    <%if (PageType == "new")
      {%>
    <div style="width: 1100px;">
        <div class="nav_desc" style="color: #1e74c9; float: left; margin-top: 5px;"><a href="AppInfoListNew.aspx" style="color: #1e74c9; font-size: 13px;">首页</a>&raquo; 首页推荐管理 </div>

        <div style="text-align: right;">

            <asp:Button ID="Button2" runat="server" Text="导出配置内容" OnClick="Button2_Click" />
            <asp:Label ID="Label1" runat="server" Text=""></asp:Label>
            <asp:Button ID="Button1" runat="server" Text="数据生效" OnClick="Button1_Click" Visible="false" />
        </div>
    </div>
    <div style="clear: both;"></div>
    <br />
    <% }%>
    <div class="column">

        <div style="float: left; margin-left: 18%;">
            <p style="text-align: right; margin-bottom: 10px;">
                <%--<a href="HomePageBannerList.aspx?GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>">管理</a>--%>
            </p>

            <div class="banner">
                <%
                    foreach (GroupElemsEntity item in this.FlashRecommList)
                    {
                %>
                <div class="bannerimg" data-type="hide">
                    <a href="HomePageRecommendPosList.aspx?PosID=<%=item.PosID %>&order=<%=item.OrderNo %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>&page=<%= this.PageType%>">
                        <img src="<%=item.RecommPicUrl %>" class="banner_img" width="700px" height="244px;" alt="<%=item.RecommTitle %>" title="<%--<%=string.Format("【{0}】   有效时间：{1} 至 {2}",item.RecommTitle,item.StartTime.ToString("yyyy-MM-dd HH:mm:ss"),item.EndTime.ToString("yyyy-MM-dd HH:mm:ss")) %>--%>" /></a>
                </div>
                <%
                    } %>

                <div class="radiobanner">
                    <%
                        foreach (GroupElemsEntity item in this.FlashRecommList)
                        {
                    %>
                    <input class="bannerbtn" name="bannerbtn" type="radio" value="0" data-value="0" data-type="show" />
                    <%
                        } %>
                </div>
                <div class="nivo-directionNav">
                    <a class="nivo-prevNav">Prev</a>
                    <a class="nivo-nextNav">Next</a>
                </div>
            </div>

            <%--  <div class="sliders-wrapper theme-default">
                <div id="sliders" class="nivoSlider">
                    <%
                        foreach (GroupElemsEntity item in this.FlashRecommList)
                        {
                    %>
                    <a href="HomePageRecommendPosList.aspx?PosID=<%=item.PosID %>&order=<%=item.OrderNo %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>&page=<%= this.PageType%>">


                        <img src="<%=item.RecommPicUrl %>" width="750px" height="244px;" alt="<%=item.RecommTitle %>" title="" /></a>
                    <%
                        } %>
        </div>

    </div>
            --%>
        </div>
        <div style="float: left; margin-left: 10px; display: none;">
            <p style="text-align: right; margin-bottom: 10px;">.</p>
            <% foreach (GroupElemsEntity item in this.FixedRecommList)
               {
            %>
            <div style="width: 245px; height: 113px; border: 1px solid #808080; padding: 1px; margin-bottom: 10px;">

                <a href="HomePageRecommendPosList.aspx?PosID=<%=item.PosID %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>&page=<%= this.PageType%>">

                    <img src="<%=item.RecommPicUrl %>" width="245" height="113" alt="<%=item.RecommTitle %>" title="<%=item.RecommTitle %>" /></a>
            </div>
            <%
               } %>
        </div>
        <div style="clear: both;"></div>
    </div>
    <hr style="margin-bottom: 10px;" />
    <div id="container" style="/*border: 0; */">
        <ul id="ul_container">
            <%foreach (var item in this.RandomRecommList)
              {%>
            <li class="show-app-pos" style="position: relative;">
                <input class="id" type="hidden" value="<%=item.GroupElemID %>" />
                <input class="orderno" type="hidden" value="<%=item.PosID%>" />
                <div style="float: left;">
                    <img src="<%=item.RecommPicUrl%>" width="80" height="80" alt="" />
                </div>
                <div style="float: left; margin-left: 10px; width: 400px;">
                    <a href="HomePageRecommendPosList.aspx?PosID=<%=item.PosID %>&GroupElemID=<%=item.GroupElemID %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>&page=<%=this.PageType %>">
                        <span style="margin: 5px; position: absolute; right: 5px; bottom: 5px;">编辑</span></a>
                    <%
                  if (item.ShowType == 1)
                  {
                    %>
                    <span class="adv"></span>
                    <%
                  }
                    %>
                    <p><%=item.RecommTitle %></p>
                    <p>状态：<%= this.BindStatus(item) %></p>
                    <p>类型：<%=this.BindType(item.ElemID) %></p>
                    <p>推荐标签：<%=this.BindTag(item.RecommTag) %></p>
                    <p style="overflow: hidden;">推荐语:<%=item.RecommWord %></p>
                </div>
                <div style="clear: both;"></div>
            </li>
            <%}%>
            <li class="show-app-pos add">
                <input class="id" type="hidden" value="0" /><input class="orderno" type="hidden" value="99999" /><a href="javascript:void(0)" onclick="openAdd(this);"><img src="Images/plus2.png" /></a>
            </li>
        </ul>
    </div>
    <div id="Div_Log" style="width: 1100px; display: none;">
        <div>
            <div style="float: left; font-size: 20px;">操作日志</div>
            <div style="float: right; padding: 5px;">
                <asp:Button ID="BtnExportLog" runat="server" Text="导出操作记录" CssClass="btn" OnClick="BtnExportLog_Click" />
            </div>
        </div>

        <asp:Repeater ID="Repeater1" runat="server">
            <HeaderTemplate>
                <table style="width: 100%" class="grid">
                    <thead>
                        <tr>
                            <th>操作人</th>
                            <th>操作类型</th>
                            <th>操作内容</th>
                            <th>操作时间</th>
                        </tr>
                    </thead>
            </HeaderTemplate>
            <ItemTemplate>
                <tr>
                    <td><%#Eval("UserName") %></td>
                    <td><%#Eval("OperateExplain") %></td>
                    <td><%#Eval("OperateContent") %></td>
                    <td><%#Eval("OperateTime") %></td>
                </tr>

            </ItemTemplate>
            <FooterTemplate>
                </table>
            </FooterTemplate>

        </asp:Repeater>
        <div class="wrap">
            <webdiyer:AspNetPager runat="server" ID="AspNetPager1" OnPageChanged="AspNetPager1_PageChanged" CssClass="pages" CurrentPageButtonClass="cpb"
                PagingButtonSpacing="0px" NumericButtonCount="10" PageSize="10" AlwaysShow="true">
            </webdiyer:AspNetPager>
        </div>
    </div>
</asp:Content>
