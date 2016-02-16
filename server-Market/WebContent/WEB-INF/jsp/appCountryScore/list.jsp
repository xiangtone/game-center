<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'appCountryScore';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">当前位置: <span id="childTitle"></span></div>
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">	
								<form id="applistRes" action="${ctx }/appCountryScore/list" method="post">
									应用名称：<input type="text" name="appName" id="appName" value="${fn:escapeXml(param.appName)}" />&nbsp;&nbsp;	
									isCategory：
									<select id="isCategory" name="isCategory" style="width:40px;">
										<option value="0" <c:if test="${param.isCategory==0}"> selected="selected"</c:if>>否</option>										
										<option value="1" <c:if test="${param.isCategory==1}"> selected="selected"</c:if>>是</option>
									</select>&nbsp;&nbsp;	
									app专题： 
									<select id="albumId" name="albumId" onchange="querySecondCategory(this.value)">
										<option value="1" <c:if test="${param.albumId==1}"> selected="selected"</c:if>>Homes</option>										
										<option value="2" <c:if test="${param.albumId==2}"> selected="selected"</c:if>>Apps</option>
										<option value="3" <c:if test="${param.albumId==3}"> selected="selected"</c:if>>Games</option>		
									</select>&nbsp;&nbsp;					
								    app页签：
								    <select id="columnId" name="columnId" >
								    
									</select>&nbsp;&nbsp;	
									国家：
									<select name="raveId" id="raveId" onchange="selectQuery(this.value)">							
										<c:forEach var="country" items="${countrys}">
											<option value="${country.id}" <c:if test="${country.id==param.raveId}"> selected="selected"</c:if>>${country.name}(${country.nameCn})</option>
										</c:forEach>
									</select>&nbsp;&nbsp;
									<button type="submit" class="butsearch" id="btn-search">查询</button>
									<input type="button" class="butsearch" value="重置" onclick="resetQuery()"/>								
									&nbsp;&nbsp;
									<input type="button" class="bigbutsubmit" value="配 置"  id="butsubmit_id"/>
									&nbsp;&nbsp;
									<input type="button" class="bigbutsubmit" value="复制排行"  id="copyRanking_id"/>
								</form>
				</div>
				<div class="operateShop">
					<ul>
					    <!-- <li><a href="${ctx }/appCountryScore/add">添加</a></li> -->
						<li><a id="cmd-delete">删除</a></li>
					</ul>
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
						<td align="center">专题</td>
						<td align="center">页签(英文)</td>
						<td align="center">国家名(英文)</td>						
						<td align="center">图标</td>
						<td align="center">得分</td>
						<td align="center">衰减周期</td>
						<td align="center">开始生效日期</td>	
						<td align="center">上线天数</td>	
						<td align="center">最终得分</td>	
						<td align="center">是否启用</td>					
						<td align="center">创建时间</td>
						<td align="center">isCategory</td>
						<td align="center">操作</td>
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
							<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>	
							<td align="center"><a href="${obj.id }">${obj.appName }(${obj.appInfo.id })</a></td>
							<td align="center">${obj.appInfo.category.categoryCn }(${obj.appInfo.category.name })</td>
							<td align="center">${obj.albumName }</td>
							<td align="center">${obj.columnNameCn}(${obj.columnName})</td>
							<td align="center">${obj.country.nameCn}(${obj.country.name})</td>					
							<td align="center"><img src="${path}${obj.appInfo.bigLogo}" width="50" height="50"></td>												
							<td align="center">
							<fmt:formatNumber value="${obj.score }" pattern="#"/>
							</td>
							<td align="center">
							<fmt:formatNumber value="${obj.fadingDay }" pattern="#"/>
							</td>
							<td align="center">
							<fmt:formatDate pattern="yyyy-MM-dd" value="${obj.startDate}"/>
							</td>
							<td align="center">
								${obj.days }
							</td>
							<td align="center">
							<fmt:formatNumber value="${obj.enforceScore }" pattern="#0.00##"/>
							</td>
							<td align="center">
							<c:if test="${obj.state ==true}">是</c:if>
							<c:if test="${obj.state ==false}">否</c:if>
							</td>	
							<td align="center">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>
							<td align="center">
								<c:if test="${obj.isCategory == 0}">否</c:if>
								<c:if test="${obj.isCategory == 1}">是</c:if>
							</td>
							<td align="center">
								<c:if test="${obj.state ==false}"><a href="updateState/${obj.id }/1?raveId=${obj.country.id}" style="color: green;font-size: 14px"><b>启用</b></a></c:if>
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
							contextPath="${ctx }/appCountryScore/list"
							params="${params}" />
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			querySecondCategory($("#albumId").val());
			var params = {"appName" : $("#appName").val(),"raveId":$("#raveId").val(),"albumId":$("#albumId").val(),"columnId":$("#columnId").val()};
			paginationUtils(params);
		});	
		
		//根据APP分类联动APP页签
		function querySecondCategory(value){
			$.ajax({
				url : "${ctx}/appAlbumStatistics/albumColumn",
				type : "POST",
				dataType : "json",
				async:false,
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
		
		function resetQuery(){
			if(confirm("确实要重置吗?")){
				var appName =$("#appName");
				appName.val("");
// 				$("#albumId").val('');
// 				$("#columnId").val('');
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
		
		$("#cmd-delete").click(function() {
			var raveId = $("#raveId").val();
			var id = checkId();
			if (id != ''){
				$.alert("删除后将不可恢复，确定删除？", function() {
					$.get("delete", {
						id : id
					}, function(data) {
						$.alert("删除成功！");
						//window.location.reload();
						var albumId = $("#albumId").val();
						var columnId = $("#columnId").val();
						window.location.href = "${ctx }/appCountryScore/list?raveId="+raveId+"&albumId="+albumId+"&columnId="+columnId;
					});
				}, true);
			}
		});
		
		//配置
		$("#butsubmit_id").click(function() {
			var raveId = $("#raveId").val();
			var albumId = $("#albumId").val();
			var columnId = $("#columnId").val();
			window.location.href = "${ctx }/appCountryScore/config?raveId="+raveId+"&albumId="+albumId+"&columnId="+columnId;
		});
		$("#copyRanking_id").click(function() {
			$.alert("复制强制排行后将不可恢复，确定执行？", function() {
			var raveId = $("#raveId").val();
			$.ajax({
				url : "${ctx}/appCountryScore/copyRanking",
				type : "POST",
				dataType : "json",
				data :{"raveId":raveId},
				success: function(response){
	        		var data = eval("(" + response + ")");
	        		if(data.flag==0){
	        			$.alert("复制强制排行成功！", function() {
	        		    	window.location.href = "${ctx }/appCountryScore/list?raveId="+raveId;	        				
	        			});
	        		}else if(data.flag==2){
	        			$.alert("当前选定目标国家为默认国家！");
	        		}else{
	        			$.alert("复制强制排行失败！");
	        		}
	            }
	        });
			}, true);
		});
		function selectQuery(id){
			var albumId = $("#albumId").val();
			var columnId = $("#columnId").val();
			window.location.href ="${ctx }/appCountryScore/list?raveId="+id+"&albumId="+albumId+"&columnId="+columnId;
		}
	</script>
</body>
</html>