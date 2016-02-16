<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>铃声下载统计>详情</title>
	<script src="${ctx}/static/js/util.js"></script>
	<script type="text/javascript">
		menu_flag = 'musicdownload';
		var toolbar = "[{text : '导出EXCEL',iconCls : 'icon-undo',handler : exportExcel}]";
		var __ctxPath = "${ctx}";
		$(function(){
			loadDatagrid();
			$('#searchButton').click(function(){
				datagrid.datagrid('load',{
					"format" : "true",
					"Q_theDate" : $('#Q_theDate').val(),
					"Q_country" : $("#Q_country").val(),
					"Q_musicId" : $('#Q_musicId').val()
				});
			});
			$('#cancelSearchButton').click(function(){
				$("#Q_country").val("");
			});
		});
		
		function loadDatagrid(){
			datagrid = $("#music_detail_datagrid").datagrid({
				width:$("#rightbox").outerWidth(),
				height : $(".z_nav").outerHeight() - 52,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : __ctxPath + '/musicdownload/view',
				loadMsg : '数据加载.....',
				sortName : 'theDate',
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
					"format" : "true",
					"Q_theDate" : $('#Q_theDate').val(),
					"Q_country" : $("#Q_country").val(),
					"Q_musicId" : $('#Q_musicId').val()
				},
				columns : [[
					{field : 'theDate',title : '时间',width : 150,align : 'center'},
					{field : 'appTypeString',title : '应用类型',width : 100,align : 'center'},
					{field : 'musicId',title : '铃声编号',width : 150,align : 'center'},
					{field : 'musicName',title : '铃声名称',width : 150,align : 'center'},
					{field : 'countryCn',title : '国家',width : 100,align : 'center'},
					{field : 'categoryName',title : '应用分类',width : 100,align : 'center'},
					{field : 'downloadNum',title : '下载次数',width : 100,align : 'center'}
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
			var xmlName = "musicdownload.xml";
			window.document.location = __ctxPath + "/export/export2Excel?xmlName="
					+ xmlName
					+ "&sort=theDate"
					+ "&format=true"
					+ "&Q_dataSource=MYSQL2"
					+ "&order=desc"
					+ "&Q_country=" + $("#Q_country").val()
					+ "&Q_theDate=" + $("#Q_theDate").val()
					+ "&Q_musicId=" + $('#Q_musicId').val();
		}
	</script>
</head>
<body>
	<div class="easyui-panel" title="搜索" iconCls="icon-search" collapsible="true" doSize="true">
	<div class="panel_search">
			<input id="Q_theDate" name="Q_theDate"  type="hidden" value="${param.theDate }">
			<input id='Q_musicId' name='Q_musicId'  type="hidden" value="${param.musicId }"/>
			<label>国家:</label>&nbsp;<input id='Q_country' name='Q_country' maxlength="50"/>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cancelSearchButton" name="cancelSearchButton" iconCls="icon-cancel">重置搜索</a>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="searchButton" name="searchButton" iconCls="icon-search">搜索</a>
			&nbsp;&nbsp;<a href="javascript:history.back();"  class="easyui-linkbutton" iconCls="icon-undo">返回</a>
			</div>
		</div>
	<div style="margin-top: 5px;">
	<table id="music_detail_datagrid" class="easyui-datagrid"></table>
	</div>
</body>
</html>