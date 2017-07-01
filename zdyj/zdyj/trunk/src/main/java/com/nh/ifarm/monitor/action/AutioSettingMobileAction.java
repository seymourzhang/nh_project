package com.nh.ifarm.monitor.action;

import com.nh.ifarm.monitor.service.AutioSettingService;
import com.nh.ifarm.user.service.SBUserImeiService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.common.PubFun;
import com.sun.org.apache.regexp.internal.RE;
//import com.sun.tools.javac.code.Attribute;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Seymour on 2017/1/10.
 */
@Controller
@RequestMapping("/autioSettingMobile")
public class AutioSettingMobileAction extends BaseAction {

    @Autowired
    private AutioSettingService autioSettingService;

    @Autowired
    private SBUserImeiService sbUserImeiService;

    @RequestMapping("/queryInfo")
    public void queryInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JSONObject resJson = new JSONObject();
        String dealRes = "";
        try{
            PageData pd = new PageData();
            pd = this.getPageData();
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//          Date curDate = new Date();

            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            int FarmId = tUserJson.optInt("FarmId");
            String RemindMethod = tUserJson.optString("RemindMethod");
            pd.put("farmId", FarmId);
            pd.put("RemindMethod", RemindMethod);
            pd.put("userId", userId);
            PageData temp = autioSettingService.queryInfo(pd);

            resJson.put("houseAlarmSetting", temp.get("houseAlarmSetting"));
            resJson.put("FarmId", FarmId);
            resJson.put("Error", temp.get("Error"));
            resJson.put("Result", temp.get("Result"));
            dealRes = Constants.RESULT_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            resJson.put("Error", "程序处理错误，请联系管理员！");
            resJson.put("Result", "Fail");
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/saveInfo")
    public void saveInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JSONObject resJson = new JSONObject();
        String dealRes = "";
        try{
            PageData result = new PageData();
            PageData pd = new PageData();
            pd = this.getPageData();
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date();

            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            int FarmId = tUserJson.optInt("FarmId");
            int HouseId = tUserJson.optInt("HouseId");
            String RemindMethod = tUserJson.optString("RemindMethod");
            String status = tUserJson.optString("status");
            JSONArray alarmers = tUserJson.optJSONArray("alarmers");

            pd.put("userId", userId);
            pd.put("farmId", FarmId);
            pd.put("houseId", HouseId);
            pd.put("remindMethod", RemindMethod);
            pd.put("status", status);
            pd.put("alarmers", alarmers);
            pd.put("create_date", curDate);
            pd.put("create_time", curDate);
            pd.put("create_person", userId);
            pd.put("modify_date", curDate);
            pd.put("modify_time", curDate);
            pd.put("modify_person", userId);
            result = autioSettingService.saveInfo(pd);

            resJson.put("Error", result.get("Error"));
            resJson.put("Result", result.get("Result"));
            dealRes = Constants.RESULT_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            resJson.put("Error", "程序处理错误，请联系管理员！");
            resJson.put("Result", "Fail");
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/queryMobileAlarm")
    public void queryMobileAlarm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        String dealRes = "";
        try {
            PageData result = new PageData();
            PageData pd = new PageData();
            pd = this.getPageData();
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);

            Date curDate = new Date();

            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            String imeiNo = tUserJson.optString("ImeiNo");
            pd.put("imei_no", imeiNo);
            if(!PubFun.isNull(imeiNo)){
                PageData oldInfo = autioSettingService.queryImeiInfo(pd);
                String alarmStatus = "";
                if(oldInfo != null){
                    if("1".equals(oldInfo.get("bak1")) || "0".equals(oldInfo.get("bak1"))){
                        alarmStatus = oldInfo.get("bak1").toString();
                    }else{
                        alarmStatus = "1" ;
                    }
                }else{
                    alarmStatus = "1" ;
                }
                resJson.put("alarmStatus", alarmStatus);
                resJson.put("Error", "");
                resJson.put("Result", "Success");
            }else{
                resJson.put("Error", "查询参数错误，请联系管理员。");
                resJson.put("Result", "Fail");
            }
            dealRes = Constants.RESULT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            resJson.put("Error", "程序处理错误，请联系管理员！");
            resJson.put("Result", "Fail");
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/setMobileAlarm")
    public void setMobileAlarm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        String dealRes = "";
        try {
            PageData result = new PageData();
            PageData pd = new PageData();
            pd = this.getPageData();
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);

            Date curDate = new Date();

            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            String imeiNo = tUserJson.optString("ImeiNo");
            String alarmStatus = tUserJson.optString("alarmStatus");
            if (!PubFun.isNull(imeiNo) && ("0".equals(alarmStatus) || "1".equals(alarmStatus))) {
                HashMap<String, Object> mParas = new HashMap<String, Object>();
                pd.put("imei_no", imeiNo);
                PageData oldInfo = autioSettingService.queryImeiInfo(pd);
                if (oldInfo != null) {
                    oldInfo.put("bak1", alarmStatus);
                    oldInfo.put("create_time", new Date());
                } else {
                    oldInfo = new PageData();
                    oldInfo.put("imei_no", imeiNo);
                    oldInfo.put("user_id", userId);
                    oldInfo.put("bak1", alarmStatus);
                    oldInfo.put("create_time", new Date());
                    oldInfo.put("create_date", new Date());
                    oldInfo.put("create_person", userId);
                }
                sbUserImeiService.insert(oldInfo);
                resJson.put("Error", "");
                resJson.put("Result", "Success");
            } else {
                resJson.put("Error", "查询参数错误，请联系管理员。");
                resJson.put("Result", "Fail");
            }
            dealRes = Constants.RESULT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            resJson.put("Error", "程序处理错误，请联系管理员！");
            resJson.put("Result", "Fail");
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
