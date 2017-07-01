

$(document).ready(function(){
	initTable("menuList", getMenuTableColumns(), []);
	searchData();

});

    //检索
	function searchData(){
		var param = {menu_pid:$("#menu_pid").val(),menu_name:$("#menu_name").val()};
  	$.ajax({
  		url : path + "/system/getMenuList",
  		data : param,
  		type : "POST",
  		dataType : "json",
  		success : function(result) {
  			var list = result.obj;
  			var dataJosn = $.parseJSON(JSON.stringify(list));
  			$("#menuListTable").bootstrapTable("load",dataJosn);
  		}
  	});
		
	}

//初始化列表
function getMenuTableColumns(){
    var columns = [{
        field: "menu_pid",
        title: "上级菜单ID",
        width: '10%',
        halign:"center",
        align: "left"
    },{
        field: "menu_pname",
        title: "上级菜单名称",
        width: '10%',
        halign:"center",
        align: "left"
    },{
        field: "menu_id",
        title: "菜单ID",
        width: '10%',
        halign:"center",
        align: "left"
    },{
        field: "menu_name",
        title: "菜单名称",
        width: '10%',
        halign:"center",
        align: "left"
    },{
        field: "menu_url",
        title: "菜单路径",
        width: '30%',
        halign:"center",
        align: "left"
    },{
        field: "menu_icon",
        title: "菜单图标",
        width: '10%',
        halign:"center",
        align: "left"
    },{
            field: "operate",
            title: "操作",
            width: '20%',
            formatter: function(value,row,index){
                return '<a href="javascript:void(0);" onclick=\'editMenu("' + row.menu_id+'","'+row.menu_name+'","'+row.menu_url+'","'+row.menu_pid+'","'+row.menu_icon+'","'+row.show_house_type+ '")\' class="btn mini blue">'+
                '<i class="icon-edit"></i> 修改</a> &nbsp;&nbsp;&nbsp;&nbsp;'+
                '<a href="javascript:void(0);" onclick=\'delMenu("' + row.menu_id+ '")\' class="btn mini black">'+
                '<i class="icon-trash"></i> 删除</a>';
    			
            }
        }];
    return columns;
}

