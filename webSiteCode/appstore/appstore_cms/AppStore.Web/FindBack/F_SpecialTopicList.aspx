<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.Master" AutoEventWireup="true" Inherits="AppStore.Web.SpecialTopicList" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeaderPlace" runat="server">
    <script type="text/javascript">

        ShareData = function (data) {
            DlgIfrmCloseInSubwin("GetShareSpecialTopic", data);
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlace" runat="server">

    <div id="container">
        <asp:Repeater runat="server" ID="DataList">
            <HeaderTemplate>
                <table style="width: 100%" class="grid">
                    <thead>
                        <tr>
                            <th>专题ID</th>
                            <th style="width: 202px;">专题封面</th>
                            <th>专题名称</th>
                            <th>应用操作</th>

                        </tr>
                    </thead>
            </HeaderTemplate>
            <ItemTemplate>
                <tr class='<%#(Container.ItemIndex%2==0)?"odd":""%>'>
                    <td><%#Eval("GroupID") %>
                    </td>
                    <td>
                        <img src='<%#Eval("GroupPicUrl") %>' width="200" height="134" alt="" />
                    </td>
                    <td><%#Eval("GroupName") %></td>
                    <td>
                        <a class="btnSelect" href="javascript:ShareData({GroupID:'<%#Eval("GroupID") %>', GroupName:'<%#Eval("GroupName")%>',GroupTypeID:'<%#Eval("GroupTypeID") %>', GroupPicUrl:'<%#Eval("GroupPicUrl") %>'})"
                            title="查找带回" style="margin-top: 20px;">选择</a>
                    </td>
                </tr>

            </ItemTemplate>
            <FooterTemplate>
                </table>
            </FooterTemplate>
        </asp:Repeater>
    </div>
</asp:Content>
