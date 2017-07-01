/**
 * Created by Yoven on 19/4/2017.
 */
function taoTaiColumns(){
	var dataColumns;
    if(hunState == 0){
    dataColumns = [{field: "operation_date",
        title: "淘汰日",
        width: '10%'
        // visible: false
    }, {field: "houseId",
        title: "淘汰栋ID",
        visible: false
    }, {
        field: "houseName",
        title: "淘汰栋舍",
        width: '10%'
    }, {
        field: "weed_out_total_count",
        title: "淘汰母鸡数量",
        width: '10%'
    }, {
    	field: "weed_out_total_weight",
        title: "淘汰母鸡重量（kg）",
        width: '10%'
    }, {
    	field: "weed_out_avg_price",
        title: "淘汰母鸡单价",
        width: '10%'
    }, {
    	field: "weed_out_total_price",
        title: "淘汰母鸡金额",
        width: '10%'
    }, {
        field: "weed_out_male_total_count",
        title: "淘汰公鸡数量",
        width: '10%'
    }, {
    	field: "weed_out_male_total_weight",
        title: "淘汰公鸡重量（kg）",
        width: '10%'
    }, {
    	field: "weed_out_male_avg_price",
        title: "淘汰公鸡单价",
        width: '10%'
    }, {
    	field: "weed_out_male_total_price",
        title: "淘汰公鸡金额",
        width: '10%'
    }];
    }else{
    	dataColumns = [{field: "operation_date",
            title: "淘汰日",
            width: '20%'
            // visible: false
        }, {field: "houseId",
            title: "淘汰栋ID",
            visible: false
        }, {
            field: "houseName",
            title: "淘汰栋舍",
            width: '20%'
        }, {
            field: "weed_out_total_count",
            title: "淘汰数量",
            width: '20%'
        }, {
        	field: "weed_out_total_weight",
            title: "淘汰重量（kg）",
            width: '10%'
        }, {
        	field: "weed_out_avg_price",
            title: "淘汰单价",
            width: '10%'
        }, {
        	field: "weed_out_total_price",
            title: "淘汰金额",
            width: '20%'
        }];
    }
    
    return dataColumns;
};

//检查变量
function checkVarTaoTai(objBatch){
    objBatch.resultFlag = true;
    objBatch.resultMsg = "检测通过";
    var test;

        test = parseInt(objBatch.weed_out_total_weight);
        if (isNaN(test))
        {
            objBatch.resultFlag = false;
            objBatch.resultMsg = "重量必须是数字!";
        }
        test = parseInt(objBatch.weed_out_total_count);
        if (isNaN(test))
        {
            objBatch.resultFlag = false;
            objBatch.resultMsg = "数量必须是数字!";
        }
        if(test!=objBatch.weed_out_total_count){
        	objBatch.resultFlag = false;
            objBatch.resultMsg = "数量必须是整数!";
        }
        test = parseInt(objBatch.weed_out_avg_price);
        if (isNaN(test))
        {
            objBatch.resultFlag = false;
            objBatch.resultMsg = "单价必须是数字!";
        }
        if(objBatch.weed_out_avg_price==0 && objBatch.weed_out_total_weight !=0){
        	objBatch.resultFlag = false;
            objBatch.resultMsg = "单价价格不能为0!";
        }
        if(objBatch.weed_out_avg_price!=0 && objBatch.weed_out_total_weight ==0){
        	objBatch.resultFlag = false;
            objBatch.resultMsg = "重量价格不能为0!";
        }

    if(""== document.getElementById("taoTaiQueryTime").value){
        objBatch.resultFlag = false;
        objBatch.resultMsg = "淘汰日不能为空!";
    }

    layer.msg(objBatch.resultMsg);
}

