package com.nh.ifarm.farm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.nh.ifarm.user.service.RoleService;
import com.nh.ifarm.util.service.OrganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.ifarm.farm.service.FarmService;
import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

/**
 * Created by LeLe on 6/30/2016.
 */
@Service
public class FarmServiceImpl implements FarmService {
   
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Autowired
	private OrganService organService;
	
	public List<PageData> selectByCondition(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDFarmMapper.selectByCondition", pd);
	};
	
	public List<PageData> selectAll() throws Exception{
		return (List<PageData>) dao.findForList("SDFarmMapper.selectAll", null);
	}
	
	public List<PageData> selectHouseByCondition(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDFarmMapper.selectHouseByCondition", pd);
	}
	
	public List<PageData> selectHouseById(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDFarmMapper.selectHouseById", pd);
	}
	
	public List<PageData> selectBatchByCondition(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDFarmMapper.selectBatchByCondition", pd);
	}
	
	public List<PageData> selectOganizationList(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDFarmMapper.selectOganizationList", pd);
	}

	public List<PageData> findFarm(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDFarmMapper.findFarm", pd);
	}
	
	public List<PageData> findHouse(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDFarmMapper.findHouse", pd);
	}
	
	public List<PageData> findBatchlistPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("SDFarmMapper.findBatchlistPage", page);
	}
	public List<PageData> findCode(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDFarmMapper.findCode", pd);
	}

	public List<PageData> findAreaChina(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDFarmMapper.findAreaChina", pd);
	}
	

	public List<PageData> findDevice(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDFarmMapper.findDevice", pd);
	}

	public PageData findDevice_v2(PageData pd) throws Exception {
		return (PageData) dao.findForObject("SDFarmMapper.selectDevice_v2", pd);
	}

	public PageData findDeviceForOut(PageData pd) throws Exception {
		return (PageData) dao.findForObject("SDFarmMapper.selectDeviceForOut", pd);
	}

	public List<PageData> findDeviceIsExist(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDFarmMapper.findDeviceIsExist", pd);
	}

	public List<PageData> findSensor(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDFarmMapper.findSensor", pd);
	}

	public int mappingDevice(PageData pd) throws Exception {
		return (int)dao.save("SDFarmMapper.mappingDevice", pd);
	}

	public int delDevice(PageData pd) throws Exception {
		return (int)dao.delete("SDFarmMapper.delDevice", pd);
	}

	public int delSensor(PageData pd) throws Exception {
		return (int)dao.delete("SDFarmMapper.delSensor", pd);
	}

	public int insertSensor(PageData pd) throws Exception {
		return (int)dao.delete("SDFarmMapper.insertSensor", pd);
	}

	public List<PageData> findHouseDevice(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDFarmMapper.findHouseDevice", pd);
	}
	
	public int saveFarm(PageData pd) throws Exception {
		Integer rt = (Integer) dao.save("SDFarmMapper.saveFarm", pd);
		Integer objId = (Integer) pd.get("id");

		PageData paramPd = new PageData();
		paramPd.put("parent_id",pd.getString("parent_id"));
		paramPd.put("level_id",pd.getString("level_id"));
		paramPd.put("org",objId.toString());
		paramPd.put("user_id",pd.getInteger("create_person"));
		organService.saveFarmMapping(paramPd);
		return rt;
	}

	public void editFarm(PageData pd) throws Exception {
		dao.update("SDFarmMapper.editFarm", pd);
	}

	public int saveBatch(PageData pd) throws Exception {
		return (Integer) dao.save("SDFarmMapper.saveBatch", pd);
	}
	

	public void editBatch(PageData pd) throws Exception {
		dao.update("SDFarmMapper.editBatch", pd);
		
	}

	public void editHouse(PageData pd) throws Exception {
		dao.update("SDFarmMapper.editHouse", pd);
		if(null != pd.get("freeze_status") && null != pd.get("orgList")){
			organService.deleteOrg(pd);
		}
	}

	public int saveHouse(PageData pd) throws Exception {
		return (Integer) dao.save("SDFarmMapper.saveHouse", pd);
	}
	

	public int saveHouseAlarm(PageData pd) throws Exception {
		int rt = (Integer) dao.save("SDFarmMapper.saveHouseAlarm", pd);
		rt *= (Integer) dao.save("SDFarmMapper.initHouseAlarmBasicHis", pd);
		return rt;
	}

	public int saveDeviHouse(PageData pd) throws Exception {
		return (Integer) dao.save("SDFarmMapper.saveDeviHouse", pd);
	}
	

	public void delDeviHouse(PageData pd) throws Exception {
		dao.delete("SDFarmMapper.delDeviHouse", pd);
		
	}
	

	public PageData isBatchHouseNull(PageData pd) throws Exception {
	return (PageData)dao.findForObject("SDFarmMapper.isBatchHouseNull", pd);
	}
	
}
