package com.nh.ifarm.device.entity;

import com.nh.ifarm.util.common.PageData;

import java.util.ArrayList;
import java.util.List;
//import java.util.function.ObjLongConsumer;

/**
 * 控制器指标类
 * Created by Raymon on 7/6/2016.
 */
public class DeviceQuota {

    public DeviceQuotaType ID = new DeviceQuotaType("id","序号ID") ;
    public DeviceQuotaType DEVICE_ID = new DeviceQuotaType("device_id","设备ID号") ;
    public DeviceQuotaType PORT_ID = new DeviceQuotaType("port_id","接口ID号");
    public DeviceQuotaType DEVICE_TYPE = new DeviceQuotaType("device_type","设备类型");
    public DeviceQuotaType ALARM_CODE = new DeviceQuotaType("alarm_code","报警编码","3001");
    public DeviceQuotaType SET_TEMP = new DeviceQuotaType("set_temp","目标温度","3002");
    public DeviceQuotaType OUTSIDE_TEMP = new DeviceQuotaType("outside_temp","室外温度");
    public DeviceQuotaType AVG_TEMP = new DeviceQuotaType("avg_temp","平均温度","3003");
    public DeviceQuotaType INSIDE_TEMP1 = new DeviceQuotaType("inside_temp1","温度1","3004");
    public DeviceQuotaType INSIDE_TEMP2 = new DeviceQuotaType("inside_temp2","温度2","3005");
    public DeviceQuotaType INSIDE_TEMP3 = new DeviceQuotaType("inside_temp3","温度3","3006");
    public DeviceQuotaType INSIDE_TEMP4 = new DeviceQuotaType("inside_temp4","温度4","3007");

    public DeviceQuotaType INSIDE_TEMP5 = new DeviceQuotaType("inside_temp5","温度5");
    public DeviceQuotaType INSIDE_TEMP6 = new DeviceQuotaType("inside_temp6","温度6");
    public DeviceQuotaType INSIDE_TEMP7 = new DeviceQuotaType("inside_temp7","温度7");
    public DeviceQuotaType INSIDE_TEMP8 = new DeviceQuotaType("inside_temp8","温度8");
    public DeviceQuotaType INSIDE_TEMP9 = new DeviceQuotaType("inside_temp9","温度9");
    public DeviceQuotaType LUX = new DeviceQuotaType("lux","光照1");
    public DeviceQuotaType STATUS = new DeviceQuotaType("status","状态");
    public DeviceQuotaType YT_DATETIME = new DeviceQuotaType("yt_datetime","原始数据时间");

    public DeviceQuotaType HUMIDITY = new DeviceQuotaType("humidity","湿度","3033");
    public DeviceQuotaType WATER_CONSUMPTION = new DeviceQuotaType("water_consumption","耗水","3026");
    public DeviceQuotaType CO2 = new DeviceQuotaType("co2","二氧化碳");
    public DeviceQuotaType NEGATIVE_PRESSURE = new DeviceQuotaType("negative_pressure","负压");
    public DeviceQuotaType LARGER_FAN_1 = new DeviceQuotaType("larger_fan_1","大风机1","3011");
    public DeviceQuotaType LARGER_FAN_2 = new DeviceQuotaType("larger_fan_2","大风机2","3012");
    public DeviceQuotaType LARGER_FAN_3 = new DeviceQuotaType("larger_fan_3","大风机3","3013");
    public DeviceQuotaType SMALL_FAN_1 = new DeviceQuotaType("small_fan_1","小风机1");
    public DeviceQuotaType SMALL_FAN_2 = new DeviceQuotaType("small_fan_2","小风机2");
    public DeviceQuotaType SMALL_FAN_3 = new DeviceQuotaType("small_fan_3","小风机3");
    public DeviceQuotaType FENESTELLA = new DeviceQuotaType("fenestella","小窗");
    public DeviceQuotaType HEATING_STATE = new DeviceQuotaType("heating","加热状态");
    public DeviceQuotaType REFRIGERATION_STATE = new DeviceQuotaType("refrigeration","制冷状态","3010");
    public DeviceQuotaType SKATEBOARD = new DeviceQuotaType("skateboard","幕帘");
    public DeviceQuotaType COLLECT_DATETIME = new DeviceQuotaType("collect_datetime","时间","3032");
    public DeviceQuotaType SOURCE_CODE = new DeviceQuotaType("source_code","原码值");
    public DeviceQuotaType APP_KEY = new DeviceQuotaType("app_key","验证值");
    public DeviceQuotaType ALARM_STATE = new DeviceQuotaType("alarm_state","报警状态","3009");
    public DeviceQuotaType DEVICE_VERSION = new DeviceQuotaType("device_version","设备版本","4047");
    public DeviceQuotaType DAY_FEED_WEIGHT = new DeviceQuotaType("day_feed_weight","日饲料量","3027");

    protected List<DeviceQuotaType> list = new ArrayList<>();

    /**
     * 构造函数
     */
    public DeviceQuota(){
        list.add(ID);
        list.add(DEVICE_ID);
        list.add(PORT_ID);
        list.add(DEVICE_TYPE);
        list.add(ALARM_CODE);
        list.add(SET_TEMP);
        list.add(OUTSIDE_TEMP);
        list.add(AVG_TEMP);
        list.add(INSIDE_TEMP1);
        list.add(INSIDE_TEMP2);
        list.add(INSIDE_TEMP3);
        list.add(INSIDE_TEMP4);

        list.add(INSIDE_TEMP5);
        list.add(INSIDE_TEMP6);
        list.add(INSIDE_TEMP7);
        list.add(INSIDE_TEMP8);
        list.add(INSIDE_TEMP9);
        list.add(LUX);
        list.add(STATUS);
        list.add(YT_DATETIME);

        list.add(HUMIDITY);
        list.add(WATER_CONSUMPTION);
        list.add(CO2);
        list.add(NEGATIVE_PRESSURE);
        list.add(LARGER_FAN_1);
        list.add(LARGER_FAN_2);
        list.add(LARGER_FAN_3);
        list.add(FENESTELLA);
        list.add(HEATING_STATE);
        list.add(REFRIGERATION_STATE);
        list.add(SKATEBOARD);
        list.add(COLLECT_DATETIME);
        list.add(SOURCE_CODE);
        list.add(APP_KEY);
        list.add(ALARM_STATE);
        list.add(DEVICE_VERSION);
        list.add(DAY_FEED_WEIGHT);
    }

    /**
     * 获取设备指标列表
     * @return
     */
    public List<DeviceQuotaType> getList(){
        return list;
    }

    /**
     * 获取数据对象
     * @return 数据对象
     */
    public PageData getPageData(){
       PageData tmp = new PageData();
       for(Object obj : list.toArray()) {
           DeviceQuotaType dqt = (DeviceQuotaType) obj;
            tmp.put(dqt.getCode(), dqt.getValue());
       }
       return tmp;
    }

    /**
     * 设置设备关键键
     */
    public void setDeviceKeyId() {
        ID.setValue(DEVICE_TYPE.getValue()+"-"+DEVICE_ID.getValue()+"-"+PORT_ID.getValue());
    }

}
