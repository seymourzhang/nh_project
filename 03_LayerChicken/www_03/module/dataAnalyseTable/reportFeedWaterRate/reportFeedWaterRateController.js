angular.module('myApp.reportFeedWaterRate', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 日采食曲线
.controller("reportFeedWaterCtrl",function($scope, $state,$ionicLoading, $http, $stateParams, $ionicPopup, AppData) {
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
    var params = { 
    		'IsNeedDelay':'Y',
        	'FeedWaterFlag' : null,
        	'FarmBreedId' :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
        	"FarmId":$scope.sparraw_user_temp.farminfo.id       	
        }
	//采食饮水
     $scope.changes1 = function(){
     	console.log("采食饮水");
     	$scope.cullDeathRateData.xData=[];
        console.log(params);
        if(params.FeedWaterFlag==null){
            params.FeedWaterFlag='Feed';
            var yName  ="单位：克/天·只"
		}else if (params.FeedWaterFlag=='Water') {
           var yName  ="单位：毫升/天·只"
		}else if (params.FeedWaterFlag=='Feed') {
           var yName  ="单位：克/天·只"
		}
		 console.log("params");
		Sparraw.ajaxPost('layer_report/queryFeedWater.action', params, function(data){
					if (data.ResponseDetail.Result == "Success" && data.ResponseDetail.xAxis.length != 0){
						 $scope.cullDeathRateData.yName =  [];
						 $scope.cullDeathRateData.yData =  []; 
						 var ageLeng =[];
						for (var i = 0; i < data.ResponseDetail.LayerRate.length; i++) {
							$scope.cullDeathRateData.yName[i] =  data.ResponseDetail.LayerRate[i].HouseName;
							$scope.cullDeathRateData.yData[i] =  data.ResponseDetail.LayerRate[i].HouseDatas;
						}
					    var Maxage = Math.max.apply(null, data.ResponseDetail.xAxis);
					    $scope.cullDeathRateData.xData=data.ResponseDetail.xAxis;
					    console.log($scope.cullDeathRateData.xData);
						 
					    
					    var L = data.ResponseDetail.xAxis.length;
					    if($scope.cullDeathRateData.xData.length<60){
							var j = 60-data.ResponseDetail.xAxis.length;
                          for (var i = 0; i < j; i++) {
                            $scope.cullDeathRateData.xData[L+i] = Maxage + i + 1;
                          }
					    }
					    var day_age_week  ="日龄"
					    console.log($scope.cullDeathRateData.xData);
						$scope.cullDeathRateData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						$scope.cullDeathRateData.selectUnit = '';
						Echart_initLine01($scope.cullDeathRateData.xData,
											 $scope.cullDeathRateData.yData,
											 $scope.cullDeathRateData.yName,
											 $scope.cullDeathRateData.yColor,
											 $scope.cullDeathRateData.hiddenPara,
											 $scope.cullDeathRateData.selectUnit,
											 yName,
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
	  
	  $scope.changes = function(){
    	console.log("这是changes方法");
        var dd = document.getElementById("chang").innerHTML ;
        if(dd=='采食'){
        	 console.log("这是changes2方法");
             document.getElementById("chang").innerHTML = '饮水';
             document.getElementById("cullSurTi").innerHTML = '采食曲线';
              params.FeedWaterFlag='Feed';
             return $scope.changes1();
        }else if (dd=='饮水'){
        	 console.log("这是changes1方法");
        	 document.getElementById("chang").innerHTML = '采食';
        	 document.getElementById("cullSurTi").innerHTML = '饮水曲线';
        	  params.FeedWaterFlag='Water';
       	     return $scope.changes1();
        }
    }


    setLandscape(true,true);
	$scope.changes1();


})
