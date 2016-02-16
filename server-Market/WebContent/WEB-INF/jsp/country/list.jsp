<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'country';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx}/country/list" method="post">
						平台国家英文名称：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name) }"/>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
						<li><a href="${ctx }/country/add">添加</a></li>
						<li><a id="cmd-delete">删除</a></li>
					</ul>
				</div>
			</div>
		<div class="secmainoutside">
			<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall"/></td>
						<td align="center">
							countryid
						</td>
						<td align="center">
							国家名
						</td>
						<td align="center">
							国家中文名
						</td>
						<td align="center">
							创建日期
						</td>
						<td align="center">
							操作
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${countrys.recordCount == 0}">
						<tr>
							<td colspan="6" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${countrys.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
						<td align="center">
							<a href="${obj.id }">${obj.id }</a>
						</td>
						<td align="center">
							${fn:escapeXml(obj.name)}
						</td>
						<td align="center">
							${fn:escapeXml(obj.nameCn)}
						</td>
						<td align="center">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>							    
						</td>

						<td align="center">
							<a href="countryInfo/${obj.id }">查看详细</a>
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${countrys.recordCount != 0}">
						<ccgk:pagination paginationVo="${countrys}" contextPath="${ctx}/country/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var params = {"name" : $("#name").val()};
		paginationUtils(params);
	});
	function checkId(){
		var item = $("[name=recordId]");
		var id = "";
		if (item.size() > 0) {
			item.each(function() {
						if ($(this).attr("checked") == true) {
							id += $(this).val();
							id += ",";
						}
					});
		}
		id = id.substr(0, id.lastIndexOf(","));
		if (id == "") {
			$.alert("请选择记录");
			return '';
		} else {
			return id;
		}
	}
	
	$("#cmd-delete").click(function() {
		var id = checkId();
		if (id != ''){
			$.alert("删除后将不可恢复，确定删除？", function() {
				$.get("delete", {
					id : id
				}, function(data) {
					$.alert("删除成功！");
					//window.location.reload();
					window.location.href = "list";
				});
			}, true);
		}
	});
	</script>
</body>
</html>