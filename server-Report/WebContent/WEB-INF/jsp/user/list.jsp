<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户管理</title>
<script type="text/javascript">
	var menu_flag = 'user';
	$(function(){
		$("#cmd-delete").click(function() {
			var id = checkId();
			if (id != '') {
				$.alert("删除用户账号后将不能登录及其它一切操作，且不可恢复，确定删除吗？", function() {
					$.post("delete", {
						id : id
					}, function(data) {
						var result = eval("("+data+")");
						if(result.success == true){
							$.alert("删除成功！");
							window.location.reload();
						}else{
							$.alert("删除失败！");
						}
					});
				}, true);
			}
		});

		$("#cmd-active").click(function() {
			var id = checkId();
			if (id != '') {
				$.alert("确定激活选中的账号吗？", function() {
					$.post("active", {
						id : id
					}, function(data) {
						$.alert("激活成功！");
						window.location.reload();
					});
				}, true);
			}
		});
		$("#cmd-disable").click(function() {
			var id = checkId();
			if (id != '') {
				$.alert("账号冻结后用户将不能登录系统，确定冻结选中的账号吗？", function() {
					$.post("disable", {
						id : id
					}, function(data) {
						$.alert("冻结成功！");
						window.location.reload();
					});
				}, true);
			}
		});
	});
	
	function checkId() {
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
</script>
</head>
<body>

	<div class="mainoutside">
		<div class="secmainoutside">
			<div class="mainarea_shop">
				<c:if test="${loginUser.name == 'admin' }">
				<div class="servicesearch pt18">
					<form action="" method="post">
						账号：<input type="text" name="name" value="${param.name }" maxlength="50"/>&nbsp;&nbsp;
						账号状态：<select name="activable">
							<option value="0">==所有==</option>
							<option value="1"
								<c:if test="${param.activable=='1' }"> selected="selected"</c:if>>激活</option>
							<option value="2"
								<c:if test="${param.activable=='2' }"> selected="selected"</c:if>>冻结</option>
						</select>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				</c:if>
				<div class="operateShop">
				<c:if test="${loginUser.name == 'admin' }">
					<ul>
						<li><a href="${ctx }/user/add"><span class="l-btn-text icon-add">添加</span></a></li>
						<li><a id="cmd-delete"><span class="l-btn-text icon-no">删除</span></a></li>
						<li><a type="button"  id="cmd-active"><span  class="l-btn-text icon-edit">激活账号</span></a></li>
						<li><a type="button" id="cmd-disable"><span  class="l-btn-text icon-edit">冻结账号</span></a></li>
					</ul>
					</c:if>
				</div>
			</div>
			<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall" /></td>
						<td>账号</td>
						<td>密码</td>
						<td>电话</td>
						<td>邮箱</td>
						<td>录入时间</td>
						<td>可用状态</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="7" align="center">没有符合要求的数据！</td>
						</tr>
					</c:if>
					<c:forEach var="user" items="${result.data}">
						<tr>
							<td align="center"><c:if test="${user.name!='admin' }">
									<input type="checkbox" name="recordId" value="${user.id }" />
								</c:if></td>
							<td align="center">
							<c:if test="${user.name != 'admin'}">
									<a href="${user.id }"> <c:if test="${user.activable == 0 }">
											<font color='red'>${user.name }</font>
										</c:if> <c:if test="${user.activable == 1 }">${user.name }</c:if>
									</a>
								</c:if> 
								<c:if test="${user.name == 'admin' && loginUser.name == 'admin'}">
									<a href="${user.id }"> <c:if test="${user.activable == 0 }">
											<font color='red'>${user.name }</font>
										</c:if> <c:if test="${user.activable == 1 }">${user.name }</c:if>
									</a>
								</c:if>
								<c:if test="${user.name == 'admin' && loginUser.name != 'admin'}">
									<c:if test="${user.activable == 0 }">
											<font color='red'>${user.name }</font>
									</c:if> <c:if test="${user.activable == 1 }">${user.name }</c:if>
								</c:if>
								</td>
							<td align="center">*****</td>
							<td align="center">${user.mobile }</td>
							<td align="center">${user.email }</td>
							<td align="center"><fmt:formatDate
									value="${user.insertDate }" pattern="yyyy-MM-dd" /></td>
							<td align="center"><c:if test="${user.activable == 1 }">激活</c:if>
								<c:if test="${user.activable == 0 }">冻结</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}"
							contextPath="${ctx}/user/list" params="${params}" />
					</c:if>
				</div>
			</div>
			</div>
		</div>
	</div>
</body>
</html>