<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<head>
<meta charset="utf-8" />
<%@ include file="../../framework/inc.jsp"%>
<link rel="stylesheet" href="<%=path %>/framework/css/datepicker.css" />

<!-- <script src="<%=path%>/framework/js/bootstrap_table/bootstrap-table.js"></script> -->
<!-- <link href="<%=path%>/framework/js/bootstrap_table/bootstrap-table.css" rel="stylesheet" /> -->
<!-- <script src="<%=path%>/framework/js/bootstrap_table/locale/bootstrap-table-zh-CN.js"></script> -->

<!-- <link rel="stylesheet" href="<%=path%>/framework/js/bootstrap_editable/1.5.1/css/bootstrap-editable.css"> -->
<!-- <script src="<%=path%>/framework/js/bootstrap_editable/1.5.1/js/bootstrap-editable.js"></script> -->
<!-- <script src="<%=path%>/framework/js/bootstrap_table/extensions/editable/bootstrap-table-editable.js"></script> -->

<!-- <script type="text/javascript" src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script> -->
<!-- <script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script> -->
<!-- <script type="text/javascript" src="http://cdn.hcharts.cn/modules/exporting.js"></script> -->
<script type="text/javascript" src="<%=path%>/framework/js/charts/highcharts.js"></script>
<!-- <script type="text/javascript" src="<%=path%>/framework/js/charts/exporting.src.js"></script> -->
<script type="text/javascript">
	var isRead= "${pd.write_read}";
	var menuId = "${pd.menu_id}";
	var corporation_id = "${corporation_id}";
    var farm_id = "${farm_id}";
    var house_id = "${house_id}";
    var at = "${alarm_type}";
    var alarmIndex = "${alarmIndex}";
    var alarmProbeList = "${alarm_probe}";
	jQuery(document).ready(function() {
// 		App.init(); // initlayout and core plugins
// 		var win_h = $(window).height() - 208;
// 		$("#monitor_date_table").css("min-height", win_h);
// 		$("#page-content").css("min-height", win_h);
// 		 $("#user_date_table").css("min-height",win_h-80);
// 		 $("#container").css("height",win_h-100);
		 <%--if(<%=request.getParameter("alarm_type")%> != '' && <%=request.getParameter("alarm_type")%> != null){--%>
		  <%--document.getElementById('alarmType').value = <%=request.getParameter("alarm_type")%>;--%>
		 <%--}--%>
        if(at != '' && at != null){
            document.getElementById('alarmType').value = at;
        }

//		 if(document.getElementById('alarmType').value != 1){
//			 $("#alarm_delay").attr("disabled",true);
//			 $("#temp_cpsation").attr("disabled",true);
//			 $("#yincang").attr("disabled",true);
//			 $("#temp_cordon").attr("disabled",true);
//		 }
		 		 
//		 $("#temperature").click(function () {
//			 document.getElementById('alarmType').value= "1";
// 			 $("#anniu").css("margin-left","0");
//			 $("#alarmParam").css("display", "block");
			 
//			 search();
	    });
//		 $("#negativePressure").click(function () {
//			 document.getElementById('alarmType').value= "2";
//			 $("#alarmParam").css("display", "none");
//			 search();
//	    });
//		 $("#carbon").click(function () {
//			 document.getElementById('alarmType').value= "3";
//			 $("#alarmParam").css("display", "none");
//			 search();
//	    });
		 
//		 tempCordon();
// 		 search();
// 		 querySBDayageSettingSub();
//	});
	
//	function tempCordon(){
//		if($("#temp_cpsation").val()==0){
//			$("#temp_cordon").attr("disabled",true);
//		}else {
//			$("#temp_cordon").attr("disabled",false);
//		}
//	}
	
</script>

