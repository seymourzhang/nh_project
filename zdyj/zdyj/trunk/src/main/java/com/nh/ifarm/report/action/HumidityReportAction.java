package com.nh.ifarm.report.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nh.ifarm.report.service.HumidityReportService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.DateUtil;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.service.ModuleService;

@Controller
@RequestMapping("/humidityReport")
public class HumidityReportAction extends BaseAction {
	
	@Autowired
	private HumidityReportService humidityReportService;
	
	
	@Autowired
	private ModuleService moduleService;
	
	@RequestMapping("/showHumidityReport")
	public ModelAndView showHumidityReport() throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id", getUserId());
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("modules/report/humidityReport");
		mv.addObject("pd",pd);
		return mv;
//		ModelAndView mv = this.getModelAndView();
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		String id = pd.getString("id");
//		String pid = pd.getString("pid");
//
//		/**当天日期**/
//		String beginTime=DateUtil.getDay();
//		String endTime=DateUtil.getDay();
//		pd.put("beginTime", beginTime);
//		pd.put("endTime", endTime);
//
//		pd.put("id", id);
//		pd.put("pid", pid);
//		List<PageData> hum=humidityReportService.getHumidityReport(pd);
//		mv.setViewName("/modules/report/humidityReport");
//		mv.addObject("HumidityReport",hum);
//		mv.addObject("farmList",getFarmList());
//		mv.addObject("houseList",getHouseList(pd));
//		mv.addObject("batchList",getBatchList(pd));
//		mv.addObject("pd",pd);
//		return mv;
	}
	
	/**
	 * 根据查询条件查询温度曲线图
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/queryHumidityReport")
	public void queryHumidityReport(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		String buttonValue= pd.getString("buttonValue");
		if(buttonValue==null){
			buttonValue=pd.getString("btValue");
		}
		String queryTime= pd.getString("queryTime");
		String beginTime=DateUtil.getDay();
		String endTime=DateUtil.getDay();
		Date date=new Date();
		if(!"".equals(queryTime)){
			 beginTime=queryTime;
			 endTime=queryTime;
			 date=DateUtil.fomatDate(queryTime);
		}
		
		List<PageData> hum=new ArrayList<PageData>();
		if(buttonValue.equals("week")){//近一周
			beginTime = DateUtil.getStringByDate(DateUtils.addDays(date, -7));
			endTime = DateUtil.getStringByDate(date);
			pd.put("beginTime", beginTime);
			pd.put("endTime", endTime);
			hum=humidityReportService.getHumidityReportMonth(pd);
			
		}else if(buttonValue.equals("month")){//近一个月
			beginTime = DateUtil.getStringByDate(DateUtils.addMonths(date, -1));
			endTime = DateUtil.getStringByDate(date);
			pd.put("beginTime", beginTime);
			pd.put("endTime", endTime);
			hum=humidityReportService.getHumidityReportMonth(pd);
		}else{ //当日 或者是没选择日期
			 pd.put("beginTime", beginTime);
			 pd.put("endTime", endTime);
			 hum=humidityReportService.getHumidityReport(pd);
		}
		j.setSuccess(true);
		j.setObj(hum);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/humiUrl")
	public ModelAndView humiUrl()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.setViewName("modules/report/humiCompare");
		mv.addObject("farmList",getFarmList());
		mv.addObject("houseList",getHouseList(pd));
		mv.addObject("batchList",getBatchList(pd));
		mv.addObject("pd",pd);
		return mv;
	}
	
	@RequestMapping("/getHouse")
	public void getHouse(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl = getHouseList(pd);
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}
	
	
	@RequestMapping("/getBatch")
	public void getBatch(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl = getBatchList(pd);
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}
	
	/**
	 * 获取农场信息
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
