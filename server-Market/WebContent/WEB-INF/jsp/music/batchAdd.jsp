<%@page import="com.mas.rave.util.RandNum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'musicin';
	</script>
</head>
<body>
	<div class="sitemap" id="sitemap">
		当前位置:<span id="childTitle"></span>
	</div>
	<div class="mainoutside mt18">
		<div id="secmainoutside">
		<form id="batchAddForm" action="" method="post">		
			<div class="border">
			<table align="center" width="100%">
				<tr>
					<td class="name" colspan="3"><div class="title">批量录入ringtones</div></td>
				</tr>
				<tr>
					<td class="name">&nbsp;</td>
				</tr>
				<tr>
					<td>
					<label>请选择http://www.voga360.com 中ringtones下载类别 : </label>
					<select id="category111" name="category111">
						<c:forEach var="category" items="${categorys}">
							<option value="${category}">${category}</option>
						</c:forEach>
					</select>
					</td>
					<td>
					<label>请输入下载页面: </label>
				
                    <input id="pageNum" name="pageNum" value="1" >
                    <label><font color="red">(pageNum表示翻页的页数,pageNum=1表示第一页)</font></label>
					</td>
					<td class="content_pageNum">
						<span id="span_pageNum"></span>
					</td>
				</tr>
				<tr>
					<td class="name">&nbsp;</td>
				</tr>
				<tr>
					<td class="name" ><input type="button" id="batchRingtonesAdd" class="bigbutsubmit" value="批量录入" /></td>
				</tr>
				<tr>
					<td class="name" ><div id="ringtonesAddResult" style="padding-top: 20px"></div></td>
				</tr>
			</table>
			</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	function checkValidate(){		
		return $("#batchAddForm").validate({
			rules: {
				"pageNum": {
	                      required: true,
	                      digits:true,
	                      max:10,
	                      range:[1,10]
	                  
				}
			},
			messages: {
				"pageNum": {
					required: "请输入下载页面",
					digits: "请输入整数数字",
					max:"最大值为10",
					range:"输入数字必须是1-10之间"
				}
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			}
		});
	}
	
	
		$("#batchRingtonesAdd").click(function(){
			if(!checkValidate().form()) return; 
			var ringtonesAddResult = $("#ringtonesAddResult");
			ringtonesAddResult.html("批量录入处理中,请稍后...");
			$.ajax({
			  type: "POST",
			  url: "${ctx}/music/batchAdd/",
			  data : {"loginUser":"${loginUser.name }","category": $("#category111").val(),"pageNum":$("#pageNum").val()},
			  success:function(data){
				  var htmlstr = "处理结果：<br/><br/>";
				  for(var i=0;i < data.length;i++){
					  htmlstr += data[i] + "<br/>";
				  }
				  $("#ringtonesAddResult").html(htmlstr);
			  },
			  error:function(errmsg){
				  //alert("errmsg:" + errmsg);
				  ringtonesAddResult.html(errmsg + "");
			  }
			}); 
		});
		
	</script>
</body>
</html>