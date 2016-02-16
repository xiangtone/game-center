<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'appAlbumColumn';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">当前位置: <span id="childTitle"></span>- <font >${country.name}(${country.nameCn})--${appAlbumColumn.appAlbum.name }--${appAlbumColumn.name }</font></div>
	<div class="mainoutside">
				<div class="mainarea_shop">
				<div class="servicesearch pt18">
				<div class="border">
					<table>
						<tr>
							<td width="400">
								<form id="applistRes"
									action="${ctx }/appAlbumColumn/handListRes/${appAlbumColumn.columnId}?raveId=${raveId}"
									method="post">
									应用名称：<input type="text" name="name" id="name" value="${fn:escapeXml(param.name)}" />&nbsp;&nbsp;
									<input type="hidden" id="raveId" name="raveId" value="${raveId}"/>
									<input type="hidden" id="temp" name="temp" value="${temp }"/>																		
									<button type="submit" class="butsearch" id="btn-search">查询</button>
									<input type="button" class="butsearch" value="重置" onclick="resetQuery()"/>								
									
								</form>
							</td>
							<td width="100">
									<input id="callback" name="callback" class="bigbutsubmit" style="margin-left: 50px" type="button" value="返回" >
							</td>
							<td width="100">
								<form action="${ctx }/appAlbumColumn/${appAlbumColumn.columnId}/config">
									<input type="hidden" id="raveId" name="raveId" value="${raveId}"/>
									<input type="hidden" id="temp" name="temp" value="${temp }"/>		
									<input type="submit" class="bigbutsubmit" value="配 置"  id="butsubmit_id"/>
								</form>
							</td>
							<td width="300">
							&nbsp;&nbsp;&nbsp;&nbsp;顺延排行:<input type="text" id="laterRanking" name="laterRanking" value="0" maxlength="11" style="width:30px;"/>&nbsp;位
							&nbsp;&nbsp;<input type="button" class="bigbutsubmit" id="ranking_later" value="执行顺延"/>
							</td>
							<td width="200">
							&nbsp;&nbsp;<input type="button" class="bigbutsubmit" id="ranking_clear" value="清空排名"/>				
							<c:if test="${country.name!=defaultCountry}">
							    <input type="button" title="点击我会清空${country.name }中手动分发的数据并将${defaultCountry}的手动分发的数据全部复制到${country.name }中" class="bigbutsubmit" id="handData_copy" value="复制排名"/>
							</c:if>
							</td>
						</tr>
					</table>
			</div>
				</div>
				<div class="operateShop">
					<ul>
						<li><a id="cmd-delete">删除</a></li>
						<li><a type="button" class="lang" id="export_appAlbumOperant" href="#" onclick="exportExcel()">导出表格</a></li>				
						<!--<li><a type="button" class="lang" id="ranking_edit">编辑排名</a></li>  -->
						<li><a type="button" class="lang" id="ranking_save">保存排名</a></li>
					</ul>
				</div>
			</div>
		<div class="secmainoutside">
			<div class="border">

			<table class="mainlist">
				<thead>
					<tr>
						<td width="2%"><input type="checkbox" class="checkall" /></td>
						<td align="center">appId</td>
						<td align="center">页签</td>
						<td align="center">专辑(英文)</td>
						<td align="center">分类名(英文)</td>
						<td align="center">应用名</td>
						<td align="center">logo</td>
						<td align="center">评分</td>		
						<!-- <td align="center">浏览数</td>  -->						
						<td align="center">下载数(实-初)</td>
						<td align="center">排行</td>

					</tr>
				</thead>
				<tbody>
					<c:if test="${result.recordCount == 0}">
						<tr>
							<td colspan="7" align="center">没有符合要求的数据！</td>
						</tr>
					</c:if>
					<c:forEach var="obj" items="${result.data}">
						<tr onMouseOver="chgTrColor(this)" onMouseOut="chgTrColor(this)">
							<td align="center"><input type="checkbox" name="recordId"
								value="${obj.id}" /></td>
							<td align="center">${obj.appInfo.id }</td>
							<td align="center">${obj.appAlbum.name }</td>
							<td align="center">${obj.appAlbumColumn.nameCn }(${obj.appAlbumColumn.name })</td>
							<td align="center">${obj.appInfo.category.categoryCn }(${obj.appInfo.category.name })</td>
							<td align="center">${obj.appName }</td>
							<td align="center"><img alt="${obj.appName }" src="${path}${obj.bigLogo}" width="50" height="50"></td>
							<td align="center">${obj.stars }</td>
							<!--<td align="center"></td>  -->
							<td align="center">${obj.realDowdload }-${obj.initDowdload }</td>
							<td align="center"><input type="text" id="ranking_${obj.id }"
								value="${obj.ranking}" maxlength="9"
								style="width: 60px;" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="page">
				<div class="pageContent">
					<c:if test="${result.recordCount != 0}">
						<ccgk:pagination paginationVo="${result}"
							contextPath="${ctx}/appAlbumColumn/handListRes/${appAlbumColumn.columnId}"
							params="${params}" />
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			var cou = parseInt("${result.recordCount}");
			var num = parseInt("${num}");
			if(cou >= num){
				alert('当前分发数量已经超过${num}');
			}
			var params = {"name" : $("#name").val()};
			paginationUtils(params);
		});
		function checkId() {
			var item = $("[name=recordId]");
			var id = "";
			if (item.size() > 0) {
				item.each(function() {
					if ($(this).attr("checked") == true) {
						id += $(this).val();
						id += ",";
					}
				});
			}
			id = id.substr(0, id.lastIndexOf(","));
			if (id == "") {
				$.alert("请选择记录");
				return '';
			} else {
				return id;
			}
		}	
		$("#cmd-delete").click(function() {
			var id = checkId();
			if (id != '') {
				$.alert("删除后将不可恢复，确定删除？", function() {
					$.ajax({
						url : "${ctx}/appAlbumColumn/delete",
						type : "POST",
						data : {
							'id' : id
						},
						dataType : "json",
						success : function(data) {
							var flag = eval("(" + data + ")");
							if (flag.success == "true") {
								$.alert("删除成功！");
								//window.location.reload();
								var href = window.location.href;
								window.location.href = href;
							}
						},
						error : function(data) {
							alert("error:" + data);
						}
					});
				}, true);
			}
		});
		/**
		    * 保存排行结果
		    */
			$("#ranking_save").click(function(){
				var rankings = $("input[type=text][id*='ranking_']");
				var paramstr = "";
				for(var i = 0; i < rankings.length ; i ++ ){
					var input = $(rankings[i]);
					var id = input.attr("id");
					var ranking = input.attr("value");
					//检查是否输入是合法的整数
					 var reg = /^\d+$/;
					 if (!ranking.match(reg)&&ranking!=null&&ranking!=""){
							$.messager.alert("提示:","排名号输入不正确,请检查!");
						return;  
					 }
						if(ranking == "0"){
							$.messager.alert("提示:","排名号不能为0,请检查!");
							return;
						}
					paramstr += id.replace("ranking_","") + "#" + ranking + "&";
				}
				if(paramstr == ""){
					return;
				}
				var columnId = "${appAlbumColumn.columnId}";
				var raveId = "${country.id}";
				$.ajax({
					url : "${ctx}/appAlbumColumn/judgeAlbumIsRun?raveId="+raveId+"&columnId="+columnId,
					type : "POST",
					dataType : "json",
					success : function(data) {
						var data1 = eval("("+data+")");
						 if(data1.flag==2){
							 alert("分发程序未运行完，不能清除排名值！");	
							 rankings.attr("readonly","true");
						 }else{							 
						 $.messager.confirm('确认提示:','您确认要保存排名结果吗?',function(r){   
							    if (r){
							        $.ajax({
							        	url :"${ctx}/appAlbumColumn/updateRanking/"+columnId,
							        	type : "POST",
							        	dataType : "json",
										data : {"paramstr" : paramstr,"raveId" : raveId},
							        	success : function(response){
							        		var result = eval("(" + response + ")");
							        		if(result.success == 4){
							        			$.messager.alert("提示:","排名值不能重复!");
							    				return;
							        		}else if(result.success == 2){
							        			$.messager.alert("提示:","您输入的不是正整数!");
							    				return;
							        		}else if(result.success == 3){
							        			$.messager.alert("提示:","排名出异常了,请联系管理员!");
							    				return;
							        		}else{
							        			$.messager.alert("提示:","保存成功!");						        			
							        			window.location.href = "${ctx }/appAlbumColumn/handListRes/${appAlbumColumn.columnId}?raveId=${raveId}&temp=${temp}";
							        		}
							        	},
							        	error : function (error){
							        		$.messager.alert("提示:","保存失败!");
							        	}
							        });
							    }
							});
						 }						 
					},
		        	error : function (error){
		        		return false;
		        	}
				});

			});		 
			$("#ranking_clear").click(function(){
				var columnId = "${appAlbumColumn.columnId}";
				var raveId = "${country.id}";
				$.ajax({
					url : "${ctx}/appAlbumColumn/judgeAlbumIsRun?raveId="+raveId+"&columnId="+columnId,
					type : "POST",
					dataType : "json",
					success : function(data) {
						var data1 = eval("("+data+")");
						 if(data1.flag==2){
							 alert("分发程序未运行完，不能清除排名值！");	
						 }else{							 
							$.messager.confirm('提示:','您确认要清除排名结果吗?',function(r){   
							    if (r){
							        $.ajax({
							        	url :"${ctx}/appAlbumColumn/clearRanking/"+columnId+"?raveId="+raveId,
							        	type : "POST",
							        	dataType : "json",
							        	success : function(response){
							        		var result = eval("(" + response + ")");
							        		if(result.success == 2){
							        			$.messager.alert("提示:","清除排名出异常了,请联系管理员!");
							    				return;
							        		}else{
							        			$.messager.alert("提示:","清除排名成功!");						        			
							        			window.location.href = "${ctx }/appAlbumColumn/handListRes/${appAlbumColumn.columnId}?raveId=${raveId}&temp=${temp}";
							        		}
							        	},
							        	error : function (error){
							        		$.messager.alert("提示:","保存失败!");
							        	}
							        });
							    }
							});
						 }
					},
		        	error : function (error){
		        		return false;
		        	}
				});

			 }); 
		
			$("#ranking_later").click(function() {
				var id = checkId(); 
				if (id != '') {
						//获取延迟排序的值
						var laterRanking = $("#laterRanking").val();
						if(laterRanking == "" || laterRanking == undefined){
							$.messager.alert("提示:","顺延排名号不能为空,请检查!");
							return;
						}
						//顺延排名号不能为0
						if(laterRanking == "0"){
							$.messager.alert("提示:","顺延排名号不能为0,请检查!");
							return;
						}
						//检查是否输入是合法的整数
						 var reg = /^\d+$/;
						 if (!laterRanking.match(reg)){
						    $.messager.alert("提示:","顺延排名号输入不正确,请检查!");
							return;  
						 }
						 if(laterRanking>=10000){
							 $.messager.alert("提示:","顺延排名号过长,请检查!");
								return; 
						 }
						var columnId = "${appAlbumColumn.columnId}";
						var raveId = "${country.id}";
						$.ajax({
							url : "${ctx}/appAlbumColumn/judgeAlbumIsRun?raveId="+raveId+"&columnId="+columnId,
							type : "POST",
							dataType : "json",
							success : function(data) {
								var data1 = eval("("+data+")");
								 if(data1.flag==2){
									 alert("分发程序未运行完，不能清除排名值！");	
								 }else{									 
									$.messager.confirm('提示:','您确认要延迟排名吗?',function(r){   
									    if (r){
									        $.ajax({
									        	url :"${ctx}/appAlbumColumn/laterRanking/"+columnId,
									        	type : "POST",
									        	dataType : "json",
												data : {'ids' : id,'raveId':raveId,'num':laterRanking},
									        	success : function(response){
									        		var result = eval("(" + response + ")");
									        		if(result.success == 3){
									        			$.messager.alert("提示:","排名值不能重复!");
									    				return;
									        		}else if(result.success == 2){
									        			$.messager.alert("提示:","延迟排名出异常了,请联系管理员!");
									    				return;
									        		}else{
									        			$.messager.alert("提示:","延迟排名成功!");						        			
									        			window.location.href = "${ctx }/appAlbumColumn/handListRes/${appAlbumColumn.columnId}?raveId=${raveId}&temp=${temp}";
									        		}
									        	},
									        	error : function (error){
									        		$.messager.alert("提示:","保存失败!");
									        	}
									        });
									    }
									});
								 }
								
							},
				        	error : function (error){
				        		
				        	}
					  });
				  }
			});
			$("#handData_copy").click(function(){
				var columnId = "${appAlbumColumn.columnId}";
				var raveId = "${country.id}";
				var defaultCountry = "${defaultCountry}";
				$.ajax({
					url : "${ctx}/appAlbumColumn/judgeAlbumIsRun?raveId="+raveId+"&columnId="+columnId,
					type : "POST",
					dataType : "json",
					success : function(data) {
						var data1 = eval("("+data+")");
						 if(data1.flag==2){
							 alert("分发程序未运行完，不能复制手动分发数据！");	
						 }else{							 
							$.messager.confirm('提示:','您确认要复制'+defaultCountry+'手动分发的数据到${country.name }吗?',function(r){   
							    if (r){
							        $.ajax({
							        	url :"${ctx}/appAlbumColumn/handDataCopy/"+columnId+"?raveId="+raveId,
							        	type : "POST",
							        	dataType : "json",
							        	success : function(response){
							        		var result = eval("(" + response + ")");
							        		if(result.success == 2){
							        			$.messager.alert("提示:","手动分发数据复制出异常了,请联系管理员!");
							    				return;
							        		}else{
							        			$.messager.alert("提示:","手动分发数据复制成功!");						        			
							        			window.location.href = "${ctx }/appAlbumColumn/handListRes/${appAlbumColumn.columnId}?raveId=${raveId}&temp=${temp}";
							        		}
							        	},
							        	error : function (error){
							        		$.messager.alert("提示:","保存失败!");
							        	}
							        });
							    }
							});
						 }
					},
		        	error : function (error){
		        		return false;
		        	}
				});

			 }); 
		$("#callback").click(function() {
			var temp ="${temp}"; 
			if(temp=="list"){
				window.location.href='${ctx }/appAlbumColumn/list?raveId=${raveId}';
			}else if(temp=="listRes"){
				window.location.href='${ctx }/appAlbumColumn/listRes/${appAlbumColumn.columnId}?raveId=${raveId}';
			}else{
				window.location.href='${ctx }/appAlbumColumn/list';	
			}
			
		});
		//导出
		function exportExcel(){
			//导出调用
			var xmlName = "appAlbumHand.xml";
			window.document.location = "${ctx}/appAlbumColumn/exporthand2Excel/${appAlbumColumn.columnId}?xmlName="
					+ xmlName
					+ "&name=" + $('#name').val()
					+ "&temp=" + $('#temp').val()
					+ "&raveId=" + $('#raveId').val();
		}
		function resetQuery(){
			if(confirm("确实要重置吗?")){
				var name =$("#name");
				name.val("");
			}
		}
		 
		function selectQuery(id){
			window.location.href = "${ctx }/appAlbumColumn/handListRes/${appAlbumColumn.columnId}?raveId="+id;
		}
	</script>
</body>
</html>