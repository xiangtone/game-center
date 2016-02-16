<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'search';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置:  <a href="${ctx}/search/list"><span id="childTitle"></span></a> -&gt; <a href="list">图标管理 </a>-&gt; 新增平台国家信息
	</div>
		<div id="lockWindow" class="easyui-window" style="padding:10px">
		<div id="progressbar" class="easyui-progressbar" style="width:220px;float: left;"></div><div id="show" style="width:20px;float:rigth;">0%</div>
	</div>	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">添加图标</div>
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">英文名称</td>
							<td class="content">
								<input type="text" class="text" id="name" name="name" maxlength='256'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span>
							</td>
						</tr>
						<tr>
							<td class="name">中文称</td>
							<td class="content">
								<input type="text" class="text" id="nameCn" name="nameCn" maxlength='256'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_nameCn"></span>
							</td>
						</tr>
						<tr>
							<td class="name">图标上传</td>
							<td>
								<input type="file" class="text" id="urlFile" name="urlFile" maxlength='150'/>
								<span class="red">*</span>
							</td>	
							<td class="content_info">
								<span id="span_urlFile"></span>
							</td>					
						</tr>	
						<tr>
							<td class="name">是否有效</td>
							<td class="content">
								<input type="radio" name="state" id="state" value="true" checked="checked"/>是
								<input type="radio" name="state" id="state" value="false" />否
							</td>
							<td>
							</td>
						</tr>					
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn" colspan="2">
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
		//初始化锁屏窗口
		$("#lockWindow").window({
			width  : 250,
			height : 100,
			modal  : true,
			inline : false,
			closed : true,
			title  : '文件上传...',
			collapsible : false,
			minimizable : false,
			maximizable : false,
			closable    : true,
			draggable   : false,
			resizable   : false
		});

		//初始化进度条
		$("#progressbar").progressbar({value: 0});
		$("#addForm").validate({
			rules: {
				"name": {
	                        required: true,
	                        maxlength: 80
	                     },
	           "nameCn": {
		                 required: true,
		                 maxlength: 80
		               },        
	            "urlFile": {
	                       required: true,
	                   }    
	                            
			},
			messages: {
				"name": {
					required: "请输入英文名称",
					 maxlength: "名称最长不得超过80个字符"
				},
				"nameCn": {
					required: "请输入中文名称",
					 maxlength: "名称最长不得超过80个字符"
				},				
				"urlFile": {
					required: "请选择图标",				
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
						 $("#show").html("100%");
						 $('#progressbar').progressbar('value', 100);
						 $("#lockWindow").window("close");
						 $('#progressbar').progressbar('value',15);
		        		if(response.flag==0){
		        			$.alert('添加成功', function(){window.location.href = "list";});
	            		}else if(response.flag==2){
	            			$.alert('添加失败,URL格式不正确,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else{
	            			$.alert('添加失败,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}
		        		setabled('input[type="submit"]', window.document);
		            }
	            });
				$("#lockWindow").window("open");
				//调用虚拟进度条
				autoIncrement();
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