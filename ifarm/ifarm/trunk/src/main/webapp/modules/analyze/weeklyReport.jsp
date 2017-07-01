<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

<body style="background-color: #ffffff;">
<div id="page-content" class="clearfix" style="padding-top: 0px;background:#e7e5e5;">
    <div class="row-fluid">
        <div class="span12">
            <input id="toolBarFarmParmUserId" type="hidden" value="${pd.user_id}">
            <input id="toolBarFarmParmPath" type="hidden" value="<%=path%>">
            <div id = "toolBarFarm" class="row-fluid">
                <%--allowtransparency="yes"--%>
            </div>
            <iframe id="iframe_tab_1" name="inStockForm" width="99.8%" height="100%" frameborder="no" allowtransparency="yes" src="">
            </iframe>
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
</script>
<!-- #main-content -->
<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path%>/modules/analyze/js/analyzeReport.js"></script>
<script type="text/javascript" src="<%=path%>/modules/analyze/js/weeklyReport.js"></script>
<!-- 确认窗口 -->

</body>
</html>
