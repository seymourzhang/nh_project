package com.nh.ifarm.system.service.impl;

import com.nh.ifarm.system.service.RepMobileService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Seymour on 2017/5/31.
 */
@Service
public class RepMobileServiceImpl implements RepMobileService {

    @Resource(name = "daoSupport")
    protected DaoSupport dao;

    public List<PageData> getRepMobile() throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getRepMobile", null);
    }

    public List<PageData> getWeekDate(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getWeekDate", pd);
    }

    public List<PageData> getItemData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getItemData", pd);
    }
    public List<PageData> getDeliveryData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getDeliveryData", pd);
    }
    public List<PageData> getCutFeedData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getCutFeedData", pd);
    }
    public List<PageData> getBreedData(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getBreedData", pd);
    }


    public List<PageData> getItemDataTotal(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getItemDataTotal", pd);
    }
    public List<PageData> getDeliveryDataTotal(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getDeliveryDataTotal", pd);
    }
    public List<PageData> getCutFeedDataTotal(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getCutFeedDataTotal", pd);
    }
    public List<PageData> getBreedDataTotal(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getBreedDataTotal", pd);
    }


    public List<PageData> getItemDataAvg(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getItemDataAvg", pd);
    }
    public List<PageData> getDeliveryDataAvg(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getDeliveryDataAvg", pd);
    }
    public List<PageData> getCutFeedDataAvg(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getCutFeedDataAvg", pd);
    }
    public List<PageData> getBreedDataAvg(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getBreedDataAvg", pd);
    }


    public List<PageData> getItemDataTarget(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getItemDataTarget", pd);
    }
    public List<PageData> getDeliveryDataTarget(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getDeliveryDataTarget", pd);
    }
    public List<PageData> getCutFeedDataTarget(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getCutFeedDataTarget", pd);
    }
    public List<PageData> getBreedDataTarget(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("RepMobileMapper.getBreedDataTarget", pd);
    }

}
