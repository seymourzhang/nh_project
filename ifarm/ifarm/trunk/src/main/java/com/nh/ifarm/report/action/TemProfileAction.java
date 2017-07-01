package com.nh.ifarm.report.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.nh.ifarm.report.service.TemProfileService;
import com.nh.ifarm.util.common.DateUtil;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.service.ModuleService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nh.ifarm.util.action.BaseAction;

/**
 * @ClassName: TemProfileAction
 * @Date 2016-7-14
 * @author Varro
 * 温度曲线图控制类
 */

@Controller
@RequestMapping("/temProfile")
public class TemProfileAction extends BaseAction {
	
	@Autowired
	private TemProfileService temProfileService;
	
	
	@Autowired
	private ModuleService moduleService;
	
	
	
	@RequestMapping("/showTemProfile")
	public ModelAndView showTemProfile() throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id", getUserId());
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("modules/report/temProfile");
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
//		List<PageData> tem=temProfileService.getTemProfile(pd);
//		mv.setViewName("/modules/report/temProfile");
//		mv.addObject("TemProfile",tem);
//
//
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
	@RequestMapping("/queryTemProfile")
	public void queryTemProfile(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		String buttonValue= pd.getString("buttonValue");
		if(buttonValue==null){
			buttonValue=pd.getString("btValue");
		}
		String queryTime= pd.getString("queryTime");
		String beginTime= DateUtil.getDay();
		String endTime=DateUtil.getDay();
		Date date=new Date();
		if(!"".equals(queryTime)){
			 beginTime=queryTime;
			 endTime=queryTime;
			 date=DateUtil.fomatDate(queryTime);
		}
		
		List<PageData> tem=new ArrayList<PageData>();
		if(buttonValue.equals("week")){//近一周
			beginTime = DateUtil.getStringByDate(DateUtils.addDays(date, -7));
			endTime = DateUtil.getStringByDate(date);
			pd.put("beginTime", beginTime);
			pd.put("endTime", endTime);
			tem=temProfileService.getTemProfileMonth(pd);
			
		}else if(buttonValue.equals("month")){//近一个月
			beginTime = DateUtil.getStringByDate(DateUtils.addMonths(date, -1));
			endTime = DateUtil.getStringByDate(date);
			pd.put("beginTime", beginTime);
			pd.put("endTime", endTime);
			tem=temProfileService.getTemProfileMonth(pd);
		}else{ //当日 或者是没选择日期
			 pd.put("beginTime", beginTime);
			 pd.put("endTime", endTime);
			 tem=temProfileService.getTemProfile(pd);
		}
		j.setSuccess(true);
		j.setObj(tem);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/tempCompareUrl")
	public ModelAndView tempCompareUrl()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.setViewName("modules/report/tempCompare");
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
