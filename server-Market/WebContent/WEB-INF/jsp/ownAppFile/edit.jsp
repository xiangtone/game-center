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
		当前位置: <a href="${ctx }/ownApp/list" style="color: red;">APK升级管理</a>-&gt; 修改APK文件信息
	</div>
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改APK文件信息</div>
			
			<form id="editForm" action="${ctx }/ownAppFile/update/${appFile.appInfo.id}" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
							<tr>
							<td class="name">应用名</td>
							<td class="content" colspan="2">
								${appFile.appInfo.name}
							</td>
						</tr>
						<tr>
							<td class="name">语言</td>
							<td class="content" colspan="2">
							  	 <input type="radio" name="language" id="language" value="1" <c:if test="${ appFile.language==1}">checked="checked"</c:if> />中文
								<input type="radio" name="language" id="language" value="2"  <c:if test="${ appFile.language==2}">checked="checked"</c:if> />英文
								<input type="radio" name="language" id="language" value="3" <c:if test="${ appFile.language==3}">checked="checked"</c:if> />其他
							</td>
						</tr>
						<tr>
							<td class="name">更新类型</td>
							<td  colspan="2">
								<input type="radio" name="upgradeType" id="upgradeType" value="1" <c:if test="${ appFile.upgradeType==1}">checked="checked"</c:if>/>Upgrade
								<input type="radio" name="upgradeType" id="upgradeType" value="2" <c:if test="${ appFile.upgradeType==2}">checked="checked"</c:if> />Forced Upgrade
							</td>
						</tr>
						<tr>
							<td class="name">是否生成增量</td>
							<td class="content">
								<input type="radio" id="type" name="type" value="2">是
								<input type="radio" id="type" name="type" value="1" checked="checked">否
							</td>
							<td class="content">&nbsp;</td>
						</tr>
						<tr>
							<td class="name">APK路径</td>
							<td class="content" colspan="2">
								<input type="text" class="text" id="url" name="url" value="${fn:escapeXml(appFile.url)}" maxlength='300'/>
							</td>
						</tr>
						<tr>
							<td class="name">APK上传</td>
							<td class="content" colspan="2">
								<input type="file" class="text" id="appApkFile" name="appApkFile" maxlength='150'/>(必须为apk文件)
								<input type="hidden" id="apkKey" name="apkKey" value="${appFile.apkKey }">
								<input type="hidden" id="id" name="id" value="${appFile.id }">
								<input type="hidden" id="versionCode" name="versionCode" value="${appFile.versionCode }">
								<input type="hidden" id="versionName" name="versionName" value="${appFile.versionName }">
								<input type="hidden" id="packageName" name="packageName" value="${appFile.packageName }">
								<input type="hidden" id=serverId name="serverId" value="${appFile.serverId }">
								<input type="hidden" id="appName" name="appName"  value="${appFile.appName}" />
								<input type="hidden" id="fileSize" name="fileSize"  value="${appFile.fileSize}" />
								<input type="hidden" id="haslist" name="haslist"  value="${appFile.haslist}" />
								<input type="hidden" id="type" name="type" value="0">
							</td>
						</tr>
						<tr>
							<td class="name">更新描述</td>
							<td >
								<textarea rows="15" cols="50" id="updateInfo" name="updateInfo"  >${fn:escapeXml(appFile.updateInfo) }</textarea>
							</td>
							<td class="content_info">
								<span id="span_updateInfo"></span>
							</td>	
						</tr>
						<tr>
							<td class="name">备注</td>
							<td >
							<textarea rows="5" cols="50" id="remark" name="remark" >${fn:escapeXml(appFile.remark) }</textarea></td>
							<td class="content_info">
								<span id="span_remark"></span>
							</td>						
						</tr>
						<tr>
							<td class="name">适应机型</td>
							<td  class="content" colspan="2">
								<input type="radio" name="osType" id="osType" value="1" <c:if test="${appFile.osType==1}">checked="checked"</c:if>/>ios
								<input type="radio" name="osType" id="osType" value="2" <c:if test="${appFile.osType==2}">checked="checked"</c:if>/>andriod
							</td>
						</tr>
						<tr>
							<td class="name">状态</td>
							<td class="content" colspan="2">
								<input type="radio" name="state" id="state" value="1" <c:if test="${appFile.state==true}">checked="checked"</c:if> />是
								<input type="radio" name="state" id="state" value="0" <c:if test="${appFile.state==false}">checked="checked"</c:if> />否
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn" colspan="2">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/ownAppFile/list/${appFile.appInfo.id}';" value="返回"/>
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
		$("#editForm").validate({
			rules: {
				 "updateInfo": {
                     maxlength: 5000
               },
               "remark": {
                     maxlength: 30
               }
			},
			messages: {
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
				$("#butsubmit_id").attr("disabled","true");
				setDisabled('input[type="submit"]', window.document);
				$(form).ajaxSubmit({
					type : "POST",
					dataType : "json",
					 success: function(response){
						 if(response.flag == "0"){
								$.alert('更新应用成功', function(){
									window.location.href = "${ctx}/ownAppFile/list/${appFile.appInfo.id}";
								});
							}else if(response.flag == "1"){
								$.alert('更新版本低于当前版本或同一版本,请重新输入');
					    		$("#butsubmit_id").attr("disabled","false");
							}else if(response.flag == "2"){
								$.alert('上传文件不是APK文件,请重新输入');
								$("#butsubmit_id").attr("disabled","false");
							}else if(response.flag == "5"){
								$.alert('上传的apk有问题,解析apk包名出错,请重新输入');
								$("#butsubmit_id").attr("disabled","false");
							}else if(response.flag == "6"){
								$.alert(response.pac+'包名已经存在或者apkKey已经存在,请重新输入');
								$("#butsubmit_id").attr("disabled","false");
							}else if(response.flag == "7"){
								$.alert('输入数据长度超出,请重新输入');
								$("#butsubmit_id").attr("disabled","false");
							}else if(response.flag == "8"){
								$.alert('数据插入失败,请重新输入');
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