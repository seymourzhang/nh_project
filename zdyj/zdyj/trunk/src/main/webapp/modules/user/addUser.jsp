<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	  <%@ include file="../../framework/inc.jsp"%>
    <base href="<%=basePath%>">
    <link rel="stylesheet" href="<%=path %>/framework/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/style-metro.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/style.css"/>
    <link rel="stylesheet" href="<%=path %>/framework/css/bootstrap-fileupload.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/uniform.default.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/zTreeStyle.css" />
	<script type="text/javascript" src="<%=path %>/framework/jquery/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/jquery/jquery.ztree.core.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/jquery/jquery.ztree.excheck.js"></script>
	<script type="text/javascript">
		/* var zNodes =[
			{ id:1, pId:0, name:"随意勾选 1", open:true},
			{ id:11, pId:1, name:"随意勾选 1-1", open:true},
			{ id:111, pId:11, name:"随意勾选 1-1-1"},
			{ id:112, pId:11, name:"随意勾选 1-1-2"},
			{ id:12, pId:1, name:"随意勾选 1-2", open:true},
			{ id:121, pId:12, name:"随意勾选 1-2-1"},
			{ id:122, pId:12, name:"随意勾选 1-2-2"},
			{ id:2, pId:0, name:"随意勾选 2", checked:true, open:true},
			{ id:21, pId:2, name:"随意勾选 2-1"},
			{ id:22, pId:2, name:"随意勾选 2-2", open:true},
			{ id:221, pId:22, name:"随意勾选 2-2-1", checked:true},
			{ id:222, pId:22, name:"随意勾选 2-2-2"},
			{ id:23, pId:2, name:"随意勾选 2-3"}
		]; */

	 $(document).ready(function(){
		 $("input[name='user_code']").blur(function(){
				var ac=$("input[name='user_code']").val();
				var acd=new RegExp("^[0-9A-Za-z_]{6,}$");
				if(ac==""){
					$('#addUser_msg').html("用户名不能为空！");
				}else if(restAdminUser(ac)){
						$('#addUser_msg').html("用户名已经存在！");
					}else if(!acd.test(ac)){
						$('#addUser_msg').html("用户名必须大于等于6位有字母和数字或下划线组成！");
					}else{
						$('#addUser_msg').html("");
					}
			  });
		 
		 $("input[name='user_password']").blur(function(){
				var pwd=new RegExp("(?![0-9A-Z]+$)(?![0-9a-z]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{8,}$");
				var pd=$("input[name='user_password']").val();
				if(pd==""){
					$('#addUser_msg').html("密码不能为空！");
				}
				else if(!pwd.test(pd)){
					$('#addUser_msg').html("密码必须大于等于6位有字母和数字或下划线组成！");
				}else{
					$('#addUser_msg').html("");
				}
			  });
			
			$("input[name='confirmation']").blur(function(){
				var pd=$("input[name='user_password']").val();
				var cpwd=$("input[name='confirmation']").val();
				if(pd==""){
					$('#addUser_msg').html("密码不能为空！");
				} else if(pd!=cpwd){
					$('#addUser_msg').html("两次输入密码不同！");
				}else{
					$('#addUser_msg').html("");
				}
			  });


         initTreeOrg();
//		$("#role_id").change(function() {
//			changeOrg();
//		});
//		changeOrg();
	})

        function initTreeOrg(){
            $.ajax({
                type : "post",
                url : "<%=path%>/role/getOrgByRoleId",
                data : {
                    checkedFlag: 0
				},
                dataType: "json",
                success : function(result) {
                    var zNodes =result.obj;
                    var orgListTreeSetting = {
                        view: {
                            selectedMulti: false,
                            fontCss: { 'color': 'blue', 'font-family': '微软雅黑', 'font-size': '12px' }
                        },
                        check: {
                            enable: true,
                            chkDisabledInherit: true
                        },
                        data: {
                            simpleData: {
                                enable: true
                            }
                        }
                    };
                    $.fn.zTree.init($("#treeDemo"), orgListTreeSetting, zNodes);
                }
            })
        }

	<%--function changeOrg(){--%>
			<%--$.ajax({--%>
				<%--type : "post",--%>
				<%--url : "<%=path%>/role/getOrgByRoleId",--%>
				<%--data : {--%>
					<%--"role_id" : $("#role_id").val()--%>
				<%--},--%>
				<%--dataType: "json",--%>
				<%--success : function(result) {--%>
					<%--var setting = {--%>
							<%--check: {--%>
								<%--enable: true,--%>
								<%--chkDisabledInherit: true--%>
							<%--},--%>
							<%--data: {--%>
								<%--simpleData: {--%>
									<%--enable: true--%>
								<%--}--%>
							<%--}--%>
						<%--};--%>
					<%--var zNodes =result.obj;--%>
					<%--$.fn.zTree.init($("#treeDemo"), setting, zNodes);--%>
					<%----%>
				<%--}--%>
			<%--})--%>
		<%--}--%>
	
	
	/* function houseClick(){
		 var farmId= $("#farm_id").val();
			if(farmId==""){
				$("#addUser_msg").html("请选择农场！");
				$("input[type=checkbox]").attr("disabled",true);
				$("input[name=house_code1]:checkbox").attr("checked", false);
			}else{
				$("input[type=checkbox]").attr("disabled",false);
			}
	 } */
	 
	
	function submitForm(){
			var ac=$("input[name='user_code']").val();
			var acd=new RegExp("^[0-9A-Za-z_]{6,}$");
			if(ac==""){
				$('#addUser_msg').html("用户名不能为空！");
				return false;
			}else if(restAdminUser(ac)){
				$('#addUser_msg').html("用户名已经存在！");
				return false;
			}else if(!acd.test(ac)){
				$('#addUser_msg').html("用户名必须大于等于6位有字母和数字或下划线组成！");
				return false;
			}
			var pwd=new RegExp("(?![0-9A-Z]+$)(?![0-9a-z]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{8,}$");
			var pd=$("input[name='user_password']").val();
			if(pd==""){
				$('#addUser_msg').html("密码不能为空！");
				return false;
			}else if(!pwd.test(pd)){
				$('#addUser_msg').html("密码必须大于等于6位有字母和数字或下划线组成！");
				return false;
			}
			var pd=$("input[name='user_password']").val();
			var cpwd=$("input[name='confirmation']").val();
			if(pd==""){
				$('#addUser_msg').html("密码不能为空！");
				return false;
			} else if(pd!=cpwd){
				$('#addUser_msg').html("两次输入密码不同！");
				return false;
			}
		 
		return true;
	}
	
