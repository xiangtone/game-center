<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'ourPartners';
	</script>

</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	<input type="hidden" id="serverPath" value="${ctx }"/>
	<input type="hidden" id="tableName" value="t_our_partners"/>
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/ourPartners/list" method="post">
						名称(英文)：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name)}"/>&nbsp;&nbsp;		
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
					    <li><a href="${ctx }/ourPartners/add">添加</a></li>
						<li><a id="cmd-delete">删除</a></li>
						<li><a type="button" class="lang" id="sort_edit" >编辑排序</a></li>
						<li><a type="button" class="lang" id="sort_save">保存排序</a></li>
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
							名称(id)
						</td>
						<td>
							合作伙伴名(英文)
						</td>
						<td>
							链接地址
						</td>
						<td>
							logo
						</td>
						<td>
							描述
						</td>
						<td align="center">
							是否有效
						</td>
						<td align="center">
							排序值
						</td>
						<td>
							时间
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
								<a href="${obj.id }">${fn:escapeXml(obj.id) }</a>
							</td>
							<td align="center">
								${obj.nameCh }(${obj.nameEn })
							</td>
							<td align="center">
								${obj.url }
							</td>
							<td align="center">
								<img alt="大图标" width="50" height="50" src="${path}${obj.logo }">
							</td>
							<td align="center">
								${obj.remark }
							</td>
							<td align="center">
								<c:if test="${obj.state==0}">
									<font color="red">无效</font>
								</c:if>
								<c:if test="${obj.state==1}">
									正常
								</c:if>
							</td>
							<td align="center">
								<input type="text" id="sort_${obj.id }" value="${fn:escapeXml(obj.sort) }" maxlength="9" readonly="readonly" style="width:30px;"/>
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
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/ourPartners/list" params="${params}"/>
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
				$.ajax({
					url:"${ctx}/ourPartners/delete",
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