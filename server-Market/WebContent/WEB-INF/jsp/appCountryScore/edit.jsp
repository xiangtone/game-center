<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appCountryScore';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 修改强制排名信息
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside"> 
			<div class="title">修改强制排名信息</div>
			
			<form id="editForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">应用名称</td>
							<td class="content">
								<input type="hidden" id="appId" name="appId" value="${appCountryScore.appInfo.id }">
								<input type="hidden" id="appName" name="appName" value="${appCountryScore.appInfo.name }">${appCountryScore.appInfo.name }(${appCountryScore.appInfo.id })
							</td>
							<td class="content_info">
							</td>
						</tr>												
						<tr>
							<td class="name">所属国家</td>
							<td class="content">
							<input type="hidden" id="raveId" name="raveId" value="${appCountryScore.country.id}">
							<select name="raveId1" id="raveId1" disabled="disabled">
								<c:forEach var="country" items="${countrys}">
									<option value="${country.id}" <c:if test="${appCountryScore.country.id== country.id}">selected="selected"</c:if> >${country.name}(${country.nameCn})</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
							</td>
							<td class="content_info">
								<span id="span_raveId"></span>
							</td>
						</tr> 
						<tr id="albumIdTr" <c:if test="${appCountryScore.isCategory == 1}">style='display:none;'</c:if> >
							<td class="name">app专题</td>
							<td class="content">
							<select id="albumId" disabled="disabled">
								<option value="${appCountryScore.albumId}">${appCountryScore.albumName}</option>
							</select>&nbsp;&nbsp;
							</td>
							<td class="content_info">
							</td>
						</tr> 
						<tr id="columnIdTr" <c:if test="${appCountryScore.isCategory == 1}">style='display:none;'</c:if> >
							<td class="name">app页签</td>
							<td class="content">
							<select name="columnId" id="columnId" disabled="disabled">
								<option value="${appCountryScore.columnId}">${appCountryScore.columnName}(${appCountryScore.columnNameCn})</option>
							</select>&nbsp;&nbsp;
							</td>
							<td class="content_info">
							</td>
						</tr> 
						
						<tr>
							<td class="name"><label>得分</label></td>
							<td class="content"><input type="text" name="score" id="score" value="${appCountryScore.score}" maxlength="9"/></td>
							<td class="content_info">
								<span id="span_score"></span>
							</td>
						</tr>
						<tr>
							<td class="name"><label>衰减周期</label></td>
							<td class="content"><input type="text" name="fadingDay" id="fadingDay" value="${appCountryScore.fadingDay}" maxlength="9"/></td>
							<td class="content_info">
								<span id="span_fadingDay"></span>
							</td>
						</tr>
						<tr>
						<td class="name">开始生效日期 : </td>
						<td>
							<input class="Wdate" id="startDate1"  name="startDate1" style="width: 160px"
              	 			 onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});" 
              	 			 value="<fmt:formatDate pattern='yyyy-MM-dd' value='${appCountryScore.startDate}'/>"/>
						</td>
						<td class="content"><span id="span_startDate1"></span></td>
						</tr>	
						<tr>
						<td class="name">isCategory : </td>
						<td class="content">
							<input type="radio" name="isCategory" id="isCategory1" value="1" <c:if test="${appCountryScore.isCategory == 1}">checked</c:if> >是    
							<input type="radio" name="isCategory" id="isCategory2" value="0" style="margin-left:40px;" <c:if test="${appCountryScore.isCategory == 0}">checked</c:if> >否
						</td>
						<td class="content"><span id="span_startDate1"></span></td>
						</tr>	
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='list?raveId=${appCountryScore.country.id}';" value="返回"/>
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
		$("#editForm").validate({
			rules: {
				"score": {
	                   required: true,
	                   integer:true,
	                   min:true
	              },
	            "fadingDay": {
                      required: true,
                      integer:true,
                      min:true
         		 },
        		"startDate1": {
                      required: true             
           		}
			},
			messages: {
				"score": {
					required: "请输入得分",
					min:"得分不能小于等于0"
				},
				"fadingDay": {
					required: "请输入衰减周期",
					min:"衰减周期不能小于等于0"
				},
        		"startDate1": {
        			required: "请输入初始时间"         
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
		        			$.alert('修改成功', function(){window.location.href = "list?raveId=${appCountryScore.country.id}&albumId=${appCountryScore.albumId}&columnId=${appCountryScore.columnId}";});
	            		}else{
	                		$.alert('修改失败,请重新输入');
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
			
		//
		$("#isCategory1").click(function(){
			$("#albumIdTr").hide();
			$("#columnIdTr").hide();
		});
		
		$("#isCategory2").click(function(){
			$("#albumIdTr").show();
			$("#columnIdTr").show();
		});
		
	});
	</script>
</body>
</html>
