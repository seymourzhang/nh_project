package com.nh.ifarm.system.service.impl;

import com.nh.ifarm.system.service.ProcedureService;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 存储过程服务类
 * Created by Raymon on 7/8/2016.
 */
@Service
public class ProcedureServiceImpl implements ProcedureService {
    /*
    * 传参格式：‘2016-07-08 09:00:00’,'2016-07-08 09:59:59’
    * 注：时间到59分59秒
    * */
    String SP_MONITOR_DAILY ="SP_MONITOR_DAILY";
    String SP_ALARM_DAILY ="SP_ALARM_DAILY";

    /*
    * 传参格式：‘2016-07-08’,'2016-07-08’
    * 注：默认每天跑前一天的数据，开始日期和结束日期都是前一天的日期）
    * */
    String SP_MONITOR_MONTH ="SP_ALARM_DAILY";
    String SP_ALARM_MONTH ="SP_ALARM_MONTH";

    /*开始时间*/
    String beginDateTime="beginDateTime";
    /*结束时间*/
    String endDateTime="endDateTime";
    /*开始日期*/
    String beginDate="beginDate";
    /*结束日期*/
    String endDate="endDate";
    /*上一天日期*/
    String lastDate="lastDate";
    /*时区值*/
    String timeZone="GMT+8:00";

    @Resource(name = "daoSupport")
    protected DaoSupport dao;

    /**
     * 按小时执行
     */
    public void runForHour(){
        try{
            PageData pd = new PageData();
            pd.put(beginDateTime,getDateTime(-1,0,0,false));
            pd.put(endDateTime,getDateTime(-1,59,59,false));
//            System.out.println(pd.get(beginDateTime));
//            System.out.println(pd.get(endDateTime));
            dao.save("ProcedureMapper.SP_MONITOR_DAILY",pd);
            dao.save("ProcedureMapper.SP_ALARM_DAILY",pd);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 按天执行
     */
    public void runForDay(){
        try{
            PageData pd = new PageData();
            pd.put(beginDate,getDateTime(-24,0,0,true));
            pd.put(endDate,getDateTime(-24,59,59,true));
            pd.put(lastDate,getDateTime(-24,59,59,true));
//            System.out.println(pd.get(beginDate));
//            System.out.println(pd.get(endDate));
            dao.save("ProcedureMapper.SP_MONITOR_MONTH",pd);
            dao.save("ProcedureMapper.SP_ALARM_MONTH",pd);
            dao.save("ProcedureMapper.SP_LUX",pd);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取日期时间
     * @param addHour 增加小时数
     * @param minute 分
     * @param second 秒
     * @return 日期或时间
     */
    String getDateTime(int addHour,int minute,int second,boolean isDay){
        String rt;
        Calendar cal = Calendar.getInstance ();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR,addHour);
        cal.set(Calendar.SECOND,minute);
        cal.set(Calendar.MINUTE,second);
        if (isDay) {
            rt = Const.getSDF().format(cal.getTime()).substring(0,10);
        } else{
            rt = Const.getSDF().format(cal.getTime());
        }
        return  rt;
    }



//    public static void main(String[] args) {
//        ProcedureServiceImpl psi = new ProcedureServiceImpl();
//        System.out.println(psi.getDateTime(-1,0,0,false));
//        System.out.println(psi.getDateTime(-1,59,59,false));
//        System.out.println(psi.getDateTime(-24,0,0,true));
//        System.out.println(psi.getDateTime(-24,59,59,true));
//    }

}
