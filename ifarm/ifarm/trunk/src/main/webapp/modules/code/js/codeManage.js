// /**
//  *
//  */
var userListTableName = "userList";

$(document).ready(function(){
	initTable("codeList", getCodeTableColumns(), []);
	searchData();

});

    //检索
	function searchData(){
		var param = {code_type:$("#code_type").val(),code_name:$("#code_name").val()};
  	$.ajax({
  		url : path + "/system/getCodeList",
  		data : param,
  		type : "POST",
  		dataType : "json",
  		success : function(result) {
  			var list = result.obj;
  			var dataJosn = $.parseJSON(JSON.stringify(list));
  			$("#codeListTable").bootstrapTable("load",dataJosn);
  		}
  	});
		
	}

//初始化列表
function getCodeTableColumns(){
    var columns = [{
        field: "code_type",
        title: "编码类型",
        width: '10%',
        halign:"center",
        align: "left"
    },{
        field: "biz_code",
        title: "编码",
        width: '10%',
        halign:"center",
        align: "left"
    },{
        field: "code_name",
        title: "编码名称",
        width: '10%',
        halign:"center",
        align: "left"
    },{
        field: "bak1",
        title: "备注1",
        width: '15%',
        halign:"center",
        align: "left"
    },{
        field: "bak2",
        title: "备注2",
        width: '15%',
        halign:"center",
        align: "left"
    },{
            field: "code_desc",
            title: "编码描述",
            width: '20%',
            halign:"center",
            align: "left"
        },{
            field: "operate",
            title: "操作",
            width: '20%',
            formatter: function(value,row,index){
                return '<a href="javascript:void(0);" onclick=\'editCode("' + row.code_type+'","'+row.biz_code+'","'+row.code_name+'","'+row.bak1+'","'+row.bak2+'","'+row.code_desc + '")\' class="btn mini blue">'+
                '<i class="icon-edit"></i> 修改</a> &nbsp;&nbsp;&nbsp;&nbsp;';
//                '<a href="javascript:void(0);" onclick=\'delCode("' + row.code_type+'","'+row.biz_code + '")\' class="btn mini black">'+
//                '<i class="icon-trash"></i> 删除</a>';
    			
            }
        }];
    return columns;
}

/****弹出新增编码窗口*****/
function openCodeWin(){
	if (isRead == 0) {
		layer.alert('无权限，请联系管理员!', {
			skin : 'layui-layer-lan',
			closeBtn : 0,
			shift : 4
		// 动画类型
		});
		return;
	}
	
var str = '<br><div class="container-fluid" >';
	str += '<div class="row-fluid">';
		str += '<div class="span3">';
			str += '<span_customer >编码类型</span_customer >' ;
			str += '<input type="text" style="margin-top:-3px;width: 135px;" name="code_type1" id="code_type1"/>' ;
		str += '</div>';
			str += '<div class="span3" >';
			str += '<span_customer >编码</span_customer >';
			str += '<input type="text" style="margin-top:-3px;width: 143px;" name="biz_code" id="biz_code"/>';
			str += '</div>';
			str += '<div class="span3">';
			str += '<span_customer >编码名称</span_customer >';
			str += '<input type="text" style="margin-top:-3px;width: 143px;" name="code_name1" id="code_name1"/>';
		str += '</div>';	
		str += '<div class="span3">';
		str += '<span_customer >备注1</span_customer >';
		str += '<input type="text" style="margin-top:-3px;width: 143px;" name="bak1" id="bak1"/>';
	str += '</div>';	
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span3" >';
	str += '<span_customer >备注2</span_customer >';
	str += '<input type="text" style="margin-top:-3px;width: 143px;" name="bak2" id="bak2"/>';
	str += '</div>';
	str += '<div class="span6">';
	str += '<span_customer >编码描述</span_customer >';
	str += '<input type="text" style="margin-top:-3px;width: 391px;" name="code_desc" id="code_desc"/>' ;
str += '</div>';
	str += '</div>';
	str += '</div>';
layer.open({
	  type: 1,
	  skin: 'layui-layer-lan', //加上边框
	  area: ['1010px', '200px'], //宽高
	  title: "新增",
	  content: str,
	  btn: ['确定','取消'],
	  yes: function(index){
//		if(submitForm()){ 
		var param;
		param ={code_type:$("input[name='code_type1']").val(),
				biz_code:$("#biz_code").val(),
				code_name:$("#code_name1").val(),
				bak1:$("#bak1").val(),
				bak2:$("#bak2").val(),
				code_desc:$("#code_desc").val()};
		$.ajax({
			url : path + "/system/addCode",
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
//				var obj = result.obj;
//				var dataJosn = $.parseJSON(JSON.stringify(obj));
//                $("#codeListTable").bootstrapTable('load',dataJosn);
				if(result.success == true) {
					layer.close(index); 
					layer.msg(result.msg);
					return;
				}else{
					layer.msg(result.msg);
					return;
				}
			}
		});
//	  }
	  }
	});
}

