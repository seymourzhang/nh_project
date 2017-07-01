angular.module('myApp.survivalRate', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])

.controller('survivalRateCtrl', function($scope, $state) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	console.log("进去了么？");
	setLandscape(true,true);
	document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
	if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
	}else{
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
	}

	
	


	$scope.getChart = function(DateType){
		console.log($scope.sparraw_user_temp);
		var params = {
			"FarmType"     : $scope.sparraw_user_temp.farminfo.farm_type_id,
			"DateType"     : DateType,
			"FarmBreedId"  : $scope.sparraw_user_temp.houseinfos[0].BreedBatchNo,
			"CurveType"    : "08" ,//"01"-累计死淘率；"02"-产蛋率。"03"-均重。
			"FarmId"       : $scope.sparraw_user_temp.farminfo.id 
		};
		
		$scope.xCompany = "";
		if (DateType == '01') {
			$scope.xCompany = "日龄";
		}else{
			$scope.xCompany = "周龄";
		}

		console.log(params);
		Sparraw.ajaxPost('reportCurveMobile/reportCurve', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				var tempLineConfig = setChartParams(data.ResponseDetail.DCRate);
				var ttXData = null;
				ttXData = data.ResponseDetail.xAxis;
				Echart_initLine02(
					ttXData,
					tempLineConfig,
					"单位:%",
					[0,100],
					false,
					"",
					null,
					null,
					$scope.xCompany
				);
			}else{
				Echart_initLine02(['0'],[{"yName":"0","yData":['0']}]);
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
		});
	};

	$scope.chooseHouse = function(DateType){
		if (DateType == '01') {
			document.getElementById('dateBtn').style.background = "#A9A9A9";
			document.getElementById('weekBtn').style.background = "#3DCB64";
		}else{
			document.getElementById('dateBtn').style.background = "#3DCB64";
			document.getElementById('weekBtn').style.background = "#A9A9A9";
		}
		$scope.getChart(DateType);
	}

	setTimeout(function() {
		if ($scope.sparraw_user_temp.farminfo.farm_type_id == '3') {
			$scope.chooseHouse('01');
		}else{
			$scope.chooseHouse('02');
		}
	}, persistentData.horizontalTime);
})