<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appAlbum';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx }/appAlbum/listRes/${appAlbum.id}" method="post">
						名称：<input type="text" name="name" value="${fn:escapeXml(param.name)}"/>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
					     <li><a href="${ctx }/appAlbum/${appAlbum.id}/config">配置</a></li>
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
							平台id
						</td>
						<td align="center">
							分类
						</td>
						<td align="center">
							排序
						</td>
						<td align="center">
							应用名
						</td>
						<td align="center">
							星星评级
						</td>
						<td align="center">
							版本名
						</td>
						<td align="center">
							文件大小
						</td>
						<td align="center">
							下载数
						</td>
						<td align="center">
							下载地址
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
							${obj.id }
						</td>
						<td align="center">
							${obj.raveId }
						</td>
						<td align="center">
							${obj.appAlbum.name }
						</td>
						<td align="center">
							${obj.sort }
						</td>
						<td align="center">
							${obj.appName }
						</td>
						<td align="center">
							${obj.stars }
						</td>
						<td align="center">
							${obj.versionName }
						</td>
						<td align="center">
							${obj.fileSize }
						</td>
						<td align="center">
							${obj.initDowdload }
						</td>
						<td align="center">
							<a href="${path }${obj.url }">下载</a>
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/appAlbum/listRes/${appAlbum.id}" params="${params}"/>
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
				$.ajax({
					url:"${ctx}/appAlbum/delete",
					type:"POST",
					data:{'id':id},
					dataType:"json",
					success:function(data){
						var flag = eval("("+data+")");
						if(flag.success == "true"){
							$.alert("删除成功！");
							//window.location.reload();
							window.location.href = "list";
						}
					},
					error:function(data){
						alert("error:" + data);
					}
				});
			}, true);
		}
	});
	</script>
</body>
</html>