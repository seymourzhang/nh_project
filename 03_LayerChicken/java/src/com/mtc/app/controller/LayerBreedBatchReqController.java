/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.mtc.app.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mtc.app.service.*;
import com.mtc.entity.app.SBHouseBreed;
import org.apache.commons.collections.list.SynchronizedList;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mtc.app.biz.LayerBreedBatchReqManager;
import com.mtc.common.util.DealSuccOrFail;
import com.mtc.common.util.PubFun;
import com.mtc.common.util.constants.Constants;
import com.mtc.entity.app.SBLayerBreedDetail;
import com.mtc.entity.app.SBLayerFarmBreed;
import com.mtc.entity.app.SBLayerHouseBreed;
import com.mtc.app.service.SDUserOperationService;

/**
 * @ClassName: HouseReqController
 * @Description:
 * @Date 2015年11月24日 下午3:46:30
 * @Author Yin Guo Xiang
 * 
 */
@Controller
@RequestMapping("layer_breedBatch")
public class LayerBreedBatchReqController {

	private static Logger mLogger = Logger.getLogger(LayerBreedBatchReqController.class);
	@Autowired
	private LayerBreedBatchReqManager tLayerBreedBatchReqManager;
	@Autowired
	private SBLayerFarmBreedService tSBLayerFarmBreedService;
	@Autowired
	private SBLayerHouseBreedService tSBLayerHouseBreedService;
	@Autowired
	private SBLayerBreedDetailService lSBLayerBreedDetailservice;
	@Autowired
	private SBDeviHouseService tSBDeviHouseService;
	@Autowired
	private BaseQueryService tBaseQueryService;
	@Autowired
	private MySQLSPService tMySQLSPService;
	@Autowired
	private SDUserOperationService sSDUerOperationService;

