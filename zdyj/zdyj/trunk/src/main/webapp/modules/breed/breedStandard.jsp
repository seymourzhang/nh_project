<%--
  Created by IntelliJ IDEA.
  User: Seymour
  Date: 2016/11/14
  Time: 11:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

    <script src="<%=path%>/framework/js/bootstrap_table/bootstrap-table.js"></script>
    <link href="<%=path%>/framework/js/bootstrap_table/bootstrap-table.css" rel="stylesheet" />
    <script src="<%=path%>/framework/js/bootstrap_table/locale/bootstrap-table-zh-CN.js"></script>

    <link rel="stylesheet" href="<%=path%>/framework/js/bootstrap_editable/1.5.1/css/bootstrap-editable.css">
    <script src="<%=path%>/framework/js/bootstrap_editable/1.5.1/js/bootstrap-editable.js"></script>
    <script src="<%=path%>/framework/js/bootstrap_table/extensions/editable/bootstrap-table-editable.js"></script>
    <script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>

    <script type="text/javascript" src="<%=path%>/modules/breed/js/breedStandard.js"></script>
</head>
<script>
    $(function () {
        <%--getColums();--%>
        <%--function getColums() {--%>
            <%--var dataColumns = [{--%>
                <%--field: "grow_week_age",--%>
                <%--title: "生长<br>周龄",--%>
                <%--width: "5%",--%>
            <%--}, {--%>
                <%--title: "母鸡死淘率%",--%>
            <%--}, {--%>
                <%--field: "female_life",--%>
                <%--title: "母鸡成活率%",--%>
            <%--}, {--%>
                <%--title: "平均体重（克）",--%>
            <%--}, {--%>
                <%--title: "饲料消耗（克/只）",--%>
            <%--}, {--%>
                <%--field: "evenness",--%>
                <%--title: "均匀度%",--%>
            <%--}, {--%>
                <%--title: "母鸡体重范围（克）",--%>
            <%--}];--%>
            <%--return dataColumns;--%>
        <%--}--%>
        <%--initTable("breedSTD1", getColums(), ${standards});--%>
//        BroilerHlh(1);
        init("${pd.house_type}","${pd.goods_type}");
    });

