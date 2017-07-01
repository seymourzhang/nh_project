package com.nh.ifarm.alarm.service.impl;

import com.nh.ifarm.alarm.service.AlarmCurrService;
import com.nh.ifarm.alarm.service.AlarmPushService;
import com.nh.ifarm.push.api.JPushUtil;
import com.nh.ifarm.push.api.MiPushUtil;
import com.nh.ifarm.util.common.Logger;
import com.nh.ifarm.util.common.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yin Guo Xiang on 2017/1/14.
 */
@Service
public class AlarmPushServiceImpl implements AlarmPushService{

    private static Logger mLogger =Logger.getLogger(AlarmPushServiceImpl.class);
    @Autowired
    private AlarmCurrService alarmCurrService;

    @Override
    public void run() {
        mLogger.info("=====Now 开始 AlarmPushServiceImpl.run" );
        try {
            Object alarm_time = new Object();
            String content;
            Object houseId;
            Object alarmId;
            Object alarmName;
            Object houseName;
            Object farmName;
            List alias = new ArrayList();
            HashMap<String,String> contents = null;

            PageData mpd = new PageData();
            mpd.put("deal_status", "01");
            List<PageData> curAlarmIncoList = alarmCurrService.selectByCondition(mpd);
            if(curAlarmIncoList != null && curAlarmIncoList.size() > 0){
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
                Date newDate = new Date();
                String result = null;

                JPushUtil jPushUtil = new JPushUtil();
                MiPushUtil miPushUtil = new MiPushUtil();
                for(PageData tPD : curAlarmIncoList){
                    alias = new ArrayList();
                    houseId = tPD.get("house_id");
                    alarmName = tPD.get("alarm_name");
                    alarmId = tPD.get("id");
                    houseName = tPD.get("house_name").toString();
                    farmName = tPD.get("farm_name").toString();
                    alarm_time = tPD.get("alarm_time");

                    content = farmName + "--" + houseName + "栋，" + sdf1.format(alarm_time).toString() + "发生" + alarmName;

                    contents = new HashMap<String,String>();
                    contents.put("MessageTitle", "智慧鸡场报警");
                    contents.put("messageId", sdf2.format(newDate));
                    contents.put("MessageContent", content);

                    PageData tempPD = new PageData();
                    tempPD.put("house_id", houseId);
                    List<PageData> imeiInfoList = alarmCurrService.selectImeiByHouseId(tempPD);
                    for (PageData ImeiInfo : imeiInfoList) {
                        if(ImeiInfo != null && ImeiInfo.get("imei_no") != null && !ImeiInfo.get("imei_no").equals("")){
                            alias.add(ImeiInfo.get("imei_no"));
                        }
                    }

                    PageData sLJpushlog = new PageData();
                    sLJpushlog.put("house_id",Integer.parseInt(houseId.toString()));
                    sLJpushlog.put("content",content);
                    sLJpushlog.put("alarm_id",alarmId.toString());
                    sLJpushlog.put("tag_name_array",alias.toString().length() > 1000 ? alias.toString().substring(0, 999) : alias.toString());

                    if (alias.size() != 0) {
                        sLJpushlog.put("jg_push_time",new Date());
                        result = jPushUtil.pushMessageByAliasName(contents, alias);
                        if (!"Success".equals(result)) {
                            sLJpushlog.put("jg_res","fail");
                            if (result.length() > 490) {
                                sLJpushlog.put("jg_message",result.substring(0, 490));
                            } else {
                                sLJpushlog.put("jg_message",result);
                            }
                        } else {
                            sLJpushlog.put("jg_res","Success");
                        }
                        result = miPushUtil.pushMessageByTagName(contents, alias);
                        sLJpushlog.put("mi_push_time",new Date());
                        if (!"Success".equals(result)) {
                            sLJpushlog.put("mi_res","fail");
                            if (result.length() > 490) {
                                sLJpushlog.put("mi_message",result.substring(0, 490));
                            } else {
                                sLJpushlog.put("mi_message",result);
                            }
                        } else {
                            sLJpushlog.put("mi_res","Success");
                        }
                    }else{
                        sLJpushlog.put("jg_message","imei is null");
                        sLJpushlog.put("mi_message","imei is null");
                    }

                    alarmCurrService.insertPushLog(sLJpushlog);
                }
            } else {
                mLogger.info("=====No AlarmNotification to push =====");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLogger.info("=====Now 结束 AlarmPushServiceImpl.run");
    }
}
