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
					<form action="<%=path%>/user/userManage" method="post" style="background-color: #ffffff;" id="userForm">
						<input type="hidden" name = "write_read" value="${pd.write_read}">
						<%-- <input type="hidden" name="id" value="${pd.id}">
						<input type="hidden" name="pid" value="${pd.pid}"> --%>
<!-- 							<div class="container-fluid"> -->
								<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;margin-top: -15px;">
								<div style="margin-left: 10px;">
									<div class="span3" align="left">
										<span_customer2>用户</span_customer2>
										<input type="text" value="${pd.user_real_name }" placeholder="请输入用户登录名或名称" name="user_real_name">
									</div>
									<div class="span3" align="left">
										<span_customer2>手机号码</span_customer2>
										<input type="text" value="${pd.user_mobile_1 }" placeholder="请输入用户手机号码" name="user_mobile_1">
									</div>
									<div class="span3" align="left">
										<a href="javascript:search();" class="btn blue"><i class="icon-search"></i> 查询</a>
									</div>
									<div class="span3" align="left">
									</div>
									</div>
								</div>

<!-- 								<div class="row-fluid"> -->
<!-- 									<div class="span12"> -->
<!-- 										<hr style="height:10px;border:none;border-top:1px solid #555555;" /> -->
<!-- 									</div> -->
<!-- 								</div> -->
                                
								<div class="row-fluid" style="padding-top: 5px;">
									<div class="span12">
										<a href="javascript:;" class="btn blue" onclick="add();"><i class="icon-plus"></i> 新增</a>
									</div>
								</div>

								<div class="row-fluid">
									<div class="span12">
										<table id="userListTable"></table>
										<script type="text/javascript">
                                            var userList = [];
										</script>
										<c:if test="${!empty listUser}">
											<c:forEach var="lu" items="${listUser}" varStatus="vs">
												<script type="text/javascript">
													var tmpObj = new Object();
													tmpObj.id = "${lu.id}";
													tmpObj.user_code = "${lu.user_code}";
													tmpObj.user_real_name = "${lu.user_real_name}";
                                                    tmpObj.user_mobile_1 = "${lu.user_mobile_1}";
                                                    tmpObj.farm_name_chs = "${lu.farm_name_chs}";
                                                    tmpObj.house_name = "${lu.house_name}";
                                                    tmpObj.user_status = "${lu.user_status}";
                                                    tmpObj.user_status_desc = "${lu.user_status == 1 ? '正常' : '停用'}";
                                                    tmpObj.farm_id = "${lu.farm_id}";
                                                    tmpObj.house_code = "${lu.house_code}";
                                                    tmpObj.operate = '<a href="javascript:void(0);" onclick="editUser(' + "${lu.id}" + ')" class="btn mini blue">';
                                                    tmpObj.operate += '<i class="icon-edit"></i> 修改</a> &nbsp;&nbsp;&nbsp;&nbsp;';
                                                    tmpObj.operate += '<a href="javascript:void(0);" onclick="delUser(' + "${lu.id}" + ')" class="btn mini black">';
                                                    tmpObj.operate += '<i class="icon-trash"></i> 删除</a>';
                                                    userList.push(tmpObj);
												</script>
											</c:forEach>
                                        </c:if>
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
	//检索
	function search(){
		$("#userForm").submit();
		/* layer.load(1, {
			  shade: [0.3,'#fff'], //0.1透明度的白色背景
			  time: 1000
			}); */
	}
	
		//新增
		function add(){
			if(isRead==0){
				layer.msg('无权限，请联系管理员!', {});
				return;
			}
			layer.open({
				type: 2, 
				title: "新增",
				skin: 'layui-layer-lan',
				area: ['670px', '320px'],
			    content: '<%=path%>/user/addUserUrl'
		    });
		}
		//编辑
		function editUser(id){
			if(isRead==0){
				layer.msg('无权限，请联系管理员!', {});
				return;
			}
			layer.open({
				type: 2, 
				title: "修改",
				skin: 'layui-layer-lan',
				area: ['670px', '320px'],
			    content: "<%=path%>/user/editUserUrl?id=" + id
			});
		}
		//删除
		function delUser(id) {

			if(isRead==0){
				layer.msg('无权限，请联系管理员!', {});
				return;
			}
			//询问框
			layer.confirm('确定删除该用户吗？', {
                skin: 'layui-layer-lan'
                , closeBtn: 0
                , shift: 4 //动画类型
                , btn : [ '确定', '取消' ]
                //按钮
            }, function() {
				$.ajax({
					url : "<%=path%>/user/delUser",
					data : {
						id : id
					},
					type : "POST",
					success : function(result) {
						result = $.parseJSON(result);
						if (result.success) {
                            location.reload();
						} else{
                            layer.msg("删除用户失败！(" + result.msg + ")", {});
						}
					}
				});
			});
		}

		jQuery(document).ready(function() {
			App.init(); // initlayout and core plugins
			/* layer.load(1, {
				  shade: [0.3,'#fff'], //0.1透明度的白色背景
				  time: 2000
				}); */
		});
	</script>
	<script type="text/javascript" src="<%=path%>/modules/user/js/UserManage.js"></script>
</body>
</html>
