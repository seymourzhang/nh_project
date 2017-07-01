/**
 * Created by LeLe on 11/14/2016.
 */
var ar ;

$(document).ready(function() {
    ar = getInstance({系统使用:"tab_1",区域统计:"tab_2"}, {iframe_tab_1:"systemUseInfoReport.cpt",iframe_tab_2:"userStatReport.cpt"});
    ar.setCurrOrgId(org_id);
    ar.initToolBarFarm();
});

