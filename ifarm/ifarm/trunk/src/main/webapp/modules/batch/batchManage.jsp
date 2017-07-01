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
<div id="page-content" class="clearfix">
    <div class="row-fluid">
        <div class="span12">
            <div class="tabbable tabbable-custom boxless" >
                <div class="row-fluid">
                    <%--标签菜单栏--%>
                    <div class="span12" style="margin-left: 0px;height: 10px">
                        <ul class="nav nav-pills row-fluid" style="margin-bottom: 0px; ">
                            <li  class="active" id="faChu" style="text-align: center;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tabFaChu" data-toggle="tab" id="faChuA">发雏</a>
                            </li>
                            <li id="faLiao" style="text-align: center;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tabFaLiao" data-toggle="tab" id="faLiaoA">发料</a>
                            </li>
                            <li id="faYao" style="text-align: center;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tabFaYao" data-toggle="tab" id="faYaoA">发药</a>
                            </li>
                            <li  class="createBatch" id="createBatch" style="text-align: center;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none;" >
                                <a href="#tabCreateBatch" data-toggle="tab" id="createBatchA">进鸡</a>
                            </li>
                            <li  id="editBatch" style="text-align: center;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none; " >
                                <a href="#tabEditBatch" data-toggle="tab" id="editBatchA">调鸡</a>
                            </li>
                            <li  id="taoTai" style="text-align: center;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none; " >
                                <a href="#tabTaoTai" data-toggle="tab" id="taoTaiA">淘汰销售</a>
                            </li>
                            <li  id="overBatch" style="text-align: center;background-color: #BFBFBF;border-right: 1px solid #E0DFDF;display: none; " >
                                <a href="#tabOverBatch" data-toggle="tab" id="overBatchA">出栏</a>
                            </li>
                            <li  id="settle" style="text-align: center;background-color: #BFBFBF;display: none;" >
                                <a href="#tabSettle" data-toggle="tab" id="settleA">结算</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <input type="hidden" name="house_length" id="house_length" value="${pd.length}">

                <div class="tab-content row-fluid" style="border:none;padding-top: 2px;">
                    <%-- 发雏 --%>
                    <div class="tab-pane" id="tabFaChu">
                        <%--功能栏--%>
                        <div class="row-fluid" id="toolbarFaChu" style="background:#e7e5e5;padding-top: 10px;">
                            <div class="span12">
                                <div class="container-fluid">
                                    <div class="row-fluid" id="fc">
                                    <div class="span3" align="left">
                                   <span_customer2>${pd.level_name}</span_customer2>
                                <select id="faChuCompanySelect" name="faChuCompanySelect" style="width: 200px;">
                                <c:if test="${!empty companyList}">
									<c:forEach var="company" items="${companyList}">
										<option value="${company.org_id }">${company.org_name }</option>
									</c:forEach>
								</c:if>
                                </select>
                                        </div>
                                        <div class="span9" align="right">
                                        <button type="button" class="btn blue" style="text-align: left;vertical-align: middle;margin-left: -19px;" onclick="openFaChuWin();" id="addFaChu"
                                        <c:if test="${pd.write_read!=2}"> disabled="disabled"</c:if>>
																<i class="icon-plus">&nbsp;新增</i>
															</button>
<!--                                         <button type="button" class="btn blue" style="text-align: left;vertical-align: middle;" onclick="uploadConfirm();" id="addData"> -->
<!-- 																<i class="icon-arrow-up">&nbsp;文件导入</i> -->
<!-- 															</button> -->
										<button id="uploadButtonFacade" data-loading-text="文件上传中..." class="btn blue"></button>
                                        <button id="uploadButton" class="btn blue fileinput-button">
													            <span><i class="icon-arrow-up">&nbsp;文件导入</i></span>
													            <input id="fileupload" type="file" name="eFiles" >
													        </button>
                                            
<!-- 										<button type="button" class="btn blue" style="text-align: left;vertical-align: middle;" onclick="downLoad();" id="addData"> -->
																<a class="btn blue" href=download><i class="icon-arrow-down">&nbsp;文件模板下载</i></a>
<!-- 															</button> -->
                                        </div>
                                        
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid">
                            <div class="span12">
                                <p class="font s18 bold" id = "faChuFarmTitle" align="center" style="font-weight:bold;">
                                    ${pd.company_name}发雏记录
                                </p>
                                <div id="faChuFrame" align="center">
                                    <table id="faChuTable"></table>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <%-- 发料 --%>
                    <div class="tab-pane" id="tabFaLiao">
                        <%--功能栏--%>
                        <div class="row-fluid" id="toolbarFaLiao" style="background:#e7e5e5;padding-top: 10px;">
                            <div class="span12">
                                <div class="container-fluid">
                                    <div class="row-fluid" id="fc">
                                    <div class="span3" align="left">
                                   <span_customer2>${pd.level_name}</span_customer2>
                                <select id="faLiaoCompanySelect" name="faLiaoCompanySelect" style="width: 200px;">
                                <c:if test="${!empty companyList}">
									<c:forEach var="company" items="${companyList}">
										<option value="${company.org_id }">${company.org_name }</option>
									</c:forEach>
								</c:if>
                                </select>
                                        </div>
                                        <div class="span9" align="right">
                                        <button type="button" class="btn blue" style="text-align: left;vertical-align: middle;margin-left: -19px;" onclick="openFaLiaoWin();" id="addFaLiao"
                                        <c:if test="${pd.write_read!=2}"> disabled="disabled"</c:if>>
																<i class="icon-plus">&nbsp;新增</i>
															</button>
