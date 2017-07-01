/**
 * Created by Yoven on 15/4/2017.
 */

$(function () {
    'use strict';
    var url = path + "/batch/upload?upload_file_type=1";
    $('#fileupload').fileupload({
        url: url,
        autoUpload: true,
        add: function (e, data){
            if(data.originalFiles.length>0){
            }
            data.submit();
        },
        done: function (e, data) {
            var rt = '(' + data.result + ')';
            var json = eval(rt);
            reFlushData(currTabName);
            if(json.success == true){
            	layer.msg("文件导入成功");
            } else {
            	if(json.obj.error_type==0){
            		layer.msg("无数据");
            	}else if(json.obj.error_type==1){
            		layer.msg("系统配置错误");
            	}else if(json.obj.error_type==2){
            		layer.msg("Excel标签页错误");
            	}else if(json.obj.error_type==3){
            		layer.msg("开始行或列错误或列名为空");
            	}else if(json.obj.error_type==4){
            		layer.msg("列头错误");
            	}else if(json.obj.error_type==5){
            		layer.msg("列数错误");
            	}else if(json.obj.error_type==6){
            		layer.msg("单元格类型错误");
            	}else if(json.obj.error_type==7){
            		layer.msg("数据保存错误");
            	}
            	
            }
            return;
        },
        fail:function (e, data) {
        	layer.msg(e);
            return;
        }
    });
    
    $('#fileupload1').fileupload({
        url: path + "/batch/upload?upload_file_type=2",
        autoUpload: true,
        add: function (e, data){
            if(data.originalFiles.length>0){
            }
            data.submit();
        },
        done: function (e, data) {
            var rt = '(' + data.result + ')';
            var json = eval(rt);
            reFlushData(currTabName);
            if(json.success == true){
            	layer.msg("文件导入成功");
            } else {
            	if(json.obj.error_type==0){
            		layer.msg("无数据");
            	}else if(json.obj.error_type==1){
            		layer.msg("系统配置错误");
            	}else if(json.obj.error_type==2){
            		layer.msg("Excel标签页错误");
            	}else if(json.obj.error_type==3){
            		layer.msg("开始行或列错误或列名为空");
            	}else if(json.obj.error_type==4){
            		layer.msg("列头错误");
            	}else if(json.obj.error_type==5){
            		layer.msg("列数错误");
            	}else if(json.obj.error_type==6){
            		layer.msg("单元格类型错误");
            	}else if(json.obj.error_type==7){
            		layer.msg("数据保存错误");
            	}
            	
            }
            return;
        },
        fail:function (e, data) {
        	layer.msg(e);
            return;
        }
    });
    
    $('#fileupload2').fileupload({
        url: path + "/batch/upload?upload_file_type=2",
        autoUpload: true,
        add: function (e, data){
            if(data.originalFiles.length>0){
            }
            data.submit();
        },
        done: function (e, data) {
            var rt = '(' + data.result + ')';
            var json = eval(rt);
            reFlushData(currTabName);
            if(json.success == true){
            	layer.msg("文件导入成功");
            } else {
            	if(json.obj.error_type==0){
            		layer.msg("无数据");
            	}else if(json.obj.error_type==1){
            		layer.msg("系统配置错误");
            	}else if(json.obj.error_type==2){
            		layer.msg("Excel标签页错误");
            	}else if(json.obj.error_type==3){
            		layer.msg("开始行或列错误或列名为空");
            	}else if(json.obj.error_type==4){
            		layer.msg("列头错误");
            	}else if(json.obj.error_type==5){
            		layer.msg("列数错误");
            	}else if(json.obj.error_type==6){
            		layer.msg("单元格类型错误");
            	}else if(json.obj.error_type==7){
            		layer.msg("数据保存错误");
            	}
            	
            }
            return;
        },
        fail:function (e, data) {
        	layer.msg(e);
            return;
        }
    });
    
});

