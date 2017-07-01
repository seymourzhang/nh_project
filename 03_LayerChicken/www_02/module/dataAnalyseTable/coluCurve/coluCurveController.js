angular.module('myApp.coluCurve', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//产蛋率曲线(演示)
.controller("coluCurveCtrl",function($scope, $state, $http,$ionicLoading, $ionicSlideBoxDelegate, $stateParams, $ionicSideMenuDelegate, crisisServiceFactory, AppData) {
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
		"ViewType":"01",
		"selectedHouse": "",
		"containBatchHouse":[]
    };
    $scope.chooseBtn = true;

	//产蛋率
	// var yLeftRange = [70,100];
	var yLeftRange = null;
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
		Sparraw.ajaxPost('layer_report/queryLayerCurve.action', params, function(data){
					if (data.ResponseDetail.Result == "Success" && data.ResponseDetail.xAxis.length != 0){
						 $scope.cullDeathRateData.yName =  [];
						 $scope.cullDeathRateData.yData =  []; 
						 var yMaxValue = null;
						 var yMinValue = null;

						
						for (var i = 0; i < data.ResponseDetail.LayerRate.length; i++) {
							$scope.cullDeathRateData.yName[i] =  data.ResponseDetail.LayerRate[i].HouseName;
							$scope.cullDeathRateData.yData[i] =  data.ResponseDetail.LayerRate[i].HouseDatas;
							for (var j = 0; j < $scope.cullDeathRateData.yData[i].length; j++) {
								if ($scope.cullDeathRateData.yData[i][j] < 50 ) {
									$scope.cullDeathRateData.yData[i][j] = "-";
								}
							}
							var tempMax = Math.max.apply(null,$scope.cullDeathRateData.yData[i]);
							var tempMin = Math.min.apply(null,$scope.cullDeathRateData.yData[i]);
							if(yMaxValue == null || yMaxValue < tempMax){
								yMaxValue = tempMax ;
							}
							if(yMinValue == null || yMinValue > tempMin){
								yMinValue = tempMin ;
							}
						}

						if(yMaxValue > 100 ){
							yMaxValue = 100;
						}else{
							yMaxValue = null;
						}
						if(yMinValue < 70 ){
							yMinValue = 70;
						}else{
							yMinValue = null;
						}
						
						yLeftRange = [yMinValue,yMaxValue];
					    $scope.cullDeathRateData.xData=data.ResponseDetail.xAxis;
						var Maxage = Math.max.apply(null, data.ResponseDetail.xAxis);
					    if ($scope.cullDeathRateData.ViewType=="01"){
								
								if(Maxage < 88){
									var j = 88 - Maxage;
									var L = data.ResponseDetail.xAxis.length
								   for (var i = 0; i < j; i++) {
									 $scope.cullDeathRateData.xData[L+i] = Maxage + i + 1;
								   }
								}
						
                                var day_age_week  ="周龄";
								
								/**/
								var temp = [];// 22 57 
								var fromIndex = 0;
								var endIndex = 0;
								for(var i = 0 ;i < $scope.cullDeathRateData.xData.length; i++ ){
									var x = $scope.cullDeathRateData.xData[i];
									if(x == 22){
										fromIndex = i;
									}
									
									if(x == 57){
										endIndex = i;
									}
									
									if(x >= 22 && x <= 57){
										temp.push(x);
									}
								}
								yMaxValue = null;
								yMinValue = null;
								for (var i = 0; i < data.ResponseDetail.LayerRate.length; i++) {
									$scope.cullDeathRateData.yName[i] =  data.ResponseDetail.LayerRate[i].HouseName;
									$scope.cullDeathRateData.yData[i] =  data.ResponseDetail.LayerRate[i].HouseDatas;
									var temp2 = [];
									
									for(j=fromIndex; j<endIndex; j++ ){
										temp2.push(data.ResponseDetail.LayerRate[i].HouseDatas[j])
									}
									$scope.cullDeathRateData.yData[i] = temp2;
									
									var tempMax = Math.max.apply(null,$scope.cullDeathRateData.yData[i]);
									var tempMin = Math.min.apply(null,$scope.cullDeathRateData.yData[i]);
									
									console.log("tempMin:"+tempMin + ",tempMax:" +tempMax)
									
									if(yMaxValue == null || yMaxValue < tempMax){
										yMaxValue = tempMax ;
									}
									if(yMinValue == null || yMinValue > tempMin){
										yMinValue = tempMin ;
									}
								}
								
								console.log("yMinValue:"+yMinValue + ",yMaxValue:" +yMaxValue)
								if(yMaxValue > 100 ){
									yMaxValue = 100;
								}else{
									yMaxValue = null;
								}
								if(yMinValue < 85 ){
									yMinValue = 85;
								}else{
									yMinValue = null;
								}
								yMaxValue = 100;
								yMinValue = 80;
								console.log("yMinValue:"+yMinValue + ",yMaxValue:" +yMaxValue)
								$scope.cullDeathRateData.xData = temp;
								yLeftRange = [yMinValue,yMaxValue];
								$scope.cullDeathRateData.yName.unshift("标准");
								$scope.cullDeathRateData.yData.unshift(["80.2","88.3","91.9","93.0","93.7","94.1","94.4","94.7","94.8","94.9","94.8","94.7","94.6","94.4","94.2","93.9","93.7","93.5","93.3","93.0","92.7","92.4","92.1","91.7","91.4","91.1","90.7","90.3","89.8","89.4","89.0","88.6","88.1","87.6","87.1","86.7","86.1","85.6","85.1","84.5","84.0","83.4","82.9","82.4","81.8","81.2","80.6","80.0","79.4","78.8","78.2","77.5","76.8","76.2","75.5","74.9","74.2","73.5","72.7"]); 
					    }else{	
							var L = data.ResponseDetail.xAxis.length
							if(L < 60){
								var j = 60 - L;
								for (var i = 0; i < j; i++) {
									$scope.cullDeathRateData.xData[L+i] = Maxage + i + 1;
								}
							}
                            var day_age_week  ="日龄" 
					    }
						$scope.cullDeathRateData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						$scope.cullDeathRateData.selectUnit = '';
						Echart_initLine01($scope.cullDeathRateData.xData,
											 $scope.cullDeathRateData.yData,
											 $scope.cullDeathRateData.yName,
											 $scope.cullDeathRateData.yColor,
											 $scope.cullDeathRateData.hiddenPara,
											 $scope.cullDeathRateData.selectUnit,
											 '单位：％',
											 function (params){
						                        var res = day_age_week + ' :' + params[0].name;
									            for (var i = 0, l = params.length; i < l; i++) {
									                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value + '';
									            }
		                                        return    res;
						                     },yLeftRange
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
					    
						var day_age_week  ="周龄"
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
                               day_age_week  ="周龄"
					    }else{
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
    	var dd = "蛋重";//document.getElementById("chang").innerHTML;
	        if(dd=='产蛋率'){
	             return $scope.changes2();
	        }else if (dd=='蛋重'){
	        	 return $scope.changes1();
	        }
	};


	setLandscape(true,true);
	$scope.changes1();


})