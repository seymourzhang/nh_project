/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.mtc.scheduler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mtc.app.biz.AlarmReqManager;
import com.mtc.common.util.IPUtil;

/**
 * @ClassName: GatewayExecute
 * @Description: 
 * @Date 2015年11月30日 上午11:24:32
 * @Author Yin Guo Xiang
 * 
 */

public class GatewayExecuteJob {
	private static Logger mLogger =Logger.getLogger(GatewayExecuteJob.class);
	
	@Autowired
	private AlarmReqManager tAlarmReqManager;
	
	private static int counter = 0;
	
	public void Service() {
		if(!IPUtil.needRunTask()){
			return ;
		}
		counter ++ ;
		mLogger.info("=====Now 开始  第 " + counter +" 次 GatewayExecuteJob.Service");
		try {
			Thread.sleep(2000);
			mLogger.info("=====AlarmReqManager:"+tAlarmReqManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mLogger.info("=====Now 结束  第 " + counter +" 次 GatewayExecuteJob.Service");
	}
}
