var ar ;

$(document).ready(function() {
    ar = getInstance({入库:"tab_1"}, {iframe_tab_1:"temProfile.cpt"});
    ar.initToolBarFarm();
});

// var tickInterval=2;
// var count0rg;
// var num;
//
// $(document).ready(function(){
// 	 $('.date-picker').datepicker({
//      	language: 'zh-CN',
//          autoclose: true,
//          todayHighlight: true
//      });
//
// 	 var win_h = $(window).height()-208;
// 	 $("#user_date_table").css("min-height",win_h);
// 	 $("#page-content").css("min-height",win_h);
// 	 $("#container").css("height",win_h-50);
//
//
// 	 $("#btn").click(function(){
// 		    var div = $("#tempRep");
// 		        div.show();
// 		})
//
// //	   $("#farmId").change(function() {
// //		 setHouseId();
// //		});
// //
// //		$("#houseId").change(function() {
// //			setBatchId();
// //		});
// 		$("#batchId").change(function() {
// 			OrgSearch(count0rg,num);
// 		});
// 		$("#queryTime").change(function() {
// 			OrgSearch(count0rg,num);
// 		});
//
// 		function time(){
// 		    var time=new Date();
// 		    var h=time.getFullYear();
// 		    var m=time.getMonth()+1;
// 		    var d=time.getDate();
// 		    return h+"-"+m+"-"+d;
// 		}
// 		document.getElementById("queryTime").value = time();
//
//
// //	 setHouseId();
// //	 setBatchId();
// 	 createChar();
//
// });
//
// function OrgSearch(count0rg,num){
// 	queryTemProfile(count0rg);
// }
//
// //function setHouseId(){
// //	$.ajax({
// //		type : "post",
// //		url : path + "/temProfile/getHouse",
// //		data : {
// //			"farmId" : $("#farmId").val()
// //		},
// //		dataType: "json",
// //		success : function(result) {
// //			var list = result.obj;
// //			$("#houseId option").remove();
// //			for (var i = 0; i < list.length; i++) {
// //				$("#houseId").append("<option value=" + list[i].id + ">" + list[i].house_name+ "</option>");
// //			}
// //			if(houseId!=''){
// //				$("#houseId").val(houseId);
// //			}else{
// //				$("#houseId").val(list[0].id);
// //			}
// //
// //			setBatchId();
// //			queryTemProfile();
// //		}
// //	})
// //}
//
// //function setBatchId(){
// //	alert(count0rg);
// //	$.ajax({
// //		type : "post",
// //		url : path + "/temProfile/getBatch",
// //		data : {
// //			"farmId" :$("#orgId"+(count0rg-1)).val().split(",")[1],//农场
// //			"houseId" : $("#orgId"+count0rg).val().split(",")[1]//栋舍
// //		},
// //		dataType: "json",
// //		success : function(result) {
// //			var list = result.obj;
// //			$("#batchId option").remove();
// //			for (var i = 0; i < list.length; i++) {
// //				$("#batchId").append("<option value=" + list[i].batch_no+ ">" + list[i].batch_no + "</option>");
// //			}
// //			$("#batchId").val(list[0].batch_no);
// //			queryTemProfile(count0rg);
// //		}
// //	})
// //}
// function  queryTemProfile(count0rg){
// 	$.ajax({
// 		type : "post",
// 		url : path + "/temProfile/queryTemProfile",
// 		data : {
// //			"farmId" : $("#farmId").val(),//农场
// //			"houseId" : $("#houseId").val(),//栋舍
// 			"farmId" :$("#orgId"+(count0rg-1)).val().split(",")[1],//农场
// 			"houseId" : $("#orgId"+count0rg).val().split(",")[1],//栋舍
// 			"batchId" : $("#batchId").val(),//批次
// 			"queryTime" : $("#queryTime").val(),//日期
// 			"buttonValue" : $("#buttonValue").val()//点击按钮值
// 		},
// 		dataType: "json",
// 		success : function(result) {
// 			 xNames = [];//清空X坐标
// 			 insideTemp1=[];
// 			 insideTemp2=[];
// 			 insideTemp3=[];
// 			 insideTemp4=[];
//
// 			 insideSetTemp = [];//清空目标温度
// //			 insideAvgTemp = [];//清空实际温度
// 			 highAlarmTemp = [];//清空高报温度
// 			 lowAlarmTemp = [];//清空低报温度
// 			 outsideTemp = [];//清空室外温度
// 			var list = result.obj;
// 			for (var i = 0; i < list.length; i++) {
// 				if(list[i].time == null){
// 					xNames.push(list[i].date.substring(0,10));
// 				}else{
// 					xNames.push(list[i].time+'点');
// 				}
// 				insideTemp1.push(list[i].inside_temp1);
// 				insideTemp2.push(list[i].inside_temp2);
// 				insideTemp3.push(list[i].inside_temp3);
// 				insideTemp4.push(list[i].inside_temp4);
// 				insideSetTemp.push(list[i].inside_set_temp);
// //				insideAvgTemp.push(list[i].inside_avg_temp );
// 				highAlarmTemp.push(list[i].high_alarm_temp );
// 				lowAlarmTemp.push(list[i].low_alarm_temp );
// 				outsideTemp.push(list[i].outside_temp );
// 			}
// 			createChar();
// 		}
// 	})
// }
// function  buttonWeek(){
// 	$("#buttonValue").val("week");
// 	tickInterval=1;
// 	queryTemProfile(count0rg);
// }
// function  buttonMonth(){
// 	tickInterval=2;
// 	$("#buttonValue").val("month");
// 	queryTemProfile(count0rg);
//
// }
// function  buttonDay(){
// 	tickInterval=2;
// 	$("#buttonValue").val("day");
// 	queryTemProfile(count0rg);
// }
//
//
// /**
//  * 创建温度曲线图
//  */
// function createChar() {
// 	 $('#container').highcharts({
// 			 chart: {
// 		          type: 'spline'
// 		      },
// 	        title: {
// 	            text: '',
// 	            x: -20 //center
// 	        },
// 	        xAxis: {
// 	        	tickInterval: tickInterval,
// //	        	tickPositions: [0, 2, 4, 6, 8],
// 	            categories: xNames
// 	        },
// 	        credits: {
// 	            enabled: false
// 	       },
// 	       height:300,
// 	        yAxis: {
// 	            title: {
// 	                text: '温度'
// 	            },
// 	            min: -15,
// 	            minorGridLineWidth: 0,
// 	            gridLineWidth: 0,
// 	            alternateGridColor: null,
// 	            plotLines: [{
// 	                value: 0,
// 	                width: 1,
// 	                color: '#808080'
// 	            }]
// 	        },
// 	        tooltip: {
// 	            valueSuffix: '°C'
// 	        },
// 	        plotOptions: {
// 	        	allowPointSelect:false,
// 	            spline: {
// 	                lineWidth: 1,
// 	                states: {
// 	                    hover: {
// 	                        lineWidth: 5
// 	                    }
// 	                },
// 	                marker: {
// 	                    enabled: false
// 	                }
// 	            }
// 	        },
// 	        legend: {
// 	            layout: 'vertical',
// 	            align: 'right',
// 	            verticalAlign: 'middle',
// 	            borderWidth: 0
// 	        },
// 	        series: [{
// 	            name: '目标温度',
// 	            lineWidth: 1,
// 	            states: {
//                     hover: {
//                         lineWidth: 1
//                     }
//                 },
// 	            marker: {
//                     enabled: false
//                 },
// 	            data: insideSetTemp
// 	        }, {
// 	            name: '探头1',
// 	            lineWidth: 1.5,
// 	            states: {
//                     hover: {
//                         lineWidth: 1.5
//                     }
//                 },
// 	            marker: {
//                     enabled: false
//                 },
// 	            data: insideTemp1
//
//
//
// 	        },{
// 	            name: '探头2',
// 	            lineWidth: 1.5,
// 	            states: {
//                     hover: {
//                         lineWidth: 1.5
//                     }
//                 },
// 	            marker: {
//                     enabled: false
//                 },
// 	            data: insideTemp2
//
//
//
// 	        },{
// 	            name: '探头3',
// 	            lineWidth: 1.5,
// 	            states: {
//                     hover: {
//                         lineWidth: 1.5
//                     }
//                 },
// 	            marker: {
//                     enabled: false
//                 },
// 	            data: insideTemp3
//
//
//
// 	        },{
// 	            name: '探头4',
// 	            lineWidth: 1.5,
// 	            states: {
//                     hover: {
//                         lineWidth: 1.5
//                     }
//                 },
// 	            marker: {
//                     enabled: false
//                 },
// 	            data: insideTemp4
//
//
//
// 	        },{
// 	            name: '高报温度',
// 	            lineWidth: 2,
// 	            states: {
//                     hover: {
//                         lineWidth: 2
//                     }
//                 },
// 	            marker: {
//                     enabled: false
//                 },
// 	            data: highAlarmTemp
// 	        }, {
// 	            name: '低报温度',
// 	            lineWidth: 2.5,
// 	            states: {
//                     hover: {
//                         lineWidth: 2.5
//                     }
//                 },
// 	            marker: {
//                     enabled: false
//                 },
// 	            data: lowAlarmTemp
// 	        }, {
// 	            name: '室外温度',
// 	            lineWidth: 3,
// 	            states: {
//                     hover: {
//                         lineWidth: 3
//                     }
//                 },
// 	            marker: {
//                     enabled: false
//                 },
// 	            data: outsideTemp
// 	        }]
// 	    });
// }
//
// function btWeek() {
// 	$("#btValue").val("week");
// 	tickInterval = 1;
// 	queryTemProfile3();
// }
// function btMonth() {
// 	tickInterval = 2;
// 	$("#btValue").val("month");
// 	queryTemProfile3();
//
// }
// function btDay() {
// 	tickInterval = 2;
// 	$("#btValue").val("day");
// 	queryTemProfile3();
// }
//
// function queryTemProfile3() {
// 	$.ajax({
// 		type : "post",
// 		url : path + "/temProfile/queryTemProfile",
// 		data : {
// 			"farmId" : $("#farmId2").val(),
// 			"houseId" : $("#houseId2").val(),
// 			"batchId" : $("#batchId2").val(),
// 			"queryTime" : $("#queryTime2").val(),// 日期
// 			"btValue" : $("#btValue").val()
// 		// 点击按钮值
// 		},
// 		dataType : "json",
// 		success : function(result) {
// 			var xpNames = new Array();// 清空X坐标
// 			var insideTemp01 = new Array();// X坐标
// 			var insideTemp02 = new Array();// X坐标
// 			var insideTemp03 = new Array();// X坐标
// 			var insideTemp04 = new Array();// X坐标
//
// 			var insideSetTemps = new Array();// 清空目标温度
// 			var highAlarmTemps = new Array();// 清空高报温度
// 			var lowAlarmTemps = new Array();// 清空低报温度
// 			var outsideTemps = new Array();// 清空室外温度
//
// 			var lists = result.obj;
// 			for (var i = 0; i < lists.length; i++) {
// 				if (lists[i].time == null) {
// 					xpNames.push(lists[i].date.substring(0, 10));
// 				} else {
// 					xpNames.push(lists[i].time + '点');
// 				}
// 				insideTemp01.push(lists[i].inside_temp1);
// 				insideTemp02.push(lists[i].inside_temp2);
// 				insideTemp03.push(lists[i].inside_temp3);
// 				insideTemp04.push(lists[i].inside_temp4);
// 				insideSetTemps.push(lists[i].inside_set_temp);
// 				highAlarmTemps.push(lists[i].high_alarm_temp);
// 				lowAlarmTemps.push(lists[i].low_alarm_temp);
// 				outsideTemps.push(lists[i].outside_temp);
// 			}
// 			createChar3(xpNames, insideSetTemps, insideTemp01, insideTemp02,insideTemp03, insideTemp04, highAlarmTemps, lowAlarmTemps,outsideTemps);
// 		}
// 	});
// }
//
// // 对比图
// function createChar3(xNames, insideSetTemp, insideTemp1, insideTemp2,insideTemp3, insideTemp4, highAlarmTemp, lowAlarmTemp, outsideTemp) {
// 	$('#compareRep').highcharts({
// 		chart : {
// 			type : 'spline'
// 		},
// 		title : {
// 			text : '',
// 			x : -20
// 		// center
// 		},
// 		xAxis : {
// 			tickInterval : tickInterval,
// 			// tickPositions: [0, 2, 4, 6, 8],
// 			categories : xNames
// 		},
// 		credits : {
// 			enabled : false
// 		},
// 		height : 300,
// 		yAxis : {
// 			title : {
// 				text : '温度(°C)'
// 			},
// 			min : -15,
// 			minorGridLineWidth : 0,
// 			gridLineWidth : 0,
// 			alternateGridColor : null,
// 			plotLines : [ {
// 				value : 0,
// 				width : 1,
// 				color : '#808080'
// 			} ]
// 		},
// 		tooltip : {
// 			valueSuffix : '°C'
// 		},
// 		plotOptions : {
// 			allowPointSelect : false,
// 			spline : {
// 				lineWidth : 1,
// 				states : {
// 					hover : {
// 						lineWidth : 5
// 					}
// 				},
// 				marker : {
// 					enabled : false
// 				}
// 			}
// 		},
// 		legend : {
// 			layout : 'vertical',
// 			align : 'right',
// 			verticalAlign : 'middle',
// 			borderWidth : 0
// 		},
// 		series : [ {
// 			name : '目标温度',
// 			lineWidth : 1,
// 			states : {
// 				hover : {
// 					lineWidth : 1
// 				}
// 			},
// 			marker : {
// 				enabled : false
// 			},
// 			data : insideSetTemp
// 		}, {
// 			name : '探头1',
// 			lineWidth : 1.5,
// 			states : {
// 				hover : {
// 					lineWidth : 1.5
// 				}
// 			},
// 			marker : {
// 				enabled : false
// 			},
// 			data : insideTemp1
//
// 		}, {
// 			name : '探头2',
// 			lineWidth : 1.5,
// 			states : {
// 				hover : {
// 					lineWidth : 1.5
// 				}
// 			},
// 			marker : {
// 				enabled : false
// 			},
// 			data : insideTemp2
//
// 		}, {
// 			name : '探头3',
// 			lineWidth : 1.5,
// 			states : {
// 				hover : {
// 					lineWidth : 1.5
// 				}
// 			},
// 			marker : {
// 				enabled : false
// 			},
// 			data : insideTemp3
//
// 		}, {
// 			name : '探头4',
// 			lineWidth : 1.5,
// 			states : {
// 				hover : {
// 					lineWidth : 1.5
// 				}
// 			},
// 			marker : {
// 				enabled : false
// 			},
// 			data : insideTemp4
//
// 		}, {
// 			name : '高报温度',
// 			lineWidth : 2,
// 			states : {
// 				hover : {
// 					lineWidth : 2
// 				}
// 			},
// 			marker : {
// 				enabled : false
// 			},
// 			data : highAlarmTemp
// 		}, {
// 			name : '低报温度',
// 			lineWidth : 2.5,
// 			states : {
// 				hover : {
// 					lineWidth : 2.5
// 				}
// 			},
// 			marker : {
// 				enabled : false
// 			},
// 			data : lowAlarmTemp
// 		}, {
// 			name : '室外温度',
// 			lineWidth : 3,
// 			states : {
// 				hover : {
// 					lineWidth : 3
// 				}
// 			},
// 			marker : {
// 				enabled : false
// 			},
// 			data : outsideTemp
// 		} ]
// 	});
// }
//
//
