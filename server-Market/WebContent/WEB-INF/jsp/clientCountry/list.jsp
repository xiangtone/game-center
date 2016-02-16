<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'clientCountry';
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
		<div class="secmainoutside">
			<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx }/clientCountry/list" method="post">
						国家名：<input type="text" name="country" id="country" value="${fn:escapeXml(param.country)}"/>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
					    <li><a href="${ctx }/clientCountry/add">添加</a></li>
					    <li><a id="cmd-delete">删除</a></li> 
					</ul>
				</div>
			</div>
			<div class="border">
			<table class="mainlist" width="100%">
				<thead>
					<tr>
					<td width="2%"><input type="checkbox" class="checkall"/></td>
						<td align="center">
							国家中文名
						</td>
						<td align="center">
							国家英文名
						</td>
						<td align="center">
							ICO
						</td>
						<td align="center">
							创建日期
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="6" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}" varStatus="i">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<td align="center"><input type="checkbox" name="recordId" value="${obj.countryCn}"/></td>
						<td align="center">
							<a href="edit?countryCn=${obj.countryCn }&currentPage=${result.currentPage}">${fn:escapeXml(obj.countryCn)}</a>
						</td>
						<td align="center">
						${obj.country}
						</td>
						<td align="center">
						<c:if test="${!empty obj.iconUrl }"><img alt="大图标" width="50" height="50" src="${path}${obj.iconUrl }"></c:if>
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
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/clientCountry/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
			
		</div>
	</div>
	
	<script type="text/javascript">
	function check(va){
	$.ajax({
		url:"${ctx}/clientCountry/"+va,
		type:"POST",
		data:{'countryCn':countryCn,'country':country},
		dataType:"json",
		success:function(response){
			var data = eval("("+response+")");
			if(data.flag == "0"){
				alert("修改成功！");
				window.location.href = "list";
			}else if(data.flag == "2"){
				 alert("请输入"+countryCn+"的英文名称！");
				window.location.href = "list";
			}else{
				alert("修改失败！");
				window.location.href = "list";
			}
		},
		error:function(data){
			alert("修改失败！");
			window.location.href = "list";
		}
	});
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
	</script>
</body>
</html>