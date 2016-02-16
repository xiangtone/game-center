<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'category';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 增加资源分类
	</div>

	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">增加二级资源分类</div>
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
				<!-- 	<tr>
							<td class="name">所属国家</td>
							<td class="content">
							<select name="raveId" id="raveId">
								<c:forEach var="country" items="${countrys}">
									<option value="${country.id}" >${country.name}(${country.nameCn})</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
							<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_raveId"></span>
							</td>
						</tr>  -->
						<tr>
							<td class="name">应用分类</td>
							<td class="content">
								<select id="fatherId" name="fatherId" onchange="change(this.value)">
									<c:forEach var="father" items="${fatherIds}">
										<option value="${father.id}" >${father.name}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td class="name">一级分类</td>
							<td class="content">
								<select name="firstId" id="firstId">
								</select>
							</td>
							<td class="content_name">
								<span id="span_firstId"></span><span>一级分类名不能为空</span>
							</td>
						</tr>
						<tr>
							<td class="name">二级分类名(英文)</td>
							<td class="content">
								<input type="text" class="text"
								id="name" name="name" maxlength='256'  /><span class="red">*</span>
							</td>
							<td class="content_name">
								<span id="span_name"></span><span>二级分类英文名不能为空</span>
							</td>
						</tr>
						<tr>
							<td class="name">二级分类名(中文)</td>
							<td class="content">
									<input type="text" class="text"
								id="categoryCn" name="categoryCn" maxlength='256' /><span class="red">*</span> 
							</td>
							<td class="content_categoryCn">
								<span id="span_categoryCn"></span><span>二级分类名不能为空</span>
							</td>
						</tr>
						<tr>
							<td class="name">推荐</td>
							<td class="content">
								<textarea rows="15" cols="110" id="recommend" name="recommend" ></textarea>
							</td>
							<td class="content_recommend">
								<span id="span_recommend"></span><span>最多100字符</span>
							</td>
						</tr>
						<tr>
							<td class="name">logo上传(big)</td>
							<td>
								<input type="file" class="text" id="bigApkFile" name="bigApkFile" maxlength='150'/>
								<span class="red">*</span>
							</td>
							<td class="content_bigApkFile">
								<span id="span_bigApkFile"></span>
							</td>
						</tr>
						<tr>
							<td class="name">logo上传(small)</td>
							<td>
								<input type="file" class="text" id="apkFile" name="apkFile" maxlength='150'/>
							</td>
							<td></td>
						</tr>
						<tr>
							<td class="name">排序值</td>
							<td class="content"><input type="text" class="text"
								id="sort" name="sort" maxlength='9' value='0' /></td>
							<td class="content_info">
								<span id="span_sort"></span>
							</td>
						</tr>
						<tr>
							<td class="name">是否有效</td>
							<td class="content">
								<input type="radio" name="state" id="state" value="1" checked="checked"/>是
								<input type="radio" name="state" id="state" value="0" />否
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
		change($("#fatherId").val());
		$.validator.addMethod("integer", function (value, element) {
            var regex = /^-?\d+$/;
            return  regex.test(value);
        }, "排序值只能输入整数");  
		$("#addForm").validate({
			rules: {
	             "categoryCn" :{
	            		 required: true,
                    	maxlength: 20
	             },
	             "name" :{
            		 required: true,
                	maxlength: 20
           	  	 },
	             "bigApkFile" :{
	            	 	required: true
	             },
	             "recommend" :{
	            	 maxlength: 100
	             },
	             "sort" :{
	            	 required: true,
	            	 integer: true
	             }
			},
			messages: {
				"categoryCn": {
					required: "二级分类名不能为空",
	                maxlength: "名称最长不得超过20个字符"
				},
				"name": {
					required: "二级分类英文名不能为空",
	                maxlength: "名称最长不得超过20个字符"
				},
				 "bigApkFile" :{
					 required: "大图不能为空"
	             },
	             "recommend" :{
	            	 maxlength: "推荐最长不得超过100个字符"
	             },
	             "sort" :{
	            	 required: "排序值不能为空"
	             }
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			},
			submitHandler: function(form) {
				var firstId = $("#firstId").val();
				if(firstId==0){
					alert("一级分类名不能为空");
					return ;
				}
				$("#butsubmit_id").attr("disabled","true");
				setDisabled('input[type="submit"]', window.document);
				$(form).ajaxSubmit({
					type : "POST",
					dataType : "json",
					 success: function(response){
		        		if(response.flag==0){
		        			$.alert('添加成功', function(){window.location.href = "list";});
	            		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else{
	            			$.alert('增加失败,分类名重复或数据有错,请重新输入');
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
	function change(value){
		if(value == 0){
			$("#firstId").html('');
			document.getElementById("firstId").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/category/getCategory",
				type : "POST",
				dataType : "json",
				data : {"id" : value,"level":2},
				success : function (data){
					var result = eval("(" + data + ")");
					if(result.success == 1){
						$("#firstId").html('');
						$("#firstId").append(result.option);
						var firstId = "${param.firstId}";
						if("" != firstId && firstId != 0){
							$("#firstId").val(firstId);
						}
					}else{
						$("#firstId").html('');
						$("#firstId").append("<option value='0'>--all--</option>");
					}
				},
				error : function (error){
					$.messager.alert("提示:","异常出现未知异常!");
				}
			});
		}
	}
	</script>
</body>
</html>