package com.nh.ifarm.report.service;

import java.util.List;

import com.nh.ifarm.util.common.PageData;

public interface NegativePressureService {
	/**
     * 按条件查询
     * @param pd
     * @return List<SDFarm>
     * @throws Exception
     */
	List<PageData> getNegativePressure(PageData pd) throws Exception;
	
	
	/**
     * 按条件查询饮水曲线图月表
     * @param pd
     * @return List<SDFarm>
     * @throws Exception
     */
	public List<PageData> getNegativePressureMonth(PageData pd) throws Exception ;
}
