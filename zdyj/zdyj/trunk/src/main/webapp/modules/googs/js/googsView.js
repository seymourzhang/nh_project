var objGoods = new Object();


$(document).ready(function() {
	initObjGoods();

	$(".date-picker").datepicker({
		language : 'zh-CN',
		autoclose : true,
        // defaultDate : new Date(),
		todayHighlight : true
	});
    $(".date-picker").datepicker('setDate', new Date());//

	$(".date-picker1").datepicker({
		language : 'zh-CN',
		autoclose : true,
		defaultDate : new Date(),
		todayHighlight : true
	});
	$(".date-picker1").datepicker('setDate', new Date());//

	$("#good_type").change(function() {
		setGoodName();
	});

	/** *****耗用*************************************************** */

	$("#good_type_out").change(function() {
		setGoodNameOut();
	});

	/** *****耗用*************************************************** */

	$("#goods_id_select").change(function() {
//		setGoodId();
		setCorporation();
		setFactory();
		setSpec();
		setUnit();
	});
	/****************************/
	$("#good_type_stock").change(function() {
		setGoodNameStock();
	});
	$("#good_id_stock_select").change(function() {
		setCorporationStock();
		setFactoryStock();
		setSpecStock();
		setUnitStock();
	});
	setGoodName();
	setGoodNameOut();
	setGoodNameStock();
	
	
//	setCorporationStock();
//	setFactoryStock();
//	setSpec();
//	setUnit();
//	setSpecStock();
//	setUnitStock();
	
	var dataJosn;
	initTable("inStock", getInStockTableColumns(), dataJosn);
	initTable("outStock", getOutStockTableColumns(), dataJosn);
	initTableWithToolBar("stock",'stockToolbar', getStockTableColumns(), dataJosn);
	initTableWithToolBar("approvalStock",'approvalStockToolbar', getApprovalStockTableColumns(), dataJosn);
	initTable("approvalStockChange", getApprovalStockChangeTableColumns(), dataJosn);

	getInstock();
	// getOutStock();
	// queryStock();
	// queryStockApproval();
	// queryStockApprovalChange();
	
//	$('#sssasdPrice').typeahead({
//        source: [
//            { id: 1, name: 'Toronto' },
//            { id: 2, name: 'Montreal' },
//            { id: 3, name: 'New York' },
//            { id: 4, name: 'Buffalo' },
//            { id: 5, name: 'Boston' },
//            { id: 6, name: 'Columbus' },
//            { id: 7, name: 'Dallas' },
//            { id: 8, name: 'Vancouver' },
//            { id: 9, name: 'Seattle' },
//            { id: 10, name: 'Los Angeles' }
//        ]
//    });
    initGoodsSelect();

    initSelectTab(tabId);
    initRights();
});

function initSelectTab(tabId){
    $('#uiTab li:eq(' + tabId + ') a').tab('show');
    queryStockApproval();
    queryStockApprovalChange();
}

function empty(){
	if(document.getElementById("goods_id_select").value==""){
		document.getElementById("good_id").value = null;
	}	
	if(document.getElementById("goods_id_out_select").value==""){
		document.getElementById("good_id_out").value = null;
	}	
	if(document.getElementById("good_id_stock_select").value==""){
		document.getElementById("good_id_stock").value = null;
	}
}

