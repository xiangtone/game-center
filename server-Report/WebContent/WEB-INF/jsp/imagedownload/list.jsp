<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>图片下载统计</title>
	<script src="${ctx}/static/js/util.js"></script>
	<script type="text/javascript">
		menu_flag = 'imagedownload';
		var toolbar = "[{text : '导出EXCEL',iconCls : 'icon-undo',handler : exportExcel}]";
		var __ctxPath = "${ctx}";
		$(function(){
			loadDatagrid();
			$('#searchButton').click(function(){
				datagrid.datagrid('load',{
					"format" : "true",
					"Q_groupBy" : "theDate,imageId",
					"Q_imageName" : $('#Q_imageName').val(),
					"Q_country" : $('#Q_country').val(),
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				});
			});
			$('#cancelSearchButton').click(function(){
				$('#Q_imageName').val("");
				$('#Q_startTime').val('');
				$('#Q_endTime').val('');
				$('#Q_country').val(''),
				$("#Q_endTime").datepicker("option","minDate",null);
				$("#Q_startTime").datepicker("option","maxDate",null);
			});
		});
		
		function loadDatagrid(){
			datagrid = $("#image_detail_datagrid").datagrid({
				width:$("#rightbox").outerWidth(),
				height : $(".z_nav").outerHeight() - 52,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : __ctxPath + '/imagedownload/query',
				loadMsg : '数据加载.....',
				sortName : 'theDate',
				sortOrder : 'desc',
				remoteSort : false,
				pagination : true,
				rownumbers : true,
				fitColumns:true,
				showFooter : true,
				idField : 'id',
				queryParams : {
					"limit" : 10,
					"start" : 0,
					"format" : "true",
					"Q_groupBy" : "theDate,imageId",
					"Q_imageName" : $("#Q_imageName").val(),
					"Q_country" : $("#Q_country").val(),
					"Q_startTime" : $('#Q_startTime').val(),
					"Q_endTime" : $('#Q_endTime').val()
				},
				columns : [[
					{field : 'theDate',title : '时间',width : 150,align : 'center'},
					{field : 'appTypeString',title : '应用类型',width : 100,align : 'center'},
					{field : 'imageId',title : '图片编号',width : 150,align : 'center'},
					{field : 'imageName',title : '图片名称',width : 150,align : 'center'},
					/*{field : 'countryCn',title : '国家',width : 100,align : 'center'},*/
					{field : 'categoryName',title : '应用分类',width : 100,align : 'center'},
					{field : 'downloadNum',title : '下载次数',width : 100,align : 'center'},
					{field : 'countryCn',title : '操作',width : 100,align : 'center',
						formatter:function(countryCn,rowData){
							if(null != countryCn && '' != countryCn){
								return "<a href='${ctx}/imagedownload/showMore?theDate=" + rowData.theDate 
								+ "&imageId=" + rowData.imageId + "'>详情</a>";
							}else{
								return "";//此处为了屏蔽掉底部总计栏的详情url的显示.
							}
						}
					}]],
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
			var xmlName = "imagedownload_groupby.xml";
			window.document.location = __ctxPath + "/export/export2Excel?xmlName="
					+ xmlName
					+ "&sort=theDate"
					+ "&Q_dataSource=MYSQL2"
					+ "&order=desc"
					+ "&format=true"
					+ "&Q_groupBy=theDate,imageId"
					+ "&Q_imageName=" + $("#Q_imageName").val()
					+ "&Q_country=" + $("#Q_country").val()
					+ "&Q_startTime=" + $('#Q_startTime').val()
					+ "&Q_endTime=" + $('#Q_endTime').val();
		}
	</script>
</head>
<body>
	<div class="easyui-panel" title="搜索" iconCls="icon-search"
		collapsible="true" doSize="true">
		<div class="panel_search">
		<label>开始日期 : </label><input id="Q_startTime" readonly="readonly"
			class="Wdate" style="width: 130px;"> <label>--&nbsp;结束日期
			: </label><input id="Q_endTime" readonly="readonly" class="Wdate"
			style="width: 130px;"> <label>图片名称:</label>&nbsp;<input
			id='Q_imageName' name='Q_imageName' maxlength="50" /> <label>国家:</label>&nbsp;<input
			id='Q_country' name='Q_country' maxlength="50" /> &nbsp;&nbsp;<a
			href="javascript:void(0)" class="easyui-linkbutton"
			id="cancelSearchButton" name="cancelSearchButton"
			iconCls="icon-cancel">重置搜索</a> &nbsp;&nbsp;<a
			href="javascript:void(0)" class="easyui-linkbutton"
			id="searchButton" name="searchButton" iconCls="icon-search">搜索</a>
			</div>
	</div> 
	<div style="margin-top: 5px;">
		<table id="image_detail_datagrid" class="easyui-datagrid"></table>
	</div>
</body>
</html>