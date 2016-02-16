<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>菜单管理</title>
	<script src="${ctx}/static/js/util.js"></script>
	<script type="text/javascript">
		menu_flag = 'menu';
		var toolbar = "[{text : '新增菜单',iconCls : 'icon-add',handler : add},"
		              + "{text : '编辑菜单',iconCls : 'icon-edit',handler : edit},"
		              + "{text : '删除菜单',iconCls : 'icon-no',handler : del},"
		              + "{text : '取消选择',iconCls : 'icon-undo',handler : cancel}]";
		var __ctxPath = "${ctx}";
		$(function(){
			loadDatagrid();
		    $.extend($.fn.validatebox.defaults.rules, {
		        number: {
		            validator: function(value, param){
		                return /^[0-9]+$/.test(value);
		            },
		            message: '必须为数字'
		        },
		        letter: {
		        	validator: function(value, param){
		                return /^[a-zA-Z]+$/.test(value);
		            },
		            message: '必须为字母'
		        },
		        selectValueRequired: {
		        	validator: function(value,param){
		        		return value != '请选择';
		        	},
		        	message:'该选项不能为空'
		        }
		    });
			//保存数据
			$("#saveBtn").click(function(){
				if($("#addForm").form('validate')){
					var flag = $("#flag").val();
					var success = true;
					valid(flag,success);
				}else{
					return false;
				}
			});
			
			//取消保存
			$("#cancelBtn").click(function(){
				$("#addwindow").window("close");
			});
			
			$("#typeid").combobox({
				onSelect : function(record){
					var flag = $("#flag").val();
					//新增菜单只能选择一级主菜单,不能新增子级菜单
					if(parseInt(record.value) > 1){
						if(flag == "add"){
							$.messager.alert("温馨提示","只能新增一级主菜单,二级子菜单新增功能不开放");
							$("#typeid").combobox("select",1);
							return false;
						}
						loadParentMenu(record.value);
					}
				}
			});
			
		});
		
		//数据唯一性校验数据
		function valid(flag,success){
			$.ajax({
				type : "POST",
				url : '${ctx}/menu/valid',
				dataType : "json",
				data : $("#addForm").serialize(),
				success : function(data){
					$("#errorMsg").html("");
					//非出现重复数据
					if(data.count == 0){
						save(flag);
					}else{
						$("#errorMsg").html("已存在相同菜单编码/名称/请求接口!请检查.");
					}
				}
			});
		}
		
		//提交数据保存
		function save(flag){
			//提交数据
			$.ajax({
				type : "POST",
				url : '${ctx}/menu/' + flag,
				dataType : "json",
				data : $("#addForm").serialize(),
				success : function(data){
					$("#addwindow").window("close");
					$.messager.alert("温馨提示:", data.desc,null,function(){
						window.location.href = "${ctx}/menu/list";
					});
				}
			});
		}
		
		//加载数据
		function loadDatagrid(){
			datagrid = $("#menu_detail_datagrid").datagrid({
				width:$("#rightbox").outerWidth(),
				height : $(".z_nav").outerHeight() - 52,
				nowrap : false,
				striped : true,
				collapsible : true,
				url : __ctxPath + '/menu/query',
				loadMsg : '数据加载.....',
				sortName : 'id',
				sortOrder : 'desc',
				remoteSort : false,
				//pagination : true,
				rownumbers : true,
				fitColumns:true,
				//showFooter : true,
				idField : 'id',
				queryParams : {
					"limit" : 10,
					"start" : 0
				},
				frozenColumns : [[{field : 'ck',checkbox : true}]],
				columns : [[
					/*{field : 'id',title : '编号',width : 50,align : 'center'},
					{field : 'code',title : '编码',width : 100,align : 'center'},*/
					{field : 'name',title : '菜单名称',width : 100,align : 'center'},
					{field : 'type',title : '菜单类型',width : 100,align : 'center',
						formatter:function(type){
							if(type.id == 1){
								return "一级菜单";
							}else{
								return "二级菜单";
							}
						}
					},
					{field : 'seq',title : '排序序号',width : 100,align : 'center'}/*,
					{field : 'uri',title : '请求接口',width : 100,align : 'center'}*/
					]],
				toolbar : eval(toolbar),
				onLoadSuccess:function(data){
					//alert("load success !");
				}
			});
		}
		
		//新增菜单
		function add(){
			$("#errorMsg").html("");
			$("#addForm").form('clear');
			$("#flag").val('add');
			//$("#parentId").combobox("loadData",eval('[{value:"",text:"请选择"}]'));
			$("#typeid").combobox({disabled:true}).combobox("select",1);
			$("#parentId").combobox({disabled:true});
			$("#addwindow").window({title : '新增菜单'}).window("open");
		}
		
		//编辑菜单
		function edit(){
			$("#errorMsg").html("");
			$("#addForm").form('clear');
			var ckRows = datagrid.datagrid("getSelections");
			if(ckRows.length == 0){
				$.messager.alert("温馨提示:","请选择一条记录.");
			}else if(ckRows.length > 1){
				$.messager.alert("温馨提示:","只能选择一条记录.");
			}else{
				var data = datagrid.datagrid("getSelected");
				$("#id").val(data.id);
				$("#typeid").combobox({disabled:true}).combobox("setValue",data.type.id);
				if(data.type.id == 1){
					$("#parentId").combobox({disabled:true});
				}else{
					$("#parentId").combobox({disabled:false});
				}
				loadParentMenu(data.type.id);
				$("#parentId").combobox("select",data.parentId);
				$("#flag").val('edit');
				$("#name").val(data.name);
				//$("#code").val(data.code);
				//$("#icon").val(data.icon);
				$("#seq").val(data.seq);
				//$("#uri").val(data.uri);
				$("#addwindow").window({
					title : '编辑菜单'
				}).window('open');
			}
		}
		
		//删除菜单
		function del(){
			var rows = datagrid.datagrid("getSelections");
			if(rows.length == 0){
				$.messager.alert("温馨提示:","请选择需要删除的数据.");
			}else{
				$.messager.confirm("确认提示", "您确认要删除该数据吗?", function(b){
					if(b){
						var ids = "";
						for(var i = 0; i < rows.length; i ++){
							ids += rows[i].id + ",";
						}
						ids = ids.substring(0, ids.length - 1);
						$.ajax({
							type : "POST",
							dataType : "json",
							url : "${ctx}/menu/delete",
							data : {"ids" : ids},
							success : function(data){
								$.messager.alert("温馨提示",data.desc,null,function(){
									window.location.href = "${ctx}/menu/list";
								});
							}
						});
					}
				});
			}
		}
		
		//取消所选
		function cancel(){
			datagrid.datagrid("clearSelections");
			$("input:checkbox").attr("checked",false);
			$("#errorMsg").html("");
			$("#parentId").combobox("select","");
			$("#typeid").combobox("select",0);
		}
		
		//加载父级菜单
		function loadParentMenu(typeId){
			var datas = datagrid.datagrid("getRows");
			var data = [];
			//data.push({"value":"","text":"请选择","select":true});
			$("#parentId").combobox("clear");
			$.each(datas,function(index,val){
				if((parseInt(typeId) - 1) == val.type.id){
					data.push({"value":val.id,"text": val.name});
				}
			});
			$("#parentId").combobox("loadData",data);
		}
		
	</script>
