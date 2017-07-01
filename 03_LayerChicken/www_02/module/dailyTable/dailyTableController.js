 angular.module('myApp.dailyTable', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 //生产报表
.controller("dailyTableCtrl",function($scope, $state, $ionicLoading, $http, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	setPortrait(true,true);

	$scope.goDailyReport = function(){
		$state.go("dailyReport");
	}

	$scope.goDailyStatistical = function(){
		$state.go("dailyStatistical");
	}
	$scope.goProductionDaily = function(){
		var param = {"fromPage":"dailyTable"};
		$state.go("productionDaily",param);
		
	}

	$scope.goWeekly = function(){
		$state.go("weekly");
	}



	$scope.goDocPlace = function(){
		$state.go("docPlace");
	}



})