function initGoodsSelect(){
    $.fn.typeahead.Constructor.prototype.blur = function() {
        var that = this;
        setTimeout(function () { that.hide(); }, 250);
    };

    //入库
    $('#goods_id_select').typeahead({
    	minLength:3,
        source: function(query, process) {
        	var goods = getGoodsNameList('good_id', query);
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
            var goods = getGoodsNameList('good_id', item);
            var flag = false;
            for(var key in goods){
            	if(item == goods[key].id || item == goods[key].text){
            		flag = true;
				}
			}
            return flag;
		}
        ,highlighter: function (item) {
            var goods = getGoodsNameList('good_id', item);
            var good = goods.find(function (p) {
                return p.id == item;
            });
            return good.id+"|"+good.text;
        }
        ,updater: function (item) {
            var goods = getGoodsNameList('good_id', item);
            var good = goods.find(function (p) {
                return p.id == item;
            });
            setGoodsInfo('good_id', good.id);
            return good.text;
        }
        // ,minLength:1
        // ,displayKey: 'text'
    });
    
    //耗用
    $('#goods_id_out_select').typeahead({
    	minLength:3,
        source: function(query, process) {
        	var goods = getGoodsNameList('good_id_out', query);
            var results = goods.map(function (item,index,input){
                return item.id+"";
			});
            if(results.length ==0){
            	document.getElementById("good_id_out").value = null;
            }
            process(results);
            // return goods;
        }
        ,matcher: function (item) {
            var goods = getGoodsNameList('good_id_out', item);
            var flag = false;
            for(var key in goods){
            	if(item == goods[key].id || item == goods[key].text){
            		flag = true;
				}
			}
            return flag;
		}
        ,highlighter: function (item) {
            var goods = getGoodsNameList('good_id_out', item);
            var good = goods.find(function (p) {
                return p.id == item;
            });
            return good.id+"|"+good.text;
        }
        ,updater: function (item) {
            var goods = getGoodsNameList('good_id_out', item);
            var good = goods.find(function (p) {
                return p.id == item;
            });
            setGoodsInfo('good_id_out', good.id);
            return good.text;
        }
        // ,minLength:1
        // ,displayKey: 'text'
    });
    
    //库存调整
    $('#good_id_stock_select').typeahead({
    	minLength:3,
        source: function(query, process) {
        	var goods = getGoodsNameList('good_id_stock', query);
            var results = goods.map(function (item,index,input){
                return item.id+"";
			});
            if(results.length ==0){
            	document.getElementById("good_id_stock").value = null;
            }
            process(results);
            // return goods;
        }
        ,matcher: function (item) {
            var goods = getGoodsNameList('good_id_stock', item);
            var flag = false;
            for(var key in goods){
            	if(item == goods[key].id || item == goods[key].text){
            		flag = true;
				}
			}
            return flag;
		}
        ,highlighter: function (item) {
            var goods = getGoodsNameList('good_id_stock', item);
            var good = goods.find(function (p) {
                return p.id == item;
            });
            return good.id+"|"+good.text;
        }
        ,updater: function (item) {
            var goods = getGoodsNameList('good_id_stock', item);
            var good = goods.find(function (p) {
                return p.id == item;
            });
            setGoodsInfo('good_id_stock', good.id);
            return good.text;
        }
        // ,minLength:1
        // ,displayKey: 'text'
    });
    
}

function setGoodsInfo(selectName, goodId){
    var select = document.getElementById(selectName);
    var options = select.options;
    for(var key in options){
        if(goodId == options[key].value){
        	options[key].selected = true;
        }
	}
}

function getGoodsNameList(selectName, value){
	value = process( value);
	var goodsNameList = [];
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
			goodsNameList.push({id:oValue, text:oText});
		}
	}
	// goodsNameList = [{id:'1', text:'新的什么'},{id:'2', text:'新的什么2'},{id:'3', text:'的什么'}];
	return goodsNameList;
}

function setGoodName() {
	$.ajax({
		type : "post",
		url : path + "/googs/getGoods",
		data : {
			"good_type" : $("#good_type").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
//			var good=[{id:'',text:'请选择'}];
//			for (var i = 0; i < list.length; i++) {
//				good.push({id:list[i].good_id,text:list[i].good_name});
//			}
//			$('#good_name').typeahead({ //将返回数据加载到组件          
//		        source: good
//		    });
//			$("#good_id").select2({
//				 data: good,
//				 theme: "classic",
//				 placeholder:'请选择',
//				 allowClear:true
//				});
			
			$("#good_id option").remove();
            $("#good_id").append("<option value='' selected = 'selected'>" + "</option>");
			for (var i = 0; i < list.length; i++) {
				$("#good_id").append("<option value=" + list[i].good_id + ">" + list[i].good_name + "</option>");
			}
//			setGoodId();
//			setCorporation();
//			setFactory();
		}
	})
}

//function setGoodId() {
//	if($("#good_name").val() != null && $("#good_name").val() !=""){
//	$.ajax({
//		type : "post",
//		url : path + "/googs/getGoods",
//		data : {
//			"good_name" : $("#good_name").val()
//		},
//		dataType : "json",
//		success : function(result) {
//			var list = result.obj;
//			if(list.length !=0){
//			document.getElementById("good_id").value = list[0].good_id;
//			setCorporation();
//			setFactory();
//			}
//		}
//	});
//	}
//}

function setCorporation() {
	$.ajax({
		type : "post",
		url : path + "/googs/getCorporation",
		data : {
//			"good_type" : $("#good_type").val(),
			"good_id" : $("#good_id").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#corporation_id option").remove();
			for (var i = 0; i < list.length; i++) {
				$("#corporation_id").append("<option value=" + list[i].corporation_id + ">" + list[i].corporation + "</option>");
			}
		}
	})
}

