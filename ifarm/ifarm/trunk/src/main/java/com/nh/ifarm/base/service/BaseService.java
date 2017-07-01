package com.nh.ifarm.base.service;

import com.nh.ifarm.util.common.Logger;
import java.util.HashMap;

/**
 * @Description:
 * @Author: laven
 * @Date: 2017/6/8 下午2:19
 */
public class BaseService {
    protected HashMap<String, String> resMsgMap = new HashMap<String, String>();

    protected Logger logger = Logger.getLogger(this.getClass());

    /**
     * 翻译Office类内的返回错误编码
     *
     * -1：无错误 0：无数据 1：系统配置错误 2：Excel标签页错误 3：开始行或列错误或列名为空 4：列头错误 5：列数错误 6：单元格类型错误 7:数据保存错误
     */
    protected HashMap<String, String> getResMsgMap(){
        resMsgMap.put("-1", "无错误");
        resMsgMap.put("0", "无数据");
        resMsgMap.put("1", "系统配置错误");
        resMsgMap.put("2", "Excel标签页错误");
        resMsgMap.put("3", "开始行或列错误或列名为空");
        resMsgMap.put("4", "列头错误");
        resMsgMap.put("5", "列数错误");
        resMsgMap.put("6", "单元格类型错误");
        resMsgMap.put("7", "数据保存错误");
        return resMsgMap;
    }
}
