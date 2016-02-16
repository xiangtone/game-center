<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>角色管理&gt; 角色菜单及操作权限配置</title>
	<script type="text/javascript">
		var menu_flag = 'role';
		$(function(){
			$("h4").click(function(){
				$(this).next().slideToggle();
				//$(this).siblings("h4").next().slideUp();
			});
			
			$(".checkbox_parent").click(function(){
				var checkbox_childrens = $(this).parent().next().find(":checkbox");
				if($(this).is(":checked")){
					$(checkbox_childrens).attr("checked",true);
				}else{
					$(checkbox_childrens).attr("checked",false);
				}
			});
			
			$(".checkbox_child").click(function(){
				var checkbox_parent = $(this).parent().parent().prev().find(":checkbox");
				if($(this).is(":checked")){
					$(checkbox_parent).attr("checked",true);
				}else{
					var checks = $(this).parent().siblings().find(":checked");
					if(checks.length == 0){
						$(checkbox_parent).attr("checked",false);
					}
				}
			});
		});
	</script>
</head>
<body>
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">${role.name }</div>
			<div class="border">
			<form id="editForm" action="" method="post">
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
								 <c:forEach var="menus" items="${allMenus}">
						      		<c:if test="${menus.parentId == 0 }">
						      			<h4><input type="checkbox" name="menus" class="checkbox_parent" id="${menus.code }" value="${menus.id }" <c:if test="${menus.checked }">checked="checked"</c:if>>${menus.name }</h4>
										<ul class="menu_ul">
							       		<c:forEach var="menu" items="${menus.childMenu}">
											<li class="menu_li"><input type="checkbox" name="menus" class="checkbox_child" id="${menu.code }" value="${menu.id }" <c:if test="${menu.checked }">checked="checked"</c:if>>${menu.name}</li>
										</c:forEach>
										</ul>
						      		</c:if>
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
			</form>
			</div>
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
				$("#butsubmit_id").attr("disabled",true);
				setDisabled('input[type="submit"]', window.document);
				$(form).ajaxSubmit({
					 success: function(response){
		        		var data = eval("("+response+")");
		        		if(data.flag==0){
	            			$.alert('配置角色成功', function(){
	            				window.location.href = "../list";
	            			});
	            		}else{
	                		$.alert('角色名称重复,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}
		        		setabled('input[type="submit"]', window.document);
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