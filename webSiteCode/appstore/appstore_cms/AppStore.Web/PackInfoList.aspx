<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="PackInfoList.aspx.cs" Inherits="AppStore.Web.PackInfoList" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
        a {
            text-decoration: none;
        }

        .ui-widget-header {
            background: #49aef5 none repeat scroll 0 0 !important;
        }

        .grid tr td {
            padding: 5px;
        }

        .selpermission {
            color: #a66;
            cursor: pointer;
        }

        #div_gridper {
            margin: 20px;
            font-size: 16px;
            text-align: center;
        }

        #grid_per {
            border: 1px solid #eaeaea;
            border-bottom: 0;
        }

            #grid_per tr {
                border-bottom: 1px solid #eaeaea;
            }

                #grid_per tr td {
                    width: 360px;
                    font-size: 15px;
                    line-height: 25px;
                    text-align: center;
                    border-bottom: 1px solid #eaeaea;
                }

        td {
            text-align: center;
        }

        #ul_gridper li {
            font-size: 15px;
            line-height: 25px;
            list-style: none;
            text-align: center;
        }
    </style>
    <script type="text/javascript">
        
        function selper(id) {
            $("#ul_gridper").html("");
            $.ajax({
                type: "POST",
                url: 'PackInfoList.aspx?ac=permission&id=' + id,
                success: function (data) {
                    var strs = new Array(); //定义一数组
                    strs = data.split(","); //字符分割
                    for (i = 0; i < strs.length ; i++) {
                        $("#grid_per").append("<tr><td>" + strs[i] + "</td></tr>"); //分割后的字符输出
                    }

                    DlgIfrm("permission");
                }

            });
        }
        function updatever(name, pid, aid) {
            if (confirm("确定要把该安装包设为主版本安装包吗？")) {
                $.ajax({
                    type: "GET",
                    url: 'PackInfoList.aspx?ac=updatever&page=new&ShowName='+name+'&Packid='+pid+'&Appid='+aid,
                    success: function (data) {
                        alert(data);
                        window.location.reload();
                    }

                });
            }
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">


    <%if (PageType != "new")
      {%>
    <div class="nav">
        <span class="nav_title">应用商店管理</span>
        <span class="nav_desc">首页
             &raquo; 
                <%=this.ShowName %>

              &raquo; 安装包管理</span>
    </div>
    <% }
      else
      {
    %>
    <%--         <span class="nav_title" style="color:#1e74c9;">游戏中心管理</span>--%>
    <div>
        <span class="nav_desc" style="color: #1e74c9;"><a href="AppInfoListNew.aspx" style="color: #1e74c9;" >首页</a>
            &raquo; 
                <%=this.ShowName %></span>
    </div>
    <br />
    <%} %>


    <%if (PageType != "new")
      {%>
    <div class="wrap">
        <a class="btn" href="PackInfoAdd.aspx?appID=<%=this.AppID %>&ShowName=<%=this.ShowName %>&type=<%=this.Type %>">新增</a>
    </div>
    <% } %>
    <asp:Repeater ID="objRepeater" runat="server">
        <HeaderTemplate>
            <table style="width: 100%" class="grid">
                <thead>
                    <tr>
                        <th>安装包ID</th>
                        <th>图标</th>
                        <th>名称</th>
                        <th>状态</th>

                        <th>主版本</th>

                        <th>文件大小</th>

                        <%if (PageType != "new")
                          {%>
                        <th>下载数</th>
                        <th>评论数</th>
                        <th>评分</th>
                        <th width="200">下载地址</th>

                        <% }
                          else
                          {%>
                        <th>包名</th>
                        <th>版本</th>
                        <th>游戏权限调用</th>

                        <%} %>
                        <th>建档日期</th>
                        <th>更新日期</th>

                        <th>操作</th>


                    </tr>
                </thead>
        </HeaderTemplate>
        <ItemTemplate>
            <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                <td>

                    <%#Eval("PackID")%>
                </td>
                <td style="width: 60px;">
                    <img src='<%#Eval("IconUrl") %>' width="60" height="60" />
                    <br />
                </td>
                <td><%#Eval("ShowName") %>
                </td>
                <td>
                    <%#Convert.ToInt32( Eval("Status")) == 1 ? "启用" : "禁用" %>
                </td>

                <td>

                    <%# Convert.ToInt32(Eval("IsMainVer"))==1?"是":"否" %>
                </td>




                <td>
                    <%# Math.Round( Convert.ToDecimal( Eval("PackSize"))/1024/1024,2) %> M
                </td>

                <%if (PageType != "new")
                  {%>
                <td>
                    <%#Eval("DownTimes") %>
                </td>
                <td>
                    <%#Eval("CommentTimes") %>
                </td>
                <td>
                    <%#Eval("CommentScore") %>
                </td>

                <td>

                    <input type="text" value="<%#Eval("PackUrl") %>" size="25" style="border: none;" />
                </td>

                <% }
                  else
                  {%>
                <td>
                    <%#Eval("PackName") %>
                </td>
                <td>
                    <%#Eval("VerName") %>
                </td>
                <td>
                    <div style="width: 200px; height: auto;">
                        <%# BindPermission(Eval("permission").ToString(),Eval("PackID")) %>

                        <%--<%#Eval("permission").ToString().Replace("#","<br />").Replace(",","<br />") %>--%>
                    </div>
                </td>

                <%} %>

                <td><%#((DateTime) Eval("CreateTime")).ToString("yyyy-MM-dd") %>
                </td>
                <td>
                    <%#((DateTime)Eval("UpdateTime")).ToString("yyyy-MM-dd") %>
                </td>



                <%if (PageType != "new")
                  {%>
                <td>
                    <a href="PackInfoAdd.aspx?appID=<%=this.AppID %>&ShowName=<%=this.ShowName %>&type=<%=this.Type %>" style="line-height: 60px;">添加</a>

                    <a href="PackInfoEdit.aspx?PackID=<%#Eval("PackID") %>&AppID=<%=this.AppID %>&ShowName=<%=this.ShowName %>&type=<%=this.Type %>" style="line-height: 60px;">修改</a>

                    <a class="del_packinfo" href="PackInfoList.aspx?PackID=<%#Eval("PackID") %>&appID=<%=this.AppID %>&ShowName=<%=this.ShowName %>">删除</a>
                </td>
                <% }
                  else
                  {%>
                <td>
                    <a class="" href="<%#Eval("PackUrl") %>" style="color: #1e74c9;">下载</a>

                     <%# BindVer(Convert.ToInt32(Eval("IsMainVer")),Convert.ToInt32(Eval("PackID")),Convert.ToInt32(Eval("AppID"))) %>
                
                </td>
                <%} %>
            </tr>

        </ItemTemplate>
        <FooterTemplate>
            </table>
        </FooterTemplate>

    </asp:Repeater>

    <div id="permission" style="display: none; width: 400px; height: auto; padding-top: 10px;" title="游戏权限">

        <div id="div_gridper">

            <table id="grid_per">
            </table>
            <%-- <ul id="ul_gridper">

            </ul>--%>
        </div>
    </div>
</asp:Content>
