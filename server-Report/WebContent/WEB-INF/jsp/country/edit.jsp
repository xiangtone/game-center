<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>国家</title>
	<script type="text/javascript">
		var menu_flag = 'country';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list">国家</a> -&gt; 编辑国家
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">编辑国家</div>
			
			<form id="form" action="" method="post">
			<input type="hidden" name="regionId" value="${obj.regionId}"/>
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">缩写</td>
							<td class="content">
								<input type="text" class="text" id="name" name="name" value="${obj.name}" maxlength='100'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span>
							</td>
						</tr>
						
						<tr>
							<td class="name">imsi</td>
							<td class="content">
								<input type="text" class="text" id="imsi" name="imsi" value="${obj.imsi}" maxlength='100'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span>
							</td>
						</tr>
						
					<tr>
							<td class="name">名称</td>
							<td class="content">
							<input type="text" class="text" id="remark" name="remark" value="${obj.remark}" maxlength='100'/>
							</td>
							<td class="content_info">
								<span id="span_activable"></span><span></span>
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
	<script type="text/javascript">
	//$(document).ready(function(){
		//		var options = "";
			//	$.each(company_type,function(index, context) {
				//	options += "<option value="+context.code+">"+context.name+"</option>";
			//	});
				//$("#type").html(options);
			//});
$(document).ready(function(){
		$("#form").validate({
			rules: {
				"name": {
	                                required: true,
	                                maxlength: 100
	                            }
			},
			messages: {
				"name": {
					required: "请输入广告",
	                maxlength: "名称最长不得超过100个字符"
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
	            			$.alert('修改国家成功', function(){window.location.href = "list";});
	            		}else{
	                		$.alert('修改失败,请重新输入');
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