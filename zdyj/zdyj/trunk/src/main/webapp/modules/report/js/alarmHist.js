var count0rg;
var num=1;
var paramTypeList = new Array("alarmHist","alarmHistDetail");
//var allSearch = "true";
var paramTypeSelectValue = null;
var asyncFlag = false;
var tabId = 0;
var pSize = 50;

$(document).ready(function(){
	App.init();
    initHelp();

    $('.date-picker').datepicker({
        language: 'zh-CN',
        autoclose: true,
        todayHighlight: true
    });

    // var win_h = $(window).height()-208;
    // $("#user_date_table").css("min-height",win_h);
    // $("#page-content").css("min-height",win_h);
    // $("#container").css("height",win_h-50);

//	 $("#farmId").change(function() {
//		 setHouseId();
//		});

//	 $("#houseId").change(function() {
//		 // raymon 20160922
//		 setBatchId();
//		 // queryAlarmHist();
//		 // raymon 20160922
//	 });

    // raymon 20160922
//	 $("#batchId").change(function() {
//		 search();
//	 });

//	  setHouseId();
    // raymon 20160922
    // $("#state2").css("display", "block");
    // $("#detail2").css("display", "none");
    forward3();
});

function initHelp(){
    help.size = ['600px', '380px'];
    help.context = document.getElementById("helpContext").innerHTML;
}

function OrgSearch(count0rg,num){
	if("" != corporation_id){
		var select = document.getElementById("orgId"+(count0rg-2));
		if(null != select && "undefined" != select){
                for(var i=0; i<select.options.length; i++){
                    var corporation = select.options[i].value;
                    var strs= new Array();
                    strs = corporation.split(",");
                    if(strs[0] == corporation_id){
                        corporation_id="";
                        select.options[i].selected = true;
                        select.onchange();
                        break;
                    }
			}
		}
        corporation_id="";
	} else{
        if("" != farm_id){
            var select2 = document.getElementById("orgId"+(count0rg-1));
            if(null != select2 && "undefined" != select2) {
                for(var i=0; i<select2.options.length; i++) {
                    var farm = select2.options[i].value;
                    var strs2 = new Array();
                    strs2 = farm.split(",");
                    if (strs2[0] == farm_id) {
                        farm_id="";
                        select2.options[i].selected = true;
                        select2.onchange();
                        break;
                    }
                }
            }
            farm_id="";
        }else if("" != house_id){
        	var select3 = document.getElementById("orgId"+(count0rg));
            if(null != select3 && "undefined" != select3) {
                for(var i=0; i<select3.options.length; i++) {
                    var house = select3.options[i].value;
                    var strs3 = new Array();
                    strs3 = house.split(",");
                    if (strs3[0] == house_id) {
                        house_id="";
                        select3.options[i].selected = true;
                        select3.onchange();
                        reflushAlarmHist2();
                        break;
                    }
                }
            }
            house_id="";
        }
        else{
            if("" == corporation_id && "" == farm_id && "" == house_id){
            	reflushAlarmHist2();
            }
        }

}
}


//function setBatchId(){
//	$.ajax({
//		type : "post",
//		url : path + "/temProfile/getBatch",
//		data : {
//			"farmId" : $("#orgId" + (count0rg - 1)).val().split(",")[1],
//			"houseId" : $("#orgId" + count0rg).val().split(",")[1]
//		},
//		dataType: "json",
//		success : function(result) {
//			var list = result.obj;
//			$("#batchId option").remove();
//			for (var i = 0; i < list.length; i++) {
//				$("#batchId").append("<option value=" + list[i].batch_no+ ">" + list[i].batch_no + "</option>");
//			}
//			document.getElementById("batchId").value=list[0].batch_no;
//			
//			$.ajax({                              
//		        // async: true,
//		        url: path+"/alarmHist/queryAlarmHist2",
//		        data: {
//					"farmId" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId":$("#orgId" + count0rg).val().split(",")[1],"batchNo":$("#batchId").val()
//				},
//		        type: "POST",
//		        dataType: "json",
//		        cache: false,
//		        // timeout:50000,
//		        success: function (result) {
//		            var list = eval(result.obj);
//		            $("#tbodyAlarmHistList tr").remove();
//		            for (var i = 0; i < list.length; i++) {
//		                var str = '';
//		                var strForward = "\"forward("+list[i]["date_age"]+","+list[i]["batch_no"]+",'"+list[i]["date"]+"');\"";
//		                str += "<tr style='height:30px' onclick="+strForward+">";
//		                str += "<td style='height:30px;'>" + list[i]["farm_name"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["house_name"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["date_age"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["high_temp_num"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["low_temp_num"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["high_negative_pressure_num"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["low_negative_pressure_num"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["high_co2_num"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["high_water_num"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["low_water_num"] + "</td>";
//		                $("#tbodyAlarmHistList").append(str);
//		            }
//
//		        }
//		    });
//			
//		}
//	});
//}

