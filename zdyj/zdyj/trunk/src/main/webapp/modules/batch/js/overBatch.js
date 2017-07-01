/**
 * Created by LeLe on 11/2/2016.
 */
function overBatchColumns(dataList,hunState){
	var able = true;
	if(dataList.length !=0 &&dataList[0].house_type =="2"){
		able = false;
	}
    var dataColumns;
    if(hunState == 0){
    dataColumns = [{field: "operation_date",
        title: "出栏日",
        width: '9%'
        // visible: false
    }, {field: "houseId",
        title: "出栏栋ID",
        visible: false
    }, {
        field: "house",
        title: "出栏栋舍",
        width: '10%'
    }, {
        field: "over_batch_count",
        title: "出栏数量",
        width: '11%'
    }, {
        field: "batchId",
        title: "批次序号",
        visible: false
    }, {
        field: "batchNo",
        title: "批次号",
        visible: false
    }, {
        field: "female_count",
        title: "母鸡数量",
        width: '10%'
    }, {
        field: "female_weight",
        title: "只均重（g）",
        width: '10%',
        visible: able
    }, {
        field: "male_count",
        title: "公鸡数量",
        width: '10%'
    }, {
        field: "male_weight",
        title: "只均重（g）",
        width: '10%',
        visible: able
    }, 
//    {
//        field: "weed_out_total_weight",
//        title: "淘汰鸡总重（kg）",
//        width: '8%'
//    }, {
//        field: "weed_out_total_count",
//        title: "淘汰鸡总数",
//        width: '10%'
//    }, {
//        field: "weed_out_avg_price",
//        title: "淘汰鸡均价",
//        width: '10%'
//    }, 
    {
        field: "bak",
        title: "备注",
        width: '30%'
    }];
    }else{
    	dataColumns = [{field: "operation_date",
            title: "出栏日",
            width: '15%'
            // visible: false
        }, {field: "houseId",
            title: "出栏栋ID",
            visible: false
        }, {
            field: "house",
            title: "出栏栋舍",
            width: '15%'
        }, {
            field: "female_count",
            title: "出栏数量",
            width: '15%'
        }, {
            field: "batchId",
            title: "批次序号",
            visible: false
        }, {
            field: "batchNo",
            title: "批次号",
            visible: false
        }, 
//        {
//            field: "female_count",
//            title: "数量",
//            width: '15%'
//        }, 
        {
            field: "female_weight",
            title: "只均重（g）",
            width: '15%',
            visible: able
        }, 
//        {
//            field: "weed_out_total_weight",
//            title: "淘汰鸡总重（kg）",
//            width: '8%'
//        }, {
//            field: "weed_out_total_count",
//            title: "淘汰鸡总数",
//            width: '10%'
//        }, {
//            field: "weed_out_avg_price",
//            title: "淘汰鸡均价",
//            width: '10%'
//        }, 
        {
            field: "bak",
            title: "备注",
            width: '40%'
        }];
    }
    return dataColumns;
};

//检查变量
function checkVarOverBatch(objBatch, dataList){
    objBatch.resultFlag = true;
    objBatch.resultMsg = "检测通过";
    var test;
    test = objBatch.female_count;
    if (parseInt(test)!=test)
    {
        objBatch.resultFlag = false;
        if(objBatch.is_mix ==1){
        	objBatch.resultMsg = "数量必须是整数!";
        }else{
        objBatch.resultMsg = "母鸡数必须是整数!";
        }
    }

    test = objBatch.male_count;
    if (parseInt(test)!=test)
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "公鸡数必须是整数!";
    }

    test = parseInt(objBatch.female_weight);
    if (isNaN(test))
    {
        objBatch.resultFlag = false;
        if(objBatch.is_mix ==1){
        	objBatch.resultMsg = "鸡均重必须是数字!";
        }else{
        objBatch.resultMsg = "母鸡均重必须是数字!";
        }
    }
