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
		当前位置: <span id="childTitle"></span>
			<!-- 排序需要传以下2个隐藏域的值,分别为:当前系统服务器请求上下文,数据库表名 -->
	<input type="hidden" id="serverPath" value="${ctx }" />
	<input type="hidden" id="tableName" value="t_search_keyword" />
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/search/list" method="post">
						&nbsp;&nbsp;<label>资源类型:</label> 
						<select id="albumId" name="albumId">
							<c:forEach var="appAlbum" items="${appAlbums}">
								<option value="${appAlbum.id}"
									<c:if test="${appAlbum.id == param.albumId}">selected='selected'</c:if> 	
								>${appAlbum.name}</option>							
							</c:forEach>
						</select>
							&nbsp;&nbsp;国家：
							<select name="raveId" id="raveId">
								<option value="0">全部</option>												
								<c:forEach var="country" items="${countrys}">
									<option value="${country.id}"
									 <c:if test="${country.id==param.raveId}"> selected="selected"</c:if>
								    >${country.name}(${country.nameCn})</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx }/searchIcon/list';" value="图标管理" />				
					</form>
				</div>
				<div class="operateShop">
					<ul>
					    <li><a href="${ctx}/search/showAdd">添加</a></li>
						<li><a id="cmd-delete">删除</a></li>
						<li><a type="button" class="lang" id="sort_edit">编辑排序</a></li>
						<li><a type="button" class="lang" id="sort_save">保存排序</a></li>
					</ul>
				</div>
			</div>
		<div class="secmainoutside">
			<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall"/></td>
						<td>
							搜索关键字
						</td>
						<td>
							国家名称
						</td>
						<td>
							资源名称(资源ID)
						</td>
						<td>
							搜索图标
						</td>
						<td>
							资源logo
						</td>
						<td>
							flag
						</td>
						<td>
							资源类型
						</td>
						<td>
							排序
						</td>
						<td>
							searchNum
						</td>
						<td>
							创建时间
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="10" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId" value="${obj.searchId}"/></td>
							<td align="center">
								<a href="show${obj.searchId }?albumId=${param.albumId}&raveId=${param.raveId}&currentPage=${result.currentPage}">${fn:escapeXml(obj.keyword) }</a>
							</td>
							<td align="center">
								${obj.country.name}(${obj.country.nameCn})
							</td>
							<td align="center">
							<c:if test="${obj.resId==0}">
								<c:if test="${obj.flag==2}">
								<a href="listRes/${obj.searchId }">查看列表详细</a>
								</c:if>
								<c:if test="${obj.flag==0}">
								<span style="color: red">无资源</span>
								</c:if>
							</c:if>
							
								<c:if test="${obj.resId!=0}">${obj.resName}(${obj.resId})</c:if>
								
							</td>
							<c:choose>
								<c:when test="${obj.iconUrl!=null and obj.iconUrl!=''}">
									<td align="center">
								      <img alt="${obj.resName}" width="50" height="50" src="${path}${obj.iconUrl }">
									</td>
								</c:when>
							<c:otherwise>
								<td align="center">
									<img alt="图标" width="50" height="50" src="${ctx}/static/res/ringtones_disk_pic.png">
								</td>
								</c:otherwise>
							</c:choose>
							<td align="center">	
								<c:if test="${obj.flag==1 and obj.resLogo!=null}">
									
									  <img alt="${obj.resName}" width="50" height="50" src="${path}${obj.resLogo }">
								</c:if>	
							</td>													
							<td align="center">
								<c:if test="${obj.flag==0}">关键字</c:if>							
								<c:if test="${obj.flag==1}">资源</c:if>
								<c:if test="${obj.flag==2}">列表</c:if>
							</td>							
							<td align="center">
								<c:if test="${obj.albumId==2}">App&Game</c:if>
								<c:if test="${obj.albumId==4}">Ringtones</c:if>
								<c:if test="${obj.albumId==5}">Wallpaper</c:if>
							</td>
							<td align="center"><input type="text" id="sort_${obj.searchId }"
								value="${fn:escapeXml(obj.sort) }" maxlength="9" readonly="readonly"
								style="width: 30px;" />
							</td>	
							<td align="center">
								${obj.searchNum }
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
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/search/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var params = {"albumId" : $("#albumId").val(),"raveId" : $("#raveId").val()};
		paginationUtils(params);
	});	
	function checkId(){
		var item = $("[name=recordId]");
		var id = "";
		if (item.size() > 0) {
			item.each(function() {
						if ($(this).attr("checked") == true) {
							id += $(this).val();
							id += ",";
						}
					});
		}
		id = id.substr(0, id.lastIndexOf(","));
		if (id == "") {
			$.alert("请选择记录");
			return '';
		} else {
			return id;
		}
	}
	
	$("#cmd-delete").click(function() {
		var id = checkId();
		if (id != ''){
			$.alert("删除后将不可恢复，确定删除？", function() {
				$.ajax({
					url:"${ctx}/search/delete",
					type:"POST",
					data:{'id':id},
					dataType:"json",
					success:function(data){
						var flag = eval("("+data+")");
						if(flag.success == "true"){
							alert("删除成功！");
							//window.location.reload();
							window.location.href = "list";
						}else{
							alert("删除失败！");
							//window.location.reload();
							window.location.href = "list";
						}
					},
					error:function(data){
						alert("删除失败！");
						//window.location.reload();
						window.location.href = "list";
					}
				});
			}, true);
		}
	});
	$("#albumId").change(function() {
		$("#btn-search").click();
	});
	$("#raveId").change(function() {
		$("#btn-search").click();
	});
	</script>
</body>
</html>