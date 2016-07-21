<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="PopularList.aspx.cs" Inherits="AppStore.Web.PopularList" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <link href="theme/css/isotope.css" rel="stylesheet" />
    <link href="theme/css/progressBar.css" rel="stylesheet" />
    <script type="text/javascript" src="javascript/jquery.isotope.min.js"></script>

    <script type="text/javascript">
       
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
                layoutMode: 'fitRows'
            });

            //var $container2 = $('#container2');

            //$container2.isotope({
            //    itemSelector: '.show-app-pos',
            //    layoutMode: 'fitRows'
            //});
        });

    </script>
    <style type="text/css">
        table, tr, td {
            margin: 0;
            padding: 0;
            border: 0;
        }
        #Add {
        background: #14968e none repeat scroll 0 0;
    border-radius: 1.2em;
    height: 100px;
    text-align: center;
    line-height: 10px;
    margin: 5px;
    padding: 5px;
    width: 230px;
    left:10px;
    position: absolute;
    transform: translate(5px, 5px);
        }
        ul {
            margin: 0px;
            padding: 0px;
            list-style: none;
        }
        .new {
         cursor:move;}
        .container .red {
            color: Red;
        }

        .container .blue {
            color: Blue;
        }

        .container .green {
            color: green;
        }

        .container .white {
            color: white;
        }

        .container .black {
            color: #333;
        }

        .container ul li p {
            margin-left: 5px;
        }

        .container ul li.add {
            text-align: center;
        }

            .container ul li.add img {
                width: 110px;
            }

        .container ul li {
            height: 100px;
            width: 230px;
            margin: 5px;
            -webkit-border-radius: 1.2em;
            -moz-border-radius: 1.2em;
            border-radius: 1.2em;
            background: #14968e;
            line-height: 10px;
            padding: 5px;
            float:left;display:inline;
        }

            .container ul li .pic {
                text-align: center;
            }

                .container ul li .pic img {
                    margin: 0 auto;
                    max-height: 250px;
                    max-width: 300px;
                }

            .container ul li table td.name {
                font-size: 16px;
                font-weight: bold;
                line-height: 1.5em;
                vertical-align: middle;
                text-align: center;
                color: white;
                width: 100px;
            }

            .container ul li .name a {
                color: white;
                text-decoration: none;
                font-size: 16px;
                font-weight: bold;
            }

            .container ul li .icon {
                white-space: nowrap;
                font-size: 12px;
                font-weight: bold;
                line-height: 1.5em;
                text-align: center;
                color: white;
                width: 100px;
                overflow: hidden;
                max-width: 110px;
            }

                .container ul li .icon img {
                    height: 80px;
                    max-width: 100px;
                    max-height: 80px;
                }

            .container ul li .weight {
                font-size: 0.9em;
                left: 0.5em;
                bottom: 0.5em;
                top: 0;
                color: white;
                line-height: 1.5em;
                margin-bottom: 3px;
            }

        .wrap {
            vertical-align: middle;
            line-height: 1.5em;
            padding-left: 5px;
        }

        .container table.list_info {
            width: 100%;
            height: 110px;
            /*position: absolute;*/
            bottom: 0;
        }

            .container table.list_info td {
                line-height: 1.5em;
                padding-top: 3px;
                padding-right: 5px;
                vertical-align: top;
            }

        .container a {
            color: white;
            text-decoration: none;
        }

            .container a:active {
                background-color: transparent;
            }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div id="background" class="background" style="display: none;">
    </div>
    <div id="progressBar" class="progressBar" style="display: none;">
        数据处理中，请稍等...W
    </div>
    <div class="nav">
         <%-- <%--<span class="nav_title">游戏中心管理</span> --%> --%> <span class="nav_desc">热门搜索/游戏管理</span>
    </div>
    <div class="wrap" style="text-align: left;display:none;">
        热门游戏
    </div>
    <div id="container" class="container" style="display:none;">
        <ul>
            <li class="show-app-pos add"><a href="PopularGameEdit.aspx?SchemeID=<%=this.SchemeID %>">
                <img src="Images/plus.png" /></a></li>
            <asp:Repeater runat="server" ID="PopularGameList">
                <ItemTemplate>
                    <li class="show-app-pos">
                        <table class="list_info">
                            <tr>
                                <td class="icon" rowspan="2"><a href="PopularGameEdit.aspx?id=<%#Eval("GroupElemID") %>">
                                    <img src="<%#Eval("RecommPicUrl")%>" alt="游戏图标" /></a>
                                    <br />
                                    <a href="PopularGameEdit.aspx?id=<%#Eval("GroupElemID") %>"><%#Eval("RecommTitle")%></a></td>
                                <td style="text-align: right;">状态：
                                <%#BindStatus(this.GetDataItem()) %><br />
                                    <%#BindTime(this.GetDataItem()) %><br />
                                </td>
                            </tr>
                            <tr>
                                <td style="text-align: right; padding-top: 10px; vertical-align: bottom;" colspan="2"><a href="PopularGameEdit.aspx?id=<%#Eval("GroupElemID") %>">更新</a>&nbsp;&nbsp;&nbsp;<asp:LinkButton ID="LinkButton2" runat="server" CommandArgument='<%#Eval("GroupElemID") %>' OnCommand="OnDel" OnClientClick="return confirm('确认删除吗?');">删除</asp:LinkButton></td>
                            </tr>
                        </table>
                    </li>
                </ItemTemplate>
            </asp:Repeater>
        </ul>
    </div>
    <div class="wrap" style="text-align: left;">
        热门搜索词
    </div>
    <div id="container2" class="container">
     
