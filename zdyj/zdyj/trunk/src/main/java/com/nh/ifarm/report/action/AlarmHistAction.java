package com.nh.ifarm.report.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nh.ifarm.report.service.AlarmHistService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.DateUtil;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.service.ModuleService;

@Controller
@RequestMapping("/alarmHist")
public class AlarmHistAction extends BaseAction {
     
	@Autowired
	private AlarmHistService alarmHistService;
	
	@Autowired
	private ModuleService moduleService;
	
	@RequestMapping("/showAlarmHist")
	public ModelAndView showAlarmHist() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
//		pd.put("farmId", getFarmList().get(0).get("id"));
//		pd.put("houseId", getHouseList(pd).get(0).get("id"));
//		if(getBatchList(pd).size()!=0){
//		pd.put("batchNo", getBatchList(pd).get(0).get("batch_no"));
//		}
//		List<PageData> alarm = alarmHistService.getAlarmHist(pd);
		mv.setViewName("/modules/report/alarmHist");
//		mv.addObject("AlarmHist",alarm);
//		mv.addObject("farmList",getFarmList());
//		mv.addObject("houseList",getHouseList(pd));
		mv.addObject("alarmNameList",getAlarmNameList());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String s = sdf.format(new Date());
			pd.put("beginTime", s);	
			pd.put("endTime", s);
		mv.addObject("pd",pd);
		mv.addObject("farm_id",pd.get("farm_id"));
		mv.addObject("house_id",pd.get("house_id"));
		mv.addObject("corporation_id",pd.get("corporation_id"));
		mv.addObject("batch_no",pd.get("batch_no"));
		return mv;
	}
	
	/**
	 * 主查询
	 * @param id
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryAlarmHist2")
	public void queryAlarmHist2(HttpServletResponse response, HttpSession session) throws Exception {
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl = alarmHistService.getAlarmHist(pd);		
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/queryAlarmHist3")
	public void queryAlarmHist3(HttpServletResponse response, HttpSession session) throws Exception {
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl = new ArrayList<PageData>();
		mcl = alarmHistService.getAlarmHistDetail(pd);		
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/queryAlarmHist4")
	public ModelAndView queryAlarmHist4() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		List<PageData> alarm = alarmHistService.getAlarmHistDetail(pd);
		mv.setViewName("/modules/report/alarmHist");
		mv.addObject("AlarmHistDetail",alarm);
		mv.addObject("farmList",getFarmList());
		mv.addObject("houseList",getHouseList(pd));
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**
	 * 根据查询条件查询记录
	 * @param response
	 * @throws Exception
	 */
//	@RequestMapping("/queryAlarmHist")
//	public void queryAlarmHist(HttpServletResponse response) throws Exception{
//		Json j=new Json();
//		PageData pd = this.getPageData();
//		String buttonValue= pd.getString("buttonValue");
//		String queryTime= pd.getString("queryTime");
//		String beginTime=DateUtil.getDay();
//		String endTime=DateUtil.getDay();
//		Date date=new Date();
//		if(!"".equals(queryTime)){
//			 beginTime=queryTime;
//			 endTime=queryTime;
//			 date=DateUtil.fomatDate(queryTime);
//		}
//		
//		List<PageData> alarm=new ArrayList<PageData>();
//		if(buttonValue.equals("week")){//近一周
//			beginTime = DateUtil.getStringByDate(DateUtils.addDays(date, -7));
//			endTime = DateUtil.getStringByDate(date);
//			pd.put("beginTime", beginTime);
//			pd.put("endTime", endTime);
//			alarm=alarmHistService.getAlarmHistMonth(pd);
//			
//		}else if(buttonValue.equals("month")){//近一个月
//			beginTime = DateUtil.getStringByDate(DateUtils.addMonths(date, -1));
//			endTime = DateUtil.getStringByDate(date);
//			pd.put("beginTime", beginTime);
//			pd.put("endTime", endTime);
//			alarm=alarmHistService.getAlarmHistMonth(pd);
//		}else{ //当日 或者是没选择日期
//			 pd.put("beginTime", beginTime);
//			 pd.put("endTime", endTime);
//			 alarm=alarmHistService.getAlarmHist(pd);
//		}
//		j.setSuccess(true);
//		j.setObj(alarm);
//		super.writeJson(j, response);
//	}
//	
	@RequestMapping("/getHouse")
	public void getHouse(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl = getHouseList(pd);
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}
//	
	@RequestMapping("/getAlarmName")
	public void getAlarmName(HttpServletResponse response) throws Exception{
		Json j=new Json();
//		PageData pd = this.getPageData();
		List<PageData> mcl = getAlarmNameList();
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}
	
	/**
	 * 获取农场信息
	 * @param pd 数据对象
	 * @return 数据列表
     */
	List<PageData> getFarmList() throws Exception {
		List<PageData> mcl;
			mcl = moduleService.service("farmServiceImpl", "selectAll", null);//farmService.selectAll();
		return mcl;
	}
	
	/**
	 * 获取栋舍信息
	 * @param pd 数据对象
	 * @return 数据列表
     */
	List<PageData> getHouseList(PageData pd) throws Exception {
		List<PageData> mcl;
			mcl = moduleService.service("farmServiceImpl", "selectHouseByCondition", new Object[]{pd});//farmService.selectHouseAll();
		return mcl;
	}
	
	/**
	 * 获取报警类型
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> getAlarmNameList() throws Exception {
		List<PageData> mcl;
			mcl = alarmHistService.selectSBCode();
		return mcl;
	}
	
	/**
	 * 获取农场栋舍批次信息
	 * @param pd 数据对象
	 * @return 数据列表
     */
	List<PageData> getBatchList(PageData pd) throws Exception {
		List<PageData> mcl;
			mcl = moduleService.service("farmServiceImpl", "selectBatchByCondition", new Object[]{pd});//farmService.selectHouseAll();
		return mcl;
	}
	
}
