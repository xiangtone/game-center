<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'music';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 增加ringtones信息管理
	</div>
	<div id="lockWindow" class="easyui-window" style="padding:10px">
		<div id="progressbar" class="easyui-progressbar" style="width:220px;float: left;"></div><div id="show" style="width:20px;float:rigth;">0%</div>
	</div>	
	
		<%-- 初始化弹出窗口 --%>
	<div id="showApkWindow" class="easyui-window" title="请选择Music" iconCls="icon-save" closed="true"  modal="true" style="width:600px;height:350px;padding:5px;background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div id="showApk" region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				&nbsp;
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<%--
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="allCheck()">全选</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="allCancel()">重置</a> --%>
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="saveCheckbox()">确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消</a>
			</div>
		</div>
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">添加ringtones</div>
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
						<td class="name">所属应用分类</td>
							<td class="content">
								<select id="categoryId" name="categoryId" onchange="change(this.value)"  disabled="disabled">
								<c:forEach var="category" items="${categorys}">
									<option value="${category.id}" <c:if test="${category.id==4}">selected='selected'</c:if>>${category.name}</option>
								</c:forEach>
								</select>
								<span class="red">*</span>
							</td>
						</tr>
						<tr>
							<td class="name">所属专辑一级分类</td>
							<td class="content">
								<select name="categoryId1" id="categoryId1" onchange="getSecondCategory(this.value)">
								</select>
								<span class="red">*</span>
							</td>
							<td class="content_categoryId1">
								<span id="span_categoryId1"></span>
							</td>
						</tr>	
						<tr>
							<td class="name">所属专辑二级分类</td>
							<td class="content">
								<select name="categoryId2" id="categoryId2">
								</select>
							</td>
							<td class="content_info">
							</td>
						</tr>									
						<!--  <tr>
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
							<td class="name">别名</td>
							<td class="content">
								<textarea rows="10" cols="50" id="anotherName" name="anotherName" ></textarea>
							</td>
							<td class="content_info">
							<span id="span_anotherName"></span>							
							</td>
						</tr>						
						<tr>
							<td class="name">歌唱者</td>
							<td class="content" colspan="2">
								<input type="text" class="text" id="artist" name="artist" maxlength='100'/>
							</td>													
						</tr>
						<tr>
						<td class="name">资费类型</td>
						<td class="content" colspan="2">
								<select id="free" name="free">
									<option value="0" selected="selected">公共资源 </option>
									<option value="1" >平台</option>
									<option value="2" >自运营</option>
							
								</select>
						</td>
						</tr>
						<tr>
							<td class="name">图标上传</td>
							<td colspan="2">
								<input type="file" class="text" id="logoFile" name="logoFile" maxlength='150'/>
							</td>					
						</tr>
						<tr>
							<td class="name">Music上传</td>
							<td>
								<input type="radio" id="localFile" name="uploadType" checked="checked" value="1" title="本地上传" onclick="showFtp(this.value)"/>本地上传&nbsp;&nbsp;
								<input type="radio" id="ftpFile" name="uploadType" value="2" title="FTP上传" onclick="showFtp(this.value)"/>FTP上传
							</td>
							<td class="content_info">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="name">铃声文件上传</td>
							<td>
								<input type="file" class="text" id="musicFile" name="musicFile" maxlength='150'/>
								<span class="red">*</span><span id="span_musicFile"></span>
								
							</td>
							<td>
						    请输入Music文件路径 : <input type="text" class="text" id="ftpMusicFile" disabled="disabled" name="ftpMusicFile" value="${fn:escapeXml(requestScope.ftp_apk_defaul_path) }" style="width:150px;" maxlength='250'/>
								<span class="red">*</span> <input type="button"  class="bigbutsubmit" id="showBtn" disabled="disabled" value="选择文件" />
						   </td>
						</tr>
						<tr>
						<td class="name">铃声时长</td>
						<td>
							<input class="Wdate" id="durationString"  name="durationString" style="width: 160px"
              	 			 onfocus="WdatePicker({skin:'whyGreen',dateFmt:'HH:mm:ss',minDate:'00:00:00',maxDate:'00:59:59',startDate:'00:00:30',alwaysUseStartDate:true,isShowToday:false,errDealMode:1,hmsMenuCfg:{H:[1,6],m:[2,6],s:[2,6]}})" 
              	 			 value="00:00:00"/>
						</td>
						</tr>
						<tr>
							<td class="name">星级</td>
							<td class="content">
								<select name="stars">
									<option value="1" >1</option>
									<option value="2" >2</option>
									<option value="3" >3</option>
									<option value="4" >4</option>
									<option value="5" selected="selected" >5</option>
								</select>
							</td>
							<td>
								<textarea rows="1" cols="50" id="musicAddress" name="musicAddress" readonly="readonly" ></textarea>
							</td>	
						</tr>
						
						<tr>
							<td class="name">是否有效</td>
							<td class="content">
								<input type="radio" name="state" id="state" value="1" checked="checked"/>是
								<input type="radio" name="state" id="state" value="0" />否
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td class="name">来源</td>
							<td >
								<textarea rows="1" cols="100" id="source" name="source"></textarea>
							</td>
							<td class="content_source">
								<span id="span_source"></span>
							</td>
						</tr>												
						<tr>
							<td class="name">music简介</td>
							<td class="content">
								<textarea rows="10" cols="50" id="brief" name="brief" >It is an amazing music. You must be enjoy!</textarea>
							</td>
							<td class="content_brief">
								<span id="span_brief">200字符</span>
							</td>
						</tr>
						<tr>
							<td class="name">music说明</td>
							<td>
								<textarea rows="15" cols="110" id="description" name="description">
