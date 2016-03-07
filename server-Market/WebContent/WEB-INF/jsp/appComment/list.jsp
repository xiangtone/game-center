<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appComment';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="list" method="post">
						应用名:<input type="text" name="appId" id="appId" value="${fn:escapeXml(appId)}">
						<input type="hidden" id="flag" name="flag" value="1">
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
					    <!-- <li><a href="${ctx }/appComment/add">添加</a></li> -->
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
							编号
						</td>
						<td>
							应用名称(id)
						</td>
						<td>
							用户名
						</td>
						<td>
							用户id
						</td>
						<td>
							星级
						</td>
						<td>
							内容
						</td>
						<td>
							评论时间
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
								<a href="${ctx}/appComment/${obj.id }">
									${obj.id }
								</a>
							</td>
							<td align="center">
								${fn:escapeXml(obj.name)}(${obj.appId })
							</td>
							<td align="center">
								${obj.userName }
							</td>
							<td align="center">
								${obj.userId }
							</td>
							<td align="center">
								${obj.stars }
							</td>
							<td align="center">
								<c:if test="${obj.content.length()>10 }" >
									<a title="${fn:escapeXml(obj.content)}" onclick="open1(this.title)" >详情</a>
								</c:if>
								<c:if test="${obj.content.length()<=10 }" >${obj.content}</c:if>
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
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/appComment/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var appId = "${param.appId}";
		if(appId == null || appId == ''){
			$("option[value=true]").attr("selected","selected");
		}else{
			$("option[value='"+ appId +"']").attr("selected","selected");
		}
		var params = {'appId' : appId};
		//调用分页参数处理方法
		paginationUtils(params);
	});
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
		var id = checkId();
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
	
	function open1(val){
		alert(val);
	}
	</script>
</body>
</html>