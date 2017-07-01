angular.module('myApp.warnStatistics', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])

.controller('warnStatisticsCtrl', function($scope, $state) {
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));

    $scope.setScreenStateFun = function(){
		setLandscape(true,true);//横屏
		document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
		if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
			document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
		}else{
			document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
		}
		setTimeout(function() {
			$scope.initData();
		}, 500);
	};

	$scope.initData = function(){
		var myChart = echarts.init(document.getElementById('main'));

		$scope.warnStatData = {
			"xShaftSumData":"",
			"displayConfig":[],
			"yName":[],
			"yData":[],
			"needSelected":[true,true,true,true,true],
			"yStack":['总量','总量','总量','总量','总量'],
			"yColor":['#C6A87B','#FDA233','#439AFC','#BB27DD','#CE0A24'],
		};

	    $scope.chooseHouse($scope.sparraw_user_temp.houseinfos[0].id);
	}

	$scope.getAlarmStatic = function(selectedHouse){

		var params = {
			"FarmBreedId"  :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId ,
			"HouseId"      :  selectedHouse                        
		};

		Sparraw.ajaxPost('rep/alarm/alarmStatic.action', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
				$scope.warnStatData = {
					"xShaftSumData":"",
					"displayConfig":[],
					"yName":[],
					"yData":[],
					"needSelected":[true,true,true,true,true],
					"yStack":['总量','总量','总量','总量','总量'],
					"yColor":['#C6A87B','#FDA233','#439AFC','#BB27DD','#CE0A24'],
				};
				$scope.warnStatData.xShaftSumData = data.ResponseDetail.xAxis;
				for (var i = 0; i < data.ResponseDetail.alarmDatas.length; i++) {
					$scope.warnStatData.yName.push(data.ResponseDetail.alarmDatas[i].name);
					$scope.warnStatData.yData.push(data.ResponseDetail.alarmDatas[i].data);
				}

				for (var i = 0; i < data.ResponseDetail.alarmDatas.length; i++) {
					$scope.warnStatData.displayConfig.push({
			    		"yName"         :  $scope.warnStatData.yName[i]         ,
			    		"yData"         :  $scope.warnStatData.yData[i]         ,
			    		"yType"         :  "bar"                                ,
			    		"yStack"         :  $scope.warnStatData.yStack[i]         ,
			    		"yColor"        :  $scope.warnStatData.yColor[i]        ,
			    		"needSelected"  :  $scope.warnStatData.needSelected[i]
			    	})
				}

				setTimeout(function() {
					Echart_initLine02(
			    	$scope.warnStatData.xShaftSumData,
			    	$scope.warnStatData.displayConfig,
			    	"次数",
					null,
					false,
					"",
					null,
					null,
					"日龄");
				}, 500);
			}else if (data.ResponseDetail.Result == "Fail") {
				Echart_initLine02(['0'],[{"yName":"0","yData":['0']}]);
				Sparraw.myNotice("暂无信息");
			}else{
				Echart_initLine02(['0'],[{"yName":"0","yData":['0']}]);
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		});
	}


	$scope.chooseHouse = function(item){
		console.log(item);
		if (item == 'all') {
			$scope.getAlarmStatic(0);
			for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
				document.getElementById("IDis"+$scope.sparraw_user_temp.houseinfos[i].id).style.background = "#3DCB64";
			}
			document.getElementById("allButton").style.background = "#A9A9A9";
		}else{
			for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
				document.getElementById("IDis"+$scope.sparraw_user_temp.houseinfos[i].id).style.background = "#3DCB64";
			}
			document.getElementById("IDis"+item).style.background = "#A9A9A9";
			document.getElementById("allButton").style.background = "#3DCB64";
			$scope.getAlarmStatic(item);
		}

		
	}



	$scope.setScreenStateFun();
})