function setFactory() {
	$.ajax({
		type : "post",
		url : path + "/googs/getFactory",
		data : {
//			"good_type" : $("#good_type").val(),
			"good_id" : $("#good_id").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#factory_id option").remove();
			for (var i = 0; i < list.length; i++) {
				$("#factory_id").append("<option value=" + list[i].factory_id + ">" + list[i].factory_name + "</option>");
			}
		}
	})
}

function setSpec() {
	$.ajax({
		type : "post",
		url : path + "/googs/getSpec2",
		data : {
//			"good_type" : $("#good_type").val(),
			"good_id" : $("#good_id").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#spec option").remove();
			for (var i = 0; i < list.length; i++) {
				$("#spec").append("<option value=" + list[i].biz_code + ">" + list[i].code_name + "</option>");
			}
		}
	})
}

function setUnit() {
	$.ajax({
		type : "post",
		url : path + "/googs/getUnit2",
		data : {
//			"good_type" : $("#good_type").val(),
			"good_id" : $("#good_id").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#unit option").remove();
			for (var i = 0; i < list.length; i++) {
				$("#unit").append("<option value=" + list[i].biz_code + ">" + list[i].code_name + "</option>");
			}
		}
	})
}

function inStock() {
	if (isRead == 0) {
		layer.alert('无权限，请联系管理员!', {
			skin : 'layui-layer-lan',
			closeBtn : 0,
			shift : 4
		// 动画类型
		});
		return;
	}
	var count = $("input[name='count']").val();
	var test = parseInt(count);
	if (isNaN(test) || test <= 0)
	{
		layer.alert('入库量必须是大于0的数字，请重新输入!');
		return;
	}

	var price = $("input[name='price']").val();
	test = parseFloat(price);
	if (isNaN(test) || test <= 0)
	{
		layer.alert('单价必须是大于0的数字，请重新输入!');
		return;
	}
	var good_id = $("#good_id").val();
	if (good_id == "" || good_id ==null) {
		layer.alert('名称不能为空!');
		return;
	}
//	var corporation_id = $("#corporation_id").val();
//	if (corporation_id == "") {
//		layer.alert('供应商不能为空!', {
//			skin : 'layui-layer-lan',
//			closeBtn : 0,
//			shift : 4
//		// 动画类型
//		});
//		return;
//	}
	var exp = $("input[name='exp']").val();
	if (exp == "" && ($("#good_type option:selected").text() == "药品" || $("#good_type option:selected").text() == "疫苗") ) {
		layer.alert('保质期不能为空!', {
			skin : 'layui-layer-lan',
			closeBtn : 0,
			shift : 4
		// 动画类型
		});
		return;
	}
	layer.confirm('是否确认？', {
		skin: 'layui-layer-lan'
		, closeBtn: 0
		, shift: 4 //动画类型
	}, function ok() {
		//$.serializeObject($('#inStockForm'));
		var param = {
				"good_type":$("#good_type").val(),
				"good_id":$("#good_id").val(),
				"spec":$("#spec").val(),
				"unit":$("#unit").val(),
				"corporation_id":$("#corporation_id").val(),
				"factory_id":$("#factory_id").val(),
				"count":$("#sssasd").val(),
				"price":$("#sssasdPrice").val(),
				"exp":$("#exp").val(),
				"operation_date":$("#operation_date").val(),
				"inStockFarmId":$("#farmId").val(),
				"inStockFarm":$("#farm").val()
		};
		param["approve_status"] = 2;
		$.ajax({
			url : path + "/googs/inStock",
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
				if (result.msg == '1') {
					layer.msg("入库成功！");
					document.getElementById("sssasd").value = "";
					document.getElementById("sssasdPrice").value = "";
					document.getElementById("goods_id_select").value = "";
					getInstock();
				}else{
					layer.alert('操作失败!');
					return;
				}
			}
		});
	});
}
function getInStockTableColumns(){
	var dataColumns = [{
        field: "type_name",
        title: "类型",
        width: '5%'
    },{
    	field: "good_name",
        title: "名称",
        width: '18%'
    },{
    	field: "spec_name",
        title: "规格",
        width: '10%'
    },{
    	field: "unit_name",
        title: "单位",
        width: '5%'
    },{
    	field: "count",
        title: "入库量",
        width: '8%'
    },{
    	field: "operation_date",
        title: "入库日期",
        width: '10%'
    },{
    	field: "corporation",
    	title: "供应方",
    	width: '18%'
    },{
    	field: "factory_name",
        title: "生产厂家",
        width: '18%'
    },{
    	field: "exp",
        title: "保质期",
        width: '10%'
    },{
    	field: "price",
        title: "单价",
        width: '5%'
    }];
	return dataColumns;
}

