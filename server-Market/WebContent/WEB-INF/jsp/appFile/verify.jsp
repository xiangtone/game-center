<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>apk审核</title>
	<script type="text/javascript">
		var menu_flag = 'open';
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置: apk审核
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="operateShop">
					<ul>					
					<!-- <li><a id="cmd-delete">删除</a></li> -->
					<c:choose>
					<c:when test="${appInfo != null}">
						<c:if test="${appInfo.free==3}">
							<li><a href="${ctx }/open/list">返回</a></li>
						</c:if>
						<c:if test="${appInfo.free==2}">
							<li><a href="${ctx }/app/list">返回</a></li>
						</c:if>
					</c:when>
					</c:choose>
					</ul>
				</div>
			</div>
		<div class="secmainoutside">
			<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td align="center" width="13%">
							所属应用
						</td>
						<td align="center" width="10%">
							渠道号
						</td>
						<td align="center" width="10%">
							国家名
						</td>
						<td align="center" width="8%">
							cp名
						</td>					
						<td align="center" width="5%">
							版本号---版本名
						</td>
						<td align="center" width="5%">
							apkKey
						</td>
						<td align="center" width="15%">
							包名
						</td>
						<td align="center">
							下载地址
						</td>
						<td align="center">
							历史版本
						</td>
						<td align="center">
							开发者状态
						</td>
						<td align="center">
							审核状态
						</td>	
						<td align="center">
							操作
						</td>													
					</tr>
				</thead>
				<tbody>
					<c:if test="${result == null}">
						<tr>
							<td colspan="7" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">							
							<td align="center"  width="100">
								${fn:escapeXml(obj.appInfo.name) }(${obj.appInfo.id })
							</td>
							<td align="center">
								${obj.channel.name }(${obj.channel.id })
							</td>
							<td align="center">
								${obj.raveNames}
							</td>
							<td align="center">
								${obj.cp.name }(${obj.cp.id })
							</td>
							<td align="center">
								${obj.versionCode }---${obj.versionName }
							</td>
							<td align="center">
								${obj.apkKey }
							</td>
							<td align="center">
								${obj.packageName }
							</td>
							<td align="center">
								<a href="${path }${obj.url }">下载</a>
							</td>
							<td align="center">
								<c:if test="${obj.haslist== true }">
									有									
								</c:if>
								<c:if test="${obj.haslist==false }">
									无
								</c:if>
							</td>
							<td align="center">
								<c:if test="${obj.cpState==3}">
									草稿
								</c:if>							
								<c:if test="${obj.cpState==2}">
									未上线
								</c:if>
								<c:if test="${obj.cpState==1 and obj.state==false}">
									请求上线
								</c:if>
								<c:if test="${obj.cpState==1 and obj.state==true}">
									上线
								</c:if>
							</td>								
							<td align="center">
								<c:if test="${obj.state==false}">
									<font color="red">未通过</font>
								</c:if>
								<c:if test="${obj.state==true}">
									通过
								</c:if>
							</td>
							<td align="center">
								<c:if test="${obj.cpState==1 and obj.state==false}">
									<a href="#" onClick="doState(${obj.appInfo.id},'1')" >通过</a>|<a href="#" onClick="doState(${obj.appInfo.id},'3')">不通过</a>
								</c:if>								
							</td>													
						</tr>
					</c:forEach>
				</tbody>
			</table>
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
			$.alert("删除后将不可恢复，确定删除？并且相对应分发也会删除", function() {
				$.ajax({
					url:"${ctx}/appFile/delete",
					type:"POST",
					data:{'id':id},
					dataType:"json",
					success:function(data){
						var flag = eval("("+data+")");
						if(flag.success == "true"){
							alert("删除成功！");
							window.location.reload();
							//window.location.href = "list";
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
	function doState(id,state){
		var s="";
		if(state=='1'){
			s="审核通过";
		}else{
			s="审核不通过";
		}
		$.alert("确定要"+s+"？", function() {
			$.ajax({
				url:"${ctx}/appFile/doState/"+id+"?state="+state,
				type:"POST",
				data:{'id':id},
				dataType:"json",
				success:function(data){
					var data1 = eval("("+data+")");
					if(data1.flag== "0"){
						alert("操作成功！");
						window.location.reload();
					}else if(data1.flag== "2"){
						alert("开发者未请求上线！");
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
