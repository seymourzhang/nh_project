package com.nh.ifarm.monitor.service.impl;

import com.nh.ifarm.monitor.service.UserFilterService;
import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

//import jdk.nashorn.internal.runtime.ECMAException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Seymour on 2016/9/7.
 */
@Service
public class UserFilterServiceImpl implements UserFilterService{
    @Resource(name="daoSupport")
    private DaoSupport dao;

    public PageData selectByCondition(PageData pd) throws Exception{
        return (PageData) dao.findForObject("UserFilterMapper.selectByCondition", pd);
    }

    public int saveSet(PageData pd) throws Exception{
        return (Integer) dao.save("UserFilterMapper.saveSet",pd);
    }

    public int updateSet(PageData pd) throws Exception{
        return (Integer) dao.update("UserFilterMapper.updateSet", pd);
    }
}
