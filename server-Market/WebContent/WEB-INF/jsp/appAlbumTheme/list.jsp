<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appAlbumTheme';
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	<!-- 排序需要传以下2个隐藏域的值,分别为:当前系统服务器请求上下文,数据库表名 -->
	<input type="hidden" id="serverPath" value="${ctx }"/>
	<input type="hidden" id="tableName" value="t_app_album_theme"/>
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx }/appAlbumTheme/list" method="post">
						主题名：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name)}"/>&nbsp;&nbsp;
							&nbsp;&nbsp;国家：
							<select name="raveId" id="raveId">
								<option value="0">全部</option>					
								<c:forEach var="country" items="${countrys}">
									<option value="${country.id}" <c:if test="${country.id==param.raveId}"> selected="selected"</c:if>>${country.name}(${country.nameCn})</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
							&nbsp;&nbsp;<label>一级分类:</label> 
						<select id="appAlbumId" name="appAlbumId" onchange="queryappAlbumColumn(this.value)">
							<option value="0">全部</option>
							<c:forEach var="appAlbum" items="${appAlbums}">
							  <c:if test="${appAlbum.id<=3}">
									<option value="${appAlbum.id}"  <c:if test="${appAlbum.id==param.appAlbumId }">selected="selected"</c:if> >${appAlbum.name}</option>
							  </c:if>
							</c:forEach>
						</select>
						&nbsp;&nbsp;<label>二级分类:</label>
						<select id="appAlbumColumnId"  name="appAlbumColumnId" >
							<option value="0">--all--</option>
						</select>
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
					    <li><a href="${ctx }/appAlbumTheme/add" type="button" class="lang" >添加</a></li>
						<li><a id="cmd-delete" type="button" class="lang" >删除</a></li>
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
						<td>
							id
						</td>
						<td>
							所属分类(英文)
						</td>
						<td>
							类型(英文)
						</td>
						<td align="center">
							国家名
						</td>
						<td>
							主题名称
						</td>
						<td>
							大图标
						</td>
						<td>
							详情
						</td>
						<td>
							排序
						</td>
						<td>
							状态
						</td>
						<td>
							对应资源(id)
						</td>
						<td>
							创建时间
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
							<td align="center"><input type="checkbox" name="recordId" value="${obj.themeId}"/></td>
							<td align="center">
								<a href="${obj.themeId }?page=${result.currentPage}&raveId=${param.raveId}&appAlbumId=${param.appAlbumId}&appAlbumColumnId=${param.appAlbumColumnId}">${obj.themeId }</a>
							</td>
							<td align="center">
								<c:if test="${obj.appAlbumColumn!=null}">
									${obj.appAlbumColumn.appAlbum.name }--->${obj.appAlbumColumn.nameCn}(${obj.appAlbumColumn.name})
								</c:if>
							</td>
							<td align="center">
								<c:if test="${obj.appFile!=null}">
									${obj.appFile.appInfo.category.name}(${obj.appFile.appInfo.category.categoryCn})
								</c:if>
							</td>
							<td align="center">
								${obj.country.name }(${obj.country.nameCn })
							</td>
							<td align="center">
								${fn:escapeXml(obj.name)}
							</td>
							<td align="center">
								<img alt="大图标" width="50" height="50" src="${path}${obj.bigicon }">
							</td>
							<td align="center" style="word-break:break-all; word-wrap:break-word;" title="${fn:escapeXml(obj.description ) }">
								<ff:formatPro fieldValue="${obj.description}" type="1" len="45"/>
							</td>	
							<td align="center">
								<input type="text" id="sort_${obj.themeId }" value="${fn:escapeXml(obj.sort) }" maxlength="9" readonly="readonly" style="width:30px;"/>
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
								${obj.appFile.appName}(${obj.appFile.id})
							</td>
							<td align="center">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}"/>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/appAlbumTheme/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var appAlbumId = "${param.appAlbumId}";
		var appAlbumColumnId = "${param.appAlbumColumnId}";
		var params = {"name" : $("#name").val(),"appAlbumId" : appAlbumId == "" ? 0 :  appAlbumId,"appAlbumColumnId" :appAlbumColumnId,"raveId" : $("#raveId").val()};
		if(null != appAlbumId && appAlbumId != "" ){
			queryappAlbumColumn(appAlbumId);
		}
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
			$.alert("删除正常状态记录时，记录会更新为无效.\n删除无效状态记录时，记录会从库中删除.\n确定要删除？", function() {
				$.post("delete", {
					id : id
				}, function(data) {
					$.alert("删除成功！");
					//window.location.reload();
					window.location.href = "list";
				});
			}, true);
		}
	});
	function queryappAlbumColumn(val){
		if(val == 0){
			$("#appAlbumColumnId").html('');
			document.getElementById("appAlbumColumnId").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/appAlbumTheme/getColumn",
				type : "POST",
				dataType : "json",
				data :{"id":val},
				success: function(response){
	        		var jsonstr = eval("(" + response + ")");
	        		var ss = jsonstr.success.split(",");
	        		$("#appAlbumColumnId>option").remove();
	    			document.getElementById("appAlbumColumnId").add(new Option("--all--",0));
	        		for(var i = 1;i < ss.length; i++){
	        			var va = ss[i].split("-");
	        			document.getElementById("appAlbumColumnId").add(new Option(va[0],va[1]));
	        		}
					var appAlbumColumnId = "${param.appAlbumColumnId}";
					if("" != appAlbumColumnId && appAlbumColumnId != 0){
						$("#appAlbumColumnId").val(appAlbumColumnId);
					}
	            }
				
	        });
		}

	}
	</script>
</body>
</html>