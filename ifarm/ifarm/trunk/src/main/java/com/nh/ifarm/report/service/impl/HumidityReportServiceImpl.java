package com.nh.ifarm.report.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.nh.ifarm.report.service.HumidityReportService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

@Service
public class HumidityReportServiceImpl implements HumidityReportService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Override
	public List<PageData> getHumidityReport(PageData pd) throws Exception {

		return (List<PageData>) dao.findForList("HumidityReportMapper.HumidityReportDaily",pd);
	}

	@Override
	public List<PageData> getHumidityReportMonth(PageData pd) throws Exception {
		
		return (List<PageData>) dao.findForList("HumidityReportMapper.HumidityReportMonth",pd);
	}

}
