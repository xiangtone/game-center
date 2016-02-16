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
		当前位置: <a href="list"><span id="childTitle"></span></a> -&gt; 修改搜索关键字
	</div>
	
	<div class="mainoutside mt18">
		<div id="secmainoutside">
			<div class="title">修改搜索关键字</div>
			<form id="addForm" action="" method="post" enctype="multipart/form-data">
			<div class="border">
				<input type="hidden" name="searchId" value="${searchKeyword.searchId}"/>	
				<table class="mainadd">
					<tbody>
						<tr>
							<td class="name">所属分类</td>
							<td class="content">
								<select id="albumId" name="albumId"  onchange="changeCategory(this.value)">
								<c:forEach var="appAlbum" items="${appAlbums}">
									<option value="${appAlbum.id}" <c:if test="${searchKeyword!=null and searchKeyword.albumId==appAlbum.id}">selected="selected"</c:if> >${appAlbum.name}</option>
								</c:forEach>
								</select>
							</td>
							<td class="content_info">
								<span id="span_albumId"></span>
							</td>
						</tr>
						<tr>
							<td class="name">所属国家</td>
							<td class="content">	
								<input type="hidden" name="raveId" value="${searchKeyword.country.id}"/>																						
							    <label>${searchKeyword.country.name}(${searchKeyword.country.nameCn})</label>
							</td>
							<td class="content_info">
								<span id="span_raveId"></span>
							</td>
						</tr> 
						<tr>
							<td class="name">搜索图标</td>
							<td class="content">
							<select name="iconId" id="iconId">
							    <option value="0">无图标</option>		
								<c:forEach var="searchIcon" items="${searchIcons}">
									<option value="${searchIcon.id}"
									<c:if test="${searchKeyword!=null and searchKeyword.iconId==searchIcon.id}">selected="selected"</c:if> >
									${searchIcon.name}(${searchIcon.nameCn})</option>
								</c:forEach>
							</select>&nbsp;&nbsp;
							</td>
							<td class="content_info">
								<span id="span_iconId"></span>
							</td>
						</tr> 
						<tr>
							<td class="name">资源标识</td>
							<td class="content">
								<input type="radio" id="flag0" name="flag" value="0"<c:if test="${searchKeyword.flag==0}">checked="checked"</c:if> />关键字
															
								<input type="radio" id="flag1" name="flag" value="1"<c:if test="${searchKeyword.flag==1}">checked="checked"</c:if> />
								<span id="span1">资源&nbsp;&nbsp;&nbsp;&nbsp;</span><a id="selectRes1" href="#">(选择资源)</a>
							
								<input type="radio" id="flag2" name="flag" value="2" <c:if test="${searchKeyword.flag==2}">checked="checked"</c:if> />列表
								&nbsp;&nbsp;&nbsp;&nbsp;<a id="selectRes2" href="#">(选择资源)</a>
								
							</td>
							<td>&nbsp;</td>
						</tr>
						<tr id="a1" >
							<td class="name">资源</td>
							<td class="content">								
								<c:if test="${data!=null||data1!=null}">
									<div id='win' style="overflow-y:auto; overflow-x:auto; width:600px; height:160px;">
										<!-- 修改时数据库中存在的资源 -->
										<c:forEach items="${data1}" var="res" >	
											<div id='div_${res.id}'>
												<input type="hidden" name="menus1" value="${res.id}"/>									
												<label style="color: red">${fn:escapeXml(res.name)}(${res.id})</label>&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="button"class="bigbutdelete" name="deletebtn" id="${res.id}"/><br/>								
											</div>
										</c:forEach>
										<!-- 修改时新增的资源 -->
										<c:forEach items="${data}" var="res" >	
											<div id='div_${res.id}'>
												<input type="hidden" name="menus0" value="${res.id}"/>									
												<label>${fn:escapeXml(res.name)}(${res.id})</label>&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="button"class="bigbutdelete" name="deletebtn" id="${res.id}"/><br/>								
											</div>
										</c:forEach>
									</div>									
								</c:if>
							</td>
							<td>&nbsp;</td>
						</tr>
						<tr id="a1" >
							<td class="name">搜索关键字</td>
							<td class="content">
								<input  type="text"  class="text" name="keyword" id="keyword"
								<c:if test="${searchKeyword!=null}"> value="${searchKeyword.keyword}"</c:if>
								/>
							</td>
							<td class="content_info">
								<span id="span_keyword"></span>
							</td>
						</tr>
						<tr id ="resLogoFileTr1" >
							<td class="name">图标地址</td>
							<td >
								<textarea rows="1" cols="100" id="resLogo" name="resLogo" readonly="readonly">${fn:escapeXml(searchKeyword.resLogo)}</textarea>
							</td>
							<td class="content_info">
							</td>
						</tr>
						<tr id ="resLogoFileTr" >
							<td class="name">图标上传</td>
							<td>
								<input type="file" class="text" id="resLogoFile" name="resLogoFile" maxlength='150' value="${fn:escapeXml(urlFilePath)}"/>
								&nbsp;&nbsp;<span>请在资源选定后再选择文件</span>
							</td>	
							<td class="content_info">
							    <span id="span_urlFile"></span> 								
							</td>					
						</tr>	
						<tr>
							<td class="name">排序值</td>
							<td class="content">
								<input type="text" class="text" id="sort" name="sort" maxlength='9'
								 <c:if test="${searchKeyword!=null}"> value="${searchKeyword.sort}"</c:if>
								 <c:if test="${searchKeyword==null}"> value="0"</c:if>/>
							</td>
							<td class="content_sort">
								<span id="span_sort"></span>
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td class="name"></td>
							<td class="btn">
								<input type="button" class="bigbutsubmit" value="提交"  id="butsubmit_id"  onclick="addSubmit()"/>
								<input type="button" class="bigbutsubmit" onclick="javascript:window.location.href='list';" value="返回"/>
							</td>
							<td>&nbsp;</td>
						</tr>
					</tfoot>
				</table>
				</div>
			</form>
		</div>
	</div>
	
	<script type="text/javascript">
	$(document).ready(function(){
        var flag1 = $("input[name = 'flag']:checked").val(); 
        if(flag1!="1"){
        	$('#resLogoFileTr').attr("style","display:none");
        	$('#resLogoFileTr1').attr("style","display:none");
        }else{
        	$('#resLogoFileTr').attr("style","display:");
        	$('#resLogoFileTr1').attr("style","display:");
        }
        var albumId0 = $("#albumId").val();
		//壁纸,铃声
		if(albumId0>3){
			$("#selectRes1").attr("style","display:none");
			$("#flag1").attr("style","display:none");
			$("#span1").attr("style","display:none");
		}else{
			$("#selectRes1").attr("style","display");
			$("#flag1").attr("style","display");
			$("#span1").attr("style","display:");
		}
		$('input[name="deletebtn"]').each(function(){
            $(this).click(function(){
            	 if(confirm("是否要删除")){
            		 var str=$(this).attr('id');//得到被点击的id    
            		 $("#div_"+str).empty();
                     $("#div_"+str).remove(); 
            	  }   
            	 if($('input[name="deletebtn"]').length==0){
        			 $("#win").remove(); 
        		}
            });
        });	  
	    $("input[name='flag']").change(function(){
	        var flag = $("input[name = 'flag']:checked").val();
	        flag1 = flag;
	        if(flag == '0'){
	        	 $("#win").remove(); 
	        	 $("#keyword").val("");
	        }
	        if(flag1!="1"){
	        	$('#resLogoFileTr').attr("style","display:none");
	        	$('#resLogoFileTr1').attr("style","display:none");
	        }else{
	        	$('#resLogoFileTr').attr("style","display:");
	        	$('#resLogoFileTr1').attr("style","display:");
	        }
	    });
   	    $("#selectRes1").click(function(){
	   	     if(flag1 == '1'){
		    	 $("#addForm").attr("action","${ctx }/search/editSelectRes");
		    		$("#addForm").validate({ 
		    			ignore: ".ignore" 
		    			}); 
		    		$("#addForm").submit();
	   	     }
        });
   	    $("#selectRes2").click(function(){
	   	     if(flag1 == '2'){
		    	 $("#addForm").attr("action","${ctx }/search/editSelectRes");
		    		$("#addForm").validate({ 
		    			ignore: ".ignore" 
		    			}); 
		    		$("#addForm").submit();
	   	     }
       });
   	 $('input').keyup(function () {
   		if(!checkValidate().form()) return; 
   	 });
	});
	function checkValidate(){	
		$.validator.addMethod("integer", function (value, element) {
            var regex = /^-?\d+$/;
            return  regex.test(value);
        }, "排序值只能输入整数");  		
	   return $("#addForm").validate({
			rules: {
				"keyword": {
	               required: true,
	               maxlength: 50,
	               remote:{
 	     		       url:"${ctx}/search/judgeKeywordExist",
 	     		       type:"post",
 	     		       dataType:"json",
 	     		       data:{
 	     		    		 searchId:"${searchKeyword.searchId}",
 	     		    		 keyword:function(){return $("#keyword").val();}
 	     			       }
 	     			    }
	            },
	           "sort": {
	        	   required:true,
	        	   integer:true,
	               maxlength: 9
	            } 
			},
			messages: {
				"keyword": {
					required: "关键字不能为空",
	                maxlength: "关键字最长不得超过50字符",
	                remote:"关键字已经存在"
				},
				"sort": {
					required:"排序值不能为空",
	                maxlength: "排序值长度不能超过9位"
				}			
			},
			errorPlacement: function(error, element){
				$("#span_" + element.attr("id")).html(error).attr("class", "error").addClass('red').next().hide();
			},
			success: function(label){
				var labelparent = label.parent();
				label.parent().html(labelparent.next().html()).attr("class", "valid");
			},
			onkeyup:false
		});
	}

	function changeCategory(value){	
		var albumId = "${searchKeyword.albumId}";
		if($('input[name="deletebtn"]').length!=0){
			 if(confirm("是否要另选资源类别，选择类别时会清除所有以前的数据！")){
				 if(value!=albumId){
					 $("#win").remove(); 
				}
			 }else{
				 $("#albumId").val(albumId);
			 }
		}
		//壁纸,铃声
		if(value>3){
			$("#selectRes1").attr("style","display:none");
			$("#flag1").attr("style","display:none");
			$("#span1").attr("style","display:none");
		}else{
			$("#selectRes1").attr("style","display");
			$("#flag1").attr("style","display");
			$("#span1").attr("style","display:");
		}
	}	
	function addSubmit(){
		if(!checkValidate().form()) return; 
		var flag = $("input[name='flag']:checked").val();
		if($('input[name="deletebtn"]').length==0&&flag!='0'){
			$.alert('资源不能为空，请选择资源！');
			return;
		}	
		if($("#resLogo").val()==""&&$("#resLogoFile").val()==""&&flag=='1'){
			$.alert('图标不能为空，请上传图标！');
			return;
		}
		$("#addForm").attr("action","${ctx}/search/${searchKeyword.searchId}");		
		$("#butsubmit_id").attr("disabled","true");
	//	setDisabled('input[type="submit"]', window.document);
		$("#addForm").ajaxSubmit({
			type : "POST",
			dataType : "json",
			 success: function(response){
        		if(response.flag==0){
        			$.alert('修改成功', function(){window.location.href = "list?albumId=${albumId}&raveId=${raveId}&currentPage=${currentPage}";});
        		}else if(response.flag==2){
        			$.alert('名称已经存在,请重新输入');
        		}else{
            		$.alert('修改失败,请重新输入');
            		$("#butsubmit_id").attr("disabled","false");
        		}
        		setabled('#butsubmit_id', window.document);
            }
        });
	}
	</script>
</body>
</html>