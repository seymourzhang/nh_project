angular.module('myApp.reportEggWeightFeed', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//料蛋比曲线 
.controller("reportEggWeightFeedCtrl",function($scope, $state,$ionicLoading, $http, $stateParams, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

	
	


    $scope.goDataAnalyseTable = function(){
    	$state.go("dataAnalyseTable");
    }

	if(navigator.userAgent.indexOf('Android') >= 0) {
		//火狐浏览器获取高度
		var MAXHEIGHT = document.documentElement.clientWidth;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('eggWeight_DIV').style.height = DIVHEIGHT + 'px';
	}else if(navigator.userAgent.indexOf('Firefox') >= 0) {
		//火狐浏览器获取高度
		var MAXHEIGHT = document.documentElement.clientHeight;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('eggWeight_DIV').style.height = DIVHEIGHT + 'px';
	}else {
		//计算出手机屏幕高度
		var MAXHEIGHT = document.body.clientWidth;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('eggWeight_DIV').style.height = DIVHEIGHT + 'px';
	}


	$scope.eggFeedData = {
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
     	$scope.eggFeedData.xData=[];
     	
 		var params = { 
 			'IsNeedDelay':'Y',
        	'ViewType' : $scope.eggFeedData.ViewType,
        	'FarmBreedId' :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
        	"FarmId":$scope.sparraw_user_temp.farminfo.id       	
        }
        console.log(params);
        if(params.ViewType==null){
                params.ViewType='01';
		}
		Sparraw.ajaxPost('layer_report/queryFeedEggRatio.action', params, function(data){
					if (data.ResponseDetail.Result == "Success" && data.ResponseDetail.xAxis.length != 0){
						 $scope.eggFeedData.yName =  [];
						 $scope.eggFeedData.yData =  []; 
						 var ageLeng =[];
						for (var i = 0; i < data.ResponseDetail.LayerRate.length; i++) {
							$scope.eggFeedData.yName[i] =  data.ResponseDetail.LayerRate[i].HouseName;
							$scope.eggFeedData.yData[i] =  data.ResponseDetail.LayerRate[i].HouseDatas;
						}
						
					    var day_age_week  ="周龄";
					    $scope.eggFeedData.xData=data.ResponseDetail.xAxis;
						var Maxage = Math.max.apply(null, data.ResponseDetail.xAxis);
						console.log($scope.eggFeedData.xData);
						
					    if ($scope.eggFeedData.ViewType=="01"){
								
								if(Maxage < 88){
									var j = 88 - Maxage;
									var L = data.ResponseDetail.xAxis.length
									
								   for (var i = 0; i < j; i++) {
									 $scope.eggFeedData.xData[L+i] = Maxage + i + 1;
								   }
								}
						
                                day_age_week  ="周龄";
					    }else{
							var L = data.ResponseDetail.xAxis.length
							if(L < 60){
								var j = 60 - L;
								for (var i = 0; i < j; i++) {
									$scope.eggFeedData.xData[L+i] = Maxage + i + 1;
								}
							}
                            day_age_week  ="日龄"
					    }
						console.log($scope.eggFeedData.xData);
						
						$scope.eggFeedData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						$scope.eggFeedData.selectUnit = '';
						Echart_initLine01($scope.eggFeedData.xData,
											 $scope.eggFeedData.yData,
											 $scope.eggFeedData.yName,
											 $scope.eggFeedData.yColor,
											 $scope.eggFeedData.hiddenPara,
											 $scope.eggFeedData.selectUnit,
											 '',
											 function (params){
						                        var res = day_age_week + ' :' + params[0].name;
									            for (var i = 0, l = params.length; i < l; i++) {
									                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value;
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
  	
	

	$scope.changeViewType = function(){
		$scope.changes1();
	}


	setLandscape(true,true);
	$scope.changes1();


	
})