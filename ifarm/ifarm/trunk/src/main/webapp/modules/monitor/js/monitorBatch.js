var count0rg;
var num=2;
var allSearch = "true";
var orgLevel = 2;
var pSize = null;
var isShowRoujiOrZhongji = false;
var isShowDanjiOrYucheng = false;
var farmOrHouse = false;
var isShowEmptyHouse = false;
var m_category = bizCode;
var variety_num = bizCode;
//获取肉鸡的品种类型
var category_type = null;
var tempSensorErrorMin = 5;
var tempSensorErrorMax = 45;
var co2SensorErrorMin = 100;


$(document).ready(function() {
    App.init(); // initlayout and core plugins
    initTable("tbodyMonitorCurList", createRouJiTableColumns(), []);

    // initHelp();



    //切换tab页面时做相应参数的处理。
    $("ul li").on('shown', function (e) {
        // var $tabPane = $("#tab_"+ $(e.target).attr("code"));
        var code = $(e.target).attr("code");
        var bizCode = $(e.target).attr("bizCode");
        var parentId = $(e.target).attr("parentId");
        m_category = bizCode;
        variety_num = bizCode;
        activeid = code;
        render(parentId);
    })
    render(parentId);
});

function render(parentId){
    if(parentId == 3){
        $("#rouji_category").attr("style", "display:inline;");
    }else{
        $("#rouji_category").attr("style", "display:none;");
    }

    if(parentId == 1){
        //育成
        showDanjiOrYucheng(true,true);
    }else if(parentId == 2){
        //产蛋
        showDanjiOrYucheng(true,false);
    }else if(parentId == 3){
        //肉鸡
        showDanjiOrYucheng(false,'');
    }
}

function showEmptyHouse(v){
    isShowEmptyHouse = v;
    if(false == isShowEmptyHouse){
        document.getElementById("iconShowWithOutEmptyHouse1").style.display = "inline";
        document.getElementById("iconShowEmptyHouse1").style.display = "none";
        document.getElementById("iconShowWithOutEmptyHouse2").style.display = "inline";
        document.getElementById("iconShowEmptyHouse2").style.display = "none";
    } else{
        document.getElementById("iconShowWithOutEmptyHouse2").style.display = "none";
        document.getElementById("iconShowEmptyHouse2").style.display = "inline";
        document.getElementById("iconShowWithOutEmptyHouse1").style.display = "none";
        document.getElementById("iconShowEmptyHouse1").style.display = "inline";
    }
    reflushMonitor();
}

function showFarmOrHouse(v){
    farmOrHouse = v;
    if(false == farmOrHouse){
        document.getElementById('iconShowWithOutEmptyHouse1').innerHTML = "在养农场";
        document.getElementById('iconShowEmptyHouse1').innerHTML = "在养农场";
        document.getElementById('iconShowWithOutEmptyHouse2').innerHTML = "全部农场";
        document.getElementById('iconShowEmptyHouse2').innerHTML = "全部农场";
        document.getElementById("iconShowNongChang1").style.display = "inline";
        document.getElementById("iconShowNongChang2").style.display = "none";
        document.getElementById("iconShowDongShe1").style.display = "inline";
        document.getElementById("iconShowDongShe2").style.display = "none";
    } else{
        document.getElementById('iconShowWithOutEmptyHouse1').innerHTML = "在养栋舍";
        document.getElementById('iconShowEmptyHouse1').innerHTML = "在养栋舍";
        document.getElementById('iconShowWithOutEmptyHouse2').innerHTML = "全部栋舍";
        document.getElementById('iconShowEmptyHouse2').innerHTML = "全部栋舍";
        document.getElementById("iconShowNongChang1").style.display = "none";
        document.getElementById("iconShowNongChang2").style.display = "inline";
        document.getElementById("iconShowDongShe1").style.display = "none";
        document.getElementById("iconShowDongShe2").style.display = "inline";
    }
    reflushMonitor();
}

function showRoujiOrZhongji(v){
    isShowRoujiOrZhongji = v;
    if(false == isShowRoujiOrZhongji){
//         document.getElementById("iconShowRouJi1").style.display = "inline";
//         document.getElementById("iconShowRouJi2").style.display = "none";
// //        document.getElementById("iconShowZhongJi1").style.display = "inline";
// //        document.getElementById("iconShowZhongJi2").style.display = "none";
// //        document.getElementById("btnShowChanDan").style.display = "none";
// //        document.getElementById("btnShowYuCheng").style.display = "none";
//         document.getElementById("iconShowChanDan1").style.display = "none";
//         document.getElementById("iconShowChanDan2").style.display = "inline";
//         document.getElementById("iconShowYuCheng1").style.display = "inline";
//         document.getElementById("iconShowYuCheng2").style.display = "none";
        houseType=3;
    } else{
//      document.getElementById("iconShowRouJi1").style.display = "none";
//         document.getElementById("iconShowRouJi2").style.display = "inline";
// //        document.getElementById("iconShowZhongJi1").style.display = "none";
// //        document.getElementById("iconShowZhongJi2").style.display = "inline";
// //        document.getElementById("btnShowChanDan").style.display = "inline";
// //        document.getElementById("btnShowYuCheng").style.display = "inline";
//         document.getElementById("iconShowChanDan1").style.display = "inline";
//         document.getElementById("iconShowChanDan2").style.display = "none";
//         document.getElementById("iconShowYuCheng1").style.display = "inline";
//         document.getElementById("iconShowYuCheng2").style.display = "none";
        houseType=2;
        isShowDanjiOrYucheng= false;
    }
    // var select = document.getElementById("orgId"+(count0rg-2));
    // select.onchange();
    reflushMonitor();
}

