<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Seymour
  Date: 2016/9/2
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <%@ include file="../../framework/inc.jsp"%>
    <script type="text/javascript" src="<%=path%>/framework/js/extJquery.js"></script>
    <script type="text/javascript" src="<%=path%>/modules/monitor/js/monitor.js"></script>
    <title></title>
    <!--图标样式-->
    <link rel="stylesheet" href="<%=path %>/framework/css/zTreeStyle.css" />

    <!--主要样式-->
    <script type="text/javascript" src="<%=path %>/framework/jquery/jquery.ztree.core.js"></script>
	<script type="text/javascript" src="<%=path %>/framework/jquery/jquery.ztree.excheck.js"></script>
</head>
<script>
$(function () {
    changeOrg();
});

function changeOrg() {
	$.ajax({
		type: "post",
		url: "<%=path%>/monitor/getOrgBySetted",
		data: {},
		dataType: "json",
		success: function (result) {
			var setting = {
				check: {
					enable: true,
					chkDisabledInherit:true
				},
				data: {
					simpleData: {
						enable: true
					}
				}
			};
			var zNodes = result.obj;
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		}
	})
}

function  addMonitorSet() {
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    var nodes = treeObj.getCheckedNodes(true);
    var v = new Array();
    for (var i = 0; i < nodes.length; i++) {
        v.push(nodes[i].id);
    }

//    var param = $.serializeObject($('#adduser_form'));
    $.ajax({
        url: "<%=path%>/monitor/saveSet",
        data: {
            "groupId" : v.toString()
        },
        type: "POST",
        dataType: "json",
        success: function (result) {
            if (result.msg == '1') {
                parent.layer.closeAll();
                parent.location.reload();
                var enableMonitorSet = parent.document.getElementById("enableMonitorSet");
                if (enableMonitorSet.checked == true){
                    reflushMonitor();
                }
            } else {
                alert("添加失败！");
            }
        }
    });
}

function closeB(){
    parent.layer.closeAll();
}
</script>
<body>
	<%--引用跳转页面方法:siMenu()
	<script type="text/javascript" src="/zljk/framework/js/head.js"></script>
	引用结束--%>
	<!--  <div class="container-fluid" id="main-container" style="background-color: #ffffff;"> -->
    <div class="portlet-body form" style="padding-top: 0px;margin-left: 0px;">
        <form id="adduser_form" class="form-horizontal" style="height: 100%; width: 100%;" >
                    <%-- <input type="hidden" name="id" value="${pd.id}">
                            <input type="hidden" name="pid" value="${pd.pid}"> --%>
                        <!-- BEGIN PORTLET-->
        <div style="float: left;padding-left: 0px;">
            <%--<div class="control-group" >--%>
            <div class="zTreeDemoBackground left" style="border:1px solid #FFFFFF; height: 355px; width: 448px; overflow:auto; margin-left: 0px;">
                <ul id="treeDemo" class="ztree"></ul>
            </div>
            <%--</div>--%>
            <div style="padding-left: 0px;float:left;width: 100%; text-align: center" >
                <button type="button" class="btn blue" onclick="addMonitorSet()"><i class="icon-ok"></i>&nbsp;确 定&nbsp;&nbsp;&nbsp;</button>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn" onclick="closeB()">&nbsp;&nbsp;&nbsp;取 消&nbsp;&nbsp;&nbsp;</button>
            </div>
        </div>
        </form>
    </div>
</body>
</html>
