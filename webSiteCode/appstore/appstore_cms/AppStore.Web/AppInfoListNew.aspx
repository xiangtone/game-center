<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="AppInfoListNew.aspx.cs" Inherits="AppStore.Web.AppInfoListNew" %>

<%@ Register Assembly="AspNetPager" Namespace="Wuqi.Webdiyer" TagPrefix="webdiyer" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
        body {
            background-color: #edeef0;
            padding: 20px;
        }

        .updatetbl td {
            line-height: 35px;
            font-size: 15px;
            float: left;
            padding-left: 10px;
        }

        #Div_Log .grid tr td {
            line-height: 30px;
            padding: 0;
        }

        .packcount {
            display: block;
            height: 20px;
            width: 20px;
            background-color: #ce0000;
            border: 2px solid #FFF;
            border-radius: 20px;
            text-align: center;
            position: relative;
            top: 15px;
            right: -55px;
            color: #FFF;
            font-weight: bold;
            line-height: 20px;
        }

        .ui-widget-header {
            background: #49aef5 none repeat scroll 0 0;
        }

        .wrap {
            background-color: #edeef0;
            padding: 5px;
            border: none;
        }

        .pages .cpb {
            background: #49aef5 none repeat scroll 0 0;
        }

        .pages a:hover {
            background: #49aef5 none repeat scroll 0 0;
            cursor: pointer;
        }

        .icon {
            border-radius: 10px;
        }

        .operr {
            color: #1e74c9;
            text-decoration: none;
        }

        input[type="submit"], input[type="button"] {
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

        td {
            text-align: center;
        }

        a {
            color: #1e74c9;
            text-decoration: none;
        }

        .grid {
            border-bottom: 0;
            border-top: 0;
        }

        .nav {
            border-bottom: 1px solid #e5e5e5;
            /*cursor:progress;
            cursor:wait;*/
        }
    </style>
    <script type="text/javascript">

        $(function () {
           <% if (actype == 2)
              {%>
            $.ajax({
                type: "POST",
                url: '/LeftMenu_GameCenter.aspx?Action=2'
            });
               <%} if (actype == 5)
              {%>
            $("#Div_Log").hide();
              <%}%>
            $(".GetOperateRecord").click(function () {
                $.ajax({
                    type: "POST",
                    url: 'AppInfoListNew.aspx?ac=getrecord&id=' + $(this).attr("data-id"),
                    dataType: "json",
                    beforeSend: function () {
                        DlgIfrm("OperateRecord");
                        $("#tble").html("");
                        $("body").css("cursor", "progress");
                    },
                    success: function (data) {
                        $("body").css("cursor", "");
                        $("#tble").html("");//清空info内容
                        $.each(data, function (i, item) {
                            var reas = "";
                            if (item.reason != "") {
                                reas = "<br/> 原因：" + item.reason + "";
                            }
                            $("#tble").append(
                                    "<tr><td>" + item.OperateTime + "</td>" +
                                    "<td>" + item.UserName + "</td>" +
                                    "<td>" + item.OperateExplain + reas + "</td><tr/>");
                        });
                        DlgIfrm("OperateRecord");
                    }
                });
            });
            $(".del_appInfo").click(function () {
                if ($(this).attr("data-status") == "1") {
                    var id = $(this).attr("data-id");
                    $.ajax({
                        type: "POST",
                        url: 'AppInfoListNew.aspx?ac=isexist&id=' + id,
                        beforeSend: function () {
                            $("body").css("cursor", "progress");
                        },
                        success: function (data) {
                            $("body").css("cursor", "");
                            if (data == "1") {
                                if (confirm("推荐中存在该应用,确定要下架吗？")) {
                                    DlgIfrm("reason");
                                    $("#BtnDel").attr("data-id", id);
                                    $("#BtnDel").attr("data-status", "");
                                }
                            }
                            else {
                                DlgIfrm("reason");
                                $("#BtnDel").attr("data-id", id);
                                $("#BtnDel").attr("data-status", "");
                            }
                        }
                    });
                }
                else if ($(this).attr("data-status") == "2") {
                    if (confirm("确认要重新上架吗?")) {
                        $.ajax({
                            type: "POST",
                            url: 'AppInfoListNew.aspx?ac=update&actype=<%=actype %>&status=1&id=' + $(this).attr("data-id"),
                            beforeSend: function () {
                                $("body").css("cursor", "progress");
                            },
                            success: function (data) {
                                window.location.reload();
                            }
                        });
                    }
                }
            });
            $("#BtnClose").click(function () {
                $(".ui-dialog-titlebar-close").click();
            });
            $("#BtnDel").click(function () {
                var type = $(this).attr("data-status")
                $.ajax({
                    type: "POST",
                    url: 'AppInfoListNew.aspx?ac=update&actype=<%=actype %>&status=2&type=' + type + '&id=' + $(this).attr("data-id") + '&reason=' + $("#txtReason").val(),
                    beforeSend: function () {
                        $("body").css("cursor", "progress");
                    },
                    success: function (data) {
                        window.location.reload();
                    }
                });
            });
            $(".next").click(function () {
                if ($(this).attr("data-status") == "4") {
                    if (confirm("点击确定将游戏状态修改为测试中?")) {
                        $.ajax({
                            type: "POST",
                            url: 'AppInfoListNew.aspx?ac=update&actype=<%=actype %>&status=5&id=' + $(this).attr("data-id"),
                            beforeSend: function () {
                                $("body").css("cursor", "progress");
                            },
                            success: function (data) {
                                window.location.reload();
                            }
                        });
                    }
                }
                else if ($(this).attr("data-status") == "5") {
                    if (confirm("点击确定将提交游戏审核?")) {
                        $.ajax({
                            type: "POST",
                            url: 'AppInfoListNew.aspx?ac=update&actype=<%=actype %>&status=6&id=' + $(this).attr("data-id"),
                            beforeSend: function () {
                                $("body").css("cursor", "progress");
                            },
                            success: function (data) {
                                $("body").css("cursor", "");
                                if (data == "2") {
                                    alert("请完善安装包信息之后再提交审核")
                                }
                                else {
                                    window.location.reload();
                                }
                            }
                        });
                    }
                }
                else if ($(this).attr("data-status") == "7") {
                    if (confirm("点击确定将提交游戏审核?")) {
                        $.ajax({
                            type: "POST",
                            url: 'AppInfoListNew.aspx?ac=update&actype=<%=actype %>&status=6&id=' + $(this).attr("data-id"),
                            beforeSend: function () {
                                $("body").css("cursor", "progress");
                            },
                            success: function (data) {
                                window.location.reload();
                            }
                        });
                    }
                }
            });
            $(".last").click(function () {
                if ($(this).attr("data-status") == "5") {
                    if (confirm("点击确定将游戏状态修改为接入中?")) {
                        $.ajax({
                            type: "POST",
                            url: 'AppInfoListNew.aspx?ac=update&actype=<%=actype %>&status=4&id=' + $(this).attr("data-id"),
                            beforeSend: function () {
                                $("body").css("cursor", "progress");
                            },
                            success: function (data) {
                                window.location.reload();
                            }
                        });
                    }
                }
                else if ($(this).attr("data-status") == "6") {
                    if (confirm("点击确定将游戏状态修改为测试中?")) {
                        $.ajax({
                            type: "POST",
                            url: 'AppInfoListNew.aspx?ac=update&actype=<%=actype %>&status=5&id=' + $(this).attr("data-id"),
                            beforeSend: function () {
                                $("body").css("cursor", "progress");
                            },
                            success: function (data) {
                                $("body").css("cursor", "");
                                window.location.reload();
                            }
                        });
                    }
                }
                else if ($(this).attr("data-status") == "7") {
                    if (confirm("点击确定将游戏状态修改为测试中?")) {
                        $.ajax({
                            type: "POST",
                            url: 'AppInfoListNew.aspx?ac=update&actype=<%=actype %>&status=5&id=' + $(this).attr("data-id"),
                            beforeSend: function () {
                                $("body").css("cursor", "progress");
                            },
                            success: function (data) {
                                window.location.reload();
                            }
                        });
                    }
                }
            })
            $(".through").click(function () {
                if (confirm("确定该游戏审核通过?")) {
                    $.ajax({
                        type: "POST",
                        url: 'AppInfoListNew.aspx?ac=update&actype=<%=actype %>&status=1&type=th&id=' + $(this).attr("data-id"),
                        beforeSend: function () {
                            $("body").css("cursor", "progress");
                        },
                        success: function (data) {
                            window.location.reload();
                        }
                    });
                }
            });
            $(".notthrough").click(function () {
                if (confirm("确定该游戏审核不通过?")) {
                    DlgIfrm("reason");
                    $("#BtnDel").attr("data-id", $(this).attr("data-id"));
                    $("#BtnDel").attr("data-status", "th");
                }
            });

            $(".update").click(function () {
                cler();
                var id = $(this).attr("data-id");
                var name = $(this).attr("data-name");
                var arch = $(this).attr("data-arch");
                var status = $(this).attr("data-status");
                $("#BtnUpdate").attr("data-id", id);
                $("#updateinfo_name").html(name);
                $("#updateinfo_status").find("option").each(function () {
                    if ($(this).val() == status) {
                        $(this).attr("selected", "selected")
                    }
                });
                var boxes = document.getElementsByName("updateinfo_arch");
                for (i = 0; i < boxes.length; i++) {
                    if (arch == 3) {
                        boxes[i].checked = true;
                    } else {
                        if (boxes[i].value == arch) {
                            boxes[i].checked = true;
                        }
                        else {
                            boxes[i].checked = false;
                        }
                    }
                }
                var all_options = document.getElementById("updateinfo_status").options;
                for (i = 0; i < all_options.length; i++) {
                    if (all_options[i].value == status) {
                        all_options[i].selected = true;
                    }
                    else {
                        all_options[i].selected = false;
                    }
                }

                DlgIfrm("UpdateAppInfo");
            });
            $("#BtnUpdate").click(function () {
                getarch()
                if (confirm("确定修改该游戏状态信息?")) {
                    $.ajax({
                        type: "POST",
                        url: 'AppInfoListNew.aspx?ac=updateappinfo&actype=<%=actype %>&status=' + $("#updateinfo_status").val() + '&id=' + $(this).attr("data-id") + '&arch=' + getarch() + '&reason=' + $("#updateinfo_Reason").val(),
                        beforeSend: function () {
                            $("body").css("cursor", "progress");
                        },
                        success: function (data) {
                            $("#BtnUpdateClose").click();
                            alert(data);
                            window.location.reload();
                        }
                    });
                }

            });
            $("#BtnUpdateClose").click(function () {
                $(".ui-dialog-titlebar-close").click();

            });

        });

        function getarch() {
            var a = 0;
            $("input[name='updateinfo_arch']:checkbox:checked").each(function () {
                a += parseInt($(this).val());
            })
            return a;
        }
        function cler() {
            $("#BtnUpdate").attr("data-id", "0");
            $("#updateinfo_name").html("");
            //$("input[name='updateinfo_arch']:checkbox").removeAttr("checked");
            //$("#updateinfo_status").find("option").removeAttr("selected");
            //$("#updateinfo_status").find("option").removeAttr("selected");
        }
        function formatDate(now) {
            var year = now.getYear();
            var month = now.getMonth() + 1;
            var date = now.getDate();
            var hour = now.getHours();
            var minute = now.getMinutes();
            var second = now.getSeconds();
            return year + "-" + month + "-" + date + "   " + hour + ":" + minute + ":" + second;
        }
    </script>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div id="background" class="background" style="display: none;">
    </div>
    <div id="progressBar" class="progressBar" style="display: none;">
        数据处理中，请稍等...
    </div>
   
    <div class="nav">
        <span class="nav_desc"><a href="AppInfoListNew.aspx?Action=<%=Action %>" style="font-size: 13px;">首页</a> &raquo;
            <a href="AppInfoListNew.aspx?Action=<%=actype %>"><% =BindTitle(actype) %></a>
        </span>
    </div>
    <div class="wrap" style="float: left;">
        排序方式：
               <asp:DropDownList ID="OrderType" runat="server" AutoPostBack="false" Width="120">
                   <asp:ListItem Value="CreateTime">添加时间</asp:ListItem>
                   <asp:ListItem Value="UpdateTime">更新时间</asp:ListItem>
                   <asp:ListItem Value="DownTimes">下载次数</asp:ListItem>
               </asp:DropDownList>
        &nbsp;&nbsp;&nbsp;&nbsp; ｜&nbsp;&nbsp;&nbsp;&nbsp;
        搜索方式：
            <asp:DropDownList ID="SearchType" runat="server">
                <asp:ListItem Value="0">游戏名称</asp:ListItem>
                <asp:ListItem Value="1">开发者名称</asp:ListItem>
                <asp:ListItem Value="2">游戏ID</asp:ListItem>
                <asp:ListItem Value="3">包名</asp:ListItem>
            </asp:DropDownList>
        &nbsp;&nbsp;
        搜索内容：
        <asp:TextBox ID="Keyword_2" runat="server"></asp:TextBox>

        &nbsp;&nbsp;
        <asp:Button ID="btnSearch" runat="server" Text="搜索" CssClass="btn" OnClick="btnSearch_Click" />
    </div>
    <%if (actype == 1)
      {%>
    <div style="float: right; padding: 5px;">
        <asp:Button ID="btnExport" runat="server" Text="导出全部游戏" CssClass="btn" OnClick="btnExport_Click" />

    </div>
    <%} %>
    <%else
      {
          if (Count > 0)
          {%>
    <div style="float: right; padding: 5px;">
        <asp:Button ID="btnExport2" runat="server" Text="导出游戏" CssClass="btn" OnClick="btnExport2_Click" />

    </div>
    <% }
          else
          {%>
    <div style="float: right; padding: 5px;">
        <asp:Button ID="Button1" runat="server" Text="导出游戏" Enabled="false" EnableViewState="false" BackColor="#cccccc" />

    </div>
    <%}
      }%>

    <asp:Repeater ID="objRepeaterData" runat="server">
        <HeaderTemplate>
            <table style="width: 100%" class="grid">
                <thead>
                    <tr>
                        <th style="width: 70px;">图标</th>
                        <th>名称</th>
                        <th>游戏状态</th>
                        <th>游戏类型</th>
                        <th>合作类型</th>
                        <th>游戏公司信息</th>
                        <th>主安装包名</th>
                        <th>主版本</th>
                        <th>机型适配</th>
                        <th>安装包管理</th>
                        <th><%if (actype == 5)
                              {%>
                             审核不通过原因
                             <% }
                              else
                              {%>
                                  游戏操作
                             <% } %></th>
                        <th>建档日期</th>
                        <th>更新日期</th>

                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                <td style="width: 60px;">
                    <span class="packcount"><%#Eval("PackCount") %></span>
                    <img src='<%#Eval("MainIconUrl") %>' width="60" height="60" class="icon" /><br />
                    <%#Eval("AppID") %>
                </td>
                <td><span style="width: 60px; overflow: hidden"><%#Eval("ShowName") %></span></td>

                <td><%# BindStatus(Eval("status")) %></td>
                <td><%#Eval("AppTypeName") %></td>
                <td><%#BindType(Eval("CoopType")) %>
                </td>
                <td><%#Eval("DevName") %></td>


                <td><%#Eval("PackName") %></td>
                <td><%#Eval("MainVerName") %></td>

                <td><%#BindArch(Eval("Architecture")) %></td>


                <td>
                    <a href="PackInfoList.aspx?AppID=<%#Eval("AppID") %>&ShowName=<%#Eval("ShowName") %>&type=app&page=new">查看详情</a><br />
                    <a class="" href="<%# BindPackUrl(Eval("MainPackID"))%>" style="color: #1e74c9;">下载游戏</a>
                </td>


                <td>
                    <%if (actype == 5)
                      {%>
                    <%#BindReason(Eval("status"),Eval("AppID"))%>
                    <% }
                      else
                      {%>
                    <% if (actype == 1 || actype == 4)
                       {
                    %>
                    <a class="update" data-id="<%#Eval("AppID") %>" data-name="<%#Eval("ShowName") %>" data-status="<%#Eval("status")%>" data-arch="<%#Eval("Architecture") %>" style="cursor: pointer; color: #1e74c9;">修改信息</a>
                    <br />
                    <%
                       }
                       else if (actype == 3)
                       { 
                    %>
                    <span class="last " style="cursor: pointer; color: #1e74c9;" data-id="<%#Eval("AppID") %>" data-status="<%#Eval("status") %>"><%# BindLast(Eval("status")) %> </span>
                    <span class="next " style="cursor: pointer; color: #1e74c9;" data-id="<%#Eval("AppID") %>" data-status="<%#Eval("status") %>"><%# BindLast2(Eval("status")) %></span><br />
                    <% }
                       else if (actype == 2)
                       {
                    %>
                    <span class="through " style="cursor: pointer; color: #1e74c9;" data-id="<%#Eval("AppID") %>">审核通过</span><br />
                    <span class="notthrough " style="cursor: pointer; color: #1e74c9;" data-id="<%#Eval("AppID") %>"><%# BindThrough(Eval("status")) %></span><br />

                    <%} %>

                    <a style="cursor: pointer; display: none;" class="operr GetOperateRecord" data-id="<%#Eval("AppID") %>">操作记录</a>
                    <% } %>
                   
                </td>
                <td><%#((DateTime) Eval("CreateTime")).ToString("yyyy-MM-dd") %></td>
                <td><%#((DateTime) Eval("UpdateTime")).ToString("yyyy-MM-dd") %></td>
            </tr>

        </ItemTemplate>
        <FooterTemplate>
            </table>
        </FooterTemplate>

    </asp:Repeater>

    <div id="OperateRecord" style="display: none; width: 600px; height: 300px; padding-top: 10px;" title="操作记录">
        <table class="grid" id="tble">
        </table>
    </div>
    <div id="reason" style="width: 350px; height: 250px; padding-top: 10px; display: none;" title="请输入原因">

        <div style="margin: 10px; width: 310px;">
            <textarea id="txtReason" rows="7" cols="40" style="margin: 10px;"></textarea>
            <div style="float: right; margin-right: 0px;">
                <input type="button" id="BtnClose" value="取消" />
                <input type="button" id="BtnDel" data-id="0" value="确定" />
            </div>
        </div>
    </div>

    <div id="UpdateAppInfo" style="width: 400px; height: 320px; padding-top: 10px; display: none;" title="游戏修改">
        <div style="margin: 10px; width: 360px;">
            <table class="updatetbl" style="width: 100%;">
                <tr>
                    <td>游戏名称：</td>
                    <td><span id="updateinfo_name" style="font-size: 15px;"></span></td>
                </tr>
                <tr>
                    <td>机型适配：</td>
                    <td>
                        <input type="checkbox" name="updateinfo_arch" id="ch_arch1" value="1" />
                        Arm
                        <input type="checkbox" name="updateinfo_arch" id="ch_arch2" value="2" />
                        X86</td>
                </tr>
                <tr>
                    <td>游戏状态:</td>
                    <td style="padding-top: 5px; width: 120px;">
                        <select id="updateinfo_status" style="width: 100px;">
                            <option value="1">上架</option>
                            <option value="2">下架</option>
                        </select></td>
                </tr>
                <tr>
                    <td>操作原因:</td>
                    <td>
                        <textarea id="updateinfo_Reason" rows="3" cols="20" style="margin: 10px;"></textarea></td>
                </tr>
            </table>

            <div style="float: right; margin: 30px 30px 0px 0px;">
                <input type="button" id="BtnUpdate" data-id="0" value="保存" />
                <input type="button" id="BtnUpdateClose" value="取消" />

            </div>
        </div>
    </div>
    <div class="wrap">
        <webdiyer:AspNetPager runat="server" ID="pagerList" OnPageChanged="pagerList_PageChanged" CssClass="pages" CurrentPageButtonClass="cpb"
            PagingButtonSpacing="0px" NumericButtonCount="10" PageSize="20" AlwaysShow="true">
        </webdiyer:AspNetPager>
    </div>

    <asp:Repeater ID="objRepeaterExport" runat="server" OnItemDataBound="objRepeaterExport_ItemDataBound">
        <HeaderTemplate>
            <table>
                <thead>
                    <tr>
                        <th>名称</th>
                        <th>包名</th>
                        <th>版本</th>
                        <th>游戏类型</th>
                        <th>开发者</th>
                        <th>合作类型</th>
                        <th>游戏状态</th>
                        <th>游戏权限调用</th>
                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr>
                <asp:Literal runat="server" ID="LiteralItem"></asp:Literal>
            </tr>
        </ItemTemplate>
        <FooterTemplate>
            </table>
        </FooterTemplate>
    </asp:Repeater>
    <div id="Div_Log">
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
                    <td><%#BindCont(Eval("OperateContent"),Eval("reason")) %></td>
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