</head>
<body>
	<div style="margin-top: 5px;">
	<table id="menu_detail_datagrid" class="easyui-datagrid"></table>
	</div>
	<div id="addwindow" class="easyui-window" title="新增菜单" iconCls="icon-save" closable="false"
	closed="true" minimizable="false" maximizable="false" collapsible="false" modal="true" 
	style="width:400px;height:300px;padding:5px;background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<form id="addForm" method="post">
					<input type="hidden" id="id" name="id" value=""/>
					<input type="hidden" id="flag" name="flag" value=""/>
					<%--<label>菜单编码:&nbsp;</label><input class="easyui-validatebox" required="true" maxlength="20"
					validType="letter" type="text" name="code" id="code"/><font color=red>&nbsp;*</font><br /><br /> --%>
					<label>菜单名称:&nbsp;</label><input class="easyui-validatebox" maxlength="16"
					required="true" type="text" name="name" id="name"/><font color=red>&nbsp;*</font><br /><br />
					<label>菜单类型:</label>
					<select name="type.id" class="easyui-combobox" style="width: 150px;"
					editable="false" validType="selectValueRequired" id="typeid">
						<option value="0" selected="selected">请选择</option>
						<option value="1">一级菜单</option>
						<option value="2">二级菜单</option>
					</select><font color=red>&nbsp;*</font><br /><br />
					<label>父级菜单:</label>
					<select name="parentId" id="parentId" class="easyui-combobox" editable="false" style="width: 150px;"></select><br /><br />
					<%--<label>菜单图标 :</label><input type="text" name="icon" maxlength="45"/><br /><br /> --%>
					<label>排序序号:&nbsp;</label><input class="easyui-validatebox" validType="number" type="text" name="seq" id="seq" maxlength="5"/><br /><br />
					<%--<label>请求路径:&nbsp;</label><input class="easyui-validatebox" required="true" maxlength="45" type="text" name="uri" id="uri" /><font color=red>&nbsp;*</font><br /><br />--%>
					<span id="errorMsg" style="color: red;"></span>
				</form>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a id="saveBtn" class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)">保存</a>
				<a id="cancelBtn" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)">取消</a>
			</div>
		</div>
	</div>
</body>
</html>