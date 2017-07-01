angular.module('myApp.pushDetails', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
.controller("pushDetailsCtrl",function($scope, $state, $http, $ionicPopup,$ionicLoading,$cordovaFileTransfer,$cordovaFileOpener2,$timeout, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	$scope.TempItems = [{
		"title":"主题1",
		"time":"信息来自于:系统通知 2016/10/12",
		"center":"推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容"
	},{
		"title":"主题2",
		"time":"信息来自于:系统通知 2016/11/11",
		"center":"推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容推送内容"
	},{
		"title":"主题3",
		"time":"信息来自于:系统通知 2016/12/25",
		"center":"推送内容推送内容推送内容推送内容推送内容推送内容推送内容"
	}];

})