//function setBatchId2(){
//	$.ajax({
//		type : "post",
//		url : path + "/temProfile/getBatch",
//		data : {
//			"farmId" : $("#orgId" + (count0rg - 1)).val().split(",")[1],
//			"houseId" : $("#orgId" + count0rg).val().split(",")[1]
//		},
//		dataType: "json",
//		success : function(result) {
//			var list = result.obj;
//			$("#batchId2 option").remove();
//			for (var i = 0; i < list.length; i++) {
//				$("#batchId2").append("<option value=" + list[i].batch_no+ ">" + list[i].batch_no + "</option>");
//			}
//			$("#batchId2").val(list[0].batch_no);
//			
//			var param;
//			if($("#beginTime").val()=="" && $("#endTime").val()==""){
//				param={
//						"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"bizCode":$("#bizCode").val()
//					};
//			}else if($("#beginTime").val()=="" && $("#endTime").val()!=""){
//				param = {
//						"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"endTime":$("#endTime").val(),
//						"bizCode":$("#bizCode").val()
//				};
//			}else if($("#beginTime").val()!="" && $("#endTime").val()==""){
//				param = {
//						"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"beginTime":$("#beginTime").val(),
//						"bizCode":$("#bizCode").val()
//				};
//			}else{
//				param = {
//						"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"beginTime":$("#beginTime").val(),
//						"endTime":$("#endTime").val(),"bizCode":$("#bizCode").val()
//					};
//			}
//			
//			$.ajax({                              
//		        // async: true,
//		        url: path+"/alarmHist/queryAlarmHist3",
//		        data: param,
//		        type: "POST",
//		        dataType: "json",
//		        cache: false,
//		        // timeout:50000,
//		        success: function (result) {
//		        	var list = eval(result.obj);
//		            $("#tbodyAlarmHistDetailList tr").remove();
//		            for (var i = 0; i < list.length; i++) {
//		                var str = '';
//		                str += "<tr style='height:30px' >";
//		                str += "<td style='height:30px;'>" + list[i]["date_age"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["alarm_time"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["alarm_Name"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["set_value"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["actual_value"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["continue_time"] + "</td>";
//		                str += "<td style='height:30px;'>" + list[i]["response_person"] + "</td>";
//		                $("#tbodyAlarmHistDetailList").append(str);
//		            }
//
//		        }
//		    });
//			
//		}
//	});
//}

//function setHouseId(){
//	$.ajax({
//		type : "post",
//		url : path + "/alarmHist/getHouse",
//		data : {
//			"farmId" : $("#farmId").val()
//		},
//		dataType: "json",
//		success : function(result) {
//			var list = result.obj;
//			$("#houseId option").remove();
//			for (var i = 0; i < list.length; i++) {
//				$("#houseId").append("<option value=" + list[i].id + ">" + list[i].house_name+ "</option>");
//			}
//			setBatchId();
//		}
//	});
//}

//function setHouseId2(){
//	$.ajax({
//		type : "post",
//		url : path + "/alarmHist/getHouse",
//		data : {
//			"farmId" : $("#farmId2").val()
//		},
//		dataType: "json",
//		success : function(result) {
//			var list = result.obj;
//			$("#houseId2 option").remove();
//			for (var i = 0; i < list.length; i++) {
//				$("#houseId2").append("<option value=" + list[i].id + ">" + list[i].house_name+ "</option>");
//			}
////			$("#houseId").val(list[0].id);
////			setAlarmName();
//			setBatchId2();
//		}
//	});
//}

