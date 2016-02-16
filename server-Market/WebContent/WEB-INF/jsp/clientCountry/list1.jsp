<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'clientCountry';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <span id="childTitle"></span>
	</div>
	
	<div class="mainoutside">
		<div class="secmainoutside">
			<div class="mainarea_shop">
				<div class="servicesearch pt18">
					<form action="${ctx }/clientCountry/list" method="post">
						国家名(英文)：<input type="text" name="country" id="country" value="${fn:escapeXml(param.country)}"/>&nbsp;&nbsp;
						<button type="submit" class="butsearch" id="btn-search">查询</button>
					</form>
				</div>
			</div>
			<div class="border">
			<table class="mainlist" width="100%">
				<thead>
					<tr>
						<td align="center">
							国家中文名
						</td>
						<td align="center">
							国家英文名
						</td>
						<td align="center">
							创建日期
						</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${clientCountrys==null}">
						<tr>
							<td colspan="6" align="center">
								没有符合要求的数据！
							</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${clientCountrys}" varStatus="i">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
						<td align="center">
							${fn:escapeXml(obj.countryCn)}
						</td>
						<td align="center">
						<input type="hidden" name="countryCn" id="countryCn_${i.index}" value="${fn:escapeXml(obj.countryCn)}"/>						
						<input type="text" class="z_text" id="country_${i.index}"
							   value="${obj.country}" maxlength="100" readonly="readonly"
							   style="width: 150px;" />
						<a type="button" href="#" class="lang" id="clientCountry_edit_${i.index}" >编辑</a>|
						<a type="button" href="#" class="lang" id="clientCountry_save_${i.index}">保存</a>	   
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
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		 $("a[type=button][id*='clientCountry_edit_']").click(function(val){
			 var index = val.target.id.replace("clientCountry_edit_","");
			 $("input[type=text][id='country_"+index+"']").removeAttr("readonly");
		 });
		 $("a[type=button][id*='clientCountry_save_']").click(function(val){
			 var index = val.target.id.replace("clientCountry_save_","");
			 var country = $("input[type=text][id='country_"+index+"']").val();
			 var countryCn = $("input[id='countryCn_"+index+"']").val();
			$.ajax({
						url:"${ctx}/clientCountry/update",
						type:"POST",
						data:{'countryCn':countryCn,'country':country},
						dataType:"json",
						success:function(response){
							var data = eval("("+response+")");
							if(data.flag == "0"){
								alert("修改成功！");
								window.location.href = "list";
							}else if(data.flag == "2"){
								 alert("请输入"+countryCn+"的英文名称！");
								window.location.href = "list";
							}else{
								alert("修改失败！");
								window.location.href = "list";
							}
						},
						error:function(data){
							alert("修改失败！");
							window.location.href = "list";
						}
					});
			});
	});
	</script>
</body>
</html>