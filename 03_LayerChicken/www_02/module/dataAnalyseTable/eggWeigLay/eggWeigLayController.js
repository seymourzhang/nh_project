angular.module('myApp.eggWeigLay', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 蛋重曲线
.controller("eggWeigLayCtrl",function($scope, $state,$ionicLoading, $http, $ionicSlideBoxDelegate, $stateParams, $ionicSideMenuDelegate, crisisServiceFactory, AppData) {
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
		document.getElementById('cullDeath_DIV').style.height = DIVHEIGHT + 'px';
	}else if(navigator.userAgent.indexOf('Firefox') >= 0) {
		//火狐浏览器获取高度
		var MAXHEIGHT = document.documentElement.clientHeight;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('cullDeath_DIV').style.height = DIVHEIGHT + 'px';
	}else {
		//计算出手机屏幕高度
		var MAXHEIGHT = document.body.clientWidth;

		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('cullDeath_DIV').style.height = DIVHEIGHT + 'px';
	}

    $scope.cullDeathRateData = {
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
		"ViewType":"02",
		"selectedHouse": "",
		"containBatchHouse":[]
    };
    $scope.chooseBtn = true;
	//产蛋率
     $scope.changes1 = function(){
     	$scope.cullDeathRateData.xData=[];
     	console.log("产蛋率");
 		var params = { 
 			'IsNeedDelay':'Y',
        	'ViewType' : $scope.cullDeathRateData.ViewType,
        	'FarmBreedId' :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
        	"FarmId":$scope.sparraw_user_temp.farminfo.id       	
        }
        console.log(params);
        if(params.ViewType==null){
                  params.ViewType='02';
                  console.log("params");
		}
		Sparraw.ajaxPost('layer_report/queryEggWeightCurve.action', params, function(data){
					if (data.ResponseDetail.Result == "Success" && data.ResponseDetail.xAxis.length != 0){
						 $scope.cullDeathRateData.yName =  [];
						 $scope.cullDeathRateData.yData =  []; 
						for (var i = 0; i < data.ResponseDetail.EggAvgWeight.length; i++) {
							$scope.cullDeathRateData.yName[i] =  data.ResponseDetail.EggAvgWeight[i].HouseName;
							$scope.cullDeathRateData.yData[i] =  data.ResponseDetail.EggAvgWeight[i].HouseDatas;
						}
						 
						var day_age_week  ="周龄";
					    $scope.cullDeathRateData.xData=data.ResponseDetail.xAxis;
						var Maxage = Math.max.apply(null, data.ResponseDetail.xAxis);
						console.log($scope.cullDeathRateData.xData);
						
					    if ($scope.cullDeathRateData.ViewType=="01"){
								
								if(Maxage < 88){
									var j = 88 - Maxage;
									var L = data.ResponseDetail.xAxis.length
									
								   for (var i = 0; i < j; i++) {
									 $scope.cullDeathRateData.xData[L+i] = Maxage + i + 1;
								   }
								}
						
                                day_age_week  ="周龄";
					    }else{
							var L = data.ResponseDetail.xAxis.length
							if(L < 60){
								var j = 60 - L;
								for (var i = 0; i < j; i++) {
									$scope.cullDeathRateData.xData[L+i] = Maxage + i + 1;
								}
							}
                            day_age_week  ="日龄"
					    }
						
					    console.log($scope.cullDeathRateData.xData);
						$scope.cullDeathRateData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						$scope.cullDeathRateData.selectUnit = '';
						Echart_initLine01($scope.cullDeathRateData.xData,
											 $scope.cullDeathRateData.yData,
											 $scope.cullDeathRateData.yName,
											 $scope.cullDeathRateData.yColor,
											 $scope.cullDeathRateData.hiddenPara,
											 $scope.cullDeathRateData.selectUnit,
											 '单位：克',
											 function (params){
						                        var res = day_age_week + ' :' + params[0].name;
									            for (var i = 0, l = params.length; i < l; i++) {
									                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value + '';
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
  	//蛋重
	$scope.changes2 = function(){
		console.log("蛋重");
		$scope.cullDeathRateData.xData = [];
			var params = { 
				'IsNeedDelay':'Y',
				'ViewType' : $scope.cullDeathRateData.ViewType,
		    	'FarmBreedId' :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
		    	"FarmId":$scope.sparraw_user_temp.farminfo.id   
			}
			if(params.ViewType==null){
                  params.ViewType='02';
			}
			Sparraw.ajaxPost('layer_report/queryEggWeightCurve.action', params, function(data){
						if (data.ResponseDetail.Result == "Success" && data.ResponseDetail.xAxis.length != 0){
						 $scope.cullDeathRateData.yName =  [];
						 $scope.cullDeathRateData.yData =  []; 
						 var ageLeng =[];
						for (var i = 0; i < data.ResponseDetail.EggAvgWeight.length; i++) {
							$scope.cullDeathRateData.yName[i] =  data.ResponseDetail.EggAvgWeight[i].HouseName;
							$scope.cullDeathRateData.yData[i] =  data.ResponseDetail.EggAvgWeight[i].HouseDatas;
						}
					    var Maxage = Math.max.apply(null, data.ResponseDetail.xAxis);
					    $scope.cullDeathRateData.xData=data.ResponseDetail.xAxis;
					     console.log($scope.cullDeathRateData.xData);
					    var j = 60-data.ResponseDetail.xAxis.length;
					    var L = data.ResponseDetail.xAxis.length
					    if($scope.cullDeathRateData.xData.length<60){
                          for (var i = 0; i < j; i++) {
                            $scope.cullDeathRateData.xData[L+i] = Maxage + i;
                          }
					    }
					    if ($scope.cullDeathRateData.ViewType=="01") {
                                var day_age_week  ="周龄"
					    }else{
                                var day_age_week  ="日龄"
					    }
					    console.log($scope.cullDeathRateData.xData);
						$scope.cullDeathRateData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						$scope.cullDeathRateData.selectUnit = '';
						Echart_initLine01($scope.cullDeathRateData.xData,
											 $scope.cullDeathRateData.yData,
											 $scope.cullDeathRateData.yName,
											 $scope.cullDeathRateData.yColor,
											 $scope.cullDeathRateData.hiddenPara,
											 $scope.cullDeathRateData.selectUnit,
											 '克/枚',
											 function (params){
						                        var res = day_age_week + ' :' + params[0].name;
									            for (var i = 0, l = params.length; i < l; i++) {
									                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value + 'g';
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
	  
	  $scope.changes = function(){
    	console.log("这是changes方法");
        var dd = document.getElementById("chang").innerHTML ;
        if(dd=='蛋重'){
        	 console.log("这是changes2方法");
             document.getElementById("chang").innerHTML = '产蛋率';
             document.getElementById("cullSurTi").innerHTML = '蛋重曲线';
             return $scope.changes2();
        }else if (dd=='产蛋率'){
        	 console.log("这是changes1方法");
        	 document.getElementById("chang").innerHTML = '蛋重';
        	 document.getElementById("cullSurTi").innerHTML = '产蛋率曲线';
       	     return $scope.changes1();
        }
    }
    //切换查看方式
    $scope.changesCompareType = function(){
    	var dd = document.getElementById("chang").innerHTML;
	        if(dd=='产蛋率'){
	             return $scope.changes2();
	        }else if (dd=='蛋重'){
	        	 return $scope.changes1();
	        }
	};

	setLandscape(true,true);
	$scope.changes1();



})