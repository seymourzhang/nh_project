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
<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>
<script type="text/javascript" src="<%=path%>/modules/alarm/js/alarmCurr.js"></script>
	<script>
        jQuery(document).ready(function() {
            App.init(); // initlayout and core plugins
// 		reflushAlarmCurr();
// 		var win_h = $(window).height() - 208;
// 		 $("#real_date_table").css("min-height", win_h);
// 		$("#page-content").css("min-height", win_h);
        });

        var corporation_id = "${corporation_id}";
        var farm_id = "${farm_id}";

	</script>
</head>

<body style="background-color: #ffffff;">

	<div id="page-content" class="clearfix">
		<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
			<div class="span11" >
				<div style="padding-left: 10px;">
					<%@ include file="../../framework/org.jsp"%>
				</div>
			</div>
			<div class="span1" align="right" style="padding-top: 5px; ">
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
							<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpUnprocessed.png" style="width: 200px;height: 75px;"></td>
							<td style="text-align: left;">处理时间列显示“未处理”时，表示手机端尚未对该报警进行任何处理</td>
						</tr>
						<tr>
							<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpProcessedTime.png" style="width: 200px;height: 75px;"></td>
							<td style="text-align: left;">持续时间列中的数值，表示从报警发生到监测指标恢复正常所经过的时间，以分钟计</td>
						</tr>
						</tbody>
					</table>
				</div>
			</div>

		</div>

		<form id="farmData" method="post">
			<div class="row-fluid">
				<div class="span12">
					<table class="table table-striped table-bordered table-hover" >
						<thead style="color: #fff; background-color: #2586C4;">
						<tr>
							<th style="text-align: center;width: 15%;"><img src="<%=path %>/modules/monitor/image/farm.png" style="width: 25px; height: 25px;" /></th>
							<th style="text-align: center;width: 10%;"><img src="<%=path %>/modules/monitor/image/Shape.png" style="width: 25px; height: 25px;" /></th>
							<th style="text-align: center;width: 15%;"><img src="<%=path %>/modules/monitor/image/Group 9.png" style="width: 25px; height: 25px;" /></th>
							<th colspan="2" style="text-align: center;width: 20%;"><img src="<%=path %>/modules/monitor/image/fact.png" style="width: 25px; height: 25px;" /></th>
							<th colspan="3" style="text-align: center;width: 30%;"><img src="<%=path %>/modules/monitor/image/Fill 166.png" style="width: 25px; height: 25px;" /></th>
							<th style="text-align: center;width: 10%;"><img src="<%=path %>/modules/monitor/image/worker.png" style="width: 25px; height: 25px;" /></th>
						</tr>
						<tr>
							<th style="text-align: center;width: 15%;">农场</th>
							<th style="text-align: center;width: 10%;">栋舍</th>
							<th style="text-align: center;width: 15%;">报警类型</th>
							<th style="text-align: center;width: 10%;">报警值</th>
							<th style="text-align: center;width: 10%;">实际值</th>
							<th style="text-align: center;width: 10%;">报警时间</th>
							<th style="text-align: center;width: 10%;">处理时间</th>
							<th style="text-align: center;width: 10%;">持续时间</th>
							<th style="text-align: center;width: 10%;">响应人员</th>
						</tr>

						</thead>
						<tbody id="tbodyAlarmCurrList">
						</tbody>
					</table>
				</div>
			</div>
		</form>
	</div>

	<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
	<!-- 确认窗口 -->
</body>
</html>
