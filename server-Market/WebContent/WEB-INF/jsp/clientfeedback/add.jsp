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
		当前位置: <a href="${ctx }/clientfeedback/list"><span id="childTitle"></span></a> -&gt; 回复管理
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside"  style="width:55%;margin: 0 auto;">
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx }/clientfeedback/${clientId}/add" params="${params}"/>
					</c:if>
				</div>
			</div>
			<div class="border">
				<table class="mainlist" width="100%">
				<tbody>
				<form id="addForm" action="${ctx }/clientfeedback/${clientId}/addContent" method="post" enctype="multipart/form-data">
				<tr>
						<td>
								<input type="hidden" name="clientId" value="${clientId}">	
								<input type="hidden" name="imei" value="${imei}">	
								<textarea rows="6" cols="60" id="content" name="content" ></textarea>							
								<span class="red">*</span>
								<span id="span_content"></span>
						</td>	
						<td>
							<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
						</td>
						<td>
							<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/clientfeedback/list';" value="返回"/>							
						</td>										
					</tr>
					</form>
					<c:if test="${result==null or result.recordCount == 0}">
						<tr>
							<td colspan="4" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<c:if test="${obj.feedbackType==1}">
						<td colspan="3" align="left">						
						  发表时间:<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
						  &nbsp;&nbsp;&nbsp;&nbsp;<a onclick="deleteFeedback(this.title)" href="#" title="${obj.id}">删除</a><br/><br/>
							<span>${fn:escapeXml(obj.content) }</span>
						</td>						
						</c:if>
						<c:if test="${obj.feedbackType==2}">
						<td colspan="3" align="right">						
						  发表时间:<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
						  &nbsp;&nbsp;&nbsp;&nbsp;<a onclick="deleteFeedback(this.title)" href="#" title="${obj.id}">删除</a><br/><br/>
								<span><font color="red">${fn:escapeXml(obj.content) }</font></span>
						</td>
						</c:if>							
						</tr>
					</c:forEach>
				</tbody>
				<tfoot></tfoot>
				</table>
				</div>
		</div>
	</div>
	
	<script type="text/javascript">
	$(document).ready(function(){
			$("#addForm").validate({
				rules: {
					"content": {
		               required: true,
		               maxlength: 2000
		            }   		                            
				},
				messages: {
					"content": {
						required: "请输入回复内容",
		                maxlength: "回复内容最长不得超过2000个字符"
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
			        			$.alert('添加成功', function(){window.location.href = "${ctx }/clientfeedback/${clientId}/add?imei=${imei}";});
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
 function deleteFeedback(title){
			$.alert("确定要删除？", function() {
				$.ajax({
					url:"${ctx}/clientfeedback/delete",
					type:"POST",
					data:{'id':title},
					dataType:"json",
					success:function(data){
						var flag = eval("("+data+")");
						if(flag.success == "true"){
							alert("删除成功！");
							//window.location.reload();
							window.location.href = "${ctx }/clientfeedback/${clientId}/add?imei=${imei}";
						}
					},
					error:function(data){
						alert("删除失败！");
						//window.location.reload();
						window.location.href = "${ctx }/clientfeedback/${clientId}/add?imei=${imei}";
					}
				});
			}, true);
	 }
	</script>
</body>
</html>