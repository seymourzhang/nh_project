/**
 * Created by LeLe on 10/25/2016.
 */
var tabs = {"发雏":"faChu","发料":"faLiao","发药":"faYao", "进鸡":"createBatch", "调鸡":"editBatch", "淘汰销售":"taoTai", "出栏":"overBatch", "结算":"settle"};
var currTabName = tabs.发雏;
var showFunctionBarflag=null;
var pSize =null;
var hunState =1;

var objBatch = new Object();
function initObjBatch(){
    objBatch.batch_no = "";
    objBatch.farm_id = "";
    objBatch.display_farm_name = "";
    objBatch.farm_name = "";
    objBatch.house_code = "";
    objBatch.house_name = "";
//    objBatch.house_type = "";
    objBatch.house_code_target = "";
    objBatch.house_name_target = "";
    objBatch.operation_date = "";
    objBatch.grow_age = "";
    objBatch.male_weight = "";
    objBatch.male_count = "";
    objBatch.female_count = "";
    objBatch.female_weight = "";
    objBatch.variety_id = "";
    objBatch.variety = "";
    objBatch.corporation_id = "";
    objBatch.corporation = "";
    objBatch.bak = "";
    objBatch.jsonData = [];
    objBatch.resultFlag = false;
    objBatch.resultMsg = "";
    objBatch.house_type = 0;
    objBatch.weed_out_total_weight = "";
    objBatch.weed_out_total_count = "";
    objBatch.weed_out_avg_price = "";
    objBatch.weed_out_male_total_weight = "";
    objBatch.weed_out_male_total_count = "";
    objBatch.weed_out_male_avg_price = "";
    objBatch.feed_type = "";
    objBatch.is_mix = "";
    objBatch.company_name = "";
    objBatch.check_female_num = 0;
    objBatch.child_batch_no = "";
    objBatch.fc_variety = "";
    objBatch.fc_variety_id = 0;
    objBatch.fc_corporation = "";
    objBatch.fc_corporation_id = 0;
    objBatch.fc_factory_id = 0;
    objBatch.src_batch_no = "";
    objBatch.send_female_num = 0;
    objBatch.fc_operation_date = "";
    objBatch.fc_bak = "";
    objBatch.fc_farm_id = 0;
    objBatch.settle_id = 0;
    objBatch.gross_chicken_number=0;
    objBatch.gross_chicken_weight=0;
    objBatch.gross_male_chicken_number=0;
    objBatch.gross_male_chicken_weight=0;
    objBatch.farmName5="";
    objBatch.settle_status5=0;
    objBatch.child_batch_no5="";
};

