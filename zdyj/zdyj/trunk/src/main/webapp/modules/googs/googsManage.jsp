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
<input type="text"  name="farmId"  id="farmId" style="display:none" value="${farmId}" />
<input type="text"  name="farm"  id="farm" style="display:none" value="${farm}" />
	<div id="page-content" class="clearfix" style="padding-top: 10px;">
		<div class="row-fluid">
			<div class="span12">

				<div class="tabbable tabbable-custom boxless">
					<ul class="nav nav-pills" style="margin-bottom: 0px;" id = "uiTab">
					   <li class="active" style="text-align: center;width:25%;background-color: #BFBFBF; border-right: 1px solid #E0DFDF; " >
							<a href="#tab_3"  data-toggle="tab">物资</a>
						</li>
						<li  style="text-align: center;width:25%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >
							<a href="#tab_4" data-toggle="tab">物资关系</a>
						</li>
						<li  style="text-align: center;width:25%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >
							<a href="#tab_1" data-toggle="tab">供应商</a>
						</li>
						<li  style="text-align: center;width:24.6%;background-color: #BFBFBF; border-right: 1px solid #E0DFDF;" >
							<a href="#tab_2"   data-toggle="tab" >厂家</a>
						</li>						
					</ul>

					<div class="tab-content" style="border:none">

						<%-- 供应商 --%>
						<div class="tab-pane" id="tab_1">
							<!-- BEGIN FORM-->
							<form id="corporationForm"  onsubmit="return false;" >
<!-- 								<input type="text"  name="corporationFarmId"  id="corporationFarmId" style="display:none" value="${farmId}" /> -->
<!-- 								<input type="text"  name="corporationFarm"  id="corporationFarm" style="display:none" value="${farm}" /> -->
								<div class="container-fluid" style="background:#e7e5e5;padding-top: 10px;margin-top: -15px;">
									<div class="row-fluid">
<!-- 									 <div class="portlet box blue1" style="width: 1300px;margin-left: -20px;"> -->
<!-- 								     <div class="portlet-title"> -->
<!-- 									<div class="caption"> -->
<!-- 										<i class="icon-reorder"></i>检索条件 -->
<!-- 									</div> -->
<!-- 									 <div class="actions"> -->

<!-- 										<a href="javascript:getCorporation();" class="btn green"><i class="icon-search"></i> 查询</a> -->

<!-- 									</div>  -->

<!-- 								</div> -->
										<div class="span3" align="left" style="margin-top: 0px;">
											<span_customer2>供应商</span_customer2>
											<input type="text" value="" placeholder="请输入供应商名称" name="corporation" id="corporation">
<!-- 											<select id="corporation_id" tabindex="1"  name="corporation_id"> -->
<!-- 											</select> -->
										</div>
										<div class="span3" align="left">
											
										</div>
										<div class="span3" align="left">

										</div>
										<div class="span3" align="left">
											<button type="button" class="btn blue" onclick="getCorporation()"><i class="icon-search"></i>查询</button>
										</div>
<!-- 										</div> -->
									</div>
								</div>
								
							<!-- END FORM -->

<!-- 							<div class="row-fluid" style="margin-top: -40px;"> -->
<!-- 								<div class="span12"> -->
<!-- 									<hr style="height:10px;border:none;border-top:1px solid #555555;" /> -->
<!-- 								</div> -->
<!-- 							</div> -->
							<div class="row-fluid" style="padding-top: 5px;">
							   <div class="span4" align="left">
									<div id="goodsToolbar" class="btn-group">
										<button id='goodsToolbar_btn_edit' type='button' class='btn blue' style="display: block;" onclick="javascript:openCorporationWin();">
											<i class="icon-plus"></i>新增
										</button>
									</div>
									<div id="goodsToolbar" class="btn-group">
										<button id='goodsToolbar_btn_edit' type='button' class='btn blue' style="display: block;" onclick="javascript:updateCorporation();">
											<i class="icon-edit"></i>修改
										</button>
									</div>
								</div>
								<div class="span4">
