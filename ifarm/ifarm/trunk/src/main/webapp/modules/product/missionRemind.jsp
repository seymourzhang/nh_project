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
    <meta charset="UTF-8">
    <%@ include file="../../framework/inc.jsp"%>
</head>
<script>
        jQuery(document).ready(function () {
            checkDate($("#wd")[0]);
            setData(${tasks});
            <%--initTableWithToolBar("stock", "taskReminderToolbar", getStockTableColumns(), ${tasks});--%>
        });
</script>
<body style="background-color: #ffffff;">
<div id="page-content" class="clearfix" >

        <%--功能栏--%>
        <div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
<!--             <div style="padding-left: 10px;"> -->
                <div class="row-fluid">
                    <div class="span2" align="left">
                        <span_customer2>任务类别</span_customer2>
                        <select id="taskType" onchange="queryNext();" style="width: 90px;">
                            <c:if test="${!empty task_type}">
                                <c:forEach var="type" items="${task_type}">
                                    <option value="${type.task_type}">${type.code_name}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </div>
                    <div class="span2" align="left">
                        <span_customer2>任务项</span_customer2>
                        <select id="taskCode" style="width: 90px;">
                            <c:if test="${!empty task_code}">
                                <c:forEach var="code" items="${task_code}">
                                    <option value="${code.task_id}">${code.task_name}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </div>
                    <div class="span8" align="left">
                        <select id="wd" onchange="checkDate(this);" style="display: none" disabled="false">
                            <c:if test="${!empty date_type}">
                                <c:forEach var="type" items="${date_type}">
                                    <option value="${type.biz_code}">${type.code_name}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                        <span_customer2>按周龄</span_customer2>
                        <input id="dateValues" style="width: 600px;" placeholder="请填写周龄，多个周龄以逗号隔开，例如：1,3,6,11..." onblur="checkNum(this.value)"/>
                    </div>
                </div>
                <div class="row-fluid" style="height: 40px;">
                    <div class="span4" align="left">
                        <span_customer2>提醒日期</span_customer2>
                        <div id="weeks" style="display: inline-block;vertical-align: middle;">
                            <table>
                                <tr>
                                    <td><input name="week" type="checkbox" value="1"/>周一</td>
                                    <td><input name="week" type="checkbox" value="2"/>周二</td>
                                    <td><input name="week" type="checkbox" value="3"/>周三</td>
                                    <td><input name="week" type="checkbox" value="4"/>周四</td>
                                    <td><input name="week" type="checkbox" value="5"/>周五</td>
                                    <td><input name="week" type="checkbox" value="6"/>周六</td>
                                    <td><input name="week" type="checkbox" value="7"/>周日</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="span8" align="left">
                        <button class="btn blue"  onclick="addMissionRemind();">
                            <i class="icon-plus"></i>&nbsp;确认新增</button>&nbsp;&nbsp;
                        <button id='taskReminderToolbar_btn_delete' type='button' class='btn blue' style="display: inline;" onclick="javascript:deleteTask();">
                        <i class="icon-trash"></i> 删除
                    </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span12">
                <p id = "factFarmTitle" align="center">
                    <font size='4' ><B>${org_name}</B></font>
                </p>
                <div id="stockFrame" align="center" style="margin-top: -20px;">
                <table id="stockTable"></table>
            </div>
            </div>
        </div>

<!--         <div class="row-fluid"> -->
<!--             <div class="span12" align="left"> -->
<!--                 <table id="stockTable"></table> -->
<!--             </div> -->
<!--         </div> -->
</div>
<script type="text/javascript">
    var isRead="${pd.write_read}";//菜单是否只读
</script>
<!-- #main-content -->
<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
<script type="text/javascript" src="<%=path%>/modules/product/js/missionRemind.js"></script>
<!-- 确认窗口 -->
</body>
</html>
