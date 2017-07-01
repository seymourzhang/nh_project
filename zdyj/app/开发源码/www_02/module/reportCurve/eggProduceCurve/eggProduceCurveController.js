angular.module('myApp.eggProduceCurve', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
//产蛋率曲线
.controller("eggProduceCurveCtrl",function($scope, $state, $http, $stateParams, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	
	setLandscape(true,true);
	document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
	if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
	}else{
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
	}

	$scope.getChart = function(){
		console.log($scope.sparraw_user_temp.farminfo);
		var params = {
			"CurveType"    : "02" ,//"01"-累计死淘率；"02"-产蛋率。"03"-均重。
			"FarmId"       : $scope.sparraw_user_temp.farminfo.id 
		};
		console.log(params);
		Sparraw.ajaxPost('reportCurveMobile/reportCurve', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				var tempLineConfig = setChartParams(data.ResponseDetail.DCRate);
				var ttXData = null;
				ttXData = data.ResponseDetail.xAxis;

				Echart_initLine02(
					ttXData,
					tempLineConfig,
					"产蛋率:%",
					null,
					false,
					"",
					null,
					null,
					"周龄"
				);
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
			
		});
	};

	$scope.getChart();
})