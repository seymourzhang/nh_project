angular.module('myApp.salesAnalyze', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//销售分析曲线
.controller("salesAnalyzeCtrl",function($scope, $state,$ionicLoading, $http, $stateParams, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    $scope.goDataAnalyseTable = function(){
    	if($stateParams.lastPage == "eggSellsReport"){
			// 销售日报
			$state.go("eggSellsReport");
		}else if($stateParams.lastPage == "eggSellsList"){
			// 销售记录
			$state.go("eggSellsList");
		}else if($stateParams.lastPage == "eggSellsReportTable"){
			// 销售报表
			$state.go("eggSellsReportTable");
		}else{
			$state.go("dataAnalyseTable");
		}	
    }

    if(navigator.userAgent.indexOf('Android') >= 0) {
		//火狐浏览器获取高度
		var MAXHEIGHT = document.documentElement.clientWidth;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('salesAnalyze_DIV').style.height = DIVHEIGHT + 'px';
	}else if(navigator.userAgent.indexOf('Firefox') >= 0) {
		//火狐浏览器获取高度
		var MAXHEIGHT = document.documentElement.clientHeight;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('salesAnalyze_DIV').style.height = DIVHEIGHT + 'px';
	}else {
		//计算出手机屏幕高度
		var MAXHEIGHT = document.body.clientWidth;

		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('salesAnalyze_DIV').style.height = DIVHEIGHT + 'px';
	}


	$scope.salesAnalData = {
 		"FarmId"       :  $scope.sparraw_user_temp.farminfo.id,
    	"FarmBreedId"  :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
        "ViewType"     :  "02"  ,
        "xAxis"        :  []    ,
        "SaleDatas"    :  {},
		"ViewTypes"	 	:{"01":"按周龄","02":"按日期"}
    }
    //表数据
    var tFarmCode ;
	var tBatchNum;
	var myChart ;
	var option ;
	var LData = [];//左边的
	var RData = [];//右边的
	var yData1 = [];//销售量data
	var yData2 = [];//蛋价曲线data
	var xData = [];//日龄
	var  tTitleName = "销售分析曲线";
	var  tLegend = ['销售量','蛋价'];
	var  serialsName2 = '销售量';
	var  serialsName3 = "蛋价曲线";

	var priceType = "01";
	
    $scope.updateData = function(){
	    tFarmCode ;
		tBatchNum;
		myChart ;
		option ;
		LData = [];//左边的
		RData = [];//右边的
		yData1 = $scope.salesAnalData.SaleDatas.SaleAmount;//销售量data
		var vals = [];
		for(var val in yData1){
			var value = $scope.salesAnalData.SaleDatas.SaleAmount[val];
			//console.log("zhongliang:" + $scope.salesAnalData.SaleDatas.SaleAmount[val]);
			vals.push(value);
		}
		yData1 = vals;
		yData2 = $scope.salesAnalData.SaleDatas.SalePrice;//蛋价曲线data
		xData = $scope.salesAnalData.xAxis;//日龄
		tTitleName = "销售分析曲线";
		tLegend = ['销售量','蛋价'];
		serialsName2 = '销售量';
		serialsName3 = "蛋价";
    }


	$scope.goecharts = function (){ require.config({
	  paths: {
	      echarts: 'js/echarts-2.2.7'
	  }
	});
	require(
	  [
	      'echarts',
	      'echarts/chart/line',   
	  ],
	  function (ec) {
	      myChart = ec.init(document.getElementById('main'));
	      option = {
				tooltip : {
	                trigger: 'axis',

	                textStyle:{
	                  fontSize:13
	            	},
	            	backgroundColor: 'rgba(96,96,96,0.5)' ,//显示框的颜色
	                formatter: function (params) {
						var viewType = $scope.salesAnalData.ViewType;
						var str = "周龄";
						if (viewType == "02") {
							str = "日龄";
						}
	                	var res = str + '：' +params[0].name;
						var v1 = params[0].value*2;
						if (v1 != "-"){
							//v1 = v1.toFixed(2);
						}
						
						var v2 = params[1].value;
						if (v2 != "-"){
							//v2 = v2.toFixed(1);
						}
						
						var unit = "元/斤";
						
						if(priceType == "02"){
							unit = v2 + "元/斤";
						}else{
							unit = v2 + "元/箱";
						}
						
						
	                	res += '<br/>  销售量 : ' + v1 + '';
	                	res += '<br/>  蛋价: ' + v2;
						
	                	return res;
	                    
	                }
	            },
	            grid:
	            {
	                x:50,
	                y:30,
	                x2:50,
	                y2:30,
	                borderColor:'#BBB'
	            },
	            legend: {
	                data:tLegend
	            },
	            xAxis : [
	                {

	                    type : 'category',
	                    data : xData,
	                    splitLine:{show: false},
	                    nameLocation:'start',
	                    axisLine:{
	                    	lineStyle:{
	                    		width:1
	                    	}
	                    }
	                }
	            ],
	            yAxis : [
	                {	name:"斤",
	                	position:'left',
	                    type : 'value',
	                    data : LData,
	                    nameTextStyle:{
	                      fontSize:13,
						  color: '#000000'
	                    },
	                    axisLabel : {
	                          formatter: '{value}'
	                      },
	                    scale: true,
	                    axisLine:{
	                    	lineStyle:{
	                    		//color: '#000000',
	                    		width:1
	                    	}
	                    }
	                },
	                {	name:"元/斤",
	                	position:'right',
 	                    type : 'value',
 	                    data : RData,
	                    nameTextStyle:{
	                      fontSize:13,
						  color: '#000000'
	                    },
	                    axisLabel : {
	                          show:true,
	                          formatter: '{value}'
	                      },
	                    splitLine : false,
	                    scale: true,
	                    axisLine:{
	                    	lineStyle:{
	                    		width:1
	                    	}
	                    }
	                }
	            ],
	            series : [
	                {
	                  name:serialsName2,
	                  type:'line',
	                  smooth:false,
	                  //symbol:'none',
	                  symbolSize:0,
	                  data:yData1,
	                    itemStyle: {
			                normal: {
			                    lineStyle: {
			                        width:1.5
			                    }
			                }
			            }
	                },
	                {
	                  name:serialsName3,
	                  type:'line',
	                  yAxisIndex: 1,
	                  data:yData2,
	                  smooth:true,
	                   // symbol:'none',
	                   symbolSize:0,
	                    itemStyle: {
			                normal: {
			                    lineStyle: {
			                        width:1.5
			                    }
			                }
			            }
	                }
	            ]
	        };
	      myChart.setOption(option);
	      window.onresize = myChart.resize;
	  }
	);

	}




	var ArrList=[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33];
	function getArrayItems(arr, num) {
	    //新建一个数组,将传入的数组复制过来,用于运算,而不要直接操作传入的数组;
	    var temp_array = new Array();
	    for (var index in arr) {
	        temp_array.push(arr[index]);
	    }
	    //取出的数值项,保存在此数组
	    var return_array = new Array();
	    for (var i = 0; i<num; i++) {
	        //判断如果数组还有可以取出的元素,以防下标越界
	        if (temp_array.length>0) {
	            //在数组中产生一个随机索引
	            var arrIndex = Math.floor(Math.random()*temp_array.length);
	            //将此随机索引的对应的数组元素值复制出来
	            return_array[i] = temp_array[arrIndex];
	            //然后删掉此索引的数组元素,这时候temp_array变为新的数组
	            temp_array.splice(arrIndex, 1);
	        } else {
	            //数组中数据项取完后,退出循环,比如数组本来只有10项,但要求取出20项.
	            break;
	        }
	    }
	    return return_array;
	}




	$scope.inquire = function(){
		var params = {
			'IsNeedDelay':'Y',
			"FarmId"       :  $scope.salesAnalData.FarmId         ,
			"ViewType"     :  $scope.salesAnalData.ViewType       ,
			"FarmBreedId"  :   $scope.salesAnalData.FarmBreedId  
		};
		Sparraw.ajaxPost('layer_report/querySaleEgg.action', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				var temp = [];
				for(var i = 0 ; i < data.ResponseDetail.xAxis.length; i++){
					if(data.ResponseDetail.xAxis[i] > 88){
						break;
					}
					temp[i] = data.ResponseDetail.xAxis[i];
				}
				$scope.salesAnalData.xAxis =   temp ;
				$scope.salesAnalData.SaleDatas = data.ResponseDetail.SaleDatas ;
				priceType = data.ResponseDetail.priceType;
				$scope.updateData();
    			$scope.goecharts();
			}else if (data.ResponseDetail.Result == "Fail"){
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		});
		$scope.goecharts();
	}
	
	$scope.changeViewType = function(){
		var viewType = $scope.salesAnalData.ViewType;
		$scope.inquire();
	}

	

	setLandscape(true,true);
	$scope.changeViewType();


})

