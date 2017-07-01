 angular.module('myApp.infoSafeguard', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 // 信息维护
.controller("infoSafeguardCtrl",function($scope, $state, $http, $ionicPopup, $ionicModal,AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	$scope.goFarmRegistered = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"farmRegistered");
	}

	$scope.goBuildingTable = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"buildingTable");
	}

	$scope.goEmployeesTable = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"employeesTable");
	}

	$scope.goUserInfo = function(){
		biz_common_judgeRegistInfo($ionicPopup,$state,"userInfo");
	}

	$scope.pointDevelop = function() {
		biz_common_pointDevelop();
		return;	
	};

     $scope.showConfirm = function() {
          app_confirm('是否要退出该用户?','提示',null,function(buttonIndex){
                   if(buttonIndex == 2){
                        biz_common_userLogout();
                        $state.go("landingPage");
                   }
              }); 
   };

	$scope.exit = function(){
		biz_common_userLogout();
		$state.go("landingPage");
	}





	$scope.updateFarm = function(){

		var params = {
          "userCode"  :  sparraw_user.profile.account,
          "passWord"        :  sparraw_user.profile.password,
          "AndroidImei": Common_MOBILE_IMEI,
          "uuid":Common_MOBILE_UUID,
          "model":Common_MOBILE_MODELNAME,
          "sysVersion":Common_MOBILE_VERSION,
          "platForm":Common_MOBILE_PLATFORM
      	};

		Sparraw.ajaxPost('loginMobile/login', params, function(data){
	        if (data.ResponseDetail.Result == 'Success') {
				sparraw_user.farmList = data.ResponseDetail.FarmList;
		   		if (data.ResponseDetail.FarmList.length == 0 || data.ResponseDetail.FarmList == null) {
		   			Sparraw.myNotice("请联系管理员，为您分配农场权限。");
		   		}else{
		   			$scope.infoSafeguardModaOpen();
    				$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
		   		}
	       }else{
	          Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
	       };
	    });
	};


	$scope.clickQueryDetail = function(farmId){
    	var params = {
			"userId":sparraw_user.userinfo.id,
    		"farmId":farmId        
		};
	    Sparraw.ajaxPost('loginMobile/queryDetail', params, function(data){
	    	if (data.ResponseDetail.Result == 'Success') {
	    		sparraw_user.houseinfos = data.ResponseDetail.HouseInfos;
	    		sparraw_user.farminfo = data.ResponseDetail.FarmInfo;
	    		sparraw_user.userinfos = data.ResponseDetail.EmploeeInfos;
	    		sparraw_user.Authority = data.ResponseDetail.AuthorityInfo;
		   		sparraw_user.profile = {
								   			'id_spa':sparraw_user.userinfo.id,
								   			'secret':'mtc_secret',
								   			'user_State': false ,
								   			'account':$scope.sparraw_user_temp.profile.account,
								   			'password':$scope.sparraw_user_temp.profile.password
								   		};
				try{
					var jpushTags = [];
					// 如果用户设置了栋舍信息
					if(sparraw_user.houseinfos){

						for (var i = 0; i < sparraw_user.houseinfos.length; i++) {

							jpushTags.push('mtc_tag_' + sparraw_user.houseinfos[i].id);

						};
						// 为Jpush 设置别名和标签
						Common_JPush_setTagsWithAlias(jpushTags, 'mtc_alias_' + sparraw_user.profile.id_spa);
					}else{
						console.log("用户暂未设置栋舍信息.")
					}
						
				}catch(e){
					//console.log(e);
				}
	    		//判断权限
	    		if ($scope.sparraw_user_temp.Authority.LogOn == "all") {
	    			$scope.infoSafeguardModaClose();
	    	  		$state.go("home");
				}else{
					$state.go("landingPage");
					return app_alert("该用户暂无登陆app权限，请联系管理员。");
				};
	    		
			}else {
			   	Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			}
	    });
    };


	$ionicModal.fromTemplateUrl('farmList.html', function(modal) {  
	    $scope.modalDIV = modal;
	}, {  
	    scope: $scope,  
	    animation: 'slide-in-up'
	});

    $scope.infoSafeguardModaOpen = function(){
	  	$scope.modalDIV.show();

    }

    $scope.infoSafeguardModaClose = function(){
    	$scope.modalDIV.hide();
    }

    

})