/**
 * Created by LeLe on 1/20/2017.
 */
var ar ;
var tabList = {温湿度:"tab_1",点温差:"tab_2",二氧化碳:"tab_3",光照:"tab_4",报警:"tab_5"};
var rptList = {iframe_tab_1:"temProfile.cpt",iframe_tab_2:"tempDiff.cpt",iframe_tab_3:"carbonReport.cpt"
                ,iframe_tab_4:"lightReport.cpt",iframe_tab_5:"alarmReport.cpt"};


$(document).ready(function() {
    ar = getInstance(tabList, rptList);
    ar.setCurrTabId(tabId*1+1);
    ar.setCurrOrgId(org_id);
    ar.initToolBarFarm();

    initHelp();
});

function initHelp(){
    help.size = ['600px', '720px'];
    help.context = document.getElementById("helpContext").innerHTML;
}