function setAlarmName(){
	$.ajax({
		type : "post",
		url : path + "/alarmHist/getAlarmName",
		data : {
			"farmId" : $("#orgId" + (count0rg - 1)).val().split(",")[1],
			"houseId" : $("#orgId" + count0rg).val().split(",")[1]
		},
		dataType: "json",
		success : function(result) {
			var list = result.obj;
			$("#alarmName option").remove();
			$("#alarmName").append("<option value=\"\">" + "全部" + "</option>");
			for (var i = 0; i < list.length; i++) {
				$("#alarmName").append("<option value=" + list[i].biz_code+ ">" + list[i].code_name + "</option>");
			}
//			$("#alarmName").val(list[0].code_name);
			queryAlarmHist(); 
		}
	});
}

//function reflushAlarmHist(num){
//	if(num==1){
//	$.ajax({
//		type : "post",
//		url : path + "/alarmHist/getHouse",
//		data : {
//			"farmId" : $("#orgId" + (count0rg - 1)).val().split(",")[1]
//		},
//		dataType: "json",
//		success : function(result) {
//			var list = result.obj;
//			$("#houseId option").remove();
//			for (var i = 0; i < list.length; i++) {
//				$("#houseId").append("<option value=" + list[i].id + ">" + list[i].house_name+ "</option>");
//			}
//			setBatchId();
//			
//		}
//	});
//	}else{
//		$.ajax({
//			type : "post",
//			url : path + "/alarmHist/getHouse",
//			data : {
//				"farmId" : $("#orgId" + (count0rg - 1)).val().split(",")[1]
//			},
//			dataType: "json",
//			success : function(result) {
//				var list = result.obj;
//				$("#houseId2 option").remove();
//				for (var i = 0; i < list.length; i++) {
//					$("#houseId2").append("<option value=" + list[i].id + ">" + list[i].house_name+ "</option>");
//				}
//				setBatchId2();
//				
//			}
//		});
//	}
//
//}

function reflushAlarmHist2(){
	var orgValue2 = $("#orgId" + count0rg).val();
	if("" == orgValue2 || null == orgValue2){
		return;
	}
	var farmId = $("#orgId" + (count0rg - 1)).val().split(",")[1];
	var houseId = $("#orgId" + count0rg).val().split(",")[1];
	
	if(num==1){
		$.ajax({
			type : "post",
			url : path + "/temProfile/getBatch",
			data : {
				"farmId" : farmId,
				"houseId" : houseId
			},
			dataType: "json",
			success : function(result) {
				var list = result.obj;
				$("#batchId option").remove();
				if(0 != list.length){
				for (var i = 0; i < list.length; i++) {
					$("#batchId").append("<option value=" + list[i].batch_no+ ">" + list[i].batch_no + "</option>");
				}
				if(batch_no !=""){
					document.getElementById("batchId").value=batch_no;
				}else{
					document.getElementById("batchId").value=list[0].batch_no;
				}
				}else{
					document.getElementById("batchId").value="";
				}
				$.ajax({                              
			        // async: true,
			        url: path+"/alarmHist/queryAlarmHist2",
			        data: {
						"farmId" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId":$("#orgId" + count0rg).val().split(",")[1],"batchNo":$("#batchId").val()
					},
			        type: "POST",
			        dataType: "json",
			        cache: false,
			        // timeout:50000,
			        success: function (result) {
			            var list = result.obj;
			            initTable("alarmHist", getTableDataColumns("alarmHist"), []);
		                if(0 != list.length) {
		                    var dataJosn = $.parseJSON(JSON.stringify(list));
		                    $("#alarmHistTable").bootstrapTable('load',dataJosn);
		                } 

			        }
			    });
				
			}
		});
		}else{
			$.ajax({
				type : "post",
				url : path + "/temProfile/getBatch",
				data : {
					"farmId" : $("#orgId" + (count0rg - 1)).val().split(",")[1],
					"houseId" : $("#orgId" + count0rg).val().split(",")[1]
				},
				dataType: "json",
				success : function(result) {
					var list = result.obj;
					$("#batchId option").remove();
					if(0 != list.length){
					for (var i = 0; i < list.length; i++) {
						$("#batchId").append("<option value=" + list[i].batch_no+ ">" + list[i].batch_no + "</option>");
					}
					$("#batchId").val(list[0].batch_no);
					}else{
						$("#batchId").val("");
					}
					var param;
					if($("#beginTime").val()=="" && $("#endTime").val()==""){
						param={
								"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"batchNo2":$("#batchId").val(),"codeName":$("#codeName").val()
							};
					}else if($("#beginTime").val()=="" && $("#endTime").val()!=""){
						param = {
								"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"batchNo2":$("#batchId").val(),"endTime":$("#endTime").val(),
								"codeName":$("#codeName").val()
						};
					}else if($("#beginTime").val()!="" && $("#endTime").val()==""){
						param = {
								"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"batchNo2":$("#batchId").val(),"beginTime":$("#beginTime").val(),
								"codeName":$("#codeName").val()
						};
					}else{
						param = {
								"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"batchNo2":$("#batchId").val(),"beginTime":$("#beginTime").val(),
								"endTime":$("#endTime").val(),"codeName":$("#codeName").val()
							};
					}
					
					$.ajax({                              
				        // async: true,
				        url: path+"/alarmHist/queryAlarmHist3",
				        data: param,
				        type: "POST",
				        dataType: "json",
				        cache: false,
				        // timeout:50000,
				        success: function (result) {
				        	var list = result.obj;
				        	initTable("alarmHistDetail", getTableDataColumns("alarmHistDetail"), []);
			                if(0 != list.length) {
			                    var dataJosn = $.parseJSON(JSON.stringify(list));
			                    $("#alarmHistDetailTable").bootstrapTable('load',dataJosn);
			                } 
//			                else{
//			                    initTableRow("alarmHistDetail", getTableEmptyRow("alarmHistDetail"));
//			                }

				        }
				    });
					
				}
			});
		}

}

