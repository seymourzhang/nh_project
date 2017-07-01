angular.module('myApp.productPerformStander', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//生产性能标准
.controller("productPerformStanderCtrl", function($scope, $state,$ionicLoading, $http, AppData){
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));




	$scope.gobatchManage = function(){
		$state.go("batchManage");
	}
	$scope.prodQuotData = {
        "Headers"      :  [],
        "TableDatas"   :  [],
    }

    
	$scope.drawTable = function(){
    	
    	var comKey = ["WeekAge", "day_lay_rate", "acc_cur_lay", "acc_ori_lay", "acc_cd_rate", "chicken_weight", "day_feed", "acc_ori_lay_weight", "lay_weight"];
    	/*for (var i = 1; i <= $scope.prodQuotData.Headers.length; i++) {
    		if (i != 1) {
    			$scope.gridOptions.columnDefs.push({name:comKey[i-1],  displayName: $scope.prodQuotData.Headers[i-1], width:100 ,enableColumnMenu: false});
    		}else{
    			$scope.gridOptions.columnDefs.push({name:comKey[i-1],  displayName: $scope.prodQuotData.Headers[i-1], width:100 ,enableColumnMenu: false,pinnedLeft:true});
    		};
    	};*/
    	var headerCellTem = "<div style='color:black;text-align:center;position:relative; left:0px; top:15px;'><p>" + $scope.prodQuotData.Headers[0] + "</p></div>";
    	$scope.gridOptions.columnDefs.push({name:comKey[0], width:45 ,enableColumnMenu: false,pinnedLeft:true, headerCellTemplate:headerCellTem,
    		cellClass: function(newBatchDiv, row, col, rowRenderIndex, colRenderIndex) {
        		return 'cellEditStyle';
        	},
       	 	headerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        		return 'headerEditStyle';
    		}
    	});
    	headerCellTem = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>" + $scope.prodQuotData.Headers[1] + "</p></div>";
    	$scope.gridOptions.columnDefs.push({name:comKey[1], width:80 ,enableColumnMenu: false,headerCellTemplate:headerCellTem,
    		cellClass: function(newBatchDiv, row, col, rowRenderIndex, colRenderIndex) {
        		return 'cellEditStyle';
        	},
       	 	headerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        		return 'headerEditStyle';
    		}
    	});
    	headerCellTem = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>" + $scope.prodQuotData.Headers[2].substring(0,4)+"<br>"+ $scope.prodQuotData.Headers[2].substring(4) + "</p></div>";
    	$scope.gridOptions.columnDefs.push({name:comKey[2], width:100 ,enableColumnMenu: false,headerCellTemplate:headerCellTem,
    		cellClass: function(newBatchDiv, row, col, rowRenderIndex, colRenderIndex) {
        		return 'cellEditStyle';
        	},
       	 	headerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        		return 'headerEditStyle';
    		},
    		renderer: function(value, p, record){
				return '<div style="white-space:normal;">' + value + '</div>';
			}
    	});
    	headerCellTem = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>" + $scope.prodQuotData.Headers[3].substring(0,4)+"<br>"+ $scope.prodQuotData.Headers[3].substring(4) + "</p></div>";
    	$scope.gridOptions.columnDefs.push({name:comKey[3], width:100 ,enableColumnMenu: false,headerCellTemplate:headerCellTem,
    		cellClass: function(newBatchDiv, row, col, rowRenderIndex, colRenderIndex) {
        		return 'cellEditStyle';
        	},
       	 	headerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        		return 'headerEditStyle';
    		}
    	});
    	headerCellTem = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>" + $scope.prodQuotData.Headers[4] + "</p></div>";
    	$scope.gridOptions.columnDefs.push({name:comKey[4], width:80, enableColumnMenu: false,headerCellTemplate:headerCellTem,
    		cellClass: function(newBatchDiv, row, col, rowRenderIndex, colRenderIndex) {
        		return 'cellEditStyle';
        	},
       	 	headerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        		return 'headerEditStyle';
    		}
    	});
    	headerCellTem = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>" + $scope.prodQuotData.Headers[5] + "</p></div>";
    	$scope.gridOptions.columnDefs.push({name:comKey[5], width:80 ,enableColumnMenu: false,headerCellTemplate:headerCellTem,
    		cellClass: function(newBatchDiv, row, col, rowRenderIndex, colRenderIndex) {
        		return 'cellEditStyle';
        	},
       	 	headerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        		return 'headerEditStyle';
    		}
    	});
    	headerCellTem = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>" + $scope.prodQuotData.Headers[6] + "</p></div>";
    	$scope.gridOptions.columnDefs.push({name:comKey[6], width:80 ,enableColumnMenu: false,headerCellTemplate:headerCellTem,
    		cellClass: function(newBatchDiv, row, col, rowRenderIndex, colRenderIndex) {
        		return 'cellEditStyle';
        	},
       	 	headerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        		return 'headerEditStyle';
    		}
    	});
    	headerCellTem = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>" + $scope.prodQuotData.Headers[7] + "</p></div>";
    	$scope.gridOptions.columnDefs.push({name:comKey[7], width:110 ,enableColumnMenu: false,headerCellTemplate:headerCellTem,
    		cellClass: function(newBatchDiv, row, col, rowRenderIndex, colRenderIndex) {
        		return 'cellEditStyle';
        	},
       	 	headerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        		return 'headerEditStyle';
    		}
    	});
    	headerCellTem = "<div style='color:black;text-align:center;position:relative; left:0px; top:5px;'><p>" + $scope.prodQuotData.Headers[8] + "</p></div>";
    	$scope.gridOptions.columnDefs.push({name:comKey[8], width:80 ,enableColumnMenu: false,headerCellTemplate:headerCellTem,
    		cellClass: function(newBatchDiv, row, col, rowRenderIndex, colRenderIndex) {
        		return 'cellEditStyle';
        	},
       	 	headerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
        		return 'headerEditStyle';
    		}
    	});
    	$scope.gridOptions.enableSorting = false;
		$scope.gridOptions.data = $scope.prodQuotData.TableDatas;
		window.onresize = function(){};
    }
    $scope.getTableData = function(){
    	var params = {'IsNeedDelay':'Y'};
		Sparraw.ajaxPost('layer_breedBatch/queryProStandard.action', params, function(data){
			if (data.ResponseDetail.Result == "Success") {
	            $scope.prodQuotData.Headers = data.ResponseDetail.Headers;
    			$scope.prodQuotData.TableDatas = data.ResponseDetail.TableDatas;
				$scope.drawTable();
			}else if (data.ResponseDetail.Result == "Fail") {
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			}else{
				Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
			};
		});
    }
    


    setLandscape(false,true);
    $scope.gridOptions = {};
	$scope.gridOptions.columnDefs = [];
	$scope.getTableData();



})
