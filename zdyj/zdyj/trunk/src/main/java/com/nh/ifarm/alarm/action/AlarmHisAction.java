package com.nh.ifarm.alarm.action;

import com.nh.ifarm.alarm.service.AlarmHisService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.user.service.SDUserService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
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
 * @ClassName: AlarmHisAction
 * @Date 2016-12-26
 * @author yoven
 * 报警历史
 */

@Controller
@RequestMapping("/alarmHis")
public class AlarmHisAction extends BaseAction{
    @Autowired 
	private AlarmHisService alarmHisService;
    
    @Autowired
	private SDUserService userService;

	@RequestMapping("/showAlarmHis")
	public ModelAndView showAlarmHis(HttpSession session) throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("/modules/alarm/alarmHis");
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		//设置人员
		pd.put("id",user.getId());
		pd.put("obj_type",2);
		pd.put("user_status",1);
		pd.put("freeze_status",0);
		pd.put("listFlag",1);
		mv.addObject("userList",userService.getUserList(pd));
		return mv;
	}
	
	@RequestMapping("/selectAlarmHisByCondition")
	public void selectAlarmHisByCondition(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> alarmHis = alarmHisService.selectAlarmHisByCondition(pd);
          j.setSuccess(true);
          j.setObj(alarmHis);
        super.writeJson(j, response);
	}
	
	@RequestMapping("/selectAlarmHisDetailByCondition")
	public void selectAlarmHisDetailByCondition(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> alarmHis = alarmHisService.selectAlarmHisDetailByCondition(pd);
          j.setSuccess(true);
          j.setObj(alarmHis);
        super.writeJson(j, response);
	}
	
	@RequestMapping("/selectHouseAlarmHisByCondition")
	public void selectHouseAlarmHisByCondition(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> alarmHis = alarmHisService.selectHouseAlarmHisByCondition(pd);
          j.setSuccess(true);
          j.setObj(alarmHis);
        super.writeJson(j, response);
	}
	
	@RequestMapping("/selectHouseAlarmHisDetailByCondition")
	public void selectHouseAlarmHisDetailByCondition(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> alarmHis = alarmHisService.selectHouseAlarmHisDetailByCondition(pd);
          j.setSuccess(true);
          j.setObj(alarmHis);
        super.writeJson(j, response);
	}
	
}