function getInstock() {
	$.ajax({
		url : path + "/googs/getStockChange",
		data : {
			"operation_kind" : 2,
			"farm_id": objGoods.farmId
		},
		type : "POST",
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#inStockTable").bootstrapTable("load",list);
//			$("#inStockTbody tr").remove();
//			for (var i = 0; i < list.length; i++) {
//				var str = '';
//				str += "<tr class='odd gradeX'>";
//				str += "<td>" + list[i].type_name + "</td>";
//				str += "<td>" + list[i].good_name + "</td>";
//				str += "<td>" + list[i].spec_name + "</td>";
//				str += "<td>" + list[i].unit_name + "</td>";
//				str += "<td>" + list[i].count + "</td>";
//				str += "<td>" + list[i].operation_date + "</td>";
//				if (list[i].corporation == null) {
//					str += "<td></td>";
//				} else {
//					str += "<td>" + list[i].corporation + "</td>";
//				}
//				if (list[i].factory_name == null) {
//					str += "<td></td>";
//				} else {
//					str += "<td>" + list[i].factory_name + "</td>";
//				}
//				str += "<td>" + list[i].exp + "</td>";
//				$("#inStockTbody").append(str);
//			}
		}
	});
}
/** *****耗用*************************************************** */

function setGoodNameOut() {
	$.ajax({
		type : "post",
		url : path + "/googs/getGoods",
		data : {
			"good_type" : $("#good_type_out").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#good_id_out option").remove();
			$("#good_id_out").append("<option value='' selected = 'selected'>" + "</option>");
			for (var i = 0; i < list.length; i++) {
				$("#good_id_out").append("<option value=" + list[i].good_id + ">" + list[i].good_name + "</option>");
			}
		}
	})
}
function outStock() {
	if (isRead == 0) {
		layer.alert('无权限，请联系管理员!', {
			skin : 'layui-layer-lan',
			closeBtn : 0,
			shift : 4
		// 动画类型
		});
		return;
	}
	var count_out = $("#count_out").val();
	var test = parseInt(count_out);
	if (isNaN(test) || test <= 0)
	{
		layer.alert('耗用数量必须是大于0的数字，请重新输入!', {
			skin : 'layui-layer-lan',
			closeBtn : 0,
			shift : 4
			// 动画类型
		});
		return;
	}

	layer.confirm('是否确认？', {
		skin: 'layui-layer-lan'
		, closeBtn: 0
		, shift: 4 //动画类型
	}, function ok() {
		var param = $.serializeObject($('#outStockForm'));
		param["approve_status"] = 2;
		$.ajax({
			url : path + "/googs/outStock",
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
				if (result.msg == '1') {
					layer.msg('耗用成功！');
					document.getElementById("count_out").value = "";
					document.getElementById("goods_id_out_select").value = "";
					getOutStock();
				} else if (result.msg == '2') {
					layer.msg('耗用失败，库存不足！');
					return;
				}else{
					layer.msg('操作失败!');
					return;
				}
			}
		});
	});

}
function getOutStockTableColumns(){
	var dataColumns = [{
        field: "house_name",
        title: "栋舍",
        width: '8%'
    },{
        field: "type_name",
        title: "类型",
        width: '10%'
    },{
    	field: "good_name",
        title: "名称",
        width: '25%'
    },{
    	field: "spec_name",
        title: "规格",
        width: '10%'
    },{
    	field: "unit_name",
        title: "单位",
        width: '5%'
    },{
    	field: "count",
        title: "耗用数量",
        width: '8%'
    },{
    	field: "operation_date",
        title: "耗用日期",
        width: '18%'
    }];
	return dataColumns;
}

function getOutStock() {
	$.ajax({
		url : path + "/googs/getStockChange",
		data : {
			"operation_kind" : 1,
            "farm_id": objGoods.farmId
		},
		type : "POST",
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#outStockTable").bootstrapTable("load",list);
//			
//			$("#outStockTbody tr").remove();
//			for (var i = 0; i < list.length; i++) {
//				var str = '';
//				str += "<tr class='odd gradeX'>";
//				str += "<td>" + list[i].house_name + "</td>";
//				str += "<td>" + list[i].type_name + "</td>";
//				str += "<td>" + list[i].good_name + "</td>";
//				str += "<td>" + list[i].count + "</td>";
//				str += "<td>" + list[i].operation_date + "</td>";
//				$("#outStockTbody").append(str);
//			}
		}
	});
}

