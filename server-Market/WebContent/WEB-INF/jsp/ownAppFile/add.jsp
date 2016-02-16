<%@page import="com.mas.rave.util.RandNum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'ownApkList';
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置:<a href="${ctx }/ownApp/list" style="color: red;">APK升级管理</a>-&gt;增加apk文件
	</div>
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">添加APK</div>
			<form id="addForm" action="${ctx}/ownAppFile/add/${appInfo.id}" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">应用名(id)</td>
							<td class="content">
								<input type="hidden" name="appId" id="appId" value="${appInfo.id}">
								${appInfo.name}(${appInfo.id })
							</td>
							<td class="content">&nbsp;</td>
						</tr>
						<tr>
							<td class="name"> 语言</td>
							<td class="content">
							  	 <input type="radio" name="language" id="language" value="1" />中文
								<input type="radio" name="language" id="language" value="2" checked="checked"/>英文
								<input type="radio" name="language" id="language" value="3"/>其他
							</td>
							<td class="content">&nbsp;</td>
						</tr>
						<tr>
							<td class="name">更新类型</td>
							<td class="content">
								<input type="radio" name="upgradeType" id="upgradeType" value="1" />Upgrade
								<input type="radio" name="upgradeType" id="upgradeType" value="2" checked="checked"/>Forced Upgrade
							</td>
							<td class="content">&nbsp;</td>
						</tr>
						<tr>
							<td class="name">是否生成增量</td>
							<td colspan="2" class="content">
								<input type="radio" id="type" name="type" value="2">是
								<input type="radio" id="type" name="type" value="1" checked="checked">否
							</td>
						</tr>
						<tr>
							<td class="name">apk上传</td>
							<td class="content">
								<input type="file" class="text" id="appApkFile" name="appApkFile" maxlength='150'/>(必须为apk文件)
							</td>
							<td class="content"><span id="span_appApkFile"></span></td>
						</tr>
						<tr>
							<td class="name">更新描述</td>
							<td>
								<textarea rows="10" cols="50" id="updateInfo" name="updateInfo" >No update description</textarea>
							</td>
							<td class="content_info">
							<span id="span_updateInfo"></span>
							</td>
						</tr>
						<tr>
							<td class="name">备注</td>
							<td >
							<textarea rows="5" cols="50" id="remark" name="remark" >No remark</textarea></td>
							<td class="content_info">
								<span id="span_remark"></span>
							</td>						
						</tr>
						<tr>
							<td class="name">适应机型</td>
							<td class="content">
								<input type="radio" name="osType" id="osType" value="1" />osType
								<input type="radio" name="osType" id="osType" value="2" checked="checked"/>andriod
							</td>
							<td class="content">&nbsp;</td>
						</tr>
						<tr>
							<td class="name">是否有效</td>
							<td class="content">
								<input type="radio" name="state" id="state" value="1" checked="checked"/>是
								<input type="radio" name="state" id="state" value="0" />否
							</td>
							<td class="content">&nbsp;</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交" id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/ownAppFile/list/${appInfo.id}';" value="返回"/>
							</td>
							<td class="content">&nbsp;</td>
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
	           "appApkFile": {
	                              required: true,
	                              maxlength: 200
	                      },
	                      
				 "updateInfo": {
                     maxlength: 5000
               },
				 "remark": {
                     maxlength: 30
               },               
			},
			messages: {
				"appApkFile": {
					required: "请选择apk文件!",
	                maxlength: "apk文件地址最长不得超过200个字符"
				},
			     "updateInfo" :{
						maxlength: "更新描述最长不得超过5000个字符"
		         },
		         "remark" :{
						maxlength: "备注最长不得超过30个字符"
		         }					
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			},
			submitHandler: function(form) {
				$("#butsubmit_id").attr("disabled",true);
				$(form).ajaxSubmit({
					type : "POST",
					dataType : "json",
					 success: function(response){
						 $("#butsubmit_id").removeAttr("disabled");
						 var uploadType = $("input[type='radio'][name='uploadType']:checked");
						 if($(uploadType).val() == 2){
							 $("#appApkFile").attr("disabled",true);
						 }
						 $("#show").html("100%");
						 $('#progressbar').progressbar('value', 100);
						 $("#lockWindow").window("close");
						 $('#progressbar').progressbar('value',15);
						 if(response.flag == "0"){
							$.alert('添加应用成功', function(){
								window.location.href = "${ctx}/ownAppFile/list/${appInfo.id}";
							});
						}else if(response.flag == "1"){
							$.alert('上传版本低于当前版本或同一版本,请重新输入');
						}else if(response.flag == "2"){
							$.alert('上传文件不是APK文件,请重新输入');
						}else if(response.flag == "5"){
							$.alert('上传的apk有问题,解析apk包名出错,请重新输入');
						}else if(response.flag == "6"){
							$.alert(response.pac+'包名已经存在或者apkKey已经存在,请重新输入');
						}else if(response.flag == "7"){
							$.alert('输入数据长度超出,请重新输入');
						}else if(response.flag == "8"){
							$.alert('数据插入失败,请重新输入');
						}else if(response.flag == "3"){
							if(response.pac!=null){
								$.alert(response.pac+'包名已经更新');
								window.location.href = "${ctx}/ownAppFile/list/${appInfo.id}";
							}else{
								$.alert('增加失败,请重新输入');
							}
						}else{
							$.alert('上传失败,请重新输入');
						}
		            }
	            });
				$("#lockWindow").window("open");
				//调用虚拟进度条
				autoIncrement();
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