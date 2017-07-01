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
<script type="text/javascript" src="<%=path%>/framework/js/charts/highcharts.js"></script>
<!-- <script type="text/javascript" src="<%=path%>/framework/js/charts/exporting.src.js"></script> -->
<%--<script type="text/javascript" src="<%=path%>/modules/report/js/alarmHist.js"></script>--%>
</head>
<%--<script>--%>
<%--var corporation_id = "${corporation_id}";--%>
<%--var farm_id = "${farm_id}";--%>
<%--var house_id = "${house_id}";--%>
<%--var batch_no = "${batch_no}";--%>
<%--var alarmHistIndex = "${alarmHistIndex}";--%>
<%--var userId = "${pd.user_id}";--%>
<%--</script>--%>

<body style="background-color: #ffffff;" >
<div id="page-content" class="clearfix">
	<form action="<%=path%>/alarmHist/queryAlarmHist2"  method="post" id="alarmHistForm">

			<div class="row-fluid">
				<div class="span12">
					<div class="tabbable tabbable-custom boxless" >
						<div class="row-fluid">
							<%--标签菜单栏--%>
                            <div class="span12" style="	margin-left: 0px;height: 10px">
								<ul class="nav nav-pills row-fluid" style="margin-bottom: 0px; ">
									<li  class="active" id="stateChartTab" style="text-align: center;width:33.3%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >
										<a href="#" onclick="forward0();"  data-toggle="tab" id="stateTab0">统计图</a>
									</li>
									<li  id="detailTab" style="text-align: center;width:33.3%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >
										<a href="#" onclick="forward2();"  data-toggle="tab" id="detailTab2">历史明细</a>
									</li>
									<li id="stateTab" style="text-align: center;width:33.1%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >
										<a href="#" onclick="forward3();"  data-toggle="tab" id="stateTab1">次数统计</a>
									</li>
								</ul>
							</div>
						</div>

						<%--功能栏--%>
						<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px; ">
							<div class="span12">
								<div class="row-fluid">
									<div class="span11">
										<div style="padding-left: 10px;display: none;" id = "searchBar1">
											<%@ include file="../../framework/org.jsp"%>
											&nbsp;&nbsp;&nbsp;&nbsp;
											<span_customer2>批次</span_customer2>
											<select id="batchId" tabindex="1" name="batchId" onchange="reflushAlarmHist3();" style="width: 148px;margin-top: 0px;">
											</select>
										</div>
										<div style="padding-left: 10px;" id = "searchBar0">
											<div id = "toolBarFarm">
											</div>
										</div>
									</div>
									<div class="span1" align="right" >
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
														<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpForwardDetail.png" style="width: 200px;height: 80px;"></td>
														<td style="text-align: left;">鼠标点击蓝色字体处，可跳转至报警明细界面</td>
													</tr>
													<tr >
														<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpProcessTime.png" style="width: 200px;height: 80px;"></td>
														<td style="text-align: left;">持续时间列中的数值，表示从报警发生到监测指标恢复正常所经过的时间，以分钟计</td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
									<%--<div class="span6" id="toolbarButton">--%>
<!-- 										<div id="state2"> -->
<!-- 										</div> -->
<!-- 										<div id="detail2"> -->
<!-- 											<input type="hidden" name="dateage" id="dateage"> -->
<!-- 											<span_customer2>批次</span_customer2> -->
<!-- 											<select id="batchId2" class="m-wrap span12" tabindex="1" name="batchId2" onchange="reflushAlarmHist3(2);" style="width: 148px;margin-top: 0px;"> -->
<!-- 											</select> -->
<!-- 										</div> -->
									<%--</div>--%>
								</div>
								<div class="row-fluid" id="searchBar2" style="display: none;">
									<div class="span12">
										<div style="padding-left: 10px;height: 40px; float:left; display:inline;">
											<span_customer2>报警类型</span_customer2>
											<select id="codeName" tabindex="1" name="codeName" onchange="reflushAlarmHist4();" style="width: 133px;display:inline;">
												<option value="">全部</option>
												<option value="高温报警" style="display: none;" id="cn1">高温报警</option>
												<option value="低温报警" style="display: none;" id="cn2">低温报警</option>
												<option value="点温差报警" style="display: none;" id="cn3">点温差报警</option>
												<option value="负压高" style="display: none;" id="cn4">负压高</option>
												<option value="负压低" style="display: none;" id="cn5">负压低</option>
												<option value="光照异常" style="display: none;" id="cn6">光照异常</option>
												<option value="二氧化碳超高" style="display: none;" id="cn7">二氧化碳超高</option>
												<option value="断电报警" style="display: none;" id="cn8">断电报警</option>
												<%--<option value="设备断开" style="display: none;" id="cn9">设备断开</option>--%>
