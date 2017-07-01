package com.nh.ifarm.alarm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.nh.ifarm.alarm.service.AlarmService;

import org.springframework.stereotype.Service;

import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

@Service
public class AlarmServiceImpl implements AlarmService {

	@SuppressWarnings("restriction")
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@SuppressWarnings("unchecked")
	public List<PageData> selectByCondition(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectByCondition", pd);
	}
	
	public List<PageData> selectByCondition1(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectByCondition1", pd);
	}
	
	public List<PageData> selectByCondition2(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectByCondition2", pd);
	}
	
	public List<PageData> selectByCondition3(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectByCondition3", pd);
	}
    
	public void saveSBDayageSettingSub(PageData pd) throws Exception{
		   dao.save("SBDayageSettingSubMapper.saveSBDayageSettingSub", pd);
	}
	
	public void saveSBDayageSettingSub2(PageData pd) throws Exception{
		   dao.save("SBDayageSettingSubMapper.saveSBDayageSettingSub2", pd);
	}
	
	public void deleteSBDayageSettingSub(PageData pd) throws Exception{
		   dao.update("SBDayageSettingSubMapper.deleteSBDayageSettingSub", pd);
	}
	
	public void deleteSBDayageSettingSub2(PageData pd) throws Exception{
		   dao.update("SBDayageSettingSubMapper.deleteSBDayageSettingSub2", pd);
	}
	
	public void updateSBDayageSettingSub(PageData pd) throws Exception{
		   dao.update("SBDayageSettingSubMapper.updateSBDayageSettingSub", pd);
	}
	
	public void saveSBDayageTempSub(List<PageData> list) throws Exception{
		   dao.batchSave("SBDayageSettingSubMapper.saveSBDayageTempSub", list);
	}
	
	public void saveSBDayageTempSub2(PageData pd) throws Exception{
		   dao.save("SBDayageSettingSubMapper.saveSBDayageTempSub2", pd);
	}
	
	public void deleteSBDayageTempSub(PageData pd) throws Exception{
		   dao.delete("SBDayageSettingSubMapper.deleteSBDayageTempSub", pd);
	}
	
	public void deleteSBDayageTempSub2(PageData pd) throws Exception{
		   dao.delete("SBDayageSettingSubMapper.deleteSBDayageTempSub2", pd);
	}
	
	public List<PageData> selectSBDayageTempSubByCondition(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectSBDayageTempSubByCondition", pd);
	}
	
	public void updateSBDayageTempSub(List<PageData> list) throws Exception{
		   dao.batchUpdate("SBDayageSettingSubMapper.updateSBDayageTempSub", list);
	}
	
	public List<PageData> selectSBHouseAlarmByCondition(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectSBHouseAlarmByCondition", pd);
	}
	
	public void updateSBHouseAlarm(PageData pd) throws Exception{
		 dao.update("SBDayageSettingSubMapper.updateSBHouseAlarm", pd);
	}
	
	public List<PageData> selectSBReminderByCondition(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectSBReminderByCondition", pd);
	}
	
	public void saveSBReminder(PageData pd) throws Exception{
		dao.save("SBDayageSettingSubMapper.saveSBReminder", pd);
	}
	
	public void updateSBReminder(PageData pd) throws Exception{
		dao.update("SBDayageSettingSubMapper.updateSBReminder", pd);
	}
	
	public List<PageData> selectSBRemindSwitch(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectSBRemindSwitch", pd);
	}
	
	public void saveSBRemindSwitch(PageData pd) throws Exception{
		dao.save("SBDayageSettingSubMapper.saveSBRemindSwitch", pd);
	}
	
	public void updateSBRemindSwitch(PageData pd) throws Exception{
		dao.update("SBDayageSettingSubMapper.updateSBRemindSwitch", pd);
	}
	
	public List<PageData> selectSBRemindAlarmcodeByCondition(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectSBRemindAlarmcodeByCondition", pd);
	}
	
	public void saveSBRemindAlarmcode(PageData pd) throws Exception{
		dao.save("SBDayageSettingSubMapper.saveSBRemindAlarmcode", pd);
	}
	
	public List<PageData> selectSBRemindSetting(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectSBRemindSetting", pd);
	}
	
	public void saveSBRemindSetting(PageData pd) throws Exception{
		dao.save("SBDayageSettingSubMapper.saveSBRemindSetting", pd);
	}
	
	public List<PageData> findUserAll(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.findUserAll", pd);
	}
	
	public List<PageData> selectSBCode(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectSBCode", pd);
	}
	
	public List<PageData> selectInsideTemp(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectInsideTemp", pd);
	}
	
	public List<PageData> selectDevice(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("SBDayageSettingSubMapper.selectDevice", pd);
	}
	
	public void updateDeviceSub(PageData pd) throws Exception{
		dao.update("SBDayageSettingSubMapper.updateDeviceSub", pd);
	}
	
	public void saveSbDayageSettingSubHis(PageData pd) throws Exception{
		   dao.save("SBDayageSettingSubMapper.saveSbDayageSettingSubHis", pd);
	}
	
	public void saveSbHouseAlarmHis(PageData pd) throws Exception{
		   dao.save("SBDayageSettingSubMapper.saveSbHouseAlarmHis", pd);
	}
  
}  
