<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'market';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>-&gt; 版本升级管理
	</div>
	
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx}/zappUpdate/${apkId}/list" method="post">
						版本名:<input type="text" name="versionName" id="versionName" value="${fn:escapeXml(param.versionName) }"/>&nbsp;&nbsp;
						升级状态:<select name="upgradeType" id="upgradeType">
						    <option value="0" <c:if test="${param.upgradeType==0}">selected="selected"</c:if>>全部</option>
							<option value="1" <c:if test="${param.upgradeType==1}">selected="selected"</c:if>>不升级</option>
							<option value="2" <c:if test="${param.upgradeType==2}">selected="selected"</c:if>>升级</option>
							<option value="3" <c:if test="${param.upgradeType==3}">selected="selected"</c:if>>强制升级</option>
						</select>
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/markAppFile/list/${appFile.appInfo.id}';" value="返回"/>
						
					</form>
				</div>
				<div class="operateShop">
					<ul>
						<li><a href="${ctx }/zappUpdate/${apkId}/add">添加</a></li>
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
							版本名
						</td>
						<td align="center">
							版本号
						</td>
						<td align="center">
							升级状态
						</td>
						<td align="center">
							更新内容
						</td>
						<td align="center">
							创建日期
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount==0}">
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
							<a href="${ctx }/zappUpdate/${apkId}/${obj.id }">${fn:escapeXml(obj.versionName)}</a>
						</td>
						<td align="center">
							${fn:escapeXml(obj.versionCode)}
						</td>
						<td align="center">
						    <c:if test="${obj.upgradeType==1}">不升级</c:if>
						    <c:if test="${obj.upgradeType==2}">升级</c:if>
						    <c:if test="${obj.upgradeType==3}">强制升级</c:if>						
						</td>
						<td align="center">
							<c:if test="${obj.updateInfo.length()>60}" >
								<a title="${fn:escapeXml(obj.updateInfo)}" onclick="open1(this.title)"href="#" >详情</a>
							</c:if>
							<c:if test="${obj.updateInfo.length()<=60 }" >${fn:escapeXml(obj.updateInfo)}</c:if>
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
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/zappUpdate/${apkId}/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var params = {"versionName" : $("#versionName").val(),"upgradeType" : $("#upgradeType").val()};
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
				$.post("${ctx}/zappUpdate/${apkId}/delete", {
					id : id
				}, function(data) {
					$.alert("删除成功！");
					//window.location.reload();
					window.location.href = "${ctx}/zappUpdate/${apkId}/list";
				});
			}, true);
		}
	});
	function open1(val){
		alert(val);
	}
	</script>
</body>
</html>