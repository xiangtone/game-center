<%@page import="com.mas.rave.util.RandNum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>内容APP管理</title>
	<script type="text/javascript">
		var menu_flag = 'ownApkList';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 增加APP管理
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">增加APP管理</div>
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name"> 应用名</td>
							<td class="content">
								<input type="text" class="text" id="name" name="name" maxlength='100'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span>
							</td>
						</tr>
						<tr>
							<td class="name">责任人</td>
							<td class="content">
								<input type="text" class="text" id="source" name="source" maxlength="50" />
							</td>
							<td class="content_brief">
								50字符
							</td>
						</tr>
						<tr>
							<td class="name">logo上传(size)(96x96)</td>
							<td>
								<input type="file" class="text" id="bigApkFile" name="bigApkFile" maxlength='150'/>
								<span class="red">*</span>
							</td>
							<td class="content_bigApkFile">
								<span id="span_bigApkFile"></span>
							</td>
						</tr>
						<tr>
							<td class="name">logo上传(small)</td>
							<td colspan="2">
								<input type="file" class="text" id="apkFile" name="apkFile" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">标签</td>
							<td class="content" colspan="2">
								<input type="text" class="text" id="tags" name="tags" maxlength='100' value="No Label"/>
								<input type="hidden" name="free" id="free" value="1" />
							</td>
						</tr>
						<tr>
							<td class="name">app简介</td>
							<td class="content">
								<textarea rows="10" cols="50" id="brief" name="brief" >It is an amazing app. Join us to play!</textarea>
							</td>
							<td class="content_brief">
								<span id="span_brief">200字符</span>
							</td>
						</tr>
						<tr>
							<td class="name">app说明</td>
							<td>
								<textarea rows="15" cols="110" id="description" name="description" >
Although we don't have its decription, yet it won't stop you from playing it.Click "Download", then you'll find how brilliant this app is!
								</textarea>
							</td>
							<td class="content_info">
								<span id="span_description"></span>
							</td>							
						</tr>
						<tr>
							<td class="name">备注</td>
							<td class="content">
								<input type="text" class="text" id="remark" name="remark" maxlength='100' value="No remark"/>
							</td>
							<td class="content_info">
								<span id="span_remark"></span>
							</td>
						</tr>
						<tr>
							<td class="name">星级</td>
							<td class="content">
								<select name="stars">
									<option value="1" >1</option>
									<option value="2" >2</option>
									<option value="3" >3</option>
									<option value="4" >4</option>
									<option value="5" selected="selected" >5</option>
								</select>
							</td>
							<td></td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								 <input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<!--<input type="submit" class="bigbutsubmit" value="提交"/> -->
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
	                      maxlength: 100
	             },
	             "appApkFile1" :{
	            	 required: true
	             },
	             "bigApkFile" :{
	            	 required: true
	             },
	             "brief":{
	            	 required: true,
                     maxlength: 200
	             },
	             	          
		          "remark" :{
	                 maxlength: 30
		          },
		          "description" :{
		             maxlength: 5000
			      }
		          
		    },
			messages: {
				"name": {
					required: "请输入名称",
	                maxlength: "名称最长不得超过200个字符"
				},
				"appApkFile1": {
					required: "请至少输入一张图片"
				},
				"bigApkFile": {
					required: "请至少输入大图标"
				},
				"brief" :{
					required: "请输入简介",
					maxlength: "简介最长不得超过200个字符"
				},
		         "remark" :{
						maxlength: "备注最长不得超过30个字符"
		         },
		         "description" :{
						maxlength: "描述最长不得超过5000个字符"
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
	            			$.alert('添加应用成功', function(){window.location.href = "list";});
	            		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==3){
	            			$.alert('名称已经存在或数据出错,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else{
	                		$.alert('添加失败,请重新输入');
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