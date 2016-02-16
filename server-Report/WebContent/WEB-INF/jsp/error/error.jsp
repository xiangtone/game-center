<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="org.apache.jasper.util.ExceptionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>系统异常</title>
</head>
<body>
	<div style="margin: 150px;" align="center">
		<h1><font color="red">系统发生未知异常!</font></h1><br/>
		<span id="show"></span> 
		<%--
			Exception e = (Exception)request.getAttribute("exception"); 
			out.print(e.getMessage());  
		--%>
		<script type="text/javascript">
			var i = 5;
			setInterval(function(){
				$("#show").html("");
				$("#show").html("<h1><font color=blue>" + i + "秒后转入欢迎页面...</font></h1>");
				if(i <= 1){
					window.location.href = "${ctx}/wellcome/execute";
				}
				i -- ;
			}, 1000);
		</script>
	</div>
</body>
</html>