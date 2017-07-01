/**
 * Created by LeLe on 11/10/2016.
 */
var urlPath = "/fr/ReportServer?reportlet=";
// var urlPath = "http://localhost:8080/fr/ReportServer?reportlet=";
var urlParamOrgId = "&org_id=";
var urlParamFarmId = "&farm_id=";
var urlParamHouseId = "&house_id=";
var urlParamUserId = "&user_id=";
var urlParamBatchNo = "&batch_no=";
var urlParamSearchDateTime = "&d=";
var urlParamSearchType = "&type=";
var currTabId = "tab_1";
var currFrameId = "iframe_" + currTabId;
var tabList = {};
var frameList = {};
var orgList = [];
var currOrgId = "";
var path = "";
var userId = "";



//获取对象实例
function getInstance(tabList, frameList){
    this.tabList = tabList;
    this.frameList = frameList;
    path = document.getElementById("toolBarFarmParmPath").value;
    userId = document.getElementById("toolBarFarmParmUserId").value;
    return this;
};

//设置当前机构ID
function setCurrOrgId(orgId){
    currOrgId = orgId;
}

function setCurrTabId(id){
    currTabId = "tab_" + id;
    currFrameId = "iframe_" + currTabId;
}

//初始化农场工具栏
function initToolBarFarm(enableFlag){
    // farmList = [{farmId: 2, farmName: "新疆蛋种鸡育成场"},{farmId: 3, farmName: "新疆产蛋一场"}];
    enableFlag = (null == arguments[0])?true:false;
    var divStr = "";
    var i =1;

    if(enableFlag == true){
        $.ajax({
            type : "post",
            url : path + "/org/getOrgByUser",
            data : {},
            dataType : "json",
            success : function(result) {
                orgList = result.obj;
                if(orgList.length > 0 && ""==currOrgId){
                    currOrgId = orgList[0].id;
                }
                divStr += "<div class='span12' align='left'>";
                for(var key in orgList){
                    divStr += "<a id='btn_" + i + "' value = '" + orgList[key].id + "' href='javascript:;' class='btn blue' onclick='openUrl(" + orgList[key].id + ");'></i>" + orgList[key].name_cn + "</a>&nbsp;&nbsp;";
                    i+=1;
                }
                divStr += "</div>";
                document.getElementById("toolBarFarm").innerHTML = divStr;
                openUrl(currOrgId);
            }
        });
    } else{
        openUrl("");
    }
};

//打开url
function openUrl(orgId){
    // if(orgId != "" && orgId != null){
        currOrgId = orgId ;
        openUrlByFramId(frameList[currFrameId], currOrgId);
    // }
};

//通过farm id打开url
function openUrlByFramId(reportName, paramValue){
    var urlParam = "";

    if("undefined" != typeof(extendReportParam) && null != extendReportParam && "" != extendReportParam){
        urlParam += extendReportParam;
    }

    urlParam += urlParamOrgId + currOrgId;
    if("undefined" != typeof(farm_id) && null != farm_id && "" != farm_id) {
        urlParam += urlParamFarmId + farm_id;
        farm_id = null;
    }
    // } else{
    //     urlParam += urlParamOrgId + paramValue;
    //     house_id =null;
    //     batch_no =null;
    // }
    if("undefined" != typeof(house_id) && null != house_id && "" != house_id){
        urlParam += urlParamHouseId + house_id;
        house_id =null;
    } else{
        batch_no =null;
    }
    if("undefined" != typeof(batch_no) && null != batch_no && "" != batch_no){
        urlParam += urlParamBatchNo + batch_no;
        batch_no =null;
    }
    if("undefined" != typeof(userId) && null != userId && "" != userId){
        urlParam += urlParamUserId + userId;
    }
    if("undefined" != typeof(searchDateTime) && null != searchDateTime && "" != searchDateTime){
        urlParam += urlParamSearchDateTime + searchDateTime;
        searchDateTime = null;
    }
    if("undefined" != typeof(searchType) && null != searchType && "" != searchType){
        urlParam += urlParamSearchType + searchType;
        searchType = null;
    }

    var openPath = "";
    if("" == report_ip){
        openPath = window.location.protocol + "//" + window.location.host + urlPath + path.replace("/","") + "/" + reportName + urlParam ;
    } else{
        openPath = window.location.protocol + "//" + report_ip + ":" + report_port + urlPath + path.replace("/","") + "/" + reportName + urlParam ;
    }
    // alert(openPath);
    document.getElementById(currFrameId).style.height = window.parent.mainFrameHeight;
    document.getElementById(currFrameId).src = openPath;
    // console.log("mainFrameHeight:" +  window.parent.mainFrameHeight);
};



//切换标签页事件处理
$(function(){
    document.documentElement.style.overflowY = 'hidden';
    $('a[data-toggle="tab"]').on('shown', function (e) {
        getUrlParam();
        currTabId = tabList[$(e.target).text()];
        currFrameId = "iframe_" + currTabId;
        openUrl(currOrgId);
    });
});


function getUrlParam(){
    var p = getGlobalObj("analyzeReportParam");
    console.log(p);

    if("undefined" != typeof(p) && null != p){
        if("undefined" != typeof(p.org_id) && null != p.org_id){
            if(null != currOrgId && "undefined" != typeof(currOrgId)) {
                currOrgId = p.org_id;
            }
        }
        if("undefined" != typeof(p.user_id) && null != p.user_id){
            if(null != userId && "undefined" != typeof(userId)){
                userId = p.user_id;
            }
        }
        if("undefined" != typeof(p.farm_id) && null != p.farm_id){
            if( "undefined" != typeof(farm_id)) {
                farm_id = p.farm_id;
            }
        }
        if("undefined" != typeof(p.house_id) && null != p.house_id){
            if("undefined" != typeof(house_id)) {
                house_id = p.house_id;
            }
        }
        if("undefined" != typeof(p.batch_no) && null != p.batch_no){
            if( "undefined" != typeof(batch_no)) {
                batch_no = p.batch_no;
            }
        }
        if("undefined" != typeof(p.d) && null != p.d){
            if("undefined" != typeof(searchDateTime)) {
                searchDateTime = p.d;
            }
        }
        if("undefined" != typeof(p.type) && null != p.type){
            if("undefined" != typeof(searchType)) {
                searchType = p.type;
            }
        }
    }


};