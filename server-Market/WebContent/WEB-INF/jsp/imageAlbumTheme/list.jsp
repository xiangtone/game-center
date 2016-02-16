<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'imageAlbumTheme';
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	<!-- 排序需要传以下2个隐藏域的值,分别为:当前系统服务器请求上下文,数据库表名 -->
	<input type="hidden" id="serverPath" value="${ctx }"/>
	<input type="hidden" id="tableName" value="t_res_image_theme"/>
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx }/imageAlbumTheme/list" method="post">
						主题名：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name)}"/>&nbsp;&nbsp;
						&nbsp;&nbsp;国家：
							<select name="raveId" id="raveId">
								<option value="0">全部</option>					
								<c:forEach var="country" items="${countrys}">
									<option value="${country.id}" <c:if test="${country.id==param.raveId}"> selected="selected"</c:if>>${country.name}(${country.nameCn})</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
					    <li><a href="${ctx }/imageAlbumTheme/add" type="button" class="lang" >添加</a></li>
						<li><a id="cmd-delete" type="button" class="lang" >删除</a></li>
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
							id
						</td>
						<td>
							主题名称(中文)
						</td>
						<td align="center">
							国家名
						</td>
						<td>
							图标
						</td>
						<td>
							详情
						</td>
						<td>
							排序
						</td>
						<td>
							状态
						</td>
						<td>
							创建时间
						</td>
						<td>
							更新时间
						</td>
						<td>
							操作
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="9" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId" value="${obj.themeId}"/></td>
							<td align="center">
								<a href="${obj.themeId }">${obj.themeId }</a>
							</td>
							<td align="center">
								${fn:escapeXml(obj.name)}(${fn:escapeXml(obj.nameCn)})
							</td>
							<td align="center">
								${obj.country.name }(${obj.country.nameCn })
							</td>
							<td align="center">
								<img alt="图标" width="50" height="50" src="${path}${obj.bigicon }">
							</td>
							<td align="center" style="word-break:break-all; word-wrap:break-word;" title="${fn:escapeXml(obj.description ) }">
								<ff:formatPro fieldValue="${obj.description}" type="1" len="45"/>
							</td>	
							<td align="center">
								<input type="text" id="sort_${obj.themeId }" value="${fn:escapeXml(obj.sort) }" maxlength="9" readonly="readonly" style="width:30px;"/>
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
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.updateTime}"/>
							</td>
							<td align="center">
								<c:if test="${obj.state==true}">
									<a href="${ctx }/wallpaper/listRes/${obj.themeId }?resType=theme&raveId=${obj.country.id}">分发</a>
								</c:if>
								<c:if test="${obj.state==false}">
								</c:if>
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/imageAlbumTheme/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var params = {"name" : $("#name").val(),"raveId" : $("#raveId").val()};
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
			$.alert("删除正常状态记录时，记录会更新为无效，相对应分发数据会删除.\n删除无效状态记录时，记录会从库中删除.\n确定要删除？", function() {
				$.post("delete", {
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