angular.module('myApp.cullDeathRate', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 死淘率曲线
.controller("cullDeathRateCtrl",function($scope, $state,$ionicLoading, $http, $ionicSlideBoxDelegate, $stateParams, $ionicSideMenuDelegate, crisisServiceFactory, AppData) {
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
		"ViewType":"01",
		"compareType":  "" ,
		"selectedHouse": "",
		"containBatchHouse":[]
    };
	
	var type = "01";
	var yLeftRange = [0,10];//undefined;//[0,15];
	//死淘率
     $scope.changes1 = function(){
     	$scope.cullDeathRateData.xData=[];
     	console.log("死淘率:" + type);
 		var params = { 
 			'IsNeedDelay':'Y',
        	'ViewType' : type,
        	'FarmBreedId' :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
        	"FarmId":$scope.sparraw_user_temp.farminfo.id       	
        }
        if(params.ViewType==null){
                  params.ViewType='02';
                  console.log("params");
		}
		Sparraw.ajaxPost('layer_report/queryDeathCull.action', params, function(data){
					if (data.ResponseDetail.Result == "Success" && data.ResponseDetail.xAxis.length != 0){
						 $scope.cullDeathRateData.yName =  [];
						 $scope.cullDeathRateData.yData =  []; 
						 
						 var yLeftMin = null;
						 var yLeftMax = null;
						 
						for (var i = 0; i < data.ResponseDetail.LayerRate.length; i++) {
							$scope.cullDeathRateData.yName[i] =  data.ResponseDetail.LayerRate[i].HouseName;
							$scope.cullDeathRateData.yData[i] =  data.ResponseDetail.LayerRate[i].HouseDatas;
							
							var max = Math.max.apply(null, data.ResponseDetail.LayerRate[i].HouseDatas);
							var min = Math.min.apply(null, data.ResponseDetail.LayerRate[i].HouseDatas);
							
							console.log("max:" + max + ",min；" + min);
							if(yLeftMax == null){
								yLeftMax = max;
							}else if(yLeftMax < max){
								yLeftMax = max;
							}
							
							if(yLeftMin == null){
								yLeftMin = min;
							}else if(yLeftMin > min){
								yLeftMin = min;
							}
						}
						
						
					    var Maxage = Math.max.apply(null, data.ResponseDetail.xAxis);
					    $scope.cullDeathRateData.xData=data.ResponseDetail.xAxis;
					    //console.log($scope.cullDeathRateData.xData);
					    
						
					    if(Maxage < 88){
						  var j = 88 - Maxage;
						  var L = data.ResponseDetail.xAxis.length;
                          for (var i = 0; i < j; i++) {
                            $scope.cullDeathRateData.xData[L+i] = Maxage + i + 1;
                          }
					    }
						
					    if (type=="01" || type=="03"){
                                var day_age_week  ="周龄"
					    }else{
                                var day_age_week  ="日龄"
					    }
						
					    console.log($scope.cullDeathRateData.xData);
						$scope.cullDeathRateData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						var tail = "";
						if(type === "01"){
							tail = "‰";
							$scope.cullDeathRateData.selectUnit = '‰';
							if(yLeftMin != null && yLeftMin < 0){
								yLeftMin = 0;
							}
							
							if(yLeftMax != null && yLeftMax > 2){
								yLeftMax = 2;
							}
							yLeftMax = (yLeftMax).toFixed(1);
						}

						if(type === "03"){
							tail = "％";
							$scope.cullDeathRateData.selectUnit = '％';
							if(yLeftMin != null && yLeftMin < 0){
								yLeftMin = 0;
							}
							
							if(yLeftMax != null && yLeftMax > 5){
								yLeftMax = 5;
							}
							yLeftMax = Math.ceil(yLeftMax);
							//yLeftMax = (yLeftMax).toFixed(1);
						}
						yLeftRange = [yLeftMin,yLeftMax];
						

					    console.log($scope.cullDeathRateData.xData);
						$scope.cullDeathRateData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						$scope.cullDeathRateData.selectUnit = '';
						Echart_initLine01($scope.cullDeathRateData.xData,
											 $scope.cullDeathRateData.yData,
											 $scope.cullDeathRateData.yName,
											 $scope.cullDeathRateData.yColor,
											 $scope.cullDeathRateData.hiddenPara,
											 $scope.cullDeathRateData.selectUnit,
											 '单位：' + tail,
											 function (params){
						                        var res = day_age_week + ' :' + params[0].name;
									            for (var i = 0, l = params.length; i < l; i++) {
									                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value + '';
									            }
		                                        return    res;
						                     },
											 yLeftRange
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
        if(dd=='每周数'){
        	 console.log("这是changes2方法");
            
        	 document.getElementById("chang").innerHTML = '累计数';
             document.getElementById("cullSurTi").innerHTML = '每周死淘率曲线';
             // params.FeedWaterFlag='Feed';
			 yLeftRange = [0,10];
			 type = "01";
             return $scope.changes1();
        }else if (dd=='累计数'){
        	 console.log("这是changes1方法");
			 document.getElementById("chang").innerHTML = '每周数';
        	 document.getElementById("cullSurTi").innerHTML = '累计死淘率曲线';
			 yLeftRange = null;
        	 // params.FeedWaterFlag='Water';
			  type = "03";
       	     return $scope.changes1();
        }
    }


    setLandscape(true,true);
	$scope.changes1();

})
