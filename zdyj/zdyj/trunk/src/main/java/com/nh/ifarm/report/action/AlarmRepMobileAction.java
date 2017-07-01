package com.nh.ifarm.report.action;

import com.nh.ifarm.alarm.service.AlarmCurrService;
import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.report.service.AlarmHistService;
import com.nh.ifarm.report.service.RepCurveMobileService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.common.PubFun;
import com.nh.ifarm.util.service.OrganService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Seymour on 2017/06/23
 */
@Controller
@RequestMapping("/alarmMobile")
public class AlarmRepMobileAction extends BaseAction {

    @Autowired
    private OrganService organService;

    @Autowired
    private AlarmCurrService alarmCurrService;

    @Autowired
    private AlarmHistService alarmHistService;

    @Autowired
    private BatchManageService batchManageService;

    @Autowired
    private RepCurveMobileService repCurveMobileService;

    @RequestMapping("/alarmDealQuery")
    public void alarmDealQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        JSONObject jsonObject = new JSONObject(aa);

        int userId = jsonObject.optInt("id_spa");
        JSONObject tUserJson = jsonObject.getJSONObject("params");
        int FarmId = tUserJson.optInt("FarmId");

        pd.put("parent_id", FarmId);
        List<PageData> lpd = organService.getOrgListById(pd);
        JSONArray alarmMessages = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
        for (PageData a : lpd) {
            JSONObject alarmMessage = new JSONObject();
            JSONArray alarmDatas = new JSONArray();
            int houseId = Integer.parseInt(a.get("id").toString());
            pd.put("houseId", houseId);
            alarmMessage.put("HouseId", houseId);
            alarmMessage.put("HouseName", a.get("name_cn"));
            List<PageData> data = alarmCurrService.selectByCondition(pd);
            for (PageData b : data) {
                JSONObject alarmData = new JSONObject();
                alarmData.put("aDayAge", b.get("day_age"));
                alarmData.put("aDate", sdf.format(sd.parse(b.get("alarm_time").toString())));
                alarmData.put("aTime", sdf1.format(sd.parse(b.get("alarm_time").toString())));
                alarmData.put("alarmID", b.get("id"));
                alarmData.put("alarmCode", b.get("alarm_code"));
                alarmData.put("alarmName", b.get("alarm_name"));
                alarmData.put("realValue", b.get("actual_value"));
                alarmData.put("targetValue", b.get("set_value"));
                alarmData.put("process_status", b.get("deal_status"));
                alarmDatas.put(alarmData);
            }
            alarmMessage.put("CurAlarmData", alarmDatas);
            if(alarmDatas.length() != 0){
                alarmMessages.put(alarmMessage);
            }
        }
        resJson.put("alarmMessage", alarmMessages);
        resJson.put("Result", "Success");
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/alarmDealDelay")
    public void alarmDealDelay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        JSONObject jsonObject = new JSONObject(aa);

        int userId = jsonObject.optInt("id_spa");
        JSONObject tUserJson = jsonObject.getJSONObject("params");
        JSONArray curAlarmData = tUserJson.getJSONArray("CurAlarmData");

