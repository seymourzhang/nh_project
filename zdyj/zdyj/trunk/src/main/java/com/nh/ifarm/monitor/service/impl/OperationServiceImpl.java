package com.nh.ifarm.monitor.service.impl;

import com.nh.ifarm.monitor.service.OperationService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import com.sun.corba.se.spi.orb.Operation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Seymour on 2016/10/13.
 */
@Service
public class OperationServiceImpl implements OperationService {

    @Resource(name="daoSupport")
    private DaoSupport dao;

    public List<PageData> selectAll(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("OperationMapper.selectAll", pd);
    }

    public List<PageData> selectByConditionMy(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("OperationMapper.selectByConditionMy", pd);
    }

    public List<PageData> selectForUp(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("OperationMapper.selectForUp", pd);
    }

    public List<PageData> selectByCondition(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("OperationMapper.selectByCondition", pd);
    }
}
