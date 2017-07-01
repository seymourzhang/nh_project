 angular.module('myApp.alarmStatistics', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 // 报警统计
.controller("alarmStatisticsCtrl",function($scope, $state, $http, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	
	$scope.setData = function(){
		//数据源
		$scope.transferHouseId  =  ""  ;
		$scope.buildingDayAge   =  ""  ;
		$scope.alarmStatisticsData = {
			"houseData":[{
	                "houseName"         :  ""                                             ,  //栋舍名称
	                "dayAge"            :  ""                                             ,  //日龄
	                "temp_avg_alarm"    :  {'H':"",'L':""}  ,  //平均
	                "temp_in1_alarm"    :  {'H':"",'L':""}  ,  //前区
	                "temp_in2_alarm"    :  {'H':"",'L':""}  ,  //中区
	                "temp_in3_alarm"    :  {'H':"",'L':""}  ,  //后区
	                "point_temp_alarm"  :  ""                                             ,  //点温差
	                "power_status"      :  ""                                             ,  //断电报警（0）
	                "lux_alarm"         :  ""                                             ,  //光照
	                "co2_alarm"         :  ""                                                //二氧化碳
	          }]
		}
		setTimeout(function() {
	    	$scope.statisticsQuery();
	    }, persistentData.horizontalTime);
	}
	
	$scope.statisticsQuery = function(){
		//监控报警
		var params = {
			"FarmId"  :  $scope.sparraw_user_temp.farminfo.id
		};
		Sparraw.ajaxPost('alarmMobile/alarmInfo', params, function(data){
			console.log(data);
			if (data.ResponseDetail.Result == "Success") {
				
				$scope.alarmStatisticsData.houseData = data.ResponseDetail.AlarmData;

			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		},null,200000);
	}
	
	$scope.goAlarmLog = function(item){		
		$scope.buildingDayAge = item.dayAge;
		$scope.transferHouseId = item.houseID;
	}

	setLandscape(true,true);//横屏
	setTimeout(function() {
		$scope.setData();
	}, 500);



})