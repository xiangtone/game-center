<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>平台注册用户</title>
	<script src="${ctx}/static/js/util.js"></script>
	<script type="text/javascript">
		menu_flag = 'masuser';
		var toolbar = "[{text : '导出EXCEL',iconCls : 'icon-undo',handler : exportExcel}]";
		var __ctxPath = "${ctx}";
		$(function(){
			loadDatagrid();
			$('#searchButton').click(function(){
				datagrid.datagrid('load',{
					//"Q_userId" : $('#Q_userId').val(),
					"Q_userName" : $('#Q_userName').val(),
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				});
			});
			$('#cancelSearchButton').click(function(){
				//$('#Q_userId').val("");
				$('#Q_userName').val("");
				$('#Q_startTime').val('');
				$('#Q_endTime').val('');
				$("#Q_endTime").datepicker("option","minDate",null);
				$("#Q_startTime").datepicker("option","maxDate",null);
			});
		});
		
		function loadDatagrid(){
			datagrid = $("#masuser_detail_datagrid").datagrid({
				width:$("#rightbox").outerWidth(),
				height : $(".z_nav").outerHeight() - 52,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : __ctxPath + '/masuser/query',
				loadMsg : '数据加载.....',
				sortName : 'userId',
				sortOrder : 'desc',
				remoteSort : false,
				pagination : true,
				rownumbers : true,
				fitColumns:true,
				//showFooter : true,
				idField : 'userId',
				queryParams : {
					"limit" : 10,
					"start" : 0,
					//"Q_userId" : $('#Q_userId').val(),
					"Q_userName" : $('#Q_userName').val(),
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				},
				columns : [[
					/*{field : 'userId',title : '编号',width : 50,align : 'center'},*/
					{field : 'userName',title : '用户名',width : 100,align : 'center'},
					{field : 'nickName',title : '昵称',width : 100,align : 'center'},
					/*{field : 'sex',title : '性别',width : 50,align : 'center',
						formatter:function(sex){
							if(sex == "0"){
								return '女';
							}else{
								return '男';
							}
						}},
					{field : 'age',title : '年龄',width : 50,align : 'center'},*/
					{field : 'phone',title : '手机号码',width : 100,align : 'center'},
					{field : 'aValue',title : '虚拟货币',width : 60,align : 'center'},
					{field : 'aValuePresent',title : '全局赠送',width : 60,align : 'center'},
					{field : 'rechargeNum',title : '充值次数',width : 60,align : 'center'},
					{field : 'aValueAll',title : '充值总额',width : 60,align : 'center'},
					{field : 'aValuePresentAll',title : '赠送总额',width : 60,align : 'center'},
					{field : 'state',title : '状态',width : 50,align : 'center',
						formatter:function(state){
							if(state == "0"){
								return "离线";
							}else{
								return "在线";
							}
						}},
					{field : 'address',title : '地址',width : 100,align : 'center'},
					{field : 'createTime',title : '日期',width : 80,align : 'center',
					formatter:function(rechargeTime){
						//return formatDateTime(rechargeTime, true);
						return rechargeTime;
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
			var xmlName = "masuser.xml";
			window.document.location = __ctxPath + "/export/export2Excel?xmlName="
					+ xmlName
					+ "&sort=userId"
					+ "&order=desc"
					+ "&Q_dataSource=MYSQL2"
					+ "&Q_userName=" + $('#Q_userName').val()
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
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cancelSearchButton" name="cancelSearchButton" iconCls="icon-cancel">重置搜索</a>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="searchButton" name="searchButton" iconCls="icon-search">搜索</a>
			</div>
		</div>
	<div style="margin-top: 5px;">
	<table id="masuser_detail_datagrid" class="easyui-datagrid"></table>
	</div>
</body>
</html>