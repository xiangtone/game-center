<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="GroupElement.aspx.cs" Inherits="AppStore.Web.GroupElement" %>

<%@ Register Assembly="AspNetPager" Namespace="Wuqi.Webdiyer" TagPrefix="webdiyer" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
<%--    <link href="theme/css/isotope.css" rel="stylesheet" />--%>
    <link href="theme/css/progressBar.css" rel="stylesheet" />
    <script src="javascript/jquery.isotope.min.js"></script>


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

            <%if (PageType == "new")
              {%>
            $(".nav_title").hide();
            //$(".nav").prepend('<a href="AppInfoListNew.aspx?Action=1" style="font-size: 13px;">首页</a> &raquo;');
            $(".nav a").css("color", "#1e74c9");
            $(".nav a").css("font-size", "13px");
            $("input[type='button']").css("background-color", "#49aef5");
            $("input[type='submit']").css("background-color", "#49aef5");
             $("input[type='button']").css("font-weight", "normal");
             $("input[type='submit']").css("font-weight", "normal");
            <%
                  if (GroupID == 93 || GroupID == 94)
                  {%>
            //$(".wrap").hide();
            $("#Div_Log").show();

            <%}
              }%>
           <% if (type == "groupinfo")
              {%>
            $(".nav").prepend('<a href="GroupInfo.aspx?SchemeID=104&page=new" style="font-size: 13px;color:rgb(30, 116, 201);">分类管理</a> &raquo;');
            <%
                  
              }%>
        });
    </script>
    <style type="text/css">
        a {
            text-decoration: none;
        }input[type="submit"], input[type="button"] {
        height:30px;  color: #fff;}

        ul {
            margin: 0px;
            padding: 0px;
            list-style: none;
        }

        #container ul li p .red {
            color: Red;
        }

        #container ul li p .blue {
            color: Blue;
        }

        #container ul li p .green {
            color: green;
        }

        #container ul li p .white {
            /*color: white;*/
        }

        #container ul li p .black {
            color: black;
        }

        #container ul li p {
            margin-left: 5px;
        }

        #container ul li.add {
            text-align: center;
        }

            #container ul li.add img {
                margin-top: 20px;
            }

        #container ul li {
            height: 170px;
            width: 250px;
            margin: 5px;
            /*-webkit-border-radius: 1.2em;
            -moz-border-radius: 1.2em;*/
            /*border-radius: 1.2em;*/
            background: #e5e5e5;
            line-height: 10px;
            padding: 5px;
        }

            #container ul li .time {
                position: absolute;
                font-size: 12px;
                /*color: hsla(0, 100%, 100%, 0.8);
                color: white;*/
                right: 0.5em;
                top: 2em;
                width: 120px;
                height: 20px;
                line-height: 20px;
                padding: 5px;
            }

            #container ul li .name {
                left: 0.5em;
                bottom: 1.6em;
                font-size: 1.05em;
                font-weight: bold;
                /*color: hsla(0, 100%, 100%, 1);
                color: white;*/
                margin: 8px 0 2px 5px;
            }

            #container ul li .weight {
                font-size: 0.9em;
                left: 0.5em;
                bottom: 0.5em;
                /*color: hsla(0, 100%, 100%, 0.8);
                color: white;*/
                line-height: 1.5em;
                margin-bottom: 3px;
            }
        .show-app-pos p {
        
        /*color:#000;*/

        }
        .show-app-pos a {
            /*color: rgb(30, 116, 201);*/
            color: #1e74c9;
            text-decoration: none;
        }

            .show-app-pos a:active {
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
    <div style="border-bottom: 3px solid #aaa;width:99%;padding:5px">
        <div class="nav" style="border:0px;float:left;margin-top:5px;">
            <%--<span class="nav_title">游戏中心管理</span>--%> <span class="nav_desc"><%--<a href="GroupInfo.aspx?SchemeID=<%=SchemeID %>&page=<%=PageType %>">分组管理  &raquo;--%>
                <a href="GroupElement.aspx?GroupID=<%=this.GroupID %>&SchemeID=<%=SchemeID %>&page=<%=PageType %>&GroupName=<%=this.GroupName %>"><%=this.GroupName %></a><%--&raquo;分组元素管理--%></span>
        </div>
         <div style="text-align: right; padding: 5px; height: 30px; width: 100%;padding-right:10px;">
            <asp:Button ID="Button1" runat="server" Text="导出列表" CssClass="btn" OnClick="Button1_Click" Visible="false" />
        </div>
    </div>
    <div id="container" style="width:99%;">

        <p style="clear: both;"></p>
        <ul>
            <li class="show-app-pos add" url="#"><a href="GroupElementAdd.aspx?GroupID=<%=this.GroupID %>&GroupName=<%=this.GroupName %>&SchemeID=<%=SchemeID %>&page=<%=PageType %>">
                <image src="Images/plus.png" />
            </a></li>
            <asp:Repeater runat="server" ID="DataList1">
                <ItemTemplate>
                    <li class="show-app-pos" url="#">
                        <p class="name"><%--<a href="AppInfoAdd.aspx?id=<%#Eval("GroupElemID") %>">--%><%#Eval("RecommTitle") %><%--</a>--%></p>
                        <%-- <a href="BeginnerRecommendEdit.aspx?id=<%#Eval("GroupElemID") %>">--%>
                        <img src="<%#Eval("RecommPicUrl") %>" width="90" height="90" style="margin: 3px 0 3px 5px;" />
                        <%-- </a>--%>
                        <p class="time"><%#Eval("RecommWord") %></p>

                        <p class="weight">

                            <%#BindTimeStatus(this.GetDataItem()) %>
                            <br />
                            <%#BindTime(this.GetDataItem()) %>
                        </p>
                        <p style="text-align: right;">
                            <a href="GroupElementAdd.aspx?GroupID=<%=this.GroupID %>&GroupElementID=<%#Eval("GroupElemID") %>&GroupName=<%=this.GroupName %>&SchemeID=<%=SchemeID %>&page=<%=PageType %>">编辑</a>&nbsp;<asp:LinkButton ID="LinkButton1" runat="server" CommandArgument='<%#Eval("GroupElemID") %>' OnCommand="OnDel" OnClientClick="return confirm('确认删除吗?');">删除</asp:LinkButton>
                        </p>

                    </li>
                </ItemTemplate>
            </asp:Repeater>

        </ul>
    </div>

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
