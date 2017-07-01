/**
 * Created by LeLe on 2017-05-15.
 */
var ar ;

$(document).ready(function() {
    ar = getInstance({育成:"tab_1",产蛋:"tab_2"}, {iframe_tab_1:"multiFieldHouseCompareReport.cpt&batch_type=1",iframe_tab_2:"multiFieldHouseCompareReport.cpt&batch_type=2"});
    ar.setCurrOrgId(org_id);
    ar.initToolBarFarm();
});