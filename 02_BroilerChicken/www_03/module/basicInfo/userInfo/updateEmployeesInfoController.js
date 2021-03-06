angular.module('myApp.updateEmployeesInfo', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 员工修改
.controller("updateEmployeesInfoCtrl",function($scope, $state, $http, $stateParams,  $ionicPopup, $ionicModal, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));



	//查询
	for(i in $scope.sparraw_user_temp.userinfos){
		if($scope.sparraw_user_temp.userinfos[i].id == $stateParams.employeesID){
			$scope.tempVar.userTemp = JSON.parse(JSON.stringify($scope.sparraw_user_temp.userinfos[i]));
		}
	}

	

	$scope.backBtn    =  false  ;//返回按钮 默认显示
	$scope.visible    =  false  ;//编辑按钮 默认显示
	$scope.sheerDiv   =  false  ;//覆盖图片 默认显示
	$scope.cancelBtn  =  true   ;//保存按钮 默认隐藏
	$scope.houseAffiliation = true;//栋舍列表 默认显示
	
	//编辑事件
	$scope.startEdit = function(){
		if ($scope.sparraw_user_temp.Authority.basicInfo === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};
		//自己无法修改自己角色
	   	if ($scope.tempVar.userTemp.tele == $scope.sparraw_user_temp.userinfo.tele) {return app_alert('用户无法修改自己的信息');};
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


	for (var i = 0; i < $scope.sparraw_user_temp.userinfos.length; i++) {
		if ($scope.sparraw_user_temp.userinfos[i].tele == $scope.tempVar.userTemp.tele) {

			if ($scope.sparraw_user_temp.userinfos[i].role == 4) {
				$scope.houseAffiliation = true;
			}else{
				$scope.houseAffiliation = false;
			};
		};
	};



	$scope.JudgeRole = function(judgeFirstTime){
		if ($scope.tempVar.userTemp.role == 4) {
			$scope.houseAffiliation = true;
		}else {
			$scope.houseAffiliation = false;
		};

		if (judgeFirstTime != 'firstTime') {
			$scope.tempVar.userTemp.author_id = "pleaseSelect";
		}
		
		$scope.permissionList = {};  
		if ($scope.tempVar.userTemp.role == 1 || $scope.tempVar.userTemp.role == 2) {
			$scope.permissionList = {
				0:'超级'
			};
		}else if ($scope.tempVar.userTemp.role == 4) {
			$scope.permissionList = {
				2:'2级',3:'3级'
			};
		}else {
			$scope.permissionList = {
				1:'1级',2:'2级',3:'3级'
			};
		};

	}



	if ($scope.tempVar.userTemp.role == 4) {
		$scope.selectedHouse = [];//获取选取的栋舍id
		for (var x = 0; x < $scope.tempVar.userTemp.houses.length; x++) {
			$scope.selectedHouse.push($scope.tempVar.userTemp.houses[x]);
		};
	}else {
		$scope.tempVar.userTemp.houses = null;
	};
	

	$scope.testAry = [];
	$scope.testAry2 = [];

	$scope.devList = [];//将栋 舍 列 表显示在页面上
    for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
    	$scope.devList.push({"text"     :           $scope.sparraw_user_temp.houseinfos[i].houseName ,
    						 "checked"  :           false                                            ,
    						 "id"       :           $scope.sparraw_user_temp.houseinfos[i].id
    	});    	
    	$scope.testAry.push($scope.devList[i].id);
    };

    

    //对比页面所选栋舍号并且显示所选栋舍
	for(var s in $scope.testAry){
	    for(var x in $scope.selectedHouse){
	        if($scope.testAry[s] === $scope.selectedHouse[x]){
	            $scope.testAry2.push($scope.testAry[s]);
	        }
	    }
	  };

	for (var i = 0; i < $scope.devList.length; i++) {
		for (var x = 0; x < $scope.testAry2.length; x++) {
			if ($scope.testAry2[x] === $scope.devList[i].id) {
				$scope.devList[i].checked = true;
			};
		};

	};



    $scope.judgeHousChange = function(){
    	$scope.tempVar.userTemp.houses = [];
    	for (var i = 0; i < $scope.devList.length; i++) {
	    	if ($scope.devList[i].checked == true) {
	    		$scope.tempVar.userTemp.houses.push($scope.devList[i].id);
	    	}else {

	    	};
	    };
    }


	$scope.saveUpdate = function(){

		if ($scope.sparraw_user_temp.Authority.basicInfo === "All") {

		}else{
			return app_alert("该用户无此操作权限。");
		};	
	    /* 校验信息*/
	   	if ($scope.tempVar.userTemp.name == "" || $scope.tempVar.userTemp.name == undefined ) {
	   		return Sparraw.myNotice('请输入姓名。');
	   	};

	   	if ($scope.tempVar.userTemp.role == "" || $scope.tempVar.userTemp.role == undefined ) {
	   		return Sparraw.myNotice('请选择角色。');
	   	};

	   	if ($scope.tempVar.userTemp.author_id == "pleaseSelect" || $scope.tempVar.userTemp.author_id == undefined ) {
	   		return Sparraw.myNotice('请选择权限。');
	   	};

		var selectedHouse = [];
		for (var i = 0; i < $scope.devList.length; i++) {
	    	if ($scope.devList[i].checked == true) {
	    		selectedHouse.push($scope.devList[i].id);
	    	}else {

	    	};
	    };
	    //判断饲养员是否选择了所属栋舍
	    if($scope.tempVar.userTemp.role==4){
	    	console.log(selectedHouse.length);
	   		if (selectedHouse.length == 0) {
	   			return Sparraw.myNotice("请选择所属栋舍");
	   		}else{

	   		};
	    }else{

	    };
		
		    var params = {
		    	'operate'  : "UPDATE" ,
		    	'userInfo' : {
				      'id'         : $scope.tempVar.userTemp.id                    ,
				      'name'       : $scope.tempVar.userTemp.name                  ,
				      'tele'       : $scope.tempVar.userTemp.tele       	       ,
				      'role'       : $scope.tempVar.userTemp.role                  ,
				      'farmid'     : $scope.sparraw_user_temp.farminfo.id          ,
				      'author_id'  : $scope.tempVar.userTemp.author_id             ,
				      'houses'     : $scope.tempVar.userTemp.houses  	                           
				  }

		    };

		    
		    Sparraw.ajaxPost('sys/user/update.action', params, function(data){

		    	if (data.ResponseDetail.ErrorMsg == null) {
			   		var delIndex = -1;
					for(i in $scope.sparraw_user_temp.userinfos){
						if($scope.sparraw_user_temp.userinfos[i].id == $scope.tempVar.userTemp.id){
							delIndex = i ;
						}
					}

					$scope.sparraw_user_temp.userinfos.splice(delIndex,1,JSON.parse(JSON.stringify($scope.tempVar.userTemp)));

					sparraw_user = JSON.parse(JSON.stringify($scope.sparraw_user_temp));

					$scope.tempVar.userTemp = {};

			    	biz_common_getLatestData($state,'employeesTable');
			    	app_alert('修改成功。');
				   }else {
				   		Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
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



  	$scope.clickPhone = function(){
  		app_confirm('手机号码暂时无法修改，如需修改手机请删除该员工后再添加。','提示',null,function(buttonIndex){
                    if(buttonIndex == 2){
                     

			        };
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

    $scope.JudgeRole('firstTime');

})
