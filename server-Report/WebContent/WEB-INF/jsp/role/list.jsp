<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>角色管理</title>
	<script type="text/javascript">
		var menu_flag = 'role';
	</script>
</head>
<body>
	
	<div class="mainoutside">
		<div class="secmainoutside">
			<div class="mainarea_shop">
				<div class="operateShop">
					<ul>
						<li><a href="add"><span class="l-btn-text icon-add">添加</span></a></li>
						<li><a id="cmd-delete" ><span class="l-btn-text icon-no">删除</span></a></li>
						<li><a type="button" id="cmd-config"><span  class="l-btn-text icon-edit">高级配置</span></a></li>
					</ul>
				</div>
			</div>
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
						<tr>
							<td align="center"><input type="checkbox" name="recordId" value="${role.id }"/></td>
							<td align="center">
								<a href="${role.id }">${role.name }</a>
							</td>
							<td align="center">
								${role.description }
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
				$.alert("删除角色后，角色下的所有用户将不属于这些角色，确定删除吗？", function() {
					$.post("delete", {
						id : id
					}, function(data) {
						var result = eval("("+data+")");
						if(result.success == true){
							$.alert("删除成功！");
							window.location.reload();
						}else{
							$.alert("删除失败,该角色下尚有用户存在！");
						}
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