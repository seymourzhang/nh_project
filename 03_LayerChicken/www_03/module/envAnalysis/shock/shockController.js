angular.module('myApp.shock', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])

.controller('shockCtrl', function($scope, $state) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	setLandscape(true,true);

	$scope.initData = function(){
		$scope.batchIdDiv = false;
		document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
		if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
			document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
		}else{
			document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
		}
		$scope.chartData = {
			"selectedHouseId"    :  ""  ,
			"selectedBatchId"    :  ""  ,
			"breedIdArray"       :  []  ,
			"farmId"             :  ""  ,
			"charType"           :  ""  ,//图表类型
			"assignOn"           :  ""  ,//是否选中
			"selectedTime"       :  ""  ,//选择时间
			"turn"               :  ""  ,//转向
			"DataDate"           :  ""  ,//服务器返回的时间
			"NavTitle"           :  ""  ,
			"firstTimeDate"      :  ""  ,
			"AgeFlag"            :  ""  ,//varchar型,"Y"-指定参数；"N"-没有指定参数
			"AgeRange"           :  ""  ,//varchar型，日龄值
			"TimeFlag"           :  ""  ,//varchar型,"Y"-指定参数；"N"-没有指定参数
			"TimeRange"          :  ""   //varchar型，时间值
		};
		$scope.chartData.farmId = $scope.sparraw_user_temp.farminfo.id;

		setTimeout(function() {
			$scope.getBatch();
			//$scope.defaultChart();
		}, 1500);
	};


	$scope.getBatch = function(){
		//获取批次信息
		var params = {
			"FarmId"   :  $scope.chartData.farmId
		};
		Sparraw.ajaxPost('sys/code/getFarmBreedData.action', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				//获取批次列表
				$scope.chartData.breedIdArray = data.ResponseDetail.FarmBreedIdArray;
				//选中默认搜索批次
				for(var key in $scope.chartData.breedIdArray){
					$scope.chartData.selectedBatchId = key;
				}
				//选中默认栋舍批次号
				$scope.chartData.selectedHouseId = $scope.sparraw_user_temp.userinfo.houses[0].HouseId                   ;
				$scope.chartData.charType = "01";
				//栋舍按钮变色
				$scope.chooseHouse($scope.sparraw_user_temp.houseinfos[0].id);
				$scope.defaultChart();
			}else if (data.ResponseDetail.Result == "Fail") {
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		});
	};

	$scope.switchCondition = function(Condition){
		if ($scope.chartData.charType == "03") {
			$scope.chartData.charType = "02";
		}
		$scope.defaultChart();
	}

	$scope.defaultChart = function(){
		$scope.chartData.AgeFlag      = "N" ;
		$scope.chartData.AgeRange     = "" ;
		$scope.chartData.TimeFlag     = "N" ;
		$scope.chartData.TimeRange    = "" ;

		$scope.getChart($scope.chartData.selectedBatchId  ,
						$scope.chartData.selectedHouseId  ,
						$scope.chartData.charType         ,
						$scope.chartData.AgeFlag          ,
						$scope.chartData.AgeRange         ,
						$scope.chartData.TimeFlag         ,
						$scope.chartData.TimeRange);
	}

	//批次、栋舍、类型、是否选中、选择时间
	$scope.getChart = function(FarmBreedId,HouseId,DataType,AgeFlag,AgeRange,TimeFlag,TimeRange){
		//获取图表数据
		var params = {
			"FarmBreedId"  :  FarmBreedId     ,  //农场批次id
			"HouseId"      :  HouseId         ,  //栋舍id
			"DataType"     :  DataType        ,  //曲线图数据类型 01-日龄曲线；02-小时曲线；03-分钟曲线。
			"ReqFlag"      : AgeFlag          ,   //varchar型,"Y"-指定参数；"N"-没有指定参数
			"DataRange"    : TimeRange            //varchar型，时间值
		};

		Sparraw.ajaxPost('layer_report/queryTempKLine.action', params, function(data){
			if (data.ResponseDetail.Result == "Success") {

				$scope.setChartParams(data);

				Echart_initLine03(
					data.ResponseDetail.xAxis,
					$scope.yData
				);
				
				if ($scope.chartData.charType == "01") {
					$scope.chartData.AgeFlag      = "N" ;
					$scope.chartData.AgeRange     = ""  ;
					$scope.chartData.TimeFlag     = "N" ;
					$scope.chartData.TimeRange    = ""  ;
				}else if ($scope.chartData.charType == "02") {
					
				}else{

				}
				$scope.leftBtnDisa = false;
				$scope.rightBtnDisa = false;
				//$scope.btnStyle();
			}else if (data.ResponseDetail.Result == "Fail") {
				Echart_initLine02(['0'],[{"yName":"0","yData":['0']}]);
				$scope.leftBtnDisa = true;
				$scope.rightBtnDisa = true;
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			}else{
				Echart_initLine02(['0'],[{"yName":"0","yData":['0']}]);
				$scope.leftBtnDisa = true;
				$scope.rightBtnDisa = true;
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		},$scope.timeoutHandle)
	};


	$scope.setChartParams = function(data){

		$scope.yData = [];

		for (var i = 0; i < data.ResponseDetail.THDatas.length; i++) {
			if (data.ResponseDetail.THDatas[i].MaxTemp == 0) {
				data.ResponseDetail.THDatas[i].MaxTemp = "-";
			}
			if (data.ResponseDetail.THDatas[i].MinTemp == 0) {
				data.ResponseDetail.THDatas[i].MinTemp = "-";
			}
		}

		for (var i = 0; i < data.ResponseDetail.THDatas.length; i++) {
			$scope.yData.push([
					data.ResponseDetail.THDatas[i].MaxTemp,
					data.ResponseDetail.THDatas[i].MinTemp,
					data.ResponseDetail.THDatas[i].MaxTemp,
					data.ResponseDetail.THDatas[i].MinTemp
				]);
		}
		

		

		//判断标题
		if ($scope.chartData.charType == "02") {
			$scope.chartData.NavTitle = "(日龄：" + data.ResponseDetail.data_age + ")";
			if ($scope.chartData.AgeFlag == "N" && $scope.chartData.TimeFlag == "N") {
				$scope.chartData.firstTimeDate = data.ResponseDetail.DataDate;
			}
		}else if ($scope.chartData.charType == "01") {
			$scope.chartData.NavTitle =  "";
		}
	};

	

	$scope.timeoutHandle = function(){
		app_alert("连接超时。");
		Echart_initLine02(['0'],[{"yName":"0","yData":['0']}]);
	}

	$scope.rotating = function(direction){
		$scope.chartData.turn = direction;

		if ($scope.chartData.charType == "01" && $scope.chartData.turn == "up") {
			return Sparraw.myNotice("横轴已到最大粒度。");
		}else{
			if ($scope.chartData.charType == "02" && $scope.chartData.turn == "down") {
				return Sparraw.myNotice("横轴已到最小粒度。");
			}
		}

		/*$scope.chartData.turn 转向
		$scope.chartData.assignOn 是否选中
		$scope.chartData.charType 曲线图类型*/
		if ($scope.chartData.turn == "up") {
			if ($scope.chartData.charType == '02') {
				$scope.chartData.charType = "01";
				$scope.chartData.AgeFlag      = "N" ;
				$scope.chartData.AgeRange     = "" ;
				$scope.chartData.TimeFlag     = "N" ;
				$scope.chartData.TimeRange    = "" ;
			}else if ($scope.chartData.charType == '03') {
	      		$scope.chartData.charType     = "02";
	      		$scope.chartData.TimeFlag = "N";
	      		$scope.chartData.TimeRange    = "" ;
			}
		}else{
			if ($scope.chartData.charType == '01') {
				$scope.chartData.charType  = "02";
				var TempSelectYear = "";
			    var TempSelectDate = $scope.chartData.TimeRange.substr(0, 5);
			    var TempjudgeDate = "2000" + "-" + $scope.chartData.firstTimeDate.substr(5, 5);
			    var TempSelectTime = "2000" + "-" + TempSelectDate;
			    var oDate1 = new Date(TempjudgeDate);
				var oDate2 = new Date(TempSelectTime);
				if(oDate1.getTime() < oDate2.getTime()){
			        TempSelectYear = Number($scope.chartData.firstTimeDate.substr(0, 4)) - 1;
			    }else{
			    	TempSelectYear = Number($scope.chartData.firstTimeDate.substr(0, 4));
			    }
			    $scope.chartData.TimeRange = TempSelectYear + "-" + TempSelectDate;
			}
		}
		$scope.getChart($scope.chartData.selectedBatchId  ,
						$scope.chartData.selectedHouseId  ,
						$scope.chartData.charType         ,
						$scope.chartData.AgeFlag          ,
						$scope.chartData.AgeRange         ,
						$scope.chartData.TimeFlag         ,
						$scope.chartData.TimeRange);
	};

	$scope.initData();

	$scope.chooseHouse = function(item){
		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			document.getElementById("IDis"+$scope.sparraw_user_temp.houseinfos[i].id).style.background = "#33cd5f";
		}
		document.getElementById("IDis"+item).style.background = "#A9A9A9";
		$scope.chartData.selectedHouseId = item;
		$scope.switchCondition("House");
	}


