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
	 $(document).ready(function(){
		 $("input[name='user_code']").blur(function(){
				var ac=$("input[name='user_code']").val();
				if(ac==""){
					$('#addUser_msg').html("用户名不能为空！");
				}else if(restAdminUser(ac)){
						$('#addUser_msg').html("用户名已经存在！");
					}else{
						$('#addUser_msg').html("");
					}
			  });
		 
		 $("input[name='user_password']").blur(function(){
				var pwd=new RegExp("(?![a-z]+$|[0-9]+$)^[a-zA-Z0-9]{6,}$");
				var pd=$("input[name='user_password']").val();
				if(pd==""){
					$('#addUser_msg').html("密码不能为空！");
				}
				else if(!pwd.test(pd)){
					$('#addUser_msg').html("密码必须大于等于6位有字母和数字组成！");
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

//		$("#role_id").change(function() {
//			changeOrg();
//		});
//		changeOrg();
         initTreeOrg();
	})


     function initTreeOrg(){
	     var userId = $("#userId").val();
         $.ajax({
             type : "post",
             url : "<%=path%>/role/getOrgByRoleId",
             data : {
                 checkedFlag: 1
				 ,user_id: userId
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
					<%--$("#treeDemo").empty();--%>
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
	<%----%>
	 
	
	function submitForm(){
		var pwd=new RegExp("(?![a-z]+$|[0-9]+$)^[a-zA-Z0-9]{6,}$");
		var pd=$("input[name='user_password']").val();
		if(pd !="" ){
			if(!pwd.test(pd)){
				$('#addUser_msg').html("密码必须大于等于6位有字母和数字组成！");
				return false;
			}
		}
		var cpwd=$("input[name='confirmation']").val();
		if(cpwd !="" ){
			if(pd!=cpwd){
				$('#addUser_msg').html("两次输入密码不同！");
				return false;
			}
		}
		 
		return true;
	}
	
function  editUser(){
	var param =$.serializeObject($('#edituser_form'));
	if(submitForm()){
        var treeObj=$.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getCheckedNodes(true);
        var orgStr = "";

        if(nodes.length > 0){
            for(var key in nodes){
                orgStr += nodes[key].id + ",";
            }
        }

        param["org_str"] = orgStr;

		$.ajax({
			url: "<%=path%>/user/editUser",
			data: param,
			type : "POST",
			dataType: "json",
			success: function(result) {
                if(result.success==true){
                    parent.location.reload();
                    parent.layer.closeAll();
                }else{
                    layer.msg('修改失败，请联系管理员!', {});
                }
			}
		});
	}
	
}

function closeB(){
	parent.layer.closeAll();
}
	
	</script>
	
  </head>
  
  <body>
   <div class="portlet-body form" style="padding-top: 15px;margin-left: -30px;">
	<!-- BEGIN FORM-->
     <form id="edituser_form" class="form-horizontal"   >
         <input type="hidden" id="userId" name="id"   value="${userList.id}"/>
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
						<input type="text" readonly="readonly" name="user_code" value="${userList.user_code}">
					</div>
				</div>
				<div class="row-fluid">
				<div class="span1" align="right">
				</div>
				<div class="span3" align="right">
					<span_customer2>密码</span_customer2>
				</div>
					<div class="span8" align="left">
						<input type="password" name="user_password" placeholder="******">
					</div>
				</div>
				<div class="row-fluid">
				<div class="span1" align="right">
				</div>
				<div class="span3" align="right">
					<span_customer2>确认密码</span_customer2>
				</div>
					<div class="span8" align="left">
						<input type="password" name="confirmation" placeholder="******">
					</div>
				</div>
				<div class="row-fluid">
				<div class="span1" align="right">
				</div>
				<div class="span3" align="right">
					<span_customer2>中文名</span_customer2>
				</div>
					<div class="span8" align="left">
						<input type="text" name="user_real_name" value="${userList.user_real_name}">
					</div>
				</div>
				<div class="row-fluid">
				<div class="span1" align="right">
				</div>
				<div class="span3" align="right">
					<span_customer2>英文名</span_customer2>
				</div>
					<div class="span8" align="left">
						<input type="text" name="user_real_name_en" value="${userList.user_real_name_en}">
					</div>
				</div>
				<div class="row-fluid">
				<div class="span1" align="right">
				</div>
				<div class="span3" align="right">
					<span_customer2>手机号</span_customer2>
				</div>
					<div class="span8" align="left">
						<input type="text" name="user_mobile_1" value="${userList.user_mobile_1}">
					</div>
				</div>
    		</div>
    		<div class="span6" align="left">
    		  <div class="row-fluid">
    		  <div class="span3" align="right">
					<span_customer2>所属角色</span_customer2>
			  </div>
					<div class="span9" align="left">
						<select id="role_id" name="role_id">
	                      <c:if test="${!empty roleList}">
	                      <c:forEach var="role" items="${roleList}">
	                      	 <option value="${role.role_id }" <c:if test="${pd.role_id==role.role_id}">selected</c:if>>${role.role_name }</option>
	                      </c:forEach>
	                     </c:if>
					  	</select>
					</div>
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
				 <button type="button" class="btn blue" onclick="editUser()">确定</button>
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
