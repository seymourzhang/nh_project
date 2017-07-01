/**
 * Created by LeLe on 11/2/2016.
 */
//创建批次表格
function createBatchColumns(hunState){
    var dataColumns;
    if(hunState == 0){
    dataColumns = [{field: "houseId",
                            title: "栋舍ID",
                            visible: false
                        }, {
                            field: "house",
                            title: "栋舍",
                            width: '5%'
                        }, {
                            field: "batchId",
                            title: "批次序号",
                            visible: false
                        }, {
                            field: "batchNo",
                            title: "批次号",
                            width: '5%'
                        }, {
                            field: "variety_id",
                            title: "品种ID",
                            visible: false
                        },  {
                            field: "variety",
                            title: "品种",
                            width: '15%'
                        }, {
                            field: "corporation_id",
                            title: "来源ID",
                            visible: false
                        },  {
                            field: "corporation",
                            title: "来源",
                            width: '15%'
                        }, {
                            field: "grow_age",
                            title: "生长日龄",
                            width: '5%'
                        }, {
                            field: "operation_date",
                            title: "进鸡日",
                            width: '10%'
                        }, {
                            field: "female_count",
                            title: "母鸡数量",
                            width: '10%'
                        }, {
                            field: "male_count",
                            title: "公鸡数量",
                            width: '10%'
                        }, {
                            field: "rate",
                            title: "公母配比",
                            width: '5%'
                        }, {
                            field: "bak",
                            title: "备注",
                            width: '20%'
                        }, {field: "status",
                            title: "状态",
                            visible: false
                        }];
    }else{
    	dataColumns = [{field: "houseId",
            title: "栋舍ID",
            visible: false
        }, {
            field: "house",
            title: "栋舍",
            width: '5%'
        }, {
            field: "batchId",
            title: "批次序号",
            visible: false
        }, {
            field: "batchNo",
            title: "批次号",
            width: '5%'
        }, {
            field: "variety_id",
            title: "品种ID",
            visible: false
        },  {
            field: "variety",
            title: "品种",
            width: '15%'
        }, {
            field: "corporation_id",
            title: "来源ID",
            visible: false
        },  {
            field: "corporation",
            title: "来源",
            width: '15%'
        }, {
            field: "grow_age",
            title: "生长日龄",
            width: '5%'
        }, {
            field: "operation_date",
            title: "进鸡日",
            width: '10%'
        }, {
            field: "female_count",
            title: "进鸡数量",
            width: '10%'
        }, {
            field: "bak",
            title: "备注",
            width: '35%'
        }, {field: "status",
            title: "状态",
            visible: false
        }];
    }
    return dataColumns;
};

//刷新创建批次表格数据
function reflushCreateBatchTable(objBatch){
    objBatch.jsonData=[{houseId:"1", house: "H1",batchId: "20161101F4", batchNo: "F4", variety_id: "1", variety: "战斗机"
        , corporation_id: "1", corporation: "微软", grow_age: "1", operation_date: "2016/11/1"
        , female_count: 10000, male_count: 100, bak: "这是条测试记录"}
                        ,{houseId:"2", house: "H2",batchId: "20161101F2", batchNo: "F2", variety_id: "1", variety: "战斗机"
                        , corporation_id: "1", corporation: "微软", grow_age: "1", operation_date: "2016/11/1"
                        , female_count: 10000, male_count: 100, bak: "这是条测试记录2"}];
};

//显示批次号下拉框
function showFaChu(tabName, faChuList){
    document.getElementById(tabName + 'No2').options.length = 0;
//    document.getElementById(tabName + 'No2').add(new Option('',''));
    if(typeof(faChuList)!="undefined" && faChuList.length !=0){
    for(var key=0;key<faChuList.length;key++){
        document.getElementById(tabName + 'No2').add(new Option(faChuList[key].child_batch_no,faChuList[key].child_batch_no));
    }
    document.getElementById("createBatchNo").value = faChuList[0].child_batch_no;
    document.getElementById("createBatchGoodSelect").value = faChuList[0].variety_id;
    document.getElementById("createBatchCorporationSelect").value = faChuList[0].corporation_id;
    }
    getCheckFemaleNum();
};

