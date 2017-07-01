<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <meta charset="utf-8" />
  <%@ include file="../../framework/inc.jsp"%>
    <base href="<%=basePath%>">
    <link rel="stylesheet" href="<%=path %>/framework/css/datepicker.css" />

<script src="<%=path%>/framework/js/bootstrap_table/bootstrap-table.js"></script>
<link href="<%=path%>/framework/js/bootstrap_table/bootstrap-table.css" rel="stylesheet" />
<script src="<%=path%>/framework/js/bootstrap_table/locale/bootstrap-table-zh-CN.js"></script>

<link rel="stylesheet" href="<%=path%>/framework/js/bootstrap_editable/1.5.1/css/bootstrap-editable.css">
<script src="<%=path%>/framework/js/bootstrap_editable/1.5.1/js/bootstrap-editable.js"></script>
<script src="<%=path%>/framework/js/bootstrap_table/extensions/editable/bootstrap-table-editable.js"></script>
	<script type="text/javascript" src="<%=path%>/modules/alarm/js/alarm.js"></script>
  </head>
  <script>
  jQuery(document).ready(function() {
	  document.getElementById("alarmType").value = '${pd.alarm_type}';
// 	  document.getElementById("farmId").value = '${pd.farmId}';
// 	  document.getElementById("houseId").value = '${pd.houseId}';
	  alarmTypeHide();
	});
  
	function alarmTypeHide(){
		if($("#alarmType").val()=="2"){
			$("#pressure").css("display", "block");
			$("#temp").css("display", "none");
			$("#co2").css("display", "none");
			$("#deprivation").css("display", "none");
		}else if($("#alarmType").val()=="3"){
			$("#pressure").css("display", "none");
			$("#temp").css("display", "none");
			$("#co2").css("display", "block");
			$("#deprivation").css("display", "none");
		}else if($("#alarmType").val()=="4"){
			$("#pressure").css("display", "none");
			$("#temp").css("display", "none");
			$("#co2").css("display", "none");
			$("#deprivation").css("display", "block");
		}else{
			$("#pressure").css("display", "none");
			$("#temp").css("display", "block");
			$("#co2").css("display", "none");
			$("#deprivation").css("display", "none");
		}
	}
	
	function submitForm(){
		var day_age=$("input[name='day_age']").val();
		var set_temp=$("input[name='set_temp']").val();
		var high_alarm_temp=$("input[name='high_alarm_temp']").val();
		var low_alarm_temp=$("input[name='low_alarm_temp']").val();
		var high_alarm_negative_pressure=$("input[name='high_alarm_negative_pressure']").val();
		var low_alarm_negative_pressure=$("input[name='low_alarm_negative_pressure']").val();
		var high_alarm_co2=$("input[name='high_alarm_co2']").val();
		var set_water_deprivation=$("input[name='set_water_deprivation']").val();
		var high_water_deprivation=$("input[name='high_water_deprivation']").val();
		var low_water_deprivation=$("input[name='low_water_deprivation']").val();
		if(day_age =="" ){
				$('#addAlarm_msg').html("日龄不能为空！");
				return false;
		}
		if(day_age < 0 ){
				$('#addAlarm_msg').html("日龄不能负！");
				return false;
		}
		if($("#alarmType").val()=="1"){
			if(set_temp =="" ){
				$('#addAlarm_msg').html("目标温度不能为空！");
				return false;
		}else if(high_alarm_temp =="" ){
			$('#addAlarm_msg').html("高报温度不能为空！");
			return false;
	}else if(low_alarm_temp =="" ){
		$('#addAlarm_msg').html("低报温度不能为空！");
		return false;
      }	
		}else if($("#alarmType").val()=="2"){
			if(high_alarm_negative_pressure =="" ){
			$('#addAlarm_msg').html("高报负压不能为空！");
			return false;
	}else if(low_alarm_negative_pressure =="" ){
		$('#addAlarm_msg').html("低报负压不能为空！");
		return false;
      }	
		}else if($("#alarmType").val()=="3"){

			if(high_alarm_co2 =="" ){
			$('#addAlarm_msg').html("高报二氧化碳不能为空！");
			return false;
	}
		}else if($("#alarmType").val()=="4"){
			if(set_water_deprivation =="" ){
				$('#addAlarm_msg').html("目标饮水量不能为空！");
				return false;
		}else if(high_water_deprivation =="" ){
			$('#addAlarm_msg').html("高报饮水量不能为空！");
			return false;
	}else if(low_water_deprivation =="" ){
		$('#addAlarm_msg').html("低报饮水量不能为空！");
		return false;
      }	
		}
	showdiv('加载中，请稍候');
	return true;
}
	
	function  addAlarm(){
		var param =$.serializeObject($('#addAlarm_form'));
		if(submitForm()){
			$.ajax({
				url: "<%=path%>/alarm/addAlarm?farmId="+$("#orgId" + (count0rg - 1)).val().split(",")[1]+"&houseId="+$("#orgId" + count0rg).val().split(",")[1],
				data: param,
				type : "POST",
				dataType: "json",
				success: function(result) {
					if(result.msg=='1'){
						parent.location.reload();   
						parent.layer.closeAll();
					}else{
						alert("添加失败！");
					}
				}
			});
		}
	}
	
	