function  addUser(){
	 /* var chk_value =[];    
	  $("input[name=house_code1]:checked").each(function(){    
	  	 chk_value.push($(this).val());    
	  });  
	  $("#house_code").val(chk_value); */
    var param =$.serializeObject($('#adduser_form'));

    var treeObj=$.fn.zTree.getZTreeObj("treeDemo");
    var nodes = treeObj.getCheckedNodes(true);
	var orgStr = "";

    if(nodes.length > 0){
        for(var key in nodes){
            orgStr += nodes[key].id + ",";
        }
    } else{
        layer.msg('请为新建用户选择至少一个机构!', {});
        return;
	}

    param["org_str"] = orgStr;

	if(submitForm()){
		$.ajax({
			url: "<%=path%>/user/addUser",
			data: param,
			type : "POST",
			dataType: "json",
			success: function(result) {
				if(result.success==true){
					parent.location.reload();   
					parent.layer.closeAll();
				}else{
                    layer.msg('添加失败，请联系管理员!', {});
				}
			}
		});
		
		
	}
	
}
	
	
//判断用户名是否重名
function restAdminUser(str){
	var rs;
	$.ajaxSetup({ async: false });
    $.get("<%=path%>/user/isUserCodeNull?user_code="+str, function(result){
    	rs = $.parseJSON(result);
   	 if(rs.msg=="1"){
        	rs = true;//存在
        }else{
        	rs = false;//不存在
        }
   });
   return rs; 
}



