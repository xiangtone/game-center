<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'app';
	</script>

</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/app/list" method="post">
						名称：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name)}"/>&nbsp;&nbsp;		
						cp：
							<select name="cpId" id="cpId">
								<option value="">All</option>
								<c:forEach var="cp" items="${cps}">
									<option value="${cp.id}" <c:if test="${param.cpId==cp.id }"> selected="selected"</c:if>>${cp.name}</option>
								</c:forEach>
							</select>
						&nbsp;&nbsp;	
						责任人：<input type="text" name="source" id="source" value="${fn:escapeXml(param.source)}"/>&nbsp;&nbsp;		
						开始日期 :<input id="Q_startTime" name="startTime" readonly="readonly" class="Wdate" style="width:130px;" value="${param.startTime}">--&nbsp;
						结束日期 : <input id="Q_endTime" name="endTime" readonly="readonly" class="Wdate" style="width:130px;" value="${param.endTime}">						
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						<input type="button" class="bigbutsubmit" value="导 出"  id="export_appAlbumOperant" onclick="exportExcel()"/>
						
						<button type="button" class="butsearch" id="te" onclick="testIn()">testIn</button>
						<input type="hidden" value="<%=Constant.APP_TEST_URL %>" id="ur" name="ur">
					</form>
				</div>
				<div class="operateShop">
					<ul>
					    <li><a href="${ctx }/app/add">添加</a></li>
						<li><a id="cmd-delete">删除</a></li>
					</ul>
				</div>
			</div>
		<div class="secmainoutside">
			<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall"/></td>
						<td>
							名称(id)
						</td>
						<td class="name"><b style="color: red;">责任人</b></td>
						<td>
							cp(id)
						</td>
						<td>
							所属一二分类
						</td>
						<td>
							分类级别
						</td>
						<td>
							logo
						</td>
						<td>
							更新数
						</td>
						<td align="center">
							下载数(实)--(虚)
						</td>
						<td align="center">
							浏览数
						</td>						
						<td>
							时间
						</td>
						<td>
							操作
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
							<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
							<td align="center">
								<a href="${obj.id }?page=${result.currentPage}">${fn:escapeXml(obj.name) }(${obj.id })</a>
							</td>
							<td align="center">
								${obj.source }
							</td>
							<td align="center">
								${obj.cp.name }(${obj.cp.id })
							</td>
							<td align="center">
								<c:if test="${obj.category.firstFatherId==2 }">Apps--</c:if>
								<c:if test="${obj.category.firstFatherId==3 }">Games--</c:if>
								${obj.category.firstName}-- ${obj.category.name }
							</td>
							<td align="center">
									<c:if test="${obj.category.level==0 }">
										应用分类
									</c:if>
									<c:if test="${obj.category.level==1 }">
										一级分类
									</c:if>
									<c:if test="${obj.category.level==2 }">
										二级分类
									</c:if>
							</td>
							<td align="center">
								<img alt="大图标" width="50" height="50" src="${path}${obj.bigLogo }">
							</td>
							<td align="center">
								${obj.updateNum }
							</td>
							<td align="center">
								${obj.realDowdload } -- ${obj.initDowdload }
							</td>
							<td align="center">
							${obj.pageOpen }							
							</td>
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>							
							<td align="center">
							<c:if test="${obj.packageName==null }"> 
							   <a href="${ctx }/appFile/list/${obj.id}"><font color="red">apk管理</font></a>
							</c:if>
							<c:if test="${obj.packageName!=null }"> 
							   <a href="${ctx }/appFile/list/${obj.id}">apk管理</a>
							</c:if>	
								<a href="${ctx }/app/add/2/${obj.id}">截图</a>
								<a href="info/${obj.id }">详细</a>
								<a href="${ctx }/appComment/list?appId=${obj.name}&flag=2" target="_BLANK">评论详情</a>
								<c:if test="${obj.packageName!=null }"> 
									<a href="${ctx }/app/appConfig/${obj.id}?apkId=0">配置</a>
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
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/app/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var params = {"name" : $("#name").val(),"cpId":$("#cpId").val(),"source":$("#source").val(),"startTime":$("#Q_startTime").val(),"endTime":$("#Q_endTime").val()};
		paginationUtils(params);
	});
	function checkId(flag){
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
			if(flag==1){
			$.alert("请选择记录");
			}
			return '';
		} else {
			return id;
		}
	}
	
	$("#cmd-delete").click(function() {
		var id = checkId(1);
		if (id != ''){
			$.alert("删除后将不可恢复，确定删除？", function() {
				$.ajax({
					url:"${ctx}/app/delete",
					type:"POST",
					data:{'id':id},
					dataType:"json",
					success:function(data){
						var flag = eval("("+data+")");
						if(flag.success == "true"){
							alert("删除成功！");
							//window.location.reload();
							window.location.href = "list";
						}
					},
					error:function(data){
						alert("删除失败！");
						//window.location.reload();
						window.location.href = "list";
					}
				});
			}, true);
		}
	});
	//导出
	function exportExcel(){
		//导出调用
		var xmlName = "appSelfSupport.xml";
		window.document.location = "${ctx}/app/export2Excel?xmlName="
				+ xmlName
				+ "&name=" + $('#name').val();
	}
	
	function testIn(){
		var id = checkId(0);
		if (id != ''){
			window.open($("#ur").val()+"?appIds="+id); 
		}else{
			if(confirm("未选择应用,是否需要跳转到Testin提交页面?")){
				window.open($("#ur").val()); 
			}
		}
	}
	</script>
</body>
</html>