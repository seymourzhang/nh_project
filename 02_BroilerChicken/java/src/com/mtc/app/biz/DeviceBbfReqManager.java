/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.mtc.app.biz;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mtc.app.service.BaseQueryService;
import com.mtc.app.service.SBBbfarSercice;
import com.mtc.app.service.SBMonitorCurrSercice;
import com.mtc.app.service.SBMonitorHistSercice;
import com.mtc.app.service.SBRotemNetDataService;
import com.mtc.common.util.PubFun;
import com.mtc.entity.app.SBBbfar;
import com.mtc.entity.app.SBMonitorCurr;
import com.mtc.entity.app.SBMonitorHist;
import com.mtc.entity.app.SBRotemNetData;

/**
 * @ClassName: DeviceBbfReqController
 * @Description: 
 * @Date 2016-1-5 上午11:43:11
 * @Author Shao Yao Yu
 * 
 */
@Component
public class DeviceBbfReqManager {

	private static Logger mLogger = Logger.getLogger(DeviceBbfReqManager.class);
	
	@Autowired
	private   SBBbfarSercice tSBBbfarSercice;
	@Autowired
	private   SBRotemNetDataService tSBRotemNetDataService;
	@Autowired
	private   SBMonitorHistSercice tSBMonitorHistSercice;
	@Autowired
	private   SBMonitorCurrSercice tSBMonitorCurrSercice;
	@Autowired
	private BaseQueryService mBaseQueryService;
	
	public int saveSbbbfar(HashMap<String, Object> mParas) {
		SBBbfar tSBBbfar = (SBBbfar) mParas.get("SBBbfar");
		return tSBBbfarSercice.insertSbbbfar(tSBBbfar);
	}
	
	public int saveSBRotemNetData(HashMap<String, Object> mParas) {
		SBRotemNetData tSBRotemNetData = (SBRotemNetData) mParas.get("SBRotemNetData");
		return tSBRotemNetDataService.insertRotemNetData(tSBRotemNetData);
	}
	
	public void saveMonitorCur(HashMap<String, Object> mParas) {
		SBMonitorCurr tSBMonitorCurr = (SBMonitorCurr) mParas.get("SBMonitorCurr");
		SBMonitorHist tSBMonitorHist = (SBMonitorHist) mParas.get("SBMonitorHist");
		int HouseId = tSBMonitorCurr.getHouseId();
	    tSBMonitorCurrSercice.deleteByHouId(HouseId);
		tSBMonitorCurrSercice.insertSBMonitorCurr(tSBMonitorCurr);
		tSBMonitorHist.setId(tSBMonitorCurr.getId());
		tSBMonitorHistSercice.insertSBMonitorHist(tSBMonitorHist);
	}
	
