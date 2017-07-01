package com.nh.ifarm.device.wirelessyt.impl;


import com.nh.ifarm.device.service.impl.DeviceServiceImpl;
import com.nh.ifarm.device.wirelessyt.WirelessYTService;
import com.nh.ifarm.device.wirelessyt.common.WirelessYTConstants;
import com.nh.ifarm.device.wirelessyt.entity.WirelessYTDevice;
import com.nh.ifarm.device.wirelessyt.entity.WirelessYTQuota;
import com.nh.ifarm.device.wirelessyt.task.WirelessYTTask;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Logger;
import com.nh.ifarm.util.common.PageData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * 引通无线采集器服务类
 * Created by GX on 11/22/2016.
 */
@Service
public class WirelessYTServiceImpl extends DeviceServiceImpl implements WirelessYTService,Runnable {
    /**
     * 日志对象
     */
    private static Logger mLogger =Logger.getLogger(WirelessYTServiceImpl.class);
    /**
     * Socket服务器
     */
    private static ServerSocket socketServer = null;

    private Socket socket=null;
    /**
     * 服务状态:00-未启动  01-启动中  02-正常停止
     */
    private String state = WirelessYTConstants.STOP_02;
    /**
     * 构造函数
     */
    public WirelessYTServiceImpl() {
    }
    /**
     * 获取服务状态
     * @return 服务状态
     */
    public String getServiceState() {
        return state;
    }
    /**
     * 设置服务状态
     * @param state 服务状态
     */
    public void setServiceState(String state) {
        this.state = state;
    }
    /**
     *
     * 开始服务
     */
    public void run() {
        try {
            if (socketServer == null || socketServer.isClosed()) {
                socketServer = new ServerSocket(WirelessYTConstants.PORTNO);
            }
            if (socketServer == null) {
                mLogger.info("启动引通(YINComm)设备服务失败。");
                return;
            }
            mLogger.info("启动引通(YINComm)设备服务成功。端口号：" + WirelessYTConstants.PORTNO);
            state = WirelessYTConstants.RUN_01;
            while (true) {
                try {
                    socket = socketServer.accept();
                    WirelessYTTask tWirelessYTTask = new WirelessYTTask();
                    tWirelessYTTask.setSocket(socket);
                    tWirelessYTTask.settWirelessYTServiceImpl(this);
                    Thread tThread = new Thread(tWirelessYTTask);
                    tThread.start();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socketServer != null) {
                try {
                    socketServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            state = WirelessYTConstants.STOP_02;
        }
    }


    /**
     * @param yq 引通设备采集器数据
     * @return 数据对象
     */
    private PageData WirelessYTDeviceToPageData(WirelessYTDevice yq){
        PageData tmp = new PageData();
        try {
            tmp.put("main_id", WirelessYTConstants.DEVICE_TYPE + "-" + yq.getCid() + "-x");
            tmp.put("device_code", yq.getCid());
            tmp.put("device_type",WirelessYTConstants.DEVICE_TYPE);
            tmp.put("device_factory", "无线农汇通");
            tmp.put("port_id", "x");
            tmp.put("power_status", yq.getPower_status());
            tmp.put("voltage", yq.getVoltage());
            tmp.put("run_time", yq.getRun_time());
            tmp.put("hardware_version", yq.getHardware_version());
            tmp.put("software_version", yq.getSoftware_version());
            tmp.put("signal_strength", yq.getSignalStrength());
            tmp.put("device_property", yq.getDevice_attr());

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return tmp;
    }

    /**
     * @param yq 引通设备传感器数据
     * @return 数据对象
     */
    private PageData wirelessYTQuotaToPageData(WirelessYTQuota yq){
        PageData tmp = new PageData();
        try {
            tmp.put(yq.ID.getCode(), yq.ID.getValue());
            tmp.put(yq.DEVICE_ID.getCode(), yq.DEVICE_ID.getValue());
            tmp.put(yq.PORT_ID.getCode(), yq.PORT_ID.getValue());
            tmp.put(yq.DEVICE_TYPE.getCode(), yq.DEVICE_TYPE.getValue());
            tmp.put(yq.ALARM_CODE.getCode(), yq.ALARM_CODE.getValue());
            tmp.put(yq.SET_TEMP.getCode(), yq.SET_TEMP.getValue());
            tmp.put(yq.OUTSIDE_TEMP.getCode(), yq.OUTSIDE_TEMP.getValue());
            tmp.put(yq.AVG_TEMP.getCode(), yq.AVG_TEMP.getValue());
            tmp.put(yq.INSIDE_TEMP1.getCode(), yq.INSIDE_TEMP1.getValue());
            tmp.put(yq.INSIDE_TEMP2.getCode(), yq.INSIDE_TEMP2.getValue());
            tmp.put(yq.INSIDE_TEMP3.getCode(), yq.INSIDE_TEMP3.getValue());
            tmp.put(yq.INSIDE_TEMP4.getCode(), yq.INSIDE_TEMP4.getValue());
            tmp.put(yq.INSIDE_TEMP5.getCode(), yq.INSIDE_TEMP5.getValue());
            tmp.put(yq.INSIDE_TEMP6.getCode(), yq.INSIDE_TEMP6.getValue());
            tmp.put(yq.INSIDE_TEMP7.getCode(), yq.INSIDE_TEMP7.getValue());
            tmp.put(yq.INSIDE_TEMP8.getCode(), yq.INSIDE_TEMP8.getValue());
            tmp.put(yq.INSIDE_TEMP9.getCode(), yq.INSIDE_TEMP9.getValue());
            tmp.put(yq.LUX.getCode(), yq.LUX.getValue());
            tmp.put(yq.STATUS.getCode(), yq.STATUS.getValue());
            tmp.put(yq.WATER_CONSUMPTION.getCode(), yq.WATER_CONSUMPTION.getValue());
            tmp.put(yq.HUMIDITY.getCode(), yq.HUMIDITY.getValue());
            tmp.put(yq.CO2.getCode(), yq.CO2.getValue());
            tmp.put(yq.NEGATIVE_PRESSURE.getCode(), yq.NEGATIVE_PRESSURE.getValue());
            tmp.put(yq.LARGER_FAN_1.getCode(), yq.LARGER_FAN_1.getValue());
            tmp.put(yq.LARGER_FAN_2.getCode(), yq.LARGER_FAN_2.getValue());
            tmp.put(yq.LARGER_FAN_3.getCode(), yq.LARGER_FAN_3.getValue());
            tmp.put(yq.SMALL_FAN_1.getCode(), yq.SMALL_FAN_1.getValue());
            tmp.put(yq.SMALL_FAN_2.getCode(), yq.SMALL_FAN_2.getValue());
            tmp.put(yq.SMALL_FAN_3.getCode(), yq.SMALL_FAN_3.getValue());
            tmp.put(yq.FENESTELLA.getCode(), yq.FENESTELLA.getValue());
            tmp.put(yq.HEATING_STATE.getCode(), yq.HEATING_STATE.getValue());
            tmp.put(yq.REFRIGERATION_STATE.getCode(), yq.REFRIGERATION_STATE.getValue());
            tmp.put(yq.SKATEBOARD.getCode(), yq.SKATEBOARD.getValue());
            tmp.put(yq.COLLECT_DATETIME.getCode(),Const.getSDF().parse(yq.COLLECT_DATETIME.getValue()));
            tmp.put(yq.SOURCE_CODE.getCode(), yq.SOURCE_CODE.getValue());
            tmp.put("signal_strength",yq.getSignalStrength());
            Date YT_DATETIME = null ;
            try {
                YT_DATETIME = new Date(Long.parseLong(yq.YT_DATETIME.getValue())*1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            tmp.put(yq.YT_DATETIME.getCode(),YT_DATETIME);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return tmp;
    }
    /**
     * 开始服务
     */
    public void start() {
        try {
            if(state.equals(WirelessYTConstants.RUN_01)) {
                // 更新引通主数据(采集器)
                writeMainData(WirelessYTTask.getRtPdMain());
                // 更新引通明细数据(传感器)
                writeDetailData(WirelessYTTask.getRtPdDetail());
            } else {
                new Thread(this).start();
            }
            Thread.sleep(3000);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新传感器明细数据至数据库
     */
    private void writeDetailData(PageData pd){
        for(Object obj : pd.values()){
            WirelessYTQuota rq = (WirelessYTQuota)obj;
            long diff = System.currentTimeMillis() - rq.getMaketime();
            if(diff/1000 > 180){
                continue; //如果数据是180秒之前的，则不需要更新
            }
            PageData tmp = wirelessYTQuotaToPageData(rq);
            try{
                List<PageData> list =findDeviceCur(tmp);
                if(list.size()>0) {
                    //移动数据至历史表
                    moveToHistory(tmp);
                    updateYT(tmp);
                } else{
                    //插入新数据
                    insert(tmp);
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * 更新采集器主数据至数据库
     */
    private void writeMainData(PageData pd){
        for(Object obj : pd.values()){
            WirelessYTDevice rq = (WirelessYTDevice)obj;
            long diff = System.currentTimeMillis() - rq.getMakeTime() ;
            if(diff/1000 > 180){
                continue; //如果数据是180秒之前的，则不需要更新
            }
            PageData tmp = WirelessYTDeviceToPageData(rq);
            try{
                List<PageData> list =findDeviceByMainId(tmp);
                if(list.size()>0) {
                    updateDeviceData(tmp);
                } else{
                    //插入新数据
                    insertDevice(tmp);
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
