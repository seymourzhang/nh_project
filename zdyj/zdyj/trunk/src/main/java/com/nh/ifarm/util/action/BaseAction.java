package com.nh.ifarm.util.action;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.common.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping("/baseController")
public class BaseAction {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	
	
	/**
	 * 得到PageData
	 */
	public PageData getPageData(){
		PageData pd = new PageData(this.getRequest());
		if (this.getRequest().getRequestURL().indexOf("Mobile/") > 0){
		} else {
			pd.put("report_ip", PubFun.getPropertyValue("Report.Ip"));
			pd.put("report_port", PubFun.getPropertyValue("Report.Port"));
		}
		return pd;
	}
	
	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	
	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		return request;
	}
	

	/**
	 * 得到分页列表的信息 
	 */
	public Page getPage(){
		
		return new Page();
	}
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}
	
	
	public void writeJson(Object object,HttpServletResponse res) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			json = StringEscapeUtils.unescapeJava(json);
			json = json.replaceAll("\":\"\\{\"","\":\\{\"");
			json = json.replaceAll("\"\\}\"\\,\"","\"\\}\\,\"");

			res.setContentType("text/html;charset=utf-8");
			res.getWriter().write(json);
			res.getWriter().flush();
			res.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//获取IP地址
	public String getIpAddr(HttpServletRequest request) {  
	      String ip = request.getHeader("x-forwarded-for");  
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	         ip = request.getHeader("Proxy-Client-IP");  
	     }  
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	         ip = request.getHeader("WL-Proxy-Client-IP");  
	      }  
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	          ip = request.getRemoteAddr();  
	     }  
	     return ip;  
	} 
	
	/**
	 * 上传文件
	 */
	public String uploadFile(HttpServletRequest request, MultipartFile myfile) {
		if (null != myfile && !myfile.isEmpty()) {
			Date now = new Date();
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy_MM");   
			String dateUrl=dateformat.format(now);
			// 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中
			String realPath = request.getSession().getServletContext()
					.getRealPath("/upload/fileUpload/"+dateUrl);
			File file=new File(realPath);
			if(!file.exists()){
				file.mkdir();
			}
			// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的
			try {
				FileUtils.copyInputStreamToFile(myfile.getInputStream(),
						new File(realPath,now.getTime()+ myfile.getOriginalFilename()));
				
				return "/upload/fileUpload/"+dateUrl+"/"+now.getTime()+ myfile.getOriginalFilename();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 获取用户ID
	 * @return
	 */
	protected int getUserId(){
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);
		return user.getId();
	}

	/**
	 * 获取用户权限
	 * */
	protected PageData getUserRights(PageData pd, HttpSession session){
		if(!StringUtils.isBlank(pd.getString("write_read"))){
			session.setAttribute("write_read", pd.getString("write_read"));
		}else{
			pd.put("write_read", session.getAttribute("write_read"));
		}
		return pd;
	}

}