//获取发雏id与名称
function getFaChu(){
    var rt = new Array();
    $.ajax({
        type: "post",
        url: path + "/batch/getFaChuData2",
        data: {farm_id :objBatch.farm_id},
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            var rt = new Array();
            for(var key in dataList){
            	if(dataList[key].check_female_num !=undefined){
                var tmp ={child_batch_no:dataList[key].child_batch_no, child_batch_no: dataList[key].child_batch_no, variety_id: dataList[key].variety_id,
                		corporation_id: dataList[key].corporation_id};
                rt.push(tmp);
            	}
            }
            showFaChu(currTabName, rt);
        }
    });
//    return rt;
};

function getFaChu2(){
    var rt = new Array();
    $.ajax({
        type: "post",
        url: path + "/batch/getFaChuData2",
        data: {farm_id :objBatch.farm_id},
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            var rt = new Array();
            for(var key in dataList){
            	if(dataList[key].check_female_num !=undefined){
                var tmp ={child_batch_no:dataList[key].child_batch_no, variety_id: dataList[key].variety_id,
                		corporation_id: dataList[key].corporation_id};
                rt.push(tmp);
            	}
            }
            for(var key in dataList){
            	if(dataList[key].child_batch_no == $("#createBatchNo").val()){
            		document.getElementById("createBatchGoodSelect").value = dataList[key].variety_id;
            		document.getElementById("createBatchCorporationSelect").value = dataList[key].corporation_id;
//            		document.getElementById("createBatchFemaleNum").value =dataList[key].send_female_num;
//            		document.getElementById("createBatchFemaleNum2").value =dataList[key].send_female_num;
            		break;
            	}
            }
        }
    });
    return rt;
};

//获取发雏核对数量
function getCheckFemaleNum(){
	$.ajax({
        type: "post",
        url: path + "/batch/getFaChuData2",
        data: {farm_id :objBatch.farm_id,
        	   child_batch_no:$("#createBatchNo").val()},
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            for(var key in dataList){
            	objBatch.check_female_num = dataList[key].send_female_num;
            }
            getFaChu2();
        }
    });
}

//显示品种下拉框
function showVariety(tabName, varietyList){
    document.getElementById(tabName + 'GoodSelect').options.length = 0;
    for(var key in varietyList){
        document.getElementById(tabName + 'GoodSelect').add(new Option(varietyList[key].variety,varietyList[key].variety_id));
    }
//    alert($("#createBatchGoodSelect").val());
//    document.getElementById("createBatchGoodSelect").value = objBatch.fc_variety_id;
//    alert($("#createBatchGoodSelect").val());
    getCorporation(tabName);//showCorporation(tabName, getCorporation(tabName)); //显示来源下拉框
};

//获取品种id与名称
function getVariety(){
    var rt = new Array();
    $.ajax({
        type: "post",
        url: path + "/googs/getGoods",
        data: {
            good_type: 1
        },
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            var rt = new Array();
            for(var key in dataList){
                var tmp ={variety_id:dataList[key].good_id, variety: dataList[key].good_name};
                rt.push(tmp);
            }
            showVariety(currTabName, rt);
        }
    });
    return rt;
};

//品种选择值变化事件
function changeGoodSelect(){
	getCorporation(currTabName);//showCorporation(currTabName, getCorporation(currTabName));
};

//显示来源下拉框
function showCorporation(tabName, corporationList){
    document.getElementById(tabName + 'CorporationSelect').options.length = 0;
    for(var key in corporationList){
        document.getElementById(tabName + 'CorporationSelect').add(new Option(corporationList[key].corporation,corporationList[key].corporation_id));
    }
//    document.getElementById(tabName + 'CorporationSelect').value = objBatch.fc_corporation_id;
};

