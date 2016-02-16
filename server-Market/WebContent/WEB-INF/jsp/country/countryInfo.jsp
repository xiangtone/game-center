<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'country';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="${ctx }/country/list"><span id="childTitle"></span></a> -&gt; 平台国家详细信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">查看平台国家信息</div>
			
			<form id="addForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">countryid</td>
							<td class="content">
								${country.id}
							</td>
						</tr>
						<tr>
							<td class="name">国家英文名称</td>
							<td class="content">
								${country.name}
							</td>
						</tr>
							<tr>
							<td class="nameCn">国家中文名称</td>
							<td >
								${country.nameCn}
							</td>
						</tr>
						<tr>
							<td class="name">创建日期</td>
							<td class="content">   								
							    <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${country.createTime}"/>							    
							</td>
						</tr>
											
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/country/list';" value="返回"/>
							</td>
						</tr>
					</tfoot>
				</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>