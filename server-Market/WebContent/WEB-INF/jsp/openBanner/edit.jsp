<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'openBanner';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 替换banner图信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">替换banner图信息</div>
			<form id="editForm" action="${ctx }/openBanner/update" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>						
						<tr>
						<tr>
							<td class="name">主题列表id</td>
							<td class="content">
								<select name="appAlbumThemId" id="appAlbumThemId">
								<c:forEach var="appAlbumThem" items="${appAlbumThems}">
									<option value="${appAlbumThem.themeId}">${appAlbumThem.nameCn }(${appAlbumThem.themeId})</option>
								</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
						<td class="name">banner效果图</td>
							<td class="content">
							<img alt="" src="${path}${openBanner.bigicon }">
							
							<input type="hidden" id="themeId" name="themeId" value=${openBanner.themeId }>
							<input type="hidden" id="bigicon" name="bigicon" value=${openBanner.bigicon }>
							<input type="hidden" id="apkId" name="apkId"  value="${openBanner.apkId }">								
							<input type="hidden" id="flag" name="flag" value=${openBanner.flag }>
							<input type="hidden" id="state" name="state" value=${openBanner.state }>
							
							</td>
						</tr>
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
			submitHandler: function(form) {
				$(form).ajaxSubmit({
					type : "POST",
					dataType : "json",
					 success: function(response){
						 var data = eval("(" + response+ ")");
		        		if(data.flag==0){
	            			$.alert('更新banner成功', function(){window.location.href = "list";});
		        		}else if(data.flag==2){
	            			$.alert(data.msg);
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