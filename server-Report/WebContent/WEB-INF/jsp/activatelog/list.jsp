<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>平台启动日志</title>
	<script src="${ctx}/static/js/util.js"></script>
	<script type="text/javascript">
		menu_flag = 'activatelog';
		var toolbar = "[{text : '导出EXCEL',iconCls : 'icon-undo',handler : exportExcel}]";
		var __ctxPath = "${ctx}";
		$(function(){
			loadDatagrid();
			$('#searchButton').click(function(){
				datagrid.datagrid('load',{
					"Q_userName" : $('#Q_userName').val(),
					"Q_clientId" : $('#Q_clientId').val(),
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				});
			});
			$('#cancelSearchButton').click(function(){
				$('#Q_userName').val("");
				$('#Q_clientId').val("");
				$('#Q_startTime').val('');
				$('#Q_endTime').val('');
				//移除限制
				$("#Q_startTime").datepicker("option","maxDate",null);
				$("#Q_endTime").datepicker("option","minDate",null);
			});
			$('#win').window({   
				title:'平台激活设备',
				minimizable:false,
				maximizable:false,
				collapsible:false,
				resizable:true,
			    width:1060,    
			    height:400,    
			    modal:true   
			});
			$('#win').window('close');
		});
		
		function loadDatagrid(){
			datagrid = $("#activatelog_detail_datagrid").datagrid({
				width:$("#rightbox").outerWidth(),
				height : $(".z_nav").outerHeight() - 52,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : __ctxPath + '/activateLog/query',
				loadMsg : '数据加载.....',
				sortName : 'id',
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
					"Q_userName" : $('#Q_userName').val(),
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				},
				columns : [[
					{field : 'clientId',title : '设备编号',width : 100,align : 'center',
						formatter:function(clientId){
						return "<a href='#' title='请点击我!'>"+clientId+"</a>";
					}},
					/*{field : 'userId',title : '编号',width : 100,align : 'center'},*/
					{field : 'userName',title : '用户名',width : 120,align : 'center'},
					{field : 'appId',title : '应用编号',width : 120,align : 'center'},
					{field : 'appName',title : '应用名称',width : 110,align : 'center'},
					{field : 'appVersionName',title : 'zapp版本',width : 100,align : 'center'},
					{field : 'area',title : '地区',width : 100,align : 'center'},
					{field : 'createTime',title : '日期',width : 150,align : 'center',
						formatter:function(createTime){
							return formatDateTime(createTime, true);
						}}
					]],
				toolbar : eval(toolbar),
				onLoadSuccess:function(data){
					//alert("load success !");
				},
				onClickCell: function(rowIndex, field, value){
					if(field=='clientId'){
						$('#win').window('open');
						datagrid.datagrid('clearSelections');
						loadClientSingerUserDatagrid(value);
						clientSingerUserDatagrid.datagrid('load',{
							"Q_clientId" : value
						});
					}
				}
			});
		}
		function loadClientSingerUserDatagrid(value){
			clientSingerUserDatagrid = $("#activate_detail_datagrid").datagrid({
				width:1040,
				height :350,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : __ctxPath + '/clientuser/query',
				loadMsg : '数据加载.....',
				sortName : 'createTime',
				sortOrder : 'desc',
				remoteSort : false,
				pagination : true,
				fitColumns:true,
				//showFooter : true,
				idField : 'clientId',
				queryParams : {	
					"Q_clientId" : value
				},
				columns : [[
					{field : 'clientId',title : '设备编号',width : 40,align : 'center'},
					{field : 'phone',title : '手机号码',width : 80,align : 'center'},
					{field : 'deviceModel',title : '手机机型',width : 60,align : 'center'},
					{field : 'deviceVendor',title : '手机厂商',width : 60,align : 'center'},
					{field : 'deviceType',title : '终端类型',width : 40,align : 'center',
						formatter:function(deviceType){
							if(deviceType == "1"){
								return "手机";
							}else if(deviceType == "2"){
								return "平板";
							}else{
								return "无法识别";
							}
							
						}},
					{field : 'netType',title : '网络类型',width : 40,align : 'center'},
					{field : 'osVersion',title : '系统版本',width : 40,align : 'center'},
					{field : 'zappVersionName',title : 'zapp版本',width : 140,align : 'center'},
					{field : 'area',title : '地区',width : 80,align : 'center'},
					{field : 'activeNum',title : '启动次数',width : 40,align : 'center'},
					{field : 'createTime',title : '日期',width : 80,align : 'center',
						formatter:function(createTime){
							return formatDateTime(createTime, true);
						}}
					]],
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
			var xmlName = "activatelog.xml";
			window.document.location = __ctxPath + "/export/export2Excel?xmlName="
					+ xmlName
					+ "&order=desc"
					+ "&Q_dataSource=MYSQL2"
					+ "&Q_userName=" + $('#Q_userName').val()
					+ "&Q_clientId=" + $('#Q_clientId').val()
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
			<label style="padding-right:5px;">用户名:&nbsp;<input id='Q_userName' name='Q_userName' maxlength="90"/></label>
			<label style="padding-right:5px;">设备编号:&nbsp;<input id='Q_clientId' name='Q_clientId' maxlength="10"/></label>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cancelSearchButton" name="cancelSearchButton" iconCls="icon-cancel">重置搜索</a>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="searchButton" name="searchButton" iconCls="icon-search">搜索</a>
		</div>
		</div>
	<div style="margin-top: 5px;">
	<table id="activatelog_detail_datagrid" class="easyui-datagrid"></table>
	<div id="win" style="padding:5px 3px;">
		<table id="activate_detail_datagrid" class="easyui-datagrid"></table>	
	</div>  
	</div>
</body>
</html>