<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'cp';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="${ctx }/cp/list"><span id="childTitle"></span></a> -&gt; cp详细
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">查看cp信息</div>
			
			<form id="addForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">cpid</td>
							<td class="content">
								${cp.id}
							</td>
						</tr>
						<tr>
							<td class="name">cp名</td>
							<td class="content">
								${fn:escapeXml(cp.name)}
							</td>
						</tr>
							<tr>
							<td class="name">明文密码</td>
							<td >
								${cp.pwd }
							</td>
						</tr>
						<tr>
							<td class="name">暗文密码</td>
							<td class="content">
								${cp.password }
							</td>
						</tr>
						
						<tr>
							<td class="name">描述</td>
							<td class="content">
								${cp.description}
							</td>
						</tr>
					
						<tr>
							<td class="name">cp后台账号</td>
							<td class="content_info">
								${cp.backendAccount }
							</td>
						</tr>
					
						<tr>
							<td class="name">cp后台地址</td>
							<td class="content_info">
								${cp.backendUrl}
							</td>
						</tr>
						<tr>
							<td class="name">cp后台密码</td>
							<td >
								${cp.backendPassword }
							</td>
						</tr>
						<tr>
							<td class="name">地址</td>
							<td >
								${cp.address }
							</td>
						</tr>
						<tr>
							<td class="name">电话</td>
							<td >
								${cp.phoneNum }
							</td>
						</tr>
						<tr>
							<td class="name">email</td>
							<td >
								${cp.email }
							</td>
						</tr>
						<tr>
							<td class="name">联系人</td>
							<td >
								${cp.contact }
							</td>
						</tr>
						<tr>
							<td class="name">备注</td>
							<td >
								${cp.remark }
							</td>
						</tr>
						<tr>
							<td class="name">支付列表</td>
							<td >
								${cp.payWay }
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/cp/list';" value="返回"/>
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