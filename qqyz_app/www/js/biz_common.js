// 判断农场、栋舍、员工是否有信息(该方法针对老板)
var biz_common_judgeRegistInfo = function(ionicPopup, state, page) {
    if (sparraw_user.userinfo.role == 1) {
        if (!sparraw_user.farminfo || sparraw_user.farminfo == 0) {
            var confirmPopup = ionicPopup.confirm({
                title: '提醒',
                template: '使用此功能前,请先创建农场信息。',
                buttons: [{
                    text: '取消',
                    onTap: function() {
                        console.log("留在本页");
                    }
                },
                {
                    text: '确定',
                    type: 'button-positive',
                    onTap: function() {
                        state.go("farmRegistered");
                    }
                }]
            });

        } else if (!sparraw_user.houseinfos || sparraw_user.houseinfos.length == 0) {
            var confirmPopup = ionicPopup.confirm({
                title: '提醒',
                template: '使用此功能前,请先创建栋舍信息。',
                buttons: [{
                    text: '取消',
                    onTap: function() {
                        console.log("留在本页");
                    }
                },
                {
                    text: '确定',
                    type: 'button-positive',
                    onTap: function() {
                        state.go("buildingTable");
                    }
                }]
            });
        } else if (!sparraw_user.userinfos || sparraw_user.userinfos.length == 0) {
            var confirmPopup = ionicPopup.confirm({
                title: '提醒',
                template: '使用此功能前,请先创建员工信息。',
                buttons: [{
                    text: '取消',
                    onTap: function() {
                        console.log("留在本页");
                    }
                },
                {
                    text: '确定',
                    type: 'button-positive',
                    onTap: function() {
                        state.go("employeesTable");
                    }
                }]
            });
            return;
        } else {
            console.log("都有啦~");
            state.go(page);
        };
    } else {
        state.go(page);
    };
};

// 重新获取该用户最新数据
var biz_common_getLatestData = function(state,nextPage) {

    Common_setDeviceImei();
    var params = {
        "userId":sparraw_user.profile.id_spa,
        "farmId":sparraw_user.farminfo.id
    };
    
    Sparraw.ajaxPost('loginMobile/queryDetail', params, function(data){
        if (data.ResponseDetail.Result == 'Success') {
            sparraw_user.houseinfos = data.ResponseDetail.HouseInfos;
            sparraw_user.farminfo = data.ResponseDetail.FarmInfo;
            sparraw_user.userinfos = data.ResponseDetail.EmploeeInfos;
            //Authority权限
            sparraw_user.Authority = data.ResponseDetail.AuthorityInfo;
        }else{
            Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
        };
        if(state && nextPage && nextPage != ""){
            state.go(nextPage);
        }
    });
};


// 将用户信息存于缓存中
function biz_common_saveUserInfo(userInfo){
    if(userInfo != null){
        localStorage.userInfo = userInfo; //设置一个键值
        var loginListStr = "{}";
        if(localStorage.loginList){
            loginListStr = localStorage.loginList;
        }
        var loginListObj = JSON.parse(loginListStr);
        var loginKey = JSON.parse(userInfo).userCode ;

        if(loginListObj.hasOwnProperty(loginKey)){
            delete loginListObj[loginKey];  
        }
        loginListObj[loginKey] = userInfo;

        //获取key并且倒序
        if (Common_getJsonSize(loginListObj) >= 10) {
            var listIndex = 0;
            var tempArr = [];
            var tempArr2 = [];
            for (var key in loginListObj){
                tempArr.push(key);
            }
            tempArr = tempArr.reverse();

            //获取大于10的key
            for (var i = 0; i < tempArr.length; i++) {
                listIndex ++;
                if (listIndex >= 10) {
                    tempArr2.push(tempArr[i]);
                }
            }

            //删除
            for (var i = 0; i < tempArr2.length; i++) {
                console.log(loginListObj[tempArr2[i]]);
                delete loginListObj[tempArr2[i]];
            }

        }else{

        }
        localStorage.loginList = JSON.stringify(loginListObj);
    }
}

// 缓存中获取用户信息
function biz_common_getUserInfo(){
    if(localStorage.userInfo){
        return JSON.parse(localStorage.userInfo) ;
    }else{
        return null;
    }
}

