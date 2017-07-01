package com.nh.ifarm.code.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.code.service.CodeManageService;
import com.nh.ifarm.farm.service.FarmService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

@Service
public class CodeManageServiceImpl implements CodeManageService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Autowired
	BatchManageService batchManageService;
	
	@Override
	public PageData findCodeInfo(PageData pd) throws Exception {
		return (PageData)dao.findForObject("SDCodeMapper.findCodeInfo", pd);
	}
	
	@Override
	public PageData saveCode(PageData pd) throws Exception {
		PageData rt = new PageData();
    	rt.put("result",true);
        rt.put("msg",1);
    	int i = 0;
    	i = (Integer) dao.save("SDCodeMapper.saveCode", pd);
    	if(pd.get("code_type").toString().equals("FEED_TYPE")){
    	pd.put("good_code", pd.get("biz_code"));
    	pd.put("good_name", pd.get("code_name"));
    	pd.put("good_type", 1);
    	pd.put("bak", "");
    	pd.put("create_person",pd.get("user_id"));
		pd.put("create_date", new Date());	
		pd.put("create_time", new Date());
		pd.put("modify_person",pd.get("user_id"));
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());
    	i = (Integer)dao.save("BatchManageMapper.saveGoods", pd);
    	}
    	if(i != 1){
    		rt.put("result",false);
            rt.put("msg",0);
    	}
    	return rt;
	}
	
	@Override
	public PageData updateCode(PageData pd) throws Exception {
		PageData rt = new PageData();
    	rt.put("result",true);
        rt.put("msg",1);
    	int i = 0;
    	i = (Integer) dao.update("SDCodeMapper.editCode", pd);
    	if(pd.get("code_type").toString().equals("FEED_TYPE")){
    	pd.put("good_code", pd.get("biz_code"));
    	pd.put("good_type", 1);
    	List<PageData> goods = batchManageService.getGoodsList(pd);
		pd.putAll(goods.get(0));
		pd.put("good_name", pd.get("code_name"));
		pd.put("modify_person",pd.get("user_id"));
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());
    	i *= (Integer)dao.update("BatchManageMapper.editGoods", pd);
 	        dao.update("BatchManageMapper.editCorporationGood", pd);
    	}
 	       if(i != 1){
 	    		rt.put("result",false);
 	            rt.put("msg",0);
 	    	}
 	      return rt;
	}
	
	@Override
	public void deleteCode(PageData pd) throws Exception {
		dao.delete("SDCodeMapper.delCode", pd);
	}
    
	@Override
	public List<PageData> getCodeList(PageData pd)  throws Exception {
		return (List<PageData>)dao.findForList("SDCodeMapper.getCodeList2", pd);
	}
	
	@Override
	public List<PageData> getCodeType()  throws Exception {
		return (List<PageData>)dao.findForList("SDCodeMapper.getCodeType", null);
	}
	
}
