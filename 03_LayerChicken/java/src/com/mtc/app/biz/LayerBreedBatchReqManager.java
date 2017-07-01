package com.mtc.app.biz;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mtc.app.service.*;
import com.mtc.entity.app.*;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mtc.common.util.PubFun;

@Component
public class LayerBreedBatchReqManager {
	private static Logger mLogger =Logger.getLogger(LayerBreedBatchReqManager.class);
	@Autowired
	private BaseQueryService mBaseQueryService;
	@Autowired
	private SBLayerFarmBreedService tSBLayerFarmBreedService;
	@Autowired
	private SBLayerHouseBreedService tSBLayerHouseBreedService;
	@Autowired
	private SBLayerBreedDetailService tSBLayerBreedDetailService;
	@Autowired
	private SBLayerFarmBreedService tSBFarmBreedService;
	@Autowired
	private SBEggSellsService tSBEggSellsService;
	
	private String tCalType = "02";

	public int createBatch(HashMap<String, Object> tt) throws JSONException,
			ParseException {
		JSONArray lSBLayerHouseBreed = (JSONArray) tt.get("SBLayerHouseBreed");
		SBLayerFarmBreed tSBLayerFarmBreed = (SBLayerFarmBreed) tt
				.get("SBLayerFarmBreed");
		tSBFarmBreedService.insert(tSBLayerFarmBreed);
		List<SBLayerBreedDetail> tJSONArray = new ArrayList<SBLayerBreedDetail>();
		List<SBEggSells> tSBEggSellsJSONArray = new ArrayList<SBEggSells>();
		SBLayerBreedDetail ySBLayerBreedDetail = (SBLayerBreedDetail) tt
				.get("SBLayerBreedDetail");
		BigDecimal tBigDecimal = new BigDecimal(0);
		for (int i = 0; i < lSBLayerHouseBreed.length(); i++) {
			SBLayerHouseBreed lsbhousebreed = (SBLayerHouseBreed) lSBLayerHouseBreed
					.get(i);
			lsbhousebreed.setFarmBreedId(tSBLayerFarmBreed.getId());
			tSBLayerHouseBreedService.insert(lsbhousebreed);

			// 当前日期、当前日龄以及当前周龄
			Date nowDate = new Date();
			int nowAge = PubFun.daysBetween(tSBLayerFarmBreed.getPlaceDate(),nowDate) + ySBLayerBreedDetail.getDayAge();
			int nowWeekAge = PubFun.getWeekAge(tCalType, nowAge, nowDate);
			SBLayerBreedDetail tSBLayerBreedDetail = null;
			int m = 0;
			for (int j = 0; j < 800; j++) {
				m++;
				// 每次循环所对应的日期、日龄以及周龄
				Date dealdate = PubFun.addDate(
						tSBLayerFarmBreed.getPlaceDate(), j);
				int dealAge = ySBLayerBreedDetail.getDayAge() + j;
				int dealWeekAge = PubFun.getWeekAge(tCalType, dealAge, dealdate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(dealdate);
				int dealDayOFWeek = cal.get(Calendar.DAY_OF_WEEK);
				// 1、当前周龄 大于 处理的周龄时，需要插入一条数据
				// 2、当前周龄 小于等于 处理的周龄时，需要每天插入一条数据
				if ((nowWeekAge > dealWeekAge && m >= 4 && dealDayOFWeek == 7)
						|| nowWeekAge <= dealWeekAge) {
					tSBLayerBreedDetail = new SBLayerBreedDetail();
					tSBLayerBreedDetail.setGrowthDate(dealdate);
					tSBLayerBreedDetail.setHouseBreedId(lsbhousebreed.getId());
					tSBLayerBreedDetail.setDayAge(dealAge);
					tSBLayerBreedDetail.setWeekAge(dealWeekAge);
					tSBLayerBreedDetail
							.setIsHistory(nowWeekAge > dealWeekAge ? "Y" : "N");
					tSBLayerBreedDetail.setCreateDate(ySBLayerBreedDetail
							.getCreateDate());
					tSBLayerBreedDetail.setCreatePerson(ySBLayerBreedDetail
							.getCreatePerson());
					tSBLayerBreedDetail.setCreateTime(ySBLayerBreedDetail
							.getCreateDate());
					tSBLayerBreedDetail.setDeathAm(0);
					tSBLayerBreedDetail.setDeathPm(0);
					tSBLayerBreedDetail.setCullingAm(0);
					tSBLayerBreedDetail.setCullingPm(0);
					tSBLayerBreedDetail.setCurCd(0);
					tSBLayerBreedDetail.setAccCd(0);
					tSBLayerBreedDetail.setCurFeed(tBigDecimal);
					tSBLayerBreedDetail.setAccFeed(tBigDecimal);
					tSBLayerBreedDetail.setCurWeight(tBigDecimal);
					tSBLayerBreedDetail.setCurAmount(lsbhousebreed
							.getPlaceNum());
					tSBLayerBreedDetail.setYtdAmount(lsbhousebreed
							.getPlaceNum());
					tSBLayerBreedDetail.setCurWater(tBigDecimal);
					tSBLayerBreedDetail.setAccWater(tBigDecimal);
					tSBLayerBreedDetail.setCurLayNum(0);
					tSBLayerBreedDetail.setAccLayNum(0);
					tSBLayerBreedDetail.setCurLayWeight(tBigDecimal);
					tSBLayerBreedDetail.setAccLayWeight(tBigDecimal);
					tSBLayerBreedDetail.setCurLayBroken(0);
					tSBLayerBreedDetail.setAccLayBroken(0);
					tSBLayerBreedDetail.setCreateDate(ySBLayerBreedDetail
							.getCreateDate());
					tSBLayerBreedDetail.setCreatePerson(ySBLayerBreedDetail
							.getCreatePerson());
					tSBLayerBreedDetail.setCreateTime(ySBLayerBreedDetail
							.getCreateDate());
					tSBLayerBreedDetail.setModifyDate(ySBLayerBreedDetail
							.getCreateDate());
					tSBLayerBreedDetail.setModifyTime(ySBLayerBreedDetail
							.getCreateDate());
					tSBLayerBreedDetail.setModifyPerson(ySBLayerBreedDetail
							.getCreatePerson());
					tJSONArray.add(tSBLayerBreedDetail);
				}
				if(i == 0){
					SBEggSells NSBEggSells = new SBEggSells();
					NSBEggSells.setFarmId(tSBLayerFarmBreed.getFarmId());
					NSBEggSells.setFarmBreedId(tSBLayerFarmBreed.getId());
					NSBEggSells.setSellDate(dealdate);
					NSBEggSells.setIsHistory("N");
					
					NSBEggSells.setGoodBoxSize(tBigDecimal);
					NSBEggSells.setGoodPriceType("01");
					NSBEggSells.setGoodPriceValue(tBigDecimal);
					NSBEggSells.setGoodSaleboxNum(tBigDecimal);
					NSBEggSells.setGoodSaleWeight(tBigDecimal);
					NSBEggSells.setBrokenBoxSize(tBigDecimal);
					NSBEggSells.setBrokenPriceType("01");
					NSBEggSells.setBrokenPriceValue(tBigDecimal);
					NSBEggSells.setBrokenSaleboxNum(tBigDecimal);
					NSBEggSells.setBrokenSaleWeight(tBigDecimal);
					
					NSBEggSells.setChickenManure(tBigDecimal);
					
					NSBEggSells.setWeekAge(dealWeekAge);
					NSBEggSells.setDayAge(dealAge);
					NSBEggSells.setModifyDate(ySBLayerBreedDetail
							.getModifyDate());
					NSBEggSells.setModifyPerson(ySBLayerBreedDetail
							.getModifyPerson());
					NSBEggSells.setModifyTime(ySBLayerBreedDetail
							.getModifyTime());
					NSBEggSells.setCreateDate(ySBLayerBreedDetail
							.getCreateDate());
					NSBEggSells.setCreatePerson(ySBLayerBreedDetail
							.getCreatePerson());
					NSBEggSells.setCreateTime(ySBLayerBreedDetail
							.getCreateTime());
					tSBEggSellsJSONArray.add(NSBEggSells);
				}
			}
		}
		tSBEggSellsService.insertBatch(tSBEggSellsJSONArray);
		tSBLayerBreedDetailService.insertBatch(tJSONArray);
		mLogger.info("新建批次成功");
		return tSBLayerFarmBreed.getId();
	}

	public List<SBLayerHouseBreed> dealSave(HashMap<String,Object> tPara) throws JSONException, ParseException {
		List<SBLayerHouseBreed> layerHouseBreeds = (List<SBLayerHouseBreed>) tPara.get("SBLayerHouseBreed");
		SBLayerFarmBreed sbLayerFarmBreed = (SBLayerFarmBreed) tPara.get("SBLayerFarmBreed");

		boolean flag = (boolean) tPara.get("farmBreedFlag");
		int farmBreedId = 0;
		if (flag) {
			tSBLayerFarmBreedService.insert(sbLayerFarmBreed);
		}

		farmBreedId = sbLayerFarmBreed.getId();
		List<SBLayerBreedDetail> lSBBreedDetail = null;
		List<SBEggSells> tSBEggSellsJSONArray = new ArrayList<SBEggSells>();
		BigDecimal tBigDecimal = new BigDecimal(0);
		if (layerHouseBreeds != null && layerHouseBreeds.size() > 0) {
			for (int i = 0; i < layerHouseBreeds.size(); ++i) {
				layerHouseBreeds.get(i).setFarmBreedId(farmBreedId);
				tSBLayerHouseBreedService.insert(layerHouseBreeds.get(i));
				int houseBreedId = layerHouseBreeds.get(i).getId();
				Date tempDate = layerHouseBreeds.get(i).getPlaceDate();
				lSBBreedDetail = new ArrayList<>();
				// 当前日期、当前日龄以及当前周龄
				Date nowDate = new Date();
				int nowAge = PubFun.daysBetween(layerHouseBreeds.get(i).getPlaceDate(), nowDate) + layerHouseBreeds.get(i).getPlaceDayAge();
				int nowWeekAge = PubFun.getWeekAge(tCalType, nowAge, nowDate);
				int m = 0;
				for (int j = 0; j < 800; j++) {
					m++;
					tempDate = PubFun.addDate(layerHouseBreeds.get(i).getPlaceDate(), j);
					int dealAge = layerHouseBreeds.get(i).getPlaceDayAge() + j;
					int dealWeekAge = PubFun.getWeekAge(tCalType, dealAge, tempDate);
					Calendar cal = Calendar.getInstance();
					cal.setTime(tempDate);
					int dealDayOFWeek = cal.get(Calendar.DAY_OF_WEEK);
					// 1、当前周龄 大于 处理的周龄时，需要插入一条数据
					// 2、当前周龄 小于等于 处理的周龄时，需要每天插入一条数据
					if ((nowWeekAge > dealWeekAge && m >= 4 && dealDayOFWeek == 7) || nowWeekAge <= dealWeekAge) {
						SBLayerBreedDetail tSBLayerBreedDetail = new SBLayerBreedDetail();
						tSBLayerBreedDetail.setHouseBreedId(houseBreedId);
						tSBLayerBreedDetail.setDayAge(dealAge);
						tSBLayerBreedDetail.setWeekAge(dealWeekAge);
						tSBLayerBreedDetail.setGrowthDate(tempDate);
						tSBLayerBreedDetail.setIsHistory(nowWeekAge > dealWeekAge ? "Y" : "N");
						tSBLayerBreedDetail.setDeathAm(0);
						tSBLayerBreedDetail.setDeathPm(0);
						tSBLayerBreedDetail.setCullingAm(0);
						tSBLayerBreedDetail.setCullingPm(0);
						tSBLayerBreedDetail.setCurCd(0);
						tSBLayerBreedDetail.setAccCd(0);
						tSBLayerBreedDetail.setCurFeed(new BigDecimal(0));
						tSBLayerBreedDetail.setAccFeed(new BigDecimal(0));
						tSBLayerBreedDetail.setCurWeight(new BigDecimal(0));
						tSBLayerBreedDetail.setCurWater(new BigDecimal(0));
						tSBLayerBreedDetail.setAccWater(new BigDecimal(0));
						tSBLayerBreedDetail.setCurLayNum(0);
						tSBLayerBreedDetail.setAccLayNum(0);
						tSBLayerBreedDetail.setCurLayWeight(new BigDecimal(0));
						tSBLayerBreedDetail.setAccLayWeight(new BigDecimal(0));
						tSBLayerBreedDetail.setCurLayBroken(0);
						tSBLayerBreedDetail.setAccLayBroken(0);
						tSBLayerBreedDetail.setCurAmount(layerHouseBreeds.get(i).getPlaceNum());
						tSBLayerBreedDetail.setYtdAmount(layerHouseBreeds.get(i).getPlaceNum());
						tSBLayerBreedDetail.setNumBak1(new BigDecimal(0));
						tSBLayerBreedDetail.setCreatePerson(layerHouseBreeds.get(i).getCreatePerson());
						tSBLayerBreedDetail.setCreateDate(new Date());
						tSBLayerBreedDetail.setCreateTime(new Date());
						tSBLayerBreedDetail.setModifyPerson(layerHouseBreeds.get(i).getModifyPerson());
						tSBLayerBreedDetail.setModifyDate(new Date());
						tSBLayerBreedDetail.setModifyTime(new Date());
						lSBBreedDetail.add(tSBLayerBreedDetail);
					}
					if(i == 0){
						SBEggSells NSBEggSells = new SBEggSells();
						NSBEggSells.setFarmId(sbLayerFarmBreed.getFarmId());
						NSBEggSells.setFarmBreedId(sbLayerFarmBreed.getId());
						NSBEggSells.setSellDate(tempDate);
						NSBEggSells.setIsHistory("N");

						NSBEggSells.setGoodBoxSize(tBigDecimal);
						NSBEggSells.setGoodPriceType("01");
						NSBEggSells.setGoodPriceValue(tBigDecimal);
						NSBEggSells.setGoodSaleboxNum(tBigDecimal);
						NSBEggSells.setGoodSaleWeight(tBigDecimal);
						NSBEggSells.setBrokenBoxSize(tBigDecimal);
						NSBEggSells.setBrokenPriceType("01");
						NSBEggSells.setBrokenPriceValue(tBigDecimal);
						NSBEggSells.setBrokenSaleboxNum(tBigDecimal);
						NSBEggSells.setBrokenSaleWeight(tBigDecimal);

						NSBEggSells.setChickenManure(tBigDecimal);

						NSBEggSells.setWeekAge(dealWeekAge);
						NSBEggSells.setDayAge(dealAge);
						NSBEggSells.setModifyDate(nowDate);
						NSBEggSells.setModifyPerson(sbLayerFarmBreed.getModifyPerson());
						NSBEggSells.setModifyTime(nowDate);
						NSBEggSells.setCreateDate(nowDate);
						NSBEggSells.setCreatePerson(sbLayerFarmBreed.getCreatePerson());
						NSBEggSells.setCreateTime(nowDate);
						tSBEggSellsJSONArray.add(NSBEggSells);
					}
				}
				if (lSBBreedDetail != null && lSBBreedDetail.size() != 0) {
					tSBLayerBreedDetailService.insertBatch(lSBBreedDetail);
				}
			}
		}
		tSBEggSellsService.insertBatch(tSBEggSellsJSONArray);
		mLogger.info("新增入雏：" + sbLayerFarmBreed.getId());
		return layerHouseBreeds;
	}

	public List<SBLayerHouseBreed> dealUpdate(HashMap<String,Object> tMap){
		SBLayerFarmBreed sbLayerFarmBreed = (SBLayerFarmBreed) tMap.get("SBLayerFarmBreed");

		tSBLayerFarmBreedService.updateByPrimaryKey(sbLayerFarmBreed);

		List<SBLayerHouseBreed> layerHouseBreeds = (List<SBLayerHouseBreed>) tMap.get("SBLayerHouseBreedUp");
		if (layerHouseBreeds != null) {
			for (SBLayerHouseBreed layerHouseBreed : layerHouseBreeds) {
				tSBLayerHouseBreedService.updateByPrimaryKey(layerHouseBreed);
			}
		}

		List<SBLayerBreedDetail> sbLayerBreedDetails = (List<SBLayerBreedDetail>) tMap.get("SBLayerBreedDetailList");
		if (sbLayerBreedDetails != null){
			for (SBLayerBreedDetail sbLayerBreedDetail : sbLayerBreedDetails) {
				tSBLayerBreedDetailService.updateByPrimaryKey(sbLayerBreedDetail);
			}
		}

		List<SBLayerHouseBreed> sbLayerHouseBreedList = (List<SBLayerHouseBreed>) tMap.get("SBLayerHouseBreedInsert");
		if (sbLayerHouseBreedList != null) {
			for (int i = 0; i < sbLayerHouseBreedList.size(); ++i) {
				sbLayerHouseBreedList.get(i).setFarmBreedId(sbLayerFarmBreed.getId());
				tSBLayerHouseBreedService.insert(sbLayerHouseBreedList.get(i));
				int houseBreedId = sbLayerHouseBreedList.get(i).getId();

				List<SBLayerBreedDetail> sbLayerBreedDetailList = new ArrayList<>();
				Date placeDate = sbLayerHouseBreedList.get(i).getPlaceDate();
				// 当前日期、当前日龄以及当前周龄
				Date nowDate = new Date();
				int nowAge = 0;
				try {
					nowAge = PubFun.daysBetween(placeDate, nowDate) + sbLayerHouseBreedList.get(i).getPlaceDayAge();
				}catch (Exception e) {
					e.printStackTrace();
				}
				int nowWeekAge = PubFun.getWeekAge(tCalType, nowAge, nowDate);
				int m = 0;
				for (int j = 0; j < 800; ++j) {
					++m;
					Date tempDate = new Date();
					try {
						tempDate = PubFun.addDate(placeDate, j);
					} catch (Exception e) {
						e.printStackTrace();
					}
					int dealAge = sbLayerHouseBreedList.get(i).getPlaceDayAge() + j;
					int dealWeekAge = PubFun.getWeekAge(tCalType, dealAge, tempDate);
					Calendar cal = Calendar.getInstance();
					cal.setTime(tempDate);
					int dealDayOFWeek = cal.get(Calendar.DAY_OF_WEEK);
					// 1、当前周龄 大于 处理的周龄时，需要插入一条数据
					// 2、当前周龄 小于等于 处理的周龄时，需要每天插入一条数据
					if ((nowWeekAge > dealWeekAge && m >= 4 && dealDayOFWeek == 7) || nowWeekAge <= dealWeekAge) {
						SBLayerBreedDetail sbLayerBreedDetail = new SBLayerBreedDetail();
						sbLayerBreedDetail.setHouseBreedId(houseBreedId);
						sbLayerBreedDetail.setDayAge(dealAge);
						sbLayerBreedDetail.setWeekAge(dealWeekAge);
						sbLayerBreedDetail.setGrowthDate(tempDate);
						sbLayerBreedDetail.setIsHistory(nowWeekAge > dealWeekAge ? "Y" : "N");
						sbLayerBreedDetail.setDeathAm(0);
						sbLayerBreedDetail.setDeathPm(0);
						sbLayerBreedDetail.setCullingAm(0);
						sbLayerBreedDetail.setCullingPm(0);
						sbLayerBreedDetail.setCurCd(0);
						sbLayerBreedDetail.setAccCd(0);
						sbLayerBreedDetail.setCurFeed(new BigDecimal(0));
						sbLayerBreedDetail.setAccFeed(new BigDecimal(0));
						sbLayerBreedDetail.setCurWeight(new BigDecimal(0));
						sbLayerBreedDetail.setCurWater(new BigDecimal(0));
						sbLayerBreedDetail.setAccWater(new BigDecimal(0));
						sbLayerBreedDetail.setCurLayNum(0);
						sbLayerBreedDetail.setAccLayNum(0);
						sbLayerBreedDetail.setCurLayWeight(new BigDecimal(0));
						sbLayerBreedDetail.setAccLayWeight(new BigDecimal(0));
						sbLayerBreedDetail.setCurLayBroken(0);
						sbLayerBreedDetail.setAccLayBroken(0);
						sbLayerBreedDetail.setCurAmount(sbLayerHouseBreedList.get(i).getPlaceNum());
						sbLayerBreedDetail.setYtdAmount(sbLayerHouseBreedList.get(i).getPlaceNum());
						sbLayerBreedDetail.setNumBak1(new BigDecimal(0));
						sbLayerBreedDetail.setCreatePerson(sbLayerHouseBreedList.get(i).getCreatePerson());
						sbLayerBreedDetail.setCreateDate(new Date());
						sbLayerBreedDetail.setCreateTime(new Date());
						sbLayerBreedDetail.setModifyPerson(sbLayerHouseBreedList.get(i).getModifyPerson());
						sbLayerBreedDetail.setModifyDate(new Date());
						sbLayerBreedDetail.setModifyTime(new Date());
						sbLayerBreedDetailList.add(sbLayerBreedDetail);
					}
				}
				if (sbLayerBreedDetailList.size() != 0 && sbLayerBreedDetailList != null) {
					tSBLayerBreedDetailService.insertBatch(sbLayerBreedDetailList);
				}
			}
		}
		mLogger.info("更新入雏：" + sbLayerFarmBreed.getId());
		return sbLayerHouseBreedList;
	}

	public void lairageDeal(HashMap<String,Object> tMap) throws Exception{
		String flag = tMap.get("flag").toString();
		if ("house".equals(flag)) {
			SBLayerHouseBreed sbLayerHouseBreed = (SBLayerHouseBreed) tMap.get("SBLayerHouseBreed");
			SBLayerBreedDetail sbLayerBreedDetail = (SBLayerBreedDetail) tMap.get("SBLayerBreedDetail");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String SQL = "DELETE FROM s_b_layer_breed_detail WHERE house_breed_id = " + sbLayerBreedDetail.getHouseBreedId() + " AND growth_date > '" + sdf.format(sbLayerBreedDetail.getGrowthDate()) + "'";
			mBaseQueryService.deleteByAny(SQL);
			tSBLayerHouseBreedService.updateByPrimaryKey(sbLayerHouseBreed);
			tSBLayerBreedDetailService.updateByPrimaryKey(sbLayerBreedDetail);
			mLogger.info("出栏确认：" + sbLayerBreedDetail.getHouseBreedId());
		}else if ("farm".equals(flag)){
			SBLayerFarmBreed sbLayerFarmBreed = (SBLayerFarmBreed) tMap.get("SBLayerFarmBreed");
			tSBLayerFarmBreedService.updateByPrimaryKey(sbLayerFarmBreed);
			mLogger.info("批次结束确认：" + sbLayerFarmBreed.getId());
		}
	}
}
