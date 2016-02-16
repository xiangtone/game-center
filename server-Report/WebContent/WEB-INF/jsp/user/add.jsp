<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>用户管理&gt; 新增用户</title>
	<script type="text/javascript">
		var menu_flag = 'user';
	</script>
</head>
<body>
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">添加新用户</div>
			<div class="border">
			<form id="addForm" action="" method="post">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">账号</td>
							<td class="content">
								<input type="text" class="text" id="name" name="name" maxlength='50' onkeyup="value=value.replace(/[^\w]/ig,'')"/>
								<span class="red">*<font color=red>(只允许输入50位数字,字母及下划线)</font></span>
							</td>
							<td class="content_info">
								<span id="span_name"></span><span>请填写登录唯一账号</span>
							</td>
						</tr>
						<tr>
							<td class="name">密码</td>
							<td class="content">
								<input type="password" class="text" id="password" name="password" maxlength='50' onkeyup="value=value.replace(/[^\w]/ig,'')"/>
								<span class="red">*<font color=red>(只允许输入50位数字,字母及下划线)</font></span>
							</td>
							<td class="content_info">
								<span id="span_password"></span><span>请填写密码</span>
							</td>
						</tr>
						<tr>
							<td class="name">电话</td>
							<td class="content">
								<input type="text" class="text" id="mobile" name="mobile" maxlength='256'/>
							</td>
							<td class="content_info">
								<span id="span_mobile"></span><span>员工联系手机号</span>
							</td>
						</tr>
						<tr>
							<td class="name">邮箱</td>
							<td class="content">
								<input type="text" class="text" id="email" name="email" maxlength='256'/>
							</td>
							<td class="content_info">
								<span id="span_email"></span><span>员工公司邮箱</span>
							</td>
						</tr>
						<tr>
							<td class="name">启用状态</td>
							<td class="content">
								<input type="radio" name="activable"  value="1" checked/>&nbsp;<label>激活</label>&nbsp;
								<input type="radio" name="activable"  value="0" />&nbsp;<label>冻结</label>&nbsp;
							</td>
							<td class="content_info">
								<span id="span_activable"></span><span>员工账号激活状态</span>
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='list';" value="返回"/>
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
		$("#addForm").validate({
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
				setDisabled('input[type="submit"]', window.document)
				$(form).ajaxSubmit({
					 success: function(response){
		        		var data = eval("("+response+")");
		        		if(data.flag==0){
	            			$.alert('添加用户成功', function(){window.location.href = data.id + '';});
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