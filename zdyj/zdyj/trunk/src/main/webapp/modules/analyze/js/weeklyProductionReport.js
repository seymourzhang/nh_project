/**
 * Created by LeLe on 11/15/2016.
 */
var ar ;
var extendReportParam = "";

$(document).ready(function() {
    ar = getInstance({入库:"tab_1"}, {iframe_tab_1:"weekProductionReport.cpt"});
    ar.setCurrOrgId(org_id);
    ar.initToolBarFarm();
});