        for (int i = 0; i < curAlarmData.length(); ++i) {
            JSONObject data = curAlarmData.optJSONObject(i);
            pd.put("id", data.get("alarmID"));
            pd.put("house_id", data.get("houseId"));
            pd.put("deal_delay", data.get("delayTime"));
            pd.put("deal_status", "02");
            pd.put("deal_time", new Date());
            pd.put("response_person", userId);
            alarmCurrService.updateAlarm(pd);
        }
        resJson.put("Error", "");
        resJson.put("Result", "Success");
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/alarmInfo")
    public void alarmInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        try {
            JSONObject jsonObject = new JSONObject(aa);

            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            int FarmId = tUserJson.optInt("FarmId");

            pd.put("user_id", userId);
            pd.put("parent_id", FarmId);
            pd.put("farmId", FarmId);
            PageData pa = new PageData();
            pa.put("farm_id", FarmId);
            List<PageData> houses = organService.getOrgList(pd);
            JSONArray alarmData = new JSONArray();
            for (PageData house : houses) {
                pd.put("houseId", house.get("id"));
                pa.put("house_code", house.get("id"));
                List<PageData> batch = batchManageService.getCreateBatchData(pa);
                JSONObject alarm = new JSONObject();
                JSONObject alarm1 = new JSONObject();//前区
                JSONObject alarm2 = new JSONObject();//中区
                JSONObject alarm3 = new JSONObject();//后区
                JSONObject alarmAvg = new JSONObject();//平均
                if (batch.size() != 0) {
                    pd.put("batchNo", batch.get(0).get("batchNo"));
                    PageData lpd = alarmHistService.selectAlarmForMobile(pd);
                    if (lpd != null) {
                        alarm.put("houseID", lpd.get("house_id"));
                        alarm.put("houseName", lpd.get("house_name"));
                        alarm.put("dayAge", lpd.get("dayAge"));
                        alarm1.put("H", lpd.get("temp_in1_H_alarm"));
                        alarm1.put("L", lpd.get("temp_in1_L_alarm"));
                        alarm.put("temp_in1_alarm", alarm1);
                        alarm2.put("H", lpd.get("temp_in2_H_alarm"));
                        alarm2.put("L", lpd.get("temp_in2_L_alarm"));
                        alarm.put("temp_in2_alarm", alarm2);
                        alarm3.put("H", lpd.get("temp_in3_H_alarm"));
                        alarm3.put("L", lpd.get("temp_in3_L_alarm"));
                        alarm.put("temp_in3_alarm", alarm3);
                        alarmAvg.put("H", lpd.get("temp_avg_H_alarm"));
                        alarmAvg.put("L", lpd.get("temp_avg_L_alarm"));
                        alarm.put("temp_avg_alarm", alarmAvg);
                        alarm.put("point_temp_alarm", lpd.get("point_temp_alarm"));
                        alarm.put("power_status", lpd.get("power_status_alarm"));
                        alarm.put("co2_alarm", lpd.get("co2_alarm"));
                        alarm.put("lux_alarm", lpd.get("lux_alarm"));
                        alarmData.put(alarm);
                        resJson.put("Result", "Success");
                    }
                } else {
                    alarm.put("houseID", house.get("id"));
                    alarm.put("houseName", house.get("name_cn"));
                    alarm.put("dayAge", 0);
                    alarm1.put("H", 0);
                    alarm1.put("L", 0);
                    alarm.put("temp_in1_alarm", alarm1);
                    alarm2.put("H", 0);
                    alarm2.put("L", 0);
                    alarm.put("temp_in2_alarm", alarm2);
                    alarm3.put("H", 0);
                    alarm3.put("L", 0);
                    alarm.put("temp_in3_alarm", alarm3);
                    alarmAvg.put("H", 0);
                    alarmAvg.put("L", 0);
                    alarm.put("temp_avg_alarm", alarmAvg);
                    alarm.put("point_temp_alarm", 0);
                    alarm.put("power_status", 0);
                    alarm.put("co2_alarm", 0);
                    alarm.put("lux_alarm", 0);
                    alarmData.put(alarm);
                    resJson.put("Result", "Success");
                }
            }
            resJson.put("AlarmData", alarmData);
            resJson.put("Error", "");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (JSONException ee) {
                ee.printStackTrace();
            }
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/queryAlarmLog")
    public void queryAlarmLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        JSONObject jsonObject = new JSONObject(aa);

        int userId = jsonObject.optInt("id_spa");
        JSONObject tUserJson = jsonObject.getJSONObject("params");
        int FarmId = tUserJson.optInt("FarmId");
        int HouseId = tUserJson.optInt("HouseId");
        String AlarmCategory = tUserJson.optString("AlarmCategory");
        int AgeBegin = tUserJson.optInt("AgeBegin");
        int AgeEnd = tUserJson.optInt("AgeEnd");

        JSONArray alarmLog = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

        pd.put("farmId2", FarmId);
        pd.put("houseId2", HouseId);
        pd.put("beginAge", AgeBegin);
        pd.put("endAge", AgeEnd);

        PageData pp = new PageData();
        pp.put("farm_id", FarmId);
        pp.put("house_code", HouseId);
        List<PageData> histHist = new ArrayList<>();
        PageData lpd = batchManageService.selectBatchDataForMobile(pp);
        if (lpd != null) {
            String batchNo = lpd.get("batch_no").toString();
            pd.put("batchNo2", batchNo);
            if ("All".equals(AlarmCategory)) {
                histHist = alarmHistService.getAlarmHistDetail(pd);
            } else if ("frontTemp".equals(AlarmCategory)) {
                pd.put("bizCode", "Q%");
                histHist = alarmHistService.getAlarmHistDetail(pd);
            } else if ("middleTemp".equals(AlarmCategory)) {
                pd.put("bizCode", "Z%");
                histHist = alarmHistService.getAlarmHistDetail(pd);
            } else if ("backTemp".equals(AlarmCategory)) {
                pd.put("bizCode", "H%");
                histHist = alarmHistService.getAlarmHistDetail(pd);
            } else if ("pointTemp".equals(AlarmCategory)) {
                pd.put("codeName", "点温差%");
                histHist = alarmHistService.getAlarmHistDetail(pd);
            } else if ("avgTemp".equals(AlarmCategory)) {
                pd.put("bizCode", "P%");
                histHist = alarmHistService.getAlarmHistDetail(pd);
            } else if ("powerStatus".equals(AlarmCategory)) {
                pd.put("bizCode", "%point%");
                histHist = alarmHistService.getAlarmHistDetail(pd);
            } else if ("lux".equals(AlarmCategory)) {
                pd.put("codeName", "%光照");
                histHist = alarmHistService.getAlarmHistDetail(pd);
            } else if ("co2".equals(AlarmCategory)) {
                pd.put("codeName", "二氧化碳%");
                histHist = alarmHistService.getAlarmHistDetail(pd);
            }
        }
        if (!histHist.isEmpty()) {
            for (PageData pageData : histHist) {
                JSONObject temp = new JSONObject();
                temp.put("aDayAge", pageData.get("date_age").toString());
                temp.put("aDate", sdf1.format(sdf.parse(pageData.get("alarm_time").toString())));
                temp.put("aTime", sdf1.format(sdf.parse(pageData.get("alarm_time").toString())));
                temp.put("alarmID", pageData.get("id"));
                temp.put("alarmCode", pageData.get("alarm_code"));
                temp.put("alarmName", pageData.get("alarm_Name").toString().replace("报警",""));
                temp.put("realValue", pageData.get("actual_value").toString().replace(" 度","").replace(" ml/m3", "").replace(" lux",""));
                temp.put("targetValue", pageData.get("set_value").toString().replace(" 度","").replace(" ml/m3", "").replace(" lux",""));

                String deal_statusTxt = "";
                if("01".equals(pageData.get("deal_status"))){
                    deal_statusTxt = "未处理";
                }else if("02".equals(pageData.get("deal_status"))){
                    deal_statusTxt = "处理中";
                }else if("03".equals(pageData.get("deal_status"))){
                    deal_statusTxt = "已处理";
                }

                temp.put("process_status", deal_statusTxt);
                temp.put("deal_person", pageData.get("response_person"));

                if(PubFun.isNull(pageData.get("deal_time"))){
                    temp.put("deal_time", "");
                }else{
                    temp.put("deal_time", sdf1.format(sdf.parse(pageData.get("deal_time").toString())));
                }

                temp.put("is_normal", pageData.get("is_remove"));

                if(PubFun.isNull(pageData.get("continue_time"))){
                    temp.put("last_time", "");
                }else{
                    temp.put("last_time", pageData.get("continue_time").toString().replace(" 分钟",""));
                }
                alarmLog.put(temp);
            }
            resJson.put("Result", "Success");
        } else {
            resJson.put("Error", "暂无数据！");
            resJson.put("Result", "Fail");
        }
        resJson.put("AlarmLog", alarmLog);
        resJson.put("HouseId", HouseId);
        resJson.put("Error", "");
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/alarmStatic")
    public void alarmStatic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        JSONObject jsonObject = new JSONObject(aa);
        try {
            JSONObject prarObj = jsonObject.getJSONObject("params");
            int id_spa = jsonObject.optInt("id_spa");
            int FarmId = prarObj.optInt("FarmId");
            int HouseId = prarObj.optInt("HouseId");
            String BreedBatchId = prarObj.optString("BreedBatchId");
            String BreedBatchNo = prarObj.optString("BreedBatchNo");
            JSONArray tJSONArray = new JSONArray();
            pd.put("house_id", HouseId);
            pd.put("farm_id", FarmId);
            pd.put("batch_id", BreedBatchId);
            pd.put("batch_no", BreedBatchNo);
            List<PageData> Loutcome = repCurveMobileService.getAlarmStatistic(pd);
            if (Loutcome.size() > 0) {
                JSONArray xAxis = new JSONArray();
                JSONObject point = new JSONObject();
                JSONObject tempHeight = new JSONObject();
                JSONObject tempLow = new JSONObject();
                JSONObject co2 = new JSONObject();
                JSONObject power = new JSONObject();
                List<String> pointList = new ArrayList<>();
                List<String> tempH = new ArrayList<>();
                List<String> tempL = new ArrayList<>();
                List<String> co2List = new ArrayList<>();
                List<String> powerList = new ArrayList<>();
                for (HashMap<String, Object> hashMap : Loutcome) {
                    xAxis.put(hashMap.get("dayAge").toString());
                    pointList.add(hashMap.get("point_temp_alarm").toString());
                    tempH.add(hashMap.get("temp_H").toString());
                    tempL.add(hashMap.get("temp_L").toString());
                    co2List.add(hashMap.get("co2").toString());
                    powerList.add(hashMap.get("power_status").toString());
                }
                point.put("name", "点温差");
                point.put("data", pointList);

                tempHeight.put("name", "高温");
                tempHeight.put("data", tempH);

                tempLow.put("name", "低温");
                tempLow.put("data", tempL);

                co2.put("name", "CO2");
                co2.put("data", co2List);

                power.put("name", "断电");
                power.put("data", powerList);

                tJSONArray.put(point);
                tJSONArray.put(tempHeight);
                tJSONArray.put(tempLow);
                tJSONArray.put(co2);
                tJSONArray.put(power);
                resJson.put("Result", "Success");
                resJson.put("FarmBreedId", BreedBatchId);
                resJson.put("alarmDatas", tJSONArray);
                resJson.put("xAxis", xAxis);
            } else {
                resJson.put("Result", "Fail");
                resJson.put("Error", "暂无批次信息！");
            }
            dealRes = Constants.RESULT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson = new JSONObject();
                resJson.put("Exception", e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            dealRes = Constants.RESULT_FAIL;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
