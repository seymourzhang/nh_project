<%--
  Created by IntelliJ IDEA.
  User: Raymon
  Date: 1/17/2017
  Time: 15:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String systemName ="正大智慧鸡场管理系统";
    String systemVersion = "2017";
    String systemRightsInfo = "上海农汇信息科技有限公司版权所有  沪ICP备13027505  Copyright©2006-2017 All Rights Reserved";
%>

<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link id = "linkFavicon" rel="shortcut icon" href="<%= path%>/framework/image/favicon.ico">
<title><%= systemName%></title>

<!-- Bootstrap -->
<link rel="stylesheet" href= "<%= path%>/framework/bootstrap/3.3.0/css/bootstrap.min.css">
<link rel="stylesheet" href="<%= path%>/framework/awesome/3.2.1/css/font-awesome.css">
<link rel="stylesheet" href= "<%= path%>/framework/css/system.css">

<script>
    var system = {
        undefined: "undefined",
        function: "function",
        path: "<%=path%>",
        basePath: "<%=basePath%>",
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
    };
</script>