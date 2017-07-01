angular.module('myApp.alarmSetting', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 // 报警设置
.controller("alarmSettingCtrl",function($scope, $state,$ionicLoading, $http, $ionicScrollDelegate, $ionicModal, AppData) {
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

    var MAXHEIGHT = 0;
    var DIVHEIGHT = 0;
    var basicDIV = document.getElementById('basic_DIV');
    var tempDIV = document.getElementById('temp_DIV');
    var co2DIV = document.getElementById('co2_DIV');
    var beamDIV = document.getElementById('beam_DIV');
    $scope.basicShow = false;
	$scope.tempShow = false;
	$scope.co2Show = false;
	$scope.beamShow = false;

	if (Common_isAndroid() || Common_isIOS()) {
		MAXHEIGHT = document.body.clientHeight;
	}else{
		MAXHEIGHT = document.documentElement.clientHeight;
	}
	DIVHEIGHT = MAXHEIGHT - 90;
	basicDIV.style.height = DIVHEIGHT + 'px';
	tempDIV.style.height = DIVHEIGHT + 'px';
	co2DIV.style.height = DIVHEIGHT + 'px';
	beamDIV.style.height = DIVHEIGHT + 'px';

    $scope.setData = function(){
    	$scope.alarmSet = {
    		"farmId": "",
     		"houseId": "",
     		//基础信息
    		"alarm_type" : 0,//报警设置类型，指标类型：0-基础， 1-温度，2-光照，3-二氧化碳
    		"alarm_delay":"",//报警延迟，0-0分钟，1-1分钟，2-2分钟，3-3分钟，4-4分钟，5-5分钟，
			"temp_cpsation":"",//温度补偿，1-是；0-否
			"temp_cordon":"",//补偿数值
			"alarm_method":"",//报警形式，02-独立温区报警，03-平均温度报警
			"temp_probe":[],
			//表格信息
    		"tableData" : [],
    		"high_alarm_co2":"",
    		"high_caution_co2":"",
    		"whetherNewco2" : false,
    		//拷贝信息
    		"copyFarm":$scope.sparraw_user_temp.farminfo,
    		"copyNativeHouse":"",
    		"copyType":"",
    		"copySelectHouse":"",
    		"copyHouseArr":[],
    		"otherHouseIdArray":[],
    		"houseType":""
    	};
    	if (selectBackPage.alarmTempData.alarmType == undefined) {
    		$scope.alarmSet.houseId = JSON.stringify($scope.sparraw_user_temp.houseinfos[0].id);
    	}else{
    		$scope.alarmSet.houseId = selectBackPage.alarmTempData.houseId;
    		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
	    		if ($scope.sparraw_user_temp.houseinfos[i].id == $scope.alarmSet.houseId) {
	    			$scope.alarmSet.copyNativeHouse = $scope.sparraw_user_temp.houseinfos[i];
	    		}
	    	}
    	}
    	for (var i = 0; i < $scope.sparraw_user_temp.farmList.length; i++) {
    		if ($scope.sparraw_user_temp.farmList[i].farmId == $scope.sparraw_user_temp.farminfo.id) {
    			$scope.alarmSet.farmId = $scope.sparraw_user_temp.farmList[i].farmId;
    		}
    	}
    	if (selectBackPage.alarmTempData.alarmType == undefined || !selectBackPage.alarmTempData.alarmType) {
    		$scope.selectType(0);
    	}else{
    		$scope.selectType(selectBackPage.alarmTempData.alarmType);
    	}

    	


    	/*
    	1、$scope.selectType()//绘制界面
    	2、$scope.selectedHouse()//选择栋舍获取基本信息
    	3、$scope.getTableData()//获取分支表信息*/
    };

    $scope.goEnvMonitoring = function(){
    	selectBackPage.alarmTempData.alarmType = undefined;
    	$state.go("envMonitoring");
    }

    $scope.selectType = function(type){
    	var basicTD = document.getElementById('basicTD');
    	var tempTD = document.getElementById('tempTD');
    	var co2TD = document.getElementById('co2TD');
    	var beamTD = document.getElementById('beamTD');

    	$scope.alarmSet.alarm_type = type;
    	switch(type){
    		case 1:
				basicTD.style.background = "#FFF";
				tempTD.style.background = "#33cd5f";
				co2TD.style.background = "#FFF";
				beamTD.style.background = "#FFF";

				basicTD.style.color = "#33cd5f";
				tempTD.style.color = "#FFF";
				co2TD.style.color = "#33cd5f";
				beamTD.style.color = "#33cd5f";

				$scope.basicShow = false;
				$scope.tempShow = true;
				$scope.co2Show = false;
				$scope.beamShow = false;
				$scope.alarmSet.copyType = "温度";
				$scope.getTableData();
			  	return;
			  break;
		  	case 2:
		  		basicTD.style.background = "#FFF";
				tempTD.style.background = "#FFF";
				co2TD.style.background = "#FFF";
				beamTD.style.background = "#33cd5f";
	
				basicTD.style.color = "#33cd5f";
				tempTD.style.color = "#33cd5f";
				co2TD.style.color = "#33cd5f";
				beamTD.style.color = "#FFF";

				$scope.basicShow = false;
				$scope.tempShow = false;
				$scope.co2Show = false;
				$scope.beamShow = true;
				$scope.alarmSet.copyType = "光照";
				$scope.getTableData();
		  		return;
		  	break;
			case 3:
				basicTD.style.background = "#FFF";
				tempTD.style.background = "#FFF";
				co2TD.style.background = "#33cd5f";
				beamTD.style.background = "#FFF";
	
				basicTD.style.color = "#33cd5f";
				tempTD.style.color = "#33cd5f";
				co2TD.style.color = "#FFF";
				beamTD.style.color = "#33cd5f";

				
				$scope.basicShow = false;
				$scope.tempShow = false;
				$scope.co2Show = true;
				$scope.beamShow = false;
				$scope.alarmSet.copyType = "二氧化碳";
				$scope.getTableData();
			  	return;
			  break;
			default:
				basicTD.style.background = "#33cd5f";
				tempTD.style.background = "#FFF";
				co2TD.style.background = "#FFF";
				beamTD.style.background = "#FFF";

				basicTD.style.color = "#FFF";
				tempTD.style.color = "#33cd5f";
				co2TD.style.color = "#33cd5f";
				beamTD.style.color = "#33cd5f";

				$scope.basicShow = true;
				$scope.tempShow = false;
				$scope.co2Show = false;
				$scope.beamShow = false;
				$scope.alarmSet.copyType = "基础";
				$scope.selectedHouse();
				return;
		};

		
    };

    $ionicModal.fromTemplateUrl('useHelp.html', function(modal) {  
	    $scope.modalDIV = modal;
	}, {  
	    scope: $scope,  
	    animation: 'slide-in-up'
	});

    $scope.openFun = function(){
	  	$scope.modalDIV.show();
    };

    $scope.closeFun = function(){
    	$scope.modalDIV.hide();
    };

    $scope.selectedHouse = function(){
    	selectBackPage.alarmTempData.houseId = $scope.alarmSet.houseId;

    	for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
    		if ($scope.sparraw_user_temp.houseinfos[i].id == selectBackPage.alarmTempData.houseId) {
    			$scope.alarmSet.copyNativeHouse = $scope.sparraw_user_temp.houseinfos[i];
    		}
    	}
    	//清空复制的栋舍与选中的栋舍
    	$scope.alarmSet.copyHouseArr = [];
    	$scope.alarmSet.copySelectHouse = "";

    	if ($scope.alarmSet.alarm_type == 0) {
    		var params = {
				"farmId"   : $scope.alarmSet.farmId                ,
				"houseId"  : $scope.alarmSet.houseId
			};
			
			Sparraw.ajaxPost('alarmSettingMobile/basicQuery', params, function(data){
				if (data.ResponseDetail.Result == "Success") {
					$scope.alarmSet.alarm_delay   = String(data.ResponseDetail.alarm_delay);
					$scope.alarmSet.temp_cpsation = data.ResponseDetail.temp_cpsation;
					$scope.alarmSet.temp_cordon   = data.ResponseDetail.temp_cordon  ;
					$scope.alarmSet.alarm_method  = data.ResponseDetail.alarm_method ;
					$scope.alarmSet.temp_probe    = data.ResponseDetail.temp_probe   ;
					$scope.alarmSet.point_alarm   = data.ResponseDetail.point_alarm  ;

					if (!$scope.alarmSet.temp_probe || $scope.alarmSet.temp_probe == undefined) {
						$scope.showProbe = false;
					}else{
						$scope.showProbe = true;
					}
					$scope.temperatureOffsetChange();//判断温度补偿是否打开
				}else if (data.ResponseDetail.Result == "Fail") {
					Sparraw.myNotice(data.ResponseDetail.Error)  ;
			    	$scope.alarmSet.alarm_delay    = '0'         ;
				    $scope.alarmSet.temp_cpsation  = false       ;
				    $scope.alarmSet.temp_cordon    = '0'         ;
				    $scope.alarmSet.alarm_method   = '0'         ;
				    $scope.alarmSet.temp_probe = [{
			            "is_alarm": false,
			            "probe_name_cn": "前区1",
			            "probe_name": "inside_temp1"
			        },
			        {
			            "is_alarm": false,
			            "probe_name_cn": "前区2",
			            "probe_name": "inside_temp2"
			        },
			        {
			            "is_alarm": false,
			            "probe_name_cn": "中区1",
			            "probe_name": "inside_temp10"
			        },
			        {
			            "is_alarm": false,
			            "probe_name_cn": "后区1",
			            "probe_name": "inside_temp19"
			        },
			        {
			            "is_alarm": false,
			            "probe_name_cn": "后区2",
			            "probe_name": "inside_temp20"
			        }];
				}else{
					Sparraw.myNotice(data.ResponseDetail.Error);
				};
			});
    	}else{
    		$scope.getTableData();
    	}
    	
    };



    $scope.getTableData = function(){
    	$scope.alarmSet.tableData = [];
    	selectBackPage.alarmTempData.alarmType = $scope.alarmSet.alarm_type;
    	for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
	    	if ($scope.sparraw_user_temp.houseinfos[i].id == $scope.alarmSet.houseId) {
				$scope.alarmSet.houseType = $scope.sparraw_user_temp.houseinfos[i].houseType;
			}
		}

		var params = {
			"farmId"         :  $scope.alarmSet.farmId                ,
			"houseId"        :  $scope.alarmSet.houseId               ,
			"alarm_type"     :  $scope.alarmSet.alarm_type            ,
			"houseType"      :  $scope.alarmSet.houseType
		};
		Sparraw.ajaxPost('alarmSettingMobile/itemQuery', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				$scope.alarmSet.tableData = data.ResponseDetail.DataArray;
				if (params.alarm_type == 3) {
					$scope.alarmSet.whetherNewco2 = false;
					$scope.alarmSet.high_alarm_co2 = data.ResponseDetail.DataArray[0].high_alarm_co2;
					$scope.alarmSet.high_caution_co2 = data.ResponseDetail.DataArray[0].high_caution_co2;
				}
			}else{
				if (params.alarm_type == 3) {
					$scope.alarmSet.whetherNewco2 = true;
					$scope.alarmSet.high_alarm_co2 = "";
					$scope.alarmSet.high_caution_co2 = "";
				}
				Sparraw.myNotice(data.ResponseDetail.Error);
			};
		},null,60000);
    };

    $scope.temperatureOffsetChange = function() {
		if ($scope.alarmSet.temp_cpsation) {
			$scope.showTempCpsationVal = true;
		}else {
			$scope.showTempCpsationVal = false;
		}
	};

	$scope.saveAlarmSet = function(){
		//添加权限
		if ($scope.sparraw_user_temp.Authority.AlarmSet == "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};

		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
	    	if ($scope.sparraw_user_temp.houseinfos[i].id == $scope.alarmSet.houseId) {
				$scope.alarmSet.houseType = $scope.sparraw_user_temp.houseinfos[i].houseType;
			}
		}

		var params = {
			"farmId"         :  $scope.alarmSet.farmId                ,
			"houseId"        :  $scope.alarmSet.houseId               ,
			"alarm_delay"    :  $scope.alarmSet.alarm_delay           ,
         	"temp_cpsation"  :  $scope.alarmSet.temp_cpsation         ,
         	"temp_cordon"    :  $scope.alarmSet.temp_cordon           ,
         	"alarm_method"   :  $scope.alarmSet.alarm_method          ,
         	"point_alarm"    :  $scope.alarmSet.point_alarm           ,
         	"temp_probe"     :  $scope.alarmSet.temp_probe            ,
         	"houseType"      :  $scope.alarmSet.houseType
		};
		Sparraw.ajaxPost('alarmSettingMobile/basicSave', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				Sparraw.myNotice("保存成功。");
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			};
		});
	};


	$scope.deleteTableCell = function(item){
		//添加权限
		if ($scope.sparraw_user_temp.Authority.AlarmSet == "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
	    	if ($scope.sparraw_user_temp.houseinfos[i].id == $scope.alarmSet.houseId) {
				$scope.alarmSet.houseType = $scope.sparraw_user_temp.houseinfos[i].houseType;
			}
		}

		var params = {
			"farmId"         :  $scope.alarmSet.farmId                ,
			"houseId"        :  $scope.alarmSet.houseId               ,
			"alarm_type"     :  $scope.alarmSet.alarm_type            ,
			"day_age"        :  item.day_age                          ,
         	"deleteRow"      :  item.uid_num                          ,
         	"houseType"      :  $scope.alarmSet.houseType
		};
		Sparraw.ajaxPost('alarmSettingMobile/itemDelete', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				Sparraw.myNotice("删除成功。");
				$scope.getTableData();
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			};
		},null,60000);
	}


	$scope.getHighCautionCo2 = function(co2Value){
		if (!isNaN(co2Value) || co2Value != 0) {
            $scope.alarmSet.high_caution_co2 = co2Value * 0.9;
        }else{
            $scope.alarmSet.high_caution_co2 = 0;
        }
	}

	$scope.saveCo2 = function(){
		/*日龄
		育成175
		产蛋455*/
		//添加权限
		if ($scope.sparraw_user_temp.Authority.AlarmSet == "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};

		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
	    	if ($scope.sparraw_user_temp.houseinfos[i].id == $scope.alarmSet.houseId) {
				$scope.alarmSet.houseType = $scope.sparraw_user_temp.houseinfos[i].houseType;
			}
		}

		if ($scope.alarmSet.high_alarm_co2 < 5001) {
            
        }else{
            app_alert("CO2报警值不能大于5000,请重新输入。");
            return false;
        }

        if ($scope.alarmSet.high_caution_co2 < $scope.alarmSet.high_alarm_co2) {
            
        }else{
            app_alert("CO2警示值必须小于CO2报警值,请重新输入。");
            return false;
        }

		var params = {
         	"farmId"           :  $scope.alarmSet.farmId                ,
			"houseId"          :  $scope.alarmSet.houseId               ,
            "alarm_type"       :  "3"                                   ,
            "day_age"          :  ""                                    ,
            "high_alarm_co2"   :  $scope.alarmSet.high_alarm_co2        ,
            "high_caution_co2" :  $scope.alarmSet.high_caution_co2      ,
            "set_co2"          :  0                                     ,
            "houseType"        :  $scope.alarmSet.houseType
		};

		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			if ($scope.sparraw_user_temp.houseinfos[i].id == $scope.alarmSet.houseId) {
				if ($scope.sparraw_user_temp.houseinfos[i].houseTypeName == "育成") {
					params.day_age = 175;
				}else{
					params.day_age = 455;
				}
			}
		}

		if ($scope.alarmSet.whetherNewco2) {
			Sparraw.ajaxPost('alarmSettingMobile/itemAdd', params, function(data){
				if (data.ResponseDetail.Result == "Success") {
					Sparraw.myNotice("添加成功");
				}else{
					Sparraw.myNotice(data.ResponseDetail.Error);
				};
			});
		}else{
			params.uid_num = $scope.alarmSet.tableData[0].uid_num;
			Sparraw.ajaxPost('alarmSettingMobile/itemUpdate', params, function(data){
				if (data.ResponseDetail.Result == "Success") {
					Sparraw.myNotice("修改成功");
				}else{
					Sparraw.myNotice(data.ResponseDetail.Error);
				};
			});
		}
	};

	$ionicModal.fromTemplateUrl('copyPage.html', function(modal) {  
	    $scope.copyDIV = modal;
	}, {  
	    scope: $scope,  
	    animation: 'slide-in-up'
	});

	$scope.copyPageOpenFun = function(){
		$scope.copyDIV.show();
		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			if ($scope.sparraw_user_temp.houseinfos[i].id != $scope.alarmSet.copyNativeHouse.id) {
				$scope.alarmSet.copyHouseArr.push($scope.sparraw_user_temp.houseinfos[i]);
			}
		}
	};

	$scope.copyPageCloseFun = function(){
		$scope.copyDIV.hide();
	};

	$scope.saveCopy = function(){
		$scope.alarmSet.otherHouseIdArray[0] = $scope.alarmSet.copySelectHouse.id;

		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
	    	if ($scope.sparraw_user_temp.houseinfos[i].id == $scope.alarmSet.copyNativeHouse.id) {
				$scope.alarmSet.houseType = $scope.sparraw_user_temp.houseinfos[i].houseType;
			}
		}
		
		var params = {
			"farmId"            :  $scope.alarmSet.copyFarm.id           ,
			"houseId"           :  $scope.alarmSet.copyNativeHouse.id    ,
			"otherHouseIdArray" :  $scope.alarmSet.otherHouseIdArray     ,
			"alarm_type"        :  $scope.alarmSet.alarm_type            ,
			"houseType"         :  $scope.alarmSet.houseType                          
		};

		Sparraw.ajaxPost('alarmSettingMobile/itemApplyOther', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				$scope.copyDIV.hide();
				Sparraw.myNotice("保存成功。");
			}else{
				Sparraw.myNotice(data.ResponseDetail.Error);
			};
		},null,60000);
	}

    $scope.setData();
})
















