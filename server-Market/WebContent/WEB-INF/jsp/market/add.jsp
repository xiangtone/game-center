<%@page import="com.mas.rave.util.RandNum"%>
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
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 新增平台
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">添加平台</div>
			
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td class="name">所属分类</td>
							<td class="content">
								<select name="categoryId">
								<c:forEach var="category" items="${categorys}">
								<c:if test="${category.id<=3}">
									<option value="${category.id}" >${category.categoryCn}</option>
								</c:if>
								</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
						<td class="name">所属渠道(父渠道)</td>
							<td class="content">
								<select name="channelId" id="channelId">
								<c:forEach var="channel" items="${channels}">
									<c:if test="${channel.fatherId == 1 }" >
										<option value="${channel.id}" >${channel.name}</option>
									</c:if>
								</c:forEach>
								</select>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_channelId"></span><span>渠道不能为空</span>
							</td>
						</tr>
						<tr>
							<td class="name">cp</td>
							<td>
							<select name="cpId" id="cpId">
								<c:forEach var="cp" items="${cps}">
									<option value="${cp.id}" >${cp.name}</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
							<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_cpId"></span><span>cp不能为空</span>
							</td>
						</tr>
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
							<td class="name">别名</td>
							<td class="content">
								<textarea rows="10" cols="50" id="anotherName" name="anotherName" ></textarea>
							</td>
							<td class="content_info">
							<span id="span_anotherName"></span>
							</td>
						</tr>						
						<tr>
							<td class="name">图标上传</td>
							<td>
								<input type="file" class="text" id="apkFile" name="apkFile" maxlength='150'/>
							</td>
							
						</tr>
						<tr>
							<td class="name">大图标上传(96x96)</td>
							<td>
								<input type="file" class="text" id="bigApkFile" name="bigApkFile" maxlength='150'/>
								<span class="red">*</span>
							</td>
							<td class="content_bigApkFile">
								<span id="span_bigApkFile"></span>
							</td>
						</tr>
						<tr>
							<td class="name">图标1上传</td>
							<td>
								<input type="file" class="text" id="appApkFile1" name="appApkFile1" maxlength='150'/>
								<span class="red">*</span>
							</td>
							<td class="content_appApkFile1">
								<span id="span_appApkFile1"></span>
							</td>
						</tr>
						<tr>
							<td class="name">图标2上传</td>
							<td>
								<input type="file" class="text" id="appApkFile2" name="appApkFile2" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">图标3上传</td>
							<td>
								<input type="file" class="text" id="appApkFile3" name="appApkFile3" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">简介</td>
							<td class="content">
								<textarea rows="10" cols="50" id="brief" name="brief" >简介</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">标签</td>
							<td class="content">
								<input type="text" class="text" id="tags" name="tags" maxlength='100' value="无标签"/>
								<input type="hidden" name="free" id="free" value="1" />
							</td>
						</tr>
						<tr>
							<td class="name">描述</td>
							<td class="content">
								<input type="text" class="text" id="brief" name="brief" maxlength='100' value="无描述"/>
							</td>
						</tr>
						<tr>
							<td class="name">app说明</td>
							<td class="content">
								<textarea rows="10" cols="50" id="description" name="description" >无说明</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">备注</td>
							<td class="content">
								<input type="text" class="text" id="remark" name="remark" maxlength='100' value="无备注"/>
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
						</tr>
						<tr>
							<td class="name">是否有效</td>
							<td class="content">
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
		var flag = "${flag}";
		if(flag == "true"){
			$.alert('添加应用成功', function(){
				window.location.href = "list";
			});
		}else if(flag == "false"){
			$.alert('添加失败,请重新输入');
    		$("#butsubmit_id").attr("disabled","false");
		}
		$("#addForm").validate({
			rules: {
				"name": {
	                   required: true,
	                   maxlength: 100
	              },
	             "appApkFile1" :{
	            	 required: true,
                     maxlength: 100
	             },
	             "bigApkFile" :{
	            	 required: true,
                     maxlength: 100
	             },
	             "channelId":{
	            	 required: true
	             },
	             "cpId":{
	            	 required: true
	             },
		          "anotherName" :{
		             maxlength: 2500
			      }
	             
			},
			messages: {
				"name": {
					required: "请输入名称",
	                maxlength: "名称最长不得超过200个字符"
				},
				"appApkFile1": {
					required: "请至少输入一张图片",
	                maxlength: "图片最长不得超过200个字符"
				},
				"bigApkFile": {
					required: "请至少输入大图标",
	                maxlength: "大图标最长不得超过200个字符"
				},
				"channelId":{
					required: "渠道不能为空"
	             },
	             "cpId":{
	            	 required: "cp不能为空"
	             },
		         "anotherName" :{
					maxlength: "别名最长不得超过2500个字符"
		         }
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			},
			submitHandler: function(form) {
				$("#butsubmit_id").attr("disabled","true");
				setDisabled('input[type="submit"]', window.document);
				$(form).sumbit();
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