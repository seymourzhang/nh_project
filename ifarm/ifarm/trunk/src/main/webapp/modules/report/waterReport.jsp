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
<script type="text/javascript" src="<%=path%>/framework/js/charts/exporting.src.js"></script>

</head>
<script>
var houseId = "${pd.house_id}";
var xNames = new Array();//X坐标

var waterConsumption = new Array();//实际耗水
var setWaterDeprivation = new Array();//目标耗水
var highWaterDeprivation = new Array();//高报耗水
var lowWaterDeprivation = new Array();//低报耗水

<c:forEach items="${WaterReport}" var="wr">
	xNames.push('${wr.time }点');
	waterConsumption.push(${wr.water_consumption});  //实际耗水
	setWaterDeprivation.push(${wr.set_water_deprivation}); //目标耗水
	highWaterDeprivation.push(${wr.high_water_deprivation}); //高报耗水
	lowWaterDeprivation.push(${wr.low_water_deprivation}); //低报耗水
</c:forEach>
</script>

<body style="background-color: #ffffff;">
			<div id="page-content" class="clearfix"  style="padding-top: 10px;"> 
				<div class="row-fluid" style="background-color: #ffffff;">
					<form  method="post" style="background-color: #ffffff;">
					<input type="hidden" name="farmId2" value="" id="farmId2">
					<input type="hidden" name="houseId2" value="" id="houseId2">
					<input type="hidden" name="batchId2" value="" id="batchId2">
					<input type="hidden" name="queryTime2" value="" id="queryTime2">
					  <div class="span12">
							<!-- BEGIN PORTLET-->
							<div class="portlet box blue1">
								<div class="portlet-title">
									<div class="caption">
										<i class="icon-reorder"></i>检索条件
									</div>
									<!-- <div class="actions">

										<a href="javascript:;" class="btn green"><i class="icon-search"></i> 查询</a>

									</div> -->

								</div>
								<div class="portlet-body form1">
									<!-- BEGIN FORM-->
									<div class="form-horizontal" style="height: 40px;">
										<div style="height: 20px;">
										<%@ include file="../../framework/org.jsp"%>
<!-- 											<div class="span2"> -->
<!-- 												<div class="control-group"> -->
<!-- 													<label class="control-label" style="width: 50px;">农场</label> -->
<!-- 													<div class="controls" style="margin-left: 55px;"> -->
<!-- 														<select id="farmId" class="m-wrap span12" tabindex="1" name="farmId" >	 -->
<!-- 			                                                 <c:if test="${!empty farmList}"> -->
<!-- 			                                                 <c:forEach var="farm" items="${farmList}"> -->
<!--                                                                  <option value="${farm.id }" <c:if test="${pd.farm_id==farm.id}">selected</c:if>>${farm.farm_name_chs }</option> -->
<!-- 			                                                 </c:forEach> -->
<!-- 		                                                 </c:if> -->
<!-- 														</select> -->
<!-- 													</div> -->
<!-- 												</div> -->
<!-- 											</div> -->

											<!--/span-->

<!-- 											<div class="span2"> -->

<!-- 												<div class="control-group"> -->
<!-- 													<label class="control-label" style="width: 30px;">栋舍</label> -->
<!-- 													<div class="controls" style="margin-left: 35px;"> -->
<!-- 														<select id="houseId" class="m-wrap span12" tabindex="1" > -->
<!-- 			                                             <c:if test="${!empty houseList}"> -->
<!-- 			                                                 <c:forEach var="house" items="${houseList}"> -->
<!--                                                                  <option value="${house.id}" <c:if test="${pd.house_id==house.id}">selected</c:if>>${house.house_name}</option> -->
<!-- 			                                                 </c:forEach> -->
<!--                                                          </c:if> -->
<!-- 														</select> -->
<!-- 													</div> -->
<!-- 												</div> -->

<!-- 											</div> -->

											<!--/span-->

											<!--/span-->
											<div class="span2">
												<div class="control-group">
													<label class="control-label" style="width: 30px;">批次</label>
													<div class="controls" style="margin-left: 35px;">
														<select id="batchId" class="m-wrap span12" tabindex="1"  name="batchId">
<%-- 			                                                 <c:if test="${!empty batchList}"> --%>
<%-- 			                                                 <c:forEach var="batch" items="${batchList}"> --%>
<%-- 			                                                 <option value="${batch.batch_no}">${batch.batch_no}</option> --%>
<%-- 			                                                 </c:forEach> --%>
<%-- 			                                                 </c:if> --%>
														</select>
													</div>
												</div>
											</div>

											<!--/span-->
											<!--/span-->

											<div class="span2">

												<div class="control-group">

													<label class="control-label" style="width: 30px;">日期</label>

													<div class="controls" style="margin-left: 35px;">
														<div class="input-append date date-picker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">

															<input class="m-wrap  span11 m-ctrl-medium date-picker " type="text" name="queryTime" id="queryTime" /><span class="add-on"><i class="icon-calendar"></i></span>
			
														</div>

													</div>

												</div>

											</div>

											<!--/span-->
											
											<!-- 对比 -->
										       <div style="margin-left: 80px; float:left">
						                          <input id="btn" type="button" onclick="waterUrl();" value="对比">
									           </div>
											<!-- /对比 -->

										</div>

									</div>

									<!-- END FORM-->
									
								</div> 

							</div>

							<!-- END PORTLET-->

							<div class="portlet box blue1">
								<div class="portlet-title">
									<div class="caption">
										<i class="icon-globe"></i>饮水量曲线图
									</div>
								</div>

								<div class="portlet-body" id="user_date_table">
									<input type="hidden" name="buttonValue" id="buttonValue">
									<div id="container" class="form-horizontal" ></div>
									<div class="portlet-body form1"> 
										<div class="span4">
											<div class="control-group" style="padding-left: 100px;">
												<input type="button"  style="width: 80px;" value="近一个月" onclick="javascript:buttonMonth();">
											</div>
										</div>
										<div class="span4">
											<div class="control-group" style="padding-left: 100px;">
												<input type="button"  style="width: 80px;" value="近一周" onclick="javascript:buttonWeek();">
											</div>
										</div>
										<div class="span4">
											<div class="control-group" style="padding-left: 100px;">
												<input type="button"  style="width: 80px;" value="当日" onclick="javascript:buttonDay();">
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<!-- 对比图 -->
							<div id="waterRep" style="display:none;" class="portlet box blue1">
								<div class="portlet-title">
									<div class="caption">
										<i class="icon-globe"></i>对比曲线图
									</div>
								</div>

								<div class="portlet-body" id="user_date_table">
									<input type="hidden" name="btValue" id="btValue">
									<div id="compareRep" class="form-horizontal" ></div>
									<div class="portlet-body form1"> 
										<div class="span4">
											<div class="control-group" style="padding-left: 100px;">
												<input type="button"  style="width: 80px;" value="近一个月" onclick="javascript:btMonth();">
											</div>
										</div>
										<div class="span4">
											<div class="control-group" style="padding-left: 100px;">
												<input type="button"  style="width: 80px;" value="近一周" onclick="javascript:btWeek();">
											</div>
										</div>
										<div class="span4">
											<div class="control-group" style="padding-left: 100px;">
												<input type="button"  style="width: 80px;" value="当日" onclick="javascript:btDay();">
											</div>
										</div>
									</div>
								</div>
							</div>
							
						</div>
					</form>
				</div> 
		 </div> 
<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=path%>/modules/report/js/waterReport.js"></script>
</body>
</html>
