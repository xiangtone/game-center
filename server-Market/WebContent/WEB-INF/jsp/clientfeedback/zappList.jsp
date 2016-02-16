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
		当前位置: <a href="../list"><span id="childTitle"></span></a>-&gt; 常用回馈管理 
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx}/clientfeedback/zapp/list" method="post">
						提问：<input type="text" id="question" name="question" value="${fn:escapeXml(param.question) }"/>&nbsp;&nbsp;
						回答内容：<input type="text" id="replyContent" name="replyContent" value="${fn:escapeXml(param.replyContent) }"/>&nbsp;&nbsp;										
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						<button type="button" class="butsearch" onclick="resetQuery(this)">重置</button>
						<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/clientfeedback/list';" value="返回"/>
						&nbsp;&nbsp;Zapp版本号:<input type="text" value="${clientFeedbackZappCode.zappcode}" style="width:30px;" readonly="readonly" />
						&nbsp;&nbsp;版本更新时间:<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${clientFeedbackZappCode.createTime}"/>
						&nbsp;&nbsp;<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/clientfeedback/zapp/updateZappCode';" value="更新"/>
										
					</form>
				</div>
			<div class="operateShop">
					<ul>
						<li><a href="${ctx }/clientfeedback/zapp/add">添加</a></li>
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
							编号
						</td>
						<td align="left">
							提问
						</td>
						<td align="left">
							回答内容
						</td>
						<td>
							是否生效
						</td>
						<td>
							创建时间
						</td>		
						<td>
							操作
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="5" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>					
							<td align="center">
								<a href="${ctx}/clientfeedback/zapp/${obj.id }">${obj.id }</a>
							</td>
							<!-- 
							<td align="center">
								<c:if test="${obj.question.length()>30 }" >
									<a title="${fn:escapeXml(obj.question)}" onclick="open1(this.title)" >详情</a>
								</c:if>
								<c:if test="${obj.question.length()<=30 }" >${fn:escapeXml(obj.question)}</c:if>
							</td>
							 -->
							<td align="left" style="word-break:break-all; word-wrap:break-word;" title="${fn:escapeXml(obj.question ) }">
								<ff:formatPro fieldValue="${obj.question}" type="1" len="45"/>
							</td>
							<!-- 
							<td align="center">
								<c:if test="${obj.replyContent.length()>30 }" >
									<a title="${fn:escapeXml(obj.replyContent)}" onclick="open1(this.title)" >详情</a>
								</c:if>
								<c:if test="${obj.replyContent.length()<=30 }" >${fn:escapeXml(obj.replyContent)}</c:if>
							</td>
							-->
							<td align="left" style="word-break:break-all; word-wrap:break-word;" title="${fn:escapeXml(obj.replyContent ) }">
								<ff:formatPro fieldValue="${obj.replyContent}" type="1" len="45"/>
							</td>					
							<td align="center">								
								<c:if test="${obj.state==true}">有效</c:if>
								<c:if test="${obj.state==false}"><font color="red">无效</font></c:if>								
							</td>
							<td align="center">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>
							<td align="center" width="10%">
							<a href="${ctx }/clientfeedback/zapp/info/${obj.id }">查看详细</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/clientfeedback/zapp/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
	var params = {"question" : $("#question").val(),"replyContent" : $("#replyContent").val()};
	paginationUtils(params);
	
	$("#cmd-delete").click(function() {
		var id = checkId();
		if (id != ''){
			$.alert("确定要删除？", function() {
				$.ajax({
					url:"${ctx}/clientfeedback/zapp/delete",
					type:"POST",
					data:{'id':id},
					dataType:"json",
					success:function(data){
						var flag = eval("("+data+")");
						if(flag.success == "true"){
							alert("删除成功！");
							//window.location.reload();
							window.location.href = "${ctx}/clientfeedback/zapp/list";
						}
					},
					error:function(data){
						alert("删除失败！");
						//window.location.reload();
						window.location.href = "${ctx}/clientfeedback/zapp/list";
					}
				});
			}, true);
		}
	});
	});
		
	function resetQuery(){
		if(confirm("确实要重置吗?")){
			$("#question").val("");
			$("#replyContent").val("");
		}
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
	function open1(val){
		alert(val);
	}
	</script>
</body>
</html>