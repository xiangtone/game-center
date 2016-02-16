<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>后台管理系统</title>
	<script src="${ctx}/static/js/jquery/jquery.min.js"></script>
	<script src="${ctx}/static/js/common.js"></script>
	<link rel="stylesheet" href="${ctx}/static/css/module.css" type="text/css"/> 
	<link href="${ctx}/static/css/style.css" rel="stylesheet" />
	<script type="text/javascript">
		$(function(){
			$("#btn_submit").click(function(){
				var userName = $("#Username").val();
				var userPwd = $("#password").val();
				if(userName == ""){
					$("#errorName").html("<span>用户名不能为空!</span>");
					return;
				}else{
					$("#errorName").html("");
				}
				
				if(userPwd == ""){
					$("#errorPwd").html("<span>密码不能为空!</span>");
					return;
				}else{
					$("#errorPwd").html("");
				}
				$("#loginForm").submit();
			});
			//支持回车登陆
			document.onkeydown = function(e){ 
				var ev = document.all ? window.event : e;
				if(ev.keyCode==13 && $("#Username").val()!="" && $("#password").val()!="") {
					$("#btn_submit").click();
 				}
			}

		});
	</script> 
</head>
	<body  class="z_body">
		<div class="z_top">
		<a class="z_logo" href="#">
	    </a> 
	</div>
	<div class="z_c">
	<form action="${ctx }/user/login" id="loginForm" method="post">
		<div class="z_lg"></div>
		<div class="z_login">
	    <div class="z_t1">后台管理系统</div>
	    <div class="z_c1">
	    <div class="input1">
	    	<input id="Username" name="name" type="text" size="25"  maxlength="30"  onkeyup="value=value.replace(/[^\w]/ig,'')" placeholder="Email / ID"/>
		     <script type="text/javascript">
			document.getElementById('Username').focus();
			</script>
	    </div>
	     <div class="z_ts" id="errorName"></div>
	     <div class="input1">
	    	<input name="password" id="password" type="password" size="25"  maxlength="50" onkeyup="value=value.replace(/[^\w]/ig,'')"  placeholder="Password"/>
	    </div>
	    <div class="z_ts" id="errorPwd"></div>
	    </div>
	    <div class="z_b1">
	    	<input type="button" id="btn_submit" value="Login" class="z_but"  />
	    </div>
	    </div>
	    </form>
	</div>
	<!--/right-->
	</body>
</html>