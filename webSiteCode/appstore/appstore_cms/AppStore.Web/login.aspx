<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="login.aspx.cs" Inherits="AppStore.Web.login" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta http-equiv="charset" content="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no" />
    <title>欢迎使用华硕游戏中心管理后台</title>
    <script src="http://cms.niuwan.cc/js/jquery-1.9.1.js" type="text/javascript"></script>
    <script src="http://cms.niuwan.cc/js/nwbase_utils.js" type="text/javascript"></script>
<%--    <script type='text/javascript' src='/Javascript/jquery.json-2.2.js'></script>--%>

    <style type="text/css">
        @font-face {
            font-family: myfz;
            src: url(themes/font/fzzxhjtxz.TTF);
        }

        @font-face {
            font-family: myfz-IE;
            src: url(themes/font/fzzxhjtxz.eot);
        }

        * {
            font-family: myfz,myfz-IE,Arial,'Microsoft YaHei',宋体,Verdan;
            color: #000;
        }

        body {
            margin: 0px;
            padding: 0px;
            text-align: left;
            background-color: #FFF;
            min-width: 300px;
            min-height: 340px;
        }

        #midl {
            width: 300px;
            height: 340px;
            position: absolute;
        }

        #logo {
            margin: 0 auto;
            margin-bottom: 21px;
            text-align: center;
        }

            #logo img {
                width: 270px;
                height: 100px;
            }

        #titl {
            height: 60px;
            text-align: center;
            font-size: 20px;
        }

        #cont {
            height: 200px;
            text-align: center;
            margin: 0 auto;
            /*margin-top: 30px;*/
        }

        input[type=text], input[type=password] {
            height: 42px;
            width: 255px;
            padding-left: 17px;
            border: 1px solid #d9d9d9;
        }

        input[type=button], input[type=submit] {
            width: 274px;
            height: 42px;
            margin-top: 17px;
            margin-bottom: 17px;
            /*background-color: #b66;*/
                        background-color: #b66;

            color: #FFF;
            border: 0;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            -webkit-appearance: none;
        }

            input[type=button]:hover, input[type=submit]:hover {
                background-color: #b77;
                color: #EEE;
                border: 0;
                cursor: pointer;
            }

            input[type=button]:active, input[type=submit]:active {
                background-color: #b77;
                color: #EEE;
                border: 1px inset #DDD;
                cursor: pointer;
            }

        #cont #lblLoginName, #lblLoginPwd {
            width: 60px;
            height: 35px;
            color: #329b8b;
            display: inline-block;
            vertical-align: top;
        }

        #cont #lblMsg {
            width: 274px;
            color: #cc0000;
            margin: 0 auto;
        }

        ::-webkit-input-placeholder { /* WebKit browsers */
            color: #999;
        }

        :-moz-placeholder { /* Mozilla Firefox 4 to 18 */
            color: #999;
            opacity: 1;
        }

        ::-moz-placeholder { /* Mozilla Firefox 19+ */
            color: #999;
            opacity: 1;
        }

        :-ms-input-placeholder { /* Internet Explorer 10+ */
            color: #999;
        }
    </style>
    <script type="text/javascript" src="<%=ResolveUrl("~") + "Javascript/md5.js"%>"></script>
    <script type="text/javascript">
        var resizeTimer = null;
        $(window).resize(function () {
            resizeTimer = resizeTimer ? null : setTimeout(DoResize, 10);
        });
        document.domain = 'cms.niuwan.cc';
        $(window).ready(function () {
            DoResize();
            $(document).bind('keypress', function (e) {
                var code = e.keyCode || e.which;
                if (code == 13) { //Enter keycode
                    OnLogin();
                }
            });

            sessionStorage.clear(); // clear last visit url
        });

        $(document).resize(function () {
            DoResize();
        })

        function DoResize() {
            t = ($(window).height() - 340) / 2;
            l = ($(window).width() - 300) / 2;
            var t1 = parseInt(t, 10) - 30;
            var l1 = parseInt(l, 10);
            t1 = t1 > 0 ? t1 : 0;
            l1 = l1 > 0 ? l1 : 0;
            $("#midl").css("margin-top", t1 + "px");
            $("#midl").css("margin-left", l1 + "px");
        }

        function OnLogin() {
            if ($("#txtLoginName").val() == '' || $("#txtLoginPwd").val() == '') {
                $("#lblMsg").text("用户名和密码不允许为空.");
            }
            else {
                $("#lblMsg").text("");
                usr = $("#txtLoginName").val();
                pwd = $("#txtLoginPwd").val();
                url = $("#txtUrl").val();

                jump_url = getQueryString("t");
                jump_url = decodeURIComponent(jump_url || "Default_GameCenter.aspx");
                var data = {
                    "user_name": usr,
                    "password": CryptoJS.MD5(pwd).toString(),
                    "url": "Default_GameCenter.aspx"
                };

                $.post("PasswdCheck.aspx", JSON.stringify(data), function (d) {
                    var res = JSON.parse(d);
                    if (res.uid > 0) {
                      
                            window.location = jump_url;
                    }
                    else {
                        $("#txtLoginName").val(res.user_name);
                        $("#lblMsg").html(res.msg);
                    }
                });

            }

            return false;
        }
    </script>
</head>
<body id="login_body">
    <div id="midl">
        <div id="logo">
            <img src="images/huashuo2.png" alt="logo" />
        </div>
        <div id="titl">欢迎使用华硕游戏中心管理后台</div>
        <div id="cont">
            <input id="txtLoginName" type="text" runat="server" placeholder="帐号" /><br />
            <input id="txtLoginPwd" type="password" runat="server" placeholder="密码" /><br />
            <label id="lblLoginBtn" runat="server"></label>
            <input id="btnSubmit" type="submit" runat="server" style="background-color:#49aef5;" onclick="javascript: return OnLogin();" value="登　录" /><br />
            <label id="lblMsg" runat="server"></label>
            <br />
        </div>
    </div>
</body>
</html>