<!--                                         <button type="button" class="btn blue" style="text-align: left;vertical-align: middle;" onclick="uploadConfirm();" id="addData"> -->
<!-- 																<i class="icon-arrow-up">&nbsp;文件导入</i> -->
<!-- 															</button> -->
										<button id="uploadButtonFacade1" data-loading-text="文件上传中..." class="btn blue"></button>
                                        <button id="uploadButton1" class="btn blue fileinput-button">
													            <span><i class="icon-arrow-up">&nbsp;文件导入</i></span>
													            <input id="fileupload1" type="file" name="eFiles" >
													        </button>
                                            
<!-- 										<button type="button" class="btn blue" style="text-align: left;vertical-align: middle;" onclick="downLoad();" id="addData1"> -->
																<a class="btn blue" href=download><i class="icon-arrow-down">&nbsp;文件模板下载</i></a>
<!-- 															</button> -->
                                        </div>
                                        
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid">
                            <div class="span12">
                                <p class="font s18 bold" id = "faLiaoFarmTitle" align="center" style="font-weight:bold;">
                                    ${pd.company_name}发料记录
                                </p>
                                <div id="faLiaoFrame" align="center">
                                    <table id="faLiaoTable"></table>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <%-- 发药 --%>
                    <div class="tab-pane" id="tabFaYao">
                        <%--功能栏--%>
                        <div class="row-fluid" id="toolbarFaYao" style="background:#e7e5e5;padding-top: 10px;">
                            <div class="span12">
                                <div class="container-fluid">
                                    <div class="row-fluid" id="fc">
                                    <div class="span3" align="left">
                                   <span_customer2>${pd.level_name}</span_customer2>
                                <select id="faYaoCompanySelect" name="faYaoCompanySelect" style="width: 200px;">
                                <c:if test="${!empty companyList}">
									<c:forEach var="company" items="${companyList}">
										<option value="${company.org_id }">${company.org_name }</option>
									</c:forEach>
								</c:if>
                                </select>
                                        </div>
                                        <div class="span9" align="right">
                                        <button type="button" class="btn blue" style="text-align: left;vertical-align: middle;margin-left: -19px;" onclick="openFaLiaoWin();" id="addFaYao"
                                        <c:if test="${pd.write_read!=2}"> disabled="disabled"</c:if>>
																<i class="icon-plus">&nbsp;新增</i>
															</button>
<!--                                         <button type="button" class="btn blue" style="text-align: left;vertical-align: middle;" onclick="uploadConfirm();" id="addData"> -->
<!-- 																<i class="icon-arrow-up">&nbsp;文件导入</i> -->
<!-- 															</button> -->
										<button id="uploadButtonFacade2" data-loading-text="文件上传中..." class="btn blue"></button>
                                        <button id="uploadButton2" class="btn blue fileinput-button">
													            <span><i class="icon-arrow-up">&nbsp;文件导入</i></span>
													            <input id="fileupload2" type="file" name="eFiles" >
													        </button>
                                            
<!-- 										<button type="button" class="btn blue" style="text-align: left;vertical-align: middle;" onclick="downLoad();" id="addData2"> -->
																<a class="btn blue" href=download><i class="icon-arrow-down">&nbsp;文件模板下载</i></a>
<!-- 															</button> -->
                                        </div>
                                        
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid">
                            <div class="span12">
                                <p class="font s18 bold" id = "faYaoFarmTitle" align="center" style="font-weight:bold;">
                                    ${pd.company_name}发药记录
                                </p>
                                <div id="faYaoFrame" align="center">
                                    <table id="faYaoTable"></table>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <%-- 进鸡 --%>
                    <div class="tab-pane" id="tabCreateBatch">
                        <%--功能栏--%>
                        <div class="row-fluid" id="toolbarCreateBatch" style="background:#e7e5e5;padding-top: 10px;">
                            <div class="span12">
                                <div class="container-fluid">
                                    <div class="row-fluid" id="jj1">
                                    <div class="span3" align="left">
                                    <span_customer2>混养</span_customer2>&nbsp;&nbsp;&nbsp;&nbsp;
