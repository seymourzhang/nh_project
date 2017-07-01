/**
 * Created by yoven on 10/31/2016.
 */

$(document).ready(function(){
	 $('.date-picker').datepicker({
     	language: 'zh-CN',
         autoclose: true,
         todayHighlight: true
     });
	 
	 var win_h = $(window).height()-208;
	 $("#user_date_table").css("min-height",win_h);
	 $("#page-content").css("min-height",win_h);
	 $("#container").css("height",win_h-50);

	  $("#good_type").change(function() {
		  setDrugId();
		 });
	  
	  $("#drug_id_select").change(function() {
			setCorporation();
			setSpec();
			setUnit();
		});

      searchData();
      initDrugsSelect();
	});

function empty(){
	if(document.getElementById("drug_id_select").value==""){
		document.getElementById("drug_id").value = null;
	}	
}

function initDrugsSelect(){
    $.fn.typeahead.Constructor.prototype.blur = function() {
        var that = this;
        setTimeout(function () { that.hide(); }, 250);
    };

    //计划
    $('#drug_id_select').typeahead({
        source: function(query, process) {
        	var goods = getDrugsNameList('drug_id', query);
            var results = goods.map(function (item,index,input){
                return item.id+"";
			});
            if(results.length ==0){
            	document.getElementById("drug_id").value = null;
            }
            process(results);
            // return goods;
        }
        ,matcher: function (item) {
            var goods = getDrugsNameList('drug_id', item);
            var flag = false;
            for(var key in goods){
            	if(item == goods[key].id || item == goods[key].text){
            		flag = true;
				}
			}
            return flag;
		}
        ,highlighter: function (item) {
            var goods = getDrugsNameList('drug_id', item);
            var good = goods.find(function (p) {
                return p.id == item;
            });
            return good.id+"|"+good.text;
        }
        ,updater: function (item) {
            var goods = getDrugsNameList('drug_id', item);
            var good = goods.find(function (p) {
                return p.id == item;
            });
            setDrugsInfo('drug_id', good.id);
            return good.text;
        }
    });
}

function setDrugsInfo(selectName, goodId){
    var select = document.getElementById(selectName);
    var options = select.options;
    for(var key in options){
        if(goodId == options[key].value){
        	options[key].selected = true;
        }
	}
}

function getDrugsNameList(selectName, value){
	value = process( value);
	var drugsNameList = [];
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
			drugsNameList.push({id:oValue, text:oText});
		}
	}
	// goodsNameList = [{id:'1', text:'新的什么'},{id:'2', text:'新的什么2'},{id:'3', text:'的什么'}];
	return drugsNameList;
}

function setDrugId(){
	$.ajax({
		type : "post",
		url : path + "/drug/getGoods",
		data : {
			"good_type" : $("#good_type").val()
		},
		dataType: "json",
		success : function(result) {
			var list = result.obj;
			$("#drug_id option").remove();
			$("#drug_id").append("<option value=\"\">" + "全部"+ "</option>");
			for (var i = 0; i < list.length; i++) {
				$("#drug_id").append("<option value=" + list[i].good_id + ">" + list[i].good_name+ "</option>");
			}
		}
	});
}

