//package com.mtc.zljk.monitor.job;
//
//import MonitorService;
//import IPUtil;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * 自动运行作业类
// * Created by Raymon on 7/8/2016.
// */
//@Component("monitorAutoRun")
//public class AutoRun {
//    private static Logger mLogger =Logger.getLogger(AutoRun.class);
//    @Autowired
//    MonitorService monitorService;
//
//    @Scheduled(cron="0/60 * * * * ? ") //每分钟执行一次
//    public void run() {
//        if(!IPUtil.needRunTask()){
//            mLogger.info("本机不启用monitorAutoRun");
//            return ;
//        }
//        monitorService.run();
//    }
//
//}
