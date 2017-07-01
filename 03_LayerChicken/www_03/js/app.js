
// Ionic Starter App

angular.module('myApp', ['ionic','myApp.controllers','myApp.services','ionic-datepicker','ngCordova','ngTouch', 'ui.grid', 'ui.grid.pinning','ui.grid.edit', 'ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection', 'ui.grid.resizeColumns','ui.grid.autoResize',
                         'myApp.home','myApp.modifyUserInfo','myApp.landingProtocol','myApp.landingPage','myApp.forgotPassword','myApp.disclaimer','myApp.userList',
                         'myApp.infoSafeguard','myApp.useRegistered','myApp.farmRegistered','myApp.buildingTable','myApp.updateBuildingInfo','myApp.addbuilding','myApp.employeesTable','myApp.updateEmployeesInfo','myApp.addEmployees',
                         'myApp.about','myApp.contactUs','myApp.functionIntroduce','myApp.feedBack',
                         'myApp.envMonitoringTable','myApp.envMonitoring','myApp.alarmLogDelay','myApp.tempChart','myApp.alarmStatistics','myApp.alarmLog','myApp.alarmSettings','myApp.voiceSettings',
                         'myApp.messList','myApp.messDetails','myApp.myView',
                         'myApp.dailyTable','myApp.productionDaily','myApp.dailyReport','myApp.weekly',
                         'myApp.eggSellsReportTable','myApp.eggSellsReport','myApp.eggSellsList','myApp.salesAnalyze',
                         'myApp.dataAnalyseTable','myApp.eggWeigLayRate','myApp.coluCurve','myApp.eggWeigLay','myApp.onlyChickEggs','myApp.onlyChickEggsDemo','myApp.reportFeedWaterRate','myApp.cullDeathRate','myApp.reportEggWeightFeed','myApp.waterFeed','myApp.chickenWeightAnalyze',
                         'myApp.batchManage','myApp.newBatch','myApp.productPerformStander','myApp.historyDataImport',
                         'myApp.docPlaceAffirm','myApp.breedAffirm','myApp.batchClear',
                         'myApp.reportIndex','myApp.daily','myApp.week',
                         'myApp.indicatorsAnalysis','myApp.weight','myApp.waterAndFeed','myApp.ingestionAndWater','myApp.deathAndCulling',
                         'myApp.eggAnalysis','myApp.layingRate','myApp.eggWeight','myApp.feedEgg','myApp.onlyChickenLaying',
                         "myApp.apparentTempCalc","myApp.ventCompute","myApp.ventConclusion",
                         'myApp.envAnalyIndex','myApp.tempHumi','myApp.tempDiff','myApp.shock','myApp.tips',
                         'myApp.warnTabIndex','myApp.envWarnChart','myApp.envWarnChart','myApp.warnStatistics',
                         'myApp.textTableIndex','myApp.breedDiary','myApp.mediRecord'],function($httpProvider) {
  // Use x-www-form-urlencoded Content-Type
  $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
 
  /**
   * The workhorse; converts an object to x-www-form-urlencoded serialization.
   * @param {Object} obj
   * @return {String}
   */ 
  var param = function(obj) {
    var query = '', name, value, fullSubName, subName, subValue, innerObj, i;
      
    for(name in obj) {
      value = obj[name];
        
      if(value instanceof Array) {
        for(i=0; i<value.length; ++i) {
          subValue = value[i];
          fullSubName = name + '[' + i + ']';
          innerObj = {};
          innerObj[fullSubName] = subValue;
          query += param(innerObj) + '&';
        }
      }
      else if(value instanceof Object) {
        for(subName in value) {
          subValue = value[subName];
          fullSubName = name + '[' + subName + ']';
          innerObj = {};
          innerObj[fullSubName] = subValue;
          query += param(innerObj) + '&';
        }
      }
      else if(value !== undefined && value !== null)
        query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
    }
      
    return query.length ? query.substr(0, query.length - 1) : query;
  };
 
  // Override $http service's default transformRequest
  $httpProvider.defaults.transformRequest = [function(data) {
    return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
  }];
})



