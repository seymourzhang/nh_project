angular.module('myApp.layingRate', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//产蛋率
.controller('layingRateCtrl', function($scope, $state) {
	
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
		"ViewType":"01",
		"selectedHouse": "",
		"containBatchHouse":[]
    };
	//产蛋率
	var yLeftRange = null;
     $scope.changes1 = function(){
     	$scope.cullDeathRateData.xData=[];
     	console.log("产蛋率");
 		var params = { 
 			'IsNeedDelay':'Y',
        	'ViewType' : $scope.cullDeathRateData.ViewType,
        	'FarmBreedId' :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
        	"FarmId":$scope.sparraw_user_temp.farminfo.id       	
        }
		Sparraw.ajaxPost('layer_report/queryLayerCurve.action', params, function(data){
					if (data.ResponseDetail.Result == "Success" && data.ResponseDetail.xAxis.length != 0){
						 $scope.cullDeathRateData.yName =  [];
						 $scope.cullDeathRateData.yData =  []; 
						 var yMaxValue = null;
						 var yMinValue = null;
						for (var i = 0; i < data.ResponseDetail.LayerRate.length; i++) {
							$scope.cullDeathRateData.yName[i] =  data.ResponseDetail.LayerRate[i].HouseName;
							$scope.cullDeathRateData.yData[i] =  data.ResponseDetail.LayerRate[i].HouseDatas;
							var tempMax = Math.max.apply(null,$scope.cullDeathRateData.yData[i]);
							var tempMin = Math.min.apply(null,$scope.cullDeathRateData.yData[i]);
							if(yMaxValue == null || yMaxValue < tempMax){
								yMaxValue = tempMax ;
							}
							if(yMinValue == null || yMinValue > tempMin){
								yMinValue = tempMin ;
							}
						}
						if(yMaxValue > 100 ){
							yMaxValue = 100;
						}else{
							yMaxValue = null;
						}
						if(yMinValue < 70 ){
							yMinValue = 70;
						}else{
							yMinValue = null;
						}
						yLeftRange = [yMinValue,yMaxValue];
						console.log(yLeftRange);
					    $scope.cullDeathRateData.xData=data.ResponseDetail.xAxis;
						var Maxage = Math.max.apply(null, data.ResponseDetail.xAxis);
						console.log($scope.cullDeathRateData.xData);
						
					    if ($scope.cullDeathRateData.ViewType=="01"){
								
								if(Maxage < 88){
									var j = 88 - Maxage;
									var L = data.ResponseDetail.xAxis.length
								   for (var i = 0; i < j; i++) {
									 $scope.cullDeathRateData.xData[L+i] = Maxage + i + 1;
								   }
								}
						
                                var day_age_week  ="周龄";
					    }else{
							var L = data.ResponseDetail.xAxis.length
							if(L < 60){
								var j = 60 - L;
								for (var i = 0; i < j; i++) {
									$scope.cullDeathRateData.xData[L+i] = Maxage + i + 1;
								}
							}
                            var day_age_week  ="日龄"
					    }
						
						
					    console.log($scope.cullDeathRateData.xData);
						$scope.cullDeathRateData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						$scope.cullDeathRateData.selectUnit = '';

						var tempLineConfig = setChartParams(params.ViewType, $scope.cullDeathRateData.yData,$scope.cullDeathRateData.yName);
						Echart_initLine02($scope.cullDeathRateData.xData,
										  tempLineConfig,
										  '单位：％',
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
		$scope.changes1();
	}

	setTimeout(function() {
		$scope.chooseType('day');
	}, persistentData.horizontalTime);

})