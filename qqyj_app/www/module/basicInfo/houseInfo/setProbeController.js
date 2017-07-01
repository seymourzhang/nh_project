 angular.module('myApp.setProbe', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 //栋舍添加
.controller("setProbeCtrl",function($scope, $state, $http, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	



	$scope.inquireDeviceData = function(){

		$scope.deviceData = {
			"farmId"    :"",
			"houseId"   :persistentData.setProbeHouse.id,
			"device_code": "",
	        "sensorInfo": []
		}

    	for (var i = 0; i < $scope.sparraw_user_temp.farmList.length; i++) {
    		if ($scope.sparraw_user_temp.farmList[i].farmId == $scope.sparraw_user_temp.farminfo.id) {
    			$scope.deviceData.farmId = $scope.sparraw_user_temp.farmList[i].farmId;
    		}
    	}

		var params = {
			"farm_id"  :  $scope.deviceData.farmId  ,
			"house_id" :  $scope.deviceData.houseId
		};
		Sparraw.ajaxPost('farmMobile/deviceQuery', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				if (data.ResponseDetail.sensorInfo.length == 0) {
					$scope.deviceData.sensorInfo.push({
                  		"sensor_no":1,
                  		"sensor_code":"1000",
                  		"show_column":"inside_temp1"//前区1
					},{
                  		"sensor_no":2,
                  		"sensor_code":"1000",
                  		"show_column":"inside_temp2"//前区2
					},{
                  		"sensor_no":3,
                  		"sensor_code":"1000",
                  		"show_column":"inside_temp10"//中区1
					},{
                  		"sensor_no":4,
                  		"sensor_code":"1000",
                  		"show_column":"inside_temp19"//后区1
					},{
                  		"sensor_no":5,
                  		"sensor_code":"1000",
                  		"show_column":"inside_temp20"//后区2
					});
				}else{
					$scope.deviceData.sensorInfo = data.ResponseDetail.sensorInfo;
				}
				$scope.deviceData.device_code = data.ResponseDetail.device_code;
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
		});
	}


	$scope.judgeSelected = function(item){
		if (item == 6) {
			return {"color":"#6E6E6E","pointer-events":"none"};
		}
	}

	/* 校验信息*/
	$scope.judgeParams = function(){
		var deleRepeatArr = [];
   		for (var i = 0; i < $scope.deviceData.sensorInfo.length; i++) {
   			deleRepeatArr.push($scope.deviceData.sensorInfo[i].show_column);
   		}
   		if (deleRepeatArr.length != Common_deleteRepeat(deleRepeatArr).length) {
   			app_alert('温度探头的位置重复！');
   			return false;
   		}
   		return true;
	}


	$scope.saveProbe = function(){
		if ($scope.judgeParams()) {
	   		var params = {
				"farm_id"      :  $scope.deviceData.farmId            ,
				"house_id"     :  $scope.deviceData.houseId           ,
				"device_code"  :  $scope.deviceData.device_code       ,
				"sensorInfo"   :  $scope.deviceData.sensorInfo
			};
			Sparraw.ajaxPost('farmMobile/deviceSave', params, function(data){
				if (data.ResponseDetail.Result == "Success") {
					app_alert(persistentData.setProbeHouse.name + "栋探头保存成功");

					biz_common_getLatestData($state,"updateBuildingInfo");
					
				}else{
					Sparraw.myNotice(data.ResponseDetail.Error);
				}
			});
	   	}

	}

	$scope.back = function(){
		$state.go("updateBuildingInfo");
	}




	$scope.inquireDeviceData();
})