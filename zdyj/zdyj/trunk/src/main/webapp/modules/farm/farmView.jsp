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
<body style="background-color: #ffffff;">
	<div id="page-content" class="clearfix" style="padding-top: 10px;">
		<div class="row-fluid">
			<div class="span12">
				<form id="farmViewForm">
				<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;margin-top: -15px;">
				<div style="margin-left: 10px;">
									<div class="span3" align="left">
									    <span_customer2>农场</span_customer2>
										<input type="text" value="" placeholder="请输入农场名称或编号" name="farm_name_chs" id="farm_name_chs">
									</div>
									<div class="span3" align="left">
									    <span_customer2>养殖品种</span_customer2>
										<select id="feed_type" name="feed_type">
                                        </select>
									</div>
									<div class="span3" align="left">
									    <span_customer2>养殖方式</span_customer2>
										<select id="feed_method" name="feed_method">
                                        </select>
									</div>
									<div class="span3" align="left">
										<a href="javascript:search();" class="btn blue"><i class="icon-search"></i> 查询</a>
									</div>
									</div>
								</div>
						
				<div class="row-fluid" style="padding-top: 5px;">
					<div class="span12">
						<a href="javascript:;" class="btn blue" onclick="addFarm();"><i class="icon-plus"></i> 新增</a>
					</div>
				</div>

<!-- 				<p></p> -->
                    <div class="row-fluid" >
								<div class="span12">
									<div id="farmFrame" align="left">
										<div>
											<table id="farmTable"></table>
										</div>
									</div>
								</div>
							</div>
<!-- 				<div class="row-fluid"> -->
<!-- 					<div class="span12" align="center"> -->
<!-- 						<table class="table table-striped table-bordered table-hover" id="sample_1"> -->

<!-- 							<thead> -->

<!-- 							<tr style="background-color: #1288C0; color: white;" > -->
<!-- 								<th class="hidden-480" style="width: 5% ;text-align: center;">编号</th> -->
<!-- 								<th style="width: 15% ;text-align: center;">农场名称</th> -->
<!-- 								<th style="width: 45% ;text-align: center;">地区</th> -->
<!-- 								<th style="width: 10% ;text-align: center;">养殖品种</th> -->
<!-- 								<th style="width: 10% ;text-align: center;">养殖方式</th> -->
<!-- 								<th style="width: 15% ;text-align: center;">操作</th> -->
<!-- 							</tr> -->