$(document).ready(function(){ 
	objBatch.farm_id = farm_id;
    objBatch.farm_name = farm_name;
    
    var i=0;
    //发雏
    if(batchIndex.toString().indexOf("203")>-1){
    document.getElementById("faChu").style.display = "";
    if(i==0){
    document.getElementById("faChu").className = "active";
    document.getElementById("tabFaChu").className = "tab-pane active";
    currTabName = tabs.发雏;
    }
    i=i+1;
    }
    //发料
    if(batchIndex.toString().indexOf("209")>-1){
    document.getElementById("faLiao").style.display = "";
    if(i==0){
    document.getElementById("faLiao").className = "active";
    document.getElementById("tabFaLiao").className = "tab-pane active";
    currTabName = tabs.发料;
    }
    i=i+1;
    }
    //发药
    if(batchIndex.toString().indexOf("210")>-1){
    document.getElementById("faYao").style.display = "";
    if(i==0){
    document.getElementById("faYao").className = "active";
    document.getElementById("tabFaYao").className = "tab-pane active";
    currTabName = tabs.发药;
    }
    i=i+1;
    }
    //进鸡
    if(batchIndex.toString().indexOf("204")>-1){
    document.getElementById("createBatch").style.display = "";
    if(i==0){
        document.getElementById("createBatch").className = "active";
        document.getElementById("tabCreateBatch").className = "tab-pane active";
        currTabName = tabs.进鸡;
        }
    i=i+1;
    }
    //调鸡
    if(batchIndex.toString().indexOf("205")>-1){
    document.getElementById("editBatch").style.display = "";
    if(i==0){
        document.getElementById("editBatch").className = "active";
        document.getElementById("tabEditBatch").className = "tab-pane active";
        currTabName = tabs.调鸡;
        }
    i=i+1;
    }
    //淘汰
    if(batchIndex.toString().indexOf("206")>-1){
    document.getElementById("taoTai").style.display = "";
    if(i==0){
        document.getElementById("taoTai").className = "active";
        document.getElementById("tabTaoTai").className = "tab-pane active";
        currTabName = tabs.淘汰;
        }
    i=i+1;
    }
    //出栏
    if(batchIndex.toString().indexOf("207")>-1){
    document.getElementById("overBatch").style.display = "";
    if(i==0){
        document.getElementById("overBatch").className = "active";
        document.getElementById("tabOverBatch").className = "tab-pane active";
        currTabName = tabs.出栏;
        }
    i=i+1;
    }
    //结算
    if(batchIndex.toString().indexOf("208")>-1){
    document.getElementById("settle").style.display = "";
    if(i==0){
        document.getElementById("settle").className = "active";
        document.getElementById("tabSettle").className = "tab-pane active";
        currTabName = tabs.结算;
        }
    i=i+1;
    }
    document.getElementById("faChu").style.width = ((100-0.48)/i).toFixed(2)+'%';
    document.getElementById("faLiao").style.width = ((100-0.48)/i).toFixed(2)+'%';
    document.getElementById("faYao").style.width = ((100-0.48)/i).toFixed(2)+'%';
    document.getElementById("createBatch").style.width = ((100-0.48)/i).toFixed(2)+'%';
    document.getElementById("editBatch").style.width = ((100-0.48)/i).toFixed(2)+'%';
    document.getElementById("taoTai").style.width = ((100-0.48)/i).toFixed(2)+'%';
    document.getElementById("overBatch").style.width = ((100-0.48)/i).toFixed(2)+'%';
    document.getElementById("settle").style.width = ((100-0.48)/i).toFixed(2)+'%';
    
    $("#createBatchGrowDay").focus(function(){ 
        var createBatchGrowDay_txt = $(this).val(); 
        if(createBatchGrowDay_txt == this.defaultValue){ 
          $(this).val(""); 
        } 
      }); 
      $("#createBatchGrowDay").blur(function(){ 
        var createBatchGrowDay_txt = $(this).val(); 
        if (createBatchGrowDay_txt == "") { 
          $(this).val(this.defaultValue); 
        } 
      }); 
      
      reFlushData(currTabName);
    
    $("#faChuCompanySelect").change(function() {
    	document.getElementById("faChuFarmTitle").innerHTML=$("#faChuCompanySelect option:selected").text() + "发雏记录";
    	reFlushData2("faChu");
	});
    $("#faLiaoCompanySelect").change(function() {
    	document.getElementById("faLiaoFarmTitle").innerHTML=$("#faLiaoCompanySelect option:selected").text() + "发料记录";
    	reFlushData2("faLiao");
	});
    $("#faYaoCompanySelect").change(function() {
    	document.getElementById("faYaoFarmTitle").innerHTML=$("#faYaoCompanySelect option:selected").text() + "发药记录";
    	reFlushData2("faYao");
	});
    $("#settleCompanySelect").change(function() {
	document.getElementById("settleFarmTitle").innerHTML=$("#settleCompanySelect option:selected").text() + "结算记录";
	reFlushData2("settle");
    });
    $("#createBatchFarmSelect").change(function() {
    	objBatch.farm_id = document.getElementById("createBatchFarmSelect").value;
        objBatch.farm_name =$("#createBatchFarmSelect option:selected").text();
        document.getElementById("createBatchFarmTitle").innerHTML= "<div class='font s18 bold'>" + $("#createBatchFarmSelect option:selected").text() + "进鸡记录</div>";
        getFaChu();
        reFlushData2("createBatch");
	});
    $("#editBatchFarmSelect").change(function() {
    	objBatch.farm_id = document.getElementById("editBatchFarmSelect").value;
        objBatch.farm_name =$("#editBatchFarmSelect option:selected").text();
        document.getElementById("editBatchFarmTitle").innerHTML= "<div class='font s18 bold'>" + $("#editBatchFarmSelect option:selected").text() + "调鸡记录</div>";
    	reFlushData2("editBatch");
	});
    $("#taoTaiFarmSelect").change(function() {
    	objBatch.farm_id = document.getElementById("taoTaiFarmSelect").value;
        objBatch.farm_name =$("#taoTaiFarmSelect option:selected").text();
        document.getElementById("taoTaiFarmTitle").innerHTML= "<div class='font s18 bold'>" + $("#taoTaiFarmSelect option:selected").text() + "淘汰销售记录</div>";
    	reFlushData2("taoTai");
	});
    $("#overBatchFarmSelect").change(function() {
    	objBatch.farm_id = document.getElementById("overBatchFarmSelect").value;
        objBatch.farm_name =$("#overBatchFarmSelect option:selected").text();
        document.getElementById("overBatchFarmTitle").innerHTML= "<div class='font s18 bold'>" + $("#overBatchFarmSelect option:selected").text() + "出栏记录</div>";
    	reFlushData2("overBatch");
	});
    
});

//切换标签页事件处理
$(function(){
    $('a[data-toggle="tab"]').on('shown', function (e) {
        currTabName = tabs[$(e.target).text()];
        reFlushData(currTabName);
    });
});

