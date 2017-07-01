angular.module('myApp.productionDaily', 
    ['ionic','ngCordova','ngTouch',
     'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
     ])
//生产日报
.controller("productionDailyCtrl",function($scope, $state, $http, $ionicLoading, $stateParams, $ionicPopup,$ionicSideMenuDelegate, AppData) {
  Sparraw.intoMyController($scope, $state);
  $scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));


    $scope.goDailyTable = function(){
    if($stateParams.fromPage == "dailyReport"){
      $state.go("dailyReport");
    }else{// dailyReport font-weight: bold;
      $state.go("dailyTable");
    }
      
    }

    $scope.prodDayData = {
    "FarmBreedId"    :  $scope.sparraw_user_temp.farminfo.farmBreedBatchId           ,
    "selectHouse"    :  JSON.stringify($scope.sparraw_user_temp.userinfo.houses[0])  ,
    "selectHouseId"  :  ""     ,
    "selectWeek"     :  "0"    ,
    "WeekAgeBegin"   :  ""     ,
    "WeekAgeEnd"     :  ""     ,
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


    var TableHeadName = ["growth_age"       ,
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
      if(i == 1){
        otherHeaderDiv.push("<div style='color:black;text-align:center;position:relative; left:0px; top:0px;height:40px;'><p>" + otherHeadName[i] + "</p></div>");
      }else {
        otherHeaderDiv.push("<div style='color:black;text-align:center;position:relative; left:0px; top:8px;height:40px;'><p>" + otherHeadName[i] + "</p></div>");
      }
    }
    var templateTabel = ['','','','<div style="width:120px;height:40px;"><div style="position:relative; left:0px; top:0px; width:120px;height:20px; background:rgba(251, 251, 251, 1)"><p style="text-align:center;">' + "产蛋" + '</p></div><div style="width:60px;height:20px;"><div style="width:60px;height:20px;"><p style="text-align:center;">' + "枚" + '</p></div><div style="width:60px;height:20px; position:relative;left:60px; top:-20px;"><p style="text-align:center;"> ' + "%" + '</p></div></div></div>','','<div style="width:120px;height:40px;"><div style="width:120px;height:20px; background:rgba(251, 251, 251, 1)"><p style="text-align:center;">' + "蛋重量" + '</p></div><div style="width:60px;height:20px;"><div style="width:60px;height:20px;"><p style="text-align:center;">' + "公斤" + '</p></div><div style="width:60px;height:20px; position:relative;left:60px; top:-20px;"><p style="text-align:center;"> ' + "克/枚" + '</p></div></div></div>',];
    for (var i = 0; i < TableHeadName.length; i++) {
      if(i == 2 || i == 11){
        header.push({
          'name'                :  TableHeadName[i],
          'width'               :  '60',
          'displayName'         :  '',
          'headerCellTemplate'  :  headerDiv[i],
          'cellTemplate'        :  '',
          'headerCellClass'     :  '',
          'cellClass'           :  'middle'
        })
      }else if (i == 0 || i == 8 || i == 9) {
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
        if (i == 3 || i == 4 || i == 5 || i == 6) {
          if (i == 3 || i == 5) {
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
          if(i == 1){
            header.push({
            'name'                :  TableHeadName[i],
            'width'               :  '40',
            'displayName'         :  '',
            'headerCellTemplate'  :  otherHeaderDiv[i],
            'cellTemplate'        :  '',
            'headerCellClass'     :  '',
            'cellClass'           :  'middle'})
          }else {
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
    }
    //$scope.GetTable();


    $scope.inquire = function(){

      $scope.prodDayData.WeekAgeBegin = $scope.prodDayData.selectWeek;
      $scope.prodDayData.WeekAgeEnd = $scope.prodDayData.selectWeek;
      var params = {
        'IsNeedDelay':'Y',
        "FarmBreedId":$scope.prodDayData.FarmBreedId,
        "HouseId":$scope.prodDayData.selectHouseId,
        "WeekAgeBegin":$scope.prodDayData.WeekAgeBegin,
        "WeekAgeEnd":$scope.prodDayData.WeekAgeEnd
    };
    console.log(params);
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

          }


          $scope.prodDayData.original_amount = $scope.prodDayData.PlaceNum;
          $scope.prodDayData.cur_amount = $scope.prodDayData.weekData[$scope.prodDayData.weekData.length-1].curAmount;


          if ($scope.prodDayData.original_amount == $scope.prodDayData.cur_amount) {
          $scope.prodDayData.survRate = 100;
        }else{
          $scope.prodDayData.survRate = (parseFloat($scope.prodDayData.cur_amount / $scope.prodDayData.original_amount)*100).toFixed(2);
          if (!Common_isNum($scope.prodDayData.survRate) || !isFinite($scope.prodDayData.survRate)) {
            $scope.prodDayData.survRate = 0;
          }else{

          }
        }



          $scope.GetTable();
      }else if (data.ResponseDetail.Result == "Fail"){
        Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
      }else{
        Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
      };      
    });
    }

    $scope.judgeWeek = function(){
      console.log($scope.prodDayData.selectWeek);
      $scope.inquire();
    }

    $scope.judgeHouse = function(){
      console.log($scope.prodDayData.selectHouseId);
      $scope.inquire();
    }
    

    setLandscape(false,true);
    $scope.gridOptions = {};
  $scope.gridOptions.columnDefs = [];

    $scope.GetTable();
  $scope.judgeHouse();



})

