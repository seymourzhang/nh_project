/**
 * Created by LeLe on 2017-05-19.
 */
var ar ;
var iframeList = {iframe_tab_1:"farmStatReport.cpt"};
var farmReportName = "farmListReport.cpt";
var houseReportName = "houseListReport.cpt";
var extendParam = "&__bypagesize__=false";

$(document).ready(function() {
    ar = getInstance({入库:"tab_1"}, iframeList);
    // ar.setCurrOrgId(org_id);
    // ar.initToolBarFarm();
    ar.openUrl("");
});


function cjkEncode(text) {
    if (text == null) {
        return "";
    }
    var newText = "";
    for (var i = 0; i < text.length; i++) {
        var code = text.charCodeAt (i);
        if (code >= 128 || code == 91 || code == 93) {//91 is "[", 93 is "]".
            newText += "[" + code.toString(16) + "]";
        } else {
            newText += text.charAt(i);
        }
    }
    return newText;
}
function goToFarmList(apn,userId){
//            alert(apn);
//            alert(cjkEncode(apn));
    currFrameId = "iframe_tab_1";
    openPath = window.location.protocol + "//" + window.location.host + urlPath + path.replace("/","") + "/" + farmReportName + extendParam + "&user_id=" + userId + "&&area_parent_name="+cjkEncode(apn);
    document.getElementById(currFrameId).style.height = window.parent.mainFrameHeight;
    document.getElementById(currFrameId).src = openPath;
    // window.location="ReportServer?reportlet=ifarm/farmListReport.cpt&user_id=" + userId + "&&area_parent_name="+cjkEncode(apn);
}
function goToHouseList(apn,userId){
//            alert(apn);
//            alert(cjkEncode(apn));
    currFrameId = "iframe_tab_1";
    openPath = window.location.protocol + "//" + window.location.host + urlPath + path.replace("/","") + "/" + houseReportName + extendParam + "&user_id=" + userId + "&&area_parent_name="+cjkEncode(apn);
    document.getElementById(currFrameId).style.height = window.parent.mainFrameHeight;
    document.getElementById(currFrameId).src = openPath;
    // window.location="ReportServer?reportlet=ifarm/farmListReport.cpt&user_id=" + userId + "&&area_parent_name="+cjkEncode(apn);
}