package com.nh.ifarm.alarm.action;


import com.nh.ifarm.alarm.service.AlarmService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.user.service.SDUserService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.common.PubFun;
import com.nh.ifarm.util.service.ModuleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: AlarmAction
 * @Date 2016-8-03
 * @author Yoven
 * 报警设置类
 */
@Controller
@RequestMapping("/alarm")
public class AlarmAction extends BaseAction{
	
	@Autowired
	private AlarmService alarmService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private SDUserService userService;
	
	String alarm_type = "1";

	@RequestMapping("/showAlarm")
	public ModelAndView showAlarm() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.setViewName("/modules/alarm/alarm");
		pd.put("code_type", "alarm_delay");
		mv.addObject("pd",pd);
		mv.addObject("alarm_delay",alarmService.selectSBCode(pd));
		pd.put("code_type", "time");
		Json j = new Json();
		j.setObj(alarmService.selectSBCode(pd));
		mv.addObject("hourList",j.getObj());
		mv.addObject("farm_id",pd.get("farm_id"));
		mv.addObject("house_id",pd.get("house_id"));
		mv.addObject("corporation_id",pd.get("corporation_id"));
		String mi = PubFun.getPropertyValue("Alarm.Index");
		mv.addObject("alarmIndex",mi.replace(" ","").toString());
		pd.put("code_type", "alarm_probe");
		List<PageData> alarmProbe = alarmService.selectSBCode(pd);
		List<PageData> alarmProbe2 = new ArrayList<PageData>();
		if(mi.replace(" ","").toString().indexOf("102")==-1){
		for(int i =0;i<alarmProbe.size();i++){
			if(!alarmProbe.get(i).get("code_name").equals("平均温度报警")){
				alarmProbe2.add(alarmProbe.get(i));
			}
		}
		mv.addObject("alarm_probe",alarmProbe2);
		}else{
			mv.addObject("alarm_probe",alarmProbe);
		}
		
		return mv;
	}
	
	@RequestMapping("/insideTemp")
	public void insideTemp(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> tem=new ArrayList<PageData>();
		pd.put("code_type", "SENSOR_LOCATION");
		tem = alarmService.selectInsideTemp(pd);
		j.setSuccess(true);
		j.setObj(tem);	
		super.writeJson(j, response);
	}
	
	@RequestMapping("/device")
	public void device(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> tem=new ArrayList<PageData>();
		tem = alarmService.selectDevice(pd);
		j.setSuccess(true);
		j.setObj(tem);	
		super.writeJson(j, response);
	}
	
	@RequestMapping("/updateDeviceSub")
	public void updateDeviceSub(HttpServletResponse response,HttpServletRequest request) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)request.getSession().getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		pd.put("modify_person", user.getId());
		pd.put("modify_date", new Date());
		alarmService.updateDeviceSub(pd);
		j.setSuccess(true);
		super.writeJson(j, response);
	}
	
	/**
	 * 根据查询条件查询报警曲线图
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/queryAlarm")
	public void queryAlarm(HttpServletResponse response,HttpServletRequest request) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		String alarmType= alarm_type;
		
		List<PageData> tem=new ArrayList<PageData>();
//		if(alarmType.equals("1")){//温度
			tem=alarmService.selectByCondition2(pd);
			
//		}else if(alarmType.equals("2")){//负压
//			tem=alarmService.selectByCondition2(pd);
//		}else if(alarmType.equals("3")){ //二氧化碳
//			 tem=alarmService.selectByCondition2(pd);
//		}else if(alarmType.equals("5")){ //湿度
//			 tem=alarmService.selectByCondition2(pd);
//		}else{//饮水量
//			tem=alarmService.selectByCondition2(pd);
//		}
//		PageData tem2 = new PageData();
//		tem2.put(alarmType, tem);
		j.setSuccess(true);
		j.setObj(tem);	
		j.setMsg(alarmType);
		super.writeJson(j, response);
	}
	
	/**
	 * 主查询
	 * @param id
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryAlarm2")
	public void queryAlarm2(HttpServletResponse response,HttpServletRequest request) throws Exception {
		Json j=new Json();
		PageData pd = this.getPageData();
		alarm_type = (String) pd.get("alarm_type");
		List<PageData> mcl = alarmService.selectByCondition(pd);
	    List<PageData> mc2 = alarmService.selectSBHouseAlarmByCondition(pd);
	    if(mc2.size()!=0){
		j.setObj1(mc2.get(0));
	    }else{
	    	j.setObj1("");
	    }
		j.setSuccess(true);
		j.setObj(mcl);	
		super.writeJson(j, response);
	}
	
	@RequestMapping("/deleteAlarm")
	public void deleteAlarm(HttpServletResponse response,HttpServletRequest request) throws Exception {
//		ModelAndView mv = this.getModelAndView();
		Json g = new Json();
		SDUser user = (SDUser) request.getSession().getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		pd.put("modify_person", user.getId());
		pd.put("modify_date", new Date());
		pd.put("modify_time", new Date());
		String tr = (String) pd.get("deleteRow");
		String[] alarmSet = tr.split(";");
		for (String as : alarmSet) {
			String[] alarmSet2 = as.split(",");
			int tt = Integer.parseInt(alarmSet2[0]);
			pd.put("uid_num", tt);
			pd.put("day_age", Integer.parseInt(alarmSet2[1])*7);

			g = tempSubDelete(pd, user.getId());

			alarmService.deleteSBDayageSettingSub(pd);
			pd.put("alarm_operation", "删除");
			alarmService.saveSbDayageSettingSubHis(pd);
			alarmService.deleteSBDayageTempSub(pd);
		}
		alarm_type = pd.get("alarm_type").toString();
		List<PageData> mcl = alarmService.selectByCondition(pd);
//	    List<PageData> mc2 = alarmService.selectSBHouseAlarmByCondition(pd);
//	    if(mc2.size()!=0){
//			g.setObj1(mc2.get(0));
//		    }else{
//		    	g.setObj1("");
//		    }
		g.setSuccess(true);
		g.setObj(mcl);
		super.writeJson(g, response);
	}

	public Json tempSubDelete(PageData pd, int userId){
		Json g = new Json();
		try {
			List<PageData> pageData1 = alarmService.selectByCondition(pd);
			float set_temp1=0,high_alarm_temp1=0,high_temp_warning1=0,low_alarm_temp1=0,low_temp_warning1=0,set_temp3=0,high_alarm_temp3=0,
					high_temp_warning3=0,low_alarm_temp3=0,low_temp_warning3=0,
					set_lux1=0,set_lux3=0,high_lux1=0,high_lux_warning1=0,high_lux3=0,high_lux_warning3=0,
					low_lux1=0,low_lux_warning1=0,low_lux3=0,low_lux_warning3=0,
					set_co21=0,high_alarm_co21=0,high_co2_warning1=0,set_co23=0,high_alarm_co23=0,high_co2_warning3=0,
					set_water_deprivation1=0,high_water_deprivation1=0,low_water_deprivation1=0,
					set_water_deprivation3=0,high_water_deprivation3=0,low_water_deprivation3=0,
					set_humidity1=0,high_alarm_humidity1=0,high_humidity_warning1=0,low_alarm_humidity1=0,low_humidity_warning1=0,set_humidity3=0,high_alarm_humidity3=0,
					high_humidity_warning3=0,low_alarm_humidity3=0,low_humidity_warning3=0,
					set_negative_pressure1=0,high_alarm_negative_pressure1=0,high_negative_pressure_warning1=0,low_alarm_negative_pressure1=0,low_negative_pressure_warning1=0,set_negative_pressure3=0,high_alarm_negative_pressure3=0,
				    high_negative_pressure_warning3=0,low_alarm_negative_pressure3=0,low_negative_pressure_warning3=0;
			int uid_num=0,day_age=0,day_age2=0,startTime2=0,endTime2=0;
			boolean choose=false,choose2 = false,choose3 = false;
			if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==1){
				//计算温度差、基值
				for(int i=0;i<pageData1.size();i++){
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i==0){
						if(pageData1.size() != 1){
							//目标温度2
							set_temp3 = Float.valueOf(pageData1.get(i+1).get("set_temp").toString()).floatValue();
							//高报温度2
							high_alarm_temp3 = Float.valueOf(pageData1.get(i+1).get("high_alarm_temp").toString()).floatValue();
							//高报温度警示2
							high_temp_warning3 = Float.valueOf(pageData1.get(i+1).get("high_temp_warning").toString()).floatValue();
							//低报温度2
							low_alarm_temp3 = Float.valueOf(pageData1.get(i+1).get("low_alarm_temp").toString()).floatValue();
							//低报温度警示2
							low_temp_warning3 = Float.valueOf(pageData1.get(i+1).get("low_temp_warning").toString()).floatValue();
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i+1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue();
							choose2 = true;
						}
						break;
					}else
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i>0 && i<pageData1.size()-1){
						//目标温度差2
						set_temp1 = (Float.valueOf(pageData1.get(i+1).get("set_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("set_temp").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//目标温度2
						set_temp3 = Float.valueOf(pageData1.get(i-1).get("set_temp").toString()).floatValue();
						//高报温度差2
						high_alarm_temp1 = (Float.valueOf(pageData1.get(i+1).get("high_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("high_alarm_temp").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//高报温度警示差2
						high_temp_warning1 = (Float.valueOf(pageData1.get(i+1).get("high_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("high_temp_warning").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//高报温度2
						high_alarm_temp3 = Float.valueOf(pageData1.get(i-1).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示2
						high_temp_warning3 = Float.valueOf(pageData1.get(i-1).get("high_temp_warning").toString()).floatValue();
						//低报温度差2
						low_alarm_temp1 = (Float.valueOf(pageData1.get(i+1).get("low_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("low_alarm_temp").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报温度警示差2
						low_temp_warning1 = (Float.valueOf(pageData1.get(i+1).get("low_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("low_temp_warning").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报温度2
						low_alarm_temp3 = Float.valueOf(pageData1.get(i-1).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示2
						low_temp_warning3 = Float.valueOf(pageData1.get(i-1).get("low_temp_warning").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i+1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue();
						day_age = Integer.valueOf(pageData1.get(i-1).get("day_age").toString()).intValue();
						choose2 = true;
						break;
					}else if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i==pageData1.size()-1){
						//目标温度2
						set_temp3 = Float.valueOf(pageData1.get(i-1).get("set_temp").toString()).floatValue();
						//高报温度2
						high_alarm_temp3 = Float.valueOf(pageData1.get(i-1).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示2
						high_temp_warning3 = Float.valueOf(pageData1.get(i-1).get("high_temp_warning").toString()).floatValue();
						//低报温度2
						low_alarm_temp3 = Float.valueOf(pageData1.get(i-1).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示2
						low_temp_warning3 = Float.valueOf(pageData1.get(i-1).get("low_temp_warning").toString()).floatValue();
						uid_num = Integer.valueOf(pageData1.get(i-1).get("id").toString()).intValue();
						day_age = Integer.valueOf(pageData1.get(i-1).get("day_age").toString()).intValue();
						day_age2 = Integer.valueOf(pd.get("dage").toString()).intValue();
						choose = true;
					}
				}
			}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2){
				//计算负压差、基值
				for(int i=0;i<pageData1.size();i++){
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i==0){
						if(pageData1.size() != 1){
							//光照目标值2
							set_lux3 = Float.valueOf(pageData1.get(i+1).get("set_lux").toString()).floatValue();
							//光照上限值2
							high_lux3 = Float.valueOf(pageData1.get(i+1).get("high_lux").toString()).floatValue();
							//光照上限警示2
							high_lux_warning3 = Float.valueOf(pageData1.get(i+1).get("high_lux_warning").toString()).floatValue();
							//光照下限值2
							low_lux3 = Float.valueOf(pageData1.get(i+1).get("low_lux").toString()).floatValue();
							//光照下限警示2
							low_lux_warning3 = Float.valueOf(pageData1.get(i+1).get("low_lux_warning").toString()).floatValue();
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i+1).get("id").toString()).intValue();
							day_age2 = (Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue()-1)*7;
							String[] st2 = pageData1.get(i+1).get("start_time").toString().split(":");
							startTime2 = Integer.valueOf(st2[0]).intValue();
							String[] et2 = pageData1.get(i+1).get("end_time").toString().split(":");
							endTime2 = Integer.valueOf(et2[0]).intValue();
							choose3 = true;
						}
						break;
					}else
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i>0 && i<pageData1.size()-1){
						//光照参照值差2
						set_lux1 = (Float.valueOf(pageData1.get(i + 1).get("set_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("set_lux").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue()*7)*24);
						//光照参照值2
						set_lux3 = Float.valueOf(pageData1.get(i-1).get("set_lux").toString()).floatValue();
						//高报光照差2
						high_lux1 = (Float.valueOf(pageData1.get(i+1).get("high_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("high_lux").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue()*7)*24);
						//高报光照警示差2
						high_lux_warning1 = (Float.valueOf(pageData1.get(i+1).get("high_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("high_lux_warning").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue()*7)*24);
						//高报光照2
						high_lux3 = Float.valueOf(pageData1.get(i-1).get("high_lux").toString()).floatValue();
						//高报光照警示2
						high_lux_warning3 = Float.valueOf(pageData1.get(i-1).get("high_lux_warning").toString()).floatValue();
						//低报光照差2
						low_lux1 = (Float.valueOf(pageData1.get(i+1).get("low_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("low_lux").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue()*7)*24);
						//低报光照警示差2
						low_lux_warning1 = (Float.valueOf(pageData1.get(i+1).get("low_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("low_lux_warning").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue()*7)*24);
						//低报光照2
						low_lux3 = Float.valueOf(pageData1.get(i-1).get("low_lux").toString()).floatValue();
						//低报光照警示2
						low_lux_warning3 = Float.valueOf(pageData1.get(i-1).get("low_lux_warning").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i-1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue()*7;
						day_age = (Integer.valueOf(pageData1.get(i).get("day_age").toString()).intValue()-1)*7;
						String[] st2 = pageData1.get(i+1).get("start_time").toString().split(":");
						startTime2 = Integer.valueOf(st2[0]).intValue();
						String[] et2 = pageData1.get(i+1).get("end_time").toString().split(":");
						endTime2 = Integer.valueOf(et2[0]).intValue();
						choose2 = true;
						break;
					}else if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i==pageData1.size()-1){
						//光照参照值2
						set_lux3 = Float.valueOf(pageData1.get(i-1).get("set_lux").toString()).floatValue();
						//高报光照2
						high_lux3 = Float.valueOf(pageData1.get(i-1).get("high_lux").toString()).floatValue();
						//高报光照警示2
						high_lux_warning3 = Float.valueOf(pageData1.get(i-1).get("high_lux_warning").toString()).floatValue();
						//低报光照2
						low_lux3 = Float.valueOf(pageData1.get(i-1).get("low_lux").toString()).floatValue();
						//低报光照警示2
						low_lux_warning3 = Float.valueOf(pageData1.get(i-1).get("low_lux_warning").toString()).floatValue();
						uid_num = Integer.valueOf(pageData1.get(i-1).get("id").toString()).intValue();
						day_age = (Integer.valueOf(pageData1.get(i).get("day_age").toString()).intValue()-1)*7;
						day_age2 = Integer.valueOf(pd.get("dage").toString()).intValue();
						choose = true;
					}
				}
			}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==3){
				//计算二氧化碳差、基值
				for(int i=0;i<pageData1.size();i++){
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i==0){
						if(pageData1.size() != 1){
							//目标二氧化碳2
							set_co23 = Float.valueOf(pageData1.get(i+1).get("set_co2").toString()).floatValue();
							//高报二氧化碳2
							high_alarm_co23 = Float.valueOf(pageData1.get(i+1).get("high_alarm_co2").toString()).floatValue();
							//高报二氧化碳警示2
							high_co2_warning3 = Float.valueOf(pageData1.get(i+1).get("high_co2_warning").toString()).floatValue();
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i+1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue();
						}
						break;
					}else
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i>0 && i<pageData1.size()-1){
						//目标二氧化碳差2
						set_co21 = (Float.valueOf(pageData1.get(i+1).get("set_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("set_co2").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//目标二氧化碳2
						set_co23 = Float.valueOf(pageData1.get(i-1).get("set_co2").toString()).floatValue();
						//高报二氧化碳差2
						high_alarm_co21 = (Float.valueOf(pageData1.get(i+1).get("high_alarm_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("high_alarm_co2").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//高报二氧化碳警示差2
						high_co2_warning1 = (Float.valueOf(pageData1.get(i+1).get("high_co2_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("high_co2_warning").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//高报二氧化碳2
						high_alarm_co23 = Float.valueOf(pageData1.get(i-1).get("high_alarm_co2").toString()).floatValue();
						//高报二氧化碳警示2
						high_co2_warning3 = Float.valueOf(pageData1.get(i-1).get("high_co2_warning").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i+1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue();
						day_age  = Integer.valueOf(pageData1.get(i-1).get("day_age").toString()).intValue();
						break;
					}
				}
			}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==5){
				//计算湿度差、基值
				for(int i=0;i<pageData1.size();i++){
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i==0){
						if(pageData1.size() != 1){
							//目标湿度2
							set_humidity3 = Float.valueOf(pageData1.get(i+1).get("set_humidity").toString()).floatValue();
							//高报湿度2
							high_alarm_humidity3 = Float.valueOf(pageData1.get(i+1).get("high_alarm_humidity").toString()).floatValue();
							//高报湿度警示2
							high_humidity_warning3 = Float.valueOf(pageData1.get(i+1).get("high_humidity_warning").toString()).floatValue();
							//低报湿度2
							low_alarm_humidity3 = Float.valueOf(pageData1.get(i+1).get("low_alarm_humidity").toString()).floatValue();
							//低报湿度警示2
							low_humidity_warning3 = Float.valueOf(pageData1.get(i+1).get("low_humidity_warning").toString()).floatValue();
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i+1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue();
							choose2 = true;
						}
						break;
					}else
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i>0 && i<pageData1.size()-1){
						//目标湿度差2
						set_humidity1 = (Float.valueOf(pageData1.get(i+1).get("set_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("set_humidity").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//目标湿度2
						set_humidity3 = Float.valueOf(pageData1.get(i-1).get("set_humidity").toString()).floatValue();
						//高报湿度差2
						high_alarm_humidity1 = (Float.valueOf(pageData1.get(i+1).get("high_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("high_alarm_humidity").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//高报湿度警示差2
						high_humidity_warning1 = (Float.valueOf(pageData1.get(i+1).get("high_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("high_humidity_warning").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//高报湿度2
						high_alarm_humidity3 = Float.valueOf(pageData1.get(i-1).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示2
						high_humidity_warning3 = Float.valueOf(pageData1.get(i-1).get("high_humidity_warning").toString()).floatValue();
						//低报湿度差2
						low_alarm_humidity1 = (Float.valueOf(pageData1.get(i+1).get("low_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("low_alarm_humidity").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报湿度警示差2
						low_humidity_warning1 = (Float.valueOf(pageData1.get(i+1).get("low_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("low_humidity_warning").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报湿度2
						low_alarm_humidity3 = Float.valueOf(pageData1.get(i-1).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示2
						low_humidity_warning3 = Float.valueOf(pageData1.get(i-1).get("low_humidity_warning").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i+1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue();
						day_age = Integer.valueOf(pageData1.get(i-1).get("day_age").toString()).intValue();
						choose2 = true;
						break;
					}else if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i==pageData1.size()-1){
						//目标温度2
						set_humidity3 = Float.valueOf(pageData1.get(i-1).get("set_humidity").toString()).floatValue();
						//高报温度2
						high_alarm_humidity3 = Float.valueOf(pageData1.get(i-1).get("high_alarm_humidity").toString()).floatValue();
						//高报温度警示2
						high_humidity_warning3 = Float.valueOf(pageData1.get(i-1).get("high_humidity_warning").toString()).floatValue();
						//低报温度2
						low_alarm_humidity3 = Float.valueOf(pageData1.get(i-1).get("low_alarm_humidity").toString()).floatValue();
						//低报温度警示2
						low_humidity_warning3 = Float.valueOf(pageData1.get(i-1).get("low_humidity_warning").toString()).floatValue();
						uid_num = Integer.valueOf(pageData1.get(i-1).get("id").toString()).intValue();
						day_age = Integer.valueOf(pageData1.get(i-1).get("day_age").toString()).intValue();
						day_age2 = Integer.valueOf(pd.get("dage").toString()).intValue();
						choose = true;
					}
				}
			}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==6){
				//计算负压差、基值
				for(int i=0;i<pageData1.size();i++){
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i==0){
						if(pageData1.size() != 1){
							//目标负压2
							set_negative_pressure3 = Float.valueOf(pageData1.get(i+1).get("set_negative_pressure").toString()).floatValue();
							//高报负压2
							high_alarm_negative_pressure3 = Float.valueOf(pageData1.get(i+1).get("high_alarm_negative_pressure").toString()).floatValue();
							//高报负压警示2
							high_negative_pressure_warning3 = Float.valueOf(pageData1.get(i+1).get("high_negative_pressure_warning").toString()).floatValue();
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i+1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue();
						}
						break;
					}else
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i>0 && i<pageData1.size()-1){
						//目标负压差2
						set_negative_pressure1 = (Float.valueOf(pageData1.get(i+1).get("set_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("set_negative_pressure").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//目标负压2
						set_negative_pressure3 = Float.valueOf(pageData1.get(i-1).get("set_negative_pressure").toString()).floatValue();
						//高报负压差2
						high_alarm_negative_pressure1 = (Float.valueOf(pageData1.get(i+1).get("high_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("high_alarm_negative_pressure").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//高报负压警示差2
						high_negative_pressure_warning1 = (Float.valueOf(pageData1.get(i+1).get("high_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("high_negative_pressure_warning").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//高报负压2
						high_alarm_negative_pressure3 = Float.valueOf(pageData1.get(i-1).get("high_alarm_negative_pressure").toString()).floatValue();
						//高报负压警示2
						high_negative_pressure_warning3 = Float.valueOf(pageData1.get(i-1).get("high_negative_pressure_warning").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i+1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue();
						day_age  = Integer.valueOf(pageData1.get(i-1).get("day_age").toString()).intValue();
						break;
					}
				}
			}else{
				for(int i=0;i<pageData1.size();i++){
					//计算耗水差、基值
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i==0){
						if(pageData1.size() != 1){
							//目标耗水2
							set_water_deprivation3 = Float.valueOf(pageData1.get(i+1).get("set_water_deprivation").toString()).floatValue();
							//高报耗水2
							high_water_deprivation3 = Float.valueOf(pageData1.get(i+1).get("high_water_deprivation").toString()).floatValue();
							//低报耗水2
							low_water_deprivation3 = Float.valueOf(pageData1.get(i+1).get("low_water_deprivation").toString()).floatValue();
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i+1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue();
						}
						break;
					}else
					if(pageData1.get(i).get("id").toString().equals(pd.get("uid_num").toString()) && i>0 && i<pageData1.size()-1){
						//目标耗水差2
						set_water_deprivation1 = (Float.valueOf(pageData1.get(i+1).get("set_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("set_water_deprivation").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//目标耗水2
						set_water_deprivation3 = Float.valueOf(pageData1.get(i-1).get("set_water_deprivation").toString()).floatValue();
						//高报耗水差2
						high_water_deprivation1 = (Float.valueOf(pageData1.get(i+1).get("high_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("high_water_deprivation").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//高报耗水2
						high_water_deprivation3 = Float.valueOf(pageData1.get(i-1).get("high_water_deprivation").toString()).floatValue();
						//低报耗水差2
						low_water_deprivation1 = (Float.valueOf(pageData1.get(i+1).get("low_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("low_water_deprivation").toString()).floatValue())/
								((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报耗水2
						low_water_deprivation3 = Float.valueOf(pageData1.get(i-1).get("low_water_deprivation").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i+1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue();
						day_age = Integer.valueOf(pageData1.get(i-1).get("day_age").toString()).intValue();
						break;
					}
				}
			}
//			if (choose2) {
				PageData pd4 = new PageData();
				pd4.put("uid_num", uid_num);
				pd4.put("alarm_type", pd.get("alarm_type"));
				if (choose) {
					pd4.put("day_age1", day_age);
					}
				if (choose2) {
					if(Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2){
						pd4.put("day_age1", day_age);
					}else{
				pd4.put("day_age2", day_age2);
					}
				}
				if(!choose3){
				alarmService.deleteSBDayageTempSub(pd4);
				}else{
					choose2 = true;
				}
				List<PageData> list = new ArrayList<PageData>();
				if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 1) {
					if(choose2){
					//修改相邻记录的温度
					for (int i = day_age; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", uid_num);
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							pd5.put("alarm_type", pd.get("alarm_type"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd5.put("day_age", i + 1);
							date.setHours(j);
							pd5.put("record_datetime", date);
							pd5.put("set_temp", set_temp3 + set_temp1 * ((i - day_age) * 24 + j));
							pd5.put("high_alarm_temp", high_alarm_temp3 + high_alarm_temp1 * ((i - day_age) * 24 + j));
							pd5.put("low_alarm_temp", low_alarm_temp3 + low_alarm_temp1 * ((i - day_age) * 24 + j));
							pd5.put("high_temp_warning", high_temp_warning3 + high_temp_warning1 * ((i - day_age) * 24 + j));
							pd5.put("low_temp_warning", low_temp_warning3 + low_temp_warning1 * ((i - day_age) * 24 + j));
							pd5.put("set_humidity", null);
							pd5.put("high_alarm_humidity", null);
							pd5.put("low_alarm_humidity", null);
							pd5.put("high_humidity_warning", null);
							pd5.put("low_humidity_warning", null);
							pd5.put("set_negative_pressure", null);
							pd5.put("high_alarm_negative_pressure", null);
							pd5.put("low_alarm_negative_pressure", null);
							pd5.put("high_negative_pressure_warning", null);
							pd5.put("low_negative_pressure_warning", null);
							pd5.put("set_lux", null);
							pd5.put("high_lux", null);
							pd5.put("low_lux", null);
							pd5.put("high_lux_warning", null);
							pd5.put("low_lux_warning", null);
							pd5.put("set_co2", null);
							pd5.put("high_alarm_co2", null);
							pd5.put("high_co2_warning", null);
							pd5.put("set_water_deprivation", null);
							pd5.put("high_water_deprivation", null);
							pd5.put("low_water_deprivation", null);
							pd5.put("is_start", "0");
							list.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					}
					if(choose){
						for (int i = day_age; i < Integer.valueOf(pd.get("dage").toString()).intValue(); i++) {
							for (int j = 1; j <= 24; j++) {
								PageData pd5 = new PageData();
								pd5.put("uid_num", uid_num);
								pd5.put("create_person", userId);
								pd5.put("create_date", new Date());
								pd5.put("create_time", new Date());
								pd5.put("modify_person", userId);
								pd5.put("modify_date", new Date());
								pd5.put("modify_time", new Date());
								pd5.put("farmId", pd.get("farmId"));
								pd5.put("houseId", pd.get("houseId"));
								pd5.put("alarm_type", pd.get("alarm_type"));
								Date date = new Date();
								date.setMinutes(0);
								date.setSeconds(0);
								pd5.put("day_age", i + 1);
								date.setHours(j);
								pd5.put("record_datetime", date);
								pd5.put("set_temp", set_temp3);
								pd5.put("high_alarm_temp", high_alarm_temp3);
								pd5.put("low_alarm_temp", low_alarm_temp3);
								pd5.put("high_temp_warning", high_temp_warning3);
								pd5.put("low_temp_warning", low_temp_warning3);
								pd5.put("set_humidity", null);
								pd5.put("high_alarm_humidity", null);
								pd5.put("low_alarm_humidity", null);
								pd5.put("high_humidity_warning", null);
								pd5.put("low_humidity_warning", null);
								pd5.put("set_negative_pressure", null);
								pd5.put("high_alarm_negative_pressure", null);
								pd5.put("low_alarm_negative_pressure", null);
								pd5.put("high_negative_pressure_warning", null);
								pd5.put("low_negative_pressure_warning", null);
								pd5.put("set_lux", null);
								pd5.put("high_lux", null);
								pd5.put("low_lux", null);
								pd5.put("high_lux_warning", null);
								pd5.put("low_lux_warning", null);
								pd5.put("set_co2", null);
								pd5.put("high_alarm_co2", null);
								pd5.put("high_co2_warning", null);
								pd5.put("set_water_deprivation", null);
								pd5.put("high_water_deprivation", null);
								pd5.put("low_water_deprivation", null);
								pd5.put("is_start", "0");
								list.add(pd5);
							}
						}
					}
					alarmService.saveSBDayageTempSub(list);
				} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2) {
					if(choose2){
					//修改相邻记录的负压
					for (int i = day_age; i < day_age2-7; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", uid_num);
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							pd5.put("alarm_type", pd.get("alarm_type"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd5.put("day_age", i + 1);
							date.setHours(j);
							pd5.put("record_datetime", date);
							pd5.put("set_temp", null);
							pd5.put("high_alarm_temp", null);
							pd5.put("low_alarm_temp", null);
							pd5.put("high_temp_warning", null);
							pd5.put("low_temp_warning", null);
							pd5.put("set_humidity", null);
							pd5.put("high_alarm_humidity", null);
							pd5.put("low_alarm_humidity", null);
							pd5.put("high_humidity_warning", null);
							pd5.put("low_humidity_warning", null);
							pd5.put("set_negative_pressure", null);
							pd5.put("high_alarm_negative_pressure", null);
							pd5.put("low_alarm_negative_pressure", null);
							pd5.put("high_negative_pressure_warning", null);
							pd5.put("low_negative_pressure_warning", null);
//						pd5.put("set_lux",set_lux3+set_lux1*((i-day_age)*24+j));
//						pd5.put("high_lux",high_lux3+high_lux1*((i-day_age)*24+j));
//						pd5.put("low_lux",low_lux3+low_lux1*((i-day_age)*24+j));
							pd5.put("set_lux", set_lux3);
							pd5.put("high_lux", high_lux3);
							pd5.put("low_lux", low_lux3);
							pd5.put("high_lux_warning", high_lux_warning3);
							pd5.put("low_lux_warning", low_lux_warning3);
//							pd5.put("set_lux", set_lux3 + set_lux1 * ((day_age2 - 1 - day_age) * 24 + 24));
//							pd5.put("high_lux", high_lux3 + high_lux1 * ((day_age2 - 1 - day_age) * 24 + 24));
//							pd5.put("low_lux", low_lux3 + low_lux1 * ((day_age2 - 1 - day_age) * 24 + 24));
//							pd5.put("high_lux_warning", high_lux_warning3 + high_lux_warning1 * ((day_age2 - 1 - day_age) * 24 + 24));
//							pd5.put("low_lux_warning", low_lux_warning3 + low_lux_warning1 * ((day_age2 - 1 - day_age) * 24 + 24));
							pd5.put("set_co2", null);
							pd5.put("high_alarm_co2", null);
							pd5.put("high_co2_warning", null);
							pd5.put("set_water_deprivation", null);
							pd5.put("high_water_deprivation", null);
							pd5.put("low_water_deprivation", null);
//							String[] st = pd.get("start_time").toString().split(":");
//							startTime = Integer.valueOf(st[0]).intValue();
//							String[] et = pd.get("end_time").toString().split(":");
//							endTime = Integer.valueOf(et[0]).intValue();
							if (startTime2 <= j && j < endTime2) {
								pd5.put("is_start", "0");
							} else {
								pd5.put("is_start", "1");
							}
							list.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					}
					if(choose){
						for (int i = day_age; i < Integer.valueOf(pd.get("dage").toString()).intValue(); i++) {
							for (int j = 1; j <= 24; j++) {
								PageData pd5 = new PageData();
								pd5.put("uid_num", uid_num);
								pd5.put("create_person", userId);
								pd5.put("create_date", new Date());
								pd5.put("create_time", new Date());
								pd5.put("modify_person", userId);
								pd5.put("modify_date", new Date());
								pd5.put("modify_time", new Date());
								pd5.put("farmId", pd.get("farmId"));
								pd5.put("houseId", pd.get("houseId"));
								pd5.put("alarm_type", pd.get("alarm_type"));
								Date date = new Date();
								date.setMinutes(0);
								date.setSeconds(0);
								pd5.put("day_age", i + 1);
								date.setHours(j);
								pd5.put("record_datetime", date);
								pd5.put("set_temp", null);
								pd5.put("high_alarm_temp", null);
								pd5.put("low_alarm_temp", null);
								pd5.put("high_temp_warning", null);
								pd5.put("low_temp_warning", null);
								pd5.put("set_humidity", null);
								pd5.put("high_alarm_humidity", null);
								pd5.put("low_alarm_humidity", null);
								pd5.put("high_humidity_warning", null);
								pd5.put("low_humidity_warning", null);
								pd5.put("set_negative_pressure", null);
								pd5.put("high_alarm_negative_pressure", null);
								pd5.put("low_alarm_negative_pressure", null);
								pd5.put("high_negative_pressure_warning", null);
								pd5.put("low_negative_pressure_warning", null);
								pd5.put("set_lux", set_lux3);
								pd5.put("high_lux", high_lux3);
								pd5.put("low_lux", low_lux3);
								pd5.put("high_lux_warning", high_lux_warning3);
								pd5.put("low_lux_warning", low_lux_warning3);
								pd5.put("set_co2", null);
								pd5.put("high_alarm_co2", null);
								pd5.put("high_co2_warning", null);
								pd5.put("set_water_deprivation", null);
								pd5.put("high_water_deprivation", null);
								pd5.put("low_water_deprivation", null);
								if (startTime2 <= j && j < endTime2) {
									pd5.put("is_start", "0");
								} else {
									pd5.put("is_start", "1");
								}
								list.add(pd5);
							}
						}
					}
					alarmService.saveSBDayageTempSub(list);
				} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 3) {
					//修改相邻记录的二氧化碳
					for (int i = day_age; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", uid_num);
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							pd5.put("alarm_type", pd.get("alarm_type"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd5.put("day_age", i + 1);
							date.setHours(j);
							pd5.put("record_datetime", date);
							pd5.put("set_temp", null);
							pd5.put("high_alarm_temp", null);
							pd5.put("low_alarm_temp", null);
							pd5.put("high_temp_warning", null);
							pd5.put("low_temp_warning", null);
							pd5.put("set_humidity", null);
							pd5.put("high_alarm_humidity", null);
							pd5.put("low_alarm_humidity", null);
							pd5.put("high_humidity_warning", null);
							pd5.put("low_humidity_warning", null);
							pd5.put("set_negative_pressure", null);
							pd5.put("high_alarm_negative_pressure", null);
							pd5.put("low_alarm_negative_pressure", null);
							pd5.put("high_negative_pressure_warning", null);
							pd5.put("low_negative_pressure_warning", null);
							pd5.put("set_lux", null);
							pd5.put("high_lux", null);
							pd5.put("low_lux", null);
							pd5.put("high_lux_warning", null);
							pd5.put("low_lux_warning", null);
							pd5.put("set_co2", set_co23 + set_co21 * ((i - day_age) * 24 + j));
							pd5.put("high_alarm_co2", high_alarm_co23 + high_alarm_co21 * ((i - day_age) * 24 + j));
							pd5.put("high_co2_warning", high_co2_warning3 + high_co2_warning1 * ((i - day_age) * 24 + j));
							pd5.put("set_water_deprivation", null);
							pd5.put("high_water_deprivation", null);
							pd5.put("low_water_deprivation", null);
							pd5.put("is_start", "0");
							list.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list);
				}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 5) {
					if(choose2){
					//修改相邻记录的湿度
					for (int i = day_age; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", uid_num);
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							pd5.put("alarm_type", pd.get("alarm_type"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd5.put("day_age", i + 1);
							date.setHours(j);
							pd5.put("record_datetime", date);
							pd5.put("set_temp", null);
							pd5.put("high_alarm_temp", null);
							pd5.put("low_alarm_temp", null);
							pd5.put("high_temp_warning", null);
							pd5.put("low_temp_warning", null);
							pd5.put("set_humidity", set_humidity3 + set_humidity1 * ((i - day_age) * 24 + j));
							pd5.put("high_alarm_humidity", high_alarm_humidity3 + high_alarm_humidity1 * ((i - day_age) * 24 + j));
							pd5.put("low_alarm_humidity", low_alarm_humidity3 + low_alarm_humidity1 * ((i - day_age) * 24 + j));
							pd5.put("high_humidity_warning", high_humidity_warning3 + high_humidity_warning1 * ((i - day_age) * 24 + j));
							pd5.put("low_humidity_warning", low_humidity_warning3 + low_humidity_warning1 * ((i - day_age) * 24 + j));
							pd5.put("set_negative_pressure", null);
							pd5.put("high_alarm_negative_pressure", null);
							pd5.put("low_alarm_negative_pressure", null);
							pd5.put("high_negative_pressure_warning", null);
							pd5.put("low_negative_pressure_warning", null);
							pd5.put("set_lux", null);
							pd5.put("high_lux", null);
							pd5.put("low_lux", null);
							pd5.put("high_lux_warning", null);
							pd5.put("low_lux_warning", null);
							pd5.put("set_co2", null);
							pd5.put("high_alarm_co2", null);
							pd5.put("high_co2_warning", null);
							pd5.put("set_water_deprivation", null);
							pd5.put("high_water_deprivation", null);
							pd5.put("low_water_deprivation", null);
							pd5.put("is_start", "0");
							list.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					}
					if(choose){
						for (int i = day_age; i < Integer.valueOf(pd.get("dage").toString()).intValue(); i++) {
							for (int j = 1; j <= 24; j++) {
								PageData pd5 = new PageData();
								pd5.put("uid_num", uid_num);
								pd5.put("create_person", userId);
								pd5.put("create_date", new Date());
								pd5.put("create_time", new Date());
								pd5.put("modify_person", userId);
								pd5.put("modify_date", new Date());
								pd5.put("modify_time", new Date());
								pd5.put("farmId", pd.get("farmId"));
								pd5.put("houseId", pd.get("houseId"));
								pd5.put("alarm_type", pd.get("alarm_type"));
								Date date = new Date();
								date.setMinutes(0);
								date.setSeconds(0);
								pd5.put("day_age", i + 1);
								date.setHours(j);
								pd5.put("record_datetime", date);
								pd5.put("set_temp", null);
								pd5.put("high_alarm_temp", null);
								pd5.put("low_alarm_temp", null);
								pd5.put("high_temp_warning", null);
								pd5.put("low_temp_warning", null);
								pd5.put("set_humidity", set_humidity3);
								pd5.put("high_alarm_humidity", high_alarm_humidity3);
								pd5.put("low_alarm_humidity", low_alarm_humidity3);
								pd5.put("high_humidity_warning", high_humidity_warning3);
								pd5.put("low_humidity_warning", low_humidity_warning3);
								pd5.put("set_negative_pressure", null);
								pd5.put("high_alarm_negative_pressure", null);
								pd5.put("low_alarm_negative_pressure", null);
								pd5.put("high_negative_pressure_warning", null);
								pd5.put("low_negative_pressure_warning", null);
								pd5.put("set_lux", null);
								pd5.put("high_lux", null);
								pd5.put("low_lux", null);
								pd5.put("high_lux_warning", null);
								pd5.put("low_lux_warning", null);
								pd5.put("set_co2", null);
								pd5.put("high_alarm_co2", null);
								pd5.put("high_co2_warning", null);
								pd5.put("set_water_deprivation", null);
								pd5.put("high_water_deprivation", null);
								pd5.put("low_water_deprivation", null);
								pd5.put("is_start", "0");
								list.add(pd5);
							}
						}
					}
					alarmService.saveSBDayageTempSub(list);
				}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 6) {
					//修改相邻记录的二氧化碳
					for (int i = day_age; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", uid_num);
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							pd5.put("alarm_type", pd.get("alarm_type"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd5.put("day_age", i + 1);
							date.setHours(j);
							pd5.put("record_datetime", date);
							pd5.put("set_temp", null);
							pd5.put("high_alarm_temp", null);
							pd5.put("low_alarm_temp", null);
							pd5.put("high_temp_warning", null);
							pd5.put("low_temp_warning", null);
							pd5.put("set_humidity", null);
							pd5.put("high_alarm_humidity", null);
							pd5.put("low_alarm_humidity", null);
							pd5.put("high_humidity_warning", null);
							pd5.put("low_humidity_warning", null);
							pd5.put("set_negative_pressure", set_negative_pressure3 + set_negative_pressure1 * ((i - day_age) * 24 + j));
							pd5.put("high_alarm_negative_pressure", high_alarm_negative_pressure3 + high_alarm_negative_pressure1 * ((i - day_age) * 24 + j));
							pd5.put("low_alarm_negative_pressure", null);
							pd5.put("high_negative_pressure_warning", high_negative_pressure_warning3 + high_negative_pressure_warning1 * ((i - day_age) * 24 + j));
							pd5.put("low_negative_pressure_warning", null);
							pd5.put("set_lux", null);
							pd5.put("high_lux", null);
							pd5.put("low_lux", null);
							pd5.put("high_lux_warning", null);
							pd5.put("low_lux_warning", null);
							pd5.put("set_co2", null);
							pd5.put("high_alarm_co2", null);
							pd5.put("high_co2_warning", null);
							pd5.put("set_water_deprivation", null);
							pd5.put("high_water_deprivation", null);
							pd5.put("low_water_deprivation", null);
							pd5.put("is_start", "0");
							list.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list);
				} else {
					//修改相邻记录的耗水
					for (int i = day_age; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", uid_num);
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							pd5.put("alarm_type", pd.get("alarm_type"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd5.put("day_age", i + 1);
							date.setHours(j);
							pd5.put("record_datetime", date);
							pd5.put("set_temp", null);
							pd5.put("high_alarm_temp", null);
							pd5.put("low_alarm_temp", null);
							pd5.put("set_lux", null);
							pd5.put("high_lux", null);
							pd5.put("low_lux", null);
							pd5.put("set_co2", null);
							pd5.put("high_alarm_co2", null);
							pd5.put("set_water_deprivation", set_water_deprivation3 + set_water_deprivation1 * ((i - day_age) * 24 + j));
							pd5.put("high_water_deprivation", high_water_deprivation3 + high_water_deprivation1 * ((i - day_age) * 24 + j));
							pd5.put("low_water_deprivation", low_water_deprivation3 + low_water_deprivation1 * ((i - day_age) * 24 + j));
							pd5.put("is_start", "0");
							list.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list);
				}
//			}
		}catch (Exception e){
			e.printStackTrace();
			g.setMsg("2");
		}
		return g;
	}
	
	@RequestMapping("/updateHouseAlarm")
	public void updateHouseAlarm(HttpServletResponse response,HttpServletRequest request) throws Exception{
//		ModelAndView mv = this.getModelAndView();
		Json j=new Json();
		SDUser user = (SDUser)request.getSession().getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		pd.put("modify_person",user.getId());
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());
		alarmService.updateSBHouseAlarm(pd);
		
//		alarmService.saveSbHouseAlarmHis(pd);
		
		alarm_type = pd.get("alarm_type").toString();
//		List<PageData> mcl = alarmService.selectByCondition(pd);
	    List<PageData> mc1 = alarmService.selectSBHouseAlarmByCondition(pd);
	    if(mc1.size()!=0){
			j.setObj(mc1.get(0));
		    }else{
		    	j.setObj("");
		    }
		j.setSuccess(true);
//		j.setObj(mcl);	
		super.writeJson(j, response);
	}
	
	@RequestMapping("/saveSbHouseAlarmHis")
	public void saveSbHouseAlarmHis(HttpServletResponse response,HttpServletRequest request) throws Exception{
//		ModelAndView mv = this.getModelAndView();
		Json j=new Json();
		SDUser user = (SDUser)request.getSession().getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		pd.put("modify_person",user.getId());
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());
		
		alarmService.saveSbHouseAlarmHis(pd);
		
		alarm_type = pd.get("alarm_type").toString();
	    List<PageData> mc1 = alarmService.selectSBHouseAlarmByCondition(pd);
	    if(mc1.size()!=0){
			j.setObj(mc1.get(0));
		    }else{
		    	j.setObj("");
		    }
		j.setSuccess(true);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/updateAlarm")
	public void updateAlarm(HttpServletResponse response,HttpServletRequest request) throws Exception {
		Json g = new Json();
		SDUser user = (SDUser) request.getSession().getAttribute(Const.SESSION_USER);
		PageData pd1 = this.getPageData();
		PageData pd = new PageData();
		pd1.put("modify_person", user.getId());
		pd1.put("modify_date", new Date());
		pd1.put("modify_time", new Date());
		String tr = (String) pd1.get("updateRow");
		String[] alarmSet = tr.split(";");
		for (String as : alarmSet) {
			String[] alarmSet2 = as.split(",");
			int uidnum = Integer.parseInt(alarmSet2[0]);
			pd1.put("uid_num", uidnum);
			int farmId = Integer.parseInt(alarmSet2[1]);
			pd1.put("farmId", farmId);
			int houseId = Integer.parseInt(alarmSet2[2]);
			pd1.put("houseId", houseId);
			int dayage = Integer.parseInt(alarmSet2[3]);
			pd1.put("day_age", dayage);
			int startTime = 0, endTime = 0;
			if (Integer.valueOf(pd1.get("alarm_type").toString()).intValue() == 1) {
				float set_temp = Float.parseFloat(alarmSet2[4]);
				pd1.put("set_temp", set_temp);
				float high_alarm_temp = Float.parseFloat(alarmSet2[5]);
				pd1.put("high_alarm_temp", high_alarm_temp);
				float high_temp_warning = Float.parseFloat(alarmSet2[6]);
				pd1.put("high_temp_warning", high_temp_warning);
				float low_alarm_temp = Float.parseFloat(alarmSet2[7]);
				pd1.put("low_alarm_temp", low_alarm_temp);
				float low_temp_warning = Float.parseFloat(alarmSet2[8]);
				pd1.put("low_temp_warning", low_temp_warning);
			} else if (Integer.valueOf(pd1.get("alarm_type").toString()).intValue() == 2) {
				pd1.put("day_age", dayage * 7);
				float set_lux = Float.parseFloat(alarmSet2[4]);
				pd1.put("set_lux", set_lux);
				float high_lux = Float.parseFloat(alarmSet2[5]);
				pd1.put("high_lux", high_lux);
				float high_lux_warning = Float.parseFloat(alarmSet2[6]);
				pd1.put("high_lux_warning", high_lux_warning);
				float low_lux = Float.parseFloat(alarmSet2[7]);
				pd1.put("low_lux", low_lux);
				float low_lux_warning = Float.parseFloat(alarmSet2[8]);
				pd1.put("low_lux_warning", low_lux_warning);
				String start_time = alarmSet2[9];
				pd1.put("start_time", start_time);
				String end_time = alarmSet2[10];
				pd1.put("end_time", end_time);
				String[] st = start_time.split(":");
				startTime = Integer.valueOf(st[0]).intValue();
				String[] et = end_time.split(":");
				endTime = Integer.valueOf(et[0]).intValue();
			} else if (Integer.valueOf(pd1.get("alarm_type").toString()).intValue() == 3) {
				float set_co2 = Float.parseFloat(alarmSet2[4]);
				pd1.put("set_co2", set_co2);
				float high_alarm_co2 = Float.parseFloat(alarmSet2[5]);
				pd1.put("high_alarm_co2", high_alarm_co2);
				float high_co2_warning = Float.parseFloat(alarmSet2[6]);
				pd1.put("high_co2_warning", high_co2_warning);
			}else if (Integer.valueOf(pd1.get("alarm_type").toString()).intValue() == 5) {
				float set_humidity = Float.parseFloat(alarmSet2[4]);
				pd1.put("set_humidity", set_humidity);
				float high_alarm_humidity = Float.parseFloat(alarmSet2[5]);
				pd1.put("high_alarm_humidity", high_alarm_humidity);
				float high_humidity_warning = Float.parseFloat(alarmSet2[6]);
				pd1.put("high_humidity_warning", high_humidity_warning);
				float low_alarm_humidity = Float.parseFloat(alarmSet2[7]);
				pd1.put("low_alarm_humidity", low_alarm_humidity);
				float low_humidity_warning = Float.parseFloat(alarmSet2[8]);
				pd1.put("low_humidity_warning", low_humidity_warning);
			}else if (Integer.valueOf(pd1.get("alarm_type").toString()).intValue() == 6) {
				float set_negative_pressure = Float.parseFloat(alarmSet2[4]);
				pd1.put("set_negative_pressure", set_negative_pressure);
				float high_alarm_negative_pressure = Float.parseFloat(alarmSet2[5]);
				pd1.put("high_alarm_negative_pressure", high_alarm_negative_pressure);
				float high_negative_pressure_warning = Float.parseFloat(alarmSet2[6]);
				pd1.put("high_negative_pressure_warning", high_negative_pressure_warning);
				float low_alarm_negative_pressure = Float.parseFloat(alarmSet2[7]);
				pd1.put("low_alarm_negative_pressure", low_alarm_negative_pressure);
				float low_negative_pressure_warning = Float.parseFloat(alarmSet2[8]);
				pd1.put("low_negative_pressure_warning", low_negative_pressure_warning);
			}

			alarmService.updateSBDayageSettingSub(pd1);
			pd1.put("alarm_operation", "修改");
			alarmService.saveSbDayageSettingSubHis(pd1);

			//栋舍报警设置
//			alarmService.updateSBHouseAlarm(pd1);
//			alarmService.saveSbHouseAlarmHis(pd1);

			pd.put("farmId", pd1.get("farmId"));
			pd.put("houseId", pd1.get("houseId"));
			pd.put("day_age", pd1.get("day_age"));
			pd.put("alarm_type", pd1.get("alarm_type"));
			pd.put("dage", pd1.get("dage"));
			g = tempSubUpdate(pd, pd1, user.getId(), startTime, endTime);
		}
		alarm_type = pd.get("alarm_type").toString();
		List<PageData> mcl = alarmService.selectByCondition(pd);
		List<PageData> mc2 = alarmService.selectSBHouseAlarmByCondition(pd);
		if (mc2.size() != 0) {
			g.setObj1(mc2.get(0));
		} else {
			g.setObj1("");
		}
		g.setSuccess(true);
		g.setObj(mcl);
		super.writeJson(g, response);
	}

	public Json tempSubUpdate(PageData pd, PageData pd1, int userId, int startTime, int endTime) {
		Json g = new Json();
		try {
			List<PageData> pageData1 = alarmService.selectByCondition(pd);
			float set_temp = 0, high_alarm_temp = 0, high_temp_warning = 0, low_alarm_temp = 0, low_temp_warning = 0, 
					set_temp1 = 0, high_alarm_temp1 = 0, high_temp_warning1 = 0, low_alarm_temp1 = 0, low_temp_warning1 = 0, 
					set_temp2 = 0, high_alarm_temp2 = 0, high_temp_warning2 = 0, low_alarm_temp2 = 0, low_temp_warning2 = 0,
					set_temp3 = 0, high_alarm_temp3 = 0, high_temp_warning3 = 0, low_alarm_temp3 = 0, low_temp_warning3 = 0,
					set_lux = 0, high_lux = 0, high_lux_warning = 0, set_lux1 = 0, high_lux1 = 0, high_lux_warning1 = 0, 
					set_lux2 = 0, high_lux2 = 0, high_lux_warning2 = 0, set_lux3 = 0, high_lux3 = 0, high_lux_warning3 = 0,
					low_lux = 0, low_lux_warning = 0, low_lux1 = 0, low_lux_warning1 = 0, low_lux2 = 0, low_lux_warning2 = 0, low_lux3 = 0, low_lux_warning3 = 0,
					set_co2 = 0, high_alarm_co2 = 0, high_co2_warning = 0, set_co21 = 0, high_alarm_co21 = 0, high_co2_warning1 = 0, set_co22 = 0,
					high_alarm_co22 = 0, high_co2_warning2 = 0,
					set_co23 = 0, high_alarm_co23 = 0, high_co2_warning3 = 0,
					set_humidity = 0, high_alarm_humidity = 0, high_humidity_warning = 0, low_alarm_humidity = 0, low_humidity_warning = 0, 
					set_humidity1 = 0, high_alarm_humidity1 = 0, high_humidity_warning1 = 0, low_alarm_humidity1 = 0, low_humidity_warning1 = 0, 
				    set_humidity2 = 0, high_alarm_humidity2 = 0, high_humidity_warning2 = 0, low_alarm_humidity2 = 0, low_humidity_warning2 = 0,
					set_humidity3 = 0, high_alarm_humidity3 = 0, high_humidity_warning3 = 0, low_alarm_humidity3 = 0, low_humidity_warning3 = 0,
					set_negative_pressure = 0, high_alarm_negative_pressure = 0, high_negative_pressure_warning = 0, low_alarm_negative_pressure = 0, low_negative_pressure_warning = 0, 
					set_negative_pressure1 = 0, high_alarm_negative_pressure1 = 0, high_negative_pressure_warning1 = 0, low_alarm_negative_pressure1 = 0, low_negative_pressure_warning1 = 0, 
					set_negative_pressure2 = 0, high_alarm_negative_pressure2 = 0, high_negative_pressure_warning2 = 0, low_alarm_negative_pressure2 = 0, low_negative_pressure_warning2 = 0,
					set_negative_pressure3 = 0, high_alarm_negative_pressure3 = 0, high_negative_pressure_warning3 = 0, low_alarm_negative_pressure3 = 0, low_negative_pressure_warning3 = 0,
					set_water_deprivation = 0, high_water_deprivation = 0, low_water_deprivation = 0, set_water_deprivation1 = 0, high_water_deprivation1 = 0, low_water_deprivation1 = 0,
					set_water_deprivation2 = 0, high_water_deprivation2 = 0, low_water_deprivation2 = 0, set_water_deprivation3 = 0, high_water_deprivation3 = 0, low_water_deprivation3 = 0;
			int uid_num = 0, day_age = 0, day_age2 = 0, startTime2 = 0, endTime2 = 0;
			boolean choose=false,choose2=false,choose3=false;
			if (Integer.valueOf(pd1.get("alarm_type").toString()).intValue() == 1) {
				//计算温度差、基值
				for (int i = 0; i < pageData1.size(); i++) {
					if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == 0) {
						//目标温度差1
						set_temp = (Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//目标温度1
						set_temp2 = Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue();
						//高报温度差1
						high_alarm_temp = (Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//高报温度警示差1
						high_temp_warning = (Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//高报温度1
						high_alarm_temp2 = Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示1
						high_temp_warning2 = Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue();
						//低报温度差1
						low_alarm_temp = (Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//低报温度警示差1
						low_temp_warning = (Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//低报温度1
						low_alarm_temp2 = Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示1
						low_temp_warning2 = Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue();
						//目标温度2
						set_temp3 = Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue();
						//高报温度2
						high_alarm_temp3 = Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示2
						high_temp_warning3 = Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue();
						//低报温度2
						low_alarm_temp3 = Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示2
						low_temp_warning3 = Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue();
						if (pageData1.size() != 1) {
							//目标温度差2
							set_temp1 = (Float.valueOf(pageData1.get(i + 1).get("set_temp").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//高报温度差2
							high_alarm_temp1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_temp").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//高报温度警示差2
							high_temp_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_temp_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//低报温度差2
							low_alarm_temp1 = (Float.valueOf(pageData1.get(i + 1).get("low_alarm_temp").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//低报温度警示差2
							low_temp_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_temp_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						}else{
							choose = true;
						}						
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i > 0 && i < pageData1.size() - 1) {
						//目标温度差1
						set_temp = (Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标温度1
						set_temp2 = Float.valueOf(pageData1.get(i - 1).get("set_temp").toString()).floatValue();
						//高报温度差1
						high_alarm_temp = (Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报温度警示差1
						high_temp_warning = (Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报温度1
						high_alarm_temp2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示1
						high_temp_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_temp_warning").toString()).floatValue();
						//低报温度差1
						low_alarm_temp = (Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报温度警示差1
						low_temp_warning = (Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//低报温度1
						low_alarm_temp2 = Float.valueOf(pageData1.get(i - 1).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示1
						low_temp_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_temp_warning").toString()).floatValue();
//					if(!pageData1.get(i+1).get("set_temp").equals("")){
						//目标温度差2
						set_temp1 = (Float.valueOf(pageData1.get(i + 1).get("set_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//目标温度2
						set_temp3 = Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue();
						//高报温度差2
						high_alarm_temp1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//高报温度警示差2
						high_temp_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//高报温度2
						high_alarm_temp3 = Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示2
						high_temp_warning3 = Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue();
						//低报温度差2
						low_alarm_temp1 = (Float.valueOf(pageData1.get(i + 1).get("low_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//低报温度警示差2
						low_temp_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//低报温度2
						low_alarm_temp3 = Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示2
						low_temp_warning3 = Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue();
//					}
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						choose2 = true;
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == pageData1.size() - 1) {
						//目标温度差
						set_temp = (Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标温度
						set_temp2 = Float.valueOf(pageData1.get(i - 1).get("set_temp").toString()).floatValue();
						//高报温度差
						high_alarm_temp = (Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报温度警示差
						high_temp_warning = (Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报温度
						high_alarm_temp2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示
						high_temp_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_temp_warning").toString()).floatValue();
						//低报温度差
						low_alarm_temp = (Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报温度警示差
						low_temp_warning = (Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//低报温度
						low_alarm_temp2 = Float.valueOf(pageData1.get(i - 1).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示
						low_temp_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_temp_warning").toString()).floatValue();
						//目标温度2
						set_temp3 = Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue();
						//高报温度2
						high_alarm_temp3 = Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示2
						high_temp_warning3 = Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue();
						//低报温度2
						low_alarm_temp3 = Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示2
						low_temp_warning3 = Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue();
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						choose = true;
						break;
					}
				}
			} else if (Integer.valueOf(pd1.get("alarm_type").toString()).intValue() == 2) {
				//计算负压差、基值
				for (int i = 0; i < pageData1.size(); i++) {
					if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == 0) {
						//目标负压差1
//					set_negative_pressure = (Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue() -
//							Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue())/
//					(Float.valueOf(pd.get("day_age").toString()).floatValue()*24);
						//目标负压1
//					set_negative_pressure2 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
						//光照目标值差1
						set_lux = (Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//光照目标值1
						set_lux2 = Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue();
						//光照上限值差1
						high_lux = (Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//光照上限警示差1
						high_lux_warning = (Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//光照上限值1
						high_lux2 = Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue();
						//光照上限警示1
						high_lux_warning2 = Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue();
						//光照下限值差1
						low_lux = (Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//光照下限警示差1
						low_lux_warning = (Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//光照下限值
						low_lux2 = Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue();
						//光照下限警示
						low_lux_warning2 = Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue();
						//光照目标值2
						set_lux3 = Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue();
						//光照上限值2
						high_lux3 = Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue();
						//光照上限警示2
						high_lux_warning3 = Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue();
						//光照下限值
						low_lux3 = Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue();
						//光照下限警示
						low_lux_warning3 = Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue();
						if (pageData1.size() != 1) {
							//目标负压差2
//						set_negative_pressure1 = (Float.valueOf(pageData1.get(i+1).get("set_negative_pressure").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue())/
//						((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
							//目标负压2
//						set_negative_pressure3 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
							//光照参照值差2
							set_lux1 = (Float.valueOf(pageData1.get(i + 1).get("set_lux").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//光照上限值差2
							high_lux1 = (Float.valueOf(pageData1.get(i + 1).get("high_lux").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//光照上限警示差2
							high_lux_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_lux_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//光照下限值差2
							low_lux1 = (Float.valueOf(pageData1.get(i + 1).get("low_lux").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//光照下限警示差2
							low_lux_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_lux_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
							day_age2 = (Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue()-1) * 7;
							String[] st2 = pageData1.get(i + 1).get("start_time").toString().split(":");
							startTime2 = Integer.valueOf(st2[0]).intValue();
							String[] et2 = pageData1.get(i + 1).get("end_time").toString().split(":");
							endTime2 = Integer.valueOf(et2[0]).intValue();
							choose3=true;
						}else{
							choose = true;
						}
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i > 0 && i < pageData1.size() - 1) {
						//目标负压差1
//						set_negative_pressure = (Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i-1).get("set_negative_pressure").toString()).floatValue())/
//						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//目标负压1
//						set_negative_pressure2 = Float.valueOf(pageData1.get(i-1).get("set_negative_pressure").toString()).floatValue();
						//光照目标值差1
						set_lux = (Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照目标值1
						set_lux2 = Float.valueOf(pageData1.get(i - 1).get("set_lux").toString()).floatValue();
						//光照上限值差1
						high_lux = (Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照上限警示差1
						high_lux_warning = (Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						
						//光照上限值1
						high_lux2 = Float.valueOf(pageData1.get(i - 1).get("high_lux").toString()).floatValue();
						//光照上限警示1
						high_lux_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_lux_warning").toString()).floatValue();
						//光照下限值差1
						low_lux = (Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照下限警示差1
						low_lux_warning = (Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						
						//光照下限值1
						low_lux2 = Float.valueOf(pageData1.get(i - 1).get("low_lux").toString()).floatValue();
						//光照下限警示1
						low_lux_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_lux_warning").toString()).floatValue();

						//目标负压差2
//						set_negative_pressure1 = (Float.valueOf(pageData1.get(i+1).get("set_negative_pressure").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue())/
//						((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
						//目标负压2
//						set_negative_pressure3 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
						//光照目标值差2
						set_lux1 = (Float.valueOf(pageData1.get(i + 1).get("set_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//光照目标值2
						set_lux3 = Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue();
						//光照上限值差2
						high_lux1 = (Float.valueOf(pageData1.get(i + 1).get("high_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//光照上限警示差2
						high_lux_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//光照上限值2
						high_lux3 = Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue();
						//光照上限警示2
						high_lux_warning3 = Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue();
						//光照下限值差2
						low_lux1 = (Float.valueOf(pageData1.get(i + 1).get("low_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//光照下限警示差2
						low_lux_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//光照下限值2
						low_lux3 = Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue();
						//光照下限警示2
						low_lux_warning3 = Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue() * 7;
						day_age = (Integer.valueOf(pageData1.get(i).get("day_age").toString()).intValue()-1) * 7;
						String[] st2 = pageData1.get(i + 1).get("start_time").toString().split(":");
						startTime2 = Integer.valueOf(st2[0]).intValue();
						String[] et2 = pageData1.get(i + 1).get("end_time").toString().split(":");
						endTime2 = Integer.valueOf(et2[0]).intValue();
						choose2 = true;
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == pageData1.size() - 1) {
						//目标负压差
//						set_negative_pressure = (Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i-1).get("set_negative_pressure").toString()).floatValue())/
//						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//目标负压
//						set_negative_pressure2 = Float.valueOf(pageData1.get(i-1).get("set_negative_pressure").toString()).floatValue();
						//光照目标值差
						set_lux = (Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照目标值
						set_lux2 = Float.valueOf(pageData1.get(i - 1).get("set_lux").toString()).floatValue();
						//光照上限值差
						high_lux = (Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照上限警示差
						high_lux_warning = (Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						
						//光照上限值
						high_lux2 = Float.valueOf(pageData1.get(i - 1).get("high_lux").toString()).floatValue();
						//光照上限警示
						high_lux_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_lux_warning").toString()).floatValue();
						//光照下限值差
						low_lux = (Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照下限警示差
						low_lux_warning = (Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						
						//光照下限值
						low_lux2 = Float.valueOf(pageData1.get(i - 1).get("low_lux").toString()).floatValue();
						//光照下限警示
						low_lux_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_lux_warning").toString()).floatValue();
						//光照目标值2
						set_lux3 = Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue();
						//光照上限值2
						high_lux3 = Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue();
						//光照上限警示2
						high_lux_warning3 = Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue();
						//光照下限值2
						low_lux3 = Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue();
						//光照下限警示2
						low_lux_warning3 = Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue();
						day_age = (Integer.valueOf(pageData1.get(i).get("day_age").toString()).intValue()-1) * 7;
						choose = true;
						break;
					}
				}
			} else if (Integer.valueOf(pd1.get("alarm_type").toString()).intValue() == 3) {
				//计算二氧化碳差、基值
				for (int i = 0; i < pageData1.size(); i++) {
					if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == 0) {
						//目标二氧化碳差1
						set_co2 = (Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//目标二氧化碳1
						set_co22 = Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue();
						//高报二氧化碳差1
						high_alarm_co2 = (Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//高报二氧化碳警示差1
						high_co2_warning = (Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//高报二氧化碳1
						high_alarm_co22 = Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue();
						//高报二氧化碳警示1
						high_co2_warning2 = Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue();
						//低报二氧化碳差1
//					low_alarm_co2 = (Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue() -
//							Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue())/
//					(Float.valueOf(pd.get("day_age").toString()).floatValue()*24);
						//低报二氧化碳1
//					low_alarm_co22 = Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue();
						if (pageData1.size() != 1) {
							//目标二氧化碳差2
							set_co21 = (Float.valueOf(pageData1.get(i + 1).get("set_co2").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//目标二氧化碳2
							set_co23 = Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue();
							//高报二氧化碳差2
							high_alarm_co21 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_co2").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//高报二氧化碳警示差2
							high_co2_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_co2_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//高报二氧化碳2
							high_alarm_co23 = Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue();
							//高报二氧化碳警示2
							high_co2_warning3 = Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue();
							//低报二氧化碳差2
//						low_alarm_co21 = (Float.valueOf(pageData1.get(i+1).get("low_alarm_co2").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue())/
//						((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
							//低报二氧化碳2
//						low_alarm_co23 = Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue();
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						}
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i > 0 && i < pageData1.size() - 1) {
						//目标二氧化碳差1
						set_co2 = (Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_co2").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标二氧化碳1
						set_co22 = Float.valueOf(pageData1.get(i - 1).get("set_co2").toString()).floatValue();
						//高报二氧化碳差1
						high_alarm_co2 = (Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_co2").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报二氧化碳警示差1
						high_co2_warning = (Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_co2_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报二氧化碳1
						high_alarm_co22 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_co2").toString()).floatValue();
						//高报二氧化碳警示1
						high_co2_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_co2_warning").toString()).floatValue();
						//低报二氧化碳差1
//						low_alarm_co2 = (Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i-1).get("low_alarm_co2").toString()).floatValue())/
//						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报二氧化碳1
//						low_alarm_co22 = Float.valueOf(pageData1.get(i-1).get("low_alarm_co2").toString()).floatValue();

						//目标二氧化碳差2
						set_co21 = (Float.valueOf(pageData1.get(i + 1).get("set_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//目标二氧化碳2
						set_co23 = Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue();
						//高报二氧化碳差2
						high_alarm_co21 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//高报二氧化碳警示差2
						high_co2_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_co2_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//高报二氧化碳2
						high_alarm_co23 = Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue();
						//高报二氧化碳警示2
						high_co2_warning3 = Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue();
						//低报二氧化碳差2
//						low_alarm_co21 = (Float.valueOf(pageData1.get(i+1).get("low_alarm_co2").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue())/
//						((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
						//低报二氧化碳2
//						low_alarm_co23 = Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == pageData1.size() - 1) {
						//目标二氧化碳差
						set_co2 = (Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_co2").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标二氧化碳
						set_co22 = Float.valueOf(pageData1.get(i - 1).get("set_co2").toString()).floatValue();
						//高报二氧化碳差
						high_alarm_co2 = (Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_co2").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报二氧化碳警示差
						high_co2_warning = (Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_co2_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报二氧化碳
						high_alarm_co22 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_co2").toString()).floatValue();
						//高报二氧化碳警示
						high_co2_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_co2_warning").toString()).floatValue();
						//低报二氧化碳差
//						low_alarm_co2 = (Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i-1).get("low_alarm_co2").toString()).floatValue())/
//						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报二氧化碳
//						low_alarm_co22 = Float.valueOf(pageData1.get(i-1).get("low_alarm_co2").toString()).floatValue();
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					}
				}
			}else if (Integer.valueOf(pd1.get("alarm_type").toString()).intValue() == 5) {
				//计算湿度差、基值
				for (int i = 0; i < pageData1.size(); i++) {
					if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == 0) {
						//目标湿度差1
						set_humidity = (Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//目标湿度1
						set_humidity2 = Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue();
						//高报湿度差1
						high_alarm_humidity = (Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//高报湿度警示差1
						high_humidity_warning = (Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//高报湿度1
						high_alarm_humidity2 = Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示1
						high_humidity_warning2 = Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue();
						//低报湿度差1
						low_alarm_humidity = (Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//低报湿度警示差1
						low_humidity_warning = (Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//低报湿度1
						low_alarm_humidity2 = Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示1
						low_humidity_warning2 = Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue();
						//目标湿度2
						set_humidity3 = Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue();
						//高报湿度2
						high_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示2
						high_humidity_warning3 = Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue();
						//低报湿度2
						low_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示2
						low_humidity_warning3 = Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue();
						if (pageData1.size() != 1) {
							//目标湿度差2
							set_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("set_humidity").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//高报湿度差2
							high_alarm_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_humidity").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//高报湿度警示差2
							high_humidity_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_humidity_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//低报湿度差2
							low_alarm_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("low_alarm_humidity").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//低报湿度警示差2
							low_humidity_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_humidity_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						}else{
							choose = true;
						}						
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i > 0 && i < pageData1.size() - 1) {
						//目标湿度差1
						set_humidity = (Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标湿度1
						set_humidity2 = Float.valueOf(pageData1.get(i - 1).get("set_humidity").toString()).floatValue();
						//高报湿度差1
						high_alarm_humidity = (Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报湿度警示差1
						high_humidity_warning = (Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报湿度1
						high_alarm_humidity2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示1
						high_humidity_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_humidity_warning").toString()).floatValue();
						//低报湿度差1
						low_alarm_humidity = (Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报湿度警示差1
						low_humidity_warning = (Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//低报湿度1
						low_alarm_humidity2 = Float.valueOf(pageData1.get(i - 1).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示1
						low_humidity_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_humidity_warning").toString()).floatValue();
//					if(!pageData1.get(i+1).get("set_humidity").equals("")){
						//目标湿度差2
						set_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("set_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//目标湿度2
						set_humidity3 = Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue();
						//高报湿度差2
						high_alarm_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//高报湿度警示差2
						high_humidity_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//高报湿度2
						high_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue();
						//高报温度警示2
						high_humidity_warning3 = Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue();
						//低报湿度差2
						low_alarm_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("low_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//低报湿度警示差2
						low_humidity_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//低报湿度2
						low_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示2
						low_humidity_warning3 = Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue();
//					}
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						choose2 = true;
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == pageData1.size() - 1) {
						//目标湿度差
						set_humidity = (Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标湿度
						set_humidity2 = Float.valueOf(pageData1.get(i - 1).get("set_humidity").toString()).floatValue();
						//高报湿度差
						high_alarm_humidity = (Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报湿度警示差
						high_humidity_warning = (Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报湿度
						high_alarm_humidity2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示
						high_humidity_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_humidity_warning").toString()).floatValue();
						//低报湿度差
						low_alarm_humidity = (Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报湿度警示差
						low_humidity_warning = (Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//低报湿度
						low_alarm_humidity2 = Float.valueOf(pageData1.get(i - 1).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示
						low_humidity_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_humidity_warning").toString()).floatValue();
						//目标湿度2
						set_humidity3 = Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue();
						//高报湿度2
						high_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示2
						high_humidity_warning3 = Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue();
						//低报湿度2
						low_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示2
						low_humidity_warning3 = Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue();
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						choose = true;
						break;
					}
				}
			}else if (Integer.valueOf(pd1.get("alarm_type").toString()).intValue() == 6) {
				//计算负压差、基值
				for (int i = 0; i < pageData1.size(); i++) {
					if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == 0) {
						//目标负压差1
						set_negative_pressure = (Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//目标负压1
						set_negative_pressure2 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
						//高报负压差1
						high_alarm_negative_pressure = (Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//高报负压警示差1
						high_negative_pressure_warning = (Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//高报负压1
						high_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
						//高报负压警示1
						high_negative_pressure_warning2 = Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue();
						//低报负压差1
					low_alarm_negative_pressure = (Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue() -
							Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue())/
					(Float.valueOf(pd.get("day_age").toString()).floatValue()*24);
						//低报负压1
					low_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
					//低报负压警示1
					low_negative_pressure_warning2 = Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue();
					//低报负压警示差1
					low_negative_pressure_warning = (Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue() -
							Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue()) /
							(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						if (pageData1.size() != 1) {
							//目标负压差2
							set_negative_pressure1 = (Float.valueOf(pageData1.get(i + 1).get("set_negative_pressure").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//目标负压2
							set_negative_pressure3 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
							//高报负压差2
							high_alarm_negative_pressure1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_negative_pressure").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//高报负压警示差2
							high_negative_pressure_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_negative_pressure_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//高报负压2
							high_alarm_negative_pressure3 = Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
							//高报负压警示2
							high_negative_pressure_warning3 = Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue();
							//低报负压差2
						low_alarm_negative_pressure1 = (Float.valueOf(pageData1.get(i+1).get("low_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue())/
						((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
							//低报负压2
						low_alarm_negative_pressure3 = Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
						//低报负压警示2
						low_negative_pressure_warning3 = Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue();
						//低报负压警示差2
						low_negative_pressure_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						}
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i > 0 && i < pageData1.size() - 1) {
						//目标负压差1
						set_negative_pressure = (Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标负压1
						set_negative_pressure2 = Float.valueOf(pageData1.get(i - 1).get("set_negative_pressure").toString()).floatValue();
						//高报负压差1
						high_alarm_negative_pressure = (Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报负压警示差1
						high_negative_pressure_warning = (Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报负压1
						high_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_negative_pressure").toString()).floatValue();
						//高报负压警示1
						high_negative_pressure_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_negative_pressure_warning").toString()).floatValue();
						//低报负压差1
						low_alarm_negative_pressure = (Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue())/
						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报负压1
						low_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue();
						//低报负压警示1
						low_negative_pressure_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_negative_pressure_warning").toString()).floatValue();
						//低报负压警示差1
						low_negative_pressure_warning = (Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);

						//目标负压差2
						set_negative_pressure1 = (Float.valueOf(pageData1.get(i + 1).get("set_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//目标负压2
						set_negative_pressure3 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
						//高报负压差2
						high_alarm_negative_pressure1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//高报负压警示差2
						high_negative_pressure_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//高报负压2
						high_alarm_negative_pressure3 = Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
						//高报负压警示2
						high_negative_pressure_warning3 = Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue();
						//低报负压差2
						low_alarm_negative_pressure1 = (Float.valueOf(pageData1.get(i+1).get("low_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue())/
						((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
						//低报负压2
						low_alarm_negative_pressure3 = Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
						//低报负压警示2
						low_negative_pressure_warning3 = Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue();
						//低报负压警示差2
						low_negative_pressure_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == pageData1.size() - 1) {
						//目标负压差
						set_negative_pressure = (Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标负压
						set_negative_pressure2 = Float.valueOf(pageData1.get(i - 1).get("set_negative_pressure").toString()).floatValue();
						//高报二氧化碳差
						high_alarm_negative_pressure = (Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报负压警示差
						high_negative_pressure_warning = (Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报负压
						high_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_negative_pressure").toString()).floatValue();
						//高报负压警示
						high_negative_pressure_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_negative_pressure_warning").toString()).floatValue();
						//低报负压差
						low_alarm_negative_pressure = (Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue())/
						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报负压
						low_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue();
						//低报负压警示
						low_negative_pressure_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_negative_pressure_warning").toString()).floatValue();
						//低报负压警示差
						low_negative_pressure_warning = (Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					}
				}
			} else {
				for (int i = 0; i < pageData1.size(); i++) {
					//计算耗水差、基值
					if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == 0) {
						//目标耗水差1
						set_water_deprivation = (Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//目标耗水1
						set_water_deprivation2 = Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue();
						//高报耗水差1
						high_water_deprivation = (Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//高报耗水1
						high_water_deprivation2 = Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue();
						//低报耗水差1
						low_water_deprivation = (Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue()) /
								(Float.valueOf(pd.get("day_age").toString()).floatValue() * 24);
						//低报耗水1
						low_water_deprivation2 = Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue();
						if (pageData1.size() != 1) {
							//目标耗水差2
							set_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("set_water_deprivation").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//目标耗水2
							set_water_deprivation3 = Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue();
							//高报耗水差2
							high_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("high_water_deprivation").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//高报耗水2
							high_water_deprivation3 = Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue();
							//低报耗水差2
							low_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("low_water_deprivation").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//低报耗水2
							low_water_deprivation3 = Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue();
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						}
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i > 0 && i < pageData1.size() - 1) {
						//目标耗水差1
						set_water_deprivation = (Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标耗水1
						set_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("set_water_deprivation").toString()).floatValue();
						//高报耗水差1
						high_water_deprivation = (Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报耗水1
						high_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("high_water_deprivation").toString()).floatValue();
						//低报耗水差1
						low_water_deprivation = (Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报耗水1
						low_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("low_water_deprivation").toString()).floatValue();
						//目标耗水差2
						set_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("set_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//目标耗水2
						set_water_deprivation3 = Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue();
						//高报耗水差2
						high_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("high_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//高报耗水2
						high_water_deprivation3 = Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue();
						//低报耗水差2
						low_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("low_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//低报耗水2
						low_water_deprivation3 = Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					} else if (pageData1.get(i).get("id").toString().equals(pd1.get("uid_num").toString()) && i == pageData1.size() - 1) {
						//目标耗水差
						set_water_deprivation = (Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标耗水
						set_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("set_water_deprivation").toString()).floatValue();
						//高报耗水差
						high_water_deprivation = (Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报耗水
						high_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("high_water_deprivation").toString()).floatValue();
						//低报耗水差
						low_water_deprivation = (Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报耗水
						low_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("low_water_deprivation").toString()).floatValue();
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					}
				}
			}

			//在子表中修改数据
			PageData pd3 = new PageData();
			pd3.put("uid_num", pd1.get("uid_num"));
			pd3.put("alarm_type", pd.get("alarm_type"));
			if(choose && Integer.valueOf(pd.get("alarm_type").toString()).intValue() != 2){
				pd3.put("day_age1", day_age);
			}
			if(choose2){
				if(Integer.valueOf(pd.get("alarm_type").toString()).intValue() != 2){
//					pd3.put("day_age1", day_age);
//				}else{
					pd3.put("day_age2", day_age2);
				}
			}
			if(choose3){
//				pd3.put("day_age2", day_age2);
				choose2=true;
			}
//			if(!choose3){
//				alarmService.deleteSBDayageTempSub(pd3);
//				}else{
//					choose2 = true;
//				}
//			List<PageData> pageData2 = alarmService.selectSBDayageTempSubByCondition(pd3);
			List<PageData> list = new ArrayList<PageData>();
			alarmService.deleteSBDayageTempSub(pd3);
			if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 1) {
				//温度修改
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd4 = new PageData();
						pd4.put("create_person", userId);
						pd4.put("create_date", new Date());
						pd4.put("create_time", new Date());
						pd4.put("uid_num", pd1.get("uid_num"));
						pd4.put("modify_person", userId);
						pd4.put("modify_date", new Date());
						pd4.put("modify_time", new Date());
						pd4.put("farmId", pd.get("farmId"));
						pd4.put("houseId", pd.get("houseId"));
						pd4.put("alarm_type", pd.get("alarm_type"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd4.put("day_age", i + 1);
						date.setHours(j);
						pd4.put("record_datetime", date);
						pd4.put("set_temp", set_temp2 + set_temp * ((i - day_age) * 24 + j));
						pd4.put("high_alarm_temp", high_alarm_temp2 + high_alarm_temp * ((i - day_age) * 24 + j));
						pd4.put("low_alarm_temp", low_alarm_temp2 + low_alarm_temp * ((i - day_age) * 24 + j));
						pd4.put("high_temp_warning", high_temp_warning2 + high_temp_warning * ((i - day_age) * 24 + j));
						pd4.put("low_temp_warning", low_temp_warning2 + low_temp_warning * ((i - day_age) * 24 + j));
						pd4.put("set_humidity", null);
						pd4.put("high_alarm_humidity", null);
						pd4.put("low_alarm_humidity", null);
						pd4.put("high_humidity_warning", null);
						pd4.put("low_humidity_warning", null);
						pd4.put("set_negative_pressure", null);
						pd4.put("high_alarm_negative_pressure", null);
						pd4.put("low_alarm_negative_pressure", null);
						pd4.put("high_negative_pressure_warning", null);
						pd4.put("low_negative_pressure_warning", null);
						pd4.put("set_lux", null);
						pd4.put("high_lux", null);
						pd4.put("low_lux", null);
						pd4.put("high_lux_warning", null);
						pd4.put("low_lux_warning", null);
						pd4.put("set_co2", null);
						pd4.put("high_alarm_co2", null);
						pd4.put("high_co2_warning", null);
						pd4.put("set_water_deprivation", null);
						pd4.put("high_water_deprivation", null);
						pd4.put("low_water_deprivation", null);
						pd4.put("is_start", "0");
						list.add(pd4);
//					alarmService.updateSBDayageTempSub(pd3);
					}
				}
				if(choose){
					for (int i = Integer.valueOf(pd.get("day_age").toString()).intValue(); i < Integer.valueOf(pd.get("dage").toString()).intValue(); i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd4 = new PageData();
							pd4.put("create_person", userId);
							pd4.put("create_date", new Date());
							pd4.put("create_time", new Date());
							pd4.put("uid_num", pd1.get("uid_num"));
							pd4.put("modify_person", userId);
							pd4.put("modify_date", new Date());
							pd4.put("modify_time", new Date());
							pd4.put("farmId", pd.get("farmId"));
							pd4.put("houseId", pd.get("houseId"));
							pd4.put("alarm_type", pd.get("alarm_type"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd4.put("day_age", i + 1);
							date.setHours(j);
							pd4.put("record_datetime", date);
							pd4.put("set_temp", set_temp3);
							pd4.put("high_alarm_temp", high_alarm_temp3);
							pd4.put("low_alarm_temp", low_alarm_temp3);
							pd4.put("high_temp_warning", high_temp_warning3);
							pd4.put("low_temp_warning", low_temp_warning3);
							pd4.put("set_humidity", null);
							pd4.put("high_alarm_humidity", null);
							pd4.put("low_alarm_humidity", null);
							pd4.put("high_humidity_warning", null);
							pd4.put("low_humidity_warning", null);
							pd4.put("set_negative_pressure", null);
							pd4.put("high_alarm_negative_pressure", null);
							pd4.put("low_alarm_negative_pressure", null);
							pd4.put("high_negative_pressure_warning", null);
							pd4.put("low_negative_pressure_warning", null);
							pd4.put("set_lux", null);
							pd4.put("high_lux", null);
							pd4.put("low_lux", null);
							pd4.put("high_lux_warning", null);
							pd4.put("low_lux_warning", null);
							pd4.put("set_co2", null);
							pd4.put("high_alarm_co2", null);
							pd4.put("high_co2_warning", null);
							pd4.put("set_water_deprivation", null);
							pd4.put("high_water_deprivation", null);
							pd4.put("low_water_deprivation", null);
							pd4.put("is_start", "0");
							list.add(pd4);
						}
					}
				}
				alarmService.saveSBDayageTempSub(list);
			} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2) {
				//负压修改
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd4 = new PageData();
						pd4.put("uid_num", pd1.get("uid_num"));
						pd4.put("create_person", userId);
						pd4.put("create_date", new Date());
						pd4.put("create_time", new Date());
						pd4.put("modify_person", userId);
						pd4.put("modify_date", new Date());
						pd4.put("modify_time", new Date());
						pd4.put("farmId", pd.get("farmId"));
						pd4.put("houseId", pd.get("houseId"));
						pd4.put("alarm_type", pd.get("alarm_type"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd4.put("day_age", i + 1);
						date.setHours(j);
						pd4.put("record_datetime", date);
						pd4.put("set_temp", null);
						pd4.put("high_alarm_temp", null);
						pd4.put("low_alarm_temp", null);
						pd4.put("high_temp_warning", null);
						pd4.put("low_temp_warning", null);
						pd4.put("set_humidity", null);
						pd4.put("high_alarm_humidity", null);
						pd4.put("low_alarm_humidity", null);
						pd4.put("high_humidity_warning", null);
						pd4.put("low_humidity_warning", null);
						pd4.put("set_negative_pressure", null);
						pd4.put("high_alarm_negative_pressure", null);
						pd4.put("low_alarm_negative_pressure", null);
						pd4.put("high_negative_pressure_warning", null);
						pd4.put("low_negative_pressure_warning", null);
//					pd3.put("set_negative_pressure", set_negative_pressure2+set_negative_pressure*(i+1));
//					pd4.put("set_lux",set_lux2+set_lux*((i-day_age)*24+j));
//					pd4.put("high_lux",high_lux2+high_lux*((i-day_age)*24+j));
//					pd4.put("low_lux",low_lux2+low_lux*((i-day_age)*24+j));
//						pd4.put("set_lux", set_lux2 + set_lux * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//						pd4.put("high_lux", high_lux2 + high_lux * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//						pd4.put("low_lux", low_lux2 + low_lux * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//						pd4.put("high_lux_warning", high_lux_warning2 + high_lux_warning * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//						pd4.put("low_lux_warning", low_lux_warning2 + low_lux_warning * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
						pd4.put("set_lux", set_lux3);
						pd4.put("high_lux", high_lux3);
						pd4.put("low_lux", low_lux3);
						pd4.put("high_lux_warning", high_lux_warning3);
						pd4.put("low_lux_warning", low_lux_warning3);
						pd4.put("set_co2", null);
						pd4.put("high_alarm_co2", null);
						pd4.put("high_co2_warning", null);
						pd4.put("set_water_deprivation", null);
						pd4.put("high_water_deprivation", null);
						pd4.put("low_water_deprivation", null);
						if (startTime <= j && j < endTime) {
							pd4.put("is_start", "0");
						} else {
							pd4.put("is_start", "1");
						}
						list.add(pd4);
//					alarmService.updateSBDayageTempSub(pd3);
					}
				}
				if(choose){
					for (int i = Integer.valueOf(pd.get("day_age").toString()).intValue(); i < Integer.valueOf(pd.get("dage").toString()).intValue(); i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd4 = new PageData();
							pd4.put("uid_num", pd1.get("uid_num"));
							pd4.put("create_person", userId);
							pd4.put("create_date", new Date());
							pd4.put("create_time", new Date());
							pd4.put("modify_person", userId);
							pd4.put("modify_date", new Date());
							pd4.put("modify_time", new Date());
							pd4.put("farmId", pd.get("farmId"));
							pd4.put("houseId", pd.get("houseId"));
							pd4.put("alarm_type", pd.get("alarm_type"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd4.put("day_age", i + 1);
							date.setHours(j);
							pd4.put("record_datetime", date);
							pd4.put("set_temp", null);
							pd4.put("high_alarm_temp", null);
							pd4.put("low_alarm_temp", null);
							pd4.put("high_temp_warning", null);
							pd4.put("low_temp_warning", null);
							pd4.put("set_humidity", null);
							pd4.put("high_alarm_humidity", null);
							pd4.put("low_alarm_humidity", null);
							pd4.put("high_humidity_warning", null);
							pd4.put("low_humidity_warning", null);
							pd4.put("set_negative_pressure", null);
							pd4.put("high_alarm_negative_pressure", null);
							pd4.put("low_alarm_negative_pressure", null);
							pd4.put("high_negative_pressure_warning", null);
							pd4.put("low_negative_pressure_warning", null);
							pd4.put("set_lux", set_lux3);
							pd4.put("high_lux", high_lux3);
							pd4.put("low_lux", low_lux3);
							pd4.put("high_lux_warning", high_lux_warning3);
							pd4.put("low_lux_warning", low_lux_warning3);
							pd4.put("set_co2", null);
							pd4.put("high_alarm_co2", null);
							pd4.put("high_co2_warning", null);
							pd4.put("set_water_deprivation", null);
							pd4.put("high_water_deprivation", null);
							pd4.put("low_water_deprivation", null);
							if (startTime <= j && j < endTime) {
								pd4.put("is_start", "0");
							} else {
								pd4.put("is_start", "1");
							}
							list.add(pd4);
//						alarmService.updateSBDayageTempSub(pd3);
						}
					}
				}
				alarmService.saveSBDayageTempSub(list);
			} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 3) {
				//二氧化碳修改
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd4 = new PageData();
						pd4.put("uid_num", pd1.get("uid_num"));
						pd4.put("create_person", userId);
						pd4.put("create_date", new Date());
						pd4.put("create_time", new Date());
						pd4.put("modify_person", userId);
						pd4.put("modify_date", new Date());
						pd4.put("modify_time", new Date());
						pd4.put("farmId", pd.get("farmId"));
						pd4.put("houseId", pd.get("houseId"));
						pd4.put("alarm_type", pd.get("alarm_type"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd4.put("day_age", i + 1);
						date.setHours(j);
						pd4.put("record_datetime", date);
						pd4.put("set_temp", null);
						pd4.put("high_alarm_temp", null);
						pd4.put("low_alarm_temp", null);
						pd4.put("high_temp_warning", null);
						pd4.put("low_temp_warning", null);
						pd4.put("set_humidity", null);
						pd4.put("high_alarm_humidity", null);
						pd4.put("low_alarm_humidity", null);
						pd4.put("high_humidity_warning", null);
						pd4.put("low_humidity_warning", null);
						pd4.put("set_negative_pressure", null);
						pd4.put("high_alarm_negative_pressure", null);
						pd4.put("low_alarm_negative_pressure", null);
						pd4.put("high_negative_pressure_warning", null);
						pd4.put("low_negative_pressure_warning", null);
						pd4.put("set_lux", null);
						pd4.put("high_lux", null);
						pd4.put("low_lux", null);
						pd4.put("high_lux_warning", null);
						pd4.put("low_lux_warning", null);
						pd4.put("set_co2", set_co22 + set_co2 * ((i - day_age) * 24 + j));
						pd4.put("high_alarm_co2", high_alarm_co22 + high_alarm_co2 * ((i - day_age) * 24 + j));
						pd4.put("high_co2_warning", high_co2_warning2 + high_co2_warning * ((i - day_age) * 24 + j));
//					pd3.put("low_alarm_co2",low_alarm_co22+low_alarm_co2*(i+1));
						pd4.put("set_water_deprivation", null);
						pd4.put("high_water_deprivation", null);
						pd4.put("low_water_deprivation", null);
						pd4.put("is_start", "0");
						list.add(pd4);
//					alarmService.updateSBDayageTempSub(pd3);
					}
				}
				alarmService.saveSBDayageTempSub(list);
			}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 5) {
				//湿度修改
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd4 = new PageData();
						pd4.put("create_person", userId);
						pd4.put("create_date", new Date());
						pd4.put("create_time", new Date());
						pd4.put("uid_num", pd1.get("uid_num"));
						pd4.put("modify_person", userId);
						pd4.put("modify_date", new Date());
						pd4.put("modify_time", new Date());
						pd4.put("farmId", pd.get("farmId"));
						pd4.put("houseId", pd.get("houseId"));
						pd4.put("alarm_type", pd.get("alarm_type"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd4.put("day_age", i + 1);
						date.setHours(j);
						pd4.put("record_datetime", date);
						pd4.put("set_temp", null);
						pd4.put("high_alarm_temp", null);
						pd4.put("low_alarm_temp", null);
						pd4.put("high_temp_warning", null);
						pd4.put("low_temp_warning", null);
						pd4.put("set_humidity", set_humidity2 + set_humidity * ((i - day_age) * 24 + j));
						pd4.put("high_alarm_humidity", high_alarm_humidity2 + high_alarm_humidity * ((i - day_age) * 24 + j));
						pd4.put("low_alarm_humidity", low_alarm_humidity2 + low_alarm_humidity * ((i - day_age) * 24 + j));
						pd4.put("high_humidity_warning", high_humidity_warning2 + high_humidity_warning * ((i - day_age) * 24 + j));
						pd4.put("low_humidity_warning", low_humidity_warning2 + low_humidity_warning * ((i - day_age) * 24 + j));
						pd4.put("set_negative_pressure", null);
						pd4.put("high_alarm_negative_pressure", null);
						pd4.put("low_alarm_negative_pressure", null);
						pd4.put("high_negative_pressure_warning", null);
						pd4.put("low_negative_pressure_warning", null);
						pd4.put("set_lux", null);
						pd4.put("high_lux", null);
						pd4.put("low_lux", null);
						pd4.put("high_lux_warning", null);
						pd4.put("low_lux_warning", null);
						pd4.put("set_co2", null);
						pd4.put("high_alarm_co2", null);
						pd4.put("high_co2_warning", null);
						pd4.put("set_water_deprivation", null);
						pd4.put("high_water_deprivation", null);
						pd4.put("low_water_deprivation", null);
						pd4.put("is_start", "0");
						list.add(pd4);
//					alarmService.updateSBDayageTempSub(pd3);
					}
				}
				if(choose){
					for (int i = Integer.valueOf(pd.get("day_age").toString()).intValue(); i < Integer.valueOf(pd.get("dage").toString()).intValue(); i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd4 = new PageData();
							pd4.put("create_person", userId);
							pd4.put("create_date", new Date());
							pd4.put("create_time", new Date());
							pd4.put("uid_num", pd1.get("uid_num"));
							pd4.put("modify_person", userId);
							pd4.put("modify_date", new Date());
							pd4.put("modify_time", new Date());
							pd4.put("farmId", pd.get("farmId"));
							pd4.put("houseId", pd.get("houseId"));
							pd4.put("alarm_type", pd.get("alarm_type"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd4.put("day_age", i + 1);
							date.setHours(j);
							pd4.put("record_datetime", date);
							pd4.put("set_temp", null);
							pd4.put("high_alarm_temp", null);
							pd4.put("low_alarm_temp", null);
							pd4.put("high_temp_warning", null);
							pd4.put("low_temp_warning", null);
							pd4.put("set_humidity", set_humidity3);
							pd4.put("high_alarm_humidity", high_alarm_humidity3);
							pd4.put("low_alarm_humidity", low_alarm_humidity3);
							pd4.put("high_humidity_warning", high_humidity_warning3);
							pd4.put("low_humidity_warning", low_humidity_warning3);
							pd4.put("set_negative_pressure", null);
							pd4.put("high_alarm_negative_pressure", null);
							pd4.put("low_alarm_negative_pressure", null);
							pd4.put("high_negative_pressure_warning", null);
							pd4.put("low_negative_pressure_warning", null);
							pd4.put("set_lux", null);
							pd4.put("high_lux", null);
							pd4.put("low_lux", null);
							pd4.put("high_lux_warning", null);
							pd4.put("low_lux_warning", null);
							pd4.put("set_co2", null);
							pd4.put("high_alarm_co2", null);
							pd4.put("high_co2_warning", null);
							pd4.put("set_water_deprivation", null);
							pd4.put("high_water_deprivation", null);
							pd4.put("low_water_deprivation", null);
							pd4.put("is_start", "0");
							list.add(pd4);
						}
					}
				}
				alarmService.saveSBDayageTempSub(list);
			}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 6) {
				//负压修改
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd4 = new PageData();
						pd4.put("uid_num", pd1.get("uid_num"));
						pd4.put("create_person", userId);
						pd4.put("create_date", new Date());
						pd4.put("create_time", new Date());
						pd4.put("modify_person", userId);
						pd4.put("modify_date", new Date());
						pd4.put("modify_time", new Date());
						pd4.put("farmId", pd.get("farmId"));
						pd4.put("houseId", pd.get("houseId"));
						pd4.put("alarm_type", pd.get("alarm_type"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd4.put("day_age", i + 1);
						date.setHours(j);
						pd4.put("record_datetime", date);
						pd4.put("set_temp", null);
						pd4.put("high_alarm_temp", null);
						pd4.put("low_alarm_temp", null);
						pd4.put("high_temp_warning", null);
						pd4.put("low_temp_warning", null);
						pd4.put("set_humidity", null);
						pd4.put("high_alarm_humidity", null);
						pd4.put("low_alarm_humidity", null);
						pd4.put("high_humidity_warning", null);
						pd4.put("low_humidity_warning", null);
						pd4.put("set_negative_pressure", set_negative_pressure2 + set_negative_pressure * ((i - day_age) * 24 + j));
						pd4.put("high_alarm_negative_pressure", high_alarm_negative_pressure2 + high_alarm_negative_pressure * ((i - day_age) * 24 + j));
						pd4.put("low_alarm_negative_pressure", low_alarm_negative_pressure2 + low_alarm_negative_pressure * ((i - day_age) * 24 + j));
						pd4.put("high_negative_pressure_warning", high_negative_pressure_warning2 + high_negative_pressure_warning * ((i - day_age) * 24 + j));
						pd4.put("low_negative_pressure_warning", low_negative_pressure_warning2 + low_negative_pressure_warning * ((i - day_age) * 24 + j));
						pd4.put("set_lux", null);
						pd4.put("high_lux", null);
						pd4.put("low_lux", null);
						pd4.put("high_lux_warning", null);
						pd4.put("low_lux_warning", null);
						pd4.put("set_co2", null);
						pd4.put("high_alarm_co2", null);
						pd4.put("high_co2_warning", null);
//					pd3.put("low_alarm_co2",low_alarm_co22+low_alarm_co2*(i+1));
						pd4.put("set_water_deprivation", null);
						pd4.put("high_water_deprivation", null);
						pd4.put("low_water_deprivation", null);
						pd4.put("is_start", "0");
						list.add(pd4);
//					alarmService.updateSBDayageTempSub(pd3);
					}
				}
				alarmService.saveSBDayageTempSub(list);
			} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 4) {
				//耗水修改
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd4 = new PageData();
						pd4.put("uid_num", pd1.get("uid_num"));
						pd4.put("create_person", userId);
						pd4.put("create_date", new Date());
						pd4.put("create_time", new Date());
						pd4.put("modify_person", userId);
						pd4.put("modify_date", new Date());
						pd4.put("modify_time", new Date());
						pd4.put("farmId", pd.get("farmId"));
						pd4.put("houseId", pd.get("houseId"));
						pd4.put("alarm_type", pd.get("alarm_type"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd4.put("day_age", i + 1);
						date.setHours(j);
						pd4.put("record_datetime", date);
						pd4.put("set_temp", null);
						pd4.put("high_alarm_temp", null);
						pd4.put("low_alarm_temp", null);
						pd4.put("set_lux", null);
						pd4.put("high_lux", null);
						pd4.put("low_lux", null);
						pd4.put("set_co2", null);
						pd4.put("high_alarm_co2", null);
						pd4.put("set_water_deprivation", set_water_deprivation2 + set_water_deprivation * ((i - day_age) * 24 + j));
						pd4.put("high_water_deprivation", high_water_deprivation2 + high_water_deprivation * ((i - day_age) * 24 + j));
						pd4.put("low_water_deprivation", low_water_deprivation2 + low_water_deprivation * ((i - day_age) * 24 + j));
						pd4.put("is_start", "0");
						list.add(pd4);
//					alarmService.updateSBDayageTempSub(pd3);
					}
				}
				alarmService.saveSBDayageTempSub(list);
			}

			if (choose2) {
				int day_age3 = Integer.valueOf(pd.get("day_age").toString()).intValue();
				PageData pd5 = new PageData();
				pd5.put("uid_num", uid_num);
				pd5.put("alarm_type", pd.get("alarm_type"));
//				pd5.put("day_age2", day_age2);
//				if(choose3){
//					pd5.put("day_age2", day_age2);
//				}else{
					if(Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2){
						pd5.put("day_age1", day_age);
					}else{
					pd5.put("day_age2", day_age2);
					}
//				}
//				   List<PageData> pageData3 = alarmService.selectSBDayageTempSubByCondition(pd5);
			  if(Integer.valueOf(pd.get("alarm_type").toString()).intValue() != 2){
				alarmService.deleteSBDayageTempSub(pd5);
			  }
				List<PageData> list2 = new ArrayList<PageData>();
				if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 1) {
					//修改相邻记录的温度
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd6 = new PageData();
							pd6.put("uid_num", uid_num);
							pd6.put("create_person", userId);
							pd6.put("create_date", new Date());
							pd6.put("create_time", new Date());
							pd6.put("modify_person", userId);
							pd6.put("modify_date", new Date());
							pd6.put("modify_time", new Date());
							pd6.put("alarm_type", pd.get("alarm_type"));
							pd6.put("farmId", pd.get("farmId"));
							pd6.put("houseId", pd.get("houseId"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd6.put("day_age", i + 1);
							date.setHours(j);
							pd6.put("record_datetime", date);
							pd6.put("set_temp", set_temp3 + set_temp1 * ((i - day_age3) * 24 + j));
							pd6.put("high_alarm_temp", high_alarm_temp3 + high_alarm_temp1 * ((i - day_age3) * 24 + j));
							pd6.put("low_alarm_temp", low_alarm_temp3 + low_alarm_temp1 * ((i - day_age3) * 24 + j));
							pd6.put("high_temp_warning", high_temp_warning3 + high_temp_warning1 * ((i - day_age3) * 24 + j));
							pd6.put("low_temp_warning", low_temp_warning3 + low_temp_warning1 * ((i - day_age3) * 24 + j));
							pd6.put("set_humidity", null);
							pd6.put("high_alarm_humidity", null);
							pd6.put("low_alarm_humidity", null);
							pd6.put("high_humidity_warning", null);
							pd6.put("low_humidity_warning", null);
							pd6.put("set_negative_pressure", null);
							pd6.put("high_alarm_negative_pressure", null);
							pd6.put("low_alarm_negative_pressure", null);
							pd6.put("high_negative_pressure_warning", null);
							pd6.put("low_negative_pressure_warning", null);
							pd6.put("set_lux", null);
							pd6.put("high_lux", null);
							pd6.put("low_lux", null);
							pd6.put("high_lux_warning", null);
							pd6.put("low_lux_warning", null);
							pd6.put("set_co2", null);
							pd6.put("high_alarm_co2", null);
							pd6.put("high_co2_warning", null);
							pd6.put("set_water_deprivation", null);
							pd6.put("high_water_deprivation", null);
							pd6.put("low_water_deprivation", null);
							pd6.put("is_start", "0");
							list2.add(pd6);
//							alarmService.updateSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2) {
					if(!choose3){
						day_age2 = day_age2-7;
					}
					//修改相邻记录的负压
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd6 = new PageData();
							pd6.put("uid_num", pd1.get("uid_num"));
							pd6.put("create_person", userId);
							pd6.put("create_date", new Date());
							pd6.put("create_time", new Date());
							pd6.put("modify_person", userId);
							pd6.put("modify_date", new Date());
							pd6.put("modify_time", new Date());
							pd6.put("alarm_type", pd.get("alarm_type"));
							pd6.put("farmId", pd.get("farmId"));
							pd6.put("houseId", pd.get("houseId"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd6.put("day_age", i + 1);
							date.setHours(j);
							pd6.put("record_datetime", date);
							pd6.put("set_temp", null);
							pd6.put("high_alarm_temp", null);
							pd6.put("low_alarm_temp", null);
							pd6.put("high_temp_warning", null);
							pd6.put("low_temp_warning", null);
							pd6.put("set_humidity", null);
							pd6.put("high_alarm_humidity", null);
							pd6.put("low_alarm_humidity", null);
							pd6.put("high_humidity_warning", null);
							pd6.put("low_humidity_warning", null);
							pd6.put("set_negative_pressure", null);
							pd6.put("high_alarm_negative_pressure", null);
							pd6.put("low_alarm_negative_pressure", null);
							pd6.put("high_negative_pressure_warning", null);
							pd6.put("low_negative_pressure_warning", null);
//							pd4.put("set_negative_pressure", set_negative_pressure3+set_negative_pressure1*(i+1));
//						   pd6.put("set_lux",set_lux3+set_lux1*((i-day_age3)*24+j));
//						   pd6.put("high_lux",high_lux3+high_lux1*((i-day_age3)*24+j));
//						   pd6.put("low_lux",low_lux3+low_lux1*((i-day_age3)*24+j));
//							pd6.put("set_lux", set_lux3 + set_lux1 * ((day_age2 - 1 - day_age3) * 24 + 24));
//							pd6.put("high_lux", high_lux3 + high_lux1 * ((day_age2 - 1 - day_age3) * 24 + 24));
//							pd6.put("low_lux", low_lux3 + low_lux1 * ((day_age2 - 1 - day_age3) * 24 + 24));
//							pd6.put("high_lux_warning", high_lux_warning3 + high_lux_warning1 * ((day_age2 - 1 - day_age3) * 24 + 24));
//							pd6.put("low_lux_warning", low_lux_warning3 + low_lux_warning1 * ((day_age2 - 1 - day_age3) * 24 + 24));
							pd6.put("set_lux", set_lux3);
							pd6.put("high_lux", high_lux3);
							pd6.put("low_lux", low_lux3);
							pd6.put("high_lux_warning", high_lux_warning3);
							pd6.put("low_lux_warning", low_lux_warning3);
							pd6.put("set_co2", null);
							pd6.put("high_alarm_co2", null);
							pd6.put("high_co2_warning", null);
							pd6.put("set_water_deprivation", null);
							pd6.put("high_water_deprivation", null);
							pd6.put("low_water_deprivation", null);
							if (startTime <= j && j < endTime) {
								pd6.put("is_start", "0");
							} else {
								pd6.put("is_start", "1");
							}
							list2.add(pd6);
//							alarmService.updateSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 3) {
					//修改相邻记录的二氧化碳
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd6 = new PageData();
							pd6.put("uid_num", uid_num);
							pd6.put("create_person", userId);
							pd6.put("create_date", new Date());
							pd6.put("create_time", new Date());
							pd6.put("modify_person", userId);
							pd6.put("modify_date", new Date());
							pd6.put("modify_time", new Date());
							pd6.put("alarm_type", pd.get("alarm_type"));
							pd6.put("farmId", pd.get("farmId"));
							pd6.put("houseId", pd.get("houseId"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd6.put("day_age", i + 1);
							date.setHours(j);
							pd6.put("record_datetime", date);
							pd6.put("set_temp", null);
							pd6.put("high_alarm_temp", null);
							pd6.put("low_alarm_temp", null);
							pd6.put("high_temp_warning", null);
							pd6.put("low_temp_warning", null);
							pd6.put("set_humidity", null);
							pd6.put("high_alarm_humidity", null);
							pd6.put("low_alarm_humidity", null);
							pd6.put("high_humidity_warning", null);
							pd6.put("low_humidity_warning", null);
							pd6.put("set_negative_pressure", null);
							pd6.put("high_alarm_negative_pressure", null);
							pd6.put("low_alarm_negative_pressure", null);
							pd6.put("high_negative_pressure_warning", null);
							pd6.put("low_negative_pressure_warning", null);
							pd6.put("set_lux", null);
							pd6.put("high_lux", null);
							pd6.put("low_lux", null);
							pd6.put("high_lux_warning", null);
							pd6.put("low_lux_warning", null);
							pd6.put("set_co2", set_co23 + set_co21 * ((i - day_age3) * 24 + j));
							pd6.put("high_alarm_co2", high_alarm_co23 + high_alarm_co21 * ((i - day_age3) * 24 + j));
							pd6.put("high_co2_warning", high_co2_warning3 + high_co2_warning1 * ((i - day_age3) * 24 + j));
//							pd4.put("low_alarm_co2",low_alarm_co23+low_alarm_co21*(i+1));
							pd6.put("set_water_deprivation", null);
							pd6.put("high_water_deprivation", null);
							pd6.put("low_water_deprivation", null);
							pd6.put("is_start", "0");
							list2.add(pd6);
//							alarmService.updateSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 5) {
					//修改相邻记录的湿度
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd6 = new PageData();
							pd6.put("uid_num", uid_num);
							pd6.put("create_person", userId);
							pd6.put("create_date", new Date());
							pd6.put("create_time", new Date());
							pd6.put("modify_person", userId);
							pd6.put("modify_date", new Date());
							pd6.put("modify_time", new Date());
							pd6.put("alarm_type", pd.get("alarm_type"));
							pd6.put("farmId", pd.get("farmId"));
							pd6.put("houseId", pd.get("houseId"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd6.put("day_age", i + 1);
							date.setHours(j);
							pd6.put("record_datetime", date);
							pd6.put("set_temp", null);
							pd6.put("high_alarm_temp", null);
							pd6.put("low_alarm_temp", null);
							pd6.put("high_temp_warning", null);
							pd6.put("low_temp_warning", null);
							pd6.put("set_humidity", set_humidity3 + set_humidity1 * ((i - day_age3) * 24 + j));
							pd6.put("high_alarm_humidity", high_alarm_humidity3 + high_alarm_humidity1 * ((i - day_age3) * 24 + j));
							pd6.put("low_alarm_humidity", low_alarm_humidity3 + low_alarm_humidity1 * ((i - day_age3) * 24 + j));
							pd6.put("high_humidity_warning", high_humidity_warning3 + high_humidity_warning1 * ((i - day_age3) * 24 + j));
							pd6.put("low_humidity_warning", low_humidity_warning3 + low_humidity_warning1 * ((i - day_age3) * 24 + j));
							pd6.put("set_negative_pressure", null);
							pd6.put("high_alarm_negative_pressure", null);
							pd6.put("low_alarm_negative_pressure", null);
							pd6.put("high_negative_pressure_warning", null);
							pd6.put("low_negative_pressure_warning", null);
							pd6.put("set_lux", null);
							pd6.put("high_lux", null);
							pd6.put("low_lux", null);
							pd6.put("high_lux_warning", null);
							pd6.put("low_lux_warning", null);
							pd6.put("set_co2", null);
							pd6.put("high_alarm_co2", null);
							pd6.put("high_co2_warning", null);
							pd6.put("set_water_deprivation", null);
							pd6.put("high_water_deprivation", null);
							pd6.put("low_water_deprivation", null);
							pd6.put("is_start", "0");
							list2.add(pd6);
//							alarmService.updateSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 6) {
					//修改相邻记录的负压
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd6 = new PageData();
							pd6.put("uid_num", uid_num);
							pd6.put("create_person", userId);
							pd6.put("create_date", new Date());
							pd6.put("create_time", new Date());
							pd6.put("modify_person", userId);
							pd6.put("modify_date", new Date());
							pd6.put("modify_time", new Date());
							pd6.put("alarm_type", pd.get("alarm_type"));
							pd6.put("farmId", pd.get("farmId"));
							pd6.put("houseId", pd.get("houseId"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd6.put("day_age", i + 1);
							date.setHours(j);
							pd6.put("record_datetime", date);
							pd6.put("set_temp", null);
							pd6.put("high_alarm_temp", null);
							pd6.put("low_alarm_temp", null);
							pd6.put("high_temp_warning", null);
							pd6.put("low_temp_warning", null);
							pd6.put("set_humidity", null);
							pd6.put("high_alarm_humidity", null);
							pd6.put("low_alarm_humidity", null);
							pd6.put("high_humidity_warning", null);
							pd6.put("low_humidity_warning", null);
							pd6.put("set_negative_pressure", set_negative_pressure3 + set_negative_pressure1 * ((i - day_age3) * 24 + j));
							pd6.put("high_alarm_negative_pressure", high_alarm_negative_pressure3 + high_alarm_negative_pressure1 * ((i - day_age3) * 24 + j));
							pd6.put("low_alarm_negative_pressure", low_alarm_negative_pressure3 + low_alarm_negative_pressure1 * ((i - day_age3) * 24 + j));
							pd6.put("high_negative_pressure_warning", high_negative_pressure_warning3 + high_negative_pressure_warning1 * ((i - day_age3) * 24 + j));
							pd6.put("low_negative_pressure_warning", low_negative_pressure_warning3 + low_negative_pressure_warning1 * ((i - day_age3) * 24 + j));
							pd6.put("set_lux", null);
							pd6.put("high_lux", null);
							pd6.put("low_lux", null);
							pd6.put("high_lux_warning", null);
							pd6.put("low_lux_warning", null);
							pd6.put("set_co2", null);
							pd6.put("high_alarm_co2", null);
							pd6.put("high_co2_warning", null);
//							pd4.put("low_alarm_co2",low_alarm_co23+low_alarm_co21*(i+1));
							pd6.put("set_water_deprivation", null);
							pd6.put("high_water_deprivation", null);
							pd6.put("low_water_deprivation", null);
							pd6.put("is_start", "0");
							list2.add(pd6);
//							alarmService.updateSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				} else {
					//修改相邻记录的耗水
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd6 = new PageData();
							pd6.put("uid_num", uid_num);
							pd6.put("create_person", userId);
							pd6.put("create_date", new Date());
							pd6.put("create_time", new Date());
							pd6.put("modify_person", userId);
							pd6.put("modify_date", new Date());
							pd6.put("modify_time", new Date());
							pd6.put("alarm_type", pd.get("alarm_type"));
							pd6.put("farmId", pd.get("farmId"));
							pd6.put("houseId", pd.get("houseId"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd6.put("day_age", i + 1);
							date.setHours(j);
							pd6.put("record_datetime", date);
							pd6.put("set_temp", null);
							pd6.put("high_alarm_temp", null);
							pd6.put("low_alarm_temp", null);
							pd6.put("set_lux", null);
							pd6.put("high_lux", null);
							pd6.put("low_lux", null);
							pd6.put("set_co2", null);
							pd6.put("high_alarm_co2", null);
							pd6.put("set_water_deprivation", set_water_deprivation3 + set_water_deprivation1 * ((i - day_age3) * 24 + j));
							pd6.put("high_water_deprivation", high_water_deprivation3 + high_water_deprivation1 * ((i - day_age3) * 24 + j));
							pd6.put("low_water_deprivation", low_water_deprivation3 + low_water_deprivation1 * ((i - day_age3) * 24 + j));
							pd6.put("is_start", "0");
							list2.add(pd6);
//							alarmService.updateSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				}
			}
			g.setMsg("1");
			g.setSuccess(true);
		} catch (Exception e) {
			g.setMsg("2");
			g.setSuccess(false);
			e.printStackTrace();
		}
		return g;
	}
	
	@RequestMapping("/addAlarm")
	public void addAlarm(HttpServletResponse response,HttpServletRequest request) throws Exception {
		Json j2 = new Json();
		SDUser user = (SDUser) request.getSession().getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2) {
			pd.put("day_age", Integer.valueOf(pd.get("day_age").toString()).intValue() * 7);
		}
		List<PageData> pageData5 = alarmService.selectByCondition3(pd);//主要条件：农场、栋舍、日龄
		int pdID = 0;

		if (pageData5.size() == 0) {
			pd.put("create_person", user.getId());
			pd.put("create_date", new Date());
			pd.put("create_time", new Date());
			pd.put("modify_person", user.getId());
			pd.put("modify_date", new Date());
			pd.put("modify_time", new Date());
			alarmService.saveSBDayageSettingSub(pd);
			pdID = Integer.valueOf(alarmService.selectByCondition3(pd).get(0).get("id").toString()).intValue();
		} else {
			pd.put("modify_person", user.getId());
			pd.put("modify_date", new Date());
			pd.put("modify_time", new Date());
			pdID = Integer.valueOf(pageData5.get(0).get("id").toString()).intValue();
			pd.put("uid_num", pdID);
			alarmService.updateSBDayageSettingSub(pd);
		}
		pd.put("alarm_operation", "新增");
		alarmService.saveSbDayageSettingSubHis(pd);

		j2 = tempSubSave(pd, user.getId(), pdID);
		List<PageData> mcl = alarmService.selectByCondition(pd);
		List<PageData> mc2 = alarmService.selectSBHouseAlarmByCondition(pd);
		if (mc2.size() != 0) {
			j2.setObj1(mc2.get(0));
		} else {
			j2.setObj1("");
		}
		j2.setSuccess(true);
		j2.setObj(mcl);
		super.writeJson(j2, response);
	}

	public Json tempSubSave(PageData pd, int userId, int pdID) {
		Json j2 = new Json();
		int startTime = 0;
		int endTime = 0;
		float set_temp = 0, high_alarm_temp = 0, high_temp_warning = 0, low_alarm_temp = 0, low_temp_warning = 0, 
				set_temp1 = 0, high_alarm_temp1 = 0, high_temp_warning1 = 0, low_alarm_temp1 = 0, low_temp_warning1 = 0, 
				set_temp2 = 0, high_alarm_temp2 = 0, high_temp_warning2 = 0, low_alarm_temp2 = 0, low_temp_warning2 = 0,
				set_temp3 = 0, high_alarm_temp3 = 0, high_temp_warning3 = 0, low_alarm_temp3 = 0, low_temp_warning3 = 0,
				set_lux = 0, high_lux = 0, high_lux_warning = 0,
				set_lux1 = 0, high_lux1 = 0, high_lux_warning1 = 0, 
				set_lux2 = 0, high_lux2 = 0, high_lux_warning2 = 0, 
				set_lux3 = 0, high_lux3 = 0, high_lux_warning3 = 0,
				low_lux = 0, low_lux_warning = 0, low_lux1 = 0, low_lux_warning1 = 0, low_lux2 = 0, low_lux_warning2 = 0, low_lux3 = 0, low_lux_warning3 = 0,
				set_co2 = 0, high_alarm_co2 = 0, high_co2_warning = 0, set_co21 = 0, high_alarm_co21 = 0, high_co2_warning1 = 0, 
				set_co22 = 0, high_alarm_co22 = 0, high_co2_warning2 = 0,
				set_co23 = 0, high_alarm_co23 = 0, high_co2_warning3 = 0,
			    set_humidity = 0, high_alarm_humidity = 0, high_humidity_warning = 0, low_alarm_humidity = 0, low_humidity_warning = 0, 
				set_humidity1 = 0, high_alarm_humidity1 = 0, high_humidity_warning1 = 0, low_alarm_humidity1 = 0, low_humidity_warning1 = 0, 
				set_humidity2 = 0, high_alarm_humidity2 = 0, high_humidity_warning2 = 0, low_alarm_humidity2 = 0, low_humidity_warning2 = 0,
				set_humidity3 = 0, high_alarm_humidity3 = 0, high_humidity_warning3 = 0, low_alarm_humidity3 = 0, low_humidity_warning3 = 0,
				set_negative_pressure = 0, high_alarm_negative_pressure = 0, high_negative_pressure_warning = 0, low_alarm_negative_pressure = 0, low_negative_pressure_warning = 0, 
			    set_negative_pressure1 = 0, high_alarm_negative_pressure1 = 0, high_negative_pressure_warning1 = 0, low_alarm_negative_pressure1 = 0, low_negative_pressure_warning1 = 0, 
				set_negative_pressure2 = 0, high_alarm_negative_pressure2 = 0, high_negative_pressure_warning2 = 0, low_alarm_negative_pressure2 = 0, low_negative_pressure_warning2 = 0,
				set_negative_pressure3 = 0, high_alarm_negative_pressure3 = 0, high_negative_pressure_warning3 = 0, low_alarm_negative_pressure3 = 0, low_negative_pressure_warning3 = 0,
				set_water_deprivation = 0, high_water_deprivation = 0, low_water_deprivation = 0, set_water_deprivation1 = 0, high_water_deprivation1 = 0, low_water_deprivation1 = 0,
				set_water_deprivation2 = 0, high_water_deprivation2 = 0, low_water_deprivation2 = 0, set_water_deprivation3 = 0, high_water_deprivation3 = 0, low_water_deprivation3 = 0;
		int uid_num = 0;
		int day_age = 0;
		int day_age2 = 0, startTime2 = 0, endTime2 = 0;
		boolean choose = false,choose2 = false,choose3=false;
		try {
			List<PageData> pageData1 = alarmService.selectByCondition(pd);//查询一个栋舍的全部记录
			if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 1) {
				//计算温度差、基值
				for (int i = 0; i < pageData1.size(); i++) {
					if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i == 0) {
						//目标温度1
						set_temp2 = Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue();
						//高报温度1
						high_alarm_temp2 = Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示1
						high_temp_warning2 = Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue();
						//低报温度1
						low_alarm_temp2 = Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示1
						low_temp_warning2 = Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue();
						//目标温度2
						set_temp3 = Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue();
						//高报温度2
						high_alarm_temp3 = Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示2
						high_temp_warning3 = Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue();
						//低报温度2
						low_alarm_temp3 = Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示2
						low_temp_warning3 = Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue();
						if (pageData1.size() != 1) {
							if (!pageData1.get(i + 1).get("set_temp").equals("")) {
								//目标温度差2
								set_temp1 = (Float.valueOf(pageData1.get(i + 1).get("set_temp").toString()).floatValue() -
										Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue()) /
										((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
								
								//高报温度差2
								high_alarm_temp1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_temp").toString()).floatValue() -
										Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue()) /
										((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
								//高报温度警示差2
								high_temp_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_temp_warning").toString()).floatValue() -
										Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue()) /
										((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
								
								//低报温度差2
								low_alarm_temp1 = (Float.valueOf(pageData1.get(i + 1).get("low_alarm_temp").toString()).floatValue() -
										Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue()) /
										((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
								//低报温度警示差2
								low_temp_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_temp_warning").toString()).floatValue() -
										Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue()) /
										((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
								
								//uid_num
								uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
								day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
								choose3=true;
							}
						}else{
							choose =true;
						}
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i > 0 && i < pageData1.size() - 1) {
						//目标温度差1
						set_temp = (Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标温度1
						set_temp2 = Float.valueOf(pageData1.get(i - 1).get("set_temp").toString()).floatValue();
						//高报温度差1
						high_alarm_temp = (Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报温度警示差1
						high_temp_warning = (Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报温度1
						high_alarm_temp2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示1
						high_temp_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_temp_warning").toString()).floatValue();
						//低报温度差1
						low_alarm_temp = (Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报温度警示差1
						low_temp_warning = (Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//低报温度1
						low_alarm_temp2 = Float.valueOf(pageData1.get(i - 1).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示1
						low_temp_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_temp_warning").toString()).floatValue();

						//目标温度差2
						set_temp1 = (Float.valueOf(pageData1.get(i + 1).get("set_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//目标温度2
						set_temp3 = Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue();
						//高报温度差2
						high_alarm_temp1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//高报温度警示差2
						high_temp_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//高报温度2
						high_alarm_temp3 = Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示2
						high_temp_warning3 = Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue();
						//低报温度差2
						low_alarm_temp1 = (Float.valueOf(pageData1.get(i + 1).get("low_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//低报温度警示差2
						low_temp_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//低报温度2
						low_alarm_temp3 = Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示2
						low_temp_warning3 = Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						//日龄
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						choose2 = true;
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i == pageData1.size() - 1) {
						//目标温度差
						set_temp = (Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标温度
						set_temp2 = Float.valueOf(pageData1.get(i - 1).get("set_temp").toString()).floatValue();
						//高报温度差
						high_alarm_temp = (Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报温度警示差
						high_temp_warning = (Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报温度
						high_alarm_temp2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示
						high_temp_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_temp_warning").toString()).floatValue();
						//低报温度差
						low_alarm_temp = (Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_alarm_temp").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报温度警示差
						low_temp_warning = (Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_temp_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//低报温度
						low_alarm_temp2 = Float.valueOf(pageData1.get(i - 1).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示
						low_temp_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_temp_warning").toString()).floatValue();
						//目标温度2
						set_temp3 = Float.valueOf(pageData1.get(i).get("set_temp").toString()).floatValue();
						//高报温度2
						high_alarm_temp3 = Float.valueOf(pageData1.get(i).get("high_alarm_temp").toString()).floatValue();
						//高报温度警示2
						high_temp_warning3 = Float.valueOf(pageData1.get(i).get("high_temp_warning").toString()).floatValue();
						//低报温度2
						low_alarm_temp3 = Float.valueOf(pageData1.get(i).get("low_alarm_temp").toString()).floatValue();
						//低报温度警示2
						low_temp_warning3 = Float.valueOf(pageData1.get(i).get("low_temp_warning").toString()).floatValue();
						uid_num = Integer.valueOf(pageData1.get(i - 1).get("id").toString()).intValue();
						//日龄
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						choose = true;
						break;
					}
				}
			} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2) {
				//计算负压差、基值
				for (int i = 0; i < pageData1.size(); i++) {
					int d = Integer.valueOf(pageData1.get(i).get("day_age").toString()).intValue() * 7;
					if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							d == Integer.valueOf(pd.get("day_age").toString()).intValue() && i == 0) {
						//目标负压1
//					set_negative_pressure2 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
						//光照目标值1
						set_lux2 = Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue();
						//光照上限值1
						high_lux2 = Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue();
						//光照上限警示1
						high_lux_warning2 = Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue();
						//光照下限值1
						low_lux2 = Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue();
						//光照下限警示1
						low_lux_warning2 = Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue();
						//光照上限值2
						high_lux3 = Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue();
						//光照上限警示2
						high_lux_warning3 = Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue();
						//光照目标值2
						set_lux3 = Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue();
						//光照下限值2
						low_lux3 = Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue();
						//光照下限警示2
						low_lux_warning3 = Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue();
						if (pageData1.size() != 1) {
							//目标负压差2
//						set_negative_pressure1 = (Float.valueOf(pageData1.get(i+1).get("set_negative_pressure").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue())/
//						((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
							//目标负压2
//						set_negative_pressure3 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
							//光照目标值差2
							set_lux1 = (Float.valueOf(pageData1.get(i + 1).get("set_lux").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//光照上限值差2
							high_lux1 = (Float.valueOf(pageData1.get(i + 1).get("high_lux").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//光照上限警示差2
							high_lux_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_lux_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//光照下限值差2
							low_lux1 = (Float.valueOf(pageData1.get(i + 1).get("low_lux").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//光照下限警示差2
							low_lux_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_lux_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
							day_age2 = (Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue()-1) * 7;
							String[] st2 = pageData1.get(i + 1).get("start_time").toString().split(":");
							startTime2 = Integer.valueOf(st2[0]).intValue();
							String[] et2 = pageData1.get(i + 1).get("end_time").toString().split(":");
							endTime2 = Integer.valueOf(et2[0]).intValue();
							choose3=true;
						}
						else{
							choose=true;
						}
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							d == Integer.valueOf(pd.get("day_age").toString()).intValue() && i > 0 && i < pageData1.size() - 1) {
						//目标负压差1
//						set_negative_pressure = (Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i-1).get("set_negative_pressure").toString()).floatValue())/
//						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//目标负压1
//						set_negative_pressure2 = Float.valueOf(pageData1.get(i-1).get("set_negative_pressure").toString()).floatValue();
						//光照目标值差1
						set_lux = (Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照目标值1
						set_lux2 = Float.valueOf(pageData1.get(i - 1).get("set_lux").toString()).floatValue();
						//光照上限值差1
						high_lux = (Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照上限警示差1
						high_lux_warning = (Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						
						//光照上限值1
						high_lux2 = Float.valueOf(pageData1.get(i - 1).get("high_lux").toString()).floatValue();
						//光照上限警示1
						high_lux_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_lux_warning").toString()).floatValue();
						//光照下限值差1
						low_lux = (Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照下限警示差1
						low_lux_warning = (Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						
						//光照下限值1
						low_lux2 = Float.valueOf(pageData1.get(i - 1).get("low_lux").toString()).floatValue();
						//光照下限警示1
						low_lux_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_lux_warning").toString()).floatValue();
						//目标负压差2
//						set_negative_pressure1 = (Float.valueOf(pageData1.get(i+1).get("set_negative_pressure").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue())/
//						((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
						//目标负压2
//						set_negative_pressure3 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
						//光照目标值差2
						set_lux1 = (Float.valueOf(pageData1.get(i + 1).get("set_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//光照目标值2
						set_lux3 = Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue();
						//光照上限值差2
						high_lux1 = (Float.valueOf(pageData1.get(i + 1).get("high_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//光照上限警示差2
						high_lux_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//光照上限值2
						high_lux3 = Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue();
						//光照上限警示2
						high_lux_warning3 = Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue();
						//光照下限值差2
						low_lux1 = (Float.valueOf(pageData1.get(i + 1).get("low_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//光照下限警示差2
						low_lux_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() * 7 - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//光照下限值2
						low_lux3 = Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue();
						//光照下限警示2
						low_lux_warning3 = Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i - 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i+1).get("day_age").toString()).intValue() * 7;
						//日龄
						day_age = (Integer.valueOf(pageData1.get(i).get("day_age").toString()).intValue()-1) * 7;
						String[] st2 = pageData1.get(i + 1).get("start_time").toString().split(":");
						startTime2 = Integer.valueOf(st2[0]).intValue();
						String[] et2 = pageData1.get(i + 1).get("end_time").toString().split(":");
						endTime2 = Integer.valueOf(et2[0]).intValue();
						choose2 = true;
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							d == Integer.valueOf(pd.get("day_age").toString()).intValue() && i == pageData1.size() - 1) {
						//目标负压差
//						set_negative_pressure = (Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i-1).get("set_negative_pressure").toString()).floatValue())/
//						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//目标负压
//						set_negative_pressure2 = Float.valueOf(pageData1.get(i-1).get("set_negative_pressure").toString()).floatValue();
						//光照目标值差
						set_lux = (Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照目标值
						set_lux2 = Float.valueOf(pageData1.get(i - 1).get("set_lux").toString()).floatValue();
						//光照上限值差
						high_lux = (Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照上限警示差
						high_lux_warning = (Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						
						//光照上限值
						high_lux2 = Float.valueOf(pageData1.get(i - 1).get("high_lux").toString()).floatValue();
						//光照上限警示
						high_lux_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_lux_warning").toString()).floatValue();
						//光照下限值差
						low_lux = (Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_lux").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						//光照下限值差
						low_lux_warning = (Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_lux_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue() * 7) * 24);
						
						//光照下限值
						low_lux2 = Float.valueOf(pageData1.get(i - 1).get("low_lux").toString()).floatValue();
						//光照下限警示
						low_lux_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_lux_warning").toString()).floatValue();
						//光照目标值2
						set_lux3 = Float.valueOf(pageData1.get(i).get("set_lux").toString()).floatValue();
						//光照上限值2
						high_lux3 = Float.valueOf(pageData1.get(i).get("high_lux").toString()).floatValue();
						//光照上限警示2
						high_lux_warning3 = Float.valueOf(pageData1.get(i).get("high_lux_warning").toString()).floatValue();
						//光照下限值2
						low_lux3 = Float.valueOf(pageData1.get(i).get("low_lux").toString()).floatValue();
						//光照下限警示2
						low_lux_warning3 = Float.valueOf(pageData1.get(i).get("low_lux_warning").toString()).floatValue();
						uid_num = Integer.valueOf(pageData1.get(i - 1).get("id").toString()).intValue();
						//日龄
						day_age = (Integer.valueOf(pageData1.get(i).get("day_age").toString()).intValue()-1) * 7;
						choose = true;
						break;
					}
				}
			} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 3) {
				//计算二氧化碳差、基值
				for (int i = 0; i < pageData1.size(); i++) {
					if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i == 0) {
						//目标二氧化碳1
						set_co22 = Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue();
						//高报二氧化碳1
						high_alarm_co22 = Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue();
						//高报二氧化碳警示1
						high_co2_warning2 = Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue();
						//低报二氧化碳1
//					low_alarm_co22 = Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue();
						if (pageData1.size() != 1) {
							//目标二氧化碳差2
							set_co21 = (Float.valueOf(pageData1.get(i + 1).get("set_co2").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//目标二氧化碳2
							set_co23 = Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue();
							//高报二氧化碳差2
							high_alarm_co21 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_co2").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//高报二氧化碳警示差2
							high_co2_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_co2_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//高报二氧化碳2
							high_alarm_co23 = Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue();
							//高报二氧化碳警示2
							high_co2_warning3 = Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue();
							//低报二氧化碳差2
//						low_alarm_co21 = (Float.valueOf(pageData1.get(i+1).get("low_alarm_co2").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue())/
//						((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
							//低报二氧化碳2
//						low_alarm_co23 = Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue();
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						}
                        uid_num = Integer.valueOf(pageData1.get(i).get("id").toString()).intValue(); //temp solution
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i > 0 && i < pageData1.size() - 1) {
						//目标二氧化碳差1
						set_co2 = (Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_co2").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标二氧化碳1
						set_co22 = Float.valueOf(pageData1.get(i - 1).get("set_co2").toString()).floatValue();
						//高报二氧化碳差1
						high_alarm_co2 = (Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_co2").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报二氧化碳警示差1
						high_co2_warning = (Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_co2_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报二氧化碳1
						high_alarm_co22 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_co2").toString()).floatValue();
						//高报二氧化碳警示1
						high_co2_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_co2_warning").toString()).floatValue();
						//低报二氧化碳差1
//						low_alarm_co2 = (Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i-1).get("low_alarm_co2").toString()).floatValue())/
//						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报二氧化碳1
//						low_alarm_co22 = Float.valueOf(pageData1.get(i-1).get("low_alarm_co2").toString()).floatValue();
						//目标二氧化碳差2
						set_co21 = (Float.valueOf(pageData1.get(i + 1).get("set_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//目标二氧化碳2
						set_co23 = Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue();
						//高报二氧化碳差2
						high_alarm_co21 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//高报二氧化碳警示差2
						high_co2_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_co2_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//高报二氧化碳2
						high_alarm_co23 = Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue();
						//高报二氧化碳警示2
						high_co2_warning3 = Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue();
						//低报二氧化碳差2
//						low_alarm_co21 = (Float.valueOf(pageData1.get(i+1).get("low_alarm_co2").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue())/
//						((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
						//低报二氧化碳2
//						low_alarm_co23 = Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						//日龄
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i == pageData1.size() - 1) {
						//目标二氧化碳差
						set_co2 = (Float.valueOf(pageData1.get(i).get("set_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_co2").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标二氧化碳
						set_co22 = Float.valueOf(pageData1.get(i - 1).get("set_co2").toString()).floatValue();
						//高报二氧化碳差
						high_alarm_co2 = (Float.valueOf(pageData1.get(i).get("high_alarm_co2").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_co2").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报二氧化碳警示差
						high_co2_warning = (Float.valueOf(pageData1.get(i).get("high_co2_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_co2_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报二氧化碳
						high_alarm_co22 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_co2").toString()).floatValue();
						//高报二氧化碳警示
						high_co2_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_co2_warning").toString()).floatValue();
						//低报二氧化碳差
//						low_alarm_co2 = (Float.valueOf(pageData1.get(i).get("low_alarm_co2").toString()).floatValue() -
//								Float.valueOf(pageData1.get(i-1).get("low_alarm_co2").toString()).floatValue())/
//						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报二氧化碳
//						low_alarm_co22 = Float.valueOf(pageData1.get(i-1).get("low_alarm_co2").toString()).floatValue();
						//日龄
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					}
				}
			}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 5) {
				//计算湿度差、基值
				for (int i = 0; i < pageData1.size(); i++) {
					if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i == 0) {
						//目标湿度1
						set_humidity2 = Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue();
						//高报湿度1
						high_alarm_humidity2 = Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示1
						high_humidity_warning2 = Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue();
						//低报湿度1
						low_alarm_humidity2 = Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示1
						low_humidity_warning2 = Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue();
						//目标湿度2
						set_humidity3 = Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue();
						//高报湿度2
						high_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示2
						high_humidity_warning3 = Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue();
						//低报湿度2
						low_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示2
						low_humidity_warning3 = Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue();
						if (pageData1.size() != 1) {
							if (!pageData1.get(i + 1).get("set_humidity").equals("")) {
								//目标湿度差2
								set_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("set_humidity").toString()).floatValue() -
										Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue()) /
										((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
								
								//高报湿度差2
								high_alarm_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_humidity").toString()).floatValue() -
										Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue()) /
										((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
								//高报湿度警示差2
								high_humidity_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_humidity_warning").toString()).floatValue() -
										Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue()) /
										((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
								
								//低报湿度差2
								low_alarm_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("low_alarm_humidity").toString()).floatValue() -
										Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue()) /
										((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
								//低报湿度警示差2
								low_humidity_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_humidity_warning").toString()).floatValue() -
										Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue()) /
										((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
								
								//uid_num
								uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
								day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
								choose3=true;
							}
						}else{
							choose =true;
						}
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i > 0 && i < pageData1.size() - 1) {
						//目标湿度差1
						set_humidity = (Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标湿度1
						set_humidity2 = Float.valueOf(pageData1.get(i - 1).get("set_humidity").toString()).floatValue();
						//高报温度差1
						high_alarm_humidity = (Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报湿度警示差1
						high_humidity_warning = (Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报湿度1
						high_alarm_humidity2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示1
						high_humidity_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_humidity_warning").toString()).floatValue();
						//低报湿度差1
						low_alarm_humidity = (Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报湿度警示差1
						low_humidity_warning = (Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//低报湿度1
						low_alarm_humidity2 = Float.valueOf(pageData1.get(i - 1).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示1
						low_humidity_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_humidity_warning").toString()).floatValue();

						//目标湿度差2
						set_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("set_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//目标湿度2
						set_humidity3 = Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue();
						//高报湿度差2
						high_alarm_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//高报湿度警示差2
						high_humidity_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//高报湿度2
						high_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示2
						high_humidity_warning3 = Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue();
						//低报湿度差2
						low_alarm_humidity1 = (Float.valueOf(pageData1.get(i + 1).get("low_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//低报湿度警示差2
						low_humidity_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//低报湿度2
						low_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示2
						low_humidity_warning3 = Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						//日龄
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						choose2 = true;
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i == pageData1.size() - 1) {
						//目标湿度差
						set_humidity = (Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标湿度
						set_humidity2 = Float.valueOf(pageData1.get(i - 1).get("set_humidity").toString()).floatValue();
						//高报湿度差
						high_alarm_humidity = (Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报湿度警示差
						high_humidity_warning = (Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报湿度
						high_alarm_humidity2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示
						high_humidity_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_humidity_warning").toString()).floatValue();
						//低报湿度差
						low_alarm_humidity = (Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_alarm_humidity").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报湿度警示差
						low_humidity_warning = (Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_humidity_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//低报湿度
						low_alarm_humidity2 = Float.valueOf(pageData1.get(i - 1).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示
						low_humidity_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_humidity_warning").toString()).floatValue();
						//目标湿度2
						set_humidity3 = Float.valueOf(pageData1.get(i).get("set_humidity").toString()).floatValue();
						//高报湿度2
						high_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("high_alarm_humidity").toString()).floatValue();
						//高报湿度警示2
						high_humidity_warning3 = Float.valueOf(pageData1.get(i).get("high_humidity_warning").toString()).floatValue();
						//低报湿度2
						low_alarm_humidity3 = Float.valueOf(pageData1.get(i).get("low_alarm_humidity").toString()).floatValue();
						//低报湿度警示2
						low_humidity_warning3 = Float.valueOf(pageData1.get(i).get("low_humidity_warning").toString()).floatValue();
						uid_num = Integer.valueOf(pageData1.get(i - 1).get("id").toString()).intValue();
						//日龄
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						choose = true;
						break;
					}
				}
			}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 6) {
				//计算负压差、基值
				for (int i = 0; i < pageData1.size(); i++) {
					if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i == 0) {
						//目标负压1
						set_negative_pressure2 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
						//高报负压1
						high_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
						//高报负压警示1
						high_negative_pressure_warning2 = Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue();
						//低报负压1
					    low_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
					    //低报负压警示1
						low_negative_pressure_warning2 = Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue();
						if (pageData1.size() != 1) {
							//目标负压差2
							set_negative_pressure1 = (Float.valueOf(pageData1.get(i + 1).get("set_negative_pressure").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//目标负压2
							set_negative_pressure3 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
							//高报负压差2
							high_alarm_negative_pressure1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_negative_pressure").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//高报负压警示差2
							high_negative_pressure_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_negative_pressure_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							
							//高报负压2
							high_alarm_negative_pressure3 = Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
							//高报负压警示2
							high_negative_pressure_warning3 = Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue();
							//低报负压差2
						    low_alarm_negative_pressure1 = (Float.valueOf(pageData1.get(i+1).get("low_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue())/
						    ((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
							//低报负压2
						    low_alarm_negative_pressure3 = Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
						    //低报负压警示2
							low_negative_pressure_warning3 = Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue();
							//低报负压警示差2
							low_negative_pressure_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_negative_pressure_warning").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						}
                        uid_num = Integer.valueOf(pageData1.get(i).get("id").toString()).intValue(); //temp solution
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i > 0 && i < pageData1.size() - 1) {
						//目标付负压差1
						set_negative_pressure = (Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标负压1
						set_negative_pressure2 = Float.valueOf(pageData1.get(i - 1).get("set_negative_pressure").toString()).floatValue();
						//高报负压差1
						high_alarm_negative_pressure = (Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报负压警示差1
						high_negative_pressure_warning = (Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报负压1
						high_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_negative_pressure").toString()).floatValue();
						//高报负压警示1
						high_negative_pressure_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_negative_pressure_warning").toString()).floatValue();
						//低报负压差1
						low_alarm_negative_pressure = (Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue())/
						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报负压1
						low_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue();
						//低报负压警示1
						low_negative_pressure_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_negative_pressure_warning").toString()).floatValue();
						//低报负压警示差1
						low_negative_pressure_warning = (Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标负压差2
						set_negative_pressure1 = (Float.valueOf(pageData1.get(i + 1).get("set_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//目标负压2
						set_negative_pressure3 = Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue();
						//高报负压差2
						high_alarm_negative_pressure1 = (Float.valueOf(pageData1.get(i + 1).get("high_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//高报负压警示差2
						high_negative_pressure_warning1 = (Float.valueOf(pageData1.get(i + 1).get("high_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						
						//高报负压2
						high_alarm_negative_pressure3 = Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
						//高报负压警示2
						high_negative_pressure_warning3 = Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue();
						//低报负压差2
						low_alarm_negative_pressure1 = (Float.valueOf(pageData1.get(i+1).get("low_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue())/
						((Float.valueOf(pageData1.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
						//低报负压2
						low_alarm_negative_pressure3 = Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
						//低报负压警示2
						low_negative_pressure_warning3 = Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue();
						//低报负压警示差2
						low_negative_pressure_warning1 = (Float.valueOf(pageData1.get(i + 1).get("low_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						//日龄
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i == pageData1.size() - 1) {
						//目标负压差
						set_negative_pressure = (Float.valueOf(pageData1.get(i).get("set_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标负压
						set_negative_pressure2 = Float.valueOf(pageData1.get(i - 1).get("set_negative_pressure").toString()).floatValue();
						//高报负压差
						high_alarm_negative_pressure = (Float.valueOf(pageData1.get(i).get("high_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_alarm_negative_pressure").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报负压警示差
						high_negative_pressure_warning = (Float.valueOf(pageData1.get(i).get("high_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						
						//高报负压
						high_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i - 1).get("high_alarm_negative_pressure").toString()).floatValue();
						//高报负压警示
						high_negative_pressure_warning2 = Float.valueOf(pageData1.get(i - 1).get("high_negative_pressure_warning").toString()).floatValue();
						//低报负压差
						low_alarm_negative_pressure = (Float.valueOf(pageData1.get(i).get("low_alarm_negative_pressure").toString()).floatValue() -
								Float.valueOf(pageData1.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue())/
						((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData1.get(i-1).get("day_age").toString()).floatValue())*24);
						//低报负压
						low_alarm_negative_pressure2 = Float.valueOf(pageData1.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue();
						//低报负压警示
						low_negative_pressure_warning2 = Float.valueOf(pageData1.get(i - 1).get("low_negative_pressure_warning").toString()).floatValue();
						//低报负压警示差
						low_negative_pressure_warning = (Float.valueOf(pageData1.get(i).get("low_negative_pressure_warning").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_negative_pressure_warning").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//日龄
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					}
				}
			} else {
				for (int i = 0; i < pageData1.size(); i++) {
					//计算耗水差、基值
					if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i == 0) {
						//目标耗水1
						set_water_deprivation2 = Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue();
						//高报耗水1
						high_water_deprivation2 = Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue();
						//低报耗水1
						low_water_deprivation2 = Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue();
						if (pageData1.size() != 1) {
							//目标耗水差2
							set_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("set_water_deprivation").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//目标耗水2
							set_water_deprivation3 = Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue();
							//高报耗水差2
							high_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("high_water_deprivation").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//高报耗水2
							high_water_deprivation3 = Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue();
							//低报耗水差2
							low_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("low_water_deprivation").toString()).floatValue() -
									Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue()) /
									((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
							//低报耗水2
							low_water_deprivation3 = Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue();
							//uid_num
							uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
							day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						}
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i > 0 && i < pageData1.size() - 1) {
						//目标耗水差1
						set_water_deprivation = (Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标耗水1
						set_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("set_water_deprivation").toString()).floatValue();
						//高报耗水差1
						high_water_deprivation = (Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报耗水1
						high_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("high_water_deprivation").toString()).floatValue();
						//低报耗水差1
						low_water_deprivation = (Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报耗水1
						low_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("low_water_deprivation").toString()).floatValue();
						//目标耗水差2
						set_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("set_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//目标耗水2
						set_water_deprivation3 = Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue();
						//高报耗水差2
						high_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("high_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//高报耗水2
						high_water_deprivation3 = Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue();
						//低报耗水差2
						low_water_deprivation1 = (Float.valueOf(pageData1.get(i + 1).get("low_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pageData1.get(i + 1).get("day_age").toString()).floatValue() - Float.valueOf(pd.get("day_age").toString()).floatValue()) * 24);
						//低报耗水2
						low_water_deprivation3 = Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue();
						//uid_num
						uid_num = Integer.valueOf(pageData1.get(i + 1).get("id").toString()).intValue();
						day_age2 = Integer.valueOf(pageData1.get(i + 1).get("day_age").toString()).intValue();
						//日龄
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					} else if (pageData1.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
							pageData1.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
							pageData1.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i == pageData1.size() - 1) {
						//目标耗水差
						set_water_deprivation = (Float.valueOf(pageData1.get(i).get("set_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("set_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//目标耗水
						set_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("set_water_deprivation").toString()).floatValue();
						//高报耗水差
						high_water_deprivation = (Float.valueOf(pageData1.get(i).get("high_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("high_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//高报耗水
						high_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("high_water_deprivation").toString()).floatValue();
						//低报耗水差
						low_water_deprivation = (Float.valueOf(pageData1.get(i).get("low_water_deprivation").toString()).floatValue() -
								Float.valueOf(pageData1.get(i - 1).get("low_water_deprivation").toString()).floatValue()) /
								((Float.valueOf(pd.get("day_age").toString()).floatValue() - Float.valueOf(pageData1.get(i - 1).get("day_age").toString()).floatValue()) * 24);
						//低报耗水
						low_water_deprivation2 = Float.valueOf(pageData1.get(i - 1).get("low_water_deprivation").toString()).floatValue();
						//日龄
						day_age = Integer.valueOf(pageData1.get(i - 1).get("day_age").toString()).intValue();
						break;
					}
				}
			}

			PageData pd2 = new PageData();
			pd2.put("uid_num", uid_num);
			pd2.put("alarm_type", pd.get("alarm_type"));
			if(choose){
				pd2.put("day_age1", day_age);
			}
			if(choose2){
				if(Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2){
					pd2.put("day_age1", day_age);
				}else{
					pd2.put("day_age2", day_age2);
				}
			}
			if(choose3){
				pd2.put("day_age2", day_age2);
				choose2=true;
			}
			//在子表中插入或修改数据
			alarmService.deleteSBDayageTempSub(pd2);

			List<PageData> list = new ArrayList<PageData>();
			if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 1) {
				//温度插入
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd3 = new PageData();
						pd3.put("uid_num", pdID);
						pd3.put("alarm_type", pd.get("alarm_type"));
						pd3.put("create_person", userId);
						pd3.put("create_date", new Date());
						pd3.put("create_time", new Date());
						pd3.put("modify_person", userId);
						pd3.put("modify_date", new Date());
						pd3.put("modify_time", new Date());
						pd3.put("farmId", pd.get("farmId"));
						pd3.put("houseId", pd.get("houseId"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd3.put("day_age", i + 1);
						date.setHours(j);
						pd3.put("record_datetime", date);
						pd3.put("set_temp", set_temp2 + set_temp * ((i - day_age) * 24 + j));
						pd3.put("high_alarm_temp", high_alarm_temp2 + high_alarm_temp * ((i - day_age) * 24 + j));
						pd3.put("low_alarm_temp", low_alarm_temp2 + low_alarm_temp * ((i - day_age) * 24 + j));
						pd3.put("high_temp_warning", high_temp_warning2 + high_temp_warning * ((i - day_age) * 24 + j));
						pd3.put("low_temp_warning", low_temp_warning2 + low_temp_warning * ((i - day_age) * 24 + j));
						pd3.put("set_humidity", null);
						pd3.put("high_alarm_humidity", null);
						pd3.put("low_alarm_humidity", null);
						pd3.put("high_humidity_warning", null);
						pd3.put("low_humidity_warning", null);
						pd3.put("set_negative_pressure", null);
						pd3.put("high_alarm_negative_pressure", null);
						pd3.put("low_alarm_negative_pressure", null);
						pd3.put("high_negative_pressure_warning", null);
						pd3.put("low_negative_pressure_warning", null);
						pd3.put("set_lux", null);
						pd3.put("high_lux", null);
						pd3.put("low_lux", null);
						pd3.put("high_lux_warning", null);
						pd3.put("low_lux_warning", null);
						pd3.put("set_co2", null);
						pd3.put("high_alarm_co2", null);
						pd3.put("high_co2_warning", null);
						pd3.put("set_water_deprivation", null);
						pd3.put("high_water_deprivation", null);
						pd3.put("low_water_deprivation", null);
						pd3.put("is_start", "0");
						list.add(pd3);
//						alarmService.saveSBDayageTempSub(pd2);
					}
				}
				if(choose){
					for (int i = Integer.valueOf(pd.get("day_age").toString()).intValue(); i < Integer.valueOf(pd.get("dage").toString()).intValue(); i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd3 = new PageData();
							pd3.put("uid_num", pdID);
							pd3.put("alarm_type", pd.get("alarm_type"));
							pd3.put("create_person", userId);
							pd3.put("create_date", new Date());
							pd3.put("create_time", new Date());
							pd3.put("modify_person", userId);
							pd3.put("modify_date", new Date());
							pd3.put("modify_time", new Date());
							pd3.put("farmId", pd.get("farmId"));
							pd3.put("houseId", pd.get("houseId"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd3.put("day_age", i + 1);
							date.setHours(j);
							pd3.put("record_datetime", date);
							pd3.put("set_temp", set_temp3);
							pd3.put("high_alarm_temp", high_alarm_temp3);
							pd3.put("low_alarm_temp", low_alarm_temp3);
							pd3.put("high_temp_warning", high_temp_warning3);
							pd3.put("low_temp_warning", low_temp_warning3);
							pd3.put("set_humidity", null);
							pd3.put("high_alarm_humidity", null);
							pd3.put("low_alarm_humidity", null);
							pd3.put("high_humidity_warning", null);
							pd3.put("low_humidity_warning", null);
							pd3.put("set_negative_pressure", null);
							pd3.put("high_alarm_negative_pressure", null);
							pd3.put("low_alarm_negative_pressure", null);
							pd3.put("high_negative_pressure_warning", null);
							pd3.put("low_negative_pressure_warning", null);
							pd3.put("set_lux", null);
							pd3.put("high_lux", null);
							pd3.put("low_lux", null);
							pd3.put("high_lux_warning", null);
							pd3.put("low_lux_warning", null);
							pd3.put("set_co2", null);
							pd3.put("high_alarm_co2", null);
							pd3.put("high_co2_warning", null);
							pd3.put("set_water_deprivation", null);
							pd3.put("high_water_deprivation", null);
							pd3.put("low_water_deprivation", null);
							pd3.put("is_start", "0");
							list.add(pd3);
						}
					}
				}
				alarmService.saveSBDayageTempSub(list);
			} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2) {
				//负压插入
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd3 = new PageData();
						pd3.put("uid_num", pdID);
						pd3.put("alarm_type", pd.get("alarm_type"));
						pd3.put("create_person", userId);
						pd3.put("create_date", new Date());
						pd3.put("create_time", new Date());
						pd3.put("modify_person", userId);
						pd3.put("modify_date", new Date());
						pd3.put("modify_time", new Date());
						pd3.put("farmId", pd.get("farmId"));
						pd3.put("houseId", pd.get("houseId"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd3.put("day_age", i + 1);
						date.setHours(j);
						pd3.put("record_datetime", date);
						pd3.put("set_temp", null);
						pd3.put("high_alarm_temp", null);
						pd3.put("low_alarm_temp", null);
						pd3.put("high_temp_warning", null);
						pd3.put("low_temp_warning", null);
						pd3.put("set_humidity", null);
						pd3.put("high_alarm_humidity", null);
						pd3.put("low_alarm_humidity", null);
						pd3.put("high_humidity_warning", null);
						pd3.put("low_humidity_warning", null);
						pd3.put("set_negative_pressure", null);
						pd3.put("high_alarm_negative_pressure", null);
						pd3.put("low_alarm_negative_pressure", null);
						pd3.put("high_negative_pressure_warning", null);
						pd3.put("low_negative_pressure_warning", null);
//				    	pd2.put("set_negative_pressure", set_negative_pressure2+set_negative_pressure*((i+1-day_age)*j));
//				    	pd3.put("set_lux", set_lux2+set_lux*((i-day_age)*24+j));
//				    	pd3.put("high_lux", high_lux2+high_lux*((i-day_age)*24+j));
//				    	pd3.put("low_lux", low_lux2+low_lux*((i-day_age)*24+j));
						pd3.put("set_lux", set_lux3);
						pd3.put("high_lux", high_lux3);
						pd3.put("low_lux", low_lux3);
						pd3.put("high_lux_warning", high_lux_warning3);
						pd3.put("low_lux_warning", low_lux_warning3);
//						pd3.put("set_lux", set_lux2 + set_lux * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//						pd3.put("high_lux", high_lux2 + high_lux * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//						pd3.put("low_lux", low_lux2 + low_lux * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//						pd3.put("high_lux_warning", high_lux_warning2 + high_lux_warning * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//						pd3.put("low_lux_warning", low_lux_warning2 + low_lux_warning * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
						pd3.put("set_co2", null);
						pd3.put("high_alarm_co2", null);
						pd3.put("high_co2_warning", null);
						pd3.put("set_water_deprivation", null);
						pd3.put("high_water_deprivation", null);
						pd3.put("low_water_deprivation", null);
						String[] st = pd.get("start_time").toString().split(":");
						startTime = Integer.valueOf(st[0]).intValue();
						String[] et = pd.get("end_time").toString().split(":");
						endTime = Integer.valueOf(et[0]).intValue();
						if (startTime <= j && j < endTime) {
							pd3.put("is_start", "0");
						} else {
							pd3.put("is_start", "1");
						}
						list.add(pd3);
//						alarmService.saveSBDayageTempSub(pd2);
					}
				}
				if(choose){
					for (int i = Integer.valueOf(pd.get("day_age").toString()).intValue(); i < Integer.valueOf(pd.get("dage").toString()).intValue(); i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd3 = new PageData();
							pd3.put("uid_num", pdID);
							pd3.put("alarm_type", pd.get("alarm_type"));
							pd3.put("create_person", userId);
							pd3.put("create_date", new Date());
							pd3.put("create_time", new Date());
							pd3.put("modify_person", userId);
							pd3.put("modify_date", new Date());
							pd3.put("modify_time", new Date());
							pd3.put("farmId", pd.get("farmId"));
							pd3.put("houseId", pd.get("houseId"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd3.put("day_age", i + 1);
							date.setHours(j);
							pd3.put("record_datetime", date);
							pd3.put("set_temp", null);
							pd3.put("high_alarm_temp", null);
							pd3.put("low_alarm_temp", null);
							pd3.put("high_temp_warning", null);
							pd3.put("low_temp_warning", null);
							pd3.put("set_humidity", null);
							pd3.put("high_alarm_humidity", null);
							pd3.put("low_alarm_humidity", null);
							pd3.put("high_humidity_warning", null);
							pd3.put("low_humidity_warning", null);
							pd3.put("set_negative_pressure", null);
							pd3.put("high_alarm_negative_pressure", null);
							pd3.put("low_alarm_negative_pressure", null);
							pd3.put("high_negative_pressure_warning", null);
							pd3.put("low_negative_pressure_warning", null);
//					    	pd2.put("set_negative_pressure", set_negative_pressure2+set_negative_pressure*((i+1-day_age)*j));
//					    	pd3.put("set_lux", set_lux2+set_lux*((i-day_age)*24+j));
//					    	pd3.put("high_lux", high_lux2+high_lux*((i-day_age)*24+j));
//					    	pd3.put("low_lux", low_lux2+low_lux*((i-day_age)*24+j));
							pd3.put("set_lux", set_lux3);
							pd3.put("high_lux", high_lux3);
							pd3.put("low_lux", low_lux3);
							pd3.put("high_lux_warning", high_lux_warning3);
							pd3.put("low_lux_warning", low_lux_warning3);
							pd3.put("set_co2", null);
							pd3.put("high_alarm_co2", null);
							pd3.put("high_co2_warning", null);
							pd3.put("set_water_deprivation", null);
							pd3.put("high_water_deprivation", null);
							pd3.put("low_water_deprivation", null);
							String[] st = pd.get("start_time").toString().split(":");
							startTime = Integer.valueOf(st[0]).intValue();
							String[] et = pd.get("end_time").toString().split(":");
							endTime = Integer.valueOf(et[0]).intValue();
							if (startTime <= j && j < endTime) {
								pd3.put("is_start", "0");
							} else {
								pd3.put("is_start", "1");
							}
							list.add(pd3);
						}
					}
				}
				alarmService.saveSBDayageTempSub(list);
			} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 3) {
				//二氧化碳插入
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd3 = new PageData();
						pd3.put("uid_num", pdID);
						pd3.put("alarm_type", pd.get("alarm_type"));
						pd3.put("create_person", userId);
						pd3.put("create_date", new Date());
						pd3.put("create_time", new Date());
						pd3.put("modify_person", userId);
						pd3.put("modify_date", new Date());
						pd3.put("modify_time", new Date());
						pd3.put("farmId", pd.get("farmId"));
						pd3.put("houseId", pd.get("houseId"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd3.put("day_age", i + 1);
						date.setHours(j);
						pd3.put("record_datetime", date);
						pd3.put("set_temp", null);
						pd3.put("high_alarm_temp", null);
						pd3.put("low_alarm_temp", null);
						pd3.put("high_temp_warning", null);
						pd3.put("low_temp_warning", null);
						pd3.put("set_humidity", null);
						pd3.put("high_alarm_humidity", null);
						pd3.put("low_alarm_humidity", null);
						pd3.put("high_humidity_warning", null);
						pd3.put("low_humidity_warning", null);
						pd3.put("set_negative_pressure", null);
						pd3.put("high_alarm_negative_pressure", null);
						pd3.put("low_alarm_negative_pressure", null);
						pd3.put("high_negative_pressure_warning", null);
						pd3.put("low_negative_pressure_warning", null);
						pd3.put("set_lux", null);
						pd3.put("high_lux", null);
						pd3.put("low_lux", null);
						pd3.put("high_lux_warning", null);
						pd3.put("low_lux_warning", null);
						pd3.put("set_co2", set_co22 + set_co2 * ((i - day_age) * 24 + j));
						pd3.put("high_alarm_co2", high_alarm_co22 + high_alarm_co2 * ((i - day_age) * 24 + j));
						pd3.put("high_co2_warning", high_co2_warning2 + high_co2_warning * ((i - day_age) * 24 + j));
//				    	pd2.put("low_alarm_co2", low_alarm_co22+low_alarm_co2*((i+1-day_age)*j));
						pd3.put("set_water_deprivation", null);
						pd3.put("high_water_deprivation", null);
						pd3.put("low_water_deprivation", null);
						pd3.put("is_start", "0");
						list.add(pd3);
//						alarmService.saveSBDayageTempSub(pd2);
					}
				}
				alarmService.saveSBDayageTempSub(list);
			}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 5) {
				//湿度插入
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd3 = new PageData();
						pd3.put("uid_num", pdID);
						pd3.put("alarm_type", pd.get("alarm_type"));
						pd3.put("create_person", userId);
						pd3.put("create_date", new Date());
						pd3.put("create_time", new Date());
						pd3.put("modify_person", userId);
						pd3.put("modify_date", new Date());
						pd3.put("modify_time", new Date());
						pd3.put("farmId", pd.get("farmId"));
						pd3.put("houseId", pd.get("houseId"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd3.put("day_age", i + 1);
						date.setHours(j);
						pd3.put("record_datetime", date);
						pd3.put("set_temp", null);
						pd3.put("high_alarm_temp", null);
						pd3.put("low_alarm_temp", null);
						pd3.put("high_temp_warning", null);
						pd3.put("low_temp_warning", null);
						pd3.put("set_humidity", set_humidity2 + set_humidity * ((i - day_age) * 24 + j));
						pd3.put("high_alarm_humidity", high_alarm_humidity2 + high_alarm_humidity * ((i - day_age) * 24 + j));
						pd3.put("low_alarm_humidity", low_alarm_humidity2 + low_alarm_humidity * ((i - day_age) * 24 + j));
						pd3.put("high_humidity_warning", high_humidity_warning2 + high_humidity_warning * ((i - day_age) * 24 + j));
						pd3.put("low_humidity_warning", low_humidity_warning2 + low_humidity_warning * ((i - day_age) * 24 + j));
						pd3.put("set_negative_pressure", null);
						pd3.put("high_alarm_negative_pressure", null);
						pd3.put("low_alarm_negative_pressure", null);
						pd3.put("high_negative_pressure_warning", null);
						pd3.put("low_negative_pressure_warning", null);
						pd3.put("set_lux", null);
						pd3.put("high_lux", null);
						pd3.put("low_lux", null);
						pd3.put("high_lux_warning", null);
						pd3.put("low_lux_warning", null);
						pd3.put("set_co2", null);
						pd3.put("high_alarm_co2", null);
						pd3.put("high_co2_warning", null);
						pd3.put("set_water_deprivation", null);
						pd3.put("high_water_deprivation", null);
						pd3.put("low_water_deprivation", null);
						pd3.put("is_start", "0");
						list.add(pd3);
//						alarmService.saveSBDayageTempSub(pd2);
					}
				}
				if(choose){
					for (int i = Integer.valueOf(pd.get("day_age").toString()).intValue(); i < Integer.valueOf(pd.get("dage").toString()).intValue(); i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd3 = new PageData();
							pd3.put("uid_num", pdID);
							pd3.put("alarm_type", pd.get("alarm_type"));
							pd3.put("create_person", userId);
							pd3.put("create_date", new Date());
							pd3.put("create_time", new Date());
							pd3.put("modify_person", userId);
							pd3.put("modify_date", new Date());
							pd3.put("modify_time", new Date());
							pd3.put("farmId", pd.get("farmId"));
							pd3.put("houseId", pd.get("houseId"));
							Date date = new Date();
							date.setMinutes(0);
							date.setSeconds(0);
							pd3.put("day_age", i + 1);
							date.setHours(j);
							pd3.put("record_datetime", date);
							pd3.put("set_temp", null);
							pd3.put("high_alarm_temp", null);
							pd3.put("low_alarm_temp", null);
							pd3.put("high_temp_warning", null);
							pd3.put("low_temp_warning", null);
							pd3.put("set_humidity", set_humidity3);
							pd3.put("high_alarm_humidity", high_alarm_humidity3);
							pd3.put("low_alarm_humidity", low_alarm_humidity3);
							pd3.put("high_humidity_warning", high_humidity_warning3);
							pd3.put("low_humidity_warning", low_humidity_warning3);
							pd3.put("set_negative_pressure", null);
							pd3.put("high_alarm_negative_pressure", null);
							pd3.put("low_alarm_negative_pressure", null);
							pd3.put("high_negative_pressure_warning", null);
							pd3.put("low_negative_pressure_warning", null);
							pd3.put("set_lux", null);
							pd3.put("high_lux", null);
							pd3.put("low_lux", null);
							pd3.put("high_lux_warning", null);
							pd3.put("low_lux_warning", null);
							pd3.put("set_co2", null);
							pd3.put("high_alarm_co2", null);
							pd3.put("high_co2_warning", null);
							pd3.put("set_water_deprivation", null);
							pd3.put("high_water_deprivation", null);
							pd3.put("low_water_deprivation", null);
							pd3.put("is_start", "0");
							list.add(pd3);
						}
					}
				}
				alarmService.saveSBDayageTempSub(list);
			}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 6) {
				//负压插入
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd3 = new PageData();
						pd3.put("uid_num", pdID);
						pd3.put("alarm_type", pd.get("alarm_type"));
						pd3.put("create_person", userId);
						pd3.put("create_date", new Date());
						pd3.put("create_time", new Date());
						pd3.put("modify_person", userId);
						pd3.put("modify_date", new Date());
						pd3.put("modify_time", new Date());
						pd3.put("farmId", pd.get("farmId"));
						pd3.put("houseId", pd.get("houseId"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd3.put("day_age", i + 1);
						date.setHours(j);
						pd3.put("record_datetime", date);
						pd3.put("set_temp", null);
						pd3.put("high_alarm_temp", null);
						pd3.put("low_alarm_temp", null);
						pd3.put("high_temp_warning", null);
						pd3.put("low_temp_warning", null);
						pd3.put("set_humidity", null);
						pd3.put("high_alarm_humidity", null);
						pd3.put("low_alarm_humidity", null);
						pd3.put("high_humidity_warning", null);
						pd3.put("low_humidity_warning", null);
						pd3.put("set_negative_pressure", set_negative_pressure2 + set_negative_pressure * ((i - day_age) * 24 + j));
						pd3.put("high_alarm_negative_pressure", high_alarm_negative_pressure2 + high_alarm_negative_pressure * ((i - day_age) * 24 + j));
						pd3.put("low_alarm_negative_pressure", low_alarm_negative_pressure2 + low_alarm_negative_pressure * ((i - day_age) * 24 + j));
						pd3.put("high_negative_pressure_warning", high_negative_pressure_warning2 + high_negative_pressure_warning * ((i - day_age) * 24 + j));
						pd3.put("low_negative_pressure_warning", low_negative_pressure_warning2 + low_negative_pressure_warning * ((i - day_age) * 24 + j));
						pd3.put("set_lux", null);
						pd3.put("high_lux", null);
						pd3.put("low_lux", null);
						pd3.put("high_lux_warning", null);
						pd3.put("low_lux_warning", null);
						pd3.put("set_co2", null);
						pd3.put("high_alarm_co2", null);
						pd3.put("high_co2_warning", null);
//				    	pd2.put("low_alarm_co2", low_alarm_co22+low_alarm_co2*((i+1-day_age)*j));
						pd3.put("set_water_deprivation", null);
						pd3.put("high_water_deprivation", null);
						pd3.put("low_water_deprivation", null);
						pd3.put("is_start", "0");
						list.add(pd3);
//						alarmService.saveSBDayageTempSub(pd2);
					}
				}
				alarmService.saveSBDayageTempSub(list);
			} else {
				//耗水插入
				for (int i = day_age; i < Integer.valueOf(pd.get("day_age").toString()).intValue(); i++) {
					for (int j = 1; j <= 24; j++) {
						PageData pd3 = new PageData();
						pd3.put("uid_num", pdID);
						pd3.put("alarm_type", pd.get("alarm_type"));
						pd3.put("create_person", userId);
						pd3.put("create_date", new Date());
						pd3.put("create_time", new Date());
						pd3.put("modify_person", userId);
						pd3.put("modify_date", new Date());
						pd3.put("modify_time", new Date());
						pd3.put("farmId", pd.get("farmId"));
						pd3.put("houseId", pd.get("houseId"));
						Date date = new Date();
						date.setMinutes(0);
						date.setSeconds(0);
						pd3.put("day_age", i + 1);
						date.setHours(j);
						pd3.put("record_datetime", date);
						pd3.put("set_temp", null);
						pd3.put("high_alarm_temp", null);
						pd3.put("low_alarm_temp", null);
						pd3.put("set_lux", null);
						pd3.put("high_lux", null);
						pd3.put("low_lux", null);
						pd3.put("set_co2", null);
						pd3.put("high_alarm_co2", null);
						pd3.put("set_water_deprivation", set_water_deprivation2 + set_water_deprivation * ((i - day_age) * 24 + j));
						pd3.put("high_water_deprivation", high_water_deprivation2 + high_water_deprivation * ((i - day_age) * 24 + j));
						pd3.put("low_water_deprivation", low_water_deprivation2 + low_water_deprivation * ((i - day_age) * 24 + j));
						list.add(pd3);
//						alarmService.saveSBDayageTempSub(pd2);
					}
				}
				alarmService.saveSBDayageTempSub(list);
			}

			if (choose2) {
				PageData pd4 = new PageData();
				pd4.put("uid_num", uid_num);
				pd4.put("alarm_type", pd.get("alarm_type"));
				
				if(choose3){
					pd4.put("day_age2", day_age2);
				}else{
					if(Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2){
						pd4.put("day_age1", day_age);
					}else{
					pd4.put("day_age2", day_age2);
					}
				}
				alarmService.deleteSBDayageTempSub(pd4);

				List<PageData> list2 = new ArrayList<PageData>();
				int day_age3 = Integer.valueOf(pd.get("day_age").toString()).intValue();
				if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 1) {
					//修改相邻记录的温度
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", uid_num);
							pd5.put("alarm_type", pd.get("alarm_type"));
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							Date date2 = new Date();
							date2.setMinutes(0);
							date2.setSeconds(0);
							pd5.put("day_age", i + 1);
							date2.setHours(j);
							pd5.put("record_datetime", date2);
							pd5.put("set_temp", set_temp3 + set_temp1 * ((i - day_age3) * 24 + j));
							pd5.put("high_alarm_temp", high_alarm_temp3 + high_alarm_temp1 * ((i - day_age3) * 24 + j));
							pd5.put("low_alarm_temp", low_alarm_temp3 + low_alarm_temp1 * ((i - day_age3) * 24 + j));
							pd5.put("high_temp_warning", high_temp_warning3 + high_temp_warning1 * ((i - day_age3) * 24 + j));
							pd5.put("low_temp_warning", low_temp_warning3 + low_temp_warning1 * ((i - day_age3) * 24 + j));
							pd5.put("set_humidity", null);
							pd5.put("high_alarm_humidity", null);
							pd5.put("low_alarm_humidity", null);
							pd5.put("high_humidity_warning", null);
							pd5.put("low_humidity_warning", null);
							pd5.put("set_negative_pressure", null);
							pd5.put("high_alarm_negative_pressure", null);
							pd5.put("low_alarm_negative_pressure", null);
							pd5.put("high_negative_pressure_warning", null);
							pd5.put("low_negative_pressure_warning", null);
							pd5.put("set_lux", null);
							pd5.put("high_lux", null);
							pd5.put("low_lux", null);
							pd5.put("high_lux_warning", null);
							pd5.put("low_lux_warning", null);
							pd5.put("set_co2", null);
							pd5.put("high_alarm_co2", null);
							pd5.put("high_co2_warning", null);
							pd5.put("set_water_deprivation", null);
							pd5.put("high_water_deprivation", null);
							pd5.put("low_water_deprivation", null);
							pd5.put("is_start", "0");
							list2.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2) {
					if(!choose3){
						day_age2 = day_age2-7;
					}
					//修改相邻记录的负压
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", pdID);
							pd5.put("alarm_type", pd.get("alarm_type"));
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							Date date2 = new Date();
							date2.setMinutes(0);
							date2.setSeconds(0);
							pd5.put("day_age", i + 1);
							date2.setHours(j);
							pd5.put("record_datetime", date2);
							pd5.put("set_temp", null);
							pd5.put("high_alarm_temp", null);
							pd5.put("low_alarm_temp", null);
							pd5.put("high_temp_warning", null);
							pd5.put("low_temp_warning", null);
							pd5.put("set_humidity", null);
							pd5.put("high_alarm_humidity", null);
							pd5.put("low_alarm_humidity", null);
							pd5.put("high_humidity_warning", null);
							pd5.put("low_humidity_warning", null);
							pd5.put("set_negative_pressure", null);
							pd5.put("high_alarm_negative_pressure", null);
							pd5.put("low_alarm_negative_pressure", null);
							pd5.put("high_negative_pressure_warning", null);
							pd5.put("low_negative_pressure_warning", null);
//					pd5.put("set_lux",set_lux3+set_lux1*((i-day_age3)*24+j));
//					pd5.put("high_lux",high_lux3+high_lux1*((i-day_age3)*24+j));
//					pd5.put("low_lux",low_lux3+low_lux1*((i-day_age3)*24+j));
							pd5.put("set_lux", set_lux3);
							pd5.put("high_lux", high_lux3);
							pd5.put("low_lux", low_lux3);
							pd5.put("high_lux_warning", high_lux_warning3);
							pd5.put("low_lux_warning", low_lux_warning3);
//							pd5.put("set_lux", set_lux2 + set_lux * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//							pd5.put("high_lux", high_lux2 + high_lux * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//							pd5.put("low_lux", low_lux2 + low_lux * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//							pd5.put("high_lux_warning", high_lux_warning2 + high_lux_warning * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
//							pd5.put("low_lux_warning", low_lux_warning2 + low_lux_warning * ((Integer.valueOf(pd.get("day_age").toString()).intValue() - 1 - day_age) * 24 + 24));
							pd5.put("set_co2", null);
							pd5.put("high_alarm_co2", null);
							pd5.put("high_co2_warning", null);
							pd5.put("set_water_deprivation", null);
							pd5.put("high_water_deprivation", null);
							pd5.put("low_water_deprivation", null);
							String[] st = pd.get("start_time").toString().split(":");
							startTime = Integer.valueOf(st[0]).intValue();
							String[] et = pd.get("end_time").toString().split(":");
							endTime = Integer.valueOf(et[0]).intValue();
							if (startTime <= j && j < endTime) {
								pd5.put("is_start", "0");
							} else {
								pd5.put("is_start", "1");
							}
							list2.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 3) {
					//修改相邻记录的二氧化碳
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", uid_num);
							pd5.put("alarm_type", pd.get("alarm_type"));
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							Date date2 = new Date();
							date2.setMinutes(0);
							date2.setSeconds(0);
							pd5.put("day_age", i + 1);
							date2.setHours(j);
							pd5.put("record_datetime", date2);
							pd5.put("set_temp", null);
							pd5.put("high_alarm_temp", null);
							pd5.put("low_alarm_temp", null);
							pd5.put("high_temp_warning", null);
							pd5.put("low_temp_warning", null);
							pd5.put("set_humidity", null);
							pd5.put("high_alarm_humidity", null);
							pd5.put("low_alarm_humidity", null);
							pd5.put("high_humidity_warning", null);
							pd5.put("low_humidity_warning", null);
							pd5.put("set_negative_pressure", null);
							pd5.put("high_alarm_negative_pressure", null);
							pd5.put("low_alarm_negative_pressure", null);
							pd5.put("high_negative_pressure_warning", null);
							pd5.put("low_negative_pressure_warning", null);
							pd5.put("set_lux", null);
							pd5.put("high_lux", null);
							pd5.put("low_lux", null);
							pd5.put("high_lux_warning", null);
							pd5.put("low_lux_warning", null);
							pd5.put("set_co2", set_co23 + set_co21 * ((i - day_age3) * 24 + j));
							pd5.put("high_alarm_co2", high_alarm_co23 + high_alarm_co21 * ((i - day_age3) * 24 + j));
							pd5.put("high_co2_warning", high_co2_warning3 + high_co2_warning1 * ((i - day_age3) * 24 + j));
							pd5.put("set_water_deprivation", null);
							pd5.put("high_water_deprivation", null);
							pd5.put("low_water_deprivation", null);
							pd5.put("is_start", "0");
							list2.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 5) {
					//修改相邻记录的湿度
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", uid_num);
							pd5.put("alarm_type", pd.get("alarm_type"));
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							Date date2 = new Date();
							date2.setMinutes(0);
							date2.setSeconds(0);
							pd5.put("day_age", i + 1);
							date2.setHours(j);
							pd5.put("record_datetime", date2);
							pd5.put("set_temp", null);
							pd5.put("high_alarm_temp", null);
							pd5.put("low_alarm_temp", null);
							pd5.put("high_temp_warning", null);
							pd5.put("low_temp_warning", null);
							pd5.put("set_humidity", set_humidity3 + set_humidity1 * ((i - day_age3) * 24 + j));
							pd5.put("high_alarm_humidity", high_alarm_humidity3 + high_alarm_humidity1 * ((i - day_age3) * 24 + j));
							pd5.put("low_alarm_humidity", low_alarm_humidity3 + low_alarm_humidity1 * ((i - day_age3) * 24 + j));
							pd5.put("high_humidity_warning", high_humidity_warning3 + high_humidity_warning1 * ((i - day_age3) * 24 + j));
							pd5.put("low_humidity_warning", low_humidity_warning3 + low_humidity_warning1 * ((i - day_age3) * 24 + j));
							pd5.put("set_negative_pressure", null);
							pd5.put("high_alarm_negative_pressure", null);
							pd5.put("low_alarm_negative_pressure", null);
							pd5.put("high_negative_pressure_warning", null);
							pd5.put("low_negative_pressure_warning", null);
							pd5.put("set_lux", null);
							pd5.put("high_lux", null);
							pd5.put("low_lux", null);
							pd5.put("high_lux_warning", null);
							pd5.put("low_lux_warning", null);
							pd5.put("set_co2", null);
							pd5.put("high_alarm_co2", null);
							pd5.put("high_co2_warning", null);
							pd5.put("set_water_deprivation", null);
							pd5.put("high_water_deprivation", null);
							pd5.put("low_water_deprivation", null);
							pd5.put("is_start", "0");
							list2.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				}else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 6) {
					//修改相邻记录的负压
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", uid_num);
							pd5.put("alarm_type", pd.get("alarm_type"));
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							Date date2 = new Date();
							date2.setMinutes(0);
							date2.setSeconds(0);
							pd5.put("day_age", i + 1);
							date2.setHours(j);
							pd5.put("record_datetime", date2);
							pd5.put("set_temp", null);
							pd5.put("high_alarm_temp", null);
							pd5.put("low_alarm_temp", null);
							pd5.put("high_temp_warning", null);
							pd5.put("low_temp_warning", null);
							pd5.put("set_humidity", null);
							pd5.put("high_alarm_humidity", null);
							pd5.put("low_alarm_humidity", null);
							pd5.put("high_humidity_warning", null);
							pd5.put("low_humidity_warning", null);
							pd5.put("set_negative_pressure", set_negative_pressure3 + set_negative_pressure1 * ((i - day_age3) * 24 + j));
							pd5.put("high_alarm_negative_pressure", high_alarm_negative_pressure3 + high_alarm_negative_pressure1 * ((i - day_age3) * 24 + j));
							pd5.put("low_alarm_negative_pressure", low_alarm_negative_pressure3 + low_alarm_negative_pressure1 * ((i - day_age3) * 24 + j));
							pd5.put("high_negative_pressure_warning", high_negative_pressure_warning3 + high_negative_pressure_warning1 * ((i - day_age3) * 24 + j));
							pd5.put("low_negative_pressure_warning", low_negative_pressure_warning3 + low_negative_pressure_warning1 * ((i - day_age3) * 24 + j));
							pd5.put("set_lux", null);
							pd5.put("high_lux", null);
							pd5.put("low_lux", null);
							pd5.put("high_lux_warning", null);
							pd5.put("low_lux_warning", null);
							pd5.put("set_co2", null);
							pd5.put("high_alarm_co2", null);
							pd5.put("high_co2_warning", null);
							pd5.put("set_water_deprivation", null);
							pd5.put("high_water_deprivation", null);
							pd5.put("low_water_deprivation", null);
							pd5.put("is_start", "0");
							list2.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				} else if (Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 4) {
					//修改相邻记录的耗水
					for (int i = day_age3; i < day_age2; i++) {
						for (int j = 1; j <= 24; j++) {
							PageData pd5 = new PageData();
							pd5.put("uid_num", uid_num);
							pd5.put("alarm_type", pd.get("alarm_type"));
							pd5.put("create_person", userId);
							pd5.put("create_date", new Date());
							pd5.put("create_time", new Date());
							pd5.put("modify_person", userId);
							pd5.put("modify_date", new Date());
							pd5.put("modify_time", new Date());
							pd5.put("farmId", pd.get("farmId"));
							pd5.put("houseId", pd.get("houseId"));
							Date date2 = new Date();
							date2.setMinutes(0);
							date2.setSeconds(0);
							pd5.put("day_age", i + 1);
							date2.setHours(j);
							pd5.put("record_datetime", date2);
							pd5.put("record_datetime", date2);
							pd5.put("set_temp", null);
							pd5.put("high_alarm_temp", null);
							pd5.put("low_alarm_temp", null);
							pd5.put("set_lux", null);
							pd5.put("high_lux", null);
							pd5.put("low_lux", null);
							pd5.put("set_co2", null);
							pd5.put("high_alarm_co2", null);
							pd5.put("set_water_deprivation", set_water_deprivation3 + set_water_deprivation1 * ((i - day_age3) * 24 + j));
							pd5.put("high_water_deprivation", high_water_deprivation3 + high_water_deprivation1 * ((i - day_age3) * 24 + j));
							pd5.put("low_water_deprivation", low_water_deprivation3 + low_water_deprivation1 * ((i - day_age3) * 24 + j));
							list2.add(pd5);
//					alarmService.saveSBDayageTempSub(pd4);
						}
					}
					alarmService.saveSBDayageTempSub(list2);
				}
			}
			j2.setMsg("1");
			j2.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			j2.setMsg("2");
		}
		return j2;
	}
	
	@RequestMapping(value="/addAlarmUrl")
	public ModelAndView addAlarmUrl()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.setViewName("modules/alarm/addAlarm");
		mv.addObject("pd",pd);
//		mv.addObject("farmList",moduleService.service("farmServiceImpl", "selectAll", null));
//		mv.addObject("houseList",moduleService.service("farmServiceImpl", "selectHouseByCondition", new Object[]{pd}));
		return mv;
	}
	
	@RequestMapping(value="/bindingUser")
	public void bindingUser(HttpServletResponse response,HttpServletRequest request)throws Exception{
		Json j2=new Json();
		SDUser user = (SDUser)request.getSession().getAttribute(Const.SESSION_USER);
//		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{

		if(!pd.get("user_name1").equals("")
				&& !pd.get("userId1").equals("")){ //&& !pd.get("user_name1").equals(pd.get("userId1"))
			PageData pd2 = new PageData();
			pd2.put("user_order", 1);
			pd2.put("user_id", pd.get("user_name1"));
			pd2.put("farmId", pd.get("farmId"));
			pd2.put("houseId", pd.get("houseId"));
			pd2.put("modify_person",user.getId());
			pd2.put("modify_time", new Date());
			alarmService.updateSBReminder(pd2);
		}else if(!pd.get("user_name1").equals("")){
			PageData pd2 = new PageData();
			pd2.put("user_order", 1);
			pd2.put("remind_method", 0);
			pd2.put("farmId", pd.get("farmId"));
			pd2.put("houseId", pd.get("houseId"));
			pd2.put("user_type", 0);
			pd2.put("create_time", new Date());
			pd2.put("create_person", user.getId());
			pd2.put("user_id", pd.get("user_name1"));
			pd2.put("modify_person",user.getId());
			pd2.put("modify_time", new Date());
			alarmService.saveSBReminder(pd2);
		}
		
		if(!pd.get("user_name2").equals("")
				&& !pd.get("userId2").equals("")){ //&& !pd.get("user_name2").equals(pd.get("userId2"))
			PageData pd2 = new PageData();
			pd2.put("user_order", 2);
			pd2.put("user_id", pd.get("user_name2"));
			pd2.put("farmId", pd.get("farmId"));
			pd2.put("houseId", pd.get("houseId"));
			pd2.put("modify_person",user.getId());
			pd2.put("modify_time", new Date());
			alarmService.updateSBReminder(pd2);
		}else if(!pd.get("user_name2").equals("")){
			PageData pd2 = new PageData();
			pd2.put("user_order", 2);
			pd2.put("remind_method", 0);
			pd2.put("farmId", pd.get("farmId"));
			pd2.put("houseId", pd.get("houseId"));
			pd2.put("user_type", 0);
			pd2.put("create_time", new Date());
			pd2.put("create_person", user.getId());
			pd2.put("user_id", pd.get("user_name2"));
			pd2.put("modify_person",user.getId());
			pd2.put("modify_time", new Date());
			alarmService.saveSBReminder(pd2);
		}
		
		if(!pd.get("user_name3").equals("")
				&& !pd.get("userId3").equals("")){ //&& !pd.get("user_name3").equals(pd.get("userId3"))
			PageData pd2 = new PageData();
			pd2.put("user_order", 3);
			pd2.put("user_id", pd.get("user_name3"));
			pd2.put("farmId", pd.get("farmId"));
			pd2.put("houseId", pd.get("houseId"));
			pd2.put("modify_person",user.getId());
			pd2.put("modify_time", new Date());
			alarmService.updateSBReminder(pd2);
		}else if(!pd.get("user_name3").equals("")){
			PageData pd2 = new PageData();
			pd2.put("user_order", 3);
			pd2.put("remind_method", 0);
			pd2.put("farmId", pd.get("farmId"));
			pd2.put("houseId", pd.get("houseId"));
			pd2.put("user_type", 0);
			pd2.put("create_time", new Date());
			pd2.put("create_person", user.getId());
			pd2.put("user_id", pd.get("user_name3"));
			pd2.put("modify_person",user.getId());
			pd2.put("modify_time", new Date());
			alarmService.saveSBReminder(pd2);
		}
		
		List<PageData> pageData = alarmService.selectSBRemindSwitch(pd);
		if(pageData.size()!=0){
			PageData pd2 = new PageData();
			pd2.put("farmId", pd.get("farmId"));
			pd2.put("houseId", pd.get("houseId"));
			pd2.put("status", pd.get("status"));
			pd2.put("modify_person",user.getId());
			pd2.put("modify_time", new Date());
			alarmService.updateSBRemindSwitch(pd2);
		}else{
			PageData pd2 = new PageData();
			pd2.put("farmId", pd.get("farmId"));
			pd2.put("houseId", pd.get("houseId"));
			pd2.put("remind_method", 0);
			pd2.put("status", pd.get("status"));
			pd2.put("create_time", new Date());
			pd2.put("create_person", user.getId());
			pd2.put("modify_person",user.getId());
			pd2.put("modify_time", new Date());
			alarmService.saveSBRemindSwitch(pd2);
		}
		
		List<PageData> pageData2 = alarmService.selectSBRemindSetting(pd);
		if(pageData2.size()==0){
			PageData pd2 = new PageData();
			pd2.put("farmId", pd.get("farmId"));
			pd2.put("remind_method", 0);
			pd2.put("switch_rele_house", "Y");
			pd2.put("person_rele_house", "Y");
			pd2.put("alarm_rele_house", "Y");
			pd2.put("create_time", new Date());
			alarmService.saveSBRemindSetting(pd2);
		}
		
		List<PageData> pageData3 =null;
		if(pd.get("alarm_type").toString().equals("1")){
			pd.put("alarm_code", "A001");
			pageData3 = alarmService.selectSBRemindSetting(pd);
			if(pageData3.size()==0){
					PageData pd2 = new PageData();
					pd2.put("farmId", pd.get("farmId"));
					pd2.put("houseId", pd.get("houseId"));
					pd2.put("remind_method", 0);
					pd2.put("alarm_code", "A001");
					pd2.put("create_time", new Date());
					pd2.put("create_person", user.getId());
					pd2.put("modify_person",user.getId());
					pd2.put("modify_time", new Date());
					alarmService.saveSBRemindAlarmcode(pd2);
					PageData pd3 = new PageData();
					pd3.put("farmId", pd.get("farmId"));
					pd3.put("houseId", pd.get("houseId"));
					pd3.put("remind_method", 0);
					pd3.put("alarm_code", "A002");
					pd3.put("create_time", new Date());
					pd3.put("create_person", user.getId());
					pd3.put("modify_person",user.getId());
					pd3.put("modify_time", new Date());
					alarmService.saveSBRemindAlarmcode(pd3);
		
			}
		}else if(pd.get("alarm_type").toString().equals("2")){
			pd.put("alarm_code", "A003");
			pageData3 = alarmService.selectSBRemindSetting(pd);
			if(pageData3.size()==0){
					PageData pd2 = new PageData();
					pd2.put("farmId", pd.get("farmId"));
					pd2.put("houseId", pd.get("houseId"));
					pd2.put("remind_method", 0);
					pd2.put("alarm_code", "A003");
					pd2.put("create_time", new Date());
					pd2.put("create_person", user.getId());
					pd2.put("modify_person",user.getId());
					pd2.put("modify_time", new Date());
					alarmService.saveSBRemindAlarmcode(pd2);
					PageData pd3 = new PageData();
					pd3.put("farmId", pd.get("farmId"));
					pd3.put("houseId", pd.get("houseId"));
					pd3.put("remind_method", 0);
					pd3.put("alarm_code", "A004");
					pd3.put("create_time", new Date());
					pd3.put("create_person", user.getId());
					pd3.put("modify_person",user.getId());
					pd3.put("modify_time", new Date());
					alarmService.saveSBRemindAlarmcode(pd3);
		
			}
		}else if(pd.get("alarm_type").toString().equals("3")){
			pd.put("alarm_code", "A005");
			pageData3 = alarmService.selectSBRemindSetting(pd);
			if(pageData3.size()==0){
					PageData pd2 = new PageData();
					pd2.put("farmId", pd.get("farmId"));
					pd2.put("houseId", pd.get("houseId"));
					pd2.put("remind_method", 0);
					pd2.put("alarm_code", "A005");
					pd2.put("create_time", new Date());
					pd2.put("create_person", user.getId());
					pd2.put("modify_person",user.getId());
					pd2.put("modify_time", new Date());
					alarmService.saveSBRemindAlarmcode(pd2);
					PageData pd3 = new PageData();
					pd3.put("farmId", pd.get("farmId"));
					pd3.put("houseId", pd.get("houseId"));
					pd3.put("remind_method", 0);
					pd3.put("alarm_code", "A006");
					pd3.put("create_time", new Date());
					pd3.put("create_person", user.getId());
					pd3.put("modify_person",user.getId());
					pd3.put("modify_time", new Date());
					alarmService.saveSBRemindAlarmcode(pd3);
		
			}
		}else if(pd.get("alarm_type").toString().equals("4")){
			pd.put("alarm_code", "A007");
			pageData3 = alarmService.selectSBRemindSetting(pd);
			if(pageData3.size()==0){
					PageData pd2 = new PageData();
					pd2.put("farmId", pd.get("farmId"));
					pd2.put("houseId", pd.get("houseId"));
					pd2.put("remind_method", 0);
					pd2.put("alarm_code", "A007");
					pd2.put("create_time", new Date());
					pd2.put("create_person", user.getId());
					pd2.put("modify_person",user.getId());
					pd2.put("modify_time", new Date());
					alarmService.saveSBRemindAlarmcode(pd2);
					PageData pd3 = new PageData();
					pd3.put("farmId", pd.get("farmId"));
					pd3.put("houseId", pd.get("houseId"));
					pd3.put("remind_method", 0);
					pd3.put("alarm_code", "A008");
					pd3.put("create_time", new Date());
					pd3.put("create_person", user.getId());
					pd3.put("modify_person",user.getId());
					pd3.put("modify_time", new Date());
					alarmService.saveSBRemindAlarmcode(pd3);
		
			}
		}
		j2.setMsg("1");
		j2.setSuccess(true);
		
	} catch (Exception e) {
		e.printStackTrace();
		j2.setMsg("2");
	}
	super.writeJson(j2, response);
		
	}
	
	@RequestMapping(value="/bindingUserUrl")
	public ModelAndView bindingUserUrl(HttpSession session)throws Exception{
		ModelAndView mv = this.getModelAndView();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id", user.getId());
//		List<PageData> mcl = alarmService.selectByCondition(pd);
		mv.setViewName("modules/alarm/speechAlarm");
//		mv.addObject("sBDayageSettingSubList",mcl);
		List<PageData> pageData = alarmService.selectSBReminderByCondition(pd);
		for(PageData pageData1 : pageData){
			if(pageData1.get("user_order").toString().equals("1")){
				mv.addObject("alarmUser1",pageData1.get("user_id"));
			}else if(pageData1.get("user_order").toString().equals("2")){
				mv.addObject("alarmUser2",pageData1.get("user_id"));
			}else{
				mv.addObject("alarmUser3",pageData1.get("user_id"));
			}
		}
		List<PageData> pageData2 = alarmService.selectSBRemindSwitch(pd);
		if(pageData2.size()!=0){
			mv.addObject("status",pageData2.get(0).get("status"));
		}else{
			mv.addObject("status","N");
		}
		
//		pd.put("user_code", null);
//		pd.put("id", null);
		//设置人员
		pd.put("id",user.getId());
		pd.put("obj_type",2);
		pd.put("user_status",1);
		pd.put("freeze_status",0);
		pd.put("listFlag",1);
		mv.addObject("alarmUserList",userService.getUserList(pd));
		mv.addObject("alarm_type",pd.get("alarm_type"));
		mv.addObject("houseId",pd.get("houseId"));
		mv.addObject("farmId",pd.get("farmId"));
		return mv;
	}
	
	@RequestMapping(value="/applyAlarmUrl")
	public ModelAndView applyAlarmUrl(HttpServletRequest request)throws Exception{
		ModelAndView mv = this.getModelAndView();
		SDUser user = (SDUser)request.getSession().getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("modules/alarm/applyAlarm");
		mv.addObject("pd",pd);
		pd.put("organization_id", pd.get("farmId"));		
		List<PageData> farm = moduleService.service("organServiceImpl", "getOrgListById", new Object[]{pd});
		mv.addObject("farm",farm.get(0).get("name_cn"));//农场名称
		mv.addObject("farmId",farm.get(0).get("organization_id"));//农场organization_id
		pd.put("organization_id", pd.get("houseId"));
		List<PageData> house = moduleService.service("organServiceImpl", "getOrgListById", new Object[]{pd});
		mv.addObject("house",house.get(0).get("name_cn")); //栋舍名称
		mv.addObject("houseId",house.get(0).get("organization_id")); //栋舍organization_id
		pd.put("farm_id", farm.get(0).get("id"));
		pd.put("user_id", user.getId());
		List<PageData> houseList = moduleService.service("organServiceImpl", "getHouseListByUserId", new Object[]{pd});
		mv.addObject("houseList",houseList);
		mv.addObject("alarm_type",pd.get("alarm_type"));
		mv.addObject("dage", pd.get("dage"));
		return mv;
	}
	
	@RequestMapping("/applyAlarm")
	public void applyAlarm(HttpServletResponse response,HttpServletRequest request) throws Exception {
		Json j2 = new Json();
		SDUser user = (SDUser) request.getSession().getAttribute(Const.SESSION_USER);
//		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pd2 = new PageData();
		pd2.put("farmId", pd.get("farmId"));
		pd2.put("houseId", pd.get("houseId2"));
		pd2.put("alarm_type", pd.get("alarm_type"));
		j2 = tempSubApply(pd, pd2, user.getId());
		super.writeJson(j2, response);
	}

	public Json tempSubApply(PageData pd, PageData pd2, int userId) throws Exception{
		Json j2 = new Json();
		List<PageData> pageData = alarmService.selectByCondition(pd);
		List<PageData> pageData2 = alarmService.selectByCondition3(pd2);
		try {
			for(PageData pageData3 : pageData2){
				pd2.put("uid_num", pageData3.get("id"));
				pd2.put("modify_person",userId);
				pd2.put("modify_date", new Date());
				pd2.put("modify_time", new Date());
				alarmService.deleteSBDayageSettingSub(pd2);
				pd2.put("alarm_operation", "删除");
				pd2.put("day_age", pageData3.get("day_age"));
				alarmService.saveSbDayageSettingSubHis(pd2);
				alarmService.deleteSBDayageTempSub(pd2);
			}
			for(PageData pageData1 : pageData){
				int g=0;
				int pdID=0,startTime=0,endTime=0;
				if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2){
					String[] st = pageData1.get("start_time").toString().split(":");
					startTime = Integer.valueOf(st[0]).intValue();
					String[] et = pageData1.get("end_time").toString().split(":");
					endTime = Integer.valueOf(et[0]).intValue();
					pd.put("day_age", Integer.valueOf(pageData1.get("day_age").toString()).intValue()*7);
				}else{
					pd.put("day_age", Integer.valueOf(pageData1.get("day_age").toString()).intValue());
				}
				pd.put("houseId", pd.get("houseId2"));
				pd.put("set_temp", pageData1.get("set_temp"));
				pd.put("high_alarm_temp", pageData1.get("high_alarm_temp"));
				pd.put("low_alarm_temp", pageData1.get("low_alarm_temp"));
				pd.put("high_temp_warning", pageData1.get("high_temp_warning"));
				pd.put("low_temp_warning", pageData1.get("low_temp_warning"));
				pd.put("set_humidity", pageData1.get("set_humidity"));
				pd.put("high_alarm_humidity", pageData1.get("high_alarm_humidity"));
				pd.put("low_alarm_humidity", pageData1.get("low_alarm_humidity"));
				pd.put("high_humidity_warning", pageData1.get("high_humidity_warning"));
				pd.put("low_humidity_warning", pageData1.get("low_humidity_warning"));
				pd.put("set_negative_pressure", pageData1.get("set_negative_pressure"));
				pd.put("high_alarm_negative_pressure", pageData1.get("high_alarm_negative_pressure"));
				pd.put("low_alarm_negative_pressure", pageData1.get("low_alarm_negative_pressure"));
				pd.put("high_negative_pressure_warning", pageData1.get("high_negative_pressure_warning"));
				pd.put("low_negative_pressure_warning", pageData1.get("low_negative_pressure_warning"));
//			pd.put("set_negative_pressure", pageData1.get("set_negative_pressure"));
				pd.put("set_lux", pageData1.get("set_lux"));
				pd.put("high_lux", pageData1.get("high_lux"));
				pd.put("low_lux", pageData1.get("low_lux"));
				pd.put("high_lux_warning", pageData1.get("high_lux_warning"));
				pd.put("low_lux_warning", pageData1.get("low_lux_warning"));
				pd.put("start_time", pageData1.get("start_time"));
				pd.put("end_time", pageData1.get("end_time"));
				pd.put("set_co2", pageData1.get("set_co2"));
				pd.put("high_alarm_co2", pageData1.get("high_alarm_co2"));
				pd.put("high_co2_warning", pageData1.get("high_co2_warning"));
//			pd.put("low_alarm_co2", pageData1.get("low_alarm_co2"));
				pd.put("set_water_deprivation", pageData1.get("set_water_deprivation"));
				pd.put("high_water_deprivation", pageData1.get("high_water_deprivation"));
				pd.put("low_water_deprivation", pageData1.get("low_water_deprivation"));
				pd.put("create_person",userId);
				pd.put("create_date", new Date());
				pd.put("create_time", new Date());
				pd.put("modify_person",userId);
				pd.put("modify_date", new Date());
				pd.put("modify_time", new Date());
				int rling=0;
				if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2){
					rling = Integer.valueOf(pageData1.get("day_age").toString()).intValue()*7;
				}else{
					rling = Integer.valueOf(pageData1.get("day_age").toString()).intValue();
				}
				for(PageData pageData3 : pageData2){
					if(rling == Integer.valueOf(pageData3.get("day_age").toString()).intValue()){
						g++;
						pdID = Integer.valueOf(pageData3.get("id").toString()).intValue();
						pd.put("uid_num", pdID);
						alarmService.updateSBDayageSettingSub(pd);
						pd.put("alarm_operation", "修改");
						alarmService.saveSbDayageSettingSubHis(pd);

						float set_temp=0,high_alarm_temp=0,high_temp_warning=0,low_alarm_temp=0,low_temp_warning=0,
								set_temp1=0,high_alarm_temp1=0,high_temp_warning1=0,low_alarm_temp1=0,low_temp_warning1=0,
								set_temp2=0,high_alarm_temp2=0,high_temp_warning2=0,low_alarm_temp2=0,low_temp_warning2=0,
								set_temp3=0,high_alarm_temp3=0,high_temp_warning3=0,low_alarm_temp3=0,low_temp_warning3=0,
								set_lux=0,high_lux=0,high_lux_warning=0,set_lux1=0,high_lux1=0,high_lux_warning1=0,
								set_lux2=0,high_lux2=0,high_lux_warning2=0,set_lux3=0,high_lux3=0,high_lux_warning3=0,
								low_lux=0,low_lux_warning=0,low_lux1=0,low_lux_warning1=0,low_lux2=0,low_lux_warning2=0,low_lux3=0,low_lux_warning3=0,
								set_co2=0,high_alarm_co2=0,high_co2_warning=0,set_co21=0,high_alarm_co21=0,high_co2_warning1=0,set_co22=0,high_alarm_co22=0,high_co2_warning2=0,
								set_co23=0,high_alarm_co23=0,high_co2_warning3=0,
								set_humidity=0,high_alarm_humidity=0,high_humidity_warning=0,low_alarm_humidity=0,low_humidity_warning=0,
								set_humidity1=0,high_alarm_humidity1=0,high_humidity_warning1=0,low_alarm_humidity1=0,low_humidity_warning1=0,
								set_humidity2=0,high_alarm_humidity2=0,high_humidity_warning2=0,low_alarm_humidity2=0,low_humidity_warning2=0,
								set_humidity3=0,high_alarm_humidity3=0,high_humidity_warning3=0,low_alarm_humidity3=0,low_humidity_warning3=0,
								set_negative_pressure=0,high_alarm_negative_pressure=0,high_negative_pressure_warning=0,low_alarm_negative_pressure=0,low_negative_pressure_warning=0,
								set_negative_pressure1=0,high_alarm_negative_pressure1=0,high_negative_pressure_warning1=0,low_alarm_negative_pressure1=0,low_negative_pressure_warning1=0,
								set_negative_pressure2=0,high_alarm_negative_pressure2=0,high_negative_pressure_warning2=0,low_alarm_negative_pressure2=0,low_negative_pressure_warning2=0,
								set_negative_pressure3=0,high_alarm_negative_pressure3=0,high_negative_pressure_warning3=0,low_alarm_negative_pressure3=0,low_negative_pressure_warning3=0,
								set_water_deprivation=0,high_water_deprivation=0,low_water_deprivation=0,set_water_deprivation1=0,high_water_deprivation1=0,low_water_deprivation1=0,
								set_water_deprivation2=0,high_water_deprivation2=0,low_water_deprivation2=0,set_water_deprivation3=0,high_water_deprivation3=0,low_water_deprivation3=0;
						int uid_num=0;
						int day_age=0,day_age2=0,startTime2=0,endTime2=0;
						boolean choose = false,choose2=false,choose3=false;
//					try {
						List<PageData> pageData5 = alarmService.selectByCondition(pd2);//查询一个栋舍的全部记录
						if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==1){
							//计算温度差、基值
							for(int i=0;i<pageData5.size();i++){
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==0){
									//目标温度1
									set_temp2 = Float.valueOf(pageData5.get(i).get("set_temp").toString()).floatValue();
									//高报温度1
									high_alarm_temp2 = Float.valueOf(pageData5.get(i).get("high_alarm_temp").toString()).floatValue();
									//高报温度警示1
									high_temp_warning2 = Float.valueOf(pageData5.get(i).get("high_temp_warning").toString()).floatValue();
									//低报温度1
									low_alarm_temp2 = Float.valueOf(pageData5.get(i).get("low_alarm_temp").toString()).floatValue();
									//低报温度警示1
									low_temp_warning2 = Float.valueOf(pageData5.get(i).get("low_temp_warning").toString()).floatValue();
									//目标温度2
									set_temp3 = Float.valueOf(pageData5.get(i).get("set_temp").toString()).floatValue();
									//高报温度2
									high_alarm_temp3 = Float.valueOf(pageData5.get(i).get("high_alarm_temp").toString()).floatValue();
									//高报温度警示2
									high_temp_warning3 = Float.valueOf(pageData5.get(i).get("high_temp_warning").toString()).floatValue();
									//低报温度2
									low_alarm_temp3 = Float.valueOf(pageData5.get(i).get("low_alarm_temp").toString()).floatValue();
									//低报温度警示2
									low_temp_warning3 = Float.valueOf(pageData5.get(i).get("low_temp_warning").toString()).floatValue();
									if(pageData5.size()!=1){
										//目标温度差2
										set_temp1 = (Float.valueOf(pageData5.get(i+1).get("set_temp").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("set_temp").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//目标温度2
										set_temp3 = Float.valueOf(pageData5.get(i).get("set_temp").toString()).floatValue();
										//高报温度差2
										high_alarm_temp1 = (Float.valueOf(pageData5.get(i+1).get("high_alarm_temp").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("high_alarm_temp").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//高报温度警示差2
										high_temp_warning1 = (Float.valueOf(pageData5.get(i+1).get("high_temp_warning").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("high_temp_warning").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										
										//高报温度2
										high_alarm_temp3 = Float.valueOf(pageData5.get(i).get("high_alarm_temp").toString()).floatValue();
										//高报温度警示2
										high_temp_warning3 = Float.valueOf(pageData5.get(i).get("high_temp_warning").toString()).floatValue();
										//低报温度差2
										low_alarm_temp1 = (Float.valueOf(pageData5.get(i+1).get("low_alarm_temp").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("low_alarm_temp").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//低报温度警示差2
										low_temp_warning1 = (Float.valueOf(pageData5.get(i+1).get("low_temp_warning").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("low_temp_warning").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										
										//低报温度2
										low_alarm_temp3 = Float.valueOf(pageData5.get(i).get("low_alarm_temp").toString()).floatValue();
										//低报温度警示2
										low_temp_warning3 = Float.valueOf(pageData5.get(i).get("low_temp_warning").toString()).floatValue();
										//uid_num
										uid_num = Integer.valueOf(pageData5.get(i+1).get("id").toString()).intValue();
										day_age2 = Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue();
										choose3=true;
									}
									choose=true;
									break;
								}else
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i>0 && i<pageData5.size()-1){
									//目标温度差1
									set_temp = (Float.valueOf(pageData5.get(i).get("set_temp").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_temp").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标温度1
									set_temp2 = Float.valueOf(pageData5.get(i-1).get("set_temp").toString()).floatValue();
									//高报温度差1
									high_alarm_temp = (Float.valueOf(pageData5.get(i).get("high_alarm_temp").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_alarm_temp").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//高报温度警示差1
									high_temp_warning = (Float.valueOf(pageData5.get(i).get("high_temp_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_temp_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//高报温度1
									high_alarm_temp2 = Float.valueOf(pageData5.get(i-1).get("high_alarm_temp").toString()).floatValue();
									//高报温度警示1
									high_temp_warning2 = Float.valueOf(pageData5.get(i-1).get("high_temp_warning").toString()).floatValue();
									//低报温度差1
									low_alarm_temp = (Float.valueOf(pageData5.get(i).get("low_alarm_temp").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_alarm_temp").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//低报温度警示差1
									low_temp_warning = (Float.valueOf(pageData5.get(i).get("low_temp_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_temp_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//低报温度1
									low_alarm_temp2 = Float.valueOf(pageData5.get(i-1).get("low_alarm_temp").toString()).floatValue();
									//低报温度警示1
									low_temp_warning2 = Float.valueOf(pageData5.get(i-1).get("low_temp_warning").toString()).floatValue();
									//目标温度差2
									set_temp1 = (Float.valueOf(pageData5.get(i+1).get("set_temp").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("set_temp").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标温度2
									set_temp3 = Float.valueOf(pageData5.get(i).get("set_temp").toString()).floatValue();
									//高报温度差2
									high_alarm_temp1 = (Float.valueOf(pageData5.get(i+1).get("high_alarm_temp").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("high_alarm_temp").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//高报温度警示差2
									high_temp_warning1 = (Float.valueOf(pageData5.get(i+1).get("high_temp_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("high_temp_warning").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//高报温度2
									high_alarm_temp3 = Float.valueOf(pageData5.get(i).get("high_alarm_temp").toString()).floatValue();
									//高报温度警示2
									high_temp_warning3 = Float.valueOf(pageData5.get(i).get("high_temp_warning").toString()).floatValue();
									//低报温度差2
									low_alarm_temp1 = (Float.valueOf(pageData5.get(i+1).get("low_alarm_temp").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("low_alarm_temp").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//低报温度警示差2
									low_temp_warning1 = (Float.valueOf(pageData5.get(i+1).get("low_temp_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("low_temp_warning").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//低报温度2
									low_alarm_temp3 = Float.valueOf(pageData5.get(i).get("low_alarm_temp").toString()).floatValue();
									//低报温度警示2
									low_temp_warning3 = Float.valueOf(pageData5.get(i).get("low_temp_warning").toString()).floatValue();
									//uid_num
									uid_num = Integer.valueOf(pageData5.get(i+1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue();
									day_age = Integer.valueOf(pageData5.get(i-1).get("day_age").toString()).intValue();
									choose2 = true;
									break;
								}else if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==pageData5.size()-1){
									//目标温度差
									set_temp = (Float.valueOf(pageData5.get(i).get("set_temp").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_temp").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标温度
									set_temp2 = Float.valueOf(pageData5.get(i-1).get("set_temp").toString()).floatValue();
									//高报温度差
									high_alarm_temp = (Float.valueOf(pageData5.get(i).get("high_alarm_temp").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_alarm_temp").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//高报温度警示差
									high_temp_warning = (Float.valueOf(pageData5.get(i).get("high_temp_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_temp_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//高报温度
									high_alarm_temp2 = Float.valueOf(pageData5.get(i-1).get("high_alarm_temp").toString()).floatValue();
									//高报温度警示
									high_temp_warning2 = Float.valueOf(pageData5.get(i-1).get("high_temp_warning").toString()).floatValue();
									//低报温度差
									low_alarm_temp = (Float.valueOf(pageData5.get(i).get("low_alarm_temp").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_alarm_temp").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//低报温度警示差
									low_temp_warning = (Float.valueOf(pageData5.get(i).get("low_temp_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_temp_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//低报温度
									low_alarm_temp2 = Float.valueOf(pageData5.get(i-1).get("low_alarm_temp").toString()).floatValue();
									//低报温度警示
									low_temp_warning2 = Float.valueOf(pageData5.get(i-1).get("low_temp_warning").toString()).floatValue();
									//目标温度2
									set_temp3 = Float.valueOf(pageData5.get(i).get("set_temp").toString()).floatValue();
									//高报温度2
									high_alarm_temp3 = Float.valueOf(pageData5.get(i).get("high_alarm_temp").toString()).floatValue();
									//高报温度警示2
									high_temp_warning3 = Float.valueOf(pageData5.get(i).get("high_temp_warning").toString()).floatValue();
									//低报温度2
									low_alarm_temp3 = Float.valueOf(pageData5.get(i).get("low_alarm_temp").toString()).floatValue();
									//低报温度警示2
									low_temp_warning3 = Float.valueOf(pageData5.get(i).get("low_temp_warning").toString()).floatValue();
									uid_num = Integer.valueOf(pageData5.get(i-1).get("id").toString()).intValue();
									//日龄
									day_age = Integer.valueOf(pageData5.get(i-1).get("day_age").toString()).intValue();
									choose = true;
									break;
								}
							}
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2){
							//计算负压差、基值
							for(int i=0;i<pageData5.size();i++){
								int d =Integer.valueOf(pageData5.get(i).get("day_age").toString()).intValue()*7;
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										d==Integer.valueOf(pd.get("day_age").toString()).intValue() && i==0){
									//目标负压1
//								set_negative_pressure2 = Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue();
									//光照目标值1
									set_lux3 = Float.valueOf(pageData5.get(i).get("set_lux").toString()).floatValue();
									//光照上限值1
									high_lux3 = Float.valueOf(pageData5.get(i).get("high_lux").toString()).floatValue();
									//光照上限警示1
									high_lux_warning3 = Float.valueOf(pageData5.get(i).get("high_lux_warning").toString()).floatValue();
									//光照下限值
									low_lux3 = Float.valueOf(pageData5.get(i).get("low_lux").toString()).floatValue();
									//光照下限警示
									low_lux_warning3 = Float.valueOf(pageData5.get(i).get("low_lux_warning").toString()).floatValue();
									if(pageData5.size()!=1){
										//目标负压差2
//									set_negative_pressure1 = (Float.valueOf(pageData5.get(i+1).get("set_negative_pressure").toString()).floatValue() -
//											Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue())/
//									((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//目标负压2
//									set_negative_pressure3 = Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue();
										//光照目标值差2
										set_lux1 = (Float.valueOf(pageData5.get(i+1).get("set_lux").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("set_lux").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//光照目标值2
										set_lux3 = Float.valueOf(pageData5.get(i).get("set_lux").toString()).floatValue();
										//光照上限值差2
										high_lux1 = (Float.valueOf(pageData5.get(i+1).get("high_lux").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("high_lux").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//光照上限警示差2
										high_lux_warning1 = (Float.valueOf(pageData5.get(i+1).get("high_lux_warning").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("high_lux_warning").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										
										//光照上限值2
										high_lux3 = Float.valueOf(pageData5.get(i).get("high_lux").toString()).floatValue();
										//光照上限警示2
										high_lux_warning3 = Float.valueOf(pageData5.get(i).get("high_lux_warning").toString()).floatValue();
										//光照下限值差2
										low_lux1 = (Float.valueOf(pageData5.get(i+1).get("low_lux").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("low_lux").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//光照下限警示差2
										low_lux_warning1 = (Float.valueOf(pageData5.get(i+1).get("low_lux_warning").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("low_lux_warning").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										
										//光照下限值
										low_lux3 = Float.valueOf(pageData5.get(i).get("low_lux").toString()).floatValue();
										//光照下限警示
										low_lux_warning3 = Float.valueOf(pageData5.get(i).get("low_lux_warning").toString()).floatValue();
										//uid_num
										uid_num = Integer.valueOf(pageData5.get(i+1).get("id").toString()).intValue();
										day_age2 = (Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue()-1)*7;
										String[] st2 = pageData5.get(i+1).get("start_time").toString().split(":");
										startTime2 = Integer.valueOf(st2[0]).intValue();
										String[] et2 = pageData5.get(i+1).get("end_time").toString().split(":");
										endTime2 = Integer.valueOf(et2[0]).intValue();
										choose3=true;
									}
									choose=true;
									break;
								}else
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										d==Integer.valueOf(pd.get("day_age").toString()).intValue() && i>0 && i<pageData5.size()-1){
									//目标负压差1
//									set_negative_pressure = (Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue() -
//											Float.valueOf(pageData5.get(i-1).get("set_negative_pressure").toString()).floatValue())/
//									((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标负压1
//									set_negative_pressure2 = Float.valueOf(pageData5.get(i-1).get("set_negative_pressure").toString()).floatValue();
									//光照目标值差1
									set_lux = (Float.valueOf(pageData5.get(i).get("set_lux").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_lux").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue()*7)*24);
									//光照目标值1
									set_lux2 = Float.valueOf(pageData5.get(i-1).get("set_lux").toString()).floatValue();
									//光照上限值差1
									high_lux = (Float.valueOf(pageData5.get(i).get("high_lux").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_lux").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue()*7)*24);
									//光照上限警示差1
									high_lux_warning = (Float.valueOf(pageData5.get(i).get("high_lux_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_lux_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue()*7)*24);
									
									//光照上限值1
									high_lux2 = Float.valueOf(pageData5.get(i-1).get("high_lux").toString()).floatValue();
									//光照上限警示1
									high_lux_warning2 = Float.valueOf(pageData5.get(i-1).get("high_lux_warning").toString()).floatValue();
									//光照下限值差1
									low_lux = (Float.valueOf(pageData5.get(i).get("low_lux").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_lux").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue()*7)*24);
									//光照下限警示差1
									low_lux_warning = (Float.valueOf(pageData5.get(i).get("low_lux_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_lux_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue()*7)*24);
									
									//光照下限值1
									low_lux2 = Float.valueOf(pageData5.get(i-1).get("low_lux").toString()).floatValue();
									//光照下限警示1
									low_lux_warning2 = Float.valueOf(pageData5.get(i-1).get("low_lux_warning").toString()).floatValue();
									//目标负压差2
//									set_negative_pressure1 = (Float.valueOf(pageData5.get(i+1).get("set_negative_pressure").toString()).floatValue() -
//											Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue())/
//									((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标负压2
//									set_negative_pressure3 = Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue();
									//光照目标值差2
									set_lux1 = (Float.valueOf(pageData5.get(i+1).get("set_lux").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("set_lux").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//光照目标值2
									set_lux3 = Float.valueOf(pageData5.get(i).get("set_lux").toString()).floatValue();
									//光照上限值差2
									high_lux1 = (Float.valueOf(pageData5.get(i+1).get("high_lux").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("high_lux").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//光照上限警示差2
									high_lux_warning1 = (Float.valueOf(pageData5.get(i+1).get("high_lux_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("high_lux_warning").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//光照上限值2
									high_lux3 = Float.valueOf(pageData5.get(i).get("high_lux").toString()).floatValue();
									//光照上限警示2
									high_lux_warning3 = Float.valueOf(pageData5.get(i).get("high_lux_warning").toString()).floatValue();
									//光照下限值差2
									low_lux1 = (Float.valueOf(pageData5.get(i+1).get("low_lux").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("low_lux").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//光照下限警示差2
									low_lux_warning1 = (Float.valueOf(pageData5.get(i+1).get("low_lux_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("low_lux_warning").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//光照下限值2
									low_lux3 = Float.valueOf(pageData5.get(i).get("low_lux").toString()).floatValue();
									//光照下限警示2
									low_lux_warning3 = Float.valueOf(pageData5.get(i).get("low_lux_warning").toString()).floatValue();
									//uid_num
									uid_num = Integer.valueOf(pageData5.get(i-1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue()*7;
									day_age = (Integer.valueOf(pageData5.get(i).get("day_age").toString()).intValue()-1)*7;
									String[] st2 = pageData5.get(i+1).get("start_time").toString().split(":");
									startTime2 = Integer.valueOf(st2[0]).intValue();
									String[] et2 = pageData5.get(i+1).get("end_time").toString().split(":");
									endTime2 = Integer.valueOf(et2[0]).intValue();
									choose2 = true;
									break;
								}else if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										d==Integer.valueOf(pd.get("day_age").toString()).intValue() && i==pageData5.size()-1){
									//目标负压差
//									set_negative_pressure = (Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue() -
//											Float.valueOf(pageData5.get(i-1).get("set_negative_pressure").toString()).floatValue())/
//									((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标负压
//									set_negative_pressure2 = Float.valueOf(pageData5.get(i-1).get("set_negative_pressure").toString()).floatValue();
									//光照目标值差
									set_lux = (Float.valueOf(pageData5.get(i).get("set_lux").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_lux").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue()*7)*24);
									//光照目标值
									set_lux2 = Float.valueOf(pageData5.get(i-1).get("set_lux").toString()).floatValue();
									//光照上限值差
									high_lux = (Float.valueOf(pageData5.get(i).get("high_lux").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_lux").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue()*7)*24);
									//光照上限警示差
									high_lux_warning = (Float.valueOf(pageData5.get(i).get("high_lux_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_lux_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue()*7)*24);
									
									//光照上限值
									high_lux2 = Float.valueOf(pageData5.get(i-1).get("high_lux").toString()).floatValue();
									//光照上限警示
									high_lux_warning2 = Float.valueOf(pageData5.get(i-1).get("high_lux_warning").toString()).floatValue();
									//光照下限值差
									low_lux = (Float.valueOf(pageData5.get(i).get("low_lux").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_lux").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue()*7)*24);
									//光照下限警示差
									low_lux_warning = (Float.valueOf(pageData5.get(i).get("low_lux_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_lux_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue()*7)*24);
									
									//光照下限值
									low_lux2 = Float.valueOf(pageData5.get(i-1).get("low_lux").toString()).floatValue();
									//光照下限警示
									low_lux_warning2 = Float.valueOf(pageData5.get(i-1).get("low_lux_warning").toString()).floatValue();
									
									//光照目标值2
									set_lux3 = Float.valueOf(pageData5.get(i).get("set_lux").toString()).floatValue();
									//光照上限值2
									high_lux3 = Float.valueOf(pageData5.get(i).get("high_lux").toString()).floatValue();
									//光照上限警示2
									high_lux_warning3 = Float.valueOf(pageData5.get(i).get("high_lux_warning").toString()).floatValue();
									//光照下限值2
									low_lux3 = Float.valueOf(pageData5.get(i).get("low_lux").toString()).floatValue();
									//光照下限警示2
									low_lux_warning3 = Float.valueOf(pageData5.get(i).get("low_lux_warning").toString()).floatValue();
									uid_num = Integer.valueOf(pageData5.get(i-1).get("id").toString()).intValue();
									day_age = (Integer.valueOf(pageData5.get(i).get("day_age").toString()).intValue()-1)*7;
									choose = true;
									break;
								}
							}
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==3){
							//计算二氧化碳差、基值
							for(int i=0;i<pageData5.size();i++){
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==0){
									//目标二氧化碳1
									set_co22 = Float.valueOf(pageData5.get(i).get("set_co2").toString()).floatValue();
									//高报二氧化碳1
									high_alarm_co22 = Float.valueOf(pageData5.get(i).get("high_alarm_co2").toString()).floatValue();
									//高报二氧化碳警示1
									high_co2_warning2 = Float.valueOf(pageData5.get(i).get("high_co2_warning").toString()).floatValue();
									//低报二氧化碳1
//								low_alarm_co22 = Float.valueOf(pageData5.get(i).get("low_alarm_co2").toString()).floatValue();
									if(pageData5.size()!=1){
										//目标二氧化碳差2
										set_co21 = (Float.valueOf(pageData5.get(i+1).get("set_co2").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("set_co2").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//目标二氧化碳2
										set_co23 = Float.valueOf(pageData5.get(i).get("set_co2").toString()).floatValue();
										//高报二氧化碳差2
										high_alarm_co21 = (Float.valueOf(pageData5.get(i+1).get("high_alarm_co2").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("high_alarm_co2").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//高报二氧化碳警示差2
										high_co2_warning1 = (Float.valueOf(pageData5.get(i+1).get("high_co2_warning").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("high_co2_warning").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										
										//高报二氧化碳2
										high_alarm_co23 = Float.valueOf(pageData5.get(i).get("high_alarm_co2").toString()).floatValue();
										//高报二氧化碳警示2
										high_co2_warning3 = Float.valueOf(pageData5.get(i).get("high_co2_warning").toString()).floatValue();
										//低报二氧化碳差2
//									low_alarm_co21 = (Float.valueOf(pageData5.get(i+1).get("low_alarm_co2").toString()).floatValue() -
//											Float.valueOf(pageData5.get(i).get("low_alarm_co2").toString()).floatValue())/
//									((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//低报二氧化碳2
//									low_alarm_co23 = Float.valueOf(pageData5.get(i).get("low_alarm_co2").toString()).floatValue();
										//uid_num
										uid_num = Integer.valueOf(pageData5.get(i+1).get("id").toString()).intValue();
										day_age2 = Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue();
									}
									break;
								}else
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i>0 && i<pageData5.size()-1){
									//目标二氧化碳差1
									set_co2 = (Float.valueOf(pageData5.get(i).get("set_co2").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_co2").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标二氧化碳1
									set_co22 = Float.valueOf(pageData5.get(i-1).get("set_co2").toString()).floatValue();
									//高报二氧化碳差1
									high_alarm_co2 = (Float.valueOf(pageData5.get(i).get("high_alarm_co2").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_alarm_co2").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//高报二氧化碳警示差1
									high_co2_warning = (Float.valueOf(pageData5.get(i).get("high_co2_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_co2_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//高报二氧化碳1
									high_alarm_co22 = Float.valueOf(pageData5.get(i-1).get("high_alarm_co2").toString()).floatValue();
									//高报二氧化碳警示1
									high_co2_warning2 = Float.valueOf(pageData5.get(i-1).get("high_co2_warning").toString()).floatValue();
									//低报二氧化碳差1
//									low_alarm_co2 = (Float.valueOf(pageData5.get(i).get("low_alarm_co2").toString()).floatValue() -
//											Float.valueOf(pageData5.get(i-1).get("low_alarm_co2").toString()).floatValue())/
//									((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//低报二氧化碳1
//									low_alarm_co22 = Float.valueOf(pageData5.get(i-1).get("low_alarm_co2").toString()).floatValue();
									//目标二氧化碳差2
									set_co21 = (Float.valueOf(pageData5.get(i+1).get("set_co2").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("set_co2").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标二氧化碳2
									set_co23 = Float.valueOf(pageData5.get(i).get("set_co2").toString()).floatValue();
									//高报二氧化碳差2
									high_alarm_co21 = (Float.valueOf(pageData5.get(i+1).get("high_alarm_co2").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("high_alarm_co2").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//高报二氧化碳警示差2
									high_co2_warning1 = (Float.valueOf(pageData5.get(i+1).get("high_co2_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("high_co2_warning").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//高报二氧化碳2
									high_alarm_co23 = Float.valueOf(pageData5.get(i).get("high_alarm_co2").toString()).floatValue();
									//高报二氧化碳警示2
									high_co2_warning3 = Float.valueOf(pageData5.get(i).get("high_co2_warning").toString()).floatValue();
									//低报二氧化碳差2
//									low_alarm_co21 = (Float.valueOf(pageData5.get(i+1).get("low_alarm_co2").toString()).floatValue() -
//											Float.valueOf(pageData5.get(i).get("low_alarm_co2").toString()).floatValue())/
//									((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//低报二氧化碳2
//									low_alarm_co23 = Float.valueOf(pageData5.get(i).get("low_alarm_co2").toString()).floatValue();
									//uid_num
									uid_num = Integer.valueOf(pageData5.get(i+1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue();
									day_age = Integer.valueOf(pageData5.get(i-1).get("day_age").toString()).intValue();
									break;
								}else if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==pageData5.size()-1){
									//目标二氧化碳差
									set_co2 = (Float.valueOf(pageData5.get(i).get("set_co2").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_co2").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标二氧化碳
									set_co22 = Float.valueOf(pageData5.get(i-1).get("set_co2").toString()).floatValue();
									//高报二氧化碳差
									high_alarm_co2 = (Float.valueOf(pageData5.get(i).get("high_alarm_co2").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_alarm_co2").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//高报二氧化碳警示差
									high_co2_warning = (Float.valueOf(pageData5.get(i).get("high_co2_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_co2_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//高报二氧化碳
									high_alarm_co22 = Float.valueOf(pageData5.get(i-1).get("high_alarm_co2").toString()).floatValue();
									//高报二氧化碳警示
									high_co2_warning2 = Float.valueOf(pageData5.get(i-1).get("high_co2_warning").toString()).floatValue();
									//低报二氧化碳差
//									low_alarm_co2 = (Float.valueOf(pageData5.get(i).get("low_alarm_co2").toString()).floatValue() -
//											Float.valueOf(pageData5.get(i-1).get("low_alarm_co2").toString()).floatValue())/
//									((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//低报二氧化碳
//									low_alarm_co22 = Float.valueOf(pageData5.get(i-1).get("low_alarm_co2").toString()).floatValue();
									day_age = Integer.valueOf(pageData5.get(i-1).get("day_age").toString()).intValue();
									break;
								}
							}
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==5){
							//计算湿度差、基值
							for(int i=0;i<pageData5.size();i++){
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==0){
									//目标湿度1
									set_humidity2 = Float.valueOf(pageData5.get(i).get("set_humidity").toString()).floatValue();
									//高报湿度1
									high_alarm_humidity2 = Float.valueOf(pageData5.get(i).get("high_alarm_humidity").toString()).floatValue();
									//高报湿度警示1
									high_humidity_warning2 = Float.valueOf(pageData5.get(i).get("high_humidity_warning").toString()).floatValue();
									//低报湿度1
									low_alarm_humidity2 = Float.valueOf(pageData5.get(i).get("low_alarm_humidity").toString()).floatValue();
									//低报湿度警示1
									low_humidity_warning2 = Float.valueOf(pageData5.get(i).get("low_humidity_warning").toString()).floatValue();
									//目标湿度2
									set_humidity3 = Float.valueOf(pageData5.get(i).get("set_humidity").toString()).floatValue();
									//高报湿度2
									high_alarm_humidity3 = Float.valueOf(pageData5.get(i).get("high_alarm_humidity").toString()).floatValue();
									//高报湿度警示2
									high_humidity_warning3 = Float.valueOf(pageData5.get(i).get("high_humidity_warning").toString()).floatValue();
									//低报湿度2
									low_alarm_humidity3 = Float.valueOf(pageData5.get(i).get("low_alarm_humidity").toString()).floatValue();
									//低报湿度警示2
									low_humidity_warning3 = Float.valueOf(pageData5.get(i).get("low_humidity_warning").toString()).floatValue();
									if(pageData5.size()!=1){
										//目标湿度差2
										set_humidity1 = (Float.valueOf(pageData5.get(i+1).get("set_humidity").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("set_humidity").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//目标湿度2
										set_humidity3 = Float.valueOf(pageData5.get(i).get("set_humidity").toString()).floatValue();
										//高报湿度差2
										high_alarm_humidity1 = (Float.valueOf(pageData5.get(i+1).get("high_alarm_humidity").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("high_alarm_humidity").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//高报湿度警示差2
										high_humidity_warning1 = (Float.valueOf(pageData5.get(i+1).get("high_humidity_warning").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("high_humidity_warning").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										
										//高报湿度2
										high_alarm_humidity3 = Float.valueOf(pageData5.get(i).get("high_alarm_humidity").toString()).floatValue();
										//高报湿度警示2
										high_humidity_warning3 = Float.valueOf(pageData5.get(i).get("high_humidity_warning").toString()).floatValue();
										//低报湿度差2
										low_alarm_humidity1 = (Float.valueOf(pageData5.get(i+1).get("low_alarm_humidity").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("low_alarm_humidity").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//低报湿度警示差2
										low_humidity_warning1 = (Float.valueOf(pageData5.get(i+1).get("low_humidity_warning").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("low_humidity_warning").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										
										//低报湿度2
										low_alarm_humidity3 = Float.valueOf(pageData5.get(i).get("low_alarm_humidity").toString()).floatValue();
										//低报湿度警示2
										low_humidity_warning3 = Float.valueOf(pageData5.get(i).get("low_humidity_warning").toString()).floatValue();
										//uid_num
										uid_num = Integer.valueOf(pageData5.get(i+1).get("id").toString()).intValue();
										day_age2 = Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue();
										choose3=true;
									}
									choose=true;
									break;
								}else
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i>0 && i<pageData5.size()-1){
									//目标湿度差1
									set_humidity = (Float.valueOf(pageData5.get(i).get("set_humidity").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_humidity").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标湿度1
									set_humidity2 = Float.valueOf(pageData5.get(i-1).get("set_humidity").toString()).floatValue();
									//高报湿度差1
									high_alarm_humidity = (Float.valueOf(pageData5.get(i).get("high_alarm_humidity").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_alarm_humidity").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//高报湿度警示差1
									high_humidity_warning = (Float.valueOf(pageData5.get(i).get("high_humidity_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_humidity_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//高报湿度1
									high_alarm_humidity2 = Float.valueOf(pageData5.get(i-1).get("high_alarm_humidity").toString()).floatValue();
									//高报湿度警示1
									high_humidity_warning2 = Float.valueOf(pageData5.get(i-1).get("high_humidity_warning").toString()).floatValue();
									//低报湿度差1
									low_alarm_humidity = (Float.valueOf(pageData5.get(i).get("low_alarm_humidity").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_alarm_humidity").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//低报湿度警示差1
									low_humidity_warning = (Float.valueOf(pageData5.get(i).get("low_humidity_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_humidity_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//低报湿度1
									low_alarm_humidity2 = Float.valueOf(pageData5.get(i-1).get("low_alarm_humidity").toString()).floatValue();
									//低报湿度警示1
									low_humidity_warning2 = Float.valueOf(pageData5.get(i-1).get("low_humidity_warning").toString()).floatValue();
									//目标湿度差2
									set_humidity1 = (Float.valueOf(pageData5.get(i+1).get("set_humidity").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("set_humidity").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标湿度2
									set_humidity3 = Float.valueOf(pageData5.get(i).get("set_humidity").toString()).floatValue();
									//高报湿度差2
									high_alarm_humidity1 = (Float.valueOf(pageData5.get(i+1).get("high_alarm_humidity").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("high_alarm_humidity").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//高报湿度警示差2
									high_humidity_warning1 = (Float.valueOf(pageData5.get(i+1).get("high_humidity_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("high_humidity_warning").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//高报湿度2
									high_alarm_humidity3 = Float.valueOf(pageData5.get(i).get("high_alarm_humidity").toString()).floatValue();
									//高报湿度警示2
									high_humidity_warning3 = Float.valueOf(pageData5.get(i).get("high_humidity_warning").toString()).floatValue();
									//低报湿度差2
									low_alarm_humidity1 = (Float.valueOf(pageData5.get(i+1).get("low_alarm_humidity").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("low_alarm_humidity").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//低报湿度警示差2
									low_humidity_warning1 = (Float.valueOf(pageData5.get(i+1).get("low_humidity_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("low_humidity_warning").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//低报湿度2
									low_alarm_humidity3 = Float.valueOf(pageData5.get(i).get("low_alarm_humidity").toString()).floatValue();
									//低报湿度警示2
									low_humidity_warning3 = Float.valueOf(pageData5.get(i).get("low_humidity_warning").toString()).floatValue();
									//uid_num
									uid_num = Integer.valueOf(pageData5.get(i+1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue();
									day_age = Integer.valueOf(pageData5.get(i-1).get("day_age").toString()).intValue();
									choose2 = true;
									break;
								}else if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==pageData5.size()-1){
									//目标湿度差
									set_humidity = (Float.valueOf(pageData5.get(i).get("set_humidity").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_humidity").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标湿度
									set_humidity2 = Float.valueOf(pageData5.get(i-1).get("set_humidity").toString()).floatValue();
									//高报湿度差
									high_alarm_humidity = (Float.valueOf(pageData5.get(i).get("high_alarm_humidity").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_alarm_humidity").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//高报湿度警示差
									high_humidity_warning = (Float.valueOf(pageData5.get(i).get("high_humidity_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_humidity_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//高报湿度
									high_alarm_humidity2 = Float.valueOf(pageData5.get(i-1).get("high_alarm_humidity").toString()).floatValue();
									//高报湿度警示
									high_humidity_warning2 = Float.valueOf(pageData5.get(i-1).get("high_humidity_warning").toString()).floatValue();
									//低报湿度差
									low_alarm_humidity = (Float.valueOf(pageData5.get(i).get("low_alarm_humidity").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_alarm_humidity").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//低报湿度警示差
									low_humidity_warning = (Float.valueOf(pageData5.get(i).get("low_humidity_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_humidity_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//低报湿度
									low_alarm_humidity2 = Float.valueOf(pageData5.get(i-1).get("low_alarm_humidity").toString()).floatValue();
									//低报湿度警示
									low_humidity_warning2 = Float.valueOf(pageData5.get(i-1).get("low_humidity_warning").toString()).floatValue();
									//目标湿度2
									set_humidity3 = Float.valueOf(pageData5.get(i).get("set_humidity").toString()).floatValue();
									//高报湿度2
									high_alarm_humidity3 = Float.valueOf(pageData5.get(i).get("high_alarm_humidity").toString()).floatValue();
									//高报湿度警示2
									high_humidity_warning3 = Float.valueOf(pageData5.get(i).get("high_humidity_warning").toString()).floatValue();
									//低报湿度2
									low_alarm_humidity3 = Float.valueOf(pageData5.get(i).get("low_alarm_humidity").toString()).floatValue();
									//低报湿度警示2
									low_humidity_warning3 = Float.valueOf(pageData5.get(i).get("low_humidity_warning").toString()).floatValue();
									uid_num = Integer.valueOf(pageData5.get(i-1).get("id").toString()).intValue();
									//日龄
									day_age = Integer.valueOf(pageData5.get(i-1).get("day_age").toString()).intValue();
									choose = true;
									break;
								}
							}
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==6){
							//计算负压差、基值
							for(int i=0;i<pageData5.size();i++){
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==0){
									//目标负压1
									set_negative_pressure2 = Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue();
									//高报负压1
									high_alarm_negative_pressure2 = Float.valueOf(pageData5.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
									//高报负压警示1
									high_negative_pressure_warning2 = Float.valueOf(pageData5.get(i).get("high_negative_pressure_warning").toString()).floatValue();
									//低报负压1
								    low_alarm_negative_pressure2 = Float.valueOf(pageData5.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
								    //低报负压警示1
									low_negative_pressure_warning2 = Float.valueOf(pageData5.get(i).get("low_negative_pressure_warning").toString()).floatValue();
									if(pageData5.size()!=1){
										//目标负压差2
										set_negative_pressure1 = (Float.valueOf(pageData5.get(i+1).get("set_negative_pressure").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//目标负压2
										set_negative_pressure3 = Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue();
										//高报负压差2
										high_alarm_negative_pressure1 = (Float.valueOf(pageData5.get(i+1).get("high_alarm_negative_pressure").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("high_alarm_negative_pressure").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//高报负压警示差2
										high_negative_pressure_warning1 = (Float.valueOf(pageData5.get(i+1).get("high_negative_pressure_warning").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("high_negative_pressure_warning").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										
										//高报负压2
										high_alarm_negative_pressure3 = Float.valueOf(pageData5.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
										//高报负压警示2
										high_negative_pressure_warning3 = Float.valueOf(pageData5.get(i).get("high_negative_pressure_warning").toString()).floatValue();
										//低报负压差2
									low_alarm_negative_pressure1 = (Float.valueOf(pageData5.get(i+1).get("low_alarm_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("low_alarm_negative_pressure").toString()).floatValue())/
									((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//低报负压2
									low_alarm_negative_pressure3 = Float.valueOf(pageData5.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
									//低报负压警示2
									low_negative_pressure_warning3 = Float.valueOf(pageData5.get(i).get("low_negative_pressure_warning").toString()).floatValue();
									//低报负压警示差2
									low_negative_pressure_warning1 = (Float.valueOf(pageData5.get(i+1).get("low_negative_pressure_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("low_negative_pressure_warning").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//uid_num
										uid_num = Integer.valueOf(pageData5.get(i+1).get("id").toString()).intValue();
										day_age2 = Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue();
									}
									break;
								}else
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i>0 && i<pageData5.size()-1){
									//目标负压差1
									set_negative_pressure = (Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_negative_pressure").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标负压1
									set_negative_pressure2 = Float.valueOf(pageData5.get(i-1).get("set_negative_pressure").toString()).floatValue();
									//高报负压差1
									high_alarm_negative_pressure = (Float.valueOf(pageData5.get(i).get("high_alarm_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_alarm_negative_pressure").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//高报负压警示差1
									high_negative_pressure_warning = (Float.valueOf(pageData5.get(i).get("high_negative_pressure_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_negative_pressure_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//高报负压1
									high_alarm_negative_pressure2 = Float.valueOf(pageData5.get(i-1).get("high_alarm_negative_pressure").toString()).floatValue();
									//高报负压警示1
									high_negative_pressure_warning2 = Float.valueOf(pageData5.get(i-1).get("high_negative_pressure_warning").toString()).floatValue();
									//低报负压差1
									low_alarm_negative_pressure = (Float.valueOf(pageData5.get(i).get("low_alarm_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue())/
									((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//低报负压1
									low_alarm_negative_pressure2 = Float.valueOf(pageData5.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue();
									//低报负压警示1
									low_negative_pressure_warning2 = Float.valueOf(pageData5.get(i-1).get("low_negative_pressure_warning").toString()).floatValue();
									//低报负压警示差1
									low_negative_pressure_warning = (Float.valueOf(pageData5.get(i).get("low_negative_pressure_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_negative_pressure_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标负压差2
									set_negative_pressure1 = (Float.valueOf(pageData5.get(i+1).get("set_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标负压2
									set_negative_pressure3 = Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue();
									//高报负压差2
									high_alarm_negative_pressure1 = (Float.valueOf(pageData5.get(i+1).get("high_alarm_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("high_alarm_negative_pressure").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//高报负压警示差2
									high_negative_pressure_warning1 = (Float.valueOf(pageData5.get(i+1).get("high_negative_pressure_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("high_negative_pressure_warning").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//高报负压2
									high_alarm_negative_pressure3 = Float.valueOf(pageData5.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
									//高报负压警示2
									high_negative_pressure_warning3 = Float.valueOf(pageData5.get(i).get("high_negative_pressure_warning").toString()).floatValue();
									//低报负压差2
									low_alarm_negative_pressure1 = (Float.valueOf(pageData5.get(i+1).get("low_alarm_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("low_alarm_negative_pressure").toString()).floatValue())/
									((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//低报负压2
									low_alarm_negative_pressure3 = Float.valueOf(pageData5.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
									//低报负压警示2
									low_negative_pressure_warning3 = Float.valueOf(pageData5.get(i).get("low_negative_pressure_warning").toString()).floatValue();
									//低报负压警示差2
									low_negative_pressure_warning1 = (Float.valueOf(pageData5.get(i+1).get("low_negative_pressure_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("low_negative_pressure_warning").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//uid_num
									uid_num = Integer.valueOf(pageData5.get(i+1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue();
									day_age = Integer.valueOf(pageData5.get(i-1).get("day_age").toString()).intValue();
									break;
								}else if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==pageData5.size()-1){
									//目标负压差
									set_negative_pressure = (Float.valueOf(pageData5.get(i).get("set_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_negative_pressure").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标负压
									set_negative_pressure2 = Float.valueOf(pageData5.get(i-1).get("set_negative_pressure").toString()).floatValue();
									//高报负压差
									high_alarm_negative_pressure = (Float.valueOf(pageData5.get(i).get("high_alarm_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_alarm_negative_pressure").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//高报负压警示差
									high_negative_pressure_warning = (Float.valueOf(pageData5.get(i).get("high_negative_pressure_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_negative_pressure_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									
									//高报负压
									high_alarm_negative_pressure2 = Float.valueOf(pageData5.get(i-1).get("high_alarm_negative_pressure").toString()).floatValue();
									//高报负压警示
									high_negative_pressure_warning2 = Float.valueOf(pageData5.get(i-1).get("high_negative_pressure_warning").toString()).floatValue();
									//低报负压差
									low_alarm_negative_pressure = (Float.valueOf(pageData5.get(i).get("low_alarm_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue())/
									((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//低报负压
									low_alarm_negative_pressure2 = Float.valueOf(pageData5.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue();
									//低报负压警示
									low_negative_pressure_warning2 = Float.valueOf(pageData5.get(i-1).get("low_negative_pressure_warning").toString()).floatValue();
									//低报负压警示差
									low_negative_pressure_warning = (Float.valueOf(pageData5.get(i).get("low_negative_pressure_warning").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_negative_pressure_warning").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									day_age = Integer.valueOf(pageData5.get(i-1).get("day_age").toString()).intValue();
									break;
								}
							}
						}else{
							for(int i=0;i<pageData5.size();i++){
								//计算耗水差、基值
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==0){
									//目标耗水1
									set_water_deprivation2 = Float.valueOf(pageData5.get(i).get("set_water_deprivation").toString()).floatValue();
									//高报耗水1
									high_water_deprivation2 = Float.valueOf(pageData5.get(i).get("high_water_deprivation").toString()).floatValue();
									//低报耗水1
									low_water_deprivation2 = Float.valueOf(pageData5.get(i).get("low_water_deprivation").toString()).floatValue();
									if(pageData5.size()!=1){
										//目标耗水差2
										set_water_deprivation1 = (Float.valueOf(pageData5.get(i+1).get("set_water_deprivation").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("set_water_deprivation").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//目标耗水2
										set_water_deprivation3 = Float.valueOf(pageData5.get(i).get("set_water_deprivation").toString()).floatValue();
										//高报耗水差2
										high_water_deprivation1 = (Float.valueOf(pageData5.get(i+1).get("high_water_deprivation").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("high_water_deprivation").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//高报耗水2
										high_water_deprivation3 = Float.valueOf(pageData5.get(i).get("high_water_deprivation").toString()).floatValue();
										//低报耗水差2
										low_water_deprivation1 = (Float.valueOf(pageData5.get(i+1).get("low_water_deprivation").toString()).floatValue() -
												Float.valueOf(pageData5.get(i).get("low_water_deprivation").toString()).floatValue())/
												((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
										//低报耗水2
										low_water_deprivation3 = Float.valueOf(pageData5.get(i).get("low_water_deprivation").toString()).floatValue();
										//uid_num
										uid_num = Integer.valueOf(pageData5.get(i+1).get("id").toString()).intValue();
										day_age2 = Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue();
									}
									break;
								}else
								if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i>0 && i<pageData5.size()-1){
									//目标耗水差1
									set_water_deprivation = (Float.valueOf(pageData5.get(i).get("set_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标耗水1
									set_water_deprivation2 = Float.valueOf(pageData5.get(i-1).get("set_water_deprivation").toString()).floatValue();
									//高报耗水差1
									high_water_deprivation = (Float.valueOf(pageData5.get(i).get("high_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//高报耗水1
									high_water_deprivation2 = Float.valueOf(pageData5.get(i-1).get("high_water_deprivation").toString()).floatValue();
									//低报耗水差1
									low_water_deprivation = (Float.valueOf(pageData5.get(i).get("low_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//低报耗水1
									low_water_deprivation2 = Float.valueOf(pageData5.get(i-1).get("low_water_deprivation").toString()).floatValue();
									//目标耗水差2
									set_water_deprivation1 = (Float.valueOf(pageData5.get(i+1).get("set_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("set_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标耗水2
									set_water_deprivation3 = Float.valueOf(pageData5.get(i).get("set_water_deprivation").toString()).floatValue();
									//高报耗水差2
									high_water_deprivation1 = (Float.valueOf(pageData5.get(i+1).get("high_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("high_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//高报耗水2
									high_water_deprivation3 = Float.valueOf(pageData5.get(i).get("high_water_deprivation").toString()).floatValue();
									//低报耗水差2
									low_water_deprivation1 = (Float.valueOf(pageData5.get(i+1).get("low_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData5.get(i).get("low_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pageData5.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//低报耗水2
									low_water_deprivation3 = Float.valueOf(pageData5.get(i).get("low_water_deprivation").toString()).floatValue();
									//uid_num
									uid_num = Integer.valueOf(pageData5.get(i+1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData5.get(i+1).get("day_age").toString()).intValue();
									day_age = Integer.valueOf(pageData5.get(i-1).get("day_age").toString()).intValue();
									break;
								}else if(pageData5.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
										pageData5.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
										pageData5.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==pageData5.size()-1){
									//目标耗水差
									set_water_deprivation = (Float.valueOf(pageData5.get(i).get("set_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("set_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//目标耗水
									set_water_deprivation2 = Float.valueOf(pageData5.get(i-1).get("set_water_deprivation").toString()).floatValue();
									//高报耗水差
									high_water_deprivation = (Float.valueOf(pageData5.get(i).get("high_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("high_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//高报耗水
									high_water_deprivation2 = Float.valueOf(pageData5.get(i-1).get("high_water_deprivation").toString()).floatValue();
									//低报耗水差
									low_water_deprivation = (Float.valueOf(pageData5.get(i).get("low_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData5.get(i-1).get("low_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData5.get(i-1).get("day_age").toString()).floatValue())*24);
									//低报耗水
									low_water_deprivation2 = Float.valueOf(pageData5.get(i-1).get("low_water_deprivation").toString()).floatValue();
									day_age = Integer.valueOf(pageData5.get(i-1).get("day_age").toString()).intValue();
									break;
								}
							}
						}
						
						PageData pd10 = new PageData();
						pd10.put("uid_num", uid_num);
						pd10.put("alarm_type", pd.get("alarm_type"));
						if(choose){
							pd10.put("day_age1", day_age);
						}
						if(choose2){
							if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2){
								pd10.put("day_age1", day_age);
							}else{
							pd10.put("day_age2", day_age2);
							}
						}
//						if(choose3){
//							pd10.put("day_age2", day_age2);
//							choose=true;
//						}
						alarmService.deleteSBDayageTempSub(pd10);

						int day_age3 = rling;

						List<PageData> list = new ArrayList<PageData>();
//					List<PageData> pageData6 = alarmService.selectSBDayageTempSubByCondition(pd3);
						if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==1){
							//温度修改
							for(int i=day_age;i<day_age3;i++){
								for(int j=1;j<=24;j++){
									PageData pd3 = new PageData();
									pd3.put("uid_num", pdID);
									pd3.put("create_person",userId);
									pd3.put("create_date", new Date());
									pd3.put("create_time", new Date());
									pd3.put("modify_person",userId);
									pd3.put("modify_date", new Date());
									pd3.put("modify_time", new Date());
									pd3.put("alarm_type", pd.get("alarm_type"));
									pd3.put("farmId", pd.get("farmId"));
									pd3.put("houseId", pd.get("houseId"));
									Date date = new Date();
									date.setMinutes(0);
									date.setSeconds(0);
									pd3.put("day_age", i+1);
									date.setHours(j);
									pd3.put("record_datetime", date);
									pd3.put("set_temp", set_temp2+set_temp*((i-day_age)*24+j));
									pd3.put("high_alarm_temp",high_alarm_temp2+high_alarm_temp*((i-day_age)*24+j));
									pd3.put("low_alarm_temp",low_alarm_temp2+low_alarm_temp*((i-day_age)*24+j));
									pd3.put("high_temp_warning",high_temp_warning2+high_temp_warning*((i-day_age)*24+j));
									pd3.put("low_temp_warning",low_temp_warning2+low_temp_warning*((i-day_age)*24+j));
									pd3.put("set_humidity", null);
									pd3.put("high_alarm_humidity", null);
									pd3.put("low_alarm_humidity", null);
									pd3.put("high_humidity_warning", null);
									pd3.put("low_humidity_warning", null);
									pd3.put("set_negative_pressure", null);
									pd3.put("high_alarm_negative_pressure", null);
									pd3.put("low_alarm_negative_pressure", null);
									pd3.put("high_negative_pressure_warning", null);
									pd3.put("low_negative_pressure_warning", null);
									pd3.put("set_lux",null);
									pd3.put("high_lux",null);
									pd3.put("low_lux",null);
									pd3.put("high_lux_warning",null);
									pd3.put("low_lux_warning",null);
									pd3.put("set_co2",null);
									pd3.put("high_alarm_co2",null);
									pd3.put("high_co2_warning",null);
									pd3.put("set_water_deprivation", null);
									pd3.put("high_water_deprivation",null);
									pd3.put("low_water_deprivation",null);
									pd3.put("is_start", "0");
									list.add(pd3);
//							alarmService.saveSBDayageTempSub(pd3);
								}
							}
							if(choose){
								for(int i=day_age3;i<Integer.valueOf(pd.get("dage").toString()).intValue();i++){
									for(int j=1;j<=24;j++){
										PageData pd3 = new PageData();
										pd3.put("uid_num", pdID);
										pd3.put("create_person",userId);
										pd3.put("create_date", new Date());
										pd3.put("create_time", new Date());
										pd3.put("modify_person",userId);
										pd3.put("modify_date", new Date());
										pd3.put("modify_time", new Date());
										pd3.put("alarm_type", pd.get("alarm_type"));
										pd3.put("farmId", pd.get("farmId"));
										pd3.put("houseId", pd.get("houseId"));
										Date date = new Date();
										date.setMinutes(0);
										date.setSeconds(0);
										pd3.put("day_age", i+1);
										date.setHours(j);
										pd3.put("record_datetime", date);
										pd3.put("set_temp", set_temp3);
										pd3.put("high_alarm_temp",high_alarm_temp3);
										pd3.put("low_alarm_temp",low_alarm_temp3);
										pd3.put("high_temp_warning",high_temp_warning3);
										pd3.put("low_temp_warning",low_temp_warning3);
										pd3.put("set_humidity", null);
										pd3.put("high_alarm_humidity", null);
										pd3.put("low_alarm_humidity", null);
										pd3.put("high_humidity_warning", null);
										pd3.put("low_humidity_warning", null);
										pd3.put("set_negative_pressure", null);
										pd3.put("high_alarm_negative_pressure", null);
										pd3.put("low_alarm_negative_pressure", null);
										pd3.put("high_negative_pressure_warning", null);
										pd3.put("low_negative_pressure_warning", null);
										pd3.put("set_lux",null);
										pd3.put("high_lux",null);
										pd3.put("low_lux",null);
										pd3.put("high_lux_warning",null);
										pd3.put("low_lux_warning",null);
										pd3.put("set_co2",null);
										pd3.put("high_alarm_co2",null);
										pd3.put("high_co2_warning",null);
										pd3.put("set_water_deprivation", null);
										pd3.put("high_water_deprivation",null);
										pd3.put("low_water_deprivation",null);
										pd3.put("is_start", "0");
										list.add(pd3);
//								alarmService.saveSBDayageTempSub(pd3);
									}
								}
							}
							alarmService.saveSBDayageTempSub(list);
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2){
							//负压修改
							for(int i=day_age;i<day_age3;i++){
								for(int j=1;j<=24;j++){
									PageData pd3 = new PageData();
									pd3.put("uid_num", pdID);
									pd3.put("create_person",userId);
									pd3.put("create_date", new Date());
									pd3.put("create_time", new Date());
									pd3.put("modify_person",userId);
									pd3.put("modify_date", new Date());
									pd3.put("modify_time", new Date());
									pd3.put("alarm_type", pd.get("alarm_type"));
									pd3.put("farmId", pd.get("farmId"));
									pd3.put("houseId", pd.get("houseId"));
									Date date = new Date();
									date.setMinutes(0);
									date.setSeconds(0);
									pd3.put("day_age", i+1);
									date.setHours(j);
									pd3.put("record_datetime", date);
									pd3.put("set_temp", null);
									pd3.put("high_alarm_temp",null);
									pd3.put("low_alarm_temp",null);
									pd3.put("high_temp_warning",null);
									pd3.put("low_temp_warning",null);
									pd3.put("set_humidity", null);
									pd3.put("high_alarm_humidity", null);
									pd3.put("low_alarm_humidity", null);
									pd3.put("high_humidity_warning", null);
									pd3.put("low_humidity_warning", null);
									pd3.put("set_negative_pressure", null);
									pd3.put("high_alarm_negative_pressure", null);
									pd3.put("low_alarm_negative_pressure", null);
									pd3.put("high_negative_pressure_warning", null);
									pd3.put("low_negative_pressure_warning", null);
//							pd3.put("set_negative_pressure", set_negative_pressure2+set_negative_pressure*(i+1));
//							pd3.put("set_lux",set_lux2+set_lux*((i-day_age)*24+j));
//							pd3.put("high_lux",high_lux2+high_lux*((i-day_age)*24+j));
//							pd3.put("low_lux",low_lux2+low_lux*((i-day_age)*24+j));
									pd3.put("set_lux",set_lux3);
									pd3.put("high_lux",high_lux3);
									pd3.put("low_lux",low_lux3);
									pd3.put("high_lux_warning",high_lux_warning3);
									pd3.put("low_lux_warning",low_lux_warning3);
//									pd3.put("set_lux",set_lux2+set_lux*((day_age3-1-day_age)*24+24));
//									pd3.put("high_lux",high_lux2+high_lux*((day_age3-1-day_age)*24+24));
//									pd3.put("low_lux",low_lux2+low_lux*((day_age3-1-day_age)*24+24));
//									pd3.put("high_lux_warning",high_lux_warning2+high_lux_warning*((day_age3-1-day_age)*24+24));
//									pd3.put("low_lux_warning",low_lux_warning2+low_lux_warning*((day_age3-1-day_age)*24+24));
									pd3.put("set_co2",null);
									pd3.put("high_alarm_co2",null);
									pd3.put("high_co2_warning",null);
									pd3.put("set_water_deprivation", null);
									pd3.put("high_water_deprivation",null);
									pd3.put("low_water_deprivation",null);
									if(startTime<=j && j<endTime){
										pd3.put("is_start","0");
									}else{
										pd3.put("is_start","1");
									}
									list.add(pd3);
//							alarmService.saveSBDayageTempSub(pd3);
								}
							}
							if(choose ){//&& day_age3 == Integer.valueOf(pageData.get(pageData.size()-1).get("day_age").toString()).intValue()*7
								for(int i=day_age3;i<Integer.valueOf(pd.get("dage").toString()).intValue();i++){
									for(int j=1;j<=24;j++){
										PageData pd3 = new PageData();
										pd3.put("uid_num", pdID);
										pd3.put("create_person",userId);
										pd3.put("create_date", new Date());
										pd3.put("create_time", new Date());
										pd3.put("modify_person",userId);
										pd3.put("modify_date", new Date());
										pd3.put("modify_time", new Date());
										pd3.put("alarm_type", pd.get("alarm_type"));
										pd3.put("farmId", pd.get("farmId"));
										pd3.put("houseId", pd.get("houseId"));
										Date date = new Date();
										date.setMinutes(0);
										date.setSeconds(0);
										pd3.put("day_age", i+1);
										date.setHours(j);
										pd3.put("record_datetime", date);
										pd3.put("set_temp", null);
										pd3.put("high_alarm_temp",null);
										pd3.put("low_alarm_temp",null);
										pd3.put("high_temp_warning",null);
										pd3.put("low_temp_warning",null);
										pd3.put("set_humidity", null);
										pd3.put("high_alarm_humidity", null);
										pd3.put("low_alarm_humidity", null);
										pd3.put("high_humidity_warning", null);
										pd3.put("low_humidity_warning", null);
										pd3.put("set_negative_pressure", null);
										pd3.put("high_alarm_negative_pressure", null);
										pd3.put("low_alarm_negative_pressure", null);
										pd3.put("high_negative_pressure_warning", null);
										pd3.put("low_negative_pressure_warning", null);
										pd3.put("set_lux",set_lux3);
										pd3.put("high_lux",high_lux3);
										pd3.put("low_lux",low_lux3);
										pd3.put("high_lux_warning",high_lux_warning3);
										pd3.put("low_lux_warning",low_lux_warning3);
										pd3.put("set_co2",null);
										pd3.put("high_alarm_co2",null);
										pd3.put("high_co2_warning",null);
										pd3.put("set_water_deprivation", null);
										pd3.put("high_water_deprivation",null);
										pd3.put("low_water_deprivation",null);
//										if(startTime<=j && j<endTime){
//											pd3.put("is_start","0");
//										}else{
											pd3.put("is_start","1");
//										}
										list.add(pd3);
									}
								}
							}
							alarmService.saveSBDayageTempSub(list);
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==3){
							//二氧化碳修改
							for(int i=day_age;i<day_age3;i++){
								for(int j=1;j<=24;j++){
									PageData pd3 = new PageData();
									pd3.put("uid_num", pdID);
									pd3.put("create_person",userId);
									pd3.put("create_date", new Date());
									pd3.put("create_time", new Date());
									pd3.put("modify_person",userId);
									pd3.put("modify_date", new Date());
									pd3.put("modify_time", new Date());
									pd3.put("alarm_type", pd.get("alarm_type"));
									pd3.put("farmId", pd.get("farmId"));
									pd3.put("houseId", pd.get("houseId"));
									Date date = new Date();
									date.setMinutes(0);
									date.setSeconds(0);
									pd3.put("day_age", i+1);
									date.setHours(j);
									pd3.put("record_datetime", date);
									pd3.put("set_temp", null);
									pd3.put("high_alarm_temp",null);
									pd3.put("low_alarm_temp",null);
									pd3.put("high_temp_warning",null);
									pd3.put("low_temp_warning",null);
									pd3.put("set_humidity", null);
									pd3.put("high_alarm_humidity", null);
									pd3.put("low_alarm_humidity", null);
									pd3.put("high_humidity_warning", null);
									pd3.put("low_humidity_warning", null);
									pd3.put("set_negative_pressure", null);
									pd3.put("high_alarm_negative_pressure", null);
									pd3.put("low_alarm_negative_pressure", null);
									pd3.put("high_negative_pressure_warning", null);
									pd3.put("low_negative_pressure_warning", null);
									pd3.put("set_lux",null);
									pd3.put("high_lux",null);
									pd3.put("low_lux",null);
									pd3.put("high_lux_warning",null);
									pd3.put("low_lux_warning",null);
									pd3.put("set_co2", set_co22+set_co2*((i-day_age)*24+j));
									pd3.put("high_alarm_co2",high_alarm_co22+high_alarm_co2*((i-day_age)*24+j));
									pd3.put("high_co2_warning",high_co2_warning2+high_co2_warning*((i-day_age)*24+j));
//							pd3.put("low_alarm_co2",low_alarm_co22+low_alarm_co2*(i+1));
									pd3.put("set_water_deprivation", null);
									pd3.put("high_water_deprivation",null);
									pd3.put("low_water_deprivation",null);
									pd3.put("is_start", "0");
									list.add(pd3);
//							alarmService.saveSBDayageTempSub(pd3);
								}
							}
							alarmService.saveSBDayageTempSub(list);
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==5){
							//湿度修改
							for(int i=day_age;i<day_age3;i++){
								for(int j=1;j<=24;j++){
									PageData pd3 = new PageData();
									pd3.put("uid_num", pdID);
									pd3.put("create_person",userId);
									pd3.put("create_date", new Date());
									pd3.put("create_time", new Date());
									pd3.put("modify_person",userId);
									pd3.put("modify_date", new Date());
									pd3.put("modify_time", new Date());
									pd3.put("alarm_type", pd.get("alarm_type"));
									pd3.put("farmId", pd.get("farmId"));
									pd3.put("houseId", pd.get("houseId"));
									Date date = new Date();
									date.setMinutes(0);
									date.setSeconds(0);
									pd3.put("day_age", i+1);
									date.setHours(j);
									pd3.put("record_datetime", date);
									pd3.put("set_temp", null);
									pd3.put("high_alarm_temp",null);
									pd3.put("low_alarm_temp",null);
									pd3.put("high_temp_warning",null);
									pd3.put("low_temp_warning",null);
									pd3.put("set_humidity", set_humidity2+set_humidity*((i-day_age)*24+j));
									pd3.put("high_alarm_humidity", high_alarm_humidity2+high_alarm_humidity*((i-day_age)*24+j));
									pd3.put("low_alarm_humidity", low_alarm_humidity2+low_alarm_humidity*((i-day_age)*24+j));
									pd3.put("high_humidity_warning", high_humidity_warning2+high_humidity_warning*((i-day_age)*24+j));
									pd3.put("low_humidity_warning", low_humidity_warning2+low_humidity_warning*((i-day_age)*24+j));
									pd3.put("set_negative_pressure", null);
									pd3.put("high_alarm_negative_pressure", null);
									pd3.put("low_alarm_negative_pressure", null);
									pd3.put("high_negative_pressure_warning", null);
									pd3.put("low_negative_pressure_warning", null);
									pd3.put("set_lux",null);
									pd3.put("high_lux",null);
									pd3.put("low_lux",null);
									pd3.put("high_lux_warning",null);
									pd3.put("low_lux_warning",null);
									pd3.put("set_co2",null);
									pd3.put("high_alarm_co2",null);
									pd3.put("high_co2_warning",null);
									pd3.put("set_water_deprivation", null);
									pd3.put("high_water_deprivation",null);
									pd3.put("low_water_deprivation",null);
									pd3.put("is_start", "0");
									list.add(pd3);
//							alarmService.saveSBDayageTempSub(pd3);
								}
							}
							if(choose){
								for(int i=day_age3;i<Integer.valueOf(pd.get("dage").toString()).intValue();i++){
									for(int j=1;j<=24;j++){
										PageData pd3 = new PageData();
										pd3.put("uid_num", pdID);
										pd3.put("create_person",userId);
										pd3.put("create_date", new Date());
										pd3.put("create_time", new Date());
										pd3.put("modify_person",userId);
										pd3.put("modify_date", new Date());
										pd3.put("modify_time", new Date());
										pd3.put("alarm_type", pd.get("alarm_type"));
										pd3.put("farmId", pd.get("farmId"));
										pd3.put("houseId", pd.get("houseId"));
										Date date = new Date();
										date.setMinutes(0);
										date.setSeconds(0);
										pd3.put("day_age", i+1);
										date.setHours(j);
										pd3.put("record_datetime", date);
										pd3.put("set_temp", null);
										pd3.put("high_alarm_temp",null);
										pd3.put("low_alarm_temp",null);
										pd3.put("high_temp_warning",null);
										pd3.put("low_temp_warning",null);
										pd3.put("set_humidity", set_humidity3);
										pd3.put("high_alarm_humidity", high_alarm_humidity3);
										pd3.put("low_alarm_humidity", low_alarm_humidity3);
										pd3.put("high_humidity_warning", high_humidity_warning3);
										pd3.put("low_humidity_warning", low_humidity_warning3);
										pd3.put("set_negative_pressure", null);
										pd3.put("high_alarm_negative_pressure", null);
										pd3.put("low_alarm_negative_pressure", null);
										pd3.put("high_negative_pressure_warning", null);
										pd3.put("low_negative_pressure_warning", null);
										pd3.put("set_lux",null);
										pd3.put("high_lux",null);
										pd3.put("low_lux",null);
										pd3.put("high_lux_warning",null);
										pd3.put("low_lux_warning",null);
										pd3.put("set_co2",null);
										pd3.put("high_alarm_co2",null);
										pd3.put("high_co2_warning",null);
										pd3.put("set_water_deprivation", null);
										pd3.put("high_water_deprivation",null);
										pd3.put("low_water_deprivation",null);
										pd3.put("is_start", "0");
										list.add(pd3);
//								alarmService.saveSBDayageTempSub(pd3);
									}
								}
							}
							alarmService.saveSBDayageTempSub(list);
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==6){
							//负压修改
							for(int i=day_age;i<day_age3;i++){
								for(int j=1;j<=24;j++){
									PageData pd3 = new PageData();
									pd3.put("uid_num", pdID);
									pd3.put("create_person",userId);
									pd3.put("create_date", new Date());
									pd3.put("create_time", new Date());
									pd3.put("modify_person",userId);
									pd3.put("modify_date", new Date());
									pd3.put("modify_time", new Date());
									pd3.put("alarm_type", pd.get("alarm_type"));
									pd3.put("farmId", pd.get("farmId"));
									pd3.put("houseId", pd.get("houseId"));
									Date date = new Date();
									date.setMinutes(0);
									date.setSeconds(0);
									pd3.put("day_age", i+1);
									date.setHours(j);
									pd3.put("record_datetime", date);
									pd3.put("set_temp", null);
									pd3.put("high_alarm_temp",null);
									pd3.put("low_alarm_temp",null);
									pd3.put("high_temp_warning",null);
									pd3.put("low_temp_warning",null);
									pd3.put("set_humidity", null);
									pd3.put("high_alarm_humidity", null);
									pd3.put("low_alarm_humidity", null);
									pd3.put("high_humidity_warning", null);
									pd3.put("low_humidity_warning", null);
									pd3.put("set_negative_pressure", set_negative_pressure2+set_negative_pressure*((i-day_age)*24+j));
									pd3.put("high_alarm_negative_pressure", high_alarm_negative_pressure2+high_alarm_negative_pressure*((i-day_age)*24+j));
									pd3.put("low_alarm_negative_pressure", low_alarm_negative_pressure2+low_alarm_negative_pressure*((i-day_age)*24+j));
									pd3.put("high_negative_pressure_warning", high_negative_pressure_warning2+high_negative_pressure_warning*((i-day_age)*24+j));
									pd3.put("low_negative_pressure_warning", low_negative_pressure_warning2+low_negative_pressure_warning*((i-day_age)*24+j));
									pd3.put("set_lux",null);
									pd3.put("high_lux",null);
									pd3.put("low_lux",null);
									pd3.put("high_lux_warning",null);
									pd3.put("low_lux_warning",null);
									pd3.put("set_co2", null);
									pd3.put("high_alarm_co2",null);
									pd3.put("high_co2_warning",null);
//							pd3.put("low_alarm_co2",low_alarm_co22+low_alarm_co2*(i+1));
									pd3.put("set_water_deprivation", null);
									pd3.put("high_water_deprivation",null);
									pd3.put("low_water_deprivation",null);
									pd3.put("is_start", "0");
									list.add(pd3);
//							alarmService.saveSBDayageTempSub(pd3);
								}
							}
							alarmService.saveSBDayageTempSub(list);
						}else{
							//耗水修改
							for(int i=day_age;i<day_age3;i++){
								for(int j=1;j<=24;j++){
									PageData pd3 = new PageData();
									pd3.put("uid_num", pdID);
									pd3.put("create_person",userId);
									pd3.put("create_date", new Date());
									pd3.put("create_time", new Date());
									pd3.put("modify_person",userId);
									pd3.put("modify_date", new Date());
									pd3.put("modify_time", new Date());
									pd3.put("alarm_type", pd.get("alarm_type"));
									pd3.put("farmId", pd.get("farmId"));
									pd3.put("houseId", pd.get("houseId"));
									Date date = new Date();
									date.setMinutes(0);
									date.setSeconds(0);
									pd3.put("day_age", i+1);
									date.setHours(j);
									pd3.put("record_datetime", date);
									pd3.put("set_temp", null);
									pd3.put("high_alarm_temp",null);
									pd3.put("low_alarm_temp",null);
									pd3.put("high_temp_warning",null);
									pd3.put("low_temp_warning",null);
									pd3.put("set_lux",null);
									pd3.put("high_lux",null);
									pd3.put("low_lux",null);
									pd3.put("high_lux_warning",null);
									pd3.put("low_lux_warning",null);
									pd3.put("set_co2",null);
									pd3.put("high_alarm_co2",null);
									pd3.put("high_co2_warning",null);
									pd3.put("set_water_deprivation", set_water_deprivation2+set_water_deprivation*((i-day_age)*24+j));
									pd3.put("high_water_deprivation",high_water_deprivation2+high_water_deprivation*((i-day_age)*24+j));
									pd3.put("low_water_deprivation",low_water_deprivation2+low_water_deprivation*((i-day_age)*24+j));
									pd3.put("is_start", "0");
									list.add(pd3);
//							alarmService.saveSBDayageTempSub(pd3);
								}
							}
							alarmService.saveSBDayageTempSub(list);
						}
                        
						if(choose2){
						PageData pd4 = new PageData();
						pd4.put("uid_num",uid_num);
						pd4.put("alarm_type", pd.get("alarm_type"));
						if(choose){
							pd4.put("day_age1", day_age);
						}
						
						if(choose3){
							pd4.put("day_age2", day_age2);
						}else{
							if(Integer.valueOf(pd.get("alarm_type").toString()).intValue() == 2){
								pd4.put("day_age1", day_age);
							}else{
							pd4.put("day_age2", day_age2);
							}
						}
						alarmService.deleteSBDayageTempSub(pd4);
						}
						List<PageData> list2 = new ArrayList<PageData>();
//					   List<PageData> pageData7 = alarmService.selectSBDayageTempSubByCondition(pd4);
						if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==1 && choose2){
							//修改相邻记录的温度
							for(int i=day_age3;i<day_age2;i++){
								for(int j=1;j<=24;j++){
									PageData pd5 = new PageData();
									pd5.put("uid_num",pdID);
									pd5.put("create_person",userId);
									pd5.put("create_date", new Date());
									pd5.put("create_time", new Date());
									pd5.put("modify_person",userId);
									pd5.put("modify_date", new Date());
									pd5.put("modify_time", new Date());
									pd5.put("alarm_type", pd.get("alarm_type"));
									pd5.put("farmId", pd.get("farmId"));
									pd5.put("houseId", pd.get("houseId"));
									Date date2 = new Date();
									date2.setMinutes(0);
									date2.setSeconds(0);
									pd5.put("day_age", i+1);
									date2.setHours(j);
									pd5.put("record_datetime", date2);
									pd5.put("set_temp", set_temp3+set_temp1*(i+1));
									pd5.put("high_alarm_temp",high_alarm_temp3+high_alarm_temp1*(i+1));
									pd5.put("low_alarm_temp",low_alarm_temp3+low_alarm_temp1*(i+1));
									pd5.put("high_temp_warning",high_temp_warning3+high_temp_warning1*(i+1));
									pd5.put("low_temp_warning",low_temp_warning3+low_temp_warning1*(i+1));
									pd5.put("set_humidity", null);
									pd5.put("high_alarm_humidity", null);
									pd5.put("low_alarm_humidity", null);
									pd5.put("high_humidity_warning", null);
									pd5.put("low_humidity_warning", null);
									pd5.put("set_negative_pressure", null);
									pd5.put("high_alarm_negative_pressure", null);
									pd5.put("low_alarm_negative_pressure", null);
									pd5.put("high_negative_pressure_warning", null);
									pd5.put("low_negative_pressure_warning", null);
									pd5.put("set_lux",null);
									pd5.put("high_lux",null);
									pd5.put("low_lux",null);
									pd5.put("high_lux_warning",null);
									pd5.put("low_lux_warning",null);
									pd5.put("set_co2",null);
									pd5.put("high_alarm_co2",null);
									pd5.put("high_co2_warning",null);
									pd5.put("set_water_deprivation", null);
									pd5.put("high_water_deprivation",null);
									pd5.put("low_water_deprivation",null);
									pd5.put("is_start", "0");
									list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd4);
								}
							}
							alarmService.saveSBDayageTempSub(list2);
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2 && choose2){
							//修改相邻记录的负压
							for(int i=day_age3;i<day_age2-7;i++){
								for(int j=1;j<=24;j++){
									PageData pd5 = new PageData();
									pd5.put("uid_num",pdID);
									pd5.put("create_person",userId);
									pd5.put("create_date", new Date());
									pd5.put("create_time", new Date());
									pd5.put("modify_person",userId);
									pd5.put("modify_date", new Date());
									pd5.put("modify_time", new Date());
									pd5.put("alarm_type", pd.get("alarm_type"));
									pd5.put("farmId", pd.get("farmId"));
									pd5.put("houseId", pd.get("houseId"));
									Date date2 = new Date();
									date2.setMinutes(0);
									date2.setSeconds(0);
									pd5.put("day_age", i+1);
									date2.setHours(j);
									pd5.put("record_datetime", date2);
									pd5.put("set_temp", null);
									pd5.put("high_alarm_temp",null);
									pd5.put("low_alarm_temp",null);
									pd5.put("high_temp_warning",null);
									pd5.put("low_temp_warning",null);
									pd5.put("set_humidity", null);
									pd5.put("high_alarm_humidity", null);
									pd5.put("low_alarm_humidity", null);
									pd5.put("high_humidity_warning", null);
									pd5.put("low_humidity_warning", null);
									pd5.put("set_negative_pressure", null);
									pd5.put("high_alarm_negative_pressure", null);
									pd5.put("low_alarm_negative_pressure", null);
									pd5.put("high_negative_pressure_warning", null);
									pd5.put("low_negative_pressure_warning", null);
//								pd4.put("set_negative_pressure", set_negative_pressure3+set_negative_pressure1*(i+1));
//								pd5.put("set_lux",set_lux3+set_lux1*(i+1));
//								pd5.put("high_lux",high_lux3+high_lux1*(i+1));
//								pd5.put("low_lux",low_lux3+low_lux1*(i+1));
//									pd5.put("set_lux",set_lux2+set_lux*((day_age3-1-day_age)*24+24));
//									pd5.put("high_lux",high_lux2+high_lux*((day_age3-1-day_age)*24+24));
//									pd5.put("low_lux",low_lux2+low_lux*((day_age3-1-day_age)*24+24));
//									pd5.put("high_lux_warning",high_lux_warning2+high_lux_warning*((day_age3-1-day_age)*24+24));
//									pd5.put("low_lux_warning",low_lux_warning2+low_lux_warning*((day_age3-1-day_age)*24+24));
									pd5.put("set_lux",set_lux3);
									pd5.put("high_lux",high_lux3);
									pd5.put("low_lux",low_lux3);
									pd5.put("high_lux_warning",high_lux_warning3);
									pd5.put("low_lux_warning",low_lux_warning3);
//									pd5.put("set_lux",set_lux3+set_lux1*day_age2);
//									pd5.put("high_lux",high_lux3+high_lux1*day_age2);
//									pd5.put("low_lux",low_lux3+low_lux1*day_age2);
//									pd5.put("high_lux_warning",high_lux_warning3+high_lux_warning1*day_age2);
//									pd5.put("low_lux_warning",low_lux_warning3+low_lux_warning1*day_age2);
									pd5.put("set_co2",null);
									pd5.put("high_alarm_co2",null);
									pd5.put("high_co2_warning",null);
									pd5.put("set_water_deprivation", null);
									pd5.put("high_water_deprivation",null);
									pd5.put("low_water_deprivation",null);
									if(startTime<=j && j<endTime){
										pd5.put("is_start","0");
									}else{
										pd5.put("is_start","1");
									}
									list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd5);
								}
							}
							alarmService.saveSBDayageTempSub(list2);
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==3 && high_alarm_co21 != 0 ){
							//修改相邻记录的二氧化碳
							for(int i=day_age3;i<day_age2;i++){
								for(int j=1;j<=24;j++){
									PageData pd5 = new PageData();
									pd5.put("uid_num",uid_num);
									pd5.put("create_person",userId);
									pd5.put("create_date", new Date());
									pd5.put("create_time", new Date());
									pd5.put("modify_person",userId);
									pd5.put("modify_date", new Date());
									pd5.put("modify_time", new Date());
									pd5.put("alarm_type", pd.get("alarm_type"));
									pd5.put("farmId", pd.get("farmId"));
									pd5.put("houseId", pd.get("houseId"));
									Date date2 = new Date();
									date2.setMinutes(0);
									date2.setSeconds(0);
									pd5.put("day_age", i+1);
									date2.setHours(j);
									pd5.put("record_datetime", date2);
									pd5.put("set_temp", null);
									pd5.put("high_alarm_temp",null);
									pd5.put("low_alarm_temp",null);
									pd5.put("high_temp_warning",null);
									pd5.put("low_temp_warning",null);
									pd5.put("set_humidity", null);
									pd5.put("high_alarm_humidity", null);
									pd5.put("low_alarm_humidity", null);
									pd5.put("high_humidity_warning", null);
									pd5.put("low_humidity_warning", null);
									pd5.put("set_negative_pressure", null);
									pd5.put("high_alarm_negative_pressure", null);
									pd5.put("low_alarm_negative_pressure", null);
									pd5.put("high_negative_pressure_warning", null);
									pd5.put("low_negative_pressure_warning", null);
									pd5.put("set_lux",null);
									pd5.put("high_lux",null);
									pd5.put("low_lux",null);
									pd5.put("high_lux_warning",null);
									pd5.put("low_lux_warning",null);
									pd5.put("set_co2", set_co23+set_co21*(i+1));
									pd5.put("high_alarm_co2",high_alarm_co23+high_alarm_co21*(i+1));
									pd5.put("high_co2_warning",high_co2_warning3+high_co2_warning1*(i+1));
//								pd4.put("low_alarm_co2",low_alarm_co23+low_alarm_co21*(i+1));
									pd5.put("set_water_deprivation", null);
									pd5.put("high_water_deprivation",null);
									pd5.put("low_water_deprivation",null);
									pd5.put("is_start", "0");
									list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd4);
								}
							}
							alarmService.saveSBDayageTempSub(list2);
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==5 && choose2){
							//修改相邻记录的湿度
							for(int i=day_age3;i<day_age2;i++){
								for(int j=1;j<=24;j++){
									PageData pd5 = new PageData();
									pd5.put("uid_num",pdID);
									pd5.put("create_person",userId);
									pd5.put("create_date", new Date());
									pd5.put("create_time", new Date());
									pd5.put("modify_person",userId);
									pd5.put("modify_date", new Date());
									pd5.put("modify_time", new Date());
									pd5.put("alarm_type", pd.get("alarm_type"));
									pd5.put("farmId", pd.get("farmId"));
									pd5.put("houseId", pd.get("houseId"));
									Date date2 = new Date();
									date2.setMinutes(0);
									date2.setSeconds(0);
									pd5.put("day_age", i+1);
									date2.setHours(j);
									pd5.put("record_datetime", date2);
									pd5.put("set_temp", null);
									pd5.put("high_alarm_temp",null);
									pd5.put("low_alarm_temp",null);
									pd5.put("high_temp_warning",null);
									pd5.put("low_temp_warning",null);
									pd5.put("set_humidity", set_humidity3+set_humidity1*(i+1));
									pd5.put("high_alarm_humidity", high_alarm_humidity3+high_alarm_humidity1*(i+1));
									pd5.put("low_alarm_humidity", low_alarm_humidity3+low_alarm_humidity1*(i+1));
									pd5.put("high_humidity_warning", high_humidity_warning3+high_humidity_warning1*(i+1));
									pd5.put("low_humidity_warning", low_humidity_warning3+low_humidity_warning1*(i+1));
									pd5.put("set_negative_pressure", null);
									pd5.put("high_alarm_negative_pressure", null);
									pd5.put("low_alarm_negative_pressure", null);
									pd5.put("high_negative_pressure_warning", null);
									pd5.put("low_negative_pressure_warning", null);
									pd5.put("set_lux",null);
									pd5.put("high_lux",null);
									pd5.put("low_lux",null);
									pd5.put("high_lux_warning",null);
									pd5.put("low_lux_warning",null);
									pd5.put("set_co2",null);
									pd5.put("high_alarm_co2",null);
									pd5.put("high_co2_warning",null);
									pd5.put("set_water_deprivation", null);
									pd5.put("high_water_deprivation",null);
									pd5.put("low_water_deprivation",null);
									pd5.put("is_start", "0");
									list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd4);
								}
							}
							alarmService.saveSBDayageTempSub(list2);
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==6 && high_alarm_negative_pressure1 != 0 ){
							//修改相邻记录的负压
							for(int i=day_age3;i<day_age2;i++){
								for(int j=1;j<=24;j++){
									PageData pd5 = new PageData();
									pd5.put("uid_num",uid_num);
									pd5.put("create_person",userId);
									pd5.put("create_date", new Date());
									pd5.put("create_time", new Date());
									pd5.put("modify_person",userId);
									pd5.put("modify_date", new Date());
									pd5.put("modify_time", new Date());
									pd5.put("alarm_type", pd.get("alarm_type"));
									pd5.put("farmId", pd.get("farmId"));
									pd5.put("houseId", pd.get("houseId"));
									Date date2 = new Date();
									date2.setMinutes(0);
									date2.setSeconds(0);
									pd5.put("day_age", i+1);
									date2.setHours(j);
									pd5.put("record_datetime", date2);
									pd5.put("set_temp", null);
									pd5.put("high_alarm_temp",null);
									pd5.put("low_alarm_temp",null);
									pd5.put("high_temp_warning",null);
									pd5.put("low_temp_warning",null);
									pd5.put("set_humidity", null);
									pd5.put("high_alarm_humidity", null);
									pd5.put("low_alarm_humidity", null);
									pd5.put("high_humidity_warning", null);
									pd5.put("low_humidity_warning", null);
									pd5.put("set_negative_pressure", set_negative_pressure3+set_negative_pressure1*(i+1));
									pd5.put("high_alarm_negative_pressure", high_alarm_negative_pressure3+high_alarm_negative_pressure1*(i+1));
									pd5.put("low_alarm_negative_pressure", low_alarm_negative_pressure3+low_alarm_negative_pressure1*(i+1));
									pd5.put("high_negative_pressure_warning", high_negative_pressure_warning3+high_negative_pressure_warning1*(i+1));
									pd5.put("low_negative_pressure_warning", low_negative_pressure_warning3+low_negative_pressure_warning1*(i+1));
									pd5.put("set_lux",null);
									pd5.put("high_lux",null);
									pd5.put("low_lux",null);
									pd5.put("high_lux_warning",null);
									pd5.put("low_lux_warning",null);
									pd5.put("set_co2", null);
									pd5.put("high_alarm_co2",null);
									pd5.put("high_co2_warning",null);
//								pd4.put("low_alarm_co2",low_alarm_co23+low_alarm_co21*(i+1));
									pd5.put("set_water_deprivation", null);
									pd5.put("high_water_deprivation",null);
									pd5.put("low_water_deprivation",null);
									pd5.put("is_start", "0");
									list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd4);
								}
							}
							alarmService.saveSBDayageTempSub(list2);
						}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==4 &&(set_water_deprivation1 != 0 || high_water_deprivation1 != 0 || low_water_deprivation1 != 0)){
							//修改相邻记录的耗水
							for(int i=day_age3;i<day_age2;i++){
								for(int j=1;j<=24;j++){
									PageData pd5 = new PageData();
									pd5.put("uid_num",uid_num);
									pd5.put("create_person",userId);
									pd5.put("create_date", new Date());
									pd5.put("create_time", new Date());
									pd5.put("modify_person",userId);
									pd5.put("modify_date", new Date());
									pd5.put("modify_time", new Date());
									pd5.put("alarm_type", pd.get("alarm_type"));
									pd5.put("farmId", pd.get("farmId"));
									pd5.put("houseId", pd.get("houseId"));
									Date date2 = new Date();
									date2.setMinutes(0);
									date2.setSeconds(0);
									pd5.put("day_age", i+1);
									date2.setHours(j);
									pd5.put("record_datetime", date2);
									pd5.put("set_temp", null);
									pd5.put("high_alarm_temp",null);
									pd5.put("low_alarm_temp",null);
									pd5.put("set_lux",null);
									pd5.put("high_lux",null);
									pd5.put("low_lux",null);
									pd5.put("set_co2",null);
									pd5.put("high_alarm_co2",null);
									pd5.put("set_water_deprivation", set_water_deprivation3+set_water_deprivation1*(i+1));
									pd5.put("high_water_deprivation",high_water_deprivation3+high_water_deprivation1*(i+1));
									pd5.put("low_water_deprivation",low_water_deprivation3+low_water_deprivation1*(i+1));
									pd5.put("is_start", "0");
									list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd4);
								}
							}
							alarmService.saveSBDayageTempSub(list2);
						}

					}
				}

				if(g==0){
					alarmService.saveSBDayageSettingSub(pd);
					pd.put("alarm_operation", "新增");
					alarmService.saveSbDayageSettingSubHis(pd);
					pdID = Integer.valueOf(alarmService.selectByCondition3(pd).get(0).get("id").toString()).intValue();

					float set_temp=0,high_alarm_temp=0,high_temp_warning=0,low_alarm_temp=0,low_temp_warning=0,
							set_temp1=0,high_alarm_temp1=0,high_temp_warning1=0,low_alarm_temp1=0,low_temp_warning1=0,
							set_temp2=0,high_alarm_temp2=0,high_temp_warning2=0,low_alarm_temp2=0,low_temp_warning2=0,
							set_temp3=0,high_alarm_temp3=0,high_temp_warning3=0,low_alarm_temp3=0,low_temp_warning3=0,
							set_lux=0,high_lux=0,high_lux_warning=0,set_lux1=0,high_lux1=0,high_lux_warning1=0,
							set_lux2=0,high_lux2=0,high_lux_warning2=0,set_lux3=0,high_lux3=0,high_lux_warning3=0,
							low_lux=0,low_lux_warning=0,low_lux1=0,low_lux_warning1=0,low_lux2=0,low_lux_warning2=0,low_lux3=0,low_lux_warning3=0,
							set_co2=0,high_alarm_co2=0,high_co2_warning=0,set_co21=0,high_alarm_co21=0,high_co2_warning1=0,set_co22=0,high_alarm_co22=0,high_co2_warning2=0,
							set_co23=0,high_alarm_co23=0,high_co2_warning3=0,
							set_humidity=0,high_alarm_humidity=0,high_humidity_warning=0,low_alarm_humidity=0,low_humidity_warning=0,
							set_humidity1=0,high_alarm_humidity1=0,high_humidity_warning1=0,low_alarm_humidity1=0,low_humidity_warning1=0,
							set_humidity2=0,high_alarm_humidity2=0,high_humidity_warning2=0,low_alarm_humidity2=0,low_humidity_warning2=0,
							set_humidity3=0,high_alarm_humidity3=0,high_humidity_warning3=0,low_alarm_humidity3=0,low_humidity_warning3=0,
							set_negative_pressure=0,high_alarm_negative_pressure=0,high_negative_pressure_warning=0,low_alarm_negative_pressure=0,low_negative_pressure_warning=0,
							set_negative_pressure1=0,high_alarm_negative_pressure1=0,high_negative_pressure_warning1=0,low_alarm_negative_pressure1=0,low_negative_pressure_warning1=0,
							set_negative_pressure2=0,high_alarm_negative_pressure2=0,high_negative_pressure_warning2=0,low_alarm_negative_pressure2=0,low_negative_pressure_warning2=0,
							set_negative_pressure3=0,high_alarm_negative_pressure3=0,high_negative_pressure_warning3=0,low_alarm_negative_pressure3=0,low_negative_pressure_warning3=0,
							set_water_deprivation=0,high_water_deprivation=0,low_water_deprivation=0,set_water_deprivation1=0,high_water_deprivation1=0,low_water_deprivation1=0,
							set_water_deprivation2=0,high_water_deprivation2=0,low_water_deprivation2=0,set_water_deprivation3=0,high_water_deprivation3=0,low_water_deprivation3=0;
					int uid_num=0;
					int day_age=0,day_age2=0,startTime2=0,endTime2=0;
					boolean choose = false,choose2=false,choose3=false;
//				try {
					List<PageData> pageData8 = alarmService.selectByCondition(pd);//查询一个栋舍的全部记录
					if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==1){
						//计算温度差、基值
						for(int i=0;i<pageData8.size();i++){
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==0){
								//目标温度1
								set_temp2 = Float.valueOf(pageData8.get(i).get("set_temp").toString()).floatValue();
								//高报温度1
								high_alarm_temp2 = Float.valueOf(pageData8.get(i).get("high_alarm_temp").toString()).floatValue();
								//高报温度警示1
								high_temp_warning2 = Float.valueOf(pageData8.get(i).get("high_temp_warning").toString()).floatValue();
								//低报温度1
								low_alarm_temp2 = Float.valueOf(pageData8.get(i).get("low_alarm_temp").toString()).floatValue();
								//低报温度警示1
								low_temp_warning2 = Float.valueOf(pageData8.get(i).get("low_temp_warning").toString()).floatValue();
								//目标温度2
								set_temp3 = Float.valueOf(pageData8.get(i).get("set_temp").toString()).floatValue();
								//高报温度2
								high_alarm_temp3 = Float.valueOf(pageData8.get(i).get("high_alarm_temp").toString()).floatValue();
								//高报温度警示2
								high_temp_warning3 = Float.valueOf(pageData8.get(i).get("high_temp_warning").toString()).floatValue();
								//低报温度2
								low_alarm_temp3 = Float.valueOf(pageData8.get(i).get("low_alarm_temp").toString()).floatValue();
								//低报温度警示2
								low_temp_warning3 = Float.valueOf(pageData8.get(i).get("low_temp_warning").toString()).floatValue();
								if(pageData8.size()!=1){
									//目标温度差2
									set_temp1 = (Float.valueOf(pageData8.get(i+1).get("set_temp").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("set_temp").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标温度2
									set_temp3 = Float.valueOf(pageData8.get(i).get("set_temp").toString()).floatValue();
									//高报温度差2
									high_alarm_temp1 = (Float.valueOf(pageData8.get(i+1).get("high_alarm_temp").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("high_alarm_temp").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//高报温度警示差2
									high_temp_warning1 = (Float.valueOf(pageData8.get(i+1).get("high_temp_warning").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("high_temp_warning").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//高报温度2
									high_alarm_temp3 = Float.valueOf(pageData8.get(i).get("high_alarm_temp").toString()).floatValue();
									//高报温度警示2
									high_temp_warning3 = Float.valueOf(pageData8.get(i).get("high_temp_warning").toString()).floatValue();
									//低报温度差2
									low_alarm_temp1 = (Float.valueOf(pageData8.get(i+1).get("low_alarm_temp").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("low_alarm_temp").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//低报温度警示差2
									low_temp_warning1 = (Float.valueOf(pageData8.get(i+1).get("low_temp_warning").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("low_temp_warning").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//低报温度2
									low_alarm_temp3 = Float.valueOf(pageData8.get(i).get("low_alarm_temp").toString()).floatValue();
									//低报温度警示2
									low_temp_warning3 = Float.valueOf(pageData8.get(i).get("low_temp_warning").toString()).floatValue();
									//uid_num
									uid_num = Integer.valueOf(pageData8.get(i+1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue();
									choose3=true;
								}
								choose=true;
								break;
							}else
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i>0 && i<pageData8.size()-1){
								//目标温度差1
								set_temp = (Float.valueOf(pageData8.get(i).get("set_temp").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_temp").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标温度1
								set_temp2 = Float.valueOf(pageData8.get(i-1).get("set_temp").toString()).floatValue();
								//高报温度差1
								high_alarm_temp = (Float.valueOf(pageData8.get(i).get("high_alarm_temp").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_alarm_temp").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//高报温度警示差1
								high_temp_warning = (Float.valueOf(pageData8.get(i).get("high_temp_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_temp_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//高报温度1
								high_alarm_temp2 = Float.valueOf(pageData8.get(i-1).get("high_alarm_temp").toString()).floatValue();
								//高报温度警示1
								high_temp_warning2 = Float.valueOf(pageData8.get(i-1).get("high_temp_warning").toString()).floatValue();
								//低报温度差1
								low_alarm_temp = (Float.valueOf(pageData8.get(i).get("low_alarm_temp").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_alarm_temp").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//低报温度警示差1
								low_temp_warning = (Float.valueOf(pageData8.get(i).get("low_temp_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_temp_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//低报温度1
								low_alarm_temp2 = Float.valueOf(pageData8.get(i-1).get("low_alarm_temp").toString()).floatValue();
								//低报温度警示1
								low_temp_warning2 = Float.valueOf(pageData8.get(i-1).get("low_temp_warning").toString()).floatValue();
								//目标温度差2
								set_temp1 = (Float.valueOf(pageData8.get(i+1).get("set_temp").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("set_temp").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//目标温度2
								set_temp3 = Float.valueOf(pageData8.get(i).get("set_temp").toString()).floatValue();
								//高报温度差2
								high_alarm_temp1 = (Float.valueOf(pageData8.get(i+1).get("high_alarm_temp").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("high_alarm_temp").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//高报温度警示差2
								high_temp_warning1 = (Float.valueOf(pageData8.get(i+1).get("high_temp_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("high_temp_warning").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								
								//高报温度2
								high_alarm_temp3 = Float.valueOf(pageData8.get(i).get("high_alarm_temp").toString()).floatValue();
								//高报温度警示2
								high_temp_warning3 = Float.valueOf(pageData8.get(i).get("high_temp_warning").toString()).floatValue();
								//低报温度差2
								low_alarm_temp1 = (Float.valueOf(pageData8.get(i+1).get("low_alarm_temp").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("low_alarm_temp").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//低报温度警示差2
								low_temp_warning1 = (Float.valueOf(pageData8.get(i+1).get("low_temp_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("low_temp_warning").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								
								//低报温度2
								low_alarm_temp3 = Float.valueOf(pageData8.get(i).get("low_alarm_temp").toString()).floatValue();
								//低报温度警示2
								low_temp_warning3 = Float.valueOf(pageData8.get(i).get("low_temp_warning").toString()).floatValue();
								//uid_num
								uid_num = Integer.valueOf(pageData8.get(i+1).get("id").toString()).intValue();
								day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue();
								//日龄
								day_age = Integer.valueOf(pageData8.get(i-1).get("day_age").toString()).intValue();
								choose2 = true;
								break;
							}else if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==pageData8.size()-1){
								//目标温度差
								set_temp = (Float.valueOf(pageData8.get(i).get("set_temp").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_temp").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标温度
								set_temp2 = Float.valueOf(pageData8.get(i-1).get("set_temp").toString()).floatValue();
								//高报温度差
								high_alarm_temp = (Float.valueOf(pageData8.get(i).get("high_alarm_temp").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_alarm_temp").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//高报温度警示差
								high_temp_warning = (Float.valueOf(pageData8.get(i).get("high_temp_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_temp_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//高报温度
								high_alarm_temp2 = Float.valueOf(pageData8.get(i-1).get("high_alarm_temp").toString()).floatValue();
								//高报温度警示
								high_temp_warning2 = Float.valueOf(pageData8.get(i-1).get("high_temp_warning").toString()).floatValue();
								//低报温度差
								low_alarm_temp = (Float.valueOf(pageData8.get(i).get("low_alarm_temp").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_alarm_temp").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//低报温度警示差
								low_temp_warning = (Float.valueOf(pageData8.get(i).get("low_temp_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_temp_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//低报温度
								low_alarm_temp2 = Float.valueOf(pageData8.get(i-1).get("low_alarm_temp").toString()).floatValue();
								//低报温度警示
								low_temp_warning2 = Float.valueOf(pageData8.get(i-1).get("low_temp_warning").toString()).floatValue();
								//目标温度2
								set_temp3 = Float.valueOf(pageData8.get(i).get("set_temp").toString()).floatValue();
								//高报温度2
								high_alarm_temp3 = Float.valueOf(pageData8.get(i).get("high_alarm_temp").toString()).floatValue();
								//高报温度警示2
								high_temp_warning3 = Float.valueOf(pageData8.get(i).get("high_temp_warning").toString()).floatValue();
								//低报温度2
								low_alarm_temp3 = Float.valueOf(pageData8.get(i).get("low_alarm_temp").toString()).floatValue();
								//低报温度警示2
								low_temp_warning3 = Float.valueOf(pageData8.get(i).get("low_temp_warning").toString()).floatValue();
								uid_num = Integer.valueOf(pageData8.get(i-1).get("id").toString()).intValue();
								//日龄
								day_age = Integer.valueOf(pageData8.get(i-1).get("day_age").toString()).intValue();
								choose = true;
								break;
							}
						}
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2){
						//计算负压差、基值
						for(int i=0;i<pageData8.size();i++){
							int d =Integer.valueOf(pageData8.get(i).get("day_age").toString()).intValue()*7;
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									d==Integer.valueOf(pd.get("day_age").toString()).intValue() && i==0){
								//目标负压1
//							set_negative_pressure2 = Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue();
								//光照目标值1
								set_lux3 = Float.valueOf(pageData8.get(i).get("set_lux").toString()).floatValue();
								//光照上限值1
								high_lux3 = Float.valueOf(pageData8.get(i).get("high_lux").toString()).floatValue();
								//光照上限警示1
								high_lux_warning3 = Float.valueOf(pageData8.get(i).get("high_lux_warning").toString()).floatValue();
								//光照下限值1
								low_lux3 = Float.valueOf(pageData8.get(i).get("low_lux").toString()).floatValue();
								//光照下限警示1
								low_lux_warning3 = Float.valueOf(pageData8.get(i).get("low_lux_warning").toString()).floatValue();
								if(pageData8.size()!=1){
									//目标负压差2
//								set_negative_pressure1 = (Float.valueOf(pageData8.get(i+1).get("set_negative_pressure").toString()).floatValue() -
//										Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue())/
//								((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标负压2
//								set_negative_pressure3 = Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue();
									//光照目标值差2
									set_lux1 = (Float.valueOf(pageData8.get(i+1).get("set_lux").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("set_lux").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//光照目标值2
									set_lux3 = Float.valueOf(pageData8.get(i).get("set_lux").toString()).floatValue();
									//光照上限值差2
									high_lux1 = (Float.valueOf(pageData8.get(i+1).get("high_lux").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("high_lux").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//光照上限警示差2
									high_lux_warning1 = (Float.valueOf(pageData8.get(i+1).get("high_lux_warning").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("high_lux_warning").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//光照上限值2
									high_lux3 = Float.valueOf(pageData8.get(i).get("high_lux").toString()).floatValue();
									//光照上限值2
									high_lux_warning3 = Float.valueOf(pageData8.get(i).get("high_lux_warning").toString()).floatValue();
									//光照下限值差2
									low_lux1 = (Float.valueOf(pageData8.get(i+1).get("low_lux").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("low_lux").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//光照下限警示差2
									low_lux_warning1 = (Float.valueOf(pageData8.get(i+1).get("low_lux_warning").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("low_lux_warning").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//光照下限值2
									low_lux3 = Float.valueOf(pageData8.get(i).get("low_lux").toString()).floatValue();
									//光照下限警示2
									low_lux_warning3 = Float.valueOf(pageData8.get(i).get("low_lux_warning").toString()).floatValue();
									//uid_num
									uid_num = Integer.valueOf(pageData8.get(i+1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue()*7;
									String[] st2 = pageData8.get(i+1).get("start_time").toString().split(":");
									startTime2 = Integer.valueOf(st2[0]).intValue();
									String[] et2 = pageData8.get(i+1).get("end_time").toString().split(":");
									endTime2 = Integer.valueOf(et2[0]).intValue();
									choose3=true;
								}
								choose=true;
								break;
							}else
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									d==Integer.valueOf(pd.get("day_age").toString()).intValue() && i>0 && i<pageData8.size()-1){
								//目标负压差1
//								set_negative_pressure = (Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue() -
//										Float.valueOf(pageData8.get(i-1).get("set_negative_pressure").toString()).floatValue())/
//								((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标负压1
//								set_negative_pressure2 = Float.valueOf(pageData8.get(i-1).get("set_negative_pressure").toString()).floatValue();
								//光照目标值差1
								set_lux = (Float.valueOf(pageData8.get(i).get("set_lux").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_lux").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue()*7)*24);
								//光照目标值1
								set_lux2 = Float.valueOf(pageData8.get(i-1).get("set_lux").toString()).floatValue();
								//光照上限值差1
								high_lux = (Float.valueOf(pageData8.get(i).get("high_lux").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_lux").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue()*7)*24);
								//光照上限警示差1
								high_lux_warning = (Float.valueOf(pageData8.get(i).get("high_lux_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_lux_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue()*7)*24);
								
								//光照上限值1
								high_lux2 = Float.valueOf(pageData8.get(i-1).get("high_lux").toString()).floatValue();
								//光照上限警示1
								high_lux_warning2 = Float.valueOf(pageData8.get(i-1).get("high_lux_warning").toString()).floatValue();
								//光照下限值差1
								low_lux = (Float.valueOf(pageData8.get(i).get("low_lux").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_lux").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue()*7)*24);
								//光照下限警示差1
								low_lux_warning = (Float.valueOf(pageData8.get(i).get("low_lux_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_lux_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue()*7)*24);
								
								//光照下限值1
								low_lux2 = Float.valueOf(pageData8.get(i-1).get("low_lux").toString()).floatValue();
								//光照下限警示1
								low_lux_warning2 = Float.valueOf(pageData8.get(i-1).get("low_lux_warning").toString()).floatValue();
								//目标负压差2
//								set_negative_pressure1 = (Float.valueOf(pageData8.get(i+1).get("set_negative_pressure").toString()).floatValue() -
//										Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue())/
//								((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//目标负压2
//								set_negative_pressure3 = Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue();
								//光照目标值差2
								set_lux1 = (Float.valueOf(pageData8.get(i+1).get("set_lux").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("set_lux").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//光照目标值2
								set_lux3 = Float.valueOf(pageData8.get(i).get("set_lux").toString()).floatValue();
								//光照上限值差2
								high_lux1 = (Float.valueOf(pageData8.get(i+1).get("high_lux").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("high_lux").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//光照上限警示差2
								high_lux_warning1 = (Float.valueOf(pageData8.get(i+1).get("high_lux_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("high_lux_warning").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								
								//光照上限值2
								high_lux3 = Float.valueOf(pageData8.get(i).get("high_lux").toString()).floatValue();
								//光照上限警示2
								high_lux_warning3 = Float.valueOf(pageData8.get(i).get("high_lux_warning").toString()).floatValue();
								//光照下限值差2
								low_lux1 = (Float.valueOf(pageData8.get(i+1).get("low_lux").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("low_lux").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//光照下限警示差2
								low_lux_warning1 = (Float.valueOf(pageData8.get(i+1).get("low_lux_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("low_lux_warning").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()*7-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								
								//光照下限值2
								low_lux3 = Float.valueOf(pageData8.get(i).get("low_lux").toString()).floatValue();
								//光照下限警示2
								low_lux_warning3 = Float.valueOf(pageData8.get(i).get("low_lux_warning").toString()).floatValue();
								//uid_num
								uid_num = Integer.valueOf(pageData8.get(i-1).get("id").toString()).intValue();
								day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue()*7;
								//日龄
								day_age = (Integer.valueOf(pageData8.get(i).get("day_age").toString()).intValue()-1)*7;
								String[] st2 = pageData8.get(i+1).get("start_time").toString().split(":");
								startTime2 = Integer.valueOf(st2[0]).intValue();
								String[] et2 = pageData8.get(i+1).get("end_time").toString().split(":");
								endTime2 = Integer.valueOf(et2[0]).intValue();
								choose2 = true;
								break;
							}else if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									d==Integer.valueOf(pd.get("day_age").toString()).intValue() && i==pageData8.size()-1){
								//目标负压差
//								set_negative_pressure = (Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue() -
//										Float.valueOf(pageData8.get(i-1).get("set_negative_pressure").toString()).floatValue())/
//								((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标负压
//								set_negative_pressure2 = Float.valueOf(pageData8.get(i-1).get("set_negative_pressure").toString()).floatValue();
								//光照目标值差
								set_lux = (Float.valueOf(pageData8.get(i).get("set_lux").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_lux").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue()*7)*24);
								//光照目标值
								set_lux2 = Float.valueOf(pageData8.get(i-1).get("set_lux").toString()).floatValue();
								//光照上限值差
								high_lux = (Float.valueOf(pageData8.get(i).get("high_lux").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_lux").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue()*7)*24);
								//光照上限警示差
								high_lux_warning = (Float.valueOf(pageData8.get(i).get("high_lux_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_lux_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue()*7)*24);
								
								//光照上限值
								high_lux2 = Float.valueOf(pageData8.get(i-1).get("high_lux").toString()).floatValue();
								//光照上限警示
								high_lux_warning2 = Float.valueOf(pageData8.get(i-1).get("high_lux_warning").toString()).floatValue();
								//光照下限值差
								low_lux = (Float.valueOf(pageData8.get(i).get("low_lux").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_lux").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue()*7)*24);
								//光照下限警示差
								low_lux_warning = (Float.valueOf(pageData8.get(i).get("low_lux_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_lux_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue()*7)*24);
								
								//光照下限值
								low_lux2 = Float.valueOf(pageData8.get(i-1).get("low_lux").toString()).floatValue();
								//光照下限警示
								low_lux_warning2 = Float.valueOf(pageData8.get(i-1).get("low_lux_warning").toString()).floatValue();
								//光照目标值2
								set_lux3 = Float.valueOf(pageData8.get(i).get("set_lux").toString()).floatValue();
								//光照上限值2
								high_lux3 = Float.valueOf(pageData8.get(i).get("high_lux").toString()).floatValue();
								//光照上限警示2
								high_lux_warning3 = Float.valueOf(pageData8.get(i).get("high_lux_warning").toString()).floatValue();
								//光照下限值2
								low_lux3 = Float.valueOf(pageData8.get(i).get("low_lux").toString()).floatValue();
								//光照下限警示2
								low_lux_warning3 = Float.valueOf(pageData8.get(i).get("low_lux_warning").toString()).floatValue();
								uid_num = Integer.valueOf(pageData8.get(i-1).get("id").toString()).intValue();
								//日龄
								day_age = (Integer.valueOf(pageData8.get(i).get("day_age").toString()).intValue()-1)*7;
								choose = true;
								break;
							}
						}
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==3){
						//计算二氧化碳差、基值
						for(int i=0;i<pageData8.size();i++){
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==0){
								//目标二氧化碳1
								set_co22 = Float.valueOf(pageData8.get(i).get("set_co2").toString()).floatValue();
								//高报二氧化碳1
								high_alarm_co22 = Float.valueOf(pageData8.get(i).get("high_alarm_co2").toString()).floatValue();
								//高报二氧化碳警示1
								high_co2_warning2 = Float.valueOf(pageData8.get(i).get("high_co2_warning").toString()).floatValue();
								//低报二氧化碳1
//							low_alarm_co22 = Float.valueOf(pageData8.get(i).get("low_alarm_co2").toString()).floatValue();
								if(pageData8.size()!=1){
									//目标二氧化碳差2
									set_co21 = (Float.valueOf(pageData8.get(i+1).get("set_co2").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("set_co2").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标二氧化碳2
									set_co23 = Float.valueOf(pageData8.get(i).get("set_co2").toString()).floatValue();
									//高报二氧化碳差2
									high_alarm_co21 = (Float.valueOf(pageData8.get(i+1).get("high_alarm_co2").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("high_alarm_co2").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//高报二氧化碳警示差2
									high_co2_warning1 = (Float.valueOf(pageData8.get(i+1).get("high_co2_warning").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("high_co2_warning").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//高报二氧化碳2
									high_alarm_co23 = Float.valueOf(pageData8.get(i).get("high_alarm_co2").toString()).floatValue();
									//高报二氧化碳警示2
									high_co2_warning3 = Float.valueOf(pageData8.get(i).get("high_co2_warning").toString()).floatValue();
									//低报二氧化碳差2
//								low_alarm_co21 = (Float.valueOf(pageData8.get(i+1).get("low_alarm_co2").toString()).floatValue() -
//										Float.valueOf(pageData8.get(i).get("low_alarm_co2").toString()).floatValue())/
//								((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//低报二氧化碳2
//								low_alarm_co23 = Float.valueOf(pageData8.get(i).get("low_alarm_co2").toString()).floatValue();
									//uid_num
									uid_num = Integer.valueOf(pageData8.get(i+1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue();
								}
								break;
							}else
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i>0 && i<pageData8.size()-1){
								//目标二氧化碳差1
								set_co2 = (Float.valueOf(pageData8.get(i).get("set_co2").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_co2").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标二氧化碳1
								set_co22 = Float.valueOf(pageData8.get(i-1).get("set_co2").toString()).floatValue();
								//高报二氧化碳差1
								high_alarm_co2 = (Float.valueOf(pageData8.get(i).get("high_alarm_co2").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_alarm_co2").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//高报二氧化碳警示差1
								high_co2_warning = (Float.valueOf(pageData8.get(i).get("high_co2_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_co2_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//高报二氧化碳1
								high_alarm_co22 = Float.valueOf(pageData8.get(i-1).get("high_alarm_co2").toString()).floatValue();
								//高报二氧化碳警示1
								high_co2_warning2 = Float.valueOf(pageData8.get(i-1).get("high_co2_warning").toString()).floatValue();
								//低报二氧化碳差1
//								low_alarm_co2 = (Float.valueOf(pageData8.get(i).get("low_alarm_co2").toString()).floatValue() -
//										Float.valueOf(pageData8.get(i-1).get("low_alarm_co2").toString()).floatValue())/
//								((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//低报二氧化碳1
//								low_alarm_co22 = Float.valueOf(pageData8.get(i-1).get("low_alarm_co2").toString()).floatValue();
								//目标二氧化碳差2
								set_co21 = (Float.valueOf(pageData8.get(i+1).get("set_co2").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("set_co2").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//目标二氧化碳2
								set_co23 = Float.valueOf(pageData8.get(i).get("set_co2").toString()).floatValue();
								//高报二氧化碳差2
								high_alarm_co21 = (Float.valueOf(pageData8.get(i+1).get("high_alarm_co2").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("high_alarm_co2").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//高报二氧化碳警示差2
								high_co2_warning1 = (Float.valueOf(pageData8.get(i+1).get("high_co2_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("high_co2_warning").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								
								//高报二氧化碳2
								high_alarm_co23 = Float.valueOf(pageData8.get(i).get("high_alarm_co2").toString()).floatValue();
								//高报二氧化碳警示2
								high_co2_warning3 = Float.valueOf(pageData8.get(i).get("high_co2_warning").toString()).floatValue();
								//低报二氧化碳差2
//								low_alarm_co21 = (Float.valueOf(pageData8.get(i+1).get("low_alarm_co2").toString()).floatValue() -
//										Float.valueOf(pageData8.get(i).get("low_alarm_co2").toString()).floatValue())/
//								((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//低报二氧化碳2
//								low_alarm_co23 = Float.valueOf(pageData8.get(i).get("low_alarm_co2").toString()).floatValue();
								//uid_num
								uid_num = Integer.valueOf(pageData8.get(i+1).get("id").toString()).intValue();
								day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue();
								//日龄
								day_age = Integer.valueOf(pageData8.get(i-1).get("day_age").toString()).intValue();
								break;
							}else if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==pageData8.size()-1){
								//目标二氧化碳差
								set_co2 = (Float.valueOf(pageData8.get(i).get("set_co2").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_co2").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标二氧化碳
								set_co22 = Float.valueOf(pageData8.get(i-1).get("set_co2").toString()).floatValue();
								//高报二氧化碳差
								high_alarm_co2 = (Float.valueOf(pageData8.get(i).get("high_alarm_co2").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_alarm_co2").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//高报二氧化碳警示差
								high_co2_warning = (Float.valueOf(pageData8.get(i).get("high_co2_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_co2_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//高报二氧化碳
								high_alarm_co22 = Float.valueOf(pageData8.get(i-1).get("high_alarm_co2").toString()).floatValue();
								//高报二氧化碳警示
								high_co2_warning2 = Float.valueOf(pageData8.get(i-1).get("high_co2_warning").toString()).floatValue();
								//低报二氧化碳差
//								low_alarm_co2 = (Float.valueOf(pageData8.get(i).get("low_alarm_co2").toString()).floatValue() -
//										Float.valueOf(pageData8.get(i-1).get("low_alarm_co2").toString()).floatValue())/
//								((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//低报二氧化碳
//								low_alarm_co22 = Float.valueOf(pageData8.get(i-1).get("low_alarm_co2").toString()).floatValue();
								//日龄
								day_age = Integer.valueOf(pageData8.get(i-1).get("day_age").toString()).intValue();
								break;
							}
						}
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==5){
						//计算湿度差、基值
						for(int i=0;i<pageData8.size();i++){
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==0){
								//目标湿度1
								set_humidity2 = Float.valueOf(pageData8.get(i).get("set_humidity").toString()).floatValue();
								//高报湿度1
								high_alarm_humidity2 = Float.valueOf(pageData8.get(i).get("high_alarm_humidity").toString()).floatValue();
								//高报湿度警示1
								high_humidity_warning2 = Float.valueOf(pageData8.get(i).get("high_humidity_warning").toString()).floatValue();
								//低报湿度1
								low_alarm_humidity2 = Float.valueOf(pageData8.get(i).get("low_alarm_humidity").toString()).floatValue();
								//低报湿度警示1
								low_humidity_warning2 = Float.valueOf(pageData8.get(i).get("low_humidity_warning").toString()).floatValue();
								//目标湿度2
								set_humidity3 = Float.valueOf(pageData8.get(i).get("set_humidity").toString()).floatValue();
								//高报湿度2
								high_alarm_humidity3 = Float.valueOf(pageData8.get(i).get("high_alarm_humidity").toString()).floatValue();
								//高报湿度警示2
								high_humidity_warning3 = Float.valueOf(pageData8.get(i).get("high_humidity_warning").toString()).floatValue();
								//低报湿度2
								low_alarm_humidity3 = Float.valueOf(pageData8.get(i).get("low_alarm_humidity").toString()).floatValue();
								//低报湿度警示2
								low_humidity_warning3 = Float.valueOf(pageData8.get(i).get("low_humidity_warning").toString()).floatValue();
								if(pageData8.size()!=1){
									//目标湿度差2
									set_humidity1 = (Float.valueOf(pageData8.get(i+1).get("set_humidity").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("set_humidity").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标湿度2
									set_humidity3 = Float.valueOf(pageData8.get(i).get("set_humidity").toString()).floatValue();
									//高报湿度差2
									high_alarm_humidity1 = (Float.valueOf(pageData8.get(i+1).get("high_alarm_humidity").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("high_alarm_humidity").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//高报湿度警示差2
									high_humidity_warning1 = (Float.valueOf(pageData8.get(i+1).get("high_humidity_warning").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("high_humidity_warning").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//高报湿度2
									high_alarm_humidity3 = Float.valueOf(pageData8.get(i).get("high_alarm_humidity").toString()).floatValue();
									//高报湿度警示2
									high_humidity_warning3 = Float.valueOf(pageData8.get(i).get("high_humidity_warning").toString()).floatValue();
									//低报湿度差2
									low_alarm_humidity1 = (Float.valueOf(pageData8.get(i+1).get("low_alarm_humidity").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("low_alarm_humidity").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//低报湿度警示差2
									low_humidity_warning1 = (Float.valueOf(pageData8.get(i+1).get("low_humidity_warning").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("low_humidity_warning").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//低报湿度2
									low_alarm_humidity3 = Float.valueOf(pageData8.get(i).get("low_alarm_humidity").toString()).floatValue();
									//低报湿度警示2
									low_humidity_warning3 = Float.valueOf(pageData8.get(i).get("low_humidity_warning").toString()).floatValue();
									//uid_num
									uid_num = Integer.valueOf(pageData8.get(i+1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue();
									choose3=true;
								}
								choose=true;
								break;
							}else
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i>0 && i<pageData8.size()-1){
								//目标湿度差1
								set_humidity = (Float.valueOf(pageData8.get(i).get("set_humidity").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_humidity").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标湿度1
								set_humidity2 = Float.valueOf(pageData8.get(i-1).get("set_humidity").toString()).floatValue();
								//高报湿度差1
								high_alarm_humidity = (Float.valueOf(pageData8.get(i).get("high_alarm_humidity").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_alarm_humidity").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//高报湿度警示差1
								high_humidity_warning = (Float.valueOf(pageData8.get(i).get("high_humidity_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_humidity_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//高报湿度1
								high_alarm_humidity2 = Float.valueOf(pageData8.get(i-1).get("high_alarm_humidity").toString()).floatValue();
								//高报湿度警示1
								high_humidity_warning2 = Float.valueOf(pageData8.get(i-1).get("high_humidity_warning").toString()).floatValue();
								//低报湿度差1
								low_alarm_humidity = (Float.valueOf(pageData8.get(i).get("low_alarm_humidity").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_alarm_humidity").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//低报湿度警示差1
								low_humidity_warning = (Float.valueOf(pageData8.get(i).get("low_humidity_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_humidity_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//低报湿度1
								low_alarm_humidity2 = Float.valueOf(pageData8.get(i-1).get("low_alarm_humidity").toString()).floatValue();
								//低报湿度警示1
								low_humidity_warning2 = Float.valueOf(pageData8.get(i-1).get("low_humidity_warning").toString()).floatValue();
								//目标湿度差2
								set_humidity1 = (Float.valueOf(pageData8.get(i+1).get("set_humidity").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("set_humidity").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//目标湿度2
								set_humidity3 = Float.valueOf(pageData8.get(i).get("set_humidity").toString()).floatValue();
								//高报湿度差2
								high_alarm_humidity1 = (Float.valueOf(pageData8.get(i+1).get("high_alarm_humidity").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("high_alarm_humidity").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//高报湿度警示差2
								high_humidity_warning1 = (Float.valueOf(pageData8.get(i+1).get("high_humidity_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("high_humidity_warning").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								
								//高报湿度2
								high_alarm_humidity3 = Float.valueOf(pageData8.get(i).get("high_alarm_humidity").toString()).floatValue();
								//高报湿度警示2
								high_humidity_warning3 = Float.valueOf(pageData8.get(i).get("high_humidity_warning").toString()).floatValue();
								//低报湿度差2
								low_alarm_humidity1 = (Float.valueOf(pageData8.get(i+1).get("low_alarm_humidity").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("low_alarm_humidity").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//低报湿度警示差2
								low_humidity_warning1 = (Float.valueOf(pageData8.get(i+1).get("low_humidity_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("low_humidity_warning").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								
								//低报湿度2
								low_alarm_humidity3 = Float.valueOf(pageData8.get(i).get("low_alarm_humidity").toString()).floatValue();
								//低报湿度警示2
								low_humidity_warning3 = Float.valueOf(pageData8.get(i).get("low_humidity_warning").toString()).floatValue();
								//uid_num
								uid_num = Integer.valueOf(pageData8.get(i+1).get("id").toString()).intValue();
								day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue();
								//日龄
								day_age = Integer.valueOf(pageData8.get(i-1).get("day_age").toString()).intValue();
								choose2 = true;
								break;
							}else if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==pageData8.size()-1){
								//目标湿度差
								set_humidity = (Float.valueOf(pageData8.get(i).get("set_humidity").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_humidity").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标湿度
								set_humidity2 = Float.valueOf(pageData8.get(i-1).get("set_humidity").toString()).floatValue();
								//高报湿度差
								high_alarm_humidity = (Float.valueOf(pageData8.get(i).get("high_alarm_humidity").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_alarm_humidity").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//高报湿度警示差
								high_humidity_warning = (Float.valueOf(pageData8.get(i).get("high_humidity_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_humidity_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//高报湿度
								high_alarm_humidity2 = Float.valueOf(pageData8.get(i-1).get("high_alarm_humidity").toString()).floatValue();
								//高报湿度警示
								high_humidity_warning2 = Float.valueOf(pageData8.get(i-1).get("high_humidity_warning").toString()).floatValue();
								//低报湿度差
								low_alarm_humidity = (Float.valueOf(pageData8.get(i).get("low_alarm_humidity").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_alarm_humidity").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//低报湿度警示差
								low_humidity_warning = (Float.valueOf(pageData8.get(i).get("low_humidity_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_humidity_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//低报湿度
								low_alarm_humidity2 = Float.valueOf(pageData8.get(i-1).get("low_alarm_humidity").toString()).floatValue();
								//低报湿度警示
								low_humidity_warning2 = Float.valueOf(pageData8.get(i-1).get("low_humidity_warning").toString()).floatValue();
								//目标湿度2
								set_humidity3 = Float.valueOf(pageData8.get(i).get("set_humidity").toString()).floatValue();
								//高报湿度2
								high_alarm_humidity3 = Float.valueOf(pageData8.get(i).get("high_alarm_humidity").toString()).floatValue();
								//高报湿度警示2
								high_humidity_warning3 = Float.valueOf(pageData8.get(i).get("high_humidity_warning").toString()).floatValue();
								//低报湿度2
								low_alarm_humidity3 = Float.valueOf(pageData8.get(i).get("low_alarm_humidity").toString()).floatValue();
								//低报湿度警示2
								low_humidity_warning3 = Float.valueOf(pageData8.get(i).get("low_humidity_warning").toString()).floatValue();
								uid_num = Integer.valueOf(pageData8.get(i-1).get("id").toString()).intValue();
								//日龄
								day_age = Integer.valueOf(pageData8.get(i-1).get("day_age").toString()).intValue();
								choose = true;
								break;
							}
						}
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==6){
						//计算负压差、基值
						for(int i=0;i<pageData8.size();i++){
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==0){
								//目标负压1
								set_negative_pressure2 = Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue();
								//高报负压1
								high_alarm_negative_pressure2 = Float.valueOf(pageData8.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
								//高报负压警示1
								high_negative_pressure_warning2 = Float.valueOf(pageData8.get(i).get("high_negative_pressure_warning").toString()).floatValue();
								//低报负压1
							    low_alarm_negative_pressure2 = Float.valueOf(pageData8.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
							    //低报负压警示1
								low_negative_pressure_warning2 = Float.valueOf(pageData8.get(i).get("low_negative_pressure_warning").toString()).floatValue();
								if(pageData8.size()!=1){
									//目标负压差2
									set_negative_pressure1 = (Float.valueOf(pageData8.get(i+1).get("set_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标负压2
									set_negative_pressure3 = Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue();
									//高报负压差2
									high_alarm_negative_pressure1 = (Float.valueOf(pageData8.get(i+1).get("high_alarm_negative_pressure").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("high_alarm_negative_pressure").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//高报负压警示差2
									high_negative_pressure_warning1 = (Float.valueOf(pageData8.get(i+1).get("high_negative_pressure_warning").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("high_negative_pressure_warning").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									
									//高报负压2
									high_alarm_negative_pressure3 = Float.valueOf(pageData8.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
									//高报负压警示2
									high_negative_pressure_warning3 = Float.valueOf(pageData8.get(i).get("high_negative_pressure_warning").toString()).floatValue();
									//低报负压差2
								low_alarm_negative_pressure1 = (Float.valueOf(pageData8.get(i+1).get("low_alarm_negative_pressure").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("low_alarm_negative_pressure").toString()).floatValue())/
								((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//低报负压2
								low_alarm_negative_pressure3 = Float.valueOf(pageData8.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
								//低报负压警示2
								low_negative_pressure_warning3 = Float.valueOf(pageData8.get(i).get("low_negative_pressure_warning").toString()).floatValue();
								//低报负压警示差2
								low_negative_pressure_warning1 = (Float.valueOf(pageData8.get(i+1).get("low_negative_pressure_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("low_negative_pressure_warning").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//uid_num
									uid_num = Integer.valueOf(pageData8.get(i+1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue();
								}
								break;
							}else
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i>0 && i<pageData8.size()-1){
								//目标负压差1
								set_negative_pressure = (Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_negative_pressure").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标负压1
								set_negative_pressure2 = Float.valueOf(pageData8.get(i-1).get("set_negative_pressure").toString()).floatValue();
								//高报负压差1
								high_alarm_negative_pressure = (Float.valueOf(pageData8.get(i).get("high_alarm_negative_pressure").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_alarm_negative_pressure").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//高报负压警示差1
								high_negative_pressure_warning = (Float.valueOf(pageData8.get(i).get("high_negative_pressure_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_negative_pressure_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//高报负压1
								high_alarm_negative_pressure2 = Float.valueOf(pageData8.get(i-1).get("high_alarm_negative_pressure").toString()).floatValue();
								//高报负压警示1
								high_negative_pressure_warning2 = Float.valueOf(pageData8.get(i-1).get("high_negative_pressure_warning").toString()).floatValue();
								//低报负压差1
								low_alarm_negative_pressure = (Float.valueOf(pageData8.get(i).get("low_alarm_negative_pressure").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue())/
								((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//低报负压1
								low_alarm_negative_pressure2 = Float.valueOf(pageData8.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue();
								//低报负压警示1
								low_negative_pressure_warning2 = Float.valueOf(pageData8.get(i-1).get("low_negative_pressure_warning").toString()).floatValue();
								//低报负压警示差1
								low_negative_pressure_warning = (Float.valueOf(pageData8.get(i).get("low_negative_pressure_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_negative_pressure_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标负压差2
								set_negative_pressure1 = (Float.valueOf(pageData8.get(i+1).get("set_negative_pressure").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//目标负压2
								set_negative_pressure3 = Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue();
								//高报负压差2
								high_alarm_negative_pressure1 = (Float.valueOf(pageData8.get(i+1).get("high_alarm_negative_pressure").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("high_alarm_negative_pressure").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//高报负压警示差2
								high_negative_pressure_warning1 = (Float.valueOf(pageData8.get(i+1).get("high_negative_pressure_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("high_negative_pressure_warning").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								
								//高报负压2
								high_alarm_negative_pressure3 = Float.valueOf(pageData8.get(i).get("high_alarm_negative_pressure").toString()).floatValue();
								//高报负压警示2
								high_negative_pressure_warning3 = Float.valueOf(pageData8.get(i).get("high_negative_pressure_warning").toString()).floatValue();
								//低报负压差2
								low_alarm_negative_pressure1 = (Float.valueOf(pageData8.get(i+1).get("low_alarm_negative_pressure").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("low_alarm_negative_pressure").toString()).floatValue())/
								((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//低报负压2
								low_alarm_negative_pressure3 = Float.valueOf(pageData8.get(i).get("low_alarm_negative_pressure").toString()).floatValue();
								//低报负压警示2
								low_negative_pressure_warning3 = Float.valueOf(pageData8.get(i).get("low_negative_pressure_warning").toString()).floatValue();
								//低报负压警示差2
								low_negative_pressure_warning1 = (Float.valueOf(pageData8.get(i+1).get("low_negative_pressure_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("low_negative_pressure_warning").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//uid_num
								uid_num = Integer.valueOf(pageData8.get(i+1).get("id").toString()).intValue();
								day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue();
								//日龄
								day_age = Integer.valueOf(pageData8.get(i-1).get("day_age").toString()).intValue();
								break;
							}else if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==pageData8.size()-1){
								//目标负压差
								set_negative_pressure = (Float.valueOf(pageData8.get(i).get("set_negative_pressure").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_negative_pressure").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标负压
								set_negative_pressure2 = Float.valueOf(pageData8.get(i-1).get("set_negative_pressure").toString()).floatValue();
								//高报负压差
								high_alarm_negative_pressure = (Float.valueOf(pageData8.get(i).get("high_alarm_negative_pressure").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_alarm_negative_pressure").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//高报负压警示差
								high_negative_pressure_warning = (Float.valueOf(pageData8.get(i).get("high_negative_pressure_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_negative_pressure_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								
								//高报负压
								high_alarm_negative_pressure2 = Float.valueOf(pageData8.get(i-1).get("high_alarm_negative_pressure").toString()).floatValue();
								//高报负压警示
								high_negative_pressure_warning2 = Float.valueOf(pageData8.get(i-1).get("high_negative_pressure_warning").toString()).floatValue();
								//低报负压差
								low_alarm_negative_pressure = (Float.valueOf(pageData8.get(i).get("low_alarm_negative_pressure").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue())/
								((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//低报负压
								low_alarm_negative_pressure2 = Float.valueOf(pageData8.get(i-1).get("low_alarm_negative_pressure").toString()).floatValue();
								//低报负压警示
								low_negative_pressure_warning2 = Float.valueOf(pageData8.get(i-1).get("low_negative_pressure_warning").toString()).floatValue();
								//低报负压警示差
								low_negative_pressure_warning = (Float.valueOf(pageData8.get(i).get("low_negative_pressure_warning").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_negative_pressure_warning").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//日龄
								day_age = Integer.valueOf(pageData8.get(i-1).get("day_age").toString()).intValue();
								break;
							}
						}
					}else{
						for(int i=0;i<pageData8.size();i++){
							//计算耗水差、基值
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==0){
								//目标耗水1
								set_water_deprivation2 = Float.valueOf(pageData8.get(i).get("set_water_deprivation").toString()).floatValue();
								//高报耗水1
								high_water_deprivation2 = Float.valueOf(pageData8.get(i).get("high_water_deprivation").toString()).floatValue();
								//低报耗水1
								low_water_deprivation2 = Float.valueOf(pageData8.get(i).get("low_water_deprivation").toString()).floatValue();
								if(pageData8.size()!=1){
									//目标耗水差2
									set_water_deprivation1 = (Float.valueOf(pageData8.get(i+1).get("set_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("set_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//目标耗水2
									set_water_deprivation3 = Float.valueOf(pageData8.get(i).get("set_water_deprivation").toString()).floatValue();
									//高报耗水差2
									high_water_deprivation1 = (Float.valueOf(pageData8.get(i+1).get("high_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("high_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//高报耗水2
									high_water_deprivation3 = Float.valueOf(pageData8.get(i).get("high_water_deprivation").toString()).floatValue();
									//低报耗水差2
									low_water_deprivation1 = (Float.valueOf(pageData8.get(i+1).get("low_water_deprivation").toString()).floatValue() -
											Float.valueOf(pageData8.get(i).get("low_water_deprivation").toString()).floatValue())/
											((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
									//低报耗水2
									low_water_deprivation3 = Float.valueOf(pageData8.get(i).get("low_water_deprivation").toString()).floatValue();
									//uid_num
									uid_num = Integer.valueOf(pageData8.get(i+1).get("id").toString()).intValue();
									day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue();
								}
								break;
							}else
							if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i>0 && i<pageData8.size()-1){
								//目标耗水差1
								set_water_deprivation = (Float.valueOf(pageData8.get(i).get("set_water_deprivation").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_water_deprivation").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标耗水1
								set_water_deprivation2 = Float.valueOf(pageData8.get(i-1).get("set_water_deprivation").toString()).floatValue();
								//高报耗水差1
								high_water_deprivation = (Float.valueOf(pageData8.get(i).get("high_water_deprivation").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_water_deprivation").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//高报耗水1
								high_water_deprivation2 = Float.valueOf(pageData8.get(i-1).get("high_water_deprivation").toString()).floatValue();
								//低报耗水差1
								low_water_deprivation = (Float.valueOf(pageData8.get(i).get("low_water_deprivation").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_water_deprivation").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//低报耗水1
								low_water_deprivation2 = Float.valueOf(pageData8.get(i-1).get("low_water_deprivation").toString()).floatValue();
								//目标耗水差2
								set_water_deprivation1 = (Float.valueOf(pageData8.get(i+1).get("set_water_deprivation").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("set_water_deprivation").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//目标耗水2
								set_water_deprivation3 = Float.valueOf(pageData8.get(i).get("set_water_deprivation").toString()).floatValue();
								//高报耗水差2
								high_water_deprivation1 = (Float.valueOf(pageData8.get(i+1).get("high_water_deprivation").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("high_water_deprivation").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//高报耗水2
								high_water_deprivation3 = Float.valueOf(pageData8.get(i).get("high_water_deprivation").toString()).floatValue();
								//低报耗水差2
								low_water_deprivation1 = (Float.valueOf(pageData8.get(i+1).get("low_water_deprivation").toString()).floatValue() -
										Float.valueOf(pageData8.get(i).get("low_water_deprivation").toString()).floatValue())/
										((Float.valueOf(pageData8.get(i+1).get("day_age").toString()).floatValue()-Float.valueOf(pd.get("day_age").toString()).floatValue())*24);
								//低报耗水2
								low_water_deprivation3 = Float.valueOf(pageData8.get(i).get("low_water_deprivation").toString()).floatValue();
								//uid_num
								uid_num = Integer.valueOf(pageData8.get(i+1).get("id").toString()).intValue();
								day_age2 = Integer.valueOf(pageData8.get(i+1).get("day_age").toString()).intValue();
								//日龄
								day_age = Integer.valueOf(pageData8.get(i-1).get("day_age").toString()).intValue();
								break;
							}else if(pageData8.get(i).get("farm_id").toString().equals(pd.get("farmId").toString()) &&
									pageData8.get(i).get("house_id").toString().equals(pd.get("houseId").toString()) &&
									pageData8.get(i).get("day_age").toString().equals(pd.get("day_age").toString()) && i==pageData8.size()-1){
								//目标耗水差
								set_water_deprivation = (Float.valueOf(pageData8.get(i).get("set_water_deprivation").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("set_water_deprivation").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//目标耗水
								set_water_deprivation2 = Float.valueOf(pageData8.get(i-1).get("set_water_deprivation").toString()).floatValue();
								//高报耗水差
								high_water_deprivation = (Float.valueOf(pageData8.get(i).get("high_water_deprivation").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("high_water_deprivation").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//高报耗水
								high_water_deprivation2 = Float.valueOf(pageData8.get(i-1).get("high_water_deprivation").toString()).floatValue();
								//低报耗水差
								low_water_deprivation = (Float.valueOf(pageData8.get(i).get("low_water_deprivation").toString()).floatValue() -
										Float.valueOf(pageData8.get(i-1).get("low_water_deprivation").toString()).floatValue())/
										((Float.valueOf(pd.get("day_age").toString()).floatValue()-Float.valueOf(pageData8.get(i-1).get("day_age").toString()).floatValue())*24);
								//低报耗水
								low_water_deprivation2 = Float.valueOf(pageData8.get(i-1).get("low_water_deprivation").toString()).floatValue();
								//日龄
								day_age = Integer.valueOf(pageData8.get(i-1).get("day_age").toString()).intValue();
								break;
							}
						}
					}
					
					PageData pd10 = new PageData();
					pd10.put("uid_num", uid_num);
					pd10.put("alarm_type", pd.get("alarm_type"));
					if(choose){
						pd10.put("day_age1", day_age);
					}
					if(choose2){
						if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2){
							pd10.put("day_age1", day_age);
						}else{
						pd10.put("day_age2", day_age2);
						}
					}
					
//					if(choose3){
//						pd10.put("day_age2", day_age2);
//						choose=true;
//					}
					alarmService.deleteSBDayageTempSub(pd10);
					
					int day_age3 = Integer.valueOf(pageData1.get("day_age").toString()).intValue();

					List<PageData> list = new ArrayList<PageData>();
					if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==1){
						//温度插入
						for(int i=day_age;i<day_age3;i++){
							for(int j=1;j<=24;j++){
								PageData pd3 = new PageData();
								pd3.put("uid_num", pdID);
								pd3.put("alarm_type", pd.get("alarm_type"));
								pd3.put("create_person",userId);
								pd3.put("create_date", new Date());
								pd3.put("create_time", new Date());
								pd3.put("modify_person",userId);
								pd3.put("modify_date", new Date());
								pd3.put("modify_time", new Date());
								pd3.put("farmId", pd.get("farmId"));
								pd3.put("houseId", pd.get("houseId"));
								Date date = new Date();
								date.setMinutes(0);
								date.setSeconds(0);
								pd3.put("day_age", i+1);
								date.setHours(j);
								pd3.put("record_datetime", date);
								pd3.put("set_temp", set_temp2+set_temp*((i-day_age)*24+j));
								pd3.put("high_alarm_temp", high_alarm_temp2+high_alarm_temp*((i-day_age)*24+j));
								pd3.put("low_alarm_temp", low_alarm_temp2+low_alarm_temp*((i-day_age)*24+j));
								pd3.put("high_temp_warning", high_temp_warning2+high_temp_warning*((i-day_age)*24+j));
								pd3.put("low_temp_warning", low_temp_warning2+low_temp_warning*((i-day_age)*24+j));
								pd3.put("set_humidity", null);
								pd3.put("high_alarm_humidity", null);
								pd3.put("low_alarm_humidity", null);
								pd3.put("high_humidity_warning", null);
								pd3.put("low_humidity_warning", null);
								pd3.put("set_negative_pressure", null);
								pd3.put("high_alarm_negative_pressure", null);
								pd3.put("low_alarm_negative_pressure", null);
								pd3.put("high_negative_pressure_warning", null);
								pd3.put("low_negative_pressure_warning", null);
								pd3.put("set_lux", null);
								pd3.put("high_lux", null);
								pd3.put("low_lux", null);
								pd3.put("high_lux_warning", null);
								pd3.put("low_lux_warning", null);
								pd3.put("set_co2", null);
								pd3.put("high_alarm_co2", null);
								pd3.put("high_co2_warning", null);
								pd3.put("set_water_deprivation", null);
								pd3.put("high_water_deprivation", null);
								pd3.put("low_water_deprivation", null);
								pd3.put("is_start", "0");
								list.add(pd3);
//								alarmService.saveSBDayageTempSub(pd3);
							}
						}
						if(choose){
							for(int i=day_age3;i<Integer.valueOf(pd.get("dage").toString()).intValue();i++){
								for(int j=1;j<=24;j++){
									PageData pd3 = new PageData();
									pd3.put("uid_num", pdID);
									pd3.put("alarm_type", pd.get("alarm_type"));
									pd3.put("create_person",userId);
									pd3.put("create_date", new Date());
									pd3.put("create_time", new Date());
									pd3.put("modify_person",userId);
									pd3.put("modify_date", new Date());
									pd3.put("modify_time", new Date());
									pd3.put("farmId", pd.get("farmId"));
									pd3.put("houseId", pd.get("houseId"));
									Date date = new Date();
									date.setMinutes(0);
									date.setSeconds(0);
									pd3.put("day_age", i+1);
									date.setHours(j);
									pd3.put("record_datetime", date);
									pd3.put("set_temp", set_temp3);
									pd3.put("high_alarm_temp", high_alarm_temp3);
									pd3.put("low_alarm_temp", low_alarm_temp3);
									pd3.put("high_temp_warning", high_temp_warning3);
									pd3.put("low_temp_warning", low_temp_warning3);
									pd3.put("set_humidity", null);
									pd3.put("high_alarm_humidity", null);
									pd3.put("low_alarm_humidity", null);
									pd3.put("high_humidity_warning", null);
									pd3.put("low_humidity_warning", null);
									pd3.put("set_negative_pressure", null);
									pd3.put("high_alarm_negative_pressure", null);
									pd3.put("low_alarm_negative_pressure", null);
									pd3.put("high_negative_pressure_warning", null);
									pd3.put("low_negative_pressure_warning", null);
									pd3.put("set_lux", null);
									pd3.put("high_lux", null);
									pd3.put("low_lux", null);
									pd3.put("high_lux_warning", null);
									pd3.put("low_lux_warning", null);
									pd3.put("set_co2", null);
									pd3.put("high_alarm_co2", null);
									pd3.put("high_co2_warning", null);
									pd3.put("set_water_deprivation", null);
									pd3.put("high_water_deprivation", null);
									pd3.put("low_water_deprivation", null);
									pd3.put("is_start", "0");
									list.add(pd3);
								}
							}
						}
						alarmService.saveSBDayageTempSub(list);
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2){
						//负压插入
						for(int i=day_age;i<day_age3;i++){
							for(int j=1;j<=24;j++){
								PageData pd3 = new PageData();
								pd3.put("uid_num", pdID);
								pd3.put("alarm_type", pd.get("alarm_type"));
								pd3.put("create_person",userId);
								pd3.put("create_date", new Date());
								pd3.put("create_time", new Date());
								pd3.put("modify_person",userId);
								pd3.put("modify_date", new Date());
								pd3.put("modify_time", new Date());
								pd3.put("farmId", pd.get("farmId"));
								pd3.put("houseId", pd.get("houseId"));
								Date date = new Date();
								date.setMinutes(0);
								date.setSeconds(0);
								pd3.put("day_age", i+1);
								date.setHours(j);
								pd3.put("record_datetime", date);
								pd3.put("set_temp", null);
								pd3.put("high_alarm_temp", null);
								pd3.put("low_alarm_temp", null);
								pd3.put("high_temp_warning", null);
								pd3.put("low_temp_warning", null);
								pd3.put("set_humidity", null);
								pd3.put("high_alarm_humidity", null);
								pd3.put("low_alarm_humidity", null);
								pd3.put("high_humidity_warning", null);
								pd3.put("low_humidity_warning", null);
								pd3.put("set_negative_pressure", null);
								pd3.put("high_alarm_negative_pressure", null);
								pd3.put("low_alarm_negative_pressure", null);
								pd3.put("high_negative_pressure_warning", null);
								pd3.put("low_negative_pressure_warning", null);
//						    	pd3.put("set_negative_pressure", set_negative_pressure2+set_negative_pressure*((i+1-day_age)*j));
//						    	pd3.put("set_lux", set_lux2+set_lux*((i-day_age)*24+j));
//						    	pd3.put("high_lux", high_lux2+high_lux*((i-day_age)*24+j));
//						    	pd3.put("low_lux", low_lux2+low_lux*((i-day_age)*24+j));
								pd3.put("set_lux", set_lux3);
								pd3.put("high_lux", high_lux3);
								pd3.put("low_lux", low_lux3);
								pd3.put("high_lux_warning", high_lux_warning3);
								pd3.put("low_lux_warning", low_lux_warning3);
//								pd3.put("set_lux", set_lux2+set_lux*((day_age3-1-day_age)*24+24));
//								pd3.put("high_lux", high_lux2+high_lux*((day_age3-1-day_age)*24+24));
//								pd3.put("low_lux", low_lux2+low_lux*((day_age3-1-day_age)*24+24));
//								pd3.put("high_lux_warning", high_lux_warning2+high_lux_warning*((day_age3-1-day_age)*24+24));
//								pd3.put("low_lux_warning", low_lux_warning2+low_lux_warning*((day_age3-1-day_age)*24+24));
								pd3.put("set_co2", null);
								pd3.put("high_alarm_co2", null);
								pd3.put("high_co2_warning", null);
								pd3.put("set_water_deprivation", null);
								pd3.put("high_water_deprivation", null);
								pd3.put("low_water_deprivation", null);
								if(startTime<=j && j<endTime){
									pd3.put("is_start", "0");
								}else{
									pd3.put("is_start", "1");
								}
								list.add(pd3);
//								alarmService.saveSBDayageTempSub(pd3);
							}
						}
						if(choose ){//&& day_age3 == Integer.valueOf(pageData.get(pageData.size()-1).get("day_age").toString()).intValue()*7
							for(int i=day_age3;i<Integer.valueOf(pd.get("dage").toString()).intValue();i++){
								for(int j=1;j<=24;j++){
									PageData pd3 = new PageData();
									pd3.put("uid_num", pdID);
									pd3.put("alarm_type", pd.get("alarm_type"));
									pd3.put("create_person",userId);
									pd3.put("create_date", new Date());
									pd3.put("create_time", new Date());
									pd3.put("modify_person",userId);
									pd3.put("modify_date", new Date());
									pd3.put("modify_time", new Date());
									pd3.put("farmId", pd.get("farmId"));
									pd3.put("houseId", pd.get("houseId"));
									Date date = new Date();
									date.setMinutes(0);
									date.setSeconds(0);
									pd3.put("day_age", i+1);
									date.setHours(j);
									pd3.put("record_datetime", date);
									pd3.put("set_temp", null);
									pd3.put("high_alarm_temp", null);
									pd3.put("low_alarm_temp", null);
									pd3.put("high_temp_warning", null);
									pd3.put("low_temp_warning", null);
									pd3.put("set_humidity", null);
									pd3.put("high_alarm_humidity", null);
									pd3.put("low_alarm_humidity", null);
									pd3.put("high_humidity_warning", null);
									pd3.put("low_humidity_warning", null);
									pd3.put("set_negative_pressure", null);
									pd3.put("high_alarm_negative_pressure", null);
									pd3.put("low_alarm_negative_pressure", null);
									pd3.put("high_negative_pressure_warning", null);
									pd3.put("low_negative_pressure_warning", null);
									pd3.put("set_lux", set_lux3);
									pd3.put("high_lux", high_lux3);
									pd3.put("low_lux", low_lux3);
									pd3.put("high_lux_warning", high_lux_warning3);
									pd3.put("low_lux_warning", low_lux_warning3);
									pd3.put("set_co2", null);
									pd3.put("high_alarm_co2", null);
									pd3.put("high_co2_warning", null);
									pd3.put("set_water_deprivation", null);
									pd3.put("high_water_deprivation", null);
									pd3.put("low_water_deprivation", null);
									if(startTime<=j && j<endTime){
										pd3.put("is_start", "0");
									}else{
										pd3.put("is_start", "1");
									}
									list.add(pd3);
								}
							}
						}
						alarmService.saveSBDayageTempSub(list);
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==3){
						//二氧化碳插入
						for(int i=day_age;i<day_age3;i++){
							for(int j=1;j<=24;j++){
								PageData pd3 = new PageData();
								pd3.put("uid_num", pdID);
								pd3.put("alarm_type", pd.get("alarm_type"));
								pd3.put("create_person",userId);
								pd3.put("create_date", new Date());
								pd3.put("create_time", new Date());
								pd3.put("modify_person",userId);
								pd3.put("modify_date", new Date());
								pd3.put("modify_time", new Date());
								pd3.put("farmId", pd.get("farmId"));
								pd3.put("houseId", pd.get("houseId"));
								Date date = new Date();
								date.setMinutes(0);
								date.setSeconds(0);
								pd3.put("day_age", i+1);
								date.setHours(j);
								pd3.put("record_datetime", date);
								pd3.put("set_temp", null);
								pd3.put("high_alarm_temp", null);
								pd3.put("low_alarm_temp", null);
								pd3.put("high_temp_warning", null);
								pd3.put("low_temp_warning", null);
								pd3.put("set_humidity", null);
								pd3.put("high_alarm_humidity", null);
								pd3.put("low_alarm_humidity", null);
								pd3.put("high_humidity_warning", null);
								pd3.put("low_humidity_warning", null);
								pd3.put("set_negative_pressure", null);
								pd3.put("high_alarm_negative_pressure", null);
								pd3.put("low_alarm_negative_pressure", null);
								pd3.put("high_negative_pressure_warning", null);
								pd3.put("low_negative_pressure_warning", null);
								pd3.put("set_lux", null);
								pd3.put("high_lux", null);
								pd3.put("low_lux", null);
								pd3.put("high_lux_warning", null);
								pd3.put("low_lux_warning", null);
								pd3.put("set_co2", set_co22+set_co2*((i-day_age)*24+j));
								pd3.put("high_alarm_co2", high_alarm_co22+high_alarm_co2*((i-day_age)*24+j));
								pd3.put("high_co2_warning", high_co2_warning2+high_co2_warning*((i-day_age)*24+j));
//						    	pd3.put("low_alarm_co2", low_alarm_co22+low_alarm_co2*((i+1-day_age)*j));
								pd3.put("set_water_deprivation", null);
								pd3.put("high_water_deprivation", null);
								pd3.put("low_water_deprivation", null);
								pd3.put("is_start", "0");
								list.add(pd3);
//								alarmService.saveSBDayageTempSub(pd3);
							}
						}
						alarmService.saveSBDayageTempSub(list);
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==5){
						//湿度插入
						for(int i=day_age;i<day_age3;i++){
							for(int j=1;j<=24;j++){
								PageData pd3 = new PageData();
								pd3.put("uid_num", pdID);
								pd3.put("alarm_type", pd.get("alarm_type"));
								pd3.put("create_person",userId);
								pd3.put("create_date", new Date());
								pd3.put("create_time", new Date());
								pd3.put("modify_person",userId);
								pd3.put("modify_date", new Date());
								pd3.put("modify_time", new Date());
								pd3.put("farmId", pd.get("farmId"));
								pd3.put("houseId", pd.get("houseId"));
								Date date = new Date();
								date.setMinutes(0);
								date.setSeconds(0);
								pd3.put("day_age", i+1);
								date.setHours(j);
								pd3.put("record_datetime", date);
								pd3.put("set_temp", null);
								pd3.put("high_alarm_temp", null);
								pd3.put("low_alarm_temp", null);
								pd3.put("high_temp_warning", null);
								pd3.put("low_temp_warning", null);
								pd3.put("set_humidity", set_humidity2+set_humidity*((i-day_age)*24+j));
								pd3.put("high_alarm_humidity", high_alarm_humidity2+high_alarm_humidity*((i-day_age)*24+j));
								pd3.put("low_alarm_humidity", low_alarm_humidity2+low_alarm_humidity*((i-day_age)*24+j));
								pd3.put("high_humidity_warning", high_humidity_warning2+high_humidity_warning*((i-day_age)*24+j));
								pd3.put("low_humidity_warning", low_humidity_warning2+low_humidity_warning*((i-day_age)*24+j));
								pd3.put("set_negative_pressure", null);
								pd3.put("high_alarm_negative_pressure", null);
								pd3.put("low_alarm_negative_pressure", null);
								pd3.put("high_negative_pressure_warning", null);
								pd3.put("low_negative_pressure_warning", null);
								pd3.put("set_lux", null);
								pd3.put("high_lux", null);
								pd3.put("low_lux", null);
								pd3.put("high_lux_warning", null);
								pd3.put("low_lux_warning", null);
								pd3.put("set_co2", null);
								pd3.put("high_alarm_co2", null);
								pd3.put("high_co2_warning", null);
								pd3.put("set_water_deprivation", null);
								pd3.put("high_water_deprivation", null);
								pd3.put("low_water_deprivation", null);
								pd3.put("is_start", "0");
								list.add(pd3);
//								alarmService.saveSBDayageTempSub(pd3);
							}
						}
						if(choose){
							for(int i=day_age3;i<Integer.valueOf(pd.get("dage").toString()).intValue();i++){
								for(int j=1;j<=24;j++){
									PageData pd3 = new PageData();
									pd3.put("uid_num", pdID);
									pd3.put("alarm_type", pd.get("alarm_type"));
									pd3.put("create_person",userId);
									pd3.put("create_date", new Date());
									pd3.put("create_time", new Date());
									pd3.put("modify_person",userId);
									pd3.put("modify_date", new Date());
									pd3.put("modify_time", new Date());
									pd3.put("farmId", pd.get("farmId"));
									pd3.put("houseId", pd.get("houseId"));
									Date date = new Date();
									date.setMinutes(0);
									date.setSeconds(0);
									pd3.put("day_age", i+1);
									date.setHours(j);
									pd3.put("record_datetime", date);
									pd3.put("set_temp", null);
									pd3.put("high_alarm_temp", null);
									pd3.put("low_alarm_temp", null);
									pd3.put("high_temp_warning", null);
									pd3.put("low_temp_warning", null);
									pd3.put("set_humidity", set_humidity3);
									pd3.put("high_alarm_humidity", high_alarm_humidity3);
									pd3.put("low_alarm_humidity", low_alarm_humidity3);
									pd3.put("high_humidity_warning", high_humidity_warning3);
									pd3.put("low_humidity_warning", low_humidity_warning3);
									pd3.put("set_negative_pressure", null);
									pd3.put("high_alarm_negative_pressure", null);
									pd3.put("low_alarm_negative_pressure", null);
									pd3.put("high_negative_pressure_warning", null);
									pd3.put("low_negative_pressure_warning", null);
									pd3.put("set_lux", null);
									pd3.put("high_lux", null);
									pd3.put("low_lux", null);
									pd3.put("high_lux_warning", null);
									pd3.put("low_lux_warning", null);
									pd3.put("set_co2", null);
									pd3.put("high_alarm_co2", null);
									pd3.put("high_co2_warning", null);
									pd3.put("set_water_deprivation", null);
									pd3.put("high_water_deprivation", null);
									pd3.put("low_water_deprivation", null);
									pd3.put("is_start", "0");
									list.add(pd3);
								}
							}
						}
						alarmService.saveSBDayageTempSub(list);
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==6){
						//负压插入
						for(int i=day_age;i<day_age3;i++){
							for(int j=1;j<=24;j++){
								PageData pd3 = new PageData();
								pd3.put("uid_num", pdID);
								pd3.put("alarm_type", pd.get("alarm_type"));
								pd3.put("create_person",userId);
								pd3.put("create_date", new Date());
								pd3.put("create_time", new Date());
								pd3.put("modify_person",userId);
								pd3.put("modify_date", new Date());
								pd3.put("modify_time", new Date());
								pd3.put("farmId", pd.get("farmId"));
								pd3.put("houseId", pd.get("houseId"));
								Date date = new Date();
								date.setMinutes(0);
								date.setSeconds(0);
								pd3.put("day_age", i+1);
								date.setHours(j);
								pd3.put("record_datetime", date);
								pd3.put("set_temp", null);
								pd3.put("high_alarm_temp", null);
								pd3.put("low_alarm_temp", null);
								pd3.put("high_temp_warning", null);
								pd3.put("low_temp_warning", null);
								pd3.put("set_humidity", null);
								pd3.put("high_alarm_humidity", null);
								pd3.put("low_alarm_humidity", null);
								pd3.put("high_humidity_warning", null);
								pd3.put("low_humidity_warning", null);
								pd3.put("set_negative_pressure", set_negative_pressure2+set_negative_pressure*((i-day_age)*24+j));
								pd3.put("high_alarm_negative_pressure", high_alarm_negative_pressure2+high_alarm_negative_pressure*((i-day_age)*24+j));
								pd3.put("low_alarm_negative_pressure", high_alarm_negative_pressure2+high_alarm_negative_pressure*((i-day_age)*24+j));
								pd3.put("high_negative_pressure_warning", high_negative_pressure_warning2+high_negative_pressure_warning*((i-day_age)*24+j));
								pd3.put("low_negative_pressure_warning", high_negative_pressure_warning2+high_negative_pressure_warning*((i-day_age)*24+j));
								pd3.put("set_lux", null);
								pd3.put("high_lux", null);
								pd3.put("low_lux", null);
								pd3.put("high_lux_warning", null);
								pd3.put("low_lux_warning", null);
								pd3.put("set_co2", null);
								pd3.put("high_alarm_co2", null);
								pd3.put("high_co2_warning", null);
//						    	pd3.put("low_alarm_co2", low_alarm_co22+low_alarm_co2*((i+1-day_age)*j));
								pd3.put("set_water_deprivation", null);
								pd3.put("high_water_deprivation", null);
								pd3.put("low_water_deprivation", null);
								pd3.put("is_start", "0");
								list.add(pd3);
//								alarmService.saveSBDayageTempSub(pd3);
							}
						}
						alarmService.saveSBDayageTempSub(list);
					}else{
						//耗水插入
						for(int i=day_age;i<day_age3;i++){
							for(int j=1;j<=24;j++){
								PageData pd3 = new PageData();
								pd3.put("uid_num", pdID);
								pd3.put("alarm_type", pd.get("alarm_type"));
								pd3.put("create_person",userId);
								pd3.put("create_date", new Date());
								pd3.put("create_time", new Date());
								pd3.put("modify_person",userId);
								pd3.put("modify_date", new Date());
								pd3.put("modify_time", new Date());
								pd3.put("farmId", pd.get("farmId"));
								pd3.put("houseId", pd.get("houseId"));
								Date date = new Date();
								date.setMinutes(0);
								date.setSeconds(0);
								pd3.put("day_age", i+1);
								date.setHours(j);
								pd3.put("record_datetime", date);
								pd3.put("set_temp", null);
								pd3.put("high_alarm_temp", null);
								pd3.put("low_alarm_temp", null);
								pd3.put("set_lux", null);
								pd3.put("high_lux", null);
								pd3.put("low_lux", null);
								pd3.put("set_co2", null);
								pd3.put("high_alarm_co2", null);
								pd3.put("set_water_deprivation", set_water_deprivation2+set_water_deprivation*((i-day_age)*24+j));
								pd3.put("high_water_deprivation", high_water_deprivation2+high_water_deprivation*((i-day_age)*24+j));
								pd3.put("low_water_deprivation", low_water_deprivation2+low_water_deprivation*((i-day_age)*24+j));
								pd3.put("is_start", "0");
								list.add(pd3);
//								alarmService.saveSBDayageTempSub(pd3);
							}
						}
						alarmService.saveSBDayageTempSub(list);
					}
                    
					if(choose2){
					PageData pd4 = new PageData();
					pd4.put("uid_num",uid_num);
					pd4.put("alarm_type", pd.get("alarm_type"));
					if(choose){
						pd4.put("day_age1", day_age);
					}
					if(choose2){
						if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2){
							pd4.put("day_age1", day_age);
						}else{
						pd4.put("day_age2", day_age2);
						}
					}
					alarmService.deleteSBDayageTempSub(pd4);
					}
					List<PageData> list2 = new ArrayList<PageData>();
					if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==1 && choose2){
						//修改相邻记录的温度
						for(int i=day_age3;i<day_age2;i++){
							for(int j=1;j<=24;j++){
								PageData pd5 = new PageData();
								pd5.put("uid_num",uid_num);
								pd5.put("create_person",userId);
								pd5.put("create_date", new Date());
								pd5.put("create_time", new Date());
								pd5.put("modify_person",userId);
								pd5.put("modify_date", new Date());
								pd5.put("modify_time", new Date());
								pd5.put("alarm_type", pd.get("alarm_type"));
								Date date2 = new Date();
								date2.setMinutes(0);
								date2.setSeconds(0);
								pd5.put("day_age", i+1);
								date2.setHours(j);
								pd5.put("record_datetime", date2);
								pd5.put("set_temp", set_temp3+set_temp1*((i-day_age3)*24+j));
								pd5.put("high_alarm_temp",high_alarm_temp3+high_alarm_temp1*((i-day_age3)*24+j));
								pd5.put("low_alarm_temp",low_alarm_temp3+low_alarm_temp1*((i-day_age3)*24+j));
								pd5.put("high_temp_warning",high_temp_warning3+high_temp_warning1*((i-day_age3)*24+j));
								pd5.put("low_temp_warning",low_temp_warning3+low_temp_warning1*((i-day_age3)*24+j));
								pd5.put("set_humidity", null);
								pd5.put("high_alarm_humidity", null);
								pd5.put("low_alarm_humidity", null);
								pd5.put("high_humidity_warning", null);
								pd5.put("low_humidity_warning", null);
								pd5.put("set_negative_pressure", null);
								pd5.put("high_alarm_negative_pressure", null);
								pd5.put("low_alarm_negative_pressure", null);
								pd5.put("high_negative_pressure_warning", null);
								pd5.put("low_negative_pressure_warning", null);
								pd5.put("set_lux",null);
								pd5.put("high_lux",null);
								pd5.put("low_lux",null);
								pd5.put("high_lux_warning",null);
								pd5.put("low_lux_warning",null);
								pd5.put("set_co2",null);
								pd5.put("high_alarm_co2",null);
								pd5.put("high_co2_warning",null);
								pd5.put("set_water_deprivation", null);
								pd5.put("high_water_deprivation",null);
								pd5.put("low_water_deprivation",null);
								pd5.put("is_start", "0");
								list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd4);
							}
						}
						alarmService.saveSBDayageTempSub(list2);
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==2 && choose2){
						//修改相邻记录的负压
						for(int i=day_age3;i<day_age2-7;i++){
							for(int j=1;j<=24;j++){
								PageData pd5 = new PageData();
								pd5.put("uid_num",pdID);
								pd5.put("create_person",userId);
								pd5.put("create_date", new Date());
								pd5.put("create_time", new Date());
								pd5.put("modify_person",userId);
								pd5.put("modify_date", new Date());
								pd5.put("modify_time", new Date());
								pd5.put("alarm_type", pd.get("alarm_type"));
								Date date2 = new Date();
								date2.setMinutes(0);
								date2.setSeconds(0);
								pd5.put("day_age", i+1);
								date2.setHours(j);
								pd5.put("record_datetime", date2);
								pd5.put("set_temp", null);
								pd5.put("high_alarm_temp",null);
								pd5.put("low_alarm_temp",null);
								pd5.put("high_temp_warning",null);
								pd5.put("low_temp_warning",null);
								pd5.put("set_humidity", null);
								pd5.put("high_alarm_humidity", null);
								pd5.put("low_alarm_humidity", null);
								pd5.put("high_humidity_warning", null);
								pd5.put("low_humidity_warning", null);
								pd5.put("set_negative_pressure", null);
								pd5.put("high_alarm_negative_pressure", null);
								pd5.put("low_alarm_negative_pressure", null);
								pd5.put("high_negative_pressure_warning", null);
								pd5.put("low_negative_pressure_warning", null);
//							    	pd5.put("set_lux",set_lux3+set_lux1*((i-day_age3)*24+j));
//							    	pd5.put("high_lux",high_lux3+high_lux1*((i-day_age3)*24+j));
//							    	pd5.put("low_lux",low_lux3+low_lux1*((i-day_age3)*24+j));
//								pd5.put("set_lux", set_lux2+set_lux*((day_age3-1-day_age)*24+24));
//								pd5.put("high_lux", high_lux2+high_lux*((day_age3-1-day_age)*24+24));
//								pd5.put("low_lux", low_lux2+low_lux*((day_age3-1-day_age)*24+24));
//								pd5.put("high_lux_warning", high_lux_warning2+high_lux_warning*((day_age3-1-day_age)*24+24));
//								pd5.put("low_lux_warning", low_lux_warning2+low_lux_warning*((day_age3-1-day_age)*24+24));
								pd5.put("set_lux", set_lux3);
								pd5.put("high_lux", high_lux3);
								pd5.put("low_lux", low_lux3);
								pd5.put("high_lux_warning", high_lux_warning3);
								pd5.put("low_lux_warning", low_lux_warning3);
//								pd5.put("set_lux",set_lux3+set_lux1*((day_age2-1-day_age3)*24+24));
//								pd5.put("high_lux",high_lux3+high_lux1*((day_age2-1-day_age3)*24+24));
//								pd5.put("low_lux",low_lux3+low_lux1*((day_age2-1-day_age3)*24+24));
//								pd5.put("high_lux_warning",high_lux_warning3+high_lux_warning1*((day_age2-1-day_age3)*24+24));
//								pd5.put("low_lux_warning",low_lux_warning3+low_lux_warning1*((day_age2-1-day_age3)*24+24));
								pd5.put("set_co2",null);
								pd5.put("high_alarm_co2",null);
								pd5.put("high_co2_warning",null);
								pd5.put("set_water_deprivation", null);
								pd5.put("high_water_deprivation",null);
								pd5.put("low_water_deprivation",null);
								if(startTime<=j && j<endTime){
									pd5.put("is_start", "0");
								}else{
									pd5.put("is_start", "1");
								}
								list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd4);
							}
						}
						alarmService.saveSBDayageTempSub(list2);
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==3 && high_alarm_co21 != 0){
						//修改相邻记录的二氧化碳
						for(int i=day_age3;i<day_age2;i++){
							for(int j=1;j<=24;j++){
								PageData pd5 = new PageData();
								pd5.put("uid_num",uid_num);
								pd5.put("create_person",userId);
								pd5.put("create_date", new Date());
								pd5.put("create_time", new Date());
								pd5.put("modify_person",userId);
								pd5.put("modify_date", new Date());
								pd5.put("modify_time", new Date());
								pd5.put("alarm_type", pd.get("alarm_type"));
								Date date2 = new Date();
								date2.setMinutes(0);
								date2.setSeconds(0);
								pd5.put("day_age", i+1);
								date2.setHours(j);
								pd5.put("record_datetime", date2);
								pd5.put("set_temp", null);
								pd5.put("high_alarm_temp",null);
								pd5.put("low_alarm_temp",null);
								pd5.put("high_temp_warning",null);
								pd5.put("low_temp_warning",null);
								pd5.put("set_humidity", null);
								pd5.put("high_alarm_humidity", null);
								pd5.put("low_alarm_humidity", null);
								pd5.put("high_humidity_warning", null);
								pd5.put("low_humidity_warning", null);
								pd5.put("set_negative_pressure", null);
								pd5.put("high_alarm_negative_pressure", null);
								pd5.put("low_alarm_negative_pressure", null);
								pd5.put("high_negative_pressure_warning", null);
								pd5.put("low_negative_pressure_warning", null);
								pd5.put("set_lux",null);
								pd5.put("high_lux",null);
								pd5.put("low_lux",null);
								pd5.put("high_lux_warning",null);
								pd5.put("low_lux_warning",null);
								pd5.put("set_co2",set_co23+set_co21*((i-day_age3)*24+j));
								pd5.put("high_alarm_co2",high_alarm_co23+high_alarm_co21*((i-day_age3)*24+j));
								pd5.put("high_co2_warning",high_co2_warning3+high_co2_warning1*((i-day_age3)*24+j));
								pd5.put("set_water_deprivation", null);
								pd5.put("high_water_deprivation",null);
								pd5.put("low_water_deprivation",null);
								pd5.put("is_start", "0");
								list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd4);
							}
						}
						alarmService.saveSBDayageTempSub(list2);
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==5 && choose2){
						//修改相邻记录的湿度
						for(int i=day_age3;i<day_age2;i++){
							for(int j=1;j<=24;j++){
								PageData pd5 = new PageData();
								pd5.put("uid_num",uid_num);
								pd5.put("create_person",userId);
								pd5.put("create_date", new Date());
								pd5.put("create_time", new Date());
								pd5.put("modify_person",userId);
								pd5.put("modify_date", new Date());
								pd5.put("modify_time", new Date());
								pd5.put("alarm_type", pd.get("alarm_type"));
								Date date2 = new Date();
								date2.setMinutes(0);
								date2.setSeconds(0);
								pd5.put("day_age", i+1);
								date2.setHours(j);
								pd5.put("record_datetime", date2);
								pd5.put("set_temp", null);
								pd5.put("high_alarm_temp",null);
								pd5.put("low_alarm_temp",null);
								pd5.put("high_temp_warning",null);
								pd5.put("low_temp_warning",null);
								pd5.put("set_humidity", set_humidity3+set_humidity1*((i-day_age3)*24+j));
								pd5.put("high_alarm_humidity", high_alarm_humidity3+high_alarm_humidity1*((i-day_age3)*24+j));
								pd5.put("low_alarm_humidity", low_alarm_humidity3+low_alarm_humidity1*((i-day_age3)*24+j));
								pd5.put("high_humidity_warning", high_humidity_warning3+high_humidity_warning1*((i-day_age3)*24+j));
								pd5.put("low_humidity_warning", low_humidity_warning3+low_humidity_warning1*((i-day_age3)*24+j));
								pd5.put("set_negative_pressure", null);
								pd5.put("high_alarm_negative_pressure", null);
								pd5.put("low_alarm_negative_pressure", null);
								pd5.put("high_negative_pressure_warning", null);
								pd5.put("low_negative_pressure_warning", null);
								pd5.put("set_lux",null);
								pd5.put("high_lux",null);
								pd5.put("low_lux",null);
								pd5.put("high_lux_warning",null);
								pd5.put("low_lux_warning",null);
								pd5.put("set_co2",null);
								pd5.put("high_alarm_co2",null);
								pd5.put("high_co2_warning",null);
								pd5.put("set_water_deprivation", null);
								pd5.put("high_water_deprivation",null);
								pd5.put("low_water_deprivation",null);
								pd5.put("is_start", "0");
								list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd4);
							}
						}
						alarmService.saveSBDayageTempSub(list2);
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==6 && high_alarm_negative_pressure1 != 0){
						//修改相邻记录的二氧化碳
						for(int i=day_age3;i<day_age2;i++){
							for(int j=1;j<=24;j++){
								PageData pd5 = new PageData();
								pd5.put("uid_num",uid_num);
								pd5.put("create_person",userId);
								pd5.put("create_date", new Date());
								pd5.put("create_time", new Date());
								pd5.put("modify_person",userId);
								pd5.put("modify_date", new Date());
								pd5.put("modify_time", new Date());
								pd5.put("alarm_type", pd.get("alarm_type"));
								Date date2 = new Date();
								date2.setMinutes(0);
								date2.setSeconds(0);
								pd5.put("day_age", i+1);
								date2.setHours(j);
								pd5.put("record_datetime", date2);
								pd5.put("set_temp", null);
								pd5.put("high_alarm_temp",null);
								pd5.put("low_alarm_temp",null);
								pd5.put("high_temp_warning",null);
								pd5.put("low_temp_warning",null);
								pd5.put("set_humidity", null);
								pd5.put("high_alarm_humidity", null);
								pd5.put("low_alarm_humidity", null);
								pd5.put("high_humidity_warning", null);
								pd5.put("low_humidity_warning", null);
								pd5.put("set_negative_pressure", set_negative_pressure3+set_negative_pressure1*((i-day_age3)*24+j));
								pd5.put("high_alarm_negative_pressure", high_alarm_negative_pressure3+high_alarm_negative_pressure1*((i-day_age3)*24+j));
								pd5.put("low_alarm_negative_pressure", low_alarm_negative_pressure3+low_alarm_negative_pressure1*((i-day_age3)*24+j));
								pd5.put("high_negative_pressure_warning", high_negative_pressure_warning3+high_negative_pressure_warning1*((i-day_age3)*24+j));
								pd5.put("low_negative_pressure_warning", low_negative_pressure_warning3+low_negative_pressure_warning1*((i-day_age3)*24+j));
								pd5.put("set_lux",null);
								pd5.put("high_lux",null);
								pd5.put("low_lux",null);
								pd5.put("high_lux_warning",null);
								pd5.put("low_lux_warning",null);
								pd5.put("set_co2",null);
								pd5.put("high_alarm_co2",null);
								pd5.put("high_co2_warning",null);
								pd5.put("set_water_deprivation", null);
								pd5.put("high_water_deprivation",null);
								pd5.put("low_water_deprivation",null);
								pd5.put("is_start", "0");
								list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd4);
							}
						}
						alarmService.saveSBDayageTempSub(list2);
					}else if(Integer.valueOf(pd.get("alarm_type").toString()).intValue()==4 &&(set_water_deprivation1 != 0 || high_water_deprivation1 != 0 || low_water_deprivation1 != 0)){
						//修改相邻记录的耗水
						for(int i=day_age3;i<day_age2;i++){
							for(int j=1;j<=24;j++){
								PageData pd5 = new PageData();
								pd5.put("uid_num",uid_num);
								pd5.put("create_person",userId);
								pd5.put("create_date", new Date());
								pd5.put("create_time", new Date());
								pd5.put("modify_person",userId);
								pd5.put("modify_date", new Date());
								pd5.put("modify_time", new Date());
								pd5.put("alarm_type", pd.get("alarm_type"));
								Date date2 = new Date();
								date2.setMinutes(0);
								date2.setSeconds(0);
								pd5.put("day_age", i+1);
								date2.setHours(j);
								pd5.put("record_datetime", date2);
								pd5.put("set_temp", null);
								pd5.put("high_alarm_temp",null);
								pd5.put("low_alarm_temp",null);
								pd5.put("set_lux",null);
								pd5.put("high_lux",null);
								pd5.put("low_lux",null);
								pd5.put("set_co2",null);
								pd5.put("high_alarm_co2",null);
								pd5.put("set_water_deprivation", set_water_deprivation3+set_water_deprivation1*((i-day_age3)*24+j));
								pd5.put("high_water_deprivation",high_water_deprivation3+high_water_deprivation1*((i-day_age3)*24+j));
								pd5.put("low_water_deprivation",low_water_deprivation3+low_water_deprivation1*((i-day_age3)*24+j));
								pd5.put("is_start", "0");
								list2.add(pd5);
//								alarmService.saveSBDayageTempSub(pd4);
							}
						}
						alarmService.saveSBDayageTempSub(list2);
					}

				}
			}


			j2.setMsg("1");
			j2.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			j2.setMsg("2");
		}
		return j2;
	}
	
	@RequestMapping("/setWarning")
	public void setWarning(HttpServletResponse response,HttpServletRequest request) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		if(Integer.valueOf(pd.getString("alarm_type")).intValue()==1){
			if(Integer.valueOf(pd.get("wv").toString()).intValue()==1){
				float gbWarning = (float) (Float.valueOf(pd.get("high_alarm_temp").toString()).floatValue()*0.9);
				if(Integer.valueOf(pd.get("set_temp").toString()).intValue() ==0){
					j.setObj(gbWarning);
				} else if(Integer.valueOf(pd.get("set_temp").toString()).intValue() !=0 && gbWarning>Float.valueOf(pd.get("set_temp").toString()).floatValue()){
					j.setObj(gbWarning);
				}else{
					gbWarning = (Float.valueOf(pd.get("high_alarm_temp").toString()).floatValue()+Float.valueOf(pd.get("set_temp").toString()).floatValue())/2;
					j.setObj(gbWarning);
				}
					
			}else{
				float gbWarning2 = (float) (Float.valueOf(pd.get("low_alarm_temp").toString()).floatValue()*1.1);
				if(Integer.valueOf(pd.get("set_temp").toString()).intValue()==0){
					j.setObj(gbWarning2);
				} else if(Integer.valueOf(pd.get("set_temp").toString()).intValue() !=0 && gbWarning2<Float.valueOf(pd.get("set_temp").toString()).floatValue()){
					j.setObj(gbWarning2);
				}else{
					gbWarning2 = (Float.valueOf(pd.get("low_alarm_temp").toString()).floatValue()+Float.valueOf(pd.get("set_temp").toString()).floatValue())/2;
					j.setObj(gbWarning2);
				}
			}
		}else if(Integer.valueOf(pd.getString("alarm_type")).intValue()==5){
			if(Integer.valueOf(pd.get("wv").toString()).intValue()==5){
				float gbWarning5 = (float) (Float.valueOf(pd.get("high_alarm_humidity").toString()).floatValue()*0.9);
				if(Integer.valueOf(pd.get("set_humidity").toString()).intValue() ==0){
					j.setObj(gbWarning5);
				} else if(Integer.valueOf(pd.get("set_humidity").toString()).intValue() !=0 && gbWarning5>Float.valueOf(pd.get("set_humidity").toString()).floatValue()){
					j.setObj(gbWarning5);
				}else{
					gbWarning5 = (Float.valueOf(pd.get("high_alarm_humidity").toString()).floatValue()+Float.valueOf(pd.get("set_humidity").toString()).floatValue())/2;
					j.setObj(gbWarning5);
				}
					
			}else{
				float gbWarning6 = (float) (Float.valueOf(pd.get("low_alarm_humidity").toString()).floatValue()*1.1);
				if(Integer.valueOf(pd.get("set_humidity").toString()).intValue()==0){
					j.setObj(gbWarning6);
				} else if(Integer.valueOf(pd.get("set_humidity").toString()).intValue() !=0 && gbWarning6<Float.valueOf(pd.get("set_humidity").toString()).floatValue()){
					j.setObj(gbWarning6);
				}else{
					gbWarning6 = (Float.valueOf(pd.get("low_alarm_humidity").toString()).floatValue()+Float.valueOf(pd.get("set_humidity").toString()).floatValue())/2;
					j.setObj(gbWarning6);
				}
			}
		}else{
			if(Integer.valueOf(pd.getString("wv")).intValue()==3){
				float gbWarning3 = (float) (Float.valueOf(pd.get("high_lux").toString()).floatValue()*0.9);
				if(Integer.valueOf(pd.get("set_lux").toString()).intValue() ==0 ){
					j.setObj(gbWarning3);
				} else if(Integer.valueOf(pd.get("set_lux").toString()).intValue() !=0 && gbWarning3>Float.valueOf(pd.get("set_lux").toString()).floatValue()){
					j.setObj(gbWarning3);
				}else{
					gbWarning3 = (Float.valueOf(pd.get("high_lux").toString()).floatValue()+Float.valueOf(pd.get("set_lux").toString()).floatValue())/2;
					j.setObj(gbWarning3);
				}	
			}else{
				float gbWarning4 = (float) (Float.valueOf(pd.get("low_lux").toString()).floatValue()*1.1);
				if(Integer.valueOf(pd.get("set_lux").toString()).intValue()==0){
					j.setObj(gbWarning4);
				} else if(Integer.valueOf(pd.get("set_lux").toString()).intValue() !=0 && gbWarning4<Float.valueOf(pd.get("set_lux").toString()).floatValue()){
					j.setObj(gbWarning4);
				}else{
					gbWarning4 = (Float.valueOf(pd.get("low_lux").toString()).floatValue()+Float.valueOf(pd.get("set_lux").toString()).floatValue())/2;
					j.setObj(gbWarning4);
				}
			}
		}
		j.setSuccess(true);	
		super.writeJson(j, response);
	}
	
	
	
}
