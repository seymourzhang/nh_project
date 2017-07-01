angular.module('myApp.about', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
.controller("aboutCtrl",function($scope, $state, $http, AppData,$cordovaFileTransfer,$ionicLoading,$timeout,$cordovaFileOpener2) {
		Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	$scope.App_Version = CONFIG_App_Version;

	if (/android/.test(DeviInfo.DeviceType)){
		$scope.UpdateBtn = false;//显示	
	}else{
		$scope.UpdateBtn = true;//隐藏
	};

	$scope.dlAPK = function(){
		Common_checkForUpdate(function(){
     			Sparraw.myNotice("当前已是最新版本。");
     	});
	}
	$scope.queryVersion = function(){
		console.log("downLoad apk");
	};

	$scope.pointDevelop = function() {
		
		biz_common_pointDevelop();
		return;	
	};




	$scope.setLinkView = function(){
		$scope.showUrl = 'http://guide.agnet.com.cn';
		cordova.ThemeableBrowser.open($scope.showUrl, '_blank', {
		    statusbar: {
		        color: '#3dcb64'
		    },
		    toolbar: {
		        height: 44,
		        color: '#3dcb64'
		    },
		    title: {
		        color: '#FFFFFF',
		        showPageTitle: true
		    },
		    backButton: {
		        wwwImage: 'img/newFolder/public/BackArrow.png',
		        wwwImagePressed: 'img/newFolder/public/BackArrow.png',
		        wwwImageDensity: 2,
		        align: 'left',
		        event: 'backPressed'
		    },
    		backButtonCanClose: true
		}).addEventListener('loadstart', function(event) { 
			console.log("载入开始");
			console.log(event);
		}).addEventListener('loadstop', function(event) { 
			console.log("载入结束");
			console.log(event);
		}).addEventListener('loaderror', function(event) { 
			console.log("载入错误");
		}).addEventListener('backPressed', function(e) {
			console.log("返回");
		});
	}




})