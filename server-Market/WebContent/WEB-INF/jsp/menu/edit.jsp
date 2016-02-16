<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'menu';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">当前位置:<span id="childTitle"></span> &gt; 编辑菜单项</div>

	<div class="mainoutside">
		<div class="secmainoutside">
			<div class="title">编辑菜单</div>
			<form id="addForm" action="${ctx }/menu/edit" method="post">
			<div class="border">
			<table class="mainlist">
				<thead><input type="hidden" name="id" id="id" value="${menu.id }"/></thead>
				<tbody>
					<tr>
						<td class="name" align="center"><label>菜单名称 :</label></td>
						<td class="content"><input type="text" name="name" id="name" size="30" value="${fn:escapeXml(menu.name) }" maxlength="30" onkeyup="value=value.replace(/[^\a-zA-Z\u4E00-\u9FA5]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\a-zA-Z\u4E00-\u9FA5]/g,''))"/>&nbsp;<font color=red>*&nbsp;&nbsp;只能输入中文和字母</font></td>
						<td align="left"><font color=red>(必填)</font></td>
					</tr>
					<tr>
						<td class="name" align="center"><label>菜单编码 :</label></td>
						<td class="content"><input type="text" name="code" id="code" value="${fn:escapeXml(menu.code) }" maxlength="30"  onkeyup="value=value.replace(/[^\a-zA-Z]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\a-zA-Z]/g,''))"/>&nbsp;<font color=red>*&nbsp;&nbsp;只能输入字母</font></td>
						<td align="left"><font color=red>(必填)</font></td>
					</tr>
					<tr>
						<td class="name" align="center"><label>菜单类型 :</label></td>
						<td class="content">
						<select name="type.id" id="typeId" onchange="selectParents(this.value)">
							<%--<option></option> --%>
							<c:forEach items="${menuTypes }" var="types">
								<option value="${types.id }" <c:if test="${types.id == menu.type.id}">selected='selected'</c:if> >${types.name }</option>
							</c:forEach>
						</select>
						</td>
						<td></td>
					</tr>
					<tr>
					<td class="name" align="center"><label>所属父级菜单项 : </label></td>
					<td class="content">
						<select name="parentId" id="parentId">
							<option value="0">--请选择--</option>
						</select><span id="note"></span>
					</td>
					<td></td>
					</tr>
					<tr>
						<td class="name" align="center"><label>排序 :</label></td>
						<td class="content"><input type="text" name="seq" id="seq" value="${fn:escapeXml(menu.seq) }" maxlength="5"/></td>
						<td></td>
					</tr>
					<tr>
						<td class="name" align="center"><label>URI :</label></td>
						<td class="content"><input type="text" name="uri" id="uri" value="${fn:escapeXml(menu.uri) }" maxlength="50"/></td>
						<td></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="3" align="center">
							<input type="button" value="提交" id="submitBtn"  class="bigbutsubmit" />&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" class="bigbutsubmit" onclick="javascript:history.back()" value="返回"/>
						</td>
					</tr>
				</tfoot>
			</table>
			</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			$("#submitBtn").click(function(){
				var name = $("#name").val();
				var code = $("#code").val();
				var seq = $("#seq").val();
				var typeId = $("#typeId").val();
				var parentId = $("#parentId").val();
				if(name == ""){
					$.alert("菜单名称不能为空!");
					$("#name").focus();
					return;
				}
				if(code == ""){
					$.alert("菜单编码不能为空!");
					$("#code").focus();
					return;
				}
				if(isNaN(seq)||seq.indexOf(".") != -1||seq.indexOf("-")  != -1){
					$.alert("排序值只能输入0和正整数!");
					$("#seq").focus();
					return;
				}
				if(parseInt(typeId) > 1 && parseInt(parentId) == 0){
					$.alert("请选择父级菜单!");
					return;
				}
				$.ajax({
					url : "${ctx}/menu/queryByName",
					type : "POST",
					dataType : "json",
					data : {"name":$("#name").val(),"code":$("#code").val(),"id":$("#id").val()},
					success : function(data){
						var result = eval("("+data+")");
						if(result.success == 0 ){
							$.alert("已存在相同的菜单名称或编码,请检查!");
							return;
						}else{
							$("#addForm").submit();
						}
					},
					error : function(error){
						$.alert(error);
					}
				});
			});
			var typeId = $("#typeId").val();
			selectParents(typeId);
		});
		function selectParents(type){
			if(type != "" && type != undefined ){
				//如果为一级菜单,则无需查询上一级菜单项
				if(parseInt(type) == 1){
					$("#parentId").html("");
					$("#note").html("");
					document.getElementById("parentId").add(new Option("--请选择--",0));
					return;
				}
				$.ajax({
					url : "${ctx}/menu/queryParentMenu",
					type : "POST",
					dataType : "json",
					data : {"typeId" : type},
					success : function (data){
						if(null != data){
							$("#parentId").html("");
							document.getElementById("parentId").add(new Option("--请选择--",0));
							var menuId = "${menu.id }";
							$.each(data,function(key,val){
								//过滤掉当前菜单项,不在父级菜单项列表中显示
								if(parseInt(menuId) != parseInt(key)){
									document.getElementById("parentId").add(new Option(val,key));
								}
							});
							var parentId = "${menu.parentId}";
							$("#parentId").val(parentId);
							$("#note").html("");
							$("#note").html("<font color=red>*</font>");
						}
					},
					error : function (error){
						$.alert("Error:" + error);
					}
				});
			}
		}
	</script>
</body>
</html>