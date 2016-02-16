<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'market';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="${ctx }/market/list"><span id="childTitle"></span></a> -&gt; 平台详细
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">平台详细</div>
			
			<form id="addForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">应用名</td>
							<td class="content">
								${fn:escapeXml(market.name)}
							</td>
						</tr>
						<tr>
							<td class="name">别名</td>
							<td class="content">
								<textarea  readonly="readonly">${fn:escapeXml(market.anotherName)}</textarea>
							</td>
						</tr>							
							<tr>
							<td class="name">所属渠道(id)</td>
							<td class="content">
								${market.channel.name}(${market.channel.id})
							</td>
						</tr>
						<tr>
							<td class="name">cpId</td>
							<td class="content">
								${market.cp.name}(${market.cp.id})
							</td>
						</tr>
						<tr>
							<td class="name">发行商</td>
							<td class="content">
								${market.issuer}
							</td>
						</tr>
						<tr>
							<td class="name">应用分类</td>
							<td class="content_info">
								平台应用
							</td>
						</tr>
						
						<tr>
							<td class="name">描述</td>
							<td >
								${market.brief }
							</td>
						</tr>
						<tr>
							<td class="name">长的描述</td>
							<td >
								${market.description }
							</td>
						</tr>
						<tr>
							<td class="name">图标</td>
							<td >
								<img alt="" src="${path}${market.logo }" width="50" height="50">
							</td>
						</tr>
						<tr>
							<td class="name">大图标</td>
							<td >
								<img alt="" src="${path}${market.bigLogo }" width="50" height="50">
							</td>
						</tr>
						<tr>
							<td class="name">星级</td>
							<td >
								${market.stars }
							</td>
						</tr>
						<tr>
							<td class="name">appannie评分</td>
							<td >
								${market.starsInit }
							</td>
						</tr>	
						<tr>
							<td class="name">最终的评分</td>
							<td >
								${market.starsReal }
							</td>
						</tr>			
						<tr>
							<td class="name">初始时间</td>
							<td >
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${market.initialReleaseDate }"/>
							</td>
						</tr>
						<tr>
							<td class="name">更新时间</td>
							<td >
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${market.updateTime}"/>
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/market/list';" value="返回"/>
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