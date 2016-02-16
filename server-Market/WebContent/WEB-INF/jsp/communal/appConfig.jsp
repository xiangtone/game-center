<%@page import="com.mas.rave.util.RandNum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>自营APP管理</title>
	<script type="text/javascript">
		var menu_flag = 'communal';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span> -&gt; 全部APP栏目配置
	</div>
			<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/communal/appConfig/${appInfo.id}" method="post">
					应用名:&nbsp;&nbsp;<a>${appInfo.name }</a>&nbsp;&nbsp;&nbsp;&nbsp;
						归属国家:
						<select id="apkId" name="apkId" onchange="change(this.value)">
							<c:forEach var="file1" items="${files}">
								<option value="${file1.id}" <c:if test="${file.raveId==file1.raveId }">selected="selected"</c:if>>${file1.nameEn }(${file1.raveNames})</option>
							</c:forEach>
						</select>
						分发国家列表:
						<select id="countyId" name="countyId" onchange="change1(this.value)">
							<c:if test="${file.raveId==1 }">
								<c:forEach var="county" items="${countys}">
									<c:if test="${raveId!=null }">
										<option value="${county.id}" <c:if test="${raveId==county.id+'' }">selected="selected"</c:if>>${county.name }(${county.nameCn})</option>
									</c:if>
									<c:if test="${raveId==null }">
										<option value="${county.id}" <c:if test="${file.raveId==county.id }">selected="selected"</c:if>>${county.name }(${county.nameCn})</option>
									</c:if>
								</c:forEach>
							</c:if>
							<c:if test="${file.raveId!=1 }">
								<option value="${raveId}">${file.nameEn }(${file.raveNames})</option>
							</c:if>
						</select>
						 <input type="button" class="bigbutsubmit" value="提交" id="btn" />
						 <input type="button" class="bigbutsubmit" value="返回" onclick="javascript:window.location.href='${ctx}/communal/list';" />
					</form>
				</div>
		<div id="secmainoutside">
			<div class="title">自营APP栏目配置 </div>
			<form id="configForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td width="2%" align="center"><input type="checkbox" class="checkall"/></td>
						<td align="center">分类</td>
						<td align="center">页签(英文)</td>
						</tr>
						<tr>
						<c:forEach items="${result }" var="obj">
								<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
									<td align="center">
									<c:if test="${obj.checked==true }">
										<input type="checkbox" checked="checked" disabled="disabled" readonly="readonly" value="${obj.columnId}" sy />
									</c:if> 
									<c:if test="${obj.checked==false }">
											<input type="checkbox" name="recordId" value="${obj.columnId}"/>
									</c:if> 
										&nbsp;&nbsp;&nbsp;&nbsp;</td>
									<td align="center">
										<c:if test="${obj.checked==true }">
											<a ><b>${obj.appAlbum.name }</b></a>
										</c:if>
										<c:if test="${obj.checked==false }">
											${obj.appAlbum.name }
										</c:if>
											&nbsp;&nbsp;
									</td>
									<td align="center">
										${fn:escapeXml(obj.nameCn) }(${fn:escapeXml(obj.name) })&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
									</tr>
							</c:forEach>
					</tbody>
				</table>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		$("#btn").click(function() {
			var id = checkId();
			if (id != ''){
				$.alert("确定要执行此操作吗？", function() {
					$.ajax({
						url:"${ctx}/communal/doConfig",
						type:"POST",
						data:{'id':id,"apkId":$("#apkId").val(),"countyId":$("#countyId").val()},
						dataType:"json",
						success:function(data){
							var flag = eval("("+data+")");
							if(flag.flag == "0"){
								alert("配置成功！"+flag.res+" 条!");
								window.document.location = "${ctx }/communal/appConfig/${appInfo.id}?apkId=${file.id}&countyId=${raveId}";
							}
						},
						error:function(data){
							alert("配置失败！");
							//window.document.location = "${ctx }/app/appConfig/${appInfo.id}?apkId=${file.id}";
						}
					});
				}, true);
			}
		});
	
	function change(apkId){
		window.document.location = "${ctx }/communal/appConfig/${appInfo.id}?apkId="+apkId;
	}
	function change1(raveId){
		window.document.location = "${ctx }/communal/appConfig/${appInfo.id}?apkId=${file.id}&countyId="+raveId;
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
	</script>
</body>
</html>