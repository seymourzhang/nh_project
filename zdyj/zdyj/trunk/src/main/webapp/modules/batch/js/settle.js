/**
 * Created by Yoven on 21/4/2017.
 */
function settleColumns(){
    var dataColumns;
    dataColumns = [{field: "farmName",
        title: "农场",
        width: '20%'
        // visible: false
    }, {field: "batch_no",
        title: "养殖批次",
        width: '10%'
    }, {
        field: "gross_chicken_amount",
        title: "毛鸡金额",
        width: '10%'
    }, {
        field: "chicken_amount",
        title: "鸡苗金额",
        width: '10%'
    }, {
    	field: "feed_amount",
        title: "饲料金额",
        width: '10%'
    }, {
    	field: "drug_amount",
        title: "药品金额",
        width: '10%'
    }, {
    	field: "out_datetime",
        title: "出栏日期",
        width: '10%'
    }, {
    	field: "settle_status",
        title: "状态",
        width: '20%',
        formatter: function(value,row,index){
        	var status;
        	if(value ==0){
        		status = "待结算";
        	}else{
        		status = "已结算";
        	}
        	return "&nbsp;<button type='button' class='btn blue' style='display: inline;' onclick='settleSub(\""+row.batch_no+"\","+value+","+row.id+","+row.gross_chicken_number+","+row.gross_chicken_weight+",\""+row.farmName+"\")' >"+
        	status+"</button>";
        }
    }];
    
    return dataColumns;
};

function settleSub(child_batch_no,value,id,gross_chicken_number,gross_chicken_weight,farmName){
	objBatch.settle_id = id;
	objBatch.farmName5=farmName;
	objBatch.settle_status5=value;
	objBatch.child_batch_no5=child_batch_no;
//	if(value==0){
//		document.getElementById("jiesuan1").style.display = "inline";
//	}else{
//		document.getElementById("jiesuan2").style.display = "inline";
//	}
	$.ajax({
        type: "post",
        url: path + "/batch/getSettleSubData",
        data: {settle_id:id,farmName:farmName},
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            pd = eval(result.obj1);
            dataList2 = eval(result.obj2);
            document.getElementById("settleSubFarmTitle").innerHTML = farmName + "结算记录";
            //获取毛鸡
            tableDestroy("settleSub");
            initTable("settleSub", settleSubColumns(), []);
            if(dataList2.length>0){
            loadTableData("settleSub", dataList2);
	        }else{
	        	initTableRow("settleSub", getTableEmptyRow("settleSub"));
	        }
            //获取非毛鸡
            tableDestroy("settleSub2");
            initTable("settleSub2", settleSub2Columns(), []);
            loadTableData("settleSub2", dataList);
            document.getElementById("maoji").style.display = "inline";
            document.getElementById("maoji2").style.display = "inline";
            document.getElementById("maoji3").style.display = "inline";
            document.getElementById("maoji4").style.display = "inline";
            document.getElementById("js3").innerHTML =pd.priceCount;
            document.getElementById("js4").innerHTML =pd.munberCount;
            document.getElementById("js5").innerHTML =pd.weightCount;
            if(gross_chicken_number==undefined){
            	objBatch.gross_chicken_number=0;
                objBatch.gross_chicken_weight=0;
            }else{
            	objBatch.gross_chicken_number=gross_chicken_number;
                objBatch.gross_chicken_weight=gross_chicken_weight;
            }
            
        }
    });
}

function getTableEmptyRow(tableName){
    var count = $('#' + tableName + 'Table').bootstrapTable('getData').length;
    count += -10000;
    var emptyRow ;
    var defaultValue = "";
        emptyRow = {id: count,
            FromTime: defaultValue,
            ToTime: defaultValue,
            Trg_Diff: defaultValue,
            Till_Humid: defaultValue,
            On: defaultValue,
            Off: defaultValue
        };

    return emptyRow;
}

function editSettleData(){
	$.ajax({
        type: "post",
        url: path + "/batch/updateSettleData",
        data: {id:objBatch.settle_id},
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            //获取结算主表数据
            tableDestroy("settle");
            initTable("settle", settleColumns(), []);
            loadTableData("settle", dataList);
        }
    });
}

