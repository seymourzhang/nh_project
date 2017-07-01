angular.module('myApp.chickenWeightAnalyze', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//体重曲线
.controller("chickenWeightAnalyzeCtrl",function($scope, $state,$ionicLoading, $http, $ionicSlideBoxDelegate, $stateParams, $ionicSideMenuDelegate, crisisServiceFactory, AppData) {
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    
    


    $scope.goDataAnalyseTable = function(){
    	$state.go("dataAnalyseTable");
    }

    if(navigator.userAgent.indexOf('Android') >= 0) {
		//安卓高度
		var MAXHEIGHT = document.documentElement.clientWidth;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('salesAnalyze_DIV').style.height = DIVHEIGHT + 'px';
	}else if(navigator.userAgent.indexOf('Firefox') >= 0) {
		//火狐浏览器获取高度
		var MAXHEIGHT = document.documentElement.clientHeight;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('salesAnalyze_DIV').style.height = DIVHEIGHT + 'px';
	}else {
		//计算出手机屏幕高度
		var MAXHEIGHT = document.body.clientWidth;

		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('salesAnalyze_DIV').style.height = DIVHEIGHT + 'px';
	}

    $scope.chickenWeightData = {
		"xData"       :  [] ,
		"yData"       :  [] ,
		"yName"       :  [] ,
		"yColor"      :  [] ,
		"hiddenPara"  :  [] ,
		"selectedBatch"  :  [] ,
		"selectedBatchKey"  :  [] ,
		"farmBatch"  :  [] ,
		"BatchDate"  :  [] ,
		"farmBatch"  :  [] ,
		"selectUnit" :  "" ,
		"compareType":  "" ,
		"ViewType":"01",
		"selectedHouse": "",
		"containBatchHouse":[]
    };

	//体重
     $scope.changes1 = function(){
     	$scope.chickenWeightData.xData=[];
     	
 		var params = { 
 			'IsNeedDelay':'Y',
        	'ViewType' : "01",
        	'FarmBreedId' :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
        	"FarmId":$scope.sparraw_user_temp.farminfo.id       	
        }
        console.log(params);
        if(params.ViewType==null){
                params.ViewType='01';
                //console.log("params");
		}
		Sparraw.ajaxPost('layer_report/queryChickenWeight.action', params, function(data){
			//data.ResponseDetail.Result = "Fail";
					if (data.ResponseDetail.Result == "Success" && data.ResponseDetail.xAxis.length != 0){
						 $scope.chickenWeightData.yName =  [];
						 $scope.chickenWeightData.yData =  []; 
						 var ageLeng =[];
						for (var i = 0; i < data.ResponseDetail.AvgWeight.length; i++) {
							$scope.chickenWeightData.yName[i] =  data.ResponseDetail.AvgWeight[i].HouseName;
							$scope.chickenWeightData.yData[i] =  data.ResponseDetail.AvgWeight[i].HouseDatas;
						}
					    var day_age_week  ="周龄";
					    $scope.chickenWeightData.xData=data.ResponseDetail.xAxis;
						var Maxage = Math.max.apply(null, data.ResponseDetail.xAxis);
						console.log($scope.chickenWeightData.xData);
						
					    if ($scope.chickenWeightData.ViewType=="01"){
								
								if(Maxage < 88){
									var j = 88 - Maxage;
									var L = data.ResponseDetail.xAxis.length
									
								   for (var i = 0; i < j; i++) {
									 $scope.chickenWeightData.xData[L+i] = Maxage + i + 1;
								   }
								}
						
                                day_age_week  ="周龄";
					    }else{
							var L = data.ResponseDetail.xAxis.length
							if(L < 60){
								var j = 60 - L;
								for (var i = 0; i < j; i++) {
									$scope.chickenWeightData.xData[L+i] = Maxage + i + 1;
								}
							}
                            day_age_week  ="日龄"
					    }
						
						var temp = [];
						for(var i = 0 ; i < data.ResponseDetail.xAxis.length; i++){
							if(data.ResponseDetail.xAxis[i] > 88){
								break;
							}
							temp[i] = data.ResponseDetail.xAxis[i];
						}
						$scope.chickenWeightData.xData =   temp ;
				
						$scope.chickenWeightData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						$scope.chickenWeightData.selectUnit = '';
						Echart_initLine01($scope.chickenWeightData.xData,
											 $scope.chickenWeightData.yData,
											 $scope.chickenWeightData.yName,
											 $scope.chickenWeightData.yColor,
											 $scope.chickenWeightData.hiddenPara,
											 $scope.chickenWeightData.selectUnit,
											 '单位：kg',
											 function (params){
						                        var res = day_age_week + ' :' + params[0].name;
									            for (var i = 0, l = params.length; i < l; i++) {
									                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value +'';
									            }
		                                        return    res;
						                     }
					   );
					}else if(data.ResponseDetail.Result == 'Fail'){
						Sparraw.myNotice("暂无信息");
					}else{
						Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
					};
		});
	}
  	
	  

	setLandscape(true,true);
	$scope.changes1();
})
