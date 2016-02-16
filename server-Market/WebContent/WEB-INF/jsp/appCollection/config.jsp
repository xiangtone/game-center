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
		var menu_flag = 'appCollection';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span> -&gt; <font>${country.name}(${country.nameCn})----${appCollection.name}(${appCollection.nameCn})</font>
			&nbsp;&nbsp;<span>App名称(id->Country)注明:(<font color="#912CEE">自营App为紫色,</font><font class="ys1">公用App为绿色,</font>开发者App为黑色)</span>	
	</div>
	
	<div class="mainoutside pt18">
		<div id="secmainoutside">
		 <div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td>
							<form id="searchForm" action="${ctx}/appAlbumColumn/${appCollection.collectionId}/collectionConfig" method="post">
									app名字或appId：<input type="text" name="appnameid" id="appnameid" value="${fn:escapeXml(appnameid)}"/>&nbsp;&nbsp;
									请选择: 
									<select id="category_parent" name="category_parent" onchange="querySecondCategory(this.value)">
										<option value="0" <c:if test="${category_parent==0}">selected="selected"</c:if>>--all--</option>
										<option value="2" <c:if test="${category_parent==2}">selected="selected"</c:if>>Apps</option>
										<option value="3" <c:if test="${category_parent==3}">selected="selected"</c:if>>Games</option>
									</select>
									-
									<select id="categoryId" name="categoryId" >
										<option value="0">--all--</option>
									</select>
								<input type="hidden" id="raveId" name="raveId" value="${raveId}"/>	
								<input type="hidden" id="type" name="type" value="${type}"/>	
								<input type="submit" class="bigbutsubmit" id="btn-search" value="查询" />
								<input type="button" class="bigbutsubmit" value="重置" onclick="resetQuery()"/>								
								<input onclick="window.location.href='${ctx }/appAlbumColumn/collectionRes/${appCollection.collectionId}?raveId=${raveId}&type=${type}'" class="bigbutsubmit" type="button" value="返回" >
							</form>
						</td>
						<td class="btn" align="right">
						<form id="editForm" action="${ctx }/appAlbumColumn/${appCollection.collectionId}/collectionDoConfig" method="post">
							<input type="hidden" id="raveId" name="raveId" value="${raveId}"/>
							<input type="hidden" id="type" name="type" value="${type}"/>		
							<input type="hidden" name="collectionId" id="collectionId" value="${appCollection.collectionId}" >
							<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
							<input type="reset" class="bigbutsubmit" value="重填" onclick="check(0)"/>
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
											<tr height="55">
											<td width="50">
											<input type="checkbox" style="cursor:hand;width:40px;height:40px;" name="menus" value="${app.id}"  />
											</td>
											<td width="150" >
											<c:if test="${app.free==3 }">
											${app.appName}(${app.appId}-->${app.country.name}(${app.country.nameCn}))
											</c:if>
											<c:if test="${app.free!=3 }">
												<c:if test="${app.free==2 }">
												<font color="#912CEE">${app.appName}(${app.appId}-->${app.country.name}(${app.country.nameCn}))</font>									
												</c:if>
												<c:if test="${app.free!=2 }">
												<font class="ys1">${app.appName}(${app.appId}-->${app.country.name}(${app.country.nameCn}))</font>								
												</c:if>
											</c:if>
											
											</td>
											<td>
											<img alt="${app.appName }" src="${path}${app.bigLogo}" width="50" height="50">
											</td>
											</tr>
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
				</form>
				</div>
				<div class="page">
					<div class="pageContent">
						<c:if test="${result.recordCount != 0}">
							<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/appAlbumColumn/${appCollection.collectionId}/collectionConfig" params="${params}"/>
						</c:if>
					</div>
				</div>
		<div id="goTopBtn">返回顶部</div>
		
	</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		var categoryId = "${param.categoryId}";
		var params = {"appnameid" : $("#appnameid").val(),"categoryId" : categoryId == "" ? 0 : categoryId,"category_parent" : $("#category_parent").val(),"raveId":"${raveId}","type":"${type}"};
		paginationUtils(params);
		$("#editForm").validate({
			submitHandler: function(form) {
				$("#butsubmit_id").attr("disabled","true");
				var checks = $("input[type='checkbox']");
				var check = false;
				for(var i = 0;i<checks.length;i++){
					if($(checks[i]).attr("checked") == true){
						check = true;
					}
				}
				if(check == false){
					$.alert("温馨提示:您必须选择一项!");
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
	            		}else if(data.flag==1){
	            			$.alert('配置失败,请重新输入');
	                		$("#butsubmit_id").removeAttr("disabled");

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
		if(confirm("确实要重置吗?")){
	    	$("#category_parent").val("0");
	   		$("#categoryId").val("0");
	   	 	var name =$(":text");
			name.val("");
		}
	}
	</script>
</body>
</html>