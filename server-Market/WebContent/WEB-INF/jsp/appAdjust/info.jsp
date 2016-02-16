<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appAdjust';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="${ctx }/appAdjust/list"><span id="childTitle"></span></a> -&gt; 应用归属调整
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">APK详细</div>
			<form id="addForm" action="${ctx }/appAdjust/update/${appAdjust.id}" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
					<tr>
						<td class="name">应用类型</td>
							<td class="content" colspan="2">
								<select id="free" name="free" >
									<option value="0" <c:if test="${appAdjust.free==0 }">selected="selected"</c:if> >公用App</option>
									<option value="2" <c:if test="${appAdjust.free==2}">selected="selected"</c:if>>自营App</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="name"><b style="color: red;">责任人</b></td>
							<td class="content">
								<input type="text" class="text" id="source" name="source" maxlength="50" value="${appAdjust.source }" />
							</td>
							<td class="content_source">
								<span id="span_source">50字符 </span>
							</td>
						</tr>
						<tr>
							<td class="name">初始时间 </td>
							<td >
							<input class="Wdate" id="initialReleaseDate"  name="initialReleaseDate" style="width: 160px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});" 
              	 			 value="<fmt:formatDate pattern='yyyy-MM-dd' value='${appAdjust.initialReleaseDate}'/>" readonly="readonly"/>
              	 			 <span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_createTime"></span>
							</td>
						</tr>
						<tr>
							<td class="name">应用名</td>
							<td class="content" colspan="2">
								${fn:escapeXml(appAdjust.name)}
							</td>
						</tr>
						<tr>
							<td class="name">别名</td>
							<td class="content" colspan="2">
								<textarea  readonly="readonly">${fn:escapeXml(appAdjust.anotherName)}</textarea>
							</td>
						</tr>	
							<tr>
							<td class="name">所属渠道</td>
							<td class="content" colspan="2">
								${fn:escapeXml(appAdjust.channel.name)}(${appAdjust.channel.id} )
							</td>
						</tr>
						<tr>
							<td class="name">所属一级分类</td>
							<td colspan="2">
								<c:if test="${appAdjust.category.fatherId==2 }">应用(Apps)</c:if>
								<c:if test="${appAdjust.category.fatherId==3 }">游戏(Games)</c:if>
							</td>
						</tr>
							<tr>
							<td class="name">所属二级分类</td>
							<td colspan="2">
								${fn:escapeXml(appAdjust.category.categoryCn) }(${appAdjust.category.name })
							</td>
						</tr>
						<tr>
							<td class="name">cpId</td>
							<td class="content" colspan="2">
								${fn:escapeXml(appAdjust.cp.name)}(${appAdjust.cp.id})
							</td>
						</tr>
						<tr>
							<td class="name">发行商</td>
							<td class="content" colspan="2">
								${appAdjust.issuer}
							</td>
						</tr>												
						<tr>
							<td class="name">应用分类</td>
							<td class="content_info" colspan="2">
								<c:if test="${appAdjust.free==2 }">
									自营应用								
								</c:if>
								<c:if test="${appAdjust.free!=2 }">
									全部应用								
								</c:if>
							</td>
						</tr>
						
						<tr>
							<td class="name">描述</td>
							<td colspan="2">
								${fn:escapeXml(appAdjust.brief) }
							</td>
						</tr>
						
						<tr>
							<td class="name">长的描述</td>
							<td colspan="2">
								${fn:escapeXml(appAdjust.description) }
							</td>
						</tr>
						<tr>
							<td class="name">图标</td>
							<td colspan="2">
							<img alt="图标" width="50" height="50" src="${path}${appAdjust.logo }">
							</td>
						</tr>
						<tr>
							<td class="name">大图标</td>
							<td colspan="2">
								<img alt="大图标" width="50" height="50" src="${path}${appAdjust.bigLogo }">
							</td>
						</tr>
						<tr>
							<td class="name">星级</td>
							<td colspan="2">
								${appAdjust.stars }
							</td>
						</tr>
						<tr>
							<td class="name">appannie评分</td>
							<td colspan="2">
								${appAdjust.starsInit }
							</td>
						</tr>
						<tr>
							<td class="name">最终的评分</td>
							<td colspan="2">
								${appAdjust.starsReal }
							</td>
						</tr>												
						
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/appAdjust/list';" value="返回"/>
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
                },
                "source" :{
                	maxlength: 50
                }
		},
		messages: {
			"createTime": {
			    required: "初始时间不能为空"
			},
            "source": {
            	maxlength: "责任人长度不能超过50"
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
	            			$.alert('操作成功', function(){window.location.href = "${ctx}/appAdjust/list";});
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