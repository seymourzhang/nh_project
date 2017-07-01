package com.nh.ifarm.device.yingtong.service.impl;

/**
 * Created by LeLe on 7/8/2016.
 */
public class YingtongServiceStatusImpl {
    /**
     * 服务状态: 未启动
     */
    public static String STANDYBY = "00";
    /**
     * 服务状态: 启动中
     */
    public static String RUNNING = "01";
    /**
     * 服务状态: 正常停止
     */
    public static String STOP = "02";
    /**
     * 服务状态: 异常停止
     */
    public static String TERMINATION= "05";
}