</script>
<body style="background-color: #ffffff;">
    <div id="page-content" class="clearfix">
        <div class="row-fluid">
            <div class="span12">
                <div class="tabbable tabbable-custom boxless">
                    <div class="row-fluid">
                        <%--标签菜单栏--%>
                        <div class="span12" style="margin-left: 0px;height: 10px">
                            <ul class="nav nav-pills" style="margin-bottom: 0px;" id = "uiTab">
                                <li  class="active" style="text-align: center; width:49.92%; background-color: #BFBFBF;border-right: 1px solid #E0DFDF;" >
                                    <a href="#tab_1" data-toggle="tab" onclick="setHouseType(1);">育成</a>
                                </li>
                                <li  style="text-align: center; width:49.92%; background-color: #BFBFBF; border-right: 1px solid #E0DFDF; " >
                                    <a href="#tab_2" data-toggle="tab" onclick="setHouseType(2);">产蛋</a>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <%-- 功能栏 --%>
                    <div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
                        <div class="span12">
                            <div class="container-fluid">
                                <div class="row-fluid" id="jj1">
                                    <div class="span2" align="left">
                                        <span_customer2>品种</span_customer2>
                                        <select id="good_type" tabindex="1"  name="good_type" style="width: 120px" onchange="searchData();">
                                            <c:if test="${!empty goodTypeList}">
                                                <c:forEach var="goodType" items="${goodTypeList}">
                                                    <option value="${goodType.biz_code }">${goodType.code_name}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="tab-content" style="border:none;padding-top: 0px;">
                        <%-- 育成 --%>
                        <div class="tab-pane active" id="tab_1">
                            <%--<table id="breedSTDTable"></table>--%>
                                <div class="row-fluid">
                                    <div class="span12">
                                        <%--<p id = "varietyName1" align="center">--%>
                                            <%--<font size='4' ><B>${varietyName}</B></font>--%>
                                        <%--</p>--%>
                                        <table id="breedSTD1Table">
                                            <thead>
                                                <tr>
                                                    <th rowspan="3" data-valign="middle" data-align="center">生长周龄</th>
                                                    <th colspan="2" data-valign="middle" data-align="center">母鸡死淘率%</th>
                                                    <th rowspan="3" data-field="female_life" data-valign="middle" data-align="center">母鸡成<br>活率%</th>
                                                    <th colspan="2" data-valign="middle" data-align="center">平均体重（克）</th>
                                                    <th colspan="2" data-valign="middle" data-align="center">饲料消耗（克/只/日）</th>
                                                    <th rowspan="3" data-field="chick_hatching_rate" data-valign="middle" data-align="center">均匀度%</th>
                                                    <th colspan="2" rowspan="2" data-valign="middle" data-align="center">母鸡体重范围</th>
                                                </tr>
                                                <tr>
                                                    <th rowspan="2" data-field="female_week_avg_weed_out" data-align="center">每周平均</th>
                                                    <th rowspan="2" data-field="female_week_total_weed_out" data-align="center">累计</th>

                                                    <th rowspan="2" data-field="female_weight" data-align="center">母鸡</th>
                                                    <th rowspan="2" data-field="male_weight" data-align="center">公鸡</th>

                                                    <th rowspan="2" data-field="avg_feed_daliy" data-align="center">日</th>
                                                    <th rowspan="2" data-field="total_feed" data-align="center">累计</th>
                                                </tr>
                                                <tr>
                                                    <th data-field="female_min_std_weight" data-align="center">最小</th>
                                                    <th data-field="female_max_std_weight" data-align="center">最大</th>
                                                </tr>
                                            </thead>
                                        </table>
                                    </div>
                                </div>
                        </div>

                        <%-- 产蛋 --%>
                        <div class="tab-pane" id="tab_2">
                            <%--<table id="breedSTDTable"></table>--%>
                                <div class="row-fluid">
                                    <div class="span12">
                                        <table id="breedSTD2Table">
                                            <thead>
                                            <tr>
                                                <th rowspan="2" data-valign="middle" data-align="center">生长周龄</th>
                                                <th colspan="2" data-valign="middle" data-align="center">母鸡死淘率%</th>
                                                <th rowspan="2" data-field="female_life" data-valign="middle" data-align="center">母鸡成活率%</th>
                                                <th colspan="2" data-valign="middle" data-align="center">平均体重（克）</th>
                                                <th colspan="2" data-valign="middle" data-align="center">产蛋率%</th>
                                                <th colspan="2" data-valign="middle" data-align="center">每只入舍母鸡产蛋数（枚）</th>
                                                <th rowspan="2" data-field="qualified_egg_rate" data-valign="middle" data-align="center">合格种蛋率%</th>
                                                <th colspan="2" data-valign="middle" data-align="center">每只入舍母鸡产合格种蛋数（枚）</th>
                                                <th rowspan="2" data-field="chick_hatching_rate" data-valign="middle" data-align="center">雏鸡孵化率%</th>
                                                <th rowspan="2" data-field="breeding_chick_hatching" data-valign="middle" data-align="center">种雏孵化率%</th>
                                                <th colspan="2" data-valign="middle" data-align="center">种雏数（只）</th>
                                                <th colspan="2" data-valign="middle" data-align="center">饲料消耗（克/只/日）</th>
                                            </tr>
                                            <tr>
                                                <th data-field="female_week_avg_weed_out" data-align="center">每周平均</th>
                                                <th data-field="female_week_total_weed_out" data-align="center">累计</th>

                                                <th data-field="female_weight" data-align="center">母鸡</th>
                                                <th data-field="male_weight" data-align="center">公鸡</th>

                                                <th data-field="cl_laying_rate" data-align="center">存栏鸡</th>
                                                <th data-field="rs_laying_rate" data-align="center">入舍鸡</th>

                                                <th data-field="rs_female_laying_avg_count" data-align="center">每周平均</th>
                                                <th data-field="rs_female_laying_total_count" data-align="center">累计</th>

                                                <th data-field="rs_female_avg_qualified_count" data-align="center">每周平均</th>
                                                <th data-field="rs_female_total_qualified_count" data-align="center">累计</th>

                                                <th data-field="breeding_chick_avg_count" data-align="center">每周平均</th>
                                                <th data-field="breeding_chick_total_count" data-align="center">累计</th>

                                                <th data-field="egg_avg_feed_daliy" data-align="center">日</th>
                                                <th data-field="egg_total_feed" data-align="center">累计</th>
                                            </tr>
                                            </thead>
                                        </table>
                                    </div>
                                </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
