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
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 修改应用专辑
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改应用专辑</div>
			
			<form id="editForm" action="" method="post" enctype="multipart/form-data">
				<div class="border">
				<table class="mainadd">
					<tbody>										
						<tr>
							<td class="name">标题(英文)</td>
							<td class="content">
								<input type="text" class="text" id="name" name="name" value="${fn:escapeXml(appCollection.name) }" maxlength='100'/>
								<input type="hidden" id="collectionId" name="collectionId" value="${appCollection.collectionId }">
								<input type="hidden" id="type" name="type" value="${appCollection.type}">														
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span>
							</td>
						</tr>
						<tr>
							<td class="name">标题(中文)</td>
							<td class="content">
								<input type="text" class="text" id="nameCn" name="nameCn" value="${fn:escapeXml(appCollection.nameCn) }" maxlength='100'/>
							</td>
							<td class="content_info">
								<span id="span_nameCn"></span>
							</td>
						</tr>
						<tr>
						<td class="name">所属国家</td>
							<td class="content">
							<label>${appCollection.country.name}(${appCollection.country.nameCn})</label>
							<input type="hidden" id="raveId" name="raveId" value="${appCollection.country.id}">		
							</td>
							<td class="content_info">
								<span id="span_raveId"></span>
							</td>
						</tr>
						<tr>
							<td class="name">图标上传(big)</td>
							<td>
								<input type="file" class="text" id="bigImageFile" name="bigImageFile" maxlength='150'/>
							</td>					
						</tr>
						<tr>
							<td class="name">图标地址(big)</td>
							<td>
								<textarea rows="1" cols="100" id="bigicon" name="bigicon" readonly="readonly">${fn:escapeXml(appCollection.bigicon) }</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">图标上传(small)</td>
							<td>
								<input type="file" class="text" id="logoFile" name="logoFile" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">图标地址(small)</td>
							<td>
								<textarea rows="1" cols="100" id="icon" name="icon" readonly="readonly">${fn:escapeXml(appCollection.icon) }</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">说明</td>
							<td class="content">
								<textarea rows="10" cols="50" id="description" name="description" >${fn:escapeXml(appCollection.description) }</textarea>
							</td>
							<td class="content_description">
								<span id="span_description"></span>
							</td>
						</tr>
						<tr>
							<td class="name">排序值</td>
							<td class="content">
								<input type="text" class="text" id="sort" name="sort" maxlength='9' value="${fn:escapeXml(appCollection.sort) }"/>
							</td>
							<td class="content_info">
								<span id="span_sort"></span>
							</td>
						</tr>
						<tr>
							<td class="name">是否有效</td>
							<td class="content">
								<input type="radio" name="state" id="state" value="1" <c:if test="${appCollection.state==true }">checked="checked"</c:if> />是
								<input type="radio" name="state" id="state" value="0" <c:if test="${appCollection.state==false }">checked="checked"</c:if> />否
							</td>
						</tr>
						<tr>
					    <td class="name">创建时间</td>
						 <td>
						<input class="Wdate"  id="createTime1" name="createTime1" style="width: 160px" 
              	         onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'});"
              	         value="<fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' value='${appCollection.createTime}' />"/>
						</td>
						<td class="content">&nbsp;</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								 <input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='list?type=${appCollection.type}';" value="返回"/>
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
		$("#editForm").validate({
			rules: {
				"name": {
	                      required: true,
	                      maxlength: 100
	             },
	             "nameCn": {
                     maxlength: 100
          		  },
	             "sort" :{
	            	 required: true,
	            	 integer:true
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
				"nameCn": {
	                maxlength: "名称最长不得超过100个字符"
         		  },
				"sort" :{
	            	 required: "排序值不能为空"
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
	            			$.alert('更新应用成功', function(){window.location.href = "list?type=${appCollection.type}";});
		        		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==3){
	            			$.alert('专辑名已经存在');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else{
	                		$.alert('更新失败,请重新输入');
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