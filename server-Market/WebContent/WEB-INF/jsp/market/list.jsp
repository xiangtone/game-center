<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'market';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">	
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/market/list" method="post">
						名称：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name)}"/>&nbsp;&nbsp;
						状态：
						<select id="state" name="state">
							<option value="true">是</option>
							<option value="false">否</option>
						</select>
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						<button type="button" class="butsearch" id="te" onclick="testIn()">testIn</button>
						<input type="hidden" value="<%=Constant.APP_TEST_URL %>" id="ur" name="ur">
					</form>
				</div>
				<div class="operateShop">
					<ul>
					    <!-- <li><a href="${ctx }/market/add">添加</a></li> 
						<li><a id="cmd-delete">删除</a></li> --> 
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
							名称
						</td>
						<td>
							所属渠道(id)
						</td>
						<td>
							星级
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
							<td colspan="7" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
							<td align="center">
								<a href="${obj.id }">${fn:escapeXml(obj.name)}</a>
							</td>
							<td align="center">
								${obj.channel.name }(${obj.channel.id })
							</td>
							<td align="center">
								${obj.stars }
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
								   <a href="${ctx }/markAppFile/list/${obj.id}"><font color="red">apk管理</font></a>
								</c:if>
								<c:if test="${obj.packageName!=null }"> 
								   <a href="${ctx }/markAppFile/list/${obj.id}">apk管理</a>
								</c:if>	
								<a href="${ctx }/market/add/2/${obj.id}">截图</a>
								<a href="info/${obj.id }">详细</a>
								<a href="${ctx }/appComment/list?appId=${obj.name}&flag=2" target="_BLANK">评论详情</a>
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
		var state = "${param.state}";
		if(state == null || state == ''){
			$("option[value=true]").attr("selected","selected");
		}else{
			$("option[value='"+ state +"']").attr("selected","selected");
		}
		var params = {"name" : $("#name").val(),"state" : $("#state").val()};
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
				$.get("delete", {
					id : id
				}, function(data) {
					$.alert("删除成功！");
					//window.location.reload();
					window.location.href = "list";
				});
			}, true);
		}
	});
	
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