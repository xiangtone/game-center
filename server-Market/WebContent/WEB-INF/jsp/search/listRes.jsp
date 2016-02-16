<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'search';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>-${searchKeyword.keyword}(${searchKeyword.searchId})
					<!-- 排序需要传以下2个隐藏域的值,分别为:当前系统服务器请求上下文,数据库表名 -->
	<input type="hidden" id="serverPath" value="${ctx }" />
	<input type="hidden" id="tableName" value="t_search_keyword_reslist" />
	</div>
	
	<div class="mainoutside">
		<div class="secmainoutside">
			<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/search/listRes" method="post">		
						<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx }/search/list';" value="返回"/>						
					</form>
				</div>	
				<div class="operateShop">
					<ul>				
						<li><a type="button" class="lang" id="sort_edit">编辑排序</a></li>
						<li><a type="button" class="lang" id="sort_save">保存排序</a></li>
					</ul>
				</div>		
			</div>
			<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall"/></td>
						
						<td>
							资源名称(资源ID)
						</td>	
						<td>
						         图标
						</td>
						<td>
						         排序
						</td>					
						<td>
							创建时间
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="4" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>			
							<td align="center">
								<c:if test="${obj.resId!=0}">${obj.resName}(${obj.resId})</c:if>							
							</td>
							 <c:if test="${obj.resId!=0}">
							<c:choose>
								<c:when test="${obj.logo!=null and obj.logo!=''}">
									<td align="center">
								      <img alt="${obj.resName}" width="50" height="50" src="${path}${obj.logo }">
									</td>
								</c:when>
							<c:otherwise>
								<td align="center">
									<img alt="图标" width="50" height="50" src="${ctx}/static/res/ringtones_disk_pic.png">
								</td>
								</c:otherwise>
								
							</c:choose>
							</c:if>
							<td align="center"><input type="text" id="sort_${obj.id }"
								value="${fn:escapeXml(obj.sort) }" maxlength="9" readonly="readonly"
								style="width: 30px;" />
							</td>	
							<td align="center">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/search/listRes/${searchId}" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
		
	</script>
</body>
</html>