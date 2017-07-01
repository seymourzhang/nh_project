package com.nh.ifarm.report.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.nh.ifarm.report.service.WaterReportService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

@Service
public class WaterReportServiceImpl implements WaterReportService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public List<PageData> getWaterReport(PageData pd) throws Exception {
		
		return (List<PageData>) dao.findForList("WaterReportMapper.WaterReportDaily", pd);
	}

	@Override
	public List<PageData> getWaterReportMonth(PageData pd) throws Exception {
		
		return (List<PageData>) dao.findForList("WaterReportMapper.WaterReportMonth", pd);
	}

}
