<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'appAlbumStatistics';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">当前位置: <span id="childTitle"></span></div>
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<table>
						<tr>
							<td>
								<form id="appAlbumStatisticsShow"
									action="${ctx }/appAlbumStatistics/show"
									method="post">
									应用名称 : <input type="text" name="appName" id="appName" value="${fn:escapeXml(param.appName)}" />&nbsp;&nbsp;
									app分类 : <select id="albumId" name="albumId"  onchange="querySecondCategory(this.value)">
										<option value="1" <c:if test="${param.albumId==1}"> selected="selected"</c:if>>Homes</option>										
										<option value="2" <c:if test="${param.albumId==2}"> selected="selected"</c:if>>Apps</option>
										<option value="3" <c:if test="${param.albumId==3}"> selected="selected"</c:if>>Games</option>		
									</select>&nbsp;&nbsp;					
								    app页签 : <select id="columnId" name="columnId" >
										<option value="0">--all--</option>
									</select>&nbsp;&nbsp;	
										国家: 
									<select name="raveId" id="raveId">							
										<c:forEach var="country" items="${countrys}">
											<option value="${country.id}" <c:if test="${country.id==param.raveId}"> selected="selected"</c:if>>${country.name}(${country.nameCn})</option>
										</c:forEach>
									</select>&nbsp;&nbsp;
									<button type="submit" class="butsearch" id="btn-search">查询</button>
									<input type="button" class="butsearch" value="重置" onclick="resetQuery()"/>								
									<input id="appAlbum_statistics" name="appAlbum_statistics" class="butsearch" value="各维度统计" >
									<input type="button" class="bigbutsubmit" onclick="openWindow()" value="国家展示" />
									
									
								</form>
							</td>
						</tr>
					</table>

				</div>
			</div>
		<div class="secmainoutside">
		<div class="border" style="height:540px; overflow:auto; border: 1px solid #CECECE;">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"></td>
						<td align="center">apkId</td>
						<td align="center">页签</td>
						<td align="center">专辑(英文)</td>
						<td align="center">分类名(英文)</td>
						<td align="center"  width="15%">应用名(appId)</td>
						<td align="center">logo</td>
						<td align="center">星星评级</td>
						<td align="center">版本名</td>
						<td align="center">文件大小</td>
						<td align="center">分数</td>
						<td align="center">下载数</td>
						<td align="center">下载地址</td>

					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="12" align="center">没有符合要求的数据！</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"></td>
							<td align="center">${obj.appFile.id }</td>
							<td align="center">${obj.appAlbum.name }</td>
							<td align="center">${obj.appAlbumColumn.nameCn }(${obj.appAlbumColumn.name })</td>
							<td align="center">${obj.appInfo.category.categoryCn }(${obj.appInfo.category.name })</td>
							<td align="center">${obj.appName }(${obj.appInfo.id })</td>
							<td align="center"><img src="${path}${obj.bigLogo}" width="50" height="50"></td>
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
							<td align="center">${obj.initDowdload }</td>
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
							contextPath="${ctx}/appAlbumStatistics/show"
							params="${params}" />
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
				
			var params = {"appName" : $("#appName").val(),"albumId" : $("#albumId").val(),"raveId" : $("#raveId").val(),"columnId" : $("#columnId").val()};
			paginationUtils(params);
			querySecondCategory($("#albumId").val());
			var cou = parseInt("${result.recordCount}");
			var num = parseInt("${num}");
			if(cou > num){
				alert('当前分发数量已经超过${num}');
			}		
			$("#appAlbum_statistics").click(function(){
				$("#appAlbumStatisticsShow").attr("action","${ctx}/appAlbumStatistics/list");
				$("#appAlbumStatisticsShow").submit();				
				/**
				var albumId =$("#albumId").val();
				var appName =$("#appName").val();
				var raveId =$("#raveId").val();
				var columnId =$("#columnId").val();				
				window.location.href = "${ctx}/appAlbumStatistics/list?albumId="+albumId
						+"&appName="+appName+"&raveId="+raveId+"&columnId="+columnId;
				*/
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
		function resetQuery(){
			if(confirm("确实要重置吗?")){
				var name =$("#appName");
				name.val("");
			}
		}
		function querySecondCategory(value){
			if(value == 0){
				$("#columnId").html('');
				//$("#categoryId").append("<option value='0'>--all--</option>");
				document.getElementById("columnId").add(new Option("--all--",0));
			}else{
				$.ajax({
					url : "${ctx}/appAlbumStatistics/albumColumn",
					type : "POST",
					dataType : "json",
					data : {"id" : value,"columnId":'${columnId}'},
					success : function (data){
						var result = eval("(" + data + ")");
						if(result.success == 1){
							$("#columnId").html('');
							$("#columnId").append(result.option);
						}
					},
					error : function (error){
						$.messager.alert("提示:","异常出现未知异常!");
					}
				});
			}
		}
		function openWindow(){
			window.open("${ctx }/country/listInfo?temp=1");
		}
	</script>
</body>
</html>