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
<script src="<%=path%>/framework/js/bootstrap_table/bootstrap-table.js"></script>
<link href="<%=path%>/framework/js/bootstrap_table/bootstrap-table.css" rel="stylesheet" />
<script src="<%=path%>/framework/js/bootstrap_table/locale/bootstrap-table-zh-CN.js"></script>

<link rel="stylesheet" href="<%=path%>/framework/js/bootstrap_editable/1.5.1/css/bootstrap-editable.css">
<script src="<%=path%>/framework/js/bootstrap_editable/1.5.1/js/bootstrap-editable.js"></script>
<script src="<%=path%>/framework/js/bootstrap_table/extensions/editable/bootstrap-table-editable.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
<script type="text/javascript" src="<%=path%>/modules/drug/js/drugView.js"></script>
</head>

<body style="background-color: #ffffff;" >
	<div id="page-content" class="clearfix">
		<div class="row-fluid">
			<div class="span12">
				<div class="tabbable tabbable-custom boxless" >
					<div class="row-fluid">
						<%--标签菜单栏--%>
						<div class="span12" style="margin-left: 0px;height: 10px">
							<ul class="nav nav-pills row-fluid" style="margin-bottom: 0px; ">
								<li  class="active" id="stateTab" style="text-align: center;width:49.85%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >
									<a href="#state2" onclick="forward3();"  data-toggle="tab" id="stateTab1">计划查询</a>
								</li>
								<li  id="detailTab" style="text-align: center;width:49.85%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >
									<a href="#detail2" onclick="forward2();"  data-toggle="tab" id="detailTab1">实际执行</a>
								</li>
							</ul>
						</div>
					</div>

					<div class="tab-content row-fluid" style="border:none;padding-top: 2px;">
						<%-- 计划查询 --%>
						<div class="tab-pane active" id="state2">
							<form id="planForm">
								<%-- 功能栏 --%>
								<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
									<div class="span12">
										<div class="container-fluid">
											<div class="row-fluid" id="jj1">
												<div class="span2" align="left">
													<span_customer2>类型</span_customer2>
													<select id="good_type" tabindex="1"  name="good_type" style="width: 120px" onchange="searchData('plan');">
														<c:if test="${!empty goodTypeList}">
															<c:forEach var="goodType" items="${goodTypeList}">
																<option value="${goodType.biz_code }">${goodType.code_name}</option>
															</c:forEach>
														</c:if>
													</select>
												</div>
