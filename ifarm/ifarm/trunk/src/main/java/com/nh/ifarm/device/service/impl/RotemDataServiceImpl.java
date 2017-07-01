package com.nh.ifarm.device.service.impl;

import com.nh.ifarm.device.service.RotemDataService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Seymour on 2017/2/22.
 */
@Service
public class RotemDataServiceImpl implements RotemDataService {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    public List<PageData> selectRotemData() throws Exception{
        return (List<PageData>) dao.findForList("ShowDataMobileMapper.selectRotemData", null);
    }
}