//显示界面
function showTab(tabName, dataList,pd,dataList2){
    initObjBatch(); //初始化对象
    showFarm(tabName, pd.farm_name); //显示农场名称
    if(tabName !=tabs.发雏){
    objBatch.farm_id = pd.farm_id;
    objBatch.farm_name = pd.farm_name;
    }
    document.getElementById("maoji").style.display = "none";
    document.getElementById("maoji2").style.display = "none";
    document.getElementById("maoji3").style.display = "none";
    document.getElementById("maoji4").style.display = "none";
//    if(tabName !=tabs.发雏){
    if(dataList2.length !=0){
    	hunState = dataList2[0].is_mix;
    	document.getElementById("is_mix1").disabled = true;
    	document.getElementById("is_mix2").disabled = true;
    	if(dataList2[0].is_mix == document.getElementById("is_mix1").value){
    		document.getElementById("is_mix1").checked = true;
    		document.getElementById("is_mix2").checked = false;
    		document.getElementById("muji").style.display = "none";
    		document.getElementById("createBatchGoodSelect").style.width = "220px";
    		document.getElementById("muji11").style.display = "none";
    		document.getElementById("createBatchRemark").style.width = "207px";
    		document.getElementById("gongji").style.display = "none";
    		document.getElementById("jishu").style.display = "inline";
//    		document.getElementById("jishu10").style.display = "inline";
//    		document.getElementById("jishu11").style.display = "inline";
//    		document.getElementById("jishu12").style.display = "inline";
    	}else{
    		document.getElementById("is_mix1").checked = false;
    		document.getElementById("is_mix2").checked = true;
    		document.getElementById("muji").style.display = "inline";
    		document.getElementById("createBatchGoodSelect").style.width = "233px";
    		document.getElementById("muji11").style.display = "inline";
    		document.getElementById("createBatchRemark").style.width = "220px";
    		document.getElementById("gongji").style.display = "inline";
    		document.getElementById("jishu").style.display = "none";
//    		document.getElementById("jishu10").style.display = "none";
//    		document.getElementById("jishu11").style.display = "none";
//    		document.getElementById("jishu12").style.display = "none";
    	}
    }else{
    	document.getElementById("is_mix1").disabled = false;
    	document.getElementById("is_mix2").disabled = false;
    }
//    }

    if(tabName == tabs.进鸡){
    	getFarmList3();
    	showHouse(tabName, getHouse(pd.farm_id)); //显示栋舍下拉框
    	initDatepicker(tabName); //初始化日期控件
    	objBatch.feed_type = pd.feed_type;
        showVariety(tabName, getVariety()); //显示品种下拉框
        showFaChu(tabName, getFaChu()); //显示发雏批次号下拉框
        
    }
    if(tabName == tabs.调鸡){
    	getFarmList3();
    	getFarmList2(tabName); //显示农场下拉框
    	showHouse(tabName, getHouse(pd.farm_id)); //显示栋舍下拉框
    	initDatepicker(tabName); //初始化日期控件
    	if(hunState ==1){
    		document.getElementById("muji2").style.display = "none";
    		document.getElementById("gongji2").style.display = "none";
    		document.getElementById("jishu2").style.display = "inline";
    	}else{
    		document.getElementById("muji2").style.display = "inline";
    		document.getElementById("gongji2").style.display = "inline";
    		document.getElementById("jishu2").style.display = "none";
    	}
//        showHouseTarget(tabName, getHouseTarget(pd.farm_id)); //显示调入至下拉框
    }
    if(tabName == tabs.淘汰销售){
    	getFarmList3();
    	showHouse(tabName, getHouse(pd.farm_id)); //显示栋舍下拉框
    	initDatepicker(tabName); //初始化日期控件
    	if(hunState ==1){
    		document.getElementById("tt3").style.display = "none";
    		document.getElementById("tt4").style.display = "none";
    		document.getElementById("tt2").style.display = "inline";
    	}else{
    		document.getElementById("tt3").style.display = "inline";
    		document.getElementById("tt4").style.display = "inline";
    		document.getElementById("tt2").style.display = "none";
    	}
    }
    if(tabName == tabs.出栏){
    	getFarmList3();
    	showHouse(tabName, getHouse(pd.farm_id)); //显示栋舍下拉框
    	initDatepicker(tabName); //初始化日期控件
    	if(hunState ==1){
    		document.getElementById("muji3").style.display = "none";
    		document.getElementById("gongji3").style.display = "none";
    		document.getElementById("jishu3").style.display = "inline";
    	}else{
    		document.getElementById("muji3").style.display = "inline";
    		document.getElementById("gongji3").style.display = "inline";
    		document.getElementById("jishu3").style.display = "none";
    	}
        getOtherVar(dataList);
        // showWeedOut(tabName, getHouseFlag());
    }
    createTable(tabName,dataList); //创建表格
    reflushTable(tabName, dataList); //刷新表格数据
};

//创建表格
function createTable(tabName,dataList){
	tableDestroy(tabName);
    initTable(tabName, createTableColumns(tabName,dataList), objBatch.jsonData);
};

//创建表格列
function createTableColumns(tabName,dataList){
    var columns;
    if(tabName == tabs.发雏){
        columns = faChuColumns();
    }
    if(tabName == tabs.发料){
        columns = faLiaoColumns();
    }
    if(tabName == tabs.发药){
        columns = faLiaoColumns();
    }
    if(tabName == tabs.进鸡){
        columns = createBatchColumns(hunState);
    }
    if(tabName == tabs.调鸡){
        columns = editBatchColumns(hunState);
    }
    if(tabName == tabs.淘汰销售){
        columns = taoTaiColumns();
    }
    if(tabName == tabs.出栏){
        columns = overBatchColumns(dataList,hunState);
    }
    if(tabName == tabs.结算){
        columns = settleColumns();
    }
    return columns;
};

//刷新表格数据
function reflushTable(tabName, dataList){
    objBatch.jsonData = dataList;
    loadTableData(tabName, objBatch.jsonData);
};

//初始化日期控件
function initDatepicker(tabName){
    $("." + tabName + "DatePicker").datepicker({
        language : 'zh-CN',
        autoclose : true,
        todayHighlight : true
    });
    $("." + tabName + "DatePicker").datepicker('setDate', new Date());
};

