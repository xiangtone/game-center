<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="GroupInfo.aspx.cs" Inherits="AppStore.Web.GroupInfo" %>

<%@ Register Assembly="AspNetPager" Namespace="Wuqi.Webdiyer" TagPrefix="webdiyer" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
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
            right: -45px;
            color: #FFF;
            font-weight: bold;
            line-height: 20px;
        }
        .grid tr td, .grid tr th{
        padding:0 15px 10px 15px;}
        .icon {
            border-radius: 10px;
        }

        th, td {
            text-align: center;
        }    a {
            text-decoration: none;
        }input[type="submit"], input[type="button"] {
        height:30px;  color: #fff;}
    </style>
    <script type="text/javascript">

        $(function () {
            <%if (PageType == "new")
              {%>
            $(".nav_title").hide();
            //$(".nav").prepend('<a href="AppInfoListNew.aspx?Action=1" style="font-size: 13px;">首页</a> &raquo;');
            $(".nav a").css("color", "#1e74c9");
            $(".nav a").css("font-size", "13px");
            //$(".wrap").hide();
            $("#Div_Log").show();
            $("input[type='button']").css("background-color", "#49aef5");
            $("input[type='submit']").css("background-color", "#49aef5");
            $("input[type='button']").css("font-weight", "normal");
            $("input[type='submit']").css("font-weight", "normal");
            <%} %>
        });
    </script>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div id="background" class="background" style="display: none;">
    </div>
    <div id="progressBar" class="progressBar" style="display: none;">
        数据处理中，请稍等...
    </div>
    <div class="nav">
        <span class="nav_desc"><%--<a>首页</a> &raquo;&nbsp;&nbsp;--%> <a href="GroupInfo.aspx?SchemeID=<%=SchemeID %>&page=<%=PageType %>">分类管理</a></span>
    </div>

    <div style="text-align: right; padding: 5px; height: 30px; width: 100%;">
        <asp:Button ID="Button1" runat="server" Text="导出分类列表" CssClass="btn" Visible="false" OnClick="Button1_Click" />
    </div>
    <div class="wrap" style="display:none">
        搜索类型：
           <asp:DropDownList ID="GroupType" runat="server" AutoPostBack="false">
               <asp:ListItem Value="0" Selected="True">全部</asp:ListItem>
               <asp:ListItem Value="10">应用游戏</asp:ListItem>
               <asp:ListItem Value="11">应用分类</asp:ListItem>
               <asp:ListItem Value="12">游戏分类</asp:ListItem>
               <asp:ListItem Value="21">网游单机</asp:ListItem>
               <asp:ListItem Value="31">专题</asp:ListItem>
               <asp:ListItem Value="41">推荐</asp:ListItem>
               <asp:ListItem Value="51">分发</asp:ListItem>
           </asp:DropDownList>
        ｜分类状态：
               <asp:DropDownList ID="dropStatus" runat="server" AutoPostBack="false">
                   <asp:ListItem Value="">全部</asp:ListItem>
                   <asp:ListItem Value="1">启用</asp:ListItem>
                   <asp:ListItem Value="0">禁用</asp:ListItem>
               </asp:DropDownList>

        搜索内容：<input runat="server" id="Keyword_2" type="text" />

        <asp:Button ID="btnSearch" runat="server" Text="搜索" CssClass="btn" OnClick="btnSearch_Click" />｜排序方式：
               <asp:DropDownList ID="OrderType" runat="server" AutoPostBack="false">
                   <asp:ListItem Value="CreateTime">添加时间</asp:ListItem>
                   <asp:ListItem Value="UpdateTime">更新时间</asp:ListItem>
               </asp:DropDownList>
        ｜
            <input
                runat="server" id="btnAdd" type="button" value="新增" onclick="return window.location.href = 'GroupInfoEdit.aspx?PageType=addGroup&GroupID=0';" />


    </div>
    <asp:Repeater ID="objRepeater" runat="server">
        <HeaderTemplate>
            <table style="width: 100%" class="grid">
                <thead>
                    <tr>
                        <th>分类ID</th>
                        <th style="width: 70px;">分类图标</th>
                        <th>分类名称</th>
                        <th>分类类型名称</th>
                        <th>分类状态</th>
                        <th>排序类型</th>
                        <%if (PageType != "new")
                          {%>
                        <th>应用操作</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <%} %>

                        <th>建档日期</th>
                        <th>更新日期</th>
                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                <td><%#Eval("GroupID") %>
                </td>

                <td style="width: 60px;">
                    <span class="packcount"><%#Eval("ElementCount") %></span>
                    <img src='<%#Eval("GroupPicUrl") %>' width="60" height="60" class="icon" />

                </td>
                <td>
                    <a href="GroupElement.aspx?GroupID=<%#Eval("GroupID") %>&type=groupinfo&GroupName=<%#(Eval("GroupName"))%>&SchemeID=<%=SchemeID%>&page=<%=PageType %>"><%#(Eval("GroupName"))%></a>
                </td>
                <td>
                    <%#Eval("TypeName") %>
                </td>
                <td><%#Convert.ToInt32(Eval("Status")) == 1 ? "启用" : "禁用" %></td>
                <td><%#Convert.ToInt32(Eval("OrderType")) == 0 ? "默认" : "时间" %></td>
                <%if (PageType != "new")
                  {%>
                <td>
                    <a href="GroupInfoEdit.aspx?PageType=addGroup&GroupID=0" style="line-height: 60px;">添加</a>
                    <a href="GroupInfoEdit.aspx?PageType=editGroup&GroupID=<%#Eval("GroupID") %>" style="line-height: 60px;">修改</a>
                </td>
                <td><%#((DateTime)Eval("StartTime")).ToString("yyyy-mm-dd")%></td>
                <td><%#((DateTime)Eval("EndTime")).ToString("yyyy-mm-dd") %></td>
                <%} %>


                <td><%#((DateTime) Eval("CreateTime")).ToString("yyyy-MM-dd") %></td>
                <td><%#((DateTime) Eval("UpdateTime")).ToString("yyyy-MM-dd") %></td>
            </tr>

        </ItemTemplate>
        <FooterTemplate>
            </table>
        </FooterTemplate>

    </asp:Repeater>
    <div class="wrap">
        <webdiyer:AspNetPager runat="server" ID="pagerList" OnPageChanged="pagerList_PageChanged" CssClass="pages" CurrentPageButtonClass="cpb"
            PagingButtonSpacing="0px" NumericButtonCount="10" PageSize="20" AlwaysShow="true">
        </webdiyer:AspNetPager>
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