/*.run(function($ionicPlatform,$rootScope) {
  $ionicPlatform.ready(function() {
    if(window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
    }
    if(window.StatusBar) {
      StatusBar.styleDefault();
    }
  });
})
*/
.run(['$ionicPlatform','$rootScope', '$location', '$timeout', '$ionicHistory','$cordovaToast','$state', 
    function ($ionicPlatform,$rootScope, $location, $timeout, $ionicHistory,$cordovaToast,$state) {
    $ionicPlatform.ready(function () {
      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
      // for form inputs)
      if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
        //cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
        cordova.plugins.Keyboard.hideKeyboardAccessoryBar(false);
      }
      if (window.StatusBar) {
        // org.apache.cordova.statusbar required
        StatusBar.styleLightContent();
      }
      // 设置 设备的 Common_MOBILE_IMEI、Common_MOBILE_UUID、Common_MOBILE_MODELNAME、Common_MOBILE_VERSION、Common_MOBILE_PLATFORM
      Common_setDeviceImei();

      if (Common_judgeDevice()=="iphone") {
        cordova.plugins.Keyboard.disableScroll(true);
      }else{

      };
      // 保持竖屏
      app_lockOrientation('portrait');

      Common_initJpushUI();
});


    
    //主页面显示退出提示框
    $ionicPlatform.registerBackButtonAction(function (e) {

        //判断处于哪个页面时双击退出
        if ($location.path() == '/homeurl' ) {
              app_confirm('是否要退出该用户?','提示',null,function(buttonIndex){
                   if(buttonIndex == 2){
                        biz_common_userLogout();
                        $state.go("landingPage");
                   }
              });
        }else if ($location.path().indexOf('/landingPageurl') == 0 || $location.path() == '/landingProtocolurl') {
              // 按钮两次，退出App
              if ($rootScope.backButtonPressedOnceToExit) {
                  ionic.Platform.exitApp();
              } else {
                  $rootScope.backButtonPressedOnceToExit = true;
                  $cordovaToast.showShortCenter('再按一次退出系统');
                  setTimeout(function () {
                      $rootScope.backButtonPressedOnceToExit = false;
                  }, 1500);
              }
        }else if($location.path() == '/farmRegisteredurl' 
                      || $location.path() == '/buildingTable' 
                      || $location.path() == '/employeesTable'){
              $cordovaToast.showShortCenter('请点击左上角进行返回。');
        }else if($ionicHistory.backView()){
              if($location.path().indexOf('/report') == 0 
                  || $location.path().indexOf('/productionDailyurl') == 0 
                  || $location.path().indexOf('/productPerformStanderurl') == 0 
                  || $location.path().indexOf('/weeklyurl') == 0 
                  ){
                  app_lockOrientation('portrait');
              }
              $ionicHistory.goBack();
        }else {
              // 按钮两次，退出App
              if ($rootScope.backButtonPressedOnceToExit) {
                  ionic.Platform.exitApp();
              } else {
                  $rootScope.backButtonPressedOnceToExit = true;
                  $cordovaToast.showShortCenter('再按一次退出系统');
                  setTimeout(function () {
                      $rootScope.backButtonPressedOnceToExit = false;
                  }, 1500);
              }
        }
        e.preventDefault();
        return false;
    }, 101);
}])