//创建批次
function saveData(){
    if(checkRights()){
        if(currTabName == tabs.进鸡){
            objBatch.batch_no = document.getElementById(currTabName + "No").value;
            objBatch.variety_id = document.getElementById(currTabName + "GoodSelect").value;
            objBatch.variety = $("#" + currTabName + "GoodSelect option:selected").text();
            // $("#goodSelect").val(); //获取选中记录的value值
            // $("#goodSelect option:selected").text(); //获取选中记录的text值
            objBatch.corporation_id = document.getElementById(currTabName + "CorporationSelect").value;
            objBatch.corporation = $("#" + currTabName + "CorporationSelect option:selected").text();
            objBatch.grow_age = document.getElementById(currTabName + "GrowDay").value;
            objBatch.operation_date = document.getElementById(currTabName + "QueryTime").value;
            objBatch.house_code = document.getElementById(currTabName + "HouseSelect").value;
            objBatch.house_name = $("#" + currTabName + "HouseSelect option:selected").text();
            objBatch.is_mix = hunState;
            if(hunState ==0){
            objBatch.female_count = document.getElementById(currTabName + "FemaleNum").value;
            objBatch.male_count = document.getElementById(currTabName + "MaleNum").value;
            }else{
            	objBatch.female_count = document.getElementById(currTabName + "FemaleNum2").value;
            	objBatch.male_count = 0;
            }
            objBatch.bak = document.getElementById(currTabName + "Remark").value;
            checkVarCreateBatch(objBatch, $('#' + currTabName + 'Table').bootstrapTable('getData'));
            if(objBatch.resultFlag){
                checkConfirm(objBatch);
            }
        }
        if(currTabName == tabs.调鸡){
            objBatch.operation_date = document.getElementById(currTabName + "QueryTime").value;
            objBatch.house_code = document.getElementById(currTabName + "HouseSelect").value;
            objBatch.house_name = $("#" + currTabName + "HouseSelect option:selected").text();
            objBatch.house_code_target = document.getElementById(currTabName + "HouseSelectTarget").value;
            objBatch.house_name_target = $("#" + currTabName + "HouseSelectTarget option:selected").text();
            objBatch.bak = document.getElementById(currTabName + "Remark").value;
            objBatch.is_mix = hunState;
            if(hunState ==0){
            	objBatch.female_count = document.getElementById(currTabName + "FemaleNum").value;
                objBatch.male_count = document.getElementById(currTabName + "MaleNum").value;
            }else{
            	objBatch.female_count = document.getElementById(currTabName + "FemaleNum2").value;
                objBatch.male_count = 0;
            }
            checkVarEditBatch(objBatch, $('#' + currTabName + 'Table').bootstrapTable('getData'));
            if(objBatch.resultFlag){
                checkConfirm(objBatch);
            }
        }
        if(currTabName == tabs.淘汰销售){
            objBatch.operation_date = document.getElementById(currTabName + "QueryTime").value;
            objBatch.house_code = document.getElementById(currTabName + "HouseSelect").value;
            objBatch.house_name = $("#" + currTabName + "HouseSelect option:selected").text();
            if(hunState ==0){
            objBatch.weed_out_total_weight = document.getElementById(currTabName + "SumWeight2").value;
            objBatch.weed_out_total_count = document.getElementById(currTabName + "SumNum2").value;
            objBatch.weed_out_avg_price = document.getElementById(currTabName + "AvgPrice2").value;
            objBatch.weed_out_male_total_weight = document.getElementById(currTabName + "MaleSumWeight").value;
            objBatch.weed_out_male_total_count = document.getElementById(currTabName + "MaleSumNum").value;
            objBatch.weed_out_male_avg_price = document.getElementById(currTabName + "MaleAvgPrice").value;
            }else{
            	objBatch.weed_out_total_weight = document.getElementById(currTabName + "SumWeight").value;
                objBatch.weed_out_total_count = document.getElementById(currTabName + "SumNum").value;
                objBatch.weed_out_avg_price = document.getElementById(currTabName + "AvgPrice").value;
                objBatch.weed_out_male_total_weight = 0;
                objBatch.weed_out_male_total_count = 0;
                objBatch.weed_out_male_avg_price = 0;
            }
            objBatch.is_mix = hunState;
            getBatchNo(currTabName);
            checkVarTaoTai(objBatch);
            if(objBatch.resultFlag){
                checkConfirm(objBatch);
            }
        }
        if(currTabName == tabs.出栏){
            objBatch.operation_date = document.getElementById(currTabName + "QueryTime").value;
            objBatch.house_code = document.getElementById(currTabName + "HouseSelect").value;
            objBatch.house_name = $("#" + currTabName + "HouseSelect option:selected").text();
            objBatch.bak = document.getElementById(currTabName + "Remark").value;
//            objBatch.weed_out_total_weight = document.getElementById(currTabName + "SumWeight").value;
//            objBatch.weed_out_total_count = document.getElementById(currTabName + "SumNum").value;
//            objBatch.weed_out_avg_price = document.getElementById(currTabName + "AvgPrice").value;
            objBatch.is_mix = hunState;
            if(hunState ==0){
            	objBatch.female_count = document.getElementById(currTabName + "FemaleNum").value;
                objBatch.male_count = document.getElementById(currTabName + "MaleNum").value;
                objBatch.female_weight = document.getElementById(currTabName + "FemaleAvgWeight").value;
                objBatch.male_weight = document.getElementById(currTabName + "MaleAvgWeight").value;
            }else{
            	objBatch.female_count = document.getElementById(currTabName + "FemaleNum2").value;
                objBatch.male_count = 0;
                objBatch.female_weight = document.getElementById(currTabName + "FemaleAvgWeight2").value;
                objBatch.male_weight = 0;
            }
            getBatchNo(currTabName);
            checkVarOverBatch(objBatch, $('#' + currTabName + 'Table').bootstrapTable('getData'));
            if(objBatch.resultFlag){
                checkConfirm(objBatch);
            }
        }
    }
};

