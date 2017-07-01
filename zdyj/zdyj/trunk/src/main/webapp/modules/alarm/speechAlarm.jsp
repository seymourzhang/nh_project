<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <%@ include file="../../framework/inc.jsp"%>
    <base href="<%=basePath%>">
    <link rel="stylesheet" href="<%=path %>/framework/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/style-metro.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/style.css"/>
    <link rel="stylesheet" href="<%=path %>/framework/css/bootstrap-fileupload.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/uniform.default.css" />
	<script type="text/javascript" src="<%=path%>/framework/jquery/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>
<!-- 	<script type="text/javascript" src="<%=path%>/modules/alarm/js/alarm.js"></script> -->
  </head>
  <script>
  var objAlarmUser = new Object();
  function initObjAlarmUser(){
	  objAlarmUser.userId4 = $("#userId4").val();
	  objAlarmUser.userId5 = $("#userId5").val();
	  objAlarmUser.userId6 = $("#userId6").val();
	};
  
  jQuery(document).ready(function() {
	  document.getElementById("userId4").value = '${alarmUser1}';
	  document.getElementById("userId5").value = '${alarmUser2}';
	  document.getElementById("userId6").value = '${alarmUser3}';
// 	  document.getElementById("status").value = '${status}';
      initObjAlarmUser();
	});
  
  function submitForm(){
	  if(document.getElementById("userId4").value == document.getElementById("userId5").value ||
			  document.getElementById("userId5").value == document.getElementById("userId6").value ||
			  document.getElementById("userId4").value == document.getElementById("userId6").value){
		  layer.msg("不允许选择相同的用户！");
		  return false;
	  }
	  return true;
  }
  
  function  bindingUser(){
	    if(objAlarmUser.userId4 == $("#userId4").val() && objAlarmUser.userId5 == $("#userId5").val() && objAlarmUser.userId6 == $("#userId6").val()){
	    	layer.msg("未修改数据，无法保存！");
	    	return;
	    }
		var param =$.serializeObject($('#bindingUser_form'));
		if(submitForm()){
			$.ajax({
				url: "<%=path%>/alarm/bindingUser",
				data: param,
				type : "POST",
				dataType: "json",
				success: function(result) {
					if(result.msg=='1'){
						layer.msg("保存成功！", function(index) {
							parent.location.reload();   
							parent.layer.closeAll();
						});
					}else{
						layer.msg("保存失败！");
					}
				}
			});
		}
	}
  
</script>
  <body>
	<!-- BEGIN FORM-->
<!--     <form action="<%=path %>/alarm/bindingUser" class="form-horizontal"  onsubmit="return submitForm()" > -->
   <form id="bindingUser_form" class="form-horizontal"   >   
        <input type="hidden" name="farmId" id="farmId"  value="${farmId}"/>							
		<input type="hidden" name="houseId" id="houseId"  value="${houseId}"/>
		<input type="hidden" name="userId1" id="userId1"  value="${alarmUser1}"/>
		<input type="hidden" name="userId2" id="userId2"  value="${alarmUser2}"/>
		<input type="hidden" name="userId3" id="userId3"  value="${alarmUser3}"/>
		<input type="hidden" name="alarm_type" id="alarm_type"  value="${alarm_type}"/>
		<input type="hidden" name="status" value="N" id="status">
	<table style="margin-left: 20px;height: 10px;width: 350px;margin-top: 10px;">
	<tr style="height: 50px;">
	<th>
	   <span_customer2>通知人员1:</span_customer2>
	</th>
	<td>
	<select id="userId4" class="m-wrap span12" name="user_name1" tabindex="1">
       <option value="">--</option>
       <c:if test="${!empty alarmUserList}">
	    <c:forEach var="alarmUser" items="${alarmUserList}">
		<option value="${alarmUser.id }">${alarmUser.user_real_name}</option>
		</c:forEach>
		</c:if>
	    </select>
	</td>
	</tr>
	<tr style="margin-top: 60px;height: 50px;">
	<th>
	   <span_customer2>通知人员2:</span_customer2>
	</th>
	<td>
	<select id="userId5" class="m-wrap span12" name="user_name2" tabindex="1">
       <option value="">--</option>
       <c:if test="${!empty alarmUserList}">
	    <c:forEach var="alarmUser" items="${alarmUserList}">
		<option value="${alarmUser.id }">${alarmUser.user_real_name}</option>
		</c:forEach>
		</c:if>
	    </select>
	</td>
	</tr>
	<tr style="height: 50px;">
	<th>
	<span_customer2>通知人员3:</span_customer2>
	</th>
	<td>
	<select id="userId6" class="m-wrap span12" name="user_name3" tabindex="1">
       <option value="">--</option>
       <c:if test="${!empty alarmUserList}">
	    <c:forEach var="alarmUser" items="${alarmUserList}">
		<option value="${alarmUser.id }">${alarmUser.user_real_name}</option>
		</c:forEach>
		</c:if>
	    </select>
	</td>
	</tr>
<!-- 	<tr style="height: 50px;"> -->
<!-- 	<th style="display:none;"> -->
<!-- 	报警是否启用: -->
<!-- 	</th> -->
<!-- 	<td style="display:none;"> -->
<!-- 	<select id="status" class="m-wrap span12" name="status" tabindex="1"> -->
<!--        <option value="N">否</option> -->
<!--        <option value="Y">是</option> -->
<!-- 	    </select> -->
<!-- 	</td> -->
<!-- 	</tr> -->
<tr style="height: 10px;"></tr>
	<tr style="height: 20px;">
	<td style="text-align: right;">
	<button type="button" class="btn blue" onclick="bindingUser()"><i class="icon-ok"></i>&nbsp;确 定&nbsp;&nbsp;&nbsp;</button>
	</td>
	<td style="text-align: center;">
	<button type="button" class="btn" onclick="closeB()" style="margin-left: 50px;">&nbsp;&nbsp;&nbsp;取 消&nbsp;&nbsp;&nbsp;</button>
	</td>
	</tr>
	</table>
		<script>
			function closeB(){
				parent.layer.closeAll();
			}
		</script>
		</form>
		<!-- END FORM-->  
<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>		
  </body>
</html>