function showDanjiOrYucheng(v2,v){
    isShowRoujiOrZhongji = v2;
    if(v2==true){
        isShowDanjiOrYucheng = v;
        // document.getElementById("iconShowRouJi1").style.display = "none";
        // document.getElementById("iconShowRouJi2").style.display = "inline";
        if(false == isShowDanjiOrYucheng){
            houseType=2;
        }else{
            houseType=1;
//        loadJs();
        }
    }else{
        category_type = v;
        houseType=3;
        if(category_type ==1){
            document.getElementById("rouJiAll1").style.display = "none";
            document.getElementById("rouJiAll2").style.display = "inline";
            document.getElementById("baiYu2").style.display = "none";
            document.getElementById("baiYu1").style.display = "inline";
            document.getElementById("huangYu2").style.display = "inline";
            document.getElementById("huangYu1").style.display = "none";
            document.getElementById("rouZa2").style.display = "none";
            document.getElementById("rouZa1").style.display = "inline";
        }else if(category_type ==2){
            document.getElementById("rouJiAll1").style.display = "none";
            document.getElementById("rouJiAll2").style.display = "inline";
            document.getElementById("baiYu2").style.display = "inline";
            document.getElementById("baiYu1").style.display = "none";
            document.getElementById("huangYu2").style.display = "none";
            document.getElementById("huangYu1").style.display = "inline";
            document.getElementById("rouZa2").style.display = "none";
            document.getElementById("rouZa1").style.display = "inline";
        }else if(category_type ==3){
            document.getElementById("rouJiAll1").style.display = "none";
            document.getElementById("rouJiAll2").style.display = "inline";
            document.getElementById("baiYu2").style.display = "inline";
            document.getElementById("baiYu1").style.display = "none";
            document.getElementById("huangYu2").style.display = "inline";
            document.getElementById("huangYu1").style.display = "none";
            document.getElementById("rouZa2").style.display = "inline";
            document.getElementById("rouZa1").style.display = "none";
        }else{
            if(categoryType1 =="1"){
                document.getElementById("btnBaiYu").style.display = "inline";
            }
            if(categoryType2 =="2"){
                document.getElementById("btnHuangYu").style.display = "inline";
            }
            if(categoryType3 =="3"){
                document.getElementById("btnRouZa1").style.display = "inline";
            }
            document.getElementById("rouJiAll1").style.display = "inline";
            document.getElementById("rouJiAll1").style.display = "inline";
            document.getElementById("rouJiAll2").style.display = "none";
            document.getElementById("baiYu2").style.display = "inline";
            document.getElementById("baiYu1").style.display = "none";
            document.getElementById("huangYu2").style.display = "inline";
            document.getElementById("huangYu1").style.display = "none";
            document.getElementById("rouZa2").style.display = "none";
            document.getElementById("rouZa1").style.display = "inline";
        }
    }
    // var select = document.getElementById("orgId"+(count0rg-2));
    // select.onchange();
    reflushMonitor();
}

// function initHelp(){
//     help.size = ['600px', '524px'];
//     help.context = document.getElementById("helpContext").innerHTML;
// }

function OrgSearch(count0rg,num){
    document.getElementById("orgId"+(count0rg-1)).style.display = "none";
    document.getElementById("org2Id"+(count0rg-1)).style.display = "none";
    if("" != org_id){
        var select = document.getElementById("orgId"+(count0rg-2));
        if(null != select && "undefined" != select){
            for(var i=0; i<select.options.length; i++){
                var corporation = select.options[i].value;
                var strs= new Array();
                strs = corporation.split(",");
                if(strs[0] == org_id){
                    org_id="";
                    select.options[i].selected = true;
                    select.onchange();
                    break;
                }
            }
        }
        org_id="";
    } else {
        // if ("" != farm_id) {
        //     var select2 = document.getElementById("orgId" + (count0rg - 1));
        //     if (null != select2 && "undefined" != select2) {
        //         for (var i = 0; i < select2.options.length; i++) {
        //             var farm = select2.options[i].value;
        //             var strs2 = new Array();
        //             strs2 = farm.split(",");
        //             if (strs2[0] == farm_id) {
        //                 farm_id = "";
        //                 select2.options[i].selected = true;
        //                 select2.onchange();
        //                 break;
        //             }
        //         }
        //     }
        //     // farm_id = "";
        // } else {
        //     if ("" != house_id) {
        //         var select3 = document.getElementById("orgId" + (count0rg));
        //         if (null != select3 && "undefined" != select3) {
        //             for (var i = 0; i < select3.options.length; i++) {
        //                 var house = select3.options[i].value;
        //                 var strs3 = new Array();
        //                 strs3 = house.split(",");
        //                 if (strs3[0] == house_id) {
        //                     house_id = "";
        //                     select3.options[i].selected = true;
        //                     select3.onchange();
        //                     reflushMonitor();
        //                     break;
        //                 }
        //             }
        //         }
        //         house_id = "";
        //
        //     } else {
        if ("" == org_id && "" == farm_id && "" == house_id) {
            reflushMonitor();
        }
        // }
        // }
    }
}

