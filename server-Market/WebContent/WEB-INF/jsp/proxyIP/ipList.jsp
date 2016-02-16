<%@page import="com.mas.rave.util.ConstantScore"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'proxyIP';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">
		当前位置: 代理IP
	</div>

	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">代理IP</div>

			<form id="editForm" action="${ctx }/proxyIP/add" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">当前总数</td>
							<td>
								${ipCount}								
							</td>
						</tr>
						<tr>
							<td class="name">增加</td>
							<td>
								格式为ip:port,如：<font color='blue'>117.121.21.222:80</font>，多个请换行。<br/>
								<textarea rows="30" cols="80" id="ips" name="ips" >${fn:escapeXml(ips)}</textarea>
							</td>
						</tr>					
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn"><input type="submit" class="bigbutsubmit"
								value="保存" id="butsubmit_id" /> 
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
			submitHandler: function(form) {
				$("#butsubmit_id").attr("disabled","true");
				setDisabled('input[type="submit"]', window.document);
				$(form).ajaxSubmit({
					type : "POST",
					dataType : "json",
					 success: function(response){
						 var data = eval("("+response+")");
		        		if(data.flag==0){
	            			$.alert('添加成功', function(){window.location.href = "list";});
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
	</script>
</body>
</html>