<!--                                     <div style="margin-top: 3px;"> -->
                                    <input type="radio" id="is_mix1" name="createBatchIsMix" value="1" checked="checked" onclick="panDuan(1);" style="margin-top: 0px;"/>是
                                    &nbsp;&nbsp;
                                    <input type="radio" id="is_mix2" name="createBatchIsMix" value="0" onclick="panDuan(0);" style="margin-top: 0px;"/>否                                                               
<!--                                     </div> -->
                                    </div>
                                        <div class="span3">
                                        <span_customer2>批次号</span_customer2>
                                        <div style="position:relative;margin-left:54px;margin-top:-25px;">
										<span style="margin-left:190px;width:18px;overflow:hidden;">
										<select id="createBatchNo2" style="width:233px;margin-left:-203px" onchange="getCheckFemaleNum();this.parentNode.nextSibling.value=this.value">
										</select></span><input id="createBatchNo"name="createBatchNo" style="margin-left:-10px;margin-top:4px;width:215px;position:absolute;left:0px;border: 0px;">
										</div>
										</div>
                                        <div class="span3" align="left">
<!--                                         <div id="jishu10"> -->
<!--                                             <span_customer2>品种</span_customer2> -->
<!--                                             <select id="createBatchGoodSelect" onchange= "changeGoodSelect()" > -->
<!--                                             </select> -->
<!--                                             </div> -->
<!--                                          <div id="muji10" style="display: none;"> -->
                                            <span_customer2>品种</span_customer2>
<!--                                             <div id="muji10" style="display: none;"></div> -->
                                            <select id="createBatchGoodSelect" onchange= "changeGoodSelect()" >
                                            </select>
<!--                                         </div>    -->
                                        </div>
                                        
                                        <div class="span3" align="left">
<!--                                         <div id="jishu11"> -->
<!--                                             <span_customer2>来源</span_customer2> -->
<!--                                             <select id="createBatchCorporationSelect"> -->
<!--                                             </select> -->
<!--                                         </div> -->
<!--                                         <div id="muji11" style="display: none;"> -->
                                            <span_customer2>来源</span_customer2>
                                            <div id="muji11" style="display: none;">&nbsp;&nbsp;&nbsp;</div>
                                            <select id="createBatchCorporationSelect">
                                            </select>
<!--                                         </div> -->
                                        </div>
                                    </div>
                                    <div class="row-fluid" id="jj2">
                                        <div class="span3" align="left">
                                            <span_customer2>进鸡日</span_customer2>
<!--                                             <div class="controls"> -->
                                                <div class="input-append date createBatchDatePicker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
                                                    <input readonly type="text" name="queryTime" id="createBatchQueryTime" style="width: 182px">
                                                    <span class="add-on">
                                                    <i class="icon-calendar"></i>
                                                </span>
                                                </div>
<!--                                             </div> -->
                                        </div>
                                        <div class="span3" align="left">
                                            <span_customer2>生长日龄</span_customer2>
                                            <input id="createBatchGrowDay" type="text" value="0">
                                        </div>
                                        <div class="span3" align="left">
                                        <div style="display:none;" id="muji">
                                            <span_customer2>母鸡数</span_customer2>
                                            <input id="createBatchFemaleNum" type="text" value="0">
                                            </div>
                                         <div id="jishu">
                                            <span_customer2>数量</span_customer2>
                                            <input id="createBatchFemaleNum2" type="text" value="0">
                                            </div>
                                        </div>
                                        <div class="span3" align="left" style="display:none;" id="gongji">
                                            <span_customer2>公鸡数</span_customer2>
                                            <input id="createBatchMaleNum" type="text" value="0">
                                        </div>
                                    </div>
                                    <div class="row-fluid" id="jj3">
                                            <div class="span3" align="left">
                                                <span_customer2>进入场</span_customer2>
                                                <select id="createBatchFarmSelect">
                                                </select>
                                            </div>
                                    <div class="span3" align="left">
                                            <span_customer2>进入栋</span_customer2>
                                            <select id="createBatchHouseSelect" style="width:233px;">
                                            </select>
                                        </div>
                                        <div class="span3" align="left">
<!--                                         <div id="jishu12"> -->
<!--                                             <span_customer2>备注</span_customer2> -->
<!--                                             <input id="createBatchRemark" type="text" style="maxlength="40" placeholder="请填写备注"> -->
<!--                                         </div>    -->
<!--                                         <div id="muji12" style="display: none;"> -->
                                            <span_customer2>备注</span_customer2>
<!--                                             <div id="muji12" style="display: none;">&nbsp;&nbsp;</div> -->
                                            <input id="createBatchRemark" type="text" style="maxlength="40" placeholder="请填写备注">
