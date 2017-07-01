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
	$scope.goDailyTable = function(){
		if ($scope.sparraw_user_temp.Authority.DailyInput === "All") {

		}else{
			return $state.go("dailyTable");
		};
		if(chang == true){
			type = 1;
			app_confirm('数据已经被修改，请确认是否保存！','提示',null,function(buttonIndex){
                    if(buttonIndex == 2){
						$scope.save();
			        }else{
						$state.go("dailyTable");
					}
              });
		}else{
			$state.go("dailyTable");
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
        "selectHouse"    :  JSON.stringify($scope.sparraw_user_temp.userinfo.houses[0])       ,
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
		"dataInput"		 : [{
			"day_age"    : "",
			"culling_all": "",
			"curLayNum":  "",
			"egg_box_size": "25.5",
			"curBrokenNum": "",
			"daily_feed": "",
			"daily_weight": "",
			"daily_water": ""
		}]

	}
	

    $scope.inquire = function() {
		var params = {
	      	"FarmBreedId" :  $scope.dailyReportData.FarmBreedId    ,
			"HouseId"     :  $scope.dailyReportData.HouseId        ,
			"DayAge"      :  $scope.dailyReportData.selectDayAge
		};
		Sparraw.ajaxPost('layer_dataInput/queryDR_v2.action', params, function(data){
	   		if (data.ResponseDetail.Result == "Success") {
	   			//赋值
				$scope.dailyReportData.CurDate = data.ResponseDetail.CurDate,
				$scope.dailyReportData.GrowthWeekAge = data.ResponseDetail.GrowthWeekAge,
				$scope.dailyReportData.LayerWeekAge = data.ResponseDetail.LayerWeekAge,
				$scope.dailyReportData.culling_all = data.ResponseDetail.culling_all,
				$scope.dailyReportData.curLayNum = data.ResponseDetail.curLayNum,
				//$scope.dailyReportData.egg_box_size = data.ResponseDetail.egg_box_size,
				$scope.dailyReportData.curBrokenNum = data.ResponseDetail.curBrokenNum,
				$scope.dailyReportData.daily_feed = data.ResponseDetail.daily_feed,
				$scope.dailyReportData.daily_water = data.ResponseDetail.daily_water,
				$scope.dailyReportData.daily_weight = data.ResponseDetail.daily_weight,
				$scope.dailyReportData.dataInput.culling_all = data.ResponseDetail.culling_all,
				$scope.dailyReportData.dataInput.curBrokenNum = data.ResponseDetail.curBrokenNum,
				$scope.dailyReportData.dataInput.curLayNum = data.ResponseDetail.curLayNum,
				$scope.dailyReportData.dataInput.egg_box_size = data.ResponseDetail.egg_box_size,
				$scope.dailyReportData.dataInput.daily_feed = data.ResponseDetail.daily_feed,
				$scope.dailyReportData.dataInput.daily_water = data.ResponseDetail.daily_water,
				$scope.dailyReportData.dataInput.daily_weight = data.ResponseDetail.daily_weight

				if ($scope.dailyReportData.CurDayAge == "") {
					var NowDatestr = new Date().Format("yyyy-MM-dd");
					$scope.dailyReportData.DataDate = NowDatestr;
					$scope.dailyReportData.CurDayAge = data.ResponseDetail.CurDayAge;
					$scope.dailyReportData.showDate = $scope.dailyReportData.CurDayAge;
				}else{
					$scope.dailyReportData.showDate = $scope.dailyReportData.selectDayAge;
				}

				if ($scope.dailyReportData.showDate == -1) {
					$scope.dailyReportData.showDate = data.ResponseDetail.CurDayAge;
				}else{
					
				}
				//算出总箱数
				$scope.dailyReportData.all_box_num = ($scope.dailyReportData.curLayNum / 360).toFixed(1);
				//算出规格
				if (data.ResponseDetail.egg_box_size == 0) {
					$scope.dailyReportData.egg_box_size = "25.5";
				}else{
					$scope.dailyReportData.egg_box_size = (data.ResponseDetail.egg_box_size * 2).toFixed(1);
				}


			}else if (data.ResponseDetail.Result == "Fail"){
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};			
		});

		
	}

	

	


    $scope.clickDateInput = function(){
      	Sparraw.openDatePicker("dailyReportData.DataDate","$scope.setShowDateCallBack();");
    };

    $scope.setShowDateCallBack = function(){

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
      	$scope.inquire();
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
		$scope.inquire();
	}

	setTimeout(
		function (){
			$scope.judgeHouse();
		}
	,1000);
	


	var chang = false;
	$scope.changeValue = function(){
		chang = true;
		if (isNaN($scope.dailyReportData.curLayNum)) {
			$scope.dailyReportData.all_box_num = 0;
		}else{
			$scope.dailyReportData.all_box_num = ($scope.dailyReportData.curLayNum / 360).toFixed(1);
		}
	}
	

	$scope.save = function(){
		
		

		
		$scope.dailyReportData.egg_box_size = ($scope.dailyReportData.egg_box_size / 2).toFixed(2);
        //删除未修改的数据
        $scope.uploadDataInput = [{
			"day_age"    : Common_NulltoZero($scope.dailyReportData.selectDayAge),
			"culling_all": Common_NulltoZero($scope.dailyReportData.culling_all),
			"curLayNum": Common_NulltoZero($scope.dailyReportData.curLayNum),
			"egg_box_size": Common_NulltoZero($scope.dailyReportData.egg_box_size),
			"curBrokenNum": Common_NulltoZero($scope.dailyReportData.curBrokenNum),
			"daily_feed": Common_NulltoZero($scope.dailyReportData.daily_feed),
			"daily_weight": Common_NulltoZero($scope.dailyReportData.daily_weight),
			"daily_water": Common_NulltoZero($scope.dailyReportData.daily_water)
		}];

		var params = {
	      	"HouseBreedId"  :  $scope.dailyReportData.HouseBreedId  ,
	        "HouseId"       :  $scope.dailyReportData.HouseId       ,
			"dataInput"		:  $scope.uploadDataInput   		    ,
	        "culling_all"   :  $scope.dailyReportData.culling_all   ,
	        "curLayNum"     :  $scope.dailyReportData.curLayNum     ,
	        "egg_box_size"  :  $scope.dailyReportData.egg_box_size  ,
	        "curBrokenNum"  :  $scope.dailyReportData.curBrokenNum  ,
	        "daily_feed"    :  $scope.dailyReportData.daily_feed    ,
	        "daily_weight"  :  $scope.dailyReportData.daily_weight  ,
	        "daily_water"   :  $scope.dailyReportData.daily_water
		};
		if ($scope.sparraw_user_temp.Authority.DailyInput === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		Sparraw.ajaxPost('layer_dataInput/saveDR_v2.action', params, function(data){
			if (data.ResponseDetail.LoginResult == "Success") {
				chang = false;
	   			Sparraw.myNotice("保存成功！");
	   			$scope.inquire();
				if(type == 1){
					$state.go("dailyTable");
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
})