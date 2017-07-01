/**
 * Created by LeLe on 2017-05-04.
 */
var pathName = window.location.pathname.substring(1);
var webName = (pathName == '') ? '' : pathName.substring(0, pathName.indexOf('/'));
var fullWebName = (webName == "") ? (window.location.protocol + '//' + window.location.host) : (window.location.protocol + '//' + window.location.host + '/' + webName);

// console.log("pathName : " + pathName);
// console.log("webName : " + webName);
// console.log("fullWebName : " + fullWebName);

var system = {
    undefined: "undefined",
    function: "function",
    path: "/" + webName,
    basePath: fullWebName,
    isUndefined: function(obj){
        return (undefined == typeof(obj))?true:false;
    },
    isFunction: function(obj){
        return (this.function == typeof(obj))?true:false;
    },
    request: function(config, funcSuccess, funcError){
        var func = this.jsonAjax(config, funcSuccess, funcError);
    },
    jsonAjax: function(config){
        if(this.isUndefined(config.url) || null == config.url){
            config.url = "";
        };
        if(this.isUndefined(config.param)){
            config.param = null;
        };
        if(this.isUndefined(config.cache) || false != config.cache || true != config.cache){
            config.cache = false;
        };
        if(this.isUndefined(config.async) || false != config.async || true != config.async){
            config.async = false;
        };
        console.log("system.path  : " + system.path );
        $.ajax({
            url: system.path + config.url,
            data: config.data,
            type : "POST",
            cache: config.cache,
            async: config.async,
            dataType: "json",
            success: (!this.isUndefined(arguments[1]) && this.isFunction(arguments[1]))?arguments[1]:null,
            error: (!this.isUndefined(arguments[2]) && this.isFunction(arguments[2]))?arguments[2]:null,
        });
    },
    getDate: function(){
        var d = new Date()//为日期命名
        var year = d.getFullYear();
        var month = d.getMonth() + 1;
        var date = d.getDate();
        var weekday = new Array(7);//建立一个星期的数组
        weekday[0] = "星期日";
        weekday[1] = "星期一";
        weekday[2] = "星期二";
        weekday[3] = "星期三";
        weekday[4] = "星期四";
        weekday[5] = "星期五";
        weekday[6] = "星期六";
        var week = weekday[d.getDay()];
        var result = {
            year: year,
            month: month,
            date: date,
            week: week
        };
        return result;
    },
    setTitle: function (){
        var config = {url: "/info/name"};
        this.request(config, this.getTitle);
    },
    getTitle: function(result){
        if(null != result && result.success){
            var name = result.obj.system_name;
            document.title = name;
        }
        return this.name;
//            console.log("获取系统名称：" + document.title);
    },
};