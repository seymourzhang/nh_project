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
</head>

<body style="background-color: #ffffff;">
<div id="page-content" class="clearfix"  style="padding-top: 10px;">
&nbsp;&nbsp;<span_customer2>system version:</span_customer2> ${pd.version}
    <br>
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

</body>
</html>

