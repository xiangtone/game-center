<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'cp';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx}/cp/list" method="post">
						cp名称：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name) }"/>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
						<li><a href="${ctx }/cp/add">添加</a></li>
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
						<td align="center">
							cpid
						</td>
						<td align="center">
							cp名
						</td>
						<td align="center">
							描述
						</td>
						<td align="center">
							是否有效
						</td>
						<td align="center">
							支付方式列表
						</td>
						<td align="center">
							操作
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${cps.recordCount == 0}">
						<tr>
							<td colspan="7" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${cps.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
						<td align="center">
							<a href="${obj.id }">${obj.id }</a>
						</td>
						<td align="center">
							${fn:escapeXml(obj.name)}
						</td>
						<td align="center" style="word-break:break-all; word-wrap:break-word;" title="${fn:escapeXml(obj.description) }">
							<ff:formatPro fieldValue="${obj.description}" type="1" len="45"/>
						</td>
						<td align="center">
							${obj.state }
						</td>
						<td align="center">
							${obj.payWay }
						</td>
						<td align="center">
							<a href="cpInfo/${obj.id }">查看详细</a>
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${cps.recordCount != 0}">
						<ccgk:pagination paginationVo="${cps}" contextPath="${ctx}/cp/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var params = {"name" : $("#name").val()};
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
				}, function(response) {
					var data = eval("("+response+")");
					if(data.success=="true"){
						$.alert("删除成功！");
						//window.location.reload();
						window.location.href = "list";
					}else{
						$.alert("删除失败,以下("+data.success+")cp中已关联其他应用,请重新选择！");
					}
				});
			}, true);
		}
	});
	</script>
</body>
</html>