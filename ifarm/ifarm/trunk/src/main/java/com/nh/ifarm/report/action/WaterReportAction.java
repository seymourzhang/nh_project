package com.nh.ifarm.report.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.nh.ifarm.util.service.ModuleService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nh.ifarm.report.service.WaterReportService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.DateUtil;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;

@Controller
@RequestMapping("/waterReport")
public class WaterReportAction extends BaseAction {
	@Autowired
	private WaterReportService waterReportService;
	
	
	@Autowired
	private ModuleService moduleService;
	
	@RequestMapping("/showWaterReport")
	public ModelAndView showWaterReport() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String id = pd.getString("id");
		String pid = pd.getString("pid");
		
		/**当天日期**/
		String beginTime=DateUtil.getDay();
		String endTime=DateUtil.getDay();
		pd.put("beginTime", beginTime);
		pd.put("endTime", endTime);
		
		pd.put("id", id);
		pd.put("pid", pid);
		List<PageData> wr=waterReportService.getWaterReport(pd);
		mv.setViewName("/modules/report/waterReport");
		mv.addObject("WaterReport",wr);
		mv.addObject("farmList",getFarmList());
		mv.addObject("houseList",getHouseList(pd));
		mv.addObject("batchList",getBatchList(pd));
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**
	 * 根据查询条件查询温度曲线图
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/queryWaterReport")
	public void queryWaterReport(HttpServletResponse response) throws Exception{
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
		
		List<PageData> wr=new ArrayList<PageData>();
		if(buttonValue.equals("week")){//近一周
			beginTime = DateUtil.getStringByDate(DateUtils.addDays(date, -7));
			endTime = DateUtil.getStringByDate(date);
			pd.put("beginTime", beginTime);
			pd.put("endTime", endTime);
			wr=waterReportService.getWaterReportMonth(pd);
			
		}else if(buttonValue.equals("month")){//近一个月
			beginTime = DateUtil.getStringByDate(DateUtils.addMonths(date, -1));
			endTime = DateUtil.getStringByDate(date);
			pd.put("beginTime", beginTime);
			pd.put("endTime", endTime);
			wr=waterReportService.getWaterReportMonth(pd);
		}else{ //当日 或者是没选择日期
			 pd.put("beginTime", beginTime);
			 pd.put("endTime", endTime);
			wr=waterReportService.getWaterReport(pd);
		}
		j.setSuccess(true);
		j.setObj(wr);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/waterUrl")
	public ModelAndView waterUrl()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.setViewName("modules/report/waterCompare");
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
