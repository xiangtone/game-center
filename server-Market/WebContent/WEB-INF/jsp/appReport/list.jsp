<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appStatistics';
	</script>

</head>
<body>
<div class="boeder">
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	<div class="mainoutside" >
		<div class="mainarea_shop" >
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/appReport/distribute/list" method="post">
						app名字或appId：<input type="text" class="text1" name="appnameid" id="appnameid" value="${fn:escapeXml(param.appnameid)}"/>&nbsp;&nbsp;
						包名：<input type="text" class="text1" name="packageName" id="packageName" size="40" value="${fn:escapeXml(param.packageName)}"/>&nbsp;&nbsp;
						&nbsp;&nbsp;<label>一级分类:</label> 
						<select id="category_parent" name="category_parent" onchange="querySecondCategory(this.value)">
							<option value="0">全部</option>
								<option value="2" <c:if test="${2 == param.category_parent}">selected='selected'</c:if>>Apps</option>
								<option value="3" <c:if test="${3 == param.category_parent}">selected='selected'</c:if>>Games</option>
						</select>
						&nbsp;&nbsp;<label>二级分类:</label>
						<select id="categoryId"  name="categoryId" >
							<option value="0">--all--</option>
						</select>
							&nbsp;&nbsp;<label>国家：</label>
							<select name="raveId" id="raveId">
								<!-- <option value="0" >--all--</option> -->
								<c:forEach var="country" items="${countrys}">
									<option value="${country.id}" <c:if test="${country.id==param.raveId}"> selected="selected"</c:if>>${country.name}(${country.nameCn})</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search" >查询</button>
						<button type="button" class="butsearch"  onclick="resetQuery(this)">重置</button>
					<input type="button" class="bigbutsubmit" value="导 出"  id="export_appStatistics" onclick="exportExcel()"/>
					</form>
							
				</div>
			</div>
		<div class="secmainoutside">
			<div class="border">
			<div style=" width: 100%;overflow: scroll;height: 501px;">
			<table class="mainlist">
				<thead>
					<tr>
						<td align="center">
							Appid
						</td>
						<td align="center">
							appName
						</td>
						<td align="center">
							country
						</td>
						<td align="center">
							categoryName
						</td>
						<td align="center">
							initDowdload
						</td>
						<td align="center">
							realDowdload
						</td>
						<td align="center">
							Pageviews
						</td>
						<td align="center">
							updateNum
						</td>
						<td align="center">
							versionName
						</td>
						<td align="center">
							stars
						</td>
						<td align="center">
							fileSize
						</td>
						<td align="center">						
							InitialTime
						</td>
						<td align="center">
							UpdateTime
						</td>
						<td align="center">
							Home recommend Highest Rank
						</td>
						<td align="center">
							Home recommend Lowest Rank
						</td>
						<td align="center">
							Home new Highest Rank
						</td>
						<td align="center">
							Home new Lowest Rank
						</td>
						<td align="center">
							Home top Highest Rank
						</td>
						<td align="center">
							Home top Lowest Rank
						</td>
						<td align="center">
							Home POPULAR Highest Rank
						</td>
						<td align="center">
							Home POPULAR Lowest Rank
						</td>
						<td align="center">
							App HOT Highest Rank
						</td>
						<td align="center">
							App HOT Lowest Rank
						</td>
						<td align="center">
							App TOP Highest Rank
						</td>
						<td align="center">
							App TOP Lowest Rank
						</td>
						<td align="center">
							App NEW Highest Rank
						</td>
						<td align="center">
							App NEW Lowest Rank
						</td>
						<td align="center">
							Game HOT Highest Rank
						</td>
						<td align="center">
							Game HOT Lowest Rank
						</td>
						<td align="center">
							Game TOP Highest Rank
						</td>
						<td align="center">
							Game TOP Lowest Rank
						</td>
						<td align="center">
							Game NEW Highest Rank
						</td>
						<td align="center">
							Game NEW Lowest Rank
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
							<td align="center">
								${obj.appId }
							</td>
							<td align="center">
								${obj.appName }
							</td>
							<td align="center">
								${obj.country.name }
							</td>
							<td align="center">
								${obj.categoryName }
							</td>
							<td align="center">
								${obj.initDowdload }
							</td>
							<td align="center">
								${obj.realDowdload }
							</td>
							<td align="center">
								${obj.pageOpen }
							</td>
							<td align="center">
								${obj.updateNum }
							</td>
							<td align="center">
								${obj.versionName }
							</td>
							<td align="center">
								${obj.stars }
							</td>
							<td align="center">
								${obj.fileSize }
							</td>
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.updateTime}"/>
							</td>
							<td align="center">
								${obj.hrHigerank }
							</td>
							<td align="center">
								${obj.hrLowrank }
							</td>
							<td align="center">
								${obj.hnHigerank }
							</td>
							<td align="center">
								${obj.hnLowrank }
							</td>
							<td align="center">
								${obj.htHigerank }
							</td>
							<td align="center">
								${obj.htLowrank }
							</td>
							<td align="center">
								${obj.hpHigerank }
							</td>
							<td align="center">
								${obj.hpLowrank }
							</td>
							<td align="center">
								${obj.ahHigerank }
							</td>
							<td align="center">
								${obj.ahLowrank }
							</td>
							<td align="center">
								${obj.atHigerank }
							</td>
							<td align="center">
								${obj.atLowrank }
							</td>
							<td align="center">
								${obj.anHigerank }
							</td>
							<td align="center">
								${obj.anLowrank }
							</td>
							<td align="center">
								${obj.ghHigerank }
							</td>
							<td align="center">
								${obj.ghLowrank }
							</td>
							<td align="center">
								${obj.gtHigerank }
							</td>
							<td align="center">
								${obj.gtLowrank }
							</td>
							<td align="center">
								${obj.gnHigerank }
							</td>
							<td align="center">
								${obj.gnLowrank }
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/appReport/distribute/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			var categoryParent = "${param.category_parent}";
			if(null != categoryParent && categoryParent != "" ){
				querySecondCategory(categoryParent);
			}
			var categoryId = "${param.categoryId}";
			var raveId = "${param.raveId}";

			if(raveId == null || raveId == ''){
				$("#raveId option[value=true]").attr("selected","selected");
			}else{
				$("#raveId option[value='"+ raveId +"']").attr("selected","selected");
			}
			var params = {"appnameid" : $("#appnameid").val(),"packageName" : $("#packageName").val(),"state" : $("#state").val(),"categoryId" : categoryId == "" ? 0 : categoryId,"category_parent" : $("#category_parent").val(),"raveId" : $("#raveId").val()};
			paginationUtils(params);
		});
		function querySecondCategory(value){
			if(value == 0){
				$("#categoryId").html('');
				document.getElementById("categoryId").add(new Option("--all--",0));
			}else{
				$.ajax({
					url : "${ctx}/communal/secondCategory",
					type : "POST",
					dataType : "json",
					data : {"id" : value},
					success : function (data){
						var result = eval("(" + data + ")");
						if(result.success == 1){
							$("#categoryId").html('');
							$("#categoryId").append(result.option);
							var categoryId = "${param.categoryId}";
							if("" != categoryId && categoryId != 0){
								$("#categoryId").val(categoryId);
							}
						}else{
							$("#categoryId").html('');
							$("#categoryId").append("<option value='0'>--all--</option>");
						
						}
					},
					error : function (error){
						$.messager.alert("提示:","异常出现未知异常!");
					}
				});
			}
		}
		//导出
		function exportExcel(){
			//导出调用
			var xmlName = "appStatistics.xml";
			window.document.location = "${ctx}/appReport/export2Excel?xmlName="
					+ xmlName
					+ "&raveId=" + $('#raveId').val()
					+ "&category_parent=" + $('#category_parent').val()
					+ "&categoryId=" + $('#categoryId').val()
					+ "&packageName=" + $('#packageName').val()
					+ "&appnameid=" + $('#appnameid').val();
		}
		function resetQuery(){
			if(confirm("确实要重置吗?")){
			    $("#category_parent").val("0");
			    $("#packageName").val("");
			    $("#categoryId").val("0");
			    $("#raveId").val("1");
			    $("#appnameid").val("");
			}
		}
	</script>
	
</body>
</html>