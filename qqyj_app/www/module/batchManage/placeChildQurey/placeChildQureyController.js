angular.module('myApp.placeChildQurey', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 发雏确认
.controller("placeChildQureyCtrl",function($scope, $state, $http, $ionicPopup,$ionicLoading,$cordovaFileTransfer,$cordovaFileOpener2,$timeout, AppData) {
	
	setPortrait(true,true);//竖屏
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	$scope.setData = function(){
		document.getElementById("placeChildView").style.height =  DeviInfo.ScreenHeight + "px";
		document.getElementById("materialView").style.height =  DeviInfo.ScreenHeight + "px";
		document.getElementById("drugView").style.height =  DeviInfo.ScreenHeight + "px";
		$scope.placeChildShow = true;
		$scope.materialShow = false;
		$scope.drugShow = false;

		$scope.placeChildData = {
			"send_type"   :  "01" ,
			"ChildInfos"  :  []
		};
		$scope.chooseType('01');
	}

	$scope.query = function(sendType){
		$scope.placeChildData.ChildInfos = [];
		var params = {
			"farm_id"    :  $scope.sparraw_user_temp.farminfo.id ,
         	"send_type"  :  sendType
		};
		Sparraw.ajaxPost('batchMobile/placeChildQurey', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				$scope.placeChildData.ChildInfos = data.ResponseDetail.ChildInfos;
				for (var i = 0; i < $scope.placeChildData.ChildInfos.length; i++) {
					$scope.placeChildData.ChildInfos[i].index = "index" + i;
					console.log($scope.placeChildData.ChildInfos[i]);
					if ($scope.placeChildData.ChildInfos[i].sendStatus == "1") {
						$scope.placeChildData.ChildInfos[i].btnText = "确   认";
					}else{
						$scope.placeChildData.ChildInfos[i].btnText = "已确认";
					}
				}
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
		});
	};


	$scope.clickSave = function(item){
		if (!$scope.paramsRevise(item)) {return;}
		app_confirm('保存数据为：确认数量:'+item.checkNum+'，确认日期:'+item.checkDate+'，请确认。','提示',null,function(buttonIndex){
			if(buttonIndex == 2){
				app_confirm('保存后无法进行修改，是否保存？','提示',null,function(buttonIndex){
					if(buttonIndex == 2){
						var params = {
							"farmId"        :  $scope.sparraw_user_temp.farminfo.id ,
							"sendType"      :  $scope.placeChildData.send_type  ,
							"childBatchNo"  :  item.childBatchNo                ,
							"checkNum"      :  item.checkNum                    ,  
							"checkDate"     :  item.checkDate
						};
						if ($scope.placeChildData.send_type != "01") {
							params.send_id = item.send_id
						};
						Sparraw.ajaxPost('batchMobile/placeChildSave', params, function(data){
							if (data.ResponseDetail.Result == "Success") {
								Sparraw.myNotice("保存成功");
								setTimeout(function() {
									$scope.chooseType($scope.placeChildData.send_type);
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

	$scope.paramsRevise = function(item){
		if (item.sendStatus == "0") {
			Sparraw.myNotice("已确认，无法保存。");
			return false;
		}else{
			if (!checkInput(item.checkNum,'确认数量')) {
				return false;
			}else{
				if (!checkInput(item.checkDate,'确认日期','选择')) {
					return false;
				}else{
					var oDate1 = new Date(item.checkDate);//确认日期
					var oDate2 = new Date(item.sendDate);//发雏日期
					if(oDate1.getTime() < oDate2.getTime()){
				        app_alert("确认日期必须大于发雏日期。");
				        return false;
				    }else{
				    	return true;
				    }
				}
			}
		}
		
	}

	$scope.inputLabelColor = function(saveBtn){
		if (saveBtn == "已确认") {
			return "{color:'#4B4848'}";
		}else{
			return "{color:'#2f7fff'}";
		}
	}




	$scope.clickDateInput = function(item){
		for (var i = 0; i < $scope.placeChildData.ChildInfos.length; i++) {
			if ($scope.placeChildData.ChildInfos[i].index == item.index) {
				Sparraw.openDatePicker("placeChildData.ChildInfos[" + i + "].checkDate","$scope.setShowDateCallBack();");
			}
		}
    };

    $scope.setShowDateCallBack = function(){//点击日期触发的事件
      	//$scope.inquire();
    };

    $scope.saveBtnStyle = function(Status){
    	if (Status == "1") {
			return "{background:'#33cd5f'}";
		}else{
			return "{background:'#E3E3E3'}";
		};
    }


	$scope.chooseType = function(type){


		switch(type){
			case "01":
				$scope.placeChildData.send_type = "01";
				document.getElementById("placeChildBtn").style.background = "#33cd5f";
				document.getElementById("placeChildBtn").style.color = "#FFF";
				document.getElementById("materialBtn").style.background = "#FFF";
				document.getElementById("materialBtn").style.color = "#33cd5f";
				document.getElementById("drugBtn").style.background = "#FFF";
				document.getElementById("drugBtn").style.color = "#33cd5f";
				$scope.placeChildShow = true;
				$scope.materialShow = false;
				$scope.drugShow = false;
				$scope.query($scope.placeChildData.send_type);
			  break;
			case "02":
			  	$scope.placeChildData.send_type = "02";
				document.getElementById("placeChildBtn").style.background = "#FFF";
				document.getElementById("placeChildBtn").style.color = "#33cd5f";
				document.getElementById("materialBtn").style.background = "#33cd5f";
				document.getElementById("materialBtn").style.color = "#FFF";
				document.getElementById("drugBtn").style.background = "#FFF";
				document.getElementById("drugBtn").style.color = "#33cd5f";
				$scope.placeChildShow = false;
				$scope.materialShow = true;
				$scope.drugShow = false;
				$scope.query($scope.placeChildData.send_type);
			  break;
			default:
			  	$scope.placeChildData.send_type = "03";
				document.getElementById("placeChildBtn").style.background = "#FFF";
				document.getElementById("placeChildBtn").style.color = "#33cd5f";
				document.getElementById("materialBtn").style.background = "#FFF";
				document.getElementById("materialBtn").style.color = "#33cd5f";
				document.getElementById("drugBtn").style.background = "#33cd5f";
				document.getElementById("drugBtn").style.color = "#FFF";
				$scope.placeChildShow = false;
				$scope.materialShow = false;
				$scope.drugShow = true;
				$scope.query($scope.placeChildData.send_type);
		}

	};

	$scope.setData();
})