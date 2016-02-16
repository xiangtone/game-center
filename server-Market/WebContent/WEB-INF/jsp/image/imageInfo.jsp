<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'image';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="${ctx }/image/list"><span id="childTitle"></span></a> -&gt; wallpapers详细信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">wallpapers详细信息</div>
			
			<form id="addForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">应用名</td>
							<td class="content">
								${fn:escapeXml(image.name)}
							</td>
						</tr>	
						<tr>
							<td class="name">别名</td>
							<td class="content">
								<textarea readonly="readonly">${fn:escapeXml(image.anotherName)}</textarea>
							</td>
						</tr>																
						<tr>
							<td class="name">拼音</td>
							<td class="content">
								${fn:escapeXml(image.pinyin) }
							</td>
						</tr>
						<tr>
							<td class="name">所属分类</td>
							<td >
								${fn:escapeXml(image.category.categoryCn) }(${image.category.id})
							</td>
						</tr>
						<tr>
							<td class="name">国家名</td>
							<td >
								${fn:escapeXml(image.country.name) }(${fn:escapeXml(image.country.nameCn)})
							</td>
						</tr>
						<tr>
							<td class="name">大图尺寸</td>
							<td class="name">
							<c:if test="${ image.width ==0}">
							<font color="red">${image.width }(${image.length })(请上传大图)</font>
							</c:if>
							<c:if test="${ image.width !=0}">
							${image.width }(${image.length })							
							</c:if>
							</td>
						</tr>
						<tr>
							<td class="name">资源类型</td>
							<td class="content_info">
							<c:if test="${image.free==0}">
							    公共资源
							</c:if>
							<c:if test="${image.free==1}">
							    平台
							</c:if>
							<c:if test="${image.free==2}">
							     自运营
							</c:if>	
							</td>
						</tr>
						<tr>
							<td class="name">来源</td>
							<td >
								${fn:escapeXml(image.source) }
							</td>
						</tr>							
						<tr>
							<td class="name">简介</td>
							<td >
								${fn:escapeXml(image.brief) }
							</td>
						</tr>
						
						<tr>
							<td class="name">描述</td>
							<td >
								${fn:escapeXml(image.description) }
							</td>
						</tr>
						<!-- 
						<tr>
							<td class="name">小图</td>
							<td >
							<img alt="小图" width="50" height="50" src="${path}${image.logo }">
							</td>
						</tr>
						 -->
						<tr>
							<td class="name">中图</td>
							<td >
														
								<img alt="中图" width="80" height="80" src="${path}${image.biglogo }	">
								
							</td>
						</tr>
						<tr>
							<td class="name">大图</td>
							<td >
								
								<img alt="大图" width="100" height="100" src="${path}${image.url }">
								
							</td>
						</tr>
						<tr>
							<td class="name">星级</td>
							<td >
								${image.stars }
							</td>
						</tr>
						<tr>
						<td class="name">大图大小</td>
						    <c:if test="${ image.fileSize ==0}"><td><font color="red">${image.fileSize }kb</font></td>
							</c:if>
		                	<c:if test="${ image.fileSize <1024 &&image.fileSize >0}">
								<td>${image.fileSize }KB</td>
							</c:if>
							<c:if test="${ image.fileSize >1024}">
								<td>
									<fmt:formatNumber type="number" value="${image.fileSize/1024/1024 }" maxFractionDigits="2"/>MB
								</td>
							</c:if>
						</tr>						
						<tr>
							<td class="name">下载数</td>
							<td >
								(实)${fn:escapeXml(image.realDowdload) }----(虚)${fn:escapeXml(image.initDowdload) }
							</td>
						</tr>
						<tr>
							<td class="name">更新时间</td>
							<td >
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${image.updateTime }"/>
							</td>
						</tr>
						<!--
						<tr>
							<td class="name">后台操作人</td>
							<td >
								${image.operator }
							</td>
						</tr>
						  -->
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/image/list';" value="返回"/>
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