<!-- 							</thead> -->
<!-- 							<tbody> -->
<!-- 							<c:if test="${!empty SDFarmList}"> -->
<!-- 								<c:forEach var="fl" items="${SDFarmList}" varStatus="vs"> -->
<!-- 									<tr class="odd gradeX"> -->
<!-- 										<td class="hidden-480" style="text-align: center;">${fl.id}</td> -->
<!-- 										<td>${fl.farm_name_chs}</td> -->
<!-- 										<td> -->
<!-- 											<c:if test="${fl.farm_add1!='' && fl.farm_add1!=null}"> -->
<!-- 												${fl.province} -->
<!-- 											</c:if> -->
<!-- 											<c:if test="${fl.farm_add2!='' && fl.farm_add2!=null}"> -->
<!-- 												&nbsp;&nbsp;>>&nbsp;&nbsp; -->
<!-- 												${fl.city} -->
<!-- 											</c:if> -->
<!-- 											<c:if test="${fl.farm_add3!='' && fl.farm_add2!=null}"> -->
<!-- 												&nbsp;&nbsp;>>&nbsp;&nbsp; -->
<!-- 												${fl.area} -->
<!-- 											</c:if> -->
<!-- 										</td> -->
<!-- 										<td>${fl.code_name1}</td> -->
<!-- 										<td>${fl.code_name}</td> -->
<!-- 										<td class="center hidden-480" style="width: 145px;"><a href="javascript:void(0);" onclick="editFarm(${fl.id})" class="btn mini blue"><i class="icon-edit"></i> 修改</a> &nbsp;&nbsp;&nbsp; <a href="javascript:void(0);" onclick="delFarm(${fl.id})" class="btn mini black"><i class="icon-trash"></i> 删除</a></td> -->
<!-- 									</tr> -->
<!-- 								</c:forEach> -->
<!-- 							</c:if> -->
<!-- 							</tbody> -->
<!-- 						</table> -->
<!-- 					</div> -->
<!-- 				</div> -->
				</form>
			</div>
			</div>

		</div>
	</div>
	<!-- #main-content -->
	<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>
    <script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
	<!-- 确认窗口 -->
	<script type="text/javascript">
		var isRead="${pd.write_read}";//菜单是否只读
	
		jQuery(document).ready(function() {
			App.init(); // initlayout and core plugins
			 var farm_tab="${pd.fag}";
			 if(farm_tab==3){
				$("#farmTab").removeClass("active"); 
				$("#batchTab").removeClass("active"); 
				$("#houseTab").addClass("active"); 
				$("#tab_1").removeClass("active"); 
				$("#tab_2").removeClass("active"); 
				$("#tab_3").addClass("active"); 
			}
			 if(farm_tab==2){
					$("#batchTab").removeClass("active"); 
					$("#houseTab").removeClass("active"); 
					$("#farmTab").addClass("active"); 
					$("#tab_1").removeClass("active"); 
					$("#tab_3").removeClass("active"); 
					$("#tab_2").addClass("active"); 
				} 
			 feedType();
			 initTable("farm", getFarmTableColumns(), {});
		});
		//新增
		function addFarm(){
			if(isRead!=2){
                layer.msg("无权限，请联系管理员！", {});
				return;
			}
				layer.open({
					type: 2, 
					title: "新增农场",
					skin: 'layui-layer-lan',
					area: ['680px', '355px'],
				    content: '<%=path%>/farm/addFarmUrl'
			    });
		}
		//编辑
		function editFarm(id){
			if(isRead==0){
                layer.msg("无权限，请联系管理员！", {});
				return;
			}
			layer.open({
				type: 2, 
				title: "修改农场",
				skin: 'layui-layer-lan',
				area: ['680px', '355px'],
			    content: "<%=path%>/farm/editFarmUrl?id=" + id
			});
		}
		//删除
		function delFarm(id) {
			if(isRead!=2){
                layer.msg("无权限，请联系管理员！", {});
				return;
			}
			//询问框
			layer.confirm('确定要删除该农场吗？', {
                skin: 'layui-layer-lan'
                , closeBtn: 0
				, shift: 4 //动画类型
			    , btn : [ '确定', '取消' ]
			//按钮
			}, function() {
				$.ajax({
					url : "<%=path%>/farm/delFarm",
					data : {
						id : id
					},
					type : "POST",
					success : function(result) {
						result = $.parseJSON(result);
						if (result.success) {
							layer.msg(result.msg, function(index) {
							    $("#tab_fag").val(2);
								$("#farmViewForm").submit();
                                location.reload();
							});
						} else {
                            layer.msg("删除农场失败！(" + result.msg + ")", {});
						}
					}
				});
			});
		}
		
		
		
		//新增
		function addBatch(){
			if(isRead==0){
                layer.msg("无权限，请联系管理员！", {});
				return;
			}
			layer.open({
				type: 2, 
				title: "新增入拦批次",
				skin: 'layui-layer-lan',
				area: ['480px', '480px'],
			    content: '<%=path%>/farm/addBatchUrl'
		    });
		}
		//新增
		function editBatch(id){
			if(isRead==0){
                layer.msg("无权限，请联系管理员！", {});
				return;
			}
			layer.open({
				type: 2, 
				title: "修改批次",
				skin: 'layui-layer-lan',
				area: ['480px', '480px'],
			    content: "<%=path%>/farm/editBatchUrl?id=" + id
		    });
		}
		//出栏
		function laiBatch(id){
			if(isRead==0){
                layer.msg("无权限，请联系管理员！", {});
				return;
			}
			layer.open({
				type: 2, 
				title: "批次出栏",
				skin: 'layui-layer-lan',
				area: ['480px', '480px'],
			    content: "<%=path%>/farm/laiBatchUrl?id=" + id
		    });
		}
		//新增栋舍
		function addHouse(){
			if(isRead==0){
                layer.msg("无权限，请联系管理员！", {});
				return;
			}
			layer.open({
				type: 2, 
				title: "新增栋舍",
				skin: 'layui-layer-lan',
				area: ['670px', '430px'],
			    content: '<%=path%>/farm/addHouseUrl'
		    });
		}
		//编辑栋舍
		function editHouse(id,deviceID){
			if(isRead==0){
                layer.msg("无权限，请联系管理员！", {});
				return;
			}
			layer.open({
				type: 2, 
				title: "修改",
				skin: 'layui-layer-lan',
				area: ['670px', '430px'],
			    content: "<%=path%>/farm/editHouseUrl?id=" + id+"&deviceID="+deviceID
			});
		}
		
		//删除栋舍
		function delHouse(id) {
			if(isRead==0){
                layer.msg("无权限，请联系管理员！", {});
				return;
			}
			//询问框
			layer.confirm('你确定要删除此记录吗？', {
				btn : [ '确定', '取消' ]
			//按钮
			}, function() {
				$.ajax({
					url : "<%=path%>/farm/delHouse",
					data : {
						id : id
					},
					type : "POST",
					success : function(result) {
						result = $.parseJSON(result);
						if (result.success) {
                            layer.msg(result.msg, {});
                            $("#tab_fag").val(3);
                            $("#farmViewForm").submit();
						} else {
                            layer.msg(result.msg, {});
						}
					}
				});
			});
		}
		
		function feedType(){
			$.ajax({
				url : path + "/farm/getFeedTypeList",
				data : {},
				type : "POST",
				dataType : "json",
				success : function(result) {
					var list = result.obj;
					$("#feed_type option").remove();
					$("#feed_type").append("<option value=''>全部</option>");
					for (var i = 0; i < list.length; i++) {
						$("#feed_type").append("<option value=" + list[i].biz_code + ">" + list[i].code_name + "</option>");
					}
					feedMethod();
				}
			});
		}
		
		function feedMethod(){
			$.ajax({
				url : path + "/farm/getFeedMethodList",
				data : {},
				type : "POST",
				dataType : "json",
				success : function(result) {
					var list = result.obj;
					$("#feed_method option").remove();
					$("#feed_method").append("<option value=''>全部</option>");
					for (var i = 0; i < list.length; i++) {
						$("#feed_method").append("<option value=" + list[i].biz_code + ">" + list[i].code_name + "</option>");
					}
					search();
				}
			});
		}
		
		function search(){
			var param = {farm_name_chs:$("#farm_name_chs").val(),feed_type:$("#feed_type").val(),feed_method:$("#feed_method").val()};
        	$.ajax({
        		url : path + "/farm/searchFarm",
        		data : param,
        		type : "POST",
        		dataType : "json",
        		success : function(result) {
        			var list = result.obj;
        			var dataJosn = $.parseJSON(JSON.stringify(list));
        			$("#farmTable").bootstrapTable("load",dataJosn);
        		}
        	});
		}
		
		function getFarmTableColumns(){
        	var dataColumns = [
            {
                field: "id",
                title: "编号",
                width: "5%"
            },{
            	field: "farm_name_chs",
                title: "农场名称",
                width: "20%"
            },{
            	field: "diqu",
                title: "地区",
                width: "40%",
                formatter: function(value,row,index){
                    var str='';
                	if(row.farm_add1 !='' && row.farm_add1 !=null){
                		str = str+row.province;
                	}
                	if(row.farm_add2 !='' && row.farm_add2 !=null){
                		str = str+'  >>  '+row.city;
                	}
                	if(row.farm_add3 !='' && row.farm_add3 !=null){
                		str = str+'  >>  '+row.area;
                	}
                	return str;
        			
                }
            },{
            	field: "code_name1",
                title: "养殖品种",
                width: "10%"
            },{
            	field: "code_name",
                title: "养殖方式",
                width: "10%"
            },{
            	field: "opr",
                title: "操作",
                width: "20%",
        		formatter: function(value,row,index){
                    return "&nbsp;<button id='factToolbar_btn_update' type='button' class='btn blue' style='display: inline;' onclick='editFarm("+row.id+")' >"+
        			"<i class='icon-edit'></i>修改</button>"+
                    "&nbsp;<button id='factToolbar_btn_delete' type='button' class='btn blue' style='display: inline;' onclick='delFarm("+row.id+")'>"+
        			"<i class='icon-trash'></i>删除</button>";
        			
                }
            }];
        	return dataColumns;
        };
		
	</script>
</body>
</html>
