<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<link rel="stylesheet" href="<%=path%>/framework/css/bootstrap.min.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/font-awesome.min.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/style.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/style-responsive.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/style-metro.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/default.css" />
<link rel="stylesheet" href="<%=path%>/framework/css/datepicker.css" />
<script type="text/javascript" src="<%=path%>/framework/jquery/jquery-1.11.2.min.js"></script>
<script src="<%=path%>/framework/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/framework/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=path%>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>
<script type="text/javascript">
	
	 $(document).ready(function(){
		 $(".date-picker").datepicker({
		     	language: 'zh-CN',
		         autoclose: true,
		         todayHighlight: true
		     });
		 /*  $("input[name='batch_no']").blur(function(){
				var bc=$("input[name='batch_no']").val();
				if(bc==""){
					$('#addbatch_msg').html("批次号不能为空！");
				}else{
					$('#addbatch_msg').html("");
				}
			}); */
		 $("input[name='day_age_curr']").blur(function(){
				var day_age_curr=$("input[name='day_age_curr']").val();
				if(day_age_curr==""){
					alert("当前日龄不能为空！");
				    return false;
				}
			}); 
		/*  $("input[name='batch_date']").blur(function(){
				var rldate=$("input[name='batch_date']").val();
				if(rldate==""){
					$('#addbatch_msg').html("入栏日期不能为空！");
				}else{
					$('#addbatch_msg').html("");
				}
			});  */
		 $("input[name='batch_date']").change(function(){
				var rdate=$("input[name='batch_date']").val();
				var date = new Date(rdate).Format("yyyyMMdd");
				$("input[name='batch_no']").val(date); 
			}); 
		 
		 
		 
	 $("select[name='farm_id']").change(function() {
				$.ajax({
					type : "post",
					url : "<%=path%>/farm/getHouse",
					data : {
						"farmId" : $("select[name='farm_id']").val()
					},
					dataType: "json",
					success : function(result) {
						var list = result.obj;
						 $("select[name='house_code']").empty();
						for (var i = 0; i < list.length; i++) {
							$("select[name='house_code']").append("<option value=" + list[i].id + ">" + list[i].house_name+ "</option>");
						}
					}
				});
// 				$('#addbatch_msg').html("");
		});  
	});

	
	function  addBatch(){
		    var day_age_curr=$("input[name='day_age_curr']").val();
			var bc=$("input[name='batch_no']").val();
// 			 var co=$("input[name='count']").val();
			 var rldate=$("input[name='batch_date']").val();
			 var fid= $("select[name='farm_id']").val();
			 var hc=$("select[name='house_code']").val();
				/* if(bc==""){
					$('#addbatch_msg').html("批次号不能为空！");
				}else  */
				if(restBatch(bc,hc)){
					alert("该批次号已经存在！");
				    return false;
				}else if(day_age_curr==""){
					alert("当前日龄不能为空！");
				    return false;
				}else if(rldate==""){
					alert("入栏日期不能为空！");
				    return false;
				}else if(fid==""){
					alert("请选择农场！");
				    return false;
				}else if(hc==""){
					alert("请选择栋舍！");
				    return false;
				}else if(restBatchHouse(hc)){
					alert("该栋舍存在未出栏的批次,请先出栏！");
				    return false;
				}else{
					var param =$.serializeObject($('#addBatch_form'));
					var house_name=$("select[name='house_code']").find("option:selected").text();
					param["house_name"]=house_name;
					$.ajax({
						url: "<%=path%>/farm/addBatch",
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
function restBatch(str,hc){
	var rs;
	$.ajaxSetup({ async: false });
    $.get("<%=path%>/farm/isBatchNoNull?batch_no="+str+"&houseId="+hc, function(result){
    	rs = $.parseJSON(result);
   	 if(rs.msg=="1"){
        	rs = true;//存在
        }else{
        	rs = false;//不存在
        }
   });
   return rs; 
} 

//判断同一栋舍下如果有未出栏的的批次，不能做入栏操作
function restBatchHouse(str){
	var rs;
	$.ajaxSetup({ async: false });
    $.get("<%=path%>/farm/isBatchHouseNull?house_code=" + str, function(result) {
			rs = $.parseJSON(result);
			if (rs.msg == "1") {
				rs = true;//存在
			} else {
				rs = false;//不存在
			}
		});
		return rs;
	}

	function closeB() {
		parent.layer.closeAll();
	}
</script>

</head>

<body>
	<div class="portlet-body form" style="padding-top: 20px;margin-left: -30px;">
		<!-- BEGIN FORM-->
		<%--   <form action="<%=path %>/batch/addbatch" class="form-horizontal"  onsubmit="return submitForm()" > --%>
		<form id="addBatch_form" class="form-horizontal">
			<div class="control-group">
				<label class="control-label">批次号:</label>
				<div class="controls">
					<input type="text" class="span6 m-wrap" readonly="readonly" style="width: 220px;" name="batch_no" id="batch_no">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">数量:</label>
				<div class="controls">
					<input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" class="span6 m-wrap" style="width: 220px;" name="count">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">入拦日期:</label>
				<div class="controls">
					<div class="input-append date date-picker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
						<input class="m-wrap m-ctrl-medium date-picker" type="text" name="batch_date" id="batch_date" style="height: 34px;width: 193px;" /><span class="add-on"><i class="icon-calendar"></i></span>
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">所属农场:</label>
				<div class="controls">
					<select class="m-wrap" name="farm_id" style="width: 220px;">
						<option value="">全部</option>
						<c:if test="${!empty farmList}">
							<c:forEach var="farm" items="${farmList}">
								<option value="${farm.id }">${farm.farm_name_chs }</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">所属栋舍:</label>
				<div class="controls">
					<select class="m-wrap" name="house_code" tabindex="1" style="width: 220px;">
						<option value="">全部</option>
						<c:if test="${!empty houseList}">
							<c:forEach var="house" items="${houseList}">
								<option value="${house.id }">${house.house_name}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
			</div>

			<%-- <div class="control-group" >
				<label class="control-label" >出入拦类型:</label>
				<div class="controls" >
					<select id="feed_method" class="m-wrap"  name="feed_method"  style="width: 220px;">
					 <option value="">请选择</option>
                      <c:if test="${!empty operationType}">
                      <c:forEach var="ot" items="${operationType}">
                      <option value="${ot.biz_code}">${ot.code_name }</option>
                      </c:forEach>
                     </c:if>
				  	</select>
				</div>
			</div> --%>
            <div class="control-group">
				<label class="control-label">当前日龄:</label>
				<div class="controls">
					<input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" class="span6 m-wrap" style="width: 220px;" name="day_age_curr">
				</div>
			</div>
			<div class="form-actions" style="padding-left: 180px;">
				<button type="button" class="btn blue" onclick="javascript:addBatch()">
					<i class="icon-ok"></i>&nbsp;确 定&nbsp;&nbsp;&nbsp;
				</button>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<button type="button" class="btn" onclick="closeB()">&nbsp;&nbsp;&nbsp;取 消&nbsp;&nbsp;&nbsp;</button>
			</div>
		</form>
		<!-- END FORM-->
	</div>
</body>
</html>
