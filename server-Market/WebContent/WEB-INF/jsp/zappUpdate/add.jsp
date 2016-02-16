<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'market';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 新增版本升级信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">新增版本升级信息</div>
			
			<form id="addForm" action="" method="post">
				<div class="border">
			<input type="hidden" id="apkId" name="apkId" value="${apkId}">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">版本名</td>
							<td class="content">
								<input type="text" class="text" id="versionName" name="versionName" maxlength='256'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_versionName"></span>
							</td>
						</tr>
						<tr>
							<td class="name">版本号</td>
							<td class="content">
								<input type="text" class="text" id="versionCode" name="versionCode" maxlength='11'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_versionCode"></span>
							</td>
						</tr>
						<tr>
						<td class="name">升级状态</td>
						<td class="content">
						<select name="upgradeType" id="upgradeType">
							<option value="1">不升级</option>
							<option value="2" selected="selected">升级</option>
							<option value="3">强制升级</option>
						</select>
						</td>
						<td class="content_info">
						</td>
						</tr>		
						<tr>
							<td class="name">更新内容</td>
							<td class="content">
								<textarea rows="10" cols="50" id="updateInfo" name="updateInfo" ></textarea>
							</td>
							<td class="content_info">
								<span id="span_updateInfo"></span>
							</td>
						</tr>				
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/zappUpdate/${apkId}/list';" value="返回"/>
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
		$.validator.addMethod("integer", function (value, element) {
            var regex = /^-?\d+$/;
            return  regex.test(value);
        }, "版本号只能输入整数");
		$("#addForm").validate({
			rules: {
				"versionName": {
	                                required: true,
	                                maxlength: 100
	                            },
	            "versionCode": {
	                                required: true,
	                                integer: true,
	                                maxlength: 11
	                            },    
	            "updateInfo": {
	                                maxlength: 5000
	             }  	                            
			},
			messages: {
				"versionName": {
					required: "请输入版本名",
	                maxlength: "版本名最长不得超过100个字符"
				},
				"versionCode": {
					required: "请输入版本号",
	                maxlength: "版本号最长不得超过11位数"
				},
				"updateInfo": {
	                maxlength: "更新内容不得超过5000个字符"
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
		        		if(data.flag=='0'){
		        			$.alert('添加成功', function(){window.location.href = "${ctx}/zappUpdate/${apkId}/list";});
	            		}else if(data.flag=='2'){
	            			$.alert('添加失败,版本名已经存在,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(data.flag=='3'){
	            			$.alert('添加失败,版本号已经存在,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else{
	            			$.alert('添加失败,请确认输入数据合法再联系管理员!!!');
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