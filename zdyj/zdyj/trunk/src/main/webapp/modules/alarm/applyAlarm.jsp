<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <%@ include file="../../framework/inc.jsp"%>
    <link rel="stylesheet" href="<%=path %>/framework/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/style-metro.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/style.css"/>
    <link rel="stylesheet" href="<%=path %>/framework/css/bootstrap-fileupload.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/uniform.default.css" />
	<script type="text/javascript" src="<%=path%>/framework/jquery/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>
	<script type="text/javascript" src="<%=path%>/modules/alarm/js/alarm.js"></script>
  </head>
  <script>
  jQuery(document).ready(function() {

	});
  
  function  applyAlarm(){
		var param =$.serializeObject($('#applyAlarm_form'));
		$("#anniu1").attr("disabled", true);
		$("#anniu2").attr("disabled", true);
// 		$("#reflushText").css("display", "");
			$.ajax({
				url: "<%=path%>/alarm/applyAlarm",
				data: param,
				type : "POST",
				dataType: "json",
				success: function(result) {
// 					$("#reflushText").css("display", "none");
					if(result.msg=='1'){
// 						parent.location.reload(); 
                    $("#anniu1").attr("disabled", false);
		            $("#anniu2").attr("disabled", false);
		            layer.msg("应用成功！");
		            setTimeout("parent.layer.closeAll()", 2000);
					}else{
						layer.msg("应用失败！");
					}
				}
			});
		
	}
</script>
  <body>
<!--    <div class="portlet-body form" style="padding-top: 15px;margin-left: 10px;"> -->
	<!-- BEGIN FORM-->
<!--     <form action="<%=path %>/alarm/applyAlarm" class="form-horizontal"  onsubmit="return submitForm()" > -->
<!--         <form id="applyAlarm_form" class="form-horizontal"   >  -->			
			<div class="tab-content">
						<div class="tab-pane active" id="tab_1">
							<div class="portlet-body form1">
							<!-- BEGIN FORM-->
								<form id="applyAlarm_form">
									<input type="hidden" name="farmId" id="farmId"  value="${farmId}"/>
									<input type="hidden" name="houseId" id="houseId"  value="${houseId}"/>
									<input type="hidden" name="houseId2" id="houseId2" />
									<input type="hidden" name="alarm_type" id="alarm_type" value="${alarm_type}"/>
									<input type="hidden" name="dage" id="dage" value="${dage}"/>
									<div class="row-fluid">
										<div class="span12">
										</div>
									</div>
									<div class="row-fluid">
										<div class="span1">
										</div>
										<div class="span6">
											<span_customer2>当前农场:</span_customer2>
											${farm}
										</div>
										<div class="span4">
											<span_customer2>当前栋舍:</span_customer2>
											${house}
										</div>
										<div class="span1">
										</div>
									</div>

									<div class="row-fluid">
										<div class="span1">
										</div>
										<div class="span10">
											<c:if test="${!empty houseList}">
												<%int i=1; %>
												<span_customer2>应用至:</span_customer2>
												<c:forEach var="houselist" items="${houseList}">
													<c:if test="${houselist.org_code!=houseId }">
														<input type="radio" id="house<%=i %>" name="house" value="${houselist.org_code }" onclick="xuanze<%=i %>();" style="margin-top: 0px;"/>
														${houselist.org_name }
														<script>
                                                            function xuanze<%=i %>(){
                                                                document.getElementById("houseId2").value = $("#house"+<%=i %>).val();
                                                            }
														</script>
													</c:if>
													<%i++; %>
												</c:forEach>
											</c:if>
										</div>
										<div class="span1">
										</div>
									</div>


									<div class="row-fluid">
										<div class="span12" align="center">
											<div id = "reflushText" style="display:none;float:right;"><font style="margin-left: 60px;" color="#FF0000">刷新中,请稍后...</font></div>
											<button type="button" class="btn blue" onclick="applyAlarm()" id="anniu1"><i class="icon-ok"></i>&nbsp;确 定</button>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<button type="button" class="btn" onclick="closeB()" id="anniu2">&nbsp;取 消</button>
										</div>
									</div>


								</form>
								<!-- end from -->
							</div>
						</div>

					</div>
			
			
		<script>
			function closeB(){
				parent.layer.closeAll();
			}
		</script>
<!-- 		</form> -->
		<!-- END FORM-->  
<!-- 	</div> -->
  </body>
</html>