<!-- 												<div class="span10" align="left"> -->
<!-- 													<button type="button" class="btn blue" onclick="searchData('plan');" id="qued"> -->
<!-- 														<i class="icon-search"></i>&nbsp;查询计划 -->
<!-- 													</button> -->
<!-- 												</div> -->
											</div>
										</div>
									</div>
								</div>
									<div class="row-fluid">
										<div class="span12">
											<p id = "planFarmTitle" align="center">
												<font size='4' ><B>${pd.farm_name}</B></font>
											</p>
										</div>
									</div>
									<div class="row-fluid">
										<div class="span12">
										<div id="planFrame" style="display:block;">
											<table id="planTable"></table>
											</div>
										<div id="plan2Frame" style="display:none;">
											<table id="plan2Table"></table>
											</div>
										</div>
									</div>
							</form>

						</div>

						<%-- 实际执行 --%>
						<div class="tab-pane" id="detail2">
							<input type="hidden" name="farmId" id="farmId" value="${pd.farm_id }">
							<input type="hidden" name="farm_name" id="farm_name" value="${pd.farm_name }">
							<%-- 功能栏 --%>
							<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
								<div class="span12">
									<div class="container-fluid">
										<div class="row-fluid" id="jj2">
											<div class="span2" align="left">
												<span_customer2>类型</span_customer2>
													<select id="good_type1" tabindex="1"  name="good_type"  onchange="setDrugId1();" style="width: 90px">
														<c:if test="${!empty goodTypeList}">
															<c:forEach var="goodType" items="${goodTypeList}">
																<option value="${goodType.biz_code }">${goodType.code_name}</option>
															</c:forEach>
														</c:if>
													</select>
											</div>
											<div class="span4" align="left">
												<span_customer2><font id="mingcheng" style="">疫苗</font><font id="mingcheng2" style="display: none;">药品</font></span_customer2>
												<select id="drug_id1" tabindex="1"  name="drug_id1" style="display: none;">
													<c:if test="${!empty goodsList}">
														<c:forEach var="goods" items="${goodsList}">
															<option value=""></option>
															<option value="${goods.good_id }">${goods.good_name}</option>
														</c:forEach>
													</c:if>
												</select>
												<input type="text" id="drug_id1_select" data-provide="typeahead" style="width: 300px" placeholder="请输入品名或物资号" autocomplete="off" onchange="empty()" />
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
											    <select id="corporation_id"   tabindex="1"  name="corporation_id" style="width: 130px;" >
												</select>
											</div>
										</div>
										<div class="row-fluid" id="jj">
											<div class="span2" align="left">
												<span_customer2>日期</span_customer2>
												<div class="input-append date date-picker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
													<input readonly type="text" name="use_date" value="${systemDate }" id="use_date" style="width: 76px" onchange="searchData('fact');">
													<span class="add-on"><i class="icon-calendar"></i></span>
												</div>
											</div>
											<div class="span4" align="left">
												<span_customer2>用途</span_customer2>
												<input type="text" id="use_type" name="use_type" placeholder="请输入用途" style="width: 300px">
											</div>
											<div class="span2" align="left">
												<span_customer2>数量</span_customer2>
												<input type="text" id="use_unit" name="use_unit" value="0" style="width: 91px">
											</div>
											<div class="span2" align="left">
												<span_customer2>批号</span_customer2>
												<input type="text" id="good_batch_no" name="good_batch_no" style="width: 115px">
											</div>
											<div class="span2" align="left">
												<span_customer2>主要成分</span_customer2>
												<input type="text" id="main_constitute" name="main_constitute" placeholder="请输入主要成分" style="width: 100px">
											</div>
											</div>
											<div class="row-fluid" id="jj">
											<div class="span2" align="left">
												<span_customer2>栋舍</span_customer2>
												<select id="houseId" tabindex="1"  name="houseId" onchange="searchData('fact');" style="width: 110px">
													<c:if test="${!empty houseList}">
														<c:forEach var="house" items="${houseList}">
															<option value="${house.org_id }">${house.org_name}</option>
														</c:forEach>
													</c:if>
												</select>
											</div>
											<div class="span3" align="left">
												<span_customer2>负责人</span_customer2>
												<select id="use_user_id" tabindex="1"  name="use_user_id" style="width: 200px">
													<c:if test="${!empty userList}">
														<c:forEach var="user" items="${userList}">
															<option value="${user.id }">${user.user_real_name}</option>
														</c:forEach>
													</c:if>
												</select>
													<%--<span_customer>厂家</span_customer>--%>
													<select id="factory_id" tabindex="1"  name="factory_id" style="display: none">
													</select>
											</div>
											<div class="span2" align="left">
											</div>
											<div class="span4" align="left">
												<button type="button" class="btn blue" onclick="addDrug();" id="add">
													<i class="icon-ok"></i>&nbsp;确认执行</button>
												
												<button id='factToolbar_btn_delete' type='button' class='btn blue' style="display: inline;" onclick="javascript:deleteDrug();">
											<i class="icon-trash"></i><span class='glyphicon glyphicon-plus' aria-hidden='true'></span>&nbsp;删除
										</button>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="span4" align="left">
									<div id="factToolbar" class="btn-group">
										
									</div>

								</div>
								<div class="span4" align="center">
									<p id = "factFarmTitle" align="center">
										<font size='4' ><B>${pd.farm_name}</B></font>
									</p>
								</div>
								<div class="span4">

								</div>
							</div>
							<div class="row-fluid">
								<div class="span12">
								<div id="factFrame" style="display:block;">
									<table id="factTable"></table>
									</div>
									<div id="fact2Frame" style="display:none;">
									<table id="fact2Table"></table>
									</div>
								</div>

							</div>

						</div>


					</div>

				</div>
			</div>
		</div>
	</div>
<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>

</body>
</html>
