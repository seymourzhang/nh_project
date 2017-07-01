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
	$(document).ready(function(){
		  $("input[name='codeName']").blur(function(){
				var name=$("input[name='codeName']").val();
				if(name==""){
					$('#addSysDict_msg').html("编码名称不能为空！");
				}else{
					$('#addSysDict_msg').html("");
				}
		});
		
		  $("input[name='codeDesc']").blur(function(){
				var name=$("input[name='codeDesc']").val();
				if(name==""){
					$('#addSysDict_msg').html("编码描述不能为空！");
				}else{
					$('#addSysDict_msg').html("");
				}
		});
	})
	
	function addSysDict(){
		var type=$("input[name='codeType']").val();
		var code=$("input[name='bizCode']").val();
		var name=$("input[name='codeName']").val();
		var desc=$("input[name='codeDesc']").val();
		
		if(type==""){
			$('#addSysDict_msg').html("编码类型不能为空！");
		}else if(name==""){
			$('#addSysDict_msg').html("编码名称不能为空！");
		}else if(code==""){
			$('#addSysDict_msg').html("该类型编码值不能为空！");
		}else if(restDict(code,type)){
			$('#addSysDict_msg').html("该类型编码值已经存在！");
		}else if(desc==""){
			$('#addSysDict_msg').html("编码描述不能为空！");
		}else{
		
		var param =$.serializeObject($('#add_dict'));
		$.ajax({
			url: "<%=path%>/sysDict/save",
			data: param,
			type : "POST",
			dataType: "json",
			success: function(result) {
				if(result.msg=='1'){
					parent.location.reload();   
					parent.layer.closeAll();
				}else{
					alert("添加失败！");
				}
			}
		});
	  }
	}
	
	//判断批次是否重名
	function restDict(str,type){
		var rs;
		$.ajaxSetup({ async: false });
	    $.get("<%=path%>/sysDict/isRepeat?bizCode="+str+"&codeType="+type, function(result){
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

   <div class="portlet-body form" style="padding-top: 30px;margin-left: -30px;">  
       <form id="add_dict" class="form-horizontal"   >
			<div class="control-group" style="float: left;display:inline; ">
				<label class="control-label" style="width: 100px;">编码类型:</label>
				<div class="controls" style="margin-left: 110px;">
					<input type="text" class="span6 m-wrap" style="width: 230px;" name="codeType">
				</div>
			</div>
			<div class="control-group" style="float: left;display:inline; ">
				<label class="control-label" style="width: 100px;">编号名称:</label>
				<div class="controls" style="margin-left: 110px;">
					<input type="text" class="span6 m-wrap" style="width: 230px;" name="codeName">
				</div>
			</div>
			<div class="control-group" style="float: left;display:inline; ">
				<label class="control-label" style="width: 100px;">编码值:</label>
				<div class="controls" style="margin-left: 110px;">
					<input type="text" class="span6 m-wrap" style="width: 230px;" name="bizCode">
				</div>
			</div>
			<div class="control-group" style="float: left;display:inline; ">
				<label class="control-label" style="width: 100px;">编码描述:</label>
				<div class="controls" style="margin-left: 110px;">
					<input type="text" class="span6 m-wrap" style="width: 230px;" name="codeDesc">
				</div>
			</div>
			<div class="control-group" style="float: left;display:inline; ">
				<label class="control-label" style="width: 100px;">备注:</label>
				<div class="controls" style="margin-left: 110px;">
					<input type="text" class="span6 m-wrap" style="width: 230px;" name="bak1">
				</div>
			</div>
			
			<div class="control-group" style="clear:both;height: 30px;text-align: center;">
				<label class="control-label" style="padding-left: 140px;color: red; width:500px; text-align: center;" id="addSysDict_msg"></label>
			</div>
			<div class="form-actions" style="padding-left: 290px;" >
				<button type="button" class="btn blue" onclick="addSysDict();"><i class="icon-ok"></i>&nbsp;确 定&nbsp;&nbsp;&nbsp;</button>
					&nbsp;&nbsp;&nbsp;&nbsp;
				<button type="button" class="btn" onclick="closeB();">&nbsp;&nbsp;&nbsp;取 消&nbsp;&nbsp;&nbsp;</button>
			</div>
		</form>
	</div>

		<script src="framework/js/bootstrap.min.js"></script>
</body>
</html>