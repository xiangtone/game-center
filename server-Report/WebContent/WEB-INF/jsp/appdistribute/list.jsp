<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>应用分发统计</title>
	<script src="${ctx}/static/js/util.js"></script>
	<script type="text/javascript">
		menu_flag = 'appdistribute';
		var toolbar = "[{text : '导出EXCEL',iconCls : 'icon-undo',handler : exportExcel}]";
		var __ctxPath = "${ctx}";
		$(function(){
			loadDatagrid();
			$('#searchButton').click(function(){
				datagrid.datagrid('load',{
					"format" : "true",
					"Q_appNameId" : $('#Q_appNameId').val(),
					"Q_packageName" : $('#Q_packageName').val(),
					"Q_category_parent" : $('#Q_category_parent').val(),
					"Q_categoryId" : $('#Q_categoryId').val(),
					"Q_raveId" : $('#Q_raveId').val()
				});
			});
			$('#cancelSearchButton').click(function(){
				$('#Q_appNameId').val("");
				$('#Q_packageName').val('');
				$('#Q_category_parent').val('');
				$('#Q_categoryId').val('');
				$('#Q_raveId').val(1);
				//消除下拉列表选项
				clearOptions();
			});
			
		});
		
		function loadDatagrid(){
			datagrid = $("#distribute_detail_datagrid").datagrid({
				width:$("#rightbox").outerWidth(),
				height : $(".z_nav").outerHeight() - 52,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : __ctxPath + '/appdistribute/query',
				loadMsg : '数据加载.....',
				sortName : 'appId',
				sortOrder : 'desc',
				remoteSort : false,
				pagination : true,
				pageSize : 10,
				rownumbers : true,
				fitColumns: false,
				//showFooter : true,
				idField : 'appId',
				queryParams : {
					"limit" : 10,
					"start" : 0,
					"format" : "true",
					'Q_appNameId' : $("#Q_appNameId").val(),
					'Q_packageName' : $("#Q_packageName").val(),
					'Q_category_parent' : $("#Q_category_parent").val(),
					"Q_categoryId" : $('#Q_categoryId').val(),
					"Q_raveId" : $('#Q_raveId').val()
				},
				columns : [[
					{field : 'appId',title : '应用ID',width : 100,align : 'center'},
					{field : 'appName',title : '应用名称',width : 100,align : 'center'},
					{field : 'countryName',title : '国家',width : 100,align : 'center'},
					{field : 'categoryName',title : '应用分类',width : 100,align : 'center'},
					{field : 'initDowdload',title : '初始下载量',width : 100,align : 'center'},
					{field : 'realDowdload',title : '实际下载量',width : 100,align : 'center'},
					{field : 'pageOpen',title : '浏览次数',width : 100,align : 'center'},
					{field : 'updateNum',title : '更新次数',width : 100,align : 'center'},
					{field : 'versionName',title : '版本名称',width : 100,align : 'center'},
					{field : 'stars',title : '星级',width : 50,align : 'center'},
					{field : 'fileSize',title : '文件大小',width : 100,align : 'center'},
					{field : 'createTimeString',title : '入库日期',width : 100,align : 'center'},
					{field : 'updateTimeString',title : '修改日期',width : 100,align : 'center'},
					{field : 'hrHigerank',title : '最高排名<br />(Home RECOMMEND)',width : 120,align : 'center'},
					{field : 'hrLowrank',title : '最低排名<br />(Home RECOMMEND)',width : 120,align : 'center'},
					{field : 'hnHigerank',title : '最高排名<br />(Home NEW)',width : 100,align : 'center'},
					{field : 'hnLowrank',title : '最低排名<br />(Home NEW)',width : 100,align : 'center'},
					{field : 'htHigerank',title : '最高排名<br />(Home TOP)',width : 100,align : 'center'},
					{field : 'htLowrank',title : '最低排名<br />(Home TOP)',width : 100,align : 'center'},
					{field : 'hpHigerank',title : '最高排名<br />(Home POPULAR)',width : 120,align : 'center'},
					{field : 'hpLowrank',title : '最低排名<br />(Home POPULAR)',width : 120,align : 'center'},
					{field : 'ahHigerank',title : '最高排名<br />(App HOT)',width : 100,align : 'center'},
					{field : 'ahLowrank',title : '最低排名<br />(App HOT)',width : 100,align : 'center'},
					{field : 'atHigerank',title : '最高排名<br />(App TOP)',width : 100,align : 'center'},
					{field : 'atLowrank',title : '最低排名<br />(App TOP)',width : 100,align : 'center'},
					{field : 'anHigerank',title : '最高排名<br />(App NEW)',width : 100,align : 'center'},
					{field : 'anLowrank',title : '最低排名<br />(App NEW)',width : 100,align : 'center'},
					{field : 'ghHigerank',title : '最高排名<br />(Game HOT)',width : 100,align : 'center'},
					{field : 'ghLowrank',title : '最低排名<br />(Game HOT)',width : 100,align : 'center'},
					{field : 'gtHigerank',title : '最高排名<br />(Game TOP)',width : 100,align : 'center'},
					{field : 'gtLowrank',title : '最低排名<br />(Game TOP)',width : 100,align : 'center'},
					{field : 'gnHigerank',title : '最高排名<br />(Game NEW)',width : 100,align : 'center'},
					{field : 'gnLowrank',title : '最低排名<br />(Game NEW)',width : 100,align : 'center'}
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
				$.messager.alert("温馨提示","对不起，您导出的数据已经超过10万上限，建议您修改导出条件重新导出。");
				return false;
			}
			//导出调用
			var xmlName = "appdistribute.xml";
			window.document.location = __ctxPath + "/export/export2Excel?xmlName="
					+ xmlName
					+ "&sort=appId"
					+ "&Q_dataSource=MYSQL3"
					+ "&order=desc"
					+ "&format=true"
					+ "&Q_appNameId=" + $("#Q_appNameId").val()
					+ "&Q_packageName=" + $("#Q_packageName").val()
					+ "&Q_category_parent=" + $("#Q_category_parent").val()
					+ "&Q_categoryId=" + $('#Q_categoryId').val()
					+ "&Q_raveId=" + $('#Q_raveId').val();
		}
		
		function selectChange(value){
			if(value != ''){
				//消除下拉列表选项
				clearOptions();
				$.ajax({
					url : "${ctx}/category/query",
					type : "POST",
					dataType : "json",
					data : {"id" : value},
					success : function (data){
						//var result = data.categorys;eval("([" + data.categorys + "])");
						var categorySelect = document.getElementById("Q_categoryId");
						$.each(data.categorys,function(index,val){
							categorySelect.add(new Option(val.name, val.id));
						});
					},
					error : function (XMLHttpRequest, textStatus, errorThrown){
						$.messager.alert("提示:","请求失败,系统出现未知异常!");
					}
				});
			}
		}
		
		function clearOptions(){
			var categorySelect = document.getElementById("Q_categoryId");
			for(var i = 1 ; i < categorySelect.options.length;){
				categorySelect.removeChild(categorySelect.options[i]);
			}
		}
		
	</script>
</head>
<body>
	
	<div class="easyui-panel" title="搜索" iconCls="icon-search" collapsible="true" doSize="true">
			<div class="panel_search">
			<label>应用名称/应用ID:</label>&nbsp;<input id='Q_appNameId' name='Q_appNameId' maxlength="50"/>
			<label>包名 : </label><input id="Q_packageName" name="Q_packageName" maxlength="50">
			&nbsp;&nbsp;
			<label>一级分类:</label>&nbsp;
			<select id="Q_category_parent" name="Q_category_parent" onchange="selectChange(this.value)" style="border:1px solid #969ba8;">
				<option value="" selected="selected">全部</option>
				<option value="2">Apps</option>
				<option value="3">Games</option>
			</select>
			&nbsp;&nbsp;
			<label>二级分类:</label>&nbsp;
			<select id="Q_categoryId" name="Q_categoryId" style="border:1px solid #969ba8;">
				<option value="0" selected="selected">--全部--</option>
			</select>
			&nbsp;&nbsp;
			<label>国家:</label>&nbsp;
			<select id="Q_raveId" name="Q_raveId" style="border:1px solid #969ba8;">
				<c:forEach var="country" items="${countrys}">
					<option value="${country.id}" <c:if test="${country.id==1}"> selected="selected"</c:if>>${country.name}(${country.nameCn})</option>
				</c:forEach>
			</select>
			&nbsp;&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton"
				id="cancelSearchButton" name="cancelSearchButton" iconCls="icon-cancel">重置搜索</a>
			&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"
				id="searchButton" name="searchButton" iconCls="icon-search">搜索</a>
			</div>
		</div>
	<div style="margin-top: 5px;">
	<table id="distribute_detail_datagrid" class="easyui-datagrid"></table>
	</div>
</body>
</html>