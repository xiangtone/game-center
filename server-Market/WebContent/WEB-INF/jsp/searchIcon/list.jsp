<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'search';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="${ctx}/search/list"><span id="childTitle"></span></a> -&gt; 图标管理
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx}/searchIcon/list" method="post">
							英文名称：
							<input type="text" name="name" id="name" value="${fn:escapeXml(param.name) }"/>							
							状态：
							<select id="state" name="state">
								<option value="true" <c:if test="${param.state==true}"> selected="selected"</c:if>>是</option>
								<option value="false"<c:if test="${param.state==false}"> selected="selected"</c:if>>否</option>
							</select>
						   <button type="submit" class="butsearch" id="btn-search">查询</button>
						   								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/search/list';" value="返回"/>
					</form>
				</div>
				<div class="operateShop">
					<ul>
						<li><a href="${ctx }/searchIcon/add">添加</a></li>
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
							Id
						</td>
						<td align="center">
							英文名称
						</td>
						<td align="center">
							中文名称
						</td>
						<td align="center">
							图标
						</td>
						<td align="center">
							状态
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
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
						<td align="center">
							<a href="${obj.id }">${obj.id }</a>
						</td>
						<td align="center">
							${fn:escapeXml(obj.name)}
						</td>
						<td align="center">
							${fn:escapeXml(obj.nameCn)}
						</td>
						<td align="center">
							<img alt="图标" width="50" height="50" src="${path}${obj.url }">
						</td>
						<td align="center">
							<c:if test="${obj.state==false}">
								<font color="red">无效</font>
							</c:if>
							<c:if test="${obj.state==true}">
									正常
							</c:if>
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
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/searchIcon/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var params = {"name" : $("#name").val(),"state" : $("#state").val()};
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
				$.ajax({
					url:"${ctx}/searchIcon/delete",
					type:"POST",
					data:{'id':id},
					dataType:"json",
					success:function(data){
						var flag = eval("("+data+")");
						if(flag.success == "true"){
							alert("删除成功！");
							//window.location.reload();
							window.location.href = "list";
						}else{
							alert("删除失败！");
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