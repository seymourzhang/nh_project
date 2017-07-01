 angular.module('myApp.updateBuildingInfo', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 // 栋舍修改
.controller("updateBuildingInfoCtrl",function($scope, $state, $http, $stateParams, AppData,$ionicPopup) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	
	$scope.setData = function(){
		//查询
		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			if($scope.sparraw_user_temp.houseinfos[i].id == $stateParams.buildingID){
				$scope.tempVar.houseTemp = JSON.parse(JSON.stringify($scope.sparraw_user_temp.houseinfos[i]));
				console.log($scope.sparraw_user_temp.houseinfos[i]);
			}
		}
		$scope.deviceData = {
			"farmId"    :"",
			"houseId"   :"",
			"device_code": "",
	        "sensorInfo": []
		}

		$scope.backBtn    =  false  ;
		$scope.cancelBtn  =  true   ;
		$scope.visible    =  false  ;

		$scope.inquireDeviceData();
	}


	$scope.inquireDeviceData = function(){
    	for (var i = 0; i < $scope.sparraw_user_temp.farmList.length; i++) {
    		if ($scope.sparraw_user_temp.farmList[i].farmId == $scope.sparraw_user_temp.farminfo.id) {
    			$scope.deviceData.farmId = $scope.sparraw_user_temp.farmList[i].farmId;
    		}
    	}
    	$scope.deviceData.houseId = $scope.tempVar.houseTemp.id;

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


	$scope.startEdit = function(){
		//添加权限
		if ($scope.sparraw_user_temp.Authority.MasterData == "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		$scope.visible    =!  $scope.visible   ;
		$scope.sheerDiv   =!  $scope.sheerDiv  ;
		$scope.backBtn    =   true             ;
		$scope.cancelBtn  =   false            ;
		Sparraw.myNotice('请编辑');
	}

  	$scope.dealBarCode = function(){
		Common_barScan(function(result){
			$scope.tempVar.houseTemp.mtc_device_id = result.text;
			app_alert("扫描的设备编号是：" + result.text);
			document.getElementById("updateMtcIdInput").focus();
		},function(error){
			app_alert("调用扫描失败，请手动输入设备编号。");
		});
	};

	$scope.saveUpdate = function(){
	   	if ($scope.judgeParams()) {
	   		var params = {
				"farm_id"      :  $scope.deviceData.farmId  ,
				"house_id"     :  $scope.deviceData.houseId           ,
				"device_code"  :  $scope.deviceData.device_code         ,
				"sensorInfo"   :  $scope.deviceData.sensorInfo
			};
			Sparraw.ajaxPost('farmMobile/deviceSave', params, function(data){
				if (data.ResponseDetail.Result == "Success") {
					Sparraw.myNotice("保存成功");

					biz_common_getLatestData($state,"");
					$scope.cancelEvent();
				}else{
					Sparraw.myNotice(data.ResponseDetail.Error);
				}
			});
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

	$scope.remindEditor = function(){
		Sparraw.myNotice('点击编辑后即可修改');
	};


	//返回时清空填写内容 
	$scope.backTable = function(){
		if (!$scope.sheerDiv) {
			$scope.tempVar.houseTemp = {};
	    	$state.go("buildingTable");
		}else {
			Sparraw.myNotice('您还未进行保存，请保存后再返回。');
		}

  	};

  	$scope.cancelEvent = function(){
  		$scope.visible = !$scope.visible;
		$scope.sheerDiv = !$scope.sheerDiv;
		$scope.backBtn = false;
		$scope.cancelBtn = true;
  	}


  	$scope.setData();

})