var ar ;

$(document).ready(function() {
    ar = getInstance({入库:"tab_1"}, {iframe_tab_1:"carbonReport.cpt"});
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
// 	 $("#btn").click(function(){
// 		    var div = $("#carbonRep");
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
// 			queryCarbonReport();
// 		});
// 		$("#queryTime").change(function() {
// 			queryCarbonReport();
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
// //	 setHouseId();
// 	 createChar();
// 	});
//
// function OrgSearch(count0rg,num){
// 	queryCarbonReport();
// }
//
//
// //  function setHouseId(){
// //	 $.ajax({
// //		type : "post",
// //		url : path + "/carbonReport/getHouse",
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
// //			if (houseId != ''){
// //				$("#houseId").val(houseId);
// //			}else{
// //				$("#houseId").val(list[0].id);
// //			}
// //			setBatchId();
// //			queryCarbonReport();
// //		}
// //	})
// //}
//
// //function setBatchId(){
// //	$.ajax({
// //		type : "post",
// //		url : path + "/carbonReport/getBatch",
// //		data : {
// //			"farmId" : $("#farmId").val(),
// //			"houseId" : $("#houseId").val()
// //		},
// //		dataType: "json",
// //		success : function(result) {
// //			var list = result.obj;
// //			$("#batchId option").remove();
// //			for (var i = 0; i < list.length; i++) {
// //				$("#batchId").append("<option value=" + list[i].batch_no + ">" + list[i].batch_no + "</option>");
// //			}
// //			$("#batchId").val(list[0].batch_no);
// //			queryCarbonReport();
// //		}
// //	})
// //}
//
// function carbonUrl() {
// 	layer.open({
// 		type : 2,
// 		title : "请选择",
// 		skin : 'layui-layer-lan',
// 		area : [ '650px', '350px' ],
// 		content : path + "/carbonReport/carbonUrl?farmId="
// 				+ $("#farmId").val() + "&houseId=" + $("#houseId").val()
// 				+ "&batchId=" + $("#batchId").val()
// 	});
// }
//
// function  queryCarbonReport(){
// 	$.ajax({
// 		type : "post",
// 		url : path + "/carbonReport/queryCarbonReport",
// 		data : {
// 			"farmId" : $("#orgId"+(count0rg-1)).val().split(",")[1],//农场
// 			"houseId" : $("#orgId"+count0rg).val().split(",")[1],//栋舍
// 			"batchId" : $("#batchId").val(),//批次
// 			"queryTime" : $("#queryTime").val(),//日期
// 			"buttonValue" : $("#buttonValue").val()//点击按钮值
// 		},
// 		dataType: "json",
// 		success : function(result) {
// 			 xNames = [];//清空X坐标
//
// 			 co2 = [];//实际co2
// 			 highAlarmCo2 = [];//高报co2
//
// 			var list = result.obj;
// 			for (var i = 0; i < list.length; i++) {
// 				if(list[i].time == null){
// 					xNames.push(list[i].date.substring(0,10));
// 				}else{
// 					xNames.push(list[i].time+'点');
// 				}
// 				co2.push(list[i].co2);
// 				highAlarmCo2.push(list[i].high_alarm_co2);
// 			}
// 			createChar();
// 		}
// 	})
// }
// function  buttonWeek(){
// 	$("#buttonValue").val("week");
// 	tickInterval=1;
// 	queryCarbonReport();
// }
// function  buttonMonth(){
// 	tickInterval=2;
// 	$("#buttonValue").val("month");
// 	queryCarbonReport();
//
// }
// function  buttonDay(){
// 	tickInterval=2;
// 	$("#buttonValue").val("day");
// 	queryCarbonReport();
// }
//
//
// /**
//  * 创建二氧化碳曲线图
//  */
// function createChar() {
// 	 $('#container').highcharts({
// 		   chart: {
// 		          type: 'spline'
// 		      },
// 	        title: {
// 	            text: '',
// 	            x: -20 //center
// 	        },
// 	        xAxis: {
// 	        	tickInterval: tickInterval,
// 	            categories: xNames
// 	        },
// 	        credits: {
// 	            enabled: false
// 	       },
// 	       height:300,
// 	        yAxis: {
// 	            title: {
// 	                text: 'CO₂含量(ml/m³)'
// 	            },
// 	            min: 0,
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
// 	            valueSuffix: 'ml/m³'
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
// 	            name: '实际CO₂含量',
// 	            lineWidth: 1,
// 	            states: {
//                     hover: {
//                         lineWidth: 1
//                     }
//                 },
//                 marker: {
//                     enabled: false
//                 },
// 	            data: co2
// 	        }, {
// 	            name: '高报CO₂含量',
// 	            lineWidth: 1.5,
// 	            states: {
//                     hover: {
//                         lineWidth: 1.5
//                     }
//                 },
//                 marker: {
//                     enabled: false
//                 },
// 	            data: highAlarmCo2
// 	        }]
// 	    });
// }
//
// function btWeek() {
// 	$("#btValue").val("week");
// 	tickInterval = 1;
// 	queryCarbonReport3();
// }
// function btMonth() {
// 	tickInterval = 2;
// 	$("#btValue").val("month");
// 	queryCarbonReport3();
//
// }
// function btDay() {
// 	tickInterval = 2;
// 	$("#btValue").val("day");
// 	queryCarbonReport3();
// }
//
// function queryCarbonReport3() {
// 	$.ajax({
// 		type : "post",
// 		url : path + "/carbonReport/queryCarbonReport",
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
//
// 			var newCo2 = new Array();//实际co2
// 			var newHighAlarmCo2 = new Array();//高报co2
//
// 			var lists = result.obj;
// 			for (var i = 0; i < lists.length; i++) {
// 				if (lists[i].time == null) {
// 					xpNames.push(lists[i].date.substring(0, 10));
// 				} else {
// 					xpNames.push(lists[i].time + '点');
// 				}
// 				newCo2.push(lists[i].co2);
// 				newHighAlarmCo2.push(lists[i].high_alarm_co2);
// 			}
// 			createChar3(xpNames,newCo2,newHighAlarmCo2);
// 		}
// 	});
// }
//
// // 对比图
// function createChar3(xNames,co2,highAlarmCo2) {
// 	$('#compareRep').highcharts({
// 	   chart: {
// 	          type: 'spline'
// 	      },
//      title: {
//          text: '',
//          x: -20 //center
//      },
//      xAxis: {
//      	tickInterval: tickInterval,
//          categories: xNames
//      },
//      credits: {
//          enabled: false
//     },
//     height:300,
//      yAxis: {
//          title: {
//              text: 'CO₂含量(ml/m³)'
//          },
//          min: 0,
//          minorGridLineWidth: 0,
//          gridLineWidth: 0,
//          alternateGridColor: null,
//          plotLines: [{
//              value: 0,
//              width: 1,
//              color: '#808080'
//          }]
//      },
//      tooltip: {
//          valueSuffix: 'ml/m³'
//      },
//      plotOptions: {
//      	allowPointSelect:false,
//          spline: {
//              lineWidth: 1,
//              states: {
//                  hover: {
//                      lineWidth: 5
//                  }
//              },
//              marker: {
//                  enabled: false
//              }
//          }
//      },
//      legend: {
//          layout: 'vertical',
//          align: 'right',
//          verticalAlign: 'middle',
//          borderWidth: 0
//      },
//      series: [{
//          name: '实际CO₂含量',
//          lineWidth: 1,
//          states: {
//              hover: {
//                  lineWidth: 1
//              }
//          },
//          marker: {
//              enabled: false
//          },
//          data: co2
//      }, {
//          name: '高报CO₂含量',
//          lineWidth: 1.5,
//          states: {
//              hover: {
//                  lineWidth: 1.5
//              }
//          },
//          marker: {
//              enabled: false
//          },
//          data: highAlarmCo2
//      }]
//  });
//
// }
//
//
