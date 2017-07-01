angular.module('myApp.batchManage', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 农场管理
.controller("batchManageCtrl",function($scope, $ionicLoading, $state, $http, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	setPortrait(true,true);
	


	$scope.gonewBatch = function(){
		$state.go("newBatch");
	}
	$scope.goBatchClearing = function(){
		$state.go("batchClearing");
	}
	$scope.goProfitReport = function(){
		$state.go("profitReport");
	}
	$scope.goMoreBatchProfit = function(){
		$state.go("moreBatchProfit");
	}
	$scope.goMoreBatchClearing = function(){
		$state.go("moreBatchClearing");
	}
	$scope.goProductPerformStander = function(){
		$state.go("productPerformStander");
	}
	$scope.goBreedMonthProfit = function(){
		$state.go("breedMonthProfit");
	}
	$scope.goHistoryDataImport = function(){
		$state.go("historyDataImport");
	}	
	$scope.goMonthCost = function(){
		$state.go("monthCost");
	}
	$scope.goTotalProfit = function(){
		$state.go("totalProfit");
	}
	$scope.pointDevelop = function() {
		biz_common_pointDevelop();
		return;	
	};
})