<!--                                         </div>    -->
                                        </div>
                                        <div class="span3" align="left">
                                            <a id="createBatchBtnSave" href="javascript:;" class="btn blue" onclick="saveData();"
                                            <c:if test="${pd.write_read==0}"> disabled="disabled"</c:if>><i class="icon-ok"></i>确认进鸡</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid">
                            <div class="span12">
                                <p id = "createBatchFarmTitle" align="center" style="font-weight:bold;margin-top: 5px;">
                                    农场
                                </p>
                                <div id="createBatchFrame" align="center">
                                    <table id="createBatchTable"></table>
                                </div>
                            </div>
                        </div>
                    </div>

                        <%-- 调鸡 --%>
                        <div class="tab-pane" id="tabEditBatch" >
                            <%--功能栏--%>
                            <div class="row-fluid" id="toolbarEditBatch" style="background:#e7e5e5;padding-top: 10px;">
                                <div class="span12">
                                    <div class="container-fluid">
                                        <div class="row-fluid" id="tj1">
                                        <div class="span2" align="left">
                                                <span_customer2>日期</span_customer2>
                                                    <div class="input-append date editBatchDatePicker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
                                                        <input readonly type="text" name="queryTime" id="editBatchQueryTime" style="width: 113px;margin-left: 3px;">
                                                        <span class="add-on">
                                                            <i class="icon-calendar"></i>
                                                        </span>
                                                    </div>
                                            </div>
                                            <div class="span2" align="left">
                                                <span_customer2>调出场</span_customer2>
                                                <select id="editBatchFarmSelect" style="width: 142px;">
                                                </select>
                                            </div>
                                            
                                            <div class="span2" align="left" >
                                                <span_customer2>调出栋舍</span_customer2>
                                                <select id="editBatchHouseSelect" onchange="getCount();" style="width: 137px;">
                                                </select>
                                            </div>
                                        
                                            <div class="span3" align="left">
                                            <div style="display:none;" id="muji2">
                                                <span_customer2>母鸡数量</span_customer2>
                                                <input id="editBatchFemaleNum" type="text" style="width:40px;" value="0">
                                                存栏量
                                                <input id="currStock1" type="text" style="width:40px;" disabled="disabled">
                                            </div>
                                            <div id="jishu2">
                                            <span_customer2>数量</span_customer2>
                                            <input id="editBatchFemaleNum2" type="text" style="width:40px;" value="0">
                                            存栏量
                                            <input id="currStock3" type="text" style="width:40px;" disabled="disabled">
                                            </div>
                                            </div>
                                            <div class="span3" align="left" style="display:none;" id="gongji2">
                                                <span_customer2>公鸡数量</span_customer2>
                                                <input id="editBatchMaleNum" type="text" style="width:40px;" value="0">
                                                存栏量
                                                <input id="currStock2" type="text" style="width:40px;" disabled="disabled">
                                            </div>
                                        </div>
<!--                                         <div class="row-fluid" id="tj2"> -->
                                        
<!--                                         </div> -->
                                        <div class="row-fluid" id="tj2">
                                            <div class="span2" align="left">
                                                <span_customer2>调入农场</span_customer2>
                                                <select id="editBatchFarmSelectTarget" onchange="getHouseTarget();" style="width: 130px;">
                                                </select>
                                            </div>
                                            <div class="span2" align="left">
                                                <span_customer2>调入栋舍</span_customer2>
                                                <select id="editBatchHouseSelectTarget" style="width: 130px;">
                                                </select>
                                            </div>
                                            <div class="span3" align="left">
                                                <span_customer2>备注</span_customer2>
                                                <input id="editBatchRemark" type="text" maxlength="40" placeholder="请填写备注" style="width: 150px;">
                                            </div>
                                            <%--<div class="span3" align="left">--%>
                                            <%--</div>--%>
                                            <div class="span1" align="left">
                                                <a id="editBatchBtnSave" href="javascript:;" class="btn blue" onclick="saveData();" style="margin-left: -121px;"
                                                <c:if test="${pd.write_read==0}"> disabled="disabled"</c:if>>
                                                    <i class="icon-ok"></i>&nbsp;确认调鸡
                                                </a>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row-fluid">
                                <div class="span12">
                                    <p id = "editBatchFarmTitle" align="center" style="font-weight: bold;margin-top: 5px;">
                                        农场
                                    </p>
                                    <div id="editBatchFrame" align="center">
                                        <table id="editBatchTable"></table>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <%-- 淘汰销售 --%>
                        <div class="tab-pane" id="tabTaoTai">
                            <%--功能栏--%>
                            <div class="row-fluid" id="toolbarTaoTai" style="background:#e7e5e5;padding-top: 10px;">
                                <div class="span12">
                                    <div class="container-fluid">
                                        <div class="row-fluid" id="tt">
                                            <div class="span3" align="left">
                                                <span_customer2>淘汰日</span_customer2>
                                                    <div class="input-append date taoTaiDatePicker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
                                                        <input readonly type="text" name="queryTime" id="taoTaiQueryTime" style="width: 135px;">
                                                        <span class="add-on">
                                                            <i class="icon-calendar"></i>
                                                        </span>
                                                    </div>
                                            </div>
                                            <div class="span2" align="left">
                                                <span_customer2>淘汰场</span_customer2>
                                                <select id="taoTaiFarmSelect" style="width: 130px;">
                                                </select>
                                                </div>
                                            <div class="span2" align="left">
                                                <span_customer2>淘汰栋舍</span_customer2>
                                                <select id="taoTaiHouseSelect" style="width: 90px" style="width: 89%;" onchange="getCount();">
                                                </select>
                                            </div>
                                            </div>
                                            <div class="row-fluid" id="tt2">
                                            <div class="span3" align="left">
                                                <span_customer2>淘汰数量</span_customer2>
                                                <input id="taoTaiSumNum" type="text" value="0" style="width: 50px;">
                                                 存栏量
                                            <input id="currStock4" type="text" style="width:40px;" disabled="disabled">
