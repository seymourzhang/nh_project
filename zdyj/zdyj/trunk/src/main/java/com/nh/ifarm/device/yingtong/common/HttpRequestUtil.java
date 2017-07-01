package com.nh.ifarm.device.yingtong.common;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * @ClassName: HttpRequestUtil
 * @Date 2016-6-23
 * @author loyeWen
 *
 */
public class HttpRequestUtil {
	
	private static Logger mLogger =Logger.getLogger(HttpRequestUtil.class);  
	
	public static String reqGet(String tURL){
		URL url = null;
		HttpURLConnection conn = null;
		InputStream instr  = null; 
		String resSTR = "";
		try {
			mLogger.debug("HttpGetRequest URL：" + tURL);
			url = new URL(tURL);
			conn = (HttpURLConnection) url.openConnection();
		    conn.setReadTimeout(40000);
		    conn.setConnectTimeout(10000);
		    conn.setRequestMethod("GET");
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
		    conn.setRequestProperty("Content-Type", "application/json");
		    
		    instr = conn.getInputStream();   
            byte[] bis = IOUtils.toByteArray(instr);
            String ResponseString = new String(bis, "UTF-8");  
           if ((ResponseString == null) || ("".equals(ResponseString.trim()))) {
                mLogger.info("return null");
           }
           resSTR = ResponseString ;
           mLogger.info("返回结果：" + resSTR);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
		    conn.disconnect();
		}
		return resSTR;
	}
	
	public static String reqPost(String tURL,String tData){
		URL url = null;
		HttpURLConnection conn = null;
		InputStream instr  = null; 
		String resSTR = "";
		try {
			mLogger.debug("HttpPostRequest URL：" + tURL);
			mLogger.debug("HttpPostRequest Paras：" + tData);
			url = new URL(tURL);
			conn = (HttpURLConnection) url.openConnection();
		    conn.setReadTimeout(1000000);
		    conn.setConnectTimeout(150000);
		    conn.setRequestMethod("GET");
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
		    conn.setRequestProperty("Content-Type", "application/json");
		    
		    OutputStream output = new BufferedOutputStream(conn.getOutputStream());
		    output.write(tData.getBytes("UTF-8"));
		    output.flush();
		    
		    instr = conn.getInputStream();   
            byte[] bis = IOUtils.toByteArray(instr);
            String ResponseString = new String(bis, "UTF-8");  
           if ((ResponseString == null) || ("".equals(ResponseString.trim()))) {
                mLogger.info("return null");
           }
           resSTR = ResponseString ;
   		   mLogger.info("返回结果：" + resSTR);
		}catch (Exception e){
			e.printStackTrace();
//			mLogger.info(e.getMessage());
		}finally {
		    conn.disconnect();
		}
		return resSTR;
	}
}
