angular.module('myApp.breedAffirm', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 出栏
.controller("breedAffirmCtrl",function($scope, $state, $http, $ionicPopup,$ionicLoading,$cordovaFileTransfer,$cordovaFileOpener2,$timeout, AppData) {
	setPortrait(true,true);//竖屏
	
	Sparraw.intoMyController($scope, $state);

	$scope.initData = function(){
		$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
		$scope.breedAffirmData = {
			"houseInfo" : [],//用来单栋出栏
			"farmInfo":{}//用来 结束本批次
		};
		setTimeout(function() {
			$scope.inquire();
		}, 500);
	};

	$scope.clickDateInput = function(item){
    	for (var i = 0; i < $scope.breedAffirmData.houseInfo.length; i++) {
    		if ($scope.breedAffirmData.houseInfo[i].houseId == item.houseId) {
    			Sparraw.openDatePicker("breedAffirmData.houseInfo[" + i + "].market_date","$scope.setShowDateCallBack();");
    		}
    	}
      	
    };

    $scope.setShowDateCallBack = function(){
      	//$scope.inquire();
    };


	$scope.inquire = function(){
		if ($scope.sparraw_user_temp.farminfo.farmBreedStatus != "01") {
			document.getElementById("endBatchBtn").style.color = "#D4D4D4";
			document.getElementById("endBatchBtn").style.border = "solid 1px #D4D4D4";
			return app_alert("该农场尚未入雏，请先入雏。");
		}

		var params = {
			"farmId"       :  $scope.sparraw_user_temp.farminfo.id               ,
			"farmBreedId"  :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId  
		};
		Sparraw.ajaxPost('layer_breedBatch/lairageQuery_v2.action', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				$scope.breedAffirmData.houseInfo = data.ResponseDetail.settleHouse;
				$scope.breedAffirmData.farmInfo = data.ResponseDetail.settleFarm;

				var judgeShowEndBatchBtn = [];
				for (var i = 0; i < $scope.breedAffirmData.houseInfo.length; i++) {
					if ($scope.breedAffirmData.houseInfo[i].breedStatus == "01") {
						judgeShowEndBatchBtn.push($scope.breedAffirmData.houseInfo[i]);
					}
				}

				if (judgeShowEndBatchBtn.length != 0) {//不展示结束批次按钮
					document.getElementById("endBatchBtn").style.color = "#D4D4D4";
					document.getElementById("endBatchBtn").style.border = "solid 1px #D4D4D4";
				}else{
					document.getElementById("endBatchBtn").style.color = "#FFF";
					document.getElementById("endBatchBtn").style.border = "solid 1px #FFF";
				}


			}else if (data.ResponseDetail.Result == "Fail") {
				Sparraw.myNotice(data.ResponseDetail.Error);
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			};
		});
	};

	$scope.judgeBreedStatus = function(item){
		if (item.breedStatus == "01") {
			return "{background:'#439AFC'}";
		}else{
			return "{background:'#D4D4D4'}";
		}
	}

	$scope.slaughter = function(item){
		if ($scope.sparraw_user_temp.Authority.FarmBreed === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};

		if (item.breedStatus != "01") {
			return app_alert("该栋舍已出栏或未入雏，无法出栏。");
		}

		app_confirm('出栏后单栋数据将无法修改，是否出栏？','提示',null,function(buttonIndex){
           if(buttonIndex == 2){
           		var params = {
					"settleFlag":"house",
			        "farmBreedId":item.farmBreedId,
			        "houseId":item.houseId,
			        "houseName":item.houseName,
			        "houseBreedId":item.houseBreedId,
			        "marketNum":item.marketNum,
			        "marketDate":item.market_date
				};
				Sparraw.ajaxPost('layer_breedBatch/lairageBatch_v2.action', params, function(data){
					if (data.ResponseDetail.Result == "Success") {
						
						Sparraw.myNotice("出栏成功！");
						biz_common_getLatestData($state,"",$scope.initData);
					}else if (data.ResponseDetail.Result == "Fail") {
						Sparraw.myNotice(data.ResponseDetail.Error);
					}else{
						Sparraw.myNotice(data.ResponseDetail.Error);
					};
				});
           }
        }); 
	}

	$scope.endBatch = function(){
		if ($scope.sparraw_user_temp.farminfo.farmBreedStatus != "01") {
			return app_alert("该农场尚未入雏，请先入雏。");
		}

		if ($scope.sparraw_user_temp.Authority.FarmSettle === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		
		app_confirm('请确认，是否结束本批次？','提示',null,function(buttonIndex){
           if(buttonIndex == 2){
           		var params = {
           			"settleFlag"   :  "farm"                                             ,
					"farmBreedId"  :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId 
				};
				Sparraw.ajaxPost('layer_breedBatch/lairageBatch_v2.action', params, function(data){
					if (data.ResponseDetail.Result == "Success") {
						biz_common_getLatestData($state,"home");
						Sparraw.myNotice("结算成功！");
					}else if (data.ResponseDetail.Result == "Fail") {
						Sparraw.myNotice(data.ResponseDetail.Error);
					}else{
						Sparraw.myNotice(data.ResponseDetail.Error);
					};
				});
           }
        }); 
	}

	$scope.initData();
});