function reflushMonitor() {
//  var param =$.serializeObject($('#farmData'));
//    var obj = document.getElementById("enableMonitorSet");
    tableDestroy("tbodyMonitorCurList");

    if(pd_farmOrHouse !=""){
        farmOrHouse = pd_farmOrHouse;
        document.getElementById('iconShowWithOutEmptyHouse1').innerHTML = "在养栋舍";
        document.getElementById('iconShowEmptyHouse1').innerHTML = "在养栋舍";
        document.getElementById('iconShowWithOutEmptyHouse2').innerHTML = "全部栋舍";
        document.getElementById('iconShowEmptyHouse2').innerHTML = "全部栋舍";
        document.getElementById("iconShowNongChang1").style.display = "none";
        document.getElementById("iconShowNongChang2").style.display = "inline";
        document.getElementById("iconShowDongShe1").style.display = "none";
        document.getElementById("iconShowDongShe2").style.display = "inline";
    }

    if(house_type != ""){
        if(house_type == 3){
            isShowRoujiOrZhongji =false;
        }else{
            isShowRoujiOrZhongji =true;
            // document.getElementById("iconShowRouJi1").style.display = "none";
            // document.getElementById("iconShowRouJi2").style.display = "inline";
            if(house_type == 2){
                isShowDanjiOrYucheng = false;
                // document.getElementById("iconShowChanDan1").style.display = "inline";
                // document.getElementById("iconShowChanDan2").style.display = "none";
                // document.getElementById("iconShowYuCheng1").style.display = "inline";
                // document.getElementById("iconShowYuCheng2").style.display = "none";
            }else{
                isShowDanjiOrYucheng = true;
                // document.getElementById("iconShowChanDan1").style.display = "none";
                // document.getElementById("iconShowChanDan2").style.display = "inline";
                // document.getElementById("iconShowYuCheng1").style.display = "none";
                // document.getElementById("iconShowYuCheng2").style.display = "inline";
            }
        }
    }

    if(false == isShowRoujiOrZhongji){
        initTable("tbodyMonitorCurList", createRouJiTableColumns(), []);
    }else{
        if(false == isShowDanjiOrYucheng){
            initTable("tbodyMonitorCurList", createDanJiTableColumns(), []);
        }else{
            initTable("tbodyMonitorCurList", createYuChengTableColumns(), []);
        }
    }

    var param;
    param = {"farm_id": farm_id,
        "org_id": $("#orgId"+(count0rg-2)).val().split(",")[1],
        "farmOrHouse":farmOrHouse,
        "isShowRoujiOrZhongji":isShowRoujiOrZhongji,
        "isShowDanjiOrYucheng":isShowDanjiOrYucheng,
        "isShowEmptyHouse":isShowEmptyHouse,
        "m_category":m_category,
        "category_type":category_type};

//    param.isShowEmptyHouse =  isShowEmptyHouse;

    $.ajax({
        // async: true,
        url: path + "/monitor/reflushMonitorBatch",
        data: param,
        type: "POST",
        dataType: "json",
        cache: false,
        success: function (result) {
//            var list = eval(result.obj);
            var obj = result.obj;
            var dataJosn = $.parseJSON(JSON.stringify(obj));
            $("#tbodyMonitorCurListTable").bootstrapTable('load',dataJosn);
            //完成事件绑定
            $('[data-toggle="tooltip"]').tooltip();
            if(org_id !="" || pd_farmOrHouse !="" || house_type !=""){
                org_id="";
                farm_id="";
                house_id="";
                pd_farmOrHouse="";
                house_type="";
            }
        }
    });

    //完成事件绑定
    setInterval("javascript:tooltips();", 1000); //1s刷新一次
}

function tooltips(){
    $('[data-toggle="tooltip"]').tooltip();
}


