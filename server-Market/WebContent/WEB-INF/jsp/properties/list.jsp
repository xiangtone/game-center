<%@page import="com.mas.rave.util.ConstantScore"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var menu_flag = 'properties';
</script>
</head>
<body>

	<div class="sitemap" id="sitemap">
		当前位置: 编辑配置
	</div>

	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">编辑配置</div>
			<form id="editForm" action="${ctx }/properties/setScore" method="post">
			<div class="border">
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">AnnieInstallTotal</td>
							<td>
								<textarea rows="2" cols="200" id="AnnieInstallTotal" name="AnnieInstallTotal" ><%=ConstantScore.ANNIEINSTALLTOTAL %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">AnnieInstallAverage</td>
							<td >
								<textarea rows="2" cols="200" id="AnnieInstallAverage" name="AnnieInstallAverage" ><%=ConstantScore.ANNIEINSTALLAVERAGE %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">AnnieRatings</td>
							<td >
								<textarea rows="2" cols="200" id=AnnieRatings name="AnnieRatings" ><%=ConstantScore.ANNIERATINGS %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">InitialTime</td>
							<td >
								<textarea rows="2" cols="200" id="InitialTime" name="InitialTime" ><%=ConstantScore.INITIALTIME %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">AnnieRank</td>
							<td >
								<textarea rows="2" cols="200" id="AnnieRank" name="AnnieRank" ><%=ConstantScore.ANNIERANK %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Category1</td>
							<td >
								<textarea rows="2" cols="200" id="Category1" name="Category1" ><%=ConstantScore.CATEGORY1 %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Category2</td>
							<td >
								<textarea rows="2" cols="200" id="Category2" name="Category2" ><%=ConstantScore.CATEGORY2 %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Category3</td>
							<td >
								<textarea rows="2" cols="200" id="Category3" name="Category3" ><%=ConstantScore.CATEGORY3 %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Category2</td>
							<td >
								<textarea rows="2" cols="200" id="Category4" name="Category4" ><%=ConstantScore.CATEGORY4 %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Category5</td>
							<td >
								<textarea rows="2" cols="200" id="Category5" name="Category5" ><%=ConstantScore.CATEGORY5 %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Category6</td>
							<td >
								<textarea rows="2" cols="200" id="Category6" name="Category6" ><%=ConstantScore.CATEGORY6 %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Category7</td>
							<td >
								<textarea rows="2" cols="200" id="Category7" name="Category7" ><%=ConstantScore.CATEGORY7 %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Category8</td>
							<td >
								<textarea rows="2" cols="200" id="Category8" name="Category8" ><%=ConstantScore.CATEGORY8 %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Category9</td>
							<td >
								<textarea rows="2" cols="200" id="Category9" name="Category9" ><%=ConstantScore.CATEGORY9 %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Category10</td>
							<td >
								<textarea rows="2" cols="200" id="Category10" name="Category10" ><%=ConstantScore.CATEGORY10 %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Size</td>
							<td >
								<textarea rows="2" cols="200" id="Size" name="Size" ><%=ConstantScore.SIZE %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">RealDownload</td>
							<td >
								<textarea rows="2" cols="200" id="RealDownload" name="RealDownload" ><%=ConstantScore.REALDOWNLOAD %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">DownloadAverage</td>
							<td >
								<textarea rows="2" cols="200" id="DownloadAverage" name="DownloadAverage" ><%=ConstantScore.DOWNLOADAVERAGE %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Pageviews</td>
							<td >
								<textarea rows="2" cols="200" id="Pageviews" name="Pageviews" ><%=ConstantScore.PAGEVIEWS %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Ratings</td>
							<td >
								<textarea rows="2" cols="200" id="Ratings" name="Ratings" ><%=ConstantScore.RATINGS %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">HomeRecommand</td>
							<td >
								<textarea rows="2" cols="200" id="HomeRecommand" name="HomeRecommand" ><%=ConstantScore.HOMERECOMMAND %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">HomeNew</td>
							<td >
								<textarea rows="2" cols="200" id="HomeNew" name="HomeNew" ><%=ConstantScore.HOMENEW %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">HomeTop</td>
							<td >
								<textarea rows="2" cols="200" id="HomeTop" name="HomeTop" ><%=ConstantScore.HOMETOP %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">HomePopular</td>
							<td >
								<textarea rows="2" cols="200" id="HomePopular" name="HomePopular" ><%=ConstantScore.HOMEPOPULAR %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">AppHot</td>
							<td >
								<textarea rows="2" cols="200" id="AppHot" name="AppHot" ><%=ConstantScore.APPHOT %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">AppTop</td>
							<td >
								<textarea rows="2" cols="200" id="AppTop" name="AppTop" ><%=ConstantScore.APPTOP %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">AppNew</td>
							<td >
								<textarea rows="2" cols="200" id="AppNew" name="AppNew" ><%=ConstantScore.APPNEW %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">GameHot</td>
							<td >
								<textarea rows="2" cols="200" id="GameHot" name="GameHot" ><%=ConstantScore.GAMEHOT %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">GameTop</td>
							<td >
								<textarea rows="2" cols="200" id="GameTop" name="GameTop" ><%=ConstantScore.GAMETOP %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">GameNew</td>
							<td >
								<textarea rows="2" cols="200" id="GameNew" name="GameNew" ><%=ConstantScore.GAMENEW %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Global</td>
							<td >
								<textarea rows="2" cols="200" id="Global" name="Global" ><%=ConstantScore.GLOBAL %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">Email</td>
							<td >
								<textarea rows="2" cols="200" id="Email" name="Email" ><%=ConstantScore.EMAIL %></textarea>
							</td>
						</tr>
						<tr>
							<td class="name">SimulatedSwitch</td>
							<td >
								<textarea rows="2" cols="200" id="SimulatedSwitch" name="SimulatedSwitch" ><%=ConstantScore.SIMULATEDSWITCH %></textarea>
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn"><input type="submit" class="bigbutsubmit"
								value="更新" id="butsubmit_id" /> 
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
		$("#editForm").validate({			
			submitHandler: function(form) {
				var ccList =$("#Email").val();
				var n=0;
				if(ccList!=null&&ccList!=""){
					var ccArray = ccList.split(",");
					for(var i=0;i<ccArray.length;i++){
						if(!checkEmail(ccArray[i])){
							n++;//邮箱格式不正确
						}
					}
					
				}
				if(n==0){				
					$("#butsubmit_id").attr("disabled","true");
					setDisabled('input[type="submit"]', window.document);
					$(form).ajaxSubmit({
						type : "POST",
						dataType : "json",
						 success: function(response){
							 var data = eval("("+response+")");
			        		if(data.flag==0){
		            			$.alert('更新成功', function(){window.location.href = "list";});
		            		}else{
		                		$.alert('更新失败,请重新输入');
		                		$("#butsubmit_id").attr("disabled","false");
		            		}
			        		setabled('input[type="submit"]', window.document);
			            }
		            });
				}else{
					
					alert("邮箱格式不正确,请重新输入!!");
					n=0;
				}
			},
			success: function(label){
				var labelparent = label.parent();
				label.parent().html(labelparent.next().html()).attr("class", "valid");
			},
			onkeyup:false
		});
		
	});
	function checkEmail(email) { 
		if (email!=null &&email!=""&&!email.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/)) { 
		   return false; 
		} 
		return true; 
	}
	</script>
</body>
</html>