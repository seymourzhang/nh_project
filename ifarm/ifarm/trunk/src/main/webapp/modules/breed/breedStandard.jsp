<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <%@ include file="../../framework/inc.jsp"%>

        <link href="<%=path%>/framework/js/bootstrap_table/bootstrap-table.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=path%>/framework/js/bootstrap_editable/1.5.1/css/bootstrap-editable.css">

        <script src="<%=path%>/framework/js/bootstrap_table/bootstrap-table.js"></script>
        <script src="<%=path%>/framework/js/bootstrap_table/locale/bootstrap-table-zh-CN.js"></script>
        <script src="<%=path%>/framework/js/bootstrap_editable/1.5.1/js/bootstrap-editable.min.js"></script>
        <script src="<%=path%>/framework/js/bootstrap_table/extensions/editable/bootstrap-table-editable.js"></script>
        <script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>




        <script src="<%=path%>/framework/jquery/jquery.ui.widget.js"></script>
        <script src="<%=path%>/framework/jquery/jquery.fileupload.js"></script>
        <link rel="stylesheet" href="<%=path%>/modules/breed/css/jquery.fileupload.css">
        <style>
            .editable-buttons {
                display: none;
                vertical-align: top;
                margin-left: 7px;
                zoom: 1;
            }

            .tabUl {
                margin-bottom:0px;
            }
            .tabLi {
                background-color: rgb(191, 191, 191);
                border-right: 1px solid rgb(224, 223, 223);
                width:33.2%;
                text-align: center;
            }
        </style>
    </head>

    <body style="background-color: #ffffff;">
        <div id="page-content" class="clearfix">
            <div class="row-fluid">
                <div class="span12">
                        <div class="row-fluid">
                            <%--标签菜单栏--%>
                                <ul class="nav nav-pills tabUl" id = "uiTab">
                                    <c:if test="${!empty tabList}">
                                        <c:forEach var="tab" items="${tabList}">
                                            <li class="tabLi">
                                                <a href="#tab_${tab.parentId}" code="${tab.parentId}" data-toggle="tab">${tab.name}</a>
                                            </li>
                                        </c:forEach>
                                    </c:if>

                                    <%--<c:if test="${tabShow.contains('1')}">--%>
                                        <%--<li class="active tabLi">--%>
                                            <%--<a href="#tab_1" data-toggle="tab" onclick="changeTab(this, 1);">育成</a>--%>
                                        <%--</li>--%>
                                    <%--</c:if>--%>
                                    <%--<c:if test="${tabShow.contains('2')}">--%>
                                    <%--<li class="tabLi">--%>
                                        <%--<a href="#tab_2" data-toggle="tab" onclick="changeTab(this, 2);">产蛋</a>--%>
                                    <%--</li>--%>
                                    <%--</c:if>--%>
                                    <%--<c:if test="${tabShow.contains('3')}">--%>
                                    <%--<li class="tabLi">--%>
                                        <%--<a href="#tab_3" data-toggle="tab" onclick="changeTab(this, 3);">肉鸡</a>--%>
                                    <%--</li>--%>
                                    <%--</c:if>--%>
                                </ul>
                        </div>

                    <%-- 功能栏 --%>
                    <div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;" id="jj1">
                        <div class="span2" align="center">
                            <span_customer2>品种</span_customer2>
                            <select id="good_type" tabindex="1"  name="good_type" style="width: 120px" onchange="changeType();">
                                <c:if test="${!empty goodTypeList}">
                                    <c:forEach var="goodType" items="${goodTypeList}">
                                        <option code="${goodType.L_category}" value="${goodType.good_code }">${goodType.good_name}</option>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </div>

                        <div class="span5 pull-right" align="right">
                            <div class="button-toolbar">
                                <div class="btn-group" role="group">
                                    <button class="btn yellow" onclick="changeTab(this, (houseType%3==0? 3 : houseType%3) + 3);">设置预警</button>
                                    <button class="btn red" onclick="changeTab(this, (houseType%3==0? 3 : houseType%3) + 6);">设置报警</button>


                                </div>
                                <div class="btn-group" role="group" aria-label="...">
                                    <button class="btn blue" id="uploadButtonFacade" style="display: none;" data-loading-text="文件上传中..." ></button>
                                    <button id="uploadButton" class="btn blue fileinput-button"><span><i class="icon-arrow-up">&nbsp;文件导入</i></span> <input id="fileupload" type="file" name="eFiles" ></button>
                                    <button id="" class="btn blue" ><span><i class="icon-arrow-down" onclick="downloadStandardTemplate()">&nbsp;模板下载</i></span></button>

                                </div>

                            </div>
                        </div>
                    </div>


                    <div class="tab-content">
                        <%-- 育成 --%>
                        <div class="tab-pane" id="tab_1">
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

                        <%-- 产蛋 --%>
                        <div class="tab-pane" id="tab_2">
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

                        <%-- 肉鸡 --%>
                        <div class="tab-pane" id="tab_3">
                            <table id="breedSTD3Table">
                                <thead>
                                    <tr>
                                        <th rowspan="2" data-valign="middle" data-align="center">生长日龄</th>
                                        <th colspan="2" data-valign="middle" data-align="center">母鸡死淘率%</th>
                                        <th colspan="2" data-valign="middle" data-align="center">公鸡死淘率%</th>
                                        <th colspan="2" data-valign="middle" data-align="center">均重</th>
                                        <th colspan="2" data-valign="middle" data-align="center">日采食量</th>
                                        <th colspan="2" data-valign="middle" data-align="center">累计饲料消耗量</th>
                                    </tr>
                                    <tr>
                                        <th data-field="female_avg_weed_out" data-align="center">每周平均</th>
                                        <th data-field="female_total_weed_out" data-align="center">累计</th>

                                        <th data-field="male_avg_weed_out" data-align="center">每周平均</th>
                                        <th data-field="male_total_weed_out" data-align="center">累计</th>

                                        <th data-field="male_weight" data-align="center">公鸡</th>
                                        <th data-field="female_weight" data-align="center">母鸡</th>

                                        <th data-field="male_feed_daliy" data-align="center">公鸡</th>
                                        <th data-field="female_feed_daliy" data-align="center">母鸡</th>

                                        <th data-field="female_total_feed" data-align="center">公鸡</th>
                                        <th data-field="male_total_feed" data-align="center">母鸡</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script type="text/javascript" src="<%=path%>/framework/js/bootstrap_table/extensions/toolbar/bootstrap-table-toolbar.js"></script>
        <script type="text/javascript" src="<%=path%>/modules/breed/js/breedStandard.js"></script>
    </body>
</html>
