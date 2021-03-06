<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>账号消费汇总</title>
	<script src="${ctx}/static/js/util.js"></script>
	<script type="text/javascript">
		//var menu_flag = 'accountConsumeSum';
		menu_flag = 'accountConsumeSum';
		var datagrid;
		var toolbar = "[{text : '导出EXCEL',iconCls : 'icon-undo',handler : exportExcel}]";
		var __ctxPath = "${ctx}";
		$(function(){
			loadDatagrid();
			$('#searchButton').click(function(){
				datagrid.datagrid('load',{
					"Q_userName" : $('#Q_userName').val(),
					"Q_groupBy" : "userName",
					"Q_${loginUser.queryRequirement}" : "${loginUser.name}",
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				});
			});
			$('#cancelSearchButton').click(function(){
				$('#Q_userName').val('');
				$('#Q_startTime').val('');
				$('#Q_endTime').val('');
				//移除限制
				$("#Q_startTime").datepicker("option","maxDate",null);
				$("#Q_endTime").datepicker("option","minDate",null);
			});
		});
		
		function loadDatagrid(){
			datagrid = $("#consume_sum_datagrid").datagrid({
				width:$("#rightbox").outerWidth(),
				height : $(".z_nav").outerHeight() - 52,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : __ctxPath + '/account/queryAccountConsumeSum',
				loadMsg : '数据加载.....',
				sortName : 'id',
				sortOrder : 'desc',
				// singleSelect:true,
				remoteSort : false,
				pagination : true,
				rownumbers : true,
				//showFooter : true,
				idField : 'id',
				queryParams : {
					"limit" : 10,
					"start" : 0,
					"Q_userName" : $('#Q_userName').val(),
					"Q_groupBy" : "userName",
					"Q_${loginUser.queryRequirement}" : "${loginUser.name}",
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				},
				columns : [[
					/*{field : 'id',title : '编号',width : 150,align : 'center'},*/
					{field : 'userName',title : '用户账号',width : 450,align : 'center'},
					{field : 'orderValue',title : '消费总额',width : 450,align : 'center'}
					]],
				toolbar : eval(toolbar),
				onLoadSuccess:function(data){
					//alert("load success !");
				}
			});
		}
		
		//导出
		function exportExcel(){
			var total = datagrid.datagrid('getPager').data("pagination").options.total;
			if(total > 100*1000){
				$.messager.alert("温馨提示","对不起，您导出的数据已经超过上限，请修改时间区间重新导出。");
				return false;
			}
			//导出调用
			window.document.location = __ctxPath + "/export/export2Excel?xmlName=consumesum.xml" 
					+ "&Q_userName=" + $("#Q_userName").val() 
					+ "&Q_groupBy=userName" 
					+ "&order=desc"
					+ "&Q_dataSource=MYSQL2"
					+ "&Q_${loginUser.queryRequirement}=${loginUser.name}"
					+ "&Q_startTime=" + $('#Q_startTime').val()
					+ "&Q_endTime=" + $('#Q_endTime').val();
		}
	</script>
</head>
<body>
	<div class="easyui-panel" title="搜索" iconCls="icon-search" collapsible="true" doSize="true">
	<div class="panel_search">
			<label>开始日期 : </label><input id="Q_startTime" readonly="readonly" class="Wdate" style="width:130px;">
			<label>--&nbsp;结束日期 : </label><input id="Q_endTime" readonly="readonly" class="Wdate" style="width:130px;">
			<label style="padding-right:5px;">用户帐号:&nbsp;<input id='Q_userName' name='Q_userName' value=''  maxlength="50"/></label>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cancelSearchButton" name="cancelSearchButton" iconCls="icon-cancel">重置搜索</a>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="searchButton" name="searchButton" iconCls="icon-search">搜索</a>
				</div>
		</div>
	<div style="margin-top: 5px;">
	<div id="consume_sum_datagrid" class="easyui-datagrid"></div>
	</div>
</body>
</html>