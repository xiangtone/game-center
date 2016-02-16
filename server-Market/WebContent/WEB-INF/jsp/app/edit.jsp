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
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 修改自营APP信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改自营APP信息</div>
			
			<form id="editForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td class="name">应用分类</td>
							<td class="content">
								<select id="categoryId" name="categoryId" onchange="change(this.value)">
								<c:forEach var="category" items="${fatherCategorys}">
									<c:if test="${category.id<=3}">
										<c:if test="${app.category.level==1}">
											<option value="${category.id}"  <c:if test="${category.id==app.category.fatherId }">selected="selected"</c:if> >${category.name}</option>
										</c:if>
										<c:if test="${app.category.level==2}">
											<option value="${category.id}"  <c:if test="${category.id==firstCategory.fatherId }">selected="selected"</c:if> >${category.name}</option>
										</c:if>
									</c:if>
								</c:forEach>
								</select>
								<span class="red">*</span>
							</td>
						</tr>
						<tr>
							<td class="name">一级分类</td>
							<td class="content">
								<select name="categoryId1" id="categoryId1" onchange="getSecondCategory(this.value)">
									
								</select>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_categoryId1"></span>
							</td>
						</tr>
						<tr>
							<td class="name">二级分类</td>
							<td class="content">
								<select name="categoryId2" id="categoryId2">
									
								</select>
							</td>
							<td class="content_info">
							</td>
						</tr>
						<tr>
						<td class="name">所属渠道</td>
							<td class="content">
								<input type="hidden" id="channelId" name="channelId" value="200000">应用发布
							</td>
							<td class="content_info">
								<span id="span_channelId"></span>
							</td>
						</tr>
						<tr>
							<td class="name">cp</td>
							<td>
							<select name="cpId" id="cpId">
								<c:forEach var="cp" items="${cps}">
									<option value="${cp.id}" <c:if test="${app.cp.id==cp.id }">selected="selected"</c:if>>${cp.name}</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_cpId"></span>
							</td>
						</tr>
						<tr>
							<td class="name"> 应用名称</td>
							<td class="content">
							    <input type="hidden" name="id" id="id" value="${app.id }"/>
								<input type="text" class="text" id="name" name="name"  value="${fn:escapeXml(app.name) }" maxlength='100' />
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span>
							</td>
						</tr>
						<tr>
							<td class="name"><b style="color: red;">责任人</b></td>
							<td class="content">
								<input type="text" class="text" id="source" name="source" value="${fn:escapeXml(app.source)}" maxlength="50" />
							</td>
							<td class="content_brief">
								50字符
							</td>
						</tr>
						<tr>
							<td class="name">别名</td>
							<td class="content">
								<textarea rows="10" cols="50" id="anotherName" name="anotherName" >${fn:escapeXml(app.anotherName)}</textarea>
							</td>
							<td class="content_info">
							<span id="span_anotherName"></span>							
							</td>
						</tr>						
						<tr>
							<td class="name">密码</td>
							<td class="content">
								<input type="text" class="text" id="pwd" name="pwd" value="${fn:escapeXml(app.pwd)}" maxlength='100'/>
								<input type="hidden" name="free" id="free" value="${fn:escapeXml(app.free)}"/>
							</td>
							<td class="content_info">
								<span id="span_pwd"></span>
							</td>
						</tr>
						<tr>
							<td class="name">logo图标地址(size)</td>
							<td colspan="2">
								<textarea rows="1" cols="100" id="bigLogo" name="bigLogo" readonly="readonly">${fn:escapeXml(app.bigLogo)}</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">logo图标上传(size)</td>
							<td colspan="2">
								<input type="file" class="text" id="bigApkFile" name="bigApkFile" maxlength='150'>
							</td>
						</tr>
							<tr>
							<td class="name">logo路径(small)</td>
							<td colspan="2">
								<textarea rows="1" cols="100" id="logo" name="logo" readonly="readonly">${fn:escapeXml(app.logo)}</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">logo图上传(small)</td>
							<td colspan="2">
								<input type="file" class="text" id="apkFile" name="apkFile" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">星级</td>
							<td class="content">
								<select name="stars">
									<option value="1" <c:if test="${app.stars==1}"> selected="selected" </c:if> >1</option>
									<option value="2" <c:if test="${app.stars==2}"> selected="selected" </c:if>>2</option>
									<option value="3" <c:if test="${app.stars==3}"> selected="selected" </c:if>>3</option>
									<option value="4" <c:if test="${app.stars==4}"> selected="selected" </c:if>>4</option>
									<option value="5" <c:if test="${app.stars==5}"> selected="selected" </c:if>>5</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="name">app简介</td>
							<td class="content">
								<textarea rows="10" cols="50" id="brief" name="brief" >${fn:escapeXml(app.brief)}</textarea>
							</td>
							<td class="content_brief">
								<span id="span_brief">200字符</span>
							</td>
						</tr>
						<tr>
							<td class="name">app说明</td>
							<td colspan="2">
								<textarea rows="15" cols="100" id="description" name="description">${fn:escapeXml(app.description)}</textarea>
							</td>
						</tr>
						<tr>
							<td class="name">下载数</td>
							<td class="content">
								<input type="text" class="text" id="initDowdload" name="initDowdload" maxlength='10' value="${fn:escapeXml(app.initDowdload)}" />
							</td>
							<td class="content_info">
								<span id="span_initDowdload"></span>
							</td>
						</tr>
						<tr>
						<td class="name">初始时间</td>
						<td>
							<input class="Wdate" id="initialReleaseDate1"  name="initialReleaseDate1" style="width: 160px"
              	 			 onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});" 
              	 			 value="<fmt:formatDate pattern='yyyy-MM-dd' value='${app.initialReleaseDate}'/>"/>
						</td>
						<td class="content"><span id="span_initialReleaseDate1"></span></td>

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
	var level = '${app.category.level}';
	//当前应用为二级分类
	var firstCat = '${app.category.fatherId}';
	var secondCat = '${app.category.id}';
	//当前应用为一级分类
	if(level == '1'){
		firstCat = '${app.category.id}';
	}
	$(document).ready(function(){
		change($("#categoryId").val());
		getSecondCategory($("#categoryId1").val());
		firstCat = "";
		secondCat = "";
		$("#editForm").validate({
			rules: {
				"name": {
                    required: true,
                    maxlength: 100
          	 	},
	           "channelId" :{
	          		required: true
	           },
	           "cpId" :{
	          		required: true
	           },
	           "brief":{
	        	   	 required: true,
                     maxlength: 200
	            },
	            "pwd" :{
	            	required: true
	            },
	            "categoryId1" :{
	            	required: true
	            },
	            "initDowdload" :{
	             	number: true,
	             	digits:true,
		             maxlength: 9
	            },
		        "anotherName" :{
		             maxlength: 2500
			    },
		        "initialReleaseDate1" :{
		        	  required: true
			    }
		},
		messages: {
			"name": {
				required: "请输入名称",
              	maxlength: "名称最长不得超过200个字符"
			},
			"channelId" :{
				required: "请至少选择一个渠道"
			},
			"cpId" :{
				required: "请至少选择一个cp"
			},
			"brief" :{
				required: "请输入简介",
				maxlength: "简介最长不得超过200个字符"
			},
			"pwd" :{
            	required: "密码不能为空"
            },

            "categoryId1" :{
            	required: "一级分类不能为空"
            },
	        "initDowdload" :{
	        	number: '不能为字符',
	        	digits:"必须为正整数或0",
	            maxlength: "初始下载量最大长度不能超过9位"
	         },
		     "anotherName" :{
				maxlength: "别名最长不得超过2500个字符"
		     },
	         "initialReleaseDate1" :{
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
		        		if(response.flag==0){
	            			$.alert('更新应用成功', function(){window.location.href = "list?currentPage=${page}";});
	            		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==3){
	            			$.alert('名称已经存在或数据出错,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
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
	});
	
	function change(val){
		$.ajax({
			url : "${ctx}/app/getCategory",
			async:false,
			type : "POST",
			dataType : "json",
			data :{"id":val},
			success: function(response){
        		var jsonstr = eval("(" + response + ")");
        		var ss = jsonstr.success.split(",");
        		$("#categoryId1>option").remove();
        		for(var i = 1;i < ss.length; i++){
        			var va = ss[i].split("-");
        			//document.getElementById("categoryId1").add(new Option(va[0],va[1]));
        			if(va[1] == firstCat){
	        			$("#categoryId1").append("<option value="+va[1]+" selected>"+va[0]+"</option>");
        			}else{
        				$("#categoryId1").append("<option value="+va[1]+">"+va[0]+"</option>");
        			}
        		}
            }
        });
	}
	
	//根据一级分类获取二级分类
	function getSecondCategory(id){
		$.ajax({
			url : "${ctx}/app/getCategory",
			async:false,
			type : "POST",
			dataType : "json",
			data :{"id":id},
			success: function(response){
        		var jsonstr = eval("(" + response + ")");
        		var ss = jsonstr.success.split(",");
        		$("#categoryId2>option").remove();
        		document.getElementById("categoryId2").add(new Option('--Select--','0'));
        		for(var i = 1;i < ss.length; i++){
        			var va = ss[i].split("-");
        			//document.getElementById("categoryId2").add(new Option(va[0],va[1]));
        			if(va[1] == secondCat){
	        			$("#categoryId2").append("<option value="+va[1]+" selected>"+va[0]+"</option>");
        			}else{
        				$("#categoryId2").append("<option value="+va[1]+">"+va[0]+"</option>");
        			}
        		}
            }
        });
	}
	</script>
</body>
</html>