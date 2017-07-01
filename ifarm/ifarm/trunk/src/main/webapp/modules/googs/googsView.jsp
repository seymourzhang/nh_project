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
</head>

<body style="background-color: #ffffff;">
<input type="text"  name="farmId"  id="farmId" style="display:none" value="${farmId}">
<input type="text"  name="farm"  id="farm" style="display:none" value="${farm}" >

<div id="page-content" class="clearfix">
	<div class="row-fluid">
		<div class="span12">
			<div class="tabbable tabbable-custom boxless">
				<div class="row-fluid">
					<%--标签菜单栏--%>
					<div class="span12" style="margin-left: 0px;height: 10px">
						<ul class="nav nav-pills row-fluid" style="margin-bottom: 0px; " id = "uiTab">
							<li  class="active"  style="text-align: center;width:25%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >
								<a href="#tab_1" data-toggle="tab">入库</a>
							</li>
							<li  style="text-align: center;width:24.85%;background-color: #BFBFBF; border-right: 1px solid #E0DFDF;" >
								<a href="#tab_2"   data-toggle="tab" >耗用</a>
							</li>
							<li  style="text-align: center;width:24.85%;background-color: #BFBFBF; border-right: 1px solid #E0DFDF; " >
								<a href="#tab_3"  data-toggle="tab">库存调整</a>
							</li>
							<li  style="text-align: center;width:25%;background-color: #BFBFBF;" >
								<a href="#tab_4"  data-toggle="tab">库存调整审批</a>
							</li>
						</ul>

						<div class="tab-content row-fluid" style="border:none;padding-top: 0px;">
							<%-- 入库 --%>
							<div class="tab-pane active" id="tab_1">
								<!-- BEGIN FORM-->
								<form id="inStockForm">
									<input type="text"  name="inStockFarmId"  id="inStockFarmId" style="display:none" value="${farmId}" >
									<input type="text"  name="inStockFarm"  id="inStockFarm" style="display:none" value="${farm}" >
									<%--功能栏--%>
									<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
										<div class="span12">
											<div class="container-fluid">
												<div class="row-fluid">
													<div class="span2" align="left">
														<span_customer2>物资类型</span_customer2>
															<select id="good_type"  tabindex="1"  name="good_type"  style="width: 90px;" onchange="clearData();">
																<c:if test="${!empty goodType}">
																	<c:forEach var="goodType" items="${goodType}">
																		<c:if test="${goodType.biz_code!=1}">
																			<option value="${goodType.biz_code}">${goodType.code_name}</option>
																		</c:if>
																	</c:forEach>
																</c:if>
															</select>
													</div>
													<div class="span4" align="left">
														<span_customer2>名称</span_customer2>
															<select id="good_id"  name="good_id" style="display: none;">
															</select>
															<input type="text" id="goods_id_select" data-provide="typeahead" style="width: 300px;" placeholder="请输入名称或编号" autocomplete="off" onchange="empty()">
													</div>
													<div class="span2" align="left">
														<span_customer2>规格</span_customer2>&nbsp;&nbsp;&nbsp;
															<select id="spec" tabindex="1"  name="spec" style="width: 100px;">
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
												<div class="row-fluid">
													<div class="span2" align="left">
														<span_customer2>入库日期</span_customer2>
														<div class="input-append date date-picker1"  data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
															<input readonly type="text" name="operation_date" id="operation_date" style="width: 75px;">
															<span class="add-on"><i class="icon-calendar"></i></span>
														</div>
													</div>
													<div class="span4" align="left">
														<%--<div class="row-fluid">--%>
															<%--<div class="span6" align="left">--%>
																<span_customer2>数量</span_customer2>
																<input type="text"  name="count"  id="sssasd" value="0" style="width: 70px;">
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																<span_customer2>单价</span_customer2>
																<input type="text"  name="price" id="sssasdPrice" value="0" style="width: 70px;">&nbsp;元
															<%--</div>--%>
															<%--<div class="span6" align="center">--%>
																<%--<span_customer2>单价</span_customer2>--%>
																<%--<input type="text"  name="price" id="sssasdPrice" value="0" style="width: 80px;">&nbsp;元--%>
															<%--</div>--%>
														<%--</div>--%>
													</div>
													<div class="span2" align="left">
														<span_customer2>保质期</span_customer2>
														<div class="input-append date date-picker"  data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
															<input readonly type="text" name="exp" id="exp" style="width: 90px">
															<span class="add-on"><i class="icon-calendar"></i></span>
														</div>
													</div>
													<div class="span2" align="left">
														<span_customer2>厂家</span_customer2>
															<select id="factory_id" tabindex="1"  name="factory_id" style="width: 130px">
															</select>
													</div>
													<div class="span2" align="left">
														<button type="button" class="btn blue" onclick="inStock()">
															<i class="icon-ok"></i>&nbsp;确认入库
														</button>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="row-fluid">
										<div class="span12">
											<p id = "inStockFarmTitle" align="center">
												农场
											</p>
											<div id="inStockFrame" align="center">
												<table id="inStockTable"></table>
											</div>
										</div>
									</div>
								</form>
							</div>

								<%-- 耗用 --%>
								<div class="tab-pane" id="tab_2">
									<form id="outStockForm">
										<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
											<div class="span12">
												<div class="container-fluid">
													<div class="row-fluid">
														<div class="span2" align="left">
															<span_customer2>物资类型</span_customer2>
															<select id="good_type_out" tabindex="1"  name="good_type" style="width: 110px;" onchange="clearData();">
																<c:if test="${!empty goodType}">
																	<c:forEach var="goodType" items="${goodType}">
																		<c:if test="${goodType.biz_code!=1}">
																			<option value="${goodType.biz_code}">${goodType.code_name}</option>
																		</c:if>
																	</c:forEach>
																</c:if>
															</select>
														</div>
														<div class="span4" align="left">
															<span_customer2>名称</span_customer2>
																<select id="good_id_out" name="good_id" style="display: none;">
																</select>
																<input type="text" id="goods_id_out_select" data-provide="typeahead" style="width: 300px;" placeholder="请输入名称或编号" autocomplete="off" onblur="empty();">
														</div>
														<div class="span2" align="left">
															<span_customer2>数量</span_customer2>
															<input type="text" name="count" id="count_out" value="0" style="width: 120px">
														</div>
														<div class="span2" align="left">
															<span_customer2>栋舍</span_customer2>
															<select id="house_id"  tabindex="1"  name="house_id" style="width: 120px">
																<c:if test="${!empty houseList}">
																	<c:forEach var="hl" items="${houseList}">
																		<option value="${hl.id}">${hl.name_cn}</option>
																	</c:forEach>
																</c:if>
															</select>
														</div>
														<div class="span2" align="left">
															<button type="button" class="btn blue" onclick="outStock()" >
																<i class="icon-ok"></i>&nbsp;确认耗用
															</button>
														</div>
														</div>
														<div class="row-fluid">
														<div class="span2" align="left">
															<span_customer2>耗用日期</span_customer2>
															<div class="input-append date date-picker"  data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
																<input readonly type="text" name="operation_date" id="operation_date_out" style="width: 80px;"/>
																<span class="add-on"><i class="icon-calendar"></i></span>
															</div>
														</div>
														
													</div>
												</div>
											</div>
										</div>
										<div class="row-fluid">
											<div class="span12">
												<p id = "outStockFarmTitle" align="center">
													农场
												</p>
												<div id="outStockFrame" align="center">
													<table id="outStockTable"></table>
												</div>
											</div>
										</div>
									</form>
								</div>

								<%-- 库存调整 --%>
								<div class="tab-pane " id="tab_3">
									<form id="stockForm">
										<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
											<div class="span12">
												<div class="container-fluid">
													<div class="row-fluid">
														<div class="span2" align="left">
															<span_customer2>物资类型</span_customer2>
															<select id="good_type_stock" tabindex="1"  name="good_type" style="width: 110px;">
																<c:if test="${!empty goodType}">
																	<c:forEach var="goodType" items="${goodType}">
																		<c:if test="${goodType.biz_code!=1}">
																			<option value="${goodType.biz_code}">${goodType.code_name}</option>
																		</c:if>
																	</c:forEach>
																</c:if>
															</select>
														</div>
														<div class="span4" align="left">
															<span_customer2>名称</span_customer2>
																<select id="good_id_stock" tabindex="1"  name="good_id" style="display: none;">
																</select>
																<input type="text" id="good_id_stock_select" name="good_name" data-provide="typeahead" style="width: 300px;" placeholder="请输入名称或编号" autocomplete="off" onblur="empty();" />
														</div>
														<div class="span2" align="left">
															<span_customer2>规格</span_customer2>
																<select id="spec_stock" tabindex="1"  name="spec" style="width: 120px">
																</select>
														</div>
														<div class="span2" align="left">
															<span_customer2>单位</span_customer2>
																<select id="unit_stock"  tabindex="1"  name="unit" style="width: 120px">
																</select>
														</div>
														
														<div class="span2" align="left">
															<button type="button" class="btn blue" onclick="queryStock()" >
																<i class="icon-search"></i>&nbsp;查询库存</button>
														</div>
													</div>
													<div class="row-fluid">
													<div class="span2" align="left">
															<span_customer2>供应方</span_customer2>
															<select id="corporation_id_stock" tabindex="1"  name="corporation_id" style="width: 120px">
															</select>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="row-fluid">
											<div class="span4" align="left">
												<div id="stockToolbar" class="btn-group">
													<button id='stockToolbar_btn_edit' type='button' class='btn blue' style="display: block;" onclick="javascript:getMessages();">
														<span class='glyphicon glyphicon-plus' aria-hidden='true'></span>调整
													</button>

												</div>
											</div>
											<div class="span4" align="center">
												<p id = "stockFarmTitle" align="center">
													农场
												</p>
											</div>
											<div class="span4" align="center">

											</div>
										</div>
										<div class="row-fluid">
											<div class="span12">
												<div id="stockFrame" align="left">
													<div>
														<table id="stockTable"></table>
													</div>
												</div>
											</div>
										</div>
									</form>
								</div>

								<%-- 库存调整审批 --%>
								<div class="tab-pane " id="tab_4">
									<div class="row-fluid">
										<div class="span4" align="left">
											<div id="approvalStockFrame" align="left">
												<div id="approvalStockToolbar" class="btn-group">
													<button id='approvalStockToolbar_btn_reject' type='button' class='btn blue' style="display: inline;" onclick="javascript:openApprovalWin(0);">
														<i class="icon-remove"></i><span class='glyphicon glyphicon-plus' ></span>&nbsp;驳回
													</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<button id='approvalStockToolbar_btn_pass' type='button' class='btn blue' style="display: inline;" onclick="javascript:openApprovalWin(1);">
														<i class="icon-ok"></i><span class='glyphicon glyphicon-plus'></span>&nbsp;通过
													</button>
												</div>
											</div>
										</div>
										<div class="span4" align="center">
											<p id = "approvalStockFarmTitle" align="center">
												农场
											</p>
										</div>
										<div class="span4" align="left">
										</div>
									</div>
									<div class="row-fluid">
										<div class="span12" align="left">
											<div>
												<table id="approvalStockTable"></table>
											</div>
										</div>
									</div>
									<div class="row-fluid">
										<div class="span12">
											<hr style="height:10px;border:none;border-top:1px solid #555555;" />
										</div>
									</div>
									<div class="row-fluid">
										<div class="span12">
											<p id = "approvalStockChangeLog" align="center">
												<font size='3' ><B>变更记录</B></font>
											</p>
											<div id="approvalStockChangeLogFrame" align="left">
												<table id="approvalStockChangeTable"></table>
											</div>
										</div>
									</div>
								</div>

						</div>
					</div>
				</div>




			</div>
		</div>
	</div>
