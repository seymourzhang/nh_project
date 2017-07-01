angular.module('myApp.docPlaceAffirm', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 入雏
.controller("docPlaceAffirmCtrl",function($scope, $state, $http, $ionicPopup,$ionicLoading,$cordovaFileTransfer,$cordovaFileOpener2,$timeout, AppData) {
	
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	$scope.docPlaceData = {
		"firstTime":false,//判断是否是第一次取值
        "farmBreedId":$scope.sparraw_user_temp.farminfo.farmBreedBatchId,
        "houseInfo":[],
        "batchInfo":{}
	};

	

    $scope.clickDateInput = function(item){
    	for (var i = 0; i < $scope.docPlaceData.houseInfo.length; i++) {
    		if ($scope.docPlaceData.houseInfo[i].houseId == item.houseId) {
    			Sparraw.openDatePicker("docPlaceData.houseInfo[" + i + "].place_date","$scope.setShowDateCallBack();");
    			return;
    		}
    	}
    };

    $scope.setShowDateCallBack = function(){
      	//$scope.inquire();
    };


    $scope.inquire = function(){

    	if ($scope.sparraw_user_temp.farminfo.farmBreedStatus == "01") {
    		var params = {
				"farmId":$scope.sparraw_user_temp.farminfo.id,
				"farmBreedId":$scope.sparraw_user_temp.farminfo.farmBreedBatchId,  
			};
			Sparraw.ajaxPost('layer_breedBatch/queryBatch_v2.action', params, function(data){
				if (data.ResponseDetail.Result == "Success") {
					$scope.docPlaceData.houseInfo = data.ResponseDetail.houseInfo;
					$scope.docPlaceData.batchInfo = data.ResponseDetail.batchInfo;
					$scope.docPlaceData.place_type = data.ResponseDetail.batchInfo.place_type;
					$scope.docPlaceData.place_breed = data.ResponseDetail.batchInfo.place_breed;
					
					$scope.docPlaceData.firstTime = false;
					document.getElementById("place_typeSelect").disabled=true;
					document.getElementById("place_breedSelect").disabled=true;

					document.getElementById("place_typeSelect").style.color = "#E3E3E3";
					document.getElementById("place_breedSelect").style.color = "#E3E3E3";

				}else if (data.ResponseDetail.Result == "Fail") {
					Sparraw.myNotice(data.ResponseDetail.Error);
					$scope.docPlaceData.firstTime = true;
					for (var i = 0; i < $scope.sparraw_user_temp.userinfo.houses.length; i++) {
						$scope.docPlaceData.houseInfo.push({
							"houseId":$scope.sparraw_user_temp.userinfo.houses[i].HouseId,//int型，栋舍id
							"houseName":$scope.sparraw_user_temp.userinfo.houses[i].HouseName,
							"placeNum":"",//int型，入雏数量
							"placeDate":"",//varchar型，入雏日期，YYYY-MM-DD
							"batch_status":"00"
						})
					}
					console.log($scope.docPlaceData.houseInfo);
				}else{
					Sparraw.myNotice(data.ResponseDetail.Error);
				};
			});
    	}else{
    		Sparraw.myNotice("暂无数据请先入雏");
    		$scope.docPlaceData.firstTime = true;
    		$scope.sparraw_user_temp.farminfo.farmBreedBatchId = 0;
				for (var i = 0; i < $scope.sparraw_user_temp.userinfo.houses.length; i++) {
					$scope.docPlaceData.houseInfo.push({
						"houseId":$scope.sparraw_user_temp.userinfo.houses[i].HouseId,//int型，栋舍id
						"houseName":$scope.sparraw_user_temp.userinfo.houses[i].HouseName,
						"placeNum":"",//int型，入雏数量
						"placeDate":"",//varchar型，入雏日期，YYYY-MM-DD
						"batch_status":"00"
					})
			}
    	}
	}

	$scope.getthedate = function(){

		for (var i = 0; i < $scope.docPlaceData.houseInfo.length; i++) {
			if ($scope.docPlaceData.houseInfo[i].place_date == "" || !$scope.docPlaceData.houseInfo[i].place_date) {
	    		$scope.docPlaceData.houseInfo[i].place_day_age = "";
	    	}else{
				//  以下程序是参考自然周，计算生长周龄的公式， by guoxiang  2016-05-05
				var initPlaceDate = Common_addDateByDays($scope.docPlaceData.houseInfo[i].place_date,1-$scope.docPlaceData.houseInfo[i].place_day_age);
				console.log('initPlaceDate=' + initPlaceDate);
				var initPlaceWeekDay = new Date(initPlaceDate).getDay();
				console.log('initPlaceWeekDay=' + initPlaceWeekDay);
				var curDayAge = $scope.docPlaceData.houseInfo[i].place_day_age;
				console.log('curDayAge1=' + curDayAge);
				if(initPlaceWeekDay <= 3){
					curDayAge = curDayAge + initPlaceWeekDay + 6;
				}else{
					curDayAge = curDayAge - (7- initPlaceWeekDay) + 6;
				}
				console.log('curDayAge2=' + curDayAge);
				$scope.docPlaceData.houseInfo[i].place_week_age = Math.floor(curDayAge / 7); 
				console.log('$scope.docPlaceData.houseInfo.place_week_age=' + $scope.docPlaceData.houseInfo[i].place_week_age);
	    	};
		}
	}


	$scope.getBatchInfo = function(item){
		//批次信息
		var tempBatchCode = "";
		var tempBatchDate = "";
		tempBatchDate = item.place_date;

		//转为字符串并且删除“日”字符串，修改“年”“月”“/”为“-”,并且做补零处理                
        var selectDate = tempBatchDate + '';
        selectDate     = selectDate.replace(/(日)/g,"");
        selectDate     = selectDate.replace(/(月)/g,"-");
        selectDate     = selectDate.replace(/(年)/g,"-");
        selectDate     = selectDate.replace(/\//g,"-");
        if (selectDate[6] == "-") {
        	selectDate = selectDate.replace(/(.{5})/g,'$10');
        };
        if (selectDate[9]) {

        }else{
        	selectDate = selectDate.replace(/(.{8})/g,'$10');
        };
        //将得到的日期放入入雏日期
        tempBatchDate = selectDate;
        //获取批次号
        tempBatchCode = tempBatchDate;
        tempBatchCode = tempBatchCode.replace(/(-)/g,"");
        tempBatchCode = tempBatchCode.substring(2,tempBatchDate.length);
        //放入得到的信息
        $scope.docPlaceData.batchInfo.farmId = $scope.sparraw_user_temp.farminfo.id;
        $scope.docPlaceData.batchInfo.farmBreedId = 0;
        $scope.docPlaceData.batchInfo.batchCode = tempBatchCode;
        $scope.docPlaceData.batchInfo.batchDate = tempBatchDate;
	};

	$scope.judgeSave = function(item){
		if (item.batch_status == "00") {
			return "{background:'#439AFC'}";
		}else{
			return "{background:'#D4D4D4'}";
		}
	}


	$scope.save = function(item){

		if (item.batch_status != "00") {
			return app_alert("该栋舍已入雏，无法保存");
		}

		if ($scope.sparraw_user_temp.Authority.FarmBreed === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		}

		if (!item.place_date || item.place_date == null) {
			return Sparraw.myNotice("请选择入雏日期");
		}

		if (!item.place_day_age || item.place_day_age == null) {
			return Sparraw.myNotice("请输入入雏日龄");
		}

		if (!item.place_num || item.place_num == null) {
			return Sparraw.myNotice("请输入入雏数量");
		}

		if (!$scope.docPlaceData.batchInfo.place_type || 
			$scope.docPlaceData.batchInfo.place_type == null ||
			$scope.docPlaceData.batchInfo.place_type == "") {
			return Sparraw.myNotice("请选择类别");
		}

		if (!$scope.docPlaceData.batchInfo.place_breed || 
			$scope.docPlaceData.batchInfo.place_breed == null ||
			$scope.docPlaceData.batchInfo.place_breed == "") {
			return Sparraw.myNotice("请选择品种");
		}

		if ($scope.docPlaceData.firstTime) {//首次进入创建批次信息
			$scope.getBatchInfo(item);
		}




		if (item.batch_status == "00") {
			app_confirm('确认之后将不可更改：入雏日'+item.place_date+'，日龄'+item.place_day_age+'，入雏数'+item.place_num,'提示',null,function(buttonIndex){
				if(buttonIndex == 2){
					$scope.docPlaceData.houseInfo = [{
				 		"houseId":item.houseId,
						"houseName":item.houseName,
						"place_date":item.place_date,
						"place_day_age":item.place_day_age,
						"place_week_age":item.place_week_age,
						"place_num":item.place_num
				 	}];

					var params = {
						"batchInfo"  :  $scope.docPlaceData.batchInfo,
			       		"houseInfo"  :  $scope.docPlaceData.houseInfo
					};

					Sparraw.ajaxPost('layer_breedBatch/createBatch_v2.action', params, function(data){
							if (data.ResponseDetail.Result == "Success") {
								app_alert("保存成功!");
								//重新获取最新数据
								biz_common_getLatestData($state,"",$scope.inquire);
								$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
							}else if (data.ResponseDetail.Result == "Fail") {
								app_alert(data.ResponseDetail.Error);
								$scope.inquire();
							}else{
								Sparraw.myNotice(data.ResponseDetail.Error);
							};
					});
				}
        	}); 
		}
	}


	$scope.inquire();
});