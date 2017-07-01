/**
 * Created by LeLe on 11/28/2016.
 */
var deviceListTableName = "deviceList";
var sensorListTableName = "sensorList";
var obj = new Object();
// var deviceList = [{deviceCode: '1234567890', deviceFactory: '引通无线', devicePort: 'x', operate: '<button type="button" class="btn blue" onclick="delDevice()">删除</button>'}
//                     ,{deviceCode: '09876543210987654321', deviceFactory: '引通无线', devicePort: 'x', operate: '<button type="button" class="btn blue" onclick="delDevice()">删除</button>'}];
// var sensorList = [{locationId: '1', locationName: '前区1', sensorCode: '1'}
//     ,{locationId: '2', locationName: '前区2', sensorCode: ''}
//     ,{locationId: '3', locationName: '中区1', sensorCode: '3'}
//     ,{locationId: '4', locationName: '中区2', sensorCode: '5'}
//     ,{locationId: '5', locationName: '后区1', sensorCode: ''}
//     ,{locationId: '6', locationName: '后区2', sensorCode: '6'}];

var deviceList = [];
var sensorList = [];

$(document).ready(function() {
    initObj();

    initDeviceListTable();
    getDeviceListTableData();

    initSensorListTable();
    initSensorListTableEvent();
    getSensorListTableData(0);

});

//初始化数据对象
function initObj(){
    obj.farmId = $("#farmId").val();
    obj.farmName = $("#farmName").val();
    obj.houseId = $("#houseId").val();
    obj.houseName = $("#houseName").val();
    obj.houseType = $("#houseType").val();
    obj.houseTypeName = $("#houseTypeName").val();
    obj.deviceId = "";
    obj.deviceCode = "";
    obj.deviceName = "";
    obj.devicePort = "";
    obj.locationId = "";
    obj.locationName = "";
    obj.sensorCode = "";
    obj.path = $("#path").val();
    obj.sensorCurrIndex = "";
    obj.deviceCurrIndex = "";
}

//初始化设备列表
function initDeviceListTable(){
    var columns = [{
            field: "radio",
            title: "选择",
            width: '5%'
        }, {
        field: "device_code",
        title: "设备编号",
        width: '45%'
        },
            {
            field: "main_id",
            title: "设备ID",
            visible: false
        }, {
            field: "device_factory",
            title: "设备名称",
            width: '20%'
        }, {
            field: "port_id",
            title: "设备端口",
            width: '10%'
        }, {
            field: "operate",
            title: "操作",
            width: '20%'
        }];
    initTable(deviceListTableName, columns, []);
}

//获取设备列表数据
function getDeviceListTableData(){
    $.ajax({
        url : obj.path + "/farm/getDeviceList",
        data : {
            house_id : obj.houseId
        },
        type : "POST",
        dataType: "json",
        async: false,
        success : function(result) {
                deviceList = eval(result.obj);
                var checkedFlag = true;
                for(var key in deviceList){
                    deviceList[key].radio = '<input type="radio" value="' + key + '" checked="' + checkedFlag + '" onclick="getSensorListTableData(' + key + ')"/>';
                    deviceList[key].operate = '<button type="button" class="btn blue" onclick="delDevice(' + key + ')">删除</button>';
                    checkedFlag = false;
                }
                loadTableData(deviceListTableName,deviceList);
        }
    });
}

//初始化传感器列表
function initSensorListTable(){
    var columns;
    columns = [{
        field: "location_code",
        title: "位置ID",
        visible: false
    }, {
        field: "location_name",
        title: "位置",
        width: "40%"
    }, {
        field: "main_id",
        title: "设备ID",
        visible: false
    }, {
        field: "sensor_type",
        title: "传感器类型",
        visible: false
    }, {
        field: "sensor_no",
        title: "传感器编号",
        width: '60%',
        editable: {
            type: 'text',
            emptytext: "请输入传感器编号",
            mode: "inline",
            validate: function (v) {
                if (!$.trim(v)) return '传感器编号不能为空';
                setSensorLocation(obj.sensorCurrIndex, v);
            }
        }
    }];
    initTable(sensorListTableName, columns, []);
}