//cache:true-使用缓存 false-不使用缓存
.config(function($stateProvider,$urlRouterProvider,$ionicConfigProvider) {
  //禁止手势返回出现白屏
  $ionicConfigProvider.views.swipeBackEnabled(false);
  $stateProvider

/*------2016.11.01--------*/
/*登陆模块*/
// 登陆协议
.state('landingProtocol', {
      url:'/landingProtocolurl',
      cache:false,
      templateUrl: "module/landing/landingProtocol/landingProtocol.html",
      controller: 'landingProtocolCtrl'
    })
// 用户登录
.state('landingPage', {
      url:'/landingPageurl/:firstTime',
      cache:false,
      templateUrl: "module/landing/landingPage/landingPage.html",
      controller: 'landingPageCtrl'
    })
// 忘记密码
.state('forgotPassword', {
      url:'/forgotPasswordurl',
      templateUrl: "module/landing/forgotPassword/forgotPassword.html",
      controller: 'forgotPasswordCtrl'
    })
// 免责声明
.state('disclaimer', {
      url:'/disclaimerurl',
      templateUrl: "module/landing/disclaimer/disclaimer.html",
      controller: 'disclaimerCtrl'
    })
// 密码修改
.state('modifyUserInfo', {
      url:'/modifyUserInfo/:employeesID',
      cache:false,
      templateUrl: "module/basicInfo/modifyUserInfo/modifyUserInfo.html",
      controller: 'modifyUserInfoCtrl'
    })
//默认用户名列表
.state('userList', {
      url:'/userListurl',
      cache:false,
      templateUrl: "module/landing/userList/userList.html",
      controller: 'userListCtrl'
    })
/*首页*/
.state('home', {
      url:'homeurl',
      cache:false,
      templateUrl: "module/home/home.html",
      controller: 'homeCtrl'
    })
/*信息维护模块*/
// 信息维护
.state('infoSafeguard', {
      url:'/infoSafeguard',
      cache:false,
      templateUrl: "module/basicInfo/infoSafeguard/infoSafeguard.html",
      controller: 'infoSafeguardCtrl'
    })
// 用户注册
.state('useRegistered', {
      url:'/useRegisteredurl',
      templateUrl: "module/basicInfo/userInfo/useRegistered.html",
      controller: 'UseRegisteredCtrl'
    })
// 农场注册
.state('farmRegistered', {
      url:'/farmRegisteredurl',
      cache:false,
      templateUrl: "module/basicInfo/farmInfo/farmRegistered.html",
      controller: 'FarmRegisteredCtrl'
    })
// 栋舍列表
.state('buildingTable', {
      url:'/buildingTable',
      cache:false,
      templateUrl: "module/basicInfo/houseInfo/buildingTable.html",
      controller: 'BuildingTableCtrl'
    })
// 栋舍修改
.state('updateBuildingInfo', {
      url:'/buildingTable/buildingInfo/:buildingID',
      templateUrl: "module/basicInfo/houseInfo/updateBuildingInfo.html",
      controller: 'updateBuildingInfoCtrl'
    })
// 栋舍添加
.state('addbuilding', {
      url:'/addbuildingurl',
      cache:false,
      templateUrl: "module/basicInfo/houseInfo/addbuilding.html",
      controller: 'AddbuildingCtrl'
    })
// 员工列表
.state('employeesTable', {
      url:'/employeesTable',
      cache:false,
      templateUrl: "module/basicInfo/userInfo/employeesTable.html",
      controller: 'EmployeesTableCtrl'
    })
// 员工修改
.state('updateEmployeesInfo', {
      url:'/employeesTable/employeesInfo/:employeesID',
      templateUrl: "module/basicInfo/userInfo/updateEmployeesInfo.html",
      controller: 'updateEmployeesInfoCtrl'
    })
// 员工添加
.state('addEmployees', {
      url:'/addEmployeesurl',
      cache:false,
      templateUrl: "module/basicInfo/userInfo/addEmployees.html",
      controller: 'AddEmployeesCtrl'
    })
/*关于模块*/
// 关于页面
.state('about', {
      url:'/abouturl',
      templateUrl: "module/about/about.html",
      controller: 'aboutCtrl'
    })
//功能介绍
.state('functionIntroduce', {
      url:'/functionIntroduceurl',
      templateUrl: "module/about/functionIntroduce/functionIntroduce.html",
      controller: 'functionIntroduceCtrl'
    })
//联系我们
.state('contactUs', {
      url:'/contactUsurl',
      templateUrl: "module/about/contactUs/contactUs.html",
      controller: 'contactUsCtrl'
    })
//意见反馈
.state('feedBack', {
      url:'/feedBackurl',
      templateUrl: "module/about/feedBack/feedBack.html",
      controller: 'feedBackCtrl'
    })


/*环境监测模块*/
// 环控管理表
.state('envMonitoringTable', {
      url:'/envMonitoringTableurl',
      cache:false,
      templateUrl: "module/envMonitoring/envMonitoringTable/envMonitoringTable.html",
      controller: 'envMonitoringTableCtrl'
    })
// 环境监测
.state('envMonitoring', {
      url:'/envMonitoring',
      cache:false,
      templateUrl: "module/envMonitoring/envMonitoring.html",
      controller: 'envMonitoringCtrl'
    })
//报警延迟处理
.state('alarmLogDelay', {
      url:'/envMonitoring/alarmLogDelay/:receiveHouseId/:dayAge',
      cache:false,
      templateUrl: "module/envMonitoring/alarmLogDelay/alarmLogDelay.html",
      controller: 'alarmLogDelayCtrl'
    })
// 环控温度表
.state('tempChart', {
      url:'/report/tempChart/:receiveHouseId/:area',
      cache:false,
      templateUrl: "module/envMonitoring/tempChart/tempChart.html",
      controller: 'tempChartCtrl'
    })

// 环控报警统计
.state('alarmStatistics', {
      url:'/alarmStatistics',
      templateUrl: "module/envMonitoring/alarmStatistics/alarmStatistics.html",
      cache:false,
      controller: 'alarmStatisticsCtrl'
    })
// 报警日志
.state('alarmLog', {
      url:'/alarmStatistics/alarmLog/:receiveHouseId/:buildingDayAge',
      cache:false,
      templateUrl: "module/envMonitoring/alarmLog/alarmLog.html",
      controller: 'alarmLogCtrl'
    })
// 报警设置
.state('alarmSettings', {
      url:'alarmSettings',
      cache:false,
      templateUrl: "module/envMonitoring/alarmSettings/alarmSettings.html",
      controller: 'alarmSettingsCtrl'
    })
// 语音设置
.state('voiceSettings', {
      url:'voiceSettings',
      cache:false,
      templateUrl: "module/envMonitoring/voiceSettings/voiceSettings.html",
      controller: 'voiceSettingsCtrl'
    })

/*生产报表模块*/
//生产报表
.state('dailyTable', {
      url:'/dailyTableurl',
      cache:false,
      templateUrl: "module/dailyTable/dailyTable.html",
      controller: 'dailyTableCtrl'
    })
// 生产记录
.state('dailyReport', {
      url:'/dailyReporturl/:receiveHouseId',
      cache:false,
      templateUrl: "module/dailyTable/dailyReport/dailyReport.html",
      controller: 'dailyReportCtrl'
    })
// 生产日报
.state('productionDaily', {
      url:'/productionDailyurl/:receiveHouseId/:fromPage',
      cache:false,
      templateUrl: "module/dailyTable/productionDaily/productionDaily.html",
      controller: 'productionDailyCtrl'
    })
//生产周报
.state('weekly', {
      url:'/weeklyurl/:receiveHouseId',
      cache:false,
      templateUrl: "module/dailyTable/weekly/weekly.html",
      controller: 'weeklyCtrl'
    })

/*销售报表模块*/
//销售报表
.state('eggSellsReportTable', {
      url:'/module/eggsells/eggSellsReportTable.html',
      cache:false,
      templateUrl: "module/salesTable/eggSellsReportTable/eggSellsReportTable.html",
      controller: 'eggSellsReportTableCtrl'
    })
//销售记录
.state('eggSellsReport', {
      url:'/module/eggsells/eggSellsReport.html',
      cache:false,
      templateUrl: "module/salesTable/eggSellsReport/eggSellsReport.html",
      controller: 'eggSellsReportCtrl'
    })
//销售日报
.state('eggSellsList', {
      url:'/module/eggsells/eggSellsTable/:fromPage',
      cache:false,
      templateUrl: "module/salesTable/eggSellsList/eggSellsList.html",
      controller: 'eggSellsListCtrl'
    })
//销售分析曲线
.state('salesAnalyze', {
      url:'/report/salesAnalyze/:lastPage',
      cache:false,
      templateUrl: "module/salesTable/salesAnalyze/salesAnalyze.html",
      controller: 'salesAnalyzeCtrl'
    })

/*数据分析模块*/
// 数据分析
.state('dataAnalyseTable', {
      url:'/dataAnalyseTable',
      cache:false,
      templateUrl: "module/dataAnalyseTable/dataAnalyseTable.html",
      controller: 'dataAnalyseTableCtrl'
    })
//产蛋曲线
.state('eggWeigLayRate', {
      url:'/report/eggWeigLayRate',
      cache:false,
      templateUrl: "module/dataAnalyseTable/eggWeigLayRate/eggWeigLayRate.html",
      controller: 'eggWeigLayRateCtrl'
    })
//产蛋率曲线(演示)
.state('coluCurve', {
      url:'/coluCurveurl',
      templateUrl: "module/dataAnalyseTable/coluCurve/coluCurve.html",
      controller: 'coluCurveCtrl'
    })

//蛋重曲线
.state('eggWeigLay', {
      url:'/report/eggWeigLay',
      cache:false,
      templateUrl: "module/dataAnalyseTable/eggWeigLay/eggWeigLay.html",
      controller: 'eggWeigLayCtrl'
    })
//只鸡产蛋曲线
.state('onlyChickEggs', {
      url:'/report/onlyChickEggs',
      cache:false,
      templateUrl: "module/dataAnalyseTable/onlyChickEggs/onlyChickEggs.html",
      controller: 'onlyChickEggsCtrl'
    })
//只鸡产蛋曲线Demo
.state('onlyChickEggsDemo', {
      url:'/report/onlyChickEggsDemo',
      cache:false,
      templateUrl: "module/dataAnalyseTable/onlyChickEggsDemo/onlyChickEggsDemo.html",
      controller: 'onlyChickEggsCtrlDemo'
    })
//死淘(成活率)率图
.state('cullDeathRate', {
      url:'/report/cullDeathRateurl',
      cache:false,
      templateUrl: "module/dataAnalyseTable/cullDeathRate/cullDeathRate.html",
      controller: 'cullDeathRateCtrl'
    })

// 日采食曲饮水曲线
.state('reportFeedWaterRate', {
      url:'/report/reportFeedWaterRate',
      cache:false,
      templateUrl: "module/dataAnalyseTable/reportFeedWaterRate/reportFeedWaterRate.html",
      controller: 'reportFeedWaterCtrl'
    })
//料蛋比曲线 
.state('reportEggWeightFeed', {
      url:'/report/reportEggWeightFeed',
      cache:false,
      templateUrl: "module/dataAnalyseTable/reportEggWeightFeed/reportEggWeightFeed.html",
      controller: 'reportEggWeightFeedCtrl'
    })
//水料比曲线
.state('waterFeed', {
      url:'/report/waterFeed',
      cache:false,
      templateUrl: "module/dataAnalyseTable/waterFeed/waterFeed.html",
      controller: 'waterFeedCtrl'
    })

//体重曲线
.state('chickenWeightAnalyze', {
      url:'/report/chickenWeight/',
      cache:false,
      templateUrl: "module/dataAnalyseTable/chickenWeightAnalyze/chickenWeightAnalyze.html",
      controller: 'chickenWeightAnalyzeCtrl'
    })

/*农场管理模块*/
// 农场管理
.state('batchManage', {
      url:'/batchManageurl',
      cache:false,
      templateUrl: "module/farmManage/batchManage/batchManage.html",
      controller: 'batchManageCtrl'
    })
// 新建批次
.state('newBatch', {
      url:'/newBatchurl',
      cache:false,
      templateUrl: "module/farmManage/newBatch/newBatch.html",
      controller: 'newBatchCtrl'
    })
//生产性能管理
.state('productPerformStander',{
      url:'/productPerformStanderurl',
      cache:false,
      templateUrl:"module/farmManage/productPerformStander/productPerformStander.html",
      controller:'productPerformStanderCtrl'
    })
//历史数据导入
.state('historyDataImport',{
    url:'/historyDataImporturl',
    cache:false,
    templateUrl:"module/farmManage/historyDataImport/historyDataImport.html",
    controller:'historyDataImportCtrl'
    })


//消息列表
.state('messList', {
      url:'/messListurl',
      cache:false,
      templateUrl: "module/home/messList/messList.html",
      controller: 'messListCtrl'
    })
//消息详情
.state('messDetails', {
      url:'/messListurl/messDetailsurl/:Item',
      cache:false,
      templateUrl: "module/home/messDetails/messDetails.html",
      controller: 'messDetailsCtrl'
    })
//我的
.state('myView', {
      url:'/myViewurl',
      cache:false,
      templateUrl: "module/home/myView/myView.html",
      controller: 'myViewCtrl'
    })

//2017年03月23日 新版本
//入雏
.state('docPlaceAffirm', {
  url:'/docPlaceAffirm',
  cache:false,
  templateUrl: "module/docPlaceAffirm/docPlaceAffirm.html",
  controller: 'docPlaceAffirmCtrl'
})
//出栏
.state('breedAffirm', {
  url:'/breedAffirm',
  cache:false,
  templateUrl: "module/breedAffirm/breedAffirm.html",
  controller: 'breedAffirmCtrl'
})
//结算
.state('batchClear', {
      url:'/batchClearurl',
      cache:false,
      templateUrl: "module/batchClear/batchClear.html",
      controller: 'batchClearCtrl'
    })

//报表模块
.state('reportIndex', {
    url: '/reportIndex',
    cache:false,
    templateUrl: 'module/reportIndex/reportIndex.html',
    controller: 'reportIndexCtrl'
  })
//日报
.state('reportIndex.daily', {
    url: '/daily',
    cache:false,
    views: {
      'reportIndex-daily': {
        templateUrl: 'module/reportIndex/daily/reportIndex-daily.html',
        controller: 'dailyCtrl'
      }
    }
  })
//周报
.state('reportIndex.week', {
    url: '/week',
    cache:false,
    views: {
      'reportIndex-week': {
        templateUrl: 'module/reportIndex/week/reportIndex-week.html',
        controller: 'weekCtrl'
      }
    }
  })

//指标分析
.state('indicatorsAnalysis', {
    url: '/indicatorsAnalysis',
    cache:false,
    templateUrl: 'module/indicatorsAnalysis/indicatorsAnalysis.html',
    controller: 'indicatorsAnalysisCtrl'
  })

//体重
.state('indicatorsAnalysis.weight', {
    url: '/weight',
    cache:false,
    views: {
      'indicatorsAnalysis-weight': {
        templateUrl: 'module/indicatorsAnalysis/weight/indicatorsAnalysis-weight.html',
        controller: 'weightCtrl'
      }
    }
  })
//水料比
.state('indicatorsAnalysis.waterAndFeed', {
    url: '/waterAndFeed',
    cache:false,
    views: {
      'indicatorsAnalysis-waterAndFeed': {
        templateUrl: 'module/indicatorsAnalysis/waterAndFeed/indicatorsAnalysis-waterAndFeed.html',
        controller: 'waterAndFeedCtrl'
      }
    }
  })
//采食饮水
.state('indicatorsAnalysis.ingestionAndWater', {
    url: '/ingestionAndWater',
    cache:false,
    views: {
      'indicatorsAnalysis-ingestionAndWater': {
        templateUrl: 'module/indicatorsAnalysis/ingestionAndWater/indicatorsAnalysis-ingestionAndWater.html',
        controller: 'ingestionAndWaterCtrl'
      }
    }
  })
//死淘率
.state('indicatorsAnalysis.deathAndCulling', {
    url: '/deathAndCulling',
    cache:false,
    views: {
      'indicatorsAnalysis-deathAndCulling': {
        templateUrl: 'module/indicatorsAnalysis/deathAndCulling/indicatorsAnalysis-deathAndCulling.html',
        controller: 'deathAndCullingCtrl'
      }
    }
  })


//产蛋分析
.state('eggAnalysis', {
    url: '/eggAnalysis',
    cache:false,
    templateUrl: 'module/eggAnalysis/eggAnalysis.html',
    controller: 'eggAnalysisCtrl'
  })
//产蛋率
.state('eggAnalysis.layingRate', {
    url: '/layingRate',
    cache:false,
    views: {
      'eggAnalysis-layingRate': {
        templateUrl: 'module/eggAnalysis/layingRate/eggAnalysis-layingRate.html',
        controller: 'layingRateCtrl'
      }
    }
  })
//蛋重
.state('eggAnalysis.eggWeight', {
    url: '/eggWeight',
    cache:false,
    views: {
      'eggAnalysis-eggWeight': {
        templateUrl: 'module/eggAnalysis/eggWeight/eggAnalysis-eggWeight.html',
        controller: 'eggWeightCtrl'
      }
    }
  })
//料蛋比
.state('eggAnalysis.feedEgg', {
    url: '/feedEgg',
    cache:false,
    views: {
      'eggAnalysis-feedEgg': {
        templateUrl: 'module/eggAnalysis/feedEgg/eggAnalysis-feedEgg.html',
        controller: 'feedEggCtrl'
      }
    }
  })
//只鸡产蛋率
.state('eggAnalysis.onlyChickenLaying', {
    url: '/onlyChickenLaying',
    cache:false,
    views: {
      'eggAnalysis-onlyChickenLaying': {
        templateUrl: 'module/eggAnalysis/onlyChickenLaying/eggAnalysis-onlyChickenLaying.html',
        controller: 'onlyChickenLayingCtrl'
      }
    }
  })

// 体感温度计算器
.state('apparentTempCalc', {
      url:'/apparentTempCalc',
      cache:false,
      templateUrl: "module/chickenAssists/apparentTempCalc/apparentTempCalc.html",
      controller: 'apparentTempCalcCtrl'
    })
//最小通风计算器
.state('ventCompute', {
      url:'/ventCompute',
      cache:false,
      templateUrl: "module/chickenAssists/ventCompute/ventCompute.html",
      controller: 'ventComputeCtrl'
    })
//最小通风计算器结果
.state('ventConclusion', {
      url:'/ventConclusion',
      cache:false,
      templateUrl: "module/chickenAssists/ventConclusion/ventConclusion.html",
      controller: 'ventConclusionCtrl'
    })

//环控分析
  .state('envAnalyIndex', {
    url: '/envAnalyIndex',
    cache:false,
    templateUrl: 'module/envAnalysis/envAnalyIndex.html',
    controller: 'envAnalyIndexCtrl'
  })
//温湿度曲线图
.state('envAnalyIndex.tempHumi', {
    url: '/tempHumi',
    cache:false,
    views: {
      'envAnalyIndex-tempHumi': {
        templateUrl: 'module/envAnalysis/tempHumi/envAnalyIndex-tempHumi.html',
        controller: 'tempHumiCtrl'
      }
    }
  })
//点温差曲线图
.state('envAnalyIndex.tempDiff', {
    url: '/tempDiff',
    cache:false,
    views: {
      'envAnalyIndex-tempDiff': {
        templateUrl: 'module/envAnalysis/tempDiff/envAnalyIndex-tempDiff.html',
        controller: 'tempDiffCtrl'
      }
    }
  })
//震荡图
.state('envAnalyIndex.shock', {
    url: '/shock',
    cache:false,
    views: {
      'envAnalyIndex-shock': {
        templateUrl: 'module/envAnalysis/shock/envAnalyIndex-shock.html',
        controller: 'shockCtrl'
      }
    }
  })





//报警统计
.state('warnTabIndex', {//warnCount
    url: '/warnTabIndex',
    cache:false,
    templateUrl: 'module/warnCount/warnTabIndex.html',
    controller: 'warnTabIndexCtrl'
  })
//环空报警表
.state('warnTabIndex.envWarnChart', {
    url: '/envWarnChart',
    cache:false,
    views: {
      'warnTabIndex-envWarnChart': {
        templateUrl: 'module/warnCount/envWarnChart/warnTabIndex-envWarnChart.html',
        controller: 'envWarnChartCtrl'
      }
    }
  })
//报警次数统计
.state('warnTabIndex.warnStatistics', {
    url: '/warnStatistics',
    cache:false,
    views: {
      'warnTabIndex-warnStatistics': {
        templateUrl: 'module/warnCount/warnStatistics/warnTabIndex-warnStatistics.html',
        controller: 'warnStatisticsCtrl'
      }
    }
  })









//报表模块
.state('textTableIndex', {
    url: '/textTableIndex',
    cache:false,
    templateUrl: 'module/textTableIndex/textTableIndex.html',
    controller: 'textTableIndexCtrl'
  })
//饲养日记
.state('textTableIndex.breedDiary', {
    url: '/breedDiary',
    cache:false,
    views: {
      'textTableIndex-breedDiary': {
        templateUrl: 'module/textTableIndex/breedDiary/textTableIndex-breedDiary.html',
        controller: 'breedDiaryCtrl'
      }
    }
  })
//用药记录
.state('textTableIndex.mediRecord', {
    url: '/mediRecord',
    cache:false,
    views: {
      'textTableIndex-mediRecord': {
        templateUrl: 'module/textTableIndex/mediRecord/textTableIndex-mediRecord.html',
        controller: 'mediRecordCtrl'
      }
    }
  })

// 使用小贴士
.state('tips', {
      url:'/tipsurl',
      templateUrl: "module/about/tips/tips.html",
      controller: 'tipsCtrl'
    })








/*.state('eggAnalysis.EEEq', {
    url: '/EEEq',
    cache:false,
    views: {
      'eggAnalysis-EEEq': {
        templateUrl: 'module/eggAnalysis/EEEq/eggAnalysis-EEEq.html',
        controller: 'EEEqCtrl'
      }
    }
  })*/


;
})

.config(function (ionicDatePickerProvider) {
    var datePickerObj = {
      inputDate: new Date(),
      titleLabel: null,        
      mondayFirst: false,
      weeksList: ["日", "一", "二", "三", "四", "五", "六"],
      monthsList:["01月", "02月", "03月", "04月", "05月", "06月", "07月", "08月", "09月", "10月", "11月", "12月"],
      setLabel: '确认',
      closeLabel: '取消',
      from: new Date(2010, 1, 1),// Optional，
      to: new Date(2020, 11, 31),// Optional， 
      inputDate: new Date(),     // Optional，
      mondayFirst: false,        // Optional，
      templateType: 'popup',     // Optional，modal or popup，Default value is modal
      dateFormat: 'yyyy-MM-dd',  // Optional，Defaults to dd-MM-yyyy.
      showTodayButton: true,     // Optional，是否选中今天按钮，默认值：false.
      todayLabel: '今天',
      closeOnSelect: false,      // Optional，关闭是否选中日期，默认值：false.
      disabledDates: [],
      disableWeekdays: []        // Optional，星期几被disabled,数组形式，0(Sunday) to 6(Saturday)
    };
    ionicDatePickerProvider.configDatePicker(datePickerObj);
  })