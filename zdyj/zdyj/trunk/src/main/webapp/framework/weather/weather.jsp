<%--
  Created by IntelliJ IDEA.
  User: LeLe
  Date: 2017-05-02
  Time: 11:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<meta charset="utf-8" />

<div id="mWeather">
    <font color="white">
        <div class="row-fluid" style = "padding-top: 6px;">
            <div class="span2" align="center">
                <div class="row-fluid" >
                    <div class="span12">
                        <div id = "cityName"></div>
                        <div>3日天气</div>
                    </div>
                </div>
            </div>
            <div class="span10">
                <div class="row-fluid">
                    <div class="span1">
                        <div id = "day1Icon"></div>
                    </div>
                    <div class="span3">
                        <div id = "day1WL"></div>
                        <div id = "day1T"></div>
                    </div>
                    <div class="span1">
                        <div id = "day2Icon"></div>
                    </div>
                    <div class="span3">
                        <div id = "day2WL"></div>
                        <div id = "day2T"></div>
                    </div>
                    <div class="span1">
                        <div id = "day3Icon"></div>
                    </div>
                    <div class="span3">
                        <div id = "day3WL"></div>
                        <div id = "day3T"></div>
                    </div>
                </div>
            </div>
        </div>
    </font>
</div>
<script src="http://pv.sohu.com/cityjson?ie=utf-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/framework/weather/js/weather.js" charset="utf-8"></script>