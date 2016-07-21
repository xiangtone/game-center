<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="SpecialTopicList.aspx.cs" Inherits="AppStore.Web.SpecialTopicList" %>

<%@ Register Assembly="AspNetPager" Namespace="Wuqi.Webdiyer" TagPrefix="webdiyer" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <%--<link href="theme/css/isotope.css" rel="stylesheet" />--%>
    <link href="theme/css/progressBar.css" rel="stylesheet" />
    <script type="text/javascript" src="javascript/jquery.isotope.min.js"></script>
    <script type="text/javascript" src="javascript/Sortable.js"></script>

    <script type="text/javascript">
        $(function () {
            var ajaxbg = $("#background,#progressBar");
            $(document).ajaxStart(function () {
                ajaxbg.show();
            }).ajaxStop(function () {
                ajaxbg.hide();
            });
            <%if (PageType == "new")
              {%>
            $(".nav_title").hide();
            //$(".nav").prepend('<a href="AppInfoListNew.aspx?Action=1" style="font-size: 13px;">首页</a> &raquo;');
            $(".nav a").css("color", "#1e74c9");
            $(".nav a").css("font-size", "13px");
            $("#Div_Log").show();
            $("input[type='button']").css("background-color", "#49aef5");
            $("input[type='submit']").css("background-color", "#49aef5");
            $("input[type='button']").css("font-weight", "normal");
            $("input[type='submit']").css("font-weight", "normal");
            <%}%>
            //var $container = $('#container');

            //$container.isotope({
            //    itemSelector: '.show-app-pos',
            //    layoutMode: 'fitRows',
            //    //                getSortData: {
            //    //                    id: function ($elem) {
            //    //                        return parseInt($elem.find('.id').val(), 10);
            //    //                    }
            //    //                },
            //    //                sortBy: 'id',
            //    //                sortAscending: true
            //});

            ////            $("#resort").click(function () {
            ////                $('#container').isotope('updateSortData', $('#container').find('.show-app-pos')).isotope({ sortBy: 'id', sortAscending: true });
            ////            });
        });
    </script>
    <style type="text/css">
        table, tr, td {
            margin: 0;
            padding: 0;
            border: 0;

        }

        ul {
            margin: 0px;
            padding: 0px;
            list-style: none;
        }

        #container {
            border: 0px;
        }
        input[type="submit"], input[type="button"] {
        height:30px;
        color: #fff;
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
                /*color: #333;*/
                color: #000;
            }

            #container ul li p {
                /*margin-left: 5px;*/
            }

            #container .add {
                text-align: center;
                background: #e5e5e5 none repeat scroll 0 0;
                /*border-radius: 1.2em;*/
                height: 260px;
                line-height: 10px;
                margin: 5px;
                padding: 5px;
                width: 300px;
            }

                #container .add img {
                    margin-top: 80px;
                }

            #container ul li {
                height: 270px;
                width: 300px;
                margin: 5px;
                /*-webkit-border-radius: 1.2em;
                -moz-border-radius: 1.2em;*/
                /*border-radius: 1.2em;*/
                background: #e5e5e5;
                line-height: 10px;
                /*padding: 5px;*/
                float: left;
            }

            #container ul {
                float: left;
            }

        .add {
            float: left;
        }

        #container ul li .pic {
            text-align: center;
        }

            #container ul li .pic img {
                margin: 0 auto;
                height: 210px;
                max-width: 300px;
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

       .list_info td{
            color: #000;
            
        }

        #container table.list_info {
            width: 100%;
            bottom: 0.5em;
            /*position: absolute;*/
        }

            #container table.list_info td {
                line-height: 1.5em;
                /*padding-top: 3px;*/
                padding-right: 5px;
            }
 .show-app-pos a {
            /*color: rgb(30, 116, 201);*/
            color: #1e74c9;
            text-decoration: none;
        }
            .show-app-pos a:active {
                background-color: transparent;
            }

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
        a {
        text-decoration:none;}
    </style>
    <script type="text/javascript">
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
                $(".list_banner").each(function (index, item) {
                    elemId += ($(this).data("id")) + ",";
                    OrderNo += (index + 1) + ",";
                })
                $.ajax({
                    type: "POST",
                    url: 'SpecialTopicList.aspx?action=ChangeOrdeNo&SchemeID=<%=SchemeID%>',
                    data: {
                        "elemId": elemId,
                        "OrderNo": OrderNo,
                    },
                    success: function (data) {
                        alert(data);
                        location.reload();
                    }
                });
            });
        });
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div id="background" class="background" style="display: none;">
    </div>
    <div id="progressBar" class="progressBar" style="display: none;">
        数据处理中，请稍等...W
    </div>
    <div style="border-bottom: 3px solid #aaa;height:35px;">
        <div class="nav" style="float: left;border:0px;margin-top:5px;">
            <%--<span class="nav_title">游戏中心管理</span> --%> <span class="nav_desc"><a>专题管理</a> </span>
        </div>
        <div style="float: right;">
            <input type="button" id="btnSave" value="保存位置" style="height: 30px;" />
        </div>
    </div>
    <%--    <div class="wrap">
    </div>--%>
    <div id="container">
        <ul id="list-1">
            <asp:Repeater runat="server" ID="DataList">
                <ItemTemplate>
                    <li class="list_banner show-app-pos" data-id="<%#Eval("GroupID") %>" style="">
                        <input class="id" type="hidden" value="<%#Eval("GroupID") %>" />
                        <p class="pic">
                            <img src="<%#Eval("GroupPicUrl") %>" /><%--<a href="SpecialTopicEdit.aspx?id=<%#Eval("GroupID") %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>"></a>--%>
                        </p>
                        <table class="list_info">
                            <tr style="line-height:40px;">
                                <td  style="padding:10px;font-size:15px;"><%#Eval("GroupName")%></td>
                                <td  style="padding:10px;text-align:right;">状态：<%#BindSpecialStatus(this.GetDataItem()) %><br />
                                           <a href="SpecialTopicEdit.aspx?id=<%#Eval("GroupID") %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>&page=<%=PageType %>">编辑</a>&nbsp;&nbsp;&nbsp;
                            <asp:LinkButton ID="LinkButton1" runat="server" CommandArgument='<%#Eval("GroupID") %>' OnCommand="OnDel" OnClientClick="return confirm('确认删除吗?');">删除</asp:LinkButton>

                                </td>
                            </tr>
                         <%--   <tr>
                                <td>上线时间：<%#DateTime.Parse(Eval("CreateTime").ToString()).ToString("yyyy.MM.dd")%></td>
                                <td>
                                    <a href="SpecialTopicEdit.aspx?id=<%#Eval("GroupID") %>&GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>&page=<%=PageType %>">编辑</a>&nbsp;&nbsp;&nbsp;
                            <asp:LinkButton ID="LinkButton2" runat="server" CommandArgument='<%#Eval("GroupID") %>' OnCommand="OnDel" OnClientClick="return confirm('确认删除吗?');">删除</asp:LinkButton>

                                </td>
                            </tr>--%>
                        </table>
                    </li>
                </ItemTemplate>
            </asp:Repeater>
            <li class="show-app-pos add">
                <input class="id" type="hidden" value="999999" />
                <a href="SpecialTopicEdit.aspx?GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>&page=<%=PageType %>">
                    <img src="Images/plus.png" /></a>
            </li>
        </ul>
        <%--<div class="show-app-pos add">
            <input class="id" type="hidden" value="999999" />
            <a href="SpecialTopicEdit.aspx?GroupTypeID=<%=this.GroupTypeID %>&SchemeID=<%=this.SchemeID %>&page=<%=PageType %>">
                <img src="Images/plus.png" /></a>
        </div>--%>
    </div>
    <div style="clear: both;"></div>
    <script type="text/javascript">
        var el = document.getElementById('list-1');
        var sortable = Sortable.create(el, {
            animation: 150,
            filter: ".js-remove",
            handle: ".list_banner",
            onFilter: function (evt) {
                var item = evt.item,
                    ctrl = evt.target;
            }
        });
    </script>

    <div id="Div_Log" style="width: 100%; min-height: 100px; display: none; margin-top: 20px;">
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
