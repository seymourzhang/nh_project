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
<script>
		jQuery(document).ready(function() {
			 var win_h = $(window).height()-208;
			/*  $("#user_date_table").css("min-height",win_h);
			 $("#page-content").css("min-height",win_h); */
		});
	</script>
<body style="background-color: #ffffff;">
			<div id="page-content" class="clearfix" style="padding-top: 10px;" > 
				<div class="row-fluid" style="background-color: #ffffff;">
					<form action="" method="post" style="background-color: #ffffff;" id="userForm">
						<input type="hidden" name = "write_read" value="${pd.write_read}">
						<%-- <input type="hidden" name="id" value="${pd.id}">
						<input type="hidden" name="pid" value="${pd.pid}"> --%>
<!-- 							<div class="container-fluid"> -->
								<div style="background:#e7e5e5;padding-top: 10px;margin-top: -15px;">
								<div style="margin-left: 10px;">
								<div class="row-fluid" >
								    <div class="span3" align="left">
								    <span_customer2>上级菜单</span_customer2>
								    <select id="menu_pid"  name="menu_pid" >
	                                <option value="">全部</option>
											<c:if test="${!empty menuPidlist}">
												<c:forEach var="menuPid" items="${menuPidlist}">
													<option value="${menuPid.menu_id }">${menuPid.menu_name }</option>
												</c:forEach>
											</c:if>
										</select>
								    </div>
									<div class="span3" align="left">
										<span_customer2>关键词</span_customer2>
										<input type="text" placeholder="请输入菜单ID或名称" name="menu_name" id="menu_name">
									</div>
									<div class="span3" align="left">
										<a href="javascript:searchData();" class="btn blue"><i class="icon-search"></i> 查询</a>
									</div>
									</div>
                                    </div>
								</div>
                                
								<div class="row-fluid" style="padding-top: 5px;">
									<div class="span12">
										<a href="javascript:;" class="btn blue" onclick="openMenuWin();"><i class="icon-plus"></i> 新增</a>
									</div>
								</div>

								<div class="row-fluid">
									<div class="span12">
										<table id="menuListTable"></table>
									</div>
								</div>

<!-- 						</div> -->
					</form>
				</div> 
		 </div> 
		<!-- #main-content -->
	<!-- </div>  -->
	<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
	<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
	<!-- 确认窗口 -->
	<script type="text/javascript">
	var isRead="${pd.write_read}";//菜单是否只读

	</script>
	<script type="text/javascript" src="<%=path%>/modules/menu/js/menuManage.js"></script>
</body>
</html>
