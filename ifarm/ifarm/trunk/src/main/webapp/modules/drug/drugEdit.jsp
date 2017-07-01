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
<script type="text/javascript">
var dage;
var houseType = ${house_type};
if(houseType=="1"){
	dage = 25;
}else{
	dage = 65;
}
</script>
</head>

<body style="background-color: #ffffff;" >
<div id="page-content"  class="clearfix">
	<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
		<div style="padding-left: 10px;">
			<div class="row-fluid">
				<div class="span2" align="left">
					<span_customer2>类型</span_customer2>
					<select id="good_type" tabindex="1"  name="good_type" style="width: 90px;" onchange="searchData();">
						<c:if test="${!empty goodTypeList}">
							<c:forEach var="goodType" items="${goodTypeList}">
								<option value="${goodType.biz_code }">${goodType.code_name}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<div class="span4" align="left">
					<span_customer2><font id="mingcheng" style="">疫苗</font><font id="mingcheng2" style="display: none;">药品</font></span_customer2>
					<select id="drug_id" tabindex="1"  name="drug_id" style="display: none;">
						<c:if test="${!empty goodsList}">
							<c:forEach var="goods" items="${goodsList}">
								<option value=""></option>
								<option value="${goods.good_id }">${goods.good_name}</option>
							</c:forEach>
						</c:if>
					</select>
					<input type="text" id="drug_id_select" data-provide="typeahead" style="width: 300px;" placeholder="请输入品名或物资号" autocomplete="off" onchange="empty()" >
				</div>
				
				<div class="span2" align="left">
				    <span_customer2>规格</span_customer2>
					<select id="spec" tabindex="1"  name="spec" style="width: 104px;">
					</select>
				</div>
				<div class="span2" align="left">
					<span_customer2>单位</span_customer2>
					<select id="unit"   tabindex="1"  name="unit" style="width: 130px;" >
					</select>
				</div>
				<div class="span2" align="left">
					<span_customer2>供应方</span_customer2>
				    <select id="corporation_id"   tabindex="1"  name="corporation_id" style="width: 140px;" >
					</select>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2" align="left">
					<span_customer2>生长周龄</span_customer2>
					<input type="text" id="grow_week_age" name="grow_week_age" value="0" style="width: 50px;">
				</div>
				<div class="span4" align="left">
					<span_customer2>用途</span_customer2>
					<input type="text" id="use_type" style="width: 300px;" name="use_type" placeholder="请填写计划用途">
				</div>
				<div class="span2" align="left">
					<span_customer2>数量</span_customer2>
					<input type="text" id="use_unit" name="use_unit" value="0" style="width: 90px;">
				</div>
				<div class="span2" align="left">
					<span_customer2>农场</span_customer2>
					<select id="farmId" tabindex="1"  name="farmId" style="width: 130px;" onchange="searchData();">
						<c:if test="${!empty farmList}">
							<c:forEach var="farm" items="${farmList}">
								<option value="${farm.org_id }">${farm.org_name}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<div class="span2" align="left">
					<button type="button" class="btn blue" onclick="addDrug();" id="add">
						<i class="icon-plus"></i>&nbsp;确认新增</button>&nbsp;&nbsp;
					 <button id='factToolbar_btn_delete' type='button' class='btn blue' style="display: inline;" onclick="javascript:deleteDrug();">
					<span class='glyphicon glyphicon-plus' aria-hidden='true'></span><i class="icon-trash"></i>删除
				</button>	
				</div>
<!-- 				<div class="span1" align="left"> -->
				   
<!-- 				</div> -->
			</div>

		</div>
	</div>
	<div class="row-fluid">
		<div class="span4" align="left">
			<div id="factToolbar" class="btn-group">
				
			</div>
		</div>
		<div class="span4" align="left">
			<p id = "planFarmTitle" align="center">
				<font size='4' ><B>${pd.company}</B></font>
			</p>

		</div>
		<div class="span4" align="left">

		</div>
	</div>
	<div class="row-fluid">
		<div class="span12">
			<table id="planTable"></table>
		</div>
	</div>


</div>
		  
<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
<script type="text/javascript" src="<%=path%>/modules/drug/js/drugEdit.js"></script>
</body>
</html>
