<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <link rel="stylesheet" href="<%=path %>/framework/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/style-metro.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/style.css"/>
    <link rel="stylesheet" href="<%=path %>/framework/css/bootstrap-fileupload.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/uniform.default.css" />
	<script type="text/javascript" src="<%=path %>/framework/jquery/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>
	<script type="text/javascript">
// 	 var houseCode= '${pd.house_code}';

	 $(document).ready(function(){
		 $("input[name='codeDesc']").blur(function(){
				var desc=$("input[name='codeDesc']").val();
				if(desc==""){
					$('#editDict_msg').html("编码描述不能为空！");
				}else{
					$('#editDict_msg').html("");
				}
		 });
		 
		 $("input[name='codeName']").blur(function(){
				var name=$("input[name='codeName']").val();
				if(name==""){
					$('#editDict_msg').html("编码类型不能为空！");
				}else{
					$('#editDict_msg').html("");
				}
		 });
		
	 })
	
function  editDict(){
	var name=$("input[name='codeName']").val();
	var desc=$("input[name='codeDesc']").val();
	
    if(name==""){
		$('#addSysDict_msg').html("编码名称不能为空！");
	}else if(desc==""){
		$('#addSysDict_msg').html("编码描述不能为空！");
	}else{
	var param =$.serializeObject($('#edit_dict'));
		$.ajax({
			url: "<%=path%>/sysDict/edit",
			data: param,
			type : "POST",
			dataType: "json",
			success: function(result) {
				if(result.msg=='1'){
					parent.location.reload();   
					parent.layer.closeAll();
				}else{
					alert("编辑失败！");
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
  <%--   <form action="<%=path %>/user/addUser" class="form-horizontal"  onsubmit="return submitForm()" > --%>
    <form id="edit_dict" class="form-horizontal"   >
			<div class="control-group" style="float: left;display:inline; ">
				<label class="control-label" style="width: 100px;">编码类型:</label>
				<div class="controls" style="margin-left: 110px;">
					<input type="text" class="span6 m-wrap" style="width: 230px;" readonly="readonly" name="codeType" value="${dictList.code_type}">
				</div>
			</div>
			<div class="control-group" style="float: left;display:inline; ">
				<label class="control-label" style="width: 100px;">编码描述:</label>
				<div class="controls" style="margin-left: 110px;">
					<input type="text" class="span6 m-wrap" style="width: 230px;" name="codeDesc" value="${dictList.code_desc}">
				</div>
			</div>
			<div class="control-group" style="float: left;display:inline; ">
				<label class="control-label" style="width: 100px;">编号:</label>
				<div class="controls" style="margin-left: 110px;">
					<input type="text" class="span6 m-wrap" style="width: 230px;" readonly="readonly" name="bizCode" value="${dictList.biz_code}">
				</div>
			</div>
			<div class="control-group" style="float: left;display:inline; ">
				<label class="control-label" style="width: 100px;">编码名称:</label>
				<div class="controls" style="margin-left: 110px;">
					<input type="text" class="span6 m-wrap" style="width: 230px;" name="codeName" value="${dictList.code_name}">
				</div>
			</div>
			<div class="control-group" style="float: left;display:inline; ">
				<label class="control-label" style="width: 100px;">备注:</label>
				<div class="controls" style="margin-left: 110px;">
					<input type="text" class="span6 m-wrap" style="width: 230px;" name="bak1" value="${dictList.bak1}">
				</div>
			</div>
		
			<div class="control-group" style="clear:both;height: 30px;text-align: center;">
				<label class="control-label" style="padding-left: 140px;color: red; width:500px; text-align: center;" id="editDict_msg"></label>
			</div>
			<div class="form-actions" style="padding-left: 290px;" >
				<button type="button" class="btn blue" onclick="editDict()"><i class="icon-ok"></i>&nbsp;确 定&nbsp;&nbsp;&nbsp;</button>
					&nbsp;&nbsp;&nbsp;&nbsp;
				<button type="button" class="btn" onclick="closeB()">&nbsp;&nbsp;&nbsp;取 消&nbsp;&nbsp;&nbsp;</button>
			</div>
		</form>
		<!-- END FORM-->  
	</div>
  </body>
</html>