	@RequestMapping("/createBatch")
	public void createBatch(HttpServletRequest request, HttpServletResponse response) {
		mLogger.info("=====Now start executing LayerBreedBatchReqController.createBatch");
		JSONObject resJson = new JSONObject();
		String dealRes = null;
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("saveHouse.para=" + paraStr);
			JSONObject jsonObject = new JSONObject(paraStr);
			mLogger.info("jsonObject=" + jsonObject.toString());
			/** 业务处理开始，查询、增加、修改、或删除 **/
			JSONObject tHouseJson = jsonObject.getJSONObject("params");
			int userId = jsonObject.optInt("id_spa");
			String BatchCode = tHouseJson.optString("BatchCode");
			int FarmId = tHouseJson.optInt("FarmId");
			sSDUerOperationService.insert(SDUserOperationService.MENU_FARMMANAGEMENT_ADD, SDUserOperationService.OPERATION_ADD, userId);
			mLogger.info("============新建批次操作信息：新增，导入完毕");
			String SQL = "SELECT id FROM s_b_layer_farm_breed WHERE batch_status='01' AND farm_id="
					+ FarmId;
			mLogger.info("========LayerBreedBatchReqController.createBatch.SQL=" + SQL);
			List<HashMap<String, Object>> dd = tBaseQueryService
					.selectMapByAny(SQL);
			if (dd.size() == 0) {
				String place_date = tHouseJson.optString("place_date");
				int place_day_age = tHouseJson.optInt("place_day_age");
				int place_week_age = tHouseJson.optInt("place_week_age");
				int place_num = tHouseJson.optInt("place_num");
				String place_type = tHouseJson.optString("place_type");
				String place_breed = tHouseJson.optString("place_breed");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date dateStr = sdf.parse(place_date);
				Date date = new Date();
				JSONArray place_detail = tHouseJson
						.optJSONArray("place_detail");
				JSONArray tSBHouseBreed = new JSONArray();
				for (int i = 0; i < place_detail.length(); i++) {
					SBLayerHouseBreed tSBLayerHouseBreed = new SBLayerHouseBreed();
					int HouseId = place_detail.getJSONObject(i).optInt(
							"HouseId");
					int placenum1 = place_detail.getJSONObject(i).optInt(
							"place_num");
					tSBLayerHouseBreed.setFarmId(FarmId);
					tSBLayerHouseBreed.setHouseId(HouseId);
					tSBLayerHouseBreed.setPlaceNum(placenum1);
					tSBLayerHouseBreed.setCreateDate(date);
					tSBLayerHouseBreed.setCreatePerson(userId);
					tSBLayerHouseBreed.setCreateTime(date);
					tSBLayerHouseBreed.setModifyDate(date);
					tSBLayerHouseBreed.setModifyTime(date);
					tSBLayerHouseBreed.setModifyPerson(userId);
					tSBHouseBreed.put(tSBLayerHouseBreed);
				}
				SBLayerBreedDetail tSBLayerBreedDetail = new SBLayerBreedDetail();
				tSBLayerBreedDetail.setDayAge(place_day_age);
				tSBLayerBreedDetail.setWeekAge(place_week_age);
				tSBLayerBreedDetail.setCreateDate(date);
				tSBLayerBreedDetail.setCreatePerson(userId);
				tSBLayerBreedDetail.setCreateTime(date);
				tSBLayerBreedDetail.setModifyDate(date);
				tSBLayerBreedDetail.setModifyTime(date);
				tSBLayerBreedDetail.setModifyPerson(userId);
				SBLayerFarmBreed tSBLayerFarmBreed = new SBLayerFarmBreed();
				tSBLayerFarmBreed.setFarmId(FarmId);
				tSBLayerFarmBreed.setBatchCode(BatchCode);
				tSBLayerFarmBreed.setPlaceDayAge(place_day_age);
				tSBLayerFarmBreed.setPlaceDate(dateStr);
				tSBLayerFarmBreed.setPlaceNum(place_num);
				tSBLayerFarmBreed.setPlaceWeekAge(place_week_age);
				tSBLayerFarmBreed.setBatchStatus("01");
				tSBLayerFarmBreed.setBakVar1(place_type);
				tSBLayerFarmBreed.setBakVar2(place_breed);
				tSBLayerFarmBreed.setCreateDate(date);
				tSBLayerFarmBreed.setCreatePerson(userId);
				tSBLayerFarmBreed.setCreateTime(date);
				tSBLayerFarmBreed.setModifyDate(date);
				tSBLayerFarmBreed.setModifyTime(date);
				tSBLayerFarmBreed.setModifyPerson(userId);
				HashMap<String, Object> tt = new HashMap<String, Object>();
				tt.put("SBLayerHouseBreed", tSBHouseBreed);
				tt.put("SBLayerFarmBreed", tSBLayerFarmBreed);
				tt.put("SBLayerBreedDetail", tSBLayerBreedDetail);
				int BreedBatchId = tLayerBreedBatchReqManager.createBatch(tt);
				resJson.put("Result", "Success");
				resJson.put("FarmId", FarmId);
				resJson.put("BreedBatchId", BreedBatchId);
			} else {
				resJson.put("ErrorMsg", "该批次尚未结算，请结算后再重新添加");
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
		mLogger.info("=====Now end executing LayerBreedBatchReqController.createBatch");
	}

	@RequestMapping("/queryBatch")
	public void queryBatch(HttpServletRequest request, HttpServletResponse response) {
		mLogger.info("=====Now start executing LayerBreedBatchReqController.queryBatch");
		JSONObject resJson = new JSONObject();
		String dealRes = null;
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("saveHouse.para=" + paraStr);
			JSONObject jsonObject = new JSONObject(paraStr);
			int userId = jsonObject.optInt("id_spa");
			mLogger.info("jsonObject=" + jsonObject.toString());
			// ** 业务处理开始，查询、增加、修改、或删除 **//
			JSONObject tHouseJson = jsonObject.getJSONObject("params");
			int BreedBatchId = tHouseJson.getInt("BreedBatchId");
			sSDUerOperationService.insert(SDUserOperationService.MENU_FARMMANAGEMENT_ADD, SDUserOperationService.OPERATION_SELECT, userId);
			mLogger.info("========新建批次操作信息：查询，导入完毕");
			String SQL = "SELECT fb.`farm_id` ,fb.bak_var1 ,fb.bak_var2 ,fb.`place_day_age`,fb.`batch_code`, DATE_FORMAT(fb.`place_date`,'%Y-%m-%d')AS place_date,"
					+ "fb.`place_week_age`,s_f_getHouseName(hb.`house_id`) as house_name,fb.`place_num`,hb.`house_id`,hb.place_num as place_num1 FROM  s_b_layer_house_breed hb "
					+ "LEFT  JOIN s_b_layer_farm_breed fb  ON fb.id= hb.farm_breed_id  WHERE fb.`id`="
					+ BreedBatchId;
			mLogger.info("========LayerBreedBatchReqController.queryBatch.SQL=" + SQL);
			List<HashMap<String, Object>> ff = tBaseQueryService
					.selectMapByAny(SQL);
			if (ff.size() > 0) {
				Object BatchCode = ff.get(0).get("batch_code");
				Object place_date = ff.get(0).get("place_date");
				Object place_day_age = ff.get(0).get("place_day_age");
				Object place_type = ff.get(0).get("bak_var1");
				Object place_breed = ff.get(0).get("bak_var2");
				Object place_num = ff.get(0).get("place_num");
				Object FarmId = ff.get(0).get("farm_id");
				Object place_week_age = ff.get(0).get("place_week_age");
				JSONArray place_detail = new JSONArray();
				for (HashMap<String, Object> hashMap : ff) {
					JSONObject tt = new JSONObject();
					Object HouseId = hashMap.get("house_id");
					Object place_num1 = hashMap.get("place_num1");
					Object house_name = hashMap.get("house_name");
					tt.put("HouseId", HouseId);
					tt.put("HouseName", house_name);
					tt.put("place_num", place_num1);
					place_detail.put(tt);
				}
				resJson.put("Result", "Success");
				resJson.put("FarmId", FarmId);
				resJson.put("BatchCode", BatchCode);
				resJson.put("BreedBatchId", BreedBatchId);
				resJson.put("place_date", place_date);
				resJson.put("place_day_age", place_day_age);
				resJson.put("place_week_age", place_week_age);
				resJson.put("place_type", place_type);
				resJson.put("place_breed", place_breed);
				resJson.put("place_num", place_num);
				resJson.put("place_detail", place_detail);

			} else {
				resJson.put("Result", "Fail");
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
		mLogger.info("=====Now end executing LayerBreedBatchReqController.queryBatch");
	}

	@RequestMapping("/queryProStandard")
	public void queryProStandard(HttpServletRequest request, HttpServletResponse response) {
		mLogger.info("=====Now start executing LayerBreedBatchReqController.queryProStandard");
		JSONObject resJson = new JSONObject();
		String dealRes = null;
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("saveHouse.para=" + paraStr);
			JSONObject jsonObject = new JSONObject(paraStr);
			int userId = jsonObject.optInt("id_spa");
			sSDUerOperationService.insert(SDUserOperationService.MENU_FARMMANAGEMENT_PRODUCTION, SDUserOperationService.OPERATION_SELECT, userId);
			mLogger.info("=====标准操作记录：查询，录入完毕");
			JSONArray Headers = new JSONArray();
			JSONArray TableDatas = new JSONArray();
			Headers.put("周龄");
			Headers.put("饲养日<br>产蛋率(%)");
			Headers.put("累积饲养日产蛋数");
			Headers.put("累积入舍鸡产蛋数");
			Headers.put("累积<br>死淘率(%)");
			Headers.put("体重<br>(千克)");
			Headers.put("采食量<br>(克/天.只鸡)");
			Headers.put("累积入舍鸡<br>产蛋总重(千克)");
			Headers.put("平均蛋重<br>(克)");
			String SQL = "SELECT week_age, round(day_lay_rate_max,0) AS day_lay_rate_max, round(day_lay_rate_min,0) AS day_lay_rate_min,\n"
					+ "         round(acc_cur_lay_max,1) AS  acc_cur_lay_max,round(acc_cur_lay_min,1) AS acc_cur_lay_min,\n"
					+ "            round(acc_ori_lay_max,1) AS acc_ori_lay_max, round(acc_ori_lay_min,1) AS acc_ori_lay_min,\n"
					+ "               round(acc_cd_rate,1) AS acc_cd_rate, chicken_weight_max, chicken_weight_min, round(day_feed_max,0) AS day_feed_max,\n"
					+ "                   round(day_feed_min,0) AS day_feed_min, round(acc_ori_lay_weight,1) AS acc_ori_lay_weight, round(lay_weight_max,1) AS lay_weight_max,\n"
					+ "                     round(lay_weight_min,1) AS lay_weight_min\n"
					+ "FROM s_d_layer_chicken_standar";
			mLogger.info("========LayerBreedBatchReqController.queryProStandard.SQL=" + SQL);
			List<HashMap<String, Object>> standardDatas = tBaseQueryService
					.selectMapByAny(SQL);
			mLogger.info("查询数据库结果:" + standardDatas);
			for (int i = 0; i < standardDatas.size(); ++i) {
				JSONObject TableDatas1 = new JSONObject();
				Object weekAge = standardDatas.get(i).get("week_age");
				TableDatas1.put("WeekAge", weekAge);
				String dayLayRateMin = standardDatas.get(i).get("day_lay_rate_min").toString();
				String dayLayRateMax = standardDatas.get(i).get("day_lay_rate_max").toString();
				Object dayLayRate = dayLayRateMin + "-" + dayLayRateMax;
				TableDatas1.put("day_lay_rate", dayLayRate);
				String accCurLayMin = standardDatas.get(i).get("acc_cur_lay_min").toString();
				String accCurLayMax = standardDatas.get(i).get("acc_cur_lay_max").toString();
				Object accCurLay = accCurLayMin + "-" + accCurLayMax;
				TableDatas1.put("acc_cur_lay", accCurLay);
				String accOriLayMin = standardDatas.get(i).get("acc_ori_lay_min").toString();
				String accOriLayMax = standardDatas.get(i).get("acc_ori_lay_max").toString();
				Object accOriLay = accOriLayMin + "-" + accOriLayMax;
				TableDatas1.put("acc_ori_lay", accOriLay);
				Object accCdRate = standardDatas.get(i).get("acc_cd_rate");
				TableDatas1.put("acc_cd_rate", accCdRate.toString());
				String chickenWeightMin = standardDatas.get(i).get("chicken_weight_min").toString();
				String chickenWeightMax = standardDatas.get(i).get("chicken_weight_max").toString();
				Object chickenWeight = chickenWeightMin + "-" + chickenWeightMax;
				TableDatas1.put("chicken_weight", chickenWeight);
				String dayFeedMin = standardDatas.get(i).get("day_feed_min").toString();
				String dayFeedMax = standardDatas.get(i).get("day_feed_max").toString();
				Object dayFeed = dayFeedMin + "-" + dayFeedMax;
				TableDatas1.put("day_feed", dayFeed);
				Object accOriLayWeight = standardDatas.get(i).get("acc_ori_lay_weight");
				TableDatas1.put("acc_ori_lay_weight", accOriLayWeight);
				String layWeightMin = standardDatas.get(i).get("lay_weight_min").toString();
				String layWeightMax = standardDatas.get(i).get("lay_weight_max").toString();
				Object layWeight = layWeightMin + "-" + layWeightMax;
				TableDatas1.put("lay_weight", layWeight);
				TableDatas.put(TableDatas1);
			}
			dealRes = Constants.RESULT_SUCCESS;
			resJson.put("Result", "Success");
			resJson.put("Headers", Headers);
			resJson.put("TableDatas", TableDatas);
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
		mLogger.info("=====Now end executing RepProSUMReqController.queryProStandard");
	}

	@RequestMapping("/historyDataQuery")
	public void historyDataQuery(HttpServletRequest request, HttpServletResponse response) {
		mLogger.info("=====Now start executing LayerBreedBatchReqController.historyDataQuery");
		JSONObject resJson = new JSONObject();
		String dealRes = null;
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("saveHouse.para=" + paraStr);
			JSONObject jsonObject = new JSONObject(paraStr);
			int userId = jsonObject.optInt("id_spa");
			sSDUerOperationService.insert(SDUserOperationService.MENU_FARMMANAGEMENT_HISTORYDATA, SDUserOperationService.OPERATION_SELECT, userId);
			mLogger.info("=====历史数据操作信息：查询，录入完毕");
			mLogger.info("jsonObject=" + jsonObject.toString());
			JSONArray tableDatas = new JSONArray();
			JSONObject tParams = jsonObject.getJSONObject("params");
			int farmBreedId = tParams.getInt("farmBreedId");
			int houseId = Integer.parseInt(tParams.get("houseId").toString());
			String SQL = "select day_age, week_age, culling_pm, ifnull(num_bak1,0) as num_bak1, cur_lay_num, cur_lay_broken, cur_feed, cur_water, cur_weight ,cur_lay_weight " +
					"from s_b_layer_breed_detail " +
					"where house_breed_id=(select id from s_b_layer_house_breed  where farm_breed_id = " + farmBreedId + " and house_id =" + houseId + ") " +
					"AND is_history='Y'";
			mLogger.info("======LayerBreedBatchReqController.historyDataQuery.SQL = " + SQL);
			List<HashMap<String, Object>> historyDatas = tBaseQueryService.selectMapByAny(SQL);
			for (int i = 0; i < historyDatas.size(); ++i) {
				JSONObject tableDatasSub = new JSONObject();
				tableDatasSub.put("day_age", historyDatas.get(i).get("day_age"));
				tableDatasSub.put("week_age", historyDatas.get(i).get("week_age"));
				tableDatasSub.put("culling_all", historyDatas.get(i).get("culling_pm"));
				tableDatasSub.put("curLayNum", historyDatas.get(i).get("cur_lay_num"));
				tableDatasSub.put("layStandard", historyDatas.get(i).get("num_bak1"));
				tableDatasSub.put("curBrokenNum", historyDatas.get(i).get("cur_lay_broken"));
				tableDatasSub.put("curLayWeight", historyDatas.get(i).get("cur_lay_weight"));
				tableDatasSub.put("daily_feed", historyDatas.get(i).get("cur_feed"));
				tableDatasSub.put("daily_weight", historyDatas.get(i).get("cur_weight"));
				tableDatasSub.put("daily_water", historyDatas.get(i).get("cur_water"));
				tableDatas.put(tableDatasSub);
			}
			resJson.put("Result", "Success");
			resJson.put("TableDatas", tableDatas);
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
		mLogger.info("=====Now start executing LayerBreedBatchReqController.historyDataQuery");
	}

	@RequestMapping("/createBatch_v2")
	public void createBatch_v2(HttpServletRequest request, HttpServletResponse response) {
		mLogger.info("=====Now start executing LayerBreedBatchReqController.createBatch");
		JSONObject resJson = new JSONObject();
		String dealRes = null;
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("saveHouse.para=" + paraStr);
			JSONObject jsonObject = new JSONObject(paraStr);
			mLogger.info("jsonObject=" + jsonObject.toString());
			/** 业务处理开始，查询、增加、修改、或删除 **/
			JSONObject tHouseJson = jsonObject.getJSONObject("params");
			int userId = jsonObject.optInt("id_spa");
			JSONObject batchInfo = tHouseJson.optJSONObject("batchInfo");
			JSONArray houseInfo = tHouseJson.optJSONArray("houseInfo");
			sSDUerOperationService.insert(SDUserOperationService.MENU_PRODUCTION_YOUNG, SDUserOperationService.OPERATION_ADD, userId);
			mLogger.info("============新建批次操作信息：新增，导入完毕");
			int FarmId = batchInfo.optInt("farmId");
			int FarmBreedId = batchInfo.optInt("farmBreedId");
			String BatchCode = batchInfo.optString("batchCode");
			String BatchDate = batchInfo.optString("batchDate");
			String PlaceType = batchInfo.optString("place_type");
			String PlaceBreed = batchInfo.optString("place_breed");

			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			String tResult = "";
			String errorMsg = "null";
			List<SBLayerBreedDetail> lDetails = new ArrayList<>();
			List<SBLayerHouseBreed> lHouseBreed = new ArrayList<>();
			List<SBLayerHouseBreed> lHouseBreedNew = new ArrayList<>();

			if (FarmBreedId == 0) {
				String SQL = "SELECT id FROM s_b_layer_farm_breed WHERE batch_status='01' AND farm_id="
						+ FarmId;
				mLogger.info("========LayerBreedBatchReqController.createBatch.SQL=" + SQL);
				List<HashMap<String, Object>> dd = tBaseQueryService.selectMapByAny(SQL);
				if (dd.size() != 0) {
					tResult = "Fail";
					errorMsg = "该批次尚未结算，请结算后再重新添加";
				} else {
					try {
						BatchCode = sdf.format(sdf.parse(BatchDate));
					} catch (ParseException e) {
						tResult = "Fail";
						errorMsg = "日期格式错误，请联系管理员！";
					}
					List<SBLayerHouseBreed> houseBreeds = new ArrayList<>();
					int PlaceNumSum = 0;
					for (int i = 0; i < houseInfo.length(); i++) {
						SBLayerHouseBreed tSBLayerHouseBreed = new SBLayerHouseBreed();
						int HouseId = houseInfo.getJSONObject(i).optInt("houseId");
						int PlaceNum = houseInfo.getJSONObject(i).optInt("place_num");
						int placeDayAge = houseInfo.getJSONObject(i).optInt("place_day_age");
						int placeWeekAge = houseInfo.getJSONObject(i).optInt("place_week_age");
						String placeDate = houseInfo.getJSONObject(i).optString("place_date");

						String tccSQL = "SELECT count(1) from s_b_house_breed where house_id = " + HouseId + " and market_date >= date '" + placeDate + "' ";
						if(tBaseQueryService.selectIntergerByAny(tccSQL) > 0){
							tResult = "Fail";
							errorMsg = "请确认新入雏的栋舍入雏日期大于上次出栏日期。";
							break;
						}

						if (PlaceNum != 0 && placeDayAge != 0 && placeWeekAge != 0 && !"".equals(placeDate)) {
							Date temp = sdf.parse(placeDate);
							tSBLayerHouseBreed.setFarmId(FarmId);
							tSBLayerHouseBreed.setHouseId(HouseId);
							tSBLayerHouseBreed.setPlaceDayAge(placeDayAge);
							tSBLayerHouseBreed.setPlaceWeekAge(placeWeekAge);
							tSBLayerHouseBreed.setBatchStatus("01");
							tSBLayerHouseBreed.setPlaceDate(temp);
							tSBLayerHouseBreed.setPlaceNum(PlaceNum);
							tSBLayerHouseBreed.setCreateDate(now);
							tSBLayerHouseBreed.setCreateTime(now);
							tSBLayerHouseBreed.setCreatePerson(userId);
							tSBLayerHouseBreed.setModifyDate(now);
							tSBLayerHouseBreed.setModifyTime(now);
							tSBLayerHouseBreed.setModifyPerson(userId);
							houseBreeds.add(tSBLayerHouseBreed);
							PlaceNumSum += PlaceNum;
						}
					}
					SBLayerFarmBreed tSBLayerFarmBreed = new SBLayerFarmBreed();
					tSBLayerFarmBreed.setFarmId(FarmId);
					tSBLayerFarmBreed.setBatchCode(BatchCode);
					tSBLayerFarmBreed.setPlaceDate(sdf.parse(BatchDate));
					tSBLayerFarmBreed.setPlaceNum(PlaceNumSum);
					tSBLayerFarmBreed.setPlaceDayAge(0);
					tSBLayerFarmBreed.setPlaceWeekAge(0);
					tSBLayerFarmBreed.setBatchStatus("01");
					tSBLayerFarmBreed.setBakVar1(PlaceType);
					tSBLayerFarmBreed.setBakVar2(PlaceBreed);
					tSBLayerFarmBreed.setCreateDate(now);
					tSBLayerFarmBreed.setCreatePerson(userId);
					tSBLayerFarmBreed.setCreateTime(now);
					tSBLayerFarmBreed.setModifyDate(now);
					tSBLayerFarmBreed.setModifyTime(now);
					tSBLayerFarmBreed.setModifyPerson(userId);

					if (!tResult.equals("Fail")) {
						HashMap<String, Object> tt = new HashMap<String, Object>();
						tt.put("SBLayerHouseBreed", houseBreeds);
						tt.put("SBLayerFarmBreed", tSBLayerFarmBreed);
						tt.put("farmBreedFlag", true);
						List<SBLayerHouseBreed> resHouseBreedList = tLayerBreedBatchReqManager.dealSave(tt);
					}
				}
			} else {
				HashMap<String, Object> tMap = new HashMap<>();
				String sqlCheck1 = "SELECT count(1) FROM s_b_layer_farm_breed WHERE id = " + FarmBreedId + "";
				Integer checkInfo1 = tBaseQueryService.selectIntergerByAny(sqlCheck1);
				if (checkInfo1 == 0) {
					tResult = "Fail";
					errorMsg = "该入雏批次在数据库中不存在，请勿修改。";
				} else {
					SBLayerFarmBreed sbLayerFarmBreed = tSBLayerFarmBreedService.selectByPrimaryKey(FarmBreedId);
					sbLayerFarmBreed.setModifyPerson(userId);
					sbLayerFarmBreed.setModifyDate(new Date());
					sbLayerFarmBreed.setModifyTime(new Date());

					for (int i = 0; i < houseInfo.length(); i++) {
						int HouseId = houseInfo.getJSONObject(i).optInt("houseId");
						int PlaceNum = houseInfo.getJSONObject(i).optInt("place_num");
						String placeDate = houseInfo.getJSONObject(i).getString("place_date");
						int placeDayAge = houseInfo.getJSONObject(i).optInt("place_day_age");
						int placeWeekAge = houseInfo.getJSONObject(i).optInt("place_week_age");
						Date temp = new Date();
						try {
							temp = sdf.parse(placeDate);
						} catch (ParseException e) {
							tResult = "Fail";
							errorMsg = "日期格式有误，请联系管理员！";
							break;
						}
						SBLayerHouseBreed sbLayerHouseBreed = tSBLayerHouseBreedService.selectByFarmIdAndHouseId(FarmId, HouseId, FarmBreedId);
						if (sbLayerHouseBreed == null) {
								sbLayerFarmBreed.setPlaceNum(sbLayerFarmBreed.getPlaceNum() + PlaceNum);

								SBLayerHouseBreed tSBLayerHouseBreed = new SBLayerHouseBreed();
								tSBLayerHouseBreed.setFarmId(FarmId);
								tSBLayerHouseBreed.setHouseId(HouseId);
								tSBLayerHouseBreed.setPlaceNum(PlaceNum);
								tSBLayerHouseBreed.setBatchStatus("01");
								tSBLayerHouseBreed.setPlaceDayAge(placeDayAge);
								tSBLayerHouseBreed.setPlaceWeekAge(placeWeekAge);
								tSBLayerHouseBreed.setPlaceDate(temp);
								tSBLayerHouseBreed.setCreateDate(now);
								tSBLayerHouseBreed.setCreateTime(now);
								tSBLayerHouseBreed.setCreatePerson(userId);
								tSBLayerHouseBreed.setModifyDate(now);
								tSBLayerHouseBreed.setModifyTime(now);
								tSBLayerHouseBreed.setModifyPerson(userId);
								lHouseBreedNew.add(tSBLayerHouseBreed);
								tMap.put("farmBreedFlag", false);
						} else {
							if (placeDayAge != sbLayerHouseBreed.getPlaceDayAge() || placeWeekAge != sbLayerHouseBreed.getPlaceWeekAge()){
								tResult = "Fail";
								errorMsg = houseInfo.getJSONObject(i).getString("houseName") + "栋已经入雏，生长日龄或生长周龄不允许修改！";
								break;
							} else if (!sbLayerHouseBreed.getPlaceDate().equals(temp)) {
								tResult = "Fail";
								errorMsg = houseInfo.getJSONObject(i).getString("houseName") + "栋已经入雏，入雏日期不允许修改。";
								break;
							} else {
								sbLayerFarmBreed.setPlaceNum(sbLayerFarmBreed.getPlaceNum() + PlaceNum);

								if ("02".equals(sbLayerHouseBreed.getBatchStatus().toString())) {
									tResult = "Fail";
									errorMsg = houseInfo.getJSONObject(i).getString("houseName") + "栋已出栏，不允许修改！";
									break;
								}
								if (PlaceNum == sbLayerHouseBreed.getPlaceNum()) {
									continue;
								}
								int placeNumPre = sbLayerHouseBreed.getPlaceNum();
								sbLayerFarmBreed.setPlaceNum(sbLayerFarmBreed.getPlaceNum() + (PlaceNum - placeNumPre));

								sbLayerHouseBreed.setPlaceNum(PlaceNum);
								sbLayerHouseBreed.setModifyDate(now);
								sbLayerHouseBreed.setModifyTime(now);
								sbLayerHouseBreed.setModifyPerson(userId);
								lHouseBreed.add(sbLayerHouseBreed);

								List<SBLayerBreedDetail> sbLayerBreedDetails = lSBLayerBreedDetailservice.selectByhouseBreedId(sbLayerHouseBreed.getId(), -1, -1);
								for (SBLayerBreedDetail sbLayerBreedDetail : sbLayerBreedDetails) {
									int dd1 = sbLayerBreedDetail.getCurAmount() + (PlaceNum - placeNumPre);
									int dd2 = sbLayerBreedDetail.getYtdAmount() + (PlaceNum - placeNumPre);
									sbLayerBreedDetail.setCurAmount(dd1);
									sbLayerBreedDetail.setYtdAmount(dd2);
									sbLayerBreedDetail.setModifyPerson(userId);
									sbLayerBreedDetail.setModifyDate(now);
									sbLayerBreedDetail.setModifyTime(now);
									lDetails.add(sbLayerBreedDetail);
								}
							}
						}
					}
					tMap.put("SBLayerFarmBreed", sbLayerFarmBreed);
					if (!tResult.equals("Fail")) {
						tMap.put("SBLayerBreedDetailList", lDetails);
						tMap.put("SBLayerHouseBreedUp", lHouseBreed);
						tMap.put("SBLayerHouseBreedInsert", lHouseBreedNew);
						List<SBLayerHouseBreed> resHouseBreedList = tLayerBreedBatchReqManager.dealUpdate(tMap);
					}
				}
			}
			if (!tResult.equals("Fail")){
				resJson.put("Result", "Success");
				resJson.put("Error", "");
			}else{
				resJson.put("Result", "Fail");
				resJson.put("Error", errorMsg);
			}
			dealRes = Constants.RESULT_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resJson = new JSONObject();
				resJson.put("Error", "程序处理错误，请联系管理员！");
				resJson.put("Result", "Fail");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			dealRes = Constants.RESULT_FAIL;
		}
		DealSuccOrFail.dealApp(request, response, dealRes, resJson);
		mLogger.info("=====Now end executing LayerBreedBatchReqController.createBatch");
	}

	@RequestMapping("/queryBatch_v2")
	public void queryBatch_v2(HttpServletRequest request, HttpServletResponse response) {
		mLogger.info("=====Now start executing LayerBreedBatchReqController.queryBatch_v2");
		JSONObject resJson = new JSONObject();
		String dealRes = null;
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("saveHouse.para=" + paraStr);
			JSONObject jsonObject = new JSONObject(paraStr);
			int userId = jsonObject.optInt("id_spa");
			mLogger.info("jsonObject=" + jsonObject.toString());
			// ** 业务处理开始，查询、增加、修改、或删除 **//
			JSONObject tHouseJson = jsonObject.getJSONObject("params");

			int FarmBreedId = tHouseJson.optInt("farmBreedId");
			int FarmId = tHouseJson.optInt("farmId");

			sSDUerOperationService.insert(SDUserOperationService.MENU_PRODUCTION_YOUNG, SDUserOperationService.OPERATION_SELECT, userId);
			mLogger.info("========新建批次操作信息：查询，导入完毕");

			String sql = "SELECT " +
					"  sblfb.batch_code, " +
					"  date_format(sblfb.place_date, '%Y-%m-%d') batchDate, " +
					"  sblfb.bak_var1                            type, " +
					"  sblfb.bak_var2                            breed, " +
					"  h.id house_id, " +
					"  h.house_name," +
					"  sblhb.batch_status,  " +
					"  sblhb.place_num, " +
					"  date_format(sblhb.place_date, '%Y-%m-%d') place_date, " +
					"  sblhb.place_day_age, " +
					"  sblhb.place_week_age " +
					"from s_d_house h " +
					"  LEFT JOIN s_b_layer_house_breed sblhb on sblhb.house_id = h.id AND sblhb.farm_breed_id = " + FarmBreedId +
					"  LEFT JOIN s_b_layer_farm_breed sblfb on sblfb.id = sblhb.farm_breed_id " +
					"WHERE h.farm_id =  " + FarmId +
					" order by h.house_name";
			mLogger.info("========LayerBreedBatchReqController.queryBatch_2.SQL=" + sql);
			List<HashMap<String, Object>> ff = tBaseQueryService.selectMapByAny(sql);
			if (ff.size() != 0) {
				JSONArray houseInfo = new JSONArray();
				JSONObject batch = new JSONObject();
				batch = new JSONObject();
				for (HashMap<String, Object> stringObjectHashMap : ff) {
					JSONObject house = new JSONObject();
					batch.put("farmId", FarmId);
					batch.put("farmBreedId", FarmBreedId);
					Object batchCode = stringObjectHashMap.get("batch_code");
					Object batchDate = stringObjectHashMap.get("batchDate");
					Object placeType = stringObjectHashMap.get("type");
					Object placeBreed = stringObjectHashMap.get("breed");
					if (batchCode != null && batchDate != null && placeType != null && placeBreed != null) {
						batch.put("batchCode", batchCode);
						batch.put("batchDate", batchDate);
						batch.put("place_type", placeType);
						batch.put("place_breed", placeBreed);
					}

					house.put("houseId", stringObjectHashMap.get("house_id"));
					house.put("houseName", stringObjectHashMap.get("house_name"));
					house.put("batch_status", stringObjectHashMap.get("batch_status") == null ? "00" : stringObjectHashMap.get("batch_status"));
					house.put("place_date", stringObjectHashMap.get("place_date"));
					house.put("place_day_age", stringObjectHashMap.get("place_day_age"));
					house.put("place_week_age", stringObjectHashMap.get("place_week_age"));
					house.put("place_num", stringObjectHashMap.get("place_num"));
					houseInfo.put(house);
				}
				resJson.put("batchInfo", batch);
				resJson.put("houseInfo", houseInfo);
				resJson.put("Result", "Success");
			} else {
				resJson.put("Error", "暂无入雏信息！");
				resJson.put("Result", "Fail");
			}
			dealRes = Constants.RESULT_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resJson = new JSONObject();
				resJson.put("Error", "程序处理错误，请联系管理员！");
				resJson.put("Result", "Fail");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			dealRes = Constants.RESULT_FAIL;
		}
		DealSuccOrFail.dealApp(request, response, dealRes, resJson);
		mLogger.info("=====Now end executing LayerBreedBatchReqController.queryBatch_v2");
	}