//显示农场名称
function showFarm(tabName, farmName){
    objBatch.farm_name = farmName;
    if(currTabName == tabs.进鸡){
        document.getElementById(tabName + "FarmTitle").innerHTML= "<div class='font s18 bold'>" + farmName + "进鸡记录</div>";
	}else if(currTabName == tabs.调鸡){
		document.getElementById(tabName + "FarmTitle").innerHTML= "<div class='font s18 bold'>" + farmName + "调鸡记录</div>";
	}else if(currTabName == tabs.淘汰销售){
		document.getElementById(tabName + "FarmTitle").innerHTML= "<div class='font s18 bold'>" + farmName + "淘汰销售记录</div>";
	}else if(currTabName == tabs.出栏){
		document.getElementById(tabName + "FarmTitle").innerHTML= "<div class='font s18 bold'>" + farmName + "出栏记录</div>";
	}
};

//获取农场id与名称
function getFarm(dataList){
    if(dataList.length > 0){
        objBatch.farm_id = dataList[0].farmId;
        objBatch.farm_name = dataList[0].farmName;
        objBatch.display_farm_name =  "<font size='4' ><B>" + objBatch.farm_name +"</B></font>";
    }
    return objBatch.display_farm_name;
}

//显示栋舍下拉框
function showHouse(tabName, houseList){
    document.getElementById(tabName + 'HouseSelect').options.length = 0;
    for(var key in houseList){
        document.getElementById(tabName + 'HouseSelect').add(new Option(houseList[key].house_name,houseList[key].house_code));
    }
//    if(tabName != "taoTai"){
    getCount();
//    }
};

//获取栋舍id与名称
function getHouse(farm_id){
    var rt = new Array();
    $.ajax({
        type: "post",
        url: path + "/org/getOrgByPid",
        data: {
            parent_id: farm_id
        },
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            var rt = new Array();
            var houseType = "0";
            for(var key in dataList){
                var tmp ={house_code:dataList[key].id, house_name: dataList[key].name_cn, house_type:dataList[key].house_type };
                houseType = dataList[key].house_type;
                rt.push(tmp);
            }
            document.getElementById("house_length").value = rt.length;
            showHouse(currTabName, rt);

            if(currTabName == tabs.出栏 && houseType =="2"){
                // document.getElementById("overBatchFemaleAvgWeight").disabled=true;
                // document.getElementById("overBatchMaleAvgWeight").disabled=true;
//                document.getElementById("cl3").style.display = "block";
//                document.getElementById("cl4").style.display = "block";
//                document.getElementById("overBatchBtnSaveY").style.display = "none";
//                showWeedOut(currTabName,false);
            }
            // this.objBatch.house_type = houseType;
        }
    });
    return rt;
};

//检查权限
function checkRights(){
    if(isRead==0){
        layer.alert('无权限，请联系管理员!', {
            skin: 'layui-layer-lan'
            ,closeBtn: 0
            ,shift: 4 //动画类型
        });
        return false;
    } else {
        return true;
    };
};

//刷新数据
function reFlushData(tabName){
    var dataList = [];
    var param = {};
    var url = "/batch/getFaChuData";
    if(tabName == tabs.发雏){
      param = {company_id:$("#"+tabName+"CompanySelect").val()};	
      url = "/batch/getFaChuData";
  }
    if(tabName == tabs.发料){
        param = {company_id:$("#"+tabName+"CompanySelect").val()};	
        url = "/batch/getFaLiaoData";
    }
    if(tabName == tabs.发药){
        param = {company_id:$("#"+tabName+"CompanySelect").val()};	
        url = "/batch/getFaYaoData";
    }
    if(tabName == tabs.进鸡){
        clearCreateBatchUI(tabName);
        param = {farm_id:objBatch.farm_id
        		,farm_name:objBatch.farm_name};	
        url = "/batch/getCreateBatchData";
    }
    if(tabName == tabs.调鸡){
        clearEditBatchUI(tabName);
        param = {farm_id:objBatch.farm_id
        		,farm_name:objBatch.farm_name};
        url = "/batch/getEditBatchData";
    }
    if(tabName == tabs.淘汰销售){
    	clearTaoTaiUI(tabName);
    	param = {farm_id:objBatch.farm_id
    			,farm_name:objBatch.farm_name};
      url = "/batch/getTaoTaiData";
  }
    if(tabName == tabs.出栏){
        clearOverBatchUI(tabName);
        param = {farm_id:objBatch.farm_id
        		,farm_name:objBatch.farm_name};
        url = "/batch/getOverBatchData";
    }
    if(tabName == tabs.结算){
    	param = {company_id:$("#"+tabName+"CompanySelect").val()
    			,farm_id:objBatch.farm_id
        		  ,farm_name:objBatch.farm_name};	
        url = "/batch/getSettleData";
    }
    $.ajax({
        type: "post",
        url: path + url,
        data: param,
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            pd = eval(result.obj1);
            var dataList2 = eval(result.obj2);
            hiddenC(dataList2.length,$("#house_length").val(),tabName);
            showTab(tabName, dataList,pd,dataList2);
        }
    });
}

