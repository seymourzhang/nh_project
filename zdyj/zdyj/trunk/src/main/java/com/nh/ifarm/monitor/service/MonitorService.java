package com.nh.ifarm.monitor.service;

import java.util.List;

import com.nh.ifarm.util.common.PageData;

/**
 * Created by LeLe on 6/30/2016.
 */
public interface MonitorService {
    /**
     * 查询全部
     * @return List<MonitorCurr>
     * @throws Exception
     */
	List<PageData> selectAll(PageData pd) throws Exception;

	/**
	 * 查询全部 （移动专用）
	 * @return List<MonitorCurr>
	 * @throws Exception
	 */
	List<PageData> selectAllForMobile() throws Exception;
    
	/**
	 * 按条件查询
	 * @param pd
	 * @return List<MonitorCurr>
	 * @throws Exception
	 */
	List<PageData> selectByCondition(PageData pd) throws Exception;

	/**
	 * 查询报警数
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	PageData selectAlarmCounts(PageData pd) throws Exception;

	/**
	 * 运行定时任务
	 * @throws Exception
     */
	void run() ;

}