<%--            <li class="show-app-pos add">--%>
                <div> <a href="SearchWordsEdit.aspx?SchemeID=<%=this.SchemeID %>" class="add" id="Add">
                <img src="Images/plus.png" /></a></div>
<%--               </li>--%>
        <div style="clear:both;"></div>
     <ul id="newlist" style="margin-left:245px;margin-top:5px;">
            <asp:Repeater runat="server" ID="SearchWordsList">
                <ItemTemplate>
                    <li class="show-app-pos new" data-id="<%#Eval("GroupElemID") %>">
                        <table class="list_info">
                            <tr>
                                <td class="name" rowspan="2"><a href="SearchWordsEdit.aspx?id=<%#Eval("GroupElemID") %>"><%#Eval("RecommTitle")%></a></td>
                                <td style="text-align: right;">状态：
                                <%#BindStatus(this.GetDataItem()) %><br />
                                    <%#BindTime(this.GetDataItem()) %><br />
                                </td>
                            </tr>
                            <tr>
                                <td style="text-align: right; padding-top: 10px; vertical-align: bottom;" colspan="2"><a href="SearchWordsEdit.aspx?id=<%#Eval("GroupElemID") %>&SchemeID=<%=this.SchemeID %>">更新</a>&nbsp;&nbsp;&nbsp;<asp:LinkButton ID="LinkButton2" runat="server" CommandArgument='<%#Eval("GroupElemID") %>' OnCommand="OnDel" OnClientClick="return confirm('确认删除吗?');">删除</asp:LinkButton></td>
                            </tr>
                        </table>
                    </li>
                </ItemTemplate>
            </asp:Repeater>
        </ul>
               <div style="clear:both;"></div>

    </div>
    <script type="text/javascript">
        var fixHelper = function (e, ui) {
            ui.children().each(function () {
                $(this).width($(this).width());
            });
            return ui;
        };
        jQuery(function () {
            jQuery("#newlist").sortable({
                helper: fixHelper,
                animation: 150,
                delay: 1,
                start: function (e, ui) {
                    return ui;
                },
                stop: function (e, ui) {
                    var items = $('#newlist li');
                    var result = '';
                    for (var i = 0; i < items.length; i++) {
                        var cur_item = $(items[i]);
                        result += cur_item.attr('data-id')+ ":" + (i+1) + ",";
                    }
                    var data = "{\"orderinfo\": \"" + result + "\"}";
                    $.ajax({
                        type: "Post",
                        url: "PopularList.aspx/UpdateOrder",
                        contentType: "application/json; charset=utf-8",
                        data: data,
                        dataType: "json",
                        success: function (resdata) {
                        },
                        error: function (err) {
                        }
                    });
                    return ui;
                }
            });
        });
</script>
</asp:Content>
