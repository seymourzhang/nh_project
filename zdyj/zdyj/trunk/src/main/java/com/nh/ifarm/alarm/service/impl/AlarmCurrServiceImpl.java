package com.nh.ifarm.alarm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.nh.ifarm.alarm.service.AlarmCurrService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

@Service
public class AlarmCurrServiceImpl implements AlarmCurrService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	public List<PageData> selectAll() throws Exception {
		
		return (List<PageData>) dao.findForList("AlarmCurrMapper.selectAll", null);
	}

	public List<PageData> selectImeiByHouseId(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("AlarmCurrMapper.selectImeiByHouseId", pd);
	}

	public List<PageData> selectByCondition(PageData pd) throws Exception {
		
		return (List<PageData>) dao.findForList("AlarmCurrMapper.selectByCondition", pd);
	}
	
	@Override
	public int updateAlarm(PageData pd) throws Exception{
		return (Integer) dao.update("AlarmCurrMapper.updateAlarm", pd);
	}

	@Override
	public int insertPushLog(PageData pd) throws Exception{
		return (Integer) dao.update("AlarmCurrMapper.insertPushLog", pd);
	}

	public void run() {
		try{
			dao.save("AlarmCurrMapper.SP_ALARM",null);
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
}
