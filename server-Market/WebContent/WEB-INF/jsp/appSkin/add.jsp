<%@page import="com.mas.rave.util.RandNum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>zApp皮肤管理</title>
	<script type="text/javascript">
		var menu_flag = 'appSkin';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 增加zApp皮肤
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">添加zApp皮肤</div>
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td class="name">skinName应用名</td>
							<td class="content">
								<input type="text" class="text" id="skinName" name="skinName" maxlength='99'/><font color=red>&nbsp;*</font>
							</td>
							<td class="content_info">
								<span id="span_skinName"></span>
							</td>
						</tr>
						<tr>
							<td class="name">logo图</td>
							<td class="content">
								<input type="text" class="text" id="logoUrl"  name="logoUrl" readonly="readonly"/><font color=red>&nbsp;*</font>
							<div class="x_but1">
								<a id="logoFile" href="#" class="x_butsc">本地上传</a>
							</div>
							</td>
							<td class="content_info">
								<span id="span_logoUrl"></span>
							</td>
						</tr>
						<tr>
							<td class="name">描述</td>
							<td class="content">
								<textarea rows="10" cols="50" id="description" name="description" ></textarea>
							</td>
							<td class="content_info">
								<span id="span_description">500字符</span>
							</td>
						</tr>
						<tr>
							<td class="name">皮肤地址</td>
							<td class="content">
								<input type="text" class="text" id="apkFileUrl"  name="apkFileUrl" readonly="readonly"/><font color=red>&nbsp;*</font>
							<div class="x_but1">
								<a id="appFile" href="#" class="x_butsc">本地上传</a>
							</div>
							</td>
							<td class="content_info">
								<span id="span_apkFileUrl">后缀必须以.apk或.theme结尾</span>
							</td>
						</tr>
						<tr>
							<td class="name">zApp状态</td>
							<td class="content">
								<input type="radio" id="state" name="state" value="1" checked="checked">上线
								<input type="radio" id="state" name="state" value="0">下线
							</td>
							<td class="content_info">
								<span id="span_apkFileUrl"></span>
							</td>
						</tr>
						<tr>
							<td class="name">排序值</td>
							<td class="content">
								<input type="text" class="text" id="sort" name="sort" maxlength="9" value="0">
							</td>
							<td class="content_info">
								<span id="span_sort"></span>
							</td>
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
				"skinName": {
	                      required: true,
	                      maxlength: 100
	             },
	             "logoUrl" :{
	            	 required: true
	             },
	             "apkFileUrl" :{
	            	 required: true,
	             },
	             "description" :{
	            	 maxlength: 499
	             },
		            "sort" :{
		            	 required: true,
		            	 number: true
			      } 
		    },
			messages: {
				"skinName": {
					required: "请输入名称",
	                maxlength: "名称最长不得超过200个字符"
				},
				"logoUrl" :{
	            	 required: "logo图不能为空"
	             },
	             "apkFileUrl" :{
	            	 required: "皮肤地址不能为空"
	             },
	             "description" :{
	            	 maxlength: "描述不能超过500字符"
	             },
				 "sort" :{
						required: "请输入排序值",
						number: "必须为整数"
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
						 var data = eval("("+response+")");
		        		if(data.flag==0){
	            			$.alert('添加应用成功', function(){window.location.href = "list";});
	            		}else if(data.flag=1){
	            			$.alert('图片或文件不能为空,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(data.flag==2){
	            			$.alert('logo或文件不合法,请重新输入');
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
	
	$(document).ready(function(){
	     var button1 = $('#appFile'), interval1;
	     new AjaxUpload(button1, {
	    	 action: '${ctx }/appSkin/uploadFile',
				name:'file',
				//选择后自动开始上传
				autoSubmit:true,
				//返回Text格式数据
				responseType: 'json',
				data : {"flag" : 1},
				//上传的时候按钮不可用
				onSubmit : function(file,ext){
				   //设置允许上传的文件格式
				   if (!(ext && /^(apk|theme)$/.test(ext))){
					   $.messager.alert("错误提示","文件式不正确");
				   	 return false;
				    }
				  ajaxLoading();
				   },
				//上传完成后取得文件名filename为本地取得的文件名，msg为服务器返回的信息
			 onComplete:function (file, response) {  
				 ajaxLoadEnd();
				 if (response.flag==1) {  
					 if(response.pac==null){
							$("#apkFileUrl").val(response.msg);
						}else{
							$("#apkFileUrl").val("");
							 $.messager.alert("错误提示","文件已经存在");
						}
					} else {  
						 $.messager.alert("错误提示","文件式不正确");
					}  
			 }}); 
	     
	     var button2 = $('#logoFile'), interval2;
	     new AjaxUpload(button2, {
	    	 action: '${ctx }/appSkin/uploadFile',
				name:'file',
				//选择后自动开始上传
				autoSubmit:true,
				//返回Text格式数据
				responseType: 'json',
				data : {"flag" : 2},
				//上传的时候按钮不可用
				onSubmit : function(file,ext){
				   //设置允许上传的文件格式
				   if (!(ext && /^(jpg|gif|png|bmp|jpeg|JPG|GIF|PNG|BMP|JPEG)$/.test(ext))){
					   $.messager.alert("错误提示","logo格式不正确");
				   	return false;
				    }
				   ajaxLoading();
				   },
				//上传完成后取得文件名filename为本地取得的文件名，msg为服务器返回的信息
			 onComplete:function (file, response) {  
				 ajaxLoadEnd();
				 if (response.flag==1) {  
						//设置进度条
						$("#logoUrl").val(response.msg);
					} else {  
						 $.messager.alert("错误提示","logo格式不正确");
					}  
			 }}); 
	});
	
	function ajaxLoading(){
	    $("<div class=\"datagrid-mask\" style=\"z-index:10000\"></div>").css({display:"block",width:"100%",height:"100%"}).appendTo("body");
	    $("<div class=\"datagrid-mask-msg\" style=\"z-index:10000\"></div>").html("正在上传,清稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
	 }
	 function ajaxLoadEnd(){
	     $(".datagrid-mask").remove();
	     $(".datagrid-mask-msg").remove();            
	} 
	</script>
</body>
</html>