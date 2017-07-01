//package com.mtc.zljk.goods.job;
//
//import GoogsService;
//import IPUtil;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//
//@Component("batchAutoRun")
//public class AutoRun {
//    private static Logger mLogger = Logger.getLogger(AutoRun.class);
//    @Autowired
//    GoogsService googsService;
//
//    @Scheduled(cron="0 5 0 ? * *") //每天0点5分执行一次
//    public void run() {
//        if(!IPUtil.needRunTask()){
//            mLogger.info("本机不启用batchAutoRun");
//            return ;
//        }
//        try{
//            googsService.execProc("exec_SP_DELETE_STOCK_REMIND");
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}
