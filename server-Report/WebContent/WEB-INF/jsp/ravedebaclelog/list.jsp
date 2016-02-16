<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>平台崩溃日志</title>
	<script src="${ctx}/static/js/util.js"></script>
	<script type="text/javascript">
		menu_flag = 'ravedebaclelog';
		//var toolbar = "[{text : '导出EXCEL',iconCls : 'icon-undo',handler : exportExcel}]";
		var __ctxPath = "${ctx}";
		$(function(){
			loadDatagrid();
			$('#searchButton').click(function(){
				datagrid.datagrid('load',{
					"Q_masVersionName" : $('#Q_masVersionName').val(),
					"Q_masPackageName" : $('#Q_masPackageName').val(),
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				});
			});
			$('#cancelSearchButton').click(function(){
				$('#Q_masVersionName').val("");
				$('#Q_masPackageName').val("");
				$('#Q_startTime').val('');
				$('#Q_endTime').val('');
				$("#Q_endTime").datepicker("option","minDate",null);
				$("#Q_startTime").datepicker("option","maxDate",null);
			});
			$("#showmore").window({
				title: '查看详情',
				modal: true,
				shadow: false,
				closed: true
			});
		});
		
		function loadDatagrid(){
			datagrid = $("#ravedebaclelog_datagrid").datagrid({
				width:$("#rightbox").outerWidth(),
				height : $(".z_nav").outerHeight() - 52,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : __ctxPath + '/ravedebaclelog/query',
				loadMsg : '数据加载.....',
				sortName : 'id',
				sortOrder : 'desc',
				remoteSort : false,
				pagination : true,
				//rownumbers : true,
				fitColumns:true,
				//showFooter : true,
				idField : 'id',
				queryParams : {
					"limit" : 10,
					"start" : 0,
					"Q_masVersionName" : $('#Q_masVersionName').val()
				},
				columns : [[
					/*{field : 'id',title : '编号',width : 50,align : 'center'},
					{field : 'clientId',title : '手机号码',width : 100,align : 'center'},*/
					{field : 'masPackageName',title : '平台名称',width : 100,align : 'center'},	
					{field : 'masVersionName',title : '平台版本名称',width : 100,align : 'center'},
					/*{field : 'masVersionCode',title : '平台版本编码',width : 50,align : 'center'},*/
					{field : 'content',title : '崩溃原因',width : 75,align : 'center',
						formatter:function(content,rowData,index){
							if(content.length > 10){
								return "<a href='javascript:void(0)' onclick='show(" + rowData.id + ")'>详情</a>";
							}
						}},
					{field : 'deviceModel',title : '终端机型',width : 75,align : 'center'},
					{field : 'deviceType',title : '终端类型',width : 75,align : 'center',
						formatter:function(deviceType){
							if(deviceType == 1){
								return "手机";
							}else if(deviceType == 2){
								return "平板";
							}else{
								return "";
							}
						}},
					{field : 'osVersion',title : '系统版本号',width : 75,align : 'center'},
					{field : 'osVersionName',title : '系统版本',width : 75,align : 'center'},
					{field : 'state',title : '显示评论',width : 75,align : 'center',
						formatter:function(state){
							if(state == "0"){
								return "不显示";
							}else if(state == "1"){
								return "显示";
							}else{
								return "";
							}
							
						}},
					{field : 'deviceVendor',title : '手机厂商',width : 75,align : 'center'},
					{field : 'createTime',title : '崩溃时间',width : 150,align : 'center',
						formatter:function(createTime){
							return formatDateTime(createTime, true);
						}}
					]],
				//toolbar : eval(toolbar),
				onLoadSuccess:function(data){
					//alert("load success !");
				}
			});
		}
		
		//导出
		function exportExcel(){
			var total = datagrid.datagrid('getPager').data("pagination").options.total;
			if(total > 100*1000){
				$.messager.alert("温馨提示","对不起，您导出的数据已经超过上限，请修改搜索条件重新导出。");
				return false;
			}
			//导出调用
			var xmlName = "ravedebaclelog.xml";
			window.document.location = __ctxPath + "/export/export2Excel?xmlName="
					+ xmlName
					+ "&sort=id"
					+ "&Q_dataSource=MYSQL2"
					+ "&Q_phone=" + $('#Q_phone').val()
					+ "&Q_startTime=" + $('#Q_startTime').val()
					+ "&Q_endTime=" + $('#Q_endTime').val();
		}
		
		function show(id){
			$.ajax({
				url : '${ctx}/ravedebaclelog/queryByPrimarykey',
				type : 'POST',
				dataType : 'json',
				data : {"Q_id" : id},
				success : function(data){
					var content = data.content;
					$("#showcontent").html("");
					$("#showcontent").html(content);
					$("#showmore").window("open");
				}
			});
		}
	</script>
</head>
<body>
	<div class="easyui-panel" title="搜索" iconCls="icon-search" collapsible="true" doSize="true">
		<div class="panel_search">
			<label>开始日期 : </label><input id="Q_startTime" readonly="readonly" class="Wdate" style="width:130px;">
			<label>--&nbsp;结束日期 : </label><input id="Q_endTime" readonly="readonly" class="Wdate" style="width:130px;">
			&nbsp;&nbsp; <label style="padding-right:5px;">平台名称:&nbsp;<input id='Q_masPackageName' name='Q_masPackageName' maxlength="32"/></label>
			&nbsp;&nbsp;<label style="padding-right:5px;">平台版本名称:&nbsp;<input id='Q_masVersionName' name='Q_masVersionName' maxlength="32"/></label>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cancelSearchButton" name="cancelSearchButton" iconCls="icon-cancel">重置搜索</a>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="searchButton" name="searchButton" iconCls="icon-search">搜索</a>
			</div>
		</div>
	<div style="margin-top: 5px;">
	<table id="ravedebaclelog_datagrid" class="easyui-datagrid" ></table>
	</div>
	<div id="showmore" title="详情" class="easyui-window" style="width:750px;height:450px;padding:5px;background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div id="showcontent" region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
			</div>
		</div>
	</div>
</body>
</html>