//生产监控(肉鸡)列表
function createRouJiTableColumns(){
    var dc = [];
    var dc2 =[];
    // var col =4;
    // if(farmOrHouse){
    //  col=5;
    // }
    // 肉鸡
//    dc2.push({field: "is_mix",
//        title: "&nbsp;&nbsp;",
//        width: '5%',
//        sortable: true,
//        formatter: function(value,row,index){
//          if(value==1){
//            return "混养";
//          }else{
//            return "分养";
//          }
//        }
//    });
    // 基本信息
    dc.push({
        title: "基本信息",
        colspan: 6,
        align: 'center'
    });
    dc2.push({
        field: "province",
        title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;省份&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
        width: '7%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
        }
    });
    dc2.push({
        field: "farm_name_chs",
        title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;农场&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
        width: '7%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="'+ row.farm_user_real_name+' '+row.farm_user_mobile_1+'" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
        }
    });
    if(farmOrHouse){
        dc2.push({
            field: "house_name",
            title: "栋舍",
            width: '3%',
            sortable: true,
            cellStyle:function(value, row, index) {
                return {css:{"background-color": "#c6f7db"}};
            },
            formatter: function(value,row,index){
                return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="'+ row.house_user_real_name+' '+row.house_user_mobile_1+'" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        });
    }else{
        dc2.push({
            field: "house_count",
            title: "栋舍数",
            width: '3%',
            sortable: true,
            cellStyle:function(value, row, index) {
                return {css:{"background-color": "#c6f7db"}};
            },
            formatter: function(value,row,index){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        });
    }

    dc2.push({
        field: "age",
        title: "日龄",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });
    dc2.push({
        field: "send_child_count",
        title: "入雏数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });
    dc2.push({
        field: "cur_weight",
        title: "均重(克)",
        width: '7%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            if(row.female_cur_weight==0 || row.female_cur_weight ==undefined){
                // if(row.m_female_cur_amount>0){
                //     if(row.female_cur_weight_flag==0){
                //         return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.m_female_cur_amount+'&nbsp;</a>';
                //     }else if(row.female_cur_weight_flag==1){
                //         return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'"  style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.m_female_cur_amount+'&nbsp;</a>';
                //     }else{
                //         return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'"  style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.m_female_cur_amount+'&nbsp;</a>';
                //     }
                // }else{
                    if(row.female_cur_weight_flag==0){
                        return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                    }else if(row.female_cur_weight_flag==1){
                        return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'"  style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                    }else{
                        return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'"  style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                    }
                // }
            }else{
                if(row.female_cur_weight_flag==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_weight+'&nbsp;</a>';
                }else if(row.female_cur_weight_flag==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_weight+'&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_weight+'&nbsp;</a>';
                }
            }

        }
    });

    // 昨日信息
    dc.push({
            title: "昨日信息",
            colspan: 5,
            align: 'center'
        }
    );
    dc2.push({
        field: "cur_amount",
        title: "存栏数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(row.female_cur_amount==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+(row.female_cur_amount + row.male_cur_amount)+'</a>';
            }
        }
    });
    // dc2.push({
    //     field: "death",
    //     title: "死亡",
    //     width: '5%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#e8f0c5"}};
    //     },
    //     formatter: function(value,row,index){
    //      if(row.female_death==undefined){
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //      }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+(row.female_death +row.male_death)+'</a>';
    //      }
    //     }
    // });
    //
    // dc2.push({
    //     field: "culling",
    //     title: "淘汰",
    //     width: '5%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#e8f0c5"}};
    //     },
    //     formatter: function(value,row,index){
    //      if(row.female_culling==undefined){
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //         }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+(row.female_culling+row.male_culling)+'</a>';
    //      }
    //     }
    // });

    dc2.push({
        field: "female_cur_cd",
        title: "死淘数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });

    dc2.push({
        field: "female_cur_cd_rate",
        title: "死淘率(%)",
        width: '8%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value ==undefined){
                if(row.female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else if(row.female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_avg_weed_out+'<br>高报：'+row.female_avg_weed_out_high_alarm+'<br>高预：'+row.female_avg_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_avg_weed_out+'<br>高报：'+row.female_avg_weed_out_high_alarm+'<br>高预：'+row.female_avg_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }
            }else{
                if(row.female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }else if(row.female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_avg_weed_out+'<br>高报：'+row.female_avg_weed_out_high_alarm+'<br>高预：'+row.female_avg_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_avg_weed_out+'<br>高报：'+row.female_avg_weed_out_high_alarm+'<br>高预：'+row.female_avg_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }
            }
        }
    });

    // dc2.push({
    //     field: "female_avg_weed_out",
    //     title: "标准(%)",
    //     width: '3%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#e8f0c5"}};
    //     },
    //     formatter: function(value,row,index){
    //      if(value==undefined){
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //      }else{
    //      return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //      }
    //     }
    // });
    dc2.push({
        field: "cur_feed",
        title: "采食(千克)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+row.cur_feed+'</a>';
            }
        }
    });
    dc2.push({
        field: "cur_water",
        title: "饮水(吨/栋)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">'+row.cur_water+'</a>';
            }
        }
    });

    // 累计信息
    dc.push({
            title: "累计信息",
            colspan: 3,
            align: 'center'
        }
    );

    dc2.push({
        field: "pile_female_cur_cd",
        title: "死淘数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#f1e1ff"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });

    dc2.push({
        field: "pile_female_cur_cd_rate",
        title: "死淘率(%)",
        width: '8%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#f1e1ff"}};
        },
        formatter: function(value,row,index){
            if(value ==undefined){
                if(row.pile_female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-</p>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else if(row.pile_female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_total_weed_out+'<br>高报：'+row.female_total_weed_out_high_alarm+'<br>高预：'+row.female_total_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_total_weed_out+'<br>高报：'+row.female_total_weed_out_high_alarm+'<br>高预：'+row.female_total_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }
            }else{
                if(row.pile_female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }else if(row.pile_female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_total_weed_out+'<br>高报：'+row.female_total_weed_out_high_alarm+'<br>高预：'+row.female_total_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_total_weed_out+'<br>高报：'+row.female_total_weed_out_high_alarm+'<br>高预：'+row.female_total_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }
            }
        }
    });
    // dc2.push({
    //     field: "pile_act_feed_daliy",
    //     title: "只日耗料(克/只)",
    //     width: '10%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#f1e1ff"}};
    //     },
    //     formatter: function(value,row,index){
    //         if(value==undefined){
    //             if(row.pile_act_feed_daliy_flag ==0){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //             }else if(row.pile_act_feed_daliy_flag ==1){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_total_feed+'<br>高报：'+row.female_total_feed_high_alarm+'<br>低报：'+row.female_total_feed_low_alarm+'<br>高预：'+row.female_total_feed_high_warm+'<br>低预：'+row.female_total_feed_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //             }else{
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_total_feed+'<br>高报：'+row.female_total_feed_high_alarm+'<br>低报：'+row.female_total_feed_low_alarm+'<br>高预：'+row.female_total_feed_high_warm+'<br>低预：'+row.female_total_feed_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //             }
    //         }else{
    //             if(row.pile_act_feed_daliy_flag ==0){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //             }else if(row.pile_act_feed_daliy_flag ==1){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_total_feed+'<br>高报：'+row.female_total_feed_high_alarm+'<br>低报：'+row.female_total_feed_low_alarm+'<br>高预：'+row.female_total_feed_high_warm+'<br>低预：'+row.female_total_feed_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //             }else{
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_total_feed+'<br>高报：'+row.female_total_feed_high_alarm+'<br>低报：'+row.female_total_feed_low_alarm+'<br>高预：'+row.female_total_feed_high_warm+'<br>低预：'+row.female_total_feed_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //             }
    //         }
    //     }
    // });

    // dc2.push({
    //     field: "female_feed_daliy",
    //     title: "只日耗料标准",
    //     width: '6%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#f1e1ff"}};
    //     },
    //     formatter: function(value,row,index){
    //      if(value==undefined){
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //      }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+row.female_feed_daliy+'</a>';
    //      }
    //     }
    // });
    dc2.push({
        field: "pile_water_feed_rate",
        title: "水料比",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#f1e1ff"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });

    // 能耗信息
    //     dc.push({
    //             title: "能耗信息",
    //             colspan: 2,
    //             align: 'center'
    //         }
    //     );
    //     dc2.push({
    //         field: "ran_liao",
    //         title: "燃料",
    //         width: '5%',
    //         sortable: true,
    //         cellStyle:function(value, row, index) {
    //             return {css:{"background-color": "#f1eaaa"}};
    //         },
    //         formatter: function(value,row,index){
    //          if(value==undefined){
    //              return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //          }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //          }
    //         }
    //     });
    //
    //     dc2.push({field: "dian_liang",
    //         title: "电量",
    //         width: '5%',
    //         sortable: true,
    //         cellStyle:function(value, row, index) {
    //             return {css:{"background-color": "#f1eaaa"}};
    //         },
    //         formatter: function(value,row,index){
    //          if(value==undefined){
    //              return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //          }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //          }
    //         }
    //     });

    var dataColumns = [dc, dc2];
    return dataColumns;
};

//生产监控(蛋鸡)列表
function createDanJiTableColumns(){
    var dc = [];
    var dc2 =[];
    // var col = 4;
    // if(farmOrHouse){
    //  col=5;
    // }
    // 基本信息
    dc.push({
        title: "基本信息",
        colspan: 6,
        align: 'center'
    });
    dc2.push({
        field: "province",
        title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;省份&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
        width: '7%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
        }
    });
    dc2.push({
        field: "farm_name_chs",
        title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;农场&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
        width: '7%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" title="'+ row.farm_user_real_name+' '+row.farm_user_mobile_1+'" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
        }
    });
    if(farmOrHouse){
        dc2.push({
            field: "house_name",
            title: "栋舍",
            width: '3%',
            sortable: true,
            cellStyle:function(value, row, index) {
                return {css:{"background-color": "#c6f7db"}};
            },
            formatter: function(value,row,index){
                return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="'+ row.house_user_real_name+' '+row.house_user_mobile_1+'" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        });
    }else{
        dc2.push({
            field: "house_count",
            title: "栋舍数",
            width: '3%',
            sortable: true,
            cellStyle:function(value, row, index) {
                return {css:{"background-color": "#c6f7db"}};
            },
            formatter: function(value,row,index){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        });
    }
    dc2.push({
        field: "age",
        title: "日龄",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });
    dc2.push({
        field: "send_child_count",
        title: "入雏数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });
    dc2.push({
        field: "cur_weight",
        title: "均重(克)",
        width: '7%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            if(row.female_cur_weight==0 || row.female_cur_weight==undefined){
                if(row.female_cur_weight_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else if(row.female_cur_weight_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }
            }else{
                if(row.female_cur_weight_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_weight+'&nbsp;</a>';
                }else if(row.female_cur_weight_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_weight+'&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_weight+'&nbsp;</a>';
                }
            }
        }
    });

    // 昨日信息
    dc.push({
            title: "昨日信息",
            colspan: 10,
            align: 'center'
        }
    );
    dc2.push({
        field: "cur_amount",
        title: "存栏数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(row.female_cur_amount==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+(row.female_cur_amount + row.male_cur_amount)+'</a>';
            }
        }
    });
    // dc2.push({
    //     field: "death_count",
    //     title: "死亡",
    //     width: '5%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#e8f0c5"}};
    //     },
    //     formatter: function(value,row,index){
    //      if(value == undefined){
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //      }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+row.death_count+'</a>';
    //      }
    //     }
    // });
    //
    // dc2.push({
    //     field: "culling_count",
    //     title: "淘汰",
    //     width: '5%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#e8f0c5"}};
    //     },
    //     formatter: function(value,row,index){
    //      if(value == undefined){
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //      }else{
    //      return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+row.culling_count+'</a>';
    //      }
    //     }
    // });

    dc2.push({
        field: "cur_cd_count",
        title: "死淘数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value == undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+row.cur_cd_count+'</a>';
            }
        }
    });

    dc2.push({
        field: "cur_cd_rate",
        title: "死淘率(%)",
        width: '8%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value == undefined){
                if(row.female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else if(row.female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_avg_weed_out+'<br>高报：'+row.female_week_avg_weed_out_high_alarm+'<br>高预：'+row.female_week_avg_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_avg_weed_out+'<br>高报：'+row.female_week_avg_weed_out_high_alarm+'<br>高预：'+row.female_week_avg_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }
            }else{
                if(row.female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }else if(row.female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_avg_weed_out+'<br>高报：'+row.female_week_avg_weed_out_high_alarm+'<br>高预：'+row.female_week_avg_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_avg_weed_out+'<br>高报：'+row.female_week_avg_weed_out_high_alarm+'<br>高预：'+row.female_week_avg_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }
            }
        }
    });
    dc2.push({
        field: "cur_feed",
        title: "采食(千克)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+row.cur_feed+'</a>';
            }
        }
    });
    dc2.push({
        field: "cur_water",
        title: "饮水(吨/栋)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value == undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">'+row.cur_water+'</a>';
            }
        }
    });

    // 产蛋信息
    // dc.push({
    //         title: "产蛋信息",
    //         colspan: 5,
    //         align: 'center'
    //     }
    // );
    dc2.push({
        field: "laying_cur_amount",
        title: "产蛋数(枚)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value == undefined){
                if(row.laying_cur_amount_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>低报：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-</a>';
                }else if(row.laying_cur_amount_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.rs_female_laying_avg_count+'<br>低报：'+row.rs_female_laying_avg_count_low_alarm+'<br>低预：'+row.rs_female_laying_avg_count_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.rs_female_laying_avg_count+'<br>低报：'+row.rs_female_laying_avg_count_low_alarm+'<br>低预：'+row.rs_female_laying_avg_count_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-</a>';
                }
            }else{
                if(row.laying_cur_amount_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>低报：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'</a>';
                }else if(row.laying_cur_amount_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.rs_female_laying_avg_count+'<br>低报：'+row.rs_female_laying_avg_count_low_alarm+'<br>低预：'+row.rs_female_laying_avg_count_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.rs_female_laying_avg_count+'<br>低报：'+row.rs_female_laying_avg_count_low_alarm+'<br>低预：'+row.rs_female_laying_avg_count_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'</a>';
                }
            }
        }
    });
    dc2.push({
        field: "act_cl_laying_rate",
        title: "产蛋率(%)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value == undefined){
                if(row.act_cl_laying_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>低报：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else if(row.act_cl_laying_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.cl_laying_rate+'<br>低报：'+row.rs_laying_rate_low_alarm+'<br>低预：'+row.rs_laying_rate_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.cl_laying_rate+'<br>低报：'+row.rs_laying_rate_low_alarm+'<br>低预：'+row.rs_laying_rate_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }
            }else{
                if(row.act_cl_laying_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>低报：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }else if(row.act_cl_laying_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.cl_laying_rate+'<br>低报：'+row.rs_laying_rate_low_alarm+'<br>低预：'+row.rs_laying_rate_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.cl_laying_rate+'<br>低报：'+row.rs_laying_rate_low_alarm+'<br>低预：'+row.rs_laying_rate_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }
            }
        }
    });
    dc2.push({
        field: "sum_laying_qual_cur_wight",
        title: "总蛋重(千克)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value ==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&house_id="+row.house_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&house_id="+row.house_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">'+row.sum_laying_qual_cur_wight+'</a>';
            }
        }
    });
    dc2.push({
        field: "laying_qual_cur_wight",
        title: "蛋重(克/枚)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value ==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&house_id="+row.house_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&house_id="+row.house_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">'+row.laying_qual_cur_wight+'</a>';
            }
        }
    });
    dc2.push({
        field: "break_count",
        title: "破损数(枚)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value ==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&house_id="+row.house_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&house_id="+row.house_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });

    // 累计信息
    dc.push({
            title: "累计信息",
            colspan: 5,
            align: 'center'
        }
    );

    dc2.push({
        field: "pile_cur_cd_count",
        title: "死淘数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#f1e1ff"}};
        },
        formatter: function(value,row,index){
            if(value == undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+row.pile_cur_cd_count+'</a>';
            }
        }
    });

    dc2.push({
        field: "pile_cur_cd_rate",
        title: "死淘率(%)",
        width: '8%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#f1e1ff"}};
        },
        formatter: function(value,row,index){
            if(value == undefined){
                if(row.pile_female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else if(row.pile_female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_total_weed_out+'<br>高报：'+row.female_week_total_weed_out_high_alarm+'<br>高预：'+row.female_week_total_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_total_weed_out+'<br>高报：'+row.female_week_total_weed_out_high_alarm+'<br>高预：'+row.female_week_total_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }
            }else{
                if(row.pile_female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }else if(row.pile_female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_total_weed_out+'<br>高报：'+row.female_week_total_weed_out_high_alarm+'<br>高预：'+row.female_week_total_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_total_weed_out+'<br>高报：'+row.female_week_total_weed_out_high_alarm+'<br>高预：'+row.female_week_total_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'&nbsp;</a>';
                }
            }
        }
    });
    // dc2.push({
    //     field: "pile_act_feed_daliy",
    //     title: "只日耗料(克/只)",
    //     width: '10%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#f1e1ff"}};
    //     },
    //     formatter: function(value,row,index){
    //         if(value == undefined){
    //             if(row.pile_act_feed_daliy_flag ==0){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //             }else if(row.pile_act_feed_daliy_flag ==1){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.bz_total_feed+'<br>高报：'+row.total_feed_high_alarm+'<br>低报：'+row.total_feed_low_alarm+'<br>高预：'+row.total_feed_high_warm+'<br>低预：'+row.total_feed_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //             }else{
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.bz_total_feed+'<br>高报：'+row.total_feed_high_alarm+'<br>低报：'+row.total_feed_low_alarm+'<br>高预：'+row.total_feed_high_warm+'<br>低预：'+row.total_feed_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //             }
    //         }else{
    //             if(row.pile_act_feed_daliy_flag ==0){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //             }else if(row.pile_act_feed_daliy_flag ==1){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.bz_total_feed+'<br>高报：'+row.total_feed_high_alarm+'<br>低报：'+row.total_feed_low_alarm+'<br>高预：'+row.total_feed_high_warm+'<br>低预：'+row.total_feed_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //             }else{
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.bz_total_feed+'<br>高报：'+row.total_feed_high_alarm+'<br>低报：'+row.total_feed_low_alarm+'<br>高预：'+row.total_feed_high_warm+'<br>低预：'+row.total_feed_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //             }
    //         }
    //     }
    // });
    dc2.push({
        field: "pile_water_feed_rate",
        title: "水料比",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#f1e1ff"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });
    dc2.push({
        field: "pile_laying_cur_amount",
        title: "产蛋数(枚)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#f1e1ff"}};
        },
        formatter: function(value,row,index){
            if(value == undefined){
                if(row.pile_laying_cur_amount_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>低报：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-</a>';
                }else if(row.pile_laying_cur_amount_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.rs_female_laying_total_count+'<br>低报：'+row.rs_female_laying_total_count_low_alarm+'<br>低预：'+row.rs_female_laying_total_count_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.rs_female_laying_total_count+'<br>低报：'+row.rs_female_laying_total_count_low_alarm+'<br>低预：'+row.rs_female_laying_total_count_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-</a>';
                }
            }else{
                if(row.pile_laying_cur_amount_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>低报：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'</a>';
                }else if(row.pile_laying_cur_amount_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.rs_female_laying_total_count+'<br>低报：'+row.rs_female_laying_total_count_low_alarm+'<br>低预：'+row.rs_female_laying_total_count_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.rs_female_laying_total_count+'<br>低报：'+row.rs_female_laying_total_count_low_alarm+'<br>低预：'+row.rs_female_laying_total_count_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+value+'</a>';
                }
            }
        }
    });
    dc2.push({
        field: "pile_laying_qual_cur_wight",
        title: "蛋重(克/枚)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#f1e1ff"}};
        },
        formatter: function(value,row,index){
            if(value ==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&house_id="+row.house_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&house_id="+row.house_id +"&batch_no="+row.batch_no +"&pId=产蛋率"+"&tabId="+variety_num + '\','+isRead+')">'+row.pile_laying_qual_cur_wight+'</a>';
            }
        }
    });

    // 能耗信息
    //     dc.push({
    //             title: "能耗信息",
    //             colspan: 2,
    //             align: 'center'
    //         }
    //     );
    //     dc2.push({
    //         field: "ran_liao",
    //         title: "燃料",
    //         width: '5%',
    //         sortable: true,
    //         cellStyle:function(value, row, index) {
    //             return {css:{"background-color": "#f1eaaa"}};
    //         },
    //         formatter: function(value,row,index){
    //          if(value==undefined){
    //              return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //          }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //          }
    //         }
    //     });
    //
    //     dc2.push({field: "dian_liang",
    //         title: "电量",
    //         width: '5%',
    //         sortable: true,
    //         cellStyle:function(value, row, index) {
    //             return {css:{"background-color": "#f1eaaa"}};
    //         },
    //         formatter: function(value,row,index){
    //          if(value==undefined){
    //              return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //          }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //          }
    //         }
    //     });

    var dataColumns = [dc, dc2];
    return dataColumns;
};

