var houseType = 1;
var goodType = 1;

$(function () {
    $('[data-toggle="tooltip"]').tooltip();

    //页面初始化后,获取当前页面存在的tab个数,初始化样式,使其两边对其
    var size = $("#uiTab li").length;
    var isActive = 0;
    $("#uiTab li").each(function(index, element){
        if(index == isActive){
            $(element).addClass("active");
            houseType = $(element).find("a").attr("code");
        }
        $(element).attr("style","width:" + (99.6/size) + "%");
    });

    //初始化页面内容,对应tab页面按钮控制显示
    // $(".tab-content .tab-pane").each(function(index, element){
    //     if(index == isActive){
    //         $(element).addClass("active");
    //         $(element).empty();
    //         var iframe = createIframe();
    //         $(element).append(iframe);
    //     }
    // });

    //切换tab页面时做相应参数的处理。
    $("#uiTab li").on('show', function (e) {
        changeTab(null, $(e.target).attr("code"));
    })




    // if(tabShow == "3"){
    //     houseType = 3;
    // }else{
    //     houseType = 1;
    // }
    // $('#uiTab li:eq(' + 0 + ') a').tab('show');
    //初始化tab页面
    $("#tab_" + houseType).addClass("active");
    changeTab(null, houseType);

    /**
     * 绑定上传按钮上传文件的操作
     * @type {string}
     */
    var url = "";
    $('#fileupload').fileupload({
        url: url,
        autoUpload: true,
        add: function (e, data){
            var url = "";
            if((houseType%3==0? 3 : houseType%3) == 1 || (houseType%3==0? 3 : houseType%3) == 2){
                url = path + "/breed/uploadCultivateStandard?upload_file_type=3";
            }else if ((houseType%3==0? 3 : houseType%3) == 3) {
                url = path + "/breed/uploadCultivateStandard?upload_file_type=4";
            }
            $(this).fileupload('option', 'url', url);
            $('#uploadButtonFacade').show();
            $('#uploadButtonFacade').button('loading');
            $('#uploadButton').hide();
            data.submit();
        },
        done: function (e, data) {
            $('#uploadButtonFacade').button('reset');
            $('#uploadButtonFacade').hide();
            $('#uploadButton').show();
            var rt = '(' + data.result + ')';
            var json = eval(rt);
            if(json.success == true){
                layer.msg("文件导入成功");
                changeTab(null, houseType);
            } else {
                if(json.msg){
                    layer.msg(json.msg);
                }else{
                    layer.msg("请联系系统管理员!");
                }

            }
            return;
        },
        fail:function (e, data) {
            layer.msg(e);
            return;
        }
    });

});

/**
 * 添加预警及统设的行
 */
function addEditRow(){
    //转换houseType变量
    var tabNum = houseType%3==0? 3 : houseType%3;
    //获取表格对象ID
    var tableId = "breedSTD" + tabNum + "Table";
    //获取表格的表头数组
    var columns = $('#'+tableId).bootstrapTable('getOptions').columns;
    //最终获取到的表头字段field标记数组
    var columnFiledArray = getColumnFieldArray(columns);

    //获取系统内比例
    var columnData = new Object();
    for(var i = 0; i < columnFiledArray.length; i++){
        columnData[columnFiledArray[i]]="";
    }
    var json = JSON.stringify(columnData);
    $.ajax({
        url: path + "/breed/getColumnFileRateArray",
        data: {"columnFieldArray": json, "goodsTypeId": goodType, "type": houseType},
        dataType: "json",
        success: function (result) {
            var list = result.obj;
            //根据系统返回的比率列表新建行
            var tr = document.createElement("tr");
            $(tr).addClass("editrow");
            $(tr).attr("style","display:none");
            for(var i = 0; i < columnFiledArray.length; i++){
                var columnField = columnFiledArray[i];
                var td = document.createElement("td");
                if(!(columnField == "grow_week_age" || columnField =="grow_age")){
                    var rate = "";
                    for(var j = 0; j < list.length; j++){
                        var listData = list[j];
                        if(listData[columnField]){
                            rate = listData[columnField];
                        }
                    }
                    var a = document.createElement("a");
                    $(a).attr("id", houseType+columnField);
                    $(a).html(rate);
                    $(td).append(a);
                }else{
                    $(td).html("设置比例");
                }
                $(tr).append(td);
            }
            $('#'+tableId).prepend(tr);
            //完成事件绑定
            methodArray = [];
            for(var i = 0; i < columnFiledArray.length; i++){
                var columnField = columnFiledArray[i];
                var method = $('#'+houseType+columnField).editable({
                    type: "text",          //编辑框的类型。
                    title: "设置比例",      //编辑框的标题
                    disabled: false,       //是否禁用编辑
                    emptytext: "设置比例",  //空值的默认文本
                    mode: "inline",         //编辑框的模式：
                    validate: function (value) {//字段验证
                        if (!$.trim(value)) {
                            return '不能为空';
                        }
                        $(this).html(value);
                        saveSetting();
                    },
                    onblur: 'submit'
                });
                methodArray[i] = method;
            }
            $('.editrow').fadeIn(1000);
        }
    });
}



