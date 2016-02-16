<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'music';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="${ctx }/music/list"><span id="childTitle"></span></a> -&gt; ringtones详细信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">Music详细信息</div>
			<form id="addForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">应用名</td>
							<td class="content">
								${fn:escapeXml(music.name)}
							</td>
						</tr>
						<tr>
							<td class="name">别名</td>
							<td class="content">
								<textarea  readonly="readonly">${fn:escapeXml(music.anotherName)}</textarea>
							</td>
						</tr>																	
						<tr>
							<td class="name">拼音</td>
							<td class="content">
								${fn:escapeXml(music.pinyin) }
							</td>
						</tr>
						<tr>
							<td class="name">所属分类</td>
							<td >
								${fn:escapeXml(music.category.categoryCn) }(${music.category.id})
							</td>
						</tr>
						<tr>
							<td class="name">国家名</td>
							<td >
								${fn:escapeXml(music.country.name) }(${fn:escapeXml(music.country.nameCn)})
							</td>
						</tr>
						<tr>
							<td class="name">歌唱者</td>
							<td class="content">
								${music.artist}
							</td>
						</tr>
						<tr>
							<td class="name">资源类型</td>
							<td class="content_info">
							<c:if test="${music.free==0}">
							    公共资源
							</c:if>
							<c:if test="${music.free==1}">
							    平台
							</c:if>
							<c:if test="${music.free==2}">
							     自运营
							</c:if>	
							</td>
						</tr>
						<tr>
							<td class="name">来源</td>
							<td >
								${fn:escapeXml(music.source) }
							</td>
						</tr>	
						<tr>
							<td class="name">简介</td>
							<td >
								${fn:escapeXml(music.brief) }
							</td>
						</tr>
						
						<tr>
							<td class="name">描述</td>
							<td >
								${fn:escapeXml(music.description) }
							</td>
						</tr>
						<tr>
							<td class="name">图标</td>
							
							<c:choose>
								<c:when test="${music.logo!=null&&music.logo!=''}">
								<td >
								<img alt="图标" width="50" height="50" src="${path}${music.logo }">
								</td>
								</c:when>
								<c:otherwise>
								<td>
									<img alt="图标" width="50" height="50" src="${ctx}/static/res/ringtones_disk_pic.png">
								</td>	
								</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr>
							<td class="name">星级</td>
							<td >
								${music.stars }
							</td>
						</tr>
						<tr>
						<td class="name">大小</td>
		                	<c:if test="${ music.fileSize <1024}">
								<td>${music.fileSize }KB</td>
							</c:if>
							<c:if test="${ music.fileSize >1024}">
								<td>
									<fmt:formatNumber type="number" value="${music.fileSize/1024/1024 }" maxFractionDigits="2"/>MB
								</td>
							</c:if>
						</tr>
						<tr>
							<td class="name">时长</td>
					
						<c:if test="${ music.duration >=3600}">
								<td >
								<ff:formatPro fieldValue="${music.duration/3600 }" type="2" />:<ff:formatPro fieldValue="${(music.duration%3600)/60}" type="2" />:<ff:formatPro fieldValue="${(music.duration%3600)%60}" type="2" />
							   </td>
						</c:if>
						<c:if test="${ music.duration <3600&&music.duration>=60}">
								<td>
								  00:<ff:formatPro fieldValue="${music.duration/60 }" type="2" />:<ff:formatPro fieldValue="${music.duration%60 }" type="2" />
								</td>
						</c:if>
						<c:if test="${ music.duration <60}">
								 <td >
								 00:00:<ff:formatPro fieldValue="${ music.duration}" type="2" />
								</td>	
						</c:if>	
								
						</tr>
						<tr>
							<td class="name">下载</td>
							<td >
								<a href="${path }${music.url }">下载</a>
							</td>
						</tr>
						<tr>
							<td class="name">下载数</td>
							<td >
								(实)${fn:escapeXml(music.realDowdload) }----(虚)${fn:escapeXml(music.initDowdload) }
							</td>
						</tr>
						<tr>
							<td class="name">更新时间</td>
							<td >
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${music.updateTime }"/>
							</td>
						</tr>
						<!--
						<tr>
							<td class="name">后台操作人</td>
							<td >
								${music.operator }
							</td>
						</tr>
						-->
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/music/list';" value="返回"/>
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