<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'channel';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 增加二级渠道
	</div>

	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">增加渠道</div>
			<form id="addForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">父渠道管理公司</td>
							<td class="content">
							<select name="fatherName">
								<c:forEach var="father" items="${fatherIds}">
								<c:if test="${father.id >1}">
									<option value="${father.id},${father.name}" >${father.name}</option>
									</c:if>
								</c:forEach>
							</select>&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
							<td class="name">渠道管理公司</td>
							<td class="content"><input type="text" class="text"
								id="name" name="name" maxlength='256' /> <span class="red">*</span>
							</td>
							<td class="content_name"><span id="span_name"></span><span>渠道管理公司</span>
							</td>
						</tr>
						<tr>
							<td class="name">渠道类型</td>
							<td class="content">
									<input type="radio" name="type" id="type" value="1" checked="checked" />运营
									<input type="radio" name="type" id="type" value="2" />市场
							</td>
						</tr>
						<tr>
							<td class="name">联系人</td>
							<td class="content"><input type="text" class="text"
								id="contacter" name="contacter" maxlength='256' /> <span class="red">*</span></td>
							<td class="content_contacter"><span id="span_contacter"></span><span>联系人</span>
							</td>
						</tr>
						<tr>
							<td class="name">联系电话</td>
							<td class="content"><input type="text" class="text"
								id="phone" name="phone" maxlength='256' /> <span class="red">*</span></td>
							<td class="content_phone"><span id="span_phone"></span><span>联系电话</span>
							</td>
						</tr>
						<tr>
							<td class="name">邮箱地址</td>
							<td><textarea rows="2" cols="50" id="email" name="email"></textarea><span class="red">*</span>
							<td class="content_info">
								<span id="span_email"></span>
							</td>						
						</tr>
						<tr>
							<td class="name">联系地址</td>
								<td class="content"><input type="text" class="text"
								id="address" name="address" maxlength='256' /> 
							</td>
						</tr>
						<tr>
							<td class="name">简介说明</td>
							<td class="content"><input type="text" class="text"
								id="description" name="description" maxlength='256' /> 
							</td>
						</tr>
						<tr>
							<td class="name">备注</td>
							<td class="content"><input type="text" class="text"
								id="remark" name="remark" maxlength='256' />
								<input type="hidden" name="state" id="state" value="1"/>
							</td>
						</tr>
						<!--<tr>
							<td class="name">是否有效</td>
							<td class="content">
								<input type="radio" name="state" id="state" value="1" checked="checked" />是
								<input type="radio" name="state" id="state" value="0" />否
							</td>
						</tr>-->
						<tr>
							<td class="name">排序</td>
							<td class="content"><input type="text" class="text"
								id="sort" name="sort" value="0" maxlength='9' />
							</td>
							<td class="content_info">
								<span id="span_sort"></span>
							</td>
						</tr>
						<tr>
							<td class="name">操作人</td>
							<td class="content"><input type="text" class="text"
								id="operator" name="operator" maxlength='256' />
							</td>
							<td class="content_info">
								<span id="span_operator"></span>
							</td>
						</tr>
						</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='list';" value="返回"/>
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
    }, "排序值只能输入整数");  
	 //手机号码验证
	  jQuery.validator.addMethod("mobile", function(value, element) {
	   var length = value.length;
	   return this.optional(element) || (length == 11 && /^\d+$/.test(value));
	  }, "手机号码格式错误!");
		//邮箱验证
	  jQuery.validator.addMethod("email", function(value, element) {
	   return this.optional(element) || /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(value);
	  }, "邮箱格式错误!");	
	$("#addForm").validate({
		rules: {
			"name": {
                required: true,
                maxlength: 100
            },
			"contacter":{
				 required: true,
			 	 maxlength: 100
			},
			"phone":{
				 required: true,
				 mobile: true
			},
			"sort" :{
           		required: true,
           		integer: true
            },
            "email":{   
             required: true,
           	 maxlength: 40,
           	 email:true
     		 },
     		"operator":{
 			 maxlength: 10
 		 }  
		},
		messages: {
			"name": {
					required: "请输入名称",
					maxlength: "名称最长不得超过100个字符"
			},
			"contacter": {
					required: "请输入联系人",
					maxlength: "名称最长不得超过10个字符"
			},
			"phone":{
				required: "请输入手机号码",
				mobile:"请输入正确格式电话"
     		 },
			"sort" :{
           		required: "排序值不能为空"
            },
            "email":{
            	required: "请输入邮箱",
           	 	maxlength: "邮箱地址最长不得超过40个字符",
                email:"请输入正确格式邮箱"
     		 },
     		"operator":{
    			 maxlength: "操作人长度不能超过10个字符"
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
	        			$.alert('添加成功', function(){window.location.href = "list";});
            		}else{
            			$.alert('添加失败,渠道已经存在或数据有误,请重新输入');
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