/*************线性图表数据****************/

function Echart_initLine03(
	xShaftSumData  ,    //x轴数据 必填项                       数据样式 = ['1','2','3','4','5'];
	yData          
) {
    
    var myChart = echarts.init(document.getElementById('main'));
	var option = {
	    legend: {
	        data: ['温度震荡数据'],
	        inactiveColor: '#FFF',
	        textStyle: {
	            color: '#FFF'
	        }
	    },
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            animation: true
	        },
	        textStyle: {
	            fontSize: 13
	        },
	        backgroundColor: 'rgba(96,96,96,0.5)',
	        //显示框的颜色
	        formatter: function (params) {
	        	var xAxis_value = "";
	        	var res = "";
		        res += params[0].name;
		        res += "<br/>";
		        res += "最高温度：" + params[0].data[0]  + '&nbsp;&nbsp;';
		        res += "<br/>";
		        res += "最低温度：" + params[0].data[1]  + '&nbsp;&nbsp;';
		        res += "<br/>";

		        xAxis_value = params[0].name;
		        $scope.chartData.selectedTime = xAxis_value;//将x轴的字赋到判断向上向下的选择中
		        //获取每次点击的时间
		        if (xAxis_value == "") {//获取时间大于现在直接获取最新数据，否则传输正常数据
		        	$scope.chartData.AgeFlag      = "N" ;
					$scope.chartData.TimeFlag     = "N" ;
		        	$scope.chartData.selectedChartTime = "";
		        }else{
		        	$scope.chartData.AgeFlag      = "Y" ;
					$scope.chartData.TimeFlag     = "Y" ;
		        	if ($scope.chartData.charType == '01') {
						$scope.chartData.TimeRange = xAxis_value;
					}else if ($scope.chartData.charType == '02') {
						$scope.chartData.TimeRange = xAxis_value;
					}
		        }
	        	return res;
	        }
	    },
	    xAxis: {
	        type: 'category',
	        data: xShaftSumData,
	        axisLine: { lineStyle: { color: '#8392A5' } }
	    },
	    yAxis: {
	        scale: true,
	        axisLine: { lineStyle: { color: '#8392A5' } },
	        splitLine: { show: true }//分割线
	    },
	    grid: {
	        x: 35,
	        y: 20,
	        x2: 10,
	        y2: 20
	    },  
	    series: [
	        {
	            type: 'candlestick',
	            name: '温度震荡数据',
	            data: yData,
	            itemStyle: {
	                normal: {
	                    color: '#FFF',
	                    color0: '#B22222',
	                    borderColor: '#B22222',
	                    borderColor0: '#B22222'
	                }
	            }
	        }
	    ]
	};
    myChart.setOption(option);
    
    window.onresize = function() {};

}
















})