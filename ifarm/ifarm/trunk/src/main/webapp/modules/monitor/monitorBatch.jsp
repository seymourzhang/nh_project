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
<script src="<%=path%>/framework/js/bootstrap_table/bootstrap-table.js"></script>
<link href="<%=path%>/framework/js/bootstrap_table/bootstrap-table.css" rel="stylesheet" />
<script src="<%=path%>/framework/js/bootstrap_table/locale/bootstrap-table-zh-CN.js"></script>

<link rel="stylesheet" href="<%=path%>/framework/js/bootstrap_editable/1.5.1/css/bootstrap-editable.css">
<script src="<%=path%>/framework/js/bootstrap_editable/1.5.1/js/bootstrap-editable.js"></script>
<script src="<%=path%>/framework/js/bootstrap_table/extensions/editable/bootstrap-table-editable.js"></script>
<%--<script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>--%>
<script>
    var isRead="${pd.write_read}";//菜单是否只读
    var user_id = "${pd.user_id}";
    var houseType=("${pd.house_type}"!="")?"${pd.house_type}":3;
    var org_id = "${pd.org_id}";
    var farm_id = "${pd.farm_id}";
    var house_id = "${pd.house_id}";
	var pd_farmOrHouse = "${pd.farmOrHouse}";
    var house_type  = "${pd.house_type}";
	var tabId = "${pd.tabId}";
	var bizCode = "${pd.bizCode}";
	var parentId = "${pd.parentId}";
    var asyncFlag = false;
    var activeid = tabId;
	var categoryType1 = "${pd.categoryType1}";
    var categoryType2 = "${pd.categoryType2}";
    var categoryType3 = "${pd.categoryType3}";
</script>
<%--<script type="text/javascript" src="<%=path%>/modules/monitor/js/monitorBatch.js"></script>--%>
	<style>
		.tabUl {
			margin-bottom:0px;
		}
		.tabLi {
			background-color: rgb(191, 191, 191);
			border-right: 1px solid rgb(224, 223, 223);
			text-align: center;
		}
	</style>

</head>
<body style="background-color: #ffffff;">
	<div id="page-content"  class="clearfix">
		<%--<div id="page-content"  class="clearfix" style="padding-top: 10px;">--%>
			<%--<div class="row-fluid">--%>
				<%--<div class="span12">--%>
					<%--<div class="tabbable tabbable-custom boxless" >--%>
			<div class="row-fluid">
				<%--标签菜单栏--%>
					<%--<div class="span12" style="margin-left: 0px;height: 10px">--%>
						<%--<ul class="nav nav-pills row-fluid" style="margin-bottom: 0px; ">--%>
							<%--<li  class="active" id="stateTab" style="text-align: center;width:33.2%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >--%>
								<%--<a href="#tab10" onclick="showRoujiOrZhongji(false);"  data-toggle="tab" id="stateTab1">肉鸡</a>--%>
							<%--</li>--%>
							<%--<li  id="detailTab" style="text-align: center;width:33.2%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >--%>
								<%--<a href="#tab10" onclick="showDanjiOrYucheng(false);"  data-toggle="tab" id="detailTab1">商品代蛋鸡</a>--%>
							<%--</li>--%>
							<%--<li  id="detailTab" style="text-align: center;width:33.2%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >--%>
								<%--<a href="#tab10" onclick="showDanjiOrYucheng(true);"  data-toggle="tab" id="detailTab1">商品代育成</a>--%>
							<%--</li>--%>
						<%--</ul>--%>
					<%--</div>--%>

					<%--tab便签--%>
					<div class="row-fluid">
						<ul class="nav nav-pills tabUl" id = "uiTab">
							<c:if test="${!empty pd.tabList}">
								<c:forEach var="tab" items="${pd.tabList}">
									<li class="tabLi">
										<a href="#tab10" code="${tab.code}" bizCode="${tab.bizCode}" parentId="${tab.parentId}" data-toggle="tab">${tab.name}</a >
									</li>
								</c:forEach>
							</c:if>
						</ul>
					</div>
					<script>
                        //页面初始化后,获取当前页面存在的tab个数,初始化样式,使其两边对其
                        var size = $("ul li").length;
                        var isActive = 0;
                        $("ul li").each(function(index, element){
                            if(!(tabId == null || tabId == "")){
                                var code = $(element).children().attr("code");
                                if(code == tabId){
                                    $(element).addClass("active");
                                }
                            }else{
                                if(index == isActive){
                                    $(element).addClass("active");
                                }
                            }
                            $(element).attr("style","width:" + (99.6/size) + "%");
                        });

                        // //初始化页面内容,对应tab页面按钮控制显示
                        // $(".tab-content .tab-pane").each(function(index, element){
                        //     if(index == isActive){
                        //         $(element).addClass("active");
                        //         $(element).empty();
                        //         var div = document.createElement("div");
                        //         $(div).html("初始化");
                        //         $(element).append(div);
                        //     }
                        // });


					</script>
					<%--主体页面--%>
					<%--<div class="tab-content">--%>
						<%--<c:if test="${!empty pd.tabList}">--%>
							<%--<c:forEach var="tab" items="${pd.tabList}">--%>
								<%--<div class="tab-pane" id="tab_${tab.code}">--%>
									<%--<h1>${tab.name}</h1>--%>
								<%--</div>--%>
							<%--</c:forEach>--%>
						<%--</c:if>--%>
					<%--</div>div--%>



			</div>
			</div>
	       <div class="tab-pane" id="tab10">
			<div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
				<div class="span7" >
				<div style="padding-left: 10px;">

				<button id="btnShowNongChang" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showFarmOrHouse(false);">
					<i class="icon-check" id ="iconShowNongChang1" style="display: inline">按农场</i>
					<i class="icon-check-empty" id ="iconShowNongChang2" style="display: none">按农场</i>
				</button>
				<button id="btnShowDongShe" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showFarmOrHouse(true);">
					<i class="icon-check-empty" id ="iconShowDongShe1" style="display: inline">按栋舍</i>
					<i class="icon-check" id ="iconShowDongShe2" style="display: none">按栋舍</i>
				</button>&nbsp;&nbsp;&nbsp;
					<%@ include file="../../framework/org.jsp"%>
				<%--<button id="btnShowRouJi" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showRoujiOrZhongji(false);">--%>
						<%--<i class="icon-check" id ="iconShowRouJi1" style="display: inline">肉鸡</i>--%>
						<%--<i class="icon-check-empty" id ="iconShowRouJi2" style="display: none">肉鸡</i>--%>
					<%--</button>--%>
