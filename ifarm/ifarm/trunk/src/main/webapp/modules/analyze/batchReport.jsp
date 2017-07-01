<%--
  Created by IntelliJ IDEA.
  User: LeLe
  Date: 1/20/2017
  Time: 16:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
//	String urlPre = "../../../fr/ReportServer?reportlet=" + path.replace("/","") ;
//	String urlParamUserId = "?user_id=";
%>

<!DOCTYPE html>
<head>
    <meta charset="utf-8" />
    <%@ include file="../../framework/inc.jsp"%>
    <style>.发雏查询{}</style>
</head>

<body style="background-color: #ffffff;margin:0px;" >
<div id="page-content" class="clearfix" style="background:#e7e5e5;">
    <div class="row-fluid">
        <div class="span12">
            <div class="tabbable tabbable-custom boxless">
                <div class="row-fluid">
                    <%--标签菜单栏--%>
                    <div class="span12" id = "tab" style="margin-left: 0px;height: 10px;display: none;">
                        <ul class="nav nav-pills row-fluid" style="margin-bottom: 0px; " id = "uiTab">
                            <li  class="${pd.tabs[0]} " id="li0"  style="text-align: center;width:20%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tab_1" data-toggle="tab">父母代育成</a>
                            </li>
                            <li class="${pd.tabs[1]} " id="li1" style="text-align: center;width:19.9%;background-color: #BFBFBF; border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tab_2"   data-toggle="tab" >父母代产蛋</a>
                            </li>
                            <li class="${pd.tabs[2]} " id="li2" style="text-align: center;width:19.9%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tab_3"  data-toggle="tab">商品代育成</a>
                            </li>
                            <li class="${pd.tabs[3]} " id="li3" style="text-align: center;width:20%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tab_4"  data-toggle="tab">商品代产蛋</a>
                            </li>
                            <li class="${pd.tabs[4]} " id="li4" style="text-align: center;width:20%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tab_5"  data-toggle="tab">白羽肉鸡</a>
                            </li>
                            <li class="${pd.tabs[5]} "id="li5" style="text-align: center;width:19.9%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tab_6"  data-toggle="tab">黄羽肉鸡</a>
                            </li>
                            <li class="${pd.tabs[6]} " id="li6" style="text-align: center;width:20%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tab_7"  data-toggle="tab">肉杂鸡</a>
                            </li>
                            <li class="${pd.tabs[7]} " id="li7" style="text-align: center;width:20%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tab_8"  data-toggle="tab">种鸡育成</a>
                            </li>
                            <li class="${pd.tabs[8]} " id="li8" style="text-align: center;width:20%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tab_9"  data-toggle="tab">种鸡产蛋</a>
                            </li>
                            <li class="${pd.tabs[9]} " id="li9" style="text-align: center;width:20%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tab_10"  data-toggle="tab">肉鸡饲养</a>
                            </li>

                            <%--<li class="${pd.tabs[4]} " style="text-align: center;width:20%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;" >--%>
                            <%--<a href="#tab_5"  data-toggle="tab">报警</a>--%>
                            <%--</li>--%>
                        </ul>
                    </div>
                </div>

                <input id="toolBarFarmParmUserId" type="hidden" value="${pd.user_id}">
                <input id="toolBarFarmParmPath" type="hidden" value="<%=path%>">
                <%--<div id = "toolBarFarm" class="row-fluid" style="padding-top: 5px; ">--%>
                <%--</div>--%>
                <div class="row-fluid" style="padding-top: 5px; ">
                    <div class="span11"  style="padding-top: 5px; ">
                        <div id = "toolBarFarm">
                        </div>
                    </div>
                    <div class="span1" align="right"  style="padding-top: 5px; ">
                        <%@ include file="../../framework/help/help.jsp"%>
                        <div id="helpContext" style="display: none;">
                            <table id = "helpTable" class="table">
                                <thead>
                                <tr>
                                    <td style="font-weight:bold;text-align: left;">图例</td>
                                    <td style="font-weight:bold;text-align: left;">说明</td>
                                </tr>
                                </thead>
                                <tbody>
                                <tr >
                                    <td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpReportSearchByDate.png" style="width: 200px;height: 45px;"></td>
                                    <td style="text-align: left;">通过鼠标点选，可选择需要查询趋势图是当天、前7天还是前30天的</td>
                                </tr>
                                <tr>
                                    <td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpReportCompare.png" style="width: 200px;height: 45px;"></td>
                                    <td style="text-align: left;">鼠标点击对比按钮，可切换至对比图，在对比图中最多可以对比2个栋舍和批次的某1个监测指标</td>
                                </tr>
                                <tr>
                                    <td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpReportBeLarger.png" style="width: 200px;height: 80px;"></td>
                                    <td style="text-align: left;">鼠标点击图表中的绿色展开按钮可将图表放大，点击黄色还原按钮可将图表还原</td>
                                </tr>
                                <tr>
                                    <td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpReportDesc.png" style="width: 200px;height: 45px;"></td>
                                    <td style="text-align: left;">鼠标点击图表中的图例文字，可显示或隐藏相应的图线</td>
                                </tr>
                                <tr>
                                    <td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpReportCharts.png" style="width: 200px;height: 180px;"></td>
                                    <td style="text-align: left;">
                                        1）鼠标移动至图表中图线区域，可自动显示相应的信息<br>
                                        2）在图表中图线区域，按住鼠标左键向右拖动，可显示某一段时间的图表<br>
                                        3）鼠标点击图表中图线上的点，可显示至更明细时间对应的图表（天->半小时、半小时->分钟）
                                    </td>
                                </tr>
                                <tr>
                                    <td style="text-align: left;"><img src="<%=path%>/framework/help/image/helpReportLightColor.png" style="width: 200px;height: 45px;"></td>
                                    <td style="text-align: left;">光照图表中，不同颜色的柱形表示不同的光照强度范围</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>


                <%--内容栏--%>
                <div class="row-fluid" style="background:#e7e5e5;">
                    <div class="span12">
                        <div class="tab-content" style="border:none;">
                            <div class="tab-pane ${pd.tabs[0]}" id="tab_1" style="display: none;">
                                <%--<iframe id="inStockForm" name="inStockForm" width="100%" height="700" frameborder="no" allowtransparency="yes" src="<%=urlPre%>/inStockForm.cpt<%=urlParamUserId%>${pd.user_id} ">--%>
                                <iframe id="iframe_tab_1" name="iframe_tab_1" width="99.8%" height="100%" frameborder="no" src="">
                                </iframe>
                            </div>
                            <div class="tab-pane ${pd.tabs[1]}" id="tab_2" style="display: none;">
                                <iframe id="iframe_tab_2" name="iframe_tab_2" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>
                            <div class="tab-pane ${pd.tabs[2]}" id="tab_3" style="display: none;">
                                <iframe id="iframe_tab_3" name="iframe_tab_3" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>
                            <div class="tab-pane ${pd.tabs[3]}" id="tab_4" style="display: none;">
                                <iframe id="iframe_tab_4" name="iframe_tab_4" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>
                            <%--<div class="tab-pane ${pd.tabs[4]}" id="tab_5">--%>
                            <%--<iframe id="iframe_tab_5" name="alarm" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">--%>
                            <%--</iframe>--%>
                            <%--</div>--%>
                            <div class="tab-pane ${pd.tabs[4]}" id="tab_5" style="display: none;">
                                <iframe id="iframe_tab_5" name="iframe_tab_5" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>

                            <div class="tab-pane ${pd.tabs[5]}" id="tab_6" style="display: none;">
                                <iframe id="iframe_tab_6" name="iframe_tab_6" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>
                            <div class="tab-pane ${pd.tabs[6]}" id="tab_7" style="display: none;">
                                <iframe id="iframe_tab_7" name="iframe_tab_7" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>
                            <div class="tab-pane ${pd.tabs[7]}" id="tab_8" style="display: none;">
                                <iframe id="iframe_tab_8" name="iframe_tab_8" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>
                            <div class="tab-pane ${pd.tabs[8]}" id="tab_9" style="display: none;">
                                <iframe id="iframe_tab_9" name="iframe_tab_9" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>
                            <div class="tab-pane ${pd.tabs[9]}" id="tab_10" style="display: none;">
                                <iframe id="iframe_tab_10" name="iframe_tab_10" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

        </div>

    </div>
