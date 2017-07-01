angular.module('myApp.weight', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//体重
.controller('weightCtrl', function($scope, $state) {
	
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    setLandscape(true,true);

    document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
	if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
	}else{
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
	}
	document.getElementById('main').style.height = (persistentData.analysisChartH + 10 )+ '%';

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
						var tempLineConfig = setChartParams(params.ViewType, $scope.chickenWeightData.yData,$scope.chickenWeightData.yName);

						Echart_initLine02($scope.chickenWeightData.xData,
										  tempLineConfig,
										  '单位：kg',
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
  	
	  
	setTimeout(function() {
		$scope.changes1();
	}, persistentData.horizontalTime);
})