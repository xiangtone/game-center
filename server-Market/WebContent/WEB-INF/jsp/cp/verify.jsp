<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'developersCp';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx}/cp/verify" method="post">
						cp名称：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name) }"/>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<!-- 
				<div class="operateShop">
					<ul>
						<li><a href="${ctx }/cp/add">添加</a></li>
						<li><a id="cmd-delete">删除</a></li>
					</ul>
				</div>
				 -->
			</div>
		<div class="secmainoutside">
			<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td width="100">
							cpid
						</td>
						<td width="150" align="center">
							cp名
						</td>
						<td width="100" align="center">
							描述
						</td>
						<td width="250" align="center">
							地址
						</td>
						<td width="150" align="center">
							电话
						</td>
						<td width="100" align="center">
							qq
						</td>
						<td width="150" align="center">
							邮箱
						</td>
						<td width="100" align="center">
							状态
						</td>
						<td width="170"align="center">
							创建时间
						</td>
						<td width="170"align="center">
							更新时间
						</td>
						<td width="100" align="center">
							操作
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${cps.recordCount == 0}">
						<tr>
							<td colspan="7" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${cps.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">					
						<td align="center">
							${obj.id }
						</td>
						<td align="center">
							${fn:escapeXml(obj.name)}
						</td>
						<td align="center">
							${obj.description }
						</td>
						<td align="center">
							${obj.address }
						</td>
						<td align="center">
							${obj.phoneNum }
						</td>
						<td align="center">
							${obj.qq }
						</td>
						<td align="center">
							${obj.email }
						</td>
						<td align="center">
						  <c:if test="${obj.cpState==0 }">
							<span class="ys1">未提交</span>
						  </c:if>
						  <c:if test="${obj.cpState==1 }">
							<span class="ys2">审核中</span>
						  </c:if>
						  <c:if test="${obj.cpState==2 }">
							<span class="ys3">通过审核</span>
						  </c:if>
						  <c:if test="${obj.cpState==3 }">
							<span class="ys4">未通过审核</span>
						  </c:if>
						</td>
						<td align="center">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
						</td>
						<td align="center">							
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.updateTime}"/>
						</td>
						<td align="center">
							<c:if test="${obj.state==false && obj.cpState==1 }">
									<a href="#" onClick="doState(${obj.id},'1')" >通过</a>|<a href="#" onClick="doState(${obj.id},'0')" >不通过</a>
							</c:if>							
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${cps.recordCount != 0}">
						<ccgk:pagination paginationVo="${cps}" contextPath="${ctx}/cp/verify" params="${params}"/>
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
				$.get("delete", {
					id : id
				}, function(response) {
					var data = eval("("+response+")");
					if(data.success=="true"){
						$.alert("删除成功！");
						//window.location.reload();
						window.location.href = "list";
					}else{
						$.alert("删除失败,以下("+data.success+")cp中已关联其他应用,请重新选择,！");
					}
				});
			}, true);
		}
	});
	function doState(id,state){
		var s="";
		if(state=='1'){
			s="通过";
		}else{
			s="取消";
		}
		$.alert("确定要["+s+"]当前cp？", function() {
			$.ajax({
				url:"${ctx}/cp/doState/"+id+"?state="+state,
				type:"POST",
				data:{'id':id},
				dataType:"json",
				success:function(data){
					var data1 = eval("("+data+")");
					if(data1.flag== "0"){
						alert("操作成功！");
						window.location.reload();
					}else{
						alert("操作失败！");
						window.location.reload();
					}
				},
				error:function(data){
					alert("操作失败！");
					window.location.reload();
				}
			});
		}, true);
	}
	</script>
</body>
</html>