//创建发雏表格
function faChuColumns(){
    var dataColumns = [{field: "operation_date",
                            title: "日期",
                            width: '10%'
                        }, {
                            field: "farm_name",
                            title: "农场",
                            width: '10%'
                        }, {
                            field: "child_batch_no",
                            title: "养殖批次",
                            width: '5%'
                        }, {
                            field: "variety",
                            title: "品种编号",
                            width: '5%'
                        }, {
                            field: "corporation",
                            title: "雏鸡来源",
                            width: '5%'
                        },  {
                            field: "src_batch_no",
                            title: "雏源批号",
                            width: '10%'
                        }, {
                            field: "send_female_num",
                            title: "发雏数量",
                            width: '5%'
                        },  {
                            field: "check_female_num",
                            title: "核对数量",
                            width: '15%'
                        }, {
                            field: "check_date",
                            title: "核对日期",
                            width: '5%'
                        }, {field: "opr",
                            title: "操作",
                            width: "15%",
                    		formatter: function(value,row,index){
                    			if(row.is_enable ==1){
                    			return "&nbsp;<button id='factToolbar_btn_update' type='button' class='btn blue' style='display: inline;' " +
                    					"onclick='updateFaChu(\""+row.operation_date+"\","+row.farm_id+",\""+row.child_batch_no+"\","+row.variety_id+","+row.corporation_id+
                    					",\""+row.src_batch_no+"\","+row.send_female_num+",\""+row.bak+"\");' >"+
                    			"<i class='icon-edit'></i>修改</button>";
//                                "&nbsp;<button id='factToolbar_btn_delete' type='button' class='btn blue' style='display: inline;' onclick='delFaChu("+row.farm_id+",\""+row.child_batch_no+"\");'>"+
//                    			"<i class='icon-trash'></i>删除</button>";
                    			}else{
                    				return "&nbsp;<button id='factToolbar_btn_update' type='button' class='btn blue' style='display: inline;' " +
                					"onclick='updateFaChu(\""+row.operation_date+"\","+row.farm_id+",\""+row.child_batch_no+"\","+row.variety_id+","+row.corporation_id+
                					",\""+row.src_batch_no+"\","+row.send_female_num+",\""+row.bak+"\");' >"+
                			"<i class='icon-edit'></i>修改</button>";
//                            "&nbsp;<button id='factToolbar_btn_delete' type='button' class='btn blue' disabled='disabled' style='display: inline;' onclick='delFaChu("+row.farm_id+",\""+row.child_batch_no+"\");'>"+
//                			"<i class='icon-trash'></i>删除</button>";
                    			}
                            }
                        }];
    return dataColumns;
};

//显示品种下拉框
function showVariety2(varietyList){
    document.getElementById('variety_id').options.length = 0;
    for(var key in varietyList){
        document.getElementById('variety_id').add(new Option(varietyList[key].variety,varietyList[key].variety_id));
    }
    if(objBatch.fc_variety_id !=0){
    document.getElementById("variety_id").value = objBatch.fc_variety_id;
    }
    getCorporation2();//showCorporation(tabName, getCorporation(tabName)); //显示来源下拉框
};

var good_type2 =0;
//获取品种id与名称
function getVariety2(type){
    var rt = new Array();
    good_type2 = type;
    $.ajax({
        type: "post",
        url: path + "/googs/getGoods",
        data: {
            good_type: type
        },
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            var rt = new Array();
            for(var key in dataList){
                var tmp ={variety_id:dataList[key].good_id, variety: dataList[key].good_name};
                rt.push(tmp);
            }
            showVariety2(rt);
        }
    });
    return rt;
};

//品种选择值变化事件
function changeGood2Select(){
	getCorporation2();//showCorporation(currTabName, getCorporation(currTabName));
};

//显示来源下拉框
function showCorporation2(corporationList){
    document.getElementById('corporation_id').options.length = 0;
    for(var key in corporationList){
        document.getElementById('corporation_id').add(new Option(corporationList[key].corporation,corporationList[key].corporation_id));
    }
    if(objBatch.fc_corporation_id !=0){
        document.getElementById("corporation_id").value = objBatch.fc_corporation_id;
        }
    if(good_type2 !=1){
    	getCorporationGood();
    }
};

//获取来源id与名称
function getCorporation2() {
    var rt = new Array();
    if (document.getElementById('variety_id').options.length > 0) {
        $.ajax({
            type: "post",
            url: path + "/googs/getCorporation",
            data: {
                good_type: good_type2,
                good_id : $("#variety_id").val()
            },
            dataType: "json",
            success: function (result) {
                dataList = eval(result.obj);
                var rt = new Array();
                for (var key in dataList) {
                    var tmp = {corporation_id: dataList[key].corporation_id, corporation: dataList[key].corporation};
                    rt.push(tmp);
                }
                showCorporation2(rt);
            }
        });
        return rt;
    }
};

