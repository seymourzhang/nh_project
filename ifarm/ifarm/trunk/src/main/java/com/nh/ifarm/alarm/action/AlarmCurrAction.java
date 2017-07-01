package com.nh.ifarm.alarm.action;

import com.nh.ifarm.alarm.service.AlarmCurrService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.*;
import com.nh.ifarm.util.service.ModuleService;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: AlarmCurrAction
 * @Date 2016-6-30
 * @author zl
 * 实时警报
 */

@Controller
@RequestMapping("/alarmCurr")
public class AlarmCurrAction extends BaseAction{
    @Autowired 
	private AlarmCurrService alarmCurrService;

	static final String alarmCurrList="alarmCurrList";
	
	@Autowired
	private ModuleService moduleService;

	@RequestMapping("/showAlarmCurr")
	public ModelAndView showAlarmCurr(HttpSession session) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		pd.put("user_id",user.getId());
		List<PageData> mcl = getList(pd);
		mv.setViewName("/modules/alarm/alarmCurr");
		mv.addObject(alarmCurrList,mcl);
		mv.addObject("farm_id",pd.get("farm_id"));
		mv.addObject("corporation_id",pd.get("corporation_id"));
		mv.addObject("farmList",getFarmList());
		mv.addObject("houseList",getHouseList(pd));
		return mv;
	}

	@RequestMapping("/reflushAlarmCurr")
	public void reflushAlarmCurr(HttpServletResponse response, HttpSession session) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		pd.put("user_id",user.getId());
		List<PageData> mcl = getList(pd);
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/reflushAlarmCurr2")
	public void reflushAlarmCurr2(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl = getHouseList(pd);
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/mobileAlarm")
	public void mobileAlarm(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String dealRes = null ;
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("deal_status", "01");
		JSONObject jfm=new JSONObject();
		List<PageData> mcl = alarmCurrService.selectByCondition(pd);
		if(mcl!=null&&mcl.size()!=0){
//			jfm.put("monitor", mcl);
			jfm.put("Result", "Success");
			jfm.put("AlarmStatus", "1");
			dealRes = Constants.RESULT_SUCCESS ;
		}else{
			dealRes = Constants.RESULT_SUCCESS ;
			jfm.put("AlarmStatus", "0");
			jfm.put("ErrorMsg","未查询到数据！");
		}
		DealSuccOrFail.dealApp(request,response,dealRes,jfm);
	}
	
	@RequestMapping("/mobileAlarmCurr")
	public void mobileAlarmCurr(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String dealRes = null ;
		PageData pd = new PageData();
		pd = this.getPageData();
		String aa=pd.toString();
		aa=aa.substring(1,aa.length()-2);
		JSONObject jsonObject = new JSONObject(aa);
		JSONObject tUserJson = jsonObject.getJSONObject("params");
		String farmId= tUserJson.optString("FarmId");
		String houseId= tUserJson.optString("HouseId");
		pd.put("farmId", farmId);
		pd.put("houseId", houseId);
		JSONObject jfm=new JSONObject();
		List<PageData> mcl = alarmCurrService.selectByCondition(pd);
		if(mcl!=null){
			jfm.put("alarmMessage", mcl);
			jfm.put("Result", "Success");
			dealRes = Constants.RESULT_SUCCESS ;
		}else{
			dealRes = Constants.RESULT_FAIL ;
			jfm.put("ErrorMsg","未查询到数据！");
		}
		DealSuccOrFail.dealApp(request,response,dealRes,jfm);
	}
	
	@RequestMapping("/mobilesSolveAlarmCurr")
	public void mobilesSolveAlarmCurr(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String dealRes = null ;
		JSONObject jfm=new JSONObject();
		PageData pd = new PageData();
		pd = this.getPageData();
		String aa=pd.toString();
		aa=aa.substring(1,aa.length()-2);
		JSONObject jsonObject = new JSONObject(aa);
		JSONObject tUserJson = jsonObject.getJSONObject("params");
		String houseId= tUserJson.optString("houseId");
		String delayTime= tUserJson.optString("delayTime");
		String deal_time= tUserJson.optString("deal_time");
		String response_person= tUserJson.optString("response_person");
		pd.put("house_id", houseId);
		pd.put("deal_delay", delayTime);
		pd.put("deal_status", "02");
		pd.put("response_person", response_person);
		if(!StringUtils.isBlank(deal_time)){
			pd.put("deal_time", deal_time);
		}else{
			pd.put("deal_time", new Date());
		}
		
		int num=alarmCurrService.updateAlarm(pd);
		if(num!=0){
			jfm.put("Result", "Success");
			dealRes = Constants.RESULT_SUCCESS ;
		}else{
			dealRes = Constants.RESULT_FAIL ;
			jfm.put("ErrorMsg","没有要处理的信息！");
		}
		DealSuccOrFail.dealApp(request,response,dealRes,jfm);
	}
	
	

	/**
	 * 获取数据列表
	 * @param pd 数据对象
	 * @return 数据列表
     */
	List<PageData> getList(PageData pd) throws Exception {
		List<PageData> mcl;
		if(pd.get("farmId")!=null || pd.get("houseId")!=null) {
			mcl = alarmCurrService.selectByCondition(pd);
		} else {
			mcl = alarmCurrService.selectAll(pd);
		}
		return mcl;
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
}
