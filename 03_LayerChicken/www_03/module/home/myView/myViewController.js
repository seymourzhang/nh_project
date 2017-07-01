angular.module('myApp.myView', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
.controller("myViewCtrl",function($scope, $state,$ionicPopup, $http, AppData,$cordovaFileTransfer,$ionicLoading,$timeout,$cordovaFileOpener2) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	console.log($scope.sparraw_user_temp);
	setPortrait(true,true);//竖屏

	$scope.showConfirm = function() {
          app_confirm('是否要退出该用户?','提示',null,function(buttonIndex){
                   if(buttonIndex == 2){
                        biz_common_userLogout();
                        $state.go("landingPage");
                   }
              }); 
   };


   $scope.goFarmRegistered = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"farmRegistered");
	}

	$scope.goBuildingTable = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"buildingTable");
	}

	$scope.goEmployeesTable = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"employeesTable");
	}
	$scope.goMyStandard = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"myStandard");
	}
	$scope.goPasswordModify = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"modifyUserInfo");
	}
	$scope.goProductPerformStander = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"productPerformStander");
	}
	$scope.goAlarmSettings = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"alarmSettings");
	}
	$scope.goFeedBack = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"feedBack");
	}
	$scope.goTips = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"tips");
	}

	$scope.checkBaseInfo = function() {
		
		if(!$scope.sparraw_user_temp.farminfo){
			app_alert("请先完善农场信息.")
			return false;
		}
		
		if(!$scope.sparraw_user_temp.userinfo.houses){
			app_alert("请先完善栋舍信息.")
			return false;
		}
		return true;
	};
	
	$scope.pointDevelop = function() {
		biz_common_pointDevelop();
		return;	
	};

	$scope.queryAlarm = function(){
		var params = {
			"ImeiNo"       :  Common_MOBILE_IMEI,  
		};
		Sparraw.ajaxPost('sys/alarm/queryMobileAlarm.action', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				$scope.alarm = data.ResponseDetail.alarm;
				if (data.ResponseDetail.alarmStatus == "1") {
					$scope.alarm = true;
					$scope.alarmStatus = 1;
				}else{
					$scope.alarm = false;
					$scope.alarmStatus = 0;
				}
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		});
	}

	$scope.alarmChanger = function(){
		if ($scope.alarm) {
			$scope.alarm = false;
			$scope.alarmStatus = 0;
		}else{
			$scope.alarm = true;
			$scope.alarmStatus = 1;
		}
		console.log($scope.alarmStatus);
		var params = {
			"ImeiNo": Common_MOBILE_IMEI,
        	"alarmStatus": $scope.alarmStatus
		};
		Sparraw.ajaxPost('sys/alarm/setMobileAlarm.action', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				if ($scope.alarmStatus == 1) {
					Sparraw.myNotice("已开启报警");
				}else{
					Sparraw.myNotice("已关闭报警");
				}
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		});
	}

	$scope.queryAlarm();
})