<!-- 												<c:if test="${!empty alarmNameList}"> -->
<!-- 													<c:forEach var="alarm" items="${alarmNameList}"> -->
<!-- 														<option value="${alarm.biz_code }">${alarm.code_name}</option> -->
<!-- 													</c:forEach> -->
<!-- 												</c:if> -->
											</select>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<span_customer2>起始时间</span_customer2>
												<div class="input-append date date-picker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months" style="display:inline;">
													<input type="text" name="beginTime" id="beginTime" style="width: 98px;" readonly onchange="reflushAlarmHist4();" value="${pd.beginTime }">
													<span class="add-on"><i class="icon-calendar"></i></span>
												</div>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<span_customer2>截止时间</span_customer2>
												<div class="input-append date date-picker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
													<input type="text" name="endTime" id="endTime" style="width: 98px;" readonly onchange="reflushAlarmHist4();" value="${pd.endTime }">
													<span class="add-on"><i class="icon-calendar"></i></span>
												</div>
										</div>
									</div>
								</div>
							</div>
						</div>


						<div class="tab-pane active" id="tab_stateChart">
									<iframe id="iframe_tab_stateChart" name="alarm" width="99.8%" height="700px"   frameborder="no" allowtransparency="yes" src="">
									</iframe>
						</div>
						<div class="tab-pane" id="tab_detail">
							<div class="row-fluid">
								<div class="span12">
									<div id="alarmHistDetailFrame" align="center">
										<table id="alarmHistDetailTable"></table>
									</div>
								</div>
							</div>
						</div>
                       <div class="tab-pane" id="tab_state">
                                                        <div class="row-fluid">
							                                <div class="span12">
							                                    <div id="alarmHistFrame" align="center">
							                                        <table id="alarmHistTable"></table>
							                                    </div>
							                                </div>
							                            </div>
				     </div>


					</div>
				</div>
			</div>

	</form>
</div>
<script>
    var isRead="${pd.write_read}";//菜单是否只读
    var org_id = "${corporation_id}";
    var report_ip = "${pd.report_ip}";
    var report_port = "${pd.report_port}";
    var tabId = "${pd.tabId}";
    var searchDateTime=null;

    var cid = "${corporation_id}";
    var fid = "${farm_id}";
    var hid = "${house_id}";

    var corporation_id = "${corporation_id}";
    var farm_id = "${farm_id}";
    var house_id = "${house_id}";
    var batch_no = "${batch_no}";
    var alarmHistIndex = "${alarmHistIndex}";
    var userId = "${pd.user_id}";
</script>
<script type="text/javascript" src="<%=path%>/modules/report/js/alarmHist.js"></script>
<script type="text/javascript">
    <%--var isRead="${pd.write_read}";//菜单是否只读--%>
    <%--var org_id = "${pd.org_id}";--%>
    <%--var farm_id = "${pd.farm_id}";--%>
    <%--var house_id = "${pd.house_id}";--%>
    <%--var batch_no = "${pd.batch_no}";--%>
    <%--var report_ip = "${pd.report_ip}";--%>
    <%--var report_port = "${pd.report_port}";--%>
    <%--var tabId = "${pd.tabId}";--%>
    <%--var searchDateTime=null;--%>
    //    forward0();
</script>

<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
</body>
</html>
