<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%--<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">--%>
<%--<html>--%>
<!DOCTYPE html>
<head>
	<%@ include file="../../framework/inc.jsp"%>
    <base href="<%=basePath%>">
</head>
  
  <body>
		<input type="hidden" id = "path" value ="<%=path%>">
	   <div class="container-fluid">
		   <p></p>
		   <div class="row-fluid">
			   <div class="span3" align="left">
				   所属农场
				   <input type="text" name="farmName" id="farmName" disabled="true" value="${houseData.parent_name}" style="width: 130px"/>
				   <input type="hidden" name="farmId" id="farmId" disabled="true" value="${houseData.parent_id}"/>
			   </div>
			   <div class="span2" align="left">
				   栋舍名称
				   <input type="text" name="houseName" id="houseName" disabled="true" value="${houseData.name_cn}" style="width: 50px"/>
				   <input type="hidden" name="houseId" id="houseId" disabled="true" value="${houseData.id}"/>
			   </div>
			   <div class="span2" align="left">
				   栋舍类型
				   <input type="text" name="houseType" id="houseTypeName" disabled="true" value="${houseData.house_type_name}" style="width: 60px"/>
				   <input type="hidden" name="houseType" id="houseType" disabled="true" value="${houseData.house_type}"/>
			   </div>
			   <div class="span3" align="left">
				   设备编号
				   <input type="text" name="houseType" id="deviceCode" value="" placeholder="请输入设备编号" style="width: 150px"/>
			   </div>
			   <div class="span2" align="right">
				   <button type="button" class="btn blue" onclick="mappingDevice()">绑定</button>
			   </div>

		   </div>

		   <%--<div class="row-fluid">--%>
			   <%--<div class="span12" align="center">--%>
				   <%--<hr style="height:10px;border:none;border-top:1px solid #555555;" />--%>
			   <%--</div>--%>
		   <%--</div>--%>

		   <div class="row-fluid">
			   <div class="span6" align="center">
				   <table id="deviceListTable"></table>
			   </div>
			   <div class="span6" align="center">
				   <table id="sensorListTable"></table>
			   </div>
		   </div>

	   </div>

		<!-- END FORM-->
	   <script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
	   <script type="text/javascript" src="<%=path%>/modules/farm/js/setDeviceHouseRelation.js"></script>

  </body>
</html>
