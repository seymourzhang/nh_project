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

<script src="<%=path%>/framework/js/bootstrap_table/bootstrap-table.js"></script>
<link href="<%=path%>/framework/js/bootstrap_table/bootstrap-table.css" rel="stylesheet" />
<script src="<%=path%>/framework/js/bootstrap_table/locale/bootstrap-table-zh-CN.js"></script>

<link rel="stylesheet" href="<%=path%>/framework/js/bootstrap_editable/1.5.1/css/bootstrap-editable.css">
<script src="<%=path%>/framework/js/bootstrap_editable/1.5.1/js/bootstrap-editable.js"></script>
<script src="<%=path%>/framework/js/bootstrap_table/extensions/editable/bootstrap-table-editable.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=path%>/framework/js/charts/highcharts.js"></script>
	<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>

<!-- <script type="text/javascript" src="<%=path%>/framework/js/charts/exporting.src.js"></script> -->
<script type="text/javascript">
var isRead="1";//"${pd.write_read}";//菜单是否只读

</script>

</head>
<body style="background-color: #ffffff;">
	<!--  <div class="container-fluid" id="main-container" style="background-color: #ffffff;"> -->
	<div id="page-content" class="clearfix">
		<form action="" method="post" id="alarmForm">
			<%--功能栏--%>
			<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
				<div class="row-fluid">
					<div class="span12">
						<div style="padding-left: 10px;">
							<%@ include file="../../framework/org.jsp"%>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span_customer2>设置人员</span_customer2>
							<select id="config_person"  name="config_person" tabindex="1" style="width: 148px;" onchange="searchData();">
<!-- 								<option value="1">全部</option> -->
<!-- 								<option value="1">张三</option> -->
<!-- 								<option value="2">李四</option> -->
                                    <option value="">全部</option>
                                    <c:if test="${!empty userList}">
														<c:forEach var="user" items="${userList}">
															<option value="${user.id }">${user.user_real_name}</option>
														</c:forEach>
													</c:if>
							</select>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<div class="span12">
						<div style="padding-left: 10px;height: 40px; float:left; display:inline;">
							<span_customer2>设置类别</span_customer2>
							<select id="alarm_type"  name="alarm_type" tabindex="1" style="width: 148px;display:inline;" onchange="searchData();">
								<option value="1">温度设置</option>
								<option value="2">光照设置</option>
								<option value="3">二氧化碳设置</option>
								<option value="4">基础设置</option>
							</select>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span_customer2>开始时间</span_customer2>
							<div class="input-append date date-picker1" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months" style="display:inline;">
								<input type="text" name="start_date" id="start_date" style="width: 96px;" onchange="searchData();" readonly>
								<span class="add-on"><i class="icon-calendar"></i></span>
							</div>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span_customer2>截止时间</span_customer2>
							<div class="input-append date date-picker1" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
								<input type="text" name="end_date" id="end_date" style="width: 96px;" onchange="searchData();" readonly>
								<span class="add-on"><i class="icon-calendar"></i></span>
							</div>
<!-- 							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
<!-- 							<button type="button" class="btn blue" style="display:inline-block;margin-bottom: 10px;" onclick="searchData();" > -->
<!-- 								<i class="icon-search"></i>&nbsp;查询 -->
<!-- 							</button> -->
						</div>
					</div>
				</div>
			</div>

			<div class="row-fluid">
				<div class="span12">
					<div id="TemperatureCurveFrame" style="display:block;">
						<table id="TemperatureCurveTable"></table>
					</div>

					<div id="CarbonFrame" style="display:none;" >
						<table id="CarbonTable"></table>
					</div>

					<div id="NegativePressureFrame" style="display:none;" >
						<table id="NegativePressureTable"></table>
					</div>

					<div id="BasisFrame" style="display:none;" >
						<table id="BasisTable"></table>
					</div>
				</div>
			</div>


		</form>
	</div>
	<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>

	<script type="text/javascript" src="<%=path%>/modules/alarm/js/alarmHis.js"></script>

</body>
</html>
