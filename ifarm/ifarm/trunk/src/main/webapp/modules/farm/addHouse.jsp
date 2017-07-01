<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%--<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">--%>
<%--<html>--%>
<!DOCTYPE html>
  <head>
	<%@ include file="../../framework/inc.jsp"%>
    <base href="<%=basePath%>">
    <%--<link rel="stylesheet" href="<%=path %>/framework/css/bootstrap.min.css" />--%>
    <%--<link rel="stylesheet" href="<%=path %>/framework/css/style-metro.css" />--%>
    <%--<link rel="stylesheet" href="<%=path %>/framework/css/style.css"/>--%>
    <%--<link rel="stylesheet" href="<%=path %>/framework/css/bootstrap-fileupload.css" />--%>
    <%--<link rel="stylesheet" href="<%=path %>/framework/css/uniform.default.css" />--%>
	<%--<script type="text/javascript" src="<%=path %>/framework/jquery/jquery.min.js"></script>--%>
	<%--<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>--%>

	<script type="text/javascript">
//		var deviceList = [{deviceCode: '1234567890'},{deviceCode: '09876543210987654321'}];
        var deviceList = [];

function  addHouse(){
	 var chk_value =[];    
	  $("input[name=deviceKey1]:checked").each(function(){    
	  	 chk_value.push($(this).val());    
	  });  
	  $("#deviceKey").val(chk_value);
	  /* alert(chk_value); */
	  var hn=$("input[name='house_name']").val();
	  var fid=$("select[name='farm_id']").val();
// 	  var ht=$("select[name='house_type']").val();
	 /*  var dk=$("#deviceKey").val(); */
		if(hn==""){
//			$('#addHouse_msg').html("栋舍名称不能为空！");
            layer.msg("栋舍名称不能为空");
		}else if(fid==""||fid==null){
            layer.msg("请选择农场");
		}
// 		else if(ht==""||ht==null){
//             layer.msg("请选择栋舍类型");
// 		}
		else if(restHouseName(hn,fid)){
            layer.msg("栋舍名称已经存在");
		}else{
			var param =$.serializeObject($('#addHouse_form'));
			 $.ajax({
				url: "<%=path%>/farm/addHouse",
				data: param,
				type : "POST",
				dataType: "json",
				success: function(result) {
					if(result.msg=='1'){
//						$("#tab_fag",window.parent.document).val(3);
//						$("#farmViewForm",window.parent.document).submit();
                        parent.location.reload();
                        parent.layer.closeAll();
					}else{
                        layer.msg("添加失败");
					}
				}
			});  
		}
}
	
	
//判断栋舍是否重名
function restHouseName(hn,fid){
	var rs;
	$.ajaxSetup({ async: false });
    $.get("<%=path%>/farm/isHouseNameNull?house_name="+hn+"&farm_id="+fid, function(result){
    	rs = $.parseJSON(result);
   	 if(rs.msg=="1"){
        	rs = true;//存在
        }else{
        	rs = false;//不存在
        }
   });
   return rs; 
}

//设置栋舍类型
function setHouseType(){
	$.ajax({
		type : "post",
		url : "<%=path%>/farm/setHouseType",
		data : {
			"farmId":$("#farm_id").val()
		},
		dataType: "json",
		success : function(result) {
			var list = result.obj;
			document.getElementById("house_type").value = list;
		}
	});
}


function closeB(){
	parent.layer.closeAll();
}


	</script>



  </head>
  
  <body>
   <div class="portlet-body form">
	<!-- BEGIN FORM-->
    <form id="addHouse_form" class="form-horizontal"   >
		<input type="hidden" name="deviceKey" id="deviceKey" />
		<input type="hidden" name="house_type" id="house_type"/>
		<div class="row-fluid">
			<div class="span12">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span5">
				<span_customer2>栋舍名称</span_customer2>
				&nbsp;&nbsp;
				<input type="text" style="width: 180px;" name="house_name" placeholder="请输入栋舍的名称">
			</div>
			<div class="span5">
			<span_customer2>所属农场</span_customer2>
				&nbsp;&nbsp;
				<select id="farm_id" name="farm_id" style="width: 200px;" onchange="setHouseType();">
					<option value="">请选择</option>
					<c:if test="${!empty farmList}">
						<c:forEach var="farm" items="${farmList}">
							<option value="${farm.id }">${farm.farm_name_chs }</option>
						</c:forEach>
					</c:if>
				</select>
			</div>
			<div class="span1">
			</div>
		</div>
<!-- 		<div class="row-fluid"> -->
<!-- 			<div class="span12"> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div class="row-fluid"> -->
<!-- 			<div class="span1"> -->
<!-- 			</div> -->
<!-- 			<div class="span5"> -->
				
<!-- 			</div> -->
<!-- 			<div class="span5"> -->
<!-- 				<span_customer2>栋舍类型</span_customer2> -->
<!-- 				&nbsp;&nbsp; -->
<!-- 				<select id="house_type" name="house_type"  style="width: 200px;"> -->
<!-- 					<option value="">请选择</option> -->
<!-- 					<c:if test="${!empty houseType}"> -->
<!-- 						<c:forEach var="ht" items="${houseType}"> -->
<!-- 							<option value="${ht.biz_code}">${ht.code_name }</option> -->
<!-- 						</c:forEach> -->
<!-- 					</c:if> -->
<!-- 				</select> -->
<!-- 			</div> -->
<!-- 			<div class="span1"> -->
<!-- 			</div> -->
<!-- 		</div> -->

			<%--<div class="form-actions" style="padding-left: 290px;" >--%>
				<%--<button type="button" class="btn blue" onclick="addHouse()">确 定</button>--%>
					<%--&nbsp;&nbsp;&nbsp;&nbsp;--%>
				<%--<button type="button" class="btn" onclick="closeB()">取 消</button>--%>
			<%--</div>--%>
		<div class="row-fluid">
			<div class="span12">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span5">
				<span_customer2>传感器数</span_customer2>
				&nbsp;&nbsp;
				<select id ="selectSensorNum"  name = "selectSensorNum" style="width: 190px;">
					<option value ="1">1</option>
					<option value ="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7" selected>7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
					<option value="17">17</option>
					<option value="18">18</option>
					<option value="19">19</option>
					<option value="20">20</option>
				</select>
			</div>
			<div class="span5">
				<font color="red">注意：</font>
				室外温度传感器不计数！
			</div>
			<div class="span1">
			</div>
		</div>

		<div class="row-fluid">
			<div class="span12">
			</div>
		</div>
			<div class="row-fluid">
				<div class="span3" align="center">
				</div>
				<div class="span3" align="center">
					<button type="button" class="btn blue" onclick="addHouse()">确 定</button>
				</div>
				<div class="span3" align="center">
					<button type="button" class="btn" onclick="closeB()">取 消</button>
				</div>
				<div class="span3" align="center">
				</div>
			</div>

	</form>
		<!-- END FORM-->  
	</div>
   <script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>

  </body>
</html>
