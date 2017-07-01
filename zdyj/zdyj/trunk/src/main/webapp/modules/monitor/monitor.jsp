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
<script src="<%=path%>/framework/js/bootstrap_table/bootstrap-table.js"></script>
<link href="<%=path%>/framework/js/bootstrap_table/bootstrap-table.css" rel="stylesheet" />
<script src="<%=path%>/framework/js/bootstrap_table/locale/bootstrap-table-zh-CN.js"></script>

<link rel="stylesheet" href="<%=path%>/framework/js/bootstrap_editable/1.5.1/css/bootstrap-editable.css">
<script src="<%=path%>/framework/js/bootstrap_editable/1.5.1/js/bootstrap-editable.js"></script>
<script src="<%=path%>/framework/js/bootstrap_table/extensions/editable/bootstrap-table-editable.js"></script>
<%--<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>--%>
<script>
	var monitorIndex = "${monitorIndex}";
</script>
<script type="text/javascript" src="<%=path%>/modules/monitor/js/monitor.js"></script>
<%--<!--图标样式-->--%>
<%--<!--     <link rel="stylesheet" href="<%=path %>/framework/css/zTreeStyle.css" /> -->--%>

    <%--<!--主要样式-->--%>
<%--<!--     <script type="text/javascript" src="<%=path %>/framework/jquery/jquery.ztree.core.js"></script> -->--%>
<%--<!-- 	<script type="text/javascript" src="<%=path %>/framework/jquery/jquery.ztree.excheck.js"></script> -->--%>
</head>
<%--<script>--%>
<%--//var xNames = new Array();//X坐标--%>
<%--//--%>
<%--//--%>
<%--//var insideTemp1 = new Array();--%>
<%--//var insideTemp2 = new Array();--%>
<%--//var insideTemp3 = new Array();--%>
<%--//var insideTemp4 = new Array();--%>
<%--//--%>
<%--//var insideSetTemp = new Array();//目标温度--%>
<%--///* var insideAvgTemp = new Array();//实际温度 */--%>
<%--//var highAlarmTemp = new Array();//高报温度--%>
<%--//var lowAlarmTemp = new Array();//低报温度--%>
<%--//var outsideTemp = new Array();//室外温度--%>

<%--<c:forEach items="${TemProfile }" var="tep">--%>
	<%--xNames.push('${tep.time }点');--%>
	<%--insideTemp1.push(${tep.inside_temp1 });--%>
	<%--insideTemp2.push(${tep.inside_temp2 });--%>
	<%--insideTemp3.push(${tep.inside_temp3 });--%>
	<%--insideTemp4.push(${tep.inside_temp4 });--%>
	<%--insideSetTemp.push(${tep.inside_set_temp });--%>
	<%--&lt;%&ndash;/* insideAvgTemp.push(${tep.inside_avg_temp }); */&ndash;%&gt;--%>
	<%--highAlarmTemp.push(${tep.high_alarm_temp });--%>
	<%--lowAlarmTemp.push(${tep.low_alarm_temp });--%>
	<%--outsideTemp.push(${tep.outside_temp });--%>

<%--</c:forEach>--%>

<%--//jQuery(document).ready(function() {--%>
<%--//App.init(); // initlayout and core plugins--%>
<%--//// reflushMonitor();--%>
<%--//$("#lm1").attr("class","active");--%>
<%--//$("#se1").attr("class","selected");--%>
<%--//$("#z101").attr("class","active");--%>
<%--//$("#op1").attr("class","arrow open"); --%>
<%--//$("#monitorExpand").removeClass("collapse").addClass("expand");--%>
<%--//// $("#monitorExpandForm").slideUp(200);--%>
<%--//$("#monitor_date_table").removeClass("table-hover");--%>
<%--//--%>
<%--//$("#enableMonitorSet").change(function() {--%>
<%--//	OrgSearch(count0rg,num);--%>
<%--//});--%>
<%--//});--%>

	<%--//新增--%>
    <%--function monitorSetting(){--%>
        <%--layer.open({--%>
            <%--type: 2,--%>
            <%--title: "新增",--%>
            <%--skin: 'layui-layer-lan',--%>
            <%--area: ['450px', '450px'],--%>
            <%--content: '<%=path%>/monitor/monitorSet'--%>
        <%--});--%>
    <%--}--%>

    <%--function changeOrg() {--%>
    	<%--$.ajax({--%>
    		<%--type: "post",--%>
    		<%--url: "<%=path%>/monitor/getOrgBySetted",--%>
    		<%--data: {},--%>
    		<%--dataType: "json",--%>
    		<%--success: function (result) {--%>
    			<%--var setting = {--%>
    				<%--check: {--%>
    					<%--enable: true,--%>
    					<%--chkDisabledInherit:true--%>
    				<%--},--%>
    				<%--data: {--%>
    					<%--simpleData: {--%>
    						<%--enable: true--%>
    					<%--}--%>
    				<%--}--%>
    			<%--};--%>
    			<%--var zNodes = result.obj;--%>
    			<%--$.fn.zTree.init($("#treeDemo"), setting, zNodes);--%>
    		<%--}--%>
    	<%--});--%>
    <%--}--%>

	<%--console.log("farmList:" + ${farmList});--%>
