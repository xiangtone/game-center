<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'role';
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
						<li><a type="button" class="lang" id="cmd-config">高级配置</a></li>
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
							名称
						</td>
						<td>
							描述
						</td>
						<!-- <td>
							
						</td> -->
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
					<c:forEach var="role" items="${requestScope.roles}" varStatus="status">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId" value="${role.id }"/></td>
							<td align="center">
								<a href="${role.id }">${role.name }</a>
							</td>
							<td align="center">
								${role.description }
							</td>
							<!-- <td>
								<c:if test="${not status.first}"><a href="${role.id }/higher"><img alt="上移" src="${ctx }/static/images/arrow_up.png"/></a>&nbsp;</c:if>
								<c:if test="${not status.last}"><a href="${role.id }/lower"><img alt="下移" src="${ctx }/static/images/arrow_down.png"/></a></c:if>
							</td> -->
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/role/list" params="${params}"/>
					</c:if>
				</div>
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
						$.alert("删除角色后，角色下的所有用户将不属于这些角色，确定删除吗？", function() {
							$.post("delete", {
								id : id
							}, function(data) {
								var obj = eval("("+data+")");
								if(obj.success==="true"){
									alert("删除成功！");
								}else{
									alert("删除失败！请先清除用户");
								}
								//window.location.reload();
								window.location.href = "list";
							});
						}, true);
					}
				});

		$("#cmd-config").click(function() {
			var id = checkId();
			if(id.indexOf(',') > 0){
				$.alert('请只选择一项进行功能权限配置！');
				return;
			}
			if (id != ''){
				window.location.href = id + '/config';
			}
		});

	</script>
</body>
</html>