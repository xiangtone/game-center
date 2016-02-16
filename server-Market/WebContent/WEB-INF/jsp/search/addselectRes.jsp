<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<style type="text/css">
	#goTopBtn {
	display: none;
	width: 18px;
	line-height: 1.2;
	padding: 5px 0;
	background-color: #000;
	color: #fff;
	font-size: 12px;
	text-align: center;
	position: fixed;
	_position: absolute;
	right: 10px;
	bottom: 100px;
	_bottom: "auto";
	cursor: pointer;
	opacity: .6;
	filter: Alpha(opacity=60);
	}
	</style>
	<script type="text/javascript">
		var menu_flag = 'search';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>-<font>
		<c:if test="${searchKeyword.albumId==2}">
		App&Game--
		</c:if>
		<c:if test="${searchKeyword.albumId==4}">Ringtones--</c:if>
		<c:if test="${searchKeyword.albumId==5}">Wallpaper--</c:if>${country.name}(${country.nameCn})</font> -&gt;添加<c:if test="${searchKeyword.flag==1}">资源</c:if><c:if test="${searchKeyword.flag==2}">列表</c:if>
		<c:if test="${searchKeyword.albumId==2}">
		&nbsp;&nbsp;<span>App名称(id->Country)注明:(<font color="#912CEE">自营App为紫色,</font><font class="ys1">公用App为绿色,</font>开发者App为黑色)</span>
		</c:if>
	</div>
	
	<div class="mainoutside pt18">
		<div id="secmainoutside">
		<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td>
							<form id="searchForm" action="${ctx}/search/addSelectRes" method="post">
									资源名字或资源Id：<input type="text" name="appnameid" id="appnameid" value="${fn:escapeXml(appnameid)}"/>&nbsp;&nbsp;
									请选择: 
									<select id="category_parent" name="category_parent" onchange="querySecondCategory(this.value)">
										<c:if test="${searchKeyword.albumId==2}">
											<option value="0" <c:if test="${category_parent==0}">selected="selected"</c:if>>--all--</option>
											<option value="2" <c:if test="${category_parent==2}">selected="selected"</c:if>>Apps</option>
											<option value="3" <c:if test="${category_parent==3}">selected="selected"</c:if>>Games</option>
										</c:if>								
										<c:if test="${searchKeyword.albumId==4}">
										   <option value="4" <c:if test="${category_parent==4}">selected="selected"</c:if>>Ringtones</option>										
										</c:if>
										<c:if test="${searchKeyword.albumId==5}">
											<option value="5" <c:if test="${category_parent==5}">selected="selected"</c:if>>Wallpaper</option>
										</c:if>
										
									</select>
									-
									<select id="categoryId" name="categoryId" >
										<option value="0">--all--</option>
									</select>
								<input type="hidden" id="raveId" name="raveId" value="${searchKeyword.raveId}"/>	
								<input type="hidden" id="albumId" name="albumId" value="${searchKeyword.albumId}"/>		
								<c:if test="${menus1!=null and menus1!=''}">							
								<input type="hidden" name="menus1" value="${menus1}"/>	
								</c:if>		
								<c:if test="${searchKeyword.flag==1}">							
							    <input type="hidden" name="urlFilePath" value="${urlFilePath}"/>	
								</c:if>
								<input type="hidden" id="iconId" name="iconId" value="${searchKeyword.iconId}"/>						
								<input type="hidden" id="flag" name="flag" value="${searchKeyword.flag}"/>
								<input type="hidden" id="keyword" name="keyword" value="${searchKeyword.keyword}"/>
								<input type="hidden" id="sort" name="sort" value="${searchKeyword.sort}"/>
								<input type="submit" class="bigbutsubmit" id="btn-search" value="查询" />
								<input type="button" class="bigbutsubmit" value="重置" onclick="resetQuery()"/>								
								<input type="button" class="bigbutsubmit"  id="butreturn_id" value="返回" >
							
							</form>
						</td>
						<td class="btn" align="right">
							<input type="button" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
							<c:if test="${searchKeyword.flag!=1}">
							<input type="button" class="bigbutsubmit" value="全选" onclick="check(1)"/>
							</c:if>
							<input type="reset" class="bigbutsubmit" value="重填" onclick="check(0)"/>					
						</td>
						</tr>
						<tr>
							<td colspan="2">
								<form id="editForm"  action="${ctx}/search/showAdd" method="post">
							<input type="hidden" id="raveId" name="raveId" value="${searchKeyword.raveId}"/>	
							<c:if test="${menus1!=null and menus1!=''}">							
							<input type="hidden" name="menus1" value="${menus1}"/>	
							</c:if>	
							<c:if test="${searchKeyword.flag==1}">							
								<input type="hidden" name="urlFilePath" value="${urlFilePath}"/>	
							</c:if>
							<input type="hidden" id="iconId" name="iconId" value="${searchKeyword.iconId}"/>	
							<input type="hidden" id="albumId" name="albumId" value="${searchKeyword.albumId}"/>									
							<input type="hidden" id="flag" name="flag" value="${searchKeyword.flag}"/>
							<input type="hidden" id="keyword" name="keyword" value="${searchKeyword.keyword}"/>
							<input type="hidden" id="sort" name="sort" value="${searchKeyword.sort}"/>
								<table>
								<c:set scope="page" value="0" var="count"></c:set>
								<c:forEach begin="1" end="${counts}" varStatus="counts_index" step="1" >
									<tr height="55">
										<c:forEach items="${result.data }" var="app" begin="${count }" end="${count + 3 }" step="1" varStatus="app_index">
											<td>
											<table>
											<tr height="55">
											<c:if test="${searchKeyword.albumId==2}">
												<td width="50">
												<c:choose>
													<c:when test="${searchKeyword.flag==1}">
														<input type="radio" style="cursor:hand;width:25px;height:25px;" name="menus" value="${app.appInfo.id}"  />						
													</c:when>
													<c:otherwise>
														<input type="checkbox" style="cursor:hand;width:40px;height:40px;" name="menus" value="${app.appInfo.id}"  />										
													</c:otherwise>
												</c:choose>	
												
												</td>
												<td width="150" >
											<c:if test="${app.appInfo.free==3 }">
											${app.appName}(${app.appId}-->${app.country.name}(${app.country.nameCn}))
											</c:if>
											<c:if test="${app.appInfo.free!=3 }">
												<c:if test="${app.appInfo.free==2 }">
												<font color="#912CEE">${app.appName}(${app.appId}-->${app.country.name}(${app.country.nameCn}))</font>									
												</c:if>
												<c:if test="${app.appInfo.free!=2 }">
												<font class="ys1">${app.appName}(${app.appId}-->${app.country.name}(${app.country.nameCn}))</font>								
												</c:if>
											</c:if>
												</td>
												<td>
												<img alt="${app.appName }" src="${path}${app.logo}" width="50" height="50"></td>
											</c:if>
											<c:if test="${searchKeyword.albumId==4}">
												<td width="50">
												<c:choose>
													<c:when test="${searchKeyword.flag==1}">
														<input type="radio" style="cursor:hand;width:25px;height:25px;" name="menus" value="${app.id}"  />						
													</c:when>
													<c:otherwise>
														<input type="checkbox" style="cursor:hand;width:40px;height:40px;" name="menus" value="${app.id}"  />										
													</c:otherwise>
												</c:choose>													
												</td>
												<td width="150" >${app.name}(${app.id}-->${app.country.name}(${app.country.nameCn}))</td>
												<c:choose>
													<c:when test="${app.logo!=null&&app.logo!=''}">
														<td align="center">
															<img alt="${app.name }" width="50" height="50" src="${path}${app.logo }">
														</td>
													</c:when>
												<c:otherwise>
													<td align="center">
														<img alt="${app.name }" width="50" height="50" src="${ctx}/static/res/ringtones_disk_pic.png">
													</td>
													</c:otherwise>
												</c:choose>		
											</c:if>
											<c:if test="${searchKeyword.albumId==5}">
												<td width="50">
												<c:choose>
													<c:when test="${searchKeyword.flag==1}">
														<input type="radio" style="cursor:hand;width:25px;height:25px;" name="menus" value="${app.id}"  />						
													</c:when>
													<c:otherwise>
														<input type="checkbox" style="cursor:hand;width:40px;height:40px;" name="menus" value="${app.id}"  />										
													</c:otherwise>
												</c:choose>													</td>
												<td width="150" >${app.name}(${app.id}-->${app.country.name}(${app.country.nameCn}))</td><td><img alt="${app.name }" src="${path}${app.biglogo}" width="50" height="50"></td>																						
											</c:if>
											</tr>
											</table>
											</td>
										</c:forEach>
									</tr>
									<c:set scope="page" value="${count + 4}" var="count"/>
								</c:forEach>
								</table>
							</form>
							</td>
						</tr>
					</tbody>
				</table>
				</div>
				<div class="page">
					<div class="pageContent">
						<c:if test="${result.recordCount != 0}">
							<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/search/addSelectRes" params="${params}"/>
						</c:if>
					</div>
				</div>
		<div id="goTopBtn">返回顶部</div>
		
	</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		var categoryParent = "${param.category_parent}";
		if('${searchKeyword.albumId==4||searchKeyword.albumId==5}'=='true'){
			categoryParent = '${searchKeyword.albumId}';
		}
		if(null != categoryParent && categoryParent != "" ){
			querySecondCategory(categoryParent);
		}
		var categoryId = "${param.categoryId}";
		var params = {"appnameid" : $("#appnameid").val(),"categoryId" : categoryId == "" ? 0 : categoryId,"category_parent" : $("#category_parent").val(),
				"raveId":"${searchKeyword.raveId}","albumId":"${searchKeyword.albumId}",
				"menus1" :"${menus1}","flag" : "${searchKeyword.flag}","keyword" : "${searchKeyword.keyword}",
				"sort" : "${searchKeyword.sort}"
		};
		paginationUtils(params);
		
		$("#butreturn_id").click(function(){
			check(0);
			$("#editForm").submit();
		});
		
		$("#butsubmit_id").click(function(){
				var checks = $("input[type='checkbox']");
				var radios = $("input[type='radio']");
				var radio = false;
				var check = false;
				var flag = '${searchKeyword.flag}';
				for(var i = 0;i<radios.length;i++){
					if($(radios[i]).attr("checked") == true){
						radio = true;
					}
				}
				for(var i = 0;i<checks.length;i++){
					if($(checks[i]).attr("checked") == true){
						check = true;
					}
				}
				if(check == false&&flag==2){
					$.alert("温馨提示:您必须选择一项!");
					$("#butsubmit_id").removeAttr("disabled");
					return;
				}
				if(radio == false&&flag==1){
					$.alert("温馨提示:您必须选择一项!");
					$("#butsubmit_id").removeAttr("disabled");
					return;
				}
				$("#editForm").submit();

			});
		/**
		*设置返回顶部按钮
		*/
		$("div").scroll(function(){
		  var sc= $(this).scrollTop();
		  var rwidth = $(this).width();
      	  if(sc > 10){
				$("#goTopBtn").css("display","block");
				$("#goTopBtn").css("left",(rwidth + 125)+ "px");
			}else{
				$("#goTopBtn").css("display","none");
			}
		  });
			  	
	  $("#goTopBtn").click(function(){
		 $('div').animate({scrollTop:10},400);
		});
	  
	});
	function check(flag){
		if(flag == 0){
			$("input[type='checkbox']").attr("checked",false);
			$("input[type='radio']").attr("checked",false);
		}else{
			$("input[type='checkbox']").attr("checked",true);
			$("input[type='radio']").attr("checked",true);
		}
	}
	
	function querySecondCategory(value){
		if(value == 0){
			$("#categoryId").html('');
			//$("#categoryId").append("<option value='0'>--all--</option>");
			document.getElementById("categoryId").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/search/secondCategory",
				type : "POST",
				dataType : "json",
				data : {"id" : value,"categoryId":'${categoryId}'},
				success : function (data){
					var result = eval("(" + data + ")");
					if(result.success == 1){
						$("#categoryId").html('');
						$("#categoryId").append(result.option);
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
	    	if('${searchKeyword.albumId==4||searchKeyword.albumId==5}'=='true'){
		    	$("#category_parent").val("${searchKeyword.albumId}");
			}else{
		    	$("#category_parent").val("0");
			}
	   		$("#categoryId").val("0");
	   	 	var name =$(":text");
			name.val("");
		}
	}
	</script>
</body>
</html>
