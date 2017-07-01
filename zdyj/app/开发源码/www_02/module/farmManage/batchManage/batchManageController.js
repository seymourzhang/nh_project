angular.module('myApp.batchManage', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 农场管理
.controller("batchManageCtrl",function($scope, $ionicLoading, $state, $http, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	setPortrait(true,true);

	$scope.goApparentTempCalc = function(){
		$state.go("apparentTempCalc");
	}

	$scope.goChickenWechat = function(){
		$state.go("chickenWechat");
	}

	$scope.goSyncProject = function(){
		app_alert("历史数据达到5批次后，才能使用同行对标功能")
	}


	$scope.pointDevelop = function() {
		biz_common_pointDevelop();
		return;	
	};
})