/**
 * 获取编辑行,返回jQuery对象
 */
function getEditRow(){
    //转换houseType变量
    var tabNum = houseType%3==0? 3 : houseType%3;
    //获取表格对象ID
    var tableId = "breedSTD" + tabNum + "Table";
    return $('.editrow');
}

/**
 * 最终获取到的表头字段field标记数组
 * @param columns
 */
function getColumnFieldArray(columns){
    //最终获取到的表头字段field标记数组
    var columnFiledArray = [];
    for(var i = 0; i < columns.length; i++){
        var columnArray = columns[i];
        for(var j = 0; j < columnArray.length; j++){
            var column = columnArray[j];
            if(column.field){
                //找到数组中第一个为空的元素后更换添加,如果没有,则直接添加
                var index = arrayHasNull(columnFiledArray);
                if(index && i!=0){
                    columnFiledArray[index] = column.field;
                }else{
                    columnFiledArray.push(column.field);
                }
            }else{
                if(column.colspan){
                    var colspan = column.colspan;
                    for(var k = 0; k < colspan; k++){
                        columnFiledArray.push(null);
                    }
                }

            }
        }
    }
    return columnFiledArray;
}
/**
 * 找到数组中第一个为空的元素后更换添加,如果没有,则直接添加
 * @param array
 * @returns {*}
 */
function arrayHasNull(array){
    for(var i = 0; i < array.length; i++){
        if(!array[i]){
            return i;
        }
    }
    return null;
}

/**
 * 切换页面出发的方法
 * @param element
 * @param ht
 */
function changeTab(element, ht){
    //切换tab页面
    // $(element).tab('show');
    //设置tab页面索引
    houseType = ht;
    //设置按钮切换样式
    // if(element != null){
    //     var li = $(element).parent();
    //     li.siblings().each(function(index, element){
    //         if($(element).children()){
    //             $(element).removeClass("active");
    //         }
    //     });
    //     li.addClass("active");
    // }

    //设置按钮样式设置及显示设置
    // var showClass = "span6 alert";
    // var showWords = "育成标准";
    // if(element != null){
    //     showWords = $(element).html();
    // }
    //
    // var settingButtons = $(".button-toolbar .btn-warning,.btn-danger,.btn-success, .btn-default");
    // if(ht == 1 || ht == 2 || ht == 3){
    //     settingButtons.each(function(index, element){
    //         $(element).hide();
    //     });
    //     $(".button-toolbar .btn-default").show();
    //     showClass = showClass + " " + "alert-info";
    // }else if(ht == 4 || ht == 5 || ht == 6){
    //     settingButtons.each(function(index, element){
    //         $(element).hide();
    //     });
    //     $(".button-toolbar .btn-warning").show();
    //     showClass = showClass;
    // }else if((ht == 7 || ht == 8 || ht == 9)){
    //     settingButtons.each(function(index, element){
    //         $(element).hide();
    //     });
    //     $(".button-toolbar .btn-danger").show();
    //     showClass = showClass + " " + "alert-error";
    // }
    // var alertHis = $("#alertTab");
    // if(alertHis){
    //     $(alertHis).remove();
    // }
    // var alert = createAlert(showClass, showWords);
    // $("#addAlert").after(alert);

    if(ht == 1 || ht == 2 || ht == 3){
        //转换houseType变量
        var tabNum = houseType%3==0? 3 : houseType%3;
        var show = false;
        //处理品种类型
        $('select option').each(function(index, element){
            $(element).hide();
            $(element).attr("selected", false);
            var code = $(element).attr("code");
            if(tabNum == code){
                $(element).show();
                if(!show){
                    $(element).attr("selected", true);
                    show = true;
                }

            }
        });
    }



    //刷新数据
    searchData();
}

function changeType(){
    // 转换houseType变量
    // var tabNum = houseType%3==0? 3 : houseType%3;
    // //处理品种类型
    // $('select option').each(function(index, element){
    //     $(element).hide();
    //     var code = $(element).attr("code");
    //     if(tabNum == code){
    //         $(element).show();
    //     }
    // });
    //刷新数据
    searchData();
}

/**
 * 创建一个可以关闭的提示框
 * @param showClass
 * @param showWords
 */
// function createAlert(showClass, showWords){
//     var $div = $(document.createElement("div"));
//     $div.addClass(showClass);
//     $div.attr("id", "alertTab");
//     $div.attr("style", "margin-bottom:0px;");
//     var $button = $(document.createElement("button"));
//     $button.attr("type", "button");
//     $button.attr("class", "close");
//     $button.attr("data-dismiss", "alert");
//     var $span = $(document.createElement("span"));
//     $span.html("当前显示:" +showWords + "数据。");
//     $div.append($button);
//     $div.append($span);
//     return $div;
// }