/****弹出新增发雏窗口*****/
function openFaChuWin(){
	if (isRead != 2) {
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
		str += '<div class="span1">';
			str += '<span_customer2 >日期</span_customer2 >' ;
		str += '</div>';
			str += '<div class="span3" align="left">';
				str += '<div class="input-append date faChuDatePicker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">'+
					'<input type="text" name="operation_date" id="operation_date" style="width: 158px;height: 24px;" readonly>'+
				'<span class="add-on"><i class="icon-calendar"></i></span></div>';
			str += '</div>';
			str += '<div class="span1">';
			str += '<span_customer2 >农场</span_customer2 >';
		    str += '</div>';
			str += '<div class="span3" align="left">';
			str += '<select id="farm_id" name="farm_id" style="width: 200px;">';
			str += '</select>';
			str += '</div>';	
			str += '<div class="span1">';
			str += '<span_customer2 >养殖批次</span_customer2 >';
		    str += '</div>';
			str += '<div class="span3" align="left">';
			str += '<input type="text" style="margin-top:-3px;width: 186px;" name="child_batch_no" id="child_batch_no"/>' ;
			str += '</div>';
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span1">';
	str += '<span_customer2 >品种编号</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<select id="variety_id" name="variety_id" style="width: 200px;" onchange= "changeGood2Select();">';
	str += '</select>';
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >雏鸡来源</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<select id="corporation_id" name="corporation_id" style="width: 200px;">';
	str += '</select>';
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >雏源批号</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<input type="text" style="margin-top:-3px;width: 186px;" name="src_batch_no" id="src_batch_no"/>' ;
	str += '</div>';
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span1">';
	str += '<span_customer2 >发雏数量</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
		str += '<input type="text" style="margin-top:-3px;width: 186px;" name="send_female_num" id="send_female_num"/>' ;
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >备注</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
		str += '<input type="text" style="margin-top:-3px;width: 474px;" name="bak" id="bak"/>' ;
	str += '</div>';
	str += '</div>';
//	str+='<div class="span2"><label style="padding-left: 70px;color: red; width:450px; text-align: center;margin-top: 15px;" id="add_msg"></label></div>';
	str += '</div>';
layer.open({
	  type: 1,
	  skin: 'layui-layer-lan', //加上边框
	  area: ['885px', '235px'], //宽高
	  content: str,
	  btn: ['确定','取消'],
	  yes: function(index){
		if(submitForm()){ 
		var param;
		param ={operation_date:$("input[name='operation_date']").val(),
				farm_id:$("#farm_id").val(),
				child_batch_no:$("#child_batch_no").val(),
				variety_id:$("#variety_id").val(),
				variety:document.getElementById("variety_id").options[document.getElementById("variety_id").selectedIndex].text,
				corporation_id:$("#corporation_id").val(),
				corporation:document.getElementById("corporation_id").options[document.getElementById("corporation_id").selectedIndex].text,
				src_batch_no:$("#src_batch_no").val(),
				send_female_num:$("#send_female_num").val(),
				bak:$("#bak").val()};
		$.ajax({
			url : path + "/batch/addFaChu",
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
				var obj = result.obj;
				var dataJosn = $.parseJSON(JSON.stringify(obj));
                $("#faChuTable").bootstrapTable('load',dataJosn);
				if(result.success==true) {
					layer.close(index); 
					layer.msg(result.msg);
					return;
				}else{
					layer.msg(result.msg);
					return;
				}
			}
		});
	  }
	  }
	});
initDatepicker(currTabName); //初始化日期控件
getFarmList();
getVariety2(1);
}

//获取农场id与名称
function getFarmList(){
    $.ajax({
        type: "post",
        url: path + "/batch/getFarm2",
        data: {},
        dataType: "json",
        success: function (result) {
        	var list = result.obj;
			$("#farm_id option").remove();
			for (var i = 0; i < list.length; i++) {
				$("#farm_id").append("<option value=" + list[i].org_code + ">" + list[i].org_name + "</option>");
			}
			if(objBatch.fc_farm_id !=0){
			    document.getElementById("farm_id").value = objBatch.fc_farm_id;
			    }
        }
    });
};

