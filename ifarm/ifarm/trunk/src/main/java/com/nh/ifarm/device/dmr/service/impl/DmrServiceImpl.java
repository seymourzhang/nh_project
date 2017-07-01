package com.nh.ifarm.device.dmr.service.impl;


import com.nh.ifarm.device.dmr.entity.DMRSource;
import com.nh.ifarm.device.dmr.entity.DmrQuota;
import com.nh.ifarm.device.dmr.service.DmrService;
import com.nh.ifarm.device.entity.DeviceQuotaType;
import com.nh.ifarm.device.service.impl.DeviceServiceImpl;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

//import static java.time.LocalDateTime.now;

/**
 * 大牧人控制器服务类
 * Created by Raymon on 7/7/2016.
 */
@Service
public class DmrServiceImpl extends DeviceServiceImpl implements DmrService {

    /**
     *
     */
    final static String verifyUrl="http://123.56.196.58:8080/cis/director?action=com.dmr.cis.web.actions.service.Auth.todo&appId=JSONTESTFORSHMTC";
    /**
     * 访问URL
     */
    final static String URL="http://123.56.196.58:8080/cis/director?action=com.dmr.cis.web.actions.service.Partner.get&appKey=";

    /**
     * 设备号
     */
    public final static int DEVICETYPE=3;


    /**
     * 构造函数
     */
    public DmrServiceImpl() {
    }


    /**
     * 启动服务
     */
    public void start(){
        writeData(getDeviceList());
    }


    /**
     * 获取设备列表
     * @return 设备列表
     */
    public List<PageData> getDeviceList(){
        List<PageData> pdList = findDevice(DEVICETYPE);
        return pdList;
    }

    /**
     * 更新至数据库
     */
     void writeData(List<PageData> list){
        for(Object obj : list.toArray()) {
            PageData pd = (PageData)obj;
            DMRSource dmrs= this.DMRInit(pd);//初始化一定要才会有DMRSource对象
            dmrs=this.DMRServer(dmrs);//正式调用返回dmrs
            DmrThreadImpl dti =new DmrThreadImpl(dmrs.getDmrQuota(),this);
            dti.start();
        }

    }

    public DaoSupport dao() {
        return dao;
    }

//    public static void main(String[] args) {
//        DmrServiceImpl dm=new DmrServiceImpl();
//        DMRSource dmrs= dm.DMRInit();//初始化一定要才会有DMRSource对象
//        dmrs.setAppKey("6F834A2F3798BBF6");//后期传入校验key不用变更
//        dmrs.setControlID("101");//控制器ID 目前201,101
//        dmrs=dm.DMRServer(dmrs);//正式调用返回dmrs
//    }



    /**
     * 获取大牧人数据源
     * @param pd 数据对象
     * @return 大牧人数据源
     */
    public DMRSource DMRInit(PageData pd){
        DMRSource dmrs = new DMRSource();
        DmrQuota dq = new DmrQuota();
        dq.APP_KEY.setValue((String)pd.get(dq.APP_KEY.getCode()));
        dq.DEVICE_ID.setValue((String)pd.get(dq.DEVICE_ID.getCode()));
        dq.DEVICE_TYPE.setValue(String.valueOf(DEVICETYPE));
        dq.PORT_ID.setValue(String.valueOf(0));
        dq.setDeviceKeyId();
        dmrs.setAppKey(dq.APP_KEY.getValue());
        dmrs.setControlID(dq.DEVICE_ID.getValue());
        dmrs.setDmrQuota(dq);
        return dmrs;
    }

    public DMRSource DMRServer(DMRSource dmrs ){
        String returnState="";
        URL url = null;
        HttpURLConnection httpurlconnection = null;
        try {
            url = new URL(verifyUrl);
            //以post方式请求
            httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setDoOutput(true);
            httpurlconnection.setRequestMethod("POST");
            httpurlconnection.getOutputStream().flush();
            httpurlconnection.getOutputStream().close();
            //获取响应代码
            int code = httpurlconnection.getResponseCode();
            if(code!=200){
                returnState= "连接失败:9002";
            } else {
                for(DeviceQuotaType dqt:dmrs.getDmrQuota().getList()) {
                    String strUrl = URL + dmrs.getAppKey() + "&deviceId=" + dmrs.getControlID() + "&propertyId=" + dqt.getCommandNumber();
                    String value=null;
                    url = new URL(strUrl);
                    //以post方式请求
                    httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setDoOutput(true);
                    httpurlconnection.setRequestMethod("POST");
                    httpurlconnection.getOutputStream().flush();
                    httpurlconnection.getOutputStream().close();

                    //获取响应代码
                    code = httpurlconnection.getResponseCode();
                    if(code!=200){
                        returnState= "连接失败:9002";
                    } else {
                        //获取页面内容
                        java.io.InputStream in= httpurlconnection .getInputStream();
                        java.io.BufferedReader breader =
                                new BufferedReader(new InputStreamReader(in , "utf-8"));  //gb2312
                        String str=breader.readLine();
                        while(str != null){
                            //值
                            value=changeJson(str);
                            str=breader.readLine();
                        }
                        if(null != value)
                            dqt.setValue(value);
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            returnState= "连接失败:9001";
        } finally{
//                    System.out.println(dqt.getName()+":"+dqt.getCommandNumber()+"-"+dqt.getValue());
            if(httpurlconnection!=null){
                httpurlconnection.disconnect();
            }
        }
        return dmrs;
    }


    /**
     *
     * @TODO 解析大牧人返回值
     * @param {"result:1","type:float","value:20.0"}
     * @return 20.0
     * @Date 2016-6-23
     * @author loyeWen
     *
     */
    public String changeJson(String str){
        String rt=null;
        try {
            str=str.replaceAll("'", "");
            str=str.replaceAll("\"", "");
//            if(str.length()<3){
//                //数据格式错误1
//                return null;
//            }
            str=str.substring(1, str.length()-1);

            String st[]=str.split(",");

//            if(st.length!=3 && st[2].split(":").length!=2){
//                //数据格式错误2
//                return null;
//            }
            if("TIME" == st[1].split(":")[1].toUpperCase()) {
                rt = st[2].toUpperCase().replaceAll("VALUE:", "");
            }
            else
                rt = st[2].split(":")[1];
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return rt;
        }
    }
    /**
     *
     * @TODO 大牧人控制器写入
     *
     * @Date 2016-6-23
     * @author loyeWen
     *
     */
    public String dmrWirte(DMRSource dmrs){
        String strURL=URL + dmrs.getAppKey()+"&deviceId="+dmrs.getControlID()+"&propertyId=" ;
//                        +dmrs.getList().get(0).getDataK()
//                        +"&value="+dmrs.getList().get(0).getDataV()+"&time=111";
        String returnState="";
        try {
            URL url = null;
            HttpURLConnection httpurlconnection = null;
            try{
                url = new URL(strURL);

                //以post方式请求
                httpurlconnection = (HttpURLConnection) url.openConnection();
                httpurlconnection.setDoOutput(true);
                httpurlconnection.setRequestMethod("POST");
                httpurlconnection.getOutputStream().flush();
                httpurlconnection.getOutputStream().close();

                //获取响应代码
                int code = httpurlconnection.getResponseCode();
//		      System.out.println("code   " + code);

                if(code!=200){
                    returnState= "连接失败:9002";
                }else{
                    returnState="设置成功";
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return returnState;
    }
}
