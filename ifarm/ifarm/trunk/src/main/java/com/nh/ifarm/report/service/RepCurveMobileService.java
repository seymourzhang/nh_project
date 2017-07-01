package com.nh.ifarm.report.service;

import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;

import java.util.List;

/**
 * Created by Seymour on 2017/3/25.
 */
public interface RepCurveMobileService {

    /***
     * 获取日龄数据
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> getCurveDataDaily(PageData pd) throws Exception;

    /***
     * 获取周龄数据
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> getCurveData(PageData pd) throws Exception;

    /**
     *
     * 获取报警次数
     *
     * */
    List<PageData> getAlarmStatistic(PageData pd) throws Exception;
}
