angular.module('myApp.eggSellsList', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//销售日报
.controller("eggSellsListCtrl",function($scope, $state, $ionicLoading,$stateParams, $http, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	var farmId = $scope.sparraw_user_temp.farminfo.id;
	var farmFeedId = $scope.sparraw_user_temp.farminfo.farmBreedBatchId;
	
	setPortrait(true,true);

	//销售金额结算
	$scope.doBack = function(){
		if($stateParams.fromPage == "eggSellsReportTable"){
			$state.go("home");
		}else if($stateParams.fromPage == "eggSellsReport"){
			$state.go("home");
		}else{
			$state.go("home");
		}
	};
	
	
	if(farmId == 0){
		Sparraw.myNotice("暂无数据，请先新建农场");
		return;
	}
	
	if(farmFeedId == 0){
		Sparraw.myNotice("暂无数据，请先新建批次");
		return;
	}
	
	
	var date = new Date();

	
	console.log(date.getFullYear() + " " + (date.getMonth() + 1) + ", farmId:" + farmId + ",farmFeedId:" + farmFeedId);
	


 	var todayDate = new Date().Format("yyyy-MM");
	
	

 	var months = [];

	var currYear = date.getFullYear();
 	var currMonth = (date.getMonth() + 1);


 	for(var i = currMonth; i > currMonth - 6; i--){

 		if(i <= 0){
 			var lastMonth = 12 + i;
 			var lastYear = (currYear - 1);
			if( lastMonth > 9){
				months.push(lastYear + "-" + lastMonth);
 			}else{
 				months.push(lastYear + "-0" + lastMonth);
 			}

 		}else{
 			if( i > 9){
				months.push(currYear + "-" + i);
 			}else{
 				months.push(currYear + "-0" + i);
 			}
 			
 		}
 	}

	console.log("months:" + months);
	
	var today = currMonth + "月" + date.getDate() + "日";
	// 0 周日 6 周六
	var weekday = date.getDay();
	if(weekday == 0){
		today += ",周日";
	}else if(weekday == 1){
		today += ",周一";
	}else if(weekday == 2){
		today += ",周二";
	}else if(weekday == 3){
		today += ",周三";
	}else if(weekday == 4){
		today += ",周四";
	}else if(weekday == 5){
		today += ",周五";
	}else if(weekday == 6){
		today += ",周六";
	}

	// 合格鸡蛋总重量
	var total_Weight_good = 0;
	var total_Weight_bad = 0;
	// 合格鸡蛋总金额
	var total_Price_sum_good = 0;
	var total_Price_sum_bad = 0;

	// 合格鸡蛋均价
	var total_Price_p_good = 0;
	var total_Price_p_bad = 0;


	// 本批次合格鸡蛋总重量
	var total_Weight_good_breed = 0;
	var total_Weight_bad_breed = 0;
	// 本批次合格鸡蛋总金额
	var total_Price_good_breed = 0;
	var total_Price_bad_breed = 0;

	var total_Price_p_good_breed = 0;
	var total_Price_p_bad_breed = 0;


	$scope.farmData = {
		"FarmId"          :  farmId                ,//int型，农场id
		"BreedBatchId"    :  farmFeedId  ,
		"farmBreedStatus" :  $scope.sparraw_user_temp.farminfo.farmBreedStatus   ,  //农场批次状态
		"BatchCode"       :  ""  ,//varchar型，批次编号
		"place_date"      :  todayDate ,//varchar型，入雏日期
		"place_day_age"   :  ""  ,//int型，入舍日龄
		"place_week_age"  :  ""  ,//int型，入雏周龄
		//"place_num"       :  ""  ,//int型，入雏数量
		"place_detail"    :  []  ,//各栋舍入舍数量
		"Place_Sum"       :  "" ,//计算合计的入雏总量
		"months"		  : months,// 半年内月份 ["2016-01","2016-02","2016-03","2016-04"]
		"selectMonth"	  : todayDate,//
		"dataInput"		  :[],//
		"total_Weight_good":total_Weight_good,
		"total_Weight_bad":total_Weight_bad,
		"total_Price_sum_good":total_Price_sum_good,
		"total_Price_sum_bad":total_Price_sum_bad,
		"total_Price_p_good":total_Price_p_good,
		"total_Price_p_bad":total_Price_p_bad,
		"total_Weight_good_breed":total_Weight_good_breed,
		"total_Weight_bad_breed":total_Weight_bad_breed,
		"total_Price_good_breed":total_Price_good_breed,
		"total_Price_bad_breed":total_Price_bad_breed,
		"total_Price_p_good_breed":total_Price_p_good_breed,
		"total_Price_p_bad_breed":total_Price_p_bad_breed,
		"temp":1,
		"today":today,
	}


	var modifyData = [];

 	//销售金额结算
	$scope.forageTotal = function(sell_date){
		
	};




	$scope.inquire = function(selectMonth){
    	console.log("farmId:" + farmId + "," + "selectMonth:" + selectMonth);
    	

		if (farmId == "") {
			//Sparraw.myNotice("暂无数据，请先登陆");
			console.log("暂无数据，请先登陆");
		}else{
			var params = {
		      	"FarmId"  :  farmId  ,
		      	"FarmBreedId"  :  farmFeedId  ,
				"SelectMonth"  :  selectMonth
			};

			console.log(params);
			/*
			good_price_value//销售单价
			good_sale_weight//销售数量
			good_sale_money//销售金额
			*/
			Sparraw.ajaxPost('layer_salesInput/reportTable.action', params, function(data){
				console.log(data);
		   		if (data.ResponseDetail.Result == "Success") {
					$scope.farmData.dataInput = data.ResponseDetail.saleDetails;
					
					for(var i = 0 ; i < $scope.farmData.dataInput.length;i ++){
						var sell = $scope.farmData.dataInput[i];
						sell.good_price_value = (Number(sell.good_price_value)/2).toFixed(2);//销售单价公斤转化为斤
						sell.good_sale_weight = (Number(sell.good_sale_weight)*2).toFixed(2);//销售数量公斤转化为斤
						sell.good_sale_money = Number(sell.good_price_value*2).toFixed(2) * Number(sell.good_sale_weight/2).toFixed(2);//销售金额
					}

					console.log($scope.farmData.dataInput);

				}else if (data.ResponseDetail.Result == "Fail"){
					Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				}else{
					Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				};
			});

		}
	}

  	$scope.hiddenEmptyData = function(obj){
		// 0 周日 6 周六
		var date = new Date(obj);
		var weekday = date.getDay();
		//console.log("date:" + obj + ",weekday:" + date.getDay());
		
  		var day = weekday;//parseInt(obj.substring(4));

		//每隔七天添加下划线,如果第一天就是周六，就不用划线
		if (day == 6 && date.getDate() > 1) {
			//console.log(day);
    		return "{'border-bottom':'solid 1px #606060'}";
    	}else{
    		return "{'border-bottom':'solid 1px #D0D0D0'}";
    	}
    	
    }

	$scope.chooseDiv = false;
	$scope.getFocus = function(){
		//$scope.chooseDiv = true;
		//document.getElementById('blankDiv').style.height = 3 + 'rem';
	}

	$scope.loseBlur = function(item,judgeType){
		//$scope.chooseDiv = false;
		//document.getElementById('blankDiv').style.height = '6rem';
	}

    $scope.changeMonth = function(val){

    	var selectMonth = $scope.farmData.selectMonth;
		modifyData = [];
		// 合格鸡蛋总重量
		 total_Weight_good = 0;
		 total_Weight_bad = 0;
		// 合格鸡蛋总金额
		 total_Price_sum_good = 0;
		 total_Price_sum_bad = 0;

		 total_Price_p_good = 0;
		 total_Price_p_bad = 0;

		$scope.farmData.total_Weight_good = total_Weight_good;
		$scope.farmData.total_Weight_bad = total_Weight_bad;
		$scope.farmData.total_Price_sum_good = total_Price_sum_good;
		$scope.farmData.total_Price_sum_bad = total_Price_sum_bad;
		$scope.farmData.total_Price_p_good = total_Price_p_good;
		$scope.farmData.total_Price_p_bad = total_Price_p_bad;

		$scope.inquire(selectMonth);
		// 测试用
		//$scope.getBreedSum();
    };

 	

 	setTimeout(
		function (){
			$scope.changeMonth();
		}
	,1000);


	$scope.save = function(){

		if(modifyData.length > 0) {
			var selectMonth = $scope.farmData.selectMonth;
			console.log("modifyData:" + JSON.stringify(modifyData));

			var params = {
			      	"FarmId"  :  farmId  ,
					"FarmBreedId"  :  farmFeedId  ,
					"saleDetails"    :  modifyData
				};

				Sparraw.ajaxPost('layer_salesInput/saveDR.action', params, function(data){
			   		if (data.ResponseDetail.Result == "Success") {
						modifyData = [];
						$scope.getBreedSum();
						Sparraw.myNotice("保存成功");
						
					}else if (data.ResponseDetail.Result == "Fail"){
						Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
					}else{
						Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
					};
				});

			
		}else{
			Sparraw.myNotice("未修改任何数据");
		}
		

	}
	
	
	$scope.doShowSalesAnalyze = function(){
		$state.go("home");
		
	}
})

