<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'music';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/music/list" method="post">
						名称：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name)}"/>&nbsp;&nbsp;
						状态：
						<select id="state" name="state" id="state">
							<option value="true">是</option>
							<option value="false">否</option>
						</select>
						&nbsp;&nbsp;<label>应用分类:</label> 
						<select id="category_parent" name="category_parent" onchange="querySecondCategory(this.value)" disabled="disabled">
							<option value="0">全部</option>
							<c:forEach var="category" items="${categorys}">
								<option value="${category.id}"
									<c:if test="${ringtonesCategory!=null && category.id == ringtonesCategory.id}">selected='selected'</c:if> 
									<c:if test="${ringtonesCategory==null && category.id == param.category_parent}">selected='selected'</c:if> 	
								>${category.name}</option>							
							</c:forEach>
						</select>
						&nbsp;&nbsp;<label>一级分类:</label>
						<select id="categoryId"  name="categoryId" onchange="change(this.value)">
							<option value="0">--all--</option>
						</select>
						&nbsp;&nbsp;<label>二级分类:</label>
						<select id="categoryId1"  name="categoryId1" >
							<option value="0">--all--</option>
						</select>
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
					    <li><a href="${ctx }/music/add">添加</a></li>
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
						<td>
							名称(id)
						</td>
						<td>
							歌唱者
						</td>
						<td>
							时长
						</td>
						<td>
						文件大小
						</td>
						<td>
							所属分类(英文)
						</td>
						<td>
							所属一二分类
						</td>
						<td align="center">
							国家名
						</td>
						<td>
							图标
						</td>
						<td>
							星级
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
							<td colspan="10" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
							<td align="center">
								<a href="${obj.id }?page=${result.currentPage}">${fn:escapeXml(obj.name) }(${obj.id })</a>
							</td>
							<td align="center">
								${obj.artist }
							</td>
							<c:if test="${ obj.duration >=3600}">
								<td  align="center">
								<ff:formatPro fieldValue="${obj.duration/3600 }" type="2" />:<ff:formatPro fieldValue="${(obj.duration%3600)/60}" type="2" />:<ff:formatPro fieldValue="${(obj.duration%3600)%60}" type="2" />
							   </td>
							</c:if>
							<c:if test="${ obj.duration <3600&&obj.duration>=60}">
								<td  align="center">
								  00:<ff:formatPro fieldValue="${obj.duration/60 }" type="2" />:<ff:formatPro fieldValue="${obj.duration%60 }" type="2" />
								</td>
							</c:if>
							<c:if test="${ obj.duration <60}">
								<c:if test="${ obj.duration ==0}">
							    <td  align="center">
								 	<font color="red"> 00:00:00</font>
								</td>
								</c:if>
								<c:if test="${ obj.duration !=0}">
								 <td  align="center">
								 00:00:<ff:formatPro fieldValue="${ obj.duration}" type="2" />
								</td>
								</c:if>	
							</c:if>	
								
							<c:if test="${ obj.fileSize <1024}">
								<td align="center">${obj.fileSize }KB</td>
							</c:if>
							<c:if test="${ obj.fileSize >1024}">
								<td align="center">
									<fmt:formatNumber type="number" value="${obj.fileSize/1024/1024 }" maxFractionDigits="2"/>MB
								</td>
							</c:if>
							<td align="center">
								${obj.category.categoryCn }(${obj.category.name })
							</td>		
							<td align="center">
								<c:if test="${obj.category.firstFatherId==4 }">Ringtones--</c:if>
								${obj.category.firstName}-- ${obj.category.name }
							</td>					
							<td align="center">
								${obj.country.name }(${obj.country.nameCn })
							</td>
							<c:choose>
								<c:when test="${obj.logo!=null&&obj.logo!=''}">
									<td align="center">
										<img alt="图标" width="50" height="50" src="${path}${obj.logo }">
									</td>
								</c:when>
							<c:otherwise>
								<td align="center">
									<img alt="图标" width="50" height="50" src="${ctx}/static/res/ringtones_disk_pic.png">
								</td>
								</c:otherwise>
							</c:choose>
							
							<td align="center">
								${obj.stars }
							</td>
							<td align="center">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>
							<td align="center">
							<a href="info/${obj.id }">查看详细</a>
							<a href="${path }${obj.url }">&nbsp;下载</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/music/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var ringtonesCategory = "${ringtonesCategory}";
		if(ringtonesCategory!=""){
			querySecondCategory("${ringtonesCategory.id}");
		}
		var state = "${param.state}";
		if(state == null || state == ''){
			$("#state option[value=true]").attr("selected","selected");
		}else{
			$("#state option[value='"+ state +"']").attr("selected","selected");
		}
		var raveId = "${param.raveId}";

		if(raveId == null || raveId == ''){
			$("#raveId option[value=true]").attr("selected","selected");
		}else{
			$("#raveId option[value='"+ raveId +"']").attr("selected","selected");
		}
		if(ringtonesCategory != "" ){
			querySecondCategory("${ringtonesCategory.id}");
			var categoryId = "${param.categoryId}";
			if(categoryId!=0){
				change(categoryId);
			}
		}
		var categoryId = "${param.categoryId}";
		var categoryId1 = "${param.categoryId1}";
		var params = {"name" : $("#name").val(),"state" : $("#state").val(),"categoryId" : categoryId == "" ? 0 : categoryId,"categoryId":categoryId1,"category_parent" : $("#category_parent").val(),"raveId" : $("#raveId").val()};
		paginationUtils(params);
	});
	function querySecondCategory(value){
		if(value == 0){
			$("#categoryId").html('');
			//$("#categoryId").append("<option value='0'>--all--</option>");
			document.getElementById("categoryId").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/music/secondCategory",
				type : "POST",
				dataType : "json",
				data : {"id" : value,"level":0},
				success : function (data){
					var result = eval("(" + data + ")");
					if(result.success == 1){
						$("#categoryId").html('');
						$("#categoryId").append(result.option);
						var categoryId = "${param.categoryId}";
						if("" != categoryId && categoryId != 0){
							$("#categoryId").val(categoryId);
						}
					}else{
						$("#categoryId").html('');
						$("#categoryId").append("<option value='0'>--all--</option>");
					
					}
				},
				error : function (error){
					$.messager.alert("提示:","异常出现未知异常!");
				}
			});
		}
	}
	function change(value){
		alert(value);
		if(value == 0){
			$("#categoryId1").html('');
			document.getElementById("categoryId1").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/music/secondCategory",
				type : "POST",
				dataType : "json",
				data : {"id" : value,"level":0},
				success : function (data){
					var result = eval("(" + data + ")");
					if(result.success == 1){
						$("#categoryId1").html('');
						$("#categoryId1").append(result.option);
						var categoryId1 = "${param.categoryId1}";
						if("" != categoryId1 && categoryId1 != 0){
							$("#categoryId1").val(categoryId1);
						}
					}else{
						$("#categoryId1").html('');
						$("#categoryId1").append("<option value='0'>--all--</option>");
					
					}
				},
				error : function (error){
					$.messager.alert("提示:","异常出现未知异常!");
				}
			});
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
	
	$("#cmd-delete").click(function() {
		var id = checkId();
		if (id != ''){
			$.alert("删除正常状态记录时，记录会更新为无效，相对应分发数据会删除.\n删除无效状态记录时，记录会从库中删除.\n确定要删除？", function() {
				$.ajax({
					url:"${ctx}/music/delete",
					type:"POST",
					data:{'id':id},
					dataType:"json",
					success:function(data){
						var flag = eval("("+data+")");
						if(flag.success == "true"){
							alert("删除成功！");
							//window.location.reload();
							window.location.href = "list";
						}
					},
					error:function(data){
						alert("删除失败！");
						//window.location.reload();
						window.location.href = "list";
					}
				});
			}, true);
		}
	});
	</script>
</body>
</html>