<!--                                             </div> -->
<!--                                             <div class="span4" align="left" > -->
                                            </div>
                                            <div class="span2" align="left">
                                                <span_customer2>淘汰重量</span_customer2>
                                                <input id="taoTaiSumWeight" type="text" value="0" style="width:80px;" onblur="setSumAcount();">&nbsp;kg
<!--                                             </div> -->
                                                
<!--                                             <div class="span2" align="left"> -->
</div>
                                            <div class="span2" align="left">
                                                <span_customer2>淘汰单价</span_customer2>
                                                <input id="taoTaiAvgPrice" type="text" value="0" style="width:40px;" onblur="setSumAcount();">&nbsp;元/kg
                                               </div>
                                            <div class="span2" align="left">
                                                <span_customer2>淘汰金额</span_customer2>
                                                <input id="taoTaiSumAcount" type="text" value="0" style="width:50px;" disabled="disabled">&nbsp;元
<!--                                             </div> -->
<!--                                             <div class="span3" align="left"> -->
<!--                                                 <span_customer2>总金额</span_customer2> -->
<!--                                                 <input id="overBatchAvgPriceSum" type="text" value="0" style="width:90px;" disabled="disabled">&nbsp;元 -->
<!--                                             </div> -->
<!--                                             <div class="span2" align="left"> -->
                                            </div>
                                            <div class="span2" align="left">
                                            <a id="taoTaiBtnSave" href="javascript:;" class="btn blue" onclick="saveData();" <c:if test="${pd.write_read==0}"> disabled="disabled"</c:if>>
                                                    <i class="icon-ok"></i>&nbsp;确认销售
                                                </a>
                                            </div>
                                        </div>
                                        <div class="row-fluid" id="tt3" style="display:none;">
                                            <div class="span3" align="left">
                                                <span_customer2>淘汰母鸡数量</span_customer2>
                                                <input id="taoTaiSumNum2" type="text" value="0" style="width: 50px;">
                                                 存栏量
                                            <input id="currStock5" type="text" style="width:40px;" disabled="disabled">
<!--                                             </div> -->
<!--                                             <div class="span4" align="left" > -->
                                            </div>
                                            <div class="span2" align="left">
                                                <span_customer2>淘汰母鸡重量</span_customer2>
                                                <input id="taoTaiSumWeight2" type="text" value="0" style="width:50px;" onblur="setSumAcount();">&nbsp;kg
<!--                                             </div> -->
                                                
<!--                                             <div class="span2" align="left"> -->
</div>
                                            <div class="span2" align="left">
                                                <span_customer2>淘汰母鸡单价</span_customer2>
                                                <input id="taoTaiAvgPrice2" type="text" value="0" style="width:20px;" onblur="setSumAcount();">&nbsp;元/kg
                                               </div>
                                            <div class="span2" align="left">
                                                <span_customer2>淘汰母鸡金额</span_customer2>
                                                <input id="taoTaiSumAcount2" type="text" value="0" style="width:50px;" disabled="disabled">&nbsp;元
<!--                                             </div> -->
<!--                                             <div class="span3" align="left"> -->
<!--                                                 <span_customer2>总金额</span_customer2> -->
<!--                                                 <input id="overBatchAvgPriceSum" type="text" value="0" style="width:90px;" disabled="disabled">&nbsp;元 -->
<!--                                             </div> -->
<!--                                             <div class="span2" align="left"> -->
                                            </div>
                                        </div><div class="row-fluid" id="tt4" style="display:none;">
                                            <div class="span3" align="left">
                                                <span_customer2>淘汰公鸡数量</span_customer2>
                                                <input id="taoTaiMaleSumNum" type="text" value="0" style="width: 50px;">
                                                 存栏量
                                            <input id="currStock6" type="text" style="width:40px;" disabled="disabled">
<!--                                             </div> -->
<!--                                             <div class="span4" align="left" > -->
                                            </div>
                                            <div class="span2" align="left">
                                                <span_customer2>淘汰公鸡重量</span_customer2>
                                                <input id="taoTaiMaleSumWeight" type="text" value="0" style="width:50px;" onblur="setSumAcount();">&nbsp;kg
