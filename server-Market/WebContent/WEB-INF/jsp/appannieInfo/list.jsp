<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'appannieInfo';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">当前位置: <span id="childTitle"></span></div>
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">		
								<form id="applistRes" action="${ctx }/appannieInfo/list" method="post">
									应用名称：<input type="text" name="appName" id="appName" value="${fn:escapeXml(param.appName)}" />&nbsp;&nbsp;
									app分类 :<select id="albumId" name="albumId"  onchange="querySecondCategory(this.value)">
									    <option value="1" <c:if test="${param.albumId==1}"> selected="selected"</c:if>>Homes</option>
										<option value="2" <c:if test="${param.albumId==2}"> selected="selected"</c:if>>Apps</option>
										<option value="3" <c:if test="${param.albumId==3}"> selected="selected"</c:if>>Games</option>		
									</select>&nbsp;&nbsp;
									app页签 :<select id="columnId" name="columnId" >
										<option value="0">--all--</option>
									</select>&nbsp;&nbsp;						
									国家：
									<select name="raveId" id="raveId">							
										<c:forEach var="country" items="${countrys}">
										 <c:if test="${country.id!=1}">
											<option value="${country.id}" <c:if test="${country.id==param.raveId}"> selected="selected"</c:if>>${country.name}(${country.nameCn})</option>
										</c:if>
										</c:forEach>
									</select>&nbsp;&nbsp;
									<button type="submit" class="butsearch" id="btn-search">查询</button>
									<input type="button" class="butsearch" value="重置" onclick="resetQuery()"/>								
									&nbsp;&nbsp;
									<button type="button" class="bigbutsubmit" id="simulated-distribution">模拟分发</button>
								</form>

				</div>
			</div>
		<div class="secmainoutside">
		<div class="border">
			<table class="mainlist" width="100%">
				<thead>
					<tr>
						<td align="center">apkId</td>
						<td align="center">页签</td>
						<td align="center">分类名(英文)</td>
						<td align="center">应用名(appId)</td>
						<td align="center">logo</td>
						<td align="center">安装量</td>
						<td align="center">评分</td>	
						<td align="center">初次上线时间</td>					
						<td align="center">排行</td>
						<td align="center">大小</td>
						<td align="center">创建时间</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="7" align="center">没有符合要求的数据！</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">						
							<td align="center">${obj.appFile.id }</td>
							<td align="center">${obj.appAlbum.name }</td>
							<td align="center">${obj.appInfo.category.categoryCn }(${obj.appInfo.category.name })</td>
							<td align="center">${obj.appName }(${obj.appInfo.id })</td>
							<td align="center"><img alt="${obj.appName }" src="${path}${obj.appInfo.bigLogo}" width="50" height="50"></td>												
							<td align="center">
							<fmt:formatNumber value="${obj.annieInstallTotal }" pattern="#0.0####"/>
							</td>
							<td align="center">
							<fmt:formatNumber value="${obj.annieRatings }" pattern="#0.0####"/>
							</td>
							<td align="center">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.initialReleaseDate}"/>
							</td>
							<td align="center">
							<fmt:formatNumber value="${obj.annieRank }" pattern="#0.0####"/>
							</td>
							<c:if test="${ obj.size <1024}">
								<td align="center">${obj.size }KB</td>
							</c:if>
							<c:if test="${ obj.size >1024}">
								<td align="center">
									<fmt:formatNumber type="number" value="${obj.size/1024/1024 }" maxFractionDigits="2"/>MB
								</td>
							</c:if>
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
						<ccgk:pagination paginationVo="${result}"
							contextPath="${ctx }/appannieInfo/list"
							params="${params}" />
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			var columnId = "${param.columnId}";
			var params = {"appName" : $("#appName").val(),"albumId" : $("#albumId").val(),"columnId" : columnId == "" ? 0 : columnId,"raveId":$("#raveId").val()};
			paginationUtils(params);
			 querySecondCategory($("#albumId").val());
		});	
		$("#simulated-distribution").click(function(){
			var columnId =  $("#columnId").val();
			var raveId =  $("#raveId").val();	
			var albumId =  $("#albumId").val();	
			if(albumId==''||albumId==0||albumId==undefined){
				$.messager.alert("提示:","请选择要模拟分发的分类!");
				return;
			}
			if(columnId==''||columnId==0||columnId==undefined){
				$.messager.alert("提示:","请选择要模拟分发的页签!");
				return;
			}
			if(raveId==''||raveId==0||raveId==undefined){
				$.messager.alert("提示:","请选择要模拟分发的国家!");
				return;
			}
			$.messager.confirm('提示:','点击模拟分发后会改变栏目分发的数据,您确认要模拟分发吗?',function(r){   
			    if (r){
			        $.ajax({
			        	url :"${ctx}/appannieInfo/simulatedDistribution?raveId="+raveId+"&columnId="+columnId+"&albumId="+albumId,
			        	type : "POST",
			        	dataType : "json",
			        	success : function(response){
			        		var result = eval("(" + response + ")");
			        		if(result.flag == 2){
			        			$.messager.alert("提示:","appannie分发尚在执行,不能进行模拟分发!");
			    				return;
			        		}else if(result.flag == 3){
			        			$.messager.alert("提示:","当前appannie分发数据不存在,不能进行模拟分发操作!!");						        			
			        		}else if(result.flag == 0){
			        			$.messager.alert("提示:","模拟分发成功!");						        			
			        			window.location.href = "${ctx }/appAlbumColumn/listRes/"+columnId+"?raveId="+raveId;
			        		}else{
			        			$.messager.alert("提示:","模拟分发失败,请联系管理员!");
			    				return;
			        		}
			        	},
			        	error : function (error){
			        		$.messager.alert("提示:","保存失败!");
			        	}
			        });
			    }
			});
		 }); 
		function querySecondCategory(value){
			if(value == 0){
				$("#columnId").html('');
				//$("#categoryId").append("<option value='0'>--all--</option>");
				document.getElementById("columnId").add(new Option("--all--",0));
			}else{
				$.ajax({
					url : "${ctx}/appannieInfo/albumColumn",
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
		function resetQuery(){
			if(confirm("确实要重置吗?")){
				var appName =$("#appName");
				appName.val("");
			}
		}
	</script>
</body>
</html>