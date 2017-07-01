package com.nh.ifarm.alarm.service;

import java.util.List;

import com.nh.ifarm.util.common.PageData;

public interface AlarmCurrService {
	    /**
	     * 查询全部
	     * @return List<AlarmCurr>
	     * @throws Exception
	     */
		List<PageData> selectAll() throws Exception;
	    
		/**
		 * 按条件查询
		 * @param pd
		 * @return List<AlarmCurr>
		 * @throws Exception
		 */
		List<PageData> selectByCondition(PageData pd) throws Exception;

		/**
		 * 根据栋舍Id查找有权限听到报警声音的用户手机IMEI
		 */
		List<PageData> selectImeiByHouseId(PageData pd) throws Exception;

		/**
		 * 运行定时任务
		 * @throws Exception
	     */
		void run() ;
		
		public int updateAlarm(PageData pd) throws Exception;

		public int insertPushLog(PageData pd) throws Exception;
}