</div>
<script type="text/javascript">
    var isRead="${pd.write_read}";//菜单是否只读
    var org_id = "${pd.org_id}";
    var farm_id = "${pd.farm_id}";
    var house_id = "${pd.house_id}";
    var batch_no = "${pd.batch_no}";
    var report_ip = "${pd.report_ip}";
    var report_port = "${pd.report_port}";
    var tabId = "${pd.tabId}";
    var searchDateTime=null;
    var tabListX = "${FeedType}";
    var tabListV = "${FeedTypeValue}";
    var reportTarIndex = "${reportTarIndex}";
</script>
<!-- #main-content -->
<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path%>/modules/analyze/js/analyzeReport.js"></script>
<script type="text/javascript" src="<%=path%>/modules/analyze/js/batchReport.js"></script>
<!-- 确认窗口 -->

</body>
</html>





<%--<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>
<%--<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>--%>
<%--<%--%>
    <%--String path = request.getContextPath();--%>
    <%--String basePath = request.getScheme() + "://"--%>
            <%--+ request.getServerName() + ":" + request.getServerPort()--%>
            <%--+ path + "/";--%>
<%--//	String urlPre = "../../../fr/ReportServer?reportlet=" + path.replace("/","") ;--%>
<%--//	String urlParamUserId = "?user_id=";--%>
<%--%>--%>

