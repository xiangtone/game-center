<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
	var menu_flag = '${sessionScope.menuFlag}';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		
		当前位置: <a href="${ctx }/app/add/2/${appInfo.id}">APK截图信息</a> -&gt;新增截图
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">添加APK截图</div>
		
			<form id="addForm" action="${ctx }/appPic/add/${appInfo.id}" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">应用名</td>
							<td class="content" colspan="2">
								${appInfo.name}
							</td>
						</tr>
						<tr>
							<td class="name">标题</td>
							<td class="content">
								<input type="text" class="text" id="title" name="title" maxlength='100' value="No Title"/>
							</td>
							<td class="content_info">
								<span id="span_title"></span>
							</td>
						</tr>
						<tr>
							<td class="name"> 描述</td>
							<td class="content">
							<textarea rows="10" cols="30" id="description" name="description">No Description</textarea>
							</td>
							<td class="content_info">
								<span id="span_description"></span>
							</td>
						</tr>
						<tr>
							<td class="name">截图上传</td>
							<td>
								<input type="file" class="text" id="appPicFile" name="appPicFile" maxlength='150'/>
								<span class="red">*</span>
							</td>
							<td class="content_appPicFile">
								<span id="span_appPicFile"></span>
							</td>
						</tr>
						<tr>
							<td class="name">简介</td>
							<td >
								<textarea rows="8" cols="100" id="remark" name="remark" >No Remark</textarea>
							</td>
							<td class="content_info">
								<span id="span_remark"></span>
							</td>
						</tr>
						<tr>
							<td class="name">是否有效</td>
							<td class="content" colspan="2">
								<input type="radio" name="state" id="state" value="1" checked="checked"/>是
								<input type="radio" name="state" id="state" value="0" />否
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx }/appPic/list/${appInfo.id}';" value="返回"/>
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
	             "appPicFile" :{
	            	 required: true
	             },
	             "title": {
                     required: true,
                     maxlength: 100
                 },
	             "description": {
                     maxlength: 500
                 },
                 "remark": {
                     maxlength: 30
                 }
	             
			},
			messages: {
				"appPicFile": {
					required: "截图地址不能为空"
				},
				"title": {
					required: "请输入标题",
	                maxlength: "名称最长不得超过100个字符"
				},
				"description": {
	                maxlength: "描述最长不得超过500个字符"
				},
				"remark": {
	                maxlength: "简介最长不得超过30个字符"
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
		        		if(response.flag==0){
	            			$.alert('添加图片成功', function(){window.location.href = "${ctx }/appPic/list/${appInfo.id}?src=${src}";});
	            		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else{
	                		$.alert('增加失败,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}
		        		setabled('input[type="submit"]', window.document);
		            },
		            error: function(response){
		            	alert("error "+response.status);
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