<%--<!-- 					<button id="btnShowZhongJi" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showRoujiOrZhongji(true);"> -->--%>
<%--<!-- 						<i class="icon-check-empty" id ="iconShowZhongJi1" style="display: inline">种鸡</i> -->--%>
<%--<!-- 						<i class="icon-check" id ="iconShowZhongJi2" style="display: none">种鸡</i> -->--%>
<%--<!-- 					</button> -->--%>
					<%--<button id="btnShowChanDan" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showDanjiOrYucheng(false);">--%>
						<%--<i class="icon-check" id ="iconShowChanDan1" style="display: none">产蛋</i>--%>
						<%--<i class="icon-check-empty" id ="iconShowChanDan2" style="display: inline">产蛋</i>--%>
					<%--</button>--%>
					<%--<button id="btnShowYuCheng" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showDanjiOrYucheng(true);">--%>
						<%--<i class="icon-check-empty" id ="iconShowYuCheng1" style="display: inline">育成</i>--%>
						<%--<i class="icon-check" id ="iconShowYuCheng2" style="display: none">育成</i>--%>
					<%--</button>--%>
					</div>
				</div>
				<%--<div class="span5">--%>
					<%--<div style="margin-left: -60px;">--%>
							<%--<%@ include file="../../framework/org.jsp"%>--%>
					<%--</div>--%>


				<%--</div>--%>

				<div class="span5" align="right">
					<div id="rouji_category" style="display: none;">
						<button id="btnBaiYu" type="button" class="btn blue" style="text-align: center;vertical-align: middle;display: none;" onclick="showDanjiOrYucheng(false,1);">
							<i class="icon-check" id ="baiYu1" style="display: none">白羽肉鸡</i>
							<i class="icon-check-empty" id ="baiYu2" style="display: inline">白羽肉鸡</i>
						</button>
						<!-- 					<button id="btnShowZhongJi" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showRoujiOrZhongji(true);"> -->
						<!-- 						<i class="icon-check-empty" id ="iconShowZhongJi1" style="display: inline">种鸡</i> -->
						<!-- 						<i class="icon-check" id ="iconShowZhongJi2" style="display: none">种鸡</i> -->
						<!-- 					</button> -->
						<button id="btnHuangYu" type="button" class="btn blue" style="text-align: center;vertical-align: middle;display: none;" onclick="showDanjiOrYucheng(false,2);">
							<i class="icon-check" id ="huangYu1" style="display: none">黄羽肉鸡</i>
							<i class="icon-check-empty" id ="huangYu2" style="display: inline">黄羽肉鸡</i>
						</button>
						<button id="btnRouZa1" type="button" class="btn blue" style="text-align: center;vertical-align: middle;display: none;" onclick="showDanjiOrYucheng(false,3);">
							<i class="icon-check-empty" id ="rouZa1" style="display: inline">肉杂鸡</i>
							<i class="icon-check" id ="rouZa2" style="display: none">肉杂鸡</i>
						</button>
						<button id="btnRouJiAll" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showDanjiOrYucheng(false,'');">
							<i class="icon-check" id ="rouJiAll1" style="display: inline">全部</i>
							<i class="icon-check-empty" id ="rouJiAll2" style="display: none">全部</i>
						</button>
					</div>
                     &nbsp;&nbsp;
					<button id="btnShowEmptyHouse" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showEmptyHouse(false)">
