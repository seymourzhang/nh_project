 angular.module('myApp.buildingTable', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 // 栋舍列表
.controller("BuildingTableCtrl",function($scope, $state, $http, AppData, $ionicPopup) {

	$scope.query = function(){
		Sparraw.intoMyController($scope, $state);
		$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
		$scope.showHouseList = [];
		$scope.showHouseList = $scope.sparraw_user_temp.houseinfos;
	}
	
	$scope.goAddbuilding = function(){
		if ($scope.sparraw_user_temp.Authority.MasterData === "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		$state.go("addbuilding");
	}

	

	$scope.remove = function(item){
		if ($scope.sparraw_user_temp.Authority.MasterData === "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		app_confirm('删除该栋舍之后将无法恢复，请确认！','提示',null,function(buttonIndex){
		        if(buttonIndex == 2){
		        	var params = {
						'house_id': item.id,
					};
					Sparraw.ajaxPost('houseMobile/delete', params, function(data){
						if (data.ResponseDetail.Result == "Success") {
							biz_common_getLatestData($state,"",$scope.query);
							Sparraw.myNotice("删除成功！");
						}else{
							Sparraw.myNotice(data.ResponseDetail.Error);
						}
					});
		        }
        });
	};

	$scope.goEmployeesTable = function(para){
		console.log($scope.sparraw_user_temp.houseinfos);
		if ($scope.sparraw_user_temp.userinfo.houses == 0 || !$scope.sparraw_user_temp.userinfo.houses) {
			app_alert("您还未创建栋舍。");
		}else{
			$state.go("employeesTable");
		};
	};

	$scope.query();

})