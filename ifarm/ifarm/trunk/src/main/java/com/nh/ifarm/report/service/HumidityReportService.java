package com.nh.ifarm.report.service;

import java.util.List;

import com.nh.ifarm.util.common.PageData;

public interface HumidityReportService {
	/**
     * 按条件查询
     * @param pd
     * @return List<SDFarm>
     * @throws Exception
     */
	List<PageData> getHumidityReport(PageData pd) throws Exception;
	
	
	/**
     * 按条件查询湿度曲线图月表
     * @param pd
     * @return List<SDFarm>
     * @throws Exception
     */
	public List<PageData> getHumidityReportMonth(PageData pd) throws Exception ;
}
