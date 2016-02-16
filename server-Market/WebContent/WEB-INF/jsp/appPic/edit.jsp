<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>APP截图信息</title>
	<script type="text/javascript">
	var menu_flag = '${sessionScope.menuFlag}';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="${ctx }/app/add/2/${appPic.appInfo.id}">APK截图信息</a> -&gt;修改APP截图信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改APP截图信息</div>
			
			<form id="editForm" action="${ctx }/appPic/update/${appPic.appInfo.id}" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">应用名(id)</td>
							<td class="content" colspan="2">
								${appPic.appInfo.name}(${appPic.appInfo.id })
							</td>
						</tr>
						<tr>
							<td class="name">标题</td>
							<td class="content">
							    <input type="hidden" name="id" id="id" value="${appPic.id }"/>
								<input type="text" class="text" id="title" name="title"  value="${fn:escapeXml(appPic.title) }" maxlength='100'/>
								<input type="hidden" id="sort" name="sort" value="${fn:escapeXml(appPic.sort)}">
							</td>
							<td class="content_info">
								<span id="span_title"></span>
							</td>
						</tr>
						<tr>
							<td class="name">描述</td>
							<td class="content">
								<input type="text" class="text" id="description" name="description" value="${fn:escapeXml(appPic.description)}" maxlength='100'/>
							</td>
							<td class="content_info">
								<span id="span_description"></span>
							</td>
						</tr>
						<tr>
							<td class="name">截图路径</td>
							<td colspan="2">
								<textarea rows="1" cols="100" id="url" name="url" readonly="readonly">${fn:escapeXml(appPic.url)}</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">截图上传</td>
							<td colspan="2">
								<input type="file" class="text" id="appPicFile" name="appPicFile" maxlength='150'/>
								<span class="red">*</span>
							</td>
						</tr>
						<tr>
							<td class="name">状态</td>
							<td class="content" colspan="2">
								<input type="radio" name="state" id="state" value="1" <c:if test="${appPic.state==true}">checked="checked"</c:if> />是
								<input type="radio" name="state" id="state" value="0" <c:if test="${appPic.state==false}">checked="checked"</c:if> />否
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx }/appPic/list/${appPic.appInfo.id}';" value="返回"/>
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
				"title": {
	                 required: true,
	                 maxlength: 100
	             },
	           	 "description": {
	                 maxlength: 500
	             }	                            
			},
			messages: {
				"title": {
					required: "请输入标题",
	                maxlength: "名称最长不得超过100个字符"
				},
				"description": {
	                maxlength: "描述最长不得超过500个字符"
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
	            			$.alert('更新信息成功', function(){window.location.href = "${ctx }/appPic/list/${appPic.appInfo.id}?src=${src}";});
	            		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else{
	                		$.alert('更新失败,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}
		        		setabled('input[type="submit"]', window.document);
		            },
		            error: function(response){
		            	alert('load error');
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