//    if (test<=0)
//    {
//        objBatch.resultFlag = false;
//        objBatch.resultMsg = "母鸡均重必须大于0，请重新输入!";
//    }

    test = parseInt(objBatch.male_weight);
    if (isNaN(test))
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "公鸡均重必须是数字!";
    }
//    if (test<=0)
//    {
//        objBatch.resultFlag = false;
//        objBatch.resultMsg = "公鸡均重必须大于0，请重新输入!";
//    }

//    if(!getHouseFlag()){
//        test = parseInt(objBatch.weed_out_total_weight);
//        if (isNaN(test))
//        {
//            objBatch.resultFlag = false;
//            objBatch.resultMsg = "淘汰总重量必须是数字，请重新输入!";
//        }
//        test = parseInt(objBatch.weed_out_total_count);
//        if (isNaN(test))
//        {
//            objBatch.resultFlag = false;
//            objBatch.resultMsg = "淘汰总数量必须是数字，请重新输入!";
//        }
//        test = parseInt(objBatch.weed_out_avg_price);
//        if (isNaN(test))
//        {
//            objBatch.resultFlag = false;
//            objBatch.resultMsg = "淘汰均价价格必须是数字，请重新输入!";
//        }
//    }

    if(""== document.getElementById("overBatchQueryTime").value){
        objBatch.resultFlag = false;
        objBatch.resultMsg = "出栏日不能为空!";
    }

    for(var key in dataList){
        if((dataList[key].houseId == objBatch.house_code) && (dataList[key].operation_date != '')  && (dataList[key].operation_date != null)){
            objBatch.resultFlag = false;
            objBatch.resultMsg = "该栋舍已出栏，请重新选择!";
        }
    }

    layer.msg(objBatch.resultMsg);
}

////显示淘汰设置区域
//function showWeedOut(tabName, flag){
//    document.getElementById(tabName + 'SumWeight').disabled = flag;
//    document.getElementById(tabName + 'SumNum').disabled = flag;
//    document.getElementById(tabName + 'AvgPrice').disabled = flag;
//}

//重置出栏批次UI
function clearOverBatchUI(){
    document.getElementById(currTabName + "FemaleAvgWeight").value = 0;
    document.getElementById(currTabName + "FemaleAvgWeight2").value = 0;
    document.getElementById(currTabName + "MaleAvgWeight").value = 0;
    document.getElementById(currTabName + "FemaleNum").value = 0;
    document.getElementById(currTabName + "FemaleNum2").value = 0;
    document.getElementById(currTabName + "MaleNum").value = 0;
    document.getElementById(currTabName + "Remark").value = "";

//    document.getElementById(currTabName + "SumWeight").value = "";
//    document.getElementById(currTabName + "SumNum").value = "";
//    document.getElementById(currTabName + "AvgPrice").value = "";
}

//出栏批次
function saveOverBatchData(objBatch){
    objBatch.resultFlag = true;
    objBatch.resultMsg = "批次出栏成功!";
    $.ajax({
        type: "post",
        async:false,
        url: path + "/batch/saveOverBatchData",
        data: {batch_no: objBatch.batch_no
                , house_code: objBatch.house_code
                , house_name: objBatch.house_name
                , farm_id: objBatch.farm_id
                , farm_name: objBatch.farm_name
                , operation_date: objBatch.operation_date
                , male_count: objBatch.male_count
                , female_count: objBatch.female_count
                , male_weight: objBatch.male_weight
                , female_weight: objBatch.female_weight
//                , weed_out_total_weight: objBatch.weed_out_total_weight
//                , weed_out_total_count: objBatch.weed_out_total_count
//                , weed_out_avg_price: objBatch.weed_out_avg_price
                , bak: objBatch.bak
                , house_flag: getHouseFlag()
                , grow_age: $("#overBatchAge").val()
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

//获取其他变量
function getOtherVar(dataList){
    if(dataList.length > 0){
        objBatch.house_type = dataList[0].house_type;
        objBatch.batch_no = dataList[0].batchNo;
    }
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
            ,farm_id:objBatch.farm_id
  		  ,farm_name:objBatch.farm_name
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