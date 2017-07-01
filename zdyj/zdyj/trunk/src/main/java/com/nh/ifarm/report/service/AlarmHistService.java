package com.nh.ifarm.report.service;

import java.util.List;

import com.nh.ifarm.util.common.PageData;

public interface AlarmHistService {

	 /**
    * 按条件查询
    * @param pd
    * @return List<SDFarm>
    * @throws Exception
    */
	List<PageData> getAlarmHist(PageData pd) throws Exception;
	
	
	/**
    * 按条件查询警报历史统计表
    * @param pd
    * @return List<SDFarm>
    * @throws Exception
    */
	public List<PageData> getAlarmHistDetail(PageData pd) throws Exception ;
	
	/**
	 * 查找警报类型
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectSBCode() throws Exception;

	/***
	 * 查询报警次数
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	PageData selectAlarmForMobile(PageData pd) throws Exception;
}
