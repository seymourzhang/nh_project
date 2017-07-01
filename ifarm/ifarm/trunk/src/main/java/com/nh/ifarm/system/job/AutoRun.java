//package com.mtc.zljk.system.job;
//
//import ProcedureService;
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
//@Component("systemAutoRun")
//public class AutoRun {
//    private static Logger mLogger = Logger.getLogger(AutoRun.class);
//    @Autowired
//    ProcedureService procedureService;
//
//    @Scheduled(cron="0 0/60 * * * ? ") //每小时执行一次
//    public void runForHour() {
//        if(!IPUtil.needRunTask()){
//            mLogger.info("本机不启用systemAutoRun.runForHour");
//            return ;
//        }
//        procedureService.runForHour();
//    }
//    @Scheduled(cron="0 30 0 * * ?") //每日执行一次
//    public void runForDay() {
//        if(!IPUtil.needRunTask()){
//            mLogger.info("本机不启用systemAutoRun.runForDay");
//            return ;
//        }
//        procedureService.runForDay();
//    }
//}
