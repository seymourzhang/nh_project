/**
 * Created by LeLe on 11/15/2016.
 */
var ar ;

$(document).ready(function() {
    var rc = (window.screen.height>=1800)?30:(window.screen.height>=1080 && window.screen.height<1800)?25:( (window.screen.height>=900 && window.screen.height<1080)?19:14);

    ar = getInstance({免疫:"tab_1",用药:"tab_2"}, {iframe_tab_1:"immune.cpt&row_count="+rc,iframe_tab_2:"pharmacy.cpt&row_count="+rc});
    ar.setCurrOrgId(org_id);
    ar.initToolBarFarm();
});