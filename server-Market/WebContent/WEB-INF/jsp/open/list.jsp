<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'open';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
	<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/open/list" method="post">
						app名字或appId：<input type="text" class="text1" name="name" id="name" value="${fn:escapeXml(param.name)}"/>&nbsp;&nbsp;
						包名：<input type="text" class="text1"name="packageName" id="packageName"  value="${fn:escapeXml(param.packageName)}"/>
						<!-- &nbsp;&nbsp;					
						状态：
						<select id="state" name="state" id="state">
							<option value="true">是</option>
							<option value="false">否</option>
						</select>
						 -->
						 &nbsp;&nbsp;<label>国家:</label> 
						<select id="countryId" name="countryId">
							<option value="0">全部</option>
							<c:forEach var="country" items="${countrys}">
								<option value="${country.id}" <c:if test="${country.id == param.countryId}">selected='selected'</c:if> >${country.name}(${country.nameCn})</option>
							</c:forEach>
						</select>
						&nbsp;&nbsp;<label>一级分类:</label> 
						<select id="category_parent" name="category_parent" onchange="querySecondCategory(this.value)">
							<option value="0">全部</option>
							<c:forEach var="category" items="${categorys}">
							<c:if test="${category.id<=3}">
								<option value="${category.id}" <c:if test="${category.id == param.category_parent}">selected='selected'</c:if> >${category.name}</option>
							</c:if>
							</c:forEach>
						</select>
						&nbsp;&nbsp;<label>二级分类:</label>
						<select id="categoryId"  name="categoryId" >
							<option value="0">--all--</option>
						</select>
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						<input type="button" class="butsearch" value="重置" onclick="resetQuery(this)"/>		
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
			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall"/></td>
						<td>
							名称(id)
						</td>
						<td>
							cp(id)
						</td>
						<td>
							所属一二级分类
						</td>
						<td>
							logo
						</td>
						<td>
							更新数
						</td>
						<td align="center">
							下载数(实)--(虚)
						</td>
						<td align="center">
							浏览数
						</td>	
						<td align="center">
							开发者状态
						</td>
						<td align="center">
							审核状态
						</td>
						<td>
							pdf下载
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
							<td align="center"><input type="checkbox" name="recordId" value="${obj.id}"/></td>
							<td align="center" title="${obj.packageName }">
								${fn:escapeXml(obj.name)}(${obj.id })
							</td>
							<td align="center">
								${obj.cp.name }(${obj.cp.id })
							</td>
							<td align="center">
								<c:if test="${obj.category.fatherId==2 }">Apps</c:if>
								<c:if test="${obj.category.fatherId==3 }">Games</c:if>
								---- ${obj.category.name }
							</td>
							<td align="center">
								<img alt="大图标" width="50" height="50" src="${path}${obj.bigLogo }">
							</td>
							<td align="center">
								${obj.updateNum }
							</td>
							<td align="center">
								${obj.realDowdload } -- ${obj.initDowdload }
							</td>
							<td align="center">
							${obj.pageOpen }							
							</td>
							<td align="center">
								<c:if test="${obj.cpState==3}">
									<span class="ys1">草稿</span>
								</c:if>							
								<c:if test="${obj.cpState==2}">
									<span class="ys2">未上线</span>
								</c:if>
								<c:if test="${obj.cpState==1 and obj.state==false}">
									<span class="ys3">请求上线</span>
								</c:if>
								<c:if test="${obj.cpState==1 and obj.state==true}">
									<span class="ys4">上线</span>
								</c:if>
								<c:if test="${obj.packageName==null }"> 
									<span class="ys4">无apk</span>	
								</c:if>
							</td>								
							<td align="center">
								<c:if test="${obj.packageName==null }"> 
									<span class="ys4">无apk</span>	
								</c:if>
								<c:if test="${obj.packageName!=null }"> 
									<c:if test="${obj.state==false}">
										<span class="ys1">未通过</span>
									</c:if>
									<c:if test="${obj.state==true}">
										<span class="ys2">通过</span>
									</c:if>
								</c:if>
								
							</td>
							<td align="center">
								<c:if test="${empty obj.pdfUrl }">
									无
								</c:if>
								<c:if test="${!empty obj.pdfUrl }">
									<a href="${path }${obj.pdfUrl }">pdf</a>
								</c:if>
							</td>
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>
							<td align="center">
							<c:if test="${obj.packageName==null }"> 
							   <a href="${ctx }/open/add/1/${obj.id}"><font color="red">apk管理</font></a>
							</c:if>
							<c:if test="${obj.packageName!=null }"> 
							   <a href="${ctx }/open/add/1/${obj.id}">apk管理</a>
							</c:if>	
								<a href="${ctx }/open/add/2/${obj.id}">截图</a>
								<a href="info/${obj.id }">详情</a>
								<a href="${ctx }/appComment/list?appId=${obj.name}&flag=2" target="_BLANK">评论详情</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/open/list" params="${params}"/>
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
		}
		var categoryId = "${param.categoryId}";
		var params = {"name" : $("#name").val(),"appId" : $("#appId").val(),"packageName" : $("#packageName").val(),"state" : $("#state").val(),"categoryId" : categoryId == "" ? 0 : categoryId,"category_parent" : $("#category_parent").val()};
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
			document.getElementById("categoryId").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/open/secondCategory",
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
					url:"${ctx}/open/delete",
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