<%--</script>--%>
<body style="background-color: #ffffff;">
	<%--引用跳转页面方法:siMenu()--%>
 	<%--<script type="text/javascript" src="<%=path %>/framework/js/head.js"></script>--%>
	<%--引用结束--%>
	<!--  <div class="container-fluid" id="main-container" style="background-color: #ffffff;"> -->
	<div id="page-content"  class="clearfix">
		<%--<div id="page-content"  class="clearfix" style="padding-top: 10px;">--%>
			<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
				<div class="span9" >
					<div style="padding-left: 10px;">
							<%--<span_customer2>分公司</span_customer2>--%>
							<%--<select style="width: 160px;"></select>--%>
							<%--&nbsp;&nbsp;&nbsp;&nbsp;--%>
							<%--<span_customer2>农场</span_customer2>--%>
							<%--<select style="width: 160px;"></select>--%>
							<%@ include file="../../framework/org.jsp"%>
							<input id="enableMonitorSet" class="reload" type="checkbox" value="checked"  style="width: 1px;display: none">
								&nbsp;&nbsp;
					</div>
				</div>
				<%--<div class="span7">--%>
					<%--<button id="btnShowEmptyHouse" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showEmptyHouse()">--%>
					<%--</button>--%>
				<%--</div>--%>
				<div class="span3" align="right">
					<button id="btnShowEmptyHouse" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showEmptyHouse(false)">
						<i class="icon-check" id ="iconShowWithOutEmptyHouse1" style="display: inline">在养栋舍</i>
						<i class="icon-check-empty" id ="iconShowEmptyHouse1" style="display: none">在养栋舍</i>
					</button>
					<button id="btnShowEmptyHouse2" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showEmptyHouse(true)">
						<i class="icon-check-empty" id ="iconShowWithOutEmptyHouse2" style="display: inline">全部栋舍</i>
						<i class="icon-check" id ="iconShowEmptyHouse2" style="display: none">全部栋舍</i>
					</button>
					&nbsp;
					<%@ include file="../../framework/help/help.jsp"%>
					<div id="helpContext" style="padding-top: 5px;display: none;">
						<table id = "helpTable" class="table">
							<thead>
								<tr>
									<td style="font-weight:bold;text-align: left;">图例</td>
									<td style="font-weight:bold;text-align: left;">说明</td>
								</tr>
							</thead>
							<tbody>
								<tr >
									<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpOrgSelect.png" style="width: 200px;height: 45px;"></td>
									<td style="text-align: left;">通过鼠标点选，可选择分公司或农场</td>
								</tr>
								<tr>
									<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpSort.png" style="width: 200px;height: 45px;"></td>
									<td style="text-align: left;">鼠标点击表头箭头处，可进行顺、倒排序</td>
								</tr>
								<tr>
									<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpStatus.png" style="width: 200px;height: 80px;"></td>
									<td style="text-align: left;">鼠标点击蓝色字体处，可跳转至相应的功能界面</td>
								</tr>
								<tr>
									<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpAlarmYerrow.png" style="width: 200px;height: 45px;"></td>
									<td style="text-align: left;">黄色字体处，表示实际监测值接近低报或高报值的10%以内</td>
								</tr>
								<tr>
									<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpAlarmRed.png" style="width: 200px;height: 45px;"></td>
									<td style="text-align: left;">红色字体处，表示实际监测值高于高报值或低于低报值</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>



			<form id="farmData" method="post" >
				<div class="row-fluid">
					<div class="span12">
							<table class="table table-striped table-bordered table-hover"  id="tbodyMonitorCurListTable">
<!-- 								<thead style="color: #fff; background-color: #2586C4;"> -->
<!-- 									<tr> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/farm.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/Shape.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/Fill 203.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/Group 9.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/goal.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/out.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/avg.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/pointTemp.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 										<th colspan="5" style="text-align: center;">舍内温度&nbsp;&nbsp;&nbsp;<img src="<%=path %>/modules/monitor/image/Group 11.png" style="height: 25px;" /></th> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/humidity.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/light.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/co2.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 										<th style="text-align: center;"><img src="<%=path %>/modules/monitor/image/Fill 166.png" style="width: 25px; height: 25px;" /></th> -->
<!-- 									</tr> -->
<!-- 									<tr> -->
<!-- 										<th style="text-align: center; border-right:12px solid #2586C4;">农场</th> -->
<!-- 										<th style="text-align: center;">栋舍</th> -->
<!-- 										<th style="text-align: center;">日龄</th> -->
<!-- 										<th style="text-align: center;">状态</th> -->
<!-- 										<th style="text-align: center;">目标</th> -->
<!-- 										<th style="text-align: center;">室外</th> -->
<!-- 										<th style="text-align: center;">平均</th> -->
<!-- 										<th style="text-align: center;">点温差</th> -->
<!-- 										<th style="text-align: center;">前区1</th> -->
<!-- 										<th style="text-align: center;">前区2</th> -->
<!-- 										<th style="text-align: center;">中区</th> -->
<!-- 										<th style="text-align: center;">后区1</th> -->
<!-- 										<th style="text-align: center;">后区2</th> -->
<!-- 										<th style="text-align: center;">湿度</th> -->
<!-- 										<th style="text-align: center;">光照</th> -->
<!-- 										<th style="text-align: center;"><span>CO</span><span style="font-size: 8px;">2</span></th> -->
<!-- 										<th style="text-align: center;">时间</th> -->
<!-- 									</tr> -->
<!-- 								</thead> -->
<!-- 								<tbody id="tbodyMonitorCurListTable" style="overflow:auto;"></tbody> -->
							</table>
						<%--</div>--%>
					</div>
				</div>
			</form>
	</div>


	<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>

<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
	<!-- 确认窗口 -->
</body>
</html>
