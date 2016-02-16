<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'user';
		var datagrid;
		var toolbar = "[{text : '添加',iconCls : 'icon-add',handler : add},";
			toolbar += "{text : '删除',iconCls : 'icon-remove',handler : remove},";
			toolbar += "{text : '激活账号',iconCls : 'icon-save',handler : activable},";
			toolbar += "{text : '冻结账号',iconCls : 'icon-cancel',handler : freeze},";
			toolbar += "{text : '取消选择',iconCls : 'icon-undo',handler : reset}]";
		$(function(){
			//初始化页面时,加载数据
			//loadDatagrid();
			//重置搜索
			$("#cancelSearchButton").click(function(){
				$("#name").val("");
				$("#activable").val("");
			});
			$("#searchButton").click(function(){
				datagrid.datagrid("load",{
					"name" : $("#name").val(),
					"activable" : $("#activable").val()
				});
			});
			var params = {"name" : $("#name").val(),"activable" : $("#activable").val()};
			//调用分页参数处理方法
			paginationUtils(params);
		});
		function loadDatagrid(){
			//alert("aaaa");
			datagrid = $("#user_datagrid").datagrid({
				width:$("#rightbox").outerWidth()-20,
				height : $("#rightbox").outerHeight()-160,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : '${ctx}/user/query',
				loadMsg : '数据加载.....',
				sortName : 'id',
				sortOrder : 'asc',
				remoteSort : false,
				pagination : true,
				rownumbers : true,
				singleSelect : true,
				selectOnCheck: true,
				checkOnSelect: true,
				fitColumns:true,
				idField : 'id',
				queryParams : {
					"limit" : 10,
					"start" : 0
				},
				frozenColumns : [[{field : 'id',checkbox : true}]],
				columns : [[
					{field : 'name',title : '账号',width : 100,align : 'center'},
					{field : 'password',title : '密码',width : 100,align : 'center',
						formatter:function(password){
							return "*****";
						}
					},
					{field : 'mobile',title : '电话',width : 150,align : 'center'},
					{field : 'email',title : '邮箱',width : 150,align : 'center'},
					{field : 'activable',title : '状态',width : 150,align : 'center',
						formatter:function(activable){
							if(activable == "1"){
								return "激活";
							}else{
								return "冻结";
							}
						}
					},
					{field : 'insertDate',title : '录入时间',width : 150,align : 'center',
						formatter:function(insertDate){
							return formatDateTime(insertDate,true);
						}
					},
					]],
				toolbar : eval(toolbar),
				onLoadSuccess:function(data){
					//alert("load success !");
				}
			});
		}
		function formatDateTime(obj, IsMi) { 
		    var myDate = new Date(obj);   
		    var year = myDate.getFullYear();  
		    var month = ("0" + (myDate.getMonth() + 1)).slice(-2);  
		    var day = ("0" + myDate.getDate()).slice(-2);  
		    var h = ("0" + myDate.getHours()).slice(-2);  
		    var m = ("0" + myDate.getMinutes()).slice(-2);  
		    var s = ("0" + myDate.getSeconds()).slice(-2);   
		    var mi = ("00" + myDate.getMilliseconds()).slice(-3);  
		    if (IsMi == true) {   
		        return year + "-" + month + "-" + day + " " + h + ":" + m + ":" + s;   
		    }else {   
		        return year + "-" + month + "-" + day + " " + h + ":" + m + ":" + s + "." + mi;   
		    }  
		};
		
		//添加
		function add(){
			window.location.href="${ctx }/user/add";
		}
		
		//删除
		function remove(){
			var checkeds = datagrid.datagrid("getSelections");
			var id = "";
			$.each(checkeds,function(index,row){
				id = row.id;
			});
			if(id == ""){
				$.messager.alert("提示：","请选择要操作的数据!");
				return;
			}
			if (id != ''){
				$.alert("删除用户账号后将不能登录及其它一切操作，且不可恢复，确定删除吗？", function() {
					$.get("delete", {
						id : id
					}, function(data) {
						$.alert("删除成功！");
						window.location.reload();
					});
				}, true);
			}
		}
		
		//激活
		function activable(){
			var checkeds = datagrid.datagrid("getSelections");
			var id = "";
			$.each(checkeds,function(index,row){
				id = row.id;
			});
			if(id == ""){
				$.messager.alert("提示：","请选择要操作的数据!");
				return;
			}
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
		}
		
		//冻结
		function freeze(){
			var checkeds = datagrid.datagrid("getSelections");
			var id = "";
			$.each(checkeds,function(index,row){
				id = row.id;
			});
			if(id == ""){
				$.messager.alert("提示：","请选择要操作的数据!");
				return;
			}
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
		}
		
		//取消所选
		function reset(){
			datagrid.datagrid("clearSelections");
		}
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	<%--
	<div style="margin-top: 5px;">
	<div class="easyui-panel" title="搜索" iconCls="icon-search" collapsible="true"
			style="height:auto;width:auto; margin-bottom:5px;padding-left:5px;padding-top:10px;padding-bottom:10px;overflow: hidden;">
			<label>姓名 : </label><input id="name" name="name" value="${param.name }" style="width:130px;">
			<label>&nbsp;账号状态 : </label>
			<select id="activable" name="activable">
				<option value="">--所有--</option>
				<option value="1" <c:if test="${param.activable=='1' }"> selected="selected"</c:if>>激活</option>
				<option value="2" <c:if test="${param.activable=='2' }"> selected="selected"</c:if>>冻结</option>
			</select>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cancelSearchButton" name="cancelSearchButton" iconCls="icon-cancel">重置搜索</a>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="searchButton" name="searchButton" iconCls="icon-search">搜索</a><!--  -->
		</div>
		
	<table id="user_datagrid" class="easyui-datagrid" style="padding-top: 50px;"></table>
	</div> --%>
	<%-- --%>
	<div class="mainoutside">
	<div class="mainarea_shop">
				<c:if test="${loginUser.name == 'admin' }">
				<div class="servicesearch pt18">
					<form action="" method="get">
						姓名：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name) }" maxlength="30"/>&nbsp;&nbsp;
						账号状态：<select name="activable" id="activable">
							<option value="0">==所有==</option>
							<option value="1" <c:if test="${param.activable=='1' }"> selected="selected"</c:if>>激活</option>
							<option value="2" <c:if test="${param.activable=='2' }"> selected="selected"</c:if>>冻结</option>
						</select>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				</c:if>
				<div class="operateShop">
					<c:if test="${loginUser.name == 'admin' }">
					<ul>
						<li><a href="${ctx }/user/add">添加</a></li>
						<li><a id="cmd-delete">删除</a></li>
						<li><a type="button" class="lang" id="cmd-active">激活账号</a></li>
						<li><a type="button" class="lang" id="cmd-disable">冻结账号</a></li>
					</ul>
					</c:if>
				</div>
			</div>
		<div class="secmainoutside">
			<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall"/></td>
						<td>
							账号
						</td>
						<td>
							密码
						</td>
						<td>
							电话
						</td>
						<td>
							邮箱
						</td>
						<td>
							录入时间
						</td>
						<td>
							可用状态
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
					<c:forEach var="user" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><c:if test="${user.name != 'admin' }"><input type="checkbox" name="recordId" value="${user.id }"/></c:if></td>
							<td align="center">
								<c:if test="${user.name != 'admin' }">
								<a href="${user.id }"><c:if test="${not user.activable }"><font color='red'>${user.name }</font></c:if>
								<c:if test="${user.activable }">${user.name }</c:if></a>
								</c:if>
								<c:if test="${user.name == 'admin' and loginUser.name == 'admin' }">
									<a href="${user.id }"><c:if test="${not user.activable }"><font color='red'>${user.name }</font></c:if>
								<c:if test="${user.activable }">${user.name }</c:if></a>
								</c:if>
								<c:if test="${user.name == 'admin' and loginUser.name != 'admin' }">
									<c:if test="${not user.activable }"><font color='red'>${user.name }</font></c:if>
								<c:if test="${user.activable }">${user.name }</c:if>
								</c:if>
							</td>
							<td align="center">
								*****
							</td>
							<td align="center">
								${user.mobile }
							</td>
							<td align="center">
								${user.email }
							</td>
							<td align="center">
								<fmt:formatDate value="${user.insertDate }" pattern="yyyy-MM-dd"/>
							</td>
							<td align="center">
								<c:if test="${user.activable }">激活</c:if><c:if test="${not user.activable }">冻结</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/user/list" params="${params}"/>
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
						$.alert("删除用户账号后将不能登录及其它一切操作，且不可恢复，确定删除吗？", function() {
							$.post("delete", {
								id : id
							}, function(data) {
								$.alert("删除成功！");
								//window.location.reload();
								window.location.href = "list";
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
	</script>
</body>
</html>