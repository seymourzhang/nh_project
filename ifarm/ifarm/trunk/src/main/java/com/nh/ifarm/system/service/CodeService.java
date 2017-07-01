package com.nh.ifarm.system.service;

import java.util.List;

import com.nh.ifarm.util.common.PageData;

public interface CodeService {
	
	public List<PageData> getCodeList(PageData pd) throws Exception;
	PageData getCodeForFeedType(PageData pd ) throws Exception;
}
