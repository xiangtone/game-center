<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'market';
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 修改平台信息
	</div>
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改平台信息</div>
		<form id="editForm" action="" method="post" enctype="multipart/form-data">
		<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td class="name">所属分类</td>
							<td class="content" colspan="2">
								<input type="text" class="text" id="categoryId" name="categoryId" value="${fn:escapeXml(market.category.id)}" maxlength='100' readonly="readonly"/>
							</td>
						</tr>
						<tr>
						<td class="name">所属渠道</td>
							<td class="content" colspan="2">
								<input type="hidden" id="channelId" name="channelId" value="100000">zapp发布
							</td>
						</tr>
						<tr>
							<td class="name"> 应用名称</td>
							<td class="content">
							    <input type="hidden" name="id" id="id" value="${market.id }"/>
								<input type="text" class="text" id="name" name="name"  value="${fn:escapeXml(market.name) }" maxlength='100'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span>
							</td>
						</tr>
						<tr>
							<td class="name">别名</td>
							<td class="content">
								<textarea rows="10" cols="50" id="anotherName" name="anotherName" >${fn:escapeXml(market.anotherName)}</textarea>
							</td>
							<td class="content_info">
							<span id="span_anotherName"></span>							
							</td>
						</tr>						
						<tr>
							<td class="name">cpid</td>
							<td class="content" colspan="2">
								<input type="text" class="text" id="cpId" name="cpId" value="${fn:escapeXml(market.cp.id)}" maxlength='100' readonly="readonly"/>
							</td>
						</tr>
						
						<tr>
							<td class="name">密码</td>
							<td class="content" colspan="2">
								<input type="text" class="text" id="pwd" name="pwd" value="${fn:escapeXml(market.pwd)}" maxlength='100'/>
								<input type="hidden" name="free" id="free" value="1"/>
							</td>
						</tr>
						<tr>
							<td class="name">logo图标地址(size)</td>
							<td colspan="2">
								<textarea rows="1" cols="100" id="bigLogo" name="bigLogo" readonly="readonly">${fn:escapeXml(market.bigLogo)}</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">logo图标上传(size)</td>
							<td colspan="2">
								<input type="file" class="text" id="bigApkFile" name="bigApkFile" maxlength='150'>
							</td>
						</tr>
							<tr>
							<td class="name">logo路径(small)</td>
							<td colspan="2">
								<textarea rows="1" cols="100" id="logo" name="logo" readonly="readonly">${fn:escapeXml(market.logo)}</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">logo图上传(small)</td>
							<td colspan="2">
								<input type="file" class="text" id="apkFile" name="apkFile" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">状态</td>
							<td class="content" colspan="2">
							<c:choose>
							       <c:when test="${market.state==true}">
									    <input type="radio" name="state" id="state" value="true"  checked="checked"/>是
										<input type="radio" name="state" id="state" value="false"/>否
										
									</c:when>
									  <c:when test="${market.state==false}">
										<input type="radio" name="state" id="state" value="true"/>是
										<input type="radio" name="state" id="state" value="false"  checked="checked"/>否
									</c:when>
							</c:choose>
							</td>
						</tr>
						<tr>
							<td class="name">星级</td>
							<td class="content" colspan="2">
								<select name="stars">
									<option value="1" <c:if test="${market.stars==1}"> selected="selected" </c:if> >1</option>
									<option value="2" <c:if test="${market.stars==2}"> selected="selected" </c:if>>2</option>
									<option value="3" <c:if test="${market.stars==3}"> selected="selected" </c:if>>3</option>
									<option value="4" <c:if test="${market.stars==4}"> selected="selected" </c:if>>4</option>
									<option value="5" <c:if test="${market.stars==5}"> selected="selected" </c:if>>5</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="name">app简介</td>
							<td class="content">
								<textarea rows="10" cols="50" id="brief" name="brief" >${fn:escapeXml(market.brief)}</textarea>
							</td>
							<td class="content_brief">
								<span id="span_brief">200字符</span>
							</td>
						</tr>
						<tr>
							<td class="name">app说明</td>
							<td>
								<textarea rows="15" cols="100" id="description" name="description">${fn:escapeXml(market.description)}</textarea>
							</td>
							<td class="content_description">
								<span id="span_description"></span>
							</td>
						</tr>
						<tr>
							<td class="name">下载数</td>
							<td class="content">
								<input type="text" class="text" id="initDowdload" name="initDowdload" maxlength='10' value="${fn:escapeXml(market.initDowdload)}" />
							</td>
							<td class="content_info">
								<span id="span_initDowdload"></span>
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
				"name": {
                    required: true,
                    maxlength: 100
          	 	},
	           "channelId" :{
	          		required: true
	           },
	           "cpId" :{
	          		required: true
	           },
	           "brief":{
	        	    required: true,
                    maxlength: 200
	            },
	           "description" :{
                    maxlength: 5000
	            },
	 	        "initDowdload" :{
		        	number: true,
			        digits:true,
			        maxlength: 9
		        },
		        "anotherName" :{
		            maxlength: 2500
			    }
		},
		messages: {
			"name": {
				required: "请输入名称",
              	maxlength: "名称最长不得超过200个字符"
			},
			"channelId" :{
				required: "请至少选择一个渠道"
			},
			"cpId" :{
				required: "请至少选择一个cp"
			},
			"brief" :{
				required: "请输入简介",
				maxlength: "简介最长不得超过200个字符"
			},
			"description": {
	            maxlength: "描述最长不得超过5000个字符"
		    },
	         "initDowdload" :{
	        	 number: '不能为字符',
	        	 digits:"必须为正整数或0",
	             maxlength: "初始下载量最大长度不能超过9位"
	         },
		     "anotherName" :{
				 maxlength: "别名最长不得超过2500个字符"
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
	            			$.alert('更新应用成功', function(){window.location.href = "list";});
	            		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==3){
	                		$.alert('名称已经存在,请重新输入');
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