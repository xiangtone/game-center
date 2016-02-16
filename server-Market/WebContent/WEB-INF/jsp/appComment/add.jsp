<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appComment';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 增加app评论
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">增加app评论</div>
			
			<form id="addForm" action="${ctx}/appComment/add" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">对应app</td>
							<td class="content">
								<input type="hidden" id="appId" name="appId" value="${appInfo.id}">
								<c:out  value="${appInfo.name}"/>
							</td>
							<td class="content_info">
								<span id="span_appId"></span><span>对应app不能为空</span>
							</td>
						</tr>
						<tr>
							<td class="name"> 星级</td>
							<td class="content">
								<!-- <input type="text" class="text" id="stars" name="stars" maxlength='9' value="1"/> -->
									<select id="stars" name="stars" >
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
										<option value="4">4</option>
										<option value="5">5</option>
									</select>
							</td>
							<td class="content_info">
								<span id="span_stars"></span><span>星级必须为正整数</span>
							</td>
						</tr>
						<tr>
							<td class="name">内容</td>
							<td class="content">
								<input type="text" class="text" id="content" name="content" maxlength='100' value="无评论"/>
							</td>
							<td class="content_info">
							</td>
						</tr>
						<tr>
							<td class="name">是否显示评论</td>
							<td class="content">
								<input type="radio" id="state" name="state" value="1" checked="checked"/>是
								<input type="radio" id="state" name="state" value="0"/>否
								<input type="hidden" class="text" id="userName" name="userName" maxlength='100' value="匿名(mas)"/>
							</td>
							<td class="content_info">
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn" colspan="2"> 
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='${ctx}/appComment/list';" value="返回"/>
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
		$.validator.addMethod("integer", function (value, element) {
            var regex = /^-?\d+$/;
            return  regex.test(value);
        }, "星级只能输入整数"); 
		$("#addForm").validate({
			rules: {
				"stars": {
	                          required: true,
	                          integer:true,
	                          min:true
	              },
	              "appId":{
	            	  required: true
	              }
			},
			messages: {
				"stars": {
					required: "请输入星级",
					min:"不能小于等于0"
				},
				"appId":{
	            	  required: "对应app不能为空"
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
		        			$.alert('添加评论成功', function(){window.location.href = "${ctx}/appComment/list";});
	            		}else{
	                		$.alert('增加失败,请重新输入');
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
