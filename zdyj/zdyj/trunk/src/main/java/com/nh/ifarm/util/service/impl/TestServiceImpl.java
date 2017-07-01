package com.nh.ifarm.util.service.impl;

import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import com.nh.ifarm.util.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by LeLe on 2017/3/16.
 */
@Service
public class TestServiceImpl implements TestService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;



    public int saveTest(PageData pd) throws Exception{
        int rt = 0;
        PageData tmpPd = new PageData();
        tmpPd.put("f1",1);
        tmpPd.put("f2",2);
        dao.save("TestMapper.insertData", tmpPd);

        dao.save("TestMapper.insertData2", tmpPd);
        return rt;
    }
}
