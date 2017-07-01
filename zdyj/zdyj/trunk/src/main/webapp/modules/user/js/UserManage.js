// /**
//  *
//  */
var userListTableName = "userList";

$(document).ready(function(){
	// queryUser();
    initTableUserList();
    getTableDataUserList();

});

//初始化用户列表
function initTableUserList(){
    var columns = [{
        field: "user_code",
        title: "用户登录名",
        width: '15%'
    }, {
        field: "user_real_name",
        title: "用户名称",
        width: '40%'
    },
        {
            field: "user_mobile_1",
            title: "手机号码",
            vwidth: '20%'
        }, {
            field: "user_status_desc",
            title: "用户状态",
            width: '10%'
        }, {
            field: "user_status",
            title: "用户状态",
            visible: false
        }, {
            field: "id",
            title: "用户ID",
            visible: false
        },{
            field: "operate",
            title: "操作",
            width: '20%'
        }];
    initTable(userListTableName, columns, []);
}

//获取用户列表数据
function getTableDataUserList(){
	loadTableData(userListTableName,userList);
}

// function queryUser(){
// 	$('#user_table').datagrid({
// 	   	url:'biz/ps/queryUser.do'
// 	});
// }
//
// function addUser(){
// 	$('#addUser').show();
// 	$('#addUser').window({
// 		title:'新增用户',
// 	    width:600,
// 	    modal:true,
// 	    minimizable:false
// 	});
// }
//
// function addUser_submit(){
// 	$.ajax({
// 		cache: true,
// 		type: "POST",
// 		url: "biz/ps/addUser.do",
// 		data:$('#add_form').serialize(),// 你的formid
// 		async: false,
// 	    error: function(request) {
// 	        alert("Connection error");
// 	    },
// 	    success: function(data) {
// 	    	$.messager.alert('提示','用户新增成功','info',function () {
// 	    		$('#addUser').window('close');
// 	    		$('#addUser').form('clear');
// 	    		$('#user_table').datagrid('load');
// 	        });
// 	    }
// 	});
// }
// function deleUser_submit(user_id){
// 	$.ajax({
// 		cache: true,
// 		type: "POST",
// 		url: "biz/ps/deleUser.do",
// 		data:{"user_id":user_id},
// 		async: false,
// 	    error: function(request) {
// 	        alert("Connection error");
// 	    },
// 	    success: function(data) {
// 	    	$.messager.alert('提示','用户删除成功','info',function () {
// 	    		$('#user_table').datagrid('load');
// 	        });
// 	    }
// 	});
// }
// function deleUser(){
// 	var row = $("#user_table").datagrid("getSelected");
// 	if(row == null){
// 		$.messager.alert('提示','请选中需要删除的用户','info');
// 	}else{
// 		 $.messager.confirm("操作提示", "您确定要删除"+row.UserName+"用户吗？", function (data) {
//             if (data) {
//             	deleUser_submit(row.id);
//             }
//         });
// 	}
// }
//
// function updateUser(){
// 	var row = $("#user_table").datagrid("getSelected");
// 	if(row == null){
// 		$.messager.alert('提示','请选中需要修改的用户','info');
// 	}else{
// 		var user_id=row.id;
// 		UpdateUser_submit(user_id);
// 	}
// }
//
// function updateUser1(){
// 	$('#addUser').show();
// 	$('#addUser').window({
// 		title:'修改用户',
// 	    width:600,
// 	    modal:true,
// 	    minimizable:false
// 	});
// 	$("#addsubmit").attr("onclick","UpdateUser_submit()");
//
// }
//
// function UpdateUser_submit(){
// 	$.ajax({
// 		cache: true,
// 		type: "POST",
// 		url: "biz/ps/UpdateUser.do",
// 		data:$('#add_form').serialize(),// 你的formid
// 		async: false,
// 	    error: function(request) {
// 	    	$.messager.alert('提示','Connection error');
// 	    },
// 	    success: function(data) {
// 	    	if(data == 'SUCCESS'){
// 	    		$.messager.alert('提示','用户修改成功','info',function () {
// 		    		$('#addUser').window('close');
// 		    		$('#addUser').form('clear');
// 		    		$('#user_table').datagrid('load');
// 		        });
// 	    	}else{
// 	    		$.messager.alert('提示','用户修改失败');
// 	    	}
// 	    }
// 	});
// }
//
// function after_operate_succ(succ_message){
// 	$.messager.alert('提示',succ_message,'info',function () {
// 		$('#addUser').window('close');
// 		$('#addUser').form('clear');
// 		$('#user_table').datagrid('load');
//     });
// }



