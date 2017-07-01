angular.module('myApp.historyDataImport', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//历史数据导入
.controller("historyDataImportCtrl",function($scope, $state, $http, AppData,uiGridConstants){
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	$scope.gridOptions = {};
	$scope.historySelectData = {
		"farmBreedId"  :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
		"selectHouse"  :  JSON.stringify($scope.sparraw_user_temp.userinfo.houses[0]),
		"farmBreedBatchCode" : $scope.sparraw_user_temp.farminfo.farmBreedBatchCode,
		"houseId"	   :  "",
		"houseBreedId" :  "",
		// "houseName"    ： "",
		"modifyData"   :  [],
 		"dataInput"    :  {
			"day_age"  : "",
			"week_age" : "",
			"culling_all" : "",
			"curLayNum" : "",
			"layStandard" : "",
			"curBrokenNum" : "",
			"daily_feed"  : "",
			"daily_weight" : "",
			"daily_water" : ""
 		},
	};


	
	
	
	$scope.historySelectData.houseId = JSON.parse($scope.historySelectData.selectHouse).HouseId;
	$scope.historySelectData.houseBreedId = JSON.parse($scope.historySelectData.selectHouse).HouseBreedBatchId;

	$scope.drowTable = function(){
		var headerCellTem1 = "<div style='color:black;text-align:center;position:relative; left:0px; top:15px;'><p>周龄</p></div>";
		var headerCellTem2 = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>死淘<br>只</p></div>";
		var headerCellTem3 = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>产蛋<br>枚</p></div>";
		var headerCellTem4 = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>筐规格<br>斤/筐</p></div>";
		var headerCellTem5 = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>破损<br>枚</p></div>";
		var headerCellTem6 = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>采食<br>公斤</p></div>";
		var headerCellTem7 = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>饮水<br>立方</p></div>";
		var headerCellTem8 = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>均重<br>公斤</p></div>";
		$scope.gridOptions.columnDefs = [		
			{name:'day_age',displayName:'日龄', width:'0%',visible:false},
			{name:'week_age',displayName:'周龄',width:'12.5%',enableColumnMenu:false,enableCellEdit: false, headerCellTemplate:headerCellTem1},			
			{name:'culling_all',displayName:'死淘<br>只',width:'12%',enableColumnMenu:false,headerCellTemplate:headerCellTem2},
			{name:'curLayNum',displayName:'产蛋<br>枚',width:'15%',enableColumnMenu:false,headerCellTemplate:headerCellTem3},
			{name:'layStandard',displayName:'筐规格<br>斤/筐',width:'13.5%',enableColumnMenu:false,headerCellTemplate:headerCellTem4},
			{name:'curBrokenNum',displayName:'破损<br>枚',width:'13.5%',enableColumnMenu:false,headerCellTemplate:headerCellTem5},			
			{name:'daily_feed',displayName:'采食<br>公斤',width:'13.5%',enableColumnMenu:false,headerCellTemplate:headerCellTem6},			
			{name:'daily_water',displayName:'饮水<br>立方',width:'12.5%',enableColumnMenu:false,headerCellTemplate:headerCellTem7},		
			{name:'daily_weight',displayName:'均重',width:'12.5%',enableColumnMenu:false,headerCellTemplate:headerCellTem8}
		];
		$scope.gridOptions.enableCellEdit = true;
		$scope.gridOptions.enableCellEditOnFocus = true;
		$scope.gridOptions.enableHorizontalScrollbar = uiGridConstants.scrollbars.NEVER;
    	$scope.gridOptions.enableVerticalScrollbar = uiGridConstants.scrollbars.NEVER;
		$scope.gridOptions.data = $scope.historySelectData.dataInput;

		//判断哪些数据进行过修改
	    $scope.gridOptions.onRegisterApi = function(gridApi){
	      gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
           $scope.historySelectData.modifyData.push(rowEntity.day_age);
           $scope.historySelectData.modifyData = Common_deleteRepeat($scope.historySelectData.modifyData);
          });
	    };

		// console.log("gridOptions=" + $scope.gridOptions.data +"000"+ $scope.historyQueryData.TableDatas);
	}
	$scope.drowTable();
	$scope.getTableData = function(){
		var params = {
			"farmBreedId"  :   $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
			"houseId"      :   $scope.historySelectData.houseId
		};
		Sparraw.ajaxPost('layer_breedBatch/historyDataQuery.action', params, function(data){
			if(data.ResponseDetail.Result == 'Success'){
				$scope.historySelectData.dataInput = data.ResponseDetail.TableDatas;
				for (var m = 0; m < $scope.historySelectData.dataInput.length; m++) {
					$scope.historySelectData.dataInput[m].layStandard = $scope.historySelectData.dataInput[m].layStandard*2;
				};
				document.getElementById('inputDivId').style.height = ($scope.historySelectData.dataInput.length * 36) + 'px';
				$scope.drowTable();
			} else if (data.ResponseDetail.Result = "Fail"){
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			} else {
				sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		});
	}
	$scope.getTableData();
	$scope.judgeHouse = function(item){
		console.log(JSON.parse($scope.historySelectData.selectHouse));
		$scope.historySelectData.houseId = JSON.parse($scope.historySelectData.selectHouse).HouseId;
		$scope.getTableData();



	}
	$scope.judgeHouse();	
	$scope.save = function(){

		if ($scope.sparraw_user_temp.Authority.FarmBreed === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		

		for (var i = 0; i < $scope.historySelectData.dataInput.length; i++) {
			$scope.historySelectData.dataInput[i].$$hashKey = undefined;
		};
		//删除未修改的数据
		$scope.uploadDataInput = [];
		for (var i = 0; i < $scope.historySelectData.modifyData.length; i++) {
			for (var j = 0; j < $scope.historySelectData.dataInput.length; j++) {
				if ($scope.historySelectData.dataInput[j].day_age == $scope.historySelectData.modifyData[i]) {
					console.log($scope.historySelectData.dataInput[j]);
					$scope.uploadDataInput.push($scope.historySelectData.dataInput[j]);
				};
			};
		};

		if ($scope.uploadDataInput.length == 0) {
			return app_alert('尚未修改信息。');
		};

		for (var i = 0; i < $scope.uploadDataInput.length; i++) {
			$scope.uploadDataInput[i].day_age = Common_NulltoZero($scope.uploadDataInput[i].day_age);
			$scope.uploadDataInput[i].culling_all = Common_NulltoZero($scope.uploadDataInput[i].culling_all);
			$scope.uploadDataInput[i].curLayNum = Common_NulltoZero($scope.uploadDataInput[i].curLayNum);
			$scope.uploadDataInput[i].egg_box_size = Common_NulltoZero($scope.uploadDataInput[i].layStandard/2);
			$scope.uploadDataInput[i].curBrokenNum = Common_NulltoZero($scope.uploadDataInput[i].curBrokenNum);
			$scope.uploadDataInput[i].daily_feed = Common_NulltoZero($scope.uploadDataInput[i].daily_feed);
			$scope.uploadDataInput[i].daily_weight = Common_NulltoZero($scope.uploadDataInput[i].daily_weight);
			$scope.uploadDataInput[i].daily_water = Common_NulltoZero($scope.uploadDataInput[i].daily_water);
			if($scope.uploadDataInput[i].curLayNum != 0 && $scope.uploadDataInput[i].layStandard == 0) {
				return app_alert('产蛋不为零，须输入规格！');
			}
		};

		var params = {
		      	"HouseBreedId" :  $scope.historySelectData.houseBreedId  ,
				"HouseId"      :  $scope.historySelectData.houseId       ,
				"dataInput"    :  $scope.uploadDataInput
			};
			Sparraw.ajaxPost('layer_dataInput/saveDR_v2.action', params, function(data){
		   		if (data.ResponseDetail.LoginResult == "Success") {
					$scope.getTableData();
					$scope.drowTable();
					Sparraw.myNotice("保存成功");
				}else if (data.ResponseDetail.Result == "Fail"){
					Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				}else{
					Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				};
			});
	}
})

