angular.module('myApp.eggSellsReport', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//销售记录
.controller("eggSellsReportCtrl",function($scope, $state, $ionicLoading, $http, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	var farmId = $scope.sparraw_user_temp.farminfo.id;
	var farmFeedId = $scope.sparraw_user_temp.farminfo.farmBreedBatchId;

	if(farmId == 0){
		Sparraw.myNotice("暂无数据，请先新建农场");
		return;
	}
	
	if(farmFeedId == 0){
		Sparraw.myNotice("暂无数据，请先新建批次");
		return;
	}
	
	
	var date = new Date();

	console.log("farmId:" + farmId + ",farmFeedId:" + farmFeedId);
	
	var today = (date.getMonth() + 1) + "月" + date.getDate() + "日";
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
	
	var guige = {};
	for(var i = 25; i <= 34; i++){
		var obj = new Object();
		var key = i + 0.5;
		var val = i + "~"+ (i+1);
		guige[""+key+""] = val;
		//guige.push(obj);
	}
	console.log("guige:" + (guige))

	// 合格鸡蛋单价
	var good_price_value = 0;
	// 合格鸡蛋总重量
	var good_sale_weight = 0;
	// 合格鸡蛋总金额
	var good_sale_money = 0;
	
	var curDate = new Date().Format("yyyy-MM-dd");

	var key = "defaultGuige";
	var defaultValue = Common_getObjectFromLocalStorage(key);
	console.log("defaultValue:"+defaultValue);
	if(defaultValue == null || defaultValue == undefined){
		defaultValue = 25.5;
	}

	$scope.farmData = {
		"FarmId"          :  farmId                ,//int型，农场id
		"BreedBatchId"    :  farmFeedId  ,
		"selectDate"	  : curDate,//
		"guige":guige,
		"defaultValue":defaultValue,
		"checked":true,
		"sellData"	  :{

			"good_price_value":good_price_value,
			"good_sale_weight":good_sale_weight,
			"good_sale_money":good_sale_money,
			},
		"temp":1,
		"today":today,
		"TempGood_price_value":"",
		"TempGood_good_sale_weight":"",
		"TempGood_sale_money":"",

	}
	
	$scope.pushNotificationChange = function(){
		if($scope.farmData.checked == true){
			//console.log("true");
			document.getElementById('brokenEggsDIV').style.display = '';
		}else{
			//console.log("false");
			document.getElementById('brokenEggsDIV').style.display = 'none';
		}
	}
	
	


	$scope.inquire = function(selectDate){
    	console.log("farmId:" + farmId + "," + "selectDate:" + selectDate);
    	

		if (farmId == "") {
			//Sparraw.myNotice("暂无数据，请先登陆");
			console.log("暂无数据，请先登陆");
		}else{
			var params = {
		      	"FarmId"  :  farmId  ,
		      	"FarmBreedId"  :  farmFeedId  ,
				"SelectDate"  :  selectDate
			};

			console.log(params);
			
			Sparraw.ajaxPost('layer_salesInput/queryDRByDate.action', params, function(data){
				console.log("------------------------");
				console.log(data);
				console.log("------------------------");
		   		if (data.ResponseDetail.Result == "Success") {
					//$scope.farmData.dataInput = data.ResponseDetail.saleDetails;
					console.log("data size :" + data.ResponseDetail.saleDetails.length);
					if(data.ResponseDetail.saleDetails.length > 0) {
						var sellData = data.ResponseDetail.saleDetails[0];
						$scope.farmData.sellData = sellData;
						//保存首次获取的参数
						$scope.farmData.TempGood_sale_money = $scope.farmData.sellData.good_sale_money;
						//公斤转化为斤
						$scope.farmData.sellData.good_price_value = (Number($scope.farmData.sellData.good_price_value)/2).toFixed(2);
						$scope.farmData.sellData.good_sale_weight = (Number($scope.farmData.sellData.good_sale_weight)*2).toFixed(2);

					}else{
						$scope.farmData.sellData = {
									"good_sale_weight":good_sale_weight,
									"good_salebox_num":good_salebox_num,
									"good_price_value":good_price_value,
									"good_box_size":good_box_size,
									"good_price_type":"01",
									"good_sale_money":good_sale_money,
									"broken_box_size":broken_box_size,
									"broken_price_value":broken_price_value,
									"broken_salebox_num":broken_salebox_num,
									"broken_sale_weight":broken_sale_weight,
									"broken_sale_money":broken_sale_money,
									"broken_price_type":"01",
									"chicken_manure":chicken_manure
								};
						Sparraw.myNotice("日期不在产蛋周期内！");
					}
					
					
					
				}else if (data.ResponseDetail.Result == "Fail"){
					Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				}else{
					Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				};
			});

		}
	}

  	
	setTimeout(
		function (){
			$scope.inquire(curDate);
		}
	,1000);
	
	

    $scope.changeMonth = function(val){

    	// 合格鸡蛋总重量
		good_sale_weight = 0;
		// 合格鸡蛋总箱数
		good_salebox_num = 0;
		// 合格鸡蛋单价
		good_price_value = 0;
		// 合格鸡蛋每箱规格
		good_box_size = 0;
		// 合格鸡蛋总金额
		good_sale_money = 0;
		
		
		// 破损鸡蛋总重量
		broken_sale_weight = 0;
		// 破损鸡蛋总箱数
		broken_salebox_num = 0;
		// 破损鸡蛋单价
		broken_price_value = 0;
		// 破损鸡蛋每箱规格
		broken_box_size = 0;
		// 破损鸡蛋总金额
		broken_sale_money = 0;

		// 鸡粪收入
		chicken_manure = 0;
		
		

		$scope.inquire(val);
		// 测试用
		//$scope.getBreedSum();
    };




	$scope.save = function(){
		
		if($scope.farmData.sellData.isHistory == undefined){
			Sparraw.myNotice("日期不在产蛋周期内,无法保存。");
			return;
		}

		var selectDate = $scope.farmData.selectDate;
		
		
		var sellData = $scope.farmData.sellData;
		
	
		sellData.good_price_value = ($scope.farmData.sellData.good_price_value);
		sellData.good_box_size = ($scope.farmData.sellData.good_box_size/2).toFixed(2);
	
		sellData.broken_price_value = ($scope.farmData.sellData.broken_price_value);
		sellData.broken_box_size = ($scope.farmData.sellData.broken_box_size/2).toFixed(2);
		
		

		//斤转化为公斤
		sellData.good_price_value = (Number($scope.farmData.sellData.good_price_value)*2).toFixed(2);
		sellData.good_sale_weight = (Number($scope.farmData.sellData.good_sale_weight)/2).toFixed(2);
			
	
		
			var params = {
				"FarmId"  :  farmId  ,
				"FarmBreedId"  :  farmFeedId  ,
				"saleDetails"    :  [sellData]
			};

			Sparraw.ajaxPost('layer_salesInput/saveDRV2.action', params, function(data){
				if (data.ResponseDetail.Result == "Success") {
					
					$scope.inquire(selectDate);
					Sparraw.myNotice("保存成功");
					
					
				}else if (data.ResponseDetail.Result == "Fail"){
					Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				}else{
					Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				};
			});

	}
	
	
	$scope.doShowSalesAnalyze = function(){
		$state.go("home");
	}
	



    $scope.clickDateInput = function(){
      	Sparraw.openDatePicker("farmData.selectDate","$scope.setShowDateCallBack();");
    };

    $scope.setShowDateCallBack = function(){
      	$scope.changeMonth($scope.farmData.selectDate);
    };
    

    $scope.backFun = function(){
    	$scope.modifiedStatus = false;
    	if ($scope.farmData.TempGood_sale_money != $scope.farmData.sellData.good_sale_money) {
    		$scope.modifiedStatus = true;
    	}

		if ($scope.modifiedStatus) {
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

    $scope.goEggSellsTable = function(){
    	$scope.modifiedStatus = false;
		if ($scope.farmData.TempGood_sale_money != $scope.farmData.sellData.good_sale_money) {
			$scope.modifiedStatus = true;
		}
		if ($scope.modifiedStatus) {
			app_confirm('数据已经被修改，请确认是否保存！','提示',null,function(buttonIndex){
	                if(buttonIndex == 2){
						$scope.save();
			        }else{
						$state.go("eggSellsList");
					}
	          });
		}else{
			$state.go("eggSellsList");
		}
    }






    $scope.calculateMoney = function(){
    	$scope.farmData.sellData.good_sale_money = Number(Number($scope.farmData.sellData.good_price_value*2).toFixed(2) * Number($scope.farmData.sellData.good_sale_weight/2).toFixed(2)).toFixed(0);
    }

})