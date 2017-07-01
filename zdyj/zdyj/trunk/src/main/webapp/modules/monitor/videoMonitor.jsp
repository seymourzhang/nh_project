<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%  
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title></title>
	<meta charset="utf-8" />
	<%@ include file="../../framework/inc.jsp"%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
	<meta http-equiv="Expires" content="0" />
	<script type="text/javascript" src="<%=path%>/modules/monitor/js/videoMonitor.js"></script>
	<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>
	<script type="text/javascript" src="<%=path%>/modules/monitor/js/monitor.js"></script>
</head>
<script>
	var houseId = "${pd.house_id}";
// 	jQuery(document).ready(function() {
// 		$("#monitorExpandForm").slideUp(200);
// 		$("#monitorExpand").removeClass("collapse").addClass("expand");
// 		App.init(); // initlayout and core plugins
// 		$("#monitor_date_table").removeClass("table-hover");
// 	})
</script>
<body style="background-color: #ffffff;">
	<div id="page-content" class="clearfix"  style="padding-top: 10px;">
		<div class="row-fluid" style="background-color: #ffffff;">
			<form  method="post" style="background-color: #ffffff;">
				<div class="span12">
					<!-- BEGIN PORTLET-->
					<div class="portlet box blue1">
						<div class="portlet-title">
							<div class="caption">
								<i class="icon-reorder"></i>检索条件
							</div>
							<div class="tools">
								<a id="monitorExpand" href="javascript:;" class="collapse"></a>
							</div>
							<!-- <div class="actions">

								<a href="javascript:;" class="btn green"><i class="icon-search"></i> 查询</a>

							</div> -->

						</div>
						<div class="portlet-body form1" id="monitorExpandForm">
							<!-- BEGIN FORM-->
							<div class="form-horizontal" style="height: 40px;">
								<div style="height: 20px;">
								<%@ include file="../../framework/org.jsp"%>
<!-- 									<div class="span3"> -->
<!-- 										<div class="control-group"> -->
<!-- 											<label class="control-label" style="width: 50px;">农场</label> -->
<!-- 											<div class="controls" style="margin-left: 55px;"> -->
<!-- 												<select id="farmId" class="m-wrap span12" tabindex="1" name="farmId"> -->
													<!-- 	<option value="">全部</option> -->
<!-- 													<c:if test="${!empty farmList}"> -->
<!-- 														<c:forEach var="farm" items="${farmList}"> -->
<!-- 															<option value="${farm.id }" <c:if test="${pd.farm_id==farm.id}">selected</c:if>>${farm.farm_name_chs }</option> -->
<!-- 														</c:forEach> -->
<!-- 													</c:if> -->
<!-- 													<%--<c:if test="!empty farmList1"> -->
<!-- 														<c:forEach var="farm" items="farmList1"> -->
<!-- 															<option value="${farm.farm_id}">${farm.farm_name}</option> -->
<!-- 														</c:forEach> -->
<!-- 													</c:if>--%> -->
<!-- 												</select> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
									<!--/span-->

<!-- 									<div class="span3"> -->

<!-- 										<div class="control-group"> -->
<!-- 											<label class="control-label" style="width: 30px;">栋舍</label> -->
<!-- 											<div class="controls" style="margin-left: 35px;"> -->
<!-- 												<select id="houseId" class="m-wrap span12" tabindex="1"> -->
<!-- 													<c:if test="${!empty houseList}"> -->
<!-- 														<c:forEach var="house" items="${houseList}"> -->
<!-- 															<option value="${house.id}" <c:if test="${pd.house_code==house.id}">selected</c:if>>${house.house_name}</option> -->
<!-- 														</c:forEach> -->
<!-- 													</c:if> -->
													<!-- <option value="">全部</option> -->
<!-- 													<%--  <c:if test="${!empty houseList}"> -->
<!-- 													 <c:forEach var="house" items="${houseList}"> -->
<!-- 													 <option value="${house.id }">${house.house_name}</option> -->
<!-- 													 </c:forEach> -->
<!-- 													 </c:if> --%> -->
<!-- 												</select> -->
<!-- 											</div> -->
<!-- 										</div> -->

<!-- 									</div> -->

								</div>

							</div>

							<!-- END FORM-->

						</div>

					</div>

					<!-- END PORTLET-->

					<div class="portlet box blue1">
						<div class="portlet-title">
							<div class="caption">
								<i class="icon-globe"></i>监控画面
							</div>
						</div>
                        <div id="divPlugin" class="plugin" align="center"></div>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
<script src="<%=path%>/modules/monitor/js/jquery-1.7.1.min.js"></script>
<script src="<%=path%>/modules/monitor/js/webVideoCtrl.js"></script>
<script src="<%=path%>/modules/monitor/js/demo-easy.js"></script>
</html>
