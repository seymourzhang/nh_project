angular.module('myApp.home', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 主页面
.controller("homeCtrl",function($scope, $state, $http, $ionicPopup,$ionicLoading,$cordovaFileTransfer,$cordovaFileOpener2,$timeout, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	setPortrait(true,true);
	
	if(navigator.userAgent.indexOf('Firefox') >= 0) {
		//火狐浏览器获取高度
		var MAXHEIGHT = document.documentElement.clientHeight;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - '45';
		document.getElementById('underlying_DIV').style.height = DIVHEIGHT + 'px';
	}else {
		//计算出手机屏幕高度
		var MAXHEIGHT = document.body.scrollHeight;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - '45';
		document.getElementById('underlying_DIV').style.height = DIVHEIGHT + 'px';
	}

	$scope.setData = function(){
		if (!$scope.sparraw_user_temp.farminfo.pinyin || $scope.sparraw_user_temp.farminfo.pinyin == "") {
			$scope.weatherUrl = "http://i.tianqi.com/index.php?c=code&id=2&num=2";
		}else{
			$scope.weatherUrl = "http://i.tianqi.com/index.php?c=code&id=2&num=2&py=" + $scope.sparraw_user_temp.farminfo.pinyin;
		}

		document.getElementById("weatherId").src = $scope.weatherUrl;



	};

	

	$scope.goDailyTable = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"dailyTable");
	}
	$scope.gobatchManage = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"batchManage");
	}

	$scope.goBreedingBook = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"breedingBook");
	}

	$scope.goEnvMonitoring = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"envMonitoring");
	}
	$scope.godataAnalyseTable = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"dataAnalyseTable");
	}
	$scope.goTaskRemind = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"taskRemind");
	}
	$scope.goEnvAnalyze = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"envAnalyze");
	}

	$scope.goProdReco = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"dailyReport");
	}

	$scope.goPushDetails = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"pushDetails");
	}
	
	$scope.goTasklist = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"tasklist");
	}

	$scope.goDailyTabIndex = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"dailyTabIndex");
	}

	$scope.goTabIndex = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"valueAnalyze");
	}

	$scope.pointDevelop = function() {
		biz_common_pointDevelop();
	    return;
	};
	
	
	
	$scope.judgeAlarm = function(){
		if (nh_alarm_nowNeedAlarm == "N") {
			document.getElementById('AlarmImg').setAttribute('src', 'img/newFolder/home/envMonitoring.png');
		}else{
			document.getElementById('AlarmImg').setAttribute('src', 'img/newFolder/home/envWarning.png');
		};
	}

	$scope.paramsCorrection = function(){
		if (Common_MOBILE_IMEI == "") {
			Common_MOBILE_IMEI = "测试数据";
		}
	}
	

	Common_checkForUpdate();//检查更新

	Alarm_beginAlarmTask($scope.sparraw_user_temp.userinfo.role);//获取报警信息

	$scope.queryAlarm = function(){
		
		$scope.paramsCorrection();
		
		var params = {
			"ImeiNo"       :  Common_MOBILE_IMEI,  
		};

		Sparraw.ajaxPost('autioSettingMobile/queryMobileAlarm', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				if (data.ResponseDetail.alarmStatus == "1") {
					nh_alarm_phoneAlarmToggle = "Y";
				}else{
					nh_alarm_phoneAlarmToggle = "N";
				}
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		});
	}
	
	$scope.queryAlarm();

	setTimeout(function () {
		if (nh_alarm_nowNeedAlarm == "N" || nh_alarm_phoneAlarmToggle == "N") {
			
		}else{
			app_confirm('发生报警，是否现在进行处理?','提示',null,function(buttonIndex){
			   	if(buttonIndex == 2){
			   		$state.go("alarmLogDelay");
			   	}
			});
		};		
	},1000);



	$scope.setData();
});