var tickInterval=2;
var count0rg;
var num;

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
	 
	 $("#btn").click(function(){
		    var div = $("#waterRep");
		        div.show();
		})
	 
//	    $("#farmId").change(function() {
//	    	OrgSearch(count0rg,num);
//		});

//		$("#houseId").change(function() {
//			OrgSearch(count0rg,num);
//		});
		$("#batchId").change(function() {
			OrgSearch(count0rg,num);
		});
		$("#queryTime").change(function() {
			OrgSearch(count0rg,num);
		});
	 
		function time(){
		    var time=new Date();
		    var h=time.getFullYear();
		    var m=time.getMonth()+1;
		    var d=time.getDate();
		    return h+"-"+m+"-"+d;
		}
		document.getElementById("queryTime").value = time();
		
//	 setHouseId();
	 createChar();
});

function OrgSearch(count0rg,num){
	queryWaterReport();
}
 
//function setHouseId(){
//	$.ajax({
//		type : "post",
//		url : path + "/waterReport/getHouse",
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
//			if (houseId != ''){
//				$("#houseId").val(houseId);
//			}else {
//				$("#houseId").val(list[0].id);
//			}
//			setBatchId();
//			queryWaterReport();
//		}
//	})
//}

		

//function setBatchId(){
//	$.ajax({
//		type : "post",
//		url : path + "/waterReport/getBatch",
//		data : {
//			"farmId" : $("#farmId").val(),
//			"houseId" : $("#houseId").val()
//		},
//		dataType: "json",
//		success : function(result) {
//			var list = result.obj;
//			$("#batchId option").remove();
//			for (var i = 0; i < list.length; i++) {
//				$("#batchId").append("<option value=" + list[i].batch_no+ ">" + list[i].batch_no + "</option>");
//			}
//			$("#batchId").val(list[0].batch_no);
//			queryWaterReport();
//		}
//	})
//}

function waterUrl(){
	layer.open({
		type : 2,
		title : "请选择",
		skin : 'layui-layer-lan',
		area : [ '650px', '350px' ],
		content : path + "/waterReport/waterUrl?farmId="
				+ $("#farmId").val() + "&houseId=" + $("#houseId").val()
				+ "&batchId=" + $("#batchId").val()
	});
}

function  queryWaterReport(){
	$.ajax({
		type : "post",
		url : path + "/waterReport/queryWaterReport",
		data : {
			"farmId" : $("#orgId"+(count0rg-1)).val().split(",")[1],//农场
			"houseId" : $("#orgId"+count0rg).val().split(",")[1],//栋舍
			"batchId" : $("#batchId").val(),//批次
			"queryTime" : $("#queryTime").val(),//日期
			"buttonValue" : $("#buttonValue").val()//点击按钮值
		},
		dataType: "json",
		success : function(result) {
			 xNames = [];//清空X坐标
			 
			 waterConsumption = [];//实际耗水
			 setWaterDeprivation = [];//目标耗水
			 highWaterDeprivation = [];//高报耗水
			 lowWaterDeprivation = [];//低报耗水

			var list = result.obj;
			for (var i = 0; i < list.length; i++) {
				if(list[i].time == null){
					xNames.push(list[i].date.substring(0,10));
				}else{
					xNames.push(list[i].time+'点');
				}
				 waterConsumption.push(list[i].water_consumption);
				 setWaterDeprivation.push(list[i].set_water_deprivation);
				 highWaterDeprivation.push(list[i].high_water_deprivation);
				 lowWaterDeprivation.push(list[i].low_water_deprivation);
			}
			createChar();
		}
	})
}
function  buttonWeek(){
	$("#buttonValue").val("week");
	tickInterval=1;
	queryWaterReport();
}
function  buttonMonth(){
	tickInterval=2;
	$("#buttonValue").val("month");
	queryWaterReport();
	
}
function  buttonDay(){
	tickInterval=2;
	$("#buttonValue").val("day");
	queryWaterReport();
}


/**
 * 创建耗水量曲线图
 */
