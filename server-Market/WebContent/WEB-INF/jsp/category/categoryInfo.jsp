<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'category';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 分类详细
	</div>
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">查看分类详细</div>
			<form id="addForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">分类名</td>
							<td class="content">
								${fn:escapeXml(category.categoryCn)}
							</td>
						</tr>
							<tr>
							<td class="name">图标</td>
							<td class="content">
								${fn:escapeXml(category.icon)}
							</td>
						</tr>
							<tr>
							<td class="name">备注</td>
							<td >
								${fn:escapeXml(category.recommend) }
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/category/list';" value="返回"/>
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