//刷新数据
function reFlushData2(tabName){
    var dataList = [];
    var param = {};
    var url = "/batch/getFaChuData";
    if(tabName == tabs.发雏){
      param = {company_id:$("#"+tabName+"CompanySelect").val()
    		  ,farm_id:objBatch.farm_id
    		  ,farm_name:objBatch.farm_name};	
      url = "/batch/getFaChuData";
  }
    if(tabName == tabs.发料){
        param = {company_id:$("#"+tabName+"CompanySelect").val()
      		  ,farm_id:objBatch.farm_id
      		  ,farm_name:objBatch.farm_name};	
        url = "/batch/getFaLiaoData";
    }
    if(tabName == tabs.发药){
        param = {company_id:$("#"+tabName+"CompanySelect").val()
      		  ,farm_id:objBatch.farm_id
      		  ,farm_name:objBatch.farm_name};	
        url = "/batch/getFaYaoData";
    }
    if(tabName == tabs.进鸡){
        clearCreateBatchUI(tabName);
        param = {farm_id:$("#"+tabName+"FarmSelect").val()
        		,farm_name:$("#" + tabName + "FarmSelect option:selected").text()};	
        url = "/batch/getCreateBatchData";
    }
    if(tabName == tabs.调鸡){
        clearEditBatchUI(tabName);
        param = {farm_id:$("#"+tabName+"FarmSelect").val()
        		,farm_name:$("#" + tabName + "FarmSelect option:selected").text()};
        url = "/batch/getEditBatchData";
    }
    if(tabName == tabs.淘汰销售){
    	clearTaoTaiUI(tabName);
    	param = {farm_id:$("#"+tabName+"FarmSelect").val()
    			,farm_name:$("#" + tabName + "FarmSelect option:selected").text()};
      url = "/batch/getTaoTaiData";
  }
    if(tabName == tabs.出栏){
        clearOverBatchUI(tabName);
        param = {farm_id:$("#"+tabName+"FarmSelect").val()
        		,farm_name:$("#" + tabName + "FarmSelect option:selected").text()};
        url = "/batch/getOverBatchData";
    }
    if(tabName == tabs.结算){
    	param = {company_id:$("#"+tabName+"CompanySelect").val()
    			,farm_id:objBatch.farm_id
      		  ,farm_name:objBatch.farm_name};	
        url = "/batch/getSettleData";
    }
    $.ajax({
        type: "post",
        url: path + url,
        data: param,
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            pd = eval(result.obj1);
            var dataList2 = eval(result.obj2);
            if(dataList2.length !=0){
            	hunState = dataList2[0].is_mix;
            	document.getElementById("is_mix1").disabled = true;
            	document.getElementById("is_mix2").disabled = true;
            	if(dataList2[0].is_mix == document.getElementById("is_mix1").value){
            		document.getElementById("is_mix1").checked = true;
            		document.getElementById("is_mix2").checked = false;
            		document.getElementById("muji").style.display = "none";
            		document.getElementById("gongji").style.display = "none";
            		document.getElementById("jishu").style.display = "inline";
            	}else{
            		document.getElementById("is_mix1").checked = false;
            		document.getElementById("is_mix2").checked = true;
            		document.getElementById("muji").style.display = "inline";
            		document.getElementById("gongji").style.display = "inline";
            		document.getElementById("jishu").style.display = "none";
            	}
            }else{
            	document.getElementById("is_mix1").disabled = false;
            	document.getElementById("is_mix2").disabled = false;
            }
            setTimeout("getHouse($('#"+tabName+"FarmSelect').val());",500);
            setTimeout("hiddenC("+dataList2.length+","+$('#house_length').val()+",'"+tabName+"');",1000);
            createTable(tabName,dataList); //创建表格
            reflushTable(tabName, dataList); //刷新表格数据
        }
    });
}