// 用户退出登录
function biz_common_userLogout(){
    selectBackPage.NeedLogin = false;
    sparraw_user.userinfo    = {}  ;
    sparraw_user.farminfo    = {}  ;
    sparraw_user.houseinfos  = []  ;
    sparraw_user.userinfos   = []  ;
    sparraw_user.profile = {
                'id_spa'      :  ''            ,
                'secret'      :  'mtc_secret'  ,
                'user_State'  :  true 
              };
              
    Alarm_clearTask();
}

// 模块正在建设中提示
function biz_common_pointDevelop() {
    app_alert('模块正在建设中...', '提示');
    return;
};

//设定标准设置默认值
function biz_common_getStandardWeekData(dateArr, valueArr) {
    var getArr = [];
    for (var i = 0; i < dateArr.length; i++) {
        getArr.push({
            "weekAge": i + 1,
            "cum_rate": (valueArr[i]).toFixed(2),
            "cum_alert": (valueArr[i] + 0.2).toFixed(2)
        });
    }
    return getArr;
}

//设定标准设置默认值
function biz_common_getStandardDayData(dateArr, valueArr) {
    var getArr = [];
    for (var i = 0; i < dateArr.length; i++) {
        getArr.push({
            "dayAge": i,
            "cum_rate": (valueArr[i]).toFixed(2),
            "cum_alert": (valueArr[i] + 0.2).toFixed(2)
        });
    }
    return getArr;
}

//英文系统选择日期转换
function biz_common_dateReverse(selectDate){
    var day = new Date(selectDate);
    var Year = 0;
    var Month = 0;
    var Day = 0;
    var CurrentDate = "";
    Year = day.getFullYear(); 
    Month = day.getMonth() + 1;
    Day = day.getDate();
    CurrentDate += Year + "-";
    if (Month >= 10) {
        CurrentDate += Month + "-";
    } else {
        CurrentDate += "0" + "-" + Month;
    }
    if (Day >= 10) {
        CurrentDate += Day;
    } else {
        CurrentDate += "0" + Day;
    }
    return CurrentDate;
}

function biz_common_judgePassword(cancleCallBack){
    if(sparraw_user.profile.password == "123456") {
        app_confirm('您的密码是初始密码，是否立即进行修改？', '提示', null, function (button) {
            // 1-取消  2-确定
            if (button == 2) {
                Sparraw.goToStateURL("modifyUserInfo");
            }else{
                cancleCallBack ? cancleCallBack() : biz_common_judgeDataInput();
            }
        });
    }else{
        cancleCallBack ? cancleCallBack() : biz_common_judgeDataInput();
    };
}

function biz_common_judgeDataInput(cancleCallBack){
    var params = {
        "operation":"needAlarm",
        "FarmBreedId":sparraw_user.farminfo && sparraw_user.farminfo.farmBreedBatchId ? sparraw_user.farminfo.farmBreedBatchId:0
    };
    /*Sparraw.ajaxPost('null', params, function(data){
        if (data.ResponseDetail.Result == "Success") {
            if (data.ResponseDetail.NeedAlarm == 'Y') {
                var showList = null;
                showList = data.ResponseDetail.AlarmHouseList.toString();

                showList = showList.replace(/(,)/g,"栋,");
                var buttonLabels = ['稍后填','立即填'];

                app_confirm('截止到昨天'+ showList +'栋，尚有生产记录未填写。',null,buttonLabels,function(buttonIndex){
                    // 1-取消  2-确定
                    if(buttonIndex == 2){
                        Sparraw.goToStateURL("dailyReport");
                    }else if(buttonIndex == 1){
                        cancleCallBack && cancleCallBack();
                    }
                });

            }else{
                cancleCallBack && cancleCallBack();
            }
        }else {
             Sparraw.myNotice(data.ResponseDetail.ErrorMsg);
        }
    });*/
}


//格式化图表数据
function setChartParams(data) {
    var returnTempLineConfig = [] ;

    for (var i = 0; i < data.length; i++) {
        var tYName = "";
        var tYType = "";
        var tYColor = null;
        
        tYName = data[i].HouseName ;
        //显示类型以及线颜色
        if (tYName == "标准") {
            tYType = "bar";
            tYColor = "#E3E3E3";
        }else{
            tYType = "line";
        }

        returnTempLineConfig.push({
                "yName"       :  tYName  ,
                "yData"       :  data[i].HouseDatas ,
                "yType"       :  tYType      ,
                "yColor"      :  tYColor
            });
    }
    return returnTempLineConfig;
};