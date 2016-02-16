<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
	    var temp="${temp}";
	    var menu_flag = 'appAlbumColumn';
	    if(temp=="1"){
	    	menu_flag = 'appAlbumStatistics';
	    }
		
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: 国家展示
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx}/country/listInfo" method="post">
					<input type="hidden" name="temp" id="temp" value="${temp}"/>
						平台国家英文名称：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name) }"/>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						<input type="button" class="bigbutsubmit" onclick="callback();" value="返回"/>
					</form>
				</div>
			</div>
		<div class="secmainoutside">
			<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall"/></td>
						<td align="center">
							countryid
						</td>
						<td align="center">
							国家名(英文)
						</td>
						<td align="center">
							地址
						</td>
						<td align="center">
							是否显示
						</td>
						<td align="center">
							创建日期
						</td>
						<td align="center">
							操作
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${countrys.recordCount == 0}">
						<tr>
							<td colspan="6" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${countrys.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
						<td align="center">
							${obj.id }
						</td>
						<td align="center">
							${fn:escapeXml(obj.nameCn)}(${fn:escapeXml(obj.name)})
						</td>
						<td align="center">
							<img  width="50" height="50" src="${path}${obj.url }">
						</td>
						<td align="center">
							<c:if test="${obj.state ==1}">是</c:if>
							<c:if test="${obj.state ==0}">否</c:if>
						</td>
						<td align="center">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>							    
						</td>

						<td align="center">
						<c:if test="${obj.id!=1}">
							<c:if test="${obj.state ==1}"><a href="updateState/${obj.id }/0" style="color: red;font-size: 14px"><b>不显示</b></a></c:if>
							<c:if test="${obj.state ==0}"><a href="updateState/${obj.id }/1" style="color: green;font-size: 14px"><b>显示</b></a></c:if>
						</c:if>
						
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${countrys.recordCount != 0}">
						<ccgk:pagination paginationVo="${countrys}" contextPath="${ctx}/country/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
			change($("#categoryId").val());
			$("#addForm").validate({
				submitHandler: function(form) {
					$(form).ajaxSubmit({
						type : "POST",
						dataType : "json",
						 success: function(response){
			        		if(response.flag==0){
		            			$.alert('更新成功', function(){window.location.href = "listInfo";});
		            		}else{
		            			$.alert('更新失败', function(){window.location.href = "listInfo";});
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
		function callback(){
			var temp="${temp}";
			if(temp=="1"){
				window.location.href='${ctx }/appAlbumStatistics/show';
			}else{
				window.location.href='${ctx }/appAlbumColumn/list';
			}
		}
	</script>
	</body>
</html>