function addDrug(){
	var grow_week_age = $("#grow_week_age").val();
	 if (parseInt(grow_week_age)!=grow_week_age)
	    {
		 layer.msg('生长周龄必须是整数，请重新输入!');
	        return;
	    }
	if (parseInt(grow_week_age)<=0 || parseInt(grow_week_age)>dage) {
		layer.msg('生长周龄必须大于0且小于'+dage+'!');
		return;
	}
	var drug_id = $("#drug_id").val(); 
	if (drug_id == "") {
		layer.msg('品名不能为空!');
		return;
	}
	var use_unit = $("#use_unit").val();
	if (parseInt(use_unit)!=use_unit)
    {
        layer.msg('使用数量必须是整数，请重新输入!');
        return;
    }
	if (use_unit<=0) {
		layer.msg('使用数量必须大于0!');
		return;
	}
	var good_type = $("#good_type").val();
	if (good_type == "") {
		layer.msg('类型不能为空!');
		return;
	}
	var use_type = $("#use_type").val();
	if (use_type == "") {
		layer.msg('用途不能为空!');
		return;
	}
//	var instruction = $("#instruction").val();
//	if (instruction == "") {
//		layer.alert('使用方法不能为空!', {
//			skin : 'layui-layer-lan',
//			closeBtn : 0,
//			shift : 4
//		// 动画类型
//		});
//		return;
//	}
	var p = {
			"farmId": $("#farmId").val(),
			"farm_name": document.getElementById("farmId").options[document.getElementById("farmId").selectedIndex].text,
			"drug_id": drug_id,
			"drug_name": document.getElementById("drug_id").options[document.getElementById("drug_id").selectedIndex].text,
			"use_unit": use_unit,
			"good_type":good_type,
			"use_type": use_type,
//			"instruction": instruction,
			"grow_week_age":$("#grow_week_age").val(),
			"spec":$("#spec").val(),
			"unit":$("#unit").val(),
			"corporation_id":$("#corporation_id").val()
	    };
	// document.getElementById("reflushText").style.display="inline";
	$.ajax({
        // async: true,
        url: path+"/drug/savePlanData",
        data: p,
        type : "POST",
        dataType: "json",
        cache: false,
        // timeout:50000,
        success: function(result) {     
                var obj = result.obj;
                var goodsName = $("#good_type").find("option:selected").text();
                tableDestroy("plan");
                initTable("plan", getTableDataColumns(goodsName), []);
                if(null != obj) {
                    var dataJosn = $.parseJSON(JSON.stringify(obj));
                    $("#planTable").bootstrapTable('load',dataJosn);
                } else{
                    initTableRow("plan", getTableEmptyRow("plan"));
                }
           document.getElementById("drug_id_select").value=null;
           document.getElementById("use_unit").value=0;
           document.getElementById("grow_week_age").value=0;
           document.getElementById("use_type").value=null;
        }
    });
	// document.getElementById("reflushText").style.display="none";
}

function deleteDrug(){
	var deleteRow;
	var deleteRow2 ="";
    deleteRow = $('#planTable').bootstrapTable('getSelections');
    if(deleteRow==null||deleteRow==''){
		 layer.alert('请选择要删除的数据!', {
				skin : 'layui-layer-lan',
				closeBtn : 0,
				shift : 4
			// 动画类型
			});
		 return;
	 }
    for(var i = 0; i < deleteRow.length; i++){
    	deleteRow2 = deleteRow2+deleteRow[i].id+";";
    }
    // document.getElementById("reflushText").style.display="inline";
    layer.confirm('是否确认删除？', {
        skin: 'layui-layer-lan'
        , closeBtn: 0
        , shift: 4 //动画类型
    }, function ok() {
	$.ajax({
        // async: true,
        url: path+"/drug/deletePlanData",
        data: {"deleteRow":deleteRow2,"farmId":$("#farmId").val(),"good_type":$("#good_type").val()},
        type : "POST",
        dataType: "json",
        cache: false,
        // timeout:50000,
        success: function(result) {     
                var obj = result.obj;
                var goodsName = $("#good_type").find("option:selected").text();
                tableDestroy("plan");
                initTable("plan", getTableDataColumns(goodsName), []);
                if(null != obj) {
                    var dataJosn = $.parseJSON(JSON.stringify(obj));
                    $("#planTable").bootstrapTable('load',dataJosn);
                } else{
                    initTableRow("plan", getTableEmptyRow("plan"));
                }
                layer.msg('删除成功!');
        }
    });
    });
	// document.getElementById("reflushText").style.display="none";
}

