<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="HomePageBannerList.aspx.cs" Inherits="AppStore.Web.HomePageBannerList" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
<link href="theme/css/isotope.css" rel="stylesheet" />
<link href="theme/css/progressBar.css" rel="stylesheet" />
<script type="text/javascript" src="javascript/jquery.isotope.min.js"></script>
<style type="text/css">
    body{padding: 10px;}
    #content{padding:10px 10px 10px 30px;}
    .clear{clear:both;}
    body,div,table,tr,td,p
    {
        margin:0;
        padding:0;
        border:0;
    }
    a:active {background-color:transparent;}
    #main_form{ margin-left: 50px; margin-top:25px; }
    #main_form tr{}
    #main_form tr td{ padding-bottom: 10px; padding-right: 10px; vertical-align:top;}
    #main_form td.form_text{text-align:right;line-height:1.5em;}
    #main_form td .multi_text {width: 250px; height:100px;}
    #main_form td .form_inputbox{ height: 20px;}
    .thumbPic {border: 1px dotted #b8d0d6; max-width:900px; max-height:500px;}
    
    #container {width:1100px;}
    #container .red{ color:Red;}
    #container .blue{ color:Blue;}
    #container .green{ color:green;}
    #container .white{ color:white;}
    #container .black{ color:#333;}
    #container ul li p{ line-height:1.5em;}
    #container ul li.add{ text-align:center;}
    #container ul li.add img{ margin-top:40px;   }
    #container ul li{height: 200px;width: 500px;margin: 5px;border:1px solid #999; -webkit-border-radius: 1.2em;-moz-border-radius: 1.2em;border-radius: 1.2em;background: #fff;line-height: 10px;list-style:none;padding: 5px; cursor:pointer;}

    #container ul li .info{ margin-top: 5px;}
    #container ul li .pic{text-align:center; }
    #container ul li .pic img{margin: 0 auto; max-width:480px; height: 160px; cursor:pointer;}
    #container ul li .desc
    {
        left: 0.5em;
        bottom: 1.6em;
        font-size: 1.05em;
        line-height:1.5em;
        color: white;
        margin-bottom:3px;
    }
        
    #container ul li .weight
    {
        font-size: 0.9em;
        left: 0.5em;
        bottom: 0.5em;
        color: white;
        line-height:1.5em;
        margin-bottom:3px;
    }
    #container table.list_info{ width: 100%; bottom:0.5em; position:absolute;}
    #container table.list_info td{ line-height:1.5em; padding-top:3px; padding-right:5px;}
    .show-app-pos a{color: hsla(0, 100%, 100%, 1);color: white; text-decoration:none;}
    .show-app-pos a:active {background-color:transparent;}
    .wrap{ vertical-align:middle; line-height:1.5em; padding-left:5px;}
    #container ul li.selected{ background-color:#d6e3e1;}  
</style>
<script type="text/javascript">
    function openWin(obj) 
    {
        //obj.target="_blank";
        var li = $(obj).parentsUntil("ul");
        obj.href = "HomePageRecommendPosList.aspx?GroupTypeID=<%=this.GroupTypeID%>&SchemeID=<%=this.SchemeID%>&PosID=1&order=" + li.find("input.orderno").val(); 
        obj.click();
    }
    function openAdd(obj) {
        //obj.target="_blank";
        var all_order = $("#container li").not(".add").find("input.orderno");
        var max_val = 0;
        for (var i = 0; i < all_order.length; i++) {
            var current_val = parseInt($(all_order[i]).val());
            if (current_val > max_val) {
                max_val = current_val;
            }
        }
        max_val = max_val + 1;
        obj.href = "HomePageRecommendPosEdit.aspx?GroupTypeID=<%=this.GroupTypeID%>&SchemeID=<%=this.SchemeID%>&Action=Add&PosID=1&order=" + max_val;
        //obj.click();
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
        return "{\"orderinfo\": \"" + result + "\"}";
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

    function update_order(data) {
        $.ajax({
            type: "Post",
            url: "HomePageBannerList.aspx/UpdateOrder",
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


</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
<div class="nav">
     <%-- <%--<span class="nav_title">游戏中心管理</span> --%> --%> <span class="nav_desc">Banner管理 </span>
</div>
<div class="wrap">
</div>

<div id="container" style="border:0;">
    <ul>
            
        <asp:Repeater runat="server" ID="DataList">
            <ItemTemplate>
                <li class="show-app-pos">
                    <input class="id" type="hidden" value="<%#Eval("GroupElemID") %>" />
                    <input class="orderno" type="hidden" value="<%#Eval("OrderNo") %>" />
                    <div class="pic">
                        <img src="<%#Eval("RecommPicUrl") %>" alt="" />
                    </div>
                    <div class="info">
                        <a href="javascript:void(0)" onclick="openWin(this);"><span style="float: right; margin: 5px;">编辑</span></a>
                        <p><%#Eval("ShowName") %></p>
                        <p>状态：<%#BindStatus(this.GetDataItem()) %>&nbsp;&nbsp;&nbsp;&nbsp;<%#BindTime(this.GetDataItem()) %></p>
                    </div>
                    <div style="clear: both;"></div>
                </li>
            </ItemTemplate>
        </asp:Repeater>
        <li class="show-app-pos add"><input class="id" type="hidden" value="0" /><input class="orderno" type="hidden" value="99999" /><a href="javascript:void(0)" onclick="openAdd(this);"><img src="Images/plus2.png" /></a></li>
    </ul>
</div>

</asp:Content>
