package com.nh.ifarm.monitor.service.impl;

import com.nh.ifarm.monitor.service.VideoMonitorService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import java.util.List;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Ants on 2016/8/25.
 */
@Service
public class VideoMonitorServiceImpl implements VideoMonitorService {
    @Resource(name="daoSupport")
    private DaoSupport dao;

    public List<PageData> selectByCondition(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("VideoMonitorMapper.selectByCondition", pd);
    }
}