function reflushAlarmHist3(){
	var orgValue2 = $("#orgId" + count0rg).val();
	if("" == orgValue2 || null == orgValue2){
		return;
	}
	
	if(num==1){
	$.ajax({                              
        // async: true,
        url: path+"/alarmHist/queryAlarmHist2",
        data: {
			"farmId" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId":$("#orgId" + count0rg).val().split(",")[1],"batchNo":$("#batchId").val()
		},
        type: "POST",
        dataType: "json",
        cache: false,
        // timeout:50000,
        success: function (result) {
            var list = result.obj;
            initTable("alarmHist", getTableDataColumns("alarmHist"), []);
            if(0 != list.length) {
                var dataJosn = $.parseJSON(JSON.stringify(list));
                $("#alarmHistTable").bootstrapTable('load',dataJosn);
            } 
//            else{
//                initTableRow("alarmHist", getTableEmptyRow("alarmHist"));
//            }
//            $("#tbodyAlarmHistList tr").remove();
//            for (var i = 0; i < list.length; i++) {
//                var str = '';
//                var strForward = "\"forward("+list[i]["date_age"]+","+list[i]["batch_no"]+",'"+list[i]["date"]+"');\"";
//                str += "<tr style='height:30px' onclick="+strForward+">";
//                str += "<td style='height:30px;'>" + list[i]["farm_name"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["house_name"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["date_age"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["high_temp_num"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["low_temp_num"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["high_negative_pressure_num"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["low_negative_pressure_num"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["high_co2_num"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["high_water_num"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["low_water_num"] + "</td>";
//                $("#tbodyAlarmHistList").append(str);
//            }

        }
    });
	}else{
		var param;
		if($("#beginTime").val()=="" && $("#endTime").val()==""){
			param={
					"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"codeName":$("#codeName").val(),"batchNo2":$("#batchId").val()
				};
		}else if($("#beginTime").val()=="" && $("#endTime").val()!=""){
			param = {
					"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"endTime":$("#endTime").val(),
					"codeName":$("#codeName").val(),"batchNo2":$("#batchId").val()
			};
		}else if($("#beginTime").val()!="" && $("#endTime").val()==""){
			param = {
					"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"beginTime":$("#beginTime").val(),
					"codeName":$("#codeName").val(),"batchNo2":$("#batchId").val()
			};
		}else{
			param = {
					"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"beginTime":$("#beginTime").val(),
					"endTime":$("#endTime").val(),"codeName":$("#codeName").val(),"batchNo2":$("#batchId").val()
				};
		}
		
		$.ajax({                              
	        // async: true,
	        url: path+"/alarmHist/queryAlarmHist3",
	        data: param,
	        type: "POST",
	        dataType: "json",
	        cache: false,
	        // timeout:50000,
	        success: function (result) {
	        	var list = result.obj;
	        	initTable("alarmHistDetail", getTableDataColumns("alarmHistDetail"), []);
                if(0 != list.length) {
                    var dataJosn = $.parseJSON(JSON.stringify(list));
                    $("#alarmHistDetailTable").bootstrapTable('load',dataJosn);
                } 
//                else{
//                    initTableRow("alarmHistDetail", getTableEmptyRow("alarmHistDetail"));
//                }
//	            $("#tbodyAlarmHistDetailList tr").remove();
//	            for (var i = 0; i < list.length; i++) {
//	                var str = '';
//	                str += "<tr style='height:30px' >";
//	                str += "<td style='height:30px;'>" + list[i]["date_age"] + "</td>";
//	                str += "<td style='height:30px;'>" + list[i]["alarm_time"] + "</td>";
//	                str += "<td style='height:30px;'>" + list[i]["alarm_Name"] + "</td>";
//	                str += "<td style='height:30px;'>" + list[i]["set_value"] + "</td>";
//	                str += "<td style='height:30px;'>" + list[i]["actual_value"] + "</td>";
//	                str += "<td style='height:30px;'>" + list[i]["continue_time"] + "</td>";
//	                str += "<td style='height:30px;'>" + list[i]["response_person"] + "</td>";
//	                $("#tbodyAlarmHistDetailList").append(str);
//	            }

	        }
	    });
	}

}

