<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'appannieCountryRank';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">当前位置: <span id="childTitle"></span>&nbsp;&nbsp;<span>(注明:appannie排行间断是因为该排名对应的应用名与之前的重复)</span></div>
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">		
								<form id="applistRes" action="${ctx }/appannieCountryRank/list" method="post">
									应用名称：<input type="text" name="appName" id="appName" value="${fn:escapeXml(param.appName)}" />&nbsp;&nbsp;
									appannie分类 :<select id="annieType" name="annieType">
										<option value="0" <c:if test="${param.annieType==0}"> selected="selected"</c:if>>--All--</option>									
									    <option value="1" <c:if test="${param.annieType==1}"> selected="selected"</c:if>>Hot</option>
										<option value="2" <c:if test="${param.annieType==2}"> selected="selected"</c:if>>New</option>
									</select>&nbsp;&nbsp;	
									app分类 :<select id="albumId" name="albumId">
									    <option value="1" <c:if test="${param.albumId==1}"> selected="selected"</c:if>>Homes</option>
										<option value="2" <c:if test="${param.albumId==2}"> selected="selected"</c:if>>Apps</option>
										<option value="3" <c:if test="${param.albumId==3}"> selected="selected"</c:if>>Games</option>		
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
									<!-- <button type="button" class="butsearch" id="btn-do_config">强制排行</button> -->							
									&nbsp;&nbsp;
								</form>

				</div>
			</div>
		<div class="secmainoutside">
		<div class="border">
			<table class="mainlist" width="100%">
				<thead>
					<tr>
					    <td width="2%"><input type="checkbox" class="checkall"/></td>						
						<td align="center">应用名(appId)</td>
						<td align="center">分类名(英文)</td>
						<td align="center">页签</td>
						<td align="center">logo</td>
						<td align="center">安装量</td>
						<td align="center">评分</td>	
						<td align="center">初次上线时间</td>					
						<td align="center">appannie排行</td>
						<td align="center">排行升降</td>	
						<td align="center">appannie分类</td>
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
						    <c:if test="${obj.appInfo!=null and obj.initialReleaseDate!=null}">
								<td align="center"><input type="checkbox" name="recordId" value="${obj.id }"/></td>							
							</c:if>
						    <c:if test="${obj.appInfo==null or obj.initialReleaseDate==null}">
								<td align="center"></td>	
							</c:if>
							<c:if test="${obj.appInfo!=null}">
								<td align="center"><a href="${obj.id }">${obj.appName }(${obj.appInfo.id })</a></td>
								<td align="center">${obj.appInfo.category.categoryCn }(${obj.appInfo.category.name })</td>
							</c:if>
							<c:if test="${obj.appInfo==null}">
								<td align="center"><a href="${obj.id }"><font color="red">${obj.appName }(新应用)</font></a></td>
								<td align="center"></td>
							</c:if>
							<td align="center">${obj.appAlbum.name }</td>
							<c:if test="${obj.appInfo!=null}">
								<td align="center"><img src="${path}${obj.appInfo.bigLogo}" width="50" height="50"></td>							
							</c:if>
							<c:if test="${obj.appInfo==null}">
							<td align="center" style="height:50px"></td>
							</c:if>
							<c:if test="${obj.annieInstallTotal>0.0}">
								<td align="center">
								<fmt:formatNumber value="${obj.annieInstallTotal }" pattern="#"/>
								</td>
							</c:if>
							<c:if test="${obj.annieInstallTotal<=0.0}">
								<td align="center"><font color="red">无安装量</font></td>
							</c:if>
							<c:if test="${obj.annieRatings>0.0}">
								<td align="center">
								<fmt:formatNumber value="${obj.annieRatings }" pattern="#0.0####"/>
								</td>
							</c:if>
							<c:if test="${obj.annieRatings<=0.0}">
								<td align="center"><font color="red">无评分</font></td>
							</c:if>
							<c:if test="${obj.initialReleaseDate!=null}">
								<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.initialReleaseDate}"/>
								</td>
							</c:if>
							<c:if test="${obj.initialReleaseDate==null}">
								<td align="center"><font color="red">无初次上线时间</font></td>
							</c:if>
							<td align="center">
							<fmt:formatNumber value="${obj.annieRank }" pattern="#"/>
							</td>
							<td align="center">
							${obj.annieExtent }
							</td>	
							<td align="center">
							<c:if test="${obj.annieType==1}">Hot</c:if>
							<c:if test="${obj.annieType==2}">New</c:if>
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
						<ccgk:pagination paginationVo="${result}"
							contextPath="${ctx }/appannieCountryRank/list"
							params="${params}" />
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			var params = {"appName" : $("#appName").val(),"albumId" : $("#albumId").val(),"raveId":$("#raveId").val(),"annieType":$("#annieType").val()};
			paginationUtils(params);
		});	
		function resetQuery(){
			if(confirm("确实要重置吗?")){
				var appName =$("#appName");
				appName.val("");
			}
		}
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
		
		$("#btn-do_config").click(function() {
			var id = checkId();
			if (id != ''){
				$.alert("执行强制排名后将不可恢复，确定进行强制排名？", function() {
					$.get("doConfig", {
						id : id
					}, function(data) {
						$.alert("强制排名成功！");
						//window.location.reload();
						window.location.href = "${ctx }/appCountryScore/doConfig";
					});
				}, true);
			}
		});
		$("#btn-do_config").click(function(){
			var id = checkId();
			if (id != ''){
				$.alert("执行强制排名后将不可恢复，确定进行强制排名？", function() {
					$.ajax({
						url : "${ctx }/appannieCountryRank/doConfig?id="+id,
						type : "POST",
						dataType : "json",
						success : function(response) {							 
							var result = eval("("+response+")");
							if(result.flag == 0){
								var errorTotal = result.errorTotal; 
								if(errorTotal==0){
									$.alert("强制排名成功！", function() {
										window.location.href = "${ctx }/appannieCountryRank/list";
									});
								}else{
									$.alert("强制排名成功！但其中有"+errorTotal+"条强制排行失败", function() {
										window.location.href = "${ctx }/appannieCountryRank/list";
									});
								}
							}else{
								$.alert("强制排名失败！", function() {
									window.location.href = "${ctx }/appannieCountryRank/list";
								});
							}
						},
			        	error : function (error){
			        		return false;
			        	}
					});
		   		}, true);
			 }
	  	}); 
	</script>
</body>
</html>