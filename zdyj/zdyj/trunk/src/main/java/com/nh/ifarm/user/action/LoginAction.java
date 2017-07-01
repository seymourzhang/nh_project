package com.nh.ifarm.user.action;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONException;
import com.nh.ifarm.util.service.ModuleService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nh.ifarm.system.entity.SDMenu;
import com.nh.ifarm.system.service.SDMenuService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.user.service.SDUserService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DateUtil;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.common.PubFun;


@Controller
@RequestMapping(value="/login")
public class LoginAction extends BaseAction {
	
	@Autowired
	private SDUserService userService;
	
//	@Autowired
//	private SDMenuService sdMenuService;
	
	@Autowired
	private ModuleService moduleService;

	
	
	/**
	 * 访问登录页
	 * varro
	 * 2016-7-6
	 */
	@RequestMapping(value="/login_toLogin")
	public ModelAndView toLogin()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("/modules/user/login");
		mv.addObject("pd",pd);
		//moduleService.service("SDMenuService", "run", );
		return mv;
	}
	

	/**
	 * 移动端用户登陆
	 * varro
	 * 2016-8-1
	 */
	/*@RequestMapping("/mobileLogin")
	public void mobileLogin(HttpServletRequest request,HttpServletResponse response)throws Exception {
		//shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		
		JSONObject resJson = new JSONObject();
		PageData pd = new PageData();
		pd = this.getPageData();
		String dealRes = null ;
		String aa=pd.toString();
		aa=aa.substring(1,aa.length()-2);
		JSONObject jsonObject = new JSONObject(aa);
		
		JSONObject tUserJson = jsonObject.getJSONObject("params");
		String userName= tUserJson.optString("userCode");
		String password= tUserJson.optString("pw");
		String passwd = new SimpleHash("SHA-1", userName, password).toString();	//密码加密
		pd.put("user_code", userName);
		pd.put("user_password", passwd);
		
		pd=userService.getUserBylogin(pd);
		if(pd != null){
			SDUser user=new SDUser();
			user.setId(pd.getInteger("id"));
			user.setUser_code(pd.getString("user_code"));
			user.setUser_password(pd.getString("user_password"));
			user.setUser_real_name(pd.getString("user_real_name"));
			user.setUser_real_name_en(pd.getString("user_real_name_en"));
			user.setUser_mobile_1(pd.getString("user_mobile_1"));
			user.setUser_status(pd.getString("user_status"));
			user.setFreeze_status(pd.getString("freeze_status"));
			session.setAttribute(Const.SESSION_USER, user);
			JSONObject userInfo = genUserInfo(user);
			resJson.put("userinfo", userInfo);
			resJson.put("LoginResult", "Success");
			//shiro加入身份验证
			Subject subject = SecurityUtils.getSubject(); 
		    UsernamePasswordToken token = new UsernamePasswordToken(userName, passwd); 
		    try { 
		        subject.login(token); 
		    } catch (AuthenticationException e) { 
		    	resJson.put("ErrorMsg","身份验证失败！");
		    	dealRes = Constants.RESULT_FAIL ;
		    }
		    if(!pd.getString("user_status").equals("1")){
		    	resJson.put("ErrorMsg","账户异常！");
		    	dealRes = Constants.RESULT_FAIL ;
		    }else{
		    	dealRes = Constants.RESULT_SUCCESS ;
		    }
		}else{
			resJson.put("ErrorMsg","用户名或密码有误！");
			dealRes = Constants.RESULT_FAIL ;
		}
		response.setHeader("Access-Control-Allow-Origin", "*");
		DealSuccOrFail.dealApp(request,response,dealRes,resJson);
	}*/
	
	private JSONObject genUserInfo(SDUser tSDUser) throws JSONException{
		JSONObject userInfo = null;
		if(tSDUser != null){
			userInfo = new JSONObject();
			try {
				userInfo.put("id", tSDUser.getId());
				userInfo.put("name", tSDUser.getUser_code());
				userInfo.put("tele", tSDUser.getUser_mobile_1());
			} catch (org.json.JSONException e) {
				e.printStackTrace();
			}
		}
		return userInfo;
	}
	
	/**
	 * 用户登陆
	 * varro
	 * 2016-7-6
	 */
	@RequestMapping("/login")
	public void login(HttpServletRequest request,HttpServletResponse response)throws Exception {
		Json j=new Json();
		PageData pd = new PageData();
		pd = this.getPageData();
		String userName= pd.getString("user_code");
		String password= pd.getString("user_password");
		
		//shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();

		pd.put("user_status", '1');
		pd.put("user_code", userName);   
		String passwd = new SimpleHash("SHA-1", userName, password).toString();	//密码加密
		pd.put("user_password", passwd);
		pd=userService.getUserBylogin(pd);
		if(pd != null){
			SDUser user=new SDUser();
			user.setId(pd.getInteger("id"));
			user.setUser_code(pd.getString("user_code"));
			user.setUser_password(pd.getString("user_password"));
			user.setUser_real_name(pd.getString("user_real_name"));
			user.setUser_real_name_en(pd.getString("user_real_name_en"));
			user.setUser_mobile_1(pd.getString("user_mobile_1"));
			user.setUser_status(pd.getString("user_status"));
			user.setFreeze_status(pd.getString("freeze_status"));
			session.setAttribute(Const.SESSION_USER, user);
			
			//shiro加入身份验证
			Subject subject = SecurityUtils.getSubject(); 
		    UsernamePasswordToken token = new UsernamePasswordToken(userName, passwd); 
		    try { 
		        subject.login(token); 
		    } catch (AuthenticationException e) { 
		    	j.setMsg("身份验证失败！");
		    }
		    if(!pd.getString("user_status").equals("1")){
		    	j.setMsg("账户异常！");
		    }else{
		    	j.setSuccess(true);
				j.setMsg("登录成功");
		    }
		}else{
			j.setMsg("1");//用户名或密码有误
			//errInfo = "usererror"; 				
		}
		super.writeJson(j, response);
	}
	
	/**
	 * 进入tab标签
	 * @return
	 */
	@RequestMapping("/tab")
	public ModelAndView tab(){
		ModelAndView mav = new ModelAndView("framework/tab");
		return mav;
	}
	
	/**
	 * 进入首页后的默认页面
	 * @return
	 */
	@RequestMapping("/login_default")
	public ModelAndView defaultPage(){
		ModelAndView mav = new ModelAndView("framework/default");
		return mav;
	}
	
	
	
	/**
	 * 退出系统
	 * @param request
	 * @return
	 */
	@RequestMapping("/outLogin")
	public ModelAndView outLogin(HttpServletRequest request) {
		request.getSession().setAttribute(Const.SESSION_USER, null);
		request.getSession().setAttribute(Const.SESSION_allmenuList, null);
		ModelAndView mav = new ModelAndView("/modules/user/login");
		return mav;
	}

}
