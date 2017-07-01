/**
 * Created by LeLe on 11/15/2016.
 */
var ar ;

$(document).ready(function() {
    ar = getInstance({免疫:"tab_1",用药:"tab_2"}, {iframe_tab_1:"immune.cpt",iframe_tab_2:"pharmacy.cpt"});
    ar.setCurrOrgId(org_id);
    ar.initToolBarFarm();
});