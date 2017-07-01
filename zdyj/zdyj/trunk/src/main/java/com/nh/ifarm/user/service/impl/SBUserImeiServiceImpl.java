package com.nh.ifarm.user.service.impl;

import com.nh.ifarm.user.service.SBUserImeiService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Seymour on 2016/11/18.
 */
@Service
public class SBUserImeiServiceImpl implements SBUserImeiService{

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    public int insert(PageData pd) throws Exception{
        dao.delete("SBUserImeiMapper.delete", pd);
        return (Integer) dao.save("SBUserImeiMapper.insert", pd);
    }

    public int insertLog(PageData pd) throws Exception{
        return (Integer) dao.save("SBUserImeiMapper.insertLog", pd);
    }
}