<!--                                             </div> -->
                                                
<!--                                             <div class="span2" align="left"> -->
</div>
                                            <div class="span2" align="left">
                                                <span_customer2>淘汰公鸡单价</span_customer2>
                                                <input id="taoTaiMaleAvgPrice" type="text" value="0" style="width:20px;" onblur="setSumAcount();">&nbsp;元/kg
                                               </div>
                                            <div class="span2" align="left">
                                                <span_customer2>淘汰公鸡金额</span_customer2>
                                                <input id="taoTaiMaleSumAcount" type="text" value="0" style="width:50px;" disabled="disabled">&nbsp;元
<!--                                             </div> -->
<!--                                             <div class="span3" align="left"> -->
<!--                                                 <span_customer2>总金额</span_customer2> -->
<!--                                                 <input id="overBatchAvgPriceSum" type="text" value="0" style="width:90px;" disabled="disabled">&nbsp;元 -->
<!--                                             </div> -->
<!--                                             <div class="span2" align="left"> -->
                                            </div>
                                            <div class="span2" align="left">
                                            <a id="taoTaiBtnSave2" href="javascript:;" class="btn blue" onclick="saveData();" <c:if test="${pd.write_read==0}"> disabled="disabled"</c:if>>
                                                    <i class="icon-ok"></i>&nbsp;确认淘汰
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row-fluid">
                                <div class="span12">
                                    <p id = "taoTaiFarmTitle" align="center" style="font-weight: bold;margin-top: 5px;">
                                        农场
                                    </p>
                                    <div id="taoTaiFrame" align="center">
                                        <table id="taoTaiTable"></table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <%-- 出栏 --%>
                        <div class="tab-pane" id="tabOverBatch">
                            <%--功能栏--%>
                            <div class="row-fluid" id="toolbarOverBatch" style="background:#e7e5e5;padding-top: 10px;">
                                <div class="span12">
                                    <div class="container-fluid">
                                        <div class="row-fluid" id="cl1">
                                        <div class="span3" align="left">
                                                <span_customer2>出栏日</span_customer2>
                                                    <div class="input-append date overBatchDatePicker" data-date-format="yyyy-mm-dd" data-date-viewmode="years" data-date-minviewmode="months">
                                                        <input readonly type="text" name="queryTime" id="overBatchQueryTime" onchange="getOverBatchAge();" style="width: 90px;">
                                                        <span class="add-on">
                                                            <i class="icon-calendar"></i>
                                                        </span>
                                                    </div>
<!--                                             </div> -->
<!--                                             <div class="span1" align="left"> -->
                                                <span_customer2>出栏日龄</span_customer2>
                                                <input id="overBatchAge" type="text" disabled="disabled" style="width: 25px">
                                            </div>
                                            <div class="span6" align="left">
                                                <span_customer2>出栏场</span_customer2>
                                                <select id="overBatchFarmSelect" style="width: 130px;">
                                                </select>&nbsp;&nbsp;
<!--                                             </div> -->
<!--                                             <div class="span2" align="left"> -->
                                                <span_customer2>出栏栋舍</span_customer2>
                                                <select id="overBatchHouseSelect" onchange="getCount();" style="width: 90px">
                                                </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<!--                                             </div> -->
<!--                                             <div class="span3" align="left"> -->
                                            <div style="display:none;" id="muji3">
                                                <span_customer2>母鸡数量</span_customer2>
                                                <input id="overBatchFemaleNum" type="text" style="width: 40px;" disabled="disabled">
                                                &nbsp;&nbsp;
                                                <span_customer2>母鸡均重</span_customer2>
                                                <input id="overBatchFemaleAvgWeight" type="text" value="0" style="width:36px;">&nbsp;kg
                                            </div>
                                            <div id="jishu3">
                                            <span_customer2>数量</span_customer2>
                                                <input id="overBatchFemaleNum2" type="text" style="width: 40px;" disabled="disabled">
                                                &nbsp;&nbsp;
                                                <span_customer2>只均重</span_customer2>
                                                <input id="overBatchFemaleAvgWeight2" type="text" value="0" style="width:36px;">&nbsp;kg
                                            </div>
                                            </div>
                                            <div class="span3" align="left" style="display:none;" id="gongji3">
                                                <span_customer2>公鸡数量</span_customer2>
                                                <input id="overBatchMaleNum" type="text" style="width: 40px;" disabled="disabled">
                                                &nbsp;&nbsp;
                                                <span_customer2>公鸡均重</span_customer2>
                                                <input id="overBatchMaleAvgWeight" type="text" value="0" style="width:40px;">&nbsp;kg
                                            </div>
                                        </div>
                                        <div class="row-fluid" id="cl2">
                                            <div class="span6" align="left">
                                                <span_customer2>备注</span_customer2>
                                                &nbsp;&nbsp;
                                                <input id="overBatchRemark" type="text" maxlength="40" placeholder="请填写备注" style="width: 70%">
                                            </div>
                                            <div class="span6" align="left">
                                                <a id="overBatchBtnSaveY" href="javascript:;" class="btn blue" onclick="saveData();" <c:if test="${pd.write_read==0}"> disabled="disabled"</c:if>>
                                                    <i class="icon-ok"></i>&nbsp;确认出栏
                                                </a>
                                            </div>
                                        </div>
