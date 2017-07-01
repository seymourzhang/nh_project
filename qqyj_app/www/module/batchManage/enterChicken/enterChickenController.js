angular.module('myApp.enterChicken', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 进鸡
.controller("enterChickenCtrl",function($scope, $state, $http, $ionicPopup,$ionicLoading,$cordovaFileTransfer,$cordovaFileOpener2,$timeout, AppData) {
	
	setPortrait(true,true);//竖屏
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	$scope.setData = function(){
		console.log($scope.sparraw_user_temp);
		$scope.editDiv    = true;
		$scope.showDiv    = false;
		$scope.enterChickenData = {
			"youthInfos"     :  {}     ,
			"HouseList"      :  []     ,
			"selectHouse"    :  ""     ,
			"selectHouseId"  :  ""     ,
			"selectBBStatus" :  ""     ,
			"selectVariety"  :  "-1"   ,
			"varietyList"    :  []     ,
			"selectCorporation"  :  ""     ,
			"corporationList"    :  []     ,
			"saveState"      :  false
		};
		$scope.getHouseListFun();

	};

	$scope.judgeHouse = function(){
		$scope.enterChickenData.youthInfos = {};
		$scope.enterChickenData.varietyList = [];
		$scope.enterChickenData.corporationList = [];
		$scope.enterChickenData.selectVariety = "";
		$scope.enterChickenData.selectCorporation = "";

		if (Object.prototype.toString.call($scope.enterChickenData.selectHouse) === "[object String]") {
			$scope.enterChickenData.selectHouseId = JSON.parse($scope.enterChickenData.selectHouse).id;
		}else{
			$scope.enterChickenData.selectHouseId = $scope.enterChickenData.selectHouse.id;
		}
		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			if ($scope.sparraw_user_temp.houseinfos[i].id == $scope.enterChickenData.selectHouseId) {
				$scope.enterChickenData.selectBBStatus = $scope.sparraw_user_temp.houseinfos[i].BreedBatchStatus;
			}
		}

		$scope.query($scope.enterChickenData.selectHouseId);
	};

	$scope.getHouseListFun = function(){
		for (var i = 0; i < $scope.sparraw_user_temp.userinfos.length; i++) {//获取该用户下的所有栋舍id
			if ($scope.sparraw_user_temp.userinfo.userId == $scope.sparraw_user_temp.userinfos[i].userId) {
				for (var j = 0; j < $scope.sparraw_user_temp.userinfos[i].UserHouse.length; j++) {
					$scope.enterChickenData.HouseList = $scope.sparraw_user_temp.userinfos[i].UserHouse;
				}
			}
		}
		$scope.enterChickenData.selectHouse = $scope.enterChickenData.HouseList[0];
		$scope.judgeHouse($scope.enterChickenData.selectHouseId);
	}


	$scope.query = function(selectHouseId){
		var params = {
			"FarmId"    :  $scope.sparraw_user_temp.farminfo.id ,
			"HouseId"   :  selectHouseId
		};
		console.log(params);
		Sparraw.ajaxPost('batchMobile/placeYouthQurey', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				$scope.enterChickenData.youthInfos = data.ResponseDetail.youthInfos;
				document.getElementById('youthBatchNoInput').setAttribute("readOnly",false); 
				document.getElementById('youthBatchNoInput').style.color = "#000";
				document.getElementById('saveBtn').style.background = "#CCCCCC";
				$scope.enterChickenData.saveState = true;
				$scope.editDiv    = false;
				$scope.showDiv  = true;
				document.getElementById('saveBtn').innerHTML = "已进鸡";
			}else if (data.ResponseDetail.Result == "Fail"){
				$scope.enterChickenData.youthInfos = {
					"childBatchNo"     :  data.ResponseDetail.youthInfos.childBatchNo,
					"placeNum"         :  ""  ,
		            "variety_id"       :  ""  ,
		            "corporation_id"   :  ""  ,
		            "variety_name"     :  ""  ,
		            "corporation_name" :  ""  ,
		            "house_name"       :  ""  ,
		            "youthBatchNo"     :  ""  ,
		            "growthAge"        :  ""  ,
		            "placeDate"        :  ""
				};
				document.getElementById('youthBatchNoInput').removeAttribute("readOnly"); 
				document.getElementById('youthBatchNoInput').style.color = "#000";
				document.getElementById('saveBtn').style.background = "#33cd5f";
				$scope.enterChickenData.saveState = false;
				$scope.editDiv    = true;
				$scope.showDiv  = false;
				if ($scope.enterChickenData.selectBBStatus == 2) {
					app_alert("该栋舍上批次已出栏，请进行下一批次进鸡。");
				}
				document.getElementById('saveBtn').innerHTML = "进   鸡";
				setTimeout(function() {
					$scope.getVarietyList();
				}, 1500);
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
		});
	};

	$scope.getVarietyList = function(){
		var params = {
			"CodeType"    :  "FEED_TYPE",
			"biz_code"    :  "-1"
		};
		Sparraw.ajaxPost('sys/codeMobile/getVariety.action', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				$scope.enterChickenData.varietyList = [];
				for(var i = 0;i < data.ResponseDetail.CodeData.length;i++){
			        var item = data.ResponseDetail.CodeData[i];
			        var key = Object.keys(item);
			        $scope.enterChickenData.varietyList.push({
			        	"index" : "index" + i,
			        	"showName":item[key[0]],
			        	"transferValue":key[0]
			        })
			    }
		    	for (var i = 0; i < $scope.enterChickenData.varietyList.length; i++) {
					if ($scope.enterChickenData.varietyList[i].transferValue == $scope.enterChickenData.youthInfos.variety_id) {
						$scope.enterChickenData.youthInfos.variety_name = $scope.enterChickenData.varietyList[i].showName;
						$scope.enterChickenData.youthInfos.variety_id = $scope.enterChickenData.varietyList[i].transferValue;
						$scope.enterChickenData.selectVariety = $scope.enterChickenData.varietyList[i];
					}
				}

				if ($scope.enterChickenData.youthInfos.variety_id != "") {
					$scope.getCorporationList();
				}
				
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
		});
	};


	$scope.getCorporationList = function(){
	    if (Object.prototype.toString.call($scope.enterChickenData.selectVariety) === "[object String]") {
			$scope.enterChickenData.youthInfos.variety_id = JSON.parse($scope.enterChickenData.selectVariety).transferValue;
	    	$scope.enterChickenData.youthInfos.variety_name = JSON.parse($scope.enterChickenData.selectVariety).showName;
		}else{
			$scope.enterChickenData.youthInfos.variety_id = $scope.enterChickenData.selectVariety.transferValue;
	    	$scope.enterChickenData.youthInfos.variety_name = $scope.enterChickenData.selectVariety.showName;
		}
		var params = {
			"CodeType"    :  "FEED_TYPE",
			"biz_code"    :  $scope.enterChickenData.youthInfos.variety_id
		};

		Sparraw.ajaxPost('sys/codeMobile/getVariety.action', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				console.log(data);
				$scope.enterChickenData.corporationList = [];
				for (var i = 0; i < data.ResponseDetail.CodeData.length; i++) {
					$scope.enterChickenData.corporationList.push({
			        	"index" : "index" + i,
			        	"showName":data.ResponseDetail.CodeData[i].corporation_name,
			        	"transferValue":data.ResponseDetail.CodeData[i].corporation_id
			        })
				}
				for (var i = 0; i < $scope.enterChickenData.corporationList.length; i++) {
					if ($scope.enterChickenData.corporationList[i].transferValue == $scope.enterChickenData.youthInfos.corporation_id) {
						$scope.enterChickenData.youthInfos.corporation_name = $scope.enterChickenData.corporationList[i].showName;
						$scope.enterChickenData.youthInfos.corporation_id = $scope.enterChickenData.corporationList[i].transferValue;
						$scope.enterChickenData.selectCorporation = $scope.enterChickenData.corporationList[i];
					}
				}

			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
		});
		
	}

	$scope.judgeCorporation = function(){
		if (Object.prototype.toString.call($scope.enterChickenData.selectCorporation) === "[object String]") {
			$scope.enterChickenData.youthInfos.corporation_id = JSON.parse($scope.enterChickenData.selectCorporation).transferValue;
	    	$scope.enterChickenData.youthInfos.corporation_name = JSON.parse($scope.enterChickenData.selectCorporation).showName;
		}else{
			$scope.enterChickenData.youthInfos.corporation_id = $scope.enterChickenData.selectCorporation.transferValue;
	    	$scope.enterChickenData.youthInfos.corporation_name = $scope.enterChickenData.selectCorporation.showName;
		}
	}


	$scope.clickDateInput = function(){
		Sparraw.openDatePicker("enterChickenData.youthInfos.placeDate","$scope.setShowDateCallBack();");
    };

    $scope.setShowDateCallBack = function(){
      	//点击日期触发的事件
    };

    $scope.clickSave = function(){
    	if (!$scope.judgeSave()) {return;}
    	app_confirm('保存数据为：进鸡批次:'+$scope.enterChickenData.youthInfos.youthBatchNo+'，进鸡数量:'+$scope.enterChickenData.youthInfos.placeNum+'，生长日龄:'+$scope.enterChickenData.youthInfos.growthAge+'，进鸡日期:'+$scope.enterChickenData.youthInfos.placeDate+'，请确认。','提示',null,function(buttonIndex){
			if(buttonIndex == 2){
				app_confirm('保存后无法进行修改，是否保存？','提示',null,function(buttonIndex){
					if(buttonIndex == 2){
						var params = {
							"farmId"              :  $scope.sparraw_user_temp.farminfo.id                 ,
							"farmType"            :  $scope.sparraw_user_temp.farminfo.farm_type_id       ,
							"houseId"             :  $scope.enterChickenData.selectHouseId                ,
							"houseName"           :  $scope.enterChickenData.youthInfos.house_name        ,
							"youthBatchNo"        :  $scope.enterChickenData.youthInfos.youthBatchNo      ,
							"varietyId"           :  $scope.enterChickenData.youthInfos.variety_id        ,
							"corporationId"       :  $scope.enterChickenData.youthInfos.corporation_id    ,
							"variety"             :  $scope.enterChickenData.youthInfos.variety_name      ,
							"corporation"         :  $scope.enterChickenData.youthInfos.corporation_name  ,
							"growthAge"           :  $scope.enterChickenData.youthInfos.growthAge         ,
							"placeDate"           :  $scope.enterChickenData.youthInfos.placeDate         ,
							"isMix"               :  1                                                    ,
							"placeMaleNum"        :  0                                                    ,
							"placeFemaleNum"      :  $scope.enterChickenData.youthInfos.placeNum
						};
						Sparraw.ajaxPost('batchMobile/placeYouthSave', params, function(data){
							if (data.ResponseDetail.Result == "Success") {
								Sparraw.myNotice("保存成功");
								setTimeout(function() {
									biz_common_getLatestData($state,"",$scope.judgeHouse);	
								}, 1500);
							}else{
								Sparraw.myNotice(data.ResponseDetail.Error);
							}
						});
					}
		        });
			}
        });
    };

    $scope.judgeSave = function(){
    	if ($scope.enterChickenData.saveState) {//判断进鸡状态
    		console.log($scope.enterChickenData.saveState);
    		console.log($scope.enterChickenData.selectBBStatus);

    		Sparraw.myNotice("已进鸡，无法修改。");
    		return false;
    	}else{
    		if (!$scope.paramsCorrection()) {return false;}
    		return true;
    	}
    }

    $scope.paramsCorrection = function(){
    	if (Object.prototype.toString.call($scope.enterChickenData.selectHouse) === "[object String]") {
			$scope.enterChickenData.youthInfos.house_name = JSON.parse($scope.enterChickenData.selectHouse).name;
		}else{
			$scope.enterChickenData.youthInfos.house_name = $scope.enterChickenData.selectHouse.name;
		}
		var inputList = [{
			"inputName":"进鸡批次",
			"inputValue":$scope.enterChickenData.youthInfos.youthBatchNo
		},{
			"inputName":"进鸡数量",
			"inputValue":$scope.enterChickenData.youthInfos.placeNum
		},{
			"inputName":"生长日龄",
			"inputValue":$scope.enterChickenData.youthInfos.growthAge
		},{
			"inputName":"品种",
			"inputValue":$scope.enterChickenData.youthInfos.variety_id
		},{
			"inputName":"雏鸡来源",
			"inputValue":$scope.enterChickenData.youthInfos.corporation_id
		}];
		
		if (!checkInput($scope.enterChickenData.youthInfos.placeDate,'进鸡日期','选择')) {
			return false;
		}else{
			for (var i = 0; i < inputList.length; i++) {
    			if (!checkInput(inputList[i].inputValue,inputList[i].inputName)) {
    				return false;
    			}else{
    				if (i == (inputList.length-1)) {
    					return true;
    				}
    			}
    		}
		}
    }

	$scope.setData();
})