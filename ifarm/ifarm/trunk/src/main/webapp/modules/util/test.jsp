<%--
  Created by IntelliJ IDEA.
  User: raymon
  Date: 11/18/2016
  Time: 10:54
  To change this template use File | Settings | File Templates.
--%>
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

    <!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
    <link rel="stylesheet" href="<%=path%>/modules/breed/css/jquery.fileupload.css">
    <%--<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->--%>
    <script src="<%=path%>/framework/jquery/jquery.ui.widget.js"></script>
    <!-- The basic File Upload plugin -->
    <script src="<%=path%>/framework/jquery/jquery.fileupload.js"></script>
</head>

<body style="background-color: #ffffff;">
<div id="page-content" class="clearfix"  style="padding-top: 10px;">

    <%--<form id="fileUpload" action="excel.do?method=exportMemberExcel" enctype="multipart/form-data" method="post">--%>
        <%--<input id="excelFile" name="file" type="file"/>--%>
        <%--<input type="button" value="提交" onclick="submitExcel()"/>--%>
        <%--<button type="button" class="btn blue fileinput-button" style="text-align: left;vertical-align: middle;" onclick="uploadConfirm();" id="addData">--%>
            <%--<i class="icon-arrow-up">&nbsp;文件导入</i>--%>
        <%--</button>--%>
    <%--</form>--%>

        <button class="btn blue fileinput-button">
            <span><i class="icon-arrow-up">&nbsp;文件导入</i></span>
            <input id="fileupload" type="file" name="eFiles" >
        </button>

    <%--&nbsp;&nbsp;<span_customer2>system version:</span_customer2> ${pd.version}--%>
    <%--<br>--%>
    <%--&nbsp;&nbsp;<span_customer2>system status:</span_customer2> ${pd.status}--%>
    <%--<br>--%>
    <%--&nbsp;&nbsp;<span_customer2>cpt version:</span_customer2> ${pd.version_cpt}--%>
    <%--&nbsp;&nbsp;<span_customer2>system path:</span_customer2> ${pd.projectPath}--%>
    <%--<br>--%>
    <%--<input type="button" value="测试1" onclick="go1('测试1')" />--%>
    <%--<br>--%>
    <%--<input type="button" value="测试2" onclick="go2('测试2')" />--%>
    <%--<br>--%>
    <%--<table id="tNameTable"></table>--%>


</div>
<script type="text/javascript">
    var isRead="${pd.write_read}";//菜单是否只读
</script>
<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
<script type="text/javascript" src="<%=path%>/modules/util/js/info.js"></script>

<script>
    var path = "<%=path%>";
</script>
<script type="text/javascript" src="<%=path%>/modules/util/js/test.js"></script>

</body>
</html>