function reflushAlarmHist4(){
	var orgValue2 = $("#orgId" + count0rg).val();
	if("" == orgValue2 || null == orgValue2){
		return;
	}
	var param;
	if($("#beginTime").val()=="" && $("#endTime").val()==""){
		param={
				"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"codeName":$("#codeName").val(),"batchNo2":$("#batchId").val()
			};
	}else if($("#beginTime").val()=="" && $("#endTime").val()!=""){
		param = {
				"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"endTime":$("#endTime").val(),
				"codeName":$("#codeName").val(),"batchNo2":$("#batchId").val()
		};
	}else if($("#beginTime").val()!="" && $("#endTime").val()==""){
		param = {
				"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"beginTime":$("#beginTime").val(),
				"codeName":$("#codeName").val(),"batchNo2":$("#batchId").val()
		};
	}else{
		param = {
				"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],"beginTime":$("#beginTime").val(),
				"endTime":$("#endTime").val(),"codeName":$("#codeName").val(),"batchNo2":$("#batchId").val()
			};
	}
	
	$.ajax({                              
        // async: true,
        url: path+"/alarmHist/queryAlarmHist3",
        data: param,
        type: "POST",
        dataType: "json",
        cache: false,
        // timeout:50000,
        success: function (result) {
        	var list = result.obj;
        	initTable("alarmHistDetail", getTableDataColumns("alarmHistDetail"), []);
            if(0 != list.length) {
                var dataJosn = $.parseJSON(JSON.stringify(list));
                $("#alarmHistDetailTable").bootstrapTable('load',dataJosn);
            } 
//            else{
//                initTableRow("alarmHistDetail", getTableEmptyRow("alarmHistDetail"));
//            }
//            $("#tbodyAlarmHistDetailList tr").remove();
//            for (var i = 0; i < list.length; i++) {
//                var str = '';
//                str += "<tr style='height:30px' >";
//                str += "<td style='height:30px;'>" + list[i]["date_age"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["alarm_time"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["alarm_Name"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["set_value"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["actual_value"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["continue_time"] + "</td>";
//                str += "<td style='height:30px;'>" + list[i]["response_person"] + "</td>";
//                $("#tbodyAlarmHistDetailList").append(str);
//            }

        }
    });
}

//检索
function search(){
	$("#alarmHistForm").submit();
}


