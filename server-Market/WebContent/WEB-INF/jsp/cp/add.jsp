<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'cp';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 新增cp信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">添加新cp</div>
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">cp名</td>
							<td class="content">
								<input type="text" class="text" id="name" name="name" maxlength='256'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span><span>请填cp名称</span>
							</td>
						</tr>
						<tr>
							<td class="name">描述</td>
							<td class="content" colspan="2">
								<input type="text" class="text" id="description" name="description" maxlength='256'/>
							</td>
						</tr>
						<tr>
							<td class="name">回调列表</td>
							<td class="content" colspan="2">
								<input type="text" class="text" id="payWay" name="payWay" maxlength='256'/>
								 <input type="hidden" name="state" id="state" value="true" />
							</td>
						</tr>
						<!--<tr>
							<td class="name">是否有效</td>
							<td class="content">
								<input type="radio" name="state" id="state" value="1" checked="checked"/>是
								<input type="radio" name="state" id="state" value="0" />否
							</td>
						</tr>-->
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
		$("#addForm").validate({
			rules: {
				"name": {
	                                required: true,
	                                maxlength: 20
	                            }    
			},
			messages: {
				"name": {
					required: "请输入cp名称",
	                maxlength: "名称最长不得超过20个字符"
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
		        			$.alert('添加成功', function(){window.location.href = "list";});
	            		}else if(data.flag==2){
	            			$.alert('添加失败,cp名已经存在,请重新输入');
	            		}else{
	            			$.alert('添加失败,数据有误,请重新输入');
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