//修改发雏信息
function updateFaChu(operation_date,farm_id,child_batch_no,variety_id1,corporation_id1,src_batch_no,send_female_num,bak){
	if(isRead==0){
		layer.alert('无权限，请联系管理员!', {
		    skin: 'layui-layer-lan'
		    ,closeBtn: 0
		    ,shift: 4 //动画类型
		  });
		return;
	}
    
	var str = '<br><div class="container-fluid" >';
	str += '<div class="row-fluid">';
		str += '<div class="span1">';
			str += '<span_customer2 >日期</span_customer2 >' ;
		str += '</div>';
			str += '<div class="span3" align="left">';
				str += '<div class="input-append date faChuDatePicker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">'+
					'<input type="text" name="operation_date" id="operation_date" style="width: 158px;height:24px;" readonly>'+
				'<span class="add-on"><i class="icon-calendar"></i></span></div>';
			str += '</div>';
			str += '<div class="span1">';
			str += '<span_customer2 >农场</span_customer2 >';
		    str += '</div>';
			str += '<div class="span3" align="left">';
			str += '<select id="farm_id" name="farm_id" style="width: 200px;" disabled="disabled">';
			str += '</select>';
			str += '</div>';	
			str += '<div class="span1">';
			str += '<span_customer2 >养殖批次</span_customer2 >';
		    str += '</div>';
			str += '<div class="span3" align="left">';
			str += '<input type="text" style="margin-top:-3px;width: 186px;" name="child_batch_no" id="child_batch_no" disabled="disabled"/>' ;
			str += '</div>';
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span1">';
	str += '<span_customer2 >品种编号</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<select id="variety_id" name="variety_id" style="width: 200px;" onchange= "changeGood2Select();">';
	str += '</select>';
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >雏鸡来源</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<select id="corporation_id" name="corporation_id" style="width: 200px;">';
	str += '</select>';
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >雏源批号</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<input type="text" style="margin-top:-3px;width: 186px;" name="src_batch_no" id="src_batch_no"/>' ;
	str += '</div>';
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span1">';
	str += '<span_customer2 >发雏数量</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
		str += '<input type="text" style="margin-top:-3px;width: 186px;" name="send_female_num" id="send_female_num"/>' ;
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >备注</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
		str += '<input type="text" style="margin-top:-3px;width: 474px;" name="bak" id="bak"/>' ;
	str += '</div>';
	str += '</div>';
//	str+='<div class="span2"><label style="padding-left: 70px;color: red; width:450px; text-align: center;margin-top: 15px;" id="add_msg"></label></div>';
	str += '</div>';
layer.open({
	  type: 1,
	  skin: 'layui-layer-lan', //加上边框
	  area: ['885px', '235px'], //宽高
	  content: str,
	  btn: ['确定','取消'],
	  yes: function(index){
		if(submitForm()){ 
		var param;
		param ={operation_date:$("input[name='operation_date']").val(),
				farm_id:$("#farm_id").val(),
				child_batch_no:$("#child_batch_no").val(),
				variety_id:$("#variety_id").val(),
				variety:document.getElementById("variety_id").options[document.getElementById("variety_id").selectedIndex].text,
				corporation_id:$("#corporation_id").val(),
				corporation:document.getElementById("corporation_id").options[document.getElementById("corporation_id").selectedIndex].text,
				src_batch_no:$("#src_batch_no").val(),
				send_female_num:$("#send_female_num").val(),
				bak:$("#bak").val()};
		
		$.ajax({
			url : path + "/batch/updateFaChu",
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
				var obj = result.obj;
				var dataJosn = $.parseJSON(JSON.stringify(obj));
                $("#faChuTable").bootstrapTable('load',dataJosn);
                if(result.success==true) {
					layer.close(index); 
					layer.msg(result.msg);
					return;
				}else{
					layer.msg(result.msg);
					return;
				}
			}
		});
	  }
	  }
	});
objBatch.fc_variety_id = variety_id1;
objBatch.fc_corporation_id = corporation_id1;
objBatch.fc_farm_id = farm_id;
initDatepicker(currTabName); //初始化日期控件
getFarmList();
getVariety2(1);
    document.getElementById("src_batch_no").value = src_batch_no;
    document.getElementById("operation_date").value = operation_date;
    document.getElementById("child_batch_no").value = child_batch_no;
    document.getElementById("send_female_num").value = send_female_num;
    document.getElementById("bak").value = bak;
    
}

