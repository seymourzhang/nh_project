 angular.module('myApp.buildingTable', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 // 栋舍列表
.controller("BuildingTableCtrl",function($scope, $state, $http, AppData, $ionicPopup) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	
	// 获取栋舍信息
	$scope.queryHouses = function(){
		
			var params = {

			  'FarmId'            : $scope.sparraw_user_temp.farminfo.id          
		      
			};
		    
		    Sparraw.ajaxPost('sys/house/queryHouses.action', params, function(data){


		    	  if (data.ResponseDetail.ErrorMsg == null) {
		    		
					sparraw_user.houseinfos = data.ResponseDetail.houseinfos;
					$scope.sparraw_user_temp.houseinfos = data.ResponseDetail.houseinfos;
					
			    	
				   }else {
				   		Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
				   };

		    });
	}
	
	//$scope.queryHouses();
	
	//true-隐藏，false-显示
	/*if ($scope.sparraw_user_temp.hasOwnProperty("houseinfos")) {//有栋舍
		if ( $scope.sparraw_user_temp.hasOwnProperty("userinfos")) {//有用户
			$scope.backBtn    =  false ;
			$scope.finishBtn  =  true ;
		}else{//没用户
			$scope.backBtn    =  true  ;
			$scope.finishBtn  =  false ;
		};

	}else{//没有栋舍
		console.log("2");
		$scope.backBtn    =  true   ;
		$scope.finishBtn  =  true   ;
	};

	if ($scope.sparraw_user_temp.userinfos.length == "1") {
		$scope.finishBtn  =  false ;
		console.log("对");
	}else{
		$scope.finishBtn  =  true ;
		console.log("错");
	};*/

	


	$scope.goAddbuilding = function(){
		if ($scope.sparraw_user_temp.Authority.MasterData === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		$state.go("addbuilding");
	}
	

	$scope.remove = function(item){
		Sparraw.myNotice('如需删除该栋舍，请联系我们。');
	};

	$scope.goEmployeesTable = function(para){
		console.log($scope.sparraw_user_temp.houseinfos);
		if ($scope.sparraw_user_temp.userinfo.houses == 0 || !$scope.sparraw_user_temp.userinfo.houses) {
			app_alert("您还未创建栋舍。");
		}else{
			$state.go("employeesTable");
		};
	};
})