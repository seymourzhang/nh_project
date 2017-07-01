angular.module('myApp.ingestionAndWater', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//采食饮水
.controller('ingestionAndWaterCtrl', function($scope, $state) {
	
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
    $scope.switchBtn = false;

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

						var tempLineConfig = setChartParams(params.ViewType, $scope.cullDeathRateData.yData,$scope.cullDeathRateData.yName);
						Echart_initLine02($scope.cullDeathRateData.xData,
										  tempLineConfig,
										  yName,
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
		if (type == 'ingestion') {
			document.getElementById('ingestionBtn').style.background = "#A9A9A9";
			document.getElementById('waterBtn').style.background = "#439AFC";
			params.FeedWaterFlag='Feed';
		}else{
			document.getElementById('ingestionBtn').style.background = "#439AFC";
			document.getElementById('waterBtn').style.background = "#A9A9A9";
			params.FeedWaterFlag='Water';
		}
		$scope.changes1();
	}

	setTimeout(function() {
		$scope.chooseType('ingestion');
	}, persistentData.horizontalTime);

	
})