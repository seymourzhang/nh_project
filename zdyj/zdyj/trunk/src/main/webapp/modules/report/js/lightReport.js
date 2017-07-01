/**
 * Created by LeLe on 12/14/2016.
 */
var ar ;

$(document).ready(function() {
    ar = getInstance({入库:"tab_1"}, {iframe_tab_1:"lightReport.cpt"});
    ar.initToolBarFarm();
});