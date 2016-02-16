<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'menu';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="operateShop">
					<ul>
						<li><a href="add">添加</a></li>
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
						<td>
							菜单名称
						</td>
						<td>
							菜单编码
						</td>
						<td>
							父级菜单
						</td>
						<td>
							菜单类型
						</td>
						<td>
							URL
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${dataSize == 0}">
						<tr>
							<td colspan="7" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="menus" items="${requestScope.menuList}" varStatus="status">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId" value="${menus.id }"/></td>
							<td align="center">
								<a href="${menus.id }">${menus.name }</a>
							</td>
							<td align="center">
								${menus.code }
							</td>
							<td align="center">
								${menus.parent.name }
							</td>
							<td align="center">
								${menus.type.name }
							</td>
							<td align="center">
								${menus.uri }
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
				$.alert("您确定删除吗？", function() {
					$.post("delete", {
						"id" : id
					}, function(data) {
						var obj = eval("("+data+")");
						if(obj.success == 1){
							alert("删除成功!");
							window.location.href = "list";
						}else{
							if(obj.success == 0){
								alert("删除失败,系统出现未知异常!");
							}else{
								alert("删除失败,存在子级菜单未删除!");
							}
						}
					});
				}, true);
			}
		});
	</script>
</body>
</html>