<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ include file="../jsp/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="language" content="en" />
		<title><decorator:title default="后台管理系统"></decorator:title></title>
		<link rel="stylesheet" href="${ctx}/static/css/module.css" type="text/css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/css/tip-yellow.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/css/jquery-ui-1.8.16.custom.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/js/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/js/themes/icon.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/css/style.css" />
		<script src="${ctx}/static/js/jquery/jquery.min.js"></script>
		<script src="${ctx}/static/js/jquery/jquery-ui-1.8.16.custom.min.js"></script>
		<script src="${ctx}/static/js/jquery/jquery.ui.datepicker-zh-CN.js"></script>
		<script src="${ctx}/static/js/jquery/jquery.form.js"></script>
		<script src="${ctx}/static/js/jquery/jquery.validate.js"></script>
		<script src="${ctx}/static/js/My97DatePicker/WdatePicker.js"></script>
		<script src="${ctx}/static/js/common.js"></script>
		<script src="${ctx}/static/js/combobox.js"></script>
		<script src="${ctx}/static/js/jquery.easyui.min.js"></script>
		<script src="${ctx}/static/js/jquery/easyui-lang-zh_CN.js"></script>
		<script src="${ctx}/static/js/ajaxupload.js"></script>		
		<script type="text/javascript">
		
		$(function(){
			var text = "${sessionScope.menus}";
			$("#menu_tree").html(text);
			var a = $(".menu_two > li[id=" + menu_flag + "]").find("a");
			if (a) {
				a.attr("class","menu_a2");
				a.parent().parent().show();
			}
			
			$(".one_a").click(function() {
				$(this).next().slideToggle();
				$(this).parent().siblings().find("ul").slideUp();
			});
			if(typeof(menu_flag+"1"!="undefined")){
				$("title").text($("#"+menu_flag+"1").html());
				$("#childTitle").text($("#"+menu_flag+"1").html());
			}
			//$(span).css("background-color","FFFFFE");
			$("#Q_startTime").datepicker({
				onSelect:function(dateText,inst){  
				       $("#Q_endTime").datepicker("option","minDate",dateText);  
				    } 
			});
			$("#Q_endTime").datepicker({
				changeMonth:true,
				onSelect:function(dateText,inst){  
			        $("#Q_startTime").datepicker("option","maxDate",dateText);  
			    }
			});
            $('#lockWindow').window({
                onBeforeClose: function () { //当面板关闭之前触发的事件
            		$.ajax({
        		        type: "post",
        		        dataType: "json",
        		        url: "${ctx}/progress/closeprogress",
        		        data: "",
        		        success: function (data) {  
        		        },
        		        error: function (err) {
        		        	//document.getElementById('progressBarText').innerHTML="Error retrieving progress";
        		        }
        		    });
                }
            });

		});
		
		//虚拟进度条
		/**
		function autoIncrement(){
			var value = $("#progressbar").progressbar("value");
			if (value < 96){
				value += Math.floor(Math.random() * 10);
				$('#progressbar').progressbar('value', value);
				if(value >= 100){
					$("#show").html("100%");
				}else{
					$("#show").html(value + "%");
				}
				setTimeout('autoIncrement()', 600);
			} 
		}
		*/
		function doProgressLoop(prog, max, counter) { 
			var x = $("#progressbar").progressbar("value");
		   	var y = parseInt(x);
		   	if (!isNaN(y)) {
		   		prog = y;
		   	}
		   	if (prog < 100) {
		   		setTimeout("getProgress()", 500);
		   		setTimeout("doProgressLoop(" + prog + "," + max + "," + counter + ")", 1000);
		   	}
		}

		function getProgress() {
		$.ajax({
		        type: "post",
		        dataType: "json",
		        url: "${ctx}/progress/getprogress",
		        data: "",
		        success: function (data) {  
		        	var response = eval("(" + data + ")");
	   			    var v = $("#progressbar").progressbar("value");
		        	if(response.flag=="session"){
	        		//	alert('Sorry, session is null');
	        		}else if(response.flag=="listener"){
	        		//	alert("Progress listener is null");
	        		}else{
	        			 v = response.flag;
	        			 if(v<100){
	        				 $("#show").html(v+"%");
	        				 
	             			 $('#progressbar').progressbar('value',parseInt(v));

	        			 }else if(v>=100){
	        				 $("#show").html("100%");
	        				 $('#progressbar').progressbar('value', 100);
	        			 }
	        		}
		        },
		        error: function (err) {
		        	//document.getElementById('progressBarText').innerHTML="Error retrieving progress";
		        }
		    });
		}   
		function autoIncrement()
		{
		var max = 100;
		var prog = 0;
		var counter = 0;
		getProgress();
		doProgressLoop(prog, max, counter);
		 
//        setTimeout(function(){ 
//        	$('#lockWindow').window('close'); 
//       	setabled('input[type="submit"]', window.document);
//             },1000*60*5); 
		}
		</script>
		<decorator:head></decorator:head>
	</head>

	<body>
	<!--top-->
	<div class="z_top">
	<div class="z_logo">
    </div>
    <div class="z_log"><a href="${ctx }/user/logout">退出</a><div class="ri">您好:[<span class="ys1">${loginUser.name }</span>]<span>今天<fmt:formatDate value="<%=new java.util.Date() %>" pattern="yyyy年MM月dd日  E" />&nbsp;&nbsp;&nbsp;</span></div></div>
	</div>
	<!--/top-->
	
	<!--left-->
	<div class="z_nav">
	<div class="z_nav2">
	<div class="left_menu" id="menu_tree">
	</div>
	</div>
	<a href="${ctx}/static/document/<%=Constant.DOC_NAME %>" class="d">操作文档</a>
	</div>
	<!--/left-->
	<!--right-->
	<div class="z_right" >
		<decorator:body/>
	</div>
	<!--/right-->
	</body>
	
</html>



