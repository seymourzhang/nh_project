angular.module('myApp.survival', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//成活率
.controller('survivalCtrl', function($scope, $state) {
	
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    setLandscape(true,true);   

    document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
	if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
	}else{
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
	}

    $scope.initData = function(){
		
		$scope.chartData = {
			"compareType"    :  "" ,
			"selectedBatch"  :  [] ,
			"batchList"      :  [] ,
			"selectedHouse"  :  "" ,
			"houseList"      :  [] 
		};

		setTimeout(function() {

			//  初始化曲线的展示方式
            $scope.chartData.compareType = '01';   // 01-按批次；02-按栋舍
            $scope.batchDiv = true;
            $scope.housesDiv = false;
			//  获取 栋舍列表
			for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
                $scope.sparraw_user_temp.houseinfos[i].houseName += "栋";
                $scope.chartData.houseList.push($scope.sparraw_user_temp.houseinfos[i]);
            }

			//  获取 饲养批次列表
			var params = {
		        "FarmId"     : $scope.sparraw_user_temp.farminfo.id
		    };
		    Sparraw.ajaxPost('sys/code/getFarmBreedData.action', params, function(data){
		        if (data.ResponseDetail.Result == "Success") {
		            $scope.chartData.batchList = data.ResponseDetail.FarmBreedIdArray;
		            
		            for(var key in data.ResponseDetail.FarmBreedIdArray){
		                $scope.chartData.selectedBatch = key;
		            }

		            $scope.getChart();
		        }else{
		            return Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
		        };
		    });
		}, 1500);

	};

	//  
	$scope.changeType = function(){//筛选条件
		if ($scope.chartData.compareType == "01") {
	        $scope.batchDiv = true;
	        $scope.housesDiv = false;
	    }else{
	        $scope.batchDiv = false;
	        $scope.housesDiv = true;
	    }
		$scope.getChart();
	};

    //  获取曲线数据 
	$scope.getChart = function(){
		
		//  准备数据查询参数
		var params = {};
		params.CompareType = $scope.chartData.compareType;
		params.FarmId = $scope.sparraw_user_temp.farminfo.id;

		if (params.CompareType == '01') {
			params.FarmBreedId = $scope.chartData.selectedBatch;
		}else{
			if ($scope.chartData.selectedHouse == "" || !$scope.chartData.selectedHouse) {
				$scope.chartData.selectedHouse = $scope.sparraw_user_temp.houseinfos[0];
				params.HouseId = $scope.chartData.selectedHouse.id;
			}else{
				if (Object.prototype.toString.call($scope.chartData.selectedHouse) === "[object String]") {
					params.HouseId = JSON.parse($scope.chartData.selectedHouse).id;
				}else{
					params.HouseId = $scope.chartData.selectedHouse.id;
				}
			}
		}

		Sparraw.ajaxPost('rep/DCRate/accDCRateReq.action', params, function(data){

			for (var i = 0; i < data.ResponseDetail.DCDatas.length; i++) {
				if (data.ResponseDetail.DCDatas[i].HouseDatas) {
					for (var j = 0; j < data.ResponseDetail.DCDatas[i].HouseDatas.length; j++) {
						data.ResponseDetail.DCDatas[i].HouseDatas[j] = (100 - data.ResponseDetail.DCDatas[i].HouseDatas[j]).toFixed(1);
					}
				}
			}
			var tempLineConfig = setChartParams($scope.chartData.compareType, data.ResponseDetail.DCDatas);

			var ttXData = null;

			if(data.ResponseDetail.xData && data.ResponseDetail.xData.length > 0){
				ttXData = data.ResponseDetail.xData;
			}else{
				ttXData = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,41,42];
			}

			Echart_initLine02(
				ttXData,
				tempLineConfig,
				"单位：%",
				[90,100],
				false,
				"",
				null,
				null,
				"日龄"
			);
		});
	}

	
	setTimeout(function() {
		$scope.initData();
	}, persistentData.horizontalTime);
})