function createChar() {
	 $('#container').highcharts({
			chart: {   //图表选项
		          type: 'spline'
		      },
	        title: {
	            text: '',
	            x: -20 //center
	        },
	        xAxis: {
	        	tickInterval: tickInterval,
	            categories: xNames
	        },
	        credits: {  //版权链接，默认为true
	            enabled: false
	       },
	       height:300,
	        yAxis: {
	            title: {
	                text: '耗水量(L/10分钟)'
	            },
	            min: 0,
	            minorGridLineWidth: 0,
	            gridLineWidth: 0,  //网格(线)宽度
	            alternateGridColor: null,
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: 'L/10分钟'
	        },
	        plotOptions: {   //数据点选项
	        	allowPointSelect:false,  //是否允许使用鼠标选中数据点
	            spline: {
	                lineWidth: 1,
	                states: {
	                    hover: {
	                        lineWidth: 5
	                    }
	                },
	                marker: {
	                    enabled: false
	                }
	            }
	        },
	        legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'middle',
	            borderWidth: 0
	        },
	        series: [{
	            name: '目标耗水',
	            lineWidth: 1,
	            states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                marker: {
                    enabled: false
                },
	            data: setWaterDeprivation
	        }, {
	            name: '实际耗水',
	            lineWidth: 1.5,
	            states: {
                    hover: {
                        lineWidth: 1.5
                    }
                },
                marker: {
                    enabled: false
                },
	            data: waterConsumption
	        }, {
	            name: '高报耗水',
	            lineWidth: 2.0,
	            states: {
                    hover: {
                        lineWidth: 2.0
                    }
                },
                marker: {
                    enabled: false
                },
	            data: highWaterDeprivation
	        }, {
	            name: '低报耗水',
	            lineWidth: 2.5,
	            states: {
                    hover: {
                        lineWidth: 2.5
                    }
                },
                marker: {
                    enabled: false
                },
	            data: lowWaterDeprivation
	        }]
	    });
}


function btWeek() {
	$("#btValue").val("week");
	tickInterval = 1;
	queryWaterReport3();
}
function btMonth() {
	tickInterval = 2;
	$("#btValue").val("month");
	queryWaterReport3();

}
function btDay() {
	tickInterval = 2;
	$("#btValue").val("day");
	queryWaterReport3();
}

function queryWaterReport3() {
//	alert("aaaa");
	$.ajax({
		type : "post",
		url : path + "/waterReport/queryWaterReport",
		data : {
			"farmId" : $("#farmId2").val(),
			"houseId" : $("#houseId2").val(),
			"batchId" : $("#batchId2").val(),
			"queryTime" : $("#queryTime2").val(),// 日期
			"btValue" : $("#btValue").val()
		// 点击按钮值
		},
		dataType : "json",
		success : function(result) {
			var xpNames = new Array();// 清空X坐标
			
			var waterConsumptions = new Array();//实际耗水
			var setWaterDeprivations = new Array();//目标耗水
			var highWaterDeprivations = new Array();//高报耗水
			var lowWaterDeprivations = new Array();//低报耗水
			
			var lists = result.obj;
			for (var i = 0; i < lists.length; i++) {
				if (lists[i].time == null) {
					xpNames.push(lists[i].date.substring(0, 10));
				} else {
					xpNames.push(lists[i].time + '点');
				}
				 waterConsumptions.push(lists[i].water_consumption);
				 setWaterDeprivations.push(lists[i].set_water_deprivation);
				 highWaterDeprivations.push(lists[i].high_water_deprivation);
				 lowWaterDeprivations.push(lists[i].low_water_deprivation);
			}
			createChar3(xpNames,waterConsumptions,setWaterDeprivations,highWaterDeprivations,lowWaterDeprivations);
		}
	});
}

// 对比图
function createChar3(xNames,waterConsumption,setWaterDeprivation,highWaterDeprivation,lowWaterDeprivation){
	 $('#compareRep').highcharts({
			chart: {   //图表选项
		          type: 'spline'
		      },
	        title: {
	            text: '',
	            x: -20 //center
	        },
	        xAxis: {
	        	tickInterval: tickInterval,
	            categories: xNames
	        },
	        credits: {  //版权链接，默认为true
	            enabled: false
	       },
	       height:300,
	        yAxis: {
	            title: {
	                text: '耗水量(L/10分钟)'
	            },
	            min: 0,
	            minorGridLineWidth: 0,
	            gridLineWidth: 0,  //网格(线)宽度
	            alternateGridColor: null,
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: 'L/10分钟'
	        },
	        plotOptions: {   //数据点选项
	        	allowPointSelect:false,  //是否允许使用鼠标选中数据点
	            spline: {
	                lineWidth: 1,
	                states: {
	                    hover: {
	                        lineWidth: 5
	                    }
	                },
	                marker: {
	                    enabled: false
	                }
	            }
	        },
	        legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'middle',
	            borderWidth: 0
	        },
	        series: [{
	            name: '目标耗水',
	            lineWidth: 1,
	            states: {
                 hover: {
                     lineWidth: 1
                 }
             },
             marker: {
                 enabled: false
             },
	            data: setWaterDeprivation
	        }, {
	            name: '实际耗水',
	            lineWidth: 1.5,
	            states: {
                 hover: {
                     lineWidth: 1.5
                 }
             },
             marker: {
                 enabled: false
             },
	            data: waterConsumption
	        }, {
	            name: '高报耗水',
	            lineWidth: 2.0,
	            states: {
                 hover: {
                     lineWidth: 2.0
                 }
             },
             marker: {
                 enabled: false
             },
	            data: highWaterDeprivation
	        }, {
	            name: '低报耗水',
	            lineWidth: 2.5,
	            states: {
                 hover: {
                     lineWidth: 2.5
                 }
             },
             marker: {
                 enabled: false
             },
	            data: lowWaterDeprivation
	        }]
	    });
}

