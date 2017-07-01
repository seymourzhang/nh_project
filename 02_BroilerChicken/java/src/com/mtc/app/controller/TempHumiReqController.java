/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.mtc.app.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import com.mtc.app.service.SDUserOperationService;
import com.mtc.entity.app.SDUserOperation;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mtc.app.service.BaseQueryService;
import com.mtc.common.util.DealSuccOrFail;
import com.mtc.common.util.PubFun;
import com.mtc.common.util.constants.Constants;

/**
 * @ClassName: RepTempReqController
 * @Description:
 * @Date 2016-1-6 下午5:40:42
 * @Author Shao Yao Yu
 * 
 */
@Controller
@RequestMapping("/rep/TempHumi")
public class TempHumiReqController {

	private static Logger mLogger = Logger.getLogger(TempHumiReqController.class);
	@Autowired
	private BaseQueryService mBaseQueryService;
	@Autowired
	private SDUserOperationService sSDUserOperationService;

	@RequestMapping("/TempHumiReq")
	public void TempHumiReq(HttpServletRequest request,HttpServletResponse response) {
		mLogger.info("=======Now start executing TempHumiReqController.TempHumiReq");
		JSONObject resJson = new JSONObject();
		String dealRes = null;
		long startReqTime = System.currentTimeMillis();
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("updateFarm.para=" + paraStr);
			JSONObject jsonobject = new JSONObject(paraStr);
			int userId = jsonobject.optInt("id_spa");
			mLogger.info("jsonObject=" + jsonobject.toString());
			String tErrorContent = "Null";
			/** 业务处理开始，查询、增加、修改、或删除 **/
			JSONObject   params = jsonobject.getJSONObject("params");
			int   FarmBreedId = params.optInt("FarmBreedId");
			int   HouseId = params.optInt("HouseId");
			String DataType = params.optString("DataType");
			String ReqFlag = params.optString("ReqFlag");
			String DataRange = params.optString("DataRange");

			String SQL = "";
			if ("01".equals(DataType)){
				String tSQL = "SELECT DATE_FORMAT(place_date, '%Y-%m-%d') FROM s_b_house_breed where farm_breed_id = "+FarmBreedId+" and house_id = " + HouseId;
				String placeDate = mBaseQueryService.selectStringByAny(tSQL);
				String endDate = null;
				if(!PubFun.isNull(placeDate)){
					endDate = PubFun.addDate(placeDate, 45);
				}

				SQL = " SELECT" +
						"  (CASE WHEN a.growth_date > CURDATE() THEN 'N' ELSE 'Y' END) AS dataflag," +
						"  b.house_id," +
						"  DATE_FORMAT(a.growth_date, '%Y-%m-%d') AS growthDate," +
						"  concat(date_format(a.growth_date, '%m-%d'), '(', a.age, ')') AS x_axis," +
						"  tData2.avgtemp_max," +
						"  tData2.avgtemp_min," +
						"  tData2.tartemp," +
						"  tData2.humi" +
						" FROM s_b_breed_detail a LEFT JOIN s_b_house_breed b ON b.id = a.house_breed_id" +
						"  LEFT JOIN (SELECT" +
						"               tData3.dateId," +
						"               ROUND(MAX(tData3.inside_avg_temp), 1) AS avgtemp_max," +
						"               ROUND(MIN(tData3.inside_avg_temp), 1) AS avgtemp_min," +
						"               ROUND(AVG(tData3.inside_set_temp), 1) AS tartemp," +
						"               ROUND(AVG(tData3.inside_humidity), 1) AS humi" +
						"             FROM (SELECT" +
						"               	left(tData.timeId,10) as dateId," +
						"               	avg(tData.inside_avg_temp) AS inside_avg_temp," +
						"               	avg(tData.inside_set_temp) AS inside_set_temp," +
						"               	avg(tData.inside_humidity) AS inside_humidity" +
						"             		FROM (SELECT" +
						"                    		DATE_FORMAT(collect_datetime, '%Y-%m-%d %H') AS timeId, a.*" +
						"                   		FROM s_b_monitor_hist a WHERE a.house_id = "+HouseId+" " +
						"							AND date_format(a.collect_datetime, '%Y-%m-%d') BETWEEN '"+placeDate+"' AND '"+endDate+"') tData" +
						"             GROUP BY tData.timeId ) as tData3 GROUP BY tData3.dateId) AS tData2 ON tData2.dateId = DATE_FORMAT(a.growth_date, '%Y-%m-%d')" +
						" WHERE a.age <= 45 AND b.house_id = "+HouseId+" AND b.farm_breed_id = " + FarmBreedId ;

				mLogger.info("=========TempHumiReqController.TempHumiReq.SQL: "+SQL);
			} else if ("02".equals(DataType)){
				if (ReqFlag.equals("N")) {
					DataRange = "NULL";
					String tDateSql = "SELECT s_f_getRecentAgeDateByHouseId("+HouseId+", '"+DataRange+"',"+FarmBreedId+") ";
					DataRange = mBaseQueryService.selectStringByAny(tDateSql);
					if (DataRange == null) {
						tErrorContent = "暂无入雏信息！";
					}
				}else{
					if (DataRange.length() != 10) {
						tErrorContent = "请传入正确的日期参数（YYYY-MM-DD）。";
					}
				}
				SQL = "SELECT " +
						"  (CASE WHEN concat(tData3.data_date, ' ', CODE) > date_format(adddate(now(), INTERVAL 30 MINUTE), '%Y-%m-%d %H:%i') " +
						"    THEN 'N' " +
						"   ELSE 'Y' END)                                                         AS dataflag, " +
						"  CODE                                                                   AS x_axis, " +
						" tData3.data_date                                                        AS dataDate, " +
						"  tData4.date_age                                                        AS dateAge, " +
						"  " + HouseId + "                                                        AS house_id," +
						"  tData2.avgtemp_max, " +
						"  tData2.avgtemp_min, " +
						"  tData2.tartemp " +
						"FROM s_b_constants sc " +
						"  LEFT JOIN ( " +
						"              SELECT " +
						"                CASE WHEN tData.timeId = '00:00' " +
						"                  THEN '24:00' " +
						"                ELSE tData.timeId END                   AS timeId, " +
						"                tData.house_id, " +
						"                ROUND(MAX(tData.inside_avg_temp), 1) AS avgtemp_max, " +
						"                ROUND(MIN(tData.inside_avg_temp), 1) AS avgtemp_min, " +
						"                ROUND(AVG(tData.inside_set_temp), 1) AS tartemp " +
						"              FROM (SELECT " +
						"                      (CASE WHEN DATE_FORMAT(collect_datetime, '%i') BETWEEN '00' AND '30' " +
						"                        THEN CONCAT(DATE_FORMAT(collect_datetime, '%H'), ':30') " +
						"                       ELSE CONCAT(DATE_FORMAT(adddate(collect_datetime, INTERVAL 1 HOUR), '%H'), ':00') END) AS timeId, " +
						"                      a.* " +
						"                    FROM s_b_monitor_hist a " +
						"                    WHERE a.house_id = " + HouseId + " " +
						"                          AND DATE_FORMAT(a.collect_datetime, '%Y-%m-%d') = '" + DataRange + "' " +
						"                   ) tData " +
						"              GROUP BY tData.timeId " +
						"              ORDER BY tData.timeId " +
						"            ) AS tData2 ON tData2.timeId = sc.code " +
						"  LEFT JOIN (SELECT '" + DataRange + "' AS data_date) AS tData3 ON 1 = 1 " +
						"  LEFT JOIN (SELECT s_f_getDayAgeByHouseId(" + HouseId + ", '" + DataRange + "') as date_age) tData4 on 1 = 1 " +
						"WHERE codetype = 'HalfHour'";
			}
			if (tErrorContent.equals("Null")) {
				List<HashMap<String, Object>> listMap = mBaseQueryService.selectMapByAny(SQL);
				JSONArray THDatas = new JSONArray();
				JSONArray xAxis = new JSONArray();
				if (listMap.size() > 0) {
					JSONObject njsonObject = new JSONObject();
					for (HashMap<String, Object> hashMap : listMap) {
						njsonObject = new JSONObject();
						Object MinTemp = hashMap.get("avgtemp_min");
						Object MaxTemp = hashMap.get("avgtemp_max");
						Object TarTemp = hashMap.get("tartemp");
						Object x_axis = hashMap.get("x_axis");
						String dataflag = (String) hashMap.get("dataflag");
						if (MinTemp == null) {
							njsonObject.put("MinTemp", 0);
						} else {
							njsonObject.put("MinTemp", MinTemp);
						}
						if (MaxTemp == null) {
							njsonObject.put("MaxTemp", 0);
						} else {
							njsonObject.put("MaxTemp", MaxTemp);
						}
						if (TarTemp == null) {
							njsonObject.put("TarTemp", 0);
						} else {
							njsonObject.put("TarTemp", TarTemp);
						}
						if (dataflag.equals("Y")) {
							THDatas.put(njsonObject);
						}
						xAxis.put(x_axis);
					}
					if ("02".equals(DataType)){
						resJson.put("DataDate", listMap.get(0).get("dataDate"));
						resJson.put("data_age", listMap.get(0).get("dateAge"));
					}
				}
				resJson.put("xAxis", xAxis);
				resJson.put("THDatas", THDatas);
				resJson.put("Result", "Success");
			} else {
				resJson.put("Error", tErrorContent);
				resJson.put("Result", "Fail");
			}
		    resJson.put("FarmBreedId", FarmBreedId);
			resJson.put("HouseId", HouseId);
			dealRes = Constants.RESULT_SUCCESS;
			/** 业务处理结束 **/
		} catch (Exception e) {
			e.printStackTrace();
				resJson = new JSONObject();
				try {
					resJson.put("Exception", e.getMessage());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			dealRes = Constants.RESULT_FAIL;
		}
		long endReqTime = System.currentTimeMillis();
		if(endReqTime - startReqTime < 1500){
			try {
				Thread.sleep(1500 - endReqTime + startReqTime);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		DealSuccOrFail.dealApp(request, response, dealRes, resJson);
		mLogger.info("=====Now end executing TempHumiReqController.TempHumiReq");
	}
}
