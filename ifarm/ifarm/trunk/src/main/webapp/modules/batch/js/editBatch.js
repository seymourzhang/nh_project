/**
 * Created by LeLe on 11/2/2016.
 */
//创建批次表格
function editBatchColumns(hunState){
    var dataColumns;
    if(hunState == 0){
    dataColumns = [{field: "operation_date",
        title: "日期",
        width: '15%'
    }, {field: "houseId",
        title: "调出栋ID",
        visible: false
    }, {
        field: "house",
        title: "调出栋舍",
        width: '5%'
    }, {
        field: "houseIdTarget",
        title: "调入至ID",
        visible: false
    }, {
        field: "farmTarget",
        title: "调入农场",
        width: '10%'
    }, {
        field: "houseTarget",
        title: "调入栋舍",
        width: '5%'
    }, {
        field: "female_count_target1",
        title: "母鸡调出数量",
        width: '10%'
    }, {
        field: "female_count_target2",
        title: "母鸡调入数量",
        width: '10%'
    }, {
        field: "male_count_target1",
        title: "公鸡调出数量",
        width: '10%'
    }, {
        field: "male_count_target2",
        title: "公鸡调入数量",
        width: '10%'
    }, {
        field: "bak",
        title: "备注",
        width: '20%'
    }];
    }else{
    	dataColumns = [{field: "operation_date",
            title: "日期",
            width: '15%'
        }, {field: "houseId",
            title: "调出栋ID",
            visible: false
        }, {
            field: "house",
            title: "调出栋舍",
            width: '5%'
        }, {
            field: "houseIdTarget",
            title: "调入至ID",
            visible: false
        }, {
            field: "farmTarget",
            title: "调入农场",
            width: '10%'
        }, {
            field: "houseTarget",
            title: "调入栋舍",
            width: '5%'
        }, {
            field: "female_count_target1",
            title: "调出数量",
            width: '10%'
        }, {
            field: "female_count_target2",
            title: "调入数量",
            width: '10%'
        }, {
            field: "bak",
            title: "备注",
            width: '40%'
        }];
    }
    return dataColumns;
};

//获取农场id与名称
function getFarmList2(tabName){
    $.ajax({
        type: "post",
        url: path + "/batch/getFarm2",
        data: {},
        dataType: "json",
        success: function (result) {
        	var list = result.obj;
			$('#'+tabName+'FarmSelectTarget option').remove();
			for (var i = 0; i < list.length; i++) {
				$('#'+tabName+'FarmSelectTarget').append("<option value=" + list[i].org_code + ">" + list[i].org_name + "</option>");
			}
			getHouseTarget();
        }
    });
};

//显示调入至下拉框
function showHouseTarget(tabName, houseList){
    document.getElementById(tabName + 'HouseSelectTarget').options.length = 0;
    for(var key in houseList){
        document.getElementById(tabName + 'HouseSelectTarget').add(new Option(houseList[key].house_name,houseList[key].house_code));
    }
}

//获取栋舍id与名称
function getHouseTarget(){
    var rt = new Array();
    $.ajax({
        type: "post",
        url: path + "/org/getOrgByPid",
        data: {
            parent_id: $('#'+currTabName+'FarmSelectTarget').val()
        },
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            var rt = new Array();
            for(var key in dataList){
                var tmp ={house_code:dataList[key].id, house_name: dataList[key].name_cn};
                rt.push(tmp);
            }
            showHouseTarget(currTabName, rt);
        }
    });
    return rt;
};

//重置创建批次UI
function clearEditBatchUI(){
    document.getElementById(currTabName + "FemaleNum").value = 0;
    document.getElementById(currTabName + "FemaleNum2").value = 0;
    document.getElementById(currTabName + "MaleNum").value = 0;
    document.getElementById(currTabName + "Remark").value = "";
}

//检查变量
function checkVarEditBatch(objBatch, dataList){
    objBatch.resultFlag = true;
    objBatch.resultMsg = "检测通过";
    var test;
    test = objBatch.male_count;
    if (parseInt(objBatch.male_count)+parseInt(objBatch.female_count)<=0)
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "公、母鸡和必须大于0!";
    }
    if(test<0 && test>=document.getElementById("currStock2").value){
        objBatch.resultFlag = false;
        objBatch.resultMsg = "公鸡数必须大于等于0并且小于存量!";
    }
    if (parseInt(test)!=test)
    {
        objBatch.resultFlag = false;
        objBatch.resultMsg = "公鸡数必须是整数!";
    }

    
    test = objBatch.female_count;
    if(test<0 && test>=document.getElementById("currStock3").value){
        objBatch.resultFlag = false;
        if(objBatch.is_mix ==1){
        	objBatch.resultMsg = "数量必须大于等于0并且小于存量!";
        }else{
        objBatch.resultMsg = "母鸡数必须大于等于0并且小于存量!";
        }
    }
    if (parseInt(test)!=test)
    {
        objBatch.resultFlag = false;
        if(objBatch.is_mix ==1){
        	objBatch.resultMsg = "套数必须是整数!";
        }else{
        objBatch.resultMsg = "母鸡数必须是整数!";
        }
    }

    if(objBatch.house_code == objBatch.house_code_target){
        objBatch.resultFlag = false;
        objBatch.resultMsg = "不能同一栋舍内调出调入!";
    }

    layer.msg(objBatch.resultMsg);
}


//修改批次
function saveEditBatchData(objBatch){
    objBatch.resultFlag = true;
    objBatch.resultMsg = "修改批次成功!";
    $.ajax({
        type: "post",
        async:false,
        url: path + "/batch/saveEditBatchData",
        data: {house_code: objBatch.house_code
            , house_name: objBatch.house_name
            , house_code_target: objBatch.house_code_target
            , house_name_target: objBatch.house_name_target
            , farm_id: objBatch.farm_id
            , inFarm_id: $("#editBatchFarmSelectTarget").val()
            , operation_date: objBatch.operation_date
            , male_count: objBatch.male_count
            , female_count: objBatch.female_count
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