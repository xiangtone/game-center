<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>角色管理&gt; 修改角色资料</title>
	<script type="text/javascript">
		var menu_flag = 'role';
	</script>
</head>
<body>
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改角色资料</div>
			<div class="border">
			<form id="editForm" action="" method="post">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name" >角色名称</td>
							<td class="content" >
								<input type="hidden" name="id" value="${role.id }"/>
								<input type="text" class="text" style="width:200px;" id="name" name="name" value="${role.name }" maxlength='30' onkeyup="value=value.replace(/[^\w\u4E00-\u9FA5]/ig,'')"/>
								<span class="red">*<font color=red>(只允许输入30位中文,数字,字母及下划线)</font></span>
							</td>
							<td class="content_info" >
								<span id="span_name"></span><span>请填写角色名称</span>
							</td>
						</tr>
						<tr>
							<td class="name">角色描述</td>
							<td class="content">
								<input type="text" class="text" style="width:200px;" id="description" name="description" value="${role.description }" maxlength='256'/>
							</td>
							<td class="content_info">
								<span id="span_description"></span><span>请填写角色描述</span>
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
				"name": {
	                                required: true,
	                                maxlength: 20
	                            },
	            "password": {
	                                required: true,
	                                maxlength: 20
	                            }     
			},
			messages: {
				"name": {
					required: "请输入角色名称",
	                maxlength: "名称最长不得超过20个字符"
				}
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
	            			$.alert('修改角色成功', function(){window.location.href = "list";});
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