package com.nh.ifarm.device.yingtong.service.impl;


import com.nh.ifarm.device.service.impl.DeviceServiceImpl;
import com.nh.ifarm.device.yingtong.entity.YingtongQuota;
import com.nh.ifarm.device.yingtong.service.YingtongService;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Logger;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.annotation.Resource;

/**
 * 引通控制器服务类
 * Created by Raymon on 7/6/2016.
 */
@Service
public class YingtongServiceImpl extends DeviceServiceImpl implements YingtongService,Runnable {

	
    /**
     * 日志对象
     */
    static Logger mLogger =Logger.getLogger(YingtongServiceImpl.class);
    /**
     * Socket服务器
     */
    static ServerSocket socketServer = null;

    Socket socket=null;
    /**
     * 服务状态:00-未启动  01-启动中  02-正常停止  05-异常停止
     */
    String state = YingtongServiceStatusImpl.STANDYBY;
    /**
     * 端口号
     */
    int port = 9081;

    /**
     * 设备号
     */
    int deviceType=2;
    /**
     * 获取数据间隔时间(30s)
     */
    int timePerid = 30000;


    /**
     * 构造函数
     */
    public YingtongServiceImpl() {
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
     * 获取数据对象
     * @return 数据对象
     */
    public PageData getData(){
        return demoDataForYingtong();
    }

    /**
     * 更新至数据库
     */
    public void writeData(PageData pd){
       // start();
        for(Object obj : pd.values()){
            YingtongQuota rq = (YingtongQuota)obj;
            PageData tmp = yingtongQuotaToPageData(rq);
            try{
            	List<PageData> list =findDeviceCur(tmp);
//                List<PageData> list = (List<PageData>)dao.findForList("SDDeviceDataCurMapper.findById",tmp);
                if(list.size()>0) {
                    //移动数据至历史表
                    moveToHistory(tmp);
                    //更新已有数据
//                    update(tmp);
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
     * 清空数据对象
     */
    public void clearData(){

    }

    /**
     * 引通指标对象转换成数据对象
     * @param yq 引通指标对象
     * @return 数据对象
     */
    PageData yingtongQuotaToPageData(YingtongQuota yq){
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
//        	String aa="========执行start=====varro============";
//        	String bb="ccc";
//        	System.out.println(aa);
            if(state == YingtongServiceStatusImpl.RUNNING) {
                writeData(YingtongTaskServiceImpl.getPageData());
            } else {
                new Thread(this).start();
            }
            Thread.sleep(3000);
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     *
     * 开始服务
     */
    public void run() {
        try {
            if(socketServer == null || socketServer.isClosed()){
                socketServer = new ServerSocket(port);
            }

            if(socketServer == null){
                mLogger.info("启动引通设备服务失败。");
                return;
            }
            mLogger.info("启动引通设备服务成功");
            state = YingtongServiceStatusImpl.RUNNING;
            while (true)
            {
                try
                {
                    socket = socketServer.accept();
                    YingtongTaskServiceImpl ytsi = new YingtongTaskServiceImpl();
                    ytsi.setSocket(socket);
                    Thread tThread = new Thread(ytsi);
                    tThread.start();
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(socketServer != null){
                try {
                    socketServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            state = YingtongServiceStatusImpl.STOP;
        }

//
//        String aa = "========执行run====varro=============";
//        String cccccc = "ccc";
//
//        System.out.println(aa);
//        System.out.println(cccccc);
//
//        try {
//            if(socketServer == null || socketServer.isClosed()){
//                socketServer = new ServerSocket(port);
//                if(socketServer == null){
//                    mLogger.info("启动引通控制器服务失败。");
//                    return;
//                }
//                mLogger.info("启动服务 Success");
//                state = YingtongServiceStatusImpl.RUNNING;
//                socket = socketServer.accept();
//
//            } else{
//                if(state==YingtongServiceStatusImpl.RUNNING) {
////                	ytsi.setSocket(socketServer.accept());
////                	 new Thread(ytsi).start();
////                	 Thread.sleep(timePerid);
////                	System.out.println(bb);
////                	ytsi.bbbb();
//                    //从YingtongTaskServiceImpl获取数据
//                    for(Object tmp : YingtongTaskServiceImpl.getPageData().keySet()) {
//                        System.out.println((String)tmp+":"+YingtongTaskServiceImpl.getPageData().get(tmp));
//                    }
//                }
//            }
//
//            while (true) {
//                /**
//                 * 引通控制器任务服务对象
//                 */
//                YingtongTaskServiceImpl ytsi = new YingtongTaskServiceImpl();
//                ytsi.setSocket(socket);
////                new Thread(ytsi).start();
//                Thread.sleep(timePerid);
//                //YingtongTaskServiceImpl tSocketTask = new YingtongTaskServiceImpl(socket);
//                //tSocketTask.settSBYincommManager(tSBYincommManager);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
////                if (socketServer != null) {
////                    try {
////                        socketServer.close();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////                state = YingtongServiceStatusImpl.STOP;
//        }
    }

}