<!--                                         <div class="row-fluid" id="cl3" style="display: none;"> -->
<!--                                             <div class="span12" align="left"> -->
<!--                                                 --------------------------------------- <B>淘汰鸡销售</B> --------------------------------------- -->
<!--                                             </div> -->
<!--                                         </div> -->
<!--                                         <div class="row-fluid" id="cl4" style="display: none;"> -->
<!--                                             <div class="span2" align="left"> -->
<!--                                                 <span_customer2>总重量</span_customer2> -->
<!--                                                 <input id="overBatchSumWeight" type="text" value="0" style="width:90px;" disabled="disabled" onblur="getOverBatchAvgPriceSum();">&nbsp;kg -->
<!--                                             </div> -->
<!--                                             <div class="span2" align="left"> -->
<!--                                                 <span_customer2>总数量</span_customer2> -->
<!--                                                 <input id="overBatchSumNum" type="text" value="0" style="width: 50px;" disabled="disabled"> -->
<!--                                             </div> -->
<!--                                             <div class="span2" align="left"> -->
<!--                                                 <span_customer2>单价</span_customer2> -->
<!--                                                 <input id="overBatchAvgPrice" type="text" value="0" style="width:50px;" disabled="disabled" onblur="getOverBatchAvgPriceSum();">&nbsp;元/kg -->
<!--                                             </div> -->
<!--                                             <div class="span3" align="left"> -->
<!--                                                 <span_customer2>总金额</span_customer2> -->
<!--                                                 <input id="overBatchAvgPriceSum" type="text" value="0" style="width:90px;" disabled="disabled">&nbsp;元 -->
<!--                                             </div> -->
<!--                                             <div class="span3" align="left"> -->
<!--                                                 <a id="overBatchBtnSave" href="javascript:;" class="btn blue" onclick="saveData();"> -->
<!--                                                     <i class="icon-ok"></i>&nbsp;确认出栏 -->
<!--                                                 </a> -->
<!--                                             </div> -->
<!--                                         </div> -->
                                    </div>
                                </div>
                            </div>
                            <div class="row-fluid">
                                <div class="span12">
                                    <p id = "overBatchFarmTitle" align="center" style="font-weight: bold;margin-top: 5px;">
                                        农场
                                    </p>
                                    <div id="overBatchFrame" align="center">
                                        <table id="overBatchTable"></table>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <%-- 结算 --%>
                    <div class="tab-pane" id="tabSettle">
                        <%--功能栏--%>
                        <div class="row-fluid" id="toolbarSettle" style="background:#e7e5e5;padding-top: 10px;">
                            <div class="span12">
                                <div class="container-fluid">
                                    <div class="row-fluid" id="fc">
<!--                                     <div class="span4" align="left"> -->
<!--                                     </div> -->
                                    <div class="span4" align="left">
                                   <span_customer2>${pd.level_name}</span_customer2>
                                <select id="settleCompanySelect" name="settleCompanySelect" style="width: 200px;">
                                <c:if test="${!empty companyList}">
									<c:forEach var="company" items="${companyList}">
										<option value="${company.org_id }">${company.org_name }</option>
									</c:forEach>
								</c:if>
                                </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid" style="padding-top: 10px;">
                            <div class="span12">
                            <p id = "settleFarmTitle" align="center" style="font-size: 30px;font-weight:bold;">
                                    ${pd.company_name}结算记录
                                </p>
                            <div class="row-fluid" >
                             <div class="span12" style="background:#e7e5e5;border: 1px solid #BFBFBF;">
                                <div class="span12" id="settleFrame">
                                <p align="center" id = "settleFarmTitle2" style="margin-top:5px;font-size: 20px;font-weight:bold;">
                                                                                                                  已出栏待结算列表
                                </p>
                                </div>
<!--                                 <div class="span2" align="right"> -->
<!--                                 <button type="button" class="btn blue" style="text-align: left;vertical-align: middle;" onclick="uploadConfirm();" id="addData"> -->
<!-- 																<i class="icon-arrow-up">&nbsp;文件导入</i> -->
<!-- 															</button> -->
<!-- 															</div> -->
															</div>
                                    <table id="settleTable"></table>
                                </div>
                            </div>
                        </div>
                        &nbsp;
                        <div class="row-fluid" id="maoji" style="display: none;">
                            <div class="span12">
                                <p class="font s18 bold" id = "settleSubFarmTitle" align="center" style="font-weight:bold;">
                                    
                                </p>
                                <div id="settleSubFrame" align="center">
                                <div class="span12" style="background:#e7e5e5;">