//删除
function delFaChu(farm_id,child_batch_no) {
	if(isRead!=2){
        layer.msg("无权限，请联系管理员！", {});
		return;
	}
	//询问框
	layer.confirm('确定要删除该数据吗？', {
        skin: 'layui-layer-lan'
        , closeBtn: 0
		, shift: 4 //动画类型
	    , btn : [ '确定', '取消' ]
	//按钮
	}, function() {
		$.ajax({
			url : path + "/batch/deleteFaChu",
			data : {
				farm_id:farm_id,
				child_batch_no:child_batch_no
			},
			type : "POST",
			dataType : "json",
			success : function(result) {
				var obj = result.obj;
				var dataJosn = $.parseJSON(JSON.stringify(obj));
                $("#faChuTable").bootstrapTable('load',dataJosn);
                if(result.success==true) {
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

//检查变量
function submitForm(){
	var operation_date=$("input[name='operation_date']").val();
	var src_batch_no=$("input[name='src_batch_no']").val();
	if(currTabName == "faChu"){
		var send_female_num=$("input[name='send_female_num']").val();
		var child_batch_no=$("input[name='child_batch_no']").val();
		if(operation_date =="" ){
			layer.msg("日期不能为空！");
			return false;
		}else if(child_batch_no =="" ){
				layer.msg("养殖批次不能为空！");
				return false;
		}else if(src_batch_no =="" ){
			layer.msg("雏源批号不能为空！");
			return false;
		}else if(send_female_num =="" ){
		layer.msg("发雏数量不能为空！");
		return false;
		}else if (parseInt(send_female_num)!=send_female_num){
			layer.msg("发雏数量必须是整数！");
			return false;
		}else if(send_female_num<=0){
			layer.msg("发雏数量必须大于0！");
			return false;
		}	
	}else{
		var send_num=$("input[name='send_num']").val();
		if(operation_date =="" ){
			layer.msg("日期不能为空！");
			return false;
		}else if(src_batch_no =="" ){
			layer.msg("来源批号不能为空！");
			return false;
		}else if(send_num =="" ){
		layer.msg("分发数量不能为空！");
		return false;
		}else if (parseInt(send_num)!=send_num){
			layer.msg("分发数量必须是整数！");
			return false;
		}else if(send_num<=0){
			layer.msg("发分发数量必须大于0！");
			return false;
		}	
	}
	return true;
}


//以下为发料、发药相关----------------------------------增删改

//创建发料、发药表格
function faLiaoColumns(){
    var dataColumns = [{field: "operation_date",
                            title: "日期",
                            width: '10%'
                        }, {
                            field: "farm_name",
                            title: "农场",
                            width: '10%'
                        }, {
                            field: "good_type",
                            title: "类型",
                            width: '5%'
                        }, 
//                        {
//                            field: "send_batch_no",
//                            title: "分发批次",
//                            width: '5%'
//                        }, 
                        {
                            field: "goods_name",
                            title: "品种	",
                            width: '5%'
                        }, {
                            field: "corporation",
                            title: "供应商",
                            width: '10%'
                        }, {
                            field: "factory",
                            title: "厂家",
                            width: '5%'
                        }, {
                            field: "src_batch_no",
                            title: "来源批号",
                            width: '5%'
                        }, {
                            field: "send_num",
                            title: "分发数量",
                            width: '5%'
                        },  {
                            field: "check_num",
                            title: "核对数量",
                            width: '5%'
                        }, {
                            field: "check_date",
                            title: "核对日期",
                            width: '10%'
                        }, {field: "opr",
                            title: "操作",
                            width: "15%",
                    		formatter: function(value,row,index){
                    			if(row.is_enable ==1){
                    			return "&nbsp;<button id='factToolbar_btn_update' type='button' class='btn blue' style='display: inline;' " +
                    					"onclick='updateFaLiao(\""+row.operation_date+"\","+row.farm_id+","+row.send_ID+","+row.good_type+","+row.goods_id+","+row.factory_id+","+row.corporation_id+
                    					",\""+row.src_batch_no+"\","+row.send_num+",\""+row.bak+"\");' >"+
                    			"<i class='icon-edit'></i>修改</button>";
//                                "&nbsp;<button id='factToolbar_btn_delete' type='button' class='btn blue' style='display: inline;' onclick='delFaLiao("+row.farm_id+","+row.good_type+",\""+row.child_batch_no+"\");'>"+
//                    			"<i class='icon-trash'></i>删除</button>";
                    			}else{
                    				return "&nbsp;<button id='factToolbar_btn_update' type='button' class='btn blue' style='display: inline;' " +
                					"onclick='updateFaLiao(\""+row.operation_date+"\","+row.farm_id+","+row.send_ID+","+row.good_type+","+row.goods_id+","+row.factory_id+","+row.corporation_id+
                					",\""+row.src_batch_no+"\","+row.send_num+",\""+row.bak+"\");' >"+
                			"<i class='icon-edit'></i>修改</button>";
//                            "&nbsp;<button id='factToolbar_btn_delete' type='button' class='btn blue' disabled='disabled' style='display: inline;' onclick='delFaLiao("+row.farm_id+","+row.good_type+",\""+row.child_batch_no+"\");'>"+
//                			"<i class='icon-trash'></i>删除</button>";
                    			}
                            }
                        }];
    return dataColumns;
};

/****弹出新增发料/发药窗口*****/
function openFaLiaoWin(){
	if (isRead != 2) {
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
		str += '<div class="span1">';
			str += '<span_customer2 >日期</span_customer2 >' ;
		str += '</div>';
			str += '<div class="span3" align="left">';
			if(currTabName==tabs.发药){
				str += '<div class="input-append date faYaoDatePicker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">'+
					'<input type="text" name="operation_date" id="operation_date" style="width: 158px;height: 24px;" readonly>'+
				'<span class="add-on"><i class="icon-calendar"></i></span></div>';
			}else{
				str += '<div class="input-append date faLiaoDatePicker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">'+
				'<input type="text" name="operation_date" id="operation_date" style="width: 158px;height: 24px;" readonly>'+
			'<span class="add-on"><i class="icon-calendar"></i></span></div>';
			}
			str += '</div>';
			str += '<div class="span1">';
			str += '<span_customer2 >农场</span_customer2 >';
		    str += '</div>';
			str += '<div class="span3" align="left">';
			str += '<select id="farm_id" name="farm_id" style="width: 200px;">';
			str += '</select>';
			str += '</div>';	
			str += '<div class="span1">';
			str += '<span_customer2 >来源批号</span_customer2 >';
		    str += '</div>';
			str += '<div class="span3" align="left">';
			str += '<input type="text" style="margin-top:-3px;width: 186px;" name="src_batch_no" id="src_batch_no"/>' ;
			str += '</div>';
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span1">';
	str += '<span_customer2 >类型</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
//	if(currTabName==tabs.发药){
//	str += '<select style="width: 200px;" name="good_type" id="good_type" onchange= "getVariety2('+$("#good_type").val()+');"/>' ;
//	str += '<option value="2">疫苗</option>';
//	str += '<option value="3">药品</option>';
//	str += '</select>';
//	}else{
	str += '<select id="good_type" name="good_type" style="width: 200px;" onchange= "getVariety2('+$("#good_type").val()+');"/>' ;
//	str += '<option value="6">饲料</option>';
	str += '</select>';
//	}
	
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >品种</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<select style="width: 200px;" name="variety_id" id="variety_id" onchange= "changeGood2Select();"/>' ;
	str += '</select>';
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >供应商</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<select id="corporation_id" name="corporation_id" style="width: 200px;">';
	str += '</select>';
	str += '</div>';
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span1">';
	str += '<span_customer2 >厂家</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<select id="factory_id" name="factory_id" style="width: 200px;">';
	str += '</select>';
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >分发数量</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
		str += '<input type="text" style="margin-top:-3px;width: 186px;" name="send_num" id="send_num"/>' ;
	str += '</div>';
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span1">';
	str += '<span_customer2 >备注</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
		str += '<input type="text" style="margin-top:-3px;width: 474px;" name="bak" id="bak"/>' ;
	str += '</div>';
	str += '</div>';
//	str+='<div class="span2"><label style="padding-left: 70px;color: red; width:450px; text-align: center;margin-top: 15px;" id="add_msg"></label></div>';
	str += '</div>';
layer.open({
	  type: 1,
	  skin: 'layui-layer-lan', //加上边框
	  area: ['885px', '275px'], //宽高
	  content: str,
	  btn: ['确定','取消'],
	  yes: function(index){
		if(submitForm()){ 
		var param;
		param ={operation_date:$("input[name='operation_date']").val(),
				farm_id:$("#farm_id").val(),
				good_type:$("#good_type").val(),
				goods_id:$("#variety_id").val(),
				factory_id:$("#factory_id").val(),
				factory:document.getElementById("factory_id").options[document.getElementById("factory_id").selectedIndex].text,
				corporation_id:$("#corporation_id").val(),
				corporation:document.getElementById("corporation_id").options[document.getElementById("corporation_id").selectedIndex].text,
				src_batch_no:$("#src_batch_no").val(),
				send_num:$("#send_num").val(),
				bak:$("#bak").val(),
				tabName:currTabName};
		$.ajax({
			url : path + "/batch/addFaLiao",
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
				var obj = result.obj;
				var dataJosn = $.parseJSON(JSON.stringify(obj));
                $("#"+currTabName+"Table").bootstrapTable('load',dataJosn);
				if(result.success==true) {
					layer.close(index); 
					layer.msg(result.msg);
					return;
				}else{
					layer.msg(result.msg);
					return;
				}
			}
		});
	  }
	  }
	});
initDatepicker(currTabName); //初始化日期控件
showGoodType();
getFarmList();
getVariety2($("#good_type").val());
}

//显示类型下拉框
function showGoodType(){
    document.getElementById('good_type').options.length = 0;
    if(currTabName==tabs.发药){
    	document.getElementById('good_type').add(new Option("疫苗",2));
        document.getElementById('good_type').add(new Option("药品",3));
    }else{
    	document.getElementById('good_type').add(new Option("饲料",6));
    	document.getElementById('good_type').disabled = 'disabled';
    }
        
};

//显示厂家下拉框
function showCorporationGood(factoryList){
    document.getElementById('factory_id').options.length = 0;
    for(var key in factoryList){
        document.getElementById('factory_id').add(new Option(factoryList[key].factory,factoryList[key].factory_id));
    }
    if(objBatch.fc_factory_id !=0){
        document.getElementById("factory_id").value = objBatch.fc_factory_id;
        }
};

//获取厂家id与名称
function getCorporationGood() {
    var rt = new Array();
    if (document.getElementById('variety_id').options.length > 0) {
        $.ajax({
            type: "post",
            url: path + "/googs/getCorporationGood",
            data: {
                good_type: good_type2,
                good_id : $("#variety_id").val()
            },
            dataType: "json",
            success: function (result) {
                dataList = eval(result.obj);
                var rt = new Array();
                for (var key in dataList) {
                    var tmp = {factory_id: dataList[key].factory_id, factory: dataList[key].factory};
                    rt.push(tmp);
                }
                showCorporationGood(rt);
            }
        });
        return rt;
    }
};


//修改发料/发药信息
function updateFaLiao(operation_date,farm_id,send_ID,good_type,goods_id,factory_id,corporation_id1,src_batch_no,send_num,bak){
	if(isRead==0){
		layer.alert('无权限，请联系管理员!', {
		    skin: 'layui-layer-lan'
		    ,closeBtn: 0
		    ,shift: 4 //动画类型
		  });
		return;
	}
    
	var str = '<br><div class="container-fluid" >';
	str += '<div class="row-fluid">';
		str += '<div class="span1">';
			str += '<span_customer2 >日期</span_customer2 >' ;
		str += '</div>';
			str += '<div class="span3" align="left">';
				str += '<div class="input-append date faLiaoDatePicker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">'+
					'<input type="text" name="operation_date" id="operation_date" style="width: 158px;height: 24px;" readonly>'+
				'<span class="add-on"><i class="icon-calendar"></i></span></div>';
			str += '</div>';
			str += '<div class="span1">';
			str += '<span_customer2 >农场</span_customer2 >';
		    str += '</div>';
			str += '<div class="span3" align="left">';
			str += '<select id="farm_id" name="farm_id" style="width: 200px;">';
			str += '</select>';
			str += '</div>';	
			str += '<div class="span1">';
			str += '<span_customer2 >来源批号</span_customer2 >';
		    str += '</div>';
			str += '<div class="span3" align="left">';
			str += '<input type="text" style="margin-top:-3px;width: 186px;" name="src_batch_no" id="src_batch_no"/>' ;
			str += '</div>';
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span1">';
	str += '<span_customer2 >类型</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
//	if(currTabName==tabs.发药){
//	str += '<select style="width: 200px;" name="good_type" id="good_type" onchange= "getVariety2('+$("#good_type").val()+');"/>' ;
//	str += '<option value="2">疫苗</option>';
//	str += '<option value="3">药品</option>';
//	}else{
	str += '<select style="width: 200px;" name="good_type" id="good_type" onchange= "getVariety2('+$("#good_type").val()+');"/>' ;
//	str += '<option value="6">饲料</option>';
//	}
	str += '</select>';
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >品种</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<select style="width: 200px;" name="variety_id" id="variety_id" onchange= "changeGood2Select();"/>' ;
	str += '</select>';
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >供应商</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<select id="corporation_id" name="corporation_id" style="width: 200px;">';
	str += '</select>';
	str += '</div>';
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span1">';
	str += '<span_customer2 >厂家</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
	str += '<select id="factory_id" name="factory_id" style="width: 200px;">';
	str += '</select>';
	str += '</div>';
	str += '<div class="span1">';
	str += '<span_customer2 >分发数量</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
		str += '<input type="text" style="margin-top:-3px;width: 186px;" name="send_num" id="send_num"/>' ;
	str += '</div>';
	str += '</div>';
	str += '<div class="row-fluid">';
	str += '<div class="span1">';
	str += '<span_customer2 >备注</span_customer2 >';
    str += '</div>';
	str += '<div class="span3" align="left">';
		str += '<input type="text" style="margin-top:-3px;width: 474px;" name="bak" id="bak"/>' ;
	str += '</div>';
	str += '</div>';
//	str+='<div class="span2"><label style="padding-left: 70px;color: red; width:450px; text-align: center;margin-top: 15px;" id="add_msg"></label></div>';
	str += '</div>';
layer.open({
	  type: 1,
	  skin: 'layui-layer-lan', //加上边框
	  area: ['885px', '275px'], //宽高
	  content: str,
	  btn: ['确定','取消'],
	  yes: function(index){
		if(submitForm()){ 
		var param;
		param ={operation_date:$("input[name='operation_date']").val(),
				farm_id:$("#farm_id").val(),
				send_ID:send_ID,
				good_type:$("#good_type").val(),
				goods_id:$("#variety_id").val(),
				factory_id:$("#factory_id").val(),
				factory:document.getElementById("factory_id").options[document.getElementById("factory_id").selectedIndex].text,
				corporation_id:$("#corporation_id").val(),
				corporation:document.getElementById("corporation_id").options[document.getElementById("corporation_id").selectedIndex].text,
				src_batch_no:$("#src_batch_no").val(),
				send_num:$("#send_num").val(),
				bak:$("#bak").val(),
				tabName:currTabName};
		
		$.ajax({
			url : path + "/batch/updateFaLiao",
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
				var obj = result.obj;
				var dataJosn = $.parseJSON(JSON.stringify(obj));
                $("#"+currTabName+"Table").bootstrapTable('load',dataJosn);
                if(result.success==true) {
					layer.close(index); 
					layer.msg(result.msg);
					return;
				}else{
					layer.msg(result.msg);
					return;
				}
			}
		});
	  }
	  }
	});
showGoodType();
document.getElementById("good_type").value = good_type;
objBatch.fc_variety_id = goods_id;
objBatch.fc_corporation_id = corporation_id1;
objBatch.fc_factory_id = factory_id;
objBatch.fc_farm_id = farm_id;
initDatepicker(currTabName); //初始化日期控件
getFarmList();
getVariety2($("#good_type").val());
    document.getElementById("src_batch_no").value = src_batch_no;
    document.getElementById("operation_date").value = operation_date;
    document.getElementById("send_num").value = send_num;
    document.getElementById("bak").value = bak;
    
}

//删除
function delFaLiao(send_ID) {
	if(isRead!=2){
        layer.msg("无权限，请联系管理员！", {});
		return;
	}
	//询问框
	layer.confirm('确定要删除该数据吗？', {
        skin: 'layui-layer-lan'
        , closeBtn: 0
		, shift: 4 //动画类型
	    , btn : [ '确定', '取消' ]
	//按钮
	}, function() {
		$.ajax({
			url : path + "/batch/deleteFaLiao",
			data : {
				send_ID:send_ID
			},
			type : "POST",
			dataType : "json",
			success : function(result) {
				var obj = result.obj;
				var dataJosn = $.parseJSON(JSON.stringify(obj));
                $("#"+currTabName+"Table").bootstrapTable('load',dataJosn);
                if(result.success==true) {
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



