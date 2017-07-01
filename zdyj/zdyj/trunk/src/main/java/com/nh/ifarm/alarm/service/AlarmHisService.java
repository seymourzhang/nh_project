package com.nh.ifarm.alarm.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nh.ifarm.util.common.PageData;

/**
 * Created by yoven on 12/26/2016.
 */
public interface AlarmHisService {
	/**
	 * 按条件查询
	 * @param pd
	 * @return List<SBDayageSettingSubHis>
	 * @throws Exception
	 */
	List<PageData> selectAlarmHisByCondition(PageData pd) throws Exception;
	
	/**
	 * 按条件查询
	 * @param pd
	 * @return List<SBDayageSettingSubHis>
	 * @throws Exception
	 */
	List<PageData> selectAlarmHisDetailByCondition(PageData pd) throws Exception;
	
	List<PageData> selectHouseAlarmHisByCondition(PageData pd) throws Exception;
	
	List<PageData> selectHouseAlarmHisDetailByCondition(PageData pd) throws Exception;

}
