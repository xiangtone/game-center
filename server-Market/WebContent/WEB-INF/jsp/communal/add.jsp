<%@page import="com.mas.rave.util.RandNum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'communal';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 新增APP公用信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">添加APP公用</div>
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td class="name">应用分类</td>
							<td class="content" colspan="2">
								<select id="categoryId" name="categoryId" onchange="change(this.value)">
								<c:forEach var="category" items="${categorys}">
								<c:if test="${category.id<=3}">
									<option value="${category.id}" >${category.name}</option>
								</c:if>
								</c:forEach>
								</select>
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
								<span id="span_categoryId2"></span>
							</td>
						</tr>
						<tr>
						<td class="name">所属渠道(父渠道)</td>
							<td class="content">
								<input type="hidden" id="channelId" name="channelId" value="200000">应用发布
							</td>
							<td></td>
						</tr>
						<tr>
							<td class="name">cp</td>
							<td colspan="2">
							<input type="hidden" id="cpId" name="cpId" value="${cp.id }">${cp.name }
							</td>
						</tr>
						<tr>
							<td class="name"> 应用名</td>
							<td class="content">
								<input type="text" class="text" id="name" name="name" maxlength='100'/>
								<span class="red">*</span>
							</td>
							<td class="content_info">
								<span id="span_name"></span>
							</td>
						</tr>
						<tr>
							<td class="name"><b style="color: red;">责任人</b></td>
							<td class="content">
								<input type="text" class="text" id="source" name="source" maxlength="50" />
							</td>
							<td class="content_brief">
								50字符
							</td>
						</tr>
						<tr>
							<td class="name">别名</td>
							<td class="content">
								<textarea rows="10" cols="50" id="anotherName" name="anotherName" ></textarea>
							</td>
							<td class="content_info">
							<span id="span_anotherName"></span>							
							</td>
						</tr>						
						<tr>
							<td class="name">logo上传(size)(96x96)</td>
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
							<td colspan="2">
								<input type="file" class="text" id="apkFile" name="apkFile" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">app截图1</td>
							<td>
								<input type="file" class="text" id="appApkFile1" name="appApkFile1" maxlength='150'/>
								<span class="red">*</span>
							</td>
							<td class="content_appApkFile1">
								<span id="span_appApkFile1"></span>
							</td>
						</tr>
						<tr>
							<td class="name">app截图2</td>
							<td colspan="2">
								<input type="file" class="text" id="appApkFile2" name="appApkFile2" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">app截图3</td>
							<td colspan="2">
								<input type="file" class="text" id="appApkFile3" name="appApkFile3" maxlength='150'/>
							</td>
						</tr>
						<tr>
							<td class="name">标签</td>
							<td class="content" colspan="2">
								<input type="text" class="text" id="tags" name="tags" maxlength='100' value="No Label"/>
								<input type="hidden" name="free" id="free" value="0" />
							</td>
						</tr>
						<tr>
							<td class="name">app简介</td>
							<td class="content">
								<textarea rows="10" cols="50" id="brief" name="brief" >It is an amazing app. Join us to play!</textarea><span id="span_brief" style="">200字符</span>
							</td>
							<td class="content_brief">
								
							</td>
						</tr>
						<tr>
							<td class="name">app说明</td>
							<td >
								<textarea rows="15" cols="100" id="description" name="description" >
Although we don't have its decription, yet it won't stop you from playing it.Click "Download", then you'll find how brilliant this app is!
								</textarea>
							</td>
							<td class="content_info">
								<span id="span_description"></span>
							</td>
						</tr>
						<tr>
							<td class="name">备注</td>
							<td class="content">
								<input type="text" class="text" id="remark" name="remark" maxlength='100' value="No remark"/>
							</td>
							<td class="content_info">
								<span id="span_remark"></span>
							</td>
						</tr>
						<tr>
							<td class="name">星级</td>
							<td class="content" colspan="2">
								<select name="stars">
									<option value="1" >1</option>
									<option value="2" >2</option>
									<option value="3" >3</option>
									<option value="4" >4</option>
									<option value="5" selected="selected" >5</option>
								</select>
							</td>
						</tr>
						<!-- 
						<tr>
							<td class="name">是否有效</td>
							<td class="content">
								<input type="radio" name="state" id="state" value="1" checked="checked"/>是
								<input type="radio" name="state" id="state" value="0" />否
							</td>
						</tr>
						 -->
						<tr>
							<td class="name">下载数</td>
							<td class="content">
								<input type="text" class="text" id="initDowdload" name="initDowdload" maxlength='11' value="<%=RandNum.randNum() %>" />
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
              	 			 value="<fmt:formatDate pattern='yyyy-MM-dd' value='${now}'/>"/>
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
	$(document).ready(function(){
		change($("#categoryId").val());
		getSecondCategory($("#categoryId1").val());
		$("#addForm").validate({
			rules: {
				"name": {
                    required: true,
                    maxlength: 100
                },
                "appApkFile1" :{
                 	required: true
                },
                "bigApkFile" :{
                 	required: true
                },
                "brief":{
               	    required: true,
                    maxlength: 200
                },
                "categoryId1" :{
                   	required: true
                },  
	            "initDowdload" :{
	            	number: true,
			        digits:true,
			        maxlength: 9
	            },		          
	            "remark" :{
                    maxlength: 30
	            },
	            "description" :{
	                 maxlength: 5000
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
			"appApkFile1": {
				required: "请至少输入一张图片"
			},
			"bigApkFile": {
				required: "请至少输入大图标"
			},
			"brief" :{
				required: "请输入简介",
				maxlength: "简介最长不得超过200个字符"
			},
			"categoryId1" :{
            	required: "一级分类不能为空"
	        },
		    "initDowdload" :{
		        number: '不能为字符',
		        digits:"必须为正整数或0",
		        maxlength: "初始下载量最大长度不能超过9位"
		    },
		    "remark" :{
				maxlength: "备注最长不得超过30个字符"
		    },
		    "description" :{
				maxlength: "描述最长不得超过5000个字符"
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
	            			$.alert('增加应用成功', function(){window.location.href = "list";});
	            		}else if(response.flag==2){
	            			$.alert('图片不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==3){
	                		$.alert('名称已经存在或数据出错,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
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
	
	function change(val){
		$.ajax({
			url : "${ctx}/communal/getCategory",
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
        			document.getElementById("categoryId1").add(new Option(va[0],va[1]));
        		}
            }
        });
	}
	
	//根据一级分类获取二级分类
	function getSecondCategory(id){
		$.ajax({
			url : "${ctx}/communal/getCategory",
			async:false,
			type : "POST",
			dataType : "json",
			data :{"id":id},
			success: function(response){
        		var jsonstr = eval("(" + response + ")");
        		var ss = jsonstr.success.split(",");
        		$("#categoryId2>option").remove();
        		document.getElementById("categoryId2").add(new Option('--Select--',0));
        		for(var i = 1;i < ss.length; i++){
        			var va = ss[i].split("-");
        			document.getElementById("categoryId2").add(new Option(va[0],va[1]));
        		}
            }
        });
	}
	</script>
</body>
</html>