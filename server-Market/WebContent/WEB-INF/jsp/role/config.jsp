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
		当前位置: <a href="../list"><span id="childTitle"></span></a> -&gt; 角色菜单及操作权限配置
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">${role.name }</div>
			<form id="editForm" action="" method="post">
			<div class="border">
				<input type="hidden" name="id" value="${role.id }"/>
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">角色名称</td>
							<td class="content">
								<span>${role.name }</span>
							</td>
							<td class="content_info">
								
							</td>
						</tr>
						<tr>
							<td class="name">展示菜单项</td>
							<td class="content">
								<c:forEach var="menuType" items="${menuTypes}">
									<h4>${menuType.name }</h4>
									<c:forEach var="menu" items="${menuType.menuvos}">
										<input type="checkbox" name="menus" value="${menu.id}" <c:if test="${menu.checked }">checked="checked"</c:if> />${menu.name }&nbsp;&nbsp;
										<br/>
									</c:forEach>
									<br/> 
								</c:forEach>
							</td>
							<td class="content_info">
								<span id="span_description"></span><span></span>
							</td>
						</tr>
						<tr>
							<td class="name">可操作权限</td>
							<td class="content">
								<c:forEach var="operations" items="${roleVos}">
									<h4>${operations.name }</h4>
									<c:forEach var="operation" items="${operations.vos}" varStatus="status">
										<input type="checkbox" name="operations" value="${operation.id}" <c:if test="${operation.checked }">checked="checked"</c:if> />${operation.name }&nbsp;&nbsp;
										<c:if test="${status.count%4==0}">
											<br/>
										</c:if>
									</c:forEach>
									<br/> 
									<br/> 
								</c:forEach>
							</td>
							<td class="content_info">
								<span id="span_description"></span><span></span>
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="reset" class="bigbutsubmit" value="重填"/>
							</td>
						</tr>
					</tfoot>
				</table>
				</div>
			</form>
		</div>
	</div>
	
	<script type="text/javascript">
	$(document).ready(function(){
		$("#editForm").validate({
			rules: {
				 
			},
			messages: {
				
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			},
			submitHandler: function(form) {
				$("#butsubmit_id").attr("disabled","true");
				setDisabled('input[type="submit"]', window.document);
				$(form).ajaxSubmit({
					type : "POST",
					dataType : "json",
					 success: function(response){
						 var data = eval("("+response+")");
			        		if(data.flag==0){
		            			$.alert('配置角色成功', function(){window.location.href = "../list";});
		            		}else{
		                		$.alert('角色名称重复,请重新输入');
		                		$("#butsubmit_id").attr("disabled","false");
		            		}
			        		setabled('input[type="submit"]', window.document);;
		            }
	            });
			},
			success: function(label){
				var labelparent = label.parent();
				label.parent().html(labelparent.next().html()).attr("class", "valid");
			},
			onkeyup:false
		});
			
	});
	</script>
</body>
</html>