<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appInfoConfig';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
		<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx }/appInfoConfig/list" method="post">
						应用名：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name)}"/>&nbsp;&nbsp;
						包名：<input type="text" name="packageName" id="packageName" value="${fn:escapeXml(param.packageName)}"/>&nbsp;&nbsp;
						
						分类：
							<select name="type" id="type" >
								<option id="type" value="-1">全部</option>
								<option id="type" value="3">自动更新</option>
								<option id="type" value="2">需要增量</option>
								 <option id="type" value="0">黑名单</option>
								<option id="type" value="1">忽略</option>
								<!--
								<option id="type" value="5">Top500Home</option>
								 -->
							</select>&nbsp;&nbsp;
					 更新状态:
							<select name="state" id="state">
								<option id="state" value="-1">全部</option>
								<option id="state" value="0">历史数据</option>
								<option id="state" value="1">最新增加</option>
						</select> 
						&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
				<div class="operateShop">
					<ul>
					    <li><a id="batchImport">批量导入</a></li>
					    <li><a href="${ctx}/static/document/appconfig_upload_template.xlsx" class="d">批量导入模板下载</a></li>
						<li><a href="${ctx }/appInfoConfig/add">添加</a></li>
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
						<td align="center">
							应用名称
						</td>
						<td align="center">
							包名
						</td>
						<td align="center">
							描述
						</td>
						<td align="center">
							分类
						</td>
						<td align="center">
							更新状态
						</td>						
						<td align="center">
							应用链接地址
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
						<td align="center">
							<a href="${obj.id }">${fn:escapeXml(obj.name)}</a>
						</td>
						<td align="center">
							${fn:escapeXml(obj.packageName)}
						</td>
						<td align="center" title="${fn:escapeXml(obj.description)}">				
							<ff:formatPro fieldValue="${fn:escapeXml(obj.description)}" type="1" len="15"/>
						
						</td>
						
						<td align="center">
							<c:if test="${obj.type==0}">黑名单</c:if>
							<c:if test="${obj.type==1}">忽略</c:if>
							<c:if test="${obj.type==3}">自动更新</c:if>
							<c:if test="${obj.type==2}">需要增量</c:if>
							<!-- 
							<c:if test="${obj.type==5}">Top500Home</c:if>
							 -->
						</td>
						<td align="center">
							<c:if test="${obj.state==false}">历史数据</c:if>
							<c:if test="${obj.state==true}">最新增加</c:if>
						</td>
						<td align="center">
						<c:if test="${obj.type==3}">${obj.appUrl}</c:if>							
						</td>
						<td align="center">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime }"/>
						</td>
						<td align="center">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.updateTime }"/>
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${cps.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}" contextPath="${ctx}/appInfoConfig/list" params="${params}"/>
					</c:if>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
	     var button = $('#batchImport'), interval;
	       new AjaxUpload(button,{
	           action: '${ctx}/appInfoConfig/batchImport',
	           name: 'myfiles',
	           onSubmit : function(file, ext){
	        	   this.disable();
	        	   if (!(ext && /^(xls|xlsx)$/.test(ext))) {
	        		   button.text('批量导入');
	        		   $.alert('文件上传格式不正确,请上传Excel文件!', '系统提示');
	                    return false;
	                }
	               button.text('文件上传中！！！');
	               interval = window.setInterval(function(){
	                   var text = button.text();
	                   if (text.length < 14){
	                       button.text(text + '.');                   
	                   } else {
	                       button.text('文件上传中....');            
	                   }
	               }, 2000);
	           },
	           onComplete: function(file, response){
	        	   this.enable();
	        	   button.text('批量导入');
	        	   window.clearInterval(interval);
	        	  // $.alert(response);
	        	   $.alert(response, function() {
	        	   window.location.href = "list";
	        	   });
	           }
	       });
	});
	$(function(){
		var type = "${param.type}";
		if(type == null || type == ''){
			$("#type option[value=true]").attr("selected","selected");
		}else{
			$("#type option[value='"+ type +"']").attr("selected","selected");
		}
		var state = "${param.state}";
		if(state == null || state == ''){
			$("#state option[value=true]").attr("selected","selected");
		}else{
			$("#state option[value='"+ state +"']").attr("selected","selected");
		}
		var params = {"name" : $("#name").val(),"state" : $("#state").val(),"type" : $("#type").val()};
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
				$.get("delete", {
					id : id
				}, function(data) {
					$.alert("删除成功！");
					//window.location.reload();
					window.location.href = "list";
				});
			}, true);
		}
	});
	</script>
</body>
</html>