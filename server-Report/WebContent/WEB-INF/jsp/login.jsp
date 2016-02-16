<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>zApp</title>
	<script src="${ctx}/static/js/jquery/jquery.min.js"></script>
	<script src="${ctx}/static/js/common.js"></script>
	<link rel="stylesheet" href="${ctx}/static/css/module.css" type="text/css"/>
	<link rel="stylesheet" href="${ctx}/static/css/login.css" type="text/css"/>
	<link rel="stylesheet" href="${ctx}/static/css/style.css" type="text/css"/>
	<script type="text/javascript">
		$(function(){
			$("#btn_submit").click(function(){
				var userName = $("#Username").val();
				var userPwd = $("#password").val();
				if(userName == "用户名" || userName == ""){
					$("#showMsg>font").html("用户名不能为空!");
					return;
				}else{
					$("#showMsg>font").html("");
				}
				
				if(userPwd == "密码" || userPwd == ""){
					$("#showMsg>font").html("密码不能为空!");
					return;
				}else{
					$("#showMsg>font").html("");
				}
				$("#loginForm").submit();
			});
			//直接按回车键提交表单
			$("form").keydown(function(event){
				if(event.which == 13 ){
					$("#btn_submit").click();
				}
			});
			
			$("input").keypress(function(){
				$("#showMsg>font").html("");
			});
			
			$("input[type='text']").each(function() {
				var oldVal = $(this).val();
				$(this).css({
					"color" : "#888"
				}).focus(function() {
					if ($(this).val() != oldVal) {
						$(this).css({
							"color" : "#000"
						});
					} else {
						$(this).val("").css({
							"color" : "#888"
						});
					}
				}).blur(function() {
					if ($(this).val() == "") {
						$(this).val(oldVal).css({
							"color" : "#888"
						});
					}
				}).keydown(function() {
					$(this).css({
						"color" : "#000"
					});
				});
			});
		});
	</script>
</head>
<body class="z_body">
	<div class="z_top">
		<a class="z_logo" href="javascript:;"></a>
	</div>
	<div class="z_c">
		<div class="z_lg"></div>
		<div class="z_login">
			<form id="loginForm" action="${ctx }/user/login" method="post">
				<div class="z_t1">zApp报表系统</div>
				<div class="z_c1">
					<div class="input1">
						<input id="Username" name="name" type="text" value="用户名"
							placeholder="用户名" size="25" maxlength="30" />
					</div>
					<div class="input1">
						<input name="password" id="password" type="password"
							placeholder="密码" size="25" maxlength="30" />
					</div>
					<div class="input" style="text-align: center; padding: 15px 0;">
						<span id="showMsg"><font color=red>${requestScope.errorMsg }</font></span>
					</div>
				</div>
				<div class="z_b1">
					<input type="button" value="登陆" id="btn_submit" class="z_but" />
				</div>
			</form>
		</div>
	</div>
</body>
</html>