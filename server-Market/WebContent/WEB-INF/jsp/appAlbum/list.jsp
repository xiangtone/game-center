<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appAlbum';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx }/app/list" method="post">
						名称：<input type="text" name="name" value="${fn:escapeXml(param.name)}"/>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
			</div>
		<div class="secmainoutside">
		<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td align="center">
							编号
						</td>
						<td align="center">
							名字
						</td>
						<td align="center">
							排序
						</td>
						<td align="center">
							操作
						</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="obj" items="${result}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<td align="center">
							${obj.id}
						</td>
						<td align="center">
							${fn:escapeXml(obj.name) }
						</td>
						<td align="center">
							${obj.sort }
						</td>
						<td align="center">
							<a href="listRes/${obj.id}">分发</a>
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
		</div>
	</div>
	<script type="text/javascript">
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
					window.location.reload();
				});
			}, true);
		}
	});
	</script>
</body>
</html>