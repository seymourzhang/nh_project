 angular.module('myApp.alarmLogDelay', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
//  报警延迟处理
.controller("alarmLogDelayCtrl",function($scope, $state, $http,  crisisServiceFactory,  $ionicPopup, $stateParams, $ionicActionSheet, AppData) {
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    
	//报警查询
	$scope.alarmQuery = function(){
		var params = {
			"FarmId":$scope.sparraw_user_temp.farminfo.id
		};
		Sparraw.ajaxPost('alarmMobile/alarmDealQuery', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				console.log(data.ResponseDetail.alarmMessage);
				$scope.logDelayData.alarmMessage = data.ResponseDetail.alarmMessage;

				for (var i = 0; i < $scope.logDelayData.alarmMessage.length; i++) {
					for (var j = 0; j < $scope.logDelayData.alarmMessage[i].CurAlarmData.length; j++) {
						if ($scope.logDelayData.alarmMessage[i].CurAlarmData[j].process_status == "01") {
							$scope.logDelayData.showStatus = "未处理";
							$scope.logDelayData.alarmMessage[i].CurAlarmData[j].showStatus = $scope.logDelayData.showStatus;
						}else{
							$scope.logDelayData.showStatus = "已处理";
							console.log($scope.logDelayData.alarmMessage[i].CurAlarmData[j]);
							$scope.logDelayData.alarmMessage[i].CurAlarmData[j].showStatus = $scope.logDelayData.showStatus;
						}

						if ($scope.logDelayData.alarmMessage[i].CurAlarmData[j].process_status == "01") {
							return $scope.DealFunc();
						}else{

						}
					}
				}
			}else{
				Sparraw.myNotice("暂无报警信息");
			};

			var logDelayNumber = 0;
			for (var i = 0; i < $scope.logDelayData.alarmMessage.length; i++) {
				if ($scope.logDelayData.alarmMessage[i].CurAlarmData.length == 0) {
					logDelayNumber ++;
				}
			}
			if ($scope.logDelayData.alarmMessage.length == logDelayNumber) {
                $scope.dataNullText = true;
                $scope.logDelayTable = false;
				document.getElementById('dataNullTextId').innerHTML = "暂无报警信息";
			}else{
				$scope.dataNullText = false;
                $scope.logDelayTable = true;
				document.getElementById('dataNullTextId').innerHTML = "";
			}
		});
	}
	
	//延迟函数
	$scope.DelayFunc = function(deferTime){
		
		//添加权限
		if ($scope.sparraw_user_temp.Authority.AlarmDeal === "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};


		var CurAlarmData = [];
		for (var i = 0; i < $scope.logDelayData.alarmMessage.length; i++) {
			for (var j = 0; j < $scope.logDelayData.alarmMessage[i].CurAlarmData.length; j++) {
				if ($scope.logDelayData.alarmMessage[i].CurAlarmData[j].process_status == '01') {
					CurAlarmData.push({
						"alarmID"     :  $scope.logDelayData.alarmMessage[i].CurAlarmData[j].alarmID    ,
		    			"alarmCode"   :  $scope.logDelayData.alarmMessage[i].CurAlarmData[j].alarmCode  ,
		    			"houseId"     :  $scope.logDelayData.alarmMessage[i].HouseId                    ,
		    			"delayTime"   :  deferTime
		    		});
				};
			}
		}
		if (CurAlarmData.length == 0 || !CurAlarmData) {
			return Sparraw.myNotice("暂无需要处理的报警。");
		};
		var params = {
			"CurAlarmData":CurAlarmData,
		};
		Sparraw.ajaxPost('alarmMobile/alarmDealDelay', params, function(data){
			console.log(data);
			if (data.ResponseDetail.Result == "Success") {
				CurAlarmData = [];
				Sparraw.myNotice("处理成功。");
			}else{

			};
			$scope.alarmQuery();//再查询新信息
		});
	}
	//处理点击事件
	$scope.DealFunc = function(){
		if ($scope.sparraw_user_temp.Authority.AlarmDeal === "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};

		var hideSheet = $ionicActionSheet.show({
                      buttons: [
                        { text: '延迟10分钟' },
                        { text: '延迟20分钟' },
                        { text: '延迟30分钟' }
                      ],
                      cancelText: '取消',
                      cancel: function() {
                      },
                      buttonClicked: function(index) {
                      	switch(index){
					      case 0 :
					      	$scope.DelayFunc(10);		//先处理
					      	break;
					      case 1 :
					      	$scope.DelayFunc(20);
					      	break;
					      case 2 :
					      	$scope.DelayFunc(30);
					      	break;
					      default  :
					      	break;
					    }
                        return true;
                      }
                  });
	}

	//数据源
    $scope.setData = function(){
    	$scope.logDelayData = {
		    'alarmMessage':[],
		    'showStatus':""
	    };
	    $scope.dataNullText = false;
	    $scope.logDelayTable = true;

	    setTimeout(function() {
	    	$scope.alarmQuery();
	    }, 500);
    };
    $scope.setData();
})