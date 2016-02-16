<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		var menu_flag = 'appannieCountryRank';
	</script>
</head>
<body>
	
	<div class="sitemap" id="sitemap">
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 修改appannie排行
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改appannie排行</div>
			
			<form id="editForm" action="" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">应用名称</td>
							<td class="content">
								<input type="hidden" id="appName" name="appName" value="${appannieInfoCountryRank.appName }">${appannieInfoCountryRank.appName }
								<c:if test="${appannieInfoCountryRank.appInfo!=null}">
								(${appannieInfoCountryRank.appInfo.id })								
								</c:if>
							</td>
							<td class="content_info">
							</td>
						</tr>	
						<tr>
							<td class="name">appannie分类 </td>
							<td class="content">
								<input type="hidden" id="annieType" name="annieType" value="${appannieInfoCountryRank.annieType}">
								<select id="annieType" name="annieType"  disabled="disabled">
									    <option value="1" <c:if test="${appannieInfoCountryRank.annieType==1}"> selected="selected"</c:if>>Hot</option>
										<option value="2" <c:if test="${appannieInfoCountryRank.annieType==2}"> selected="selected"</c:if>>New</option>
								</select>
							</td>
							<td class="content_info">
							</td>
						</tr>
									
																	
						<tr>
							<td class="name">所属国家</td>
							<td class="content">
							<input type="hidden" id="raveId" name="raveId" value="${appannieInfoCountryRank.country.id}">
							<select name="raveId1" id="raveId1" disabled="disabled">
								<c:forEach var="country" items="${countrys}">
									<option value="${country.id}" <c:if test="${appannieInfoCountryRank.country.id== country.id}">selected="selected"</c:if> >${country.name}(${country.nameCn})</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
							</td>
							<td class="content_info">
							</td>
						</tr> 
						<tr>
							<td class="name">所属专辑</td>
							<td class="content">
							<input type="hidden" id="albumId" name="albumId" value="${appannieInfoCountryRank.appAlbum.id}">
							<select name="albumId1" id="albumId1" disabled="disabled">
								<c:forEach var="appAlbum" items="${appAlbums}">
								    <c:if test="${appAlbum.id<=3}">								   
										 <option value="${appAlbum.id}" <c:if test="${appannieInfoCountryRank.appAlbum.id== appAlbum.id}">selected="selected"</c:if> >${appAlbum.name}</option>
									 </c:if>
								</c:forEach>
							</select>&nbsp;&nbsp;
							</td>
							<td class="content_info">
							</td>
						</tr> 
						<tr>
							<td class="name"><label>appannie上的安装量</label></td>
							<td class="content"><input type="text" name="annieInstallTotal" id="annieInstallTotal" value="<fmt:formatNumber value="${appannieInfoCountryRank.annieInstallTotal}" pattern="##0.0##"/>" maxlength="18"/></td>
							<td class="content_info">
								<span id="span_annieInstallTotal"></span>
							</td>
						</tr>
						<tr>
							<td class="name"><label>appannie上的评分</label></td>
							<td class="content"><input type="text" name="annieRatings" id="annieRatings" value="<fmt:formatNumber value="${appannieInfoCountryRank.annieRatings}" pattern="##0.0##"/>" maxlength="18"/></td>
							<td class="content_info">
								<span id="span_annieRatings"></span>
							</td>
						</tr>
						<tr>
						<td class="name">appannie最早上线时间</td>
						<td>
							<input class="Wdate" id="initialReleaseDate1"  name="initialReleaseDate1" style="width: 160px"
              	 			 onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});" 
              	 			 value="<fmt:formatDate pattern='yyyy-MM-dd' value='${appannieInfoCountryRank.initialReleaseDate}'/>"/>
						</td>
						<td class="content"><span id="span_initialReleaseDate1"></span></td>

						</tr>	
						<tr>
							<td class="name"><label>appannie排行榜</label></td>
							<td class="content">
							<input type="hidden" name="annieRank" value="${appannieInfoCountryRank.annieRank}"/>							
							<input type="text" value="<fmt:formatNumber value="${appannieInfoCountryRank.annieRank}" pattern="#"/>" maxlength="9" disabled="disabled"/></td>
							<td class="content_info">
								<span id="span_annieRank"></span>
							</td>
						</tr>
						
						<tr>
							<td class="name"><label>appannie上的排行幅度</label></td>
						
							<td class="content">
							<input type="hidden" name="annieExtent" value="${appannieInfoCountryRank.annieExtent}"/>
							<input type="text"  value="<fmt:formatNumber value="${appannieInfoCountryRank.annieExtent}" pattern="#"/>" maxlength="9" disabled="disabled"/></td>
							<td class="content_info">
								<span id="span_annieExtent"></span>
							</td>
						</tr>						
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="submit" class="bigbutsubmit" value="提交"  id="butsubmit_id"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='list?raveId=${appannieInfoCountryRank.country.id}&albumId=${appannieInfoCountryRank.appAlbum.id}';" value="返回"/>
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
        }, "只能输入整数"); 
		$("#editForm").validate({
			rules: {
				"annieInstallTotal": {
	                   required: true,
	                   number:true,
	                   max:1000000000000,
	                   min:0
	              },
	            "annieRatings": {
                      required: true,  
                      number:true,
                      max:5,
                      min:0
         		 },
       			"initialReleaseDate1": {
                    required: true             
       			 }
         		
			},
			messages: {
				"annieInstallTotal": {
					required: "请输入appannie上的安装量",
					number:"必须输入合法的数字",
					max:"appannie上的安装量不能大于1,000,000,000,000",
					min:"appannie上的安装量不能小于0"
				},
				"annieRatings": {
					required: "请输入appannie上的评分",
					number:"必须输入合法的数字",
					max:"appannie上的评分不能大于5",
					min:"appannie上的评分不能小于0"
				},
				"initialReleaseDate1": {
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
		        			$.alert('修改成功', function(){window.location.href = "list?raveId=${appannieInfoCountryRank.country.id}&albumId=${appannieInfoCountryRank.appAlbum.id}";});
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
			
	});
	</script>
</body>
</html>
