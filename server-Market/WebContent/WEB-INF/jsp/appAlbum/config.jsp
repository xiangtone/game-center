<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appAlbum';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span> -&gt; 分发配置
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">${appAlbum.name }</div>
			<form id="editForm" action="" method="post">
			<div class="border">
				<input type="hidden" name="raveId" id="raveId" value="${appAlbum.id}" >
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">标签名</td>
							<td class="content">
								<span>${appAlbum.name }</span>
							</td>
							<td class="content_info">
							</td>
						</tr>
						<tr>
							<td class="name">展示菜单项</td>
							<td class="content">
								<c:forEach var="app" items="${appInfos}">
									<c:if test="${app.checked==false}">
										<table>
											<tr>
												<td><input type="checkbox" name="menus" value="${app.id}"  />
												<img alt="${app.name }" src="${path}${app.bigLogo}" width="50" height="50">
												</td>
												<td>${app.name }<br>
												</td>
											</tr>
										</table>
									</c:if>
									&nbsp;&nbsp;
								</c:forEach>
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="reset" class="bigbutsubmit" value="重填" onclick="check(0)"/>
								<input type="button" class="bigbutsubmit" value="全选" onclick="check(1)"/>
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
				var checks = $("input[type='checkbox'][checked=true]");
				if(checks.length == 0){
					$.alert("温馨提示:您必须选择一项!");
					$("#butsubmit_id").removeAttr("disabled");
					return;
				}
				$(form).ajaxSubmit({
					 success: function(response){
		        		var data = eval("("+response+")");
		        		if(data.flag==0){
	            			$.alert('配置成功', function(){window.location.href = "../list";});
	            		}else{
	                		$.alert('配置失败,请重新输入');
	                		$("#butsubmit_id").removeAttr("disabled");
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
	function check(flag){
		if(flag == 0){
			$("input[type='checkbox']").attr("checked",false);
		}else{
			$("input[type='checkbox']").attr("checked",true);
		}
	}
	</script>
</body>
</html>