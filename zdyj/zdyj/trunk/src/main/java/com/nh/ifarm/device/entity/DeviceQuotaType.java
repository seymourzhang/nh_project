package com.nh.ifarm.device.entity;

import org.apache.shiro.config.Ini;

/**
 * 控制器指标类型类
 * Created by Raymon on 7/6/2016.
 */
public class DeviceQuotaType {
    /*控制器指标代码*/
    String code;
    /*控制器指标名称*/
    String name;
    /*控制器指标值*/
    String value;
    /*控制器命令码*/
    String commandNumber;


    /**
     * 构造函数
     * @param code 控制器指标代码
     * @param name 控制器指标名称
     */
    public DeviceQuotaType(String code, String name) {
        Init(code,name,null,null);
    }

    /**
     * 构造函数
     * @param code 控制器指标代码
     * @param name 控制器指标名称
     * @param commandNumber 控制器命令码
     */
    public DeviceQuotaType(String code, String name,String commandNumber) {
        Init(code,name,null,commandNumber);
    }

    /**
     * 初始化方法
     * @param code 控制器指标代码
     * @param name 控制器指标名称
     * @param value 控制器指标值
     * @param commandNumber 控制器命令码
     */
    public void Init(String code, String name,String value,String commandNumber) {
        setCode(code);
        setName(name);
        setValue(value);
        setCommandNumber(commandNumber);
    }

    public String getCode(){
        return code;
    }
    public void setCode(String code) {
        this.code=code;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {this.name=name;}
    public String getValue(){
        return value;
    }
    public void setValue(String value) {this.value=value;}
    public String getCommandNumber(){
        return commandNumber;
    }
    public void setCommandNumber(String commandNumber) {this.commandNumber=commandNumber;}

    /**
     * 生成字符串值
     * 字符串值
     */
    @Override
    public String toString() {
        return code+":"+commandNumber+":"+name+":"+value;

    }
}
