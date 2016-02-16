<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'musicAlbumTheme';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">当前位置: <span id="childTitle"></span> -&gt; <font>${country.name}(${country.nameCn})--Ringtones--Theme</font></div>
	<!-- 排序需要传以下2个隐藏域的值,分别为:当前系统服务器请求上下文,数据库表名 -->
	<input type="hidden" id="serverPath" value="${ctx }" />
	<input type="hidden" id="tableName" value="t_res_music_album_res" />
	<div class="mainoutside">
		<div class="secmainoutside">
			<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<table>
						<tr>
							<td width="800">
								<form id="musicThemeForm" action="${ctx }/ringtones/listRes/${requestScope.themeId}?resType=theme&raveId=${raveId}" method="post">
									歌曲名称：<input type="text" name="Q_musicName" id="Q_musicName" value="${fn:escapeXml(param.Q_musicName)}" maxlength="100"/>&nbsp;&nbsp;
								国家：
									<select name="raveIds" id="raveIds" onchange="selectQuery(this.value)">							
										<c:forEach var="country" items="${countrys}">
											<option value="${country.id}" <c:if test="${country.id==raveId}"> selected="selected"</c:if>>${country.name}(${country.nameCn})</option>
										</c:forEach>
									</select>&nbsp;&nbsp;
									<button type="submit" class="butsearch" id="btn-search">查询</button>
									<input type="button" class="butsearch" value="重置" onclick="resetQuery()"/>								
									<input type="hidden" name="resType" value="theme">
									<input type="hidden" name="raveId" value="${raveId}"/>																														
								</form>
							</td>
							<td width="100">
								<input onclick="window.location.href='${ctx }/musicAlbumTheme/list'" class="bigbutsubmit" type="button" value="返回" >
							</td>
							<td align="left">
								<form action="${ctx }/ringtones/${requestScope.themeId}/config" method="post">
									 <input type="hidden" name="raveId" value="${raveId}"/>																								
									<input type="hidden" name="themeId" value="${requestScope.themeId }">
									<input type="hidden" name="resType" value="theme">
									<input type="submit" class="bigbutsubmit" value="配 置"  id="butsubmit_id"/>
								</form>
							</td>
							
							
						</tr>
					</table>

				</div>
				<div class="operateShop">
					<ul>
						<li><a id="cmd-delete">删除</a></li>
						<li><a type="button" class="lang" id="sort_edit">编辑排序</a></li>
						<li><a type="button" class="lang" id="sort_save">保存排序</a></li>
					</ul>
				</div>
			</div>
		<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall" /></td>
						<td align="center">编号</td>
						<td align="center">主题名称</td>
						<td align="center">分类名(英文)</td>
						<td align="center">歌曲名称</td>
						<td align="center">logo</td>
						<td align="center">星星评级</td>
						<td align="center">歌手</td>
						<td align="center">文件大小</td>
						<td align="center">排序</td>
						<td align="center">下载数(实)--(虚)</td>
						<td align="center">上线时间</td>					
						<td align="center">下载地址</td>

					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="13" align="center">没有符合要求的数据！</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId"
								value="${obj.id}" /></td>
							<td align="center">${obj.id }</td>
							<td align="center">${obj.musicTheme.name }(${obj.musicTheme.nameCn })</td>
							<td align="center">${obj.category.categoryCn }(${obj.category.name })</td>
							<td align="center">${obj.musicName }</td>
							<td align="center"><img alt="${obj.musicName }" src="${path}${obj.logo}" width="50" height="50"></td>
							<td align="center">${obj.stars }</td>
							<td align="center">${obj.artist }</td>
							<c:if test="${ obj.fileSize <1024}">
								<td align="center">${obj.fileSize }KB</td>
							</c:if>
							<c:if test="${ obj.fileSize >1024}">
								<td align="center">
									<fmt:formatNumber type="number" value="${obj.fileSize/1024/1024 }" maxFractionDigits="2"/>MB
								</td>
							</c:if>
							<td align="center"><input type="text" id="sort_${obj.id }"
								value="${fn:escapeXml(obj.sort) }" maxlength="9" readonly="readonly"
								style="width: 30px;" /></td>
							<td align="center">${obj.realDowdload } -- ${obj.initDowdload }</td>
							<td align="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/></td>							
							<td align="center"><a href="${path }${obj.url }">下载</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}"
							contextPath="${ctx}/ringtones/listRes/${requestScope.themeId}"
							params="${params}" />
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			var params = {"Q_musicName" : $("#Q_musicName").val(),"resType" : "theme","raveId":"${raveId}"};
			paginationUtils(params);
			var count = "${result.recordCount}";
			var maxcount = "${maxcount}";
			count = parseInt(count);
			maxcount = parseInt(maxcount);
			if(count > maxcount){
				alert("分发数据总数已超过" + maxcount + "个!");
			}
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

		$("#cmd-delete").click(function() {
			var id = checkId();
			if (id != '') {
				$.alert("删除后将不可恢复，确定删除？", function() {
					$.ajax({
						url : "${ctx}/ringtones/delete",
						type : "POST",
						data : {
							'id' : id
						},
						dataType : "json",
						success : function(data) {
							if (data.success == "true") {
								$.alert("删除成功！");
								//window.location.reload();
								var href = window.location.href;
								window.location.href = href;
							}
						},
						error : function(data) {
							$.alert("error:" + data);
						}
					});
				}, true);
			}
		});
		function resetQuery(){
			$("#Q_musicName").val("");
		}
		function selectQuery(id){
			window.location.href = "${ctx }/ringtones/listRes/${requestScope.themeId}?resType=theme&raveId="+id;
		}
	</script>
</body>
</html>