</div>

	<script type="text/javascript">
		var isRead="${pd.write_read}";//菜单是否只读
        var tabId = "${pd.tabId}";
		if(null == tabId || "" == tabId)
		    tabId =0;
// 		$(function(){
// 			$(".selectpicker").selectpicker({
// 			dropuAuto : false
// 			});
// 			});
		
// 		$(window).on('load', function () {  
			  
//             $('.selectpicker').selectpicker({  
//                 'selectedText': 'cat'  
//             });  
  
              
//         });  
	</script>
	<!-- #main-content -->
	<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
<!--     <script type="text/javascript" src="<%=path%>/framework/css/bootstrap-select.css"></script> -->
<!--     <script type="text/javascript" src="<%=path%>/framework/js/bootstrap-select.js"></script> -->
<!--     <script type="text/javascript" src="<%=path%>/framework/js/defaults-zh_CN.js"></script> -->
<!--     <script type="text/javascript" src="<%=path%>/framework/css/bootstrap.css"></script> -->
<!--     <script type="text/javascript" src="<%=path%>/framework/css/prettify.css"></script> -->
<!--     <script type="text/javascript" src="<%=path%>/framework/js/bootstrap-typeahead.js"></script> -->
<!--     <script type="text/javascript" src="<%=path%>/framework/js/mockjax.js"></script> -->
<!--     <script type="text/javascript" src="<%=path%>/framework/js/prettify.js"></script> -->
<!--     <script type="text/javascript" src="<%=path%>/framework/js/select2.js"></script> -->
<!--     <script type="text/javascript" src="<%=path%>/framework/js/select2.min.js"></script> -->
<!--     <script type="text/javascript" src="<%=path%>/framework/css/select2.css"></script> -->
<!--     <script type="text/javascript" src="<%=path%>/framework/css/select2.min.css"></script> -->

    <script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
    <script type="text/javascript" src="<%=path%>/modules/googs/js/googsView.js"></script> 
	<!-- 确认窗口 -->



</body>
</html>
