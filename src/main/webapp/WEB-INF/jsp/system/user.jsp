<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<%@include file="../common.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="User.manage" /></title>
</head>
<body>
	<!-- 列表 -->
	<div>
		<table id="datatable"></table>
	</div>
	
	<div id="toolbar">
		<shiro:hasPermission name="system-user-add">
			<button type="button" class="btn btn-info" onclick="add()"><spring:message code="PUB.add" /></button>
		</shiro:hasPermission>
		<shiro:hasPermission name="system-user-upd">
			<button type="button" class="btn btn-warning" onclick="upd()"><spring:message code="PUB.upd" /></button>
		</shiro:hasPermission>
		<shiro:hasPermission name="system-user-del">
			<button type="button" class="btn btn-danger" onclick="del()"><spring:message code="PUB.del" /></button>
		</shiro:hasPermission>
	</div>
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel"><spring:message code="PUB.upd" /></h4>
	            </div>
	            <form id="myForm">
	            	<input type="hidden" id="userId" name="userId" />
		            <div class="modal-body">
		            	<div>
		            		<spring:message code="User.name" />:<input type="text" id="name" name="name" />
		            	</div>
		            	<div>
		            		<spring:message code="User.age" />:<input type="text" id="age" name="age" />
		            	</div>
		            	<div>
		            		<spring:message code="User.remark" />:<input type="text" id="remark" name="remark" />
		            	</div>
		            </div>
		            <div class="modal-footer">
		                <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="PUB.close" /></button>
		                <button type="button" class="btn btn-primary" id="save"><spring:message code="PUB.ok" /></button>
		            </div>
	            </form>
	        </div><!-- /.modal-content -->
	    </div><!-- /.modal -->
	</div>
	
	<script type="text/javascript">
	function add() {
		
	}

	// 点击修改
	function upd() {
		var selections = $('#datatable').bootstrapTable('getSelections');
		if (selections.length < 1) {
			alert("Please select records...");
			return;
		}
		if (selections.length > 1) {
			alert("Only select one record...");
			return;
		}
		$.ajax({
			url:'${ctx}/system/user/getOne.do',
			type:'post',
			dataType:'json',
			data:{
				userId : selections[0].userId
			},
			success:function(result){
				if (result.CODE=="200") {
					var user = result.data;
					$("#userId").val(user.userId);
					$("#name").val(user.name);
					$("#age").val(user.age);
					$("#remark").val(user.remark);
					$('#myModal').modal('show');
				}
	    	},
	    	error:function(xhr,status,error){
	    		
	    	}
		});
	}
	
	// 修改提交
	$("#save").click(function(){
		$.ajax({
			url:'${ctx}/system/user/upd.do',
			type:'post',
			dataType:'json',
			data:{
				userId : $("#userId").val(),
				name : $("#name").val(),
				age : $("#age").val(),
				remark : $("#remark").val()
			},
			success:function(result){
				if (result.CODE=="200") {
					$('#myModal').modal('hide');
					$('#datatable').bootstrapTable('refresh');
				}
	    	},
	    	error:function(xhr,status,error){
	    		
	    	}
		});	
	});
	
	// 删除记录
	function del() {
		var selections = $('#datatable').bootstrapTable('getSelections');
		if (selections.length < 1) {
			alert("Please select records...");
			return;
		}
		if (confirm("Are you sure to delete the records?")) {
			var delIds = '';
			for (var i in selections) {
				var obj = selections[i];
				if (delIds != '') {
					delIds += ';';
				}
				delIds += obj.userId;
			}
			$.ajax({
				url:'${ctx}/system/user/del.do',
				type:'post',
				dataType:'json',
				data:{
					delIds : delIds
				},
				success:function(result){
					if (result.CODE=="200") {
						$('#datatable').bootstrapTable('refresh');
					}
		    	},
		    	error:function(xhr,status,error){
		    		
		    	}
			});
		}
	}
	
	
	$(function () {
        //1.初始化Table
        var oTable = new TableInit();
        oTable.Init();
    });
 
    var TableInit = function () {
        var oTableInit = new Object();
        //初始化Table
        oTableInit.Init = function () {
            $('#datatable').bootstrapTable({
                url: '${ctx }/system/user/getUser.do',         //请求后台的URL（*）
                method: 'post',                      //请求方式（*）
                contentType : 'application/x-www-form-urlencoded',// 默认是application/json
                toolbar: '#toolbar',                //工具按钮用哪个容器
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pagination: true,                   //是否显示分页（*）
                sortable: false,                     //是否启用排序
//                 sortOrder: "asc",                   //排序方式
                queryParams: oTableInit.queryParams,//传递参数（*）
                sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
                pageNumber:1,                       //初始化加载第一页，默认第一页
                pageSize: 10,                       //每页的记录行数（*）
                pageList: [5, 10, 25],        //可供选择的每页的行数（*）
                strictSearch: true,
                clickToSelect: true,                //是否启用点击选中行
                //height: 460,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                uniqueId: "userId",                     //每一行的唯一标识，一般为主键列
                cardView: false,                    //是否显示详细视图
                detailView: false,                   //是否显示父子表
                showRefresh: true,
                showColumns: true,
                search: true,//显示搜索框
                singleSelect: false,
                clickToSelect: true,
                columns: [{
                    field: '',
                    title: '',
                    width : '5%',
                    checkbox: true
                }, {
                    field: 'userId',
                    title: 'No.',
                    width : '5%',
                    formatter : function (value, row, index) {
                    	return index+1;
                    }
                }, {
                    field: 'name',
                    title: '<spring:message code="User.name" />',
                    width : '20%'
                }, {
                    field: 'password',
                    title: '<spring:message code="User.password" />',
                    width : '20%'
                }, {
                    field: 'age',
                    title: '<spring:message code="User.age" />',
                    width : '10%'
                }, {
                    field: 'remark',
                    title: '<spring:message code="User.remark" />',
                    width : '40%',
                    editable:true
                }]
            });
        };
      	//得到查询的参数
      	oTableInit.queryParams = function (params) {
            var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                limit: params.limit,   //页面大小
                offset: params.offset,  //页码
                search: params.search,
                sort: params.sort,
                sortOrder: params.sortOrder
//                 sdate: $("#stratTime").val(),
//                 maxrows: params.limit,
//                 pageindex:params.pageNumber,
            };
            return temp;
        };
        
        return oTableInit;
    };
	</script>
	
	
