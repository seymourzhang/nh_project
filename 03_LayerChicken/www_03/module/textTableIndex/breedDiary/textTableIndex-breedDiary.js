angular.module('myApp.breedDiary', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
//饲养日记
.controller('breedDiaryCtrl', function($scope, $state) {
	
	Sparraw.intoMyController($scope, $state);
    $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
    document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
    if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
        document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
    }else{
        document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
    }


    setLandscape(true,true);
    $scope.initData = function(){
        $scope.brDiaryData = {
            "FarmBreedId"    :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId           ,
            "selectHouse"    :  JSON.stringify($scope.sparraw_user_temp.userinfo.houses[0])  ,
            "selectHouseId"  :  ""     ,
            "selectWeek"     :  "0"    ,
            "WeekAgeBegin"   :  ""     ,
            "WeekAgeEnd"     :  ""     ,
            "DateInfos"      :  (new Date()).Format("yyyy-MM-dd")     ,//varchar型，日期信息
            "tableData"      : ""
        }

        $scope.brDiaryData.selectHouseId = JSON.parse($scope.brDiaryData.selectHouse).HouseId;
        $scope.chooseHouse($scope.brDiaryData.selectHouseId);
    };



    $scope.inquire = function(){
        var params = {
                "FarmBreedId":$scope.brDiaryData.FarmBreedId    ,
                "HouseId":$scope.brDiaryData.selectHouseId      ,
                "ViewType":"01"                                 ,
                "WeekAgeBegin":$scope.brDiaryData.WeekAgeBegin  ,
                "WeekAgeEnd":$scope.brDiaryData.WeekAgeEnd
        };
        
        Sparraw.ajaxPost('layer_dataInput/queryRemark.action', params, function(data){
            if (data.ResponseDetail.Result == "Success") {
                console.log(data.ResponseDetail);
                $scope.brDiaryData.tableData = data.ResponseDetail.weekData;
                $scope.GetTable();
            }else if (data.ResponseDetail.Result == "Fail"){
                Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
            }else{
                Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
            };      
        });
    }




    $scope.GetTable = function(){
        var toDayTitleName = ["周龄","日龄","内容"];
        var toDayTableKey = ["week_age","growth_dayage","Remark"];
        var toDayheader = [];
        for (var i = 0; i < 3; i++) {
                if (i  == 0) {
                    toDayheader.push({
                        'name'                :  toDayTableKey[i],
                        'width'               :  '60',
                        'headerCellTemplate'  :  '<div style="width:60px;height:30px; background:#FFF;border-left:solid 0.5px #ECECEC;border-right:solid 0.5px #AEAEAE; "><p class="middle" style="position: relative; top:5px;">' + toDayTitleName[i] + '</p></div>',
                        'cellTemplate'        :  '',
                        'headerCellClass'     :  '',
                        'enableCellEdit'      :  false,
                    })
                }else if (i  == 1) {
                    toDayheader.push({
                        'name'                :  toDayTableKey[i],
                        'width'               :  '60',
                        'headerCellTemplate'  :  '<div style="width:60px;height:30px; background:#FFF;border-left:solid 0.5px #ECECEC;border-right:solid 0.5px #AEAEAE; "><p class="middle" style="position: relative; top:5px;">' + toDayTitleName[i] + '</p></div>',
                        'cellTemplate'        :  '',
                        'headerCellClass'     :  '',
                        'enableCellEdit'      :  false,
                    })
                }else{
                    toDayheader.push({
                        'name'                :  toDayTableKey[i],
                        'width'               :  '640',
                        'headerCellTemplate'  :  '<div style="width:640px;height:30px; background:#FFF;border-left:solid 0.5px #ECECEC;border-right:solid 0.5px #AEAEAE; "><p class="middle" style="position: relative; top:5px;">' + toDayTitleName[i] + '</p></div>',
                        'cellTemplate'        :  '',
                        'headerCellClass'     :  '',
                        'enableCellEdit'      :  false,
                    })
                }
        }

        var showTableData = {
            'header' : toDayheader,
            'firstFixed': true, //首列是否固定ture-固定，false-不固定
            'rowHeight' : 25,//内容高度
            'TableData' : $scope.brDiaryData.tableData
        }

        $scope.gridOptions = {
            rowHeight: showTableData.rowHeight,
            columnDefs:[]
        };

        for (var i = 0; i < showTableData.header.length; i++) {
            if (i == 0) {
                $scope.gridOptions.columnDefs.push({ 
                                name                :   showTableData.header[i].name                ,  
                                displayName         :   showTableData.header[i].displayName         , 
                                width               :   showTableData.header[i].width               ,
                                headerCellClass     :   showTableData.header[i].headerCellClass     ,
                                headerCellTemplate  :   showTableData.header[i].headerCellTemplate  ,
                                cellTemplate        :   showTableData.header[i].cellTemplate        ,
                                enableCellEdit      :   showTableData.header[i].enableCellEdit      ,//cell是否可以编辑
                                enableColumnMenu    :   false                                       ,
                                pinnedLeft          :   true                                        ,
                                cellClass           :   function(grid, row, col, rowRenderIndex, colRenderIndex) {
                                                            if (row) {
                                                                return 'middle';
                                                            }
                                                        }
                });
            }else if (i == 1) {
                $scope.gridOptions.columnDefs.push({ 
                                name                :   showTableData.header[i].name                ,  
                                displayName         :   showTableData.header[i].displayName         , 
                                width               :   showTableData.header[i].width               ,
                                headerCellClass     :   showTableData.header[i].headerCellClass     ,
                                headerCellTemplate  :   showTableData.header[i].headerCellTemplate  ,
                                cellTemplate        :   showTableData.header[i].cellTemplate        ,
                                enableCellEdit      :   showTableData.header[i].enableCellEdit      ,//cell是否可以编辑
                                enableColumnMenu    :   false                                       ,
                                pinnedLeft          :   true                                        ,
                                cellClass           :   function(grid, row, col, rowRenderIndex, colRenderIndex) {
                                                            if (row) {
                                                                return 'middle';
                                                            }
                                                        }
                });
            }else{
                $scope.gridOptions.columnDefs.push({ 
                                    name                :  showTableData.header[i].name                ,  
                                    displayName         :  showTableData.header[i].displayName         , 
                                    width               :  showTableData.header[i].width               ,
                                    headerCellClass     :  showTableData.header[i].headerCellClass     ,
                                    headerCellTemplate  :  showTableData.header[i].headerCellTemplate  ,
                                    cellTemplate        :  showTableData.header[i].cellTemplate        ,
                                    enableCellEdit      :  showTableData.header[i].enableCellEdit      ,//cell是否可以编辑
                                    enableColumnMenu    :  false                                       
                });
            }
        }

        $scope.gridOptions.data = $scope.brDiaryData.tableData;

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
        switch($scope.brDiaryData.selectWeek){
            case "0":
                $scope.brDiaryData.WeekAgeBegin = 0;
                $scope.brDiaryData.WeekAgeEnd = 0;
              break;
            case "1":
                $scope.brDiaryData.WeekAgeBegin = 1;
                $scope.brDiaryData.WeekAgeEnd = 10;
              break;
            case "2":
                $scope.brDiaryData.WeekAgeBegin = 10;
                $scope.brDiaryData.WeekAgeEnd = 20;
              break;
            case "3":
                $scope.brDiaryData.WeekAgeBegin = 20;
                $scope.brDiaryData.WeekAgeEnd = 30;
              break;
            case "4":
                $scope.brDiaryData.WeekAgeBegin = 30;
                $scope.brDiaryData.WeekAgeEnd = 40;
              break;
            case "5":
                $scope.brDiaryData.WeekAgeBegin = 40;
                $scope.brDiaryData.WeekAgeEnd = 50;
              break;
            case "6":
                $scope.brDiaryData.WeekAgeBegin = 50;
                $scope.brDiaryData.WeekAgeEnd = 60;
              break;
            default:
                $scope.brDiaryData.WeekAgeBegin = 60;
                $scope.brDiaryData.WeekAgeEnd = 70;
        }
        $scope.inquire();
    }





    $scope.chooseHouse = function(item){
        for (var i = 0; i < $scope.sparraw_user_temp.houseinfos.length; i++) {
            document.getElementById("IDis"+$scope.sparraw_user_temp.houseinfos[i].id).style.background = "#439AFC";
        }
        document.getElementById("IDis"+item).style.background = "#E3E3E3";
        $scope.brDiaryData.selectHouseId = item;
        $scope.brDiaryData.WeekAgeBegin = 0;
        $scope.brDiaryData.WeekAgeEnd = 0;
        $scope.judgeWeek();
    }


    $scope.gridOptions = {};
    $scope.gridOptions.columnDefs = []; 

    setTimeout(function() {
    	$scope.initData();
    }, persistentData.horizontalTime);
})