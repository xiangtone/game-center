<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'channel';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="${ctx }/channel/list"><span id="childTitle"></span></a> -&gt; 渠道详细
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">查看渠道信息</div>
			<form id="addForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">渠道id</td>
							<td class="content">
								${channel.id}
							</td>
						</tr>
						<tr>
							<td class="name">父渠道名</td>
							<td class="content">
								${fn:escapeXml(channel.fatherName)}
							</td>
						</tr>
							<tr>
							<td class="name">渠道名</td>
							<td class="content">
								${fn:escapeXml(channel.name)}
							</td>
						</tr>
							<tr>
							<td class="name">明文密码</td>
							<td >
								${channel.pwd }
							</td>
						</tr>
						<tr>
							<td class="name">暗文密码</td>
							<td class="content">
								${channel.password }
							</td>
						</tr>
						
						<tr>
							<td class="name">渠道类型</td>
							<td class="content">
								<c:if test="${channel.type=='1' }">运营</c:if>
								<c:if test="${channel.type=='2' }">市场</c:if>
							</td>
						</tr>
					
						<tr>
							<td class="name">联系人</td>
							<td class="content_info">
								${channel.contacter }
							</td>
						</tr>
					
						<tr>
							<td class="name">联系电话</td>
							<td class="content_info">
								${channel.phone}
							</td>
						</tr>
						
						<tr>
							<td class="name">email</td>
							<td >
								${channel.email }
							</td>
						</tr>
						
						<tr>
							<td class="name">联系地址</td>
							<td >
								${channel.address }
							</td>
						</tr>
						<tr>
							<td class="name">所在区</td>
							<td >
								${channel.province.name }
							</td>
						</tr>
						<tr>
							<td class="name">简介</td>
							<td >
								${fn:escapeXml(channel.description) }
							</td>
						</tr>
						<tr>
							<td class="name">备注</td>
							<td >
								${fn:escapeXml(channel.remark) }
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/channel/list';" value="返回"/>
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