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
	<%@ include file="../../framework/inc.jsp"%>
    <link rel="stylesheet" href="<%=path %>/framework/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/style-metro.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/style.css"/>
    <link rel="stylesheet" href="<%=path %>/framework/css/bootstrap-fileupload.css" />
    <link rel="stylesheet" href="<%=path %>/framework/css/uniform.default.css" />
	<script type="text/javascript" src="<%=path %>/framework/jquery/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>
	<script type="text/javascript">
	
	 $(document).ready(function(){
		 $("input[name='farm_name_chs']").blur(function(){
				var ac=$("input[name='farm_name_chs']").val();
				if(ac==""){
					layer.msg("农场名不能为空！");
				}else if(restFarmName(ac)){
					layer.msg("农场名已经存在！");
				}else{
//					layer.msg("");
				}
			  });
		 
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
						$("#city_id option:gt(0)").remove();
						for (var i = 0; i < list.length; i++) {
							$("#city_id").append("<option value=" + list[i].id + ">" + list[i].short_name+ "</option>");
						}
						$("#city_id").val(list[0].id);
						setAreaId();
					}
				})
// 				layer.msg("");
		});
		$("#city_id").change(function() {
			setAreaId();
		});
         $("#feed_type").change(function() {
             $('#editfarm_msg').html("");
         });
	})
	
	
	
	function setAreaId(){
		$.ajax({
			type : "post",
			url : "<%=path%>/farm/getAreaChina",
			data : {
				"parent_id":$("#city_id").val(),
				"level" : 3
			},
			dataType: "json",
			success : function(result) {
				var list = result.obj;
				$("#area_id option:gt(0)").remove();
				for (var i = 0; i < list.length; i++) {
					$("#area_id").append("<option value=" + list[i].id + ">" + list[i].short_name+ "</option>");
				}
				$("#area_id").val(list[0].id);
			}
		})
	}	
	
function  addFarm(){
    var ft = $("#feed_type").val();
    var orgSelect = $("#orgSelect").val();
	var ac=$("input[name='farm_name_chs']").val();
	var ht=$("select[name='farm_type']").val();
	if(ac==""){
		layer.msg("农场名不能为空！");
	}else if(restFarmName(ac)){
		layer.msg("农场名已经存在！");
	}else{
	    if(ft==""){
	    	layer.msg("养殖品种不能为空！");
		}else if(orgSelect==""){
	    	layer.msg("上级机构不能为空！");
		}else if(ht==""||ht==null){
	         layer.msg("请选择农场类型");
		 		} else{
            var param =$.serializeObject($('#addfarm_form'));
            $.ajax({
                url: "<%=path%>/farm/addFarm",
                data: param,
                type : "POST",
                dataType: "json",
                success: function(result) {
                    if(result.msg=='1'){
                        $("#tab_fag",window.parent.document).val(2);
                        $("#farmViewForm",window.parent.document).submit();
						/* parent.location.reload();  */
                        parent.layer.closeAll();
                    }else{
//                        alert("添加失败！");
                        layer.msg("添加失败！");
                    }
                }
            });
		}
	}
	
}
	
	
//判断用户名是否重名
function restFarmName(str){
	var rs;
	<%--$.ajaxSetup({ async: false });--%>
    <%--$.get("<%=path%>/farm/isFarmNameNull?farmNameChs="+str, function(result){--%>
    	<%--rs = $.parseJSON(result);--%>
   	 <%--if(rs.msg=="1"){--%>
        	<%--rs = true;//存在--%>
        <%--}else{--%>
        	<%--rs = false;//不存在--%>
        <%--}--%>
   <%--});--%>
    rs = false; //不判断农场名称是否存在相同的
   return rs; 
}

function closeB(){
	parent.layer.closeAll();
}
	
	</script>
	
  </head>
  
  <body>
   <div class="portlet-body form">
	<!-- BEGIN FORM-->
    <form id="addfarm_form" class="form-horizontal" >
        &nbsp;
		<div class="row-fluid">
			<div class="span1" >
			</div>
			<div class="span5" >
				<span_customer2>农场名</span_customer2>
				&nbsp;&nbsp;&nbsp;
				<input type="text" style="width: 180px;" name="farm_name_chs">
			</div>
			<div class="span5" >
				<span_customer2>英文名</span_customer2>
				&nbsp;&nbsp;&nbsp;
				<input type="text" style="width: 180px;" name="farm_name_en">
			</div>