<!--  
	<div>
		<table id="dg" title="My Users" class="easyui-datagrid"
			url="${ctx }/system/user/getUser.do" toolbar="#toolbar" rownumbers="true"
			fitColumns="true" singleSelect="true">
			<thead>
				<tr>
					<th field="userId" width="50">UserId</th>
					<th field="name" width="50">Name</th>
					<th field="password" width="50">Password</th>
					<th field="age" width="50">Age</th>
					<th field="remark" width="50">Remark</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar">
			<shiro:hasPermission name="system-user-add">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">New User</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="system-user-upd">
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">Edit User</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="system-user-del">
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyUser()">Remove User</a>
			</shiro:hasPermission>
		</div>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width: 400px; height: 280px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
		<div>User Information</div>
		<form id="fm" method="post">
			<div>
				<label>Name:</label>
				<input type="text" name="name" class="easyui-validatebox" required="true">
			</div>
			<div>
				<label>Password:</label>
				<input type="text" name="password" class="easyui-validatebox" required="true">
			</div>
			<div>
				<label>Age:</label>
				<input type="text" name="age">
			</div>
			<div>
				<label>Remark:</label>
				<input type="text" name="remark">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">Save</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>

	<script type="text/javascript">
		function newUser() {
			$('#dlg').dialog('open').dialog('setTitle', 'New User');
			$('#fm').form('clear');
		}
		
		function saveUser(){
			$('#fm').form('submit',{
				url: '${ctx}/system/user/add.do',
				onSubmit: function(){
					return $(this).form('validate');
				},
				success: function(data){
					var result = eval('('+data+')');
					if (result.CODE=='200') {
						$('#dlg').dialog('close');		// close the dialog
						$('#dg').datagrid('reload');	// reload the user data
					} else {
						$.messager.show({
							title: 'Error',
							msg: result.CODE
						});
					}
				}
			});
		}
	</script>
	-->
</body>
</html>