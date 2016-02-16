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
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 修改角色资料
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改角色资料</div>
			<form id="editForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">角色名称</td>
							<td class="content">
								<input type="hidden" name="id" value="${role.id }"/>
								<input type="text" class="text" id="name" name="name" value="${fn:escapeXml(role.name) }" maxlength='256'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span><span>请填写角色名称</span>
							</td>
						</tr>
						<tr>
							<td class="name">角色描述</td>
							<td class="content">
								<input type="text" class="text" id="description" name="description" value="${fn:escapeXml(role.description) }" maxlength='256'/>
								<span class="red">*</span>
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
				</div>
			</form>
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
	             "description":{
	            	 required: true,
                     maxlength: 20
	             }
			},
			messages: {
				"name": {
					required: "请输入角色名称",
	                maxlength: "名称最长不得超过20个字符"
				},
				"description": {
					required: "请输入角色描述",
	                maxlength: "描述最长不得超过20个字符"
				}
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
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
		        		if(data.flag=="0"){
	            			$.alert('修改角色成功', function(){window.location.href = "list";});
	            		}else{
	            			$.alert('角色名称重复或数据有误,请重新输入');
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