<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'clientfeedback';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="list" method="post">
						ClientId：<input type="text" id="clientId" name="clientId" value="${fn:escapeXml(param.clientId) }"/>&nbsp;&nbsp;
						手机机型：<input type="text" id="deviceModel" name="deviceModel" value="${fn:escapeXml(param.deviceModel) }"/>&nbsp;&nbsp;						
						<!--  手机厂商：<input type="text" id="deviceVendor" name="deviceVendor" value="${fn:escapeXml(param.deviceVendor) }"/>&nbsp;&nbsp;-->												
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						<button type="button" class="butsearch" onclick="resetQuery(this)">重置</button>
						&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx }/clientfeedback/zapp/list';" value="常用回馈" />				
					</form>
				</div>
			<div class="operateShop">
					<ul>
						<li><a id="cmd-delete">删除</a></li>					
					</ul>
				</div>
			</div>
		<div class="secmainoutside">
			<div class="border">
			<table class="mainlist" width="100%">
				<thead>
					<tr>
					<td width="2%"><input type="checkbox" class="checkall"/></td>	
						<td>
							客户编号(邮箱)
						</td>
						<td>
							内容
						</td>
						<td>
							手机机型(手机版本名)
						</td>
						<td>
							zapp版本名(版本号)
						</td>
						<td>
							回馈类型
						</td>	
						<td>
							地址
						</td>					
						<td>
							回馈时间
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="12" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId" value="${obj.clientId}"/></td>						
							<td align="center" title="imei : ${obj.imei}">
								<a href="${ctx}/clientfeedback/${obj.clientId}/add?imei=${obj.imei}">
								${obj.clientId }
								<c:if test="${obj.email!=null and obj.email!=''  }">
								(${obj.email})							
								</c:if>
								</a>
							</td>	
							<td align="center" title="${fn:escapeXml(obj.content) }">
							    <c:if test="${obj.replyOrNot==null }">
							     <span><font color="red">(未回复)</font></span>
							    </c:if>
								<ff:formatPro fieldValue="${fn:escapeXml(obj.content) }" type="1" len="30"/>
							</td>
							<td align="center">
								${obj.deviceModel }
								<c:if test="${obj.osVersionName!=null and obj.osVersionName!=''  }">
								(${obj.osVersionName })								
								</c:if>
							</td>
							<td align="center">
								${obj.masVersionName }
								<c:if test="${obj.masVersionCode!=null and  obj.masVersionCode!=0}">
								(${obj.masVersionCode })								
								</c:if>
							</td>
							<td align="center">								
								<c:if test="${obj.feedbackType==1}">用户回馈</c:if><c:if test="${obj.feedbackType==2}">运营回答</c:if>								
							</td>
							<td align="center">	
							${obj.country }							
								<c:if test="${obj.province!=null and obj.province!=''}">
								(
								${obj.province }
								<c:if test="${obj.city!=null and obj.city!=''}">
								-- ${obj.city }	
								</c:if>
								)	
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
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/clientfeedback/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
	var params = {"clientId" : $("#clientId").val(),"deviceModel" : $("#deviceModel").val(),"deviceVendor" : $("#deviceVendor").val()};
	paginationUtils(params);
	
	$("#cmd-delete").click(function() {
		var id = checkId();
		if (id != ''){
			$.alert("删除时会将当前所选用户的所有回复内容都删除,确定要删除？", function() {
				$.ajax({
					url:"${ctx}/clientfeedback/deleteByClientId",
					type:"POST",
					data:{'id':id},
					dataType:"json",
					success:function(data){
						var flag = eval("("+data+")");
						if(flag.success == "true"){
							alert("删除成功！");
							//window.location.reload();
							window.location.href = "${ctx}/clientfeedback/list";
						}
					},
					error:function(data){
						alert("删除失败！");
						//window.location.reload();
						window.location.href = "${ctx}/clientfeedback/list";
					}
				});
			}, true);
		}
	});
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
	function resetQuery(){
		if(confirm("确实要重置吗?")){
			$("#clientId").val("");
			$("#deviceModel").val("");
			$("#deviceVendor").val("");
		}
	}
	</script>
</body>
</html>