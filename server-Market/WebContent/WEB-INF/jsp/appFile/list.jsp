<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>自营apk管理</title>
	<script type="text/javascript">
		var menu_flag = 'app';
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
			当前位置:<a href="${ctx }/app/list" style="color: red;">自营app管理</a>-&gt;自营apk管理
	</div>
	
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="operateShop">
					<ul>
					<c:if test="${appInfo.free!=3 }">
						<li><a href="${ctx }/appFile/showAdd/${appInfo.id}">添加</a></li>
					</c:if>
					<li><a id="cmd-delete">删除</a></li>
					<li><a href="${ctx }/app/list">返回</a></li>
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
							id
						</td>
						<td align="center">
							所属应用
						</td>
						<td align="center">
							渠道号
						</td>
						<td align="center">
							国家名
						</td>
						<td align="center">
							cp名
						</td>
						<td align="center">
							版本号---版本名
						</td>
						<td align="center">
							apkkey
						</td>
						<td align="center">
							状态
						</td>	
						<td align="center">
							包名
						</td>
						<td align="center">
							历史版本
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result == null}">
						<tr>
							<td colspan="8" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
							<td align="center" width="100">
								<c:if test="${obj.appInfo.free==3 }">${obj.id }</c:if>
								<c:if test="${obj.appInfo.free!=3 }"><a href="${ctx }/appFile/edit/${obj.id }">${obj.id }</a></c:if>
							</td>
							<td align="center">
								${fn:escapeXml(obj.appInfo.name) }(${obj.appInfo.id })
							</td>
							<td align="center">
								${obj.channel.name }(${obj.channel.id })
							</td>
							<td align="center">
								${obj.country.name }(${obj.country.nameCn })
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
								<c:if test="${obj.state==false}">
									<font color="red">无效</font>
								</c:if>
								<c:if test="${obj.state==true}">
									正常
								</c:if>
							</td>
							<td align="center">
								<a href="${path }${obj.url }">${obj.packageName }</a>
							</td>
							<td align="center">
								<c:if test="${obj.haslist== true }">
										<a href="${ctx }/appFile/fileList/${obj.id }"  target="_BLANK">查看历史版本</a>									
								</c:if>
								<c:if test="${obj.haslist==false }">
									无
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
			$.alert("删除正常状态记录时，记录会更新为无效，相对应分发数据会删除.\n删除无效状态记录时，记录会从库中删除.\n确定要删除？", function() {
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
	</script>
</body>
</html>