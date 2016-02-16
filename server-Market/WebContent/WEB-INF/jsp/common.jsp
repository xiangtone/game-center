<%@page import="com.mas.rave.util.Constant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ccgk" uri="http://www.ccgk.com/jsp/commons" %>
<%@taglib prefix="ff" uri="/formatPro" %>
<jsp:useBean id="now" class="java.util.Date" scope="request"/>
<%
String ctx = request.getContextPath();
request.setAttribute("ctx",ctx);

String path = Constant.resServer;
request.setAttribute("path",path);


String num = Constant.COUNT_NUMBER;
request.setAttribute("num",num);

String maxcount = Constant.MAXCOUNT;
request.setAttribute("maxcount", maxcount);
%>
