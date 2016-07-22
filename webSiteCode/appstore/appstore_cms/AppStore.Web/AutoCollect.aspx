<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="AutoCollect.aspx.cs" Inherits="AppStore.Web.AutoCollect" MasterPageFile="~/MasterPage.Master" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <style type="text/css">
        .auto-style1 {
            height: 20px;
        }

        .auto-style2 {
            height: 23px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $("#AddPics").click(function () {
                if ($("#txtpics1").val() != "") {
                    $("#<%=Image2.ClientID%>").attr("src", $("#txtpics1").val())
                }
                if ($("#txtpics2").val() != "") {
                    $("#<%=Image3.ClientID%>").attr("src", $("#txtpics2").val())
                }
                if ($("#txtpics3").val() != "") {
                    $("#<%=Image4.ClientID%>").attr("src", $("#txtpics3").val())
                 }

                if ($("#txtpics4").val() != "") {
                    $("#<%=Image5.ClientID%>").attr("src", $("#txtpics4").val())
                 }
                 if ($("#txtpics5").val() != "") {

                     $("#<%=Image6.ClientID%>").attr("src", $("#txtpics5").val())
                }
            });
        });
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">
    <div>
        &nbsp;
        <asp:TextBox ID="TextBox1" runat="server" Height="20px" Width="652px"></asp:TextBox>
        &nbsp;<br />
        <br />
        &nbsp;&nbsp;&nbsp;
        <asp:Button ID="Button1" runat="server" Text="格式一" OnClick="Button1_Click" />

        <asp:Button ID="Button2" runat="server" Text="格式二" OnClick="Button2_Click" />

        <br />
        <br />
        <table class="main_form">
            <tr>
                <td class="form_text">Ico</td>
                <td class="content">
                    <asp:Image ID="Image1" runat="server" Visible="false" /></td>
            </tr>
            <tr>
                <td class="title">游戏名</td>
                <td class="content">
                    <asp:TextBox ID="txtname" runat="server" Width="300px"></asp:TextBox></td>
            </tr>

            <tr>
                <td class="title">游戏分类</td>
                <td class="content">
                    <asp:DropDownList ID="AppType" runat="server" Width="300px"></asp:DropDownList>
                </td>
            </tr>
            <tr>
                <td class="title">是否是网游</td>
                <td class="content">
                    <asp:DropDownList ID="IsNetGame" runat="server" Width="300px">
                        <asp:ListItem Value="1">是</asp:ListItem>
                        <asp:ListItem Value="2" Selected="True">否</asp:ListItem>
                    </asp:DropDownList>
                </td>
            </tr>
            <tr>
                <td class="title">推荐星级</td>
                <td class="content">
                    <asp:DropDownList ID="DroRecommLevel" runat="server" Width="300px">
                        <asp:ListItem Value="0">0</asp:ListItem>
                        <asp:ListItem Value="1">1</asp:ListItem>
                        <asp:ListItem Value="2">2</asp:ListItem>
                        <asp:ListItem Value="3">3</asp:ListItem>
                        <asp:ListItem Value="4">4</asp:ListItem>

                        <asp:ListItem Value="5" Selected="True">5</asp:ListItem>
                        <asp:ListItem Value="6">6</asp:ListItem>
                        <asp:ListItem Value="7">7</asp:ListItem>
                        <asp:ListItem Value="8">8</asp:ListItem>
                        <asp:ListItem Value="9">9</asp:ListItem>
                        <asp:ListItem Value="10">10</asp:ListItem>
                    </asp:DropDownList>
                </td>
            </tr>
            <tr>
                <td class="title">下载量</td>
                <td class="content">
                    <asp:TextBox ID="txttimes" runat="server" Width="300px"></asp:TextBox></td>

            </tr>
            <tr>
                <td class="auto-style1">大小</td>
                <td class="auto-style1">
                    <asp:TextBox ID="txtsize" runat="server" Width="300px"></asp:TextBox></td>
            </tr>
            <tr>
                <td class="auto-style1">包名</td>
                <td class="auto-style1">
                    <asp:TextBox ID="txtpackName" runat="server" Width="300px"></asp:TextBox></td>
            </tr>
            <tr>
                <td class="auto-style2">下载地址</td>
                <td class="auto-style2">
                    <asp:TextBox ID="txtpackurl" runat="server" Width="300px"></asp:TextBox></td>
            </tr>

            <tr>
                <td class="title">描述</td>
                <td class="content">
                    <asp:TextBox ID="txtdesc" runat="server" Height="200px" TextMode="MultiLine" Width="500px"></asp:TextBox>
                </td>
            </tr>

            <tr>
                <td class="title">作者</td>
                <td class="content">
                    <asp:TextBox ID="txtauthor" runat="server" Width="300px"></asp:TextBox></td>
            </tr>
            <tr>
                <td class="title">更新时间</td>
                <td class="content">
                    <asp:TextBox ID="txtupdatetime" runat="server" Width="300px"></asp:TextBox></td>
            </tr>
            <tr>
                <td class="title">版本</td>
                <td class="content">
                    <asp:TextBox ID="txtversion" runat="server" Width="300px"></asp:TextBox></td>
            </tr>
            <tr>
                <td class="title">系统</td>
                <td class="content">
                    <asp:TextBox ID="txtsystem" runat="server" Width="300px"></asp:TextBox></td>
            </tr>
            <tr>
                <td class="title">更新内容</td>
                <td class="content">
                    <asp:TextBox ID="txtUpdatedesc" runat="server" Height="200px" TextMode="MultiLine" Width="500px"></asp:TextBox>
                </td>
            </tr>
            <tr>
                <td class="title"></td>
                <td class="content">图片1：
                        <input type="text" id="txtpics1" style="width: 300px;" /><br />

                    图片2：<input type="text" id="txtpics2" style="width: 300px;" /><br />

                    图片3：<input type="text" id="txtpics3" style="width: 300px;" /><br />
                    图片4：<input type="text" id="txtpics4" style="width: 300px;" /><br />

                    图片5：
                    <input type="text" id="txtpics5" style="width: 300px;" /><br />

                    <input type="button" value="添加图片" id="AddPics" />
                </td>
            </tr>
            <tr>
                <td class="title">图片</td>
                <td class="content">
                    <asp:Image ID="Image2" runat="server" Height="300px" Width="180px" ImageUrl="~/Images/empty.png"/>
                    <asp:Image ID="Image3" runat="server" Height="300px" Width="180px" ImageUrl="~/Images/empty.png" />
                    <asp:Image ID="Image4" runat="server" Height="300px" Width="180px" ImageUrl="~/Images/empty.png" />
                    <asp:Image ID="Image5" runat="server" Height="300px" Width="180px" ImageUrl="~/Images/empty.png" />
                    <asp:Image ID="Image6" runat="server" Height="300px" Width="180px" ImageUrl="~/Images/empty.png" />

                </td>
            </tr>
        </table>
        <asp:Button ID="Button3" runat="server" Text="保存" OnClick="Button3_Click" Style="height: 21px" />
    </div>
</asp:Content>
