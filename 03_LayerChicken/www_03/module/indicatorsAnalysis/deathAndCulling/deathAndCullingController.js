angular.module('myApp.deathAndCulling', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//死淘率
.controller('deathAndCullingCtrl', function($scope, $state) {
	
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
							tail = "单位：‰";
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
							tail = "单位：％";
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


						var tempLineConfig = setChartParams(params.ViewType, $scope.cullDeathRateData.yData,$scope.cullDeathRateData.yName);
						Echart_initLine02($scope.cullDeathRateData.xData,
										  tempLineConfig,
										  tail,
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
	 
    $scope.chooseType = function(btnType){
		if (btnType == 'cumulative') {
			document.getElementById('cumulativeBtn').style.background = "#A9A9A9";
			document.getElementById('weekBtn').style.background = "#439AFC";
			yLeftRange = [0,10];
			type = "01";
		}else{
			document.getElementById('cumulativeBtn').style.background = "#439AFC";
			document.getElementById('weekBtn').style.background = "#A9A9A9";
			yLeftRange = null;
			type = "03";
		}
		$scope.changes1();
		
	}

	setTimeout(function() {
		$scope.chooseType('cumulative');
	}, persistentData.horizontalTime);
	
})