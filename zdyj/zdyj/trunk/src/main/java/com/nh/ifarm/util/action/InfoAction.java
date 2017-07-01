package com.nh.ifarm.util.action;

import com.nh.ifarm.util.common.HttpRequest;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.common.PubFun;
import com.nh.ifarm.util.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by LeLe on 3/2/2017.
 */
@Controller
@RequestMapping("/info")
public class InfoAction extends BaseAction {

    @Autowired
    TestService testService;

    /**
     * 版本信息页面
     * raymon 2016-10-18
     * @return
     */
    @RequestMapping(value="/version")
    public ModelAndView showOrgManage()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String projectPath =  PubFun.getWEBINFpath().replace("WEB-INF/","");
        String versionPatchFilePath = projectPath + "version.patch";
        String version = getModifiedTime(versionPatchFilePath);
        pd.put("version",version);
//        pd.put("projectPath",projectPath);
        mv.addObject("pd",pd);
        mv.setViewName("modules/util/info");
        return mv;
    }

    /**
     * 获取天气信息
     * raymon 2017-05-17
     * @return
     */
    @RequestMapping(value="/weather")
    public void getWeatherInfo(HttpServletResponse response, HttpServletRequest request, HttpSession session){
        Json j=new Json();
        PageData pd = this.getPageData();
        String rt = "";
        try{
            rt = InfoAction.getWeatherString("http://wthrcdn.etouch.cn/weather_mini?citykey=" + pd.getString("citykey"));
            j.setSuccess(true);
            j.setObj(rt);
        } catch (Exception e){
            j.setSuccess(false);
            j.setObj(rt);
            j.setMsg(e.getMessage());
        } finally {
            super.writeJson(j, response);
        }
    }

    public static String getWeatherString(String url) throws UnsupportedEncodingException {
        URL realUrl = null;
        ByteArrayOutputStream out = null;

        try {
            //真实地址
            realUrl = new URL(url);

            //打开连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

            //设置连接属性
            connection.setRequestProperty("accept", "application/xhtml+xml,application/json,application/xml;charset=utf-8, text/javascript, */*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("contentType", "utf-8");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");

            //FileOutputStream out = new FileOutputStream("e:/text.txt");
            //这里获取的数据时压缩格式的数据所以用gzip进行解压缩
            GZIPInputStream gip = new GZIPInputStream(connection.getInputStream());
            out = new ByteArrayOutputStream();
            //缓冲
            byte []buffer = new byte[1024];
            int len ;
            while((len = gip.read(buffer))!=-1){
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }finally{
            //关闭流
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        //把字节数据转化为字符串返回回去
        return (new String(out.toByteArray(), "utf-8"));
    }

    String getModifiedTime(String patchFilePath){
        File f = new File(patchFilePath);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(f.lastModified());
        return  sdf.format(cal.getTime());
    }

    /**
     * 测试页面
     * raymon 2017-03-16
     * @return
     */
    @RequestMapping(value="/test")
    public ModelAndView test()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        testService.saveTest(pd);
        mv.addObject("pd",pd);
        mv.setViewName("modules/util/test");
        return mv;
    }
}
