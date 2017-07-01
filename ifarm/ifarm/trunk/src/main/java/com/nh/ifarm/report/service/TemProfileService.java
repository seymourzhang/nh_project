package com.nh.ifarm.report.service;

import java.util.List;

import com.nh.ifarm.util.common.PageData;

public interface TemProfileService {
	
	
	
	 /**
     * 按条件查询
     * @param pd
     * @return List<SDFarm>
     * @throws Exception
     */
	List<PageData> getTemProfile(PageData pd) throws Exception;
	
	
	/**
     * 按条件查询温度曲线图月表
     * @param pd
     * @return List<SDFarm>
     * @throws Exception
     */
	public List<PageData> getTemProfileMonth(PageData pd) throws Exception ;


	/**
	 * （app）温度查询--按日龄
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectTemForMobileDay(PageData pd) throws Exception;

	/**
	 * （app）温度查询--按小时
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectTemForMobileHour(PageData pd) throws Exception;

	/**
	 * （app）温度曲线--按分钟
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectTemForMobileMinute(PageData pd) throws Exception;

	/**
	 * （app）co2和光照查询--按日龄
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectLCForMobileDay(PageData pd) throws Exception;

	/**
	 * （app）co2和光照查询--按小时
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectLCForMobileHour(PageData pd) throws Exception;

	/**
	 * （app）co2和光照查询--按分钟
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectLCForMobileMinute(PageData pd) throws Exception;

	/**
	 * （app）co2和氨气查询--按日龄
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectCO2NH4ForMobileDay(PageData pd) throws Exception;

	/**
	 * （app）co2和氨气查询--按小时
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectCO2NH4ForMobileHour(PageData pd) throws Exception;

	/**
	 * （app）co2和氨气查询--按分钟
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectCO2NH4ForMobileMinute(PageData pd) throws Exception;
}
