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
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 修改APP主题信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改APP主题信息</div>
			
			<form id="editForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>						
						<tr>
						<td class="name">所属分类</td>
							<td class="content" colspan="2">
								<select id="appAlbumId" name="appAlbumId" onchange="change(this.value)">
								<c:forEach var="appAlbum" items="${appAlbums}">
								<c:if test="${appAlbum.id<=3}">
									<option value="${appAlbum.id}"  <c:if test="${appAlbum.id==appAlbumTheme.appAlbumColumn.appAlbum.id }">selected="selected"</c:if> >${appAlbum.name}</option>
								</c:if>
								</c:forEach>
								</select>
								<span class="red">*</span>
							</td>
						</tr>
						<tr>
							<td class="name">所属专辑分类</td>
							<td class="content" colspan="2">
								<select name="appAlbumColumnId" id="appAlbumColumnId">
								<c:forEach var="appAlbumColumn" items="${appAlbumColumns}">
									<option value="${appAlbumColumn.columnId}" <c:if test="${appAlbumTheme.appAlbumColumn.columnId==appAlbumColumn.columnId }">selected="selected"</c:if>>${appAlbumColumn.name}</option>
								</c:forEach>
								</select>
								<span class="red">*</span>
							</td>
						</tr>
						<tr>
						<td class="name">所属国家</td>
							<td class="content">
							<label>${appAlbumTheme.country.name}(${appAlbumTheme.country.nameCn})</label>
							</td>
							<td class="content_info">
								<span id="span_raveId"></span>
							</td>
						</tr>
						<tr>
							<td class="name">标题(英文)</td>
							<td class="content">
								<input type="text" class="text" id="name" name="name" value="${fn:escapeXml(appAlbumTheme.name) }" maxlength='100'/>
								<input type="hidden" id="themeId" name="themeId" value="${appAlbumTheme.themeId }">
								<input type="hidden" id="icon" name="icon" value="${appAlbumTheme.icon }">
								<input type="hidden" id="flag" name="flag" value="${appAlbumTheme.flag }">
								<input type="hidden" id="raveId" name="raveId" value="${appAlbumTheme.country.id}">								
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span>
							</td>
						</tr>
						<tr>
							<td class="name">标题(中文)</td>
							<td class="content">
								<input type="text" class="text" id="nameCn" name="nameCn" value="${fn:escapeXml(appAlbumTheme.nameCn) }" maxlength='100'/>
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
							<td class="content_bigApkFile" colspan="2">
								<span id="span_bigApkFile"></span>
							</td>
						</tr>
						<tr>
							<td class="name">logo地址(big)</td>
							<td colspan="2">
								<textarea rows="1" cols="100" id="bigicon" name="bigicon" readonly="readonly">${fn:escapeXml(appAlbumTheme.bigicon) }</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">logo上传(small)</td>
							<td>
								<input type="file" class="text" id="apkFile" name="apkFile" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">logo地址(small)</td>
							<td colspan="2">
								<textarea rows="1" cols="100" id="logo" name="logo" readonly="readonly">${fn:escapeXml(appAlbumTheme.icon) }</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">说明</td>
							<td class="content">
								<textarea rows="10" cols="50" id="description" name="description" >${fn:escapeXml(appAlbumTheme.description) }</textarea>
								<span>不能超过500字符</span>
							</td>
							<td class="content_description">
								<span id="span_description"></span>
							</td>
						</tr>
						<tr>
							<td class="name">排序值</td>
							<td class="content">
								<input type="text" class="text" id="sort" name="sort" maxlength='9' value="${fn:escapeXml(appAlbumTheme.sort)}"/>
							</td>
							<td class="content_info">
								<span id="span_sort"></span>
							</td>
						</tr>
						<tr>
							<td class="name">是否有效</td>
							<td class="content" colspan="2">
								<input type="radio" name="state" id="state" value="1" <c:if test="${appAlbumTheme.state==true }">checked="checked"</c:if> />是
								<input type="radio" name="state" id="state" value="0" <c:if test="${appAlbumTheme.state==false }">checked="checked"</c:if> />否
							</td>
						</tr>
						<tr>
							<td class="name">对应文件</td>
							<td class="content" colspan="2">
								<input type="text" id="appFileName" name="appFileName" value="${fn:escapeXml(appAlbumTheme.appFile.appName)}">
								<span class="red">*</span>
								<input type="hidden" id="apkId" name="apkId"  value="${appAlbumTheme.appFile.id }">
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
		$.validator.addMethod("integer", function (value, element) {
            var regex = /^-?\d+$/;
            return  regex.test(value);
        }, "排序值只能输入整数");  
		$("#editForm").validate({
			rules: {
				"name": {
	                 required: true,
	                 maxlength: 100,
	             	 remote:{
	     		       url:"${ctx}/appAlbumTheme/judgeThemeNameExist",
	     		       type:"post",
	     		       dataType:"json",
	     		       data:{
	     		    	    themeId:"${appAlbumTheme.themeId}",
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
	     		    	    themeId:"${appAlbumTheme.themeId}",
	     		            nameCn:function(){return $("#nameCn").val();}
	     			       }
	     			      }
	             },
	             "apkId" :{
	            	 required: true
	             },
	             "sort" :{
	            	 required: true,
	            	 integer:true
	             },
	             "description":{
                     maxlength: 500
	             }
			},
			messages: {
				"name": {
					required: "请输入名称",
	                maxlength: "名称最长不得超过200个字符",
	                remote:"主题名称已经存在"
				},
				 "nameCn":{
	            	 required: "中文名不能为空",
	            	 maxlength: "名称最长不得超过200个字符",
	            	 remote:"主题中文名称已经存在"
	             },
				"apkId" :{
					required: "请至少选择一个文件"
				},
				"sort" :{
	            	 required: "排序值不能为空"
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
	            			$.alert('更新应用成功', function(){window.location.href = "list?currentPage=${page}&raveId=${raveId}&appAlbumId=${appAlbumId}&appAlbumColumnId=${appAlbumColumnId}";});
		        		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
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
								$("#apkId").val(app1[0])
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
	</script>
</body>
</html>