function hiddenC(length,house_length, currTabName){
	house_length = $('#house_length').val();
    if(currTabName == tabs.进鸡 || currTabName == tabs.发雏 || currTabName == tabs.出栏){
        if(length==house_length){
            document.getElementById("jj1").style.display="none";
            document.getElementById("jj2").style.display="none";
            document.getElementById("jj3").style.display="none";
            document.getElementById("toolbarCreateBatch").style.display="none";
//            document.getElementById("toolbarEditBatch").style.display="block";
//            document.getElementById("toolbarTaoTai").style.display="block";
            document.getElementById("toolbarOverBatch").style.display="block";
            // document.getElementById("jj4").style.display="none";
            //    	document.getElementById("tj1").style.display="none";
            //    	document.getElementById("tj2").style.display="none";
            //    	document.getElementById("tj3").style.display="none";
            //    	document.getElementById("cl1").style.display="none";
            //    	document.getElementById("cl2").style.display="none";
            //    	document.getElementById("cl3").style.display="none";
            //    	document.getElementById("cl4").style.display="none";
            //    	document.getElementById("cl5").style.display="none";
        }else{
            document.getElementById("jj1").style.display="inline";
            document.getElementById("jj2").style.display="inline";
            document.getElementById("jj3").style.display="inline";
            document.getElementById("toolbarCreateBatch").style.display="block";
            if(length==0){
//                document.getElementById("toolbarEditBatch").style.display="none";
//                document.getElementById("toolbarTaoTai").style.display="none";
                document.getElementById("toolbarOverBatch").style.display="none";
            } else{
//                document.getElementById("toolbarEditBatch").style.display="block";
//                document.getElementById("toolbarTaoTai").style.display="block";
                document.getElementById("toolbarOverBatch").style.display="block";
            }

            // document.getElementById("jj4").style.display="inline";
            //    	document.getElementById("tj1").style.display="inline";
            //    	document.getElementById("tj2").style.display="inline";
            //    	document.getElementById("tj3").style.display="inline";
            //    	document.getElementById("cl1").style.display="inline";
            //    	document.getElementById("cl2").style.display="inline";
            //    	document.getElementById("cl3").style.display="inline";
            //    	document.getElementById("cl4").style.display="inline";
            //    	document.getElementById("cl5").style.display="inline";
        }
    }
    
    if(currTabName == tabs.调鸡 || currTabName == tabs.淘汰销售){
    	if(length>=1){
    		document.getElementById("toolbarEditBatch").style.display="block";
    		document.getElementById("toolbarTaoTai").style.display="block";
    	}else{
    		document.getElementById("toolbarEditBatch").style.display="none";
    		document.getElementById("toolbarTaoTai").style.display="none";
    	}
    }

    var createBatchCount = $("#createBatchTable").bootstrapTable("getData").length;


//    if(currTabName == tabs.出栏){
//        if(length==house_length || createBatchCount==0){
//            document.getElementById("jj1").style.display="inline";
//            document.getElementById("jj2").style.display="inline";
//            document.getElementById("jj3").style.display="inline";
//            document.getElementById("toolbarCreateBatch").style.display="block";
//            document.getElementById("toolbarEditBatch").style.display="none";
//            document.getElementById("toolbarTaoTai").style.display="none";
//            document.getElementById("toolbarOverBatch").style.display="none";
//        }else{
//            document.getElementById("jj1").style.display="none";
//            document.getElementById("jj2").style.display="none";
//            document.getElementById("jj3").style.display="none";
//            document.getElementById("toolbarCreateBatch").style.display="none";
//            document.getElementById("toolbarEditBatch").style.display="block";
//            document.getElementById("toolbarTaoTai").style.display="block";
//            document.getElementById("toolbarOverBatch").style.display="block";
//
//        }
//    }

}

//检查是否确认
function checkConfirm(objBatch){
	var tishi = '是否确认？';
	if(currTabName == tabs.出栏){
		tishi = '出栏后，日报将无法修改，请确认无误后出栏！';
	}
    layer.confirm(tishi, {
        skin: 'layui-layer-lan'
        , closeBtn: 0
        , shift: 4 //动画类型
    }, function ok() {
        if(currTabName == tabs.进鸡){
            objBatch = saveCreateBatchData(objBatch);
        }
        if(currTabName == tabs.调鸡){
            objBatch = saveEditBatchData(objBatch);
        }
        if(currTabName == tabs.淘汰销售){
            objBatch = saveTaoTaiData(objBatch);
        }
        if(currTabName == tabs.出栏){
            objBatch = saveOverBatchData(objBatch);
        }
        if(objBatch.resultFlag){
            reFlushData(currTabName);
        }
        layer.msg(objBatch.resultMsg);
    });
};

//获取指定栋舍的当前库存量
function getCount(){	
	$.ajax({
        type: "post",
        url: path + "/batch/getCount",
        data: {house_code:document.getElementById(currTabName + "HouseSelect").value,farm_id:objBatch.farm_id},
        dataType: "json",
        success: function (result) {
            dataList = eval(result.obj);
            if(currTabName == tabs.调鸡){
            	if(dataList.length==0){
                	document.getElementById("currStock1").value = 0;
                    document.getElementById("currStock2").value = 0;
                    document.getElementById("currStock3").value = 0;
                }else{
                document.getElementById("currStock1").value = dataList[0].female_count;
                document.getElementById("currStock2").value = dataList[0].male_count;
                document.getElementById("currStock3").value = dataList[0].female_count;
                }
            }
            if(currTabName == tabs.淘汰销售){
            	if(dataList.length==0){
                	document.getElementById("currStock4").value = 0;
                	document.getElementById("currStock5").value = 0;
                	document.getElementById("currStock6").value = 0;
                }else{
                document.getElementById("currStock4").value = dataList[0].female_count;
                document.getElementById("currStock5").value = dataList[0].female_count;
                document.getElementById("currStock6").value = dataList[0].male_count;
                }
            }
            if(currTabName == tabs.出栏){
            	if(dataList.length==0){
                	document.getElementById("overBatchFemaleNum").value = 0;
                	document.getElementById("overBatchFemaleNum2").value = 0;
                    document.getElementById("overBatchMaleNum").value = 0;
                }else{
                document.getElementById("overBatchFemaleNum").value = dataList[0].female_count;
                document.getElementById("overBatchFemaleNum2").value = dataList[0].female_count;
                document.getElementById("overBatchMaleNum").value = dataList[0].male_count;
                }
            	getOverBatchAge();
            }
            
        }
    });
}

