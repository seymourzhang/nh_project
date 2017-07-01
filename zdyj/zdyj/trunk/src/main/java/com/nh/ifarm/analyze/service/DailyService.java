package com.nh.ifarm.analyze.service;

import com.nh.ifarm.util.common.PageData;

import java.util.List;

/**
 * Created by Seymour on 2016/12/26.
 */

public interface DailyService {

    public int dailySave(PageData pd) throws Exception;

    public List<PageData> dailyQuery(PageData pd) throws Exception;

    public PageData selectBySpecialDate(PageData pd) throws Exception;

    public PageData selectDate(PageData pd) throws Exception;

    List<PageData> selectDailyReport(PageData pd) throws Exception;

    List<PageData> selectWeeklyReport(PageData pd) throws Exception;
}
