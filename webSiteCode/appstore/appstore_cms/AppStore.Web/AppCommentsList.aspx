<%@ Page Language="C#" AutoEventWireup="true"  MasterPageFile="~/MasterPage.Master" CodeBehind="AppCommentsList.aspx.cs" Inherits="AppStore.Web.CommentList" %>

<%@ Register Assembly="AspNetPager" Namespace="Wuqi.Webdiyer" TagPrefix="webdiyer" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    
    <style type="text/css">
        body, div, table, tr, td, p {
            margin: 0;
            padding: 0;
            border: 0;
        }

        a:active {
            background-color: transparent;
        }

        #container {
            width: 1100px;
        }
        .search-area {
            padding: 10px 0;
            float: right;
            margin-right: 30px;
        }
        td {
            word-wrap: break-word;
            word-break: break-all;
        }

        
        
    </style>
    <script type="text/javascript">
        

    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div class="nav">
        <span class="nav_title">应用中心管理</span> <span class="nav_desc">首页 &raquo; 评论管理</span>
    </div>


    

    <div id="container" style="border: 0;">
        <div class="search-area">
       
            <div>
                <asp:DropDownList ID="SearchType" runat="server">
                    <asp:ListItem Text="不限制" Value="all" Enabled="true" Selected="False"></asp:ListItem>
                    <asp:ListItem Text="按应用名称" Value="appname" Enabled="true" Selected="False"></asp:ListItem>
                    <asp:ListItem Text="按评论内容" Value="comments" Enabled="true" Selected="False"></asp:ListItem>
                </asp:DropDownList>
               
                <span>搜索内容</span>
                <asp:TextBox ID="SearchKey" runat="server"></asp:TextBox>
                <asp:Button ID="SearchBtn" OnClick="SearchBtn_Click" Text="搜索" runat="server" />
                
                <span>排序方式</span>
                <asp:DropDownList ID="OrderType" runat="server">
                    <asp:ListItem Text="评论时间升序" Value="asc" Enabled="true" Selected="False"></asp:ListItem>
                    <asp:ListItem Text="评论时间降序" Value="desc" Enabled="true" Selected="False"></asp:ListItem>
                </asp:DropDownList>
               
            </div>
    </div>

        <asp:Repeater ID="DataList" runat="server">
            <HeaderTemplate>
                <table style="width: 100%" class="grid">
                    <thead>
                        <th>ID</th>
                        <th>应用名称</th>
                        <th>类型</th>
                        <th>版本</th>
                        <th>评分</th>
                        <th>用户名</th>
                        <th>评论时间</th>
                        <th>评论内容</th>
                    </thead>
                    <tbody>
            </HeaderTemplate>
            <ItemTemplate>
                <tr>
                    <td ><%#Eval("CommentID") %></td>
                    <td style="width: 80px;"><%#AppName(Eval("AppID")) %></td>
                    <td style="width: 40px;"><%#AppType(Eval("AppID")) %></td>
                    <td style="width: 60px;"><%#Eval("LocalVerName") %></td>
                    <td style="width: 40px;"><%#Eval("UserScore") %></td>
                    <td><%#Eval("UserName")%></td>
                    <td style="width: 120px;"><%#Eval("CommentTime") %></td>
                    <td><%#Eval("Comments") %></td>
                </tr>

            </ItemTemplate>
            <FooterTemplate>
                    </tbody>
                </table>    
            </FooterTemplate>

        </asp:Repeater>
        <div class="wrap">
        <webdiyer:AspNetPager runat="server" ID="pagerList" OnPageChanged="pagerList_PageChanged" CssClass="pages" CurrentPageButtonClass="cpb"
            PagingButtonSpacing="0px" NumericButtonCount="10" PageSize="20" AlwaysShow="true">
        </webdiyer:AspNetPager>
    </div>
    </div>
    

</asp:Content>
