angular.module('myApp.updateTemp', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
//添加温度报警数据
.controller("updateTempCtrl",function($scope, $state, $http, $stateParams, AppData) {
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    
    $scope.setData = function(){
    	$scope.updateTempData = {
            "farmId"           :  ""  ,
            "tableData"        :  {}  ,  
            "houseType"        :  ""
    	};
    	$scope.updateTempData.tableData = JSON.parse($stateParams.item);
        for (var i = 0; i < $scope.sparraw_user_temp.farmList.length; i++) {
            if ($scope.sparraw_user_temp.farmList[i].farmId == $scope.sparraw_user_temp.farminfo.id) {
                $scope.updateTempData.farmId = $scope.sparraw_user_temp.farmList[i].farmId;
            }
        }
    };

    $scope.addTempSave = function(){
        //添加权限
        if ($scope.sparraw_user_temp.Authority.AlarmSet == "all") {

        }else{
            return app_alert("该用户无此操作权限。");
        };

        for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
            if ($scope.sparraw_user_temp.houseinfos[i].id == $stateParams.houseId) {
                $scope.updateTempData.houseType = $scope.sparraw_user_temp.houseinfos[i].houseType;
            }
        }

    	var params = {
         	"farmId"           :  $scope.updateTempData.farmId                       ,
            "houseId"          :  $stateParams.houseId                               ,
            "day_age"          :  $scope.updateTempData.tableData.day_age            ,
            "alarm_type"       :  "1"                                                ,
            "high_alarm_temp"  :  $scope.updateTempData.tableData.high_alarm_temp    ,
            "low_alarm_temp"   :  $scope.updateTempData.tableData.low_alarm_temp     ,
            "set_temp"         :  $scope.updateTempData.tableData.set_temp           ,
            "uid_num"          :  $scope.updateTempData.tableData.uid_num            ,
            "high_caution_temp":  $scope.updateTempData.tableData.high_caution_temp  ,
            "low_caution_temp" :  $scope.updateTempData.tableData.low_caution_temp   ,
            "houseType"        :  $scope.updateTempData.houseType                    
		};
		console.log("保存的params:");
        console.log(params);
        if (!$scope.inspectionParams(params)) {return;} //检验传输的数据
		Sparraw.ajaxPost('alarmSettingMobile/itemUpdate', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				Sparraw.myNotice("保存成功");
                $state.go("alarmSetting");
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			};
		},null,60000);
    };


    $scope.getHighCautionTemp = function(highValue){
        if (!isNaN(highValue) || highValue != 0) {
            $scope.updateTempData.tableData.high_caution_temp = highValue * 0.90;
        }else{
            $scope.updateTempData.tableData.high_caution_temp = 0;
        }
    };

    $scope.getlowCautionTemp = function(lowValue){
        if (!isNaN(lowValue) || lowValue != 0) {
            $scope.updateTempData.tableData.low_caution_temp = lowValue * 1.1;
        }else{
            $scope.updateTempData.tableData.low_caution_temp = 0;
        }
    };


    $scope.inspectionParams = function(params){
        if (!params.farmId ||!params.houseId ||!params.alarm_type ||!params.uid_num) {
            app_alert("数据有误,请重新登陆。");
            return false;
        }

        if (isNaN(params.day_age)) {
            app_alert("请输入正确的日龄");
            return false;
        }else if (isNaN(params.high_alarm_temp)) {
            app_alert("请输入正确的目标温度");
            return false;
        }else if (isNaN(params.low_alarm_temp)) {
            app_alert("请输入正确的高报温度");
            return false;
        }else if (isNaN(params.set_temp)) {
            app_alert("请输入正确的低报温度");
            return false;
        }

        if (!params.day_age) {
            app_alert("缺少日龄,请重新输入。");
            return false;
        }else if (!params.high_alarm_temp) {
            app_alert("缺少目标温度,请重新输入。");
            return false;
        }else if (!params.low_alarm_temp) {
            app_alert("缺少高报温度,请重新输入。");
            return false;
        }else if (!params.set_temp) {
            app_alert("缺少低报温度,请重新输入。");
            return false;
        }


        if (params.set_temp > 14 && params.set_temp < 36) {
            
        }else{
            app_alert("目标温度必须在15℃至35℃之间,请重新输入。");
            return false;
        }

        if (params.high_alarm_temp < 41 && params.high_alarm_temp > params.set_temp) {
            
        }else{
            app_alert("高报温度必须低于41℃并且高于目标温度,请重新输入。");
            return false;
        }

        if (params.low_alarm_temp > 11 && params.low_alarm_temp < params.set_temp) {
            
        }else{
            app_alert("低报温度必须大于11℃并且低于目标温度,请重新输入。");
            return false;
        }

        if (params.high_caution_temp < params.high_alarm_temp && params.high_alarm_temp > params.set_temp) {
            
        }else{
            app_alert("高报警示必须小于高报温度,并且高于目标温度,请重新输入。");
            return false;
        }

        if (params.low_caution_temp > params.low_alarm_temp && params.low_caution_temp < params.set_temp) {
            
        }else{
            app_alert("低报警示必须大于低报温度,并且低于目标温度,请重新输入。");
            return false;
        }
        

        return true;
    };

    $scope.setData();
})