/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.mtc.app.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mtc.app.service.BaseQueryService;
import com.mtc.app.service.SDUserOperationService;
import com.mtc.common.util.DealSuccOrFail;
import com.mtc.common.util.PubFun;
import com.mtc.common.util.constants.Constants;

/**
 * @ClassName: RepTempDiffReqController
 * @Description:
 * @Date 2016-1-6 下午5:40:42
 * @Author Shao Yao Yu
 * 
 */
@Controller
@RequestMapping("/rep/TempDiffCurve")
public class RepTempDiffReqController {

    private static Logger mLogger = Logger.getLogger(RepTempDiffReqController.class);

    @Autowired
    private BaseQueryService mBaseQueryService;
    @Autowired
    private SDUserOperationService sSDUserOperationService;

	@RequestMapping("/TempDiffCurveReq")
	public void TempDiffCurveReq(HttpServletRequest request,HttpServletResponse response) {
		mLogger.info("=======Now start executing RepTempDiffReqController.TempDiffCurveReq");
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

			 /*业务处理开始，查询、增加、修改、或删除*/
			JSONObject params = jsonobject.optJSONObject("params");
			int FarmBreedId = params.optInt("FarmBreedId");
			int HouseId = params.optInt("HouseId");
			String DataType = params.optString("DataType");
			String ReqFlag = params.optString("ReqFlag");
			String DataRange = params.optString("DataRange");
			String data_date = "null" ;
			String data_age = "null";
			JSONArray TempDatas = new JSONArray();
			List<HashMap<String, Object>> listMap = null;
			String tSQL = "";
			boolean flag = true;
			sSDUserOperationService.insert(SDUserOperationService.MENU_DATAANALYSIS_TEMPDIFF, SDUserOperationService.OPERATION_SELECT, userId);

			if (DataType.equals("01")) {

			    String fSQL = "SELECT place_date FROM s_b_house_breed where house_id = " + HouseId + " and farm_breed_id = " + FarmBreedId;
                String place = mBaseQueryService.selectStringByAny(fSQL);
                
				tSQL = "SELECT (CASE when a.growth_date > curdate() then 'N' else 'Y' end) as dataflag,'Null'as data_age,'Null'as data_date,b.house_id,concat(date_format(a.growth_date,'%m-%d'),'(',a.age,')') as x_axis," +
						"tData2.point_temp_diff "
					 + "FROM s_b_breed_detail a "
					 + "left join s_b_house_breed b on b.id = a.house_breed_id "
					 + "left join( SELECT tData.timeId,"
						           + "truncate(AVG(tData.point_temp_diff), 1) AS point_temp_diff "
								   + "from ( SELECT date_format(collect_datetime, '%Y-%m-%d') AS timeId, a.* "
								   			+ "FROM s_b_monitor_hist a WHERE 1 = 1 "
								   			+ "AND a.house_id = " + HouseId + " " ;
                if (place == null || place.equals("")){
                	tSQL = tSQL + " and 1<>1 ";
                }else{
                	tSQL = tSQL + "AND a.collect_datetime BETWEEN '" + place + "' AND date_add('" + place + "', INTERVAL 60 DAY) " ;
                }
                tSQL = tSQL + ") tData group by tData.timeId order by tData.timeId "
						+ ") as tData2 on tData2.timeId = date_format(a.growth_date,'%Y-%m-%d') "
						+ "where 1=1 "
						+ "and a.age <= 45 "
						+ "and b.house_id = " + HouseId + " "
						+ "and b.farm_breed_id =" + FarmBreedId + " "
						+ "and exists(SELECT 1 from s_b_house_breed sbh where sbh.id = a.house_breed_id and a.growth_date <= ifnull(b.market_date,now())) ";

			}else if (DataType.equals("02")) {
				if (ReqFlag.equals("N")) {
					DataRange = "NULL";
					String tDateSql = "SELECT s_f_getRecentAgeDateByHouseId("+HouseId+", '"+DataRange+"',"+FarmBreedId+") ";
					DataRange = mBaseQueryService.selectStringByAny(tDateSql);
					if (DataRange == null) {
						tErrorContent = "暂无入雏信息！";
						flag = false;
					}
				}else{
					if (DataRange.length() != 10) {
						tErrorContent = "请传入正确的日期参数（YYYY-MM-DD）。";
					}
				}
				if (flag) {
					tSQL = "SELECT (CASE WHEN concat(tData3.data_date,' ',CODE) > date_format(adddate(now(), INTERVAL 30 MINUTE), '%Y-%m-%d %H:%i') THEN 'N' ELSE 'Y' END) AS dataflag, "
							+ "CODE AS x_axis," + HouseId + " AS house_id,"
							+ "tData3.data_date as data_date,"
							+ "concat('(日龄：',s_f_getDayAgeByHouseId(" + HouseId + ",tData3.data_date),')')  AS data_age,"
							+ " tData2.point_temp_diff "
							+ " FROM s_b_constants sc "
							+ "	LEFT JOIN("
							+ " SELECT case when tData.timeId = '00:00' then '24:00' else tData.timeId end as timeId,"
							+ "tData.house_id, tData.date_age,"
							+ "truncate(AVG(tData.point_temp_diff), 1) AS point_temp_diff "
							+ " FROM ( SELECT  (CASE WHEN DATE_FORMAT(collect_datetime, '%i') BETWEEN '00' AND '30' THEN CONCAT(DATE_FORMAT(collect_datetime, '%H'),':30') ELSE CONCAT(DATE_FORMAT(adddate(collect_datetime,INTERVAL 1 HOUR), '%H'),':00') END) AS timeId,a.* "
							+ " FROM s_b_monitor_hist a "
							+ " WHERE  a.house_id = " + HouseId
							+ " and a.collect_datetime between date_format('" + DataRange + "', '%Y-%m-%d') and date_format(date_add('" + DataRange + "', INTERVAL 1 DAY), '%Y-%m-%d') "
							+ ") tData GROUP BY tData.timeId ORDER BY tData.timeId"
							+ ") AS tData2 ON tData2.timeId = sc.code "
							+ "LEFT JOIN (select '" + DataRange + "' as data_date) AS tData3 on 1=1"
							+ " WHERE codetype = 'HalfHour' ";
				}
			} else if (DataType.equals("03")) {
				String DataRangeStart = "";
				String DataRangeEnd = "";
				if (ReqFlag.equals("N")) {
					String tarTime = "";
					if(DataRange.equals(PubFun.getCurrentDate())){
						String tCurTime = PubFun.getCurrentTime();
						if(tCurTime.substring(3, 5).compareTo("30")>0){
							tarTime = tCurTime.substring(0,2) + ":30";
						}else{
							tarTime = tCurTime.substring(0,2) + ":00";
						}
					}else{
						tarTime = "00:00";
					}
					DataRangeStart = DataRange + " " + tarTime ;
					
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					Date date = formatter.parse(DataRangeStart);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					calendar.add(Calendar.MINUTE, 30);
					DataRangeEnd = formatter.format(calendar.getTime());
					
				}else{
					DataRangeEnd = DataRange ;
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					Date date = formatter.parse(DataRangeEnd);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					calendar.add(Calendar.MINUTE, -30);
					DataRangeStart = formatter.format(calendar.getTime());
					
					date = formatter.parse(DataRangeEnd);
					DataRangeEnd = formatter.format(date);
				}
				if (DataRangeStart.length() != 16 || DataRangeEnd.length() != 16) {
					tErrorContent = "DataRange日期参数有误";
				}
				
				data_date = DataRangeStart.substring(0,10);
				String tHourValue = DataRangeStart.substring(11, 13);
				String codeType = "";
				if (DataRangeStart.endsWith("00")) {
					codeType = "PerMinute1";
				} else {
					codeType = "PerMinute2";
				}
				
				tSQL = "SELECT 'Y' as dataflag ,CONCAT('"+ tHourValue+ ":', CASE when tData.timeId = '00' then '60' else tData.timeId end) AS x_axis,"
						+ "'Null'as data_date,concat('(日龄：',tData.date_age,')')  AS data_age,tData.house_id,"
						+ "truncate(AVG(tData.point_temp_diff), 1) AS point_temp_diff "
						+ "FROM (SELECT DATE_FORMAT(adddate(a.collect_datetime,INTERVAL 1 MINUTE), '%i') AS timeId,a.* "
							+ "FROM s_b_monitor_hist a WHERE 1=1 "
								+ "and a.house_id = " + HouseId + " "
								+ "AND a.collect_datetime >= STR_TO_DATE('"+ DataRangeStart+ "', '%Y-%m-%d %H:%i' ) "
								+ "AND a.collect_datetime  < STR_TO_DATE( '"+ DataRangeEnd+ "',  '%Y-%m-%d %H:%i' ) "
							 + ") tData GROUP BY tData.timeId "
						+ "UNION ALL "
							+ "SELECT  'N' as dataflag , CONCAT('"+ tHourValue+ ":', sc.code) AS x_axis,"
								+ "NULL,NULL, NULL,  NULL FROM s_b_constants sc  WHERE 1=1 "
								+ "AND sc.codetype = '"+ codeType+ "' "
								+ "AND CODE > (SELECT right(concat('0', ifnull(DATE_FORMAT(MAX(sbh.collect_datetime), '%i') + 1,0)), 2) FROM s_b_monitor_hist sbh  WHERE 1=1 "
								+ "AND sbh.house_id = " + HouseId + " "
								+ "AND sbh.collect_datetime BETWEEN "
											+ "STR_TO_DATE('"+ DataRangeStart+ "', '%Y-%m-%d %H:%i' ) AND "
											+ "STR_TO_DATE('"+ DataRangeEnd + "','%Y-%m-%d %H:%i' )) "
						+ " ORDER BY x_axis ";
			}else{
				tErrorContent = "DataType参数有误";
			}
			mLogger.info("DataType=" + DataType + " DataRange="+ DataRange);
			mLogger.info("==========RepTempDiffReqController.TempDiffCurveReq.sql=" + tSQL);
			
			if(tErrorContent.equals("Null")){
				listMap = mBaseQueryService.selectMapByAny(tSQL);
				if (listMap.size() > 0) {
					JSONArray point_temp_diffArray = new JSONArray();
					JSONArray xAxis = new JSONArray();
					for (HashMap<String, Object> hashMap : listMap) {
						Object x_axis = hashMap.get("x_axis");
						if (x_axis == null) {
							x_axis = 0;
						}
						
						if(x_axis.toString().endsWith("60")){
							int tHor = Integer.parseInt(x_axis.toString().substring(0, 2)) + 1;
							x_axis = PubFun.fillLeftChar(tHor, '0', 2) + ":00";
						}
						
						Object point_temp_diff = hashMap.get("point_temp_diff");
						if (point_temp_diff == null) {
							point_temp_diff = 0;
						}
						xAxis.put(x_axis);
						if(hashMap.get("dataflag").equals("N")){
							continue;
						}
						point_temp_diffArray.put(point_temp_diff.toString());
						if(DataType.equals("02")){
							data_date = hashMap.get("data_date")!=null?hashMap.get("data_date").toString():"Null";
						}
						if(hashMap.get("data_age") != null){
							data_age = hashMap.get("data_age").toString();
						}
					}

					resJson.put("xAxis", xAxis);
					JSONObject tJSONObject = new JSONObject();
					tJSONObject.put("TempAreaCode", "pointTempDiff");
					tJSONObject.put("TempAreaName", "点温差");
					tJSONObject.put("TempDiffCurve", point_temp_diffArray);
					TempDatas.put(tJSONObject);
						
					resJson.put("TempDatas", TempDatas);
					resJson.put("HouseId", HouseId);
					resJson.put("DataDate", data_date);
					resJson.put("data_age", data_age);
					resJson.put("FarmBreedId", FarmBreedId);
					resJson.put("Result", "Success");
					dealRes = Constants.RESULT_SUCCESS;
				} else {
					resJson.put("Result", "Fail");
					resJson.put("ErrorMsg", "请求参数错误");
					dealRes = Constants.RESULT_SUCCESS;
				}
			}else{
				resJson.put("Result", "Fail");
				resJson.put("ErrorMsg", tErrorContent);
				dealRes = Constants.RESULT_SUCCESS;
			}
			 /*业务处理结束*/
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
		if(endReqTime - startReqTime < 1500){
			try {
				Thread.sleep(1500 - endReqTime + startReqTime);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		DealSuccOrFail.dealApp(request, response, dealRes, resJson);
		mLogger.info("=====Now end executing RepTempDiffReqController.TempDiffCurveReq");
	}

    @RequestMapping("/TempDiffCurveReq_v2")
    public void TempDiffCurveReq_v2(HttpServletRequest request, HttpServletResponse response) {
        mLogger.info("=====Now start executing RepTempDiffReqController.TempDiffCurveReq");
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
            sSDUserOperationService.insert(SDUserOperationService.MENU_DATAANALYSIS_TEMPDIFF, SDUserOperationService.OPERATION_SELECT, userId);

            if (DataType.equals("02")) {
                if (AgeFlag.equals("N")) {
                    AgeRange = "NULL";
                    String tDateSql = "SELECT max(least(45,datediff(ifnull(a.market_date, curdate()),a.place_date))) as age from s_b_house_breed a where farm_breed_id = " + FarmBreedId;
                    AgeRange = mBaseQueryService.selectStringByAny(tDateSql);
                    if (AgeRange == null || AgeRange.equals("")) {
                        tErrorContent = "暂无批次信息！";
                    }
                }
            }
            if (tErrorContent.equals("Null")) {
                if (DataType.equals("01")) {
                    tSQL = "SELECT (CASE when age_list.datadate >= date_format(curdate(), '%Y-%m-%d') then 'N' else 'Y' end) as dataflag, " +
									"age_list.datadate, " +
									"age_list.dateAge AS x_axis, " +
									"age_list.houseid AS house_id, " +
									"s_f_getHouseName(age_list.houseid) as house_name, " +
									"age_list.dateAge         AS data_age, " +
									"ifnull(tData.point_temp_diff, 0) as point_temp_diff " +
									"FROM (select sds.IncreID -1 as dateAge,sbh.house_id as houseid,date_format(date_add(sbh.place_date,INTERVAL sds.IncreID -1 day), '%Y-%m-%d') as datadate from s_d_serialno sds,s_b_house_breed sbh " +
											"where sds.IncreID <= 46 and sbh.farm_breed_id = " + FarmBreedId + " " +
											") as age_list " +
										"LEFT JOIN ( SELECT  a.house_id, " +
														"datediff(a.collect_date, b.place_date) AS age, " +
														"a.point_temp_diff as point_temp_diff " +
														"FROM s_b_house_breed b INNER JOIN s_b_monitor_hist_day a " +
														"WHERE 1 = 1 " +
														"AND b.farm_breed_id = " + FarmBreedId + " " +
														"AND b.house_id = a.house_id " +
														"AND a.collect_date BETWEEN b.place_date AND date_add(b.place_date, INTERVAL 45 DAY) " +
											") as tData on tData.age = age_list.dateAge and tData.house_id = age_list.houseid " +
										"where 1=1 ORDER BY house_id,x_axis ";

                } else if (DataType.equals("02")) {
                    tSQL = "SELECT (CASE WHEN concat(hour_list.datadate, ' ', hour_list.hour) > date_format(adddate(now(), INTERVAL 30 MINUTE), '%Y-%m-%d %H:%i') THEN 'N' ELSE 'Y' END) AS dataflag, " +
									" hour_list.hour AS x_axis, " +
									" hour_list.houseid AS house_id, " +
									" s_f_getHouseName(hour_list.houseid) AS house_name, " +
									" hour_list.datadate                   AS data_date, " +
									" " + AgeRange + " AS data_age, " +
									" ifnull(tData2.point_temp_diff, 0) as point_temp_diff " +
							"FROM (select sbc.code as hour,sbh.house_id as houseid,date_format(date_add(sbh.place_date,INTERVAL " + AgeRange + " day), '%Y-%m-%d') as datadate " +
									"from s_b_constants sbc,s_b_house_breed sbh where sbc.codetype = 'HalfHour' and sbh.farm_breed_id = "+ FarmBreedId +" " +
								") as hour_list LEFT JOIN (SELECT tData.house_id, " +
															" tData.datadate, " +
															" tData.age, " +
															" CASE WHEN tData.timeId = '00:00' THEN '24:00' ELSE tData.timeId END AS timeId, " +
															" truncate(AVG(tData.point_temp_diff), 1) AS point_temp_diff " +
															" FROM (SELECT a.house_id, " +
																			"date_format(a.collect_datetime, '%Y-%m-%d') AS datadate, " +
																			"datediff(a.collect_datetime, b.place_date)  AS age, " +
																			"(CASE WHEN DATE_FORMAT(a.collect_datetime, '%i') BETWEEN '00' AND '30' " +
							 													  "THEN CONCAT(DATE_FORMAT(a.collect_datetime, '%H'), ':30') " +
																				  "ELSE CONCAT(DATE_FORMAT(adddate(a.collect_datetime, INTERVAL 1 HOUR), '%H'), ':00') END) AS timeId, " +
																		    "a.point_temp_diff " +
																	"FROM s_b_house_breed b INNER JOIN s_b_monitor_hist a " +
																	"WHERE 1 = 1 AND b.farm_breed_id = "+ FarmBreedId +" AND b.house_id = a.house_id " +
																	"AND a.collect_datetime BETWEEN date_add(b.place_date, INTERVAL " + AgeRange + " DAY) AND date_add(b.place_date,INTERVAL (" + AgeRange + " + 1) DAY)" +
								 							") AS tData  GROUP BY tData.house_id, tData.datadate, tData.age, tData.timeId" +
								") AS tData2 ON 1=1 " +
								"and hour_list.hour = tData2.timeId and hour_list.houseid = tData2.house_id " +
								"ORDER BY house_id,x_axis " ;
                } else if (DataType.equals("03")) {
                    String DataRangeStart = "";
                    String DataRangeEnd = "";
                    String speDate = "";
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

                        tSQL = "SELECT (CASE WHEN concat(minute_list.datadate, ' " + tHourValue + ":', minute_list.minute) " +
                                							" > date_format(adddate(now(), INTERVAL -2 MINUTE), '%Y-%m-%d %H:%i') THEN 'N' ELSE 'Y' END) AS dataflag, " +
										"CONCAT('" + tHourValue + ":', CASE when minute_list.minute = '00' then '60' else minute_list.minute end) AS x_axis, " +
										"minute_list.houseid as house_id, " +
										"s_f_getHouseName(minute_list.houseid) as house_name, " +
										"minute_list.datadate as data_date, " +
										AgeRange + " as data_age, " +
										"truncate(AVG(tData.point_temp_diff), 1) AS point_temp_diff " +
										"FROM (select sbc.code as minute,sbh.house_id as houseid,date_format(date_add(sbh.place_date,INTERVAL " + AgeRange + " day), '%Y-%m-%d') as datadate " +
                                									"from s_b_constants sbc,s_b_house_breed sbh where sbc.codetype = '" + codeType + "' and sbh.farm_breed_id = " + FarmBreedId + " " +
                                				") as minute_list " +
										"left join (SELECT a.house_id, " +
															"DATE_FORMAT(adddate(a.collect_datetime,INTERVAL 1 MINUTE), '%i') AS timeId, " +
															"date_format(a.collect_datetime, '%Y-%m-%d  %H:%i') as datadate, " +
															"datediff(a.collect_datetime, b.place_date) AS age, " +
															"a.point_temp_diff " +
															"FROM s_b_house_breed b INNER JOIN s_b_monitor_hist a " +
															"WHERE 1 = 1 " +
															"AND b.farm_breed_id = " + FarmBreedId + " " +
															"AND b.house_id = a.house_id " +
															"AND a.collect_datetime >= STR_TO_DATE(concat(date_format(date_add(b.place_date,INTERVAL " + AgeRange + " day), '%Y-%m-%d'),' " + DataRangeStart + "'), '%Y-%m-%d %H:%i') " +
															"AND a.collect_datetime < STR_TO_DATE(concat(date_format(date_add(b.place_date,INTERVAL " + AgeRange + " day), '%Y-%m-%d'),' " + DataRangeEnd + "'), '%Y-%m-%d %H:%i') " +
                                		") as tData on 1=1 " +
										"and minute_list.minute BETWEEN tData.timeId -1 and tData.timeId + 1 " +
										"and minute_list.houseid = tData.house_id " +
                                "GROUP BY minute_list.houseid,minute_list.minute " +
                                "ORDER BY house_id,x_axis ";
                    }
                } else {
                    tErrorContent = "DataType参数有误";
                }
                mLogger.info("==========RepTempDiffReqController.TempDiffCurveReq.sql=" + tSQL);

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
        mLogger.info("=====Now end executing RepTempDiffReqController.TempDiffCurveReq");
    }
}
