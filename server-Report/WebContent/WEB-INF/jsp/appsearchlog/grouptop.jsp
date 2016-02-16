<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>应用搜索日志 > 已排重数据 > 搜索排行榜</title>
	<script src="${ctx}/static/js/util.js"></script>
	<script type="text/javascript">
		menu_flag = 'appsearchlog';
		var toolbar = "[{text : '导出EXCEL',iconCls : 'icon-undo',handler : exportExcel}]";
		var __ctxPath = "${ctx}";
		$(function(){
			loadDatagrid();
			$('#searchButton').click(function(){
				datagrid.datagrid('load',{
					"Q_groupBy" : "clientId,content,newTime",
					//"Q_userName" : $('#Q_userName').val()
					//"Q_clientId" : $('#Q_clientId').val(),
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				});
			});
			$('#cancelSearchButton').click(function(){
				//$('#Q_userName').val("");
				//$('#Q_clientId').val("");
				$('#Q_startTime').val("");
				$('#Q_endTime').val("");
			});
		});
		
		function loadDatagrid(){
			datagrid = $("#top_detail_datagrid").datagrid({
				width:$("#rightbox").outerWidth(),
				height : $(".z_nav").outerHeight() - 52,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : __ctxPath + '/appsearchlog/grouptop',
				loadMsg : '数据加载.....',
				sortName : 'searchCount',
				sortOrder : 'desc',
				remoteSort : false,
				pagination : true,
				rownumbers : true,
				fitColumns:true,
				//showFooter : true,
				idField : 'id',
				queryParams : {
					"limit" : 10,
					"start" : 0,
					"Q_groupBy" : "clientId,content,newTime",
					//"Q_clientId" : $('#Q_clientId').val(),
					//"Q_userName" : $('#Q_userName').val(),
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				},
				columns : [[
					/* {field : 'clientId',title : '客户端编号',width : 100,align : 'center'},
					{field : 'userName',title : '用户名',width : 150,align : 'center'}, */
					{field : 'content',title : '搜索内容',width : 400,align : 'center'},
					{field : 'searchCount',title : '搜索次数',width : 300,align : 'center'}/* ,
					{field : 'createTime',title : '搜索时间',width : 150,align : 'center',
						formatter:function(rechargeTime){
							return formatDateTime(rechargeTime, true);
						}} */
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
			var xmlName = "appgrouptop.xml";
			window.document.location = __ctxPath + "/export/export2Excel?xmlName="
					+ xmlName
					//+ "&sort=searchCount"
					+ "&order=desc"
					+ "&Q_dataSource=MYSQL2"
					+ "&Q_groupBy=clientId,content,newTime"
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
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cancelSearchButton" name="cancelSearchButton" iconCls="icon-cancel">重置搜索</a>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="searchButton" name="searchButton" iconCls="icon-search">搜索</a>
			&nbsp;&nbsp;<a href="javascript:history.back();" class="easyui-linkbutton"
				id="gobackBtn" name="gobackBtn" iconCls="icon-undo">返回</a>
				</div>
		</div>
	<div style="margin-top: 5px;">
	<table id="top_detail_datagrid" class="easyui-datagrid"></table>
	</div>
</body>
</html>