	public void dealBBFarData(SBBbfar tSBBbfar) {
		if(tSBBbfar == null){
			return ;
		}
		String houseDeviceRela = tSBBbfar.getKeyid();
		//  Bak2 存 SIM卡 iccid
		if(!PubFun.isNull(tSBBbfar.getBak2())){
			houseDeviceRela = tSBBbfar.getBak2();
		}else{
			//  Bak1 存设备的 IMEI号
			if(!PubFun.isNull(tSBBbfar.getBak1())){
				houseDeviceRela = tSBBbfar.getBak1();
			}
		}
		
		String SQL1 = "SELECT c.house_id ,c.farm_id, s_f_getDayAgeByHouseId(c.house_id,CURDATE()) AS age,"
						+ "iFNULL(b.set_temp,(SELECT z.set_temp FROM s_b_dayage_temp_sub z where a.id = z.uid_num order by z.record_datetime desc LIMIT 1)) as set_temp,"
						+ "ifnull(b.high_alarm_temp,(SELECT z.high_alarm_temp FROM s_b_dayage_temp_sub z where a.id = z.uid_num order by z.record_datetime desc LIMIT 1)) as high_alarm_temp,"
						+ "ifnull(b.low_alarm_temp,(SELECT z.low_alarm_temp FROM s_b_dayage_temp_sub z where a.id = z.uid_num order by z.record_datetime desc LIMIT 1)) as low_alarm_temp,"
						+ "ifnull(b.set_humidity,(SELECT z.set_humidity FROM s_b_dayage_temp_sub z where a.id = z.uid_num order by z.record_datetime desc LIMIT 1)) as set_humidity,"
						+ "d.temp_cpsation,d.temp_cordon,"
						+ "(select group_concat(DISTINCT e.probe_code) from s_b_house_probe e where e.house_id = c.house_id ) as probe_codes "
						+ "FROM s_b_devi_house c "
						+ "LEFT JOIN s_b_house_alarm d on d.house_id = c.house_id "
						+ "LEFT JOIN s_b_dayage_temp a ON a.house_id = c.house_id AND a.feed_batch = s_f_getFarmBreedId(c.farm_id) "
						+ "LEFT JOIN s_b_dayage_temp_sub b ON a.id = b.uid_num  AND DATE(b.record_datetime) = CURDATE() AND HOUR(b.record_datetime) = HOUR(NOW()) "
						+ "WHERE  c.device_code = '" + houseDeviceRela + "' ";
		mLogger.info("DeviceReqController.dataUploadBBF.sql"+SQL1);
		List<HashMap<String, Object>> listMap1 = mBaseQueryService
				.selectMapByAny(SQL1);
		if (listMap1.size() > 0) {
			Object houseId = listMap1.get(0).get("house_id");
			Object farmId = listMap1.get(0).get("farm_id");
			Object age = listMap1.get(0).get("age");
			Object set_temp = listMap1.get(0).get("set_temp");
			Object high_alarm_temp = listMap1.get(0).get("high_alarm_temp");
			Object low_alarm_temp = listMap1.get(0).get("low_alarm_temp");
			Object set_humidity = listMap1.get(0).get("set_humidity");
			Object probe_codes = listMap1.get(0).get("probe_codes");
			String temp_cpsation = null;
			if(listMap1.get(0).get("temp_cpsation") != null){
				temp_cpsation = listMap1.get(0).get("temp_cpsation").toString();
			}else{
				temp_cpsation = "0";
			}
			
			BigDecimal temp_cordon = new BigDecimal(0);
			if(temp_cpsation.equals("1")){
				temp_cordon = PubFun.getBigDecimalData(listMap1.get(0).get("temp_cordon").toString());
			}
			
			int HouseId = (int) houseId;				
			BigDecimal T1 = PubFun.getBigDecimalData(tSBBbfar.getT1());
			BigDecimal T2 = PubFun.getBigDecimalData(tSBBbfar.getT2());
			BigDecimal T3 = PubFun.getBigDecimalData(tSBBbfar.getT3());
			BigDecimal T4 = PubFun.getBigDecimalData(tSBBbfar.getT4());
			BigDecimal T5 = PubFun.getBigDecimalData(tSBBbfar.getT5());
			BigDecimal T6 = PubFun.getBigDecimalData(tSBBbfar.getT6());
			
			SBMonitorCurr tSBMonitorCurr = new SBMonitorCurr();
			tSBMonitorCurr.setHouseId(HouseId);
			tSBMonitorCurr.setFarmId((int)farmId);
			tSBMonitorCurr.setCollectDatetime(tSBBbfar.getDateTime());
			tSBMonitorCurr.setDealStatus(0);
			tSBMonitorCurr.setDateAge(age==null?0:(int)age);
			
			tSBMonitorCurr.setInsideTemp1(T1);
			tSBMonitorCurr.setInsideTemp2(T2);
			tSBMonitorCurr.setInsideTemp3(T3);
			tSBMonitorCurr.setInsideTemp5(T4);
			tSBMonitorCurr.setInsideTemp6(T5);
			tSBMonitorCurr.setOutsideTemp(T6);
			
			BigDecimal insideAvgTemp = null;
			BigDecimal pointTempDiff = new BigDecimal(0);
			if(probe_codes != null && !probe_codes.equals("")){
				String[] probe_cd = probe_codes.toString().split(",");
				BigDecimal insideAvgTemp1 = new BigDecimal(0);
				BigDecimal Tmin = new BigDecimal(999);
				BigDecimal Tmax = new BigDecimal(0);
				int n = 0;
				for(int m = 0;m<probe_cd.length;m++){
					BigDecimal tempBigDecimal = null;
					if(probe_cd[m].equals("tempLeft1")){
						tempBigDecimal = tSBMonitorCurr.getInsideTemp1();
					}else if(probe_cd[m].equals("tempLeft2")){
						tempBigDecimal = tSBMonitorCurr.getInsideTemp2();
					}else if(probe_cd[m].equals("tempMiddle1")){
						tempBigDecimal = tSBMonitorCurr.getInsideTemp3();
					}else if(probe_cd[m].equals("tempMiddle2")){
						tempBigDecimal = tSBMonitorCurr.getInsideTemp4();
					}else if(probe_cd[m].equals("tempRight1")){
						tempBigDecimal = tSBMonitorCurr.getInsideTemp5();
					}else if(probe_cd[m].equals("tempRight2")){
						tempBigDecimal = tSBMonitorCurr.getInsideTemp6();
					}
					if(tempBigDecimal != null){
						n ++ ;
						insideAvgTemp1 = insideAvgTemp1.add(tempBigDecimal);
						Tmin = Tmin.min(tempBigDecimal);
						Tmax = Tmax.max(tempBigDecimal);
					}
				}
				mLogger.info("tempPlus="+insideAvgTemp1.toString()+" probe_count=" +n + " probe_max=" + Tmax + " probe_min=" + Tmin);
				if(n > 0){
					insideAvgTemp = insideAvgTemp1.divide(new BigDecimal(n),2);
				}
				pointTempDiff = Tmax.subtract(Tmin);
			}
			tSBMonitorCurr.setPointTempDiff(pointTempDiff);
			tSBMonitorCurr.setInsideAvgTemp(insideAvgTemp);
			if (set_temp != null) {
				tSBMonitorCurr.setInsideSetTemp(PubFun.getBigDecimalData(set_temp.toString()));
			}
			if (high_alarm_temp != null) {
				if(temp_cpsation.equals("1") && tSBMonitorCurr.getOutsideTemp() != null){
					temp_cordon = temp_cordon.add(tSBMonitorCurr.getOutsideTemp()) ;
					tSBMonitorCurr.setHighAlarmTemp(temp_cordon.max(PubFun.getBigDecimalData(high_alarm_temp.toString())));
				}else{
					tSBMonitorCurr.setHighAlarmTemp(PubFun.getBigDecimalData(high_alarm_temp.toString()));
				}
			}
			if (low_alarm_temp != null) {
				tSBMonitorCurr.setLowAlarmTemp(PubFun.getBigDecimalData(low_alarm_temp.toString()));
			}
			if (set_humidity != null) {
				tSBMonitorCurr.setTargetHumidity(PubFun.getBigDecimalData(set_humidity.toString()));
			}
			tSBMonitorCurr.setInsideHumidity(PubFun.getBigDecimalData(tSBBbfar.getH1()));
			tSBMonitorCurr.setPowerStatus(Integer.parseInt(tSBBbfar.getP()));
			tSBMonitorCurr.setUpdateDatetime(tSBBbfar.getDateTime());
			
			SBMonitorHist tSBMonitorHist = new SBMonitorHist();
			tSBMonitorHist.setHouseId(tSBMonitorCurr.getHouseId());
			tSBMonitorHist.setFarmId(tSBMonitorCurr.getFarmId());
			tSBMonitorHist.setCollectDatetime(tSBBbfar.getDateTime());
			tSBMonitorHist.setDealStatus(0);
			tSBMonitorHist.setDateAge(tSBMonitorCurr.getDateAge());
			tSBMonitorHist.setInsideTemp1(tSBMonitorCurr.getInsideTemp1());
			tSBMonitorHist.setInsideTemp2(tSBMonitorCurr.getInsideTemp2());
			tSBMonitorHist.setInsideTemp3(tSBMonitorCurr.getInsideTemp3());
			tSBMonitorHist.setInsideTemp4(tSBMonitorCurr.getInsideTemp4());	
			tSBMonitorHist.setInsideTemp5(tSBMonitorCurr.getInsideTemp5());
			tSBMonitorHist.setInsideTemp6(tSBMonitorCurr.getInsideTemp6());
			tSBMonitorHist.setOutsideTemp(tSBMonitorCurr.getOutsideTemp());
			tSBMonitorHist.setInsideAvgTemp(tSBMonitorCurr.getInsideAvgTemp());
			tSBMonitorHist.setUpdateDatetime(tSBMonitorCurr.getUpdateDatetime());
			tSBMonitorHist.setInsideSetTemp(tSBMonitorCurr.getInsideSetTemp());
			tSBMonitorHist.setHighAlarmTemp(tSBMonitorCurr.getHighAlarmTemp());
			tSBMonitorHist.setLowAlarmTemp(tSBMonitorCurr.getLowAlarmTemp());
			tSBMonitorHist.setTargetHumidity(tSBMonitorCurr.getTargetHumidity());
			tSBMonitorHist.setInsideHumidity(tSBMonitorCurr.getInsideHumidity());
			tSBMonitorHist.setPointTempDiff(tSBMonitorCurr.getPointTempDiff());
			tSBMonitorHist.setPowerStatus(tSBMonitorCurr.getPowerStatus());
			
			HashMap<String, Object> mParas2 = new HashMap<String, Object>();
			mParas2.put("SBMonitorCurr", tSBMonitorCurr);
			mParas2.put("SBMonitorHist", tSBMonitorHist);
			saveMonitorCur(mParas2);
			
		}else{
			mLogger.info("该设备(" + tSBBbfar.getKeyid() + ")没有取到相关的栋舍、日龄、目标温度等数据。");
		}
	}
}