</head>
<body style="background-color: #ffffff;">
	<!--  <div class="container-fluid" id="main-container" style="background-color: #ffffff;"> -->

		<div id="page-content" class="clearfix">
			<form action="" method="post" id="alarmForm">
			<%--标签菜单--%>
			<input type="hidden" name="alarm_type" value="1" id="alarmType">
			<input type="hidden" name="buttonValue" id="buttonValue">
			<div class="row-fluid">
				<div class="span12">
					<div class="tabbable tabbable-custom boxless" >
						<div class="row-fluid">
							<%--标签菜单栏--%>
							<div class="span12" style="margin-left: 0px;height: 10px">
								<ul class="nav nav-pills row-fluid" style="margin-bottom: 0px; ">
									<li  class="active" id="createBatch" style="text-align: center;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none;">
										<a href="#tabCreateBatch" data-toggle="tab" id="temperature">温度</a>
									</li>
									<li  id="humidity1" style="text-align: center;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none;">
										<a href="#tabHumidity" data-toggle="tab" id="humidity">湿度</a>
									</li>
									<li  id="overBatch" style="text-align: center;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none;" >
										<a href="#tabOverBatch" data-toggle="tab" id="carbon">二氧化碳</a>
									</li>
									<li  id="negativePressure1" style="text-align: center;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none;" >
										<a href="#tabNegativePressure" data-toggle="tab" id="negativePressure2">负压</a>
									</li>
									<li  id="editBatch" style="text-align: center;background-color: #BFBFBF;display: none;" >
										<a href="#tabEditBatch" data-toggle="tab" id="negativePressure">光照</a>
									</li>
								</ul>
							</div>
						</div>

						<%--功能栏--%>
						<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px; ">
							<div class="span11">
								<div style="padding-left: 10px;">
									<%@ include file="../../framework/org.jsp"%>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<button id="btnApply" type="button" class="btn blue" style="text-align: center;vertical-align: middle;display:none;" onclick="applyAlarmUrl()" <c:if test="${pd.write_read==0}"> disabled="disabled"</c:if>>
										<i class="icon-share-alt">&nbsp;应用至</i>
									</button>
									&nbsp;&nbsp;
									<button type="button" class="btn blue" style="text-align: center;vertical-align: middle;display:none;" onclick="update()" id="upData" <c:if test="${pd.write_read==0}"> disabled="disabled"</c:if>>
										<i class="icon-save">&nbsp;保存</i>
									</button>
									<button type="button" class="btn blue" onclick="upAndAdd()" id="upData2" style="display:none;" <c:if test="${pd.write_read==0}"> disabled="disabled"</c:if>>
										<i class="icon-save">&nbsp;保存</i>
									</button>
									&nbsp;&nbsp;
									<p id="btnDesc" class="font" style="color: brown;display:none;font-weight: bolder">新增或修改内容后请点击保存按钮！</p>
									<p id="msgLoad2" class="font" style="color: brown;display:none;font-weight: bolder;margin-top: -3px">无栋舍，请先添加栋舍！</p>
								</div>
							</div>
							<div class="span1" align="right">
								<%@ include file="../../framework/help/help.jsp"%>
								<div id="helpContext" style="display: none;">
									<table id = "helpTable" class="table">
										<thead>
										<tr>
											<td style="font-weight:bold;text-align: left;">图例</td>
											<td style="font-weight:bold;text-align: left;">说明</td>
										</tr>
										</thead>
										<tbody>
											<tr >
												<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpAlarmAdd.png" style="width: 200px;height: 45px;"></td>
												<td style="text-align: left;">鼠标点击新增按钮，可新增1条空白的报警设置记录，用于设置新的报警</td>
											</tr>
											<tr>
												<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpAlarmEdit.png" style="width: 200px;height: 45px;"></td>
												<td style="text-align: left;">鼠标点击带有虚线下划线的文字，可对该报警值进行修改，按回车键确认修改完毕</td>
											</tr>
											<tr>
												<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpAlarmApply.png" style="width: 200px;height: 80px;"></td>
												<td style="text-align: left;">鼠标点击应用至按钮，可在弹出窗口选择需要应用至的栋舍</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<%--<div class="span7" id="toolbarButton" style="display: none;">--%>
								<%----%>
							<%--</div>--%>
						</div>
						<div class="row-fluid" id="msgLoad" style="text-align: center" style="display:none;">
							<div class="span12">
								<span_customer2 style="vertical-align: middle">正在加载报警设置数据,请稍等...</span_customer2>
							</div>
						</div>
