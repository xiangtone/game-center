<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>zapp apptest</title>

<body bgcolor="#a9d26a">
	<div align="center">
		</br>
		<form action="${pageContext.request.contextPath}/apptest" id="formPer" method="get">
		录入appId:<input name="appIds" value="<c:out value="${map.appIds}"></c:out>"/> 
		<button type="submit">提交测试</button>(如：1提交zapp应用包;多个时请用,隔开)
		</form>
		</br>
		<a href="${pageContext.request.contextPath}/gotestin">进入ftp批量提交测试面</a>
		<c:if test="${map.success}">
		<p >
			success:<c:out value="${map.success}"></c:out><br></br>
			当 success:true 时，表示应用提交testIn上成功<br></br>
			请登陆 <a href="http://www.testin.cn/">http://www.testin.cn/</a> 网址查看测试结果<br></br>
			帐号：<c:out value="${map.testInEmail}"></c:out>
			密码：<c:out value="XXXXXXX"></c:out>
			<br></br>
		</p>
		<p>
			<c:out escapeXml="false" value="${map.describe}"></c:out>
		</p>
		</c:if>
		<c:if test="${map.success == false}">
		<p >
			success:<c:out value="${map.success}"></c:out><br></br>
			<c:out escapeXml="false" value="${map.describe}"></c:out>
		</p>
		</c:if>
	</div>
</body>
</html>