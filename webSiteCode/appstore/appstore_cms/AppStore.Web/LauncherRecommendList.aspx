<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="LauncherRecommendList.aspx.cs" Inherits="AppStore.Web.LauncherRecommendList" %>

<%@ Register Assembly="AspNetPager" Namespace="Wuqi.Webdiyer" TagPrefix="webdiyer" %>
<%@ Import Namespace="AppStore.Model" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <script src="nivo-slider/jquery.nivo.slider.js" type="text/javascript"></script>
    <link href="nivo-slider/themes/default/default.css" rel="stylesheet" />
    <link href="nivo-slider/nivo-slider.css" rel="stylesheet" />

<%--    <link href="theme/css/isotope.css" rel="stylesheet" />--%>
    <link href="theme/css/progressBar.css" rel="stylesheet" />
    <script type="text/javascript" src="javascript/jquery.isotope.min.js"></script>
    <style type="text/css">
        body, div, table, tr, td, p {
            margin: 0;
            padding: 0 0 0 3px;
            border: 0;
        }

        a:active {
            background-color: transparent;
        }

        /*#container {
            width: 98%;
        }*/

        #container ul {
            font-size: 0px;
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
                height: 100px;
                width: 80px;
                margin: 5px;
                border: none;
                background: transparent;
                line-height: 10px;
                margin: 10px;
                display: inline-block;
                list-style: none;
                padding: 5px;
                cursor: pointer;
                position: relative;
                float: left;
            }

                #container ul li .name {
                    margin-top: 5px;
                }

                #container ul li .pic {
                    text-align: center;
                    float: left;
                }

                    #container ul li .pic img {
                        margin: 0 auto;
                        -webkit-border-radius: 10px;
                        -moz-border-radius: 10px;
                        border-radius: 10px;
                        height: 80px;
                        width: 80px;
                        cursor: pointer;
                    }

                #container ul li .recomm-title {
                    float: left;
                    text-align: center;
                    height: 2em;
                    width: 100%;
                    line-height: 1.5em;
                    font-size: 12px;
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


        .delete-icon {
            position: absolute;
            right: 0;
            top: 0;
            height: 30px;
            width: 30px;
            background: url("Images/del.png") 100% 100%;
            background-size: 100% 100%;
            display: none;
        }

        .delete-mode .delete-icon {
            display: block;
        }

        #container ul li.command-button.done {
            display: none;
        }

        .nav {
            padding: 15px 0 2px 10px;
        }

        a {
            text-decoration: none;
        }

        input[type="submit"], input[type="button"] {
            height: 30px;
            color: #fff;
        }
    </style>
    <script type="text/javascript">

        function getUrlParam(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg);  //匹配目标参数
            if (r != null) return unescape(r[2]); return null; //返回参数值
        }

        function openAdd(obj) {
            var all_order = $("#sortable-list li").find("input.orderno");
            var max_val = 3;
            for (var i = 0; i < all_order.length; i++) {
                var current_val = parseInt($(all_order[i]).val());
                if (current_val > max_val) {
                    max_val = current_val;
                }
            }
            max_val = max_val + 1;
            var acttype = getUrlParam("acttype");
            obj.href = "LauncherRecommendPosEdit.aspx?acttype=" + acttype + "&Action=Add&order=0&page=<%=this.PageType%>&PosID=" + max_val;
            obj.click();
        }

        // 删除
        function startDelete() {
            $("#sortable-list li").addClass("delete-mode")
            $("li.command-button.add, li.command-button.del").fadeOut(function () {
                $("li.command-button.done").show();
            });
        }
        function finishDelete() {
            $("#sortable-list li").removeClass("delete-mode")
            $("li.command-button.done").fadeOut(function () {
                $("li.command-button.add, li.command-button.del").show();
            });
            orderinfo = '{ "orderinfo": "';
            $("#sortable-list").find(".show-app-pos").each(function (index, item) {
                orderinfo += ($(this).data("id")) + ":" + (index + 1) + ",";
            })
            orderinfo += '" }';
            $.ajax({
                type: "Post",
                url: "LauncherRecommendList.aspx/UpdateOrderNo",
                contentType: "application/json; charset=utf-8",
                data: orderinfo,
                dataType: "json",
                success: function (resdata) {
                    location.reload();
                },
                error: function (err) {
                }
            });
        }
        $(function () {
            $("#sortable-list").on('click', 'li.delete-mode .delete-icon', function () {
                var $li = $(this).parents("li");
                var elemId = $li.find("input.id").val();
                deleteElem(elemId, $li);
            })
            $("#sortable-list").on('click', 'li.command-button.done', function () {
                finishDelete()
            })
            <%if (PageType == "new")
              {%>
            //$(".nav").prepend('<a href="AppInfoListNew.aspx?Action=1" style="font-size: 13px;">首页</a> &raquo;');
            $(".nav a").css("color", "#1e74c9");
            $(".nav a").css("font-size", "13px");
            $("#Div_Log").show();
            $("input[type='button']").css("background-color", "#49aef5");
            $("input[type='submit']").css("background-color", "#49aef5");
            $("input[type='button']").css("font-weight", "normal");
            $("input[type='submit']").css("font-weight", "normal");
            <%} %>
        })


        $(function () {
            var ajaxbg = $("#background,#progressBar");
            $(document).ajaxStart(function () {
                ajaxbg.show();
            }).ajaxStop(function () {
                ajaxbg.hide();
            });

            var $container = $('#container');


        });

        function update_order(data) {
            $.ajax({
                type: "Post",
                url: "LauncherRecommendList.aspx/UpdateOrder",
                contentType: "application/json; charset=utf-8",
                data: data,
                dataType: "json",
                success: function (resdata) {
                },
                error: function (err) {
                }
            });
        }

        function deleteElem(elemId, $li) {
            $.ajax({
                type: "Post",
                url: "LauncherRecommendList.aspx/DeleteElem",
                contentType: "application/json; charset=utf-8",
                data: "{elemId: " + elemId + ",SchemeID:<%=this.SchemeID%>,GroupTypeID:<%=this.GroupTypeID%>,username:'<% =GetUserName() %>'}",
                dataType: "json",
                success: function (resdata) {
                    $li.fadeOut(function () {
                        $li.remove();

                    })
                },
                error: function (err) {
                }
            });

        }

    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div style="border-bottom: 3px solid #aaa; padding: 5px; width: 98%;">
        <div class="nav" style="border: 0px; float: left; margin-top: 5px;">
            <%--<span class="nav_title">游戏中心管理</span> --%><span class="nav_desc"> <%--<a>推荐位管理 &raquo; </a>--%><a href="LauncherRecommendList.aspx?acttype=<%=this.GroupTypeID %>,<%=this.SchemeID %>&page=<%=this.PageType %>">
                <asp:Literal ID="literalNavName" runat="server"></asp:Literal></a></span>
        </div>
        <div style="text-align: right; padding: 5px; height: 30px; width: 100%; padding-right: 10px;">
            <asp:Button ID="Button1" runat="server" Text="导出列表" CssClass="btn" OnClick="Button1_Click" Visible="false" />
        </div>
    </div>
    <script type="text/javascript">

        // key: 客户端的顺序
        // value: [GroupElemId, GroupElemPos]
        var sort_map = {}

        function init_sort_map(container) {
            container.find('li.app-item').each(function (ind, ele) {
                var groupElemId = $(ele).find(".id").val();
                var groupElemPos = $(ele).find(".orderno").val();
                sort_map[ind] = [groupElemId, groupElemPos];
            })
        }

        // 每次改变顺序后刷新顺序表
        function collect_changed_sort(container) {
            var tmp_sort_map = {}
            var changed_sort = []
            container.find('li.app-item').each(function (ind, ele) {
                var groupElemId = $(ele).find(".id").val();
                var groupElemPos = $(ele).find(".orderno").val();
                if (sort_map[ind][0] != groupElemId) {
                    groupElemPos = sort_map[ind][1];
                    changed_sort.push([groupElemId, groupElemPos]);
                    $(ele).find(".orderno").val(groupElemPos);
                }
                tmp_sort_map[ind] = [groupElemId, groupElemPos];

            })
            sort_map = tmp_sort_map;
            return changed_sort;
        }

        $(function () {
            $("#sortable-list").sortable({
                placeholder: "ui-state-highlight",
                update: function () {
                    orderinfo = '{ "orderinfo": "';
                    $(this).find(".show-app-pos").each(function (index, item) {
                        orderinfo += ($(this).data("id")) + ":" + (index + 1) + ",";
                    })
                    orderinfo += '" }';
                    $.ajax({
                        type: "Post",
                        url: "LauncherRecommendList.aspx/UpdateOrderNo",
                        contentType: "application/json; charset=utf-8",
                        data: orderinfo,
                        dataType: "json",
                        success: function (resdata) {
                        },
                        error: function (err) {
                        }
                    });
                    //var changed_sort = collect_changed_sort($('#sortable-list'));
                    //var result = "";
                    //changed_sort.forEach(function (e) {
                    //    alert(e)
                    //    result += e.join(':') + ',';
                    //})
                    //var data = "{\"orderinfo\": \"" + result + "\"}";
                    //update_order(data)
                }
            });
            $("#sortable-list").disableSelection();
            init_sort_map($('#sortable-list'));

        })
    </script>

    <div id="container" style="border: 0; width: 98%;">

        <p style="clear: both;"></p>
        <ul id="sortable-list">
            <asp:Repeater runat="server" ID="DataList">
                <ItemTemplate>
                    <li class="show-app-pos app-item" data-id="<%#Eval("GroupElemID") %>">
                        <input class="id" type="hidden" value="<%#Eval("GroupElemID") %>" />
                        <input class="orderno" type="hidden" value="<%#Eval("PosID") %>" />
                        <div class="pic">
                            <a href="LauncherRecommendPosEdit.aspx?GroupElemID=<%#Eval("GroupElemID") %>&Action=Edit&acttype=<%#ActType %>&page=<%=this.PageType %>">
                                <img src="<%#Eval("RecommPicUrl") %>" width="80" height="80" alt="" /></a>
                        </div>
                        <div class="recomm-title">
                            <span><%#Eval("RecommTitle") %></span>
                        </div>
                        <div class="delete-icon"></div>
                    </li>
                </ItemTemplate>
            </asp:Repeater>


        </ul>
        <ul>
            <li class="command-button add"><a href="javascript:void(0)" onclick="openAdd(this);" class="pic">
                <img src="Images/plus2.png" /></a></li>
            <li class="command-button del"><a href="javascript:void(0)" onclick="startDelete(this);" class="pic">
                <img src="Images/minus.png" /></a></li>
            <li class="command-button done"><a href="javascript:void(0)" onclick="finishDelete(this);" class="pic">
                <img src="Images/done.png" /></a></li>
        </ul>
    </div>

    <div style="clear: both;"></div>
    <div id="Div_Log" style="width: 98%; min-height: 100px; display: none; margin-top: 20px;">
        <div style="height: 40px;">
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
