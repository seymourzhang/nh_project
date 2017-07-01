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
	<link rel="stylesheet" href="<%=path %>/framework/css/font-awesome.min.css" />
	<link rel="stylesheet" href="<%=path %>/framework/css/style.css" />
	<link rel="stylesheet" href="<%=path %>/framework/css/style-responsive.css" />
	<link rel="stylesheet" href="<%=path %>/framework/css/style-metro.css" />
	<link rel="stylesheet" href="<%=path %>/framework/css/default.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/datepicker.css" />
	<script type="text/javascript" src="<%=path %>/framework/jquery/jquery-1.11.2.min.js"></script>
	<script src="<%=path %>/framework/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
	<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>
	<script type="text/javascript">
	
	 $(document).ready(function(){
		 $(".date-picker").datepicker({
		     	 language: 'zh-CN',
		         format: 'yyyy-mm-dd',
		         autoclose: true,
		         todayHighlight: true
		     });
		 $("input[name='day_age_curr']").blur(function(){
				var co=$("input[name='day_age_curr']").val();
				if(co==""){
					alert("当前日龄不能为空！");
				    return false;
				}
			}); 
		 var batchData="${batchData.batch_date}";
		 batchData=batchData.substring(0,10);
		 $("input[name='batch_date']").val(batchData);
		 
	});
	
	function  editBatch(){
		 
			 var co=$("input[name='count']").val();
			 var id=$("#id").val();
			 var day_age_curr = $("input[name='day_age_curr']").val();
				 if(day_age_curr==""){
					alert("当前日龄不能为空！");
				    return false;
				}else{
					$.ajax({
						url: "<%=path%>/farm/editBatch",
						data: {
							id:id,
							count:co,
							day_age_curr:day_age_curr
						},
						type : "POST",
						dataType: "json",
						success: function(result) {
							if(result.msg=='1'){
								/* $("#tab_fag",window.parent.document).val(3);
								$("#farmViewForm",window.parent.document).submit();
								parent.layer.closeAll(); */
								parent.location.reload();   
								parent.layer.closeAll();
								
							}else{
								alert("修改失败！");
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
   <div class="portlet-body form" style="margin-left: -30px;margin-top: 20px;">
	<!-- BEGIN FORM-->
    <form id="editBatch_form" class="form-horizontal"   >
           <input type="hidden" name="id" id="id" value="${batchData.id}"/>
			<div class="control-group">
				<label class="control-label">批次号:</label>
				<div class="controls" >
					<input type="text" class="span6 m-wrap" style="width: 220px;" name="batch_no" value="${batchData.batch_no}" readonly="readonly">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">数量:</label>
				<div class="controls" >
					<input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"  class="span6 m-wrap" style="width: 220px;" name="count" value="${batchData.count}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" >入拦日期:</label>
				<div class="controls" >
					<div class="input-append date date-picker"  data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
						<input class="m-wrap m-ctrl-medium date-picker"  disabled="disabled" type="text" name="batch_date"  style="height: 34px;width: 193px;" value=""/><span class="add-on"><i class="icon-calendar"></i></span>
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">所属农场:</label>
				<div class="controls" >
					<select class="m-wrap"  name="farm_id"  style="width: 220px;" disabled="disabled">
					 <option value="">全部</option>
                      <c:if test="${!empty farmList}">
	                      <c:forEach var="farm" items="${farmList}">
	                     	  <option value="${farm.id }" <c:if test="${farm.id==batchData.farm_id}">selected</c:if>>${farm.farm_name_chs }</option>
	                      </c:forEach>
                     </c:if>
				  	</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">所属栋舍:</label>
				<div class="controls" >
					<select class="m-wrap" name="house_code" tabindex="1"  style="width: 220px;" disabled="disabled">
						<option value="">全部</option>
                           <c:if test="${!empty houseList}">
	                           <c:forEach var="house" items="${houseList}">
	                           		<option value="${house.id }" <c:if test="${house.id==batchData.house_code}">selected</c:if>>${house.house_name }</option>
	                           </c:forEach>
                           </c:if>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">当前日龄:</label>
				<div class="controls" >
					<input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"  class="span6 m-wrap" style="width: 220px;" name="day_age_curr" value="${batchData.day_age_curr}">
				</div>
			</div>
			<div class="form-actions" style="margin-left: 150px;" >
				<button type="button" class="btn blue" onclick="javascript:editBatch()"><i class="icon-ok"></i>&nbsp;确 定&nbsp;&nbsp;&nbsp;</button>
					&nbsp;&nbsp;&nbsp;&nbsp;
				<button type="button" class="btn" onclick="closeB()">&nbsp;&nbsp;&nbsp;取 消&nbsp;&nbsp;&nbsp;</button>
			</div>
		</form>
		<!-- END FORM-->  
	</div>
  </body>
</html>
