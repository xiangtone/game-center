<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>国家</title>
	<script type="text/javascript">
		var menu_flag = 'country';
		
		var menu_flag = 'inputorder';
		$(function(){
			$("input[id$=Date]").attr("readonly",true);
			$("input[id$=Date]").datepicker({changeYear:true,changeMonth:true});
		});
	
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: 国家
	</div>
	
	<div class="mainoutside">
		<div class="secmainoutside">
			<div class="mainarea_shop">
				
				<div class="operateShop">
					<ul>
						<li><a href="${ctx }/country/add">添加</a></li>
					</ul>
				</div>
			</div>
			
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall"/></td>
						<td>
							缩写
						</td>
						<td>
							名称
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
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/country/list" params="${params}"/>
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
	
	$("#cmd-delete").click(function() {
		var id = checkId();
		if (id != ''){
			$.alert("删除后将不可恢复，确定删除？", function() {
				$.get("delete", {
					id : id
				}, function(data) {
					$.alert("删除成功！");
					window.location.reload();
				});
			}, true);
		}
	});
	
	</script>
</body>
</html>