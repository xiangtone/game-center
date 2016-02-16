<%@page import="com.mas.rave.util.RandNum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'communal';
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置:<a href="${ctx }/communal/list" style="color: red;">全部app管理</a>-&gt;增加apk文件
	</div>
	<div id="lockWindow" class="easyui-window" style="padding:10px">
		<div id="progressbar" class="easyui-progressbar" style="width:220px;float: left;"></div><div id="show" style="width:20px;float:rigth;">0%</div>
	</div>
	<%-- 初始化弹出窗口 --%>
	<div id="showApkWindow" class="easyui-window" title="请选择APK" iconCls="icon-save" closed="true"  modal="true" style="width:600px;height:350px;padding:5px;background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div id="showApk" region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				&nbsp;
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="saveCheckbox()">确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消</a>
			</div>
		</div>
	</div>
	<div class="mainoutside mt18">
		<div id="secmainoutside" >
			<div class="title">添加APK</div>
			<form id="addForm" action="${ctx}/communalFile/add/${appInfo.id}" method="post" enctype="multipart/form-data">
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
							<td class="name">所属国家</td>
							<td class="content">
							<select name="raveId" id="raveId">
								<c:forEach var="country" items="${countrys}">
									<option value="${country.id}" >${country.name}(${country.nameCn})</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
							<span class="red">*</span>
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
										<option value="${channel.id}" >${channel.name}</option>
								</c:forEach>
								</select>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_channelId"></span><span>渠道不能为空</span>
							</td>
						</tr>
						<tr>
							<td class="name"> 语言</td>
							<td class="content">
							  	 <input type="radio" name="language" id="language" value="1" />中文
								<input type="radio" name="language" id="language" value="2" checked="checked"/>英文
								<input type="radio" name="language" id="language" value="3"/>其他
								
								<input type="hidden" name="upgradeType" id="upgradeType" value="1" />
							</td>
							<td class="content">&nbsp;</td>
						</tr>
						<tr>
							<td class="name">是否生成增量</td>
							<td >
								<input type="radio" id="type" name="type" value="0" checked="checked">否
								<input type="radio" id="type" name="type" value="2">是
							</td>
							<td class="content">&nbsp;</td>
						</tr>
						<tr>
							<td class="name">APK上传</td>
							<td>
								<input type="radio" id="localFile" name="uploadType" checked="checked" value="1" title="本地上传" onclick="showFtp(this.value)"/>本地上传&nbsp;&nbsp;
								<input type="radio" id="ftpFile" name="uploadType" value="2" title="FTP上传" onclick="showFtp(this.value)"/>FTP上传
							</td>
							<td class="content_info">
								&nbsp;
							</td>
						</tr>
						<tr>
						<td class="name">&nbsp;请选择apk文件</td>
						<td >
						<input type="file" class="text" id="appApkFile" name="appApkFile" maxlength='150'/>(必须为apk文件)
								<span class="red">*</span><span id="span_appApkFile"></span>
						</td>
						<td>
						请输入apk文件路径 : <input type="text" class="text" id="ftpApkFile" disabled="disabled" name="ftpApkFile" value="${fn:escapeXml(requestScope.ftp_apk_defaul_path) }" style="width:250px;" maxlength='250'/>
								<span class="red">*</span> <input type="button"  class="bigbutsubmit" id="showBtn" disabled="disabled" value="选择文件" />
						</td>
						</tr>
						<tr>
							<td class="name">更新描述</td>
							<td>
								<textarea rows="10" cols="50" id="updateInfo" name="updateInfo" >No update description</textarea>
								 <input type="hidden" name="cpId" id="cpId" value="300001">
								 <input type="hidden" name="channelId" id="channelId" value="200001">
							</td>
							<td>
								<textarea rows="10" cols="50" id="apkInfo" name="apkInfo" readonly="readonly" ></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">备注</td>
							<td >
							<textarea rows="5" cols="50" id="remark" name="remark" >No remark</textarea></td>
							<td class="content">&nbsp;</td>
						</tr>
						<tr>
							<td class="name">适应机型</td>
							<td >
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
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/communalFile/list/${appInfo.id}';" value="返回"/>
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
				url : "${ctx}/communalFile/ftpUrl",
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
		//初始化锁屏窗口
		$("#lockWindow").window({
			width  : 250,
			height : 100,
			modal  : true,
			inline : false,
			closed : true,
			title  : '文件上传...',
			collapsible : false,
			minimizable : false,
			maximizable : false,
			closable    : true,
			draggable   : false,
			resizable   : false
		});

		//初始化进度条
		$("#progressbar").progressbar({value: 0});
		$("#addForm").validate({
			rules: {
	           "appApkFile": {
	                              required: true,
	                              maxlength: 200
	                      },
				"initDowdload" :{
					required: true,
					positiveinteger:true
				}
			},
			messages: {
				"appApkFile": {
					required: "请选择apk文件!",
	                maxlength: "apk文件地址最长不得超过200个字符"
				},
				"initDowdload" :{
					required: "下载数不能为空",
					positiveinteger:"下载数必须为正整数"
				}
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			},
			submitHandler: function(form) {
				$("#butsubmit_id").attr("disabled",true);
				$("#appApkFile").removeAttr("disabled");
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
								window.location.href = "${ctx}/communalFile/list/${appInfo.id}";
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
						}else if(response.flag=="10"){
							$.alert("同一应用中所有国家的包名必须一致");							
						}else if(response.flag == "3"){
							if(response.pac!=null){
								$.alert(response.pac+'包名已经更新');
								window.location.href = "${ctx}/communalFile/list/${appInfo.id}";
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
	//打开窗口
	function showFtp(val){
		if(val == 1){
			$("#appApkFile").removeAttr("disabled");
			$("#ftpApkFile").attr("disabled",true);
			$("#showBtn").attr("disabled",true);
		}else{
			$("#appApkFile").attr("disabled",true);
			$("#appApkFile").val("");
			$("#ftpApkFile").removeAttr("disabled");
			$("#showBtn").removeAttr("disabled");
			$("#span_appApkFile").html("");
		}
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
	</script>
</body>
</html>