<!--                                 <div class="span1" align="center" style="background:#08c;text-align: center;width: 8.19%;height: 100%;"> -->
<!--                                  <p style="background:#08c;text-align: right;width: 100%;height: 100%;">   -->
                                <div class="row-fluid" >
                             <div class="span12" style="background:#e7e5e5;border: 1px solid #BFBFBF;">
                              <p align="center"  style="font-weight:bold;font-size: 15px;">
                                                                                                                  毛鸡结算
                              </p>
                              </div>
                              </div>
<!--                                   </p> -->
<!--                                 </div> -->
<!--                                 <div class="span11" style="width: 89.67%"> -->
                                    <table id="settleSubTable" style="width: 100%"></table>
<!--                                     </div> -->
                                </div>
                                </div>
                            </div>
                        </div>
                        &nbsp;
                        <div class="row-fluid" id="maoji2" style="display: none;">
                            <div class="span12">
                                <div id="settleSub2Frame" align="center">
                                <div class="span12" style="background:#e7e5e5;">
                                  <p align="center"  style="font-weight:bold;font-size: 15px;">
                                                                                                                                                     苗/药/料
                                    </p>
<!--                                 <div class="span11"> -->
                                    <table id="settleSub2Table"></table>
<!--                                     </div> -->
                                        <button type="button" class='btn green' style="width: 100%;height: 30px;" onclick="addSettleSub();" <c:if test="${pd.write_read!=2}"> disabled="disabled"</c:if>>
                                        <p align="center"  style="font-weight:bold;"><i class="icon-plus"></i>新增</p></button>
                                </div>
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid" id="maoji3" style="display: none;">
                                <div class="span12">
                                    <div id="settleSub3Frame" align="center">
                                        <table id="settleSub3Table" style="width: 100%;">
                                        <tr><th style="width: 50%;background:#08c;border-right: 1px solid #E0DFDF;">
                                        <p style="color: white;">结算总金额</p></th>
										<th style="width: 50%;background:#08c;"><p style="color: white;">结算平均价格</p></th><tr>
										</table>
										<table style="width: 100%;">
										<tr><td style="width: 50%;text-align: center;background-color:  #C0C0C0;border-right: 1px solid #E0DFDF;" id="js3">
<!-- 										<input id="js3" type="text" disabled="disabled" style="text-align: center;width:98%;height: 100%;margin-top: 10px;margin-left: -1px;"></td> -->
										<td style="width: 25%;text-align: center;background-color:  #C0C0C0;border-right: 1px solid #E0DFDF;" id="js4">
<!-- 										<input id="js4" type="text" disabled="disabled" style="text-align: center;width:98%;height: 100%;margin-top: 10px;margin-left: -1px;"></td> -->
										<td style="width: 25%;text-align: center;background-color:  #C0C0C0;border: 1px solid #BFBFBF;" id="js5">
<!-- 										<input id="js5" type="text" disabled="disabled" style="text-align: center;width:98%;height: 100%;margin-top: 10px;margin-left: -1px;"></td><tr> -->
                                        </table>
                                    </div>
                                </div>
                            </div>
                       <div class="row-fluid" id="maoji4" style="display: none;">
                       <div class="span12" style="text-align: right;">
<!--                        <button type="button" class="btn blue" onclick="editSettleData();" style="display: none;" id="jiesuan1">结算</button> -->
<!--                        <button type="button" class="btn blue" onclick="editSettleData();" style="display: none;" id="jiesuan2">修改</button> -->
                       </div>
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
    var farm_id = "${pd.farm_id}";
    var farm_name = "${pd.farm_name}";
    var batchIndex = "${batchIndex}";
    
    function uploadConfirm() {
        layer.open({
            type: 2,
            title:"文件导入",
            skin: 'layui-layer-lan', //加上边框
            area: ['520px', '370px'], //宽高
            closeBtn: 0,
            shift: 4, //动画类型
            content: '<%=path%>/breed/editFileUrl'
        });
    }
</script>
<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>

<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
<script type="text/javascript" src="<%=path%>/modules/batch/js/settle.js"></script>
<script type="text/javascript" src="<%=path%>/modules/batch/js/taoTai.js"></script>
<script type="text/javascript" src="<%=path%>/modules/batch/js/faChu.js"></script>
<script type="text/javascript" src="<%=path%>/modules/batch/js/createBatch.js"></script>
<script type="text/javascript" src="<%=path%>/modules/batch/js/editBatch.js"></script>
<script type="text/javascript" src="<%=path%>/modules/batch/js/overBatch.js"></script>
<script type="text/javascript" src="<%=path%>/modules/batch/js/batchManage.js"></script>

</body>
</html>
