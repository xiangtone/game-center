<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'log';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx }/log/list" method="post">
						操作人：<input type="text" name="operator" id="operator" value="${fn:escapeXml(param.operator)}"/>&nbsp;&nbsp;
						归属栏目 ：<input type="text" name="res" id="res" value="${fn:escapeXml(param.res)}"/>&nbsp;&nbsp;
						日志事项：<input type="text" name="action" id="action" value="${fn:escapeXml(param.action)}"/>&nbsp;&nbsp;
							<label>开始日期 : </label><input id="Q_startTime" name="startTime" readonly="readonly" class="Wdate" style="width:130px;" value="${param.startTime}">
							<label>--&nbsp;结束日期 : </label><input id="Q_endTime" name="endTime" readonly="readonly" class="Wdate" style="width:130px;" value="${param.endTime}">	
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						<button type="button" class="butsearch" id="resetButton" name="resetButton" >重置</button>
					</form>
				</div>
			</div>
		<div class="secmainoutside">
		<div class="border">
			<table class="mainlist" width="100%">
				<thead>
					<tr>
						<td width="10%">
							操作人
						</td>
						<td width="40%">
						          操作资源/归属栏目
						</td>						
						<td width="10%">
							日志事项
						</td>
						<td width="20%">
							日志结果
						</td>
						<td width="15%">
							操作时间
						</td>	
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="7" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center">
								${obj.operator }
							</td>
							<td align="center">
							   <c:if test="${obj.res.length()>300 }" >
									<a title="${fn:escapeXml(obj.res)}" href="${ctx }/log/info/${obj.id }" target="new">详细log</a>
								</c:if>
								<c:if test="${obj.res.length()>60 and obj.res.length()<=300 }" >
									<a title="${fn:escapeXml(obj.res)}" onclick="open1(this.title)"href="#" >详情</a>
								</c:if>
								<c:if test="${obj.res.length()<=60 }" >${fn:escapeXml(obj.res)}</c:if>
							</td>
							<td align="center">
								${fn:escapeXml(obj.action) }
							</td>
							<td align="center">
								<c:if test="${obj.succ==1}">成功</c:if><c:if test="${obj.succ==0}">失败</c:if>
							</td>
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.time}"/>								
							</td>	
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/log/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){	
		var params = {"operator" : $("#operator").val(),"res" : $("#res").val(),"action" : $("#action").val(),"startTime":$('#Q_startTime').val(),"endTime": $('#Q_endTime').val()};
		paginationUtils(params);
		$('#resetButton').click(function(){
			$('#operator').val('');
			$('#res').val('');
			$('#action').val('');
			$('#Q_startTime').val('');
			$('#Q_endTime').val('');
			//移除限制
			$("#Q_startTime").datepicker("option","maxDate",null);
			$("#Q_endTime").datepicker("option","minDate",null);
		});
	});
	function open1(val){
		alert(val);
	}
	</script>
</body>
</html>