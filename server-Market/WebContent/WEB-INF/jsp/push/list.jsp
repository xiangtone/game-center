<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'push';
	</script>

</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/push/list" method="post">
						通知标题 :&nbsp;<input type="text" name="title" id="title" value="${fn:escapeXml(param.title)}"/>&nbsp;&nbsp;	
						推送对象:&nbsp;<select id="target" name="target">
								<option value="-1">全部</option>
								<option value="0" <c:if test="${param.target==0}"> selected="selected"</c:if>>全部版本</option>
								<option value="1" <c:if test="${param.target==1}"> selected="selected"</c:if>>指定版本</option>
								</select> &nbsp;&nbsp;	
						后续动作:&nbsp;<select id="action" name="action">
								<option value="-1">全部</option>
								<option value="0" <c:if test="${param.action==0}"> selected="selected"</c:if>>启动Zapp</option>
								<option value="1" <c:if test="${param.action==1}"> selected="selected"</c:if>>打开链接</option>
								</select> &nbsp;&nbsp;	
						开始日期 :&nbsp;<input id='startTime' name='startTime' readonly="readonly" type="text" style="width:150px" value="${param.startTime}"
									 class="Wdate" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'1999-01-01',maxDate:'#F{$dp.$D(\'endTime\')||\'{%y+1}\'}'})" maxlength="30"/>
						结束日期:&nbsp;<input id='endTime' name='endTime' readonly="readonly" type="text" style="width:150px" class="Wdate"  value="${param.endTime}"
									 onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')||\'1999-01-01\'}',maxDate:'{%y+1}'})" maxlength="30"/>					
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
					    <li><a href="${ctx }/push/add">添加</a></li>
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
							通知标题
						</td>
						<td>
							推送时间
						</td>
						<td>
							推送对象
						</td>
						<td>
							后续动作
						</td>
						<td>
							创建时间
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
								<a href="${obj.id }?page=${result.currentPage}">${fn:escapeXml(obj.title) }</a>
							</td>
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.startTime}"/>--
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.endTime}"/>
							</td>
							<td align="center">
								<c:if test="${obj.target==0 }">全部版本</c:if>
								<c:if test="${obj.target==1 }">指定版本</c:if>
							</td>
							<td align="center">
								<c:if test="${obj.action==0 }">启动Zapp</c:if>
								<c:if test="${obj.action==1 }">打开链接</c:if>
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
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/push/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var params = {"title" : $("#title").val(),"target":$("#target").val(),"action":$("#action").val(),"startTime":$("#startTime").val(),"endTime":$("#endTime").val()};
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
					url:"${ctx}/push/delete",
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
	</script>
</body>
</html>