<!-- 									<p id = "corporationFarmTitle" align="center"> -->
<!-- 										农场 -->
<!-- 									</p> -->
								</div>
								<div class="span4"></div>
							</div>
                            <div class="row-fluid">
								<div class="span12">
									<div id="corporationFrame" align="center">
										<div>
											<table id="corporationTable"></table>
										</div>
									</div>
								</div>
							</div>
							</form>
						</div>
					
						<%-- 厂家 --%>
						<div class="tab-pane" id="tab_2">
							<form id="factoryForm"  onsubmit="return false;" >
								<div class="container-fluid" style="background:#e7e5e5;padding-top: 10px;margin-top: -15px;">
									<div class="row-fluid">
<!-- 									<div class="portlet box blue1" style="width: 1300px;margin-left: -20px;"> -->
<!-- 								     <div class="portlet-title"> -->
<!-- 									<div class="caption"> -->
<!-- 										<i class="icon-reorder"></i>检索条件 -->
<!-- 									</div> -->
<!-- 									 <div class="actions"> -->

<!-- 										<a href="javascript:getFactory();" class="btn green"><i class="icon-search"></i> 查询</a> -->

<!-- 									</div>  -->

<!-- 								</div> -->
										<div class="span3" align="left" style="margin-top: 0px;">
											<span_customer2>厂家</span_customer2>
											<input type="text" value="" placeholder="请输入厂家名称" name="factory_name" id="factory_name">
<!-- 											<select id="factory_id" tabindex="1"  name="factory_id"> -->
<!-- 											</select> -->
										</div>
										<div class="span3" align="left">

										</div>
										<div class="span3" align="left">

										</div>
										<div class="span3" align="left">
                                        <button type="button" class="btn blue" onclick="getFactory()"><i class="icon-search"></i>查询</button>
										</div>
<!-- 										</div> -->
									</div>
								</div>
							
							<!-- END FORM -->

<!-- 							<div class="row-fluid" style="margin-top: -40px;"> -->
<!-- 								<div class="span12"> -->
<!-- 									<hr style="height:10px;border:none;border-top:1px solid #555555;" /> -->
<!-- 								</div> -->
<!-- 							</div> -->
							<div class="row-fluid" style="padding-top: 5px;">
							<div class="span4" align="left">
									<div id="goodsToolbar" class="btn-group">
										<button id='goodsToolbar_btn_edit' type='button' class='btn blue' style="display: block;" onclick="javascript:openFactoryWin();">
											<i class="icon-plus"></i>新增
										</button>
									</div>
									<div id="goodsToolbar" class="btn-group">
										<button id='goodsToolbar_btn_edit' type='button' class='btn blue' style="display: block;" onclick="javascript:updateFactory();">
											<i class="icon-edit"></i>修改
										</button>
									</div>
								</div>
								<div class="span4">