</script>
  <body>
	<!-- BEGIN FORM-->
         <form id="addAlarm_form" class="form-horizontal"   >   
         
         <div class="portlet-body form1">
									<!-- BEGIN FORM-->
									<div class="form-horizontal" style="height: 40px;">
										<div style="height: 20px;">
                                     <%@ include file="../../framework/org.jsp"%>

                                         <div class="span2" style="width: 250px;">

												<div class="control-group">

													<label class="control-label" style="width: 90px;margin-left: -20px;">日龄</label>

													<div class="controls" style="margin-left: 75px;">
                                                      <input type="text" id="day_age" class="span6 m-wrap" style="width: 120px;" name="day_age">
													</div>

												</div>

											</div>

											<div class="span2" style="width: 250px;">

												<div class="control-group">

													<label class="control-label" style="width: 90px;margin-left: -20px;">报警类别</label>

													<div class="controls" style="margin-left: 75px;">
                                                      <select id="alarmType" class="m-wrap span12" name="alarm_type" tabindex="1" onchange="alarmTypeHide();">
														<option value="1">温度设置</option>
                                                        <option value="2">负压设置</option>
                                                        <option value="3">二氧化碳设置</option>
                                                        <option value="4">饮水量设置</option>
														</select>
													</div>

												</div>

											</div>
											
											<div class="span3">
														<button type="button" class="btn blue" onclick="addAlarm();" style="width: 100px;margin-left: 100px;"
																	id="qued">
																	&nbsp;确 定&nbsp;&nbsp;
																</button>
														</div>
										  <div class="span3">
														<button type="button" class="btn blue" onclick="closeB();" style="width: 100px;margin-left: 60px;"
																	id="qued">
																	&nbsp;取 消&nbsp;&nbsp;
																</button>
														</div>
										
										<!-- 温度 -->		
										<div id="temp" style="display: block;margin-left: 0px;float: left;width: 700px;">								
                                          <div class="span2" style="width: 250px;">

												<div class="control-group">

													<label class="control-label" style="width: 90px;margin-left: -19px;">目标温度</label>

													<div class="controls" style="margin-left: 75px;">
                                                      <input type="text" id="set_temp" class="span6 m-wrap" style="width: 230px;" name="set_temp">
													</div>

												</div>

											</div>
											
											<div class="span2" style="width: 250px;">

												<div class="control-group">

													<label class="control-label" style="width: 90px;margin-left: 30px;">高报温度</label>

													<div class="controls" style="margin-left: 125px;">
                                                      <input type="text" id="high_alarm_temp" class="span6 m-wrap" style="width: 230px;" name="high_alarm_temp">
													</div>

												</div>

											</div>
											
											<div class="span2" style="width: 250px;">

												<div class="control-group">

													<label class="control-label" style="width: 90px;margin-left: -19px;">低报温度</label>

													<div class="controls" style="margin-left: 75px;">
                                                      <input type="text" id="low_alarm_temp" class="span6 m-wrap" style="width: 230px;"  name="low_alarm_temp">
													</div>

												</div>

											</div>
											</div>
											
											<!-- 负压-->
											<div id="pressure" style="display:none;margin-left: 0px;float: left;width: 700px;">								
											
											<div class="span2" style="width: 250px;">

												<div class="control-group">

													<label class="control-label" style="width: 90px;margin-left: -19px;">高报负压:</label>

													<div class="controls" style="margin-left: 75px;">
                                                      <input type="text" id="high_alarm_negative_pressure" class="span6 m-wrap" style="width: 230px;" name="high_alarm_negative_pressure">
													</div>

												</div>

											</div>
											
											<div class="span2" style="width: 250px;">

												<div class="control-group">

													<label class="control-label" style="width: 90px;margin-left: 30px;">低报负压:</label>

													<div class="controls" style="margin-left: 125px;">
                                                      <input type="text" id="low_alarm_negative_pressure" class="span6 m-wrap" style="width: 230px;" name="low_alarm_negative_pressure">
													</div>

												</div>

											</div>
											</div>
											
											<!-- 二氧化碳 -->
											<div id="co2" style="display:none;margin-left: 0px;float: left;width: 700px;">								
											
											<div class="span2" style="width: 250px;">

												<div class="control-group">

													<label class="control-label" style="width: 90px;margin-left: -19px;">高报二氧化碳:</label>

													<div class="controls" style="margin-left: 75px;">
                                                      <input type="text" id="high_alarm_co2" class="span6 m-wrap" style="width: 230px;" name="high_alarm_co2">
													</div>

												</div>

											</div>
											</div>
											
											<!-- 饮水量 -->
											<div id="deprivation" style="display: none;margin-left: 0px;float: left;width: 700px;">								
                                          <div class="span2" style="width: 250px;">

												<div class="control-group">

													<label class="control-label" style="width: 90px;margin-left: -19px;">目标饮水量:</label>

													<div class="controls" style="margin-left: 75px;">
                                                     <input type="text" id="set_water_deprivation" class="span6 m-wrap" style="width: 230px;" name="set_water_deprivation">
													</div>

												</div>

											</div>
											
											<div class="span2" style="width: 250px;">

												<div class="control-group">

													<label class="control-label" style="width: 80px;margin-left: 40px;">高报饮水量:</label>

													<div class="controls" style="margin-left: 125px;">
                                                      <input type="text" id="high_water_deprivation" class="span6 m-wrap" style="width: 230px;" name="high_water_deprivation">
													</div>

												</div>

											</div>
											
											<div class="span2" style="width: 250px;">

												<div class="control-group">

													<label class="control-label" style="width: 90px;margin-left: -19px;">低报饮水量:</label>

													<div class="controls" style="margin-left: 75px;">
                                                      <input type="text" id="low_water_deprivation" class="span6 m-wrap" style="width: 230px;" name="low_water_deprivation">
													</div>

												</div>

											</div>
											</div>
											
											<div class="span2" style="width: 250px;">
                                            <label class="control-label" style="padding-left: 140px;color: red; width:500px; text-align: center;" id="addAlarm_msg"></label>
											</div>
											
											<!--/span-->
										</div>
									</div>
									<!-- END FORM-->
								</div>  
           
		<script>
			function closeB(){
				parent.layer.closeAll();
			}
		</script>
		</form>
		<!-- END FORM-->  
		<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
  </body>
</html>
