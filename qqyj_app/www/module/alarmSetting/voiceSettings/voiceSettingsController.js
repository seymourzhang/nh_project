angular.module('myApp.voiceSettings', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//语音设置
.controller("voiceSettingsCtrl",function($scope, $state, $http, $ionicPopup,$ionicModal,$ionicLoading, $stateParams, AppData) {
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    $ionicLoading.show();
    setTimeout(
		function (){
			setPortrait(true,true);//进入时竖屏
			$ionicLoading.hide();

			setTimeout(
				function (){

					console.log($scope.sparraw_user_temp);

					$scope.setData();
					$scope.queryDatatt();
				}
			,500);
		}
	,1000);

    $scope.setData = function(){
    	$scope.voiceSetData = {
    		"housesVoiceInfo":[]
    	}
    }






    $scope.voiceSwitch = function(item){
    	console.log(item.status);
    }

    $scope.changeResponseA = function(item){
    	console.log(item);
    }

    $scope.changeResponseB = function(item){
    	console.log(item);
    }

    $scope.changeResponseC = function(item){
    	console.log(item);
    }

    $scope.queryDatatt = function(){
    	
		var params = {
			"FarmId":$scope.sparraw_user_temp.farminfo.id,
			"RemindMethod":"0",
		 };
		Sparraw.ajaxPost('autioSettingMobile/queryInfo', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				$scope.voiceSetData.housesVoiceInfo = data.ResponseDetail.houseAlarmSetting;
				for (var i = 0; i < $scope.voiceSetData.housesVoiceInfo.length; i++) {
					if ($scope.voiceSetData.housesVoiceInfo[i].status == "Y") {
						$scope.voiceSetData.housesVoiceInfo[i].status = true;
					}else{
						$scope.voiceSetData.housesVoiceInfo[i].status = false;
					}
				}
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		});
    }


    


    $scope.saveFun = function(item){
    	
    	console.log(item);
    	//添加权限
    	if ($scope.sparraw_user_temp.Authority.AlarmSet === "all") {
			
		}else{
			return app_alert("该用户无此操作权限。");
		};
    	
		var tUserAlarmers = [];

		var switchState = "N";
		if (item.status || item.status == "Y") {
			switchState = "Y";

			if (item.alarmers.length < 3) {
	    		return Sparraw.myNotice("您需要选择三位报警人，用来接听语音提醒。");
	    	}else{
	    		for (var i = 0; i < item.alarmers.length; i++) {
	    			console.log(item.alarmers[i]);
		    		if (!item.alarmers[i] || item.alarmers[i].userId == "" || !item.alarmers[i].userId) {
		    			return Sparraw.myNotice("您需要选择三位报警人，用来接听语音提醒。");
		    		}else{

		    		}
		    	}
	    	}

	    	
			tUserAlarmers = [{"userId":item.alarmers[0].userId,"userOrder":1,"userType":0},
							 {"userId":item.alarmers[1].userId,"userOrder":2,"userType":0},
						     {"userId":item.alarmers[2].userId,"userOrder":3,"userType":0}];

		}else{
			switchState = "N";
		}
		var params = {
			"FarmId"       : $scope.sparraw_user_temp.farminfo.id  ,
			"HouseId"      : item.HouseId                          ,
			"RemindMethod" : "0"                                   ,
			"status"       : switchState                           ,
		    "alarmers"     : tUserAlarmers
		 };
		Sparraw.ajaxPost('autioSettingMobile/saveInfo', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				Sparraw.myNotice(item.HouseName+"栋保存成功。");
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		},function(){
			Sparraw.myNotice("提交失败");
		});
	}


	$scope.openFun = function(){
	  	$scope.modalDIV.show();
    }

    $scope.closeFun = function(){
    	$scope.modalDIV.hide();
    }



	$ionicModal.fromTemplateUrl('useHelp.html', function(modal) {  
	    $scope.modalDIV = modal;
	}, {  
	    scope: $scope,  
	    animation: 'slide-in-up'
	});

})