<!-- 						<i class="icon-check" id ="iconShowWithOutEmptyFarm1" style="display: inline">在养农场</i> -->
<!-- 						<i class="icon-check-empty" id ="iconShowEmptyFarm1" style="display: none">在养农场</i> -->
						<i class="icon-check" id ="iconShowWithOutEmptyHouse1" style="display: inline">在养农场</i>
						<i class="icon-check-empty" id ="iconShowEmptyHouse1" style="display: none">在养农场</i>
					</button>
					<button id="btnShowEmptyHouse2" type="button" class="btn blue" style="text-align: center;vertical-align: middle;" onclick="showEmptyHouse(true)">
<!-- 						<i class="icon-check-empty" id ="iconShowWithOutEmptyFarm2" style="display: inline">全部农场</i> -->
<!-- 						<i class="icon-check" id ="iconShowEmptyFarm2" style="display: none">全部农场</i> -->
						<i class="icon-check-empty" id ="iconShowWithOutEmptyHouse2" style="display: inline">全部农场</i>
						<i class="icon-check" id ="iconShowEmptyHouse2" style="display: none">全部农场</i>
					</button>
					<%--&nbsp;--%>
					<%--<%@ include file="../../framework/help/help.jsp"%>--%>
					<%--<div id="helpContext" style="padding-top: 5px;display: none;">--%>
						<%--<table id = "helpTable" class="table">--%>
							<%--<thead>--%>
								<%--<tr>--%>
									<%--<td style="font-weight:bold;text-align: left;">图例</td>--%>
									<%--<td style="font-weight:bold;text-align: left;">说明</td>--%>
								<%--</tr>--%>
							<%--</thead>--%>
							<%--<tbody>--%>
								<%--<tr >--%>
									<%--<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpOrgSelect.png" style="width: 200px;height: 45px;"></td>--%>
									<%--<td style="text-align: left;">通过鼠标点选，可选择分公司或农场</td>--%>
								<%--</tr>--%>
								<%--<tr>--%>
									<%--<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpSort.png" style="width: 200px;height: 45px;"></td>--%>
									<%--<td style="text-align: left;">鼠标点击表头箭头处，可进行顺、倒排序</td>--%>
								<%--</tr>--%>
								<%--<tr>--%>
									<%--<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpStatus.png" style="width: 200px;height: 80px;"></td>--%>
									<%--<td style="text-align: left;">鼠标点击蓝色字体处，可跳转至相应的功能界面</td>--%>
								<%--</tr>--%>
								<%--<tr>--%>
									<%--<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpAlarmYerrow.png" style="width: 200px;height: 45px;"></td>--%>
									<%--<td style="text-align: left;">黄色字体处，表示实际监测值接近低报或高报值的10%以内</td>--%>
								<%--</tr>--%>
								<%--<tr>--%>
									<%--<td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpAlarmRed.png" style="width: 200px;height: 45px;"></td>--%>
									<%--<td style="text-align: left;">红色字体处，表示实际监测值高于高报值或低于低报值</td>--%>
								<%--</tr>--%>
							<%--</tbody>--%>
						<%--</table>--%>
					<%--</div>--%>
				</div>
			</div>




			<%--<form id="farmData" method="post">--%>
				<div class="row-fluid">
					<div class="span12">
							<table class="table table-striped table-bordered table-hover" id="tbodyMonitorCurListTable">

							</table>
					</div>
				</div>
			<%--</form>--%>
		   </div>
	</div>
				<%--</div>--%>
			<%--</div>--%>
	<%--</div>--%>

	<%--<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>--%>
	<%--<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>--%>
<%--<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>--%>

<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
	<script type="text/javascript" src="<%=path%>/modules/monitor/js/monitorBatch.js"></script>
	<!-- 确认窗口 -->
</body>
</html>
