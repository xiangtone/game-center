<%@page import="com.mas.rave.util.RandNum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appCollection';
		$(function(){
			$('#win').window({
				title: 'New Title',
				width: 600,
				modal: true,
				shadow: false,
				closed: true,
				height: 300
			});
		});
		function resize(){
			$('#win').window("open");
		}
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 新增应用专辑
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title"> 新增应用专辑</div>
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<input type="hidden" id="type" name="type" value="${type }">
				<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">标题(英语)</td>
							<td class="content">
								<input type="text" class="text" id="name" name="name" maxlength='100'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span>
							</td>
						</tr>
						<tr>
							<td class="name">标题(中文)</td>
							<td class="content">
								<input type="text" class="text" id="nameCn" name="nameCn" maxlength='100'/>
							</td>
							<td class="content_info">
								<span id="span_nameCn"></span>
							</td>
						</tr>
						<tr>
							<td class="name">所属国家</td>
							<td class="content">
							<select name="raveId" id="raveId">
								<c:forEach var="country" items="${countrys}">
									<option value="${country.id}" >${country.name}(${country.nameCn})</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
							<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_raveId"></span>
							</td>
						</tr> 
						<tr>
							<td class="name">图标上传(big)</td>
							<td>
								<input type="file" class="text" id="bigImageFile" name="bigImageFile" maxlength='150'/>
								<span class="red">*</span>
							</td>
							<td class="content_bigImageFile">
								<span id="span_bigImageFile"></span>
							</td>
						</tr>
						<tr>
							<td class="name">图标上传(small)</td>
							<td>
								<input type="file" class="text" id="logoFile" name="logoFile" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">说明</td>
							<td class="content">
								<textarea rows="10" cols="50" id="description" name="description" >No instructions</textarea>
							</td>
							<td class="content_description">
								<span id="span_description"></span>
							</td>
						</tr>
						<tr>
							<td class="name">排序值</td>
							<td class="content">
								<input type="text" class="text" id="sort" name="sort" maxlength='9' value="0"/>
							</td>
							<td class="content_sort">
								<span id="span_sort"></span>
							</td>
						</tr>
						<tr>
							<td class="name">是否有效</td>
							<td class="content">
								<input type="radio" name="state" id="state" value="1" checked="checked"/>是
								<input type="radio" name="state" id="state" value="0" />否
							</td>
						</tr>	
						<tr>
					    <td class="name">创建时间</td>
						 <td>
						<input class="Wdate"  id="createTime1" name="createTime1" style="width: 160px" 
              	         onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'});"
              	         value="<fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' value='${now}' />"/>
						</td>
						<td class="content">&nbsp;</td>
						</tr>					
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								 <input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='list?type=${type}';" value="返回"/>
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
        }, "排序值只能输入整数");  
		$("#addForm").validate({
			rules: {
				"name": {
	                 required: true,
	                 maxlength: 100
	             },
	             "nameCn":{
                     maxlength: 100,                    
	             },
	             "bigImageFile" :{
	            	 required: true,
                     maxlength: 100
	             },
	             "sort" :{
	            	 required: true,
	            	 integer: true
	             },
	             "description" :{
                     maxlength: 500
	             }
			},
			messages: {
				"name": {
					required: "请输入名称",
	                maxlength: "名称最长不得超过100个字符",
				},
				 "nameCn":{
	            	 maxlength: "名称最长不得超过100个字符"
	             },
				"bigImageFile": {
					required: "请至少输入大图标",
	                maxlength: "大图标最长不得超过100个字符"
				},
				"sort" :{
					required:"请输入排序值"
	             },
				"description": {
		            maxlength: "描述最长不得超过500个字符"
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
	            			$.alert('添加应用成功', function(){window.location.href = "list?type=${type}";});
		        		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==3){
	            			$.alert('专辑名已经存在');
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