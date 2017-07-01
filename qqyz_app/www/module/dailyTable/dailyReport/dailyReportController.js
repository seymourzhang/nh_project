 angular.module('myApp.dailyReport', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 // 生产记录
.controller("dailyReportCtrl",function($scope, $state,$ionicLoading, $http, $ionicPopup, $ionicScrollDelegate, $stateParams, $ionicScrollDelegate, AppData){
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	$scope.setData = function(){
		$scope.dailyReportData = {
			"FarmBreedId"       :  $scope.sparraw_user_temp.farminfo.id         ,
	        "selectHouse"       :  $scope.sparraw_user_temp.houseinfos[0]       ,
	        "HouseBreedId"      :  ""                                           ,
	        "HouseName"         :  ""                                           ,
	        "BreedBatchStatus"  :  ""                                           ,
	        "HouseId"           :  ""                                           ,
	        "SpecialFlag"       :  "N"                                          ,
	        "selectDayAge"      :  ""                                           ,
	        "DayAge"            :  ""                                           ,
	        "inputData"         :  {}
	        
		};
		setTimeout(function() {
			$scope.judgeHouse();
		}, 500);
	};

	$scope.goHome = function(){
		$state.go("home");
	};

    $scope.inquire = function() {
		var params = {
			"HouseId"     :  $scope.dailyReportData.HouseId        ,
			"BreedBatchId":  $scope.dailyReportData.HouseBreedId   ,
			"SpecialFlag" :  $scope.dailyReportData.SpecialFlag    ,
			"SpecialDate" :  $scope.dailyReportData.selectDayAge
		};
		Sparraw.ajaxPost('dailyMobile/dailyQuery', params, function(data){
			//接受数据
	   		if (data.ResponseDetail.Result == "Success") {
	   			$scope.dailyReportData.DayAge        = data.ResponseDetail.DataInfo.DayAge                                     ;
	   			$scope.dailyReportData.GrowthWeekAge = data.ResponseDetail.DataInfo.GrowthWeekAge                              ;
	   			$scope.dailyReportData.LayerWeekAge  = data.ResponseDetail.DataInfo.LayerWeekAge                               ;
	   			$scope.dailyReportData.selectDayAge  = data.ResponseDetail.DataInfo.GrowthDate                                 ;
	   			$scope.dailyReportData.SpecialFlag   = "Y"                                                                     ;
	   			$scope.dailyReportData.inputData = {
	   				"death_num_male"        :  data.ResponseDetail.DataInfo.death_num_male         ,  //int,死亡
					"death_num_female"      :  data.ResponseDetail.DataInfo.death_num_female       ,  //int,死亡
					"culling_num_male"      :  data.ResponseDetail.DataInfo.culling_num_male       ,  //int,淘汰
					"culling_num_female"    :  data.ResponseDetail.DataInfo.culling_num_female     ,  //int,淘汰
					"body_weight_male"      :  data.ResponseDetail.DataInfo.body_weight_male       ,  //number,体重
					"body_weight_female"    :  data.ResponseDetail.DataInfo.body_weight_female     ,  //number,体重
					"gender_error_male"     :  data.ResponseDetail.DataInfo.gender_error_male      ,  //int,鉴别错误
					"gender_error_female"   :  data.ResponseDetail.DataInfo.gender_error_female    ,  //int,鉴别错误
					"feed_code_female"      :  data.ResponseDetail.DataInfo.feed_code_female       ,  //String,母鸡采食料号
					"feed_weight_female"    :  data.ResponseDetail.DataInfo.feed_weight_female     ,  //number,母鸡采食重量，单位：千克
					"water_capacity_female" :  data.ResponseDetail.DataInfo.water_capacity_female  ,  //number,母鸡饮水量，单位：升
					"layer_amount"          :  data.ResponseDetail.DataInfo.layer_amount           ,  //int，产蛋数量
					"uniformity"            :  data.ResponseDetail.DataInfo.uniformity                //number,均匀度
	   			};
			}else if (data.ResponseDetail.Result == "Fail"){
				Sparraw.myNotice(data.ResponseDetail.Error);
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
			//展示状态
			if (data.ResponseDetail.Result == "Success") {
				if ($scope.dailyReportData.BreedBatchStatus == 1) {
					$scope.dataNullText = false;
                    $scope.inputTable = true;
					document.getElementById('saveBtnStatus').style.background = "#3dcb64";
					document.getElementById('dataNullTextId').innerHTML = "";
				}else{
					$scope.dataNullText = true;
                    $scope.inputTable = false;
					document.getElementById('saveBtnStatus').style.background = "#E3E3E3";
					if ($scope.dailyReportData.BreedBatchStatus == 0) {
	                    document.getElementById('dataNullTextId').innerHTML = "所选栋舍或时间未入雏";
	                }else{
	                    document.getElementById('dataNullTextId').innerHTML = "所选栋舍或时间已出栏";
                	}
				}	
			}else{
				$scope.dataNullText = true;
                $scope.inputTable = false;
                document.getElementById('saveBtnStatus').style.background = "#E3E3E3";
                document.getElementById('dataNullTextId').innerHTML = "所选栋舍或时间未入雏";
			}		
		});

		
	};

    $scope.clickDateInput = function(){
      	Sparraw.openDatePicker("dailyReportData.selectDayAge","$scope.setShowDateCallBack();");
    };

    $scope.setShowDateCallBack = function(){
      	$scope.inquire();
    };

	$scope.judgeHouse = function(){
		if (Object.prototype.toString.call($scope.dailyReportData.selectHouse) === "[object String]") {
			$scope.dailyReportData.HouseBreedId = JSON.parse($scope.dailyReportData.selectHouse).BreedBatchId;
			$scope.dailyReportData.HouseId = JSON.parse($scope.dailyReportData.selectHouse).id;
			$scope.dailyReportData.BreedBatchStatus = JSON.parse($scope.dailyReportData.selectHouse).BreedBatchStatus;
		}else{
			$scope.dailyReportData.HouseBreedId = $scope.dailyReportData.selectHouse.BreedBatchId;
			$scope.dailyReportData.HouseId = $scope.dailyReportData.selectHouse.id;
			$scope.dailyReportData.BreedBatchStatus = $scope.dailyReportData.selectHouse.BreedBatchStatus;
		}

		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			if ($scope.sparraw_user_temp.houseinfos[i].id == $scope.dailyReportData.HouseId) {
				$scope.dailyReportData.HouseName = $scope.sparraw_user_temp.houseinfos[i].name;
			}
		}


		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			if ($scope.sparraw_user_temp.houseinfos[i].id == $scope.dailyReportData.HouseId) {
				if ($scope.sparraw_user_temp.houseinfos[i].houseTypeName == "育成") {
					$scope.eggContent = false;
				}else{
					$scope.eggContent = true;
				}
			}
		}

		
		console.log($scope.sparraw_user_temp);
		$scope.inquire();
	}

	$scope.save = function(){
		//添加权限
		if ($scope.sparraw_user_temp.Authority.DataInput == "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		if ($scope.dailyReportData.BreedBatchStatus == 0) {
			return app_alert("该栋舍未新建批次，请新建批次。");
		}else if ($scope.dailyReportData.BreedBatchStatus == 1) {

		}else if ($scope.dailyReportData.BreedBatchStatus == 2){
			return app_alert("该栋舍已出栏，无法保存。");
		}



		/*暂时注释*/
		//判断是否能保存
		var selectedDate = $scope.dailyReportData.selectDayAge;
		var toDay = new Date().Format("yyyy-MM-dd");
		var dateArr = [selectedDate,toDay];
		//判断日期大小
		var minDate = dateArr[0];
    	for (var i = 0; i < dateArr.length; i++) {
    		var oDate1 = new Date(minDate);
		    var oDate2 = new Date(dateArr[i]);
		    console.log(oDate1.getTime());
		    console.log(oDate2.getTime());
		    /*if (oDate1.getTime() != oDate2.getTime()) {
		    	return app_alert("只能保存当天数据！");
		    }
		    if (oDate1.getTime() < oDate2.getTime()) {
		    	return app_alert("无法保存昨日数据！");
		    }*/
		    if (oDate1.getTime() > oDate2.getTime()) {
		    	return app_alert("无法保存明日数据！");
		    }
    	}


		var params = {
			"HouseName"              :  $scope.dailyReportData.HouseName                        ,
			"FarmId"                 :  $scope.sparraw_user_temp.farminfo.id                    ,
			"BreedBatchId"           :  $scope.dailyReportData.HouseBreedId                     ,
            "HouseId"                :  $scope.dailyReportData.HouseId                          ,
            "DayAge"                 :  $scope.dailyReportData.DayAge                           ,
            "death_num_male"         :  $scope.dailyReportData.inputData.death_num_male         ,
            "death_num_female"       :  $scope.dailyReportData.inputData.death_num_female       ,
            "culling_num_male"       :  $scope.dailyReportData.inputData.culling_num_male       ,
            "culling_num_female"     :  $scope.dailyReportData.inputData.culling_num_female     ,
            "body_weight_male"       :  $scope.dailyReportData.inputData.body_weight_male       ,
            "body_weight_female"     :  $scope.dailyReportData.inputData.body_weight_female     ,
            "gender_error_male"      :  $scope.dailyReportData.inputData.gender_error_male      ,
            "gender_error_female"    :  $scope.dailyReportData.inputData.gender_error_female    ,
            "feed_code_female"       :  $scope.dailyReportData.inputData.feed_code_female       ,
            "feed_weight_female"     :  $scope.dailyReportData.inputData.feed_weight_female     ,
            "water_capacity_female"  :  $scope.dailyReportData.inputData.water_capacity_female  ,
            "layer_amount"           :  $scope.dailyReportData.inputData.layer_amount           ,
            "uniformity"             :  $scope.dailyReportData.inputData.uniformity  
		};
		
		Sparraw.ajaxPost('dailyMobile/dailySave', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				Sparraw.myNotice("保存成功！");
			}else if (data.ResponseDetail.Result == "Fail"){
				Sparraw.myNotice(data.ResponseDetail.Error);
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			}
		});
	}

	setPortrait(true,true);
	$scope.setData();
})