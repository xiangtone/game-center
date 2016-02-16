<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>APP截图信息</title>
	<script type="text/javascript">
		var menu_flag = '${sessionScope.menuFlag}';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: APP截图信息<c:if test="${appInfo.free==3 }"> <span class="ys2"><b>(开者者不能操作截图)</b></span></c:if>
	</div>
	<!-- 排序需要传以下2个隐藏域的值,分别为:当前系统服务器请求上下文,数据库表名 -->
	<input type="hidden" id="serverPath" value="${ctx }"/>
	<input type="hidden" id="tableName" value="t_app_picture"/>
	<input type="hidden" id="type" name="type" value="${sessionScope.menuFlag}"/>
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="operateShop">
					<ul>
					<c:if test="${appInfo.free!=3}">
						<li><a href="${ctx }/appPic/showAdd/${appInfo.id}" class="lang">添加</a></li>
					</c:if>		
					<c:if test="${appInfo.free!=3 }"> 
						<li><a id="cmd-delete" class="lang">删除</a></li>		
						<li><a type="button" class="lang" id="sort_edit" >编辑排序</a></li>
						<li><a type="button" class="lang" id="sort_save">保存排序</a></li>
					</c:if>
					
					<c:choose>
					<c:when test="${appInfo != null}">
							<c:if test="${src==0}">
								<li><a href="${ctx }/communal/list" class="lang">返回</a></li>
							</c:if>
							<c:if test="${src==1}">
								<li><a href="${ctx }/market/list" class="lang">返回</a></li>
							</c:if>
							<c:if test="${src==2}">
								<li><a href="${ctx }/app/list" class="lang">返回</a></li>
							</c:if>
							<c:if test="${src==3}">
								<li><a href="${ctx }/open/list" class="lang">返回</a></li>
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
						<td width="2%"><input type="checkbox" class="checkall"/></td>
						<td>
							id
						</td>
						<td>
							所属应用
						</td>
						
						<td>
							标题
						</td>
						<td>
							描述
						</td>
						<td>
							状态
						</td>
						<td>
							排序
						</td>
						<td>
							地址
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
							<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
							<td align="center">
								<c:if test="${obj.appInfo.free==3 }">${obj.id }</c:if>
								<c:if test="${obj.appInfo.free!=3 }"><a href="${ctx }/appPic/edit/${obj.id }">${obj.id }</a></c:if>
								
							</td>
							<td align="center">
								${obj.appInfo.name }
							</td>
							<td align="center">
								${obj.title }
							</td>
							<td align="center">
								${obj.description }
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
								<input type="text" id="sort_${obj.id }" value="${fn:escapeXml(obj.sort) }" maxlength="9" readonly="readonly" style="width:30px;"/>
							</td>
							<td align="center">
								<img alt="" src="${path}${obj.url }" width="200" height="200">
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
			$.alert("删除正常状态记录时，记录会更新为无效.\n删除无效状态记录时，记录会从库中删除.\n确定要删除？", function() {
				$.ajax({
					url:"${ctx}/appPic/delete",
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
