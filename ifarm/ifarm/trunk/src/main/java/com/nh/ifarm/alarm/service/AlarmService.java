package com.nh.ifarm.alarm.service;

import java.util.List;

import com.nh.ifarm.util.common.PageData;

/**
 * Created by LeLe on 6/30/2016.
 */
public interface AlarmService {
	/**
	 * 按条件查询
	 * @param pd
	 * @return List<SBDayageSettingSub>
	 * @throws Exception
	 */
	List<PageData> selectByCondition(PageData pd) throws Exception;
	
	/**
	 * 按条件查询
	 * @param pd
	 * @return List<SBDayageSettingSub>
	 * @throws Exception
	 */
	List<PageData> selectByCondition1(PageData pd) throws Exception;
	List<PageData> selectByCondition2(PageData pd) throws Exception;
	List<PageData> selectByCondition3(PageData pd) throws Exception;
	
	/**
	 * 新增
	 * @param pd
	 * @throws Exception
	 */
	void saveSBDayageSettingSub(PageData pd) throws Exception;
	void saveSBDayageSettingSub2(PageData pd) throws Exception;
	
	/**
	 * 逻辑删除
	 * @param pd
	 * @throws Exception
	 */
	void deleteSBDayageSettingSub(PageData pd) throws Exception;
	
	/**
	 * 物理删除
	 * @param pd
	 * @throws Exception
	 */
	void deleteSBDayageSettingSub2(PageData pd) throws Exception;
	
	/**
	 * 更新
	 * @param pd
	 * @throws Exception
	 */
	void updateSBDayageSettingSub(PageData pd) throws Exception;
	
	/**
	 * 新增子表
	 * @param pd
	 * @throws Exception
	 */
	void saveSBDayageTempSub(List<PageData> pd) throws Exception;
	void saveSBDayageTempSub2(PageData pd) throws Exception;
	
	/**
	 * 逻辑删除子表
	 * @param pd
	 * @throws Exception
	 */
	void deleteSBDayageTempSub(PageData pd) throws Exception;
	
	/**
	 * 物理删除子表
	 * @param pd
	 * @throws Exception
	 */
	void deleteSBDayageTempSub2(PageData pd) throws Exception;
	
	/**
	 * 按条件查询
	 * @param pd
	 * @return List<SBDayageTempSub>
	 * @throws Exception
	 */
	List<PageData> selectSBDayageTempSubByCondition(PageData pd) throws Exception;
	
	/**
	 * 更新
	 * @param pd
	 * @throws Exception
	 */
	void updateSBDayageTempSub(List<PageData> pd) throws Exception;
	
	/**
	 * 按条件查询 栋舍报警
	 * @param pd
	 * @return List<SBHouseAlarm>
	 * @throws Exception
	 */
	List<PageData> selectSBHouseAlarmByCondition(PageData pd) throws Exception;
	
	/**
	 * 更新栋舍报警
	 * @param pd
	 * @throws Exception
	 */
	void updateSBHouseAlarm(PageData pd) throws Exception;
	
	/**
	 * 按条件查询 报警人设置
	 * @param pd
	 * @return List<SBReminder>
	 * @throws Exception
	 */
	List<PageData> selectSBReminderByCondition(PageData pd) throws Exception;
	
	/**
	 * 新增报警人设置
	 * @param pd
	 * @throws Exception
	 */
	void saveSBReminder(PageData pd) throws Exception;
	
	/**
	 * 更新报警人设置
	 * @param pd
	 * @throws Exception
	 */
	void updateSBReminder(PageData pd) throws Exception;
	
	/**
	 * 查询 提醒启用设置 
	 * @param pd
	 * @return List<SBRemindSwitch>
	 * @throws Exception
	 */
	List<PageData> selectSBRemindSwitch(PageData pd) throws Exception;
	
	/**
	 * 新增提醒启用设置 
	 * @param pd
	 * @throws Exception
	 */
	void saveSBRemindSwitch(PageData pd) throws Exception;
	
	/**
	 * 更新提醒启用设置 
	 * @param pd
	 * @throws Exception
	 */
	void updateSBRemindSwitch(PageData pd) throws Exception;
	
	/**
	 * 查询 提醒指标配置
	 * @param pd
	 * @return List<SBRemindSwitch>
	 * @throws Exception
	 */
	List<PageData> selectSBRemindAlarmcodeByCondition(PageData pd) throws Exception;
	
	/**
	 * 新增提醒指标配置
	 * @param pd
	 * @throws Exception
	 */
	void saveSBRemindAlarmcode(PageData pd) throws Exception;
	
	/**
	 * 查询 农场提醒设置
	 * @param pd
	 * @return List<SBRemindSwitch>
	 * @throws Exception
	 */
	List<PageData> selectSBRemindSetting(PageData pd) throws Exception;
	
	/**
	 * 新增 农场提醒设置
	 * @param pd
	 * @throws Exception
	 */
	void saveSBRemindSetting(PageData pd) throws Exception;
	
	public List<PageData> findUserAll(PageData pd)throws Exception;
	
	public List<PageData> selectSBCode(PageData pd)throws Exception;
	
	/**
	 * 根据栋舍查询对应探头
	 * @param pd
	 * @return List<>
	 * @throws Exception
	 */
	public List<PageData> selectInsideTemp(PageData pd)throws Exception;
	
	public List<PageData> selectDevice(PageData pd)throws Exception;
	
	/**
	 * 更新栋舍探头设置
	 * @param pd
	 * @throws Exception
	 */
	void updateDeviceSub(PageData pd) throws Exception;
	
	/**
	 * 新增历史日龄目标温度设置
	 * @param pd
	 * @throws Exception
	 */
	void saveSbDayageSettingSubHis(PageData pd) throws Exception;
	
	/**
	 * 新增栋舍报警设置历史设置
	 * @param pd
	 * @throws Exception
	 */
	void saveSbHouseAlarmHis(PageData pd) throws Exception;

}
