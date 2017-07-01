/**
 * Created by LeLe on 10/20/2016.
 */
function initOthers(tableName, oTable){
    $('#' +tableName + 'Table').bootstrapTable('removeAll');
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new tableButton();
    oButtonInit.Init();

    var tb = $('#' + tableName + "Table");
    // tb.style = "paddingTop: -50px";
    var th = $('#' + tableName + 'Table thead');
    // for(var tmpTb in tb){
    //     var th = tb[tmpTb].tHead;
        th.css("color","#fff");
        th.css("background-color","#2586C4");
    // }
    $('#' + tableName + 'Table').bootstrapTable('refresh');
}

function initTable(tableName,dataColumns,dataJosn){
    //1.初始化Table
    var oTable = new table(tableName,null,dataColumns,dataJosn, null);
    initOthers(tableName, oTable);
};

function initTableWithToolBar(tableName,toolBar,dataColumns,dataJosn) {
    //1.初始化Table
    var oTable = new table(tableName,toolBar,dataColumns,dataJosn, null);
    initOthers(tableName, oTable);
};

function initTableWithSubTable(tableName,dataColumns,dataJosn, functionExpandRow) {
    //1.初始化Table
    var oTable = new table(tableName,null ,dataColumns,dataJosn, functionExpandRow);
    initOthers(tableName, oTable);
};

function initTableColumns(dataColumns, isAllSortable){
    if(isAllSortable == true){
//        for(var i=0;i<dataColumns.length;i++){
//        	if(i != 0){
//        		dataColumns[i].sortable = isAllSortable;	
//        	}
//        }
    	for(var dataColum in dataColumns){
    		if(typeof(dataColumns[dataColum].checkbox) !="undefined" || typeof(dataColumns[dataColum].radio) !="undefined"){
    			dataColumns[dataColum].sortable = false;
    		}else{
    			dataColumns[dataColum].sortable = true;
    		}
    	}
    }
    return dataColumns;
}

function table(tableName,toolBar,dataColumns,dataJosn, functionExpandRow) {
    dataColumns = initTableColumns(dataColumns, true);
    var oTableInit = new Object();
    var config = { // url: '',         //请求后台的URL（*）
                    // method: 'get',                      //请求方式（*）
                    // toolbar: toolBar,                //工具按钮用哪个容器
                    striped: true,                      //是否显示行间隔色
                    cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                    pagination: true,                   //是否显示分页（*）
                    sortable: true,                     //是否启用排序
                    sortOrder: "asc",                   //排序方式
                    // queryParams: oTableInit.queryParams,//传递参数（*）
                    sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
                    pageNumber:1,                       //初始化加载第一页，默认第一页
                    // pageSize: 20,                       //每页的记录行数（*）
                    // pageList: [20, 40, 60, 80],        //可供选择的每页的行数（*）
                    search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
                    strictSearch: true,
                    showColumns: false,                  //是否显示所有的列
                    showRefresh: false,                  //是否显示刷新按钮
                    minimumCountColumns: 2,             //最少允许的列数
                    clickToSelect: true,                //是否启用点击选中行
                    // height: 27,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                    uniqueId: 'id',                     //每一行的唯一标识，一般为主键列
                    showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
                    cardView: false,                    //是否显示详细视图
                    // detailView: false,                   //是否显示父子表
                    columns: dataColumns,
                    data: dataJosn,
                    clickToSelect: (functionExpandRow == null)?false:true,
                    detailView: (functionExpandRow == null)?false:true,//父子表
                    onExpandRow: functionExpandRow,
                    onClickRow: function(row,element){
                    	if(typeof(num3)!=undefined){
                    	  num3 = row.id;
                    	}
                    },
                    // onEditableSave: function (field, row, oldValue, $el) {
                    //     alert(oldValue);
                    // },
                    // onEditableSave: editableSave,
                    // function (field, row, oldValue, $el) {
                    // $.ajax({
                    //     type: "post",
                    //     url: "/Editable/Edit",
                    //     data: { strJson: JSON.stringify(row) },
                    //     success: function (data, status) {
                    //         if (status == "success") {
                    //             alert("编辑成功");
                    //         }
                    //     },
                    //     error: function () {
                    //         alert("Error");
                    //     },
                    //     complete: function () {
                    //
                    //     }
                    //
                    // });
                    // },
                    };
    if(null != toolBar) {
        config.toolbar = toolBar;
    };

    if("undefined" != typeof(pSize)){
        if(null != pSize){
            config.pageSize = pSize;                       //每页的记录行数（*）
            config.pageList = [pSize, pSize*2, pSize*3, pSize*4];        //可供选择的每页的行数（*）
        } else{
            config.pagination = false;
        }
    } else{
        var p = 20;
        config.pageSize = p;                       //每页的记录行数（*）
        config.pageList = [p, p*2, p*3, p*4];        //可供选择的每页的行数（*）
    }

    if("undefined" != typeof(editableSave) && null != editableSave){ //单元格保存时事件
        config.onEditableSave = editableSave;
    }


    //初始化Table
    oTableInit.Init = function () {
        $('#'+tableName+'Table').bootstrapTable(config);
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            // limit: params.limit,   //页面大小
            // offset: params.offset,  //页码
            // departmentname: $("#txt_search_departmentname").val(),
            // statu: $("#txt_search_statu").val()
        };
        return temp;
    };
    return oTableInit;
};

function tableDestroy(tableName){
    $("#" + tableName + "Table").bootstrapTable("destroy");
}

function tableButton() {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    };
    return oInit;
};

function addRow(tableName, dataRow){
    // var count = $('#' + tableName + 'Table').bootstrapTable('getData').length;
    // count += -10000;
    // var emptyRow = {id: count};
    $('#' + tableName + 'Table').bootstrapTable('insertRow', {index: dataRow.id, row: dataRow});
};

function delRow(tableName){
    var deleteRow;
    deleteRow = $('#' + tableName + 'Table').bootstrapTable('getSelections');
    for(var tmp in deleteRow){
        $('#' + tableName + 'Table').bootstrapTable('removeByUniqueId',deleteRow[tmp].id);
    }
}

function initTableRow(tableName, dataRow){
    var data;
    data = $('#' + tableName + 'Table').bootstrapTable('getData');
    if(data.length==0){
        addRow(tableName, dataRow);
    }
};

function loadTableData(tableName, jsonData){
    $("#" + tableName + "Table").bootstrapTable("load",jsonData);
};