/****弹出新增菜单窗口*****/
function openMenuWin(){
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
		str += '<div class="span4">';
			str += '<span_customer >上级菜单</span_customer >' ;
			str += '<select id="menu_pid1" name="menu_pid1" style="width: 157px;">';
			str += '</select>';
		str += '</div>';
			str += '<div class="span4" >';
			str += '<span_customer >菜单ID</span_customer >';
			str += '<input type="text" style="margin-top:-3px;width: 143px;" name="menu_id" id="menu_id"/>';
			str += '</div>';
			str += '<div class="span4">';
			str += '<span_customer >菜单名称</span_customer >';
			str += '<input type="text" style="margin-top:-3px;width: 143px;" name="menu_name1" id="menu_name1"/>';
		str += '</div>';	
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span4" >';
	str += '<span_customer >菜单路径</span_customer >';
	str += '<input type="text" style="margin-top:-3px;width: 143px;" name="menu_url" id="menu_url"/>';
	str += '</div>';
	str += '<div class="span4">';
	str += '<span_customer >菜单图标</span_customer >';
	str += '<input type="text" style="margin-top:-3px;width: 143px;" name="menu_icon" id="menu_icon"/>' ;
    str += '</div>';
    str += '<div class="span4">';
	str += '<span_customer2 >栋舍类型包含(</span_customer2 >';
	str += '<input type="checkbox" name="show_house_type" id="show_house_type" value="1" checked="checked" style="margin-top: -2px;" />&nbsp;&nbsp;育成&nbsp;&nbsp;&nbsp;';
	str += '<input type="checkbox" name="show_house_type" id="show_house_type" value="2" style="margin-top: -2px;" />&nbsp;&nbsp;蛋鸡&nbsp;&nbsp;&nbsp;';
	str += '<input type="checkbox" name="show_house_type" id="show_house_type" value="3" style="margin-top: -2px;" />&nbsp;&nbsp;肉鸡';
	str += '<span_customer2 >)</span_customer2 >';
    str += '</div>';
	str += '</div>';
	str += '</div>';
layer.open({
	  type: 1,
	  skin: 'layui-layer-lan', //加上边框
	  area: ['820px', '190px'], //宽高
	  title: "新增",
	  content: str,
	  btn: ['确定','取消'],
	  yes: function(index){
//		if(submitForm()){ 
		var param;
		var arr = ""; 
		var items = document.getElementsByName("show_house_type"); 
		for (var i = 0; i < items.length; i++) {                    
	        if (items[i].checked) {                        
	            arr = arr +items[i].value; 
	            if(i+1<items.length && items[i+1].checked){
		        	arr = arr + "|";
		        }
	        }  
	    }       
		param ={menu_pid:$("#menu_pid1").val(),
				menu_id:$("#menu_id").val(),
				menu_name:$("#menu_name1").val(),
				menu_url:$("#menu_url").val(),
				menu_icon:$("#menu_icon").val(),
				show_house_type:arr};
		$.ajax({
			url : path + "/system/addMenu",
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
getMenuPidList(0);
}

/****弹出修改编码窗口*****/
function editMenu(menuId,menuName,menuUrl,menuPid,menuIcon,showHouseType){
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
		str += '<div class="span4">';
			str += '<span_customer >上级菜单</span_customer >' ;
			str += '<select id="menu_pid1" name="menu_pid1" style="width: 157px;" disabled="disabled">';
			str += '</select>';
		str += '</div>';
			str += '<div class="span4" >';
			str += '<span_customer >菜单ID</span_customer >';
			str += '<input type="text" style="margin-top:-3px;width: 143px;" name="menu_id" id="menu_id" disabled="disabled"/>';
			str += '</div>';
			str += '<div class="span4">';
			str += '<span_customer >菜单名称</span_customer >';
			str += '<input type="text" style="margin-top:-3px;width: 143px;" name="menu_name2" id="menu_name2"/>';
		str += '</div>';	
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span4" >';
	str += '<span_customer >菜单路径</span_customer >';
	str += '<input type="text" style="margin-top:-3px;width: 143px;" name="menu_url" id="menu_url"/>';
	str += '</div>';
	str += '<div class="span4">';
	str += '<span_customer >菜单图标</span_customer >';
	str += '<input type="text" style="margin-top:-3px;width: 143px;" name="menu_icon" id="menu_icon"/>' ;
    str += '</div>';
    str += '<div class="span4">';
	str += '<span_customer2 >栋舍类型包含(</span_customer2 >';
	str += '<input type="checkbox" name="show_house_type" id="show_house_type1" value="1" checked="checked" style="margin-top: -2px;" />&nbsp;&nbsp;育成&nbsp;&nbsp;&nbsp;';
	str += '<input type="checkbox" name="show_house_type" id="show_house_type2" value="2" style="margin-top: -2px;" />&nbsp;&nbsp;蛋鸡&nbsp;&nbsp;&nbsp;';
	str += '<input type="checkbox" name="show_house_type" id="show_house_type3" value="3" style="margin-top: -2px;" />&nbsp;&nbsp;肉鸡';
	str += '<span_customer2 >)</span_customer2 >';
    str += '</div>';
	str += '</div>';
	str += '</div>';
layer.open({
	  type: 1,
	  skin: 'layui-layer-lan', //加上边框
	  area: ['820px', '190px'], //宽高
	  title: "修改",
	  content: str,
	  btn: ['确定','取消'],
	  yes: function(index){
//		if(submitForm()){ 
		var param;
		var arr = ""; 
		var items = document.getElementsByName("show_house_type"); 
		for (var i = 0; i < items.length; i++) {                    
	        if (items[i].checked) {                        
	            arr = arr +items[i].value;  
	            if(i+1<items.length && items[i+1].checked){
		        	arr = arr + "|";
		        }
	        }  
	    }       
		param ={menu_pid:$("#menu_pid1").val(),
				menu_id:$("#menu_id").val(),
				menu_name:$("#menu_name2").val(),
				menu_url:$("#menu_url").val(),
				menu_icon:$("#menu_icon").val(),
				show_house_type:arr};
		$.ajax({
			url : path + "/system/editMenu",
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
getMenuPidList(menuPid);
//document.getElementById("menu_pid1").value = menuPid;
var houseTypeList =showHouseType.split("|");
if(houseTypeList.length !=0 && houseTypeList[0] !="*" && houseTypeList != null && houseTypeList != ""){
	for(var i=0;i<houseTypeList.length;i++){
		document.getElementById("show_house_type"+houseTypeList[i]).checked = "checked";
	}
}
document.getElementById("menu_id").value = menuId;
document.getElementById("menu_name2").value = menuName;
document.getElementById("menu_url").value = menuUrl;
document.getElementById("menu_icon").value = menuIcon;
}

function delMenu(menuId){
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
            url: path+"/system/delMenu",
            data: {menu_id:menuId},
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

function getMenuPidList(menuPid){
	$.ajax({
        // async: true,
        url: path+"/system/getMenuPidList",
        data: {},
        type : "POST",
        dataType: "json",
        // timeout:50000,
        success: function(result) {  
        	var list = result.obj;
			$("menu_pid1 option").remove();
//			$("#good_id").append('<option value=""></option>');
			for (var i = 0; i < list.length; i++) {
				$("#menu_pid1").append('<option value="' + list[i].menu_id + '">' + list[i].menu_name + '</option>');
			}
			if(menuPid !=0){
				document.getElementById("menu_pid1").value = menuPid;
			}
        }
    });
}




