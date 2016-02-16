<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'pay';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 编辑支付
	</div>

	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">编辑支付</div>

			<form id="editForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">面额</td>
							<td class="content" colspan="2">
								<select name="mogValue"  style="width:200px;">
									<option value="20000" <c:if test="${pay.mogValue==20000}">selected="selected"</c:if>>20V</option>
									<option value="50000" <c:if test="${pay.mogValue==50000}">selected="selected"</c:if>>50V</option>
									<option value="100000" <c:if test="${pay.mogValue==100000}">selected="selected"</c:if>>100V</option>
									<option value="200000" <c:if test="${pay.mogValue==200000}">selected="selected"</c:if>>200V</option>
									<option value="500000" <c:if test="${pay.mogValue==500000}">selected="selected"</c:if>>500V</option>
									<option value="1000000" <c:if test="${pay.mogValue==1000000}">selected="selected"</c:if>>1000V</option>
								</select>&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
							<td class="name">类型</td>
							<td class="content" colspan="2">
								<input type="radio" id="type" name="type" value="0" onclick="showtr(this.value)">全部
								<input type="radio" id="type" name="type" value="1"  onclick="showtr(this.value)">渠道
								<input type="radio" id="type" name="type" value="2" onclick="showtr(this.value)">cp
								<input type="radio" id="type" name="type" value="3" onclick="showtr(this.value)">appId
							</td>
						</tr>
						<tr id="a1">
							<td class="name">所属渠道</td>
							<td class="content" colspan="2">
							<select name="channelId"  id="channelId"  style="width:200px;" onchange="removeFont(this.value)">
								<option value="0">全部</option>
									<c:forEach var="channel" items="${channels}">
										<option value="${channel.id}"  <c:if test="${channel.id == pay.channelId }">selected='selected'</c:if> >${channel.name}</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr id="a2">
							<td class="name" >所属cp</td>
							<td class="content" colspan="2"><select name="cpId" id="cpId" style="width:200px;" onchange="removeFont(this.value)">
								<option value="0">全部</option>
									<c:forEach var="cp" items="${cps}">
										<option value="${cp.id}" <c:if test="${cp.id == pay.cpId }">selected='selected'</c:if>>${cp.name}</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr id="a3">
							<td class="name">appId</td>
							<td class="content" colspan="2"><select name="appId" id="appId" style="width:200px;" onchange="removeFont(this.value)">
								<option value="0" >全部</option>
									<c:forEach var="app" items="${apps}">
										<option value="${app.id}" <c:if test="${app.id == pay.appId }">selected='selected'</c:if> >${app.name}</option>
									</c:forEach>
								</select></td>
						</tr>
						<tr>
							<td class="name">充值赠送a币</td>
							<td class="content">
									<input type="hidden" name="id" value="${pay.id }"/>
								<input type="text" class="text" value="${fn:escapeXml(pay.aValuePresent)}" id="aValuePresent" name="aValuePresent" maxlength='9'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_aValuePresent"></span><span>a币必须为数字</span>
							</td>
						</tr>
						<tr>
							<td class="name">备注</td>
							<td class="content" colspan="2"><input type="text" class="text"
								id="remark" name="remark" value="${pay.remark}" maxlength='50' />
						</tr>
						<tr>
							<td class="name">状态</td>
							<td class="content" colspan="2">
								<input type="radio" name="state" id="state" value="1" <c:if test="${pay.state==true}">checked="checked" </c:if> />是
								<input type="radio" name="state" id="state" value="0"  <c:if test="${pay.state==false}">checked="checked" </c:if> />否							
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn"><input type="submit" class="bigbutsubmit"
								value="提交" id="butsubmit_id" /> <input type="button"
								class="bigbutsubmit"
								onclick="javascript:window.location.href='list';" value="返回" />
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
		jQuery.validator.addMethod("positiveinteger", function(value, element) {
			   var aint=parseInt(value);	
			    return aint >= 0 && (aint+"")==value;   
			  }, "请输入一个正整数.");
		$("#editForm").validate({
			rules: {
				"name": {
	                                required: true,
	                                maxlength: 20
	                            },
	            "aValuePresent": {
	                                required: true,
	                                positiveinteger:true
	                            }     
			},
			messages: {
				"name": {
					required: "请输入账户名",
	                maxlength: "名称最长不得超过20个字符"
				},
				"aValuePresent": {
					required: "请输入A币值",
					positiveinteger: '请输入一个正整数.',
	                maxlength: "A币值最长不得超过9个数字"
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
	            			$.alert('更新成功', function(){window.location.href = "list";});
	            		}else{
	                		$.alert('更新失败,请重新输入');
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
		//默认选中全部
		var chennalId = "${pay.channelId}" == "" ? 0 : "${pay.channelId}";
		var cpId = "${pay.cpId}" == "" ? 0 : "${pay.cpId}";
		var appId = "${pay.appId}" == "" ? 0 : "${pay.appId}";
		if(chennalId == 0 && cpId == 0 && appId == 0){
			showtr(0);
			$("input[name='type'][type='radio'][value=0]").attr("checked",true);
		}else if(chennalId != 0){
			showtr(1);
			$("input[name='type'][type='radio'][value=1]").attr("checked",true);
		}else if(cpId != 0){
			showtr(2);
			$("input[name='type'][type='radio'][value=2]").attr("checked",true);
		}else if(appId != 0){
			showtr(3);
			$("input[name='type'][type='radio'][value=3]").attr("checked",true);
		}else{
			showtr(0);
			$("input[name='type'][type='radio'][value=0]").attr("checked",true);
		}
	});


	function showtr(obj) {
		var font = "&nbsp;&nbsp;<font class='font' color=red>已可选择!</font>";
		if(obj == 0){
			$("#channelId").attr("disabled",true);
			$("#cpId").attr("disabled",true);
			$("#appId").attr("disabled",true);
			$("td>font").remove();
		}else if(obj == 1){
			$("#channelId").removeAttr("disabled");
			$("#cpId").attr("disabled",true);
			$("#appId").attr("disabled",true);
			$("td>font").remove();//移除所有font
			$("#channelId").parent().append(font);
		}else if(obj == 2){
			$("#channelId").attr("disabled",true);
			$("#cpId").removeAttr("disabled");
			$("#appId").attr("disabled",true);
			$("td>font").remove();//移除所有font
			$("#cpId").parent().append(font);
		}else if(obj == 3){
			$("#channelId").attr("disabled",true);
			$("#cpId").attr("disabled",true);
			$("#appId").removeAttr("disabled");
			$("td>font").remove();//移除所有font
			$("#appId").parent().append(font);
		}
	}
	function removeFont(value){
		$("td>font").remove();//移除所有font
	}

	</script>
</body>
</html>