<!-- 						<div class="row-fluid" id="msgLoad2" style="text-align: center" style="display:none;"> -->
<!-- 							<div class="span12"> -->
<!-- 								<span_customer2 style="vertical-align: middle">无栋舍，请先添加栋舍</span_customer2> -->
<!-- 							</div> -->
<!-- 						</div> -->

						<%--报警设置--%>
						<div class="row-fluid" style="padding-top: 5px;">
							<div class="span12">
								<%--温度报警设置--%>
								<div id="TemperatureCurveFrame" style="display:none;">
									<div class="row-fluid">
										<div class="span8">
													<div class="row-fluid">
														<div class="span12" style="text-align: left;align: left;">
															<button type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="openAdjustWin('${hourList}');" id="addData" <c:if test="${pd.write_read==0}"> disabled="disabled"</c:if>>
																<i class="icon-plus">&nbsp;新增</i>
															</button>
														</div>
													</div>
													<div style="height:346px; overflow:auto;border-bottom:1px solid #E0E0E0;border-left:1px solid #E0E0E0;border-right: 1px solid #E0E0E0;" id="addData3">
														<table id="TemperatureCurveTable"></table>
													</div>
										</div>
										<div class="span4" id="TemperatureCurveTableBasic">
											<%--class="table table-striped table-bordered table-hover"--%>
												<div class="row-fluid" style="padding-top: 4px;">
													<div class="span12" style="text-align: right;align: right;">
													</div>
												</div>
											<table class="bootstrap-table table-striped table-bordered table-hover" style="text-align: center;align: center;width: 100%;" id="houseAlarm1">
												<thead >
													<tr style="width: 100%;height: 40px;background-color: #2586C4;color: white;font-weight:bold;">
														<td >
															设置项
														</td>
														<td >
															设置值
														</td>
													</tr>
												</thead>
												<tbody>
													<tr style="border-top: 1px solid">
														<td style="width: 30%;">
															<span_customer2 >设备信息</span_customer2>
														</td>
														<td style="width: 70%;">
															<div class="row-fluid">
																<div class="span1">
																</div>
																<div class="span9">
																	<select id="device_code" name="device_code" tabindex="1" style="width:100%;height: 33px;margin-bottom: 2px">
																	</select>
																</div>
																<div class="span2">
																	<script>
                                                                        var msg ="栋舍绑定的设备";
                                                                        help.showHelpIcon("device_code", msg);
																	</script>
																</div>
															</div>
														</td>
													</tr>
													<tr style="border-top: 1px solid">
														<td style="width: 30%;">
															<span_customer2>报警形式</span_customer2>
														</td>
														<td style="width: 70%">
															<div class="row-fluid">
																<div class="span1">
																</div>
																<div class="span9">
																	<select id="yincang" name="alarm_probe" tabindex="1" style="width:100%;height: 33px;margin-bottom: 2px">
																		<!-- 																<option value="02">独立探头报警</option> -->
																		<!-- 																<option value="03">平均温度报警</option> -->
																		<c:if test="${!empty alarm_probe}">
																			<c:forEach var="alarmProbe" items="${alarm_probe}">
																				<option value="${alarmProbe.biz_code }">${alarmProbe.code_name }</option>
																			</c:forEach>
																		</c:if>
																	</select>
																</div>
																<div class="span2">
																	<script>
                                                                        var msg ="<font style='color: #C43C57;'>独立温度报警：</font>在报警传感器中勾选的传感器，将各自分别报警<br>";
                                                                        msg += "<font style='color: #C43C57;'>平均传感器报警：</font>在报警传感器中勾选的传感器，将以它们的平均值进行报警";
                                                                        help.showHelpIcon("yincang", msg);
																	</script>
																</div>
															</div>
														</td>
													</tr>
													<tr style="border-top: 1px solid;height: 37px;">
														<td style="width: 30%;">
															<span_customer2>报警传感器</span_customer2>
														</td>
														<td style="width: 70%;text-align: center;">
															<div class="row-fluid">
																<div class="span1">
																</div>
																<div class="span9">
																	<div class="span12" id="yincang2" style="width:100%;padding-top: 8px;text-align: center;"></div>
																</div>
																<div class="span2">
																</div>
															</div>
														</td>
													</tr>
													<tr style="border-top: 1px solid">
														<td style="width: 30%;">
															<span_customer2>点温差报警</span_customer2>
														</td>
														<td style="width: 70%">
															<div class="row-fluid">
																<div class="span1">
																</div>
																<div class="span9">
																	<input  type="text" id="point_alarm" name="point_alarm"  style="width:95%;height: 23px;margin-bottom: 2px" disabled="disabled">
																</div>
																<div class="span2">
																	<script>
                                                                        var msg ="<font style='color: #C43C57;'>1）</font>表示各勾选的传感器中温度最大值与最小值的差值超过点温差报警值时报警<br>";
																			msg += "<font style='color: #C43C57;'>2）</font>当该值为0时，表示禁用点温差报警";
                                                                        help.showHelpIcon("point_alarm", msg);
																	</script>
																</div>
															</div>
														</td>
													</tr>
													<tr style="border-top: 1px solid">
														<td style="width: 30%;">
															<span_customer2>温度补偿</span_customer2>
														</td>
														<td style="width: 70%">
															<div class="row-fluid">
																<div class="span1">
																</div>
																<div class="span9">
																	<input  type="text" id="temp_cordon" name="temp_cordon" style="width:95%;height: 23px;margin-bottom: 2px">
															<select id="temp_cpsation" name="temp_cpsation" tabindex="1" onchange="tempCordon()" style="width:80%;display: none">
																<option value="1">是</option>
																<option value="0">否</option>
															</select>
																</div>
																<div class="span2">
																	<script>
                                                                        var msg ="<font style='color: #C43C57;'>1）</font>表示仅当实际检测到的温度值与温度补偿值之和超过高报温度时才会触发报警<br>";
																				msg += "<font style='color: #C43C57;'>2）</font>当报警形式为平均温度报警时，报警将是对平均温度进行温度补偿<br>";
                                                                       			msg += "<font style='color: #C43C57;'>3）</font>当报警形式为独立探头报警时，报警将是对启用的报警传感器进行温度补偿<br>";
                                                                        		msg += "<font style='color: #C43C57;'>4）</font>当该值为0时，表示禁用温度补偿";
                                                                        help.showHelpIcon("temp_cordon", msg);
																	</script>
																</div>
															</div>
														</td>
													</tr>
													<tr style="border-top: 1px solid">
														<td style="width: 30%;">
															<span_customer2>报警延迟</span_customer2>
														</td>
														<td style="width: 70%">
															<div class="row-fluid">
																<div class="span1">
																</div>
																<div class="span9">
																	<select id="alarm_delay" name="alarm_delay" tabindex="1" onchange="" style="width:100%;height: 33px;margin-bottom: 2px">
																<c:if test="${!empty alarm_delay}">
																	<c:forEach var="alarmDelay" items="${alarm_delay}">
																		<option value="${alarmDelay.biz_code }">${alarmDelay.code_name }</option>
																	</c:forEach>
																</c:if>
															</select>
																</div>
																<div class="span2">
																	<script>
                                                                        var msg ="当实际监测值高于或低于设置的阈值时，延迟多少分钟才发出报警";
                                                                        help.showHelpIcon("alarm_delay", msg);
																	</script>
																</div>
															</div>
														</td>
													</tr>
													<tr style="border-top: 1px solid;height: 37px;">
														<td style="width: 30%;">
															<span_customer2>语音报警</span_customer2>
														</td>
														<td style="width: 70%">
															<div class="row-fluid">
																<div class="span1">
																</div>
																<div class="span9" style="padding-top: 10px;">
																	<a href="javascript:bindingUserUrl();" onclick="" style="width:100%;" id="bindingUser">设置报警通知人员</a>
																</div>
																<div class="span2">
																	<script>
                                                                        var msg ="设置报警发生时通知哪些人员，并且在人员未能及时接受通知时，系统将按设定的1、2、3顺序依次通知人员";
                                                                        help.showHelpIcon("bindingUser", msg);
																	</script>
																</div>
															</div>
														</td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
									<%--<div class="row-fluid">--%>
										<%--<div class="span12">--%>
										<%--</div>--%>
									<%--</div>--%>
								</div>
								<%--湿度报警设置--%>
								<div id="HumidityFrame" style="display: none;" >
								<div class="row-fluid">
														<div class="span12" style="text-align: left;align: left;">
															<button type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="openAdjustWin('${hourList}');" id="addData9" <c:if test="${pd.write_read==0}"> disabled="disabled"</c:if>>
																<i class="icon-plus">&nbsp;新增</i>
															</button>
														</div>
													</div>
													<div style="height:346px; overflow:auto;border-bottom:1px solid #E0E0E0;border-left:1px solid #E0E0E0;border-right: 1px solid #E0E0E0;" id="addData8">
														<table id="HumidityTable"></table>
													</div>
								</div>
								<%--二氧化碳报警设置--%>
									<div id="CarbonFrame" style="display: none;" >
										<div class="row-fluid">
											<div class="span12" id="addData6">
												<table id="CarbonTable" ></table>
											</div>
										</div>
