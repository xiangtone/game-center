<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
			var menu_flag = 'app';
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置: <a href="${ctx }/app/list" style="color: red;">自营app管理</a>-&gt; 修改APK文件信息
	</div>
	<%-- 初始化弹出窗口 --%>
	<div id="showApkWindow" class="easyui-window" title="请选择APK" iconCls="icon-save" closed="true"  modal="true" style="width:600px;height:350px;padding:5px;background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div id="showApk" region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				&nbsp;
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<%--
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="allCheck()">全选</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="allCancel()">重置</a> --%>
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="saveCheckbox()">确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消</a>
			</div>
		</div>
	</div>
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改APK文件信息</div>
			
			<form id="editForm" action="${ctx }/appFile/update/${appFile.appInfo.id}" method="post" enctype="multipart/form-data">
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
						<td class="name">所属国家</td>
							<td class="content">
								<label>${appFile.country.name}(${appFile.country.nameCn})</label>
							</td>
							<td class="content_appPicFile">
								<span id="span_provinceId"></span>
							</td>
						</tr>
						<tr>
						<td class="name">所属渠道(父渠道)</td>
							<td class="content">
								<select name="channelId" id="channelId">
								<c:forEach var="channel" items="${channels}">
										<option value="${channel.id}" <c:if test="${channel.id==appFile.channel.id }">selected="selected"</c:if>  >${channel.name}</option>
								</c:forEach>
								</select>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_channelId"></span><span>渠道不能为空</span>
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
						<c:if test="${appFile.appInfo.free!=0 }">
							<tr>
							<td class="name">更新类型</td>
							<td  colspan="2">
								<input type="radio" name="upgradeType" id="upgradeType" value="1" <c:if test="${ appFile.upgradeType==1}">checked="checked"</c:if>/>不更新
								<input type="radio" name="upgradeType" id="upgradeType" value="2" <c:if test="${ appFile.upgradeType==2}">checked="checked"</c:if> />更新
								<input type="radio" name="upgradeType" id="upgradeType" value="3" <c:if test="${ appFile.upgradeType==3}">checked="checked"</c:if>/>强制更新
							</td>
						</tr>
						</c:if>
						<tr>
							<td class="name">APK路径</td>
							<td class="content" colspan="2">
								<input type="text" class="text" id="url" name="url" value="${fn:escapeXml(appFile.url)}" maxlength='300'/>
							</td>
						</tr>
						<tr>
							<td class="name">APK上传</td>
							<td>
								<input type="radio" id="localFile" name="uploadType" checked="checked" value="1" title="本地上传" onclick="showFtp(this.value)"/>本地上传&nbsp;&nbsp;
								<input type="radio" id="ftpFile" name="uploadType" value="2" title="FTP上传" onclick="showFtp(this.value)"/>FTP上传
								
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
								<input type="hidden" id="raveId" name="raveId" value="${appFile.country.id}">
							</td>
						</tr>
						<tr>
							<td class="name">本地apk上传</td>
							<td colspan="2">
								<input type="file" class="text" id="appApkFile" name="appApkFile" maxlength='150'/>(必须为apk文件)
							</td>
						</tr>
						<tr>
							<td class="name">FTP上传</td>
							<td colspan="2">
						请输入apk文件路径 : <input type="text" class="text" id="ftpApkFile" disabled="disabled" name="ftpApkFile" value="${fn:escapeXml(requestScope.ftp_apk_defaul_path) }" style="width: 300px;" maxlength='250'/>
						<input type="button"  class="bigbutsubmit" id="showBtn" disabled="disabled" value="选择文件" />
						<input type="text" class="text" id="apkInfo" name="apkInfo" value="" style="width: 400px;" readonly="readonly">
						</td>
						</tr>
						<tr>
							<td class="name">渠道号</td>
							<td>
							<select name="channelId" id="channelId">
								<c:forEach var="channel" items="${channels}">
									<option value="${channel.id}" <c:if test="${channel.id== appFile.channel.id}">selected="selected"</c:if> >${channel.name}</option>
								</c:forEach>
							
							</select>&nbsp;&nbsp;
								<span class="red">*</span>
								 <input type="hidden" name="cpId" id="cpId" value="${appFile.cp.id}">
							</td>
							<td class="content_info">
								<span id="span_channelId"></span>
							</td>
						</tr>
						
						<tr>
							<td class="name">更新描述</td>
							<td colspan="2">
								<textarea rows="15" cols="50" id="updateInfo" name="updateInfo"  >${fn:escapeXml(appFile.updateInfo) }</textarea>
							</td>
							<td class="content_info">
								<span id="span_updateInfo"></span>
							</td>	
						</tr>
						<tr>
							<td class="name">适应机型</td>
							<td  colspan="2">
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
						<tr>
							<td class="name">更新时间</td>
							<td class="content" colspan="2">
								<input type="text" id="updateTime1"  name="updateTime1" value='<fmt:formatDate value="${appFile.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" style="width:200px"/> 
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn" colspan="2">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/appFile/list/${appFile.appInfo.id}';" value="返回"/>
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
		jQuery.validator.addMethod("positiveinteger", function(value, element) {
			   var aint=parseInt(value);	
			    return aint >= 0 && (aint+"")==value;   
			  }, "请输入一个正整数.");
		
		$("#showBtn").click(function(){
			var ftpApkFile = $("#ftpApkFile").val();
			if(ftpApkFile != "" ){
				ftpApkFile += "/";
			}
			$.ajax({
				url : "${ctx}/appFile/ftpUrl",
				type : "POST",
				dataType : "json",
				data : {"apkPath":ftpApkFile},
				success : function(data){
					var checkboxs = "";
					var i = 1;
					$.each(data,function(key,val){
						//checkboxs += "<input type='checkbox' name='apk_" + key +"'";
						checkboxs += "<input type='radio' name='apk_radio'";
						checkboxs += " id='" + key + "'";
						checkboxs += " value='" + val + "' />";
						checkboxs += key + "&nbsp;&nbsp;";
						if(i % 2 == 0){
							checkboxs += "<br/>";
							i = 0;
						}
						i++;
					});
					if(checkboxs == ""){
						checkboxs = "未找到该目录下的apk文件!";
					}
					$("#showApk").children().remove();
					$("#showApk").html("");
					$("#showApk").append(checkboxs);
					$("#showApkWindow").window("open");
				},
				error : function(error){
					$.messager.alert("出现未知异常!",error);
				}
			});
		});
		
		$("#editForm").validate({
			rules: {
				"channelId": {
					required: true
		        },
		        "initDowdload" :{
					required: true,
					maxlength: 10,
					positiveinteger:true
				},
				 "updateInfo": {
                     maxlength: 5000
               }
			},
			messages: {
				"channelId": {
					required: "请输入渠道号"
				},
				"initDowdload" :{
					required: "下载数不能为空",
					maxlength: "下载数不能超过10位数",
					positiveinteger:"下载数必须为正整数"
				},
				"updateInfo" :{
					maxlength: "更新描述最长不得超过5000个字符"
		         }
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			},
			submitHandler: function(form) {
				$("#butsubmit_id").attr("disabled","true");
				$("#appApkFile").removeAttr("disabled");
				setDisabled('input[type="submit"]', window.document);
				$(form).ajaxSubmit({
					type : "POST",
					dataType : "json",
					 success: function(response){
						 var uploadType = $("input[type='radio'][name='uploadType']:checked");
						 if($(uploadType).val() == 2){
							 $("#appApkFile").attr("disabled",true);
						 }
						 if(response.flag == "0"){
								$.alert('更新应用成功', function(){
									window.location.href = "${ctx}/appFile/list/${appFile.appInfo.id}";
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
							}else if(response.flag=="10"){
								$.alert("同一应用中所有国家的包名必须一致");							
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
	//打开窗口
	function showFtp(val){
		if(val == 1){
			$("#appApkFile").removeAttr("disabled");
			$("#ftpApkFile").attr("disabled",true);
			$("#showBtn").attr("disabled",true);
		}else{
			$("#appApkFile").attr("disabled",true);
			$("#ftpApkFile").removeAttr("disabled");
			$("#showBtn").removeAttr("disabled");
		}
		$("#appApkFile").val("");
		$("#apkInfo").val("");
	}
	
	//保存数据
	function saveCheckbox(){
		$("#showApkWindow").window("close");
		var checkboxs = $("#showApk input[type='radio'][name='apk_radio']:checked");
		var checkValues = "";
		for (var i = 0; i < checkboxs.length; i++) {
			checkValues += $(checkboxs[i]).val() + "\n";
		}
		$("#apkInfo").val("");//先清空原有数据
		if(checkValues != ""){
			$("#apkInfo").val(checkValues);
		}
	}
	
	//取消
	function cancel(){
		$("#showApk").children().remove();
		$("#showApk").html("");
		$("#showApkWindow").window("close");
	}
	//全选
	function allCheck(){
		$("#showApk input[type='checkbox'][name^='apk_']").attr("checked",true);
	}
	//重置 
	function allCancel(){
		$("#showApk input[type='checkbox'][name^='apk_']").removeAttr("checked");
	}
	</script>
</body>
</html>