function searchData(){
//    var dataJosn;
    var p= {"paramTypeSelectValue":"plan2","good_type":$("#good_type").val(),"farmId":$("#farmId").val(),"ord":1};  
    	// document.getElementById("reflushText").style.display="inline";
    $.ajax({
        // async: true,
        url: path+"/drug/searchData",
        data: p,
        type : "POST",
        dataType: "json",
        cache: false,
        // timeout:50000,
        success: function(result) {     
                var obj = result.obj;
                document.getElementById("drug_id_select").value = "";
                document.getElementById("grow_week_age").value = "";
                document.getElementById("use_type").value = "";
                document.getElementById("use_unit").value = "";
                var goodsName = $("#good_type").find("option:selected").text();
                if(goodsName=="疫苗"){
        			document.getElementById("mingcheng").style.display = "";
        			document.getElementById("mingcheng2").style.display = "none";
        		}else{
        			document.getElementById("mingcheng2").style.display = "";
        			document.getElementById("mingcheng").style.display = "none";
        		}
                tableDestroy("plan");
                initTable("plan", getTableDataColumns(goodsName), []);
                if(null != obj) {
                    var dataJosn = $.parseJSON(JSON.stringify(obj));
                    $("#planTable").bootstrapTable('load',dataJosn);
                } else{
                    initTableRow("plan", getTableEmptyRow("plan"));
                }
                
                getHouseType();
        }
    });
    // document.getElementById("reflushText").style.display="none";
};

function getHouseType(){
	$.ajax({
		type : "post",
		url : path + "/drug/getHouseType",
		data : {
			"farmId" : $("#farmId").val()
		},
		dataType: "json",
		success : function(result) {
			var list = result.obj;
			if(list.length<=0){
                layer.msg('请先为该农场创建栋舍！');
				return;
			}
			houseType = list[0].house_type;
			if(houseType=="1"){
				dage = 25;
			}else{
				dage = 65;
			}
		}
	});
}

function getTableEmptyRow(tableName){
    var count = $('#' + tableName + 'Table').bootstrapTable('getData').length;
    count += -10000;
    var emptyRow ;
    var defaultValue = "";
        emptyRow = {drug_id: count,
        		    grow_week_age: defaultValue,
        		    drug_name: defaultValue,
//        		    Instruction: defaultValue,
        		    use_unit: defaultValue,
        		    use_type: defaultValue
                    };

    return emptyRow;
}

function getTableDataColumns(goodsName){
        return getPlanTableDataColumns(goodsName);
}

function getPlanTableDataColumns(goodsName){
    var dataColumns = [{
        checkbox: true,
        width: '2%'
    }, {
        field: "id",
        title: "ID",
        visible: false
    }, {
        field: "grow_week_age",
        title: "生长周龄",
        width: '3%'
    }, {
        field: "drug_name",
        title: goodsName+"名称",
        width: '25%'
    }, {
        field: "spec",
        title: "规格",
        width: '10%'
    }, {
        field: "unit",
        title: "单位",
        width: '5%'
    }, 
//    {
//        field: "Instruction",
//        title: "使用方法"
//    }, 
    {
        field: "use_unit",
        title: "使用数量",
        width: '3%'
    }, {
        field: "corporation",
        title: "供应商",
        width: '10%'
    }, {
        field: "use_type",
        title: "用途",
        width: '11%'
    }];
    return dataColumns;
}

function setCorporation() {
	$.ajax({
		type : "post",
		url : path + "/googs/getCorporation",
		data : {
			"good_id" : $("#drug_id").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#corporation_id option").remove();
			for (var i = 0; i < list.length; i++) {
				$("#corporation_id").append("<option value=" + list[i].corporation_id + ">" + list[i].corporation + "</option>");
			}
		}
	});
}

function setSpec() {
	$.ajax({
		type : "post",
		url : path + "/googs/getSpec2",
		data : {
			"good_id" : $("#drug_id").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#spec option").remove();
			for (var i = 0; i < list.length; i++) {
				$("#spec").append("<option value=" + list[i].biz_code + ">" + list[i].code_name + "</option>");
			}
		}
	});
}

function setUnit() {
	$.ajax({
		type : "post",
		url : path + "/googs/getUnit2",
		data : {
			"good_id" : $("#drug_id").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#unit option").remove();
			for (var i = 0; i < list.length; i++) {
				$("#unit").append("<option value=" + list[i].biz_code + ">" + list[i].code_name + "</option>");
			}
		}
	});
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