<!-- 										<div class="row-fluid"> -->
<!-- 											<div class="span12"> -->
<!-- 											</div> -->
<!-- 										</div> -->
									</div>
                                <%--负压报警设置--%>
									<div id="NegativePressure2Frame" style="display: none;" >
										<div class="row-fluid">
											<div class="span12" id="addData7">
												<table id="NegativePressure2Table" ></table>
											</div>
										</div>
									</div>
									
								<%--光照报警设置--%>
								<div id="NegativePressureFrame" style="display: none;" >
													<div class="row-fluid">
														<div class="span6" style="text-align: left;align: left;">
															<button type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="openAdjustWin('${hourList}');" id="addData2" <c:if test="${pd.write_read==0}"> disabled="disabled"</c:if>>
																<i class="icon-plus">&nbsp;新增</i>
															</button>
														</div>
														<div class="span6" style="text-align: right;align: right;">
														</div>
													</div>
													<div style="height:315px; overflow:auto;border-bottom:1px solid #E0E0E0;border-left:1px solid #E0E0E0;border-right: 1px solid #E0E0E0;" id="addData4">
														<table id="NegativePressureTable" ></table>
													</div>
										<%--<div class="row-fluid">--%>
											<%--<div class="span12">--%>
											<%--</div>--%>
										<%--</div>--%>
								</div>
								<%--耗水量报警设置--%>
								<div id="WaterFrame" style="display: none;" >
									<div class="row-fluid">
										<div class="span12">
											<table id="WaterTable"></table>
										</div>
									</div>
								</div>
							</div>
						</div>&nbsp;

						<%--报警设置曲线图--%>
						<div class="row-fluid" id="user_date_table" style="display: none;" >
							<div class="span6" id="addData5">
								<!-- 								<div class="control-group" style="padding-left: 0px;"> -->
								<button type="button" class="btn blue" style="width: 90px;display:none;" onclick="javascript:querySBDayageSettingSub(20);" id="one">
									1-20周
								</button>&nbsp;&nbsp;
								<!-- 								</div> -->
								<!-- 							</div> -->
								<!-- 							<div class="span3"> -->
								<!-- 								<div class="control-group" style="padding-left: 5px;"> -->
								<button type="button" class="btn blue" style="width: 90px;display:none;" onclick="javascript:querySBDayageSettingSub(40);" id="two">
									21-40周
								</button>&nbsp;&nbsp;
								<!-- 								</div> -->
								<!-- 							</div> -->
								<!-- 							<div class="span3"> -->
								<!-- 								<div class="control-group" style="padding-left: 5px;"> -->
								<button type="button" class="btn blue" style="width: 90px;display:none;" onclick="javascript:querySBDayageSettingSub(60);" id="three">
									41-60周
								</button>&nbsp;&nbsp;
								<!-- 								</div> -->
								<!-- 							</div> -->
								<!-- 							<div class="span3"> -->
								<!-- 								<div class="control-group" style="padding-left: 5px;"> -->
								<button type="button" class="btn blue" style="width: 90px;display:none;" onclick="javascript:querySBDayageSettingSub(80);" id="fine">
									61周以后
								</button>
								<!-- 								</div> -->
							</div>
							<div class="span6">
							</div>
						</div>
						<div class="row-fluid" >
							<div class="span12">
								<div id="container" style="height:370px;"></div>
<!-- 								<div id="colorCard" style="display: none;" > -->
<!-- 								<img src="<%=path%>/modules/monitor/image/123.jpg" style="width: 600px; height: 50px;text-align: center;margin-left: 350px;"/> -->
<!-- 								</div> -->
							</div>
						</div>
						
					</div>
				</div>
			</div>
			</form>
		</div>

	<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
	<%--<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>--%>
<%--<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>--%>

<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
<script type="text/javascript" src="<%=path%>/modules/alarm/js/alarm.js"></script>
</body>
</html>
