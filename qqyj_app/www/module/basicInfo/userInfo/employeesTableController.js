 angular.module('myApp.employeesTable', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 //员工列表
.controller("EmployeesTableCtrl",function($scope, $state, $http, $ionicPopup, AppData) {

	$scope.getData = function(){
		Sparraw.intoMyController($scope, $state);
		$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
		$scope.showData = [];
		$scope.showData = $scope.sparraw_user_temp.userinfos;
		console.log($scope.showData);
		for (var i = 0; i < $scope.showData.length; i++) {
			$scope.showData[i].roleStr = "";
			if ($scope.showData[i].UserHouse && $scope.showData[i].roleId != 101 && $scope.showData[i].roleId != 102) {
				for (var j = 0; j < $scope.showData[i].UserHouse.length; j++) {
					$scope.showData[i].roleStr += $scope.getHouseName($scope.showData[i].UserHouse[j].id) + "/";
				}
				$scope.showData[i].roleStr = "(" + $scope.showData[i].roleStr.substring(0,$scope.showData[i].roleStr.length-1) + ")";
			}else{
				$scope.showData[i].roleStr = "";
			}
		}




		if ($scope.sparraw_user_temp.Authority.MasterData === "all") {
			$scope.operateDIV = false;
		}else{
			$scope.operateDIV = true;
		};
	}


	$scope.getHouseName = function(houseId){
		var houseName = "";
		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			if ($scope.sparraw_user_temp.houseinfos[i].id == houseId) {
				houseName = $scope.sparraw_user_temp.houseinfos[i].name;
			}
		}
		return houseName;
	}



	if (sparraw_user.profile.user_State) {
		$scope.finishBtn  =  false  ;
		$scope.backBtn    =  true   ;
	}else {
		$scope.finishBtn  =  true   ;
		$scope.backBtn    =  false  ;
	};



	$scope.goAddEmployees = function(){
		if ($scope.sparraw_user_temp.Authority) {

		}else{
			return app_alert("您还未进行农场信息设置，请设置后再添加员工信息");
		};
		if ($scope.sparraw_user_temp.Authority.MasterData === "all") {
			$state.go("addEmployees");
		}else{
			return app_alert("该用户无此操作权限。");
		};
		$state.go("addEmployees");
	}
	

	$scope.remove = function(item){
		console.log($scope.sparraw_user_temp.Authority);
		console.log("-------");
		if ($scope.sparraw_user_temp.Authority.MasterData === "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};	
		if ($scope.sparraw_user_temp.userinfo.tele == item.tele ) {
			Sparraw.myNotice('无法删除自己');
		}else {
             app_confirm('删除该用户之后将无法恢复，请确认！','提示',null,function(buttonIndex){
                    if(buttonIndex == 2){
                    var params = {
                     	'user_id'         : item.userId  
			        };
				    Sparraw.ajaxPost('userMobile/delete', params, function(data){
						if (data.ResponseDetail.ErrorMsg == null) {
						    	biz_common_getLatestData($state,"",$scope.getData);
						    	Sparraw.myNotice('删除成功');
						    	
						   }else {
						   		Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
						   };
			        });
			        }
              });
		};
	};

	$scope.resetPassWord = function(item){
		if ($scope.sparraw_user_temp.Authority.MasterData === "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		app_confirm('重置后无法恢复原密码，请确认是否要继续？','提示',null,function(buttonIndex){
            if(buttonIndex == 2){
            	var params = {
					"user_id":item.userId,
				};
				Sparraw.ajaxPost('userMobile/resetPassword', params, function(data){
					if (data.ResponseDetail.ErrorMsg == null) {
						if (item.userId == $scope.sparraw_user_temp.userinfo.userId) {
							$state.go("landingPage");
						}else{

						}
					    app_alert('重置成功,重置密码为用户名加上数字123。');

					   }else {
					   		Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
					   };
		        });
	        }
      });
	};

	$scope.saveEmployees = function(para){
		Sparraw.myNotice('保存成功');
		sparraw_user.profile.user_State = false;
		$state.go("home");
	};

	$scope.getData();
})
