 angular.module('myApp.alarmSettings', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])

 // 报警设置
.controller("alarmSettingsCtrl",function($scope, $state, $http, $ionicPopup, AppData, $ionicModal) {

	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    
    $scope.temperatureOffsetChange = function() {
		if ($scope.tempVar.tempCpsation) {
			$scope.showTempCpsationVal = true;
			$scope.tempVar.AlarmSetting.tempCpsation = 1;
		}else {
			$scope.showTempCpsationVal = false;
			$scope.tempVar.AlarmSetting.tempCpsation = 0;
		};
	}
	//判断温度补偿是否打开
	if ($scope.tempVar.AlarmSetting.tempCpsation == 1) {
		$scope.showTempCpsationVal = true;
	}else{
		$scope.showTempCpsationVal = false;
	};

	$scope.testMethods = function(){

		houseId = $scope.tempVar.AlarmSetting.HouseId;
		// 查询栋舍报警设置
		//$scope.queryAlarmSetting();

		$scope.tempVar.AlarmSetting.effAlarmProbe.tempLeft1 = true ;
    	$scope.tempVar.AlarmSetting.effAlarmProbe.tempLeft2 = true ;
    	$scope.tempVar.AlarmSetting.effAlarmProbe.tempMiddle1 = true ;
    	$scope.tempVar.AlarmSetting.effAlarmProbe.tempMiddle2 = false ;
    	$scope.tempVar.AlarmSetting.effAlarmProbe.tempRight1 = true ;
    	$scope.tempVar.AlarmSetting.effAlarmProbe.tempRight2 = true ;

        $scope.tempVar.AlarmSetting.Delay            =  0  ;  //报 警 延 迟
        $scope.tempVar.AlarmSetting.tempCpsation     =  '0'  ;  //温度补偿
        $scope.tempVar.AlarmSetting.tempCpsationVal  =  '0'  ;  //补偿数值
        $scope.tempVar.AlarmSetting.alarmProbe       =  ''  ;  //报警探头
        $scope.tempVar.AlarmSetting.pointAlarm       =  5  ;  //点温差报警
        $scope.tempVar.AlarmSetting.tarTemp			 =  '';
        $scope.tempVar.AlarmSetting.minTemp		     =  '';
        $scope.tempVar.AlarmSetting.maxTemp		     =  '';
 

		//查询是否有数据记录
		var params = {
	    		"FarmId"        :  $scope.sparraw_user_temp.farminfo.id                     ,
	    		"HouseId"       :  $scope.tempVar.AlarmSetting.HouseId
			};

		Sparraw.ajaxPost('sys/alarm/querySettingData.action', params, function(data){
			//判断传送是否成功
			if (data.ResponseDetail.Result == "Success") {
				//判断第一次还是已有信息
				if (data.ResponseDetail.ResultFlag == "Y") {
					$scope.tempVar.AlarmSetting.Delay            =  JSON.stringify(data.ResponseDetail.AlarmSetting.Delay)  ;
					$scope.tempVar.AlarmSetting.tempCpsation     =  data.ResponseDetail.AlarmSetting.tempCpsation           ;
					$scope.tempVar.AlarmSetting.tempCpsationVal  =  data.ResponseDetail.AlarmSetting.tempCpsationVal        ;
					$scope.tempVar.AlarmSetting.alarmProbe       =  data.ResponseDetail.AlarmSetting.alarmProbe             ;
					$scope.tempVar.AlarmSetting.pointAlarm       =  data.ResponseDetail.AlarmSetting.pointAlarm             ;
					$scope.tempVar.AlarmSetting.tarTemp       =  data.ResponseDetail.AlarmSetting.tarTemp		  ;
					$scope.tempVar.AlarmSetting.minTemp       =  data.ResponseDetail.AlarmSetting.minTemp		  ;
					$scope.tempVar.AlarmSetting.maxTemp       =  data.ResponseDetail.AlarmSetting.maxTemp		  ;
					
					$scope.tempVar.AlarmSetting.effAlarmProbe.tempLeft1 = 
								(data.ResponseDetail.AlarmSetting.effAlarmProbe &&data.ResponseDetail.AlarmSetting.effAlarmProbe.tempLeft1=="true")?true:false;
					
					$scope.tempVar.AlarmSetting.effAlarmProbe.tempLeft2 = 
								(data.ResponseDetail.AlarmSetting.effAlarmProbe && data.ResponseDetail.AlarmSetting.effAlarmProbe.tempLeft2=="true")?true:false;

					$scope.tempVar.AlarmSetting.effAlarmProbe.tempMiddle1 = 
								(data.ResponseDetail.AlarmSetting.effAlarmProbe && data.ResponseDetail.AlarmSetting.effAlarmProbe.tempMiddle1=="true")?true:false;
									
					$scope.tempVar.AlarmSetting.effAlarmProbe.tempMiddle2 = 
								(data.ResponseDetail.AlarmSetting.effAlarmProbe && data.ResponseDetail.AlarmSetting.effAlarmProbe.tempMiddle2=="true")?true:false;
									
					$scope.tempVar.AlarmSetting.effAlarmProbe.tempRight1 = 
								(data.ResponseDetail.AlarmSetting.effAlarmProbe && data.ResponseDetail.AlarmSetting.effAlarmProbe.tempRight1=="true")?true:false;
									
					$scope.tempVar.AlarmSetting.effAlarmProbe.tempRight2 = 
								(data.ResponseDetail.AlarmSetting.effAlarmProbe && data.ResponseDetail.AlarmSetting.effAlarmProbe.tempRight2=="true")?true:false;																										

				
					//判断温度补偿是否打开
					console.log($scope.tempVar.AlarmSetting.tempCpsation);
					if ($scope.tempVar.AlarmSetting.tempCpsation == 1) {
						$scope.showTempCpsationVal = true;
						$scope.tempVar.tempCpsation = true;
					}else{
						$scope.showTempCpsationVal = false;
						$scope.tempVar.tempCpsation = false;
					};	
				}else{
						//判断权限
						if ($scope.sparraw_user_temp.Authority.basicInfo === "All") {

						}else{

							return app_alert("暂无数据,请联系场长进行设置。");
						};

						$scope.tempVar.AlarmSetting.Delay = 3;//报警延迟默认参数
						$scope.tempVar.AlarmSetting.alarmProbe = 2;//报警方式默认参数

						//判断用户是否使用默认数据
						app_confirm('该栋舍尚未设置报警信息,是否使用默认值?','提示',null,function(buttonIndex){
		                   if(buttonIndex == 2){
		                        $scope.tempVar.AlarmSetting.effAlarmProbe.tempLeft1 = true ;
				            	$scope.tempVar.AlarmSetting.effAlarmProbe.tempLeft2 = true ;
				            	$scope.tempVar.AlarmSetting.effAlarmProbe.tempMiddle1 = true ;
				            	$scope.tempVar.AlarmSetting.effAlarmProbe.tempMiddle2 = false ;
				            	$scope.tempVar.AlarmSetting.effAlarmProbe.tempRight1 = true ;
				            	$scope.tempVar.AlarmSetting.effAlarmProbe.tempRight2 = true ;
				            	$scope.tempVar.AlarmSetting.Delay            =  data.ResponseDetail.AlarmSetting.Delay            ;
								$scope.tempVar.AlarmSetting.tempCpsation     =  data.ResponseDetail.AlarmSetting.tempCpsation     ;
								$scope.tempVar.AlarmSetting.tempCpsationVal  =  data.ResponseDetail.AlarmSetting.tempCpsationVal  ;
								$scope.tempVar.AlarmSetting.alarmProbe       =  data.ResponseDetail.AlarmSetting.alarmProbe       ;
								$scope.tempVar.AlarmSetting.pointAlarm       =  data.ResponseDetail.AlarmSetting.pointAlarm       ;
								$scope.tempVar.AlarmSetting.tarTemp       =  data.ResponseDetail.AlarmSetting.tarTemp		  ;
								$scope.tempVar.AlarmSetting.minTemp       =  data.ResponseDetail.AlarmSetting.minTemp		  ;
								$scope.tempVar.AlarmSetting.maxTemp       =  data.ResponseDetail.AlarmSetting.maxTemp		  ;

								$scope.save();
		                   }
			            }); 
			            

				};
			}else{
				Sparraw.myNotice("查询失败");
			};
		});
	}


	//默认调用查询
	$scope.tempVar.AlarmSetting.HouseId = JSON.stringify($scope.sparraw_user_temp.userinfo.houses[0].HouseId);
	$scope.testMethods();
	//点击保存时调用
	$scope.Clicksave = function(){
		if ($scope.sparraw_user_temp.Authority.basicInfo === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		var judgeClick = "1";
		obj = judgeClick;
		$scope.save(obj);
	}


    $scope.save = function(obj){


    	var params = {
    		"FarmId"        :  $scope.sparraw_user_temp.farminfo.id             ,
    		"HouseId"       :  $scope.tempVar.AlarmSetting.HouseId                  ,
    		"AlarmSetting"  : {
					            "Delay"           :  $scope.tempVar.AlarmSetting.Delay            ,
					            "tempCpsation"    :  $scope.tempVar.AlarmSetting.tempCpsation     ,
					            "tempCpsationVal" :  $scope.tempVar.AlarmSetting.tempCpsationVal  ,
					            "alarmProbe"      :  $scope.tempVar.AlarmSetting.alarmProbe       ,
					            "pointAlarm"      :  $scope.tempVar.AlarmSetting.pointAlarm       ,
					            "tarTemp"     	  :  $scope.tempVar.AlarmSetting.tarTemp       ,
					            "minTemp"  		  :  $scope.tempVar.AlarmSetting.minTemp          ,
					            "maxTemp"  		  :  $scope.tempVar.AlarmSetting.maxTemp          ,
					            "effAlarmProbe":  {	
										        "tempLeft1"   :$scope.tempVar.AlarmSetting.effAlarmProbe.tempLeft1+"",
										        "tempLeft2"   :$scope.tempVar.AlarmSetting.effAlarmProbe.tempLeft2+"",
										        "tempMiddle1" :$scope.tempVar.AlarmSetting.effAlarmProbe.tempMiddle1+"",
										        "tempMiddle2" :"false",
										        "tempRight1"  :$scope.tempVar.AlarmSetting.effAlarmProbe.tempRight1+"",
										        "tempRight2"  :$scope.tempVar.AlarmSetting.effAlarmProbe.tempRight2+""
										       }
					          }

		};


    	Sparraw.ajaxPost('sys/alarm/saveSettingData.action', params, function(data){
			if (data.ResponseDetail.ErrorMsg == null) {
	   			houseId = $scope.tempVar.AlarmSetting.HouseId;
	   			Sparraw.myNotice("保存成功！");    
		    }else {
		   		Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
		    };
	    });
    }

    $scope.copyFun = function(){
    	if ($scope.sparraw_user_temp.Authority.basicInfo === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};

    	app_confirm("所有栋舍将更新至当前设置，复制后您可以选择任何一栋单独修改，请确认。",null,null,function(buttonIndex){
			if(buttonIndex == 1){
				
			}else if(buttonIndex == 2){


			    var params = {
		    		"FarmId"        :  $scope.sparraw_user_temp.farminfo.id             ,
		    		"HouseId"       :  $scope.tempVar.AlarmSetting.HouseId                  ,
		    		"AlarmSetting"  : {
							            "Delay"           :  $scope.tempVar.AlarmSetting.Delay            ,
							            "tempCpsation"    :  $scope.tempVar.AlarmSetting.tempCpsation     ,
							            "tempCpsationVal" :  $scope.tempVar.AlarmSetting.tempCpsationVal  ,
							            "alarmProbe"      :  $scope.tempVar.AlarmSetting.alarmProbe       ,
							            "pointAlarm"      :  $scope.tempVar.AlarmSetting.pointAlarm       ,
							            "tarTemp"     	  :  $scope.tempVar.AlarmSetting.tarTemp       ,
							            "minTemp"  		  :  $scope.tempVar.AlarmSetting.minTemp          ,
							            "maxTemp"  		  :  $scope.tempVar.AlarmSetting.maxTemp          ,
							            "effAlarmProbe":  {	
												        "tempLeft1"   :$scope.tempVar.AlarmSetting.effAlarmProbe.tempLeft1+"",
												        "tempLeft2"   :$scope.tempVar.AlarmSetting.effAlarmProbe.tempLeft2+"",
												        "tempMiddle1" :$scope.tempVar.AlarmSetting.effAlarmProbe.tempMiddle1+"",
												        "tempMiddle2" :"false",
												        "tempRight1"  :$scope.tempVar.AlarmSetting.effAlarmProbe.tempRight1+"",
												        "tempRight2"  :$scope.tempVar.AlarmSetting.effAlarmProbe.tempRight2+""
												       }
							          }

				};


		    	Sparraw.ajaxPost('sys/alarm/saveSettingBatch.action', params, function(data){
					if (data.ResponseDetail.ErrorMsg == null) {
			   			houseId = $scope.tempVar.AlarmSetting.HouseId; 
			   			Sparraw.myNotice("保存成功！");   
				    }else {
				   		 Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				    };
			    },null,200000);

			}
		});	
    }


    $ionicModal.fromTemplateUrl('useHelp.html', function(modal) {  
	    $scope.modalDIV = modal;
	}, {  
	    scope: $scope,  
	    animation: 'slide-in-up'
	});

    $scope.openFun = function(){
	  	$scope.modalDIV.show();
    }

    $scope.closeFun = function(){
    	$scope.modalDIV.hide();
    }
})