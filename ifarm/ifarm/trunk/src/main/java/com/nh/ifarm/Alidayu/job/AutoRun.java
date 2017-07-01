package com.nh.ifarm.Alidayu.job;

import com.nh.ifarm.Alidayu.service.impl.AudioAlarmServiceImpl;
import com.nh.ifarm.util.common.IPUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by guoxiang on 2017/6/7.
 */
@Component("AudioAlarm")
public class AutoRun {
    private static Logger mLogger = Logger.getLogger(AutoRun.class);
    @Autowired
    AudioAlarmServiceImpl tAudioAlarmServiceImpl;

    @Scheduled(cron="0/60 * * * * ? ") //每分钟执行一次
    public void run() {

        if(!IPUtil.needRunTask()){
            mLogger.info("本机不启用语音通知服务。");
            return ;
        }

        //  生成语音任务
        tAudioAlarmServiceImpl.service();

    }
}
