 angular.module('myApp.weekly', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 //生产周报
.controller("weeklyCtrl",function($scope, $state, $http, $ionicLoading,$ionicLoading, $stateParams, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	

	


    $scope.goDailyTable = function(){
    	$state.go("dailyTable");
    }


	

	$scope.weeklyData = {
		"selectHouse"       :  JSON.stringify($scope.sparraw_user_temp.userinfo.houses[0])  ,
		"selectHouseId"     :  ""    ,
		"FarmBreedId"       :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId           ,
		"PlaceNum"          :  ""    , 
        "HouseBreedId"      :  ""    , 
        "CurLayerWeekAge"   :  ""    , 
        "CurGrowthWeekAge"  :  ""    , 
        "AmountFirstLayer"  :  ""    ,
        "DataInfos"         :  ""    , 
    	"ViewType"          :  '01'  ,
    	"weekData"          :  []    ,   
    	"unit"              :  '累计'                 
	}

	$scope.switchUnit = function(){
		//显示哪个
		if ($scope.weeklyData.unit == "累计") {
			$scope.weeklyDiv = true;
			$scope.totalDiv = false;
			$scope.weeklyData.unit = "每周";
			$scope.weeklyData.ViewType = "02";
		}else{
			$scope.weeklyDiv = false;
			$scope.totalDiv = true;
			$scope.weeklyData.unit = "累计";
			$scope.weeklyData.ViewType = "01";
		}
		$scope.judgeHouse();
	}


	$scope.inquire = function(){
		var params = {
			'IsNeedDelay':'Y',
   			"FarmBreedId":$scope.weeklyData.FarmBreedId,
			"HouseId":$scope.weeklyData.selectHouseId,
			"ViewType":$scope.weeklyData.ViewType
		};
		console.log(params);
		Sparraw.ajaxPost('layer_dataInput/queryWR.action', params, function(data){
	   		if (data.ResponseDetail.Result == "Success") {
	   			$scope.weeklyData.weekData          =  data.ResponseDetail.weekData          ;
	   			$scope.weeklyData.DataInfos         =  data.ResponseDetail.DataInfos         ;
				$scope.weeklyData.CurGrowthWeekAge  =  data.ResponseDetail.CurGrowthWeekAge  ;
				$scope.weeklyData.CurLayerWeekAge   =  data.ResponseDetail.CurLayerWeekAge   ;
				$scope.weeklyData.PlaceDate         =  data.ResponseDetail.PlaceDate         ;
				$scope.weeklyData.PlaceNum          =  data.ResponseDetail.PlaceNum          ;
				$scope.weeklyData.AmountFirstLayer  =  data.ResponseDetail.AmountFirstLayer  ;
	   			$scope.GetTable();
			}else if (data.ResponseDetail.Result == "Fail"){
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};			
		});
	}



  	$scope.GetTable = function(){
		if ($scope.weeklyData.unit == "每周") {

			console.log("累计！@！！");
			//累计数据
			var TotalHeadName = [
				"growth_weekage"   ,//int型，生长周龄
				"accCD"            ,//int型，累计死淘数，只
				"accCDRate"        ,//varchar型，累计死淘率
				"accLayNum"        ,//int型，累计产蛋数，单位：万枚
				"accLayWeight"     ,//number型，累计产蛋公斤数，单位：公斤
				"accLayNumPer"     ,//int型，只鸡累计产蛋数，单位：枚
				"accLayWeightPer"  ,//number型，只鸡累计产蛋总重量，单位：公斤
				"accFeedWeight"    ,//number型，累计饲料总重量，单位：吨
				"accFeedWeightPer" ,//number型，只鸡累计饲料消耗，单位：公斤/天·只
				"rOfFE"            ,//number型，累计料蛋比
				"accWater"          //number型，累计饮水，单位：立方
			];
			var TotalHeadText = ["生长<br>周龄",
								"",
								"",
								"",
								"",
								"",
								"",
								"",
								"",
								"累计<br>料蛋比",
								"累计饮水<br>立方"];
			var TotalTemplate = ['',
								'<div style="width:100px;height:40px;"><div style="width:100px;height:20px; background:rgba(251, 251, 251, 1);"><p style="text-align:center;">' + "累计死淘" + '</p></div><div style="width:50px;height:20px;"><div style="width:50px;height:20px;"><p style="text-align:center;">' + "只" + '</p></div><div style="width:50px;height:20px; position:relative;left:50px;top: -20px;"><p style="text-align:center;"> ' + "‰" + '</p></div></div></div>',
								'',
								'<div style="width:100px;height:40px;"><div style="width:110px;height:20px; background:rgba(251, 251, 251, 1);"><p style="text-align:center;">' + "累计产蛋" + '</p></div><div style="width:55px;height:20px;"><div style="width:55px;height:20px;"><p style="text-align:center;">' + "万枚" + '</p></div><div style="width:55px;height:20px; position:relative;left:50px;top: -20px;"><p style="text-align:center;"> ' + "公斤" + '</p></div></div></div>',
								'',
								'<div style="width:110px;height:40px;"><div style="width:110px;height:20px; background:rgba(251, 251, 251, 1);"><p style="text-align:center;">' + "只鸡累计产蛋" + '</p></div><div style="width:55px;height:20px;"><div style="width:55px;height:20px;"><p style="text-align:center;">' + "枚" + '</p></div><div style="width:55px;height:20px; position:relative;left:55px;top: -20px;"><p style="text-align:center;"> ' + "公斤" + '</p></div></div></div>',
								'',
								'<div style="width:120px;height:40px;"><div style="width:120px;height:20px; background:rgba(251, 251, 251, 1);"><p style="text-align:center;">' + "累计饲料消耗" + '</p></div><div style="width:70px;height:20px;"><div style="width:70px;height:20px;"><p style="text-align:center;">' + "吨" + '</p></div><div style="width:50px;height:20px; position:relative;left:50px;top: -20px;"><p style="text-align:center;"> ' + "公斤/只" + '</p></div></div></div>',
								'',
								'',
								''];

			var TotalheaderDiv = [];
			for (var i = 0; i < TotalHeadName.length; i++) {
				TotalheaderDiv.push("<div style='color:black;text-align:center;position:relative; left:0px; top:0px;height:40px;'><p>" + TotalHeadText[i] + "</p></div>");
			}

			for (var i = 0; i < TotalHeadName.length; i++) {
				if (i == 0) {
					$scope.gridTotal.columnDefs.push({
						name                :  TotalHeadName[i],
						enableColumnMenu    :  false,
						width               :  '40',
						displayName         :  '',
						height				:  '40',
						headerCellTemplate  :  TotalheaderDiv[i],
						cellTemplate        :  '',
						pinnedLeft          :  true,
						headerCellClass     :  '',
						cellClass           :  'middle' 
					})
				}else if (i == 1||i == 2) {
					$scope.gridTotal.columnDefs.push({
						name                :  TotalHeadName[i],
						enableColumnMenu    :  false,
						width               :  '50',
						height				:  '40',
						displayName         :  '',
						headerCellTemplate  :  TotalTemplate[i],
						cellTemplate        :  '',
						headerCellClass     :  '',
						cellClass           :  'middle' 
					})
				}else if (i == 3||i == 4) {
					$scope.gridTotal.columnDefs.push({
						name                :  TotalHeadName[i],
						enableColumnMenu    :  false,
						width               :  '55',
						height				:  '40',
						displayName         :  '',
						headerCellTemplate  :  TotalTemplate[i],
						cellTemplate        :  '',
						headerCellClass     :  '',
						cellClass           :  'middle' 
					})
				}else if (i == 5 || i == 6) {
					$scope.gridTotal.columnDefs.push({
						name                :  TotalHeadName[i],
						enableColumnMenu    :  false,
						width               :  '55',
						height				:  '40',
						displayName         :  '',
						headerCellTemplate  :  TotalTemplate[i],
						cellTemplate        :  '',
						headerCellClass     :  '',
						cellClass           :  'middle'
					})
				}else if(i == 8 || i == 7){
					$scope.gridTotal.columnDefs.push({
						name                :  TotalHeadName[i],
						enableColumnMenu    :  false,
						width               :  '60',
						height				:  '40',
						displayName         :  '',
						headerCellTemplate  :  TotalTemplate[i],
						cellTemplate        :  '',
						headerCellClass     :  '',
						cellClass           :  'middle'
					})
				}else if (i == 10 || i == 9) {
					$scope.gridTotal.columnDefs.push({
						name                :  TotalHeadName[i],
						enableColumnMenu    :  false,
						width               :  '70',
						height				:  '40',
						displayName         :  '',
						headerCellTemplate  :  TotalheaderDiv[i],
						cellTemplate        :  '',
						headerCellClass     :  '',
						cellClass           :  'middle' 
					})
				}
			}
			for (var i = 0; i < $scope.weeklyData.weekData.length; i++) {
				$scope.weeklyData.weekData[i].accFeedWeight = ($scope.weeklyData.weekData[i].accFeedWeight/1000).toFixed(1)
			}
			$scope.gridTotal.data = $scope.weeklyData.weekData;
			window.onresize = function(){};
		}else{
			console.log("周！！！！@！！");
			var TotalTemplate = ['',
                                '<div style="width:100px;height:40px;"><div style="width:100px;height:20px; background:rgba(251, 251, 251, 1)"><p style="text-align:center;">' + "周死淘" + '</p></div>' +
                                '<div style="width:50px;height:20px;"><div style="width:55px;height:20px;"><p style="text-align:center;">' + "只" + '</p></div>' +
                                '<div style="width:50px;height:20px; position:relative;left:55px; top:-20px;"><p style="text-align:center;"> ' + "‰" + '</p></div></div></div>',
								'',
								'<div style="width:110px;height:40px;"><div style="width:110px;height:20px; background:rgba(251, 251, 251, 1)"><p style="text-align:center;">' + "周产蛋" + '</p></div>' +
								'<div style="width:55px;height:20px;"><div style="width:55px;height:20px;"><p style="text-align:center;">' + "枚" + '</p></div>' +
								'<div style="width:55px;height:20px; position:relative;left:55px; top:-20px;"><p style="text-align:center;"> ' + "%" + '</p></div></div></div>',
							    '',
							    '<div style="width:120px;height:40px;"><div style="width:120px;height:20px; background:rgba(251, 251, 251, 1)"><p style="text-align:center;">' + "蛋重量" + '</p></div>' +
								'<div style="width:60px;height:20px;"><div style="width:60px;height:20px;"><p style="text-align:center;">' + "公斤" + '</p></div>' +
								'<div style="width:60px;height:20px; position:relative;left:60px; top:-20px;"><p style="text-align:center;"> ' + "克/枚" + '</p></div></div></div>',
								'',
								'',
								'<div style="width:120px;height:40px;"><div style="width:120px;height:20px; background:rgba(251, 251, 251, 1)"><p style="text-align:center;">' + "饲料消耗" + '</p></div>' +
								'<div style="width:60px;height:20px;"><div style="width:60px;height:20px;"><p style="text-align:center;">' + "公斤" + '</p></div>' +
							    '<div style="width:60px;height:20px; position:relative;left:60px; top:-20px;"><p style="text-align:center;"> ' + "克/只天" + '</p></div></div></div>',
								'',
								'<div style="width:120px;height:40px;"><div style="width:120px;height:20px; background:rgba(251, 251, 251, 1)"><p style="text-align:center;">' + "饮水量" + '</p></div>' +
								'<div style="width:55px;height:20px;"><div style="width:55px;height:20px;"><p style="text-align:center;">' + "立方" + '</p></div>' +
								'<div style="width:65px;height:20px; position:relative;left:55px; top:-20px;"><p style="text-align:center;"> ' + "ml/只天" + '</p></div></div></div>'];
			var TotalHeadName = ["生长<br>周龄",
								 "",
								 "",
								 "",
								 "",
								 "",
								 "",
								 "破损<br>枚",
								 "",
								 "",
								 "",
								 "",
								 "水料<br>比",
								 "体重<br>公斤"];

			var TotalheaderDiv = [];
			for (var i = 0; i < TotalHeadName.length; i++) {
                TotalheaderDiv.push("<div style='color:black;text-align:center;position:relative; left:0px; top:0px;height:40px;'><p>" + TotalHeadName[i] + "</p></div>");
			}
			//周数据
			var WeeklyHeadName = [
				"growth_weekage",//int型，生长周龄
				"culling_all",//int型，当周死淘数
				"cull_rate",//varchar型，当周死淘率
				"curLayNum",//int型，当周产蛋总量
				"curLayRate",//varchar型,当周产蛋率
				"curLaySumWeight",//number型，鸡蛋总重，公斤
				"curLayAvgWeight",//number型，鸡蛋均重，克/枚
				"curBrokenNum",//int型，破损蛋数量
				"weekly_feed",//number型，当周饲料，公斤
				"avg_feed",//number型，平均饲料，g/天·只
				"weekly_water",//number型，当周饮水量，立方
				"avg_water",//number型，平均饮水量，ml/天·只
				"rOfWM",//number型，水料比
				"chickenWeight",//number型，鸡体重，公斤
		    ];
			for (var i = 0; i < WeeklyHeadName.length; i++) {
				if (i == 0) {
					$scope.gridWeekly.columnDefs.push({
						name                :  WeeklyHeadName[i],
						enableColumnMenu    :  false,
						width               :  '40',
						displayName         :  '',
						headerCellTemplate  :  TotalheaderDiv[i],
						cellTemplate        :  '',
						headerCellClass     :  '',
						pinnedLeft          :  true,
						cellClass           :  'middlebold'
					})
				}else if(i == 1 || i == 3 || i == 2 || i == 4) {
					$scope.gridWeekly.columnDefs.push({
						name                :  WeeklyHeadName[i],
						enableColumnMenu    :  false,
						width               :  '55',
						displayName         :  '',
						headerCellTemplate  :  TotalTemplate[i],
						cellTemplate        :  '',
						headerCellClass     :  '',
						cellClass           :  'middle'
					})
				}else if(i == 5 || i == 6){
					$scope.gridWeekly.columnDefs.push({
						name                :  WeeklyHeadName[i],
						enableColumnMenu    :  false,
						width               :  '60',
						displayName         :  '',
						headerCellTemplate  :  TotalTemplate[i],
						cellTemplate        :  '',
						headerCellClass     :  '',
						cellClass           :  'middle'
					})
				}else if (i == 8||i == 10) {
					$scope.gridWeekly.columnDefs.push({
						name                :  WeeklyHeadName[i],
						enableColumnMenu    :  false,
						width               :  '60',
						displayName         :  '',
						headerCellTemplate  :  TotalTemplate[i],
						cellTemplate        :  '',
						headerCellClass     :  '',
						cellClass           :  'middle' 
					})
				}else if (i == 9||i == 11) {
					$scope.gridWeekly.columnDefs.push({
						name				: WeeklyHeadName[i],
						enableColumnMenu	: false,
						width				: '70',
						displayName			: '',
						headerCellTemplate	: TotalTemplate[i],
						cellTemplate		: '',
						headerCellClass		: '',
						cellClass			: 'middle'
					})
				}else{
					$scope.gridWeekly.columnDefs.push({
						name                :  WeeklyHeadName[i],
						enableColumnMenu    :  false,
						width               :  '50',
						displayName         :  '',
						headerCellTemplate  :  TotalheaderDiv[i],
						cellTemplate        :  '',
						headerCellClass     :  '',
						cellClass           :  'middle' 
					})
				}
			}
			$scope.gridWeekly.data = $scope.weeklyData.weekData;
			window.onresize = function(){};
		}
  	}
	$scope.judgeHouse = function(){
		$scope.weeklyData.selectHouseId = JSON.parse($scope.weeklyData.selectHouse).HouseId;
		$scope.inquire();
	}
	



	setLandscape(false,true);
	//表格初始化
	$scope.gridWeekly = {};
	$scope.gridWeekly.columnDefs = [];
	$scope.gridTotal  = {};
	$scope.gridTotal.columnDefs = [];
	$scope.judgeHouse();




})