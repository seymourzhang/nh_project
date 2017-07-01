package com.nh.ifarm.user.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.nh.ifarm.util.common.Const;



/**
 * 登录验证过滤器
 */
public class LoginFilter extends  OncePerRequestFilter {
	
	 @Override  
	    protected void doFilterInternal(HttpServletRequest request,  
	            HttpServletResponse response, FilterChain filterChain)  
	            throws ServletException, IOException {  
	  
	        // 不拦截的url  
	        String[] notFilter = new String[] {"/zdyj/index.jsp","/zdyj/modules/user/login.jsp","/zdyj/monitor/responseall","/zdyj/alarmCurr/mobileAlarm",
					"/zdyj/alarmCurr/mobileAlarmCurr","/zdyj/alarmCurr/mobilesSolveAlarmCurr","/zdyj/login/login",
					"/zdyj/login/login","/zdyj/login/outLogin","/zdyj/login/login_toLogin",
					"/zdyj/checkVersion.jsp","/zdyj/nht_zdyj*"};
	  
	        // 请求的url  
	        String url = request.getRequestURI();  
	          
//	        if(url.indexOf("UniqueduHome") != -1){  
	            boolean doFilter = chek(notFilter,url);  
	            if(doFilter){  
	                Object obj = request.getSession().getAttribute(Const.SESSION_USER);  
	                if(null==obj){  
	                    // 如果session中不存在登录者实体，则弹出框提示重新登录  
	                    PrintWriter out = response.getWriter();  
	                    String loginPage = request.getContextPath()+"/index.jsp";  
	                    StringBuilder builder = new StringBuilder();  
	                    builder.append("<script type=\"text/javascript\">");  
	                    builder.append("window.top.location.href='");  
	                    builder.append(loginPage);  
	                    builder.append("';");  
	                    builder.append("</script>");  
	                    out.print(builder.toString());  
	                }else {  
	                    filterChain.doFilter(request, response);  
	                }  
	            }else{  
	                filterChain.doFilter(request, response);  
	            }  
//	        }else{  
//	            filterChain.doFilter(request, response);  
//	        }  
	    }  
	      
	    /** 
	     * @param notFilter 不拦截的url 
	     * @param url ：请求的url 
	     * @return false：不拦截 
	     *         true：拦截 
	     */  
	    public boolean chek(String[] notFilter,String url){  
	        //url以css和js结尾的不进行拦截  
	        if(url.endsWith(".css") || url.endsWith(".js")|| url.endsWith(".png")|| url.endsWith(".png")|| url.endsWith(".jpg")|| url.endsWith(".ico")|| url.endsWith(".css")){  
	            return false;  
	        }
			//url 包含Mobile/ 则认为是 App端请求，不做拦截
			if (url.indexOf("Mobile/") > 0) {
				return false;
			}
			//含有notFilter中的任何一个则不进行拦截
	        for (String s : notFilter) {
	            if (url.indexOf(s.replace("*", "")) != -1) {
	                return false;
	            }
	        }
	        return true;  
	    }  
	
	
	
	
	
//	@Override
//	public void init(FilterConfig arg0) throws ServletException {
//
//	}
//
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response,
//			FilterChain chain) throws IOException, ServletException {
//		// 获得在下面代码中要用的request,response,session对象
//		HttpServletRequest servletRequest = (HttpServletRequest) request;
//		HttpServletResponse servletResponse = (HttpServletResponse) response;
//		HttpSession session = servletRequest.getSession();
//		 // 获得用户请求的URI
//		String path = servletRequest.getRequestURI();
//		
//		// 登陆页面无需过滤
//		if((path.indexOf("/zljk/modules/user/login.jsp") > -1)||(path.indexOf("/zljk/login/login")>-1)||(path.indexOf("/zljk/login/outLogin")>-1)||(path.indexOf("/zljk/login/login_toLogin")>-1)) {
//			chain.doFilter(servletRequest, servletResponse);
//			return;
//		}
//		// 从session里取用户信息
//		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
//		 // 判断如果没有取到用户信息,就跳转到登陆页面
//	    if (user == null) {
//	    	// 跳转到登陆页面
//	    	servletResponse.sendRedirect("/zljk/modules/user/login.jsp");
//		} else {
//			 // 已经登陆,继续此次请求
//			 chain.doFilter(request, response);
//		}
//		
//		
//
//	}
//	@Override
//	public void destroy() {
//
//	}
	
}
