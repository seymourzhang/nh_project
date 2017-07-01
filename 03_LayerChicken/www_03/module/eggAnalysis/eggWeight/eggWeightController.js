angular.module('myApp.eggWeight', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//蛋重
.controller('eggWeightCtrl', function($scope, $state) {
	
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    setLandscape(true,true);

    document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
	if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
	}else{
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
	}
	document.getElementById('main').style.height = persistentData.analysisChartH + '%';

	$scope.cullDeathRateData = {
		"xData"       :  [] ,
		"yData"       :  [] ,
		"yName"       :  [] ,
		"yColor"      :  [] ,
		"hiddenPara"  :  [] ,
		"selectedBatch"  :  [] ,
		"selectedBatchKey"  :  [] ,
		"farmBatch"  :  [] ,
		"BatchDate"  :  [] ,
		"farmBatch"  :  [] ,
		"selectUnit" :  "" ,
		"compareType":  "" ,
		"ViewType":"02",
		"selectedHouse": "",
		"containBatchHouse":[]
    };
    
  	//蛋重
	$scope.changes2 = function(){
		console.log("蛋重");
		$scope.cullDeathRateData.xData = [];
			var params = { 
				'IsNeedDelay':'Y',
				'ViewType' : $scope.cullDeathRateData.ViewType,
		    	'FarmBreedId' :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
		    	"FarmId":$scope.sparraw_user_temp.farminfo.id   
			}
			if(params.ViewType==null){
                  params.ViewType='02';
			}
			Sparraw.ajaxPost('layer_report/queryEggWeightCurve.action', params, function(data){
						if (data.ResponseDetail.Result == "Success" && data.ResponseDetail.xAxis.length != 0){
						 $scope.cullDeathRateData.yName =  [];
						 $scope.cullDeathRateData.yData =  []; 
						 var ageLeng =[];
						for (var i = 0; i < data.ResponseDetail.EggAvgWeight.length; i++) {
							$scope.cullDeathRateData.yName[i] =  data.ResponseDetail.EggAvgWeight[i].HouseName;
							$scope.cullDeathRateData.yData[i] =  data.ResponseDetail.EggAvgWeight[i].HouseDatas;
						}
					    var Maxage = Math.max.apply(null, data.ResponseDetail.xAxis);
					    $scope.cullDeathRateData.xData=data.ResponseDetail.xAxis;
					     console.log($scope.cullDeathRateData.xData);
					    var j = 60-data.ResponseDetail.xAxis.length;
					    var L = data.ResponseDetail.xAxis.length
					    if($scope.cullDeathRateData.xData.length<60){
                          for (var i = 0; i < j; i++) {
                            $scope.cullDeathRateData.xData[L+i] = Maxage + i;
                          }
					    }
					    if ($scope.cullDeathRateData.ViewType=="01") {
                                var day_age_week  ="周龄"
					    }else{
                                var day_age_week  ="日龄"
					    }
					    console.log($scope.cullDeathRateData.xData);
						$scope.cullDeathRateData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						$scope.cullDeathRateData.selectUnit = '';

						var tempLineConfig = setChartParams(params.ViewType, $scope.cullDeathRateData.yData,$scope.cullDeathRateData.yName);
						Echart_initLine02($scope.cullDeathRateData.xData,
										  tempLineConfig,
										  '克/枚',
										  null,
										  false,
										  null,
										  null,
										  null,
										  day_age_week
					    );
					}else if(data.ResponseDetail.Result == 'Fail'){
						Sparraw.myNotice("暂无信息");
					}else{
						Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
					};
			});
	}
	  


	$scope.chooseType = function(type){
		if (type == 'day') {
			document.getElementById('dayBtn').style.background = "#A9A9A9";
			document.getElementById('weekBtn').style.background = "#439AFC";
			$scope.cullDeathRateData.ViewType = "02";
		}else{
			document.getElementById('dayBtn').style.background = "#439AFC";
			document.getElementById('weekBtn').style.background = "#A9A9A9";
			$scope.cullDeathRateData.ViewType = "01";
		}
		$scope.changes2();
	}

	setTimeout(function() {
		$scope.chooseType('day');
	}, persistentData.horizontalTime);

	
})