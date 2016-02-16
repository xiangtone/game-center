<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'ourPartners';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 修改合作伙伴
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改合作伙伴</div>
			
			<form id="editForm" name="editForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td class="name">合作伙伴中文名</td>
							<td class="content">
								<input type="text" name="nameCh" id="nameCh" maxlength="20" value="${ourPartners.nameCh }">
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_nameCh"></span>
							</td>
						</tr>
						<tr>
							<td class="name">合作伙伴英文名</td>
							<td class="content">
								<input type="text" name="nameEn" id="nameEn" maxlength="20" value="${ourPartners.nameEn }">
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_nameEn"></span>
							</td>
						</tr>
						<tr>
						<td class="name">链接地址</td>
							<td class="content">
								http://<input type="text" id="url" name="url"  maxlength="200" value="${ourPartners.url }" class="p">
								<span class="red">*(不能包含http://)</span>
							</td>
							<td class="content_info">
								<span id="span_url"></span>
							</td>
						</tr>
						<tr>
							<td class="name">图标</td>
							<td>
								<input type="file" class="text" id="ico" name="ico" maxlength='80'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_ico"></span>
							</td>
						</tr>
						<tr>
							<td class="name">图标地址</td>
							<td >
								<input type="text" class="text" id="logo" name="logo" maxlength='80' value="${ourPartners.logo }" readonly="readonly"/>
							</td>
						</tr>
						<tr>
							<td class="name">操作人</td>
							<td class="content" colspan="2">
								<input type="text" class="text" id="operator" name="operator" maxlength='10' value="${ourPartners.operator }" />
							</td>
						</tr>
						<tr>
							<td class="name">描述</td>
							<td class="content">
								<textarea rows="10" cols="50" id="remark" name="remark" >${ourPartners.remark }</textarea>
							</td>
							<td class="content_info">
								<span id="span_remark">200字符</span>
							</td>
						</tr>
						<tr>
							<td class="name">排序值</td>
							<td>
								<input type="text" id="sort" name="sort" maxlength="5" value="${ourPartners.sort }">
							</td>
							<td class="content_info">
								<span id="span_sort"></span>
							</td>
						</tr>
						<tr>
							<td class="name">是否有效</td>
							<td class="content" colspan="2">
								<input type="radio" id="state" name="state" value="1" <c:if test="${ourPartners.state==1 }"> checked="checked"</c:if>>是
								<input type="radio" id="state" name="state" value="0" <c:if test="${ourPartners.state==0 }"> checked="checked"</c:if>>否
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
				"nameCh": {
	                 required: true
	             },
	             "nameEn" :{
	            	 required: true
	             },
	             "url" :{
	            	 required: true
	             },
		          "remark" :{
	                 maxlength: 190
		          },
		            "sort" :{
		            	 required: true,
		            	 number: true
			      } 
		    },
			messages: {
				"nameCh": {
					required: "请输入合作伙伴中文名"
				},
				 "nameEn": {
					required: "请输入合作伙伴英语名"
				},
				"url": {
					required: "链接地址不能为空"
				},
				"remark" :{
					required: "描述不能超过200字符"
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
		        		if(response.flag==0){
	            			$.alert('更新合作伙伴成功', function(){window.location.href = "list";});
	            		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==3){
	            			$.alert('名称已经存在或数据出错,请重新输入');
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
	 $('.p').blur(function(){
		 if($("#url").val()==null || $("#url").val()=="" ||  $("#url").val().match(/http:\/\/+/)!=null){
			 alert("链接地址为空或包含http://");
			 return false;
		 }
			str ="http://"+editForm.url.value;
			str = str.match(/http:\/\/.+/); 
			if (str == null){
				alert('输入的链接地址无效'); 
				return false;
			}
 		}); 
	
	</script>
</body>
</html>