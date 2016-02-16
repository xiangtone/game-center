<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>区域</title>
	<script type="text/javascript">
		var menu_flag = 'region';
		
		var menu_flag = 'inputorder';
		$(function(){
			$("input[id$=Date]").attr("readonly",true);
			$("input[id$=Date]").datepicker({changeYear:true,changeMonth:true});
		});
	
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: 区域
	</div>
	
	<div class="mainoutside">
		<div class="secmainoutside">
			<div class="mainarea_shop">
				
				<div class="operateShop">
					<ul>
						<li><a href="${ctx }/region/countryAdd/${id}">添加</a></li>
						<li><a id="cmd-delete">删除</a></li>
					</ul>
				</div>
			</div>
			
			<table class="mainlist">
				
				<tbody>
				  <c:forEach var="obj" items="${result}"  varStatus="statu">
							
								<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
								<td align="center">
									<a href="${ctx }/country/${obj.id }">${obj.name }</a>
								</td>
							<c:if test="${statu.count%4==0}">
							<tr>
							</tr>
							</c:if>
					</c:forEach>
						
				</tbody>
			</table>
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
				$.get("${ctx }/region/countryDelete", {
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