/** *****库存*************************************************** */

function setGoodNameStock() {
	$.ajax({
		type : "post",
		url : path + "/googs/getGoods",
		data : {
			"good_type" : $("#good_type_stock").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#good_id_stock option").remove();
			$("#good_id_stock").append("<option value='' selected = 'selected'>" + "</option>");
			for (var i = 0; i < list.length; i++) {
				$("#good_id_stock").append("<option value=" + list[i].good_id + ">" + list[i].good_name + "</option>");
			}
//			setCorporationStock();
//			setFactoryStock();
//			setSpec();
//			setUnit();
		}
	})
}

function setCorporationStock() {
	$.ajax({
		type : "post",
		url : path + "/googs/getCorporation",
		data : {
			"good_type" : $("#good_type_stock").val(),
			"good_id" : $("#good_id_stock").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#corporation_id_stock option").remove();
			$("#corporation_id_stock").append("<option value=''>全部</option>");
			for (var i = 0; i < list.length; i++) {
				$("#corporation_id_stock").append("<option value=" + list[i].corporation_id + ">" + list[i].corporation + "</option>");
			}
		}
	});
}

function setFactoryStock() {
	$.ajax({
		type : "post",
		url : path + "/googs/getFactory",
		data : {
			"good_type" : $("#good_type_stock").val(),
			"good_id" : $("#good_id_stock").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#factory_id_stock option").remove();
			$("#factory_id_stock").append("<option value=''>全部</option>");
			for (var i = 0; i < list.length; i++) {
				$("#factory_id_stock").append("<option value=" + list[i].factory_id + ">" + list[i].factory_name + "</option>");
			}
		}
	})
}

function setSpecStock() {
	$.ajax({
		type : "post",
		url : path + "/googs/getSpec2",
		data : {
//			"good_type" : $("#good_type").val(),
			"good_id" : $("#good_id_stock").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#spec_stock option").remove();
			$("#spec_stock").append("<option value=''>全部</option>");
			for (var i = 0; i < list.length; i++) {
				$("#spec_stock").append("<option value=" + list[i].biz_code + ">" + list[i].code_name + "</option>");
			}
		}
	})
}

function setUnitStock() {
	$.ajax({
		type : "post",
		url : path + "/googs/getUnit2",
		data : {
//			"good_type" : $("#good_type").val(),
			"good_id" : $("#good_id_stock").val()
		},
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#unit_stock option").remove();
			$("#unit_stock").append("<option value=''>全部</option>");
			for (var i = 0; i < list.length; i++) {
				$("#unit_stock").append("<option value=" + list[i].biz_code + ">" + list[i].code_name + "</option>");
			}
		}
	})
}

