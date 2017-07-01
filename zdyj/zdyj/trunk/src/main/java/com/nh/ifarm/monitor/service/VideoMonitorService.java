package com.nh.ifarm.monitor.service;

import com.nh.ifarm.util.common.PageData;
//import com.sun.tools.javac.util.List;
import java.util.List;

/**
 * Created by Ants on 2016/8/25.
 */
public interface VideoMonitorService {
    /**
	 * 按条件查询
	 * @param pd
	 * @return List<videoMonitor>
	 * @throws Exception
	 */
	List<PageData> selectByCondition(PageData pd) throws Exception;
}
