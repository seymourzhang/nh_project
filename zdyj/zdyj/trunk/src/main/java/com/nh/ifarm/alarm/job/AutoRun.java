package com.nh.ifarm.alarm.job;


import com.nh.ifarm.alarm.service.AlarmPushService;
import com.nh.ifarm.util.common.IPUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("alarmPushAutoRun")
public class AutoRun {
	private static Logger mLogger = Logger.getLogger(AutoRun.class);
	@Autowired
    AlarmPushService alarmPushService;

    @Scheduled(cron="0/60 * * * * ? ") //每分钟执行一次
    public void run() {
		if(!IPUtil.needRunTask()){
			mLogger.info("本机不启用alarmPushAutoRun");
			return ;
		}
        alarmPushService.run();
    }
}
