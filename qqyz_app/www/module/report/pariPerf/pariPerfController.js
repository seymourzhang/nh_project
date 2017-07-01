angular.module('myApp.pariPerf', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//性能指标查询(全胎次性能,生产效率达成周报)
.controller("pariPerfCtrl",function($scope, $stateParams,$state, $http, $ionicPopup,$ionicLoading,$cordovaFileTransfer,$cordovaFileOpener2,$timeout, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	$scope.toDayGridOptions = {};
	$scope.DayGridOption = true;
	$scope.dataNullText = false;
	$scope.setData = function(){
		$scope.embrAbilData = {
			"tableTitle"        :  $stateParams.tableTitle  ,
			"ReportType"        :  ""                       ,
			"viewType"          :  "01"                     ,
			"weekDateList"      :  []                       ,
			"selectedWeekDate"  :  ""                       ,
			"tableData"         :  ""
		};
		if ($scope.embrAbilData.tableTitle == "全胎次性能") {
			$scope.embrAbilData.ReportType = "01";
		}else{
			$scope.embrAbilData.ReportType = "02";
		}
		$scope.getWeekRepList();
	};

	$scope.judgeViewType = function(){
		$scope.getTableData();
	};

	$scope.judgeWeekDate = function(){
		$scope.getTableData();
	};

	$scope.getWeekRepList = function(){
		var params = {
			"FarmId"  :   $scope.sparraw_user_temp.farminfo.id
		};
		Sparraw.ajaxPost('repMobile/weekRepList', params, function(data){
			if(data.ResponseDetail.Result == 'Success'){
				for(var i = 0;i < data.ResponseDetail.weekList.length;i++){
			        var item = data.ResponseDetail.weekList[i];
			        var key = Object.keys(item);
			        $scope.embrAbilData.weekDateList.push(item[key]);
			    }
				$scope.embrAbilData.selectedWeekDate = $scope.embrAbilData.weekDateList[0];
				$scope.getTableData();
			} else if (data.ResponseDetail.Result = "Fail"){
				Sparraw.myNotice(data.ResponseDetail.Error);
			} else {
				sparraw.myNotice(data.ResponseDetail.Error);
			};
		});
	};

	$scope.getTableData = function(){
		var params = {
			"ReportType"  :  $scope.embrAbilData.ReportType        ,
	       	"ViewType"    :  $scope.embrAbilData.viewType          ,
	       	"FarmId"      :  $scope.sparraw_user_temp.farminfo.id  ,
	       	"WeekDate"    :  $scope.embrAbilData.selectedWeekDate
		};

		Sparraw.ajaxPost('repMobile/queryParityPerformance', params, function(data){
			if(data.ResponseDetail.Result == 'Success'){
				$scope.embrAbilData.tableData = [];
				$scope.DayGridOption = true;
				$scope.dataNullText = false;
				document.getElementById('dataNullTextId').innerHTML = "";
				console.log("------------");
				console.log(data.ResponseDetail.PerformanceArray.length);
				console.log(data.ResponseDetail.PerformanceArray);
				console.log("------------");

				if ($scope.embrAbilData.viewType == "05") {
					var primaryTitle = ["配种","分娩","断奶","存栏"];
					for (var i = 0; i < data.ResponseDetail.PerformanceArray.length; i++) {
						$scope.embrAbilData.tableData.push({
							"itemName"      :  primaryTitle[i],
							"itemValue"     :  "",
							"totalValue"    :  "",
							"averageValue"  :  "",
							"targetValue"   :  ""
						})
						for (var j = 0; j < data.ResponseDetail.PerformanceArray[i].length; j++) {
							$scope.embrAbilData.tableData.push(data.ResponseDetail.PerformanceArray[i][j]);
						}
					}
				}else{
					$scope.embrAbilData.tableData = data.ResponseDetail.PerformanceArray[0];
				}

				
				$scope.drawTable();
			} else if (data.ResponseDetail.Result = "Fail"){
				$scope.DayGridOption = false;
				$scope.dataNullText = true;
				document.getElementById('dataNullTextId').innerHTML = "暂无数据";
				Sparraw.myNotice(data.ResponseDetail.Error);
			} else {
				sparraw.myNotice(data.ResponseDetail.Error);
			};
		});
	};

	$scope.drawTable = function(){
		//表头部分
		var toDayTableKey = [];
		var toDayheader = [];
		var toDayTitleName = [];
		var firstColumn = 0;
		var eachColumn = 0;

		if ($scope.embrAbilData.tableTitle == "全胎次性能") {
			firstColumn = 160;
			eachColumn = 50;
			toDayTitleName = ["指标名称","当周值","累计值","平均值","目标值"];
			toDayTableKey = ["itemName",
							"itemValue",
							"totalValue",
							"averageValue",
							"targetValue"];
		}else{
			firstColumn = 165;
			eachColumn = 65;
			toDayTitleName = ["指标名称","当周值","目标值","达成率%"];
			toDayTableKey = ["itemName",
							"itemValue",
							"targetValue",
							"reachRate"];
		}

		



		for (var i = 0; i < toDayTableKey.length; i++) {
			if (i == 0) {
				toDayheader.push({
						'name'                :  toDayTableKey[i],
						'width'               :  firstColumn,
						'headerCellTemplate'  :  '<div style="width:' + firstColumn + 'px;height:30px; background:#FFF;border-left:solid 0.5px #ECECEC;border-right:solid 0.5px #AEAEAE; "><p class="middle" style="position: relative; top:5px;">' + toDayTitleName[i] + '</p></div>',
						'cellTemplate'        :  '',
						'headerCellClass'     :  '',
						'enableCellEdit'      :  false,
					})
			}else{
				toDayheader.push({
						'name'                :  toDayTableKey[i],
						'width'               :  eachColumn,
						'headerCellTemplate'  :  '<div style="width:' + eachColumn + 'px;height:30px; background:#FFF;border-left:solid 0.5px #ECECEC;border-right:solid 0.5px #AEAEAE; "><p class="middle" style="position: relative; top:5px;">' + toDayTitleName[i] + '</p></div>',
						'cellTemplate'        :  '',
						'headerCellClass'     :  '',
						'enableCellEdit'      :  false,
					})
			}
				
		}


		var toDayTableData = {
		    'header' : toDayheader,
		    'firstFixed': true, //首列是否固定ture-固定，false-不固定
		    'rowHeight' : 25,//内容高度
		    'TableData' : $scope.embrAbilData.tableData
		}
		
		var showTableData = toDayTableData;

		$scope.toDayGridOptions = {
			rowHeight: showTableData.rowHeight,
		};
		$scope.toDayGridOptions.columnDefs = [];





		//表主体部分
		  for (var i = 0; i < showTableData.header.length; i++) {
				if (i == 0 && showTableData.firstFixed == true) {
					$scope.toDayGridOptions.columnDefs.push({ 
		                        name                :  showTableData.header[i].name                ,  
		                        displayName         :  showTableData.header[i].displayName         , 
		                        width               :  showTableData.header[i].width               ,
		                        headerCellClass     :  showTableData.header[i].headerCellClass     ,
		                        headerCellTemplate  :  showTableData.header[i].headerCellTemplate  ,
		                        cellTemplate        :  showTableData.header[i].cellTemplate        ,
		                        enableCellEdit      :  showTableData.header[i].enableCellEdit      ,//cell是否可以编辑
		                        pinnedLeft          :  true                                        ,
		                        enableColumnMenu    :  false                                       ,
		                    	cellClass           :  function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                    								if (row.entity.itemName == "配种" ||
		                    									row.entity.itemName == "分娩" ||
		                    									row.entity.itemName == "断奶" ||
		                    									row.entity.itemName == "存栏" ) {
		                    									return 'primaryTitleStyle';
		                    								}else{
		                    									return 'TitleUnderlineStyle';
		                    								}
		                    								
												        }
													});
				}else{
					$scope.toDayGridOptions.columnDefs.push({ 
			                        name                :  showTableData.header[i].name                ,  
			                        displayName         :  showTableData.header[i].displayName         , 
			                        width               :  showTableData.header[i].width               ,
			                        headerCellClass     :  showTableData.header[i].headerCellClass     ,
			                        headerCellTemplate  :  showTableData.header[i].headerCellTemplate  ,
			                        cellTemplate        :  showTableData.header[i].cellTemplate        ,
			                        enableCellEdit      :  showTableData.header[i].enableCellEdit      ,//cell是否可以编辑
			                        enableColumnMenu    :  false                                       ,
			                    	cellClass           :  function(grid, row, col, rowRenderIndex, colRenderIndex) {
													            if (row.entity.itemName == "配种" ||
			                    									row.entity.itemName == "分娩" ||
			                    									row.entity.itemName == "断奶" ||
			                    									row.entity.itemName == "存栏" ) {
			                    									return 'primaryTitleStyle';
			                    								}else{
			                    									return 'UnderlineStyle';
			                    								}
													        }
														});
				}
		  }
		  $scope.toDayGridOptions.data = showTableData.TableData;
	};


	$scope.setData();
});