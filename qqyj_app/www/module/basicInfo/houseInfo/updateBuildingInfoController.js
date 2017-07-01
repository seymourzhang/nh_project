 angular.module('myApp.updateBuildingInfo', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 // 栋舍修改
.controller("updateBuildingInfoCtrl",function($scope, $state, $http, $stateParams, AppData,$ionicPopup) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	
	$scope.setData = function(){
		$scope.deviceData = {};
		//查询
		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			if($scope.sparraw_user_temp.houseinfos[i].id == $stateParams.buildingID){
				$scope.tempVar.houseTemp = JSON.parse(JSON.stringify($scope.sparraw_user_temp.houseinfos[i]));
				console.log($scope.sparraw_user_temp.houseinfos[i]);
				$scope.deviceData.houseId = $scope.sparraw_user_temp.houseinfos[i].id;
				$scope.deviceData.deviceCode = $scope.sparraw_user_temp.houseinfos[i].deviceCode;
			}
		}


		$scope.backBtn    =  false  ;
		$scope.cancelBtn  =  true   ;


		if ($scope.sparraw_user_temp.Authority.MasterData == "all") {
			
			$scope.editTable    =  false  ;
		}else{
			$scope.editTable    =  true  ;
		};


		persistentData.setProbeHouse = $scope.tempVar.houseTemp;
	}

	$scope.startEdit = function(){
		//添加权限
		if ($scope.sparraw_user_temp.Authority.MasterData == "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		$scope.editTable  =!  $scope.editTable ;
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
   		if (!checkInput(tempVar.houseTemp.name,'栋名称')) {return;}
   		var params = {
			'house_id': $scope.deviceData.houseId,
     		'house_name': tempVar.houseTemp.name
		};

		Sparraw.ajaxPost('houseMobile/update', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				Sparraw.myNotice("保存成功");

				biz_common_getLatestData($state,"");
				$scope.cancelEvent();
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
		});
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
  		$scope.editTable  =!  $scope.editTable ;
  		$scope.visible = !$scope.visible;
		$scope.sheerDiv = !$scope.sheerDiv;
		$scope.backBtn = false;
		$scope.cancelBtn = true;
  	}


  	$scope.judgeSetProbe = function(role){
  		if (role == 101 || role == 102) {
			return "{background:'#33cd5f'}";
		}else{
			return "{background:'#E3E3E3'}";
		};
  	}


  	$scope.setProbe = function(){
  		if ($scope.sparraw_user_temp.userinfo.roleId == 101 || $scope.sparraw_user_temp.userinfo.roleId == 102) {
  			$state.go("setProbe");
  		}else{
  			return app_alert("该用户无此操作权限。");
  		}  		
  	}


  	$scope.setData();

})