package com.nh.ifarm.drug.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.nh.ifarm.drug.service.DrugService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

@Service
public class DrugServiceImpl implements DrugService {
	
	@SuppressWarnings("restriction")
	@Resource(name = "daoSupport")
	private DaoSupport dao;


	@SuppressWarnings("unchecked")
	public List<PageData> selectDrugPlan(PageData pd) throws Exception {
		
		return (List<PageData>) dao.findForList("SDDrugMapper.selectDrugPlan", pd);
	}

	@SuppressWarnings("unchecked")
	public List<PageData> selectDrugFact(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDDrugMapper.selectDrugFact", pd);
	}
	
	public void saveDrugPlan(PageData pd) throws Exception {		
		pd.put("status", "Y");
        pd.put("create_date", new Date());
        pd.put("create_time", new Date());
        pd.put("modify_date", new Date());
        pd.put("modify_time", new Date());
		dao.save("SDDrugMapper.saveDrugPlan", pd);
	}
	
	public void deleteDrugPlan(PageData pd) throws Exception{
		   dao.delete("SDDrugMapper.deleteDrugPlan", pd);
	}

	public void saveDrugFact(PageData pd) throws Exception {		
		pd.put("status", "Y");
        pd.put("create_date", new Date());
        pd.put("create_time", new Date());
        pd.put("modify_date", new Date());
        pd.put("modify_time", new Date());
		dao.save("SDDrugMapper.saveDrugFact", pd);
	}
	
	public void deleteDrugFact(PageData pd) throws Exception{
		   dao.delete("SDDrugMapper.deleteDrugFact", pd);
	}
	
	public PageData selectFarm(PageData pd) throws Exception {
		return (PageData) dao.findForObject("SDDrugMapper.selectFarm", pd);
	}
	
	@SuppressWarnings("unchecked")
	public List<PageData> selectUser(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDDrugMapper.selectUser", pd);
	}
	
	@SuppressWarnings("unchecked")
	public List<PageData> selectGoods(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDDrugMapper.selectGoods", pd);
	}
	
	@SuppressWarnings("unchecked")
	public List<PageData> selectCode(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDDrugMapper.selectCode", pd);
	}
	
	@SuppressWarnings("unchecked")
	public List<PageData> selectFactory(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDDrugMapper.selectFactory", pd);
	}

}