//点击设置预警或报警统设
//type:1设置预警; type:2设置报警
function setting(element, type){
    $(".button-toolbar .btn-success").show();
    $(element).hide();
    addEditRow();
}

/**
 * 保存对预警报警统设的设置
 */
function saveSetting(element){
    // $(element).button('loading');
    var $row = getEditRow();
    var columnJsonData = getColumnJsonData($row);
    //发起请求
    $.ajax({
        url: path + "/breed/settingWarnAndAlarmRate",
        data: {"rateData": columnJsonData, "goodsTypeId": goodType, "type": houseType},
        dataType: "json",
        success: function (result) {
            if(result.success == true){
                layer.msg("设置保存成功,正在刷新数据!");
                changeTab(null, houseType);
            } else {
                if(result.msg){
                    layer.msg(json.msg);
                }else{
                    layer.msg("请联系系统管理员!");
                }
            }
            $(element).button('reset');
        }
    });
    // $row.remove();
}

function getColumnJsonData($row){
    var columnData = new Object();
    $row.find('a').each(function(index, element){
        var key = $(element).attr("id").replace(houseType, "");
        var value = $(element).html();
        columnData[key]=value;
    });
    var json = JSON.stringify(columnData);
    return json;
}

function downloadStandardTemplate(){
    if(houseType==1 || houseType == 2){
        location.href='downloadStandardTemplate';
    }else if(houseType == 3){
        location.href='downloadStandardMeatTemplate';
    }
}

// 创建育成标准的表格列
function getTableColumns(houseType){
    var dataColumns = [];
    if (houseType == 1 || houseType == 4 || houseType == 7){
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
    if (houseType == 2 || houseType == 5 || houseType == 8){
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
    if (houseType == 3 || houseType == 6 || houseType == 9){
        dataColumns = [{
            field: "grow_age",
            title: "生长<br>日龄",
            width: "5%",
        }, {
            title: "母鸡死淘率%",
        }, {
            title: "公鸡死淘率%",
        }, {
            title: "均重",
        }, {
            title: "日采食量",
        }, {
            title: "累计饲料消耗量",
        }];
    }

    return dataColumns;
}


function searchData(){
    goodType = document.getElementById("good_type").value;
    //转换houseType变量
    var tabNum = houseType%3==0? 3 : houseType%3;
    // alert(goodTypeId);
    $.ajax({
        url: path + "/breed/searchBreedStandard",
        data: {"goodTypeId": goodType, "houseTypeId": houseType},
        dataType: "json",
        success: function (result) {
            var list = result.obj;
            var tableName = "breedSTD" + tabNum;
            initTable(tableName, getTableColumns(tabNum), []);
            if(houseType > 3){
                //预警报警数据,实现自己的load方法,以方便设置鼠标悬停提示事件
                loadData(list);
            }else{
                if(null != list && list.length>0){
                    loadTableData(tableName, list);
                }
            }
            
        }
    });

}

function loadData(list){
    //转换houseType变量
    var tabNum = houseType%3==0? 3 : houseType%3;
    //获取表格对象ID
    var tableId = "breedSTD" + tabNum + "Table";
    //删除表内所有数据
    $('#' + tableId).children('tbody').empty();
    //获取表格的表头数组
    var columns = $('#'+tableId).bootstrapTable('getOptions').columns;
    //最终获取到的表头字段field标记数组
    var columnFiledArray = getColumnFieldArray(columns);
    //获取当前加载的是预警还是报警数据
    var dataType = getDataType();
    for(var i = 0; i < list.length; i++){
        var listData = list[i];
        var tr = document.createElement("tr");
        $(tr).addClass("data");
        for(var j = 0; j < columnFiledArray.length; j++){
            var columnField = columnFiledArray[j];
            var data = "";
            if(columnField == "grow_week_age" || columnField == "grow_age"){
                data = listData[columnField];
            }else{
                data = listData[columnField + dataType];
            }

            var stdData = listData[columnField];
            var td = document.createElement("td");
            var a = document.createElement("a");
            $(a).attr("id", columnField + dataType);
            if(!(columnField == "grow_week_age" || columnField == "grow_age")){
                $(a).attr("data-toggle", "tooltip");
                $(a).attr("title", stdData);
                $(a).html(data);
                $(td).append(a);
            }else{
                $(td).html(data);
            }
            $(tr).append(td);
        }
        $('#'+tableId).append(tr);
    }
    addEditRow();

    //完成事件绑定
    $('[data-toggle="tooltip"]').tooltip();

}
/**
 * 获取当前加载的是报警还是预警的数据
 */
function getDataType(){
    var dataType = "";
    if(houseType == 4 || houseType == 5 || houseType == 6){
        dataType = "_high_warm";
    }else if(houseType == 7 || houseType == 8 || houseType == 9){
        dataType = "_high_alarm";
    }
    return dataType;
}



