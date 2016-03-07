<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'category';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	<!-- 排序需要传以下2个隐藏域的值,分别为:当前系统服务器请求上下文,数据库表名 -->
	<input type="hidden" id="serverPath" value="${ctx }"/>
	<input type="hidden" id="tableName" value="t_category"/>
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/category/list" method="post">
					应用分类:<select name="faterId" id="faterId" onchange="change(this.value)">
							<c:forEach var="father" items="${fatherIds}">
								<option value="${father.id}" >${father.name}</option>
							</c:forEach>
						</select>
					一级分类:<select name="firstId" id="firstId" onchange="change1(this.value)">
						<option value="0">--all--</option>
					</select>
					二级分类:<select name="secondId" id="secondId">
						<option value="0">--all--</option>
					</select>
					名称(英文)：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name) }"/>&nbsp;&nbsp;

						状态：
						<select id="state" name="state">
							<option value="true">是</option>
							<option value="false">否</option>
						</select>
						国家:<select name="raveId" id="raveId">
							<option value="0" selected="selected">All</option>
								<c:forEach var="country" items="${countrys}">
									<option value="${country.id}" >${country.name}(${country.nameCn})</option>
								</c:forEach>
								
							</select>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
						<li><a href="${ctx }/category/add">添加一级分类</a></li> 
						<li><a href="${ctx }/category/addSecond" type="button" class="lang">添加二级分类</a></li>
						<li><a id="cmd-delete" type="button" class="lang">删除</a></li>
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
							编号
						</td>
						<td align="center">
							父级分类
						</td> 
						<td align="center">
							分类名(英文)
						</td>
						<td align="center">
							分类级别
						</td>
						<td align="center">
							国家(英文)
						</td>
						<td>
							小图标
						</td>
						<td align="center">
							推荐应用
						</td>
						<td align="center">
							排序值
						</td>
						<td align="center">
							状态
						</td>
					
						<td align="center">
							创建时间
						</td>
						<td align="center">
							更新时间
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${categorys.recordCount == 0}">
						<tr>
							<td colspan="7" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${categorys.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
							<td align="center" >
								<a href="${obj.id },${obj.level}">${obj.id }</a>
							</td>
							<td align="center">
								${obj.firstName }
							</td>
							<td align="center">
								${obj.categoryCn }(${fn:escapeXml(obj.name)})
							</td>
							<td align="center">
								<c:if test="${obj.level==0 }">应用分类</c:if>
								<c:if test="${obj.level==1 }">一级分类</c:if>
								<c:if test="${obj.level==2 }">二级分类</c:if>
							</td>
							<td align="center">
								${obj.cateNameCn }(${obj.cateName })
							</td>
							<td align="center">
								<img alt="大图标" width="50" height="50" src="${path}${obj.bigicon }">
							</td>
							<td align="center" width="200">
								${obj.recommend }
							</td>
							<!-- 排序值  -->
							<td align="center">
								<input type="text" id="sort_${obj.id }" value="${fn:escapeXml(obj.sort) }" readonly="readonly" maxlength="9" style="width:30px;"/>
							</td>
							<td align="center">
								<c:if test="${obj.state ==true }">是</c:if>
								<c:if test="${obj.state ==false }">否</c:if>
							</td>
							
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.updateTime}"/>
							</td>
							<!-- <td align="center">
							<a href="categoryInfo/${obj.id }">查看详细</a>
							<a href="listSecond/${obj.id }">查看二级分类</a>
						</td> -->
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${categorys.recordCount != 0}">
						<ccgk:pagination paginationVo="${categorys}" contextPath="${ctx}/category/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var state = "${param.state}";
		if(state == null || state == ''){
			$("option[value=true]").attr("selected","selected");
		}else{
			$("option[value='"+ state +"']").attr("selected","selected");
		}
		var fatherId = "${param.faterId}";
		if(fatherId == null || fatherId == ''){
			$("option[value=0]").attr("selected","selected");
		}else{
			$("option[value='"+ fatherId +"']").attr("selected","selected");
		}
		var raveId = "${param.raveId}";
		if(raveId == null || raveId == ''){
			$("option[value=0]").attr("selected","selected");
		}else{
			//$("option[value='"+ raveId +"']").attr("selected","selected");
			$("#raveId").val(raveId);
		}
		change($("#faterId").val());
		var faterId=$("#faterId").val();
		var firstId = "${param.firstId}";
		if(firstId!=0){
			change1(firstId);
		}
		var params = {"name" : $("#name").val(),"state" : $("#state").val(),"faterId" : faterId,"firstId" : $("#firstId").val(),"secondId" : $("#secondId").val(),"raveId":$("#raveId").val()};
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
				$.ajax({
					url:"${ctx}/category/delete",
					type:"POST",
					data:{'id':id},
					dataType:"json",
					success:function(response){
						var data = eval("("+response+")");
						if(data.flag == "0"){
							alert("删除成功！");
							//window.location.reload();
							window.location.href = "list";
						}else if(data.flag == "1"){							
							alert("删除失败，该类别已经被引用！");
							//window.location.reload();
							window.location.href = "list";
						}else if(data.flag == "2"){		
							alert("删除失败，该类别不允许删除！");
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
	
	function change(value){
		if(value == 0){
			$("#firstId").html('');
			document.getElementById("firstId").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/category/getCategory",
				type : "POST",
				dataType : "json",
				data : {"id" : value,"level":1},
				success : function (data){
					var result = eval("(" + data + ")");
					if(result.success == 1){
						$("#firstId").html('');
						$("#firstId").append(result.option);
						var firstId = "${param.firstId}";
						if("" != firstId && firstId != 0){
							$("#firstId").val(firstId);
						}
					}else{
						$("#firstId").html('');
						$("#firstId").append("<option value='0'>--all--</option>");
					}
				},
				error : function (error){
					$.messager.alert("提示:","异常出现未知异常!");
				}
			});
		}
	}
	function change1(value){
		if(value == 0){
			$("#secondId").html('');
			document.getElementById("secondId").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/category/getCategory",
				type : "POST",
				dataType : "json",
				data : {"id" : value,"level":2},
				success : function (data){
					var result = eval("(" + data + ")");
					if(result.success == 1){
						$("#secondId").html('');
						$("#secondId").append(result.option);
						var secondId = "${param.secondId}";
						if("" != secondId && secondId != 0){
							$("#secondId").val(secondId);
						}
					}else{
						$("#secondId").html('');
						$("#secondId").append("<option value='0'>--all--</option>");
					
					}
				},
				error : function (error){
					$.messager.alert("提示:","异常出现未知异常!");
				}
			});
		}
	}
	</script>
</body>
</html>