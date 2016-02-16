<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'clientfeedback';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置:  <a href="../list"><span id="childTitle"></span></a>-&gt; <a href="list">常用回馈管理</a> -&gt; 新增常用回馈
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">添加常用回馈</div>			
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<input type="hidden" id="id" value="${clientFeedbackZapp.id}">
			<div class="border">
				<table class="mainadd">
					<tbody>					
						<tr>
							<td class="name">提问</td>
							<td>
								<textarea rows="10" cols="100" id="question" name="question" >${fn:escapeXml(clientFeedbackZapp.question) }</textarea>							
							    <span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_question"></span>							
							</td>
						</tr>	
						<tr>
							<td class="name">回答内容</td>
							<td>
								<textarea rows="10" cols="100" id="replyContent" name="replyContent" >${fn:escapeXml(clientFeedbackZapp.replyContent) }</textarea>							
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_replyContent"></span>
							</td>
						</tr>
						<tr>
							<td class="name">是否有效</td>
							<td class="content">
								<input type="radio" name="state" id="state" value="1" <c:if test="${clientFeedbackZapp.state==true}">checked="checked"</c:if>/>是
								<input type="radio" name="state" id="state" value="0" <c:if test="${clientFeedbackZapp.state==false}">checked="checked"</c:if>/>否
							</td>
							<td class="content">&nbsp;</td>
						</tr>	
						<tr>
						<td class="name">创建时间 : </td>
						<td>
							<input class="Wdate" id="createTime1"  name="createTime1" style="width: 160px"
              	 			 onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'});" 
              	 			 value="<fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' value='${clientFeedbackZapp.createTime}'/>"/>
						</td>
						<td class="content">&nbsp;</td>
						</tr>			
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx }/clientfeedback/zapp/list';" value="返回"/>
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
				"question": {
	                required: true,
	                maxlength: 2000
	                },   
				"replyContent": {
		            required: true,
		            maxlength: 2000
		        }	                            
			},
			messages: {
			"question": {
				required: "请输入提问",
	            maxlength: "提问最长不得超过2000个字符"
			},
			"replyContent": {
				required: "请输入回答内容",
                maxlength: "回答内容最长不得超过2000个字符"
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
						if(response!=null){
							var data = eval("("+response+")");
			        		if(data.flag==0){
			        			$.alert('修改成功', function(){window.location.href = "${ctx }/clientfeedback/zapp/list";});
		            		}else{
		            			$.alert('修改失败,请重新输入');
		                		$("#butsubmit_id").attr("disabled","false");
		            		}
			        		setabled('input[type="submit"]', window.document);
						}else{
							$.alert('添加失败,请重新输入');
							setabled('input[type="submit"]', window.document);
						}
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