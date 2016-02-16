<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>预装设备激活统计</title>
	<script src="${ctx}/static/js/util.js"></script>
	<script type="text/javascript">
		menu_flag = 'machine';
		var toolbar = "[{text : '导出EXCEL',iconCls : 'icon-undo',handler : exportExcel}]";
		var __ctxPath = "${ctx}";
		$(function(){
			loadDatagrid();
			$('#searchButton').click(function(){
				datagrid.datagrid('load',{
					"Q_clientId" : $('#Q_clientId').val(),
					"Q_deviceVendor" : $('#Q_deviceVendor').val(),
					"Q_area" : $('#Q_area').val(),
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				});
			});
			$('#cancelSearchButton').click(function(){
				$('#Q_clientId').val("0");
				$('#Q_startTime').val('');
				$('#Q_endTime').val('');
				$('#Q_deviceVendor').val(''),
				$('#Q_area').val(''),
				$("#Q_endTime").datepicker("option","minDate",null);
				$("#Q_startTime").datepicker("option","maxDate",null);
			});
		});
		
		function loadDatagrid(){
			datagrid = $("#machine_detail_datagrid").datagrid({
				width:$("#rightbox").outerWidth(),
				height : $(".z_nav").outerHeight() - 52,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : __ctxPath + '/machine/query',
				loadMsg : '数据加载.....',
				sortName : 'updateTime',
				sortOrder : 'desc',
				remoteSort : false,
				pagination : true,
				fitColumns:true,
				//showFooter : true,
				idField : 'id',
				queryParams : {
					"limit" : 10,
					"start" : 0,
					"Q_clientId" : $('#Q_clientId').val(),
					"Q_deviceVendor" : $('#Q_deviceVendor').val(),
					"Q_area" : $('#Q_area').val(),
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				},
				columns : [[
					{field : 'phone',title : '手机号码',width : 100,align : 'center'},
					{field : 'deviceModel',title : '设备型号',width : 100,align : 'center'},
					{field : 'deviceVendor',title : '设备厂商',width : 100,align : 'center'},
					{field : 'deviceType',title : '设备类型',width : 80,align : 'center',
						formatter:function(deviceType){
							if(deviceType == "1"){
								return "手机";
							}else if(deviceType == "2"){
								return "平板";
							}else{
								return "无法识别";
							}
							
						}},
					{field : 'netType',title : '网络类型',width : 80,align : 'center'},
					{field : 'osVersion',title : '系统版本',width : 80,align : 'center'},
					{field : 'zappVersionName',title : 'zapp版本',width : 140,align : 'center'},
					{field : 'area',title : '地区',width : 80,align : 'center'},
					{field : 'activeNum',title : '上报次数',width : 50,align : 'center'},
					{field : 'createTime',title : '激活时间',width : 100,align : 'center',
						formatter:function(createTime){
							return formatDateTime(createTime, true);
						}},
					{field : 'updateTime',title : '上报时间',width : 100,align : 'center',
						formatter:function(createTime){
							return formatDateTime(createTime, true);
						}},
					{field : 'clientId',title : 'zApp状态(设备编号)',width : 100,align : 'center',
						formatter:function(clientId){
							if(clientId == "" || clientId == null){
								return "未激活";
							}else{
								return "已激活(" + clientId + ")";
							}
						}}
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
			var xmlName = "machine.xml";
			window.document.location = __ctxPath + "/export/export2Excel?xmlName="
					+ xmlName
					+ "&sort=updateTime"
					+ "&Q_dataSource=MYSQL2"
					+ "&Q_clientId=" + $('#Q_clientId').val()
					+ "&Q_deviceVendor=" + $('#Q_deviceVendor').val()
					+ "&Q_area=" + $('#Q_area').val()
					+ "&order=desc"
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
			<label>设备厂商:</label>&nbsp;<input id='Q_deviceVendor' name='Q_deviceVendor' maxlength="50"/>
			<label>地区:</label>&nbsp;<input id='Q_area' name='Q_area' maxlength="100"/>
			<label>zApp状态:</label>&nbsp;
			<select id="Q_clientId" name="Q_clientId"  style="border:1px solid #969ba8;">
				<option value="0" selected="selected">全部</option>
				<option value="1">未激活</option>
				<option value="2">已激活</option>
			</select>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cancelSearchButton" name="cancelSearchButton" iconCls="icon-cancel">重置搜索</a>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="searchButton" name="searchButton" iconCls="icon-search">搜索</a>
			</div>
		</div>
	<div style="margin-top: 5px;">
	<table id="machine_detail_datagrid" class="easyui-datagrid"></table>
	</div>
</body>
</html>
