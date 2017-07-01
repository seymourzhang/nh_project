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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Seymour on 2017/4/17.
 */
@Controller
@RequestMapping("/layer_report")
public class LayerTempDiffCurveReqController {
    private static Logger mLogger = Logger.getLogger(LayerTempCurveReqController.class);

    @Autowired
    private BaseQueryService mBaseQueryService;

    @Autowired
    private SDUserOperationService sSDUserOperationService;

    @RequestMapping("/queryDiffCurve")
    public void queryDiffCurve(HttpServletRequest request, HttpServletResponse response) {
        mLogger.info("=======Now start executing LayerTempDiffCurveReqController.queryDiffCurve");
        JSONObject resJson = new JSONObject();
        String dealRes = "";
        long startReqTime = System.currentTimeMillis();
        try {
            String paraStr = PubFun.getRequestPara(request);
            mLogger.info("updateFarm.para=" + paraStr);
            JSONObject jsonobject = new JSONObject(paraStr);
            int userId = jsonobject.optInt("id_spa");
            mLogger.info("jsonObject=" + jsonobject.toString());

            String tErrorContent = "Null";

			/* 业务处理开始，查询、增加、修改、或删除 */

            JSONObject params = jsonobject.optJSONObject("params");
            int FarmBreedId = params.optInt("FarmBreedId");
            String DataType = params.optString("DataType");
            String AgeFlag = params.optString("AgeFlag");
            String AgeRange = params.optString("AgeRange");
            String TimeFlag = params.optString("TimeFlag");
            String TimeRange = params.optString("TimeRange");
            String data_date = "null";
            String data_age = "null";
            JSONArray TempDatas = new JSONArray();
            List<HashMap<String, Object>> listMap = null;
            String tSQL = "";
            boolean flag = true;
            sSDUserOperationService.insert(SDUserOperationService.MENU_DATAANALYSIS_TEMPDIFF_CURVE, SDUserOperationService.OPERATION_SELECT, userId);

            String tDateSql = "SELECT max(datediff(ifnull(a.market_date, curdate()),a.place_date) + a.place_day_age) as age from s_b_layer_house_breed a where farm_breed_id = " + FarmBreedId;
            String displayMaxAge = mBaseQueryService.selectStringByAny(tDateSql);
            if (displayMaxAge == null || displayMaxAge.equals("")) {
                tErrorContent = "暂无批次信息！";
            }
            
            if (tErrorContent.equals("Null")) {
                if (DataType.equals("01")) {
                    tSQL = "SELECT (CASE when age_list.datadate >= date_format(curdate(), '%Y-%m-%d') then 'N' else 'Y' end) as dataflag," +
                                    "age_list.datadate," +
                                    "age_list.dateAge AS x_axis," +
                                    "age_list.houseid AS house_id," +
                                    "s_f_getHouseName(age_list.houseid) as house_name," +
                                    "age_list.dateAge         AS data_age," +
                                    "ifnull(tData.point_temp_diff, 0) as point_temp_diff " +
                                    "FROM (select " + displayMaxAge + "-sds.IncreID + 1 as dateAge,sbh.house_id as houseid,date_format(date_add(sbh.place_date,INTERVAL " + displayMaxAge + " - sds.IncreID + 1 - sbh.place_day_age day), '%Y-%m-%d') as datadate from s_d_serialno sds,s_b_layer_house_breed sbh where sds.IncreID <= least(60,"+displayMaxAge+") and sbh.farm_breed_id =  " + FarmBreedId + " " +
                                         ") as age_list " +
                                        "LEFT JOIN (SELECT b.house_id,a.collect_date," +
                                                                        "date_format(a.collect_date, '%Y-%m-%d') AS datadate," +
                                                                        "datediff(a.collect_date, b.place_date) + b.place_day_age  AS age," +
                                                                        "date_format(a.collect_date, '%Y-%m-%d') AS timeId," +
                                                                        "a.point_temp_diff " +
                                                                    "FROM s_b_layer_house_breed b INNER JOIN s_b_monitor_hist_day a ON a.house_id = b.house_id " +
                                                                    "WHERE 1 = 1 AND b.farm_breed_id =  " + FarmBreedId + " " +
                                                                    "AND a.collect_date BETWEEN date_add(date_format(b.place_date, '%Y-%m-%d'), INTERVAL   " + displayMaxAge + " - 60 - b.place_day_age  DAY) AND date_add(date_format(b.place_date, '%Y-%m-%d'), INTERVAL " + displayMaxAge + " - b.place_day_age + 1 DAY) " +
                                        ") AS tData on tData.age = age_list.dateAge and tData.house_id = age_list.houseid " +
                                    "where 1=1 ORDER BY house_id,x_axis " ;

                } else if (DataType.equals("02")) {
                    if (AgeFlag.equals("N")) {
                        AgeRange = displayMaxAge;
                    }else{
                        if(!PubFun.isNumeric(AgeRange)){
                            tErrorContent = "请传指定的日龄参数或联系管理员！";
                        }
                    }
                    tSQL = "SELECT " +
                            "(CASE WHEN concat(hour_list.datadate, ' ', hour_list.hour) > date_format(adddate(now(), INTERVAL 30 MINUTE), '%Y-%m-%d %H:%i') THEN 'N' ELSE 'Y' END) AS dataflag, " +
                            "hour_list.hour AS x_axis, " +
                            "hour_list.houseid AS house_id, " +
                            "s_f_getHouseName(hour_list.houseid) AS house_name, " +
                            "hour_list.datadate AS data_date, " +
                            "" + AgeRange + " AS data_age, " +
                            "ifnull(tData2.point_temp_diff,0) as point_temp_diff " +
                            "FROM (select sbc.code as hour,sbh.house_id as houseid,date_format(date_add(sbh.place_date,INTERVAL " + AgeRange + " - sbh.place_day_age day), '%Y-%m-%d') as datadate " +
                                        "from s_b_constants sbc,s_b_layer_house_breed sbh where sbc.codetype = 'HalfHour' and sbh.farm_breed_id = "+ FarmBreedId +") as hour_list " +
                                "LEFT JOIN (SELECT tData.house_id, " +
                                            "tData.datadate, " +
                                            "tData.age, " +
                                            "CASE WHEN tData.timeId = '00:00' THEN '24:00' ELSE tData.timeId END AS timeId, " +
                                            "truncate(AVG(tData.point_temp_diff), 1) AS point_temp_diff " +
                                            "FROM (SELECT " +
                                                    "b.house_id, " +
                                                    "a.collect_datetime, " +
                                                    "date_format(a.collect_datetime, '%Y-%m-%d') AS datadate, " +
                                                    "datediff(a.collect_datetime, b.place_date) + b.place_day_age  AS age, " +
                                                    "(CASE WHEN DATE_FORMAT(a.collect_datetime, '%i') BETWEEN '00' AND '30' THEN CONCAT(DATE_FORMAT(a.collect_datetime, '%H'), ':30') ELSE CONCAT(DATE_FORMAT(adddate(a.collect_datetime, INTERVAL 1 HOUR), '%H'), ':00') END) AS timeId, " +
                                                    "a.point_temp_diff " +
                                                    "FROM s_b_layer_house_breed b INNER JOIN s_b_monitor_hist a ON a.house_id = b.house_id " +
                                                    "WHERE 1 = 1 AND b.farm_breed_id = "+ FarmBreedId +" " +
                                                        "AND a.collect_datetime > date_add(date_format(b.place_date, '%Y-%m-%d'), INTERVAL " + AgeRange + " - b.place_day_age  DAY) " +
                                                        "AND a.collect_datetime < date_add(date_format(b.place_date, '%Y-%m-%d'), INTERVAL " + AgeRange + " - b.place_day_age + 1 DAY) " +
                                            ") AS tData " +
                                    "GROUP BY tData.house_id,tData.datadate,tdata.age,tData.timeId) AS tData2 " +
                            "ON tData2.timeId = hour_list.hour and tData2.house_id = hour_list.houseid " +
                            "WHERE 1=1 " +
                            "ORDER BY house_id, x_axis " ;

                } else if (DataType.equals("03")) {
                    String DataRangeStart = "";
                    String DataRangeEnd = "";
                    if (AgeFlag.equals("N") || PubFun.isNull(AgeRange)) {
                        tErrorContent = "参数错误，请联系管理员！";
                    }
                    if (tErrorContent.equals("Null")) {
                        if (TimeFlag.equals("N")) {
                            String tarTime = "";
                            String tCurTime = PubFun.getCurrentTime();
                            if (tCurTime.substring(3, 5).compareTo("30") > 0) {
                                tarTime = tCurTime.substring(0, 2) + ":30";
                            } else {
                                tarTime = tCurTime.substring(0, 2) + ":00";
                            }
                            DataRangeStart = tarTime;

                            SimpleDateFormat formatter = new SimpleDateFormat(
                                    "HH:mm");
                            Date date = formatter.parse(DataRangeStart);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            calendar.add(Calendar.MINUTE, 30);
                            DataRangeEnd = formatter.format(calendar.getTime());
                        } else {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            DataRangeEnd = TimeRange;
                            SimpleDateFormat formatter = new SimpleDateFormat(
                                    "HH:mm");
                            Date date = formatter.parse(DataRangeEnd);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            calendar.add(Calendar.MINUTE, -30);
                            DataRangeStart = formatter.format(calendar.getTime());

                            date = formatter.parse(DataRangeEnd);
                            DataRangeEnd = formatter.format(date);
                        }

                        String tHourValue = DataRangeStart.substring(0, 2);
                        String codeType = "";
                        if (DataRangeStart.endsWith("00")) {
                            codeType = "PerMinute1";
                        } else {
                            codeType = "PerMinute2";
                        }
                        tSQL = "SELECT (CASE WHEN concat(minute_list.datadate, ' " + tHourValue + ":', minute_list.minute) > date_format(adddate(now(), INTERVAL -2 MINUTE), '%Y-%m-%d %H:%i') THEN 'N' ELSE 'Y' END) AS dataflag," +
                                    " CONCAT('" + tHourValue + ":', CASE WHEN minute_list.minute = '00' THEN '60' ELSE minute_list.minute END) AS x_axis," +
                                    " minute_list.houseid AS house_id," +
                                    " s_f_getHouseName(minute_list.houseid) AS house_name," +
                                    " minute_list.datadate AS data_date," +
                                    " " + AgeRange + " AS data_age," +
                                    " ifnull(truncate(AVG(tData.point_temp_diff),1),0) AS point_temp_diff " +
                                " FROM (SELECT sbc.code AS minute,sbh.house_id AS houseid, date_format(date_add(sbh.place_date, INTERVAL " + AgeRange + " - sbh.place_day_age DAY), '%Y-%m-%d') AS datadate " +
                                        " FROM s_b_constants sbc, s_b_layer_house_breed sbh " +
                                        " WHERE sbc.codetype = '" + codeType + "' AND sbh.farm_breed_id = " + FarmBreedId + " " +
                                    " ) AS minute_list " +
                                    " LEFT JOIN (SELECT a.house_id, DATE_FORMAT(adddate(a.collect_datetime, INTERVAL 1 MINUTE), '%i') AS timeId," +
                                                        " date_format(a.collect_datetime, '%Y-%m-%d  %H:%i') AS datadate," +
                                                        " a.collect_datetime," +
                                                        " datediff(a.collect_datetime, b.place_date) AS age," +
                                                        " a.point_temp_diff " +
                                                " FROM s_b_layer_house_breed b INNER JOIN s_b_monitor_hist a " +
                                                " WHERE 1 = 1 " +
                                                " AND b.farm_breed_id = " + FarmBreedId + " AND b.house_id = a.house_id " +
                                                " AND a.collect_datetime >= STR_TO_DATE( concat(date_format(date_add(b.place_date, INTERVAL " + AgeRange + " - b.place_day_age DAY), '%Y-%m-%d'), ' " + DataRangeStart + "'), '%Y-%m-%d %H:%i') " +
                                                " AND a.collect_datetime < STR_TO_DATE(concat(date_format(date_add(b.place_date, INTERVAL " + AgeRange + " - b.place_day_age DAY), '%Y-%m-%d'), ' " + DataRangeEnd + "'), '%Y-%m-%d %H:%i') " +
                                    " ) AS tData ON 1 = 1 AND minute_list.minute BETWEEN tData.timeId - 1 AND tData.timeId + 1 AND minute_list.houseid = tData.house_id " +
                                "GROUP BY minute_list.houseid, minute_list.minute " +
                                "ORDER BY house_id, x_axis ";
                    }
                } else {
                    tErrorContent = "DataType参数有误";
                }

                mLogger.info("==========LayerTempDiffCurveReqController.queryDiffCurve.sql=" + tSQL);

                if (tErrorContent.equals("Null")) {
                    listMap = mBaseQueryService.selectMapByAny(tSQL);
                    if (listMap.size() > 0) {
                        int lastHouseID = 0, dealCount = 0;
                        JSONObject tJSONObject = new JSONObject();
                        JSONArray point_temp_diffArray = new JSONArray();
                        List xAxis = new ArrayList();
                        for (HashMap<String, Object> hashMap : listMap) {
                            Object houseIdO = hashMap.get("house_id");
                            Object x_axis = hashMap.get("x_axis");
                            int houseId = Integer.parseInt(houseIdO.toString());
                            dealCount ++ ;

                            if (x_axis == null) {
                                tErrorContent = "批次数据错误。";
                                break;
                            }

                            if (x_axis.toString().endsWith("60")) {
                                int tHor = Integer.parseInt(x_axis.toString().substring(0, 2)) + 1;
                                x_axis = PubFun.fillLeftChar(tHor, '0', 2) + ":00";
                            }

                            if (!xAxis.contains(x_axis)) {
                                xAxis.add(x_axis);
                            }

                            String HouseName = hashMap.get("house_name").toString();
                            if (lastHouseID != houseId) {
                                if (point_temp_diffArray.length() != 0) {
                                    tJSONObject.put("TempDiffCurve", point_temp_diffArray);
                                    TempDatas.put(tJSONObject);

                                    tJSONObject = new JSONObject();
                                    point_temp_diffArray = new JSONArray();
                                }
                                tJSONObject.put("TempAreaName", HouseName + "栋");
                                tJSONObject.put("HouseId", houseId);
                            }

                            if (hashMap.get("dataflag").equals("Y")) {
                                point_temp_diffArray.put(hashMap.get("point_temp_diff").toString());
                            }
                            if (dealCount == listMap.size() && point_temp_diffArray.length() != 0){
                                tJSONObject.put("TempDiffCurve", point_temp_diffArray);
                                TempDatas.put(tJSONObject);
                            }

                            lastHouseID = houseId;
                        }

                        resJson.put("FarmBreedId", FarmBreedId);
                        resJson.put("DataDate", data_date);
                        resJson.put("data_age", AgeRange);
                        resJson.put("TempDatas", TempDatas);
                        resJson.put("xAxis", xAxis);
                        resJson.put("Result", "Success");
                    } else {
                        resJson.put("Result", "Fail");
                        resJson.put("ErrorMsg", "请求参数错误");
                    }
                } else {
                    resJson.put("Result", "Fail");
                    resJson.put("ErrorMsg", tErrorContent);
                }
            } else {
                resJson.put("Result", "Fail");
                resJson.put("ErrorMsg", tErrorContent);
            }
            resJson.put("FarmBreedId", FarmBreedId);
            resJson.put("DataDate", data_date);
            resJson.put("data_age", AgeRange);
            resJson.put("TempDatas", TempDatas);
            dealRes = Constants.RESULT_SUCCESS;
		/* 业务处理结束 */

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
        long endReqTime = System.currentTimeMillis();
        if (endReqTime - startReqTime < 1500) {
            try {
                Thread.sleep(1500 - endReqTime + startReqTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
        mLogger.info("=======Now end executing LayerTempDiffCurveReqController.queryDiffCurve");
    }
}