/****弹出修改编码窗口*****/
function editCode(codeType,bizCode,codeName,bak1,bak2,codeDesc){
	if (isRead == 0) {
		layer.alert('无权限，请联系管理员!', {
			skin : 'layui-layer-lan',
			closeBtn : 0,
			shift : 4
		// 动画类型
		});
		return;
	}
	
var str = '<br><div class="container-fluid" >';
	str += '<div class="row-fluid">';
		str += '<div class="span3">';
			str += '<span_customer >编码类型</span_customer >' ;
			str += '<input type="text" style="margin-top:-3px;width: 135px;" name="code_type2" id="code_type2" disabled="disabled"/>' ;
		str += '</div>';
			str += '<div class="span3" >';
			str += '<span_customer >编码</span_customer >';
			str += '<input type="text" style="margin-top:-3px;width: 143px;" name="biz_code" id="biz_code" disabled="disabled"/>';
			str += '</div>';
			str += '<div class="span3">';
			str += '<span_customer >编码名称</span_customer >';
			str += '<input type="text" style="margin-top:-3px;width: 143px;" name="code_name2" id="code_name2"/>';
		str += '</div>';	
		str += '<div class="span3">';
		str += '<span_customer >备注1</span_customer >';
		str += '<input type="text" style="margin-top:-3px;width: 143px;" name="bak1" id="bak1"/>';
	str += '</div>';	
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span3" >';
	str += '<span_customer >备注2</span_customer >';
	str += '<input type="text" style="margin-top:-3px;width: 143px;" name="bak2" id="bak2"/>';
	str += '</div>';
	str += '<div class="span6">';
	str += '<span_customer >编码描述</span_customer >';
	str += '<input type="text" style="margin-top:-3px;width: 391px;" name="code_desc" id="code_desc"/>' ;
str += '</div>';
	str += '</div>';
	str += '</div>';
layer.open({
	  type: 1,
	  skin: 'layui-layer-lan', //加上边框
	  area: ['1010px', '200px'], //宽高
	  title: "修改",
	  content: str,
	  btn: ['确定','取消'],
	  yes: function(index){
//		if(submitForm()){ 
		var param;
		param ={code_type:$("input[name='code_type2']").val(),
				biz_code:$("#biz_code").val(),
				code_name:$("#code_name2").val(),
				bak1:$("#bak1").val(),
				bak2:$("#bak2").val(),
				code_desc:$("#code_desc").val()};
		$.ajax({
			url : path + "/system/editCode",
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
//				var obj = result.obj;
//				var dataJosn = $.parseJSON(JSON.stringify(obj));
//                $("#codeListTable").bootstrapTable('load',dataJosn);
				searchData();
				if(result.success == true) {
					layer.close(index); 
					layer.msg(result.msg);
					return;
				}else{
					layer.msg(result.msg);
					return;
				}
			}
		});
//	  }
	  }
	});
document.getElementById("code_type2").value = codeType;
document.getElementById("biz_code").value = bizCode;
document.getElementById("code_name2").value = codeName;
document.getElementById("bak1").value = bak1;
document.getElementById("bak2").value = bak2;
document.getElementById("code_desc").value = codeDesc;
}

function delCode(codeType,bizCode){
	if (isRead == 0) {
		layer.alert('无权限，请联系管理员!', {
			skin : 'layui-layer-lan',
			closeBtn : 0,
			shift : 4
		// 动画类型
		});
		return;
	}
    layer.confirm('是否确认删除？', {
        skin: 'layui-layer-lan'
        , closeBtn: 0
        , shift: 4 //动画类型
    }, function ok() {
    	$.ajax({
            // async: true,
            url: path+"/system/delCode",
            data: {code_type:codeType,biz_code:bizCode},
            type : "POST",
            dataType: "json",
            cache: false,
            // timeout:50000,
            success: function(result) {     
//                    var obj = result.obj;
//                    if(null != obj) {
//                        var dataJosn = $.parseJSON(JSON.stringify(obj));
//                        $("#settleSub2Table").bootstrapTable('load',dataJosn);
//                    } 
            	searchData();
				if(result.success == true) {
					layer.close(); 
					layer.msg(result.msg);
					return;
				}else{
					layer.msg(result.msg);
					return;
				}
            }
        });
    });
}