function getMessages(){
	if (isRead == 0) {
		layer.alert('无权限，请联系管理员!', {
			skin : 'layui-layer-lan',
			closeBtn : 0,
			shift : 4
		// 动画类型
		});
		return;
	}
	 var getRow = $('#stockTable').bootstrapTable('getSelections');
	 if(getRow==null||getRow==''){
		 layer.msg('请先选择需要调整的库存记录!');
		 return;
	 }
	 /***判断是否存在未审批的调整纪录*************/
	 var param =getRow[0];
		param["operation_kind"]=3;
		param["approve_status"]=1;
		$.ajax({
			url : path + "/googs/isAdjust",
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
				if(result.msg=="1") {
					layer.alert('此记录存在未审批的处理，请先审批!', {
						skin : 'layui-layer-lan',
						closeBtn : 0,
						shift : 4
					// 动画类型
					});
				}else{
					openAdjustWin(getRow);
				}
			}
		});
	 
	 
}
/****弹出调整窗口*****/
function openAdjustWin(getRow){
var str = '<br><div class="container-fluid" >';
	str += '<div class="row-fluid">';
		str += '<div class="span4">';
			str += '<span_customer2>' + '类型' +'</span_customer2>';
		str += '</div>';
			str += '<div class="span8" >';
				str += getRow[0].type_name ;
			str += '</div>';
	str += '</div>';
    str += '<div class="row-fluid">';
		str += '<div class="span4">';
			str += '<span_customer2>' + '名称' +'</span_customer2>';
		str += '</div>';
			str += '<div class="span8" >';
				str += getRow[0].good_name ;
			str += '</div>';
    str += '</div>';
    str += '<div class="row-fluid">';
		str += '<div class="span4">';
			str += '<span_customer2>' + '规格' +'</span_customer2>';
		str += '</div>';
			str += '<div class="span8">';
				str += getRow[0].spec_name ;
			str += '</div>';
    str += '</div>';
    str += '<div class="row-fluid">';
		str += '<div class="span4">';
			str += '<span_customer2>' + '生产厂家'+'</span_customer2>';
		str += '</div>';
			str += '<div class="span8">';
				if (getRow[0].factory_name == null) {
					str += '';
				}else{
					str += getRow[0].factory_name ;
				}
			str += '</div>';
    str += '</div>';
    str += '<div class="row-fluid">';
		str += '<div class="span4">';
			str+=  '<span_customer2>' + '供应方' +'</span_customer2>';
		str += '</div>';
			str += '<div class="span8">';
				if(getRow[0].corporation == null) {
					str += '';
				}else{
					str += getRow[0].corporation;
				}
			str += '</div>';
    str += '</div>';
    str += '<div class="row-fluid">';
		str += '<div class="span4">';
			str += '<span_customer2>' + '单位'+'</span_customer2>';
		str += '</div>';
			str += '<div class="span8">';
				str += getRow[0].unit_name;
			str += '</div>';
    str += '</div>';
    str += '<div class="row-fluid">';
		str += '<div class="span4">';
			str +=  '<span_customer2>' +  '当前库存量'+'</span_customer2>';
		str += '</div>';
			str += '<div class="span8">';
				str += getRow[0].stockCount;
			str += '</div>';
    str += '</div>';
    str += '<div class="row-fluid">';
		str += '<div class="span4">';
			str += '<span_customer2>' + '调整数量'+'</span_customer2>';
		str += '</div>';
			str += '<div class="span8">';
				str += '<input type="text" style="width: 70px;" name="count" id="adjustValue"/>';
			str += '</div>';
    str += '</div>';
    str += '<div class="row-fluid">';
		str += '<div class="span4">';
			str += '<span_customer2>' + '调整后库存量'+'</span_customer2>';
		str += '</div>';
			str += '<div class="span8">';
				str += '<span id="adjustNum">' + getRow[0].stockCount+'</span>';
			str += '</div>';
    str += '</div>';
    str += '<div class="row-fluid">';
		str += '<div class="span4">';
			str += '<span_customer2>' +'备注'+'</span_customer2>';
		str += '</div>';
			str += '<div class="span8">';
				str += '<input type="text" style="width: 320px;" name="adjustRemark" id="adjustRemark"/>';
			str += '</div>';
    str += '</div>';
str += '</div>';
layer.open({
	  type: 1,
	  skin: 'layui-layer-lan', //加上边框
	  area: ['570px', '440px'], //宽高
	  content: str,
	  btn: ['确定','取消'],
	  yes: function(index){
		  var adjustValue=$("#adjustValue").val();
		  if(adjustValue==null||adjustValue==''){
				 layer.msg('请输入调整数量!');
				 return;
			}
		  if(isNaN(adjustValue)){
			  layer.msg('请输入数字!');
			 return;
		  }
		var param =getRow[0];
		param["adjustValue"]=adjustValue;
		param["remark"] = $("#adjustRemark").val();
		param["farm_id"]=objGoods.farmId;
		param["farm_name"]=objGoods.farm;
		$.ajax({
			url : path + "/googs/setStockSum",
			data : param,
			type : "POST",
			dataType : "json",
			success : function(result) {
				layer.close(index); 
				queryStock();
				if(result.msg=="1") {
					layer.msg('操作成功,进入审批流程!');
				}else{
					layer.msg('操作失败!');
				}
			}
		});
	  }
	});
$("#adjustValue").blur(function(){
	  var adjustValue=$("#adjustValue").val();
	  if(isNaN(adjustValue)){
		  layer.msg('请输入数字!');
		 return;
	  }else{
		  var num=Number(getRow[0].stockCount)+Number($("#adjustValue").val());
			if(num<0){
				layer.msg('调整后的数量小于零!');
			}else{
				$("#adjustNum").text(num);
			}
	  }
 });
}


