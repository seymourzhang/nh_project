angular.module('myApp.tasklist', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
.controller("tasklistCtrl",function($scope, $state, $http, $ionicPopup,$ionicLoading,$cordovaFileTransfer,$cordovaFileOpener2,$timeout, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	$scope.setData = function(){
		$scope.taskListData = {
			"selectHouse"      :  $scope.sparraw_user_temp.houseinfos[0]     ,
            "showDate"         :  new Date().Format("yyyy-MM-dd")                  ,
            "FarmId"           :  $scope.sparraw_user_temp.farminfo.id       ,
            "UnCompleteTaskNum":  "0"                                         ,
            "delayCount"       :  "0"                                         ,
            "RemindDate"       :  ""                                         ,
            "TskInfo":[{  
                "RemindID"     :  ""                                         ,
                "TskID"        :  ""                                         ,
                "TskType"      :  ""                                         ,
                "TskName"      :  ""                                         ,
                "dealStatus"   :  ""
            }]
		};
        setTimeout(function() {
            $scope.query();
        }, 500);
	}

    $scope.clickDateInput = function(){
        Sparraw.openDatePicker("taskListData.showDate","$scope.judgeHouse();");
    };

	$scope.judgeHouse = function(){
		$scope.query();
	}

    $scope.query = function(){
        var params = {
            "RemindDate":$scope.taskListData.showDate,
            "HouseId":"",
            "FarmId":$scope.taskListData.FarmId
        };
        if (Object.prototype.toString.call($scope.taskListData.selectHouse) === "[object String]") {
            params.HouseId = JSON.parse($scope.taskListData.selectHouse).id;
        }else{
            params.HouseId = $scope.taskListData.selectHouse.id;
        }
        Sparraw.ajaxPost('taskMobile/taskQuery', params, function(data){
            if (data.ResponseDetail.Result == "Success") {
                $scope.taskListData.UnCompleteTaskNum = data.ResponseDetail.UnCompleteTaskNum;
                $scope.taskListData.delayCount = data.ResponseDetail.delayCount;
                $scope.taskListData.RemindDate = data.ResponseDetail.RemindDate;
                $scope.taskListData.TskInfo = data.ResponseDetail.TskInfo;
                $scope.taskSketch = true;
                if ($scope.taskListData.TskInfo.length == 0) {
                    $scope.dataNullText = true;
                    $scope.taskTable = false;
                    document.getElementById('dataNullTextId').innerHTML = "该日期暂无任务。";
                }else{
                    $scope.dataNullText = false;
                    $scope.taskTable = true;
                    document.getElementById('dataNullTextId').innerHTML = "";
                    for (var i = 0; i < $scope.taskListData.TskInfo.length; i++) {
                        if ($scope.taskListData.TskInfo[i].TskName.length > 10) {
                            $scope.taskListData.TskInfo[i].TskName = $scope.taskListData.TskInfo[i].TskName.substr(0, 10) + "...";
                        };
                    }
                }
            }else if (data.ResponseDetail.Result == "Fail") {
                $scope.dataNullText = true;
                $scope.taskTable = false;
                Sparraw.myNotice(data.ResponseDetail.Error);
                if (data.ResponseDetail.Error == "暂无批次信息！") {
                    $scope.taskSketch = false;
                    document.getElementById('dataNullTextId').innerHTML = "暂无批次信息。";
                }else if (data.ResponseDetail.Error == "暂无数据！") {
                    $scope.taskSketch = true;
                    document.getElementById('dataNullTextId').innerHTML = "该日期暂无任务。";
                }
            }else{
                Sparraw.myNotice(data.ResponseDetail.Error);
            };
        });
    }

    //任务状态选择
    $scope.judgeTaskState = function(choice,sku){

        //添加权限
        if ($scope.sparraw_user_temp.Authority.TaskDeal == "all") {

        }else{
            return app_alert("该用户无此操作权限。");
        };
        
        var params = {
            "FarmId"      :  $scope.taskListData.FarmId    ,
            "HouseId"     :  ""                            ,
            "RemindID"    :  sku.RemindID                  ,
            "RemindDate"  :  $scope.taskListData.showDate  ,
            "TaskName"    :  sku.TskName                   ,
            "dealStatus"  :  choice
        };
        if (Object.prototype.toString.call($scope.taskListData.selectHouse) === "[object String]") {
            params.HouseId = JSON.parse($scope.taskListData.selectHouse).id;
        }else{
            params.HouseId = $scope.taskListData.selectHouse.id;
        }
        Sparraw.ajaxPost('taskMobile/taskDeal', params, function(data){
            if (data.ResponseDetail.Result == "Success") {
                Sparraw.myNotice("操作成功！");
                $scope.query();
            }else{
                Sparraw.myNotice(data.ResponseDetail.Error);
            };
        });
    };

    $scope.setData();
})