<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'communal';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>&nbsp;&nbsp;<span>App名称(id)注明:(<font color="#912CEE">自营App为紫色,</font><font class="ys1">公用App为绿色,</font>开发者App为黑色)</span>
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form id="searchForm" action="${ctx }/communal/list" method="post">
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
						&nbsp;&nbsp;<label>应用分类:</label> 
						<select id="category_parent" name="category_parent" onchange="querySecondCategory(this.value)">
							<option value="0">全部</option>
							<c:forEach var="category" items="${categorys}">
							<c:if test="${category.id<=3}">
								<option value="${category.id}" <c:if test="${category.id == param.category_parent}">selected='selected'</c:if> >${category.name}</option>
							</c:if>
							</c:forEach>
						</select>
						&nbsp;&nbsp;<label>一级分类:</label>
						<select id="categoryId"  name="categoryId" onchange="change(this.value)">
							<option value="0">--all--</option>
						</select>
					&nbsp;&nbsp;二级分类:<select name="secondId" id="secondId">
						<option value="0">--all--</option>
					</select>
						<button type="submit" class="butsearch" id="btn-search">查询</button>
						<input type="button" class="butsearch" value="重置" onclick="resetQuery(this)"/>		
						<button type="button" class="bigbutsubmit" id="export_appOperant" onclick="exportExcel()">导 出</button>
						
						<button type="button" class="butsearch" id="te" onclick="testIn()">testIn</button>
						<input type="hidden" value="<%=Constant.APP_TEST_URL %>" id="ur" name="ur">
					</form>
				</div>
				<div class="operateShop">
					<ul>
					    <li><a href="${ctx }/communal/add">添加</a></li>
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
							所属一二分类
						</td>
						<td>
							分类级别
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
								<c:if test="${obj.free==3 }">
									${fn:escapeXml(obj.name)}(${obj.id })
								</c:if>
								<c:if test="${obj.free!=3 }">
									<c:if test="${obj.free==2 }">
									<a href="${obj.id }?page=${result.currentPage}"><font color="#912CEE">${fn:escapeXml(obj.name)}(${obj.id })</font></a>									
									</c:if>
									<c:if test="${obj.free!=2 }">
									<a href="${obj.id }?page=${result.currentPage}">${fn:escapeXml(obj.name)}(${obj.id })</a>									
									</c:if>
								</c:if>
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
								${obj.updateNum }
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
							<c:if test="${obj.packageName==null }"> 
							   <a href="${ctx }/communalFile/list/${obj.id}"><font color="red">apk管理</font></a>
							</c:if>
							<c:if test="${obj.packageName!=null }"> 
							   <a href="${ctx }/communalFile/list/${obj.id}">apk管理</a>
							</c:if>	
								<a href="${ctx }/communal/add/2/${obj.id}">截图</a>
								<a href="info/${obj.id }">详情</a>
								<a href="${ctx }/appComment/list?appId=${obj.name}&flag=2" target="_BLANK">评论详情</a>
								<c:if test="${obj.packageName!=null }"> 	
									<c:if test="${(obj.state==true && obj.free==3) || obj.free!=3}">
										<a href="${ctx }/communal/appConfig/${obj.id}?apkId=0">配置</a>
									</c:if>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/communal/list" params="${params}"/>
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
			document.getElementById("categoryId").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/communal/secondCategory",
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
	
	function checkId(flag){
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
			if(flag==1){
				$.alert("请选择记录");
			}
			return '';
		} else {
			return id;
		}
	}
	
	$("#cmd-delete").click(function() {
		var id = checkId(1);
		if (id != ''){
			$.alert("删除后将不可恢复，确定删除？", function() {
				$.ajax({
					url:"${ctx}/communal/delete",
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
	
	function testIn(){
		var id = checkId(0);
		if (id != ''){
			window.open($("#ur").val()+"?appIds="+id); 
		}else{
			if(confirm("未选择应用,是否需要跳转到Testin提交页面?")){
				window.open($("#ur").val()); 
			}
		}
	}
	//导出
	function exportExcel(){
		//导出调用
		var xmlName = "appADayAgoInfo.xml";
		window.document.location = "${ctx}/communal/export2Excel?xmlName="
				+ xmlName;
	}
	
	</script>
</body>
</html>