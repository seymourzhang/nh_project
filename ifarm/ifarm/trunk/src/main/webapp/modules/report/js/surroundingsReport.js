/**
 * Created by LeLe on 1/20/2017.
 */

var ar ;
var tabList = {温湿度:"tab_1",点温差:"tab_2",二氧化碳:"tab_3",光照:"tab_4",负压:"tab_5",饮水:"tab_6",饲料:"tab_7",氨气:"tab_8","二氧化碳/氨气":"tab_9"};
var rptList = {iframe_tab_1:"temProfile.cpt",iframe_tab_2:"tempDiff.cpt",iframe_tab_3:"carbonReport.cpt"
                ,iframe_tab_4:"lightReport.cpt",iframe_tab_5:"negativePressureReport.cpt",iframe_tab_6:"waterReport.cpt",iframe_tab_7:"feedReport.cpt",iframe_tab_8:"carbonReport.cpt",iframe_tab_9:"carbonReport.cpt"};


$(document).ready(function() {
    initExtendParam();
    ar = getInstance(tabList, rptList);
    ar.setCurrTabId(tabId*1+1);
    ar.setCurrOrgId(org_id);
    ar.initToolBarFarm();

    initHelp();
    initTab();
});

function initHelp(){
    help.size = ['600px', '720px'];
    help.context = document.getElementById("helpContext").innerHTML;
}

function initExtendParam(){
    if (reportTarIndex.toString().indexOf("911")>-1){
        rptList["iframe_tab_1"] +=  "&tarTFlag=1";
    } else{
        rptList["iframe_tab_1"] +=  "&tarTFlag=0";
    }
    if (reportTarIndex.toString().indexOf("914")>-1){
        rptList["iframe_tab_1"] +=  "&tarHFlag=1";
    } else{
        rptList["iframe_tab_1"] +=  "&tarHFlag=0";
    }
    if (reportTarIndex.toString().indexOf("915")>-1){
        rptList["iframe_tab_5"] +=  "&tarNPFlag=1";
    } else{
        rptList["iframe_tab_5"] +=  "&tarNPFlag=0";
    }
}

function initTab(){
    var total = 100;
    var totalActualWidth = 99.8;
    var num = 0;
    var widthRate = total;

    if (monitorIndex.toString().indexOf("101")>-1 || monitorIndex.toString().indexOf("102")>-1
        || monitorIndex.toString().indexOf("201")>-1 || monitorIndex.toString().indexOf("202")>-1 || monitorIndex.toString().indexOf("203")>-1
        || monitorIndex.toString().indexOf("204")>-1 || monitorIndex.toString().indexOf("205")>-1) {
        document.getElementById("li0").style.display = "block";  //温湿度
        num +=1;
    }
    if ( monitorIndex.toString().indexOf("104")>-1) {
        document.getElementById("li1").style.display = "block";  //点温差
        num +=1;
    }
    if (monitorIndex.toString().indexOf("303")>-1) {
        document.getElementById("li3").style.display = "block";  //光照
        num +=1;
    }
    if (monitorIndex.toString().indexOf("302")>-1 && monitorIndex.toString().indexOf("308")>-1) {
        document.getElementById("li8").style.display = "block";  //二氧化碳/氨气
        num +=1;
    } else{
        if (monitorIndex.toString().indexOf("302")>-1) {
            document.getElementById("li2").style.display = "block";  //二氧化碳
            num +=1;
        }
        if (monitorIndex.toString().indexOf("308")>-1) {
            document.getElementById("li7").style.display = "block";  //氨气
            num +=1;
        }
    }
    if (monitorIndex.toString().indexOf("304")>-1) {
        document.getElementById("li4").style.display = "block";  //负压
        num +=1;
    }
    if (monitorIndex.toString().indexOf("306")>-1) {
        document.getElementById("li5").style.display = "block";  //饮水
        num +=1;
    }
    if (monitorIndex.toString().indexOf("307")>-1) {
        document.getElementById("li6").style.display = "block";  //饲料
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