//获取传感器列表数据
function getSensorListTableData(key){
    obj.deviceCurrIndex = key;
    if(deviceList.length > 0 && deviceList[key].main_id != null && deviceList[key].main_id != ""){
        obj.deviceId = deviceList[key].main_id;
        obj.deviceCode = deviceList[key].device_code;
        obj.deviceName = deviceList[key].device_factory;
        obj.devicePort = deviceList[key].port_id;
        $.ajax({
            url : obj.path + "/farm/getSensorList",
            data : {
                main_id: obj.deviceId,
                device_code : obj.deviceCode
            },
            type : "POST",
            dataType: "json",
            // async: false,
            success : function(result) {
                sensorList = eval(result.obj);
                loadTableData(sensorListTableName,sensorList);
            }
        });

    } else{
        loadTableData(sensorListTableName,[]);
    }

}

//初始化传感器列表事件
function initSensorListTableEvent(){
    $('#' + sensorListTableName + 'Table').on('click-row.bs.table', function (row, $element, field) {
        obj.sensorCurrIndex = field[0].rowIndex;
    });
}

//解绑设备
function delDevice(key){
    layer.confirm('是否确认删除该设备？', {
        skin: 'layui-layer-lan'
        , closeBtn: 0
        , shift: 4 //动画类型
    }, function ok(index) {
        if(deviceList.length > 0 && deviceList[key].main_id != null && deviceList[key].main_id != ""){
            obj.deviceId = deviceList[key].main_id;
            obj.deviceCode = deviceList[key].device_code;
            obj.deviceName = deviceList[key].device_factory;
            obj.devicePort = deviceList[key].port_id;
            $.ajax({
                url : obj.path + "/farm/delDevice",
                data : {
                    main_id: obj.deviceId,
                    device_code : obj.deviceCode,
                    house_id: obj.houseId,
                    farm_id:obj.farmId
                },
                type : "POST",
                dataType: "json",
                // async: false,
                success : function(result) {
                    layer.close(index);
                    getDeviceListTableData();
                    getSensorListTableData(0);
                }
            });

        }
    });

}

//绑定设备
function mappingDevice(){
    var dCode = $("#deviceCode").val();
    if(dCode != null && dCode != "") {
        $.ajax({
            url : obj.path + "/farm/mappingDevice",
            data : {
                farm_id: obj.farmId,
                house_id: obj.houseId,
                device_code: dCode
            },
            type : "POST",
            dataType: "json",
            // async: false,
            success : function(result) {
                if(result.success){
                    // layer.msg("成功绑定设备" + dCode + "！", {
                    //     skin: 'layui-layer-lan'
                    //     , closeBtn: 0
                    //     , shift: 4 //动画类型
                    // });
                    getDeviceListTableData();
                    getSensorListTableData(0);
                } else{
                    layer.msg(result.msg + "，请重新输入！", {
                        skin: 'layui-layer-lan'
                        , closeBtn: 0
                        , shift: 4 //动画类型
                    });
                }
            }
        });
    } else{
        layer.msg("设备编号不能为空，请重新输入！", {
            skin: 'layui-layer-lan'
            , closeBtn: 0
            , shift: 4 //动画类型
        });
    }

}

//设定传感器位置
function setSensorLocation(idx, v){
    // alert(obj.sensorCurrIndex);
    if(sensorList.length > 0){
        var sensor = sensorList[idx-1];
        obj.sensor_no = v;
        obj.locationId = sensor.location_code;

        $.ajax({
            url : obj.path + "/farm/setSensorLocation",
            data : {
                farm_id: obj.farmId,
                house_id: obj.houseId,
                main_id: obj.deviceId,
                device_code: obj.deviceCode,
                sensor_no: obj.sensor_no,
                show_column: obj.locationId
            },
            type : "POST",
            dataType: "json",
            // async: false,
            success : function(result) {
                getSensorListTableData(obj.deviceCurrIndex);
            }
        });

    }
}