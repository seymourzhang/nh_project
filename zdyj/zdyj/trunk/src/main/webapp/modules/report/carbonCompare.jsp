<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<link rel="stylesheet" href="<%=path%>/framework/css/bootstrap.min.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/font-awesome.min.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/style.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/style-responsive.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/style-metro.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/default.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/datepicker.css" />
<script type="text/javascript" src="<%=path%>/framework/jquery/jquery-1.11.2.min.js"></script>
<script src="<%=path%>/framework/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/framework/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=path%>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=path%>/framework/js/charts/highcharts.js"></script>
<script type="text/javascript" src="<%=path%>/framework/js/charts/exporting.src.js"></script>
<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>
<%-- <script type="text/javascript" src="<%=path%>/modules/report/js/temProfile.js"></script> --%>
</head>
<script type="text/javascript">
	 var flag;
     $(document).ready(function(){
		 $(".date-picker").datepicker({
		     	language: 'zh-CN',
		         autoclose: true,
		         todayHighlight: true
		     });
		 
		 $("select[name='farmId']").change(function() {
				$.ajax({
					type : "post",
					url : "<%=path%>/carbonReport/getHouse",
					data : {
						"farmId" : $("select[name='farmId']").val()
					},
					dataType: "json",
					success : function(result) {
						var list = result.obj;
						 $("select[name='houseId']").empty();
						for (var i = 0; i < list.length; i++) {
							$("select[name='houseId']").append("<option value=" + list[i].id + ">" + list[i].house_name+ "</option>");
						}
					}
				});
		}); 
		 
	
		 $("select[name='houseId']").change(function() {
				$.ajax({
					type : "post",
					url : "<%=path%>/carbonReport/getBatch",
					data : {
						"houseId" : $("select[name='houseId']").val()
					},
					dataType: "json",
					success : function(result) {
						var list = result.obj;
						 $("select[name='batchId']").empty();
						for (var i = 0; i < list.length; i++) {
							$("select[name='batchId']").append("<option value=" + list[i].batch_no+ ">" + list[i].batch_no + "</option>");
						}
					}
				});
		});  
    
		function time(){
			var time=new Date();
			var h=time.getFullYear();
	     	var m=time.getMonth()+1;
		    var d=time.getDate();
			   return h+"-"+m+"-"+d;
			}
		document.getElementById("queryTime").value = time();
		 
		$("#qued").click(function(){
			setInterval(function(){
				if(flag==true){
					parent.layer.closeAll();
				}
				},500)
		}); 
		 
	});
     
     function confirm(){
//    	 alert("aaassdf	");
    	 var fid= $("#farm_id").val();
    	 var hc= $("#house_code").val();
    	 var bid= $("#batchId").val();
    	 var qt= $("#queryTime").val();
    	 flag = true;
    	 if(fid==""){
    		 $("#compare_msg").html("请选择农场！");
    		 flag = false;
    	 }else if(hc==""){
    		 $("#compare_msg").html("请选择栋舍！");
    		 flag = false;
    	 }else if(bid==""){
    		 $("#compare_msg").html("请选择批次！");
    		 flag = false;
    	 }
		if(flag == true){
	    	 window.parent.document.getElementById('farmId2').value = fid;
	    	 window.parent.document.getElementById('houseId2').value = hc;
	    	 window.parent.document.getElementById('batchId2').value = bid;
	    	 window.parent.document.getElementById('queryTime2').value = qt;
	    	 var param =$.serializeObject($('#choose_form'));
	    	 $.ajax({
	    		url : "<%=path%>/carbonReport/queryCarbonReport",
				data : param,
				type : "post",
				dataType : "json",
				success : function(result) {
					var xpNames = new Array();//清空X坐标
					
					var newCo2 = new Array();//实际co2
					var newHighAlarmCo2 = new Array();//高报co2
	
					var lists = result.obj;
					for (var i = 0; i < lists.length; i++) {
						if (lists[i].time == null) {
							xpNames.push(lists[i].date.substring(0, 10));
						} else {
							xpNames.push(lists[i].time + '点');
						}
						newCo2.push(lists[i].co2);
						newHighAlarmCo2.push(lists[i].high_alarm_co2);
					}
						
						window.parent.createChar3(xpNames,newCo2,newHighAlarmCo2);
				}
			})
		}
     }
     
     function closeB(){
    	 parent.layer.closeAll();
     }
</script>
<body>
	<div class="portlet-body form"
		style="padding-top: 20px; margin-left: -30px;">
		<!-- BEGIN FORM-->
		<form id="choose_form" class="form-horizontal">
			<input type="hidden" name="buttonValue" value="" id="buttonValue">
			<div class="control-group" style="float: left; display: inline;">
				<label class="control-label" style="width: 100px;">农场:</label>
				<div class="controls" style="margin-left: 110px;">
					<select class="m-wrap" id="farm_id" name="farmId"
						style="width: 220px;">
						<option value="">请选择</option>
						<c:if test="${!empty farmList}">
							<c:forEach var="farm" items="${farmList}">
								<option value="${farm.id }">${farm.farm_name_chs }</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
			</div>

			<div class="control-group" style="float: left; display: inline;">
				<label class="control-label" style="width: 50px;">栋舍:</label>
				<div class="controls" style="margin-left: 55px;">
					<select class="m-wrap" id="house_code" name="houseId"
						tabindex="1" style="width: 220px;">
						<option value="">请选择</option>
						<c:if test="${!empty houseList}">
							<c:forEach var="house" items="${houseList}">
								<option value="${house.id }">${house.house_name}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
			</div>

			<div class="control-group" style="float: left; display: inline;">
				<label class="control-label" style="width: 100px;">批次:</label>
				<div class="controls" style="margin-left: 110px;">
					<select class="m-wrap" id="batchId" name="batchId" tabindex="1"
						style="width: 220px;">
						<option value="">请选择</option>
						<c:if test="${!empty batchList}">
							<c:forEach var="batch" items="${batchList}">
								<option value="${batch.batch_no}">${batch.batch_no}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
			</div>

			<div class="control-group" style="float: left; display: inline;">
				<label class="control-label" style="width: 50px;">日期:</label>
				<div class="controls" style="margin-left: 55px;">
					<div class="input-append date date-picker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
						<input class="m-wrap m-ctrl-medium date-picker" type="text"
							name="queryTime" id="queryTime"
							style="height: 34px; width: 193px;" /><span class="add-on"><i
							class="icon-calendar"></i></span>
					</div>
				</div>
			</div>
			<div class="control-group"
				style="clear: both; height: 30px; text-align: center;">
				<label class="control-label"
					style="padding-left: 140px; color: red; width: 500px; text-align: center;"
					id="compare_msg"></label>
			</div>

			<div class="form-actions" style="padding-left: 230px;">
				<button type="button" class="btn blue" onclick="confirm();"
					id="qued">
					<i class="icon-ok"></i>&nbsp;确 定&nbsp;&nbsp;&nbsp;
				</button>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<button type="button" class="btn" onclick="closeB();">&nbsp;&nbsp;&nbsp;取 消&nbsp;&nbsp;&nbsp;</button>
			</div>
		</form>
		<!-- END FORM-->
	</div>
</body>
</html>
