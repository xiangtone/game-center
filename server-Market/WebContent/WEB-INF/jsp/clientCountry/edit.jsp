<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'clientCountry';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 修改国家映射表信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改国家映射表信息</div>
			
			<form id="editForm" action="${ctx }/clientCountry/update" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td class="name">国家中文名 </td>
							<td class="content">
								<input type="text" name="countryCn" id="countryCn" class="text"  value="${fn:escapeXml(country.countryCn)}" onkeyup="value=value.replace(/[^\a-zA-Z\u4E00-\u9FA5]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\a-zA-Z\u4E00-\u9FA5]/g,''))">&nbsp;<font color=red>*&nbsp;&nbsp;只能输入中文和字母</font>
								<input type="hidden" name="countryCnStr" id="countryCnStr" class="text"  value="${fn:escapeXml(country.countryCn)}">
							</td>
							<td class="content_info">
								<span id="span_countryCn"></span>
							</td>
						</tr>
						<tr>
							<td class="name">国家英文名</td>
							<td class="content">
								<input type="text" name="country" id="country" class="text" value="${fn:escapeXml(country.country)}" onkeyup="value=value.replace(/[^\a-zA-Z ''']/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\a-zA-Z '']/g,''))"/>&nbsp;<font color=red>*&nbsp;&nbsp;只能输入字母</font>
								<input type="hidden" name="countryStr" id="countryStr" class="text" value="${fn:escapeXml(country.country)}">
							</td>
							<td class="content_info">
								<span id="span_country"></span>
							</td>
						</tr>
						<tr>
							<td class="name">ico图标地址</td>
							<td colspan="2">
								<textarea rows="1" cols="100" id="iconUrl" name="iconUrl" readonly="readonly">${fn:escapeXml(country.iconUrl)}</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">ico图标上传</td>
							<td colspan="2">
								<input type="file" class="text" id="icoFile" name="icoFile" maxlength='150'>
								<input type="hidden" id="currentPage" name="currentPage" value="${currentPage }">
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
		$("#editForm").validate({
			rules: {
				"countryCn" :{
					 required: true
				},
				"country": {
                   required: true
         	 	}
		},
		messages: {
			"countryCn" :{
				 required: "中文名不能为空"
			},
			"country": {
				required: "英文名不能为空"
			}
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			},
			submitHandler: function(form) {
				setDisabled('input[type="submit"]', window.document);
				$(form).ajaxSubmit({
					type : "POST",
					dataType : "json",
					 success: function(response){
		        		if(response.flag==0){
	            			$.alert('更新成功', function(){window.location.href = "list?currentPage=${page}";});
	            		}else if(response.flag==2){
	            			$.alert('ico不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==3){
	            			$.alert('中文名已经存在!');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==4){
	            			$.alert('英文名已经存在!');
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