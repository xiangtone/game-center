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
	
	<div class="mainoutside">
		<div class="secmainoutside">
			<div class="mainarea_shop">
				<div class="operateShop">
					<ul>
						<!-- <li><a id="cmd-delete">删除</a></li> -->
							<li><a href="${ctx }/channel/list">返回</a></li>
					</ul>
				</div>
			</div>
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
								<a href="${ctx }/channel/${obj.id },${obj.fatherId }">${obj.id }</a>
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
								${obj.sort}
							</td>
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>
							<td align="center">
								<a href="${ctx}/channel/channelInfo/${obj.id }">查看详细</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<!-- 
			<div class="page">
				<div class="pageContent">
					<c:if test="${channels.recordCount != 0}">
						<ccgk:pagination paginationVo="${channels}" contextPath="${ctx}/listSecond/${father}" params="${params}"/>
					</c:if>
				</div>
			</div>
			 -->
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
					url:"${ctx}/channel/delete",
					type:"POST",
					data:{'id':id},
					dataType:"json",
					success:function(data){
						var flag = eval("("+data+")");
						if(flag.success == "true"){
							alert("删除成功！");
							window.location.reload();
						}
					},
					error:function(data){
						alert("删除失败！");
						window.location.reload();
					}
				});
			}, true);
		}
	});
	</script>
</body>
</html>