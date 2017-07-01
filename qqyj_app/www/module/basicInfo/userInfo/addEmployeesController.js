 angular.module('myApp.addEmployees', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
//员工添加
.controller("AddEmployeesCtrl",function($scope, $state, $http, $ionicPopup, AppData) {

	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	$scope.JudgeHouse = function(selectRole){
		$scope.devList = [];
		if (selectRole == 201 || selectRole == 202) {
			$scope.houseAffiliation = true;
			for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
				$scope.devList.push({"name"     :           $scope.sparraw_user_temp.houseinfos[i].name ,
		    						 "checked"  :           false                                       ,
		    						 "id"       :           $scope.sparraw_user_temp.houseinfos[i].id
		    	}); 
			}
		}else {
			$scope.houseAffiliation = false;
		};
	}


    
    $scope.testModule = function(){
    	tempVar.userTemp.user_code    = "测试用户名" + parseInt(Math.random()*100);
		tempVar.userTemp.ch_name      = "测试中文";
		tempVar.userTemp.en_name      = "testEnglish";
		tempVar.userTemp.mobile_num   = 13820170428;
		tempVar.userTemp.role_temp_id = 102;
    }





	$scope.save = function(){

		if (!$scope.devList) {return Sparraw.myNotice("请先选择角色。");}
	    //判断所选栋舍（场长、场长助理默认全选）
	    $scope.selectedHouse = [];
	    if (tempVar.userTemp.role_temp_id == "101" || tempVar.userTemp.role_temp_id == "102") {
	    	for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
				$scope.selectedHouse.push({"house_name"     :           $scope.sparraw_user_temp.houseinfos[i].name ,
									 	   "house_id"       :           $scope.sparraw_user_temp.houseinfos[i].id
				});
			};
	    }else{
	    	for (var i = 0; i < $scope.devList.length; i++) {
	    		if ($scope.devList[i].checked == true) {
	    			$scope.selectedHouse.push({"house_name"  :  $scope.devList[i].name ,
										 	   "house_id"    :  $scope.devList[i].id
					});
	    		}
	    	}
	    }


	     /* 校验信息*/
	   	if (!checkInput(tempVar.userTemp.user_code     ,'用户名')) {return;}
	   	if (!checkInput(tempVar.userTemp.ch_name       ,'中文名')) {return;}
	   	if (!checkInput(tempVar.userTemp.en_name       ,'英文名')) {return;}
	   	if (!checkInput(tempVar.userTemp.mobile_num    ,'手机号')) {return;}
	   	if (!checkInput(tempVar.userTemp.role_temp_id  ,'角色')) {return;}
	   	if ($scope.selectedHouse.length == 0) {
	   		return Sparraw.myNotice("请至少选择一栋");
	   	}
		if($scope.tempVar.userTemp.mobile_num && /^1[3|4|5|7|8]\d{9}$/.test($scope.tempVar.userTemp.mobile_num)){
			//对的
			console.log("输入正确");
		} else{
			//不对
			return Sparraw.myNotice("请输入正确手机号码");
		};


		if(tempVar.userTemp.user_code && /^[0-9A-Za-z_]{6,}$/.test(tempVar.userTemp.user_code)){
			//对的
			console.log("输入正确");
		} else{
			//不对
			return app_alert("用户名必须大于等于6位并且只有字母和数字或下划线组成！");
		};




	    var params = {
	    	'CompanyId'     : $scope.sparraw_user_temp.farminfo.CompanyId ,
		    'farmId'		: $scope.sparraw_user_temp.farminfo.id     ,
	    	'user_code'     : tempVar.userTemp.user_code        ,
			'ch_name'       : tempVar.userTemp.ch_name          ,
			'en_name'       : tempVar.userTemp.en_name          ,
			'mobile_num'    : tempVar.userTemp.mobile_num       ,
			'role_temp_id'  : tempVar.userTemp.role_temp_id     ,
			'UserHouse'     : $scope.selectedHouse

	    };
	    
	    Sparraw.ajaxPost('userMobile/add', params, function(data){
	    	
	    	if (data.ResponseDetail.Result == "Success") {
			   		$scope.tempVar.userTemp.id = data.ResponseDetail.userId;


			   		//判断是否有信息
			    	if (sparraw_user.userinfos == undefined) {
			    		$scope.sparraw_user_temp.userinfos = [];
					}else{

					};

					$scope.sparraw_user_temp.userinfos.push(JSON.parse(JSON.stringify($scope.tempVar.userTemp)));

			    	sparraw_user = JSON.parse(JSON.stringify($scope.sparraw_user_temp));

			    	//清空新建员工信息
			    	$scope.tempVar.userTemp = {};
			    	$scope.devList = [];
	    			$scope.selectedHouse = [];
			    	
			    	//重新获取服务器最新数据
			    	biz_common_getLatestData($state,'employeesTable');
			    	app_alert('保存成功，新建员工密码为用户名加上数字123。');


			   }else {
			   		Sparraw.myNotice(data.ResponseDetail.Error);
			   };


	    });

  	};

})
 