function closeB(){
	parent.layer.closeAll();
}
	
	</script>
	
  </head>
  
  <body>
   <div class="portlet-body form" style="padding-top: 15px;margin-left: -30px;">
	<!-- BEGIN FORM-->
	  <%--   <form action="<%=path %>/user/addUser" class="form-horizontal"  onsubmit="return submitForm()" > --%>
		<form id="adduser_form" class="form-horizontal"   >
			<div class="container-fluid">
				<div class="row-fluid">
						<div class="span6" align="left">
							<div class="row-fluid">
								<div class="span1" align="right">
								</div>
								<div class="span3" align="right">
									<span_customer2>用户名</span_customer2>
								</div>
								<div class="span8" align="left">
									<input type="text" name="user_code" placeholder="请输入用户登录名称">
								</div>
							</div>
							<div class="row-fluid">
								<div class="span1" align="right">
								</div>
								<div class="span3" align="right">
									<span_customer2>密码</span_customer2>
								</div>
								<div class="span8" align="left">
									<input type="password" name="user_password" placeholder="请输入用户登录密码">
								</div>
							</div>
							<div class="row-fluid">
								<div class="span1" align="right">
								</div>
								<div class="span3" align="right">
									<span_customer2>确认密码</span_customer2>
								</div>
								<div class="span8" align="left">
									<input type="password" name="confirmation" placeholder="请重复输入用户登录密码">
								</div>
							</div>
							<div class="row-fluid">
								<div class="span1" align="right">
								</div>
								<div class="span3" align="right">
									<span_customer2>中文名</span_customer2>
								</div>
								<div class="span8" align="left">
									<input type="text" name="user_real_name" placeholder="请输入用户中文名称">
								</div>
							</div>
							<div class="row-fluid">
								<div class="span1" align="right">
								</div>
								<div class="span3" align="right">
									<span_customer2>英文名</span_customer2>
								</div>
								<div class="span8" align="left">
									<input type="text" name="user_real_name_en" placeholder="请输入用户英文名称">
								</div>
							</div>
							<div class="row-fluid">
								<div class="span1" align="right">
								</div>
								<div class="span3" align="right">
									<span_customer2>手机号</span_customer2>
								</div>
								<div class="span8" align="left">
									<input type="text" name="user_mobile_1" placeholder="请输入用户的手机号码">
								</div>
							</div>
						</div>
						<div class="span6" align="left">
							<div class="row-fluid">
								<div class="span3" align="right">
									<span_customer2>所属角色</span_customer2>
								</div>
								<div class="span9" align="left">
										<select id="role_id"  name="role_id" >
											<c:if test="${!empty roleList}">
												<c:forEach var="role" items="${roleList}">
													<option value="${role.role_id }">${role.role_name }</option>
												</c:forEach>
											</c:if>
										</select>
								</div>
								<div class="row-fluid">
									<div class="span3" align="right">
										<span_customer2>所属机构</span_customer2>
									</div>
									<div class="span9" align="left">
										<div class="zTreeDemoBackground left" style="border:1px solid #e5e5e5;height: 140px;overflow:auto;">
											<ul id="treeDemo" class="ztree"></ul>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

				<div class="row-fluid">
					<div class="span4" align="center">
					</div>
					<div class="span4" align="center">
						<label style="color: red;text-align: center;" id="addUser_msg"></label>
					</div>
					<div class="span4" align="center">
					</div>
				</div>

				<div class="row-fluid">
					<div class="span3" align="center">
					</div>
					<div class="span3" align="center">
						<button type="button" class="btn blue" onclick="addUser()">确定</button>
					</div>
					<div class="span3" align="center">
						<button type="button" class="btn" onclick="closeB()">取消</button>
					</div>
					<div class="span3" align="center">
					</div>
				</div>

			</div>
		</form>
		<!-- END FORM-->  
	</div>
  </body>
</html>
