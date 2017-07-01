/**
 * Created by LeLe on 2017-05-02.
 */

var cityName = returnCitySN["cname"];

$(document).ready(function() {
    queryWeatherInfo(cityName);
});

//判断当前是上午还是下午， 1：白天 2：黑夜
function checkTime(){
    var now = new Date();
    var hour = now.getHours();
    if(hour > 6 && hour < 20){
        return 1; // 白天
    } else{
        return 2;  //黑夜
    }
}
//获取天气图标url
function getIconUrl(v){
    var iconPath = "<%=path%>/framework/weather/image/";
    var weatherIcon = [{id: 0 , name: '晴', name_en: 'Sunny'}
        ,{id: 4 , name: '多云', name_en: 'Cloudy'}
        ,{id: 5 , name: '晴间多云', name_en: 'Partly Cloudy'}
        ,{id: 7 , name: '大部多云', name_en: 'Mostly Cloudy'}
        ,{id: 9 , name: '阴', name_en: 'Overcast'}
        ,{id: 10 , name: '阵雨', name_en: 'Shower'}
        ,{id: 11 , name: '雷阵雨', name_en: 'Thundershower'}
        ,{id: 12 , name: '雷阵雨伴有冰雹', name_en: 'Thundershower with Hail'}
        ,{id: 13 , name: '小雨', name_en: 'Light Rain'}
        ,{id: 14 , name: '中雨', name_en: 'Moderate Rain'}
        ,{id: 15 , name: '大雨', name_en: 'Heavy Rain'}
        ,{id: 16 , name: '暴雨', name_en: 'Storm'}
        ,{id: 17 , name: '大暴雨', name_en: 'Heavy Storm'}
        ,{id: 18 , name: '特大暴雨', name_en: 'Severe Storm'}
        ,{id: 19 , name: '冻雨', name_en: 'Ice Rain'}
        ,{id: 20 , name: '雨夹雪', name_en: 'Sleet'}
        ,{id: 21 , name: '阵雪', name_en: 'Snow Flurry'}
        ,{id: 22 , name: '小雪', name_en: 'Light Snow'}
        ,{id: 23 , name: '中雪', name_en: 'Moderate Snow'}
        ,{id: 24 , name: '大雪', name_en: 'Heavy Snow'}
        ,{id: 25 , name: '暴雪', name_en: 'Snowstorm'}
        ,{id: 26 , name: '浮尘', name_en: 'Dust'}
        ,{id: 27, name: '扬沙', name_en: 'Sand'}
        ,{id: 28 , name: '沙尘暴', name_en: 'Duststorm'}
        ,{id: 29 , name: '强沙尘暴', name_en: 'Sandstorm'}
        ,{id: 30 , name: '雾', name_en: 'Foggy'}
        ,{id: 31 , name: '霾', name_en: 'Haze'}
        ,{id: 32 , name: '风', name_en: 'Windy'}
        ,{id: 33 , name: '大风', name_en: 'Blustery'}
        ,{id: 34 , name: '飓风', name_en: 'Hurricane'}
        ,{id: 35 , name: '热带风暴', name_en: 'Tropical Storm'}
        ,{id: 36 , name: '龙卷风', name_en: 'Tornado'}
        ,{id: 37 , name: '冷', name_en: 'Cold'}
        ,{id: 38 , name: '热', name_en: 'Hot'}
        ,{id: 99 , name: '未知', name_en: 'Unknown'}
    ];
    var ct = checkTime();
    var url = null;

    for(var key in weatherIcon){
        if (weatherIcon[key].name == v){
            url = iconPath + ct + "_" + key + ".png";
        }
    }
    return url;
}
//获取天气数据
function getWeatherData(data){
    var rt = [{"iconUrl" : null},{"iconUrl": null},{"iconUrl": null}];
    for(var i=0 ; i<3 ; i++){
        if("undefined" != typeof(data.forecast[i]) ){
            rt[i].iconUrl = getIconUrl(data.forecast[i].type);
            rt[i].tempHigh = data.forecast[i].high.replace("高温 ","");
            rt[i].tempLow = data.forecast[i].low.replace("低温 ","");
            rt[i].windLevel = data.forecast[i].fengli;
            rt[i].type = data.forecast[i].type;
        };
    }
    return rt;
}


function queryWeatherInfo(cityName){
    $.ajax({
        url : "http://wthrcdn.etouch.cn/weather_mini",
        data : {city: cityName},
        type : "GET",
        success : function(result) {
//                                    console.log("天气: ");
//                                    console.log(result);
//                                    console.log("IP: ");
//                                    console.log(returnCitySN["cip"]);
            document.getElementById("cityName").innerHTML = cityName;
            var r = JSON.parse(result);
            console.log(r);
            if("OK" == r.desc){
                var wd = getWeatherData(r.data);
//                                        console.log(wd);
                var dayName = "";
                for(var i= 0; i<3 ; i++){
                    dayName = (i==0)?"今天":((i==1)?"明天":"后天");
                    dayName = dayName + "&nbsp;&nbsp;";
                    if(null != wd[i].iconUrl){
                        document.getElementById("day" + (i+1) + "Icon").innerHTML =  '<img src="' + wd[i].iconUrl  + '">';
                        document.getElementById("day" + (i+1) + "WL").innerHTML = dayName + wd[i].type ;
                        document.getElementById("day" + (i+1) + "T").innerHTML = wd[i].tempLow + " - " + wd[i].tempHigh + "&nbsp;&nbsp;" + wd[i].windLevel;
                    }
                }
            } else{
                console.log("未能成功获取天气信息");
            }


        }
    });
}


