<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appannie';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
		<%-- 初始化弹出窗口 --%>
	<div id="showHtmlFileWindow" class="easyui-window" title="请选择APK" iconCls="icon-save" closed="true"  modal="true" style="width:600px;height:350px;padding:5px;background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div id="showHtmlFile" region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
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
			<div class="title">annie分发</div>
			<form id="topAddForm" action="appannieCountryRank" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						   <td class="name">app类别</td>
							<td class="content">
								<select id="categoryId" name="categoryId">
								    <option value="1" >Homes</option>
									<option value="2" >Apps</option>
									<option value="3" >Games</option>		
								</select>
								<span class="red">*</span>
							</td>
							<td class="content">
								<span id="span_category"></span>
							</td>
						</tr>
						<tr>
						   <td class="name">国家</td>
							<td colspan="2" class="content">
							<select name="raveId" id="raveId">
								<c:forEach var="country" items="${countrys}">
								  <c:if test="${country.id!=1}">
									<option value="${country.id}" >${country.name}(${country.nameCn})</option>								  
								  </c:if>
								</c:forEach>
							</select>
								<span class="red">*</span>&nbsp;&nbsp;
								(分发&nbsp;&nbsp;<span class="red">${defaultCountry}</span>&nbsp;&nbsp;的应用在appannie上的排行升降（如2升2名、-24降24名）参与到Global中计算)
							</td>
							
						</tr>
						<tr>
							<td class="name">html文件上传</td>
							<td class="content">
								<input type="radio" id="localFile" name="uploadType" checked="checked" value="1" title="本地上传" onclick="showFtp(this.value)"/>本地上传&nbsp;&nbsp;
								<input type="radio" id="ftpFile" name="uploadType" value="2" title="FTP上传" onclick="showFtp(this.value)"/>FTP上传
							</td>
							<td class="content">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="name">是否进入appannie上获取数据</td>
							<td class="content">
								<input type="radio" name="stateType" value="1" title="启用"/>启用&nbsp;&nbsp;
								<input type="radio" name="stateType" checked="checked" value="2" title="不启用"/>不启用
							</td>
							<td class="content">
								&nbsp;
							</td>
						</tr>				
						<tr>
						<td class="name">&nbsp;请选择html文件</td>
						<td class="content">
						<input type="file" class="text" id="htmlFile" name="htmlFile" maxlength='150'/>
								<span class="red">*</span><span id="span_htmlFile"></span>
						</td>
						<td class="content">
						<span> 请输入html文件路径 : </span>
						<input type="text" class="text" id="ftphtmlFile" disabled="disabled" name="ftphtmlFile" value="${requestScope.ftp_apk_defaul_path }" style="width:200px;" maxlength='250'/>
								<span class="red">*</span><span id="span_ftphtmlFile"></span><input type="button"  class="bigbutsubmit" id="showBtn" disabled="disabled" value="选择文件" />
						</td>								
						</tr>
						<tr>
							<td class="name">文件来源说明</td>
							<td class="content" colspan="2">						   
							<span class="red">(网页地址：
							<a href="http://www.appannie.com/apps/google-play/top/indonesia/" target="new">
							http://www.appannie.com/apps/google-play/top/indonesia/</a>)
							 <br/>选择OverAll/Applications/Games搜索，如需要Top500资源，请点击Load-all,
							 <br/> 然后用IE的F12开发人员工具-->HTML -->保存 来获取html文件</span> 
							
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								 <input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
							</td>
						</tr>
					
					</tfoot>
						
				</table>
				</div>
			</form>
		
		</div>
		<div class="border">
			<table class="mainadd">
			            <tr>
				   			<td class="name" ><div id="topResult" style="padding-top: 20px"></div></td>
				       </tr>
			</table>
			</div>
	</div>
	<script type="text/javascript">
	//打开窗口
	function showFtp(val){
		if(val == 1){
			$("#htmlFile").removeAttr("disabled");
			$("#ftphtmlFile").attr("disabled",true);
			$("#showBtn").attr("disabled",true);
		}else{
			$("#htmlFile").attr("disabled",true);
			$("#htmlFile").val("");
			$("#ftphtmlFile").removeAttr("disabled");
			$("#showBtn").removeAttr("disabled");
			$("#span_htmlFile").html("");
		}
	}
	//保存数据
	function saveCheckbox(){
		$("#showHtmlFileWindow").window("close");
		var checkboxs = $("#showHtmlFile input[type='radio'][name='htmlFile_radio']:checked");
		var checkValues = "";
		for (var i = 0; i < checkboxs.length; i++) {
			checkValues += $(checkboxs[i]).val() + "\n";
		}
		$("#ftphtmlFile").val("");//先清空原有数据
		if(checkValues != ""){
			$("#ftphtmlFile").val(checkValues);
		}
	}
	//取消
	function cancel(){
		$("#showHtmlFile").children().remove();
		$("#showHtmlFile").html("");
		$("#showHtmlFileWindow").window("close");
	}
	//全选
	function allCheck(){
		$("#showHtmlFile input[type='checkbox'][name^='htmlFile_']").attr("checked",true);
	}
	//重置 
	function allCancel(){
		$("#showHtmlFile input[type='checkbox'][name^='htmlFile_']").removeAttr("checked");
	}
	$(document).ready(function(){
		$("#showBtn").click(function(){
			var ftphtmlFile = $("#ftphtmlFile").val();
			if(ftphtmlFile != "" ){
				ftphtmlFile += "/";
			}
			$.ajax({
				url : "${ctx}/appAlbumColumn/ftpUrl",
				type : "POST",
				dataType : "json",
				data : {"htmlPath":ftphtmlFile},
				success : function(data){
					var checkboxs = "";
					$.each(data,function(key,val){
						//checkboxs += "<input type='checkbox' name='apk_" + key +"'";
						checkboxs += "<input type='radio' name='htmlFile_radio'";
						checkboxs += " id='" + key + "'";
						checkboxs += " value='" + val + "' />";
						checkboxs += key + "<br/>";					
					});
					if(checkboxs == ""){
						checkboxs = "未找到该目录下的apk文件!";
					}
					$("#showHtmlFile").children().remove();
					$("#showHtmlFile").html("");
					$("#showHtmlFile").append(checkboxs);
					$("#showHtmlFileWindow").window("open");
				},
				error : function(error){
					$.messager.alert("出现未知异常!",error);
				}
			});
		});
		
		$("#topAddForm").validate({
			rules: {
				"categoryId": {
	                 required: true
	             },
	             "raveId": {
	                 required: true
	             },
	             "htmlFile":{
	            	 required: true
	             },
	             "ftphtmlFile":{
	            	 required: true
	             }
			},
			messages: {
				"categoryId": {
					required: "app类别不能为空"
	           
				},
				"raveId": {
					required: "请选择国家"
	           
				},
				 "htmlFile":{
	            	 required: "上传文件不能为空"   
	            },
				 "ftphtmlFile":{
	            	 required: "ftp上传路径不能为空!"   
	            }
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red');
			},
			submitHandler: function(form) {
				var topResult = $("#topResult");
				var uploadType = $(":input[type='radio'][name='uploadType']:checked").val();

				topResult.html("批量分发处理中,请稍后...");
				$("#butsubmit_id").attr("disabled","true");
				setDisabled('input[type="submit"]', window.document);
				$(form).ajaxSubmit({
					type : "POST",
					dataType :"json",
					 success: function(response){
						 var htmlstr = "处理结果：";						 					
						 var res ="";
						 if(response!=null){
							 if(uploadType=="2"){
					        	 res = eval("("+response+")"); 
							 }else{
								 res = response; 
							 }
							 if(res.flag==1){
								 htmlstr+="很抱歉，系统出现无法预知的异常或者网络连接异常，请联系管理员！！！";	
			            	}else if(res.flag==2){
								 htmlstr+="上传html文件失败，请重新上传！！！";	
			            	}else if(res.flag==3){
								 htmlstr+="上传html文件类型不正确，请重新上传！！！";	
			            	}else if(res.flag==4){
								 htmlstr+="ftp上传失败，请重新上传！！！";	
			            	}else if(res.flag==5){
			            		var data = res.result;
		            			var array;
		            			 if(uploadType=="2"){
		            				 array= data.split("<br/>");
								 }else{
									 array = data.split("&lt;br/&gt;");
								 }
		            			for( var i=0;i<array.length;i++){		            				
		            				htmlstr+="<font color='red'>"+array[i]+"</font><br/>";
		            			}
			            	}else if(res.flag==6){
								 htmlstr+="请重新登录！！！";	
			            	}else if(res.flag==0){
			            			var data = res.result;
			            			var array;
			            			 if(uploadType=="2"){
			            				 array= data.split("<br/>");
									 }else{
										 array = data.split("&lt;br/&gt;");
									 }
			            			 for( var i=0;i<array.length;i++){			            				
						            	 htmlstr+=array[i];				            				
				            		 }
			            	 	 htmlstr += "<a target='new' href='${path}appannie/appannie.txt'>点击查看详情</a>";
			            		}
						 }else{
							 alert("获取返回值为空,请查看您上传的文件,如没有问题请联系管理员");
						 }
					
						  $("#topResult").html(htmlstr);
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
