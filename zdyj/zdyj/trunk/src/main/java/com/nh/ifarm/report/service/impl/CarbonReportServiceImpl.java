package com.nh.ifarm.report.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.nh.ifarm.report.service.CarbonReportService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

@Service
public class CarbonReportServiceImpl implements CarbonReportService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Override
	public List<PageData> getCarbonReport(PageData pd) throws Exception {
		
		return (List<PageData>) dao.findForList("CarbonReportMapper.CarbonReportDaily", pd);
	}

	@Override
	public List<PageData> getCarbonReportMonth(PageData pd) throws Exception {
		
		return (List<PageData>) dao.findForList("CarbonReportMapper.CarbonReportMonth", pd);
	}

}
