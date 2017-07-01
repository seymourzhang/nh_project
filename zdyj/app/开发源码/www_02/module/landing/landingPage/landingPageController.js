 angular.module('myApp.landingPage', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 // 用户登录
.controller("landingPageCtrl",function($scope, $state, $http, AppData, $stateParams, $ionicModal) {

	// 保持竖屏
	setPortrait(true,true);

	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	if(navigator.userAgent.indexOf('Firefox') >= 0) {
		//火狐浏览器获取高度
		var MAXHEIGHT = document.documentElement.clientHeight;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - '45';
		document.getElementById('landingPage_Underlying_DIV').style.height = DIVHEIGHT + 'px';
	}else {
		//计算出手机屏幕高度
		var MAXHEIGHT = document.body.scrollHeight;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - '45';
		document.getElementById('landingPage_Underlying_DIV').style.height = DIVHEIGHT + 'px';
	};

	var userTemp = biz_common_getUserInfo();
	var userCode,passWord;

	
	
	if(userTemp != null){
		userCode = userTemp.userCode;
		if(userTemp.savePW == "true"){
			passWord = userTemp.pw;	
		}else{
			passWord = "";
		}
	}else{
		userCode = "";
		passWord = "";
	}



	//跳转至协议界面
	if(userTemp == null){
		if (selectBackPage.firstTime != true) {
			selectBackPage.firstTime = true;
			$state.go("landingProtocol");
		}else{
			
		}
	}else{
		setTimeout(function() {
			$scope.automaticLogin();
		}, 500);
	}

	$scope.landing = {
		"userCode":userCode,
		"passWord":passWord
	};

	 $scope.pushNotification = { checked: true };
	 var savePWFlag = true;
	 $scope.pushNotificationChange = function() {
        if ($scope.pushNotification.checked) {
        	savePWFlag = true;
        	//console.log("保存");
        }else {
        	savePWFlag = false;
        	//console.log("不保存");
        };
      };

    $scope.Register = function(){
    	sparraw_user.profile.user_State = true;
    	$state.go("useRegistered");
    };

    $scope.tempProductionMode = CONFIG_AppMode;

    if($scope.tempProductionMode == 'Development'){
    	$scope.webUrl = '1';
	    var tempURL = "1" ;
	    API_Host = CONFIG_Development_API_Host ;
	    $scope.changeURL = function(){
	    	if(tempURL == "1"){
	    		API_Host = CONFIG_Production_API_Host ;
	    		tempURL = '2';
	    	}else{
	    		API_Host = CONFIG_Development_API_Host ;
	    		tempURL = '1';
	    	}
	    };    	
    }else if($scope.tempProductionMode == 'Production'){
    	API_Host = CONFIG_Production_API_Host ;
    }else if($scope.tempProductionMode == 'Local'){
    	API_Host = CONFIG_Local_API_Host ;
    }

    $scope.goForgotPassword = function(){
    	$state.go("forgotPassword");
    }

    var demoFlag = false;

	
    //测试账户登陆
    $scope.demoLogin = function(){
		$scope.landing = {
			"userCode":'admin',
			"passWord":"admin123"
		};
		demoFlag = true;
		$scope.accountLogin();
    };


    $scope.automaticLogin = function(){
    	if (selectBackPage.NeedLogin) {
    		if ($scope.landing.userCode != "" || !$scope.landing.userCode &&
	    		$scope.landing.passWord != "" || !$scope.landing.passWord) {
	    		$scope.accountLogin();
	    	}
    	}
    }


    $scope.companyInformation = true;
    $scope.focus = function(){
    	$scope.companyInformation = false;
    }

    $scope.blur = function(){
    	$scope.companyInformation = true;
    }

    $scope.switchAccount = function(){
		$state.go("userList");
    }

    if (persistentData.selectedUserInfo) {
    	for (var i = 0; i < persistentData.transferUserArr.length; i++) {
			if (persistentData.transferUserArr[i].userCode == persistentData.selectedUserInfo) {
				$scope.landing.userCode = persistentData.transferUserArr[i].userCode;
				if (persistentData.transferUserArr[i].savePW == "true") {
					$scope.landing.passWord = persistentData.transferUserArr[i].pw
				}else{
					$scope.landing.passWord = '';
				}
			}
		}
    }


    $ionicModal.fromTemplateUrl('farmList.html', function(modal) {  
	    $scope.modalDIV = modal;
	}, {  
	    scope: $scope,  
	    animation: 'slide-in-up'
	});

    $scope.openFarmList = function(){
	  	$scope.modalDIV.show();

    };

    $scope.closeFarmList = function(){
    	$scope.modalDIV.hide();
    }

    $scope.accountLogin = function(){

		if (Common_MOBILE_IMEI == "null") {
			Common_MOBILE_IMEI = '';
		}

		var params = {
		    	      "userCode": $scope.landing.userCode,
    				  "passWord": $scope.landing.passWord,
    				  "loginApp": "App01",
    				  "AndroidImei": Common_MOBILE_IMEI,
    				  "uuid":Common_MOBILE_UUID,
    				  "model":Common_MOBILE_MODELNAME,
    				  "sysVersion":Common_MOBILE_VERSION,
    				  "platForm":Common_MOBILE_PLATFORM
		};
		//校验信息
		
	 	if ($scope.landing.userCode == null || $scope.landing.userCode == '') {return Sparraw.myNotice('请输入用户名。');};
	 	if ($scope.landing.passWord == null || $scope.landing.passWord == '') {return Sparraw.myNotice('请输入密码。');};

		Sparraw.ajaxPost('loginMobile/login', params, function(data){
		    if (data.ResponseDetail.Result == 'Success') {
		    	persistentData.switchRemind = true;		

				// 缓存用户名密码
				if(!demoFlag){
					biz_common_saveUserInfo(JSON.stringify({"userCode": $scope.landing.userCode,"pw": $scope.landing.passWord,"savePW":savePWFlag+""}));
				}

				sparraw_user.userinfo = data.ResponseDetail.userinfo;
				sparraw_user.farmList = data.ResponseDetail.FarmList;

				persistentData.selectedUserInfo = null;
		   		

		   		if (data.ResponseDetail.FarmList.length == 0 || data.ResponseDetail.FarmList == null) {
		   			
		   			Sparraw.myNotice("请联系管理员，为您分配农场权限。");

		   		}else if(data.ResponseDetail.FarmList.length == 1){
		   			
		   			$scope.clickQueryDetail(data.ResponseDetail.FarmList[0].farmId);
		   		}else{
		   			$scope.openFarmList();
		   			
    				$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
		   		}
		   }else{
		   		console.log(data.ResponseDetail.Error);
		   		Sparraw.myNotice(data.ResponseDetail.Error);
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
								   			'account':$scope.landing.userCode,
								   			'password':$scope.landing.passWord
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
	    			$scope.closeFarmList();
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
});