//淘汰批次
function saveTaoTaiData(objBatch){
    objBatch.resultFlag = true;
    objBatch.resultMsg = "批次淘汰成功!";
    $.ajax({
        type: "post",
        async:false,
        url: path + "/batch/saveTaoTaiData",
        data: {batch_no: objBatch.batch_no
                , house_code: objBatch.house_code
                , house_name: objBatch.house_name
                , farm_id: objBatch.farm_id
                , farm_name: objBatch.farm_name
                , operation_date: objBatch.operation_date
                , weed_out_total_weight: objBatch.weed_out_total_weight
                , weed_out_total_count: objBatch.weed_out_total_count
                , weed_out_avg_price: objBatch.weed_out_avg_price
                , weed_out_male_total_weight: objBatch.weed_out_male_total_weight
                , weed_out_male_total_count: objBatch.weed_out_male_total_count
                , weed_out_male_avg_price: objBatch.weed_out_male_avg_price
                , is_mix: objBatch.is_mix
        },
        dataType: "json",
        success: function (result) {
            if(!result.success) {
                objBatch.resultFlag = false;
                objBatch.resultMsg = result.msg;
            }
            return objBatch;
        }
    });
    return objBatch;
};

//判断是否育成栋舍类型
function getHouseFlag(){
    var rt = false;
    if(objBatch.house_type == '1')
        rt = false;
    else
        rt = true;
    return rt;
};

//获取批次号
function getBatchNo(tabName){
//    var dataList = $('#' + tabName + 'Table').bootstrapTable('getData');
//    for(var key in dataList){
//        if((dataList[key].houseId == objBatch.house_code)){
//            objBatch.batch_no = dataList[key].batchNo;
//        }
//    }
	$.ajax({
        type: "post",
        url: path + "/batch/getCreateBatchData",
        data: {
            house_code: objBatch.house_code
        },
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            for(var key in dataList){
              if((dataList[key].houseId == objBatch.house_code)){
                  objBatch.batch_no = dataList[key].batchNo;
              }
          }
        }
    });
}

//计算淘汰总金额
function setSumAcount(){
	if($("#taoTaiSumWeight").val()=="" || $("#taoTaiSumWeight").val()==null || $("#taoTaiAvgPrice").val() =="" || $("#taoTaiAvgPrice").val()==null){
		document.getElementById("taoTaiSumAcount").value=0;
	}else{
	document.getElementById("taoTaiSumAcount").value = (parseInt($("#taoTaiSumWeight").val()) * parseFloat($("#taoTaiAvgPrice").val())).toFixed(2);
	}
	
	if($("#taoTaiSumWeight2").val()=="" || $("#taoTaiSumWeight2").val()==null || $("#taoTaiAvgPrice2").val() =="" || $("#taoTaiAvgPrice2").val()==null){
		document.getElementById("taoTaiSumAcount2").value=0;
	}else{
	document.getElementById("taoTaiSumAcount2").value = (parseInt($("#taoTaiSumWeight2").val()) * parseFloat($("#taoTaiAvgPrice2").val())).toFixed(2);
	}
	
	if($("#taoTaiMaleSumWeight").val()=="" || $("#taoTaiMaleSumWeight").val()==null || $("#taoTaiMaleAvgPrice").val() =="" || $("#taoTaiMaleAvgPrice").val()==null){
		document.getElementById("taoTaiMaleSumAcount").value=0;
	}else{
	document.getElementById("taoTaiMaleSumAcount").value = (parseInt($("#taoTaiMaleSumWeight").val()) * parseFloat($("#taoTaiMaleAvgPrice").val())).toFixed(2);
	}
}

//重置淘汰UI
function clearTaoTaiUI(){
  document.getElementById(currTabName + "SumNum").value = 0;
  document.getElementById(currTabName + "SumWeight").value = 0;
  document.getElementById(currTabName + "AvgPrice").value = 0;
  document.getElementById(currTabName + "SumAcount").value = 0;
  document.getElementById(currTabName + "SumNum2").value = 0;
  document.getElementById(currTabName + "SumWeight2").value = 0;
  document.getElementById(currTabName + "AvgPrice2").value = 0;
  document.getElementById(currTabName + "SumAcount2").value = 0;
  document.getElementById(currTabName + "MaleSumNum").value = 0;
  document.getElementById(currTabName + "MaleSumWeight").value = 0;
  document.getElementById(currTabName + "MaleAvgPrice").value = 0;
  document.getElementById(currTabName + "MaleSumAcount").value = 0;
  
}