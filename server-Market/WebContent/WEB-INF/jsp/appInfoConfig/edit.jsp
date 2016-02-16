<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'appInfoConfig';
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
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 编辑app配置
	</div>

	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">编辑app配置</div>

			<form id="editForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">应用名</td>
							<td class="content">
								<input type="text" class="text" value="${fn:escapeXml(appConfig.name) }" id="name" name="name" width="200" maxlength='100'/>
								<span class="red">*</span>
								<a href="javascript:void(0)" onclick="checkFile()">check file</a>
								<div id='win' style="overflow-y:scroll;">
									
								</div>
							</td>
							<td class="content_info">
								<span id="span_name"></span>
							</td>
						</tr>
						<tr>
							<td class="name">包名</td>
							<td class="content">
								<input type="text" class="text" value="${fn:escapeXml(appConfig.packageName) }" name="packageName" id="packageName"  width="200" maxlength='100'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_packageName"></span>
							</td>
						</tr>
						<tr>
							<td class="name">类型</td>
							<td class="content">
								<input type="radio" id="type" name="type" value="3" <c:if test="${appConfig.type == 3 }">checked="checked"</c:if>>自动更新
								<input type="radio" id="type" name="type" value="2" <c:if test="${appConfig.type == 2 }">checked="checked"</c:if>>需要增量
								<input type="radio" id="type" name="type" value="0" <c:if test="${appConfig.type == 0 }">checked="checked"</c:if>>黑名单
								<input type="radio" id="type" name="type" value="1" <c:if test="${appConfig.type == 1 }">checked="checked"</c:if>>忽略
								<!-- 
								<input type="radio" id="type" name="type" value="5" <c:if test="${appConfig.type == 5 }">checked="checked"</c:if>>Top500Home
								 -->
							</td>
							<td class="content_info">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="name">描述</td>
							<td class="content">
								<textarea rows="10" cols="20" name="description" id="description">${fn:escapeXml(appConfig.description) }</textarea>
							</td>
							<td class="content_info">
								<span id="span_description"></span>
							</td>
						</tr>
						<tr>
							<td class="name" >更新状态</td>
							<td class="content">
								<input type="radio" id="state" name="state" value="false" <c:if test="${appConfig.state==false }">checked="checked"</c:if> >历史数据
								<input type="radio" id="state" name="state" value="true" <c:if test="${appConfig.state==true }">checked="checked"</c:if>>最新添加
							</td>
							<td class="content_info">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="name">应用链接地址</td>
							<td >
								<textarea rows="1" cols="100" id="appUrl" name="appUrl">${fn:escapeXml(appConfig.appUrl) }</textarea>
							</td>
							<td class="content_info">
								<span id="span_appUrl"></span>
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn"><input type="submit" class="bigbutsubmit"
								value="提交" id="butsubmit_id" /> <input type="button"
								class="bigbutsubmit"
								onclick="javascript:window.location.href='list';" value="返回" />
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
				"name": {
	               required: true,
	               maxlength: 50
	           },
				"packageName": {
			       required: true,
			       maxlength: 100
			   },	
	           "description": {
	               maxlength: 200
	           },
	           "appUrl": {
                   maxlength: 500
         		}  
			},
			messages: {
				"name": {
					required: "应用名不能为空",
	                maxlength: "应用名最长不得超过50字符"
				},
				"description": {
	                maxlength: "描述最长不得超过200字符"
				},
				"packageName": {
					required: "包名不能为空",
	                maxlength: "包名最长不得超过100字符"
				},
				"appUrl": {
	                maxlength: "应用链接地址最长不得超过500字符"
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
	            			$.alert('更新成功', function(){window.location.href = "list";});
	            		}else if(data.flag==2){
	            			$.alert('该配置已经存在请重新输入');
	            			$("#butsubmit_id").attr("disabled","false");;
	            		}else if(data.flag==3){
	            			$.alert('文件名不存在，请checkfile后再提交');
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
		var value = $("#name").val();
		
		if(value != ""){
			if(value.length<3){
				alert("请至少输入三个或三个以上字符！！");
				return;
			}
			$.ajax({
				url : "${ctx}/appInfoConfig/checkFile",
				type : "POST",
				dataType : "json",
				data : {"name" : value},
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
								$("#name").val(app1[1]);
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
</body>
</html>