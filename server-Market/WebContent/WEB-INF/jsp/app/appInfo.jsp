<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'app';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="${ctx }/app/list"><span id="childTitle"></span></a> -&gt; 自营app详细
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">APK详细</div>
			<form id="addForm" action="${ctx }/app/update/${app.id}" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">应用名</td>
							<td class="content" colspan="2">
								${fn:escapeXml(app.name)}
							</td>
						</tr>
						<tr>
							<td class="name">初始时间 </td>
							<td >
							<input class="Wdate" id="initialReleaseDate"  name="initialReleaseDate" style="width: 160px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});" 
              	 			 value="<fmt:formatDate pattern='yyyy-MM-dd' value='${app.initialReleaseDate}'/>" readonly="readonly"/>
              	 			 <span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_createTime"></span>
							</td>
						</tr>
						<tr>
							<td class="name">别名</td>
							<td class="content" colspan="2">
								<textarea  readonly="readonly">${fn:escapeXml(app.anotherName)}</textarea>
							</td>
						</tr>	
							<tr>
							<td class="name">所属渠道</td>
							<td class="content" colspan="2">
								${fn:escapeXml(app.channel.name)}(${app.channel.id} )
							</td>
						</tr>
						<tr>
							<td class="name">所属一级分类</td>
							<td colspan="2">
								<c:if test="${app.category.fatherId==2 }">应用(Apps)</c:if>
								<c:if test="${app.category.fatherId==3 }">游戏(Games)</c:if>
							</td>
						</tr>
						
							<tr>
							<td class="name">所属二级分类</td>
							<td colspan="2">
								${fn:escapeXml(app.category.categoryCn) }(${app.category.name })
							</td>
						</tr>
						<tr>
							<td class="name">cpId</td>
							<td class="content" colspan="2">
								${fn:escapeXml(app.cp.name)}(${app.cp.id})
							</td>
						</tr>
						<tr>
							<td class="name">发行商</td>
							<td class="content" colspan="2">
								${app.issuer}
							</td>
						</tr>												
						<tr>
							<td class="name">应用分类</td>
							<td class="content_info" colspan="2">
								自营应用
							</td>
						</tr>
						
						<tr>
							<td class="name">描述</td>
							<td colspan="2">
								${fn:escapeXml(app.brief) }
							</td>
						</tr>
						
						<tr>
							<td class="name">长的描述</td>
							<td colspan="2">
								${fn:escapeXml(app.description) }
							</td>
						</tr>
						<tr>
							<td class="name">图标</td>
							<td colspan="2">
							<img alt="图标" width="50" height="50" src="${path}${app.logo }">
							</td>
						</tr>
						<tr>
							<td class="name">大图标</td>
							<td colspan="2">
								<img alt="大图标" width="50" height="50" src="${path}${app.bigLogo }">
							</td>
						</tr>
						<tr>
							<td class="name">星级</td>
							<td colspan="2">
								${app.stars }
							</td>
						</tr>
						<tr>
							<td class="name">appannie评分</td>
							<td colspan="2">
								${app.starsInit }
							</td>
						</tr>
						<tr>
							<td class="name">最终的评分</td>
							<td colspan="2">
								${app.starsReal }
							</td>
						</tr>												
						<tr>
							<td class="name">更新时间</td>
							<td colspan="2">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${app.updateTime }"/>
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/app/list';" value="返回"/>
							</td>
						</tr>
					</tfoot>
				</table>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#addForm").validate({
			rules: {
				"createTime": {
                    required: true
                }
		},
		messages: {
			"createTime": {
			    required: "初始时间不能为空"
			}
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			},
			submitHandler: function(form) {
				$("#butsubmit_id").attr("disabled","true");
				setDisabled('input[type="submit"]', window.document);
				$(form).ajaxSubmit({
					type : "POST",
					dataType : "json",
					 success: function(response){
						 var data = eval("("+response+")");
		        		if(data.flag==0){
	            			$.alert('操作成功', function(){window.location.href = "${ctx}/app/list";});
	            		}else{
	                		$.alert('操作失败');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}
		        		setabled('input[type="submit"]', window.document);
		            }
	            });
			},
			success: function(label){
				var labelparent = label.parent();
				label.parent().html(labelparent.next().html()).attr("class", "valid");
			},
			onkeyup:false
		});
	});
</script>
</body>
</html>