function settleSubColumns(){
    var dataColumns;
    dataColumns = [{field: "settle_type_name",
        title: "结算种类",
        width: '10%'
    }, {field: "good_munber",
        title: "数量",
        width: '15%'
    }, {
    	field: "avg_weight",
        title: "均重",
        width: '15%'
    }, {field: "good_weight",
        title: "重量(公斤)",
        width: '15%'
    }, {
        field: "good_price",
        title: "单价(元/公斤)",
        width: '15%'
    }, {
        field: "good_total_price",
        title: "金额(元)",
        width: '15%'
    }, {
    	field: "operation",
        title: "操作",
        width: '15%',
        formatter: function(value,row,index){
        	if(isRead!=2){
        		return "&nbsp;<button type='button' class='btn blue' style='display: inline;' onclick='editSettleSub("+row.id+","+ row.settle_type+","+row.good_munber
            	+","+row.good_price+","+row.good_weight+",\""+row.spec+"\","+row.good_unit+",\""+row.good_name+"\","+row.good_id+","+row.good_total_price+")'" +
            			"disabled='disabled'>"+
            	"编辑</button>";
        	}else{
        		return "&nbsp;<button type='button' class='btn blue' style='display: inline;' onclick='editSettleSub("+row.id+","+ row.settle_type+","+row.good_munber
            	+","+row.good_price+","+row.good_weight+",\""+row.spec+"\","+row.good_unit+",\""+row.good_name+"\","+row.good_id+","+row.good_total_price+")'" +
            			">"+
            	"编辑</button>";
        	}
        	
        }
    }];
    
    return dataColumns;
};

function settleSub2Columns(){
    var dataColumns;
    dataColumns = [{field: "settle_type_name",
        title: "结算种类",
        width: '10%'
    }, {field: "good_name",
        title: "名称",
        width: '20%'
    }, {
        field: "spec_name",
        title: "规格",
        width: '10%'
    }, {
        field: "unit_name",
        title: "单位",
        width: '10%'
    }, {field: "good_munber",
        title: "数量",
        width: '10%'
    }, {
        field: "good_total_price",
        title: "金额(元)",
        width: '10%'
    }, {
        field: "good_price",
        title: "单价",
        width: '10%'
    }, {
    	field: "operation",
        title: "操作",
        width: '20%',
        formatter: function(value,row,index){
        	if(isRead!=2){
        		return "&nbsp;<button type='button' class='btn blue' style='display: inline;' onclick='editSettleSub("+row.id+","+ row.settle_type+","+row.good_munber
            	+","+row.good_price+","+row.good_weight+",\""+row.spec+"\","+row.good_unit+",\""+row.good_name+"\","+row.good_id+","+row.good_total_price+")' " +
            			"disabled='disabled'>"+
            	"编辑</button>"+
            	"&nbsp;<button type='button' class='btn blue' style='display: inline;' onclick='deleteSettleSubData("+row.id+")' disabled='disabled'>"+
            	"删除</button>";
        	}else{
        		return "&nbsp;<button type='button' class='btn blue' style='display: inline;' onclick='editSettleSub("+row.id+","+ row.settle_type+","+row.good_munber
            	+","+row.good_price+","+row.good_weight+",\""+row.spec+"\","+row.good_unit+",\""+row.good_name+"\","+row.good_id+","+row.good_total_price+")' " +
            			">"+
            	"编辑</button>"+
            	"&nbsp;<button type='button' class='btn blue' style='display: inline;' onclick='deleteSettleSubData("+row.id+")'>"+
            	"删除</button>";
        	}
        }
    }];
    
    return dataColumns;
};

