// /**
//  * Created by LeLe on 2017-05-15.
//  */
//
// var ar ;
//
// $(document).ready(function() {
//     ar = getInstance({育成:"tab_1",产蛋:"tab_2"}, {iframe_tab_1:"multiFieldCurveAnalysisReport.cpt&batch_type=1",iframe_tab_2:"multiFieldCurveAnalysisReport.cpt&batch_type=2"});
//     ar.setCurrOrgId(org_id);
//     ar.initToolBarFarm();
// });


/**
 * Created by LeLe on 1/20/2017.
 */

var ar ;
var tabList = {父母代育成:"tab_1",父母代产蛋:"tab_2",商品代育成:"tab_3",商品代产蛋:"tab_4",白羽肉鸡:"tab_5",黄羽肉鸡:"tab_6",肉杂鸡:"tab_7",种鸡育成:"tab_8",种鸡产蛋:"tab_9",肉鸡饲养:"tab_10"};
var rptList = {iframe_tab_1:"multiFieldCurveAnalysisReport.cpt",iframe_tab_2:"multiFieldCurveAnalysisReport.cpt",iframe_tab_3:"multiFieldCurveAnalysisReport.cpt"
    ,iframe_tab_4:"multiFieldCurveAnalysisReport.cpt",iframe_tab_5:"multiFieldCurveAnalysisReport.cpt",iframe_tab_6:"multiFieldCurveAnalysisReport.cpt",iframe_tab_7:"multiFieldCurveAnalysisReport.cpt",iframe_tab_8:"multiFieldCurveAnalysisReport.cpt",iframe_tab_9:"multiFieldCurveAnalysisReport.cpt",iframe_tab_10:"multiFieldCurveAnalysisReport.cpt"};


$(document).ready(function() {
    initExtendParam();
    ar = getInstance(tabList, rptList);
    ar.setCurrTabId(tabId*1);
    ar.setCurrOrgId(org_id);
    ar.initToolBarFarm();

    // initHelp();
    initTab();
});

function initHelp(){
    help.size = ['600px', '720px'];
    help.context = document.getElementById("helpContext").innerHTML;
}

function initExtendParam(){
    var i = 1;
    var strParam = "&variety_num=";
    var feedTypes = tabListX.split(",");
    var feedTypeValues = tabListV.split(",");

    for(var key in feedTypes){
        rptList["iframe_tab_" + feedTypes[key]] +=  strParam + feedTypeValues[key];
    }
}

function initTab(){
    var total = 100;
    var totalActualWidth = 99.8;
    var num = 0;
    var widthRate = total;
    var tmpTabList = tabListX + ",";
    if (tmpTabList.toString().indexOf("1,")>-1) {
        document.getElementById("li0").style.display = "block";  //温湿度
        num +=1;
    }
    if ( tmpTabList.toString().indexOf("2,")>-1) {
        document.getElementById("li1").style.display = "block";  //点温差
        num +=1;
    }
    if (tmpTabList.toString().indexOf("3,")>-1) {
        document.getElementById("li2").style.display = "block";  //光照
        num +=1;
    }
    if (tmpTabList.toString().indexOf("4,")>-1 ) {
        document.getElementById("li3").style.display = "block";  //二氧化碳/氨气
        num +=1;
    }
    if (tmpTabList.toString().indexOf("5,")>-1) {
        document.getElementById("li4").style.display = "block";  //负压
        num +=1;
    }
    if (tmpTabList.toString().indexOf("6,")>-1) {
        document.getElementById("li5").style.display = "block";  //饮水
        num +=1;
    }
    if (tmpTabList.toString().indexOf("7,")>-1) {
        document.getElementById("li6").style.display = "block";  //饲料
        num +=1;
    }if (tmpTabList.toString().indexOf("8,")>-1) {
        document.getElementById("li7").style.display = "block";  //饲料
        num +=1;
    }if (tmpTabList.toString().indexOf("9,")>-1) {
        document.getElementById("li8").style.display = "block";  //饲料
        num +=1;
    }if (tmpTabList.toString().indexOf("10,")>-1) {
        document.getElementById("li9").style.display = "block";  //饲料
        num +=1;
    }




    if(num!=0){
        widthRate = widthRate / num;
        widthRate = widthRate.toFixed(2);
        var tmpWidthDiff = totalActualWidth - widthRate*num;
        tmpWidthDiff = tmpWidthDiff.toFixed(2);
        tmpWidthDiff = widthRate*1 + tmpWidthDiff/num - 0.04;
        tmpWidthDiff = tmpWidthDiff.toFixed(2);

        document.getElementById("li0").style.width = tmpWidthDiff+"%";
        document.getElementById("li1").style.width = tmpWidthDiff+"%";
        document.getElementById("li2").style.width = tmpWidthDiff+"%";
        document.getElementById("li3").style.width = tmpWidthDiff+"%";
        document.getElementById("li4").style.width = tmpWidthDiff+"%";
        document.getElementById("li5").style.width = tmpWidthDiff+"%";
        document.getElementById("li6").style.width = tmpWidthDiff+"%";
        document.getElementById("li7").style.width = tmpWidthDiff+"%";
        document.getElementById("li8").style.width = tmpWidthDiff+"%";
        document.getElementById("li9").style.width = tmpWidthDiff+"%";

        document.getElementById("tab").style.display = "block";

        var i = 1;
        for(var key in tabList){
            // console.log(document.getElementById("tab_" + i).getAttribute("class") );
            if(document.getElementById("tab_" + i).getAttribute("class").indexOf('active') >= 0){
                document.getElementById("tab_" + i).style.display = "block";
            } else{
                document.getElementById("tab_" + i).style.display = "none";
            }
            i++;
        }
    }

}


//切换标签页事件处理
$(function(){
    document.documentElement.style.overflowY = 'hidden';
    $('a[data-toggle="tab"]').on('shown', function (e) {
        currTabId = tabList[$(e.target).text()];

        var i = 1;
        for(var key in tabList){
            if( currTabId == tabList[key] ){
                document.getElementById(tabList[key]).style.display = "block";
            } else{
                document.getElementById(tabList[key]).style.display = "none";
            }
            i++;
        }
    });
});