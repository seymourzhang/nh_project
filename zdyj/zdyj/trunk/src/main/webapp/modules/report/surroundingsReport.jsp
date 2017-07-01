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

</head>

<body style="background-color: #ffffff;margin:0px;" >
<div id="page-content" class="clearfix" style="background:#e7e5e5;">
    <div class="row-fluid">
        <div class="span12">
            <div class="tabbable tabbable-custom boxless">
                <div class="row-fluid">
                    <%--标签菜单栏--%>
                    <div class="span12" style="margin-left: 0px;height: 10px">
                        <ul class="nav nav-pills row-fluid" style="margin-bottom: 0px; " id = "uiTab">
                            <li  class="${pd.tabs[0]} "  style="text-align: center;width:20%;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >
                                <a href="#tab_1" data-toggle="tab">温湿度</a>
                            </li>
                            <li class="${pd.tabs[1]} " style="text-align: center;width:20%;background-color: #BFBFBF; border-right: 1px solid #E0DFDF;" >
                                <a href="#tab_2"   data-toggle="tab" >点温差</a>
                            </li>
                            <li class="${pd.tabs[2]} " style="text-align: center;width:19.58%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;" >
                                <a href="#tab_3"  data-toggle="tab">二氧化碳</a>
                            </li>
                            <li class="${pd.tabs[3]} " style="text-align: center;width:20%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;" >
                                <a href="#tab_4"  data-toggle="tab">光照</a>
                            </li>
                            <li class="${pd.tabs[4]} " style="text-align: center;width:20%;background-color: #BFBFBF;  border-right: 1px solid #E0DFDF;" >
                                <a href="#tab_5"  data-toggle="tab">报警</a>
                            </li>
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
                            <div class="tab-pane ${pd.tabs[0]}" id="tab_1">
                                <%--<iframe id="inStockForm" name="inStockForm" width="100%" height="700" frameborder="no" allowtransparency="yes" src="<%=urlPre%>/inStockForm.cpt<%=urlParamUserId%>${pd.user_id} ">--%>
                                <iframe id="iframe_tab_1" name="tempHumidity" width="99.8%" height="100%" frameborder="no" src="">
                                </iframe>
                            </div>
                            <div class="tab-pane ${pd.tabs[1]}" id="tab_2">
                                <iframe id="iframe_tab_2" name="tempDiff" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>
                            <div class="tab-pane ${pd.tabs[2]}" id="tab_3">
                                <iframe id="iframe_tab_3" name="carbon" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>
                            <div class="tab-pane ${pd.tabs[3]}" id="tab_4">
                                <iframe id="iframe_tab_4" name="light" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
                                </iframe>
                            </div>
                            <div class="tab-pane ${pd.tabs[4]}" id="tab_5">
                                <iframe id="iframe_tab_5" name="alarm" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
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

</script>
<!-- #main-content -->
<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path%>/modules/analyze/js/analyzeReport.js"></script>
<script type="text/javascript" src="<%=path%>/modules/report/js/surroundingsReport.js"></script>
<!-- 确认窗口 -->

</body>
</html>