<!-- 			<div class="span1" > -->
<!-- 			</div> -->
		</div>
        &nbsp;
		<div class="row-fluid">
			<div class="span1" >
			</div>
			<div class="span5" >
					<%--<label style="margin-left: -17px;">--%>
						<span_customer2>上级机构</span_customer2>
					<%--</label>--%>
						<select id="orgSelect"  name="organizationId" style="margin-left: 59px;margin-top:-30px;width: 180px;">
							<option value="">请选择</option>
							<c:if test="${!empty corporationList}">
								<c:forEach var="cl" items="${corporationList}">
									<option value="${cl.organization_id },${cl.level_id}">${cl.name_cn }</option>
								</c:forEach>
							</c:if>
						</select>
			</div>
			<div class="span5" >
					<span_customer2>农场类型</span_customer2>
				<select id="farm_type" name="farm_type"  style="margin-left: 59px;margin-top:-30px;width: 180px;">
					<option value="">请选择</option>
					<c:if test="${!empty farmType}">
						<c:forEach var="ht" items="${farmType}">
							<option value="${ht.biz_code}">${ht.code_name }</option>
						</c:forEach>
					</c:if>
				</select>
			</div>
			<div class="span1" >
			</div>
		</div>
        &nbsp;
		<div class="row-fluid">
		<div class="span1" >
			</div>
			<div class="span5" >
			        <span_customer2>所在省份</span_customer2>
						<select id="province_id"  name="farm_add1" style="margin-left: 59px;margin-top:-30px;width: 180px;">
							<option value="">请选择</option>
							<c:if test="${!empty prlist}">
								<c:forEach var="prl" items="${prlist}">
									<option value="${prl.id }">${prl.short_name }</option>
								</c:forEach>
							</c:if>
						</select>
			</div>
			<div class="span5" >
				<span_customer2>养殖品种</span_customer2>
					<select id="feed_type" name="feed_type" style="margin-left: 59px;margin-top:-30px;width: 180px;">
						<option value="">请选择</option>
						<c:if test="${!empty feedType}">
							<c:forEach var="ft" items="${feedType}">
								<option value="${ft.biz_code}">${ft.code_name }</option>
							</c:forEach>
						</c:if>
					</select>
			</div>
		</div>
        &nbsp;
		<div class="row-fluid">
		<div class="span1" >
			</div>
			<div class="span5" >
				<span_customer2>所在市</span_customer2>
					<select id="city_id" name="farm_add2" style="margin-left: 59px;margin-top:-30px;width: 180px;">
						<option value="">请选择</option>
					</select>
			</div>
			<div class="span5" >
			        <span_customer2>养殖方式</span_customer2>
					<select id="feed_method" name="feed_method" style="margin-left: 59px;margin-top:-30px;width: 180px;">
						<option value="">请选择</option>
						<c:if test="${!empty feedMethod}">
							<c:forEach var="fm" items="${feedMethod}">
								<option value="${fm.biz_code}">${fm.code_name }</option>
							</c:forEach>
						</c:if>
					</select>
			</div>
			</div>
        &nbsp;
        <div class="row-fluid">
		<div class="span1" >
			</div>
			<div class="span5" >
				<span_customer2>所在区/县</span_customer2>
					<select id="area_id" name="farm_add3" style="margin-left: 59px;margin-top:-30px;width: 180px;">
						<option value="">请选择</option>
					</select>
			</div>
		</div>

            &nbsp;
			<div align="center">
				<button type="button" class="btn blue" onclick="addFarm()"><i class="icon-ok"></i>&nbsp;确 定&nbsp;&nbsp;&nbsp;</button>
					&nbsp;&nbsp;&nbsp;&nbsp;
				<button type="button" class="btn" onclick="closeB()">&nbsp;&nbsp;&nbsp;取 消&nbsp;&nbsp;&nbsp;</button>
			</div>
		</form>
		<!-- END FORM-->  
	</div>
  </body>
</html>
