package com.nh.ifarm.monitor.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.nh.ifarm.monitor.service.MonitorService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

@Service
public class MonitorServiceImpl implements MonitorService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	public List<PageData> selectAll(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("MonitorCurrMapper.selectAll", pd);
	}

	public List<PageData> selectAllForMobile() throws Exception{
		return (List<PageData>) dao.findForList("MonitorCurrMapper.selectAllForMobile", null);
	}

	public List<PageData> selectByCondition(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("MonitorCurrMapper.selectByCondition", pd);
	}

	public PageData selectAlarmCounts(PageData pd) throws Exception{
		return (PageData) dao.findForObject("MonitorCurrMapper.selectAlarmCounts", pd);
	}
	
	public List<PageData> selectMonitorBatch(PageData pd) throws Exception{
		List<PageData> list=null;
		if(Boolean.valueOf(pd.get("farmOrHouse").toString()).booleanValue()){
			if(Boolean.valueOf(pd.get("isShowRoujiOrZhongji").toString()).booleanValue()){
				if(Boolean.valueOf(pd.get("isShowDanjiOrYucheng").toString()).booleanValue()){
					list = (List<PageData>) dao.findForList("MonitorCurrMapper.selectByYuchengDongshe", pd);
				}else{
					list = (List<PageData>) dao.findForList("MonitorCurrMapper.selectByDanjiDongshe", pd);
				}
			}else{
				list = (List<PageData>) dao.findForList("MonitorCurrMapper.selectByRoujiDongshe", pd);
			}
		}else{
			if(Boolean.valueOf(pd.get("isShowRoujiOrZhongji").toString()).booleanValue()){
				if(Boolean.valueOf(pd.get("isShowDanjiOrYucheng").toString()).booleanValue()){
					list = (List<PageData>) dao.findForList("MonitorCurrMapper.selectByYuchengNongchang", pd);
				}else{
					list = (List<PageData>) dao.findForList("MonitorCurrMapper.selectByDanjiNongchang", pd);
				}
			}else{
				list = (List<PageData>) dao.findForList("MonitorCurrMapper.selectByRoujiNongchang", pd);
			}
		}
		return list;
	}

	public List<PageData> selectCategoryType(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("MonitorCurrMapper.selectCategoryType", pd);
	}

	/**
	 * 运行调度任务
	 * @throws Exception
	 */
	public void run() {
		try{
			dao.save("MonitorCurrMapper.SP_MONITOR",null);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
  
}  
