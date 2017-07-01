package com.nh.ifarm.breed.service.impl;

import com.nh.ifarm.base.service.BaseService;
import com.nh.ifarm.breed.service.SBGrowingStdService;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Seymour on 2016/11/14.
 */
@Service
public class SBGrowingStdServiceImpl extends BaseService implements SBGrowingStdService {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    public List<PageData> selectByVarietyId(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("SBGrowingStdMapper.selectByVarietyId", pd);
    }

    public List<PageData> selectBroilByVarietyId(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("SBGrowingStdMapper.selectBroilByVarietyId", pd);
    }

    @Override
    public List<PageData> selectCultivateStandardMeatData(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("SBGrowingStdMapper.selectCultivateStandardMeatData", pd);
    }

    public Json saveCultivateStandardGrowData(PageData pageData) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("SBGrowingStdMapper.insertCultivateStandardGrowData", pageData);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }

    public Json saveCultivateStandardEggData(PageData pageData) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("SBGrowingStdMapper.insertCultivateStandardEggData", pageData);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }

    public Json updateCultivateStandardEggData(PageData pageData) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("SBGrowingStdMapper.updateCultivateStandardEggData", pageData);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }

    public Json updateCultivateStandardGrowData(PageData pageData) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("SBGrowingStdMapper.updateCultivateStandardGrowData", pageData);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }

    public Json saveCultivateStandardMeatData(PageData pageData) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("SBGrowingStdMapper.insertCultivateStandardMeatData", pageData);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }

    public Json updateCultivateStandardMeatData(PageData pageData) throws Exception{
        Json resJson = new Json();
        try {
            int i = (Integer) dao.save("SBGrowingStdMapper.updateCultivateStandardMeatData", pageData);
            if(i == 1){
                resJson.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resJson.setMsg(e.getMessage());
            throw e;
        }
        return resJson;
    }

}
