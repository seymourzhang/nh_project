package com.nh.ifarm.device.job;

import com.nh.ifarm.device.dmr.service.DmrService;
import com.nh.ifarm.device.rotem.service.RotemService;
import com.nh.ifarm.device.wirelessyt.WirelessYTService;
import com.nh.ifarm.device.yingtong.service.YingtongService;
import com.nh.ifarm.util.common.IPUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 自动运行作业类
 * Created by Raymon on 7/7/2016.
 */
@Component("deviceAutoRun")
public class AutoRun {

    private static Logger mLogger = Logger.getLogger(AutoRun.class);

    @Autowired
    DmrService dmrService;
    @Autowired
    RotemService rotemService;
    @Autowired
    YingtongService yingtongService;
    @Autowired
    WirelessYTService tWirelessYTService;

    @Scheduled(cron="0/60 * * * * ? ") //每60秒执行一次
    public void run() {
//        System.out.println("更新Rotem数据至数据库...");
//        rotemService.writeData(rotemService.getData());
//        System.out.println("更新Rotem数据完毕！");
//
//        System.out.println("更新大牧人数据至数据库...");
//        dmrService.start();
//        System.out.println("更新大牧人数据完毕！");
////
//        System.out.println("更新引通数据至数据库...");
//        yingtongService.start();
//        System.out.println("更新引通数据完毕！");

        if(!IPUtil.needRunTask()){
            mLogger.info("本机不启用deviceAutoRun");
            return ;
        }

//        System.out.println("更新引通无线数据开始！");
//        tWirelessYTService.start();
//        System.out.println("更新引通无线数据完毕！");
    }
}
