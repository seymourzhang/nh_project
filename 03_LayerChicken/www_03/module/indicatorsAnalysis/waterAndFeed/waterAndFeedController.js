angular.module('myApp.waterAndFeed', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//水料比
.controller('waterAndFeedCtrl', function($scope, $state) {
	
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
	
	$scope.waterFeedData = {
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
     	$scope.waterFeedData.xData=[];
     	
 		var params = { 
 			'IsNeedDelay':'Y',
        	'ViewType' : $scope.waterFeedData.ViewType,
        	'FarmBreedId' :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
        	"FarmId":$scope.sparraw_user_temp.farminfo.id       	
        }
        console.log(params);
        if(params.ViewType==null){
                params.ViewType='01';
		}
		Sparraw.ajaxPost('layer_report/queryWaterFeed.action', params, function(data){
					if (data.ResponseDetail.Result == "Success" && data.ResponseDetail.xAxis.length != 0){
						 $scope.waterFeedData.yName =  [];
						 $scope.waterFeedData.yData =  []; 
						 var ageLeng =[];
						for (var i = 0; i < data.ResponseDetail.yDatas.length; i++) {
							$scope.waterFeedData.yName[i] =  data.ResponseDetail.yDatas[i].HouseName;
							$scope.waterFeedData.yData[i] =  data.ResponseDetail.yDatas[i].HouseDatas;
						}
					    
						var day_age_week  ="周龄";
					    $scope.waterFeedData.xData=data.ResponseDetail.xAxis;
						var Maxage = Math.max.apply(null, data.ResponseDetail.xAxis);
						console.log($scope.waterFeedData.xData);
						
					    if ($scope.waterFeedData.ViewType=="01"){
								
								if(Maxage < 88){
									var j = 88 - Maxage;
									var L = data.ResponseDetail.xAxis.length
									
								   for (var i = 0; i < j; i++) {
									 $scope.waterFeedData.xData[L+i] = Maxage + i + 1;
								   }
								}
						
                                day_age_week  ="周龄";
					    }else{
							var L = data.ResponseDetail.xAxis.length
							if(L < 60){
								var j = 60 - L;
								for (var i = 0; i < j; i++) {
									$scope.waterFeedData.xData[L+i] = Maxage + i + 1;
								}
							}
                            day_age_week  ="日龄"
					    }
						console.log($scope.waterFeedData.xData);
						
						$scope.waterFeedData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						$scope.waterFeedData.selectUnit = '';

						var tempLineConfig = setChartParams(params.ViewType, $scope.waterFeedData.yData,$scope.waterFeedData.yName);
						Echart_initLine02($scope.waterFeedData.xData,
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
		if (type == 'week') {
			document.getElementById('weekBtn').style.background = "#A9A9A9";
			document.getElementById('dayBtn').style.background = "#439AFC";
			$scope.waterFeedData.ViewType = "01";
		}else{
			document.getElementById('weekBtn').style.background = "#439AFC";
			document.getElementById('dayBtn').style.background = "#A9A9A9";
			$scope.waterFeedData.ViewType = "02";
		}
		$scope.changes1();
	}

	setTimeout(function() {
		$scope.chooseType('week');
	}, persistentData.horizontalTime);
	
})