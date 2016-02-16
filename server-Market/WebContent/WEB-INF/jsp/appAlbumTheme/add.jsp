<%@page import="com.mas.rave.util.RandNum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appAlbumTheme';
		$(function(){
			$('#win').window({
				title: 'New Title',
				width: 400,
				modal: true,
				shadow: false,
				closed: true,
				height: 400,
				resizable:false,
				draggable:false 
				
			});
		});
		function resize(){
			$('#win').window("open");
		}
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 新增APP主题
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">添加APP主题</div>
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td class="name">所属分类</td>
							<td class="content" colspan="3">
								<select id="appAlbumId" name="appAlbumId" onchange="change(this.value)">
								<c:forEach var="appAlbum" items="${appAlbums}">
								<c:if test="${appAlbum.id<=3}">
									<option value="${appAlbum.id}" >${appAlbum.name}</option>
								</c:if>
								</c:forEach>
								</select>
							</td>
						</tr>
							<tr>
						<td class="name">所属专辑</td>
							<td class="content" colspan="3">
								<select name="appAlbumColumnId" id="appAlbumColumnId">
								</select>
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
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_nameCn"></span>
							</td>
						</tr>
						<tr>
							<td class="name">logo上传(big)</td>
							<td>
								<input type="file" class="text" id="bigApkFile" name="bigApkFile" maxlength='150'/>
								<span class="red">*</span>
							</td>
							<td class="content_bigApkFile">
								<span id="span_bigApkFile"></span>
							</td>
						</tr>
						<tr>
							<td class="name">logo上传(small)</td>
							<td colspan="2">
								<input type="file" class="text" id="apkFile" name="apkFile" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">说明</td>
							<td class="content">
								<textarea rows="10" cols="50" id="description" name="description" >No instructions</textarea>
								<span>不能超过500字符</span>
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
							<td class="content" colspan="2">
								<input type="radio" name="state" id="state" value="1" checked="checked"/>是
								<input type="radio" name="state" id="state" value="0" />否
								<input type="hidden" name="flag" id="flag" value="1" />
							</td>
						</tr>
						<tr>
							<td class="name">对应文件</td>
							<td class="content">
								<input type="text" id="appFileName" name="appFileName" >
								<span class="red">*</span>
								<input type="hidden" id="apkId" name="apkId" >
								<a href="javascript:void(0)" onclick="checkFile()">check file</a>
								<div id='win' style="overflow-y:scroll;">
									
								</div>
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
		change($("#appAlbumId").val());
		$.validator.addMethod("integer", function (value, element) {
            var regex = /^-?\d+$/;
            return  regex.test(value);
        }, "排序值只能输入整数");  
		$("#addForm").validate({
			rules: {
				"name": {
	                 required: true,
	                 maxlength: 100,
	             	 remote:{
	     		       url:"${ctx}/appAlbumTheme/judgeThemeNameExist",
	     		       type:"post",
	     		       dataType:"json",
	     		       data:{
	     		            name:function(){return $("#name").val();}
	     			       }
	     			      }
	             },
	             "nameCn":{
	            	 required: true,
                    maxlength: 100,
               	 remote:{
 	     		       url:"${ctx}/appAlbumTheme/judgeThemeNameCnExist",
 	     		       type:"post",
 	     		       dataType:"json",
 	     		       data:{
 	     		            nameCn:function(){return $("#nameCn").val();}
 	     			       }
 	     			      }
	             },
	             "bigApkFile" :{
	            	 required: true,
                     maxlength: 100
	             },
	             "apkId" :{
	            	 required: true
	             },
	             "sort" :{
	            	 required: true,
	            	 integer: true
	             },
	             "description":{
                     maxlength: 500
	             }
			},
			messages: {
				"name": {
					required: "请输入名称",
	                maxlength: "名称最长不得超过100个字符",
	                remote:"主题名称已经存在"
				},
				 "nameCn":{
	            	 required: "中文名不能为空",
	            	 maxlength: "名称最长不得超过200个字符",
	            	 remote:"主题中文名称已经存在"
	             },
				"bigApkFile": {
					required: "请至少输入大图标",
	                maxlength: "大图标最长不得超过200个字符"
				},
				"apkId" :{
					required: "请至少选择一个文件"
				},
				"sort" :{
					required:"排序值不能为空"
	             },
	             "description" :{
	            	 maxlength: "名称最长不得超过500个字符",
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
	            			$.alert('添加应用成功', function(){window.location.href = "list";});
		        		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
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
	function change(val){
		$.ajax({
			url : "${ctx}/appAlbumTheme/getColumn",
			type : "POST",
			dataType : "json",
			data :{"id":val},
			success: function(response){
        		var jsonstr = eval("(" + response + ")");
        		var ss = jsonstr.success.split(",");
        		$("#appAlbumColumnId>option").remove();
        		for(var i = 1;i < ss.length; i++){
        			var va = ss[i].split("-");
        			document.getElementById("appAlbumColumnId").add(new Option(va[0],va[1]));
        		}
            }
        });
		function checkId(){
			var item = $("[name=apkId]");
			var id = "";
			if (item.size() > 0) {
				item.each(function() {
							if ($(this).attr("checked") == true) {
								id += $(this).val();
							}
						});
			}
			id = id.substr(0, id.lastIndexOf(","));
			if (id == "") {
				$.alert("请选择记录");
				return '';
			} else {
				return id;
			}
		}
	}
	
	function checkFile(){
		var value = $("#appFileName").val();
		var raveId = $("#raveId").val();
		if(value.length<3){
			alert('长度不能少于三个字符');
			return ;
		}
		if(value != ""){
			$.ajax({
				url : "${ctx}/appAlbumTheme/checkFile",
				type : "POST",
				dataType : "json",
				data : {"appFileName" : value,"raveId" : raveId},
				success : function (data){
					var result = eval("(" + data + ")");
					if(result.success == 1){
						$("#win").html('');
						$("#win").append(result.option);
						$("#win").window("open");
						$.parser.parse();
						$(function(){
							$("input[name='apkId'][type=radio]").click(function(){
								var app = $(this).val();
								var app1 = app.split(",");
								$("#appFileName").val(app1[1]);
								$("#apkId").val(app1[0]);
								$("#win").window("close");
							});
						});
					}else{
						alert("目前无此数据,请重新输入");
					}
				},
				error : function (error){
					$.messager.alert("提示:","异常出现未知异常!");
				}
			});
		}else{
			alert("数据为空,请重新输入");
		}
	}
</script>