//生产监控(育成)列表
function createYuChengTableColumns(){
    var dc = [];
    var dc2 =[];
    // var col=5;
    // if(farmOrHouse){
    //  col=6;
    // }
    // 肉鸡
    dc2.push({field: "is_mix",
        title: '&nbsp;&nbsp;',
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            if(value == 1){
                return "混养";
            }else if(value == 0){
                return "母鸡";
            }else{
                return "公鸡";
            }
        }
    });
    // 基本信息
    dc.push({
        title: "基本信息",
        colspan: 7,
        align: 'center'
    });
    dc2.push({
        field: "province",
        title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;省份&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
        width: '7%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
        }
    });
    dc2.push({
        field: "farm_name_chs",
        title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;农场&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
        width: '7%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="'+ row.farm_user_real_name+' '+row.farm_user_mobile_1+'" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
        }
    });
    if(farmOrHouse){
        dc2.push({
            field: "house_name",
            title: "栋舍",
            width: '3%',
            sortable: true,
            cellStyle:function(value, row, index) {
                return {css:{"background-color": "#c6f7db"}};
            },
            formatter: function(value,row,index){
                return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="'+ row.house_user_real_name+' '+row.house_user_mobile_1+'" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        });
    }else{
        dc2.push({
            field: "house_count",
            title: "栋舍数",
            width: '3%',
            sortable: true,
            cellStyle:function(value, row, index) {
                return {css:{"background-color": "#c6f7db"}};
            },
            formatter: function(value,row,index){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        });
    }
    dc2.push({
        field: "age",
        title: "日龄",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });
    dc2.push({
        field: "send_child_count",
        title: "入雏数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });
    dc2.push({
        field: "cur_weight",
        title: "均重(克)",
        width: '7%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#c6f7db"}};
        },
        formatter: function(value,row,index){
            if(row.female_cur_weight==0 || row.female_cur_weight==undefined){
                if(row.female_cur_weight_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else if(row.female_cur_weight_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }
            }else{
                if(row.female_cur_weight_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_weight+'&nbsp;</a>';
                }else if(row.female_cur_weight_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_weight+'&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_weight+'<br>高报：'+row.female_weight_high_alarm+'<br>低报：'+row.female_weight_low_alarm+'<br>高预：'+row.female_weight_high_warm+'<br>低预：'+row.female_weight_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_weight+'&nbsp;</a>';
                }
            }
        }
    });

    // 死淘信息
    dc.push({
            title: "昨日信息",
            colspan: 5,
            align: 'center'
        }
    );
    dc2.push({
        field: "cur_amount",
        title: "存栏数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(row.female_cur_amount==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no+"&tabId="+variety_num + '\','+isRead+')">'+row.female_cur_amount+'</a>';
            }
        }
    });
    // dc2.push({
    //     field: "death_count",
    //     title: "死亡",
    //     width: '5%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#e8f0c5"}};
    //     },
    //     formatter: function(value,row,index){
    //      if(row.female_death==undefined){
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //      }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+row.female_death+'</a>';
    //      }
    //     }
    // });
    //
    // dc2.push({
    //     field: "culling_count",
    //     title: "淘汰",
    //     width: '5%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#e8f0c5"}};
    //     },
    //     formatter: function(value,row,index){
    //      if(row.female_culling==undefined){
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //      }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+row.female_culling+'</a>';
    //      }
    //     }
    // });

    dc2.push({
        field: "female_cur_cd",
        title: "死淘数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(row.female_cur_cd==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });

    dc2.push({
        field: "cur_cd_rate",
        title: "死淘率(%)",
        width: '8%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(row.female_cur_cd_rate==undefined){
                if(row.female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else if(row.female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_avg_weed_out+'<br>高报：'+row.female_week_avg_weed_out_high_alarm+'<br>高预：'+row.female_week_avg_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_avg_weed_out+'<br>高报：'+row.female_week_avg_weed_out_high_alarm+'<br>高预：'+row.female_week_avg_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }
            }else{
                if(row.female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_cd_rate+'&nbsp;</a>';
                }else if(row.female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_avg_weed_out+'<br>高报：'+row.female_week_avg_weed_out_high_alarm+'<br>高预：'+row.female_week_avg_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_cd_rate+'&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_avg_weed_out+'<br>高报：'+row.female_week_avg_weed_out_high_alarm+'<br>高预：'+row.female_week_avg_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.female_cur_cd_rate+'&nbsp;</a>';
                }
            }
        }
    });

    // dc2.push({
    //     field: "female_avg_weed_out",
    //     title: "标准(%)",
    //     width: '3%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#e8f0c5"}};
    //     },
    //     formatter: function(value,row,index){
    //      if(value==undefined){
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //      }else{
    //      return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+row.female_avg_weed_out+'</a>';
    //      }
    //     }
    // });
    dc2.push({
        field: "cur_feed",
        title: "采食(千克)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+row.cur_feed+'</a>';
            }
        }
    });
    dc2.push({
        field: "cur_water",
        title: "饮水(吨/栋)",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#e8f0c5"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">'+row.cur_water+'</a>';
            }
        }
    });

    // 累计信息
    dc.push({
            title: "累计信息",
            colspan: 3,
            align: 'center'
        }
    );
    dc2.push({
        field: "pile_female_cur_cd",
        title: "死淘数",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#f1e1ff"}};
        },
        formatter: function(value,row,index){
            if(row.female_cur_cd==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });

    dc2.push({
        field: "pile_cur_cd_rate",
        title: "死淘率(%)",
        width: '8%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#f1e1ff"}};
        },
        formatter: function(value,row,index){
            if(row.pile_female_cur_cd_rate==undefined){
                if(row.pile_female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else if(row.pile_female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_total_weed_out+'<br>高报：'+row.female_week_total_weed_out_high_alarm+'<br>高预：'+row.female_week_total_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_total_weed_out+'<br>高报：'+row.female_week_total_weed_out_high_alarm+'<br>高预：'+row.female_week_total_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;-&nbsp;</a>';
                }
            }else{
                if(row.pile_female_cur_cd_rate_flag ==0){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>高预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.pile_female_cur_cd_rate+'&nbsp;</a>';
                }else if(row.pile_female_cur_cd_rate_flag ==1){
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_total_weed_out+'<br>高报：'+row.female_week_total_weed_out_high_alarm+'<br>高预：'+row.female_week_total_weed_out_high_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.pile_female_cur_cd_rate+'&nbsp;</a>';
                }else{
                    return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.female_week_total_weed_out+'<br>高报：'+row.female_week_total_weed_out_high_alarm+'<br>高预：'+row.female_week_total_weed_out_high_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=死淘率"+"&tabId="+variety_num + '\','+isRead+')">&nbsp;'+row.pile_female_cur_cd_rate+'&nbsp;</a>';
                }
            }
        }
    });
    // dc2.push({
    //     field: "pile_act_feed_daliy",
    //     title: "只日耗料(克/只)",
    //     width: '10%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#f1e1ff"}};
    //     },
    //     formatter: function(value,row,index){
    //         if(value==undefined){
    //             if(row.pile_act_feed_daliy_flag ==0){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //             }else if(row.pile_act_feed_daliy_flag ==1){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.bz_total_feed+'<br>高报：'+row.total_feed_high_alarm+'<br>低报：'+row.total_feed_low_alarm+'<br>高预：'+row.total_feed_high_warm+'<br>低预：'+row.total_feed_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //             }else{
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.bz_total_feed+'<br>高报：'+row.total_feed_high_alarm+'<br>低报：'+row.total_feed_low_alarm+'<br>高预：'+row.total_feed_high_warm+'<br>低预：'+row.total_feed_low_warm+'" tyle="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //             }
    //         }else{
    //             if(row.pile_act_feed_daliy_flag ==0){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：-<br>高报：-<br>低报：-<br>高预：-<br>低预：-" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //             }else if(row.pile_act_feed_daliy_flag ==1){
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.bz_total_feed+'<br>高报：'+row.total_feed_high_alarm+'<br>低报：'+row.total_feed_low_alarm+'<br>高预：'+row.total_feed_high_warm+'<br>低预：'+row.total_feed_low_warm+'" style="color: #f5a623;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //             }else{
    //                 return '<a href="#" data-toggle="tooltip" data-placement="left" data-html="true" data-original-title="标准：'+ row.bz_total_feed+'<br>高报：'+row.total_feed_high_alarm+'<br>低报：'+row.total_feed_low_alarm+'<br>高预：'+row.total_feed_high_warm+'<br>低预：'+row.total_feed_low_warm+'" style="color: #E83828;" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //             }
    //         }
    //     }
    // });

    // dc2.push({
    //     field: "avg_feed_daliy",
    //     title: "只日耗料标准",
    //     width: '6%',
    //     sortable: true,
    //     cellStyle:function(value, row, index) {
    //         return {css:{"background-color": "#f1e1ff"}};
    //     },
    //     formatter: function(value,row,index){
    //      return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=耗料"+"&tabId="+variety_num + '\','+isRead+')">'+row.avg_feed_daliy+'</a>';
    //     }
    // });
    dc2.push({
        field: "pile_water_feed_rate",
        title: "水料比",
        width: '5%',
        sortable: true,
        cellStyle:function(value, row, index) {
            return {css:{"background-color": "#f1e1ff"}};
        },
        formatter: function(value,row,index){
            if(value==undefined){
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
            }else{
                return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=饮水"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
            }
        }
    });

    // 能耗信息
    //     dc.push({
    //             title: "能耗信息",
    //             colspan: 2,
    //             align: 'center'
    //         }
    //     );
    //     dc2.push({
    //         field: "ran_liao",
    //         title: "燃料",
    //         width: '5%',
    //         sortable: true,
    //         cellStyle:function(value, row, index) {
    //             return {css:{"background-color": "#f1eaaa"}};
    //         },
    //         formatter: function(value,row,index){
    //          if(value==undefined){
    //              return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //          }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //          }
    //         }
    //     });
    //
    //     dc2.push({field: "dian_liang",
    //         title: "电量",
    //         width: '5%',
    //         sortable: true,
    //         cellStyle:function(value, row, index) {
    //             return {css:{"background-color": "#f1eaaa"}};
    //         },
    //         formatter: function(value,row,index){
    //          if(value==undefined){
    //              return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">-</a>';
    //          }else{
    //          return '<a href="#" onclick="forwardMenu(\'z802\',\'lm2\',\'se2\',\'op2\',\'多指标分析\',\'/analyze/showMultiIndexAnalysis?&user_id=' + user_id +'&org_id='+row.org_id +'&farm_id=' + row.farm_id + '&farm_type=' + row.farm_type +"&farm2_id="+row.farm_id +"&batch_no="+row.batch_no +"&pId=能耗"+"&tabId="+variety_num + '\','+isRead+')">'+value+'</a>';
    //          }
    //         }
    //     });

    var dataColumns = [dc, dc2];
    return dataColumns;
};

function loadJs() {
    var head = $("head").remove("script[role='reload']");
    $("<scri" + "pt>" + "</scr" + "ipt>").attr({ role: 'reload', src: path+'/framework/js/org.js', type: 'text/javascript' }).appendTo(head);
}

function showMessage(index,value, name){
    $("#"+value+"_"+index).tips({
        side : 4,
        msg : name,
        time : 3,
        color:'#FFF',
        bg: '#2586c4',
        x:0,
        y:0
    });

}


