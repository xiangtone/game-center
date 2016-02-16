<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>区域管理</title>
	<script type="text/javascript">
		var menu_flag = 'region';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
	     区域管理
		<input type="hidden" id="region_id" name="region_id" value="${id}"/>
	</div>
	
	<div class="mainoutside">
		<div class="secmainoutside">
			<div class="mainarea_shop">
				<div class="operateShop">
					<ul>
						<li><a id="cmd-add">添加</a></li>
					
						<li><a href="${ctx }/region/country/${id}">返回</a></li>
					</ul>
				</div>
			</div>
			
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall"/></td>
						<td>
							名称
						</td>
						<td>
							详细
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
								<a href="${obj.id }">${obj.name }</a>
							</td>
							<td align="center">
								${obj.remark }
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/region/countryAdd/${id}" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
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
	
	$("#cmd-add").click(function() {
		var id = checkId();
		var regionId = $("#region_id")[0].value;
		
		if (id != ''){
			$.alert("确定添加？", function() {
				$.get("${ctx }/region/regionAddCountry", {
					id : id,
					regionId : regionId
				}, function(data) {
					if(data =='{false}'){
						alert("添加失败！");
					}else{
						alert("添加成功！");
					}
					window.location.reload();
				});
			}, true);
		}
	});
	</script>
</body>
</html>