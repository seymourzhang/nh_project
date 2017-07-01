angular.module('myApp.tempDiff', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])

.controller('tempDiffCtrl', function($scope, $state, $stateParams) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	setLandscape(true,true);

	$scope.initData = function(){
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
		}, 1500);
	};


	$scope.getBatch = function(){
		$scope.chartData.charType = "02";
		//$scope.chooseHouse($scope.sparraw_user_temp.houseinfos[0].id);
		$scope.defaultChart();
	};

	$scope.switchCondition = function(Condition){
		if ($scope.chartData.charType == "03") {
			$scope.chartData.charType = "02";
		}
		//$scope.chooseHouse($scope.sparraw_user_temp.houseinfos[0].id);
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

		
		//测试赋值
		FarmBreedId = $scope.sparraw_user_temp.farminfo.farmBreedBatchId;
		//获取图表数据
		var params = {
			"FarmBreedId"  :  FarmBreedId     ,  //农场批次id
			"DataType"     :  DataType        ,  //曲线图数据类型 01-日龄曲线；02-小时曲线；03-分钟曲线。
			"AgeFlag"      : AgeFlag          ,   //varchar型,"Y"-指定参数；"N"-没有指定参数
			"AgeRange"     : AgeRange         ,   //varchar型，日龄值
			"TimeFlag"     : TimeFlag         ,   //varchar型,"Y"-指定参数；"N"-没有指定参数
			"TimeRange"    : TimeRange            //varchar型，时间值
		};

		Sparraw.ajaxPost('layer_report/queryDiffCurve.action', params, function(data){
			if (data.ResponseDetail.Result == "Success") {

				$scope.setChartParams(data);
				Echart_initLine02(
					data.ResponseDetail.xAxis,
					$scope.tempLineConfig,
					"温度℃",
					[0,6],
					null,
					false,
					null,
					$scope.touchFun
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
				$scope.btnStyle();
			}else if (data.ResponseDetail.Result == "Fail") {
				Echart_initLine02(['0'],[{"yName":"0","yData":['0']}]);
				document.getElementById('rightBtn').style.background = "#ECECEC" ;
				document.getElementById('leftBtn').style.background  = "#ECECEC" ;
				document.getElementById('leftBtnName').innerHTML     = ""     ;
				document.getElementById('rightBtnName').innerHTML    = ""     ;
				$scope.leftBtnDisa = true;
				$scope.rightBtnDisa = true;
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			}else{
				Echart_initLine02(['0'],[{"yName":"0","yData":['0']}]);
				document.getElementById('rightBtn').style.background = "#ECECEC" ;
				document.getElementById('leftBtn').style.background  = "#ECECEC" ;
				document.getElementById('leftBtnName').innerHTML     = ""     ;
				document.getElementById('rightBtnName').innerHTML    = ""     ;
				$scope.leftBtnDisa = true;
				$scope.rightBtnDisa = true;
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		},$scope.timeoutHandle)
	};


	$scope.setChartParams = function(data){
		$scope.chartData.DataDate =  data.ResponseDetail.DataDate;
		$scope.chartData.AgeRange = data.ResponseDetail.data_age;
		$scope.chartData.NavTitle =  "(日龄:" + data.ResponseDetail.data_age + ")";
		//判断标题
		if ($scope.chartData.charType == "02") {
			if ($scope.chartData.AgeFlag == "N" && $scope.chartData.TimeFlag == "N") {
				$scope.chartData.firstTimeDate = data.ResponseDetail.DataDate;
			}
		}else if ($scope.chartData.charType == "01") {
			$scope.chartData.NavTitle =  "";
		}

		$scope.tempLineConfig = [];
		for (var i = 0; i < data.ResponseDetail.TempDatas.length; i++) {
			$scope.tempLineConfig.push({
				"yName"         :  data.ResponseDetail.TempDatas[i].TempAreaName  ,              
	            "yData"         :  data.ResponseDetail.TempDatas[i].TempDiffCurve ,            
	            "yType"         :  "line"                                         ,
	            "needSelected"  :  null                                           ,                
	            "yAxisIndex"    :  0                                  
			});
		}
	};

	$scope.touchFun = function(params){
		var xAxis_value = "";
		for (var i = 0; i < params.length; i++) {
			if (!params[i].data) {
				params[i].data = "-";
			}
			if(params[i].name != ""){
				xAxis_value = params[i].name;
			}
		}
        $scope.chartData.selectedTime = xAxis_value;//将x轴的字赋到判断向上向下的选择中
    	var res = '时间' + ' : ' + xAxis_value;//x轴的字
		var tempArray = [];
    	for (var i = 0; i < params.length; i++) {
            tempArray[i] = params[i];
    	}
    	for (var i = 0; i < tempArray.length; i++) {
    		//格式转换
        	if (i % 2 == 0) {
        		res += '<br/>' + tempArray[i].seriesName + "：" + tempArray[i].data+'&nbsp;&nbsp;';
        	}else{
        		res +=  tempArray[i].seriesName + "：" + tempArray[i].data+'&nbsp;&nbsp;';
        	}
        }
        //获取每次点击的时间
        if (xAxis_value == "") {//获取时间大于现在直接获取最新数据，否则传输正常数据
        	$scope.chartData.AgeFlag      = "N" ;
			$scope.chartData.TimeFlag     = "N" ;
        	$scope.chartData.selectedChartTime = "";
        }else{
        	$scope.chartData.AgeFlag      = "Y" ;
			$scope.chartData.TimeFlag     = "Y" ;
        	if ($scope.chartData.charType == '01') {
				$scope.chartData.AgeRange = xAxis_value;
			}else if ($scope.chartData.charType == '02') {
				$scope.chartData.TimeRange = xAxis_value;
			}
        }
        
    	return res;
	};

	$scope.timeoutHandle = function(){
		app_alert("连接超时。");
		Echart_initLine02(['0'],[{"yName":"0","yData":['0']}]);
		$scope.btnStyle();
	}

	$scope.rotating = function(direction){
		$scope.chartData.turn = direction;

		if ($scope.chartData.charType == "01" && $scope.chartData.turn == "up") {
			return Sparraw.myNotice("横轴已到最大粒度。");
		}else{
			if ($scope.chartData.charType == "03" && $scope.chartData.turn == "down") {
				return Sparraw.myNotice("横轴已到最小粒度。");
			}
		}

		//$scope.chartData.turn 转向
		//$scope.chartData.assignOn 是否选中
		//$scope.chartData.charType 曲线图类型
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
				$scope.chartData.TimeFlag  = "N" ;
				$scope.chartData.TimeRange = ""  ;
			}else if ($scope.chartData.charType == '02') {
				$scope.chartData.charType = "03";
				$scope.chartData.AgeFlag  = "Y" ;
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

	$scope.btnStyle = function(){
		if ($scope.chartData.charType == "02") {
			document.getElementById('leftBtnName').innerHTML     = "日龄"     ;
			document.getElementById('rightBtnName').innerHTML    = "分钟"     ;
			document.getElementById('rightBtn').style.background = "#33CD5F" ;
			document.getElementById('leftBtn').style.background  = "#33CD5F" ;
		}else{
			if ($scope.chartData.charType == "01" && $scope.chartData.turn == "up") {
				document.getElementById('leftBtnName').innerHTML     = ""        ;
				document.getElementById('rightBtnName').innerHTML    = "小时"     ;
				document.getElementById('leftBtn').style.background  = "#ECECEC" ;
				document.getElementById('rightBtn').style.background = "#33CD5F" ;
			}else{
				if ($scope.chartData.charType == "03" && $scope.chartData.turn == "down") {
					document.getElementById('leftBtnName').innerHTML     = "小时"     ;
					document.getElementById('rightBtnName').innerHTML    = ""        ;
					document.getElementById('rightBtn').style.background = "#ECECEC" ;
					document.getElementById('leftBtn').style.background  = "#33CD5F" ;
				}
			}
		}
	}






	$scope.initData();



})