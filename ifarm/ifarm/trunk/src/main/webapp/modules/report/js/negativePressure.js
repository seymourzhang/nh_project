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
		    var div = $("#negativeRep");
		        div.show();
		})
	 
	 
//	    $("#farmId").change(function() {
//		 setHouseId();
//		});
//
//		$("#houseId").change(function() {
//			setBatchId();
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
		
//		 setHouseId();
		 createChar();
});

function OrgSearch(count0rg,num){
	queryNegativePressure();
}
	 
//function setHouseId() {
//	$.ajax({
//		type : "post",
//		url : path + "/temProfile/getHouse",
//		data : {
//			"farmId" : $("#farmId").val()
//		},
//		dataType : "json",
//		success : function(result) {
//			var list = result.obj;
//			$("#houseId option").remove();
//			for (var i = 0; i < list.length; i++) {
//				$("#houseId").append(
//						"<option value=" + list[i].id + ">"
//								+ list[i].house_name + "</option>");
//			}
//			if (houseId != '') {
//				$("#houseId").val(houseId);
//			} else {
//				$("#houseId").val(list[0].id);
//			}
//
//			setBatchId();
//			queryNegativePressure();
//		}
//	})
//}

//function setBatchId() {
//	$.ajax({
//		type : "post",
//		url : path + "/temProfile/getBatch",
//		data : {
//			"farmId" : $("#farmId").val(),
//			"houseId" : $("#houseId").val()
//		},
//		dataType : "json",
//		success : function(result) {
//			var list = result.obj;
//			$("#batchId option").remove();
//			for (var i = 0; i < list.length; i++) {
//				$("#batchId").append(
//						"<option value=" + list[i].batch_no + ">"
//								+ list[i].batch_no + "</option>");
//			}
//			$("#batchId").val(list[0].batch_no);
//			queryNegativePressure();
//		}
//	})
//}


	 function negativeUrl() {
//		 alert("aaaadfsf");
		 layer.open({
				type : 2,
				title : "请选择",
				skin : 'layui-layer-lan',
				area : [ '650px', '350px' ],
				content : path + "/negativePressure/negativeUrl?farmId="
						+ $("#farmId").val() + "&houseId=" + $("#houseId").val()
						+ "&batchId=" + $("#batchId").val()
			});
		}
	 
function  queryNegativePressure(){
	$.ajax({
		type : "post",
		url : path + "/negativePressure/queryNegativePressure",
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
			 
			  negativePressure = [];//实际负压
			  highAlarmNegativePressure = [];//高报负压
			 
			var list = result.obj;
			for (var i = 0; i < list.length; i++) {
				if(list[i].time == null){
					xNames.push(list[i].date.substring(0,10));
				}else{
					xNames.push(list[i].time+'点');
				}
				negativePressure.push(list[i].negative_pressure);
				highAlarmNegativePressure.push(list[i].high_alarm_negative_pressure);
			}
			createChar();
		}
	});
}

function  buttonWeek(){
	$("#buttonValue").val("week");
	tickInterval=1;
	queryNegativePressure();
}

function  buttonMonth(){
	tickInterval=2;
	$("#buttonValue").val("month");
	queryNegativePressure();
	
}

function  buttonDay(){
	tickInterval=2;
	$("#buttonValue").val("day");
	queryNegativePressure();
}


/**
 * 创建负压曲线图
 */
function createChar() {
	 $('#container').highcharts({
			chart: {
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
	        credits: {
	            enabled: false
	       },
	       height:300,
	        yAxis: {
	            title: {
	                text: '负压(Pa)'
	            },
	            min: 0,
	            minorGridLineWidth: 0,
	            gridLineWidth: 0,
	            alternateGridColor: null,
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: 'Pa'
	        },
	        plotOptions: {
	        	allowPointSelect:false,
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
	            name: '实际负压',
	            lineWidth: 1,
	            states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                marker: {
                    enabled: false
                },
	            data: negativePressure
	        }, {
	            name: '高报负压',
	            lineWidth: 1.5,
	            states: {
                    hover: {
                        lineWidth: 1.5
                    }
                },
                marker: {
                    enabled: false
                },
	            data: highAlarmNegativePressure
	        }]
	    });
}


function btWeek() {
	$("#btValue").val("week");
	tickInterval = 1;
	queryNegativePressure3();
}
function btMonth() {
	tickInterval = 2;
	$("#btValue").val("month");
	queryNegativePressure3();

}
function btDay() {
	tickInterval = 2;
	$("#btValue").val("day");
	queryNegativePressure3();
}

function queryNegativePressure3() {
	$.ajax({
		type : "post",
		url : path + "/negativePressure/queryNegativePressure",
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
			
			var negativePressures = new Array();//实际负压
			var highAlarmNegativePressures = new Array();//高报负压
			  
			var lists = result.obj;
			for (var i = 0; i < lists.length; i++) {
				if (lists[i].time == null) {
					xpNames.push(lists[i].date.substring(0, 10));
				} else {
					xpNames.push(lists[i].time + '点');
				}
				negativePressures.push(lists[i].negative_pressure);
				highAlarmNegativePressures.push(lists[i].high_alarm_negative_pressure);
			}
			createChar3(xpNames,negativePressures,highAlarmNegativePressures);
		}
	});
}

// 对比图
function createChar3(xNames,negativePressure,highAlarmNegativePressure) {
	$('#compareRep').highcharts({
		chart: {
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
        credits: {
            enabled: false
       },
       height:300,
        yAxis: {
            title: {
                text: '负压(Pa)'
            },
            min: 0,
            minorGridLineWidth: 0,
            gridLineWidth: 0,
            alternateGridColor: null,
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: 'Pa'
        },
        plotOptions: {
        	allowPointSelect:false,
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
            name: '实际负压',
            lineWidth: 1,
            states: {
                hover: {
                    lineWidth: 1
                }
            },
            marker: {
                enabled: false
            },
            data: negativePressure
        }, {
            name: '高报负压',
            lineWidth: 1.5,
            states: {
                hover: {
                    lineWidth: 1.5
                }
            },
            marker: {
                enabled: false
            },
            data: highAlarmNegativePressure
        }]
    });
}