Although we don't have its decription, yet it won't stop you from playing it.Click "Download", then you'll find how brilliant this music is!</textarea>
							</td>
							<td class="content_description">
								<span id="span_description"></span>
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								 <input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<!--<input type="submit" class="bigbutsubmit" value="提交"/> -->
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
		$("#showBtn").click(function(){
			var ftpMusicFile = $("#ftpMusicFile").val();
			if(ftpMusicFile != "" ){
				ftpMusicFile += "/";
			}	
			$.ajax({
				url : "${ctx}/music/ftpUrl",
				type : "POST",
				dataType : "json",
				data : {"musicPath":ftpMusicFile},
				success : function(data){
					var checkboxs = "";
					$.each(data,function(key,val){
						//checkboxs += "<input type='checkbox' name='apk_" + key +"'";
						checkboxs += "<input type='radio' name='music_radio'";
						checkboxs += " id='" + key + "'";
						checkboxs += " value=\"" + val + "\"/>";
						checkboxs += key + "&nbsp;&nbsp;";
						checkboxs += "<br/>";
					});
					if(checkboxs == ""){
						checkboxs = "未找到该目录下的Music文件!";
					}
					$("#showApk").children().remove();
					$("#showApk").html("");
					$("#showApk").append(checkboxs);
					$("#showApkWindow").window("open");
				},
				error : function(error){
					$.messager.alert("出现未知异常!",error);
				}
			});
	    });
		
		$("#addForm").validate({
			rules: {
				"name": {
	                      required: true,
	                      maxlength: 50,
	                      remote:{
	   	     		       url:"${ctx}/music/judgeNameExist",
	   	     		       type:"post",
	   	     		       dataType:"json",
	   	     		       data:{
	   	     		            name:function(){return $("#name").val();}
	   	     			       }
	   	     			      }
	             },
	             "categoryId1" :{
	            	 required: true
	             },
	             "musicFile" :{
	            	 required: true
	             },
	             "brief":{
                     maxlength: 200
	             },
	             "description" :{
                     maxlength: 1000
			      },
		         "anotherName" :{
		             maxlength: 2500
		         },
			     "source" :{
				     maxlength: 300
			     }			      
			},
			messages: {
				"name": {
					required: "请输入名称",
	                maxlength: "名称最长不得超过50个字符",
	                remote:"名称已经存在"
				},
				"categoryId1": {
					required: "请至少输入所属类别"
				},
				"musicFile": {
					required: "请至少输入音乐文件"
				},
				"brief" :{
					maxlength: "简介最长不得超过200个字符"
				},
				"description": {
		            maxlength: "描述最长不得超过1000个字符"
		         },
		         "anotherName" :{
					maxlength: "别名最长不得超过2500个字符"
			    },
		         "source" :{
					maxlength: "来源最长不得超过300个字符"
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
	            			$.alert('添加铃声成功', function(){window.location.href = "list";});
	            		}else if(response.flag==2){
	            			$.alert('logo图标不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==3){
	            			$.alert('music文件不合法,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==4){
	            			$.alert('名称已经存在或数据出错,请重新输入');
	                		$("#butsubmit_id").attr("disabled","false");
	            		}else if(response.flag==5){
	            			$.alert('上传文件为空或者ftp文件路径为空,请重新输入');
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
	
	function change(val){
		if(val == 0 || val==null){
			$("#categoryId1").html('');
			//$("#categoryId").append("<option value='0'>--all--</option>");
			document.getElementById("categoryId1").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/music/getCategory",
				type : "POST",
				dataType : "json",
				data : {"id" : val,"level":2},
				success : function (data){
					var result = eval("(" + data + ")");
					if(result.success == 1){
						$("#categoryId1").html('');
						$("#categoryId1").append(result.option);
					}else{
						$("#categoryId1").html('');
						$("#categoryId1").append("<option value='0'>--all--</option>");
					
					}
				},
				error : function (error){
					$.messager.alert("提示:","异常出现未知异常!");
				}
			});
		}
	}
	
	//根据一级分类获取二级分类
	function getSecondCategory(id){
		if(id == 0 || id==null){
			$("#categoryId2").html('');
			//$("#categoryId").append("<option value='0'>--all--</option>");
			document.getElementById("categoryId2").add(new Option("--all--",0));
		}else{
			$.ajax({
				url : "${ctx}/music/getCategory",
				type : "POST",
				dataType : "json",
				data : {"id" : id,"level":2},
				success : function (data){
					var result = eval("(" + data + ")");
					if(result.success == 1){
						$("#categoryId2").html('');
						$("#categoryId2").append(result.option);
					}else{
						$("#categoryId2").html('');
						$("#categoryId2").append("<option value='0'>--all--</option>");
					}
				},
				error : function (error){
					$.messager.alert("提示:","异常出现未知异常!");
				}
			});
		}
	}
	//打开窗口
	function showFtp(val){
		if(val == 1){
			$("#musicFile").removeAttr("disabled");
			$("#ftpMusicFile").attr("disabled",true);
			$("#showBtn").attr("disabled",true);
		}else{
			$("#musicFile").attr("disabled",true);
			$("#musicFile").val("");
			$("#ftpMusicFile").removeAttr("disabled");
			$("#showBtn").removeAttr("disabled");
			$("#span_musicFile").html("");
		}
		$("#musicAddress").val("");

	}
	//保存数据
	function saveCheckbox(){
		$("#showApkWindow").window("close");
		var checkbox =  $("#showApk input[type=\"radio\"][name=\"music_radio\"]");
		var checkboxs = $("#showApk input[type=\"radio\"][name=\"music_radio\"]:checked");
		var checkValues = "";
		for (var i = 0; i < checkboxs.length; i++) {
			checkValues += $(checkboxs[i]).val() + "\n";
		}
		$("#musicAddress").val("");//先清空原有数据
		if(checkValues != ""){
			$("#musicAddress").val(checkValues);
		}
	}
	
	//取消
	function cancel(){
		$("#showApk").children().remove();
		$("#showApk").html("");
		$("#showApkWindow").window("close");
	}
	</script> 
	
</body>
</html>