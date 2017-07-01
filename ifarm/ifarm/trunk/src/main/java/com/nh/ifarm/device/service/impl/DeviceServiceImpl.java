package com.nh.ifarm.device.service.impl;

import com.nh.ifarm.device.dmr.entity.DmrQuota;
import com.nh.ifarm.device.rotem.entity.RotemQuota;
import com.nh.ifarm.device.service.DeviceService;
import com.nh.ifarm.device.yingtong.entity.YingtongQuota;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by LeLe on 7/7/2016.
 */
@Service
public abstract class DeviceServiceImpl implements DeviceService {

    @Resource(name = "daoSupport")
    protected DaoSupport dao;

    /**
     * 启动服务
     */
    public abstract void start();

    /**
     * 演示数据对象（Rotem）
     * @return 数据对象
     */
    protected PageData demoDataForRotem(){
        PageData pd = new PageData();
        RotemQuota quota =new RotemQuota() ;
        quota.DEVICE_ID.setValue(Integer.toString(getRandom(1,2)));
        quota.PORT_ID.setValue(Integer.toString(0));
        quota.DEVICE_TYPE.setValue(Integer.toString(1));
        quota.ALARM_CODE.setValue(Integer.toString(getRandom(1,4)));
        quota.SET_TEMP.setValue(Double.toString(getRandom(15,30,2)));
        quota.OUTSIDE_TEMP.setValue(Double.toString(getRandom(-40,40,2)));
        quota.AVG_TEMP.setValue(Double.toString(getRandom(0,40,2)));
        quota.INSIDE_TEMP1.setValue(Double.toString(getRandom(5,40,2)));
        quota.INSIDE_TEMP2.setValue(Double.toString(getRandom(10,40,2)));
        quota.INSIDE_TEMP3.setValue(Double.toString(getRandom(15,50,2)));
        quota.INSIDE_TEMP4.setValue(Double.toString(getRandom(15,30,2)));
        quota.WATER_CONSUMPTION.setValue(Integer.toString(getRandom(1,100)));
        quota.HUMIDITY.setValue(Double.toString(getRandom(40,70,2)));
        quota.CO2.setValue(Double.toString(getRandom(20,70,2)));
        quota.NEGATIVE_PRESSURE.setValue(Double.toString(getRandom(1,40,2)));
        quota.LARGER_FAN_1.setValue(Integer.toString(getRandom(0,1)));
        quota.LARGER_FAN_2.setValue(Integer.toString(getRandom(0,1)));
        quota.LARGER_FAN_3.setValue(Integer.toString(getRandom(0,1)));
        quota.SMALL_FAN_1.setValue(Integer.toString(getRandom(0,1)));
        quota.SMALL_FAN_2.setValue(Integer.toString(getRandom(0,1)));
        quota.SMALL_FAN_3.setValue(Integer.toString(getRandom(0,1)));
        quota.FENESTELLA.setValue(Integer.toString(getRandom(0,1)));
        quota.HEATING_STATE.setValue(Integer.toString(getRandom(0,1)));
        quota.REFRIGERATION_STATE.setValue(Integer.toString(getRandom(0,1)));
        quota.SKATEBOARD.setValue(Integer.toString(getRandom(0,1)));
        quota.SOURCE_CODE.setValue(Integer.toString(getRandom(1,100)*10000000));
        quota.COLLECT_DATETIME.setValue((Const.getSDF().format(new Date())).toString());
        quota.setDeviceKeyId();
        pd.put(quota.ID.getValue(),quota);
        return pd;
    }

    /**
     * 演示数据对象（引通）
     * @return 数据对象
     */
    protected PageData demoDataForYingtong(){
        PageData pd = new PageData();
        YingtongQuota quota =new YingtongQuota() ;
        quota.DEVICE_ID.setValue(Integer.toString(2));
        quota.PORT_ID.setValue(Integer.toString(getRandom(1,2)));
        quota.DEVICE_TYPE.setValue(Integer.toString(2));
        quota.CO2.setValue(Double.toString(getRandom(20,70,2)));
        quota.NEGATIVE_PRESSURE.setValue(Double.toString(getRandom(1,40,2)));
        quota.SOURCE_CODE.setValue(Integer.toString(getRandom(1,100)*10000000));
        quota.COLLECT_DATETIME.setValue((Const.getSDF().format(new Date())).toString());
        quota.setDeviceKeyId();
        pd.put(quota.ID.getValue(),quota);
        return pd;
    }


