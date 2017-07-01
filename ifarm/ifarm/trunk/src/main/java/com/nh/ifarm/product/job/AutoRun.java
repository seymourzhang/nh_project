//package com.mtc.zljk.product.job;
//
//import TaskService;
//import IPUtil;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//
///**
// * Created by LeLe on 12/14/2016.
// */
//public class AutoRun {
//    private static Logger mLogger = Logger.getLogger(AutoRun.class);
//    @Autowired
//    TaskService taskService;
//
//    @Scheduled(cron="0 5 0 ? * *") //每天0点5分执行一次
//    public void run() {
//        if(!IPUtil.needRunTask()){
//            mLogger.info("本机不启用SP_TASK_REMIND");
//            return ;
//        }
//        try{
//            taskService.execProc("exec_SP_TASK_REMIND");
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//}
