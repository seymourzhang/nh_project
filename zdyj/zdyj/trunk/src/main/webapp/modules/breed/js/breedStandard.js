var houseType = 1;
var goodType = 1;
var pSize = null;

function init(house_type, good_type){
    if(null != house_type && "" != house_type){
        houseType = house_type;
    }

    if(null != good_type && "" != good_type){
        goodType = good_type;
    }

    initSelectTab(houseType);
}

function setHouseType(ht){
    houseType = ht;
    searchData();
}

// 创建育成标准的表格列
function getTableColumns(houseType){
    var dataColumns = [];
    if (houseType == 1){
        dataColumns = [{
            field: "grow_week_age",
            title: "生长<br>周龄",
            width: "5%",
        }, {
            title: "母鸡死淘率%",
        }, {
            field: "female_life",
            title: "母鸡成活率%",
        }, {
            title: "平均体重（克）",
        }, {
            title: "饲料消耗（克/只/日）",
        }, {
            field: "evenness",
            title: "均匀度%",
        }, {
            title: "母鸡体重范围",
        }];
    }
    if (houseType == 2){
        dataColumns = [{
            field: "grow_week_age",
            title: "生长<br>周龄",
            width: "5%",
        }, {
            title: "母鸡死淘率%",
        }, {
            field: "female_life",
            title: "母鸡<br>成活率%",
        }, {
            title: "平均体重（克）",
        }, {
            title: "产蛋率%",
        }, {
            title: "每只入舍母鸡<br>产蛋数（枚）",
        }, {
            field: "qualified_egg_rate",
            title: "合格<br>种蛋率%",
        }, {
            title: "每只入舍母鸡<br>产合格种蛋数（枚）",
        }, {
            field: "chick_hatching_rate",
            title: "雏鸡<br>孵化率%",
        }, {
            field: "breeding_chick_hatching",
            title: "种雏<br>孵化率%",
        }, {
            title: "种雏数（只）",
        }, {
            title: "饲料消耗（克/只/日）",
        }];
    }

    return dataColumns;
}


function searchData(){
    goodType = document.getElementById("good_type").value;
    // alert(goodTypeId);
    $.ajax({
        url: path + "/breed/searchBreedStandard",
        data: {"goodTypeId": goodType, "houseTypeId": houseType},
        dataType: "json",
        success: function (result) {
            var list = result.obj;
            var tableName = "breedSTD" + houseType;
            initTable(tableName, getTableColumns(houseType), []);
            if(null != list && list.length>0){
                loadTableData(tableName, list);
            }
        }
    });

}

function initSelectTab(tabId){
    $('#uiTab li:eq(' + (tabId-1) + ') a').tab('show');
    setHouseType(tabId);
}
