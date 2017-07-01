angular.module('myApp.addCO2', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
//添加报警数据
.controller("addCO2Ctrl",function($scope, $state, $http, $stateParams, AppData) {
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    
    $scope.setData = function(){
    	$scope.addCO2Data = {
            "farmId"           :  ""  ,
    		"day_age"          :  ""  ,
    		"high_alarm_co2"   :  ""  ,
    		"set_co2"          :  ""
    	};
        for (var i = 0; i < $scope.sparraw_user_temp.farmList.length; i++) {
            if ($scope.sparraw_user_temp.farmList[i].farmId == $scope.sparraw_user_temp.farminfo.id) {
                $scope.addCO2Data.farmId = $scope.sparraw_user_temp.farmList[i].farmId;
            }
        }
    };

    $scope.addCO2Save = function(){
        //添加权限
        if ($scope.sparraw_user_temp.Authority.AlarmSet == "all") {

        }else{
            return app_alert("该用户无此操作权限。");
        };
    	var params = {
         	"farmId"           :  $scope.addCO2Data.farmId              ,
            "houseId"          :  $stateParams.houseId                  ,
            "alarm_type"       :  "3"                                   ,
            "day_age"          :  $scope.addCO2Data.day_age             ,
            "high_alarm_co2"   :  $scope.addCO2Data.high_alarm_co2      ,
            "set_co2"          :  $scope.addCO2Data.set_co2
            
		};
        console.log("保存的params:");
        console.log(params);
        if (!$scope.inspectionParams(params)) {return;} //检验传输的数据
		Sparraw.ajaxPost('alarmSettingMobile/itemAdd', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				Sparraw.myNotice("保存成功");
                $state.go("alarmSetting");
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			};
		});
    };

    $scope.inspectionParams = function(params){
        if (!params.farmId ||!params.houseId ||!params.alarm_type) {
            app_alert("数据有误,请重新登陆。");
            return false;
        }

        if (isNaN(params.day_age)) {
            app_alert("请输入正确的日龄");
            return false;
        }else if (isNaN(params.high_alarm_co2)) {
            app_alert("请输入正确的CO2报警值");
            return false;
        }else if (isNaN(params.set_co2)) {
            app_alert("请输入正确的CO2参考值");
            return false;
        }

        if (!params.day_age) {
            app_alert("缺少日龄,请重新输入。");
            return false;
        }else if (!params.high_alarm_co2) {
            app_alert("缺少CO2报警值,请重新输入。");
            return false;
        }else if (!params.set_co2) {
            app_alert("缺少CO2参考值,请重新输入。");
            return false;
        }

        return true;
    };

    $scope.setData();
})