function getStockTableColumns(){
	var dataColumns = [{radio: true,
        title: "选择",
        width: '5%'
    },{
        field: "type_name",
        title: "类型",
        width: '8%'
    },{
    	field: "good_name",
        title: "名称",
        width: '18%'
    },{
    	field: "spec_name",
        title: "规格",
        width: '10%'
    },{
    	field: "unit_name",
        title: "单位",
        width: '8%'
    },{
    	field: "corporation",
    	title: "供应方",
    	width: '20%'
    },{
    	field: "factory_name",
        title: "生产厂家",
        width: '18%'
    },{
    	field: "stockCount",
        title: "库存量",
        width: '8%'
    }
    ,{
    	field: "waitCount",
        title: "待审批量",
        width: '8%'
    }   
    ];
	return dataColumns;
};
function queryStock(){

	var param = $.serializeObject($('#stockForm'));
	$.ajax({
		url : path + "/googs/getStockSum",
		data : param,
		type : "POST",
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#stockTable").bootstrapTable("load",list);
//			$("#stockTbody tr").remove();
//			for (var i = 0; i < list.length; i++) {
//				var str = '';
//				str += "<tr class='odd gradeX'>";
//				str += "<td> <input type='checkbox' name='stockId' value="+list[i].type_name+">&nbsp;</td>";
//				str += "<td>" + list[i].type_name + "</td>";
//				str += "<td>" + list[i].good_name + "</td>";
//				str += "<td>" + list[i].spec_name + "</td>";
//				str += "<td>" + list[i].unit_name + "</td>";
//				if (list[i].corporation == null) {
//					str += "<td></td>";
//				} else {
//					str += "<td>" + list[i].corporation + "</td>";
//				}
//				if (list[i].factory_name == null) {
//					str += "<td></td>";
//				} else {
//					str += "<td>" + list[i].factory_name + "</td>";
//				}
//				str += "<td>" + list[i].stockCount + "</td>";
//				$("#stockTbody").append(str);
//			}
		}
	});
}

function queryStockApproval(){
	$.ajax({
		url : path + "/googs/getStockApproval",
		data : {
			farm_id: objGoods.farmId
		},
		type : "POST",
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#approvalStockTable").bootstrapTable("load",list);
		}
	});
}
function queryStockApprovalChange(){
	$.ajax({
		url : path + "/googs/getStockApprovalChange",
		data : {
			farm_id: objGoods.farmId
		},
		type : "POST",
		dataType : "json",
		success : function(result) {
			var list = result.obj;
			$("#approvalStockChangeTable").bootstrapTable("load",list);
		}
	});
}

/*****库存****************************************************/

//库存调整审批
function getApprovalStockTableColumns(){
	var dataColumns = [{
			field: "id",
			title: "序号",
			visible: false
	},{radio: true,
		title: "选择",
		width: '5%'
	},{
		field: "type_name",
		title: "类型",
		width: '8%'
	},{
		field: "good_name",
		title: "名称",
		width: '18%'
	},{
		field: "spec_name",
		title: "规格",
		width: '10%'
	},{
		field: "unit_name",
		title: "单位",
		width: '8%'
	},{
		field: "corporation",
		title: "供应方",
		width: '18%'
	},{
		field: "factory_name",
		title: "生产厂家",
		width: '18%'
	},{
		field: "count",
		title: "库存调整量",
		width: '8%'
	},{
		field: "bak",
		title: "备注",
		width: '20%'
	}];
	return dataColumns;
}
function getApprovalStockChangeTableColumns(){
	var dataColumns = [{
        field: "id",
        title: "序号",
        visible: false,
        width: '3%'
    },{
		field: "operation_date",
		title: "提交日期",
		width: '10%'
	},{
		field: "farm_name",
		title: "农场",
		width: '10%'
	},{
		field: "type_name",
		title: "类型",
		width: '8%'
	},{
		field: "good_name",
		title: "名称",
		width: '18%'
	},{
		field: "spec_name",
		title: "规格",
		width: '10%'
	},{
		field: "unit_name",
		title: "单位",
		width: '8%'
	},{
		field: "corporation",
		title: "供应方",
		width: '18%'
	},{
		field: "factory_name",
		title: "生产厂家",
		width: '18%'
	},{
		field: "count",
		title: "变更内容",
		width: '18%'
	},{
		field: "state",
		title: "审批状态",
		width: '3%'
	},{
		field: "create_person",
		title: "提交人",
		width: '6%'
	},{
		field: "bak",
		title: "备注",
		width: '18%'
	}];
	return dataColumns;
};



function initRights(){
    if (isRead == 0 || isRead == 1) {
        document.getElementById("stockToolbar_btn_edit").style.display = "none";
        document.getElementById("approvalStockToolbar_btn_reject").style.display = "none";
        document.getElementById("approvalStockToolbar_btn_pass").style.display = "none";
        return;
    }
}

