 angular.module('myApp.dailyReport', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 // 生产记录
.controller("dailyReportCtrl",function($scope, $state,$ionicLoading, $http, $ionicPopup, $ionicScrollDelegate, $stateParams, $ionicScrollDelegate, AppData){
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	setPortrait(true,true);
	
	var type = 0;
	$scope.goHome = function(){
		if ($scope.sparraw_user_temp.Authority.DailyInput === "All") {

		}else{
			return $state.go("home");
		};
		if(chang == true){
			type = 1;
			app_confirm('数据已经被修改，请确认是否保存！','提示',null,function(buttonIndex){
                    if(buttonIndex == 2){
						$scope.save();
			        }else{
						$state.go("home");
					}
              });
		}else{
			$state.go("home");
		}
	}
	
	$scope.goProductionDaily = function(){

		if ($scope.sparraw_user_temp.Authority.DailyInput === "All") {

		}else{
			return $state.go("productionDaily",{"fromPage":"dailyReport"});
		};
		
		if(chang == true){
			type = 2;
			app_confirm('数据已经被修改，请确认是否保存！','提示',null,function(buttonIndex){
				
                    if(buttonIndex == 2){
						$scope.save();
			        }else{
						$state.go("productionDaily",{"fromPage":"dailyReport"});
					}
              });
		}else{
			$state.go("productionDaily",{"fromPage":"dailyReport"});
		}
	}
 
	$scope.dailyReportData = {
		"FarmBreedId"    :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId                ,
        "selectHouse"    :  ""                                                                ,
        "showHouseList"  :  []                                                                ,
        "HouseBreedId"   :  ""            ,
        "HouseId"        :  ""            ,
        "HouseName"      :  ""            ,
        "CurDate"        :  ""            ,//varchar型，当前日期
        "CurDayAge"      :  ""            ,//int型，当前生长日龄
        "selectDayAge"   :  -1            ,//选择的日龄
        "GrowthWeekAge"  :  ""            ,//int型，当前生长周龄
        "LayerWeekAge"   :  ""            ,//int型，当前产蛋周龄
        "DataDate"       :  ""            ,//varchar型，数据日期
        "culling_all"    :  ""            ,//int型，总死淘数量
        "curLayNum"      :  ""            ,//int型，产蛋总数量
        "all_box_num"    :  ""            ,
        "egg_box_size"   :  "25.5"        ,//number型，箱子规格，单位：公斤/箱
        "curBrokenNum"   :  ""            ,//int型，其中破损蛋数量
        "daily_feed"     :  ""            ,//number型，日耗料，单位：公斤
        "daily_water"    :  ""            ,//number型，日耗水,单位：立方米
        "daily_weight"   :  ""            ,//number型，鸡体均重,单位：公斤
        "showDate"       :  ""            ,//显示的日龄
        "between"        :  ""            ,//选择日期与当前日期的差
        "feed_remark"    :  ""            ,
        "eggTotalWeight" :  ""            ,
		"medicine_remark" :  ""            ,
		"dataInput"		 : [{
			"day_age"    : "",
			"culling_all": "",
			"curLayNum":  "",
			"egg_box_size": "25.5",
			"curBrokenNum": "",
			"daily_feed": "",
			"daily_weight": "",
			"daily_water": "",
			"curLayWeight": "",
			"feed_remark":"",
			"medicine_remark":""
		}],
		"judgeSwitchHouse":true,

	}
	//筛选出已入雏的栋舍
	for (var i = 0; i < $scope.sparraw_user_temp.userinfo.houses.length; i++) {
		if ($scope.sparraw_user_temp.userinfo.houses[i].HouseBreedStatus == "01") {
			$scope.dailyReportData.showHouseList.push($scope.sparraw_user_temp.userinfo.houses[i]);
		}
	}


    $scope.inquire = function(selectHouseId) {
    	$scope.judgeShow(selectHouseId);

		var params = {
	      	"FarmBreedId" :  $scope.dailyReportData.FarmBreedId    ,
			"HouseId"     :  selectHouseId                         ,
			"DayAge"      :  $scope.dailyReportData.selectDayAge
		};

		Sparraw.ajaxPost('layer_dataInput/queryDR_v2.action', params, function(data){
	   		if (data.ResponseDetail.Result == "Success") {
	   			//赋值
				$scope.dailyReportData.CurDate = data.ResponseDetail.CurDate;
				$scope.dailyReportData.GrowthWeekAge = data.ResponseDetail.GrowthWeekAge;
				$scope.dailyReportData.LayerWeekAge = data.ResponseDetail.LayerWeekAge;
				$scope.dailyReportData.culling_all = data.ResponseDetail.culling_all;
				$scope.dailyReportData.curLayNum = data.ResponseDetail.curLayNum;
				$scope.dailyReportData.daily_feed = data.ResponseDetail.daily_feed;
				$scope.dailyReportData.daily_water = data.ResponseDetail.daily_water;
				$scope.dailyReportData.daily_weight = data.ResponseDetail.daily_weight;
				$scope.dailyReportData.curLayWeight = data.ResponseDetail.curLayWeight;
				$scope.dailyReportData.feed_remark = data.ResponseDetail.feed_remark;
				$scope.dailyReportData.medicine_remark = data.ResponseDetail.medicine_remark;

				$scope.dailyReportData.dataInput.culling_all = data.ResponseDetail.culling_all;
				$scope.dailyReportData.dataInput.curLayNum = data.ResponseDetail.curLayNum;
				$scope.dailyReportData.dataInput.daily_feed = data.ResponseDetail.daily_feed;
				$scope.dailyReportData.dataInput.daily_water = data.ResponseDetail.daily_water;
				$scope.dailyReportData.dataInput.daily_weight = data.ResponseDetail.daily_weight;
				$scope.dailyReportData.dataInput.feed_remark = data.ResponseDetail.feed_remark;
				$scope.dailyReportData.dataInput.medicine_remark = data.ResponseDetail.medicine_remark;


				//总蛋重公斤转化为斤
				$scope.dailyReportData.curLayWeight = Number($scope.dailyReportData.curLayWeight * 2).toFixed(2);

				
				if ($scope.dailyReportData.selectDayAge == -1) {//切换栋舍时将后台传的日期赋到页面中
					var NowDatestr = (new Date()).Format("yyyy-MM-dd");
					$scope.dailyReportData.DataDate = NowDatestr;
					$scope.dailyReportData.CurDayAge = data.ResponseDetail.CurDayAge;
					$scope.dailyReportData.showDate = $scope.dailyReportData.CurDayAge;
				}else{
					$scope.dailyReportData.showDate = $scope.dailyReportData.selectDayAge;
				}


				if ($scope.dailyReportData.judgeSwitchHouse) {//切换栋舍时赋最新日龄
					$scope.dailyReportData.CurDayAge = data.ResponseDetail.CurDayAge;
				}


				if ($scope.dailyReportData.showDate == -1) {
					$scope.dailyReportData.showDate = data.ResponseDetail.CurDayAge;
				}


				if ($scope.dailyReportData.feed_remark == "-") {
					$scope.dailyReportData.feed_remark = "";
				}

				if ($scope.dailyReportData.medicine_remark == "-") {
					$scope.dailyReportData.medicine_remark = "";
				}


				$scope.calculateEggWeightOnly();
			}else if (data.ResponseDetail.Result == "Fail"){
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};			
		});

		
	}


    $scope.clickDateInput = function(){
    	$scope.dailyReportData.judgeSwitchHouse = false;
      	Sparraw.openDatePicker("dailyReportData.DataDate","$scope.setShowDateCallBack();");
    };

    $scope.setShowDateCallBack = function(){

    	if (!$scope.dailyReportData.selectHouse || $scope.dailyReportData.selectHouse.length == 0) {
			return app_alert("您负责的栋舍暂未入雏。");
		}

    	var TempNowDate = new Date();
		var NowDatestr = TempNowDate.getFullYear()+"/"+(TempNowDate.getMonth()+1)+"/"+(TempNowDate.getDate()+0);
		var NowDate = new Date(NowDatestr);

		var FormatData = ($scope.dailyReportData.DataDate).replace(/-/g, "/");

    	$scope.dailyReportData.between = $scope.GetDateDiff(FormatData,NowDatestr);

    	if (new Date(FormatData).getTime() > new Date(NowDate).getTime()) {
			$scope.dailyReportData.selectDayAge = $scope.dailyReportData.CurDayAge+$scope.dailyReportData.between;
		}else{
			$scope.dailyReportData.selectDayAge = $scope.dailyReportData.CurDayAge-$scope.dailyReportData.between;
		}
      	$scope.inquire($scope.dailyReportData.HouseId);
    };


    //计算日期差值
	$scope.GetDateDiff = function(startDate,endDate){
		var startTime = new Date(Date.parse(startDate.replace(/-/g,"/"))).getTime();     
	    var endTime = new Date(Date.parse(endDate.replace(/-/g,"/"))).getTime();     
	    var dates = Math.abs((startTime - endTime))/(1000*60*60*24);     
	    return  dates;  
	}


	$scope.judgeHouse = function(item){
		$scope.dailyReportData.HouseBreedId = JSON.parse($scope.dailyReportData.selectHouse).HouseBreedBatchId;
		$scope.dailyReportData.HouseId = JSON.parse($scope.dailyReportData.selectHouse).HouseId;
		$scope.dailyReportData.judgeSwitchHouse = true;
		$scope.dailyReportData.selectDayAge = -1;//切换栋舍时切换至当日

		$scope.inquire($scope.dailyReportData.HouseId);
	}
	
	$scope.calculateEggWeightOnly = function(){

		if ($scope.dailyReportData.curLayNum || $scope.dailyReportData.curLayNum != 0 && 
			$scope.dailyReportData.curLayWeight || $scope.dailyReportData.curLayWeight != 0 &&
			$scope.dailyReportData.curLayWeight > $scope.dailyReportData.curLayNum) {
			$scope.dailyReportData.eggTotalWeight = Number(($scope.dailyReportData.curLayWeight * 500) / $scope.dailyReportData.curLayNum).toFixed(2);
			if ($scope.dailyReportData.eggTotalWeight == Infinity) {
				$scope.dailyReportData.eggTotalWeight = "";
			}
		}else{
			$scope.dailyReportData.eggTotalWeight = "";
		}
	}
	

	$scope.save = function(){
		if ($scope.sparraw_user_temp.Authority.DailyInput === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};

		//判断只蛋重数据是否正常

		if ($scope.dailyReportData.curLayNum || $scope.dailyReportData.curLayNum != 0 && 
			$scope.dailyReportData.curLayWeight || $scope.dailyReportData.curLayWeight != 0 &&
			$scope.dailyReportData.curLayWeight > $scope.dailyReportData.curLayNum) {
			if (Number($scope.dailyReportData.eggTotalWeight) < 20 || Number($scope.dailyReportData.eggTotalWeight) > 100) {
				return app_alert("只蛋重已超出范围(20-100)");
			}
		}
		

		if ($scope.dailyReportData.medicine_remark == "" || !$scope.dailyReportData.medicine_remark) {
			$scope.dailyReportData.medicine_remark = "-";
		}
		if ($scope.dailyReportData.feed_remark == "" || !$scope.dailyReportData.feed_remark) {
			$scope.dailyReportData.feed_remark = "-";
		}
		//总蛋重斤转化为公斤
		$scope.dailyReportData.curLayWeight = Number($scope.dailyReportData.curLayWeight / 2).toFixed(2);

        //删除未修改的数据
        $scope.uploadDataInput = [{
			"day_age"    : Common_NulltoZero($scope.dailyReportData.selectDayAge),
			"culling_all": Common_NulltoZero($scope.dailyReportData.culling_all),
			"curLayNum": Common_NulltoZero($scope.dailyReportData.curLayNum),
			"daily_feed": Common_NulltoZero($scope.dailyReportData.daily_feed),
			"daily_weight": Common_NulltoZero($scope.dailyReportData.daily_weight),
			"daily_water": Common_NulltoZero($scope.dailyReportData.daily_water),
			"curLayWeight": Common_NulltoZero($scope.dailyReportData.curLayWeight),
			"feed_remark":$scope.dailyReportData.feed_remark,
			"medicine_remark":$scope.dailyReportData.medicine_remark
		}];



		var params = {
	      	"HouseBreedId"  :  $scope.dailyReportData.HouseBreedId  ,
	        "HouseId"       :  $scope.dailyReportData.HouseId       ,
			"dataInput"		:  $scope.uploadDataInput
		};


		Sparraw.ajaxPost('layer_dataInput/saveDR_v2.action', params, function(data){
			if (data.ResponseDetail.LoginResult == "Success") {
				chang = false;
	   			app_alert("保存成功！");
	   			for (var i = 0; i < $scope.dailyReportData.showHouseList.length; i++) {
	   				if ($scope.dailyReportData.showHouseList[i].HouseId == $scope.dailyReportData.HouseId) {//获取选中栋舍
	   					if (i+1 == $scope.dailyReportData.showHouseList.length) {//栋舍列表最后一栋
	   						$scope.dailyReportData.HouseId = $scope.dailyReportData.showHouseList[i].HouseId;
	   						$scope.dailyReportData.HouseBreedId = $scope.dailyReportData.showHouseList[i].HouseBreedBatchId;
	   						return $scope.inquire($scope.dailyReportData.HouseId);
	   					}else{//获取选中栋舍的后一栋
	   						$scope.dailyReportData.HouseId = $scope.dailyReportData.showHouseList[i+1].HouseId;
	   						$scope.dailyReportData.HouseBreedId = $scope.dailyReportData.showHouseList[i+1].HouseBreedBatchId;
	   						$scope.dailyReportData.selectHouse = JSON.stringify($scope.dailyReportData.showHouseList[i+1]);
	   						$scope.dailyReportData.selectDayAge = -1;
	   						return $scope.inquire($scope.dailyReportData.HouseId);
	   						
	   					}
	   				}
	   			}

				if(type == 1){
					$state.go("home");
				}else if(type == 2){
					$state.go("productionDaily",{"fromPage":"dailyReport"});
				}
			}else if (data.ResponseDetail.Result == "Fail"){
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};			
		});
	}




	$scope.judgeShow = function(selectHouseId){
		for (var i = 0; i < $scope.dailyReportData.showHouseList.length; i++) {
			if ($scope.dailyReportData.showHouseList[i].HouseId == selectHouseId) {
				if ($scope.dailyReportData.showHouseList[i].HouseBreedStatus == "01") {//展示数据
					$scope.inputList = true;
				}else{
					$scope.inputList = false;
					app_alert("该栋舍未进鸡或已出栏。");
				}
			}
		}
	}

	var chang = false;
	setTimeout(
		function (){
			//首次选中的栋舍
			$scope.dailyReportData.selectHouse = JSON.stringify($scope.dailyReportData.showHouseList[0]);
			if (!$scope.dailyReportData.selectHouse || $scope.dailyReportData.selectHouse.length == 0) {
				return app_alert("您负责的栋舍暂未入雏。");
			}

			$scope.judgeHouse();
		}
	,1000);

	

})