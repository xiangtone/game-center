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
	   var resType = "${param.resType}";
		var menu_flag =null;
	   if(resType!='theme'){
			menu_flag = 'appAlbumColumn';
	   }else if(resType=='theme'){
		   menu_flag = 'musicAlbumTheme';
	   }
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span> -&gt;
		<c:if test="${param.resType != 'theme' }"><font>${country.name}(${country.nameCn})--${param.appAlbumName }--${param.appAlbumColumnName}</font></c:if>
		<c:if test="${param.resType == 'theme' }"><font>${country.name}(${country.nameCn})--Ringtones--Theme</font></c:if>
	</div>
	
	<div class="mainoutside pt18">
		<div id="secmainoutside">
		<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td>
							<form id="searchForm" action="${ctx}/ringtones/<c:if test="${param.resType == 'theme'}">${param.themeId }</c:if><c:if test="${param.resType != 'theme' }">${requestScope.columnId}</c:if>/config" method="post">
									Music名字或MusicId：<input type="text" name="musicNameId" id="musicNameId" value="${fn:escapeXml(musicNameId)}" maxlength="100"/>&nbsp;&nbsp;
									请选择: 
									<select id="category_parent" name="category_parent" onchange="querySecondCategory(this.value)">
										<option value="0" <c:if test="${category_parent==0}">selected="selected"</c:if>>--all--</option>
										<option value="4" <c:if test="${category_parent==4}">selected="selected"</c:if>>Ringtones</option>
										<%-- 
										<option value="2" <c:if test="${category_parent==2}">selected="selected"</c:if>>Apps</option>
										<option value="3" <c:if test="${category_parent==3}">selected="selected"</c:if>>Games</option>
										<option value="5" <c:if test="${category_parent==5}">selected="selected"</c:if>>Wallpaper</option>
										--%>
									</select>
									-
									<select id="categoryId" name="categoryId" >
										<option value="0">--all--</option>
									</select>
								<input type="hidden" id="raveId" name="raveId" value="${raveId}"/>																
								<input type="hidden" name="themeId" value="${param.themeId }">
								<input type="hidden" name="resType" value="${param.resType }">
								<input type="hidden" name="appAlbumId" value="${param.appAlbumId }">
								<input type="hidden" name="appAlbumName" value="${param.appAlbumName }">
								<input type="hidden" name="appAlbumColumnName" value="${param.appAlbumColumnName }">
								<input type="submit" class="bigbutsubmit" id="btn-search" value="查询" />
								<input type="button" class="bigbutsubmit" value="重置" onclick="resetQuery()"/>								
								<input onclick="window.location.href='${ctx }/ringtones/listRes/<c:if test="${param.resType == 'theme'}">${param.themeId }?resType=theme&raveId=${raveId}</c:if><c:if test="${param.resType != 'theme' }">${requestScope.columnId}?raveId=${raveId}</c:if>'" class="bigbutsubmit" type="button" value="返回" >
							</form>
						</td>
						<td class="btn" align="right">
						<form id="editForm" action="${ctx }/ringtones/<c:if test="${param.resType == 'theme'}">${param.themeId }</c:if><c:if test="${param.resType != 'theme' }">${requestScope.columnId}</c:if>/doConfig" method="post">
							<input type="hidden" id="raveId" name="raveId" value="${raveId}"/>								
							<input type="hidden" name="themeId" value="${param.themeId }">
							<input type="hidden" name="resType" value="${param.resType }">
							<input type="hidden" name="appAlbumId" value="${param.appAlbumId }">
							<input type="hidden" name="appAlbumName" value="${param.appAlbumName }">
							<input type="hidden" name="appAlbumColumnName" value="${param.appAlbumColumnName }">
							<input type="hidden" name="columnId" id="columnId" value="${requestScope.columnId}" >
							<input type="hidden" name="albumId" id="albumId" value="${param.appAlbumId}" >
							<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
							<input type="reset" class="bigbutsubmit" value="重选" onclick="check(0)"/>
							<input type="button" class="bigbutsubmit" value="全选" onclick="check(1)"/>
						</td>
						</tr>
						<tr>
							<td colspan="2">
								<table>
								<c:set scope="page" value="0" var="count"></c:set>
								<c:forEach begin="1" end="${counts}" varStatus="counts_index" step="1" >
									<tr height="55">
										<c:forEach items="${result.data }" var="app" begin="${count }" end="${count + 3 }" step="1" varStatus="app_index">
											<td>
											<table>
											<tr height="55"><td width="50"><input type="checkbox" style="cursor:hand;width:40px;height:40px;" name="menus" value="${app.id}"  /></td><td width="150" >${app.name}(${app.country.name}(${app.country.nameCn}))</td>
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
												</table>
											</td>
										</c:forEach>
									</tr>
									<c:set scope="page" value="${count + 4}" var="count"/>
								</c:forEach>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
				</div>
				</form>
				<div class="page">
					<div class="pageContent">
						<c:if test="${result.recordCount != 0}">
							<c:if test="${param.resType == 'theme'}">
							<ccgk:pagination paginationVo="${result}" contextPath="${ctx }/ringtones/${param.themeId }/config" params="${params}"/>
							</c:if>
							<c:if test="${param.resType != 'theme' }">
							<ccgk:pagination paginationVo="${result}" contextPath="${ctx }/ringtones/${requestScope.columnId}/config" params="${params}"/>
							</c:if>
						</c:if>
					</div>
				</div>
		<div id="goTopBtn">返回顶部</div>
		
	</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		var categoryId = "${param.categoryId}";
		var params = {"musicNameId" : $("#musicNameId").val(),
				"categoryId" : categoryId == "" ? 0 : categoryId,
				"category_parent" : $("#category_parent").val(),
				"appAlbumName" : "${param.appAlbumName}",
				"appAlbumColumnName" : "${param.appAlbumColumnName}",
				"appAlbumId" : "${param.appAlbumId}",
				"themeId" : "${param.themeId}",
				"resType" : "${param.resType}",
				"raveId" : "${param.raveId}"};
		paginationUtils(params);
		$("#editForm").validate({
			submitHandler: function(form) {
				$("#butsubmit_id").attr("disabled","true");
				var checks = $("input[type='checkbox']:checked");
				if(checks.length == 0){
					$.messager.alert("提示:","您必须选择一项!");
					$("#butsubmit_id").removeAttr("disabled");
					return;
				}

				$(form).ajaxSubmit({
					 success: function(response){
		        		var data = eval("("+response+")");
		        		if(data.flag==0){
	            			$.alert('配置成功', function(){
	            				$("#btn-search").click();
	            			});
	            		}else{
	                		$.alert('配置失败,请重新输入');
	                		$("#butsubmit_id").removeAttr("disabled");
	            		}
		            }
	            });
			},
			success: function(label){
				var labelparent = label.parent();
				label.parent().html(labelparent.next().html()).attr("class", "valid");
			},
			onkeyup:false
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
	  
	  querySecondCategory('${category_parent}');
	});
	function check(flag){
		if(flag == 0){
			$("input[type='checkbox']").attr("checked",false);
		}else{
			$("input[type='checkbox']").attr("checked",true);
		}
	}
	
	function querySecondCategory(value){
		if(value == 0){
			$("#categoryId").html('');
			//$("#categoryId").append("<option value='0'>--all--</option>");
			document.getElementById("categoryId").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/appAlbumColumn/secondCategory",
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
    	$("#category_parent").val("0");
   		$("#categoryId").val("0");
   	 	$("#musicNameId").val("");
	}
	</script>
</body>
</html>