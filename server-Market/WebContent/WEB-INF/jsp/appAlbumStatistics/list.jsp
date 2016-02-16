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

	<div class="sitemap" id="sitemap">当前位置: 各维度统计</div>
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<table>
						<tr>
							<td>
								<form id="appAlbumStatisticsList"
									action="${ctx }/appAlbumStatistics/list"
									method="post">
									应用名称 : <input type="text" name="appName" id="appName" value="${fn:escapeXml(param.appName)}" />&nbsp;&nbsp;
									专题: <select id="albumId" name="albumId"  onchange="querySecondCategory(this.value)">
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
									<input id="callback" name="callback" class="bigbutsubmit" style="margin-left: 50px" type="button" value="返回" >
																								
									
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
						<td width="2%"><input type="checkbox" class="checkall" /></td>
						<td align="center">专辑分类</td>
						<td align="center">专辑(英文)</td>
						<td align="center">应用分类(英文)</td>
						<td align="center">应用名称(appId)</td>
						<td align="center">logo</td>
						<td align="center">downChange<br/>(realDownload/pageOpen)</td>
						<td align="center">days</td>
						<td align="center">searchAppRank<br/>/searchAppCount</td>
						<td align="center">downRank<br/>/downAppCount</td>
						<td align="center">enforceScore</td>
						<td align="center">starsReal</td>
						<td align="center">annieExtent</td>
						<td align="center">finalScore</td>
						<td align="center">createTime</td>
						
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="14" align="center">没有符合要求的数据！</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						
							<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)" background="red">
								<td align="center"><input type="checkbox" name="recordId"
									value="${obj.appInfo.id}" /></td>
								<td align="center">${obj.appAlbum.name }</td>
								<td align="center">${obj.appAlbumColumn.nameCn }(${obj.appAlbumColumn.name })</td>
								<td align="center">${obj.appInfo.category.categoryCn }(${obj.appInfo.category.name })</td>
								<c:if test="${obj.appFile ==null}">
								<td align="center"><font color="red">${obj.appName }(${obj.appInfo.id })(无APK文件)</font></td>
								</c:if>
								<c:if test="${obj.appFile !=null}">
								<td align="center">${obj.appName }(${obj.appInfo.id })</td>
								</c:if>
								<td align="center"><img src="${path}${obj.appInfo.bigLogo}" width="50" height="50"></td>
								<td align="center">${obj.dowdChange }(${obj.realDowdload }/${obj.pageOpen })</td>
								<td align="center">${obj.days }</td>
								<td align="center">${obj.searchAppRank }/${obj.searchAppCount}</td>	
								<td align="center">${obj.dowdRank}/${obj.dowdAppCount}</td>							
								<td align="center">${obj.enforceScore }</td>							
								<td align="center">${obj.starsReal }</td>
								<td align="center">${obj.annieExtent }</td>
								<td align="center">${obj.finalScore }</td>
								<td align="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/></td>
	
							</tr>						
					</c:forEach>
				</tbody>
			</table>
		</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}"
							contextPath="${ctx}/appAlbumStatistics/list"
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
			$("#callback").click(function() {	
				$("#appAlbumStatisticsList").attr("action","${ctx}/appAlbumStatistics/show");
				$("#appAlbumStatisticsList").submit();
			/**	
			   var albumId =$("#albumId").val();
				var appName =$("#appName").val();
				var raveId =$("#raveId").val();
				var columnId =$("#columnId").val();				
				window.location.href = "${ctx}/appAlbumStatistics/show?albumId="+albumId
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
	</script>
</body>
</html>