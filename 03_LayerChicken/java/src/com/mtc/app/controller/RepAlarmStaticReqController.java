package com.mtc.app.controller;

import com.mtc.app.service.BaseQueryService;
import com.mtc.app.service.SDUserOperationService;
import com.mtc.common.util.DealSuccOrFail;
import com.mtc.common.util.PubFun;
import com.mtc.common.util.constants.Constants;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Seymour on 2017/3/8.
 */
@Controller
@RequestMapping("/rep/alarm")
public class RepAlarmStaticReqController {

    private static Logger mLogger = Logger.getLogger(AlarmReqController.class);

    @Autowired
    private BaseQueryService tBaseQueryService;

    @Autowired
    private SDUserOperationService operationService;

    @RequestMapping("/alarmStatic")
    public void alarmStatis(HttpServletRequest request, HttpServletResponse response) throws Exception {
        mLogger.info("=====New start executing RepAlarmStatisReqController.alarmStatis");
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        try {
            String paraStr = PubFun.getRequestPara(request);
            mLogger.info("paraStr=" + paraStr);
            JSONObject jsonObject = new JSONObject(paraStr);
            mLogger.info("jsonObject=" + jsonObject.toString());
            /** 业务处理开始，查询、增加、修改、或删除 **/
            operationService.insert(SDUserOperationService.MENU_WARNCOUNTLOG, SDUserOperationService.OPERATION_SELECT, jsonObject.optInt("id_spa"));

            JSONObject prarObj = jsonObject.getJSONObject("params");
            int id_spa = jsonObject.getInt("id_spa");
            int HouseId = prarObj.getInt("HouseId");
            int FarmId = prarObj.getInt("FarmId");
            int FarmBreedId = prarObj.getInt("FarmBreedId");

            String tDateSql = "SELECT max(datediff(ifnull(a.market_date, curdate()),a.place_date) + a.place_day_age) as age from s_b_layer_house_breed a where farm_breed_id = " + FarmBreedId;
            String displayMaxAge = tBaseQueryService.selectStringByAny(tDateSql);
            String tErrorContent = "Null";
            if (displayMaxAge == null || displayMaxAge.equals("")) {
                tErrorContent = "暂无批次信息！";
            }

            JSONArray tJSONArray = new JSONArray();
            String SQL = "";
            if (HouseId != 0) {
                SQL = "SELECT" +
                        "  date_format(age_list.datadate,'%Y-%m-%d') as specialDate," +
                        "  age_list.houseid as house_id," +
                        "  s_f_getHouseName(age_list.houseid) AS housename," +
                        "  age_list.dateAge AS dayAge," +
                        "  SUM(CASE WHEN alarmData.alarm_code LIKE '%H' THEN 1 ELSE 0 END) AS temp_H," +
                        "  SUM(CASE WHEN alarmData.alarm_code LIKE '%L' THEN 1 ELSE 0 END) AS temp_L," +
                        "  SUM(CASE WHEN alarmData.alarm_code = 'C0001' THEN 1 ELSE 0 END) AS point_temp_alarm," +
                        "  SUM(CASE WHEN alarmData.alarm_code = 'C0002' THEN 1 ELSE 0 END) AS power_status," +
                        "  SUM(CASE WHEN alarmData.alarm_code = 'C0003' THEN 1 ELSE 0 END) AS co2" +
                        " from ( select " + displayMaxAge + "-sds.IncreID + 1 as dateAge," +
                                                " sbh.house_id as houseid," +
                                                " date_add(sbh.place_date,INTERVAL " + displayMaxAge + " - sds.IncreID + 1 - sbh.place_day_age day) as datadate " +
                                " from s_d_serialno sds,s_b_layer_house_breed sbh where sds.IncreID <= 30 " +
                                " and sbh.farm_breed_id = " + FarmBreedId + " and sbh.house_id = " + HouseId + ") as age_list " +
                        " LEFT JOIN (SELECT sbi.* FROM s_b_alarm_inco sbi WHERE s_f_getHouseBreedId(sbi.house_id) = sbi.house_breed_id and sbi.house_id = " + HouseId + " " +
                                    " UNION ALL SELECT sbd.* FROM s_b_alarm_done sbd WHERE s_f_getHouseBreedId(sbd.house_id) = sbd.house_breed_id and sbd.house_id = " + HouseId +
                                    ") as alarmData on 1=1 " +
                        " and alarmData.alarm_time BETWEEN age_list.datadate and date_add(age_list.datadate,INTERVAL 1 day) " +
                        " GROUP BY age_list.datadate,age_list.houseid,age_list.dateAge ";
            } else {
                /*全场*/
                SQL = "SELECT " +
                        "  ae.specialAge, " +
                        "  bd.age                        AS dayAge, " +
                        "  SUM(CASE WHEN ad.alarm_code LIKE '%H' " +
                        "    THEN 1 " +
                        "      ELSE 0 END)               AS temp_H, " +
                        "  SUM(CASE WHEN ad.alarm_code LIKE '%L' " +
                        "    THEN 1 " +
                        "      ELSE 0 END)               AS temp_L, " +
                        "  SUM(CASE WHEN ad.alarm_code = 'C0001' " +
                        "    THEN 1 " +
                        "      ELSE 0 END)               AS point_temp_alarm, " +
                        "  SUM(CASE WHEN ad.alarm_code = 'C0002' " +
                        "    THEN 1 " +
                        "      ELSE 0 END)               AS power_status, " +
                        "  SUM(CASE WHEN ad.alarm_code = 'C0003' " +
                        "    THEN 1 " +
                        "      ELSE 0 END)               AS co2 " +
                        "FROM s_b_breed_detail bd " +
                        "  LEFT JOIN s_b_house_breed hb ON hb.id = bd.house_breed_id " +
                        "  LEFT JOIN (SELECT sbi.* " +
                        "             FROM s_b_alarm_inco sbi " +
                        "             WHERE s_f_getHouseBreedId(sbi.house_id) = sbi.house_breed_id and sbi.house_id = " + HouseId +
                        "             UNION ALL SELECT sbd.* " +
                        "                       FROM s_b_alarm_done sbd " +
                        "                       WHERE s_f_getHouseBreedId(sbd.house_id) = sbd.house_breed_id and sbd.house_id = " + HouseId + ") ad " +
                "                                       ON ad.house_id = hb.house_id " +
                        "                               AND datediff(ad.alarm_time, bd.growth_date) = 0 " +
                        "  LEFT JOIN (select max(bd1.age) specialAge, bd1.house_breed_id, hb1.house_id from s_b_breed_detail bd1 " +
                        "    LEFT JOIN s_b_house_breed hb1 on hb1.id = bd1.house_breed_id " +
                        "  where hb1.farm_breed_id = " + FarmBreedId + ") ae on 1=1 " +
                        "WHERE hb.farm_breed_id = " + FarmBreedId + " " +
                        "      AND bd.age BETWEEN 0 AND ae.specialAge " +
                        "GROUP BY dayAge";
                tErrorContent = "无栋舍信息，查询出错，请联系管理员！";
            }
            mLogger.info("===========RepAlarmStatisReqController.alarmStatic.sql = " + SQL);
            if (tErrorContent.equals("Null")) {
                List<HashMap<String, Object>> Loutcome = tBaseQueryService.selectMapByAny(SQL);
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
                    resJson.put("FarmBreedId", FarmBreedId);
                    resJson.put("alarmDatas", tJSONArray);
                    resJson.put("xAxis", xAxis);
                } else {
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "暂无批次信息！");
                }
            } else {
                resJson.put("Result", "Fail");
                resJson.put("Error", tErrorContent);
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
        mLogger.info("=====Now end executing RepAlarmStaticReqController.alarmStatic");
    }
}
