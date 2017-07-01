/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.mtc.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mtc.app.service.SDUserOperationService;

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
 * 获取周均重控制器
 * 
 * @author lx
 *
 */
@Controller
@RequestMapping("/rep/weekWeight")
public class RepWeekWeightController {
	private static Logger mLogger = Logger.getLogger(RepWeekWeightController.class);

	@Autowired
	private BaseQueryService mBaseQueryService;
	@Autowired
	private SDUserOperationService sSDUserOperationService;

	@RequestMapping("/DFRRep")
	public void DFRRep(HttpServletRequest request, HttpServletResponse response) {
		mLogger.info("=======Now start executing DFRRepController.DFRRep");
		JSONObject resJson = new JSONObject();
		String dealRes = null;
		long startReqTime = System.currentTimeMillis();
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("updateFarm.para=" + paraStr);
			JSONObject jsonobject = new JSONObject(paraStr);
			mLogger.info("jsonObject=" + jsonobject.toString());
			/** 业务处理开始，查询、增加、修改、或删除 **/
			JSONObject params = jsonobject.optJSONObject("params");
			int userid = jsonobject.optInt("id_spa");
			String CompareType = params.optString("CompareType");
			if (CompareType == null || CompareType.equals("")) {
				CompareType = "01";
			}
			// 栋舍对比
			sSDUserOperationService.insert(
					SDUserOperationService.MENU_DATAANALYSIS_WEEKWEIGHT,
					SDUserOperationService.OPERATION_SELECT, userid);

			int farm_id = params.optInt("FarmId");
			List<HashMap<String, Object>> standIntakeList = null;
			if (farm_id != 0) {
				String standIntakeSQL = "SELECT age,body_weight from s_b_chicken_standar where farm_id = "
						+ farm_id + " and feed_type = 'mixed' ";
				standIntakeList = mBaseQueryService
						.selectMapByAny(standIntakeSQL);
			}

			if (CompareType.equals("01")) {
				// 批次对比
				int FarmBreedId = params.optInt("FarmBreedId");

				String sql = "SELECT (case when bd.growth_date > curdate() then 'N' else 'Y' end) as showFlag, hb.house_id ,s_f_getHouseName(hb.house_id) AS housename, bd.age ,bd.cur_weight AS DFR "
						+ "  FROM s_b_breed_detail AS bd LEFT JOIN s_b_house_breed AS hb ON bd.house_breed_id = hb.id "
						+ " WHERE hb.farm_breed_id = "
						+ FarmBreedId
						+ " AND bd.cur_weight >= 0  AND (CASE WHEN hb.market_date IS NOT NULL THEN bd.growth_date <= hb.market_date ELSE bd.growth_date IS NOT NULL END  ) GROUP BY bd.house_breed_id , bd.age  ORDER BY bd.house_breed_id,  bd.age ";
				mLogger.info("DFRRepController.DFRRep.SQL=" + sql);
				List<HashMap<String, Object>> toutcome = mBaseQueryService
						.selectMapByAny(sql);

				// 保存标准数据
				JSONArray DCDatas = new JSONArray();
				if (standIntakeList != null && standIntakeList.size() > 0) {
					JSONObject standarObject = new JSONObject();
					standarObject.put("HouseId", -1);
					standarObject.put("housename", "标准");
					ArrayList<Object> staLists = new ArrayList<Object>();
					for (HashMap<String, Object> singleData : standIntakeList) {
						staLists.add(singleData.get("body_weight"));
					}
					standarObject.put("HouseDatas", staLists);
					DCDatas.put(standarObject);
				}

				if (toutcome.size() != 0) {
					Object house_id = toutcome.get(0).get("house_id");
					int i = 0;
					Object housename = null;
					ArrayList<Object> HouseDa = new ArrayList<Object>();
					for (HashMap<String, Object> outcome : toutcome) {
						if (!house_id.equals(toutcome.get(i).get("house_id"))
								|| i + 1 == toutcome.size()) {
							JSONObject tJSONObject = new JSONObject();
							tJSONObject.put("HouseId", house_id);
							tJSONObject.put("housename", housename);
							tJSONObject.put("HouseDatas", HouseDa);
							DCDatas.put(tJSONObject);
							HouseDa = new ArrayList<Object>();
						}
						house_id = outcome.get("house_id");
						housename = outcome.get("housename");
						Object DFR = outcome.get("DFR");
						String showFlag = outcome.get("showFlag").toString();
						if (showFlag.equals("Y")) {
							HouseDa.add(DFR);
						}
						i++;
					}
				}
				resJson.put("Result", "Success");
				resJson.put("DCDatas", DCDatas);
				resJson.put("FarmBreedId", FarmBreedId);

			} else if (CompareType.equals("02")) {
				// 栋舍对比
				int HouseId = params.optInt("HouseId");
				String sql = "SELECT (case when bd.growth_date > curdate() then 'N' else 'Y' end) as showFlag, hb.farm_breed_id , (SELECT batch_code from s_b_farm_breed where id = hb.farm_breed_id) AS batch_code , bd.age ,bd.cur_weight AS DFR "
						+ " FROM s_b_breed_detail AS bd LEFT JOIN s_b_house_breed AS hb ON bd.house_breed_id = hb.id "
						+ " WHERE hb.house_id = "
						+ HouseId
						+ " AND bd.cur_weight >= 0  AND (CASE WHEN hb.market_date IS NOT NULL THEN bd.growth_date <= hb.market_date ELSE bd.growth_date IS NOT NULL END  ) GROUP BY bd.house_breed_id ,"
						+ " bd.age  ORDER BY bd.house_breed_id,  bd.age ";

				mLogger.info("DFRRepController.DFRRep.SQL=" + sql);
				List<HashMap<String, Object>> toutcome = mBaseQueryService
						.selectMapByAny(sql);

				JSONArray DCDatas = new JSONArray();
				if (standIntakeList != null && standIntakeList.size() > 0) {
					JSONObject standarObject = new JSONObject();
					standarObject.put("FarmBreedId", -1);
					standarObject.put("FBBatchCode", "标准");
					ArrayList<Object> staLists = new ArrayList<Object>();
					for (HashMap<String, Object> singleData : standIntakeList) {
						staLists.add(singleData.get("body_weight"));
					}
					standarObject.put("HouseDatas", staLists);
					DCDatas.put(standarObject);
				}

				if (toutcome.size() != 0) {
					Object farm_breed_id = toutcome.get(0).get("farm_breed_id");
					int i = 0;
					Object batch_code = null;
					ArrayList<Object> HouseDa = new ArrayList<Object>();
					for (HashMap<String, Object> outcome : toutcome) {
						if (!farm_breed_id.equals(toutcome.get(i).get(
								"farm_breed_id"))
								|| i + 1 == toutcome.size()) {
							JSONObject tJSONObject = new JSONObject();
							tJSONObject.put("FarmBreedId", farm_breed_id);
							tJSONObject.put("FBBatchCode", batch_code);
							tJSONObject.put("HouseDatas", HouseDa);
							DCDatas.put(tJSONObject);
							if(DCDatas.length()>Constants.BroilerReportMaxBatch){
								 break;
							 }
							HouseDa = new ArrayList<Object>();
						}
						farm_breed_id = outcome.get("farm_breed_id");
						batch_code = outcome.get("batch_code");
						Object DFR = outcome.get("DFR");
						String showFlag = outcome.get("showFlag").toString();
						if (showFlag.equals("Y")) {
							HouseDa.add(DFR);
						}
						i++;
					}
				}

				resJson.put("Result", "Success");
				resJson.put("DCDatas", DCDatas);
				resJson.put("HouseId", HouseId);
			}
			resJson.put("FarmId", farm_id);
			resJson.put("CompareType", CompareType);
			/** 业务处理结束 **/
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
		long endReqTime = System.currentTimeMillis();
		if (endReqTime - startReqTime < 1500) {
			try {
				Thread.sleep(1500 - endReqTime + startReqTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		DealSuccOrFail.dealApp(request, response, dealRes, resJson);
		mLogger.info("=====Now end executing DFRRepController.DFRRep");
	}

	@RequestMapping("/DFRRep_v2")
	public void DFRRep_v2(HttpServletRequest request,
			HttpServletResponse response) {
		mLogger.info("=======Now start executing DFRRepController.DFRRep_v2");
		JSONObject resJson = new JSONObject();
		String dealRes = null;
		long startReqTime = System.currentTimeMillis();
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("updateFarm.para=" + paraStr);
			JSONObject jsonobject = new JSONObject(paraStr);
			mLogger.info("jsonObject=" + jsonobject.toString());
			/** 业务处理开始，查询、增加、修改、或删除 **/
			JSONObject params = jsonobject.optJSONObject("params");
			int userid = jsonobject.optInt("id_spa");
			String CompareType = params.optString("CompareType");
			if (CompareType == null || CompareType.equals("")) {
				CompareType = "01";
			}
			// 栋舍对比
			sSDUserOperationService.insert(
					SDUserOperationService.MENU_DATAANALYSIS_WEEKWEIGHT,
					SDUserOperationService.OPERATION_SELECT, userid);

			int farm_id = params.optInt("FarmId");
			
			JSONArray DCDatas = new JSONArray();
			int DataSize = 0;
			DCDatas.put(0, "");

			if (CompareType.equals("01")) {
				// 批次对比
				int FarmBreedId = params.optInt("FarmBreedId");
				String sql = "SELECT "
						+ "(CASE WHEN bd.growth_date > curdate()  THEN 'N' ELSE 'Y' END)  AS showFlag,"
						+ "hb.house_id,"
						+ "s_f_getHouseName(hb.house_id) AS housename,"
						+ "ceil(if(bd.age = 0, 1, bd.age)/7) as week_age,"
						+ "ifnull(round(avg(case when bd.cur_weight = 0 then null ELSE bd.cur_weight end),0),0) AS week_avg_weight "
						+ "FROM s_b_breed_detail AS bd LEFT JOIN s_b_house_breed AS hb ON bd.house_breed_id = hb.id "
						+ "WHERE 1=1 "
						+ "and hb.farm_breed_id = "
						+ FarmBreedId
						+ " "
						+ "AND bd.cur_weight >= 0 "
						+ "AND (CASE WHEN hb.market_date IS NOT NULL THEN bd.growth_date <= hb.market_date ELSE bd.growth_date IS NOT NULL END) "
						+ "GROUP BY bd.house_breed_id,ceil(if(bd.age = 0, 1, bd.age)/7) "
						+ "ORDER BY bd.house_breed_id,ceil(if(bd.age = 0, 1, bd.age)/7)";
				mLogger.info("DFRRepController.DFRRep_v2.SQL=" + sql);
				List<HashMap<String, Object>> toutcome = mBaseQueryService
						.selectMapByAny(sql);
				if (toutcome.size() != 0) {
					Object house_id = toutcome.get(0).get("house_id");
					int i = 0;
					Object housename = null;
					ArrayList<Object> HouseDa = new ArrayList<Object>();
					for (HashMap<String, Object> outcome : toutcome) {
						if (!house_id.equals(toutcome.get(i).get("house_id"))
								|| i + 1 == toutcome.size()) {
							JSONObject tJSONObject = new JSONObject();
							tJSONObject.put("HouseId", house_id);
							tJSONObject.put("housename", housename);
							tJSONObject.put("HouseDatas", HouseDa);
							if(DataSize < HouseDa.size()){
								 DataSize = HouseDa.size(); 
							 }
							DCDatas.put(tJSONObject);
							HouseDa = new ArrayList<Object>();
						}
						house_id = outcome.get("house_id");
						housename = outcome.get("housename");
						Object DFR = outcome.get("week_avg_weight");
						String showFlag = outcome.get("showFlag").toString();
						if (showFlag.equals("Y")) {
							HouseDa.add(DFR);
						}
						i++;
					}
				}
				resJson.put("FarmBreedId", FarmBreedId);
			}else if (CompareType.equals("02")) {
				// 栋舍对比
				int HouseId = params.optInt("HouseId");
				String sql = "SELECT"
						+ "(CASE WHEN bd.growth_date > curdate()  THEN 'N' ELSE 'Y' END)  AS showFlag,"
						+ "hb.farm_breed_id,"
						+ "(SELECT batch_code FROM s_b_farm_breed WHERE id = hb.farm_breed_id) AS batch_code,"
						+ "ceil(if(bd.age = 0, 1, bd.age)/7) as week_age,"
						+ "ifnull(round(avg(case when bd.cur_weight = 0 then null ELSE bd.cur_weight end),0),0) AS week_avg_weight "
						+ "FROM s_b_breed_detail AS bd LEFT JOIN s_b_house_breed AS hb ON bd.house_breed_id = hb.id "
						+ "WHERE hb.house_id = "
						+ HouseId
						+ " "
						+ "AND bd.cur_weight >= 0 "
						+ "AND (CASE WHEN hb.market_date IS NOT NULL THEN bd.growth_date <= hb.market_date ELSE bd.growth_date IS NOT NULL END) "
						+ "GROUP BY bd.house_breed_id,ceil(if(bd.age = 0, 1, bd.age)/7) "
						+ "ORDER BY bd.house_breed_id,ceil(if(bd.age = 0, 1, bd.age)/7)";

				mLogger.info("DFRRepController.DFRRep_v2.SQL=" + sql);
				List<HashMap<String, Object>> toutcome = mBaseQueryService
						.selectMapByAny(sql);
				if (toutcome.size() != 0) {
					Object farm_breed_id = toutcome.get(0).get("farm_breed_id");
					int i = 0;
					Object batch_code = null;
					ArrayList<Object> HouseDa = new ArrayList<Object>();
					for (HashMap<String, Object> outcome : toutcome) {
						if (!farm_breed_id.equals(toutcome.get(i).get(
								"farm_breed_id"))
								|| i + 1 == toutcome.size()) {
							JSONObject tJSONObject = new JSONObject();
							tJSONObject.put("FarmBreedId", farm_breed_id);
							tJSONObject.put("FBBatchCode", batch_code);
							tJSONObject.put("HouseDatas", HouseDa);
							if(DataSize < HouseDa.size()){
								 DataSize = HouseDa.size(); 
							}
							if(DCDatas.length()>Constants.BroilerReportMaxBatch){
								 break;
							}
							DCDatas.put(tJSONObject);
							HouseDa = new ArrayList<Object>();
						}
						farm_breed_id = outcome.get("farm_breed_id");
						batch_code = outcome.get("batch_code");
						Object DFR = outcome.get("week_avg_weight");
						String showFlag = outcome.get("showFlag").toString();
						if (showFlag.equals("Y")) {
							HouseDa.add(DFR);
						}
						i++;
					}
				}
				resJson.put("HouseId", HouseId);
			}
			
			List<HashMap<String, Object>> standIntakeList = null;
			if (farm_id != 0) {
				String standIntakeSQL = "SELECT ceil(if(age = 0, 1, age)/7) as week_age,round(max(body_weight),0)  as body_weight "
						+ " from s_b_chicken_standar where farm_id = "
						+ farm_id
						+ " and feed_type = 'mixed' GROUP BY ceil(if(age = 0, 1, age)/7) ";
				standIntakeList = mBaseQueryService.selectMapByAny(standIntakeSQL);
			}
			JSONObject standarObject = new JSONObject();
			if (CompareType.equals("01")) {
				standarObject.put("HouseId", -1);
				standarObject.put("housename", "标准");
			}else if (CompareType.equals("02")) {
				standarObject.put("FarmBreedId", -1);
				standarObject.put("FBBatchCode", "标准");
			}
			ArrayList<Object> staLists = new ArrayList<Object>();
			if (standIntakeList != null && standIntakeList.size() > 0) {
				for (HashMap<String, Object> singleData : standIntakeList) {
					if (staLists.size() >= Constants.BroilerReportMaxWeekAge) {
						if (staLists.size() >= DataSize) {
							DataSize = staLists.size();
							break;
						}
					}
					staLists.add(singleData.get("body_weight"));
				}
			}
			standarObject.put("HouseDatas", staLists);
			DCDatas.put(0, standarObject);

			JSONArray xData = new JSONArray();
			if(DataSize <= Constants.BroilerReportMaxWeekAge){
				DataSize = Constants.BroilerReportMaxWeekAge;
			}
			for(int x = 1; x <= DataSize; x++){
				xData.put(x);
			}
			resJson.put("xData", xData);

			resJson.put("Result", "Success");
			resJson.put("DCDatas", DCDatas);
			resJson.put("FarmId", farm_id);
			resJson.put("CompareType", CompareType);
			/** 业务处理结束 **/
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
		long endReqTime = System.currentTimeMillis();
		if (endReqTime - startReqTime < 1500) {
			try {
				Thread.sleep(1500 - endReqTime + startReqTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		DealSuccOrFail.dealApp(request, response, dealRes, resJson);
		mLogger.info("=====Now end executing DFRRepController.DFRRep_v2");
	}
}
