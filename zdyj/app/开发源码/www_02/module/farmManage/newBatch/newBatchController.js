angular.module('myApp.newBatch', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 新建批次
.controller("newBatchCtrl",function($scope, $state, $http, AppData,uiGridConstants) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));



	$scope.backBatchManage = function(){
		$state.go("batchManage");
	}
	$scope.batchData = {
		"FarmId"          :  $scope.sparraw_user_temp.farminfo.id                ,//int型，农场id
		"BreedBatchId"    :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId  ,
		"farmBreedStatus" :  $scope.sparraw_user_temp.farminfo.farmBreedStatus   ,  //农场批次状态
		"BatchCode"       :  ""  ,//varchar型，批次编号
		"place_date"      :  ""  ,//varchar型，入舍时日期
		"place_day_age"   :  ""  ,//int型，入舍日龄
		"place_week_age"  :  ""  ,//int型，入雏周龄
		"eggType"         :  "01"  ,
		"chickenType"     :  "02"  ,
		"place_detail"    :  []  ,//各栋舍入舍数量
		"Place_Sum"       :  "" //计算合计的入雏总量
	}


	
	$scope.drawTable = function(){
		$scope.gridOptions = {};
		$scope.gridOptions.enableCellEditOnFocus = true;
		$scope.gridOptions.enableSorting = false;
		$scope.gridOptions.enableCellEdit = false;
		$scope.gridOptions.enableHorizontalScrollbar = uiGridConstants.scrollbars.NEVER;
    	$scope.gridOptions.enableVerticalScrollbar = uiGridConstants.scrollbars.NEVER;
    	$scope.gridOptions.columnDefs = [];

    	var HouseNameHeader = '<div style="width:178px;height:30px;"><p style="text-align:center; position:relative; left:0px; top:5px;">栋舍号</div>';

    	$scope.gridOptions.columnDefs.push({name: 'HouseName',  width: '50%', enableColumnMenu: false, headerCellTemplate: HouseNameHeader,
			cellClass: function(newBatchDiv, row, col, rowRenderIndex, colRenderIndex) {
          		if (newBatchDiv.getCellValue(row,col) === '合计') {
		        	return 'TableTotalStyle';
		        }else{
		        	return 'TableHouseStyle';
		        }
	        }
		});
		var PlaceNumHeader = '<div style="width:178px;height:30px;"><p style="text-align:center; position:relative; left:0px; top:5px;">进鸡数量</div>';
		$scope.gridOptions.columnDefs.push({name: 'place_num', width: '50%', type: 'number', enableCellEdit:true,enableColumnMenu: false, headerCellTemplate: PlaceNumHeader,
			cellClass: function(newBatchDiv, row, col, rowRenderIndex, colRenderIndex) {
          		if (newBatchDiv.getCellValue(row,col) == $scope.batchData.Place_Sum) {
		        	return 'TableTotalPlaceNumStyle';
		        }else{
		        	return 'TablePlaceNumStyle';
		        }
	        },
	        cellEditableCondition: function($scope){
     			if ($scope.row.entity.HouseName == "合计") {
     				return 0;
     			}else{
     				return 1;
     			};
     		}
		});



		

		$scope.gridOptions.data = $scope.batchData.place_detail;
		console.log($scope.gridOptions.data);
		for (var i = 0; i < $scope.gridOptions.data.length; i++) {
			// console.log($scope.gridOptions.data[i].HouseId);
			if ($scope.gridOptions.data[i].HouseId == 0) {
				$scope.gridOptions.data.pop();
			}else{

			};
		};



		$scope.gridOptions.data.push({
			'HouseName':'合计',
			'place_num':$scope.batchData.Place_Sum
		});
		document.getElementById('newBatchId').style.height = (30 * $scope.gridOptions.data.length + 33) + "px";

    }

	$scope.inquire = function(){
		//显示每栋栋舍信息
		for (var i = 0; i < $scope.sparraw_user_temp.userinfo.houses.length; i++) {
			$scope.batchData.place_detail.push({
				"HouseId"    :  $scope.sparraw_user_temp.userinfo.houses[i].HouseId    ,
				"HouseName"  :  $scope.sparraw_user_temp.userinfo.houses[i].HouseName  ,
				"place_num"  :  ""
			});
		};

		var params = {
		      "BreedBatchId"  :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId
			};

			Sparraw.ajaxPost('layer_breedBatch/queryBatch.action', params, function(data){
				console.log(data);
		   		if (data.ResponseDetail.Result == "Success") {
					$scope.batchData.BatchCode = data.ResponseDetail.BatchCode;
					//$scope.batchData.place_date = data.ResponseDetail.place_date;
					$scope.batchData.place_day_age = data.ResponseDetail.place_day_age;
					$scope.batchData.place_week_age = data.ResponseDetail.place_week_age;
					$scope.batchData.place_detail = data.ResponseDetail.place_detail;
					$scope.batchData.Place_Sum = data.ResponseDetail.place_num;
					$scope.batchData.chickenType = data.ResponseDetail.place_breed;
					$scope.batchData.eggType = data.ResponseDetail.place_type;
					//日期转换
					var TempDate = new Date(data.ResponseDetail.place_date);
					$scope.batchData.place_date = TempDate;
					$scope.drawTable();
				}else if (data.ResponseDetail.Result == "Fail"){
					Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				}else{
					Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				};
			});
			$scope.drawTable();
	}
	
	$scope.inquire();


	


	if ($scope.batchData.farmBreedStatus != "01") {
		Sparraw.myNotice("目前批次信息为空，您可以添加新的批次信息");
		$scope.saveAddBtn = false;
	}else{
		var para = document.getElementById("place_week_ageId").disabled=true;
		var para = document.getElementById("place_dateId").disabled=true;
		var para = document.getElementById("place_day_ageId").disabled=true;
		var para = document.getElementById("batchCodeId").disabled=true;
		var para = document.getElementById("chickenTypeSelect").disabled=true;
		var para = document.getElementById("eggTypeSelect").disabled=true;
		$scope.saveAddBtn = true;
	};

    
	$scope.judgeDevice = function(){
		var ua = navigator.userAgent.toLowerCase();
    	if (/iphone|ipad|ipod/.test(ua) || /android/.test(ua)) {
			console.log("是手机");
		}else{
			console.log("是电脑");
			document.getElementById("place_dateId").type = "text";
		}
	}

    $scope.GetPlacedate = function(){

    	var ua = navigator.userAgent.toLowerCase();
    	if (/iphone|ipad|ipod/.test(ua) || /android/.test(ua)) {
			console.log("是手机");
		}else{
			console.log("是电脑");
			//console.log(document.getElementById("place_dateId").value);
			
			if (document.getElementById("place_dateId").value.search(/^((?:19|20)\d\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$/) != -1) {
				console.log("正确");
			}else{
				$scope.batchData.BatchCode = "";
				return Sparraw.myNotice("请正确输入入舍时日龄格式为：2015-01-01");
			}
		}
    	$scope.batchData.place_date = document.getElementById("place_dateId").value;
    	//转为字符串并且删除“日”字符串，修改“年”“月”“/”为“-”,并且做补零处理                
        var selectDate = $scope.batchData.place_date + '';
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
        $scope.batchData.place_date = selectDate;
        var TempDate = new Date(selectDate);
        //获取批次号
        $scope.batchData.BatchCode = selectDate;
        $scope.batchData.BatchCode = $scope.batchData.BatchCode.replace(/(-)/g,"");
        $scope.batchData.BatchCode = $scope.batchData.BatchCode.substring(2,$scope.batchData.BatchCode.length);

    }


    $scope.getthedate = function(){
    	if ($scope.batchData.place_date == "" || !$scope.batchData.place_date) {
    		$scope.batchData.place_day_age = "";
    		Sparraw.myNotice("请选择入雏日，否则无法计算生长周龄。");
    	}else{
			//  以下程序是参考自然周，计算生长周龄的公式， by guoxiang  2016-05-05
			var initPlaceDate = Common_addDateByDays($scope.batchData.place_date,1-$scope.batchData.place_day_age);
			console.log('initPlaceDate=' + initPlaceDate);
			var initPlaceWeekDay = new Date(initPlaceDate).getDay();
			console.log('initPlaceWeekDay=' + initPlaceWeekDay);
			var curDayAge = $scope.batchData.place_day_age;
			console.log('curDayAge1=' + curDayAge);
			if(initPlaceWeekDay <= 3){
				curDayAge = curDayAge + initPlaceWeekDay + 6;
			}else{
				curDayAge = curDayAge - (7- initPlaceWeekDay) + 6;
			}
			console.log('curDayAge2=' + curDayAge);
			$scope.batchData.place_week_age = Math.floor(curDayAge / 7); 
			console.log('$scope.batchData.place_week_age=' + $scope.batchData.place_week_age);
    	};
	}



	$scope.Clicksave = function(){

		if ($scope.sparraw_user_temp.Authority.FarmBreed === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};


		console.log($scope.batchData.eggType);
		app_confirm('新建批次后，录入信息无法修改，您确认要保存吗？','提示',null,function(buttonIndex){
           if(buttonIndex == 2){
           		//判断是否为空

           		if ($scope.batchData.place_day_age == "" || !$scope.batchData.place_day_age) {
           			app_alert('请填写完整信息。');
           		}else{
           			for (var i = 0; i < $scope.batchData.place_detail.length; i++) {
						if ($scope.batchData.place_detail[i].place_num == 0 && $scope.batchData.place_detail[i].HouseName != '合计') {
							app_confirm($scope.batchData.place_detail[i].HouseName + '栋舍入雏数量为0,是否保存？','提示',null,function(buttonIndex){
								if(buttonIndex == 2){
									$scope.save();
								}
							})
						}
					};
					$scope.save();
           		};
           }
	    }); 
	}
	

    $scope.JudgeType = function(){
    	console.log($scope.batchData.chickenType);
    }
    $scope.JudgeEggType = function(){
    	console.log($scope.batchData.eggType);
    }


	$scope.save = function(){
		console.log($scope.batchData.eggType);
		for (var i = 0; i < $scope.batchData.place_detail.length; i++) {
			if ($scope.batchData.place_detail[i].HouseName == '合计') {
				$scope.batchData.place_detail.pop();
			};
		}

		$scope.TempPlaceSum = [];
		for (var i = 0; i < $scope.batchData.place_detail.length; i++) {
			if ($scope.batchData.place_detail[i].place_num === "") {
				$scope.batchData.place_detail[i].place_num = 0;
			};
			$scope.TempPlaceSum.push($scope.batchData.place_detail[i].place_num);
		};

		$scope.batchData.Place_Sum = eval($scope.TempPlaceSum.join('+'));

 		var params = {
	     	"FarmId"          :$scope.batchData.FarmId          ,
			"BatchCode"       :$scope.batchData.BatchCode       ,
			"place_type"      :$scope.batchData.eggType         ,
			"place_breed"     :$scope.batchData.chickenType     ,
			"place_date"      :$scope.batchData.place_date      ,
			"place_day_age"   :$scope.batchData.place_day_age   ,
			"place_week_age"  :$scope.batchData.place_week_age  ,
			"place_detail"    :$scope.batchData.place_detail    ,
			"place_num"       :$scope.batchData.Place_Sum
		};
		Sparraw.ajaxPost('layer_breedBatch/createBatch.action', params, function(data){
	   		if (data.ResponseDetail.Result == "Success") {
	   			sparraw_user.farminfo.farmBreedBatchId = data.ResponseDetail.BreedBatchId;
				//重新获取服务器最新数据
				biz_common_getLatestData($state,"batchManage");
				$scope.inquire();
				Sparraw.myNotice("保存成功。");
			}else if (data.ResponseDetail.Result == "Fail"){
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		});
	}
})
