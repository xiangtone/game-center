<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" CodeBehind="FlashPageList.aspx.cs" Inherits="AppStore.Web.FlashPageList" %>
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
        });
    </script>
    <style type="text/css">
        table,tr,td{margin:0;padding:0;border:0;}
        ul{margin: 0px;padding: 0px;list-style: none;}
        #container .red{ color:Red;}
        #container .blue{ color:Blue;}
        #container .green{ color:green;}
        #container .white{ color:white;}
        #container .black{ color:#333;}
        #container ul li p{margin-left: 5px;}
        #container ul li.add{ text-align:center;}
        #container ul li.add img{ margin-top:20px; }
        #container ul li
        {
            height: 340px;
            width: 340px;
            margin: 5px;
            -webkit-border-radius: 1.2em;
            -moz-border-radius: 1.2em;
            border-radius: 1.2em;
            background: #14968e;
            line-height: 10px;
            padding: 5px;
        }

        #container ul li .pic{text-align:center;}
        #container ul li .pic img{margin: 0 auto;max-height: 250px;max-width:300px;}
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
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
<div id="background" class="background" style="display: none;">
</div>
<div id="progressBar" class="progressBar" style="display: none;">
    数据处理中，请稍等...W
</div>
<div class="nav">
     <%-- <%--<span class="nav_title">游戏中心管理</span> --%> --%> <span class="nav_desc">闪屏界面管理 </span>
</div>
<div class="wrap">
    <input runat="server" id="btnAdd" type="button" value="新增闪屏" onclick="return window.location.href='FlashPageEdit.aspx';" />
</div>
<div id="container">
    <ul>
        <asp:Repeater runat="server" ID="DataList">
            <ItemTemplate>
                <li class="show-app-pos" url="#">
                    <p class="pic"><a href="FlashPageEdit.aspx?id=<%#Eval("GroupID") %>"><img src="<%#Eval("GroupPicUrl") %>" /></a></p>
                    <table class="list_info">
                        <tr><td><%#Eval("GroupDesc")%></td><td style="text-align:right;">状态：<%#BindStatus(this.GetDataItem()) %></td></tr>
                        <tr><td colspan="2"><%#BindTime(this.GetDataItem()) %></td></tr>
                        <tr><td>上传时间：<%#DateTime.Parse(Eval("CreateTime").ToString()).ToString("yyyy.MM.dd")%></td><td style="text-align:right;"><a href="FlashPageEdit.aspx?id=<%#Eval("GroupID") %>">更新</a>&nbsp;&nbsp;&nbsp;<asp:LinkButton ID="LinkButton2" runat="server" CommandArgument=<%#Eval("GroupID") %> OnCommand="OnDel" OnClientClick="return confirm('确认删除吗?');">删除</asp:LinkButton></td></tr>
                    </table>
                </li>
            </ItemTemplate>
        </asp:Repeater>
    </ul>
</div>
</asp:Content>