function forward(farm_id,house_id,date_age,batchNo,date,name){
	setTab(1);
	var orgValue2 = $("#orgId" + count0rg).val();
	if("" == orgValue2 || null == orgValue2){
		return;
	}
    // $("#detailTab1").css("text-decoration", "none");
    // $("#detailTab1").css("color", "#fff");
    // $("#detailTab1").css("background-color", "#08c");
    // $("#stateTab1").css("background-color", "#BFBFBF");
    // $("#stateTab1").css("color", "#eee");
    // $("#tab_state").css("display", "none");
    // $("#tab_detail").css("display", "block");
    // $("#state2").css("display", "none");
    // $("#detail2").css("display", "block");
    $.ajax({                              
        // async: true,
        url: path+"/alarmHist/queryAlarmHist3",
        data: {
			"farmId2" : $("#orgId" + (count0rg - 1)).val().split(",")[1],"houseId2":$("#orgId" + count0rg).val().split(",")[1],
			"codeName":name,"dateage":date_age,"beginTime":date.slice(0,10),"endTime":date.slice(0,10)
		},
        type: "POST",
        dataType: "json",
        cache: false,
        // timeout:50000,
        success: function (result) {
        	document.getElementById("beginTime").value = date.slice(0,10);
        	document.getElementById("endTime").value = date.slice(0,10);
        	document.getElementById("codeName").value = name;
        	var list = result.obj;
        	initTable("alarmHistDetail", getTableDataColumns("alarmHistDetail"), []);
            if(0 != list.length) {
                var dataJosn = $.parseJSON(JSON.stringify(list));
                $("#alarmHistDetailTable").bootstrapTable('load',dataJosn);
            } 
//            else{
//                initTableRow("alarmHistDetail", getTableEmptyRow("alarmHistDetail"));
//            }

        }
    });
		}

function forward2(){
	num =2;
	setTab(1);
    // $("#detailTab1").css("text-decoration", "none");
    // $("#detailTab1").css("color", "#fff");
    // $("#detailTab1").css("background-color", "#08c");
    // $("#stateTab1").css("background-color", "#BFBFBF");
    // $("#stateTab1").css("color", "#eee");
    // $("#tab_state").css("display", "none");
    // $("#tab_detail").css("display", "block");
    // $("#state2").css("display", "none");
    // $("#detail2").css("display", "block");
	var orgValue2 = $("#orgId" + count0rg).val();
	if("" != orgValue2 && null != orgValue2){
    reflushAlarmHist2();
	}
}
function forward3(){
	num=1;
	setTab(0);
    // $("#stateTab1").css("text-decoration", "none");
    // $("#stateTab1").css("color", "#fff");
    // $("#stateTab1").css("background-color", "#08c");
    // $("#detailTab1").css("background-color", "#BFBFBF");
    // $("#detailTab1").css("color", "#eee");
    // $("#tab_state").css("display", "block");
    // $("#tab_detail").css("display", "none");
    // $("#state2").css("display", "block");
    // $("#detail2").css("display", "none");
}

function getTableDataColumns(paramTypeSelectValue){
    if(paramTypeSelectValue == paramTypeList[0]) {
        return getAlarmHistTableDataColumns();
    }
    if(paramTypeSelectValue == paramTypeList[1]) {
        return getAlarmHistDetailTableDataColumns();
    }
}

