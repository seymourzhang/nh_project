angular.module('myApp.onlyChickEggsDemo', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//只鸡产蛋曲线Demo
.controller("onlyChickEggsCtrlDemo",function($scope, $state,$ionicLoading, $http, $ionicSlideBoxDelegate, $stateParams, $ionicSideMenuDelegate, crisisServiceFactory, AppData) {
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
		document.getElementById('onlyChickEggs_DIV').style.height = DIVHEIGHT + 'px';
	}else if(navigator.userAgent.indexOf('Firefox') >= 0) {
		//火狐浏览器获取高度
		var MAXHEIGHT = document.documentElement.clientHeight;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('onlyChickEggs_DIV').style.height = DIVHEIGHT + 'px';
	}else {
		//计算出手机屏幕高度
		var MAXHEIGHT = document.body.clientWidth;
		//将屏幕高度赋给div
		var DIVHEIGHT = MAXHEIGHT - 75;
		document.getElementById('onlyChickEggs_DIV').style.height = DIVHEIGHT + 'px';
	}

	$scope.onlyChickEggsData = {
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
     	$scope.onlyChickEggsData.xData=[];
     	
 		var params = { 
 			'IsNeedDelay':'Y',
        	'ViewType' : $scope.onlyChickEggsData.ViewType,
        	'FarmBreedId' :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId,
        	"FarmId":$scope.sparraw_user_temp.farminfo.id       	
        }
        console.log(params);
        if(params.ViewType==null){
                params.ViewType='01';
		}
		
		var data = {"ResponseStatus":1,"ResponseDetail":{"yDatas":[{"HouseName":"全场平均","HouseDatas":[0,0,0.8,4.7,10.6,17.2,23.9,30.6,37.3,44,50.7,57.4,64.1,70.8,77.5,84.2,90.8,97.5,104.1,110.7,117.4,124,130.6,137.2,143.8,150.4,156.9,163.4,169.8,176.1,182.4,188.7,195,201.3,207.6,213.8,0,0,0,0,0,0.1],"HouseId":"01"},{"HouseName":"01","HouseDatas":[0,0,0.8,4.8,10.8,17.3,24,30.7,37.4,44.1,50.8,57.5,64.2,70.9,77.5,84.2,90.8,97.5,104.1,110.7,117.3,123.9,130.5,137.1,143.7,150.3,156.8,163.3,169.7,176,182.3,188.6,194.9,201.2,207.4,213.7,0,0,0,0,0,0.3],"HouseId":"102"},{"HouseName":"02","HouseDatas":[0,0,0.8,4.8,10.8,17.3,24,30.7,37.4,44.1,50.8,57.5,64.2,70.9,77.5,84.2,90.8,97.5,104.1,110.7,117.4,124,130.6,137.2,143.8,150.3,156.9,163.3,169.6,175.9,182.2,188.5,194.8,201.1,207.4,213.6,0,0,0,0,0,0],"HouseId":"103"},{"HouseName":"03","HouseDatas":[0,0,0.7,4.4,10.3,17,23.7,30.4,37.1,43.9,50.6,57.3,64,70.7,77.4,84.1,90.8,97.4,104.1,110.8,117.4,124.1,130.7,137.3,143.9,150.5,157.1,163.6,170,176.3,182.7,189,195.3,201.7,207.9,214.2,0,0,0,0,0,0],"HouseId":"104"}],"ViewType":"01","Result":"Success","FarmId":26,"FarmBreedId":82,"xAxis":[17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58]}};
		
		var standChickenLayEggs = {"19":0.7,"20":3.9,"21":8.4,"22":14.0,"23":20.2,"24":26.6,"25":33.0,"26":39.6,"27":46.1,"28":52.6,"29":59.2,"30":65.8,"31":72.4,"32":78.9,"33":85.5,"34":92.0,"35":98.5,"36":105.0,"37":111.4,"38":117.9,"39":124.3,"40":130.7,"41":137.1,"42":143.4,"43":149.7,"44":156.0,"45":162.3,"46":168.5,"47":174.7,"48":180.9,"49":187.0,"50":193.1,"51":199.2,"52":205.2,"53":211.2,"54":217.2,"55":223.1,"56":229.0,"57":234.8,"58":240.6,"59":246.4,"60":252.1,"61":257.8,"62":263.4,"63":269.0,"64":274.5,"65":280.0,"66":285.5,"67":290.9,"68":296.3,"69":301.6,"70":306.9,"71":312.1,"72":317.3,"73":322.4,"74":327.5,"75":332.6,"76":337.6,"77":342.5,"78":347.4,"79":352.2,"80":357.0};
		
		Sparraw.ajaxPost('layer_report/queryChickenEggs.action', params, function(data){
					if (data.ResponseDetail.Result == "Success" && data.ResponseDetail.xAxis.length != 0){
						 $scope.onlyChickEggsData.yName =  [];
						 $scope.onlyChickEggsData.yData =  []; 
						 var ageLeng =[];
						for (var i = 0; i < data.ResponseDetail.yDatas.length; i++) {
							$scope.onlyChickEggsData.yName[i] =  data.ResponseDetail.yDatas[i].HouseName;
							$scope.onlyChickEggsData.yData[i] =  data.ResponseDetail.yDatas[i].HouseDatas;
						}
						
						
					    var day_age_week  ="周龄";
					    $scope.onlyChickEggsData.xData=data.ResponseDetail.xAxis;
						var Maxage = Math.max.apply(null, data.ResponseDetail.xAxis);
						console.log($scope.onlyChickEggsData.xData);
						
					    if ($scope.onlyChickEggsData.ViewType=="01"){
								
								if(Maxage < 88){
									var j = 88 - Maxage;
									var L = data.ResponseDetail.xAxis.length
									
								   for (var i = 0; i < j; i++) {
									 $scope.onlyChickEggsData.xData[L+i] = Maxage + i + 1;
								   }
								}
						
                                day_age_week  ="周龄";
								
								/**/
								var temp = [];// 22 57 
								var fromIndex = 0;
								var endIndex = 0;
								var standData = [];
								for(var i = 0 ;i < $scope.onlyChickEggsData.xData.length; i++ ){
									var x = $scope.onlyChickEggsData.xData[i];
									if(x == 22){
										fromIndex = i;
									}
									
									if(x == 57){
										endIndex = i;
									}
									
									if(x >= 22 && x <= 57){
										temp.push(x);
										var val = standChickenLayEggs[x].toFixed(1);
										standData.push(val);
									}
								}
								console.log("standData:"+standData)
								$scope.onlyChickEggsData.xData = temp;
								
								$scope.onlyChickEggsData.yName = [];
								$scope.onlyChickEggsData.yData = [];
								$scope.onlyChickEggsData.yName.push("标准");
								$scope.onlyChickEggsData.yData.push(standData);
								
								for (var i = 0; i < data.ResponseDetail.yDatas.length; i++) {
									$scope.onlyChickEggsData.yName.push(data.ResponseDetail.yDatas[i].HouseName);
									
									var temp2 = [];
									
									for(j=fromIndex; j<endIndex; j++ ){
										var val = data.ResponseDetail.yDatas[i].HouseDatas[j];
										if(val > 0){
											temp2.push(val)
										}else{
											break;
										}
										
									}
									//$scope.onlyChickEggsData.yData[i+1] = temp2;
									$scope.onlyChickEggsData.yData.push(temp2);
								}
								
								
								
								/**/
					    }else{
							var L = data.ResponseDetail.xAxis.length
							if(L < 60){
								var j = 60 - L;
								for (var i = 0; i < j; i++) {
									$scope.onlyChickEggsData.xData[L+i] = Maxage + i + 1;
								}
							}
                            day_age_week  ="日龄";
							/**/
							for (var i = 0; i < data.ResponseDetail.yDatas.length; i++) {
									$scope.onlyChickEggsData.yName[i] =  data.ResponseDetail.yDatas[i].HouseName;
									$scope.onlyChickEggsData.yData[i] =  data.ResponseDetail.yDatas[i].HouseDatas;
									var temp2 = [];
									
									for(j=0; j< data.ResponseDetail.yDatas[i].HouseDatas.length; j++ ){
										var val = data.ResponseDetail.yDatas[i].HouseDatas[j];
										if(val > 0){
											temp2.push(val)
										}else{
											break;
										}
										
									}
									$scope.onlyChickEggsData.yData[i] = temp2;
									
							}
							/**/
					    }
						console.log($scope.onlyChickEggsData.xData);
						
						
						
						
						$scope.onlyChickEggsData.yColor = ['#FF7F50','#87CEFA','#DA70D6','#32CD32','#6495ED','#FF69B4'];
						$scope.onlyChickEggsData.selectUnit = '';
						Echart_initLine01($scope.onlyChickEggsData.xData,
											 $scope.onlyChickEggsData.yData,
											 $scope.onlyChickEggsData.yName,
											 $scope.onlyChickEggsData.yColor,
											 $scope.onlyChickEggsData.hiddenPara,
											 $scope.onlyChickEggsData.selectUnit,
											 '枚/只',
											 function (params){
						                        var res = day_age_week + ' :' + params[0].name;
									            for (var i = 0, l = params.length; i < l; i++) {
									                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value +'';
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