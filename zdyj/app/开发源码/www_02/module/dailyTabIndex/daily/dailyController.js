angular.module('myApp.daily', 
        ['ionic','ngCordova','ngTouch',
         'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
         ])
//日报
.controller("dailyCtrl",function($scope, $state, $http, $stateParams, $ionicPopup, AppData) {
	Sparraw.intoMyController($scope, $state);
	$scope.sparraw_user_temp = JSON.parse(JSON.stringify(sparraw_user));
	
	
	document.getElementById('mainContent').style.top = persistentData.tabHeight + 'px';
	if (DeviInfo.ScreenHeight > DeviInfo.ScreenWidth) {
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenWidth - persistentData.tabHeight) + 'px';
	}else{
		document.getElementById('mainContent').style.height = (DeviInfo.ScreenHeight - persistentData.tabHeight) + 'px';
	}



	
    
    $scope.prodDayData = {
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

        var headName = ["日龄",
                        "存栏",
                        "公死淘",
                        "母死淘",
                        "只耗料g",
                        "只饮水ml",
                        "公体重g",
                        "母体重g",
                        "产蛋枚",
                        "产蛋率"];

        var headerDiv = [];
        var TableHeadName = ["index1",
                   "index2",
                   "index3",
                   "index4",
                   "index5",
                   "index6",
                   "index7",
                   "index8",
                   "index9",
                   "index10"];



        for (var i = 0; i < headName.length; i++) {
          headerDiv.push("<div style='color:black;text-align:center;position:relative; left:0px; top:0px;height:20px;'><p>" + headName[i] + "</p></div>");
        }


        var index1MaxLength = [];
        var index2MaxLength = [];
        var index3MaxLength = [];
        var index4MaxLength = [];
        var index5MaxLength = [];
        var index6MaxLength = [];
        var index7MaxLength = [];
        var index8MaxLength = [];
        var index9MaxLength = [];
        var index10MaxLength = [];

        for (var i = 0; i < $scope.prodDayData.weekData.length; i++) {
          index1MaxLength.push(String($scope.prodDayData.weekData[i].index1).length);
          index2MaxLength.push(String($scope.prodDayData.weekData[i].index2).length);
          index3MaxLength.push(String($scope.prodDayData.weekData[i].index3).length);
          index4MaxLength.push(String($scope.prodDayData.weekData[i].index4).length);
          index5MaxLength.push(String($scope.prodDayData.weekData[i].index5).length);
          index6MaxLength.push(String($scope.prodDayData.weekData[i].index6).length);
          index7MaxLength.push(String($scope.prodDayData.weekData[i].index7).length);
          index8MaxLength.push(String($scope.prodDayData.weekData[i].index8).length);
          index9MaxLength.push(String($scope.prodDayData.weekData[i].index9).length);
          index10MaxLength.push(String($scope.prodDayData.weekData[i].index10).length);
        }

        //获取数据内最大的长度
        Array.prototype.max = function() { 
          var max = this[0];
          var len = this.length; 
          for (var i = 1; i < len; i++){ 
            if (this[i] > max) { 
              max = this[i]; 
            } 
          } 
          return max;
        }
        var allDataLength = [];


        if (headName[0].length < index1MaxLength.max()) {
          allDataLength.push(index1MaxLength.max());
        }else{
          allDataLength.push(headName[0].length);
        }

        if (headName[1].length < index2MaxLength.max()) {
          allDataLength.push(index2MaxLength.max());
        }else{
          allDataLength.push(headName[1].length);
        }

        if (headName[2].length < index3MaxLength.max()) {
          allDataLength.push(index3MaxLength.max());
        }else{
          allDataLength.push(headName[2].length);
        }

        if (headName[3].length < index4MaxLength.max()) {
          allDataLength.push(index4MaxLength.max());
        }else{
          allDataLength.push(headName[3].length);
        }

        if (headName[4].length < index5MaxLength.max()) {
          allDataLength.push(index5MaxLength.max());
        }else{
          allDataLength.push(headName[4].length);
        }

        if (headName[5].length < index6MaxLength.max()) {
          allDataLength.push(index6MaxLength.max());
        }else{
          allDataLength.push(headName[5].length);
        }

        if (headName[6].length < index7MaxLength.max()) {
          allDataLength.push(index7MaxLength.max());
        }else{
          allDataLength.push(headName[6].length);
        }

        if (headName[7].length < index8MaxLength.max()) {
          allDataLength.push(index8MaxLength.max());
        }else{
          allDataLength.push(headName[7].length);
        }

        if (headName[8].length < index9MaxLength.max()) {
          allDataLength.push(index9MaxLength.max());
        }else{
          allDataLength.push(headName[8].length);
        }

        if (headName[9].length < index10MaxLength.max()) {
          allDataLength.push(index10MaxLength.max());
        }else{
          allDataLength.push(headName[9].length);
        }


        

        var headerWidth = [];
        for (var i = 0; i < TableHeadName.length; i++) {
          if (allDataLength[i] > 6) {
            headerWidth.push('90');
          }else if (allDataLength[i] > 5) {
            headerWidth.push('70');
          }else{
            headerWidth.push('60');
          }
        }
        for (var i = 0; i < TableHeadName.length; i++) {
          header.push({
              'name'                :  TableHeadName[i]  ,
              'width'               :  headerWidth[i]    ,
              'displayName'         :  ''                ,
              'headerCellTemplate'  :  headerDiv[i]      ,
              'cellTemplate'        :  ''                ,
              'headerCellClass'     :  ''                ,
              'cellClass'           :  'middle'
          })
        }

        showTableData.header = header;
        showTableData.TableData = $scope.prodDayData.weekData;
        
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
      $scope.prodDayData.WeekAgeBegin = $scope.prodDayData.selectWeek  ;
      $scope.prodDayData.WeekAgeEnd   = $scope.prodDayData.selectWeek  ;

      var params = {};
      if ($scope.prodDayData.firstTime) {
          params.HouseBreedId          = $scope.prodDayData.selectHouse.BreedBatchId  ;
          params.HouseId               = $scope.prodDayData.selectHouse.id            ;
          $scope.prodDayData.firstTime = false;
      }else{
          params.HouseBreedId = JSON.parse($scope.prodDayData.selectHouse).BreedBatchId  ;
          params.HouseId      = JSON.parse($scope.prodDayData.selectHouse).id            ;
      }
      console.log(params);

      params.WeekAgeBegin = $scope.prodDayData.WeekAgeBegin  ;
      params.WeekAgeEnd   = $scope.prodDayData.WeekAgeEnd    ;

      Sparraw.ajaxPost('dailyReportMobile/dailyReport', params, function(data){
          if (data.ResponseDetail.Result == "Success") {
            $scope.prodDayData.cur_date = data.ResponseDetail.cur_date;
            $scope.prodDayData.growth_age = data.ResponseDetail.growth_age;
            $scope.prodDayData.layer_age = data.ResponseDetail.layer_age;
            $scope.prodDayData.survival_rate = data.ResponseDetail.survival_rate;
            //清空再填入数据
            $scope.prodDayData.weekData = [];
            for (var i = 0; i < data.ResponseDetail.dataInfo.length; i++) {
              $scope.prodDayData.weekData.push({
                  "index1":data.ResponseDetail.dataInfo[i].day_age             ,
                  "index2":data.ResponseDetail.dataInfo[i].cur_amount          ,
                  "index3":data.ResponseDetail.dataInfo[i].male_cd_num         ,
                  "index4":data.ResponseDetail.dataInfo[i].female_cd_num       ,
                  "index5":data.ResponseDetail.dataInfo[i].intake_sig          ,
                  "index6":data.ResponseDetail.dataInfo[i].water_sig           ,
                  "index7":data.ResponseDetail.dataInfo[i].male_body_weight    ,
                  "index8":data.ResponseDetail.dataInfo[i].female_body_weight  ,
                  "index9":data.ResponseDetail.dataInfo[i].layer_num           ,
                  "index10":data.ResponseDetail.dataInfo[i].layer_rate
              })
            }
            $scope.GetTable();
          }else if (data.ResponseDetail.Result == "Fail"){
            Sparraw.myNotice(data.ResponseDetail.Error);
            //清空
            $scope.prodDayData.weekData = [];
            for (var i = 0; i < 10; i++) {
              $scope.prodDayData.weekData.push({
                  "index1":"",
                  "index2":"",
                  "index3":"",
                  "index4":"",
                  "index5":"",
                  "index6":"",
                  "index7":"",
                  "index8":"",
                  "index9":"",
                  "index10":""
              })
            }
            $scope.GetTable();
          }else{
            Sparraw.myNotice(data.ResponseDetail.Error);
          };      
      });
    }

    $scope.judgeWeek = function(){
      $scope.prodDayData.firstTime = true;
      $scope.inquire();
    }

    $scope.judgeHouse = function(){
      $scope.inquire();
    }
    

    setLandscape(true,true);
    $scope.gridOptions = {};
    $scope.gridOptions.columnDefs = [];

    setTimeout(function() {
      $scope.judgeHouse();
    }, persistentData.horizontalTime);
    

})