<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'user';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 修改员工资料
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改员工资料</div>
			<form id="editForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">账号</td>
							<td class="content">
								<input type="hidden" name="id" value="${user.id }"/>
								<input type="text" class="text" id="name" name="name" value="${fn:escapeXml(user.name) }" maxlength='30' <c:if test="${user.name=='admin' }">readonly="readonly"</c:if> onkeyup="value=value.replace(/[^\w]/ig,'')"/>
								<span class="red">*<font color=red>(只允许输入30位数字,字母及下划线)</font></span>
							</td>
							<td class="content_info">
								<span id="span_name"></span><span>请填写登录唯一账号<c:if test="${user.name=='admin' }"><font color="red">admin为系统管理员默认账号,不能更改!</font></c:if></span>
							</td>
						</tr>
						<tr>
							<td class="name">密码</td>
							<td class="content">
								<input type="password" class="text" id="password" name="password" value="${user.password }" maxlength='50' onkeyup="value=value.replace(/[^\w]/ig,'')"/>
								<span class="red">*<font color=red>(只允许输入50位数字,字母及下划线)</font></span>
							</td>
							<td class="content_info">
								<span id="span_password"></span><span>请填写密码</span>
							</td>
						</tr>
						<tr>
							<td class="name">电话</td>
							<td class="content">
								<input type="text" class="text" id="mobile" name="mobile" value="${fn:escapeXml(user.mobile) }" maxlength='15'/>
							</td>
							<td class="content_info">
								<span id="span_mobile"></span><span>员工联系手机号</span>
							</td>
						</tr>
						<tr>
							<td class="name">邮箱</td>
							<td class="content">
								<input type="text" class="text" id="email" name="email" value="${fn:escapeXml(user.email) }" maxlength='50'/>
							</td>
							<td class="content_info">
								<span id="span_email"></span><span>员工公司邮箱</span>
							</td>
						</tr>
						<tr>
							<td class="name">启用状态</td>
							<td class="content">
								<input type="radio" name="activable"  value="1" <c:if test="${user.activable }">checked</c:if>/>&nbsp;<label>激活</label>&nbsp;
								<input type="radio" name="activable"  value="0" <c:if test="${not user.activable }">checked</c:if> />&nbsp;<label>冻结</label>&nbsp;
							</td>
							<td class="content_info">
								<span id="span_activable"></span><span>员工账号激活状态</span>
							</td>
						</tr>
						<tr>
							<td class="name">所属角色</td>
							<td class="content">
								<c:forEach var="role" items="${roleVos}" varStatus="status">
									<!-- 如果是非admin用户 -->
									<c:if test="${loginUser.name != 'admin' }">
										<!-- 只显示当前用户所属角色 -->
										<c:if test="${role.checked}">
											<input type="checkbox" name="roles[${status.index }].id" value="${role.id}" checked="checked" />${role.name }<br/>
										</c:if>
									</c:if>
									<!-- 如果是admin用户,显示所有的角色选项 -->
									<c:if test="${loginUser.name == 'admin' }">
										<input type="checkbox" name="roles[${status.index }].id" value="${role.id}" <c:if test="${role.checked }">checked="checked"</c:if> />${role.name }<br/>
									</c:if>
								</c:forEach>
							</td>
							<td class="content_info">
								<span id="span_activable"></span><span>员工所属角色</span>
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="reset" class="bigbutsubmit" id ="btnReset" value="重填"/>
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
		//手机号码验证
		  jQuery.validator.addMethod("mobile", function(value, element) {
		   var length = value.length;
		   return this.optional(element) || (length == 11 && /^\d+$/.test(value));
		  }, "手机号码格式错误!");
			//邮箱验证
		  jQuery.validator.addMethod("email", function(value, element) {
		   return this.optional(element) || /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(value);
		  }, "邮箱格式错误!");
		  
		  $("#btnReset").click(function () {     
			  $("#span_mobile").html("").next().show();
			  $("#span_email").html("").next().show();
		  });
		  
		$("#editForm").validate({
			rules: {
				"name": {
	                                required: true,
	                                maxlength: 30
	                            },
	             "password": {
	                                required: true,
	                                maxlength: 50
	                            },
	            "mobile": {
	                                required: true,
	                                maxlength: 11,
	                                mobile:true
	                            },
				"email": {
	                                required: true,
	                                maxlength: 40,
	                                email:true
	                            }   
			},
			messages: {
				"name": {
					required: "请输入角色名称",
	                maxlength: "名称最长不得超过30个字符"
				},
				"password": {
					required: "请输入登录密码",
	                maxlength: "密码最长不得超过50个字符"
				},
				"mobile": {
					required: "请输入电话",
	                maxlength: "电话最长不得超过11个数字",
	                mobile:"请输入正确格式电话"
				},
				"email": {
					required: "请输入邮箱",
	                maxlength: "邮箱最长不得超过40个字符",
	                email:"请输入正确格式邮箱"
				}
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			},
			submitHandler: function(form) {
				if($("input[type=checkbox]:checked").length>1){
					alert("只能选择一个角色");
					return;
				}
				$("#butsubmit_id").attr("disabled","true");
				setDisabled('input[type="submit"]', window.document);
				$(form).ajaxSubmit({
					type : "POST",
					dataType : "json",
					 success: function(response){
						 var data = eval("("+response+")");
						 if(data.flag==0){
							 $.alert('更新成功', function(){window.location.href = "list";});
		            		}else{
		                		$.alert('更新失败,请重新输入');
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