angular.module('myApp.reportIndex', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//报表模块
.controller("reportIndexCtrl",function($scope, $state, $http, $ionicPopup,$ionicLoading,$cordovaFileTransfer,$cordovaFileOpener2,$timeout,AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	

	if(Common_isIOS()){
		persistentData.tabHeight = 60;
    	document.getElementById("tabView").setAttribute("class","tabs-icon-only tabs-positive tabs-top iOSBlueTab");
    	document.getElementById("backBtn").setAttribute("class","iOSbackBtn");
    }else{
    	persistentData.tabHeight = 30;
    }
    
})


