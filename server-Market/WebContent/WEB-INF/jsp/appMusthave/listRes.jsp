<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'appMusthave';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">当前位置: <span id="childTitle"></span> -&gt; <font>${country.name}(${country.nameCn})--${appCollection.name}(${appCollection.nameCn})</font></div>
	<!-- 排序需要传以下2个隐藏域的值,分别为:当前系统服务器请求上下文,数据库表名 -->
	<input type="hidden" id="serverPath" value="${ctx }" />
	<input type="hidden" id="tableName" value="t_app_collection_res" />
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<table>
						<tr>
							<td width="400">
								<form id="applistRes" action="${ctx }/appAlbumColumn/collectionRes/${appCollection.collectionId}?raveId=${country.id}&type=${type}" method="post">
									应用名称：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name)}" maxlength="100"/>&nbsp;&nbsp;
										    <input type="hidden" id="raveId" name="raveId" value="${raveId}"/>																										
											<input type="hidden" id="type" name="type" value="${type}"/>									
									<button type="submit" class="butsearch" id="btn-search">查询</button>
									<input type="button" class="butsearch" value="重置" onclick="resetQuery()"/>								
									<input type="hidden" name="resType" value="theme">
								</form>
							</td>
							<td width="100">
								<input onclick="window.location.href='${ctx }/appMusthave/list?type=${type}'" class="bigbutsubmit" type="button" value="返回" >
							</td>
							<td align="left">
								<form action="${ctx}/appAlbumColumn/${appCollection.collectionId}/collectionConfig" method="post">
									<input type="hidden" id="raveId" name="raveId" value="${raveId}"/>	
									<input type="hidden" id="type" name="type" value="${type}"/>																									
									<input type="hidden" name="collectionId" value="${appCollection.collectionId}">
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
		<div class="secmainoutside">
		<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall" /></td>
						<td align="center">apkId</td>
						<td align="center">主题名(中文)</td>
						<td align="center">分类名(英文)</td>
						<td align="center">应用名(appId)</td>
						<td align="center">logo</td>
						<td align="center">星星评级</td>
						<td align="center">文件大小</td>
						<td align="center">排序</td>
						<td align="center">下载数(实)--(虚)</td>
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
							<td align="center">${obj.appCollection.name }(${obj.appCollection.nameCn })</td>
							<td align="center">${obj.appInfo.category.categoryCn }(${obj.appInfo.category.name })</td>
							<td align="center">${obj.appName }(${obj.appInfo.id })</td>
							<td align="center"><img alt="${obj.appName }" src="${path}${obj.bigLogo}" width="50" height="50"></td>
							<td align="center">${obj.stars }</td>
							<c:if test="${ obj.fileSize <1024}">
								<td align="center">${obj.fileSize }kb</td>
							</c:if>
							<c:if test="${ obj.fileSize >1024}">
								<td align="center">
									<fmt:formatNumber type="number" value="${obj.fileSize/1024/1024 }" maxFractionDigits="2"/>MB
								</td>
							</c:if>
							<td align="center"><input type="text" id="sort_${obj.id }"
								value="${fn:escapeXml(obj.sort) }" maxlength="12" readonly="readonly"
								style="width: 60px;" /></td>
							<td align="center">${obj.realDowdload } -- ${obj.initDowdload }</td>
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
							contextPath="${ctx}/appAlbumColumn/collectionRes/${appCollection.collectionId}"
							params="${params}" />
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			var params = {"Q_imageName" : $("#Q_imageName").val(),"resType" : "theme","raveId":"${raveId}","type":"${type}"};
			paginationUtils(params);
			var count = "${result.recordCount}";
			var num = "${num}";
			count = parseInt(count);
			num = parseInt(num);
			if(count > num){
				alert("分发数据总数已超过" + num + "个!");
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
						url : "${ctx}/appAlbumColumn/collectionDelete",
						type : "POST",
						data : {
							'id' : id,
							'type' : '${type}'
						},
						dataType : "json",
						success : function(data) {
							
							var flag = eval("("+data+")");
							if(flag.success == "true"){
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
			$("#name").val("");
		}
	</script>
</body>
</html>