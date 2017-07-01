angular.module('myApp.feedEgg', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//料蛋比
.controller('feedEggCtrl', function($scope, $state) {
	
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    setLandscape(true,true);

    document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
	if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
	}else{
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
	}
	document.getElementById('main').style.height = persistentData.analysisChartH + '%';
	
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
						var tempLineConfig = setChartParams(params.ViewType, $scope.eggFeedData.yData,$scope.eggFeedData.yName);
						Echart_initLine02($scope.eggFeedData.xData,
										  tempLineConfig,
										  '',
										  null,
										  false,
										  null,
										  null,
										  null,
										  day_age_week
					    );
					}else if(data.ResponseDetail.Result == 'Fail'){
						Sparraw.myNotice("暂无信息");
						   
					}else{
						Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
					};
		});
	}
  	
  	$scope.chooseType = function(type){
		if (type == 'day') {
			document.getElementById('dayBtn').style.background = "#A9A9A9";
			document.getElementById('weekBtn').style.background = "#439AFC";
			$scope.eggFeedData.ViewType = "02";
		}else{
			document.getElementById('dayBtn').style.background = "#439AFC";
			document.getElementById('weekBtn').style.background = "#A9A9A9";
			$scope.eggFeedData.ViewType = "01";
		}
		$scope.changes1();
	}

	setTimeout(function() {
		$scope.chooseType('day');
	}, persistentData.horizontalTime);
	
	
})