//获取指定栋舍的出栏日龄
function getOverBatchAge(){
    var houseCode = document.getElementById(currTabName + "HouseSelect").value;
    var operationDate = document.getElementById(currTabName + "QueryTime").value;
    if(null != houseCode && "" != houseCode && null != operationDate && "" != operationDate){
        $.ajax({
            type: "post",
            url: path + "/batch/getOverBatchAge",
            data: {farm_id:document.getElementById(currTabName + "FarmSelect").value,batch_no:objBatch.batch_no,
            	house_code:document.getElementById(currTabName + "HouseSelect").value,operation_date:document.getElementById(currTabName + "QueryTime").value},
            dataType: "json",
            success: function (result) {
                var dataList = eval(result.obj);
                var dataList2 = eval(result.obj2);
                var currDate = getNowFormatDate();
                if(document.getElementById(currTabName + "QueryTime").value >currDate){
                    document.getElementById("overBatchQueryTime").value = currDate;
                	layer.msg("出栏日不可选大于今天的日期！");
                	return;
                }
                if(dataList2.length ==0){
                	document.getElementById("overBatchAge").value = 0;
                	return;
                }
                if(dataList.length==0){
//                    document.getElementById("overBatchAge").value = "";
//                     document.getElementById("overBatchQueryTime").value = currDate;
                    // var overBatchFemaleNum = $("#overBatchFemaleNum").val();
                    // if(parseInt(overBatchFemaleNum)!=0){
                    // getOverBatchAge();
                    // document.getElementById("overBatchQueryTime").value = "";
//                        layer.msg("出栏日设置过大，请重新选择出栏日!");
//                        return;
                    if(document.getElementById(currTabName + "QueryTime").value < dataList2[0].operation_date){
                    	layer.msg("出栏日不能小于进鸡日，请重新选择出栏日!");
                        return;
                    }else{
                    	if(objBatch.house_type ==3){
                    		document.getElementById("overBatchAge").value = 80;
                    	}else if(objBatch.house_type ==2){
                    		document.getElementById("overBatchAge").value = 455;
                    	}else{
                    		document.getElementById("overBatchAge").value = 175;
                    	}
                    	
                    }
                }else{
                    if(dataList.length!=0){
                        document.getElementById("overBatchAge").value = dataList[0].age;
                    }
                }
            }
        });
    }
}

//获取总金额
function getOverBatchAvgPriceSum(){	
	var num1,num2;
	num1 = $("#overBatchSumWeight").val();
	num2 = $("#overBatchAvgPrice").val();
	document.getElementById("overBatchAvgPriceSum").value = (num1 * num2).toFixed(2);
}

//获取当前日期
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
//    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
//            + " " + date.getHours() + seperator2 + date.getMinutes()
//            + seperator2 + date.getSeconds();
    return currentdate;
}

//选择是否混养
function panDuan(s){
	if(s==1){
		document.getElementById("muji").style.display = "none";
		document.getElementById("createBatchGoodSelect").style.width = "220px";
		document.getElementById("muji11").style.display = "none";
		document.getElementById("createBatchRemark").style.width = "207px";
		document.getElementById("gongji").style.display = "none";
		document.getElementById("jishu").style.display = "inline";
//		document.getElementById("jishu10").style.display = "inline";
//		document.getElementById("jishu11").style.display = "inline";
//		document.getElementById("jishu12").style.display = "inline";
		hunState = 1;
	}else{
		document.getElementById("muji").style.display = "inline";
		document.getElementById("createBatchGoodSelect").style.width = "233px";
		document.getElementById("muji11").style.display = "inline";
		document.getElementById("createBatchRemark").style.width = "220px";
		document.getElementById("gongji").style.display = "inline";
		document.getElementById("jishu").style.display = "none";
//		document.getElementById("jishu10").style.display = "none";
//		document.getElementById("jishu11").style.display = "none";
//		document.getElementById("jishu12").style.display = "none";
		hunState = 0;
	}
	reFlushData(currTabName);
}

//获取农场id与名称
function getFarmList3(){
    $.ajax({
        type: "post",
        url: path + "/batch/getFarm2",
        data: {},
        dataType: "json",
        success: function (result) {
        	var list = result.obj;
			$("#"+currTabName+"FarmSelect"+" option").remove();
			for (var i = 0; i < list.length; i++) {
				$("#"+currTabName+"FarmSelect").append("<option value=" + list[i].org_code + ">" + list[i].org_name + "</option>");
			}
			if(objBatch.farm_id !=0){
			    document.getElementById(currTabName+"FarmSelect").value = objBatch.farm_id;
			    }
//			showFarm(currTabName, $("#createBatchFarmSelect option:selected").text()); //显示农场名称
        }
    });
};