//切换标签页事件处理
$(function(){
	$('a[data-toggle="tab"]').on('shown', function (e) {
		if("入库" == $(e.target).text()){
			tabId =0;
			getInstock();
			return;
		}
		if("耗用" == $(e.target).text()){
            tabId =1;
			getOutStock();
			return;
		}
		if("库存调整" == $(e.target).text()){
            tabId =2;
			queryStock();
			return;
		}
		if("库存调整审批" == $(e.target).text()){
            tabId =3;
			queryStockApproval();
			queryStockApprovalChange();
			return;
		}
	});
});

function initObjGoods(){
	objGoods.farmId =  document.getElementById("farmId").value;
	objGoods.farm =  document.getElementById("farm").value;

	document.getElementById("inStockFarmTitle").innerHTML = "<font size='4' ><B>" + objGoods.farm +"入库记录</B></font>";
	document.getElementById("outStockFarmTitle").innerHTML =  "<font size='4' ><B>" + objGoods.farm +"耗用记录</B></font>";
	document.getElementById("stockFarmTitle").innerHTML = "<font size='4' ><B>" + objGoods.farm +"库存调整记录</B></font>";
	document.getElementById("approvalStockFarmTitle").innerHTML =  "<font size='4' ><B>" + objGoods.farm +"库存调整审批记录</B></font>";
}

function rejectStockChange(remark) {
	var row = $('#approvalStockTable').bootstrapTable('getSelections');
	if (row.length == 1) {
		var param = row[0];
//		var id = param["id"];
		$.ajax({
			url: path + "/googs/rejectStockChange",
			data: {
				id: param["id"],
				good_id:param["good_id"],
				good_type:param["good_type"],
				spec:param["spec"],
				unit:param["unit"],
				corporation_id:param["corporation_id"],
				factory_id:param["factory_id"],
				remark: remark,
				farm_id:objGoods.farmId
			},
			type: "POST",
			dataType: "json",
			success: function (result) {
				if (result.success) {
					layer.msg("驳回成功！");
					queryStockApproval();
					queryStockApprovalChange();
				} else {
					layer.msg("驳回出错，请联系管理员！");
				}
			}
		});

	} else {
		layer.msg("请先选择待审批记录！");
	}
};

function approvalStockChange(remark){
	var row =  $('#approvalStockTable').bootstrapTable('getSelections');
	if(row.length == 1){
		var param =row[0];
//		var id = param["id"];
		$.ajax({
			url : path + "/googs/approvalStockChange",
			data : {
                id: param["id"],
				good_id:param["good_id"],
				good_type:param["good_type"],
				spec:param["spec"],
				unit:param["unit"],
				corporation_id:param["corporation_id"],
				factory_id:param["factory_id"],
				remark: remark,
				farm_id:objGoods.farmId
			},
			type : "POST",
			dataType : "json",
			success : function(result) {
				if(result.success){
					layer.msg("通过成功！");
					queryStockApproval();
					queryStockApprovalChange();
				} else{
					layer.msg("通过出错，请联系管理员！");
				}
			}
		});
	} else{
		layer.msg("请先选择待审批记录！");
	}
};


/****弹出审批窗口*****/
function openApprovalWin(flag) {
	var str = '<br><div class="container-fluid" >';
			str += '<div class="row-fluid">';
				str += '<div class="span2">';
					str += '备注:';
				str += '</div>';
					str += '<div class="span10">';
						str += '<input type="text" style="width: 350px;" name="approvalRemark" id="approvalRemark"/>';
					str += '</div>';
			str += '</div>';
			str += '<div class="row-fluid">';
				str += '<div class="span12">';
					str += '是否确认？';
				str += '</div>';
			str += '</div>';
		str += '</div>';
	layer.open({
		type: 1,
		skin: 'layui-layer-lan', //加上边框
		area: ['500px', '200px'], //宽高
		content: str,
		btn: ['确定', '取消'],
		yes: function (index) {
			rt = $("#approvalRemark").val();
			layer.close(index);
			if(flag == 1){
				approvalStockChange(rt);
			} else{
				rejectStockChange(rt);
			}

		}
	});
};

function clearData(){
	document.getElementById("goods_id_select").value ="";
	document.getElementById("sssasd").value =0;
	document.getElementById("sssasdPrice").value =0;
	document.getElementById("goods_id_out_select").value ="";
	document.getElementById("count_out").value =0;
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

