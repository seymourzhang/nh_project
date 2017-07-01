angular.module('myApp.daily', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//日报
.controller('dailyCtrl', function($scope, $state) {
	
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    setLandscape(true,true);

    document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
	if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
	}else{
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
	}

    $scope.gridOptions = {};
  	$scope.gridOptions.columnDefs = []; 

    if(Common_isIOS()){
        document.getElementById("dailyDayTable").style.height = "85%";
        document.getElementById("houseTable").style.top = "85%";
    }else{
        document.getElementById("dailyDayTable").style.height = "76%";
        document.getElementById("houseTable").style.top = "76%";
    }

    

    $scope.initData = function(){
    	$scope.prodDayData = {
	    	"FarmBreedId"    :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId           ,
	    	"selectHouse"    :  JSON.stringify($scope.sparraw_user_temp.userinfo.houses[0])  ,
	    	"selectHouseId"  :  ""     ,
	    	"selectWeek"     :  "0"    ,
	    	"WeekAgeBegin"   :  "0"    ,
	    	"WeekAgeEnd"     :  "0"    ,
	    	"DateInfos"      :  ""     ,//varchar型，日期信息
	    	"PlaceNum"       :  ""     ,//int型，入舍数量
	    	"CurGrowthAge"   :  ""     ,//当前生长日龄
	    	"CurLayerAge"    :  ""     ,//int型，当前产蛋日龄
	    	"PlaceDate"      :  ""     ,//varchar型，入舍日
	    	"weekData"       :  ""     ,
	    	"original_amount":  ""     ,
	    	"cur_amount"     :  ""     ,
	    	"survRate"       :  ""
	    }
	    $scope.prodDayData.selectHouseId = JSON.parse($scope.prodDayData.selectHouse).HouseId;
	    $scope.chooseHouse($scope.prodDayData.selectHouseId);
    };

    $scope.inquire = function(){
      	var params = {
        	"IsNeedDelay":'Y',
        	"FarmBreedId":$scope.prodDayData.FarmBreedId,
        	"HouseId":$scope.prodDayData.selectHouseId,
        	"WeekAgeBegin":$scope.prodDayData.WeekAgeBegin,
        	"WeekAgeEnd":$scope.prodDayData.WeekAgeEnd
    	};
    	Sparraw.ajaxPost('layer_dataInput/DailyReport.action', params, function(data){
        	if (data.ResponseDetail.Result == "Success") {
          		$scope.prodDayData.DateInfos    = data.ResponseDetail.DateInfos    ;
          		$scope.prodDayData.CurGrowthAge = data.ResponseDetail.CurGrowthAge ;
          		$scope.prodDayData.PlaceDate    = data.ResponseDetail.PlaceDate    ;
          		$scope.prodDayData.PlaceNum     = data.ResponseDetail.PlaceNum     ;
          		$scope.prodDayData.weekData     = data.ResponseDetail.weekData     ;
        		$scope.prodDayData.PlaceNum     = data.ResponseDetail.PlaceNum     ;
        		$scope.prodDayData.CurGrowthAge = data.ResponseDetail.CurGrowthAge ;
        		$scope.prodDayData.CurLayerAge  = data.ResponseDetail.CurLayerAge  ;
        		$scope.prodDayData.PlaceDate    = data.ResponseDetail.PlaceDate    ;
        		$scope.prodDayData.CurLayerAge  = data.ResponseDetail.CurLayerAge  ;
        		var oDate  = new Date();
        		var oMonth = ("0" + (oDate.getMonth() + 1)).slice(-2);
        		var oDay   = ("0" + (oDate.getDate())).slice(-2);
        		var NowDate = oDate.getFullYear() + "-" + oMonth + "-" + oDay;
        		$scope.prodDayData.DateInfos = NowDate;
        		if (data.ResponseDetail.weekData.length == 0) {
        		    Sparraw.myNotice("所选数据暂无信息。");
        		}else{
                    $scope.prodDayData.original_amount = $scope.prodDayData.PlaceNum;
                    $scope.prodDayData.cur_amount = $scope.prodDayData.weekData[$scope.prodDayData.weekData.length-1].curAmount;
                }
	
       			if ($scope.prodDayData.original_amount == $scope.prodDayData.cur_amount) {
        		  	$scope.prodDayData.survRate = 100;
        		}else{
        		  	$scope.prodDayData.survRate = (parseFloat($scope.prodDayData.cur_amount / $scope.prodDayData.original_amount)*100).toFixed(2);
        		  	if (!Common_isNum($scope.prodDayData.survRate) || !isFinite($scope.prodDayData.survRate)) {
        		    	$scope.prodDayData.survRate = 0;
        		  	}
        		}
	
        		$scope.GetTable();
      		}else if (data.ResponseDetail.Result == "Fail"){
        		Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
                $scope.prodDayData.weekData = [];
      		}else{
        		Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
                $scope.prodDayData.weekData = [];
      		};      
    	});
    }




    $scope.GetTable = function(){
	    var showTableData = {
	      	'rowHeight' : ""    ,//内容高度
	      	'header'    : []    ,
	      	'TableData' : []    ,
	      	'firstFixed': true
	    };
    	var header = [];

    	var headName = ['生长<br>日龄',
                    	'',
                        '',
            			'鸡存<br>栏数',
            			'',
            			'',
            			'',
            			'',
            			'',
            			'饲料<br>公斤',
            			'饮水<br>立方',
            			'',
            			'体重<br>公斤'];
    	var otherHeadName = ['',
                            '周<br>龄',
               				'死淘<br>数',
               				'',
               				'',
               				'',
               				'',
               				'',
               				'破损枚',
               				'',
               				'',
               				'水料比',
               				''];
    	var headerDiv = [];
    	var otherHeaderDiv = [];


    	var TableHeadName = [   "growth_age"       ,
                                "week_age"         ,
								"culling_all"      ,
								"curAmount"        ,
								"curLayNum"        ,
								"curLayRate"       ,
								"curLaySumWeight"  ,
								"curLayAvgWeight"  ,
								"curBrokenNum"     ,
								"daily_feed"       ,
								"daily_water"      ,
								"rOfWM"            ,
								"chickenWeight"];



    	for (var i = 0; i < headName.length; i++) {
    	  	headerDiv.push("<div style='color:black;text-align:center;position:relative; left:0px; top:0px;height:40px;'><p>" + headName[i] + "</p></div>");
    	}

    	for (var i = 0; i < otherHeadName.length; i++) {
    	  	if(i == 1 || i == 2){
    	  	  	otherHeaderDiv.push("<div style='color:black;text-align:center;position:relative; left:0px; top:0px;height:40px;'><p>" + otherHeadName[i] + "</p></div>");
    	  	}else {
    	  	  	otherHeaderDiv.push("<div style='color:black;text-align:center;position:relative; left:0px; top:8px;height:40px;'><p>" + otherHeadName[i] + "</p></div>");
    	  	}
    	}
    	var templateTabel = ['','','','','<div style="width:120px;height:40px;"><div style="position:relative; left:0px; top:0px; width:120px;height:20px; background:rgba(251, 251, 251, 1)"><p style="text-align:center;">' + "产蛋" + '</p></div><div style="width:60px;height:20px;"><div style="width:60px;height:20px;"><p style="text-align:center;">' + "枚" + '</p></div><div style="width:60px;height:20px; position:relative;left:60px; top:-20px;"><p style="text-align:center;"> ' + "%" + '</p></div></div></div>','','<div style="width:120px;height:40px;"><div style="width:120px;height:20px; background:rgba(251, 251, 251, 1)"><p style="text-align:center;">' + "蛋重量" + '</p></div><div style="width:60px;height:20px;"><div style="width:60px;height:20px;"><p style="text-align:center;">' + "公斤" + '</p></div><div style="width:60px;height:20px; position:relative;left:60px; top:-20px;"><p style="text-align:center;"> ' + "克/枚" + '</p></div></div></div>',];
    	for (var i = 0; i < TableHeadName.length; i++) {
		      if(i == 3 || i == 12){//2-鸡存栏数 11-体重公斤
		        header.push({
		          'name'                :  TableHeadName[i],
		          'width'               :  '60',
		          'displayName'         :  '',
		          'headerCellTemplate'  :  headerDiv[i],
		          'cellTemplate'        :  '',
		          'headerCellClass'     :  '',
		          'cellClass'           :  'middle'
		        })
		      }else if (i == 0 || i == 9 || i == 10) {//0-生长日龄 8-饲料公斤 9-饮水立方
		        if(i == 0) {
		          header.push({
		            'name': TableHeadName[i],
		            'width': '40',
		            'displayName': '',
		            'headerCellTemplate': headerDiv[i],
		            'cellTemplate': '',
		            'headerCellClass': '',
		            'cellClass': 'middle'
		          })
		        }else{
		          header.push({
		            'name': TableHeadName[i],
		            'width': '80',
		            'displayName': '',
		            'headerCellTemplate': headerDiv[i],
		            'cellTemplate': '',
		            'headerCellClass': '',
		            'cellClass': 'middle'
		          })
		        }
		      }else{
		        if (i == 4 || i == 5 || i == 6 || i == 7) {//3-4-产蛋 5-6-蛋重量
		          if (i == 4 || i == 6) {
		            header.push({
		              'name'                :  TableHeadName[i],
		              'width'               :  '60',
		              'displayName'         :  '',
		              'headerCellTemplate'  :  templateTabel[i],
		              'cellTemplate'        :  '',
		              'headerCellClass'     :  '',
		              'cellClass'           :  'middle' 
		            })
		          }else{
		            header.push({
		              'name'                :  TableHeadName[i],
		              'width'               :  '60',
		              'displayName'         :  '',
		              'headerCellTemplate'  :  '',
		              'cellTemplate'        :  '',
		              'headerCellClass'     :  '',
		              'cellClass'           :  'middle' 
		            })
		          }
		        }else{
		          if(i == 1 || i == 2){//死淘数
		            header.push({
		            'name'                :  TableHeadName[i],
		            'width'               :  '40',
		            'displayName'         :  '',
		            'headerCellTemplate'  :  otherHeaderDiv[i],
		            'cellTemplate'        :  '',
		            'headerCellClass'     :  '',
		            'cellClass'           :  'middle'})
		          }else{
		            header.push({
		              'name': TableHeadName[i],
		              'width': '60',
		              'displayName': '',
		              'headerCellTemplate': otherHeaderDiv[i],
		              'cellTemplate': '',
		              'headerCellClass': '',
		              'cellClass': 'middle'
		            })
		          }
		        }
		      }
    	}

    	showTableData.header = header;
    	showTableData.TableData = $scope.prodDayData.weekData;
    	$scope.gridOptions = {
    	    rowHeight: showTableData.rowHeight,
    	};
    	$scope.gridOptions.columnDefs = [];

    	for (var i = 0; i < showTableData.header.length; i++) {
    	    if (i == 0 && showTableData.firstFixed == true) {
    	        $scope.gridOptions.columnDefs.push({
    	            name: showTableData.header[i].name,
    	            displayName: showTableData.header[i].displayName,
    	            width: showTableData.header[i].width,
    	            headerCellClass: showTableData.header[i].headerCellClass,
    	            cellClass: showTableData.header[i].cellClass,
    	            headerCellTemplate: showTableData.header[i].headerCellTemplate,
    	            cellTemplate: showTableData.header[i].cellTemplate,
    	            pinnedLeft: true,
    	            enableColumnMenu: false
    	        });
    	    } else {
    	        $scope.gridOptions.columnDefs.push({
    	            name: showTableData.header[i].name,
    	            displayName: showTableData.header[i].displayName,
    	            width: showTableData.header[i].width,
    	            headerCellClass: showTableData.header[i].headerCellClass,
    	            cellClass: showTableData.header[i].cellClass,
    	            headerCellTemplate: showTableData.header[i].headerCellTemplate,
    	            cellTemplate: showTableData.header[i].cellTemplate,
    	            enableColumnMenu: false
    	        });
	
    	    };
    	}
    	$scope.gridOptions.data = showTableData.TableData;
        console.log("--------------------------");
        console.log($scope.gridOptions.data);
        console.log("--------------------------");
    	//判断哪些数据进行过修改
    	$scope.gridOptions.onRegisterApi = function(gridApi) {
    	    $scope.gridApi = gridApi;
    	    //input获取焦点的时候
    	    gridApi.edit.on.beginCellEdit($scope,
    	    function(rowEntity, colDef, newValue, oldValue) {
	
    	    });
    	    //input失去焦点时调用
    	    //gridApi.edit.on.afterCellEdit($scope,afterCellEdit);
    	};
	
    	window.onresize = function(){};
    };

    $scope.judgeWeek = function(){
      switch($scope.prodDayData.selectWeek){
            case "0":
                $scope.prodDayData.WeekAgeBegin = 0;
                $scope.prodDayData.WeekAgeEnd = 0;
              break;
            case "1":
                $scope.prodDayData.WeekAgeBegin = 1;
                $scope.prodDayData.WeekAgeEnd = 10;
              break;
            case "2":
                $scope.prodDayData.WeekAgeBegin = 10;
                $scope.prodDayData.WeekAgeEnd = 20;
              break;
            case "3":
                $scope.prodDayData.WeekAgeBegin = 20;
                $scope.prodDayData.WeekAgeEnd = 30;
              break;
            case "4":
                $scope.prodDayData.WeekAgeBegin = 30;
                $scope.prodDayData.WeekAgeEnd = 40;
              break;
            case "5":
                $scope.prodDayData.WeekAgeBegin = 40;
                $scope.prodDayData.WeekAgeEnd = 50;
              break;
            case "6":
                $scope.prodDayData.WeekAgeBegin = 50;
                $scope.prodDayData.WeekAgeEnd = 60;
              break;
            default:
                $scope.prodDayData.WeekAgeBegin = 60;
                $scope.prodDayData.WeekAgeEnd = 70;
        }


        $scope.inquire();
    };


    $scope.chooseHouse = function(item){
		for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
			document.getElementById("IDis"+$scope.sparraw_user_temp.houseinfos[i].id).style.background = "#439AFC";
		}
		document.getElementById("IDis"+item).style.background = "#E3E3E3";
		$scope.prodDayData.selectHouseId = item;
		$scope.inquire();
	}



    setTimeout(function() {
    	$scope.initData();
    }, persistentData.horizontalTime);
})