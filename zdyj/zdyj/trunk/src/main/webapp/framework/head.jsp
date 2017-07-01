<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- BEGIN HEADER -->
<div class="header navbar navbar-inverse navbar-fixed-top" style="height: 61px;z-index: 1;">

	<div class="navbar-inner" style="height: 50px;z-index: 3;">
		<div class="container-fluid">
			<a class="brand" href="#" style="width: 330px;"> <img src="<%=path%>/framework/image/logo.png" alt="logo" style="padding-left: 13px;float: left;height: 40px;width: 40px;margin-top: -4px;" />
			<div style="font-size: 24px;color: #FFFFFF;padding-left: 8px;padding-top: 5px;float: left;font-weight:bold;">正大智慧鸡场管理系统</div>
				<!-- <span style="padding-left: 3px;float: left;padding-top: 5px;color: #FFFFFF;font-size: 14px;font-weight:lighter;">®</span> -->
			<!-- <div style="font-size: 22px;color: #FFFFFF;padding-left: 5px;padding-top: 11px;float: left;font-weight:lighter;">物联网</div> --></a> <a href="javascript:;" class="btn-navbar collapsed" data-toggle="collapse" data-target=".nav-collapse"></a>
			<ul class="nav pull-right">
				<li class="dropdown user">
					<div style="height:45px;z-index: 2;" id = "weather">
						<div>
								<font color="white">
									<div class="row-fluid" style = "padding-top: 6px;">
										<div class="span2" align="center" style="width: 60px;">
											<div class="row-fluid" >
												<div class="span12">
													<div id = "cityName"></div>
													<div id = "weatherTitle"></div>
												</div>
											</div>
										</div>
										<div class="span10" >
											<div class="row-fluid" id = "dayContext">
												<%--<div class="span1">--%>
													<%--<div id = "day1Icon"></div>--%>
												<%--</div>--%>
												<%--<div class="span3">--%>
													<%--<div id = "day1WL"></div>--%>
													<%--<div id = "day1T"></div>--%>
												<%--</div>--%>
												<%--<div class="span1">--%>
													<%--<div id = "day2Icon"></div>--%>
												<%--</div>--%>
												<%--<div class="span3">--%>
													<%--<div id = "day2WL"></div>--%>
													<%--<div id = "day2T"></div>--%>
												<%--</div>--%>
												<%--<div class="span1" style="display: none;">--%>
													<%--<div id = "day3Icon"></div>--%>
												<%--</div>--%>
												<%--<div class="span3" style="display: none;">--%>
													<%--<div id = "day3WL"></div>--%>
													<%--<div id = "day3T"></div>--%>
												<%--</div>--%>
											</div>
										</div>
									</div>
								</font>
							</div>
							<script type="text/javascript" src="<%=path%>/framework/weather/js/area_info.js"></script>

							<script>
								var htmlGetCityJson = '<script src = "' + window.location.protocol + '//pv.sohu.com/cityjson?ie=utf-8">' + '<\/script>';
                                document.write(htmlGetCityJson);
							</script>
							<script >
                                var showDayNum = (window.screen.width >=1600)?3:2;
                                var cityName = returnCitySN["cname"];
								var cityKey = getCityKey(cityName);
								var path = "<%=path%>";
