/**
 * Created by Seymour on 2016/10/26.
 */
var dataList = [];

$(document).ready(function() {
    initTableWithToolBar("stock", "taskReminderToolbar", getStockTableColumns(), []);
    $("#stockTable").bootstrapTable("load", dataList);
});

function checkDate(wd) {
    var che = document.getElementsByName("week");
    if (wd.value == '1') {
        for (var i = 0; i < che.length; ++i) {
            che[i].disabled = true;
        }
    } else {
        for (var i = 0; i < che.length; ++i) {
            che[i].disabled = false;
        }
    }
};

//新增
function getStockTableColumns(){
    var dataColumns = [{
        checkbox:true,
        title: "选择",
        width: '5%'
    },{
        field: "id",
        title: "编号"
    },{
        field: "taskType",
        title: "任务类别"
    },{
        field: "task_name",
        title: "任务项"
    },{
        field: "dateType",
        title: "时间单位"
    },{
        field: "date_values",
        title: "提醒周龄"
    },{
        field: "week_group",
        title: "提醒日"
    }];
    return dataColumns;
}

function checkNum(n) {
    var temp = n.split(",");
    var flag = false;
    var msg = "";
    temp.forEach(function (c) {
        var num = Number(c);
        if (isNaN(num)) {
            msg = "您输入的时间间隔值错误，正确的格式为:1,2,3,...";
            flag = true;
            return;
        }
    });
    if (!flag) {
        var s = temp.join(",") + ",";
        for (var i = 0; i < temp.length; i++) {
            var aa = s.replace(temp[i] + ",", "");
            var bb = aa.indexOf(temp[i] + ",");
            if (bb > -1) {
                msg = "您输入的时间间隔有相同值！";
                flag = true;
                break;
            }
        }
    }
    if (!flag) {
        for (var i = 0; i < temp.length; i++) {
            if (parseInt(temp[i]) > 90) {
                msg = "您输入的时间不允许大于90！";
                flag = true;
                break;
            }
        }
    }
    if (flag) {
        // alert("您输入的时间间隔值格式错误，正确格式为:1,2,3,.....");
        layer.alert(msg, {
            skin: 'layui-layer-lan'
            , closeBtn: 0
            , shift: 4 //动画类型
        });
        return;
    }
}

function addMissionRemind() {
    layer.confirm('请确认是否添加此次任务提醒?', {
        skin: 'layui-layer-lan'
        , closeBtn: 0
        , shift: 4 //动画类型
    }, function temp() {
        var array = new Array();
        var date = $("#dateValues")[0];
        var temp = date.value.split(",");
        var numArray = new Array();
        temp.forEach(function (c) {
            var num = Number(c);
            numArray.push(c);
        });
        var type = document.getElementById("taskType").value;
        var code = document.getElementById("taskCode").value;
        var wd = document.getElementById("wd").value;
        var dateValues = document.getElementById("dateValues").value;
        var weeks = document.getElementsByName("week");
        var checkFlag = true;
        for (var i = 0; i < weeks.length; ++i) {
            if (weeks[i].checked) {
                array.push(weeks[i].value);
                checkFlag = false;
            }
        }
        if (checkFlag) {
            layer.alert('请选择提醒日期！', {
                skin: 'layui-layer-lan'
                , closeBtn: 0
                , shift: 4 //动画类型
            });
            return;
        }
        if (type == '') {
            // alert("请选择任务类别！");
            layer.alert('请选择任务类别！', {
                skin: 'layui-layer-lan'
                , closeBtn: 0
                , shift: 4 //动画类型
            });
            return;
        }
        if (code == '') {
            // alert("请选择任务项！");
            layer.alert('请选择任务项！', {
                skin: 'layui-layer-lan'
                , closeBtn: 0
                , shift: 4 //动画类型
            });
            return;
        } else {
            $.ajax({
                url: path + "/product/saveAdd",
                data: {
                    "taskType": type,
                    "taskCode": code,
                    "taskWD": wd,
                    "dateValues": dateValues,
                    "weeks": array.toString()
                },
                dataType: "json",
                success: function (result) {
                    if (result.msg == '1') {
                        // alert("保存成功!");
                        layer.msg('保存成功！');
                        var list = result.obj;
                        $("#stockTable").bootstrapTable("load", list);
                        /*$("#formData tr:not(:first-child)").remove();
                         for (var i = 0; i < list.length; ++i) {
                         var str = "<tr name='taskId'><td><input type='checkbox' value='" + list[i]["id"] + "'/></td><td><p>" + list[i]["id"]
                         + "</p></td><td><p>" + list[i]["taskType"] + "</p></td><td><p>" + list[i]["task_name"]
                         + "</p></td><td><p>" + list[i]["dateType"] + "</p></td><td><p>" + list[i]["date_values"]
                         + "</p></td></tr>";
                         $("#formData").append(str);
                         }*/
                    } else {
                        // alert("保存失败！");
                        layer.alert(result.msg, {
                            skin: 'layui-layer-lan'
                            , closeBtn: 0
                            , shift: 4 //动画类型
                        });
                        return;
                    }
                }
            });
        }
    });
}

function deleteTask() {
    var str = new Array();
    var getRows = $('#stockTable').bootstrapTable('getSelections');
    var code = document.getElementById("taskCode").value;
    for (var i in getRows) {
        str.push(getRows[i]["id"]);
    }
    if (str == []) {
        layer.alert('请选择需要删除的任务！', {
            skin: 'layui-layer-lan'
            , closeBtn: 0
            , shift: 4 //动画类型
        });
    } else {
        layer.confirm('请确认是否删除的任务！', {
                skin: 'layui-layer-lan'
                , closeBtn: 0
                , shift: 4 //动画类型
            }, function () {
                $.ajax({
                    url: path + "/product/deleteTask",
                    data: {"id": str.toString()},
                    dataType: "json",
                    success: function (result) {
                        if (result.msg == '1') {
                            // alert("删除成功！");
                            layer.msg('删除成功！');
                            var list = result.obj;
                            $("#stockTable").bootstrapTable("load", list);
                        } else {
                            // alert(result.msg);
                            layer.msg(result.msg);
                        }
                    }
                })
            }
        );
    }
}
function queryNext(){
    var taskType = document.getElementById("taskType").value;
    $.ajax({
        url:path + "/product/queryTask",
        data:{"task_type": taskType},
        dataType:"json",
        success:function (result) {
            var list = result.obj;
            if (result.msg == "1"){
                $("#taskCode option").remove();
                for (var i=0; i<list.length; ++i){
                    $("#taskCode").append("<option value=" +list[i]["task_id"]+">"+ list[i]["task_name"]+"</option>")
                }
            }
        }
    })
};

function setData(dataList){
    this.dataList = dataList;
}