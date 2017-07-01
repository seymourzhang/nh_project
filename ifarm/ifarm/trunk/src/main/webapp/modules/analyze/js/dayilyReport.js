/**
 * Created by LeLe on 2017-05-19.
 */
var ar ;

$(document).ready(function() {
    ar = getInstance({入库:"tab_1"}, {iframe_tab_1:"dailyReport.cpt"});
    ar.setCurrOrgId(org_id);
    ar.initToolBarFarm();
});