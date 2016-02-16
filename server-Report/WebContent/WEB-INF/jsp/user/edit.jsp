<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>用户管理&gt; 修改用户资料</title>
	<script type="text/javascript">
		var menu_flag = 'user';
	</script>
</head>
<body>
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改员工资料</div>
			<div class="border">
			<form id="editForm" action="" method="post">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">账号</td>
							<td class="content">
								<input type="hidden" name="id" value="${user.id }"/>
								<input type="text" class="text" id="name" name="name" value="${user.name }" maxlength='50' onkeyup="value=value.replace(/[^\w]/ig,'')" <c:if test="${user.name=='admin' }">readonly="readonly"</c:if> />
								<span class="red">*<font color=red>(只允许输入50位数字,字母及下划线)</font></span>
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
								<input type="text" class="text" id="mobile" name="mobile" value="${user.mobile }" maxlength='256'/>
							</td>
							<td class="content_info">
								<span id="span_mobile"></span><span>员工联系手机号</span>
							</td>
						</tr>
						<tr>
							<td class="name">邮箱</td>
							<td class="content">
								<input type="text" class="text" id="email" name="email" value="${user.email }" maxlength='256'/>
							</td>
							<td class="content_info">
								<span id="span_email"></span><span>员工公司邮箱</span>
							</td>
						</tr>
						<tr>
							<td class="name">启用状态</td>
							<td class="content">
								<input type="radio" name="activable"  value="1" <c:if test="${user.activable == 1 }">checked</c:if>/>&nbsp;<label>激活</label>&nbsp;
								<input type="radio" name="activable"  value="0" <c:if test="${user.activable == 0 }">checked</c:if> />&nbsp;<label>冻结</label>&nbsp;
							</td>
							<td class="content_info">
								<span id="span_activable"></span><span>员工账号激活状态</span>
							</td>
						</tr>
						<tr>
							<td class="name">所属角色</td>
							<td class="content">
								<c:forEach var="role" items="${roleVos}" varStatus="status">
								<c:if test="${loginUser.name != 'admin'}">
									<c:if test="${role.checked }">
										<input type="radio" name="roles[0].id" value="${role.id}" checked="checked" />${role.name }
									</c:if>
								</c:if>
								<c:if test="${loginUser.name == 'admin'}">
									<input type="radio" name="roles[0].id" value="${role.id}" <c:if test="${role.checked }">checked="checked"</c:if> />${role.name }
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
				"name": {required: true,maxlength: 50},
	            "password": {required: true,maxlength: 50}     
			},
			messages: {
				"name": {required: "请输入账户名",maxlength: "名称最长不得超过50个字符"},
				"password": {required: "请输入登录密码",maxlength: "密码最长不得超过50个字符"}
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			},
			submitHandler: function(form) {
				$("#butsubmit_id").attr("disabled","true");
				setDisabled('input[type="submit"]', window.document);
				$(form).ajaxSubmit({
					 success: function(response){
		        		var data = eval("("+response+")");
		        		if(data.flag==0){
	            			$.alert('修改用户成功', function(){window.location.href = "list";});
	            		}else{
	                		$.alert('此账号已存在,请重新输入');
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