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

	// 合格鸡蛋总重量
	var good_sale_weight = 0;
	// 合格鸡蛋总箱数
	var good_salebox_num = 0;
	// 合格鸡蛋单价
	var good_price_value = 0;
	// 合格鸡蛋每箱规格
	var good_box_size = 0;
	// 合格鸡蛋总金额
	var good_sale_money = 0;
	
	
	// 破损鸡蛋总重量
	var broken_sale_weight = 0;
	// 破损鸡蛋总箱数
	var broken_salebox_num = 0;
	// 破损鸡蛋单价
	var broken_price_value = 0;
	// 破损鸡蛋每箱规格
	var broken_box_size = 0;
	// 破损鸡蛋总金额
	var broken_sale_money = 0;

	// 鸡粪收入
	var chicken_manure = 0;
	
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
			},
		"temp":1,
		"today":today,


		"TempGood_salebox_num":"",
		"TempGood_box_size":"",
		"TempGood_price_value":"",
		"TempGood_sale_money":"",
		"TempChecked":"",
		"TempBroken_salebox_num":"",
		"TempBroken_price_value":"",
		"TempBroken_sale_money":"",
		"TempChicken_manure":""
	}

	$scope.selectGuige = function(){
		console.log($scope.farmData.sellData.good_box_size);
		defaultValue = $scope.farmData.sellData.good_box_size;
		$scope.farmData.sellData.broken_box_size = $scope.farmData.sellData.good_box_size;
		Common_saveObjectToLocalStorage(key,$scope.farmData.sellData.good_box_size);
		
		good_sale_weight = $scope.farmData.sellData.good_salebox_num * $scope.farmData.sellData.good_box_size;
		broken_sale_weight = $scope.farmData.sellData.broken_salebox_num * $scope.farmData.sellData.good_box_size;
		
		//console.log("good_sale_weight b:" + good_sale_weight);
		//console.log("broken_sale_weight b:" + broken_sale_weight);
		
		// 重量转为公斤
		$scope.farmData.sellData.good_sale_weight = (good_sale_weight/2).toFixed(2);
		$scope.farmData.sellData.broken_sale_weight = (broken_sale_weight/2).toFixed(2);
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
	
	
 	//销售金额结算
	$scope.forageTotal = function(inputType){

		good_sale_weight = 0;
		broken_sale_weight = 0;
		
		// 合格鸡蛋总金额
		good_sale_money = 0;
		// 破损蛋总金额
		broken_sale_money = 0;

		
		if (!Common_isNum($scope.farmData.sellData.good_salebox_num)) {
			$scope.farmData.sellData.good_salebox_num = 0;
		}
		if (!Common_isNum($scope.farmData.sellData.good_price_value)) {
			$scope.farmData.sellData.good_price_value = 0;
		}
		
		
		
		if (!Common_isNum($scope.farmData.sellData.broken_salebox_num)) {
			$scope.farmData.sellData.broken_salebox_num = 0;
		}
		if (!Common_isNum($scope.farmData.sellData.broken_price_value)) {
			$scope.farmData.sellData.broken_price_value = 0;
		}
		
		
		if (!Common_isNum($scope.farmData.sellData.chicken_manure)) {
			$scope.farmData.sellData.chicken_manure = 0;
		}
		
		
		good_sale_money = Common_multiply($scope.farmData.sellData.good_salebox_num , $scope.farmData.sellData.good_price_value);
		
		
		
		broken_sale_money = Common_multiply($scope.farmData.sellData.broken_salebox_num , $scope.farmData.sellData.broken_price_value);
		
		
		$scope.farmData.sellData.good_sale_money = good_sale_money;
		$scope.farmData.sellData.broken_sale_money = broken_sale_money;
		
		good_sale_weight = $scope.farmData.sellData.good_salebox_num * $scope.farmData.sellData.good_box_size;
		broken_sale_weight = $scope.farmData.sellData.broken_salebox_num * $scope.farmData.sellData.good_box_size;
		
		//console.log("good_sale_weight a:" + good_sale_weight);
		//console.log("broken_sale_weight a:" + broken_sale_weight);
		
		// 重量转为公斤
		$scope.farmData.sellData.good_sale_weight = (good_sale_weight/2).toFixed(2);
		$scope.farmData.sellData.broken_sale_weight = (broken_sale_weight/2).toFixed(2);

	};



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
				console.log(data);
		   		if (data.ResponseDetail.Result == "Success") {
					//$scope.farmData.dataInput = data.ResponseDetail.saleDetails;
					console.log("data size :" + data.ResponseDetail.saleDetails.length);
					if(data.ResponseDetail.saleDetails.length > 0) {
						var sellData = data.ResponseDetail.saleDetails[0];
						console.log("sellData:" + (JSON.stringify(sellData)));
						
						$scope.farmData.sellData = sellData;
						
						
						var good_box_size = sellData.good_box_size;
						if(good_box_size == 0){
							good_box_size = defaultValue/2;
						}
						var good_price_value = sellData.good_price_value;
						var broken_box_size = sellData.broken_box_size;
						if(broken_box_size == 0){
							broken_box_size = defaultValue/2;
						}
						var broken_price_value = sellData.broken_price_value;
						
						
						$scope.farmData.sellData.good_box_size = good_box_size*2;
						$scope.farmData.sellData.good_price_value = (good_price_value);
						$scope.farmData.sellData.broken_box_size = broken_box_size*2;
						$scope.farmData.sellData.broken_price_value = (broken_price_value);
						



						$scope.farmData.TempGood_salebox_num = $scope.farmData.sellData.good_salebox_num;
						$scope.farmData.TempGood_box_size = $scope.farmData.sellData.good_box_size;
						$scope.farmData.TempGood_price_value = $scope.farmData.sellData.good_price_value;
						$scope.farmData.TempGood_sale_money = $scope.farmData.sellData.good_sale_money;
						$scope.farmData.TempChecked = $scope.farmData.checked;
						$scope.farmData.TempBroken_salebox_num = $scope.farmData.sellData.broken_salebox_num;
						$scope.farmData.TempBroken_price_value = $scope.farmData.sellData.broken_price_value;
						$scope.farmData.TempBroken_sale_money = $scope.farmData.sellData.broken_sale_money;
						$scope.farmData.TempChicken_manure = $scope.farmData.sellData.chicken_manure;

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
					
					
					$scope.forageTotal();
					
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
		
		/**good_sale_weight = sellData.good_sale_weight
		broken_sale_weight = sellData.broken_sale_weight
		
		console.log("good_sale_weight:" + good_sale_weight);
		console.log("broken_sale_weight:" + broken_sale_weight);
		**/
		console.log("modifyData:" + JSON.stringify(sellData));
		
	
			var params = {
				"FarmId"  :  farmId  ,
				"FarmBreedId"  :  farmFeedId  ,
				"saleDetails"    :  [sellData]
			};
			console.log($scope.sparraw_user_temp.Authority);
			if ($scope.sparraw_user_temp.Authority.EggSale === "All") {

			}else{
				return app_alert("该用户无此操作权限。");
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
		//console.log("sdfdfds")
		$state.go("salesAnalyze");
	}
	


	//日期选择器
	$scope.dateModel = new Date("08-14-2015");
    $scope.datePopup = new Date("08-14-2015");
    var disabledDates = [
        new Date(1437719836326),
        new Date(2015, 7, 10), //months are 0-based, this is August, 10th!
        new Date('Wednesday, August 12, 2015'), //Works with any valid Date formats like long format
        new Date("08-14-2015"), //Short format
        new Date(1439676000000) //UNIX format
    ];
    var monthList = ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"];
    var weekDaysList = ["日", "一", "二", "三", "四", "五", "六"];
    $scope.datepickerObject = {};
    $scope.datepickerObject.inputDate = new Date();

    $scope.datepickerObjectPopup = {
            titleLabel: '选择日期', //Optional
            todayLabel: '今天', //Optional
            closeLabel: '关闭', //Optional
            setLabel: '选择', //Optional
            errorMsgLabel : '请选择时间.', //Optional
            setButtonType : 'button-assertive', //Optional
            modalHeaderColor:'bar-positive', //Optional
            modalFooterColor:'bar-positive', //Optional
            templateType:'popup', //Optional
            inputDate: $scope.datepickerObject.inputDate, //Optional
            //mondayFirst: true, //Optional
            sundayFirst: true, //Optional
            disabledDates:disabledDates, //Optional
            monthList:monthList, //Optional
            weekDaysList: weekDaysList,
            from: new Date(2012, 12, 31), //Optional
            to: new Date(2020, 12, 31), //Optional
            callback: function (val) { //Optional
                datePickerCallbackPopup(val);
            }
        };

    var datePickerCallbackPopup = function (val) {
    		//重新选择日期清空入雏日期、批次编号、预计饲养天数、预计出栏日期

			//console.log(val);

			//$scope.farmData.place_date = "";

            if (typeof(val) === 'undefined') {
                console.log('未选择日期');
            } else {
                $scope.datepickerObjectPopup.inputDate = val;
                $scope.datePopup = val;
                //转为字符串并且删除“日”字符串，修改“年”“月”“/”为“-”,并且做补零处理                
                var selectDate = val.toLocaleDateString();
                
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
				
                console.log(selectDate);

				$scope.changeMonth(selectDate);
				
                //将得到的日期放入入雏日期
                $scope.farmData.selectDate = selectDate;
               
            }
    };

    $scope.backFun = function(){

    	if ($scope.sparraw_user_temp.Authority.EggSale === "All") {

		}else{
			return $state.go("eggSellsReportTable");
		};

    	$scope.modifiedStatus = false;
		if ($scope.farmData.TempGood_box_size != $scope.farmData.sellData.good_box_size ||
			$scope.farmData.TempGood_salebox_num != $scope.farmData.sellData.good_salebox_num ||
			$scope.farmData.TempGood_price_value != $scope.farmData.sellData.good_price_value ||
			$scope.farmData.TempChecked != $scope.farmData.checked ||
			$scope.farmData.TempBroken_salebox_num != $scope.farmData.sellData.broken_salebox_num ||
			$scope.farmData.TempBroken_price_value != $scope.farmData.sellData.broken_price_value ||
			$scope.farmData.TempChicken_manure != $scope.farmData.sellData.chicken_manure) {
			$scope.modifiedStatus = true;
		}
		if ($scope.modifiedStatus) {
			app_confirm('数据已经被修改，请确认是否保存！','提示',null,function(buttonIndex){
	                if(buttonIndex == 2){
						$scope.save();
			        }else{
						$state.go("eggSellsReportTable");
					}
	          });
		}else{
			$state.go("eggSellsReportTable");
		}
    }

    $scope.goEggSellsTable = function(){

    	if ($scope.sparraw_user_temp.Authority.EggSale === "All") {

		}else{
			return $state.go("eggSellsList");
		};

    	$scope.modifiedStatus = false;

		if ($scope.farmData.TempGood_box_size != $scope.farmData.sellData.good_box_size ||
			$scope.farmData.TempGood_salebox_num != $scope.farmData.sellData.good_salebox_num ||
			$scope.farmData.TempGood_price_value != $scope.farmData.sellData.good_price_value ||
			$scope.farmData.TempChecked != $scope.farmData.checked ||
			$scope.farmData.TempBroken_salebox_num != $scope.farmData.sellData.broken_salebox_num ||
			$scope.farmData.TempBroken_price_value != $scope.farmData.sellData.broken_price_value ||
			$scope.farmData.TempChicken_manure != $scope.farmData.sellData.chicken_manure) {
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

})