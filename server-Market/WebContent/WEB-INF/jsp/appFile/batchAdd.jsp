<%@page import="com.mas.rave.util.RandNum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appBatchUpload';
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置:<span id="childTitle"></span> 
	</div>
	<div class="mainoutside mt18">
		<div id="secmainoutside">
		<div class="border">
			<table align="center" width="100%">
				<tr>
					<td class="name"><div class="z_title2">批量导入APK</div></td>
				</tr>
				<tr>
					<td class="name">&nbsp;</td>
				</tr>
				<tr>
					<td>
					<label  style="margin-left: 10px;">请输入apk文件路径 : </label>
					<input type="text" class="text" id="apkPath" name="apkPath"style="width: 350px; border:1px solid #ccc;" maxlength='300'/>
					<span class="red">(如果没有指定目录,系统将以默认目录方式批量上传!)</span>
					</td>
				</tr>
				<tr>
					<td class="name">&nbsp;</td>
				</tr>
				<tr>
					<td class="name" ><input type="button" id="batchAppUpload" class="bigbutsubmit" value="批量导入" /></td>
				</tr>
				<tr>
					<td class="name" ><div id="uploadResult" style="padding-top: 20px"></div></td>
				</tr>
			</table>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$("#batchAppUpload").click(function(){
			var uploadResult = $("#uploadResult");
			var apkPath = $("#apkPath").val();
			//uploadResult.show();
			uploadResult.html("批量导入处理中,请稍后...");
			$.ajax({
			  type: "POST",
			  url: "${ctx}/appFile/batchAdd",
			  data : {"apkPath" : apkPath},
			  success:function(data){
				  var htmlstr = "处理结果：<br/><br/>";
				  for(var i=0;i < data.length;i++){
					  htmlstr += data[i] + "<br/>";
				  }
				  $("#uploadResult").html(htmlstr);
			  },
			  error:function(errmsg){
				  //alert("errmsg:" + errmsg);
				  uploadResult.html(errmsg + "");
			  }
			}); 
		});
		
		function showtr(obj) {
			if(obj == 1){
				$("#path").attr("disabled",true);
			}else{
				$("#path").removeAttr("disabled");
			}
		}
		
	</script>
</body>
</html>