<!-- 									<p id = "factoryFarmTitle" align="center"> -->
<!-- 										农场 -->
<!-- 									</p> -->
								</div>
								<div class="span4">
								</div>
							</div>
							<div class="row-fluid">
								<div class="span12">
									<div id="factoryFrame" align="center">
										<div>
											<table id="factoryTable"></table>
										</div>
									</div>
								</div>
							</div>
							</form>
						</div>

						<%-- 物资 --%>
						<div class="tab-pane active" id="tab_3">
							<form id="goodsForm" onsubmit="return false;" >
								<div class="container-fluid" style="background:#e7e5e5;padding-top: 10px;margin-top: -15px;">

									<div class="row-fluid">
										<div class="span3" align="left" style="margin-top: 0px;">
											<span_customer2 >物资</span_customer2>
											<input type="text" value="" placeholder="请输入物资名称或编码" name="good_name" id="good_name">
										</div>
										<div class="span3" align="left">
											
										</div>
										<div class="span3" align="left">
										
										</div>
										<div class="span3" align="left">
											<button type="button" class="btn blue" onclick="queryGoods()" id="goodSearch" ><i class="icon-search"></i>查 询</button>
										</div>
									</div>
								</div>
							
							
							<div class="row-fluid" style="padding-top: 5px;">
								<div class="span4" align="left">
									<div id="goodsToolbar" class="btn-group">
										<button id='goodsToolbar_btn_edit' type='button' class='btn blue' style="display: block;" onclick="javascript:openGoodsWin();">
											<i class="icon-plus"></i>新增
										</button>
									</div>
									<div id="goodsToolbar" class="btn-group">
										<button id='goodsToolbar_btn_edit' type='button' class='btn blue' style="display: block;" onclick="javascript:updateGoods();">
											<i class="icon-edit"></i>修改
										</button>
									</div>
								</div>
								<div class="span4" align="center">
								</div>
								<div class="span4" align="center">

								</div>
							</div>
							<div class="row-fluid" >
								<div class="span12" style = "margin-top: -20px;">
									<div id="goodsFrame" align="left">
										<div>
											<table id="goodsTable"></table>
										</div>
									</div>
								</div>
							</div>
							</form>
						</div>
						
						
						<%-- 物资关系  --%>
						<div class="tab-pane" id="tab_4">
							<form id="corporationGoodForm"  onsubmit="return false;" >
								<div class="container-fluid" style="background:#e7e5e5;padding-top: 10px;margin-top: -15px;">

									<div class="row-fluid">
										<div class="span3" align="left" style="margin-top: 0px;">
											<span_customer2 >物资</span_customer2>
											<input type="text" value="" placeholder="请输入物资名称或编码" name="good_name" id="good_name">
										</div>
										<div class="span3" align="left">
											<span_customer2>供应商</span_customer2>
											<input type="text" value="" placeholder="请输入供应商名称" name="corporation" id="corporation">
										</div>
										<div class="span3" align="left">
										   <span_customer2>厂家</span_customer2>
											<input type="text" value="" placeholder="请输入厂家名称" name="factory_name" id="factory_name">
										</div>
										<div class="span3" align="left">
											<button type="button" class="btn blue" onclick="getCorporationGood()" id="corporationGoodSearch" ><i class="icon-search"></i>查 询</button>
										</div>
									</div>
								</div>
							
							
							<!-- END FORM -->

<!-- 							<div class="row-fluid" style="margin-top: -40px;"> -->
<!-- 								<div class="span12"> -->
<!-- 									<hr style="height:10px;border:none;border-top:1px solid #555555;" /> -->
<!-- 								</div> -->
<!-- 							</div> -->
							<div class="row-fluid" style="padding-top: 5px;">
								<div class="span4" align="left">
									<div id="corporationGoodToolbar" class="btn-group">
										<button id='corporationGoodToolbar_btn_edit' type='button' class='btn blue' style="display: block;" onclick="javascript:opencorporationGoodWin();">
											<i class="icon-plus"></i>新增
										</button>
									</div>
									<div id="corporationGoodToolbar" class="btn-group">
										<button id='corporationGoodToolbar_btn_edit' type='button' class='btn blue' style="display: block;" onclick="javascript:deletecorporationGood();">
											<i class="icon-trash"></i>删除
										</button>
									</div>
								</div>
								<div class="span4" align="center">
<!-- 									<p id = "corporationGoodFarmTitle" align="center"> -->
<!-- 										农场 -->
<!-- 									</p> -->
								</div>
								<div class="span4" align="center">

								</div>
							</div>
							<div class="row-fluid">
								<div class="span12">
									<div id="corporationGoodFrame" align="left">
										<div>
											<table id="corporationGoodTable"></table>
										</div>
									</div>
								</div>
							</div>
							</form>
						</div>
						

					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var isRead="${pd.write_read}";//菜单是否只读
	</script>
	<!-- #main-content -->
	<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
    <script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
    <script type="text/javascript" src="<%=path%>/modules/googs/js/googsManage.js"></script> 
	<!-- 确认窗口 -->
	
</body>
</html>