    /**
     * 演示数据对象（大牧人）
     * @return 数据对象
     */
    protected PageData demoDataForDmr(){
        PageData pd = new PageData();
        DmrQuota quota =new DmrQuota() ;
        quota.DEVICE_ID.setValue(Integer.toString(getRandom(1,2)));
        quota.PORT_ID.setValue(Integer.toString(0));
        quota.DEVICE_TYPE.setValue(Integer.toString(3));
        quota.ALARM_CODE.setValue(Integer.toString(getRandom(1,4)));
        quota.SET_TEMP.setValue(Double.toString(getRandom(15,30,2)));
        quota.OUTSIDE_TEMP.setValue(Double.toString(getRandom(-40,40,2)));
        quota.AVG_TEMP.setValue(Double.toString(getRandom(0,40,2)));
        quota.INSIDE_TEMP1.setValue(Double.toString(getRandom(5,40,2)));
        quota.INSIDE_TEMP2.setValue(Double.toString(getRandom(10,40,2)));
        quota.INSIDE_TEMP3.setValue(Double.toString(getRandom(15,50,2)));
        quota.INSIDE_TEMP4.setValue(Double.toString(getRandom(15,30,2)));
        quota.WATER_CONSUMPTION.setValue(Integer.toString(getRandom(1,100)));
        quota.HUMIDITY.setValue(Double.toString(getRandom(40,70,2)));
        quota.LARGER_FAN_1.setValue(Integer.toString(getRandom(0,1)));
        quota.LARGER_FAN_2.setValue(Integer.toString(getRandom(0,1)));
        quota.LARGER_FAN_3.setValue(Integer.toString(getRandom(0,1)));
        quota.SMALL_FAN_1.setValue(Integer.toString(getRandom(0,1)));
        quota.SMALL_FAN_2.setValue(Integer.toString(getRandom(0,1)));
        quota.SMALL_FAN_3.setValue(Integer.toString(getRandom(0,1)));
        quota.FENESTELLA.setValue(Integer.toString(getRandom(0,1)));
        quota.HEATING_STATE.setValue(Integer.toString(getRandom(0,1)));
        quota.REFRIGERATION_STATE.setValue(Integer.toString(getRandom(0,1)));
        quota.SKATEBOARD.setValue(Integer.toString(getRandom(0,1)));
        quota.SOURCE_CODE.setValue(Integer.toString(getRandom(1,100)*10000000));
        quota.COLLECT_DATETIME.setValue((Const.getSDF().format(new Date())).toString());
        quota.setDeviceKeyId();
        pd.put(quota.ID.getValue(),quota);
        return pd;
    }

    /**
     * 获取设备数据
     * @return 设备数据
     */
    public List<PageData> findDevice(int deviceType) {
        List<PageData> pdList = new ArrayList<>();
        try{
            PageData pd = new PageData();
            pd.put("device_type",deviceType);
            pdList = (List<PageData>) dao.findForList("SDDeviceMapper.findDevice",pd);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return pdList;
    }
    
    
    /**
     * 获取指标数据
     * @return 设备数据
     */
    public List<PageData> findDeviceCur(PageData pd) {
        List<PageData> pdList = new ArrayList<>();
        try{
        	
           pdList = (List<PageData>) dao.findForList("SDDeviceDataCurMapper.findById",pd);
        } catch (Exception e){
        	e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return pdList;
    }

    /**
     * 插入指标数据
     * @param pd 数据对象
     */
    public boolean insert(PageData pd) {
        try{
            dao.save("SDDeviceDataCurMapper.insert",pd);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * 插入引通原始数据表
     * @param pd 数据对象
     */
    public boolean insertYincomm(PageData pd) {
        try{
            dao.save("SBYincommMapper.insert",pd);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * 插入数据
     * @param pd 数据指标对象
     */
    public boolean update(PageData pd) {
        delete(pd);
        insert(pd);
        return true;
    }
    /**
     * 修改指标数据
     */
    public void updateYT(PageData pd){
    	 try{
             dao.save("SDDeviceDataCurMapper.updateYT",pd);
         } catch (Exception e){
             System.out.println(e.getMessage());
         }
    }

    /**
     * 清除数据
     * @param pd 数据指标对象
     */
    public boolean delete(PageData pd) {
        try{
            dao.save("SDDeviceDataCurMapper.delete",pd);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * 移动数据至历史表
     * @param pd 数据对象
     */
    public boolean moveToHistory(PageData pd) {
        try{
            dao.save("SDDeviceDataHisMapper.insert",pd);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }


    /**
     * 获取随机数（演示用）
     * @param min 最小数值
     * @param max   最大数值
     * @param scale 精度
     * @return 随机小数
     */
    protected double getRandom(int min, int max,int scale) {
        Random ra =new Random();
        return new java.math.BigDecimal(ra.nextDouble()*(max-min)+min).setScale(scale,java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 获取随机数（演示用）
     * @param min 最小数值
     * @param max   最大数值
     * @return 随机小数
     */
    protected int getRandom(int min, int max) {
        Random ra =new Random();
        if(min==0 && max==1)
            return (ra.nextInt(max+1)+min+1)-1;
        return ra.nextInt(max)+min;
    }

    /**
     * 获取设备主数据
     * @return 设备数据
     */
    public List<PageData> findDeviceByMainId(PageData pd) {
        List<PageData> pdList = new ArrayList<>();
        try{
            pdList = (List<PageData>) dao.findForList("SDDeviceMapper.findDeviceByMainId",pd);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return pdList;
    }

    /**
     * 插入设备主数据
     * @param pd 数据对象
     */
    public boolean insertDevice(PageData pd) {
        try{
            dao.save("SDDeviceMapper.insert",pd);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * 修改设备主数据
     */
    public void updateDeviceData(PageData pd){
        try{
            dao.save("SDDeviceMapper.updateDeviceData",pd);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
