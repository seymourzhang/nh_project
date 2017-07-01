package com.nh.ifarm.report.service.impl;

import com.nh.ifarm.report.service.RepCurveMobileService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seymour on 2017/3/25.
 */
@Service
public class RepCurveMobileServiceImpl implements RepCurveMobileService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    public List<PageData> getCurveDataDaily(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("ReportCurveMobileMapper.selectCurveDataDaily", pd);
    }

    public List<PageData> getCurveData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("ReportCurveMobileMapper.selectCurveData", pd);
    }

    public List<PageData> getAlarmStatistic(PageData pd) throws Exception{
        int houseId = Integer.parseInt(pd.get("house_id").toString());
        List<PageData> lpd = new ArrayList<>();
        if (houseId == 0){
            lpd = (List<PageData>) dao.findForList("ReportCurveMobileMapper.selectAlarmStatisticFarm", pd);
        } else {
            lpd = (List<PageData>) dao.findForList("ReportCurveMobileMapper.selectAlarmStatisticHouse", pd);
        }
        return lpd;
    }
}
