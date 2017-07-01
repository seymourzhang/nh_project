angular.module('myApp.weekly', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
//周报
.controller("weeklyCtrl",function($scope, $state, $http, $stateParams, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	console.log("---------------_________");
	document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
	if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
	}else{
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
	}

	$scope.weeklyData = {
        "selectHouse"    :  $scope.sparraw_user_temp.houseinfos[0]  ,
        "firstTime"      :  true   ,
        "selectWeek"     :  "0"    ,
        "WeekAgeBegin"   :  ""     ,
        "WeekAgeEnd"     :  ""     ,
        "weekData"       :  []     ,
        "cur_date"       :  ""     ,//今天日期
        "growth_age"     :  ""     ,//生长日龄
        "layer_age"      :  ""     ,//产蛋日龄
        "survival_rate"  :  ""     ,  //成活率
    } 

    $scope.GetTable = function(){
        var showTableData = {
          'rowHeight' : ""    ,//内容高度
          'header'    : []    ,
          'TableData' : []    ,
          'firstFixed': true
        };
        var header = [];

        var headName = ["index1",
                        "index2",
                        "index3",
                        "index4",
                        "index5",
                        "index6",
                        "index7",
                        "index8",
                        "index9",
                        "index10",
                        "index11",
                        "index12",
                        "index13",
                        "index14"];

        var headerDiv = [];

        var Level1header = ["",
                        "死淘",
                        "",
                        "存栏数",
                        "",
                        "耗料",
                        "",
                        "饮水",
                        "",
                        "产蛋",
                        "",
                        "均重g",
                        "",
                        "",
                        ""];

        var Level2header = ["生长周龄",
                        "只数",
                        "%",
                        "公",
                        "母",
                        "累计kg",
                        "g/只/天",
                        "累计(升)",
                        "ml/只/天",
                        "枚",
                        "产蛋率",
                        "公",
                        "母",
                        "均匀度"];


        var TableHeadName = ["index1",
                   "index2",
                   "index3",
                   "index4",
                   "index5",
                   "index6",
                   "index7",
                   "index8",
                   "index9",
                   "index10",
                   "index11",
                   "index12",
                   "index13",
                   "index14"];


          

        for (var i = 0; i < headName.length; i++) {
          if (i == 0 || i == 13) {
            headerDiv.push("<div style='color:black;text-align:center;position:relative; left:0px; width:70px; top:0px;height:20px;'><p style='position:relative;top:10px;'>" + Level2header[i] + "</p></div>");
          }else if (i % 2 != 0) {
            headerDiv.push("<div style='position:relative;left:0px; width:140px;height:40px;'><div style='position:relative;left:0px; width:140px;height:20px;background:rgba(255,255,255, 1);'><p style='text-align:center;'>" + Level1header[i] + "</p></div><div style='position:absolute;left:0px; top:20px; width:70px;height:20px;'><p style='text-align:center;'>" + Level2header[i] + "</p></div><div style='position:absolute;left:70px; top:20px; width:70px;height:20px;'><p style='text-align:center;'>" + Level2header[i+1] + "</p></div></div>");
          }else{
            headerDiv.push("");
          }
        }
        

        for (var i = 0; i < TableHeadName.length; i++) {
          header.push({
              'name'                :  TableHeadName[i]  ,
              'width'               :  70                ,
              'displayName'         :  ''                ,
              'headerCellTemplate'  :  headerDiv[i]      ,
              'cellTemplate'        :  ''                ,
              'headerCellClass'     :  ''                ,
              'cellClass'           :  'middle'
          })
        }


        showTableData.header = header;
        showTableData.TableData = $scope.weeklyData.weekData;
        
        $scope.gridOptions = {
          rowHeight: showTableData.rowHeight,
        };
        $scope.gridOptions.columnDefs = [];

        

        for (var i = 0; i < showTableData.header.length; i++) {
          if (i == 0  && showTableData.firstFixed == true) {
            $scope.gridOptions.columnDefs.push({ 
                              name                :  showTableData.header[i].name                ,  
                              displayName         :  showTableData.header[i].displayName         , 
                              width               :  showTableData.header[i].width               ,
                              headerCellClass     :  showTableData.header[i].headerCellClass     ,
                              cellClass           :  showTableData.header[i].cellClass           ,
                              headerCellTemplate  :  showTableData.header[i].headerCellTemplate  ,
                              cellTemplate        :  showTableData.header[i].cellTemplate        ,
                              pinnedLeft          :  true                                        ,
                              enableColumnMenu    :  false});
          }else if (i == 13) {
            $scope.gridOptions.columnDefs.push({ 
                              name                :  showTableData.header[i].name                ,  
                              displayName         :  showTableData.header[i].displayName         , 
                              width               :  showTableData.header[i].width               ,
                              headerCellClass     :  showTableData.header[i].headerCellClass     ,
                              cellClass           :  showTableData.header[i].cellClass           ,
                              headerCellTemplate  :  showTableData.header[i].headerCellTemplate  ,
                              cellTemplate        :  showTableData.header[i].cellTemplate        ,
                              enableColumnMenu    :  false});
          }else{
            $scope.gridOptions.columnDefs.push({ 
                              name                :  showTableData.header[i].name                ,  
                              displayName         :  showTableData.header[i].displayName         , 
                              width               :  showTableData.header[i].width               ,
                              headerCellClass     :  showTableData.header[i].headerCellClass     ,
                              cellClass           :  showTableData.header[i].cellClass           ,
                              headerCellTemplate  :  showTableData.header[i].headerCellTemplate  ,
                              cellTemplate        :  showTableData.header[i].cellTemplate        ,
                              enableColumnMenu    :  false});

          };
        }
        $scope.gridOptions.data = showTableData.TableData;
        //判断哪些数据进行过修改
        $scope.gridOptions.onRegisterApi = function(gridApi){
        //$scope.gridApi = gridApi;
        //input获取焦点的时候
        /*gridApi.edit.on.beginCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){

        });*/
        //input失去焦点时调用
        //gridApi.edit.on.afterCellEdit($scope,afterCellEdit);
        };

        window.onresize = function(){};
    }

    $scope.inquire = function(){
      $scope.weeklyData.WeekAgeBegin = $scope.weeklyData.selectWeek  ;
      $scope.weeklyData.WeekAgeEnd   = $scope.weeklyData.selectWeek  ;

      var params = {};
      if ($scope.weeklyData.firstTime) {
          params.HouseBreedId          = $scope.weeklyData.selectHouse.BreedBatchId  ;
          params.HouseId               = $scope.weeklyData.selectHouse.id            ;
          $scope.weeklyData.firstTime = false;
      }else{
          params.HouseBreedId = JSON.parse($scope.weeklyData.selectHouse).BreedBatchId  ;
          params.HouseId      = JSON.parse($scope.weeklyData.selectHouse).id            ;
      }
      console.log(params);

      // params.WeekAgeBegin = $scope.weeklyData.WeekAgeBegin  ;
      // params.WeekAgeEnd   = $scope.weeklyData.WeekAgeEnd    ;

      Sparraw.ajaxPost('dailyReportMobile/weeklyReport', params, function(data){
          if (data.ResponseDetail.Result == "Success") {
            $scope.weeklyData.cur_date = data.ResponseDetail.cur_date;
            $scope.weeklyData.growth_week_age = data.ResponseDetail.growth_week_age;
            $scope.weeklyData.layer_week_age = data.ResponseDetail.layer_week_age;
            $scope.weeklyData.survival_rate = data.ResponseDetail.survival_rate;
            //清空再填入数据
            $scope.weeklyData.weekData = [];
            for (var i = 0; i < data.ResponseDetail.dataInfo.length; i++) {
              console.log(data.ResponseDetail.dataInfo[i].week_layer_rate);
              $scope.weeklyData.weekData.push({
                  "index1":data.ResponseDetail.dataInfo[i].growth_week_age     ,
                  "index2":data.ResponseDetail.dataInfo[i].week_cd_num         ,
                  "index3":data.ResponseDetail.dataInfo[i].week_cd_rate        ,
                  "index4":data.ResponseDetail.dataInfo[i].male_week_amount    ,
                  "index5":data.ResponseDetail.dataInfo[i].female_week_amount  ,
                  "index6":data.ResponseDetail.dataInfo[i].intake_acc          ,
                  "index7":data.ResponseDetail.dataInfo[i].intake_sig          ,
                  "index8":data.ResponseDetail.dataInfo[i].water_week          ,
                  "index9":data.ResponseDetail.dataInfo[i].water_sig           ,
                  "index10":data.ResponseDetail.dataInfo[i].week_layer_num     ,
                  "index11":data.ResponseDetail.dataInfo[i].week_layer_rate    ,
                  "index12":data.ResponseDetail.dataInfo[i].male_body_weight   ,
                  "index13":data.ResponseDetail.dataInfo[i].female_body_weight ,
                  "index14":data.ResponseDetail.dataInfo[i].uniformity
              })
            }
            $scope.GetTable();
          }else if (data.ResponseDetail.Result == "Fail"){
            Sparraw.myNotice(data.ResponseDetail.Error);
            //清空
            $scope.weeklyData.weekData = [];
            for (var i = 0; i < 14; i++) {
              $scope.weeklyData.weekData.push({
                  "index1":"",
                  "index2":"",
                  "index3":"",
                  "index4":"",
                  "index5":"",
                  "index6":"",
                  "index7":"",
                  "index8":"",
                  "index9":"",
                  "index10":"",
                  "index11":"",
                  "index12":"",
                  "index13":"",
                  "index14":""
              })
            }
            $scope.GetTable();
          }else{
            Sparraw.myNotice(data.ResponseDetail.Error);
          };      
      });
    }

    $scope.judgeWeek = function(){
      $scope.weeklyData.firstTime = true;
      $scope.inquire();
      //$scope.GetTable();
    }

    $scope.judgeHouse = function(){
      $scope.inquire();
      //$scope.GetTable();
    }
    

    setLandscape(true,true);
    $scope.gridOptions = {};
    $scope.gridOptions.columnDefs = [];

    $scope.judgeHouse();



})