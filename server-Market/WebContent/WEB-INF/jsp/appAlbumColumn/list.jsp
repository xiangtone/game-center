<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appAlbumColumn';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	<!-- 排序需要传以下2个隐藏域的值,分别为:当前系统服务器请求上下文,数据库表名 -->
	<input type="hidden" id="serverPath" value="${ctx }"/>
	<input type="hidden" id="tableName" value="t_app_album_column"/>
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx}/appAlbumColumn/list" method="post">
						<select name="appAlbumId" id="appAlbumId">
							<%
								int i=0;
							%>
							<c:forEach var="appAlbum" items="${appAlbums}">
								<%
									if(i==0){
								%>
								<option value="0" >--all--</option>
								<% i++;
									}
								%>
								<c:if test="${appAlbum.state==true }">
								<option value="${appAlbum.id}" >${appAlbum.name}</option>
								</c:if>
							</c:forEach>
							</select>
						&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						&nbsp;&nbsp;&nbsp;&nbsp;
						国家：
							<select name="raveId" id="raveId">							
								<c:forEach var="country" items="${countrys}">
									<option value="${country.id}" <c:if test="${country.id==param.raveId}"> selected="selected"</c:if>>${country.name}(${country.nameCn})</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
							<input type="button" class="bigbutsubmit" onclick="openWindow()" value="国家展示" />
					</form>
				</div>
				<!-- <div class="operateShop">
					<ul>
						<li><a type="button" class="lang" id="sort_edit" >编辑排序</a></li>
						<li><a type="button" class="lang" id="sort_save">保存排序</a></li>
					</ul>
				</div> -->
			</div>
		<div class="secmainoutside">
			<div class="border">
			<table class="mainlist"  width="100%">
				<thead>
					<tr>
						<td align="center">
							分类
						</td>
						<td align="center">
							页签(英文)
						</td>
						<td align="center">
							操作
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="3" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<td align="center">
							${obj.appAlbum.name }(${obj.appAlbum.id })
						</td>
						<td align="center">
							${fn:escapeXml(obj.nameCn) }(${fn:escapeXml(obj.name) })
						</td>
						<td align="center">
							<c:if test="${obj.appAlbum.id == 4 }">
							<a name="ringtonesalbummanage" href="#" onclick="ringtonesalbummanage(${obj.columnId})">分发</a>
							</c:if>
							<c:if test="${obj.appAlbum.id == 5 }">
							<a name="wallpaperalbummanage" href="#" onclick="wallpaperalbummanage(${obj.columnId})">分发</a>
							</c:if>
							<c:if test="${obj.appAlbum.id < 4}">
							<c:if test="${ obj.columnId!=45}">
							<a name="albummanage" href="#" onclick="albumhand(${obj.columnId})">手动排行</a>&nbsp;|&nbsp;
							<a name="albummanage" href="#" onclick="albummanage(${obj.columnId})">分发管理</a>
							&nbsp;|&nbsp;<a name="showOperant" href="#" onclick="showOperant(${obj.columnId})">生效列表</a>
							</c:if>
							<c:if test="${ obj.columnId==45}">
							 <a name="albummanage" href="#" onclick="albummanageLiveWallpaper(${obj.columnId})">分发管理</a>
							</c:if>							
							</c:if>
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/appAlbumColumn/list" params="${params}"/>
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var appAlbumId = "${param.appAlbumId}";
		if(appAlbumId == null || appAlbumId == ''){
			$("#appAlbumId option[value=true]").attr("selected","selected");
		}else{
			$("#appAlbumId option[value='"+ appAlbumId +"']").attr("selected","selected");
		}
		var raveId = "${param.raveId}";

		if(raveId == null || raveId == ''){
			$("#raveId option[value=true]").attr("selected","selected");
		}else{
			$("#raveId option[value='"+ raveId +"']").attr("selected","selected");
		}
		var params = {"appAlbumId" : $("#appAlbumId").val(),"raveId" : $("#raveId").val()};
		paginationUtils(params);
	});

	function albummanage(columnId){
		var raveId = $("#raveId").val();
		$("a[name='albummanage']").attr("href","listRes/"+columnId+"?raveId="+raveId);
	}
	function albummanageLiveWallpaper(columnId){
		var raveId = $("#raveId").val();
		$("a[name='albummanage']").attr("href","liveWallpaperListRes/"+columnId+"?raveId="+raveId);
	}
	function albumhand(columnId){
		var raveId = $("#raveId").val();
		$("a[name='albummanage']").attr("href","handListRes/"+columnId+"?temp=list&raveId="+raveId);
	}
	function ringtonesalbummanage(columnId){
		var raveId = $("#raveId").val();
		//href="${ctx }/ringtones/listRes/${obj.columnId}"
		$("a[name='ringtonesalbummanage']").attr("href","${ctx }/ringtones/listRes/"+columnId+"?raveId="+raveId);
	}

	function wallpaperalbummanage(columnId){
		var raveId = $("#raveId").val();
		// href="${ctx }/wallpaper/listRes/${obj.columnId}"
		$("a[name='wallpaperalbummanage']").attr("href","${ctx }/wallpaper/listRes/"+columnId+"?raveId="+raveId);
	}
	function showOperant(columnId){
		var raveId =  $("#raveId").val();
		$("a[name='showOperant']").attr("href","show/"+columnId+"?temp=list&raveId="+raveId);
	}
	function doOperant(columnId){
		var raveId =  $("#raveId").val();
		var country = $("#raveId").find("option:selected").text();
		$.alert("当前分发所选国家为"+country+"，确定生效？", function() {
			$.ajax({
				url:"${ctx}/appAlbumColumn/doOperant/"+columnId+"?raveId="+raveId,
				type:"POST",
				data:{'columnId':columnId},
				dataType:"json",
				success:function(data){
					var data1 = eval("("+data+")");
					if(data1.flag== "0"){
						alert("分发已经生效！");
						//window.location.href = "list";
					}else if(data1.flag== "1"){
						alert("分发失败！");
						//window.location.href = "list";
					}else if(data1.flag== "2"){
						alert("分发程序未运行完，不能生效！");
						//window.location.href = "list";
					}
				},
				error:function(data){
					alert("分发失败！");
					//window.location.href = "list";
				}
			});
		}, true);
	}
	
	function openWindow(){
		window.open("${ctx }/country/listInfo?temp=2");
	}
	</script>
</body>
</html>