//获取来源id与名称
function getCorporation(tabName) {
    var rt = new Array();
    if (document.getElementById(tabName + 'GoodSelect').options.length > 0) {
        $.ajax({
            type: "post",
            url: path + "/googs/getCorporation",
            data: {
                good_type: 1,
                good_id : $("#" + tabName + "GoodSelect").val()
            },
            dataType: "json",
            success: function (result) {
                dataList = eval(result.obj);
                var rt = new Array();
                for (var key in dataList) {
                    var tmp = {corporation_id: dataList[key].corporation_id, corporation: dataList[key].corporation};
                    rt.push(tmp);
                }
                showCorporation(currTabName, rt);
            }
        });
        return rt;
    }
};

//检查变量
function checkVarCreateBatch(objBatch, dataList){
    objBatch.resultFlag = true;
    objBatch.resultMsg = "检测通过";

    if(objBatch.batch_no == '' || objBatch.batch_no == null){
        objBatch.resultFlag = false;
        objBatch.resultMsg = "必须指定批次号!";
    }

    for(var key in dataList){
        if((dataList[key].houseId == objBatch.house_code) && (dataList[key].batchId != '')  && (dataList[key].batchId != null)){
            objBatch.resultFlag = false;
            objBatch.resultMsg = "该栋舍已进鸡!";
        }
    }

    if (objBatch.operation_date == '' || objBatch.operation_date == null)
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "必须指定进鸡日!";
    }

    if (objBatch.variety_id == '' || objBatch.variety_id == null)
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "必须指定品种!";
    }

    if (objBatch.corporation_id == '' || objBatch.corporation_id == null)
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "必须指定来源!";
    }
    
    var test = objBatch.male_count;
    if (parseInt(test)!=test)
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "公鸡数必须是整数!";
    }
    if (test<0)
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "公鸡数必须大于等于0!";
    }
    
    test = objBatch.female_count;
    if (parseInt(test)!=test)
    {
        objBatch.resultFlag = false;
        if(objBatch.is_mix ==1){
        	objBatch.resultMsg = "套数必须是整数!";
        }else{
        objBatch.resultMsg = "母鸡数必须是整数!";
        }
    }
    if (test<=0)
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "母鸡数必须大于0!";
    }
//    if(objBatch.batch_no == $("#createBatchNo2").val() && (parseInt(objBatch.female_count))>objBatch.check_female_num){
//    	objBatch.resultFlag = false;
//        objBatch.resultMsg = "鸡数不能大于发雏数量！";
//    }
    
    test = objBatch.grow_age;
    if (parseInt(test)!=test)
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "生长日龄必须是整数!";
    }
    if (test<=0)
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "生长日龄必须大于0!";
    }

    for(var key in dataList){
        if(typeof dataList[key].batchNo != 'undefined' && dataList[key].batchNo != objBatch.batch_no){
            objBatch.resultFlag = false;
            objBatch.resultMsg = "同一农场内批次号必须相同!";
        }
    }


    layer.msg(objBatch.resultMsg);
}

//创建批次
function saveCreateBatchData(objBatch){
    objBatch.resultFlag = true;
    objBatch.resultMsg = "新建批次成功!";
    $.ajax({
        type: "post",
        async:false,
        url: path + "/batch/saveCreateBatchData",
        data: {batch_no: objBatch.batch_no
            , house_code: objBatch.house_code
            , house_name: objBatch.house_name
            , farm_id: objBatch.farm_id
            , farm_name: objBatch.farm_name
            , operation_date: objBatch.operation_date
            , grow_age: objBatch.grow_age
            , male_count: objBatch.male_count
            , female_count: objBatch.female_count
            , variety_id: objBatch.variety_id
            , variety: objBatch.variety
            , corporation_id: objBatch.corporation_id
            , corporation: objBatch.corporation
            , bak: objBatch.bak
            , is_mix: objBatch.is_mix},
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

//重置创建批次UI
function clearCreateBatchUI(){
//    document.getElementById(currTabName + "No").value = "";
    document.getElementById(currTabName + "GrowDay").value = 0;
    document.getElementById(currTabName + "FemaleNum2").value = 0;
    document.getElementById(currTabName + "FemaleNum").value = 0;
    document.getElementById(currTabName + "MaleNum").value = 0;
    document.getElementById(currTabName + "Remark").value = "";
}