function addSettleSub(){
	if(isRead!=2){
		layer.alert('无权限，请联系管理员!', {
		    skin: 'layui-layer-lan'
		    ,closeBtn: 0
		    ,shift: 4 //动画类型
		  });
		return;
	}
    
	var str = '<br><div class="container-fluid" >';
	str += '<div class="row-fluid">';
	str += '<div class="span3" align="right">';
		str += '<span_customer2 >结算种类</span_customer2 >&nbsp;&nbsp;&nbsp;' ;
//	str += '</div>';
//		str += '<div class="span2" align="left">';
		str += '<select id="settle_type" name="settle_type" style="width: 100px;" onchange="setGoodName('+1+','+1+','+null+','+
		null+','+null+');">';
		str += '<option value="2">鸡苗</option>';
		str += '<option value="3">饲料</option>';
		str += '<option value="4">疫苗药品</option>';
		str += '</select>';
		str += '</div>';
		str += '<div class="span3" align="right">';
		str += '<span_customer2 >名称</span_customer2 >&nbsp;&nbsp;&nbsp;';
		str += '<select id="good_id" name="good_id" style="width: 100px;" onchange="setSpec(1,0,0,0);">';
		str += '</select>';
		str += '</div>';
		str += '<div class="span3" align="right">';
		str += '<span_customer2 >单位</span_customer2 >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
		str += '<select id="good_unit" name="good_unit" style="width: 100px;">';
		str += '</select>';
		str += '</div>';
		str += '<div class="span3" align="right">';
		str += '<span_customer2 >规格</span_customer2 >&nbsp;&nbsp;&nbsp;';
		str += '<select id="spec" name="spec" style="width: 100px;">';
		str += '</select>';
		str += '</div>';
		str += '</div>';
		str += '<div class="row-fluid">';
		str += '<div class="span3" align="right">';
		str += '<span_customer2 >数量</span_customer2 >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
		str += '<input type="text" style="margin-top:0px;width: 86px;" name="good_munber" id="good_munber" onblur="setgoodTotalPrice2()"/>' ;
		str += '</div>';
		str += '<div class="span3" align="right">';
		str += '<span_customer2 >单价</span_customer2 >&nbsp;&nbsp;&nbsp;';
		str += '<input type="text" style="margin-top:0px;width: 86px;" name="good_price" id="good_price" onblur="setgoodTotalPrice2()"/>' ;
		str += '</div>';
		str += '<div class="span3" align="right">';
		str += '<span_customer2 >金额</span_customer2 >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
		str += '<input type="text" style="margin-top:0px;width: 86px;" name="good_total_price" id="good_total_price"/>' ;
		str += '</select>';
		str += '</div>';
		str += '</div>';
		str += '</div>';
layer.open({
	  type: 1,
	  skin: 'layui-layer-lan', //加上边框
	  area: ['805px', '195px'], //宽高
	  content: str,
	  btn: ['确定','取消'],
	  yes: function(index){
		if(checkVarSettle()){ 
		var param;
		param ={settle_id:objBatch.settle_id,
				farm_id:objBatch.farm_id,
				settle_type:$("#settle_type").val(),
				good_munber:$("#good_munber").val(),
				good_price:$("#good_price").val(),
				good_weight:$("#good_weight").val(),
				good_total_price:$("#good_total_price").val(),
				good_id:$("#good_id").val(),
				good_name:document.getElementById("good_id").options[document.getElementById("good_id").selectedIndex].text,
				spec:$("#spec").val(),
				good_unit:$("#good_unit").val()};

		$.ajax({
			url : path + "/batch/saveSettleSubData",
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
				var obj = result.obj;
				var dataJosn = eval(obj);
				if($("#settle_type").val() ==1){
                $("#settleSubTable").bootstrapTable('load',dataJosn);
				}else{
					$("#settleSub2Table").bootstrapTable('load',dataJosn);
				}
				editSettleData();
				document.getElementById("maoji").style.display = "none";
	            document.getElementById("maoji2").style.display = "none";
	            document.getElementById("maoji3").style.display = "none";
	            document.getElementById("maoji4").style.display = "none";
	            settleSub(objBatch.child_batch_no5,objBatch.settle_status5,objBatch.settle_id,objBatch.gross_chicken_number,objBatch.gross_chicken_weight,objBatch.farmName5);
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

setGoodName(1,1,null,null,null);
}
var good_total_price5=0;
function editSettleSub(id,settle_type,good_munber,good_price,good_weight,spec,good_unit,good_name,good_id,good_total_price){
	if(isRead!=2){
		layer.alert('无权限，请联系管理员!', {
		    skin: 'layui-layer-lan'
		    ,closeBtn: 0
		    ,shift: 4 //动画类型
		  });
		return;
	}
    
	var str = '<br><div class="container-fluid" >';
	var hight;
	if(settle_type!=undefined && settle_type !=1){
		str += '<div class="row-fluid">';
		str += '<div class="span3" align="right">';
			str += '<span_customer2 >结算种类</span_customer2 >&nbsp;&nbsp;&nbsp;' ;
//		str += '</div>';
//			str += '<div class="span2" align="left">';
			str += '<select id="settle_type" name="settle_type" style="width: 100px;" onchange="setGoodName('+1+','+settle_type+','+
			good_id+','+good_unit+','+spec+');">';
			str += '<option value="2">鸡苗</option>';
			str += '<option value="3">饲料</option>';
			str += '<option value="4">疫苗药品</option>';
			str += '</select>';
			str += '</div>';
			str += '<div class="span3" align="right">';
			str += '<span_customer2 >名称</span_customer2 >&nbsp;&nbsp;&nbsp;';
			str += '<select id="good_id" name="good_id" style="width: 100px;" onchange="setSpec('+settle_type+','+good_id+','+
			good_unit+','+spec+')">';
			str += '</select>';
			str += '</div>';
			str += '<div class="span3" align="right">';
			str += '<span_customer2 >单位</span_customer2 >&nbsp;&nbsp;&nbsp;&nbsp;';
			str += '<select id="good_unit" name="good_unit" style="width: 100px;">';
			str += '</select>';
			str += '</div>';
			str += '<div class="span3" align="right">';
			str += '<span_customer2 >规格</span_customer2 >&nbsp;&nbsp;&nbsp;';
			str += '<select id="spec" name="spec" style="width: 100px;">';
			str += '</select>';
			str += '</div>';
			str += '</div>';
			hight = ['755px', '195px'];
			str += '<div class="row-fluid">';
			str += '<div class="span3" align="right">';
			str += '<span_customer2 >数量</span_customer2 >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
			str += '<input type="text" style="margin-top:0px;width: 86px;" name="good_munber" id="good_munber" onblur="setgoodTotalPrice2()"/>' ;
			str += '</div>';
			str += '<div class="span3" align="right">';
			str += '<span_customer2 >单价</span_customer2 >&nbsp;&nbsp;&nbsp;';
			str += '<input type="text" style="margin-top:0px;width: 86px;" name="good_price" id="good_price" onblur="setgoodTotalPrice2()"/>' ;
			str += '</div>';
			str += '<div class="span3" align="right">';
			str += '<span_customer2 >金额</span_customer2 >&nbsp;&nbsp;&nbsp;&nbsp;';
			str += '<input type="text" style="margin-top:0px;width: 86px;" name="good_total_price" id="good_total_price"/>' ;
			str += '</select>';
			str += '</div>';
			str += '</div>';
			str += '</div>';
	}else{
		hight = ['675px', '155px'];
		str += '<div class="row-fluid">';
		str += '<div class="span3" align="right">';
		str += '<span_customer2 >数量</span_customer2 >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
//	    str += '</div>';
//		str += '<div class="span2" align="left">';
		str += '<input type="text" style="margin-top:0px;width: 86px;" name="good_munber" id="good_munber"/>' ;
		str += '</div>';
		str += '<div class="span3" align="right">';
		str += '<span_customer2 >重量</span_customer2 >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
//	    str += '</div>';
//		str += '<div class="span2" align="left">';
		str += '<input type="text" style="margin-top:0px;width: 86px;" name="good_weight" id="good_weight" onblur="setgoodTotalPrice()"/>' ;
		str += '</select>';
		str += '</div>';
		str += '<div class="span3" align="right">';
		str += '<span_customer2 >单价</span_customer2 >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
//	    str += '</div>';
//		str += '<div class="span2" align="left">';
		str += '<input type="text" style="margin-top:0px;width: 86px;" name="good_price" id="good_price" onblur="setgoodTotalPrice()"/>' ;
		str += '</div>';
		str += '<div class="span3" align="right">';
		str += '<span_customer2 >金额</span_customer2 >&nbsp;&nbsp;&nbsp;&nbsp;';
		str += '<input type="text" style="margin-top:0px;width: 86px;" name="good_total_price" id="good_total_price"/>' ;
		str += '</select>';
		str += '</div>';
		str += '</div>';
		str += '</div>';
	}
layer.open({
	  type: 1,
	  skin: 'layui-layer-lan', //加上边框
	  area: hight, //宽高
	  content: str,
	  btn: ['确定','取消'],
	  yes: function(index){
		if(checkVarSettle()){ 
		var param;
		var url = "/batch/updateSettleSubData";
		if(settle_type ==undefined){
			url = "/batch/saveSettleSubData";
			param ={settle_id:objBatch.settle_id,
					farm_id:objBatch.farm_id,
					settle_type:1,
					good_munber:$("#good_munber").val(),
					good_price:$("#good_price").val(),
					good_weight:$("#good_weight").val(),
					good_total_price:$("#good_total_price").val(),
					good_id:good_id,
					good_name:good_name,
					spec:spec,
					good_unit:good_unit};
		}else if(settle_type ==1){
		param ={id:id,
				settle_type:1,
				good_munber:$("#good_munber").val(),
				good_price:$("#good_price").val(),
				good_weight:$("#good_weight").val(),
				good_total_price:$("#good_total_price").val(),
				good_name:good_name,
				good_id:good_id,
				spec:spec,
				good_unit:good_unit};
		
		}else{
		param ={id:id,
				settle_type:$("#settle_type").val(),
				good_munber:$("#good_munber").val(),
				good_price:$("#good_price").val(),
				good_weight:$("#good_weight").val(),
				good_total_price:$("#good_total_price").val(),
				good_id:$("#good_id").val(),
				good_name:document.getElementById("good_id").options[document.getElementById("good_id").selectedIndex].text,
				spec:$("#spec").val(),
				good_unit:$("#good_unit").val()};
		}
		$.ajax({
			url : path + url,
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
				var obj = result.obj;
				var dataJosn = eval(obj);
				if(settle_type ==1){
                $("#settleSubTable").bootstrapTable('load',dataJosn);
				}else{
					$("#settleSub2Table").bootstrapTable('load',dataJosn);
				}
				editSettleData();
				document.getElementById("maoji").style.display = "none";
	            document.getElementById("maoji2").style.display = "none";
	            document.getElementById("maoji3").style.display = "none";
	            document.getElementById("maoji4").style.display = "none";
	            settleSub(objBatch.child_batch_no5,objBatch.settle_status5,objBatch.settle_id,objBatch.gross_chicken_number,objBatch.gross_chicken_weight,objBatch.farmName5);
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

if(good_munber!=undefined){
document.getElementById("good_munber").value = good_munber;
}else{
	document.getElementById("good_munber").value = objBatch.gross_chicken_number;
}
if(settle_type ==undefined || settle_type ==1){
	if(good_weight!=undefined){	
     document.getElementById("good_weight").value = good_weight;
	}else{
		document.getElementById("good_weight").value = objBatch.gross_chicken_weight;
	}
	
}

if(good_price!=undefined){
document.getElementById("good_price").value = good_price;
}else{
	document.getElementById("good_price").value = 0;
}
if(settle_type !=undefined && settle_type !=1){
document.getElementById("settle_type").value = settle_type;
good_total_price5 = good_total_price;
setGoodName2(2,settle_type,good_id,good_unit,spec);
}else{
	setCorporationGood2(good_total_price);
}

}

function deleteSettleSubData(id){
	if (isRead != 2) {
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
            url: path+"/batch/deleteSettleSubData",
            data: {"id":id},
            type : "POST",
            dataType: "json",
            cache: false,
            // timeout:50000,
            success: function(result) {     
                    var obj = result.obj;
                    if(null != obj) {
                        var dataJosn = $.parseJSON(JSON.stringify(obj));
                        $("#settleSub2Table").bootstrapTable('load',dataJosn);
                    } 
                    editSettleData();
    	            settleSub(objBatch.child_batch_no5,objBatch.settle_status5,objBatch.settle_id,objBatch.gross_chicken_number,objBatch.gross_chicken_weight,objBatch.farmName5);
                    layer.msg('删除成功!');
            }
        });
    });
}


//检查变量
function checkVarSettle(){
	var good_munber=$("#good_munber").val();
//	var good_weight=$("good_weight").val();
	var good_price=$("#good_price").val();

	if(good_munber =="" ){
		layer.msg("数量不能为空！");
		return false;
	}else if (parseInt(good_munber)!=good_munber){
		layer.msg("数量必须是整数！");
		return false;
	}else if(good_munber<=0){
		layer.msg("数量必须大于0！");
		return false;
	}else if(good_price =="" ){
		layer.msg("单价不能为空！");
		return false;
	}else if(isNaN(good_price)){
		layer.msg("单价必须是数字！");
		return false;
	}
	return true;
}

function setGoodName(settletype,settle_type,good_id,good_unit,spec) {
	document.getElementById("good_munber").value =0;
	$.ajax({
		type : "post",
		url : path + "/batch/getGoods",
		data : {
			"settle_type" : $("#settle_type").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#good_id option").remove();
//			$("#good_id").append('<option value=""></option>');
			for (var i = 0; i < list.length; i++) {
				$("#good_id").append('<option value="' + list[i].good_id + '">' + list[i].good_name + '</option>');
			}
//			var goodMunber = $("#good_munber").val();
			var settleType5 = $("#settle_type").val();
            if(settleType5=="2"){//鸡苗
            	getSumjimiao();
            }else if(settleType5=="3"){//饲料
            	getSumsiliao();
            }else if(settleType5=="4"){//疫苗药品
            	getSummiaoyao();
            }
            
			if(settletype==1){
				setSpec(1,null,null,null);
			}else{
				setSpec(settle_type,good_id,good_unit,spec);
			}
			
		}
	});
}

function setGoodName2(settletype,settle_type,good_id,good_unit,spec) {
	$.ajax({
		type : "post",
		url : path + "/batch/getGoods2",
		data : {
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#good_id option").remove();
//			$("#good_id").append('<option value=""></option>');
			for (var i = 0; i < list.length; i++) {
				$("#good_id").append('<option value="' + list[i].good_id + '">' + list[i].good_name + '</option>');
			}
			if(settletype==1){
				setSpec(1,null,null,null);
			}else{
				setSpec(settle_type,good_id,good_unit,spec);
			}
			
		}
	});
}

//统计鸡苗数量
function getSumjimiao() {
	$.ajax({
		type : "post",
		url : path + "/batch/getSumjimiao",
		data : {farm_id:objBatch.farm_id,child_batch_no:objBatch.child_batch_no5
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			if(list!=null && list!="" && list!=undefined){
				document.getElementById("good_munber").value =list.sumCount;
			}else{
				document.getElementById("good_munber").value =0;
			}
			
		}
	});
}

//统计饲料数量
function getSumsiliao() {
	$.ajax({
		type : "post",
		url : path + "/batch/getSumsiliao",
		data : {farm_id:objBatch.farm_id,send_batch_no:objBatch.child_batch_no5
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			if(list!=null && list!="" && list!=undefined){
				document.getElementById("good_munber").value =list.sumCount;
			}else{
				document.getElementById("good_munber").value =0;
			}
			
		}
	});
}

//统计苗药数量
function getSummiaoyao() {
	$.ajax({
		type : "post",
		url : path + "/batch/getSummiaoyao",
		data : {farm_id:objBatch.farm_id,send_batch_no:objBatch.child_batch_no5
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			if(list!=null && list!="" && list!=undefined){
				document.getElementById("good_munber").value =list.sumCount;
			}else{
				document.getElementById("good_munber").value =0;
			}
			
		}
	});
}

function setCorporationGood(settle_type) {
//	$("#spec_select").attr("disabled", true);
	$.ajax({
		type : "post",
		url : path + "/googs/getCorporationGood",
		data : {good_id:$("#good_id").val()},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			if(list.length !=0){
				document.getElementById("good_price").value = list[0].price;	
			}else{
				document.getElementById("good_price").value = 0;
			}
			
			
			var goodPrice = document.getElementById("good_price").value;
			var goodMunber = document.getElementById("good_munber").value;
			if(good_total_price5!=undefined && good_total_price5!=0 && settle_type!=1){
			document.getElementById("good_total_price").value = good_total_price5;
			}else{
				document.getElementById("good_total_price").value = (goodPrice * goodMunber).toFixed(2);
			}
		}
	});
}

function setCorporationGood2(good_total_price) {
	$.ajax({
		type : "post",
		url : path + "/batch/getGoodId",
		data : {child_batch_no:objBatch.child_batch_no5},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			document.getElementById("good_price").value = list[0].price;
			
			var goodPrice = document.getElementById("good_price").value;
			var goodMunber = document.getElementById("good_weight").value;
			if(good_total_price!=undefined){
			document.getElementById("good_total_price").value = good_total_price;
			}else{
				document.getElementById("good_total_price").value = (goodPrice * goodMunber).toFixed(2);
			}
		}
	});
}

function setSpec(settle_type,good_id,good_unit,spec) {
//	$("#spec_select").attr("disabled", true);
	$.ajax({
		type : "post",
		url : path + "/googs/getSpec2",
		data : {good_id:$("#good_id").val()},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#spec option").remove();
//			$("#spec").append('<option value=""></option>');
			for (var i = 0; i < list.length; i++) {
				$("#spec").append("<option value=" + list[i].biz_code + ">" + list[i].code_name + "</option>");
			}
			document.getElementById("spec").value = spec;
//			$("#spec_select").attr("disabled", false);
			setUnit(settle_type,good_id,good_unit,spec);
		}
	});
}

function setUnit(settle_type,good_id,good_unit,spec) {
	$.ajax({
		type : "post",
		url : path + "/googs/getUnit2",
		data : {good_id:$("#good_id").val()},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#good_unit option").remove();
//			$("#good_unit").append('<option value=""></option>');
			for (var i = 0; i < list.length; i++) {
				$("#good_unit").append("<option value=" + list[i].biz_code + ">" + list[i].code_name + "</option>");
			}
			if(settle_type != undefined && settle_type !=1){
				document.getElementById("settle_type").value =settle_type;
				document.getElementById("good_id").value = good_id;
				document.getElementById("good_unit").value =good_unit;
				document.getElementById("spec").value = spec;
				}
//			initSpecSelect();
			setCorporationGood(settle_type);
		}
	});
}

function empty(){
	if(document.getElementById("goods_select").value==""){
		document.getElementById("good_id").value = null;
	}	
	if(document.getElementById("spec_select").value==""){
		document.getElementById("spec").value = null;
	}	
	if(document.getElementById("unit_select").value==""){
		document.getElementById("good_unit").value = null;
	}
}

function initSpecSelect(){
    $.fn.typeahead.Constructor.prototype.blur = function() {
        var that = this;
        setTimeout(function () { that.hide(); }, 250);
    };
//物资
$('#goods_select').typeahead({
	items: 4,
	minLength:2,
    source: function(query, process) {
    	var goods = getSpecNameList('good_id', query);
        var results = goods.map(function (item,index,input){
            return item.id+"";
		});
        if(results.length ==0){
        	document.getElementById("good_id").value = null;
        }
        process(results);
        // return goods;
    }
    ,matcher: function (item) {
        var goods = getSpecNameList('good_id', item);
        var flag = false;
        for(var key in goods){
        	if(item == goods[key].id || item == goods[key].text){
        		flag = true;
			}
		}
        return flag;
	}
    ,highlighter: function (item) {
        var goods = getSpecNameList('good_id', item);
        var good = goods.find(function (p) {
            return p.id == item;
        });
        var str;
        str = good.id.replace(",", "|");
        return str;
    }
    ,updater: function (item) {
        var goods = getSpecNameList('good_id', item);
        var good = goods.find(function (p) {
            return p.id == item;
        });
        setSpecInfo('good_id', good.id);
        return good.text;
    }
});

//单位
$('#unit_select').typeahead({
	items: 4,
    source: function(query, process) {
    	var goods = getSpecNameList('good_unit', query);
        var results = goods.map(function (item,index,input){
            return item.id+"";
		});
        if(results.length ==0){
        	document.getElementById("good_unit").value = null;
        }
        process(results);
        // return goods;
    }
    ,matcher: function (item) {
        var goods = getSpecNameList('good_unit', item);
        var flag = false;
        for(var key in goods){
        	if(item == goods[key].id || item == goods[key].text){
        		flag = true;
			}
		}
        return flag;
	}
    ,highlighter: function (item) {
        var goods = getSpecNameList('good_unit', item);
        var good = goods.find(function (p) {
            return p.id == item;
        });
        return good.text;
    }
    ,updater: function (item) {
        var goods = getSpecNameList('good_unit', item);
        var good = goods.find(function (p) {
            return p.id == item;
        });
        setSpecInfo('good_unit', good.id);
        return good.text;
    }
});

//规格
$('#spec_select').typeahead({
	items: 4,
//	minLength:4,
    source: function(query, process) {
    	var results=null;
    	query = query.replace(/\*/g, "\\*");
    	
    		if(query.length==4){
            	$.ajax({
            		type : "post",
            		url : path + "/googs/getSpec",
            		data : {code_name:query},
            		dataType : "json",
            		success : function(result) {
            			var list = result.obj;
            			$("#spec option").remove();
            			for (var i = 0; i < list.length; i++) {
            				$("#spec").append("<option value=" + list[i].biz_code + ">" + list[i].code_name + "</option>");
            			}
                	var goods = getSpecNameList('spec', query);
                    results = goods.map(function (item,index,input){
                        return item.id+"";
        			});
                    if(results.length ==0){
                    	document.getElementById("spec").value = null;
                    }
                    process(results);
                   
                    
            		}
            	});
            		}
    		if(query.length<=3){
    		document.getElementById("spec").value = null;
    		return;
    		}
    		if(query.length>4){
        		$("#spec_select").attr("disabled", true);
//        		document.getElementById("spects").style.display = "none";
        	var goods = getSpecNameList('spec', query);
            results = goods.map(function (item,index,input){
                return item.id+"";
			});
            if(results.length ==0){
            	document.getElementById("spec").value = null;
            }
            process(results);
            $("#spec_select").attr("disabled", false);
        	}
    }
    ,matcher: function (item) {
        var goods = getSpecNameList('spec', item);
        var flag = false;
        for(var key in goods){
        	if(item == goods[key].id || item == goods[key].text){
        		flag = true;
			}
		}
        return flag;
	}
    ,highlighter: function (item) {
        var goods = getSpecNameList('spec', item);
        var good = goods.find(function (p) {
            return p.id == item;
        });
        $("#spec_select").attr("disabled", false);
        return good.text;
    }
    ,updater: function (item) {
        var goods = getSpecNameList('spec', item);
        var good = goods.find(function (p) {
            return p.id == item;
        });
        setSpecInfo('spec', good.id);
        return good.text;
    }
});
}

function getSpecNameList(selectName, value){
	value = process( value);
	var specNameList = [];
    var select = document.getElementById(selectName);
    var options = select.options;
	var oValue = "";
    var oText = "";
	for(var key in options){
		key = process( key);
        oValue = options[key].value;
		oText = options[key].text;
        var oTextFlag = new RegExp(value).test(oText);
        var oValueFlag = new RegExp(value).test(oValue);
		if(oTextFlag == true || oValueFlag == true){
			specNameList.push({id:oValue, text:oText});
		}
//		if(specNameList.length==4){
//			return specNameList;
//		}
	}
	return specNameList;
}

function setSpecInfo(selectName, goodId){
    var select = document.getElementById(selectName);
    var options = select.options;
    for(var key in options){
        if(goodId == options[key].value){
        	options[key].selected = true;
        }
	}
}

//计算总金额
function setgoodTotalPrice(){
	if($("#good_weight").val()=="" || $("#good_weight").val()==null || $("#good_price").val() =="" || $("#good_price").val()==null){
		document.getElementById("good_total_price").value=0;
	}else{
		document.getElementById("good_total_price").value = (parseInt($("#good_weight").val()) * parseFloat($("#good_price").val())).toFixed(2);
	}
	
}

function setgoodTotalPrice2(){
	if($("#good_munber").val()=="" || $("#good_munber").val()==null || $("#good_price").val() =="" || $("#good_price").val()==null){
		document.getElementById("good_total_price").value=0;
	}else{
	document.getElementById("good_total_price").value = (parseInt($("#good_munber").val()) * parseFloat($("#good_price").val())).toFixed(2);
	}
}

//处理特殊字符
function process( zifu){
	zifu = zifu.replace(/\\/g, "");
	zifu = zifu.replace(/\+/g, "\\+");
	zifu = zifu.replace(/\*/g, "\\*");
	zifu = zifu.replace(/\//g, "\\/");
	zifu = zifu.replace(/\-/g, "\\-");
	zifu = zifu.replace(/\./g, "\\.");
	zifu = zifu.replace(/\(/g, "\\(");
	zifu = zifu.replace(/\)/g, "\\)");
	zifu = zifu.replace(/\{/g, "\\{");
	zifu = zifu.replace(/\}/g, "\\}");
	zifu = zifu.replace(/\[/g, "\\[");
	zifu = zifu.replace(/\]/g, "\\]");
	return zifu;
}