<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'appAlbumColumn';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">当前位置: <span id="childTitle"></span>- <font>${country.name}(${country.nameCn})--${appAlbumColumn.appAlbum.name }--${appAlbumColumn.name }</font></div>
	<!-- 排序需要传以下2个隐藏域的值,分别为:当前系统服务器请求上下文,数据库表名 -->
	<input type="hidden" id="serverPath" value="${ctx }" />
	<input type="hidden" id="tableName" value="t_app_album_res" />
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<table>
						<tr>
							<td width="800">
								<form
									action="${ctx }/appAlbumColumn/show/${appAlbumColumn.columnId}?temp=${temp }&raveId=${raveId}"
									method="post">
									应用名称：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name)}" />&nbsp;&nbsp;
									分发类型：
											<select id="source" name="source">
											    <option value="-1">全部</option>
												<option value="0">手动</option>
												<option value="1">自动</option>
											</select>&nbsp;&nbsp;
									<input type="hidden" id="temp" name="temp" value="${temp }"/>								
									<input type="hidden" id="raveId" name="raveId" value="${raveId}"/>									
									<button type="submit" class="butsearch" id="btn-search">查询</button>
									<input type="button" class="butsearch" value="重置" onclick="resetQuery()"/>								
									<input id="callback" name="callback" class="bigbutsubmit" style="margin-left: 50px" type="button" value="返回" >
									<input type="button" class="bigbutsubmit" value="导 出"  id="export_appAlbumOperant" onclick="exportExcel()"/>
									
								</form>
							</td>
						</tr>
					</table>

				</div>
			</div>
		<div class="secmainoutside">
		<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall" /></td>
						<td align="center">apkId</td>
						<td align="center">页签</td>
						<td align="center">专辑(英文)</td>
						<td align="center">分类名(英文)</td>
							<td align="center">应用名(appId)</td>
						<td align="center">logo</td>
						<td align="center">星星评级</td>
						<td align="center">版本名</td>
						<td align="center">文件大小</td>
						<td align="center">分数</td>
						<td align="center">分发类型</td>						
						<td align="center">下载数</td>
						<td align="center">下载地址</td>

					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="14" align="center">没有符合要求的数据！</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId"
								value="${obj.id}" /></td>
							<td align="center">${obj.appFile.id }</td>
							<td align="center">${obj.appAlbum.name }</td>
							<td align="center">${obj.appAlbumColumn.nameCn }(${obj.appAlbumColumn.name })</td>
							<td align="center">${obj.appInfo.category.categoryCn }(${obj.appInfo.category.name })</td>
							<td align="center">${obj.appName }(${obj.appInfo.id })</td>
							<td align="center"><img alt="${obj.appName }" src="${path}${obj.bigLogo}" width="50" height="50"></td>
							<td align="center">${obj.stars }</td>
							<td align="center">${obj.versionName }</td>
							<c:if test="${ obj.fileSize <1024}">
								<td align="center">${obj.fileSize }KB</td>
							</c:if>
							<c:if test="${ obj.fileSize >1024}">
								<td align="center">
									<fmt:formatNumber type="number" value="${obj.fileSize/1024/1024 }" maxFractionDigits="2"/>MB
								</td>
							</c:if>
							<td align="center">${obj.sort }</td>
							<c:if test="${obj.source==0 }">
								<td align="center">手动</td>
							</c:if>
							<c:if test="${obj.source==1 }">
								<td align="center">自动</td>
							</c:if>
							
							<td align="center">${obj.initDowdload }</td>
							<td align="center"><a href="${path }${obj.url }">下载</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}"
							contextPath="${ctx}/appAlbumColumn/show/${appAlbumColumn.columnId}"
							params="${params}" />
					</c:if>
				</div>
			</div>
		</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			var cou = parseInt("${result.recordCount}");
			var num = parseInt("${num}");
			if(cou >= num){
				alert('当前分发数量已经超过${num}');
			}
			var source = "${param.source}";
			if(source == null || source == ''){
				$("option[value=-1]").attr("selected","selected");
			}else{
				$("option[value='"+ source +"']").attr("selected","selected");
			}
			var params = {"name" : $("#name").val(),"source" : $("#source").val()};
			paginationUtils(params);
			
			$("#callback").click(function() {
				var temp ="${temp }"; 
				if(temp=="list"){
					window.location.href='${ctx }/appAlbumColumn/list?raveId=${raveId}';
				}else if(temp=="listRes"){
					window.location.href='${ctx }/appAlbumColumn/listRes/${appAlbumColumn.columnId}?raveId=${raveId}';
				}else{
					window.location.href='${ctx }/appAlbumColumn/list';	
				}
				
			});
		});
		function checkId() {
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
		//导出
		function exportExcel(){
			//导出调用
			var xmlName = "appAlbumOperant.xml";
			window.document.location = "${ctx}/appAlbumColumn/export2Excel/${appAlbumColumn.columnId}?xmlName="
					+ xmlName
					+ "&name=" + $('#name').val()
					+ "&temp=" + $('#temp').val()
					+ "&raveId=" + $('#raveId').val()
					+ "&source=" + $('#source').val();
		}
		function resetQuery(){
			if(confirm("确实要重置吗?")){
				var name =$("#name");
				name.val("");
			}
		}
	</script>
</body>
</html>
