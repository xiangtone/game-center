<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'cp';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 编辑cp
	</div>

	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">编辑cp</div>

			<form id="form" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">cp名</td>
							<td class="content"><input type="text" class="text"
								id="name" name="name"
								value="${fn:escapeXml(cp.name)}" maxlength='100' /> <span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span><span>请填cp名称</span>
							</td>
						</tr>
						<tr>
							<td class="name">描述</td>
							<td class="content" colspan="2"><input type="text" class="text"
								id="description" name="description" value="${fn:escapeXml(cp.description)}" maxlength='256' /></td>
						</tr>
						<tr>
							<td class="name">备注</td>
							<td class="content" colspan="2"><input type="text" class="text"
								id="remark" name="remark" value="${fn:escapeXml(cp.remark)}"
								maxlength='256' /> 
						</tr>
						<tr>
							<td class="name">密码</td>
							<td class="content">
								<input type="text" class="text" id="pwd" name="pwd" value="${fn:escapeXml(cp.pwd)}" maxlength='13'/><span class="red">*</span>
								<input type="hidden" name="state" id="state" value="true" />
							</td>
							<td class="content_pwd">
								<span id="span_pwd"></span><span>密码不能为空</span>
							</td>
						</tr>
						<!--<tr>
							<td class="name">状态</td>
							<td class="content">
								 <input type="radio" name="state" id="state" value="true" <c:if test="${cp.state==true}">checked="checked"</c:if>/>是
								<input type="radio" name="state" id="state" value="false" <c:if test="${cp.state==false}">checked="checked"</c:if>/>否
							</td>
						</tr>-->
						<tr>
							<td class="name">回调列表</td>
							<td colspan="2"><textarea rows="2" cols="50" id="payWay" name="payWay">${fn:escapeXml(cp.payWay)}</textarea>
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
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#form").validate({
			rules: {
				"name": {
	                                required: true,
	                                maxlength: 100
	                            },
				"pwd":{
					required: true,
				 	maxlength: 13,
				 	minlength:8
				}
			},
			messages: {
				"name": {
					required: "请输入cp名",
	                maxlength: "名称最长不得超过100个字符"
				},
				"pwd": {
					required: "密码不能为空",
	                maxlength: "密码最长不得超过13个字符",
	                minlength: "密码最长不得小于8个字符"
				}
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
		        			$.alert('修改成功', function(){window.location.href = "list";});
	            		}else{
	            			$.alert('添加失败,cp名已经存在或者数据有误,请重新输入');
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