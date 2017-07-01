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
    <link rel="stylesheet" href="<%=path %>/framework/css/zTreeStyle.css" />
    <script type="text/javascript" src="<%=path %>/framework/jquery/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="<%=path %>/framework/jquery/jquery.ztree.excheck.js"></script>
</head>

<body style="background-color: #ffffff;">
<div id="page-content" class="clearfix"  style="padding-top: 10px;">
    <div class="row-fluid">
        <div class="span12">
            <div class="container-fluid">
                <%--<div class="row-fluid">--%>
                    <%--<div class="span4" align="left">--%>
                        <%--<span_customer>组织名称</span_customer>--%>
                        <%--<input id="orgIdOrName" type="text">--%>
                    <%--</div>--%>
                    <%--<div class="span4" align="left">--%>
                        <%--<span_customer>组织层级</span_customer>--%>
                        <%--<select id="orgLevelSelect">--%>
                        <%--</select>--%>
                    <%--</div>--%>
                    <%--<div class="span4" align="left">--%>
                        <%--<a id="orgSearch" href="javascript:;" class="btn green" onclick="searchOrgList();"></i>查询</a>--%>
                    <%--</div>--%>
                <%--</div>--%>

                <%--<div class="row-fluid">--%>
                    <%--<div class="span12">--%>
                        <%--<hr style="height:10px;border:none;border-top:1px solid #555555;" />--%>
                    <%--</div>--%>
                <%--</div>--%>

                <div class="row-fluid">
                    <div class="span3" align="center">
                        <div class="zTreeDemoBackground left" style="border:1px solid #20124d;overflow:auto;">
                            <div align="center">
                                <a id="addOrg" href="javascript:;" class="btn blue" onclick="addOrg();"><i class="icon-plus"></i>&nbsp;&nbsp;&nbsp;新增</a>
                                <a id="editOrg" href="javascript:;" class="btn blue" onclick="editOrg();"><i class="icon-edit">&nbsp;&nbsp;&nbsp;</i>修改</a>
                                <a id="deleteOrg" href="javascript:;" class="btn blue" onclick="deleteOrg();"><i class="icon-trash">&nbsp;&nbsp;&nbsp;</i>删除</a>
                            </div>
                            <p></p>
                            <ul id="orgListTree" class="ztree"></ul>
                        </div>
                    </div>
                    <div class="span9" align="center">
                        <div class="row-fluid">
                            <div class="span8" align="left" id = "labelOrgName">
                                机构名称
                            </div>
                            <div class="span4" align="right">
                                <a id="mappingFarmHouse" href="javascript:;" class="btn blue" style="display: none;" onclick="mappingFarmHouse();"><i class="icon-link">&nbsp;</i>绑定农场</a>
                                <%--<a id="unMappingFarmHouse" href="javascript:;" class="btn blue" onclick="unMappingFarmHouse();"></i>解绑农场</a>--%>
                            </div>

                        </div>
                        <div class="row-fluid">
                            <div class="span12" align="center">
                                <table id="orgListTable"></table>
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
</script>
<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
<script type="text/javascript" src="<%=path%>/modules/util/js/orgManage.js"></script>

</body>
</html>

