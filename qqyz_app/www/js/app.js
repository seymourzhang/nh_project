
// Ionic Starter App

angular.module('myApp', ['ionic','myApp.controllers','myApp.services','ionic-datepicker','ngCordova','ngTouch', 'ui.grid', 'ui.grid.pinning','ui.grid.edit', 'ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection', 'ui.grid.resizeColumns','ui.grid.autoResize',
                         'myApp.home','myApp.pushDetails','myApp.modifyUserInfo','myApp.landingProtocol','myApp.landingPage','myApp.forgotPassword','myApp.disclaimer','myApp.userList',
                         'myApp.userInfo','myApp.infoSafeguard','myApp.useRegistered','myApp.farmRegistered','myApp.buildingTable','myApp.updateBuildingInfo','myApp.addbuilding','myApp.employeesTable','myApp.updateEmployeesInfo','myApp.addEmployees',
                         'myApp.about','myApp.contactUs','myApp.functionIntroduce','myApp.feedBack',
                         'myApp.envMonitoringTable','myApp.envMonitoring','myApp.alarmLogDelay','myApp.tempChart','myApp.co2AndLightChart','myApp.alarmStatistics','myApp.alarmLog','myApp.alarmSetting','myApp.addTemp','myApp.updateTemp','myApp.addCO2','myApp.updateCO2','myApp.addLux','myApp.updateLux','myApp.voiceSettings',
                         'myApp.dailyReport',
                         'myApp.eggSellsReportTable','myApp.eggSellsReport','myApp.eggSellsList','myApp.salesAnalyze',
                         'myApp.batchManage','myApp.newBatch','myApp.apparentTempCalc','myApp.productPerformStander','myApp.historyDataImport','myApp.chickenWechat',
                         'myApp.tasklist',
                         'myApp.tabIndex','myApp.survival','myApp.dayCullDeath','myApp.dayFoodCurve','myApp.dayWaterCurve','myApp.weekWeight','myApp.eggProduceCurve',
                         'myApp.dailyTabIndex','myApp.daily','myApp.weekly','myApp.co2AndAmmonia',
                         'myApp.report','myApp.pariPerf'],function($httpProvider) {
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
      url:'/modifyUserInfourl',
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
// 推送详情
.state('pushDetails', {
      url:'/pushDetailsurl',
      cache:false,
      templateUrl: "module/home/pushDetails/pushDetails.html",
      controller: 'pushDetailsCtrl'
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
//用户信息
.state('userInfo', {
      url:'/userInfourl',
      templateUrl: "module/basicInfo/userInfo/userInfo.html",
      controller: 'userInfoCtrl'
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

//报警设置
.state('alarmSetting', {
      url:'/alarmSetting',
      cache:false,
      templateUrl: "module/alarmSetting/alarmSetting.html",
      controller: 'alarmSettingCtrl'
    })
//添加温度报警数据
.state('addTemp', {
      url:'/alarmSetting/addTemp/:houseId',
      cache:false,
      templateUrl: "module/alarmSetting/temp/add/addTemp.html",
      controller: 'addTempCtrl'
    })
//修改温度报警数据
.state('updateTemp', {
      url:'/alarmSetting/updateTemp/:item/:houseId',
      cache:false,
      templateUrl: "module/alarmSetting/temp/update/updateTemp.html",
      controller: 'updateTempCtrl'
    })
//添加二氧化碳报警数据
.state('addCO2', {
      url:'/alarmSetting/addCO2/:houseId',
      cache:false,
      templateUrl: "module/alarmSetting/co2/add/addCO2.html",
      controller: 'addCO2Ctrl'
    })
.state('updateCO2', {
      url:'/alarmSetting/updateCO2/:item/:houseId',
      cache:false,
      templateUrl: "module/alarmSetting/co2/update/updateCO2.html",
      controller: 'updateCO2Ctrl'
    })
//添加光照报警数据
.state('addLux', {
      url:'/alarmSetting/addLux/:houseId',
      cache:false,
      templateUrl: "module/alarmSetting/lux/add/addLux.html",
      controller: 'addLuxCtrl'
    })
.state('updateLux', {
      url:'/alarmSetting/updateLux/:item/:houseId',
      cache:false,
      templateUrl: "module/alarmSetting/lux/update/updateLux.html",
      controller: 'updateLuxCtrl'
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
//co2与光照曲线
.state('co2AndLightChart', {
      url:'/report/co2AndLightChart',
      cache:false,
      templateUrl: "module/envMonitoring/co2AndLightChart/co2AndLightChart.html",
      controller: 'co2AndLightChartCtrl'
    })


//co2与氨气曲线
.state('co2AndAmmonia', {
      url:'/report/co2AndAmmonia',
      cache:false,
      templateUrl: "module/envMonitoring/co2AndAmmonia/co2AndAmmonia.html",
      controller: 'co2AndAmmoniaCtrl'
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
// 语音设置
.state('voiceSettings', {
      url:'voiceSettings',
      cache:false,
      templateUrl: "module/envMonitoring/voiceSettings/voiceSettings.html",
      controller: 'voiceSettingsCtrl'
    })
    // 生产记录
.state('dailyReport', {
      url:'/dailyReporturl/:receiveHouseId',
      cache:false,
      templateUrl: "module/dailyTable/dailyReport/dailyReport.html",
      controller: 'dailyReportCtrl'
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
// 体感温度计算器
.state('apparentTempCalc', {
      url:'/apparentTempCalc',
      cache:false,
      templateUrl: "module/farmManage/apparentTempCalc/apparentTempCalc.html",
      controller: 'apparentTempCalcCtrl'
    })
// 鸡场微信
.state('chickenWechat', {
      url:'/chickenWechat',
      cache:false,
      templateUrl: "module/farmManage/chickenWechat/chickenWechat.html",
      controller: 'chickenWechatCtrl'
    })
/*任务模块*/
//任务列表
.state('tasklist', {
      url:'/tasklisturl',
      cache:false,
      templateUrl: "module/task/tasklist.html",
      controller: 'tasklistCtrl'
    })



//指标分析
  .state('tabIndex', {
    url: '/tabIndex',
    cache:false,
    templateUrl: 'module/reportCurve/tabIndex.html',
    controller: 'tabIndexCtrl'
  })
//成活率
.state('tabIndex.survival', {
    url: '/survival',
    cache:false,
    views: {
      'tabIndex-survival': {
        templateUrl: 'module/reportCurve/survival/tabIndex-survival.html',
        controller: 'survivalCtrl'
      }
    }
  })
//日死淘率
.state('tabIndex.dayCullDeath', {
    url: '/dayCullDeath',
    cache:false,
    views: {
      'tabIndex-dayCullDeath': {
        templateUrl: 'module/reportCurve/dayCullDeath/tabIndex-dayCullDeath.html',
        controller: 'dayCullDeathCtrl'
      }
    }
  })
//日采食率
.state('tabIndex.dayFoodCurve', {
    url: '/dayFoodCurve',
    cache:false,
    views: {
      'tabIndex-dayFoodCurve': {
        templateUrl: 'module/reportCurve/dayFoodCurve/tabIndex-dayFoodCurve.html',
        controller: 'dayFoodCurveCtrl'
      }
    }
  })
//日饮水率
.state('tabIndex.dayWaterCurve', {
    url: '/dayWaterCurve',
    cache:false,
    views: {
      'tabIndex-dayWaterCurve': {
        templateUrl: 'module/reportCurve/dayWaterCurve/tabIndex-dayWaterCurve.html',
        controller: 'dayWaterCurveCtrl'
      }
    }
  })
// 周体重
.state('tabIndex.weekWeight', {
    url: '/weekWeight',
    cache:false,
    views: {
      'tabIndex-weekWeight': {
        templateUrl: 'module/reportCurve/weekWeight/tabIndex-weekWeight.html',
        controller: 'weekWeightCtrl'
      }
    }
  })
// 产蛋率
.state('tabIndex.eggProduceCurve', {
    url: '/eggProduceCurve',
    cache:false,
    views: {
      'tabIndex-eggProduceCurve': {
        templateUrl: 'module/reportCurve/eggProduceCurve/tabIndex-eggProduceCurve.html',
        controller: 'eggProduceCurveCtrl'
      }
    }
  })






//生产报告
  .state('dailyTabIndex', {
    url: '/dailyTabIndex',
    cache:false,
    templateUrl: 'module/dailyTabIndex/dailyTabIndex.html',
    controller: 'dailyTabIndexCtrl'
  })
//日报
.state('dailyTabIndex.daily', {
    url: '/daily',
    cache:false,
    views: {
      'dailyTabIndex-daily': {
        templateUrl: 'module/dailyTabIndex/daily/dailyTabIndex-daily.html',
        controller: 'dailyCtrl'
      }
    }
  })
//周报
.state('dailyTabIndex.weekly', {
    url: '/weekly',
    cache:false,
    views: {
      'dailyTabIndex-weekly': {
        templateUrl: 'module/dailyTabIndex/weekly/dailyTabIndex-weekly.html',
        controller: 'weeklyCtrl'
      }
    }
  })
//报表查看
.state('report', {
      url:'/reporturl',
      cache:false,
      templateUrl: "module/report/report.html",
      controller: 'reportCtrl'
    })

//性能指标查询(全胎次性能,生产效率达成周报)
.state('pariPerf', {
      url:'/pariPerfurl/:tableTitle',
      cache:false,
      templateUrl: "module/report/pariPerf/pariPerf.html",
      controller: 'pariPerfCtrl'
    })

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