	@RequestMapping("/lairageBatch_v2")
	public void lairageBatch_v2(HttpServletRequest request, HttpServletResponse response) {
		mLogger.info("=====Now start executing LayerBreedBatchReqController.createBatch");
		JSONObject resJson = new JSONObject();
		String dealRes = null;
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("saveHouse.para=" + paraStr);
			JSONObject jsonObject = new JSONObject(paraStr);
			mLogger.info("jsonObject=" + jsonObject.toString());
			/** 业务处理开始，查询、增加、修改、或删除 **/
			JSONObject tHouseJson = jsonObject.getJSONObject("params");
			int userId = jsonObject.optInt("id_spa");
			String SettleFlag = tHouseJson.optString("settleFlag");
			int FarmBreedId = tHouseJson.optInt("farmBreedId");

			sSDUerOperationService.insert(SDUserOperationService.MENU_PRODUCTION_OUTPUT, SDUserOperationService.OPERATION_ADD, userId);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date now = new Date();
			HashMap<String, Object> tPara = new HashMap<>();

			if ("house".equals(SettleFlag)) {
				String MarketDate = sdf.format(sdf.parse(tHouseJson.optString("marketDate")));
				int HouseId = tHouseJson.optInt("houseId");
				String HouseName = tHouseJson.optString("houseName");
				int HouseBreedId = tHouseJson.optInt("houseBreedId");
				int MarketNum = tHouseJson.optInt("marketNum");
				if (MarketNum == 0) {
					resJson.put("Result", "Fail");
					resJson.put("Error", HouseName + "您输入的出栏数据不全，请重新输入！");
				}
				SBLayerHouseBreed tdata = tSBLayerHouseBreedService.selectByPrimaryKey(HouseBreedId);
				if (sdf.parse(MarketDate).before(tdata.getPlaceDate())) {
					resJson.put("Result", "Fail");
					resJson.put("Error", HouseName + "您输入的出栏日期在入雏日期之前，请重新输入！");
				} else if ("02".equals(tdata.getBatchStatus())) {
					resJson.put("Result", "Fail");
					resJson.put("Error", HouseName + "栋已出栏！");
				} else {
					//校验,输入的出栏日期之后是否有日报数据：是，不允许出栏。
					String SQL = "SELECT count(*) FROM s_b_layer_breed_detail " +
							"WHERE house_breed_id = " + HouseBreedId + " " +
							"AND growth_date > '" + MarketDate + "' " +
							"AND (cur_cd <> 0 OR cur_feed <> 0 OR cur_weight <> 0 OR num_bak1 <> 0)";
					Integer tag = tBaseQueryService.selectIntergerByAny(SQL);
					if (tag > 0) {
						resJson.put("Result", "Fail");
						resJson.put("Error", "您输入的出栏日期之后有日报数据，不允许出栏。");
					} else {
						String sql = "select max(day_age) age " +
								" from s_b_layer_breed_detail where house_breed_id = " + HouseBreedId +
								" and growth_date <= '" + MarketDate + "'";
						List<HashMap<String, Object>> tData = tBaseQueryService.selectMapByAny(sql);
						if (tData.size() == 0) {
							resJson.put("Result", "Fail");
							resJson.put("Error", "您输入的出栏日期有误，请重新填写！");
						} else {
							int age = Integer.parseInt(tData.get(0).get("age").toString());
							SBLayerBreedDetail sbLayerBreedDetail = lSBLayerBreedDetailservice.selectByPrimaryKey(HouseBreedId, age);
							int placeNum = tdata.getPlaceNum();
							int dValue = placeNum - MarketNum - (sbLayerBreedDetail.getAccCd() - sbLayerBreedDetail.getCurCd());
							if (dValue < 0) {
								resJson.put("Result", "Fail");
								resJson.put("Error", "您输入的出栏数有误（超过初始入雏总数），请重新填写。");
							} else {
								tdata.setBakInt1(MarketNum);
								tdata.setMarketDate(sbLayerBreedDetail.getGrowthDate());
								tdata.setBatchStatus("02");
								tdata.setModifyPerson(userId);
								tdata.setModifyDate(now);
								tPara.put("SBLayerHouseBreed", tdata);

								sbLayerBreedDetail.setDeathAm(0);
								sbLayerBreedDetail.setDeathPm(0);
								sbLayerBreedDetail.setCullingAm(0);
								sbLayerBreedDetail.setCullingPm(dValue);
								sbLayerBreedDetail.setAccCd(dValue + (sbLayerBreedDetail.getAccCd() - sbLayerBreedDetail.getCurCd()));
								sbLayerBreedDetail.setCurCd(dValue);
								sbLayerBreedDetail.setModifyPerson(userId);
								sbLayerBreedDetail.setModifyDate(now);
								sbLayerBreedDetail.setModifyTime(now);
								tPara.put("SBLayerBreedDetail", sbLayerBreedDetail);
								tPara.put("flag", SettleFlag);
								tLayerBreedBatchReqManager.lairageDeal(tPara);
								resJson.put("houseId", HouseId);
								resJson.put("houseBreedId", HouseBreedId);
								resJson.put("Result", "Success");
								resJson.put("Error", "");
							}
						}
					}
				}
			} else if ("farm".equals(SettleFlag)) {
				SBLayerFarmBreed sbLayerFarmBreed = tSBLayerFarmBreedService.selectByPrimaryKey(FarmBreedId);
				if (sbLayerFarmBreed == null) {
					resJson.put("Result", "Fail");
					resJson.put("Error", "暂不存在该农场批次信息！");
				} else {
					String sql = "select count(*) from s_b_layer_house_breed where batch_status <> '02' and farm_breed_id = " + FarmBreedId;
					int num = tBaseQueryService.selectIntergerByAny(sql);
					if (num > 0) {
						resJson.put("Result", "Fail");
						resJson.put("Error", "当前批次存在未出栏栋舍，不允许结束本批次！");
					} else {
						String sSQL = "select min(place_date) place_date from s_b_layer_house_breed where farm_breed_id = " + FarmBreedId;
						String minDate = tBaseQueryService.selectStringByAny(sSQL);
						if (now.before(sdf.parse(minDate))) {
							resJson.put("Result", "Fail");
							resJson.put("Error", "最小出栏日期大于当前日期，不允许结束本批次！");
						} else {
							sbLayerFarmBreed.setBatchStatus("02");
							sbLayerFarmBreed.setMarketDate(now);
							sbLayerFarmBreed.setModifyPerson(userId);
							sbLayerFarmBreed.setModifyDate(now);
							sbLayerFarmBreed.setModifyTime(now);
							tPara.put("flag", SettleFlag);
							tPara.put("SBLayerFarmBreed", sbLayerFarmBreed);
							tLayerBreedBatchReqManager.lairageDeal(tPara);
							resJson.put("Result", "Success");
							resJson.put("Error", "");
						}
					}
				}
			}
			dealRes = Constants.RESULT_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resJson.put("Result", "Fail");
				resJson.put("Error", "程序处理错误，请联系管理员！");
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			dealRes = Constants.RESULT_SUCCESS;
		}
		DealSuccOrFail.dealApp(request, response, dealRes, resJson);
		mLogger.info("=====Now end executing LayerBreedBatchReqController.lairageBatch_v2");
	}

	@RequestMapping("/lairageQuery_v2")
	public void lairageQuery_v2(HttpServletRequest request, HttpServletResponse response) {
		mLogger.info("=====Now start executing LayerBreedBatchReqController.lairageQuery_v2");
		JSONObject resJson = new JSONObject();
		String dealRes = null;
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("saveHouse.para=" + paraStr);
			JSONObject jsonObject = new JSONObject(paraStr);
			int userId = jsonObject.optInt("id_spa");
			mLogger.info("jsonObject=" + jsonObject.toString());
			// ** 业务处理开始，查询、增加、修改、或删除 **//
			JSONObject tHouseJson = jsonObject.getJSONObject("params");

			int FarmBreedId = tHouseJson.optInt("farmBreedId");
			int FarmId = tHouseJson.optInt("farmId");

			sSDUerOperationService.insert(SDUserOperationService.MENU_PRODUCTION_OUTPUT, SDUserOperationService.OPERATION_SELECT, userId);
			mLogger.info("========新建批次操作信息：查询，导入完毕");

			String sql = "SELECT " +
					"  lhb.farm_breed_id, " +
					"  h.id house_id, " +
					"  h.house_name, " +
					"  lhb.id house_breed_id, " +
					"  lhb.batch_status, " +
					"  lhb.bak_int1 market_num, " +
					"  date_format(lhb.market_date, '%Y-%m-%d') market_date " +
					" FROM s_b_layer_house_breed lhb " +
					"  LEFT JOIN s_d_house h ON lhb.house_id = h.id and lhb.farm_breed_id = " + FarmBreedId +
					" WHERE h.farm_id = " + FarmId +
					" order by h.house_name";
			mLogger.info("========LayerBreedBatchReqController.lairageQuery_v2.SQL=" + sql);
			List<HashMap<String, Object>> ff = tBaseQueryService.selectMapByAny(sql);
			if (ff.size() != 0) {
				JSONArray houseInfo = new JSONArray();
				JSONObject batch = new JSONObject();
				batch = new JSONObject();
				for (HashMap<String, Object> stringObjectHashMap : ff) {
					JSONObject house = new JSONObject();
					batch.put("farmId", FarmId);
					batch.put("farmBreedId", FarmBreedId);

					house.put("houseId", stringObjectHashMap.get("house_id"));
					house.put("houseName", stringObjectHashMap.get("house_name"));
					house.put("marketNum", stringObjectHashMap.get("market_num"));
					house.put("houseBreedId", stringObjectHashMap.get("house_breed_id") == null ? "" : stringObjectHashMap.get("house_breed_id"));
					house.put("breedStatus", stringObjectHashMap.get("batch_status") == null ? "00" : stringObjectHashMap.get("batch_status"));
					house.put("market_date", stringObjectHashMap.get("market_date"));
					houseInfo.put(house);
				}
				resJson.put("settleFarm", batch);
				resJson.put("settleHouse", houseInfo);
				resJson.put("Result", "Success");
			} else {
				resJson.put("Error", "暂无批次信息！");
				resJson.put("Result", "Fail");
			}
			dealRes = Constants.RESULT_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resJson = new JSONObject();
				resJson.put("Error", "程序处理错误，请联系管理员！");
				resJson.put("Result", "Fail");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			dealRes = Constants.RESULT_FAIL;
		}
		DealSuccOrFail.dealApp(request, response, dealRes, resJson);
		mLogger.info("=====Now end executing LayerBreedBatchReqController.lairageQuery_v2");
	}
}
