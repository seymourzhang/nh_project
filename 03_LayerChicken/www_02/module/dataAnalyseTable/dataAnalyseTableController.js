angular.module('myApp.dataAnalyseTable', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
// 数据分析
.controller("dataAnalyseTableCtrl",function($scope, $state, $ionicLoading, $http, $ionicPopup, $stateParams, AppData) {
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    
    setPortrait(true,true);
    

    $scope.pointDevelop = function(reportId) {
    	if(reportId == 'tempChart'){//温度曲线
    		biz_common_judgeRegistInfo($ionicPopup,$state,"tempChart");
    	}else if(reportId == 'reportTempHumi'){//温湿度综合
    		biz_common_judgeRegistInfo($ionicPopup,$state,"reportTempHumi");
    	}else if(reportId == 'eggWeigLayRate'){//产蛋率曲线
    		biz_common_judgeRegistInfo($ionicPopup,$state,"eggWeigLayRate");
    	}else if(reportId == 'eggWeigLay'){//蛋重曲线
    		biz_common_judgeRegistInfo($ionicPopup,$state,"eggWeigLay");
    	}else if(reportId == 'onlyChickEggs'){//只鸡产蛋曲线
    		biz_common_judgeRegistInfo($ionicPopup,$state,"onlyChickEggs");
    	}else if(reportId == 'onlyChickEggsDemo'){//只鸡产蛋曲线
    		biz_common_judgeRegistInfo($ionicPopup,$state,"onlyChickEggsDemo");
    	}else if(reportId == 'cullDeathRate'){//死淘率曲线
    		biz_common_judgeRegistInfo($ionicPopup,$state,"cullDeathRate");
    	}else if(reportId == 'reportFeedWaterRate'){//采食饮水曲线
    		biz_common_judgeRegistInfo($ionicPopup,$state,"reportFeedWaterRate");
    	}else if(reportId == 'reportEggWeightFeed'){//料蛋比曲线
    		biz_common_judgeRegistInfo($ionicPopup,$state,"reportEggWeightFeed");
    	}else if (reportId == 'waterFeed') {//水料比曲线
    		biz_common_judgeRegistInfo($ionicPopup,$state,"waterFeed");
    	}else if (reportId == 'chickenWeightAnalyze'){//体重曲线
			biz_common_judgeRegistInfo($ionicPopup,$state,"chickenWeightAnalyze");
		}else if (reportId == 'salesAnalyze'){//销售分析曲线
			biz_common_judgeRegistInfo($ionicPopup,$state,"salesAnalyze");
		}else if (reportId == 'coluCurve'){//销售分析曲线
			biz_common_judgeRegistInfo($ionicPopup,$state,"coluCurve");
		}else{//建设中
    		biz_common_pointDevelop();
    	}
	};
})