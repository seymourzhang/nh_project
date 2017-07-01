angular.module('myApp.marketQuery', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 出栏
.controller("marketQueryCtrl",function($scope, $state, $http, $ionicPopup,$ionicLoading,$cordovaFileTransfer,$cordovaFileOpener2,$timeout, AppData) {
	
	setPortrait(true,true);//竖屏
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	$scope.setData = function(){
		$scope.marketQueryData = {
			"marketInfos":[]
		};
		$scope.query();
	};



	$scope.query = function(){
		var params = {
			"FarmId"    :  $scope.sparraw_user_temp.farminfo.id ,
		};
		console.log(params);
		Sparraw.ajaxPost('batchMobile/marketQuery', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				$scope.marketQueryData.marketInfos = data.ResponseDetail.matketInfos;
				for (var i = 0; i < $scope.marketQueryData.marketInfos.length; i++) {
					$scope.marketQueryData.marketInfos[i].index = "index" + i;
					if ($scope.marketQueryData.marketInfos[i].status == "1") {
						$scope.marketQueryData.marketInfos[i].saveBtnText = "出   栏";
					}else{
						$scope.marketQueryData.marketInfos[i].saveBtnText = "已出栏";
					}
				}
			}else if (data.ResponseDetail.Result == "Fail"){
				Sparraw.myNotice(data.ResponseDetail.Error);
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
		});
	};


	$scope.clickDateInput = function(item){
		for (var i = 0; i < $scope.marketQueryData.marketInfos.length; i++) {
			if ($scope.marketQueryData.marketInfos[i].index == item.index) {
				Sparraw.openDatePicker("marketQueryData.marketInfos[" + i + "].marketDate","$scope.setShowDateCallBack();");
			}
		}
    };

    $scope.setShowDateCallBack = function(){
      	//点击日期触发的事件
    };

    $scope.slaughterFun = function(Item){
    	console.log(Item);
    	if (!$scope.paramsRevise(Item)) {return;}
		app_confirm('保存栋舍:' + Item.houseName + '保存数据为出栏数量:' + Item.marketNum + '，只均重:' + Item.perFemaleWeight + '，出栏日期:' + Item.marketDate + '，请确认。','提示',null,function(buttonIndex){
			if(buttonIndex == 2){
				app_confirm('保存后无法进行修改，是否保存？','提示',null,function(buttonIndex){
					if(buttonIndex == 2){
						var params = {
							"batchNo"          :  Item.batchNo                          ,
				            "farmId"           :  $scope.sparraw_user_temp.farminfo.id  ,
				            "houseId"          :  Item.houseId                          ,
				            "houseName"        :  Item.houseName                        ,
				            "marketDate"       :  Item.marketDate                       ,
				            "marketNum"        :  Item.marketNum                        ,
				            "perFemaleWeight"  :  Item.perFemaleWeight                  
						};
						console.log(params);
						Sparraw.ajaxPost('batchMobile/marketSave', params, function(data){
							if (data.ResponseDetail.Result == "Success") {
								Sparraw.myNotice("出栏成功");
								setTimeout(function() {
									biz_common_getLatestData($state,"",$scope.setData);	
								}, 1500);
							}else if (data.ResponseDetail.Result == "Fail"){
								Sparraw.myNotice(data.ResponseDetail.Error);
							}else{
								Sparraw.myNotice(data.ResponseDetail.Error);
							}
						});
					}
		        });
			}
        });
    }


    $scope.saveBtnStyle = function(Status){
    	if (Status == "1") {
			return "{background:'#33cd5f'}";
		}else{
			return "{background:'#E3E3E3'}";
		};
    }

    $scope.paramsRevise = function(Item){
    	if (Item.status == "0") {
			Sparraw.myNotice("已出栏，无法修改。");
			return false;
		}else{
	    	if (!checkInput(Item.marketNum,'出栏数量')) {
	    		return false;
	    	}else{
	    		if (!checkInput(Item.perFemaleWeight,'只均重')) {
		    		return false;
		    	}else{
		    		if (!checkInput(Item.marketDate,'出栏日期','选择')) {
			    		return false;
			    	}else{
			    		return true;
			    	}
		    	}
	    	}
	    }
    }

    $scope.inputLabelColor = function(saveBtn){
		if (saveBtn == "已出栏") {
			return "{color:'#4B4848'}";
		}else{
			return "{color:'#2f7fff'}";
		}
	}


	$scope.setData();
})