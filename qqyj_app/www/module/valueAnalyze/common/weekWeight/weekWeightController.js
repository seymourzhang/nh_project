angular.module('myApp.weekWeight', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
//周体重
.controller("weekWeightCtrl",function($scope, $state, $http, $stateParams, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	
	setLandscape(true,true);
	document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
	if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
	}else{
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
	}

	$scope.getChart = function(DateType){
		console.log($scope.sparraw_user_temp.farminfo);
		var params = {
			"FarmType"     : $scope.sparraw_user_temp.farminfo.farm_type_id,
			"DateType"     : DateType,
			"FarmBreedId"  : $scope.sparraw_user_temp.houseinfos[0].BreedBatchNo,
			"CurveType"    : "03" ,//"01"-累计死淘率；"02"-产蛋率。"03"-均重。
			"FarmId"       : $scope.sparraw_user_temp.farminfo.id 
		};
		Sparraw.ajaxPost('reportCurveMobile/reportCurve', params, function(data){
			console.log(data);
			if (data.ResponseDetail.Result == "Success") {
				var tempLineConfig = setChartParams(data.ResponseDetail.DCRate);
				var ttXData = null;
				ttXData = data.ResponseDetail.xAxis;
				Echart_initLine02(
					ttXData,
					tempLineConfig,
					"均重:g",
					null,
					false,
					"",
					null,
					null,
					"周龄"
				);
			}else{
				Echart_initLine02(['0'],[{"yName":"0","yData":['0']}]);
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
			
		});
	};
	
	
	setTimeout(function() {
		if ($scope.sparraw_user_temp.farminfo.farm_type_id == '3') {
			$scope.getChart('01');
		}else{
			$scope.getChart('02');
		}
	}, persistentData.horizontalTime);
})