//                                console.log(cityKey);
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
//									    console.log(weatherIcon[key].name + " : " + v + " : " + key);
                                            url = iconPath + ct + "_" + weatherIcon[key].id + ".png";
                                        }
                                    }
                                    return url;
                                }
                                //获取天气数据
                                function getWeatherData(data){
                                    var rt = [{"iconUrl" : null},{"iconUrl": null},{"iconUrl": null}];
                                    for(var i=0 ; i<showDayNum ; i++){
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


                                $.ajax({
                                    url : path + "/info/weather",
//                                    data : {city: cityName},
                                    data : {citykey: cityKey},
                                    type : "GET",
                                    success : function(result) {
//                                    console.log("天气: ");
//                                    console.log(result);
//                                    console.log("IP: ");
//                                    console.log(returnCitySN["cip"]);
                                        document.getElementById("cityName").innerHTML = cityName;

                                        var r = JSON.parse(result);
//                                    console.log(r);
                                        if(r.success && "OK" == r.obj.desc){
                                            document.getElementById("weather").style.width = (620/3)*showDayNum + "px";
//                                            console.log("weather width : " + document.getElementById("weather").style.width);
                                            document.getElementById("weatherTitle").innerHTML = showDayNum + "日天气";
                                            var wd = getWeatherData(r.obj.data);
//                                        console.log(wd);
                                            var dayName = "";
                                            var dayContext = "";
                                            var spanIndex = 12 / showDayNum - 1;
                                            for(var i= 0; i<showDayNum ; i++){
                                                dayName = (i==0)?"今天":((i==1)?"明天":"后天");
                                                dayName = dayName + "&nbsp;&nbsp;";
                                                if(null != wd[i].iconUrl){
                                                    dayContext += '<div class="span1">';
														dayContext += '<div id = "day' + (i+1) + 'Icon">';
														dayContext += '<img src="' + wd[i].iconUrl  + '">';
														dayContext += '</div>';
                                                    dayContext += '</div>';
                                                    dayContext += '<div class="span' + spanIndex + '">';
														dayContext += '<div id = "day' + (i+1) + 'WL">';
															dayContext += dayName + wd[i].type;
														dayContext += '</div>';
														dayContext += '<div id = "day' + (i+1) + 'T">';
															dayContext += wd[i].tempLow + " - " + wd[i].tempHigh + "&nbsp;&nbsp;" + wd[i].windLevel;
														dayContext += '</div>';
                                                    dayContext += '</div>';
//                                                    document.getElementById("day" + (i+1) + "Icon").innerHTML =  '<img src="' + wd[i].iconUrl  + '">';
//                                                    document.getElementById("day" + (i+1) + "WL").innerHTML = dayName + wd[i].type ;
//                                                    document.getElementById("day" + (i+1) + "T").innerHTML = wd[i].tempLow + " - " + wd[i].tempHigh + "&nbsp;&nbsp;" + wd[i].windLevel;
                                                }
                                            }
                                            document.getElementById("dayContext").innerHTML = dayContext;
                                        } else{
                                            console.log("未能成功获取天气信息");
                                            document.getElementById("weatherTitle").innerHTML = "天气不明";
                                        }


                                    }
                                });
							</script>
					</div>
				</li>
				<li class="dropdown user"><div  style="padding-top: 6px;text-align: center;padding-right: 20px;"> <span style="color: #fff;font-size: 14px;text-align: center;" id="dateMassage"></span> 
				</div>
				</li>

				<li class="dropdown" id="header_remind_bar">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" style="margin-top:4px; ">
						<img src="<%=path%>/framework/image/remind.png" style="width: 18px;height: 18px;">
						<span class="badge"  style="margin-top:6px;" id="head_msg_remind_currCount"></span>
					</a>
					<ul class="dropdown-menu extended notification" style="margin-top:4px; " id="head_msg_remind_CurrList">
					</ul>
				</li>

				<li class="dropdown" id="header_notification_bar">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown" style="margin-top:4px; "> <img src="<%=path%>/framework/image/warn.png" style="width: 18px;height: 18px;"> <span class="badge"  style="margin-top:6px;" id="head_msg_currCount"></span>

				</a>

					<ul class="dropdown-menu extended notification" style="margin-top:6px; " id="head_msg_CurrList">

						
					</ul></li>

				<!-- BEGIN USER LOGIN DROPDOWN -->
				<li class="dropdown user"><a href="#" class="dropdown-toggle" data-toggle="dropdown" style="margin-top:8px;"> <img alt="" src="<%=path%>/framework/image/user.png" style="width: 20px;height: 20px;" /> <span class="username">${sessionUser.user_real_name }</span> <i class="icon-angle-down" style="margin-top:8px;"></i>
				</a>
					<ul class="dropdown-menu">
						<!-- <li><a href="javascript:void(0);"><i class="icon-lock"></i>
									修改密码</a>
							</li> -->
						<li><a href="<%=path%>/login/outLogin"><i class="icon-key"></i> 退出</a></li>
					</ul></li>
				<!-- END USER LOGIN DROPDOWN -->

			</ul>
		</div>
	</div>
</div>
<!-- END HEADER -->
<!--引入属于此页面的js -->

<script type="text/javascript" src="<%=path%>/framework/js/head.js"></script>