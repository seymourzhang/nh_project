 angular.module('myApp.addbuilding', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
 //栋舍添加
.controller("AddbuildingCtrl",function($scope, $state, $http, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	//将农场信息中得数据代入到栋舍中
	$scope.tempVar.houseTemp.h_length = $scope.sparraw_user_temp.farminfo.house_length;
	$scope.tempVar.houseTemp.h_width  = $scope.sparraw_user_temp.farminfo.house_width;
	$scope.tempVar.houseTemp.h_height = $scope.sparraw_user_temp.farminfo.house_height;
	$scope.tempVar.houseTemp.feedarea = $scope.sparraw_user_temp.farminfo.feedarea;


	
	$scope.addHouse = function(){
		if (!checkInput(tempVar.houseTemp.house_name,'栋名称')) {return;}
	    var params = {
	       	'farm_id'     : $scope.sparraw_user_temp.farminfo.id,
         	'house_name'  : tempVar.houseTemp.house_name        ,
         	'house_type'  : $scope.sparraw_user_temp.farminfo.farm_type_id
	    };
	    
	    Sparraw.ajaxPost('houseMobile/add', params, function(data){

	    	if (data.ResponseDetail.ErrorMsg == null) {
		    	$scope.tempVar.houseTemp.id = data.ResponseDetail.houseId;

		    	//判断是否有信息
		    	if (sparraw_user.houseinfos == undefined) {
		    		$scope.sparraw_user_temp.houseinfos = [];
				}else{

				};

		  		$scope.sparraw_user_temp.houseinfos.push(JSON.parse(JSON.stringify($scope.tempVar.houseTemp)));
		    	sparraw_user = JSON.parse(JSON.stringify($scope.sparraw_user_temp));

		    	Sparraw.myNotice('保存成功');
		    	
		    	$scope.tempVar.houseTemp = {};

		    	//重新获取服务器最新数据
    			biz_common_getLatestData($state,"buildingTable");
			}else {
			    Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};

	    });
  	};
  	$scope.dealBarCode = function(){
		Common_barScan(function(result){
			app_alert("您的设备编号是：" + result.text);
			$scope.tempVar.houseTemp.mtc_device_id = result.text;
			document.getElementById("addMtcIdInput").focus();
		},function(error){
			app_alert("调用扫描失败，请手动输入设备编号。");
		});
	};


	$scope.GoBuildingTable = function(){
		$scope.tempVar.houseTemp = {};
		$state.go("buildingTable");
	}
})