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
					<form action="<%=path%>/user/userManage2" method="post" style="background-color: #ffffff;" id="userForm">
						<input type="hidden" name = "write_read" value="${pd.write_read}">
						<%-- <input type="hidden" name="id" value="${pd.id}">
						<input type="hidden" name="pid" value="${pd.pid}"> --%>
<!-- 							<div class="container-fluid"> -->
								<div style="background:#e7e5e5;padding-top: 10px;margin-top: -15px;">
								<div style="margin-left: 10px;">
								<div class="row-fluid" >
								    <div class="span3" align="left">
								    <span_customer2>用户类型</span_customer2>
								    <select id="user_type"  name="user_type" >
								    <option value="">全部</option>
											<c:if test="${!empty userTypeList}">
												<c:forEach var="userType" items="${userTypeList}">
													<option value="${userType.biz_code }">${userType.code_name }</option>
												</c:forEach>
											</c:if>
										</select>
								    </div>
									<div class="span3" align="left">
										<span_customer2>用户&nbsp;&nbsp;&nbsp;</span_customer2>
										<input type="text" value="${pd.user_real_name }" placeholder="请输入用户登录名或名称" name="user_real_name" id="user_real_name">
									</div>
									<div class="span3" align="left">
										<span_customer2>手机号码&nbsp;</span_customer2>
										<input type="text" value="${pd.user_mobile_1 }" placeholder="请输入用户手机号码" name="user_mobile_1" id="user_mobile_1" style="margin-left: 2px;">
									</div>
									<div class="span3" align="left">
										
									</div>
									
									</div>
									<div class="row-fluid">
                                    <div class="span3" >
									        <span_customer2>所在省份</span_customer2>
												<select id="province_id"  name="farm_add1">
													<option value="">全部</option>
													<c:if test="${!empty prlist}">
														<c:forEach var="prl" items="${prlist}">
															<option value="${prl.id }">${prl.short_name }</option>
														</c:forEach>
													</c:if>
													<option value="1">其它</option>
												</select>
									</div>
									<div class="span3" >
									    <span_customer2>所在市</span_customer2>
										<select id="city_id" name="farm_add2">
											<option value="">全部</option>
										</select>
									</div>
									<div class="span3" >
											<span_customer2>所在区/县</span_customer2>
											<select id="area_id" name="farm_add3">
												<option value="">全部</option>
											</select>
									</div>
									<div class="span3" align="left">
										<a href="javascript:search();" class="btn blue"><i class="icon-search"></i> 查询</a>
									</div>
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
                                                    tmpObj.user_type = "${lu.user_type}";
                                                    tmpObj.user_type_name = "${lu.user_type_name}";
                                                    tmpObj.farm_id = "${lu.farm_id}";
                                                    tmpObj.house_code = "${lu.house_code}";
                                                    tmpObj.farm_add1 = "${lu.farm_add1}";
                                                    tmpObj.farm_add2 = "${lu.farm_add2}";
                                                    tmpObj.farm_add3 = "${lu.farm_add3}";
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
// 		$("#userForm").submit();
		var param = {user_type:$("#user_type").val(),user_real_name:$("#user_real_name").val(),user_mobile_1:$("#user_mobile_1").val(),
				farm_add1:$("#province_id").val(),farm_add2:$("#city_id").val(),farm_add3:$("#area_id").val()};
    	$.ajax({
    		url : path + "/user/userManage2",
    		data : param,
    		type : "POST",
    		dataType : "json",
    		success : function(result) {
    			var list = result.obj;
    			var dataJosn = $.parseJSON(JSON.stringify(list));
    			$("#userListTable").bootstrapTable("load",dataJosn);
    		}
    	});
		
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
		
		$("#province_id").change(function() {
			$.ajax({
				type : "post",
				url : "<%=path%>/farm/getAreaChina",
				data : {
					"parent_id":$("#province_id").val(),
					"level" : 2
				},
				dataType: "json",
				success : function(result) {
					var list = result.obj;
					$("#city_id option").remove();
					$("#city_id").append("<option value=''>" + "全部"+ "</option>");
					for (var i = 0; i < list.length; i++) {
						$("#city_id").append("<option value=" + list[i].id + ">" + list[i].short_name+ "</option>");
					}
					
					if($("#province_id").val() ==""){
						$("#city_id").val("");
					}else{
						$("#city_id").val("");
					}
					setAreaId();
				}
			});
	});
		
		$("#city_id").change(function() {
			setAreaId();
		});
		
		function setAreaId(){
			$.ajax({
				type : "post",
				url : "<%=path%>/farm/getAreaChina",
				data : {
					"parent_id":$("#city_id").val(),
					"parent_id2":$("#province_id").val(),
					"level" : 3
				},
				dataType: "json",
				success : function(result) {
					var list = result.obj;
					$("#area_id option").remove();
					$("#area_id").append("<option value=''>" + "全部"+ "</option>");
					for (var i = 0; i < list.length; i++) {
						$("#area_id").append("<option value=" + list[i].id + ">" + list[i].short_name+ "</option>");
					}
					
					if($("#city_id").val() ==""){
						$("#area_id").val("");
					}else{
						$("#area_id").val("");
					}
				}
				,error:function(XMLHttpRequest, textStatus, errorThrown) {
                     alert(textStatus);
                     alert(errorThrown);
					}
			})
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
