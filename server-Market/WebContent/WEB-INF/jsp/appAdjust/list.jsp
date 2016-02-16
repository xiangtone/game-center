<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appAdjust';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>&nbsp;&nbsp;<span>App名称(id)注明:(<font color="#912CEE">自营App为紫色,</font><font class="ys1">公用App为绿色)
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/appAdjust/list" method="post">
						app名字或appId：<input type="text" class="text1" name="name" id="name" value="${fn:escapeXml(param.name)}"/>&nbsp;&nbsp;
						包名：<input type="text" class="text1"name="packageName" id="packageName"  value="${fn:escapeXml(param.packageName)}"/>
						 &nbsp;&nbsp;<label>国家:</label> 
						<select id="countryId" name="countryId">
							<option value="0">全部</option>
							<c:forEach var="country" items="${countrys}">
								<option value="${country.id}" <c:if test="${country.id == param.countryId}">selected='selected'</c:if> >${country.name}(${country.nameCn})</option>
							</c:forEach>
						</select>
						&nbsp;&nbsp;<label>应用分类:</label> 
						<select id="category_parent" name="category_parent" onchange="querySecondCategory(this.value)">
							<option value="0">全部</option>
							<c:forEach var="category" items="${categorys}">
							<c:if test="${category.id<=3}">
								<option value="${category.id}" <c:if test="${category.id == param.category_parent}">selected='selected'</c:if> >${category.name}</option>
							</c:if>
							</c:forEach>
						</select>
						&nbsp;&nbsp;一级分类:<select name="categoryId" id="categoryId" onchange="change(this.value)">
						<option value="0">--all--</option>
					</select>
					&nbsp;&nbsp;二级分类:<select name="secondId" id="secondId">
						<option value="0">--all--</option>
						</select>
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						<input type="button" class="butsearch" value="重置" onclick="resetQuery(this)"/>		
					</form>
				</div>
			</div>
		<div class="secmainoutside">
			<div class="border">
			<table class="mainlist">
				<thead>
					<tr>
						<td>
							名称(id)
						</td>
						<td class="name"><b style="color: red;">责任人</b></td>
						<td>
							cp(id)
						</td>
						<td>
							所属一二分类
						</td>
						<td>
							分类级别
						</td>
						<td>
							logo
						</td>
						<td align="center">
							下载数(实)--(虚)
						</td>
						<td align="center">
							浏览数
						</td>	
						<td>
							时间
						</td>
						<td>
							操作
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="7" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center" title="${obj.packageName }">
									<c:if test="${obj.free==2 }">
									<font color="#912CEE">${fn:escapeXml(obj.name)}(${obj.id })</font>									
									</c:if>
									<c:if test="${obj.free!=2 }">
									${fn:escapeXml(obj.name)}(${obj.id })								
									</c:if>
							</td>
							<td align="center">
								${obj.source }
							</td>
							<td align="center">
								${obj.cp.name }(${obj.cp.id })
							</td>
							<td align="center">
								<c:if test="${obj.category.firstFatherId==2 }">Apps--</c:if>
								<c:if test="${obj.category.firstFatherId==3 }">Games--</c:if>
								${obj.category.firstName}-- ${obj.category.name }
							</td>
							<td align="center">
									<c:if test="${obj.category.level==0 }">
										应用分类
									</c:if>
									<c:if test="${obj.category.level==1 }">
										一级分类
									</c:if>
									<c:if test="${obj.category.level==2 }">
										二级分类
									</c:if>
							</td>
							<td align="center">
								<img alt="大图标" width="50" height="50" src="${path}${obj.bigLogo }">
							</td>
							<td align="center">
								${obj.realDowdload } -- ${obj.initDowdload }
							</td>
							<td align="center">
							${obj.pageOpen }							
							</td>
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>
							<td align="center">
								<a href="info/${obj.id }">调整</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/appAdjust/list" params="${params}"/>
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
		var categoryParent = "${param.category_parent}";
		if(null != categoryParent && categoryParent != "" ){
			querySecondCategory(categoryParent);
			var categoryId = "${param.categoryId}";
			if(categoryId!=0){
				change(categoryId);
			}
		}
		var categoryId = "${param.categoryId}";
		var secondId="${param.secondId}";
		var params = {"name" : $("#name").val(),"appId" : $("#appId").val(),"packageName" : $("#packageName").val(),"state" : $("#state").val(),"categoryId" : categoryId == "" ? 0 : categoryId,"secondId" : secondId == "" ? 0 : secondId,"category_parent" : $("#category_parent").val()};
		paginationUtils(params);
	});
	$(document).ready(function(){
		$("#searchForm").validate({
			rules: {
				"appId": {
					digits: 100
                    
           }          
		},
		messages: {
			"appId": {
				digits: "只能输入数字"
			}
			}
		});
	});
	function querySecondCategory(value){
		if(value == 0){
			$("#categoryId").html('');
			//$("#categoryId").append("<option value='0'>--all--</option>");
			document.getElementById("firstId").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/appAdjust/secondCategory",
				type : "POST",
				dataType : "json",
				data : {"id" : value},
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
		if(value == 0){
			$("#secondId").html('');
			document.getElementById("secondId").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/appAdjust/secondCategory",
				type : "POST",
				dataType : "json",
				data : {"id" : value},
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
	function resetQuery(){
		if(confirm("确实要重置吗?")){
		    $("#category_parent").val("0");
		    //$("#state").val("true");
		    $("#categoryId").val("0");
		    var name =$(":text");
			name.val("");
		}
	}
	
	</script>
</body>
</html>