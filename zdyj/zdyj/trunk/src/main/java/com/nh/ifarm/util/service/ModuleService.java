package com.nh.ifarm.util.service;

import org.springframework.stereotype.Service;

/**
 * 模块服务接口
 * Created by Raymon on 7/7/2016.
 */
public interface ModuleService {
    /**
     * @TODO 任何bean b =controlMain.service("ssTestss","getStr",objs);
     * @param str 类名 类名第一个字母小写
     * @param mstr 方法名
     * @param objs Object[] objs = new Object[]{123, "str", new Date()};
     * @return
     * @Date 2016-6-28
     * @author loyeWen
     *
     */
    <T> T service(String str,String mstr,Object[] objs) throws Exception;
}
