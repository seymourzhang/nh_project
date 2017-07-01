 angular.module('myApp.employeesTable', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 //员工列表
.controller("EmployeesTableCtrl",function($scope, $state, $http, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	$scope.showData = $scope.sparraw_user_temp.userinfos;


	$scope.getHouseName = function(houseId){
		var houseName = "";
		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			if ($scope.sparraw_user_temp.houseinfos[i].id == houseId) {
				houseName = $scope.sparraw_user_temp.houseinfos[i].houseName;
			}
		}
		return houseName;
	}

	for (var i = 0; i < $scope.showData.length; i++) {
		$scope.showData[i].roleStr = "";
		if ($scope.showData[i].houses) {
			for (var j = 0; j < $scope.showData[i].houses.length; j++) {
				$scope.showData[i].roleStr += $scope.getHouseName($scope.showData[i].houses[j]) + "/";
			}
			$scope.showData[i].roleStr = "(" + $scope.showData[i].roleStr.substring(0,$scope.showData[i].roleStr.length-1) + ")";
		}else{
			$scope.showData[i].roleStr = "";
		}
		


		switch($scope.showData[i].role){
	      case 1 :
	      	$scope.showData[i].role = "老板";
	      	break;
	      case 2 :
	      	$scope.showData[i].role = "场长";
	      	break;
	      case 3 :
	      	$scope.showData[i].role = "技术员";
	      	break;
	      case 4 :
	      	$scope.showData[i].role = "饲养员";
	      	break;
	      case 5 :
	      	$scope.showData[i].role = "副场长";
	      	break;
	      case 6 :
	      	$scope.showData[i].role = "统计员";
	      	break;
	      default  :
	      	$scope.showData[i].role = "只读用户";
	      	break;
	    }

	    switch($scope.showData[i].author_id){
	      case 0 :
	      	$scope.showData[i].author_id = "超级";
	      	break;
	      case 1 :
	      	$scope.showData[i].author_id = "一级";
	      	break;
	      case 2 :
	      	$scope.showData[i].author_id = "二级";
	      	break;
	      case 3 :
	      	$scope.showData[i].author_id = "三级";
	      	break;
	      default  :
	      	$scope.showData[i].author_id = "四级";
	      	break;
	    }
	}

	for (var i = 0; i < $scope.showData.length; i++) {
		if ($scope.showData[i].author_id == "超级") {
			$scope.showData[i].portraitImg = "img/newFolder/basicInfo/userInfo/user_star.png";
		}else if ($scope.showData[i].author_id == "一级") {
			$scope.showData[i].portraitImg = "img/newFolder/basicInfo/userInfo/user_1.png";
		}else if ($scope.showData[i].author_id == "二级") {
			$scope.showData[i].portraitImg = "img/newFolder/basicInfo/userInfo/user_2.png";
		}else if ($scope.showData[i].author_id == "三级") {
			$scope.showData[i].portraitImg = "img/newFolder/basicInfo/userInfo/user_3.png";
		}else if ($scope.showData[i].author_id == "四级") {
			$scope.showData[i].portraitImg = "img/newFolder/basicInfo/userInfo/user_4.png";
		}
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
		if ($scope.sparraw_user_temp.Authority.basicInfo === "All") {
			$state.go("addEmployees");
		}else{
			return app_alert("该用户无此操作权限。");
		};
	}
	

	$scope.remove = function(item){
		if ($scope.sparraw_user_temp.Authority.basicInfo === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};	
		if ($scope.sparraw_user_temp.userinfo.tele == item.tele ) {
			Sparraw.myNotice('无法删除自己');
		}else {
             app_confirm('删除该用户之后将无法恢复，请确认！','提示',null,function(buttonIndex){
                    if(buttonIndex == 2){
                     var params = {
			    	'operate'  : "DELETE" ,
			    	'userInfo' : {
					      'id'         : item.id                                            ,
					      'name'       : item.name                                          ,
					      'tele'       : item.tele       	                                ,
					      'pw'         : "123456"                                           ,
					      'role'       : item.role                                          ,
					      'farmid'     : $scope.sparraw_user_temp.farminfo.id               ,
					      'houses'     : null  	                                            
					  }
			        };
				    Sparraw.ajaxPost('sys/user/update.action', params, function(data){
						if (data.ResponseDetail.ErrorMsg == null) {
						   		var delIndex = -1;
								for(i in $scope.sparraw_user_temp.userinfos){
									if($scope.sparraw_user_temp.userinfos[i].id==item.id){
										delIndex = i ;
									}
								}
								$scope.sparraw_user_temp.userinfos.splice(delIndex,1);
								sparraw_user = JSON.parse(JSON.stringify($scope.sparraw_user_temp));
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
		if ($scope.sparraw_user_temp.Authority.basicInfo === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		app_confirm('重置后无法恢复原密码，请确认是否要继续？','提示',null,function(buttonIndex){
            if(buttonIndex == 2){
            	var params = {
					"user_id":item.id,
				};
				Sparraw.ajaxPost('sys/user/resetPassword.action', params, function(data){
					if (data.ResponseDetail.ErrorMsg == null) {
						if (item.id == $scope.sparraw_user_temp.userinfo.id) {
							$state.go("landingPage");
						}else{

						}
					    app_alert('重置成功,重置密码为123456。');

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
})