<%--<!DOCTYPE html>--%>
<%--<head>--%>
    <%--<meta charset="utf-8" />--%>
    <%--<%@ include file="../../framework/inc.jsp"%>--%>

<%--</head>--%>

<%--<body style="background-color: #ffffff;">--%>
<%--<div id="page-content" class="clearfix" style="padding-top: 0px;background:#e7e5e5;">--%>
    <%--<div class="row-fluid">--%>
        <%--<div class="span12">--%>
            <%--<input id="toolBarFarmParmUserId" type="hidden" value="${pd.user_id}">--%>
            <%--<input id="toolBarFarmParmPath" type="hidden" value="<%=path%>">--%>
            <%--<div id = "toolBarFarm" class="row-fluid">--%>
                <%--&lt;%&ndash;allowtransparency="yes"&ndash;%&gt;--%>
            <%--</div>--%>
            <%--<iframe id="iframe_tab_1" name="inStockForm" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">--%>
            <%--</iframe>--%>
        <%--</div>--%>

    <%--</div>--%>
<%--</div>--%>
<%--<script type="text/javascript">--%>
    <%--var isRead="${pd.write_read}";//菜单是否只读--%>
    <%--var org_id = "${pd.org_id}";--%>
    <%--var farm_id = "${pd.farm_id}";--%>
    <%--var house_id = "${pd.house_id}";--%>
    <%--var batch_no = "${pd.batch_no}";--%>
    <%--var report_ip = "${pd.report_ip}";--%>
    <%--var report_port = "${pd.report_port}";--%>
<%--</script>--%>
<%--<!-- #main-content -->--%>
<%--<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>--%>
<%--<script type="text/javascript" src="<%=path%>/modules/analyze/js/analyzeReport.js"></script>--%>
<%--<script type="text/javascript" src="<%=path%>/modules/analyze/js/batchReport.js"></script>--%>
<%--<!-- 确认窗口 -->--%>

<%--</body>--%>
<%--</html>--%>
