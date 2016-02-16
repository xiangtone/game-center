<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'clientfeedback';
	</script>
</head>
<body>	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 常用回馈管理-&gt; 查看详情
	</div>	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">查看详情</div>		
			<div class="border">	
				<table class="mainadd">
					<tbody>																		
						<tr>
							<td class="name">client回馈</td>
							<td class="content" style="word-break:break-all; word-wrap:break-word;">
							<label><ff:formatPro fieldValue="${clientFeedbackZapp.question}" type="1" len="1000"/></label>
							</td>
						</tr>	
						<tr>
							<td class="name">回答内容</td>
							<td class="content" style="word-break:break-all; word-wrap:break-word;">
								<label><ff:formatPro fieldValue="${clientFeedbackZapp.replyContent}" type="1" len="1000"/></label>
														
							</td>
						</tr>
						<tr>
							<td class="name">是否有效</td>
							<td class="content">
							<c:if test="${clientFeedbackZapp.state==false}">
							   无效
							</c:if>
							<c:if test="${clientFeedbackZapp.state==true}">
							    有效
							</c:if>
							</td>
						</tr>	
						<tr>
							<td class="name">创建时间</td>
							<td >
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${clientFeedbackZapp.createTime }"/>
							</td>
						</tr>			
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx }/clientfeedback/zapp/list';" value="返回"/>
							</td>
						</tr>
					</tfoot>
				</table>
				</div>
		</div>
	</div>
</body>
</html>