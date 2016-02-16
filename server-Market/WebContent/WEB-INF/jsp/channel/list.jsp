<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'channel';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	<!-- 排序需要传以下2个隐藏域的值,分别为:当前系统服务器请求上下文,数据库表名 -->
	<input type="hidden" id="serverPath" value="${ctx }"/>
	<input type="hidden" id="tableName" value="t_channel_info"/>
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="" method="post">
						名称：
						<select name="channelId" id="channelId" >
							<%
								int i=0;
							%>
							<c:forEach var="father" items="${fatherIds}">
								<%
									if(i==0){
								%>
								<option value="0" >全部</option>
								<% i++;
									}
								%>
								<option value="${father.id}" >${father.name}</option>
							</c:forEach>
							</select>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul >
						<!-- <li><a href="${ctx }/channel/add" type="button" class="lang">添加一级渠道</a></li>
						<li ><a href="${ctx }/channel/addSecond" type="button" class="lang">添加二级渠道</a></li>
						<li><a id="cmd-delete">删除</a></li> -->
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
						<td align="center">
							渠道ID
						</td>
						<td align="center">
							父渠道
						</td>
						<td align="center">
							渠道名
						</td>
						<td align="center">
							渠道类型
						</td>
						<td align="center">
							简介说明
						</td>
						<td align="center">
							所在区
						</td>
						<td align="center">
							排序
						</td>
						<td align="center">
							创建时间
						</td>
						<td align="center">
							操作
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${channels.recordCount == 0}">
						<tr>
							<td colspan="7" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${channels.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
							<td align="center" >
								<a href="${obj.id },${obj.fatherId }">${obj.id }</a>
							</td>
							<td align="center">
								${obj.fatherName}(${obj.fatherId })
							</td>
							<td align="center">
								${fn:escapeXml(obj.name)}
							</td>
							<td align="center">
								${obj.type}
							</td>
							<td align="center" style="word-break:break-all; word-wrap:break-word;" title="${fn:escapeXml(obj.description ) }">
								<ff:formatPro fieldValue="${obj.description}" type="1" len="45"/>
							</td>	
							<td align="center">
								${obj.province.name}
							</td>
							<td align="center">
								<input type="text" id="sort_${obj.id }" value="${fn:escapeXml(obj.sort) }" maxlength="9" readonly="readonly" style="width:30px;"/>
							</td>
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>
							<td align="center">
								<a href="channelInfo/${obj.id }">查看详细</a>
								<a href="listSecond/${obj.id }">查看二级渠道</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${channels.recordCount != 0}">
						<ccgk:pagination paginationVo="${channels}" contextPath="${ctx}/channel/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var channelId = "${param.channelId}";
		if(channelId == null || channelId == ''){
			$("option[value=true]").attr("selected","selected");
		}else{
			$("option[value='"+ channelId +"']").attr("selected","selected");
		}
		var params = {"channelId" : $("#channelId").val()};
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
			$.alert("删除后将不可恢复，且对应子渠道也会删除，确定删除？", function() {
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