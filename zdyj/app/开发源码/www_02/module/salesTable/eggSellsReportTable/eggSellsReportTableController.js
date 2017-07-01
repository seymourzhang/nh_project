 angular.module('myApp.eggSellsReportTable', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
//销售报表
.controller("eggSellsReportTableCtrl",function($scope, $state, $ionicLoading, $http, $ionicPopup, AppData) {
	
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    setPortrait(true,true);
    

    /*$scope.pointDevelop = function(reportId) {
        if(reportId == 'xsjl'){
            if ($scope.sparraw_user_temp.farminfo.farmBreedBatchId == 0) {
                app_alert("暂无数据，请先新建批次。");
            }else{
                $state.go("eggSellsReport");
            }
        }else if(reportId == 'xsrb'){
            $state.go("eggSellsList");
        }else if (reportId == 'jgqx') {
            $state.go("salesAnalyze",{"lastPage":"eggSellsReportTable"});
        }else{
            biz_common_pointDevelop();
        }
    };*/


    $scope.goTempChart = function(){
        $state.go("tempChart");
    };
    $scope.goCo2AndLightChart = function(){
        $state.go("co2AndLightChart");
    };

    $scope.pointDevelop = function() {
        //biz_common_judgeRegistInfo($ionicPopup,$state,"");
        biz_common_pointDevelop();
        return;
    };
})