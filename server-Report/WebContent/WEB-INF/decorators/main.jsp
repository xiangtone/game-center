<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ include file="../jsp/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><decorator:title default="zApp报表管理系统"></decorator:title></title>
<%-- --%><link rel="stylesheet" href="${ctx}/static/css/module.css"
	type="text/css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/tip-yellow.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/jquery-ui-1.8.16.custom.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/js/themes/icon.css" />
<link rel="stylesheet" href="${ctx}/static/css/style.css"
	type="text/css" />
<script src="${ctx}/static/js/jquery/jquery.min.js"></script>
<script src="${ctx}/static/js/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script src="${ctx}/static/js/jquery/jquery.ui.datepicker-zh-CN.js"></script>
<script src="${ctx}/static/js/jquery/jquery.form.js"></script>
<script src="${ctx}/static/js/jquery/jquery.validate.js"></script>
<script src="${ctx}/static/js/common.js"></script>
<script src="${ctx}/static/js/combobox.js"></script>
<script src="${ctx}/static/js/jquery.easyui.min.js"></script>
<script src="${ctx}/static/js/jquery/easyui-lang-zh_CN.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		var a = $(".menu_two > li[id=" + menu_flag + "]").find("a");
		if (a) {
			a.attr("class","menu_a2");
			a.parent().parent().show();
		}

		$("#Q_startTime").datepicker({
			changeMonth : true, //在日期的标题栏中是否出现下拉选择框，选择日期中的月份。true代表有选择框，false代表无选择框  
			//changeYear:false, //在日期的标题栏中是否出现下拉选择框，选择日期中的年份。true代表有选择框，false代表无选择框  
			//showButtonPanel:true,  //在日期面板的下方出现两个按钮，一个是今天，一个是关闭。默认值是false,不显示的  
			//closeText:"关闭",  //必须结合showButtonPanel使用，并且showButtonPanel的值必须是true，否则看不到效果
			onSelect : function(dateText, inst) {
				$("#Q_endTime").datepicker("option", "minDate", dateText);
			}
		});
		$("#Q_endTime").datepicker({
			changeMonth : true,
			onSelect : function(dateText, inst) {
				$("#Q_startTime").datepicker("option", "maxDate", dateText);
			}
		});

		$(".one_a").click(function() {
			$(this).next().slideToggle();
			$(this).parent().siblings().find("ul").slideUp();
		});
		
		/**
		*监听窗口大小变化,面板和表格自适应窗口的变化
		*/
		$(window).resize(function(){
			$(".easyui-panel").panel("resize",{heigth:29,width:$("#rightbox").outerWidth()});
			$(".easyui-datagrid").datagrid("resize",{width:$("#rightbox").outerWidth(),heigth:$(".z_nav").outerHeight() - 52});
		});

		//设置面板的高度和宽度 
		$(".easyui-panel").panel("resize",{heigth:29,width:$("#rightbox").outerWidth()});
		
		$("#doc").click(function(){
			window.location.href = "${ctx }/doc/download";
		});
	});
</script>
<decorator:head></decorator:head>
</head>

<body class="z_body">
	<div class="z_top">
		<a class="z_logo" href="javascript:void(0);"></a>
		<div class="z_log">
			<a href="${ctx }/user/logout">退出</a>
			<div class="ri">您好:&nbsp;&nbsp;${loginUser.name }&nbsp;&nbsp;今天&nbsp;<fmt:formatDate value="<%=new java.util.Date() %>" pattern="yyyy年MM月dd日  E" /></div>
		</div>
	</div>
	<div class="z_column">
		<div class="z_cd">主菜单</div>
		<div class="z_dq">
			当前位置: <span><decorator:title default="zApp报表管理系统"></decorator:title></span>
		</div>
	</div>
	<div class="z_nav">
		<div class="z_nav2">
			<div class="left_menu">
				<ul class="menu_one">
					<c:set scope="page" value="1" var="img"></c:set>
					<c:forEach var="menus" items="${sessionScope.leftMenus}">
					<c:if test="${menus.parentId == 0 }">
					<li>
					<c:if test="${!empty menus.icon }">
						<a href="javascript:void(0);" class="one_a"><span class="nav_1"><img src="${ctx }/static/images/${menus.icon }" alt="" /></span><small class="m_jt"></small>${menus.name }</a>
					</c:if>
					<c:if test="${empty menus.icon }">
						<a href="javascript:void(0);" class="one_a"><span class="nav_1"><img src="${ctx }/static/images/n3.png" alt="" /></span><small class="m_jt"></small>${menus.name }</a>
					</c:if>	
					<ul class="menu_two">
							<c:forEach var="menu" items="${menus.childMenu}">
							<li id="${menu.code }"><a href="${ctx }/${menu.uri }" class="menu_a"><span>${menu.name}</span></a></li>
							</c:forEach>
						</ul>
					</li>
					<c:set scope="page" value="${img + 1 }" var="img"></c:set>
					</c:if>
					</c:forEach>
				</ul>
			</div>
		</div>
		<a href="javascript:void(0);" id="doc" class="d">操作文档</a>
	</div>
	<!--right-->
	<div  class="z_right" id="rightbox">
		<decorator:body/>
	</div>
	<!--/right-->
</body>

</html>

