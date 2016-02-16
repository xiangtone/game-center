<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'pay';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx }/pay/list" method="post">
						面值：
						<select name="mogValue" id="mogValue">
							<option value="0">全部</option>
							<option value="20000">20V</option>
							<option value="50000">50V</option>
							<option value="100000">100V</option>
							<option value="200000">200V</option>
							<option value="500000">500V</option>
							<option value="1000000">1000V</option>
						</select>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
						<li><a href="${ctx }/pay/add">添加</a></li>
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
							编号
						</td>
						<td align="center">
							点卡面额
						</td>
						<td align="center">
							赠送规则
						</td>
						<td align="center">
							充值赠送a币
						</td>
						<td align="center">
							备注
						</td>
						<td align="center">
							状态
						</td>
						
					</tr>
				</thead>
				<tbody>
					<c:if test="${pays.recordCount == 0}">
						<tr>
							<td colspan="7" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${pays.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
						<td align="center">
							${obj.id }
						</td>
						<td align="center">
							<a href="${obj.id }"><fmt:parseNumber type="number">${obj.mogValue*0.001}</fmt:parseNumber>V</a>
						</td>
						<td align="center">
							<c:if test="${obj.channelId==0 && obj.cpId==0 && obj.appId==0}">全局</c:if>
							<c:if test="${obj.channelId!=0}">channelId=${obj.channelId}</c:if>
							<c:if test="${obj.cpId!=0}">cpId=${obj.cpId}</c:if>
							<c:if test="${obj.appId!=0}">appId=${obj.appId}</c:if>
						</td>
						<td align="center">
							${obj.aValuePresent }
						</td>
						<td align="center">
							${obj.remark }
						</td>
						<td align="center">
						<c:if test="${obj.state==true }">是</c:if>
						<c:if test="${obj.state==false }">否</c:if>
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${cps.recordCount != 0}">
						<ccgk:pagination paginationVo="${pays}" contextPath="${ctx}/pay/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var mogValue = "${param.mogValue}";
		if(mogValue == null || mogValue == ''){
			$("option[value=true]").attr("selected","selected");
		}else{
			$("option[value='"+ mogValue +"']").attr("selected","selected");
		}
		var params = {"mogValue" : mogValue};
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
	</script>
</body>
</html>