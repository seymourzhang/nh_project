package com.nh.ifarm.product.service.impl;

import com.nh.ifarm.product.service.TaskService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Seymour on 2016/10/24.
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    public List<PageData> getTaskTypeName() throws Exception{
        return (List<PageData>) dao.findForList("SDTaskMapper.getTaskTypeName", null);
    }

    public List<PageData> getTaskCodeName(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("SDTaskMapper.getTaskCodeName", pd);
    }

    public List<PageData> getDateTypeName() throws Exception{
        return (List<PageData>) dao.findForList("SDTaskMapper.getDateTypeName", null);
    }

    /**
     * 执行存储过程
     * @param procName 存储过程名
     * @throws Exception
     */
    public void execProc(String procName) throws Exception{
        dao.findForObject("SDTaskMapper." + procName, null);
    }
}
