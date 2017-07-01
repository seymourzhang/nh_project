/**
 * Created by LeLe on 11/15/2016.
 */
var ar ;
var extendReportParam = ""; //翻页查阅

$(document).ready(function() {
    ar = getInstance({入库:"tab_1"}, {iframe_tab_1:"dailyProductionReport.cpt"});
    ar.setCurrOrgId(org_id);
    ar.initToolBarFarm();
});