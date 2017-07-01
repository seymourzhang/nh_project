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
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<base href="<%=basePath%>">
	<title>正大养鸡-上传数据查询</title>
	<%@ include file="../framework/inc.jsp"%>
	<script src="<%=path%>/framework/js/bootstrap_table/bootstrap-table.js"></script>
	<link href="<%=path%>/framework/js/bootstrap_table/bootstrap-table.css" rel="stylesheet" />
	<script src="<%=path%>/framework/js/bootstrap_table/locale/bootstrap-table-zh-CN.js"></script>

	<link rel="stylesheet" href="<%=path%>/framework/js/bootstrap_editable/1.5.1/css/bootstrap-editable.css">
	<script src="<%=path%>/framework/js/bootstrap_editable/1.5.1/js/bootstrap-editable.js"></script>
	<script src="<%=path%>/framework/js/bootstrap_table/extensions/editable/bootstrap-table-editable.js"></script>
<style type="text/css">
body {
*{
	font-size:12px;
}
body {
    font-family:verdana,helvetica,arial,sans-serif;
    padding:20px;
    font-size:12px;
    margin:0;
}
</style>
</head>
<script>
	$(document).ready(function(){
		initTable("tData", getColums(), []);
		showData();
	});
	function getColums() {
		var colums = [
			{
				field: "id",
				title: "id"
			},{
				field: "date_time",
				title: "date_time"
			},{
				field: "col_a",
				title: "col_a"
			},{
				field: "col_b",
				title: "col_b"
			},{
				field: "col_c",
				title: "col_c"
			},{
				field: "col_d",
				title: "col_d"
			},{
				field: "col_e",
				title: "col_e"
			},{
				field: "col_f",
				title: "col_f"
			},{
				field: "col_g",
				title: "col_g"
			},{
				field: "col_h",
				title: "col_h"
			},{
				field: "col_i",
				title: "col_i"
			},{
				field: "col_j",
				title: "col_j"
			},{
				field: "col_k",
				title: "col_k"
			},{
				field: "col_l",
				title: "col_l"
			},{
				field: "col_m",
				title: "col_m"
			},{
				field: "col_n",
				title: "col_n"
			},{
				field: "col_o",
				title: "col_o"
			},{
				field: "col_p",
				title: "col_p"
			},{
				field: "col_q",
				title: "col_q"
			},{
				field: "col_r",
				title: "col_r"
			},{
				field: "col_s",
				title: "col_s"
			},{
				field: "col_t",
				title: "col_t"
			},{
				field: "col_u",
				title: "col_u"
			},{
				field: "col_v",
				title: "col_v"
			},{
				field: "col_w",
				title: "col_w"
			},{
				field: "col_x",
				title: "col_x"
			},{
				field: "col_y",
				title: "col_y"
			},{
				field: "col_z",
				title: "col_z"
			},{
				field: "col_aa",
				title: "col_aa"
			},{
				field: "col_ab",
				title: "col_ab"
			},{
				field: "col_ac",
				title: "col_ac"
			},{
				field: "col_ad",
				title: "col_ad"
			},{
				field: "col_ae",
				title: "col_ae"
			},{
				field: "col_af",
				title: "col_af"
			},{
				field: "col_ag",
				title: "col_ag"
			}];
		return colums;
	}
	function showData() {
		$.ajax({
			url: path + "/deviceMobile/showData",
			data:{},
			type: "POST",
			dataType: "json",
			async: false,
			success: function (result) {
				var list = result.obj;
				$("#tDataTable").bootstrapTable("load", list);
			}
		});
	}
</script>
<body>
	<table class="table table-striped table-bordered table-hover"  id="tDataTable">
		<thead>
			<tr> 
				<th style="text-align: center;">id</th>
				<th style="text-align: center;">date_time</th>
				<th style="text-align: center;">col_a</th>
				<th style="text-align: center;">col_b</th>
				<th style="text-align: center;">col_c</th>
				<th style="text-align: center;">col_d</th>
				<th style="text-align: center;">col_e</th>
				<th style="text-align: center;">col_f</th>
				<th style="text-align: center;">col_g</th>
				<th style="text-align: center;">col_h</th>
				<th style="text-align: center;">col_i</th>
				<th style="text-align: center;">col_j</th>
				<th style="text-align: center;">col_k</th>
				<th style="text-align: center;">col_l</th>
				<th style="text-align: center;">col_m</th>
				<th style="text-align: center;">col_n</th>
				<th style="text-align: center;">col_o</th>
				<th style="text-align: center;">col_p</th>
				<th style="text-align: center;">col_q</th>
				<th style="text-align: center;">col_r</th>
				<th style="text-align: center;">col_s</th>
				<th style="text-align: center;">col_t</th>
				<th style="text-align: center;">col_u</th>
				<th style="text-align: center;">col_v</th>
				<th style="text-align: center;">col_w</th>
				<th style="text-align: center;">col_x</th>
				<th style="text-align: center;">col_y</th>
				<th style="text-align: center;">col_z</th>
				<th style="text-align: center;">col_aa</th>
				<th style="text-align: center;">col_ab</th>
				<th style="text-align: center;">col_ac</th>
				<th style="text-align: center;">col_ad</th>
				<th style="text-align: center;">col_ae</th>
				<th style="text-align: center;">col_af</th>
				<th style="text-align: center;">col_ag</th>
			</tr>
		</thead>
	</table>
	<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
	<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
	<script type="text/javascript" src="<%=path%>/modules/product/js/missionRemind.js"></script>
</body>
</html>

