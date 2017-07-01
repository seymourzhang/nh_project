 angular.module('myApp.updateEmployeesInfo', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 // 员工修改
.controller("updateEmployeesInfoCtrl",function($scope, $state, $http, $stateParams,  $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	



	$scope.setData = function(){
		//查询
		for(i in $scope.sparraw_user_temp.userinfos){
			if($scope.sparraw_user_temp.userinfos[i].userId == $stateParams.employeesID){
				$scope.tempVar.userTemp.user_code  =  $scope.sparraw_user_temp.userinfos[i].userCode;
				$scope.tempVar.userTemp.ch_name  =  $scope.sparraw_user_temp.userinfos[i].userName;
				$scope.tempVar.userTemp.en_name  =  $scope.sparraw_user_temp.userinfos[i].userEnName;
				$scope.tempVar.userTemp.mobile_num  =  $scope.sparraw_user_temp.userinfos[i].tele;
				$scope.tempVar.userTemp.role_temp_id  =  $scope.sparraw_user_temp.userinfos[i].roleId;
				$scope.tempVar.userTemp.userId = $scope.sparraw_user_temp.userinfos[i].userId;
			}
		}
		$scope.tempVar.userTemp.houses = [];
		for (var i = 0; i < $scope.sparraw_user_temp.userinfos.length; i++) {

			if ($scope.sparraw_user_temp.userinfos[i].userId == $scope.tempVar.userTemp.userId) {
				if ($scope.tempVar.userTemp.role_temp_id == 201 || $scope.tempVar.userTemp.role_temp_id == 202) {
					$scope.houseAffiliation = true;
				}else{
					$scope.houseAffiliation = false;
				};
			};
		};


		$scope.devList = [];//所有的栋舍
		$scope.selectedHouseId = [];//选中的栋舍Id
		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			$scope.devList.push({"name"     :           $scope.sparraw_user_temp.houseinfos[i].name ,
	    						 "checked"  :           false                                       ,
	    						 "id"       :           $scope.sparraw_user_temp.houseinfos[i].id
	    	}); 
		}
		if ($scope.tempVar.userTemp.role_temp_id == 201 || $scope.tempVar.userTemp.role_temp_id == 202) {//非场长查看已选择的栋舍
			for (var i = 0; i < $scope.sparraw_user_temp.userinfos.length; i++) {
				if ($scope.tempVar.userTemp.userId == $scope.sparraw_user_temp.userinfos[i].userId) {
					for (var j = 0; j < $scope.sparraw_user_temp.userinfos[i].UserHouse.length; j++) {
						$scope.selectedHouseId.push($scope.sparraw_user_temp.userinfos[i].UserHouse[j].id);
					}
				}
			}
			//获取已选中的栋舍
			setTimeout(function() {
				for (var i = 0; i < $scope.devList.length; i++) {
					for (var x = 0; x < $scope.selectedHouseId.length; x++) {
						if ($scope.selectedHouseId[x] === $scope.devList[i].id) {
							$scope.devList[i].checked = true;
						};
					};

				};
			}, 500);

		}else{//场长与场长助理默认全选
			for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
				$scope.tempVar.userTemp.houses.push({"name"     :           $scope.sparraw_user_temp.houseinfos[i].name ,
		    						 				 "id"       :           $scope.sparraw_user_temp.houseinfos[i].id
		    	}); 
			}
		}

		$scope.backBtn    =  false  ;//返回按钮 默认显示
		$scope.visible    =  false  ;//编辑按钮 默认显示
		$scope.sheerDiv   =  false  ;//覆盖图片 默认显示
		$scope.cancelBtn  =  true   ;//保存按钮 默认隐藏

		
	};

	





	
	//编辑事件
	$scope.startEdit = function(){
		if ($scope.sparraw_user_temp.Authority.MasterData === "all") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		//自己无法修改自己角色
	   	if ($scope.tempVar.userTemp.tele == $scope.sparraw_user_temp.userinfo.tele) {return Sparraw.myNotice('禁止编辑当前登陆的用户信息');};
		$scope.backBtn    =  true  ;
		$scope.visible    =  true  ;
		$scope.sheerDiv   =  true  ;
		$scope.cancelBtn  =  false ;
		Sparraw.myNotice('请编辑');
	}

	//取消事件
	$scope.cancelEvent = function(){
		$scope.backBtn    =  false  ;
		$scope.visible    =  false  ;
		$scope.sheerDiv   =  false  ;
		$scope.cancelBtn  =  true   ;
  	}


	



	$scope.JudgeHouse = function(){
		$scope.devList = [];
		if ($scope.tempVar.userTemp.role_temp_id == 201 || $scope.tempVar.userTemp.role_temp_id == 202) {
			$scope.houseAffiliation = true;
			for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
				$scope.devList.push({"name"     :           $scope.sparraw_user_temp.houseinfos[i].name ,
		    						 "checked"  :           false                                       ,
		    						 "id"       :           $scope.sparraw_user_temp.houseinfos[i].id
		    	}); 
			}
		}else {
			$scope.houseAffiliation = false;
			for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
				$scope.tempVar.userTemp.houses.push({"name"     :           $scope.sparraw_user_temp.houseinfos[i].name ,
		    						 				 "id"       :           $scope.sparraw_user_temp.houseinfos[i].id
		    	}); 
			}
		};
	}

    $scope.judgeHousChange = function(){
    	for (var i = 0; i < $scope.devList.length; i++) {
	    	if ($scope.devList[i].checked == true) {
	    		$scope.tempVar.userTemp.houses.push($scope.devList[i].id);
	    	}
	    };
    }


	$scope.saveUpdate = function(){
	   	/* 校验信息*/
	    if (!checkInput($scope.tempVar.userTemp.user_code     ,'用户名')) {return;}
	   	if (!checkInput($scope.tempVar.userTemp.ch_name       ,'中文名')) {return;}
	   	if (!checkInput($scope.tempVar.userTemp.en_name       ,'英文名')) {return;}
	   	if (!checkInput($scope.tempVar.userTemp.mobile_num    ,'手机号')) {return;}
	   	if (!checkInput($scope.tempVar.userTemp.role_temp_id  ,'角色')) {return;}
	   	if (!$scope.devList) {return Sparraw.myNotice("请先选择角色。");}
	   	//判断所选栋舍（场长、场长助理默认全选）
	    $scope.tempVar.userTemp.houses = [];
	    if (tempVar.userTemp.role_temp_id == 101 || tempVar.userTemp.role_temp_id ==102) {
	    	for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
				$scope.tempVar.userTemp.houses.push({"house_name"     :           $scope.sparraw_user_temp.houseinfos[i].name ,
									 	   			"house_id"       :           $scope.sparraw_user_temp.houseinfos[i].id
				});
			};
	    }else{
	    	for (var i = 0; i < $scope.devList.length; i++) {
	    		if ($scope.devList[i].checked == true) {
	    			$scope.tempVar.userTemp.houses.push({"house_name"  :  $scope.devList[i].name ,
										 	   			"house_id"    :  $scope.devList[i].id
					});
	    		}
	    	}
	    }
	    if ($scope.tempVar.userTemp.role_temp_id == 201 || $scope.tempVar.userTemp.role_temp_id == 202) {
	   		if ($scope.tempVar.userTemp.houses.length == 0) { return Sparraw.myNotice("请至少选择一栋");}
	   	}




		    var params = {
		    		'CompanyId'     : $scope.sparraw_user_temp.farminfo.CompanyId ,
		    		'farmId'		: $scope.sparraw_user_temp.farminfo.id     ,
		    		'user_id'       : $scope.tempVar.userTemp.userId           ,
			      	'ch_name'       : $scope.tempVar.userTemp.ch_name          ,
					'en_name'       : $scope.tempVar.userTemp.en_name          ,
					'mobile_num'    : $scope.tempVar.userTemp.mobile_num       ,
					'role_temp_id'  : $scope.tempVar.userTemp.role_temp_id     ,
					'UserHouse'     : $scope.tempVar.userTemp.houses
		    };

		    
		    
		    Sparraw.ajaxPost('userMobile/update', params, function(data){

		    	if (data.ResponseDetail.ErrorMsg == null) {
			    	Sparraw.myNotice('编辑成功！');
			    	$scope.tempVar.userTemp = {};
			    	biz_common_getLatestData($state,'employeesTable');
				   }else {
				   		Sparraw.myNotice(data.ResponseDetail.Error);
				   };

		    });

	}

	$scope.alert = function(){
		Sparraw.myNotice('点击编辑后即可修改');
	}
	

	//返回时清空填写内容 
	$scope.backTable = function(){
		if (!$scope.sheerDiv) {
			$scope.tempVar.userTemp = {};
			$state.go("employeesTable");
		}else {

		}
  	};


  	$scope.setData();
  	
})