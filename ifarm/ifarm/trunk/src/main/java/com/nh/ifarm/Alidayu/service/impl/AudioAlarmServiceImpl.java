/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.nh.ifarm.Alidayu.service.impl;


import com.nh.ifarm.Alidayu.entity.SBAlarmInfoTemp;
import com.nh.ifarm.Alidayu.entity.SBCallAlarm;
import com.nh.ifarm.Alidayu.entity.SBCallDetail;
import com.nh.ifarm.Alidayu.entity.SBCallMain;
import com.nh.ifarm.Alidayu.entity.mapper.SBCallAlarmMapperCustom;
import com.nh.ifarm.Alidayu.entity.mapper.SBCallDetailMapperCustom;
import com.nh.ifarm.Alidayu.entity.mapper.SBCallMainMapper;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *
 * 根据语音配置表，获取报警信息
 *
 * @author GX
 *
 */
@Service
public class AudioAlarmServiceImpl{

	private static Logger mLogger =Logger.getLogger(AudioAlarmServiceImpl.class);

	@Autowired
	private BaseQueryService baseQueryService;

	@Autowired
	private SBCallMainMapper tSBCallMainMapper;
	@Autowired
	private SBCallAlarmMapperCustom alarmMapperCustom;
	@Autowired
	private SBCallDetailMapperCustom detailMapperCustom;

	private String tempId = getPropertyValue("tempId");
	private String sysCode = getPropertyValue("sysCode");
	private String compareInt = getPropertyValue("compareInt");
	private String noResponseTime = getPropertyValue("noResponseTime");

	private ResourceBundle conf = null;
	public  String getPropertyValue(String keyName){
		if(conf == null){
			conf= ResourceBundle.getBundle("pro/alidayu");
		}
		String value= conf.getString(keyName);
		return value;
	}


	public void service(){
		try{
			mLogger.info("Task-AudioAlarmServiceImpl start");

			// 查询需要报警的记录
			List<HashMap<String,Object>> curAlarmList = getAlarmInfo();
			if(curAlarmList == null || curAlarmList.size() == 0){
				mLogger.info("暂无需要语音通知的报警！");
				return;
			}

			// 保存每一个栋舍的报警信息  key:farmId_houseId
			HashMap<String, List<SBAlarmInfoTemp>> callMainInfoMap = tidyAlarmInfo(curAlarmList);

			Iterator iterator = callMainInfoMap.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry entry = (Map.Entry) iterator.next();
				List<SBAlarmInfoTemp> singleHouseAlarmList = (List<SBAlarmInfoTemp>)entry.getValue();

				int farmId = singleHouseAlarmList.get(0).getFarmId() ;
				int houseId = singleHouseAlarmList.get(0).getHouseId() ;
				String farmName = singleHouseAlarmList.get(0).getFarmName() ;
				String houseName = singleHouseAlarmList.get(0).getHouseName() ;

				// 生成 SBCallMain
				SBCallMain tSBCallMain = createCallMain(farmId + "-" + farmName + "，" + houseId + "-" + houseName);
				// 生成 SBCallAlarm
				List<SBCallAlarm> tSBCallAlarmList =  createCallAlarmInfo(singleHouseAlarmList);
				// 生成 参数 Json
				JSONObject tempPara = createParamJson(farmName, houseName, tSBCallAlarmList);
				// 生成 SBCallDetail
				List<SBCallDetail> tSBCallDetailList = createCallDetailInfo(farmId, houseId,tempPara);

				int mainId = saveCallMainAndOthers(tSBCallMain, tSBCallAlarmList, tSBCallDetailList);

				mLogger.info("生产新的报警，new main id :" + mainId);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		mLogger.info("Task-AudioAlarmServiceImpl end");
	}

	/**
	 *  报警参数值生成
	 */
	private JSONObject createParamJson(String farmName,String houseName,List<SBCallAlarm> callAlarms){
		JSONObject paraJson = new JSONObject();

		String alarmStr = "";
		for(int i = 0 ; i < callAlarms.size(); i++){
			alarmStr += callAlarms.get(i).getAlarmName() + ",";
		}
		if(alarmStr.length() > 0 )
		{
			if(alarmStr.length() <= 30){
				alarmStr = alarmStr.substring(0, alarmStr.length() - 1);
			}else{
				alarmStr = alarmStr.substring(0, 30);
			}
		}

		paraJson.put("name", ""); // 后续会将用户姓名放入此位置
		paraJson.put("farmName", farmName);
		paraJson.put("exceptions", alarmStr);
		paraJson.put("houseName", houseName);

		return paraJson;
	}

	/**
	 *   生成 SBCallMain
	 */
	private SBCallMain createCallMain(String callRemark){
		SBCallMain callMain = new SBCallMain();
		callMain.setSysCode(sysCode);
		callMain.setCallRemark(callRemark);
		callMain.setTemplateCode(tempId);
		callMain.setMaxTime(6);
		callMain.setCallStatus("01");
		callMain.setCreateTime(new Date());
		callMain.setModifyTime(new Date());
		return callMain;
	}

	/**
	 *   根据报警信息生成 SBCallAlarm
	 */
	private List<SBCallAlarm> createCallAlarmInfo(List<SBAlarmInfoTemp> tSBAlarmInfoTempList){
		List<SBCallAlarm> tSBCallAlarmList = new ArrayList<SBCallAlarm>();
		for(SBAlarmInfoTemp temp : tSBAlarmInfoTempList){
			SBCallAlarm callAlarm = new SBCallAlarm();
			callAlarm.setAlarmId(temp.getAlarmId());
			callAlarm.setAlarmCode(temp.getAlarmCode());
			callAlarm.setAlarmName(temp.getAlarmName());
			callAlarm.setAlarmStatus("01");
			callAlarm.setCreateTime(new Date());
			callAlarm.setModifyTime(new Date());
			tSBCallAlarmList.add(callAlarm);
		}
		return tSBCallAlarmList;
	}

	/**
	 *   根据报警人员生成 SBCallDetail
	 */
	private List<SBCallDetail> createCallDetailInfo(int farmId, int houseId, JSONObject tempParaJson){
		List<SBCallDetail> tSBCallDetailList = new ArrayList<>();
		/**查询语音通知人员*/
		String sql1 = " SELECT c.id as user_id,c.user_code,c.user_mobile_1,c.user_real_name,b.user_order from s_b_remind_setting a,s_b_reminder b "+
									" LEFT JOIN s_d_user c on b.user_id = c.id"+
									" where a.farm_id = b.farm_id and a.farm_id = " + farmId + " and b.house_id = " + houseId + " ORDER BY user_order ";

		mLogger.info("query AlarmerSQL:" + sql1);
		List<HashMap<String,Object>> alarmerList = baseQueryService.selectMapByAny(sql1);

		if(alarmerList != null && !alarmerList.isEmpty()){
			for(HashMap<String,Object> map : alarmerList){
				int userId = (int) map.get("user_id");
				String phoneNum = map.get("user_mobile_1").toString();
				String userName = map.get("user_real_name").toString();

				tempParaJson.put("name", userName);

				SBCallDetail detail = new SBCallDetail();
				detail.setCallOrder((int)map.get("user_order"));
				detail.setTemplateJson(tempParaJson.toString());
				detail.setCallUser(userId + "-" + userName);
				detail.setCallNum(phoneNum);
				detail.setCallStatus("01");
				detail.setCreateTime(new Date());
				detail.setModifyTime(new Date());
				tSBCallDetailList.add(detail);

				SBCallDetail detail2 = new SBCallDetail();
				detail2.setCallOrder((int)map.get("user_order") + 3);
				detail2.setTemplateJson(tempParaJson.toString());
				detail2.setCallUser(userId + "-" + userName);
				detail2.setCallNum(phoneNum);
				detail2.setCallStatus("01");
				detail2.setCreateTime(new Date());
				detail2.setModifyTime(new Date());
				tSBCallDetailList.add(detail2);
			}

		}
		return tSBCallDetailList;
	}

	/**
	 * 根据业务逻辑，查询需要语音提醒的报警信息
	 * @return
	 */
	private List<HashMap<String,Object>> getAlarmInfo(){
		// 查询需要报警的记录
		String queryAlarmSql = " SELECT sdf.farm_name_chs,sdh.house_name,i.id as alarm_id,i.alarm_name,i.alarm_code,ifnull(i.farm_id,0) as farm_id,ifnull(i.house_id,0) as house_id " +
				" from s_b_alarm_inco i" +
				" LEFT JOIN s_d_farm sdf on i.farm_id = sdf.id" +
				" LEFT JOIN s_d_house sdh on i.house_id = sdh.id" +
				" where 1=1" +
				" and exists( select 1 from s_b_remind_setting a,s_b_remind_switch b,s_b_remind_alarmcode c where 1=1" +
							" and a.remind_method = b.remind_method and a.farm_id = b.farm_id and b.remind_method = c.remind_method and b.farm_id = c.farm_id" +
							" and (CASE WHEN a.switch_rele_house = 'Y' THEN b.house_id = i.house_id AND b.house_Id > 0 ELSE b.house_Id = 0 END)" +
							" and (CASE WHEN a.alarm_rele_house = 'Y' THEN c.house_id = i.house_Id AND c.house_Id > 0 ELSE c.house_Id = 0 END)" +
							" and c.alarm_code = i.alarm_code" +
							" and a.farm_id = i.farm_id" +
							" and b.status = 'Y' )" +
				//  还没有进行过语音提醒
				" and not exists(select 1 from s_b_call_alarm sbca where sbca.alarm_id = i.id)" +
				//  相同报警如果30分钟之内已经拨打过，则不再拨打
				"and not exists(select 1 from s_b_call_main m1,s_b_call_alarm m2 where m2.main_code = concat(m1.sys_code,'-',m1.id) and m1.call_status = '03' and m2.alarm_code = i.alarm_code and m1.result_time > DATE_SUB(NOW(),INTERVAL 30 MINUTE)) " +
				//  未处理过的报警
				" and i.deal_status = '01'" +
				//  超过5分钟
				" and i.alarm_time < DATE_SUB(NOW(),INTERVAL " + noResponseTime + " MINUTE)" +
				//  当天的报警
				" and i.alarm_time > curdate()" +
				//  除断电之外的报警，均需要 超过界限 compareInt 以上
				" and (case when i.alarm_code = '2' then 1=1 else i.actual_value - i.set_value >= " + compareInt + " OR i.set_value - i.actual_value >= " + compareInt + " end) " ;

		mLogger.info("queryAlarmSql:" + queryAlarmSql);
		// 查询到的报警信息
		List<HashMap<String,Object>> alarms = baseQueryService.selectMapByAny(queryAlarmSql);

		return alarms;
	}

	/**
	 *
	 * 将查询出的报警信息进行整理
	 *
	 * 按照农场栋舍整理，并且整理出农场的报警设置
	 *
	 * @param alarms
	 */
	private HashMap<String, List<SBAlarmInfoTemp>> tidyAlarmInfo(List<HashMap<String,Object>> alarms ){
		HashMap<String, List<SBAlarmInfoTemp>> alarmInfoTemps = new HashMap<>();

		for(HashMap<String,Object> map : alarms){

			SBAlarmInfoTemp temp = new SBAlarmInfoTemp();
			temp.setAlarmCode(map.get("alarm_code").toString());
			temp.setAlarmName(map.get("alarm_name").toString());
			temp.setAlarmId(Integer.parseInt(map.get("alarm_id").toString()));
			temp.setFarmId(Integer.parseInt(map.get("farm_id").toString()));
			temp.setFarmName(map.get("farm_name_chs").toString());
			temp.setHouseId(Integer.parseInt(map.get("house_id").toString()));
			temp.setHouseName(map.get("house_name").toString());

			String key = Integer.parseInt(map.get("farm_id").toString()) + "_" + Integer.parseInt(map.get("house_id").toString());
			List<SBAlarmInfoTemp> temps = alarmInfoTemps.get(key);
			if(temps == null){
				temps = new ArrayList<>();
				temps.add(temp);
				alarmInfoTemps.put(key, temps);
			}else{
				temps.add(temp);
			}

		}
		return alarmInfoTemps;
	}

	/**
	 *  保存所有信息
	 * @return
	 */
	public int saveCallMainAndOthers(SBCallMain main, List<SBCallAlarm> callAlarms, List<SBCallDetail> details){

		tSBCallMainMapper.insert(main);

		for(SBCallAlarm alarm : callAlarms){
			alarm.setMainCode(main.getSysCode() + "-" + main.getId());
		}
		if(!callAlarms.isEmpty()){
			alarmMapperCustom.insertCallAlarmBatch(callAlarms);
		}

		for(SBCallDetail alarm : details){
			alarm.setMainCode(main.getSysCode() + "-" + main.getId());
		}
		if(!details.isEmpty()){
			detailMapperCustom.insertCallDetails(details);
		}
		return main.getId();
	}
}