function getAlarmHistTableDataColumns(){
    var dataColumns = [{
        field: "uid_num",
        title: "ID",
        visible: false
    }, {
        field: "farm_name",
        title: "农场",
        visible: false
    }, {
        field: "house_name",
        title: "栋舍",
        visible: false
    }, {
        field: "date_age",
        title: "日龄"
    }, {
        field: "high_temp_num",
        title: "高温报警"
       ,formatter: function(value,row,index){
    	   if(typeof(value)=='undefined'){
    		   return '-';
    	   }else{
    		   return '<a href="javascript:forward(' + row.farm_id + ',' + row.house_id  + ',' + row.date_age + ',\'' + row.batch_no +'\',\'' +row.date +'\',\'' +row.high_temp_name + '\')">'+value+'</a>';
    	   }
        }
    }, {
        field: "low_temp_num",
        title: "低温报警"
       ,formatter: function(value,row,index){
    	   if(typeof(value)=='undefined'){
    		   return '-';
    	   }else{
         return '<a href="javascript:forward(' + row.farm_id + ',' + row.house_id  + ',' + row.date_age + ',\'' + row.batch_no +'\',\'' +row.date +'\',\'' +row.low_temp_name + '\')">'+value+'</a>';
    	   }
    	 }
    }, {
        field: "point_temp_diff_num",
        title: "点温差报警"
       ,formatter: function(value,row,index){
    	   if(typeof(value)=='undefined'){
    		   return '-';
    	   }else{
         return '<a href="javascript:forward(' + row.farm_id + ',' + row.house_id  + ',' + row.date_age + ',\'' + row.batch_no +'\',\'' +row.date +'\',\'' +row.point_temp_diff_name + '\')">'+value+'</a>';
    	   }
    	}
    }, {
        field: "high_low_lux_num",
        title: "光照异常"
       ,formatter: function(value,row,index){
    	   if(typeof(value)=='undefined'){
    		   return '-';
    	   }else{
         return '<a href="javascript:forward(' + row.farm_id + ',' + row.house_id  + ',' + row.date_age + ',\'' + row.batch_no +'\',\'' +row.date +'\',\'' +row.high_low_lux_name + '\')">'+value+'</a>';
    	   }
    	}
    }, {
        field: "high_co2_num",
        title: "二氧化碳超高"
       ,formatter: function(value,row,index){
    	   if(typeof(value)=='undefined'){
    		   return '-';
    	   }else{
          return '<a href="javascript:forward(' + row.farm_id + ',' + row.house_id  + ',' + row.date_age + ',\'' + row.batch_no +'\',\'' +row.date +'\',\'' +row.high_co2_name + '\')">'+value+'</a>';
    	   }
    	}
    }, {
        field: "no_ele_num",
        title: "断电报警"
       ,formatter: function(value,row,index){
    	   if(typeof(value)=='undefined'){
    		   return '-';
    	   }else{
          return '<a href="javascript:forward(' + row.farm_id + ',' + row.house_id  + ',' + row.date_age + ',\'' + row.batch_no +'\',\'' +row.date +'\',\'' +row.no_ele_name + '\')">'+value+'</a>';
    	   }
    	}
    }, {
        field: "device_plit_num",
        title: "设备断开"
       ,formatter: function(value,row,index){
    	   if(typeof(value)=='undefined'){
    		   return '-';
    	   }else{
          return '<a href="javascript:forward(' + row.farm_id + ',' + row.house_id  + ',' + row.date_age + ',\'' + row.batch_no +'\',\'' +row.date +'\',\'' +row.device_plit_name + '\')">'+value+'</a>';
    	   }
    	}
    }];
    return dataColumns;
}

function getAlarmHistDetailTableDataColumns(){
    var dataColumns = [{
        field: "uid_num",
        title: "ID",
        visible: false
    }, {
        field: "date_age",
        title: "日龄"
    }, {
        field: "alarm_time",
        title: "报警时间"
    }, {
        field: "alarm_name2",
        title: "报警类型"
    }, {
        field: "alarm_Name",
        title: "报警明细"
    }, {
        field: "set_value",
        title: "目标值"
    }, {
        field: "actual_value",
        title: "实际值"
    }, {
        field: "continue_time",
        title: "持续时间"
    }, {
        field: "response_person",
        title: "执行人"
    }];
    return dataColumns;
}

function getTableEmptyRow(tableName){
    var count = $('#' + tableName + 'Table').bootstrapTable('getData').length;
    count += -10000;
    var emptyRow ;
    var defaultValue = "";
    if(tableName == paramTypeList[0]) {
        emptyRow = {id: count,
                    Day: defaultValue,
                    Target: defaultValue,
                    Heat: defaultValue,
                    Tunnel: defaultValue,
                    MinLevel: defaultValue,
                    MaxLevel: defaultValue
                    };
    }
    if(tableName == paramTypeList[1]) {
        emptyRow = {id: count,
            FromTime: defaultValue,
            ToTime: defaultValue,
            TunnelDiff: defaultValue,
            Till_Humid: defaultValue,
            On: defaultValue,
            Off: defaultValue
        };
    }

    return emptyRow;
}

function setTab(tabId){
	this.tabId = tabId;
	if(tabId==0){
        $("#stateTab1").css("text-decoration", "none");
        $("#stateTab1").css("background-color", "#08c");
        $("#tab_state").css("display", "block");

        $("#detailTab1").css("background-color", "#BFBFBF");
        $("#tab_detail").css("display", "none");

        document.getElementById("searchBar2").style.display = "none";
	}
	if(tabId==1){
        $("#stateTab1").css("background-color", "#BFBFBF");
        $("#tab_state").css("display", "none");

        $("#detailTab1").css("text-decoration", "none");
        $("#detailTab1").css("background-color", "#08c");
        $("#tab_detail").css("display", "block");

        document.getElementById("searchBar2").style.display = "block";
	}
}

