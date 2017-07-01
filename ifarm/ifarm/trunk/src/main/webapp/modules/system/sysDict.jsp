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
	<!--  <div class="container-fluid" id="main-container" style="background-color: #ffffff;"> -->
	<div id="page-content" class="clearfix" style="padding-top: 10px;">
		<div class="row-fluid" style="background-color: #ffffff;">
			<form action="<%=path%>/sysDict/showSysDict" method="post"
				style="background-color: #ffffff;" id="dictForm">
			  <div class="span12">
							<!-- BEGIN PORTLET-->
							<div class="portlet box blue1">
								<div class="portlet-title">
									<div class="caption">
										<i class="icon-reorder"></i>检索条件
									</div>
									<!-- <div class="tools">

										<a href="javascript:;" class="collapse"></a>

									</div> -->
									<div class="actions">

										<a href="javascript:search();" class="btn green"><i class="icon-search"></i> 查询</a>

									</div>

								</div>

								<div class="portlet-body form1">
									<!-- BEGIN FORM-->
									<div class="form-horizontal" style="height: 40px;">
										<div style="height: 20px;">
											<div class="span4">

												<div class="control-group">

													<label class="control-label" style="width: 90px;">编码类型：</label>

													<div class="controls" style="margin-left: 100px;">

														<input type="text" class="m-wrap span12" value="${pd.codeDesc }" placeholder="模糊查询编码类型" name="codeDesc"">

													</div>

												</div>

											</div>

											<!--/span-->
										</div>

									</div>

									<!-- END FORM-->
								</div>

							</div>

							<!-- END PORTLET-->

							<div class="portlet box blue1">

								<div class="portlet-title">

									<div class="caption">
										<i class="icon-globe"></i>基础数据信息
									</div>

									<div class="actions">

										<a href="javascript:;" class="btn green" onclick="add()"><i class="icon-plus"></i> 新增</a>

									</div>
								</div>

								<div class="portlet-body" id="user_date_table">
									<table class="table table-striped table-bordered table-hover" id="sample_1">

										<thead>

											<tr>
												<th class="hidden-480" style="text-align: center;">编码类型</th>
												<th>编码名称</th>
												<th>编码值</th>
												<th>编码描述</th>
												<th>备注</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:if test="${!empty dictList}">
												<c:forEach var="dl" items="${dictList}" varStatus="vs">
													<tr class="odd gradeX">
														<td class="hidden-480" style="text-align: center;">${dl.code_type}</td>
														<td>${dl.code_name}</td>
														<td>${dl.biz_code}</td>
														<td>${dl.code_desc}</td>
														<td>${dl.bak1}</td>
														<td class="center hidden-480" style="width: 145px;"><a href="javascript:void(0);" onclick="edit('${dl.code_type}','${dl.biz_code}')" class="btn mini purple"><i class="icon-edit"></i> 修改</a> &nbsp;&nbsp;&nbsp; <a href="javascript:void(0);" onclick="delDict('${dl.code_type}','${dl.biz_code}')" class="btn mini black"><i class="icon-trash"></i> 删除</a></td>
													</tr>
												</c:forEach>
											</c:if>

										</tbody>
									</table>
									<div class="row-fluid" style="margin-top: -18px;">
										<div class="span11" style="float: right;height: 40px;">
											<div class="dataTables_paginate paging_bootstrap pagination" style="float: right;margin-top: 10px;">
												${page.pageStr}
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</form>
		</div>
	</div>
	<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
	<!-- 确认窗口 -->
	<!-- 引入 -->
	<script type="text/javascript">
	
	//检索
	function search(){
		$("#dictForm").submit();
		layer.load(1, {
			  shade: [0.3,'#fff'], //0.1透明度的白色背景
			  time: 1000
			});
	}
	
		//新增
		function add(){	
			layer.open({
				type: 2, 
				title: "新增",
				skin: 'layui-layer-lan',
				area: ['670px', '470px'],
                 content: '<%=path%>/sysDict/goAdd'
		    });
 		}
		
		
		//编辑
		function edit(codeType,bizCode){
			layer.open({
				type: 2, 
				title: "修改",
				skin: 'layui-layer-lan',
				area: ['670px', '470px'],
<%-- 				content: '<%=path%>/sysDict/edit?code_type='+code_type+'&biz_code='+biz_code --%>
 			    content: "<%=path%>/sysDict/goEdit?codeType=" + codeType+"&bizCode="+bizCode 
		    });
		}
		
		//删除
		function delDict(codeType,bizCode) {
			//询问框
			layer.confirm('你确定要删除此记录吗？', {
				btn : [ '确定', '取消' ]
			//按钮
			}, function() {
				$.ajax({
					url : "<%=path%>/sysDict/delDict",
					data : { "codeType":codeType,"bizCode":bizCode
// 						codeType : codeType,
// 						bizCode : bizCode
					},
					type : "POST",
					success : function(result) {
						result = $.parseJSON(result);
						if (result.success) {
							layer.alert(result.msg, function(index) {
								layer.load(1, {
									  shade: [0.3,'#fff'], //0.1透明度的白色背景
									  time: 1000
									});
								location.reload();
							});
						} else {
							layer.alert(result.msg);
						}
					}
				});
			});
		}

		jQuery(document).ready(function() {
			App.init(); // initlayout and core plugins
			layer.load(1, {
				  shade: [0.3,'#fff'], //0.1透明度的白色背景
				  time: 2000
				});
		});
	</script>
</body>
</html>
