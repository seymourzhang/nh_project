package com.mtc.app.controller;

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

@Controller
@RequestMapping("layer_report/")
public class LayerLayEggReqController {
	private static Logger mLogger =Logger.getLogger(LayerDataInputReqController.class);
	@Autowired
	private BaseQueryService tBaseQueryService;
	@Autowired
	private SDUserOperationService sSDUserOperationService;
	//产蛋率查询
	@RequestMapping("queryLayerCurve")
	public void queryLayerCurve(HttpServletResponse response,HttpServletRequest request){
		mLogger.info("=====Now start executing LayerLayEggReqController.queryLayerCurve");
		JSONObject resJson = new JSONObject();
		String dealRes = null ;
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("saveHouse.para=" + paraStr);
			JSONObject jsonObject = new JSONObject(paraStr);
			int userId = jsonObject.optInt("id_spa");
			sSDUserOperationService.insert(SDUserOperationService.MENU_DATAANALYSIS_EGG, SDUserOperationService.OPERATION_SELECT, userId);
			mLogger.info("===========产蛋率操作信息：查询，导入完毕");
			mLogger.info("jsonObject=" + jsonObject.toString());
			//** 业务处理开始，查询、增加、修改、或删除 **//
			JSONObject tHouseJson = jsonObject.getJSONObject("params");
			int FarmBreedId = tHouseJson.getInt("FarmBreedId");
			int FarmId = tHouseJson.getInt("FarmId");
			String ViewType = tHouseJson.getString("ViewType");
			//01周显示    02 日显示
			List<HashMap<String,Object>>  ff  =null;
			String SQL = null;
			if(ViewType.equals("02")){
                SQL = " SELECT day_age as agw , house_id ,s_f_getHouseName(house_id) AS HouseName , "
                        + " CASE WHEN is_history = 'Y'"
                        + "    THEN ROUND(((cur_lay_num / 7) / cur_amount) * 100, 2)"
                        + "    ELSE ROUND((cur_lay_num / cur_amount) * 100, 2) END AS layrate"
                        + " FROM s_b_layer_breed_detail AS bd LEFT JOIN s_b_layer_house_breed AS hb ON bd.`house_breed_id` = hb.`id` "
                        + " WHERE  hb.`farm_breed_id`=" + FarmBreedId + " AND hb.place_num <> 0"
                        + " AND  growth_date > DATE_SUB(CURDATE(),INTERVAL 60 DAY) AND  growth_date <= CURDATE() "
                        + " UNION ALL SELECT day_age as agw , IFNULL(null, '01')   AS  house_id, "
                        + " IFNULL(null,'全场平均') AS HouseName , "
                        + " CASE WHEN is_history = 'Y' "
                        + " 	THEN ROUND(((SUM(cur_lay_num) / 7) / SUM(cur_amount)) * 100, 2) "
                        + " 	ELSE ROUND((SUM(cur_lay_num) / SUM(cur_amount)) * 100, 2) END AS layrate"
                        + " FROM s_b_layer_breed_detail AS bd LEFT JOIN s_b_layer_house_breed AS hb ON bd.`house_breed_id` = hb.`id` "
                        + " WHERE  hb.`farm_breed_id`=" + FarmBreedId
                        + " AND  growth_date > DATE_SUB(CURDATE(),INTERVAL 60 DAY) AND  growth_date <= CURDATE() "
                        + " GROUP BY day_age";
            }else if(ViewType.equals("01")){
				 SQL =" SELECT week_age as agw , house_id ,  s_f_getHouseName(house_id) AS HouseName ,"
						+ " CASE WHEN is_history = 'Y' "
						+ " 	THEN ROUND(((cur_lay_num / 7) / cur_amount) * 100, 2) "
						+ " 	ELSE ROUND((cur_lay_num / cur_amount) * 100, 2) END AS layrate"
						+ " FROM s_b_layer_breed_detail AS bd LEFT JOIN s_b_layer_house_breed AS hb ON bd.`house_breed_id` = hb.`id` "
						+ " WHERE  hb.`farm_breed_id`="+FarmBreedId + " AND hb.place_num <> 0"
						+ " AND growth_date <= CURDATE()"
						+ " GROUP BY week_age , house_id  UNION ALL SELECT week_age as agw , IFNULL(NULL, '01') AS house_id,  "
						+ " IFNULL(NULL,'全场平均') AS HouseName ,"
					    + " CASE WHEN is_history = 'Y' "
						+ " 	THEN ROUND(((SUM(cur_lay_num) / 7) / SUM(cur_amount)) * 100, 2) "
						+ " 	ELSE ROUND((SUM(cur_lay_num) / SUM(cur_amount)) * 100, 2) END AS layrate"
						+ " FROM s_b_layer_breed_detail AS bd LEFT JOIN s_b_layer_house_breed AS hb ON bd.`house_breed_id` = hb.`id` "
						+ " WHERE  hb.`farm_breed_id`="+FarmBreedId
						+ " AND growth_date <= CURDATE() "
						+ " GROUP BY day_age  ORDER BY house_id , agw ";
			}
			
			 mLogger.info("=======LayerLayEggReqController.queryLayerCurve.SQL=" + SQL);
			 ff = tBaseQueryService.selectMapByAny(SQL);
		     if(ff.size()>0){
		    	JSONArray  LayerRate = new JSONArray();
		    	JSONArray  HouseDatas = new JSONArray();
		    	JSONObject tJSONObject = new JSONObject();
		    	JSONArray  xAxis = new JSONArray();
		    	int i=0;
		    	boolean xboolean =true;
		    	for (HashMap<String, Object> hashMap : ff) {
		    		Object agw = hashMap.get("agw");
		   		    Object layrate = hashMap.get("layrate");
		   		    if(layrate==null){
		   		    	layrate = 0;
		   		    }
		   		    Object house_id = hashMap.get("house_id");
		    		Object HouseName = hashMap.get("HouseName");
		    		xAxis.put(agw);
		    		HouseDatas.put(layrate);
		    		boolean outcome =false;
		    		try {
		    			 outcome = !house_id.equals(ff.get(i+1).get("house_id"));
					} catch (Exception e) {
						 outcome = true;
					}
		   		    if(outcome){
			   		     tJSONObject =new JSONObject();  
			   		     tJSONObject.put("house_id", house_id);
			   		     tJSONObject.put("HouseName", HouseName);
			   		     tJSONObject.put("HouseDatas", HouseDatas);
			   		     LayerRate.put(tJSONObject);
			   		     HouseDatas = new JSONArray();
		   		    }
		   		    if(outcome&&xboolean){
		    			 resJson.put("xAxis",xAxis);
		    			 xAxis = new JSONArray();
		    			 xboolean =false;
			   		  }
		   		    i++;
		    	}
		    	resJson.put("Result","Success");	
		    	resJson.put("FarmBreedId",FarmBreedId);	
		    	resJson.put("FarmId",FarmId);	
		    	resJson.put("ViewType",ViewType);	
		    	resJson.put("LayerRate",LayerRate);	
		    }else{
		    	resJson.put("Result","Fail");
		    }
			dealRes = Constants.RESULT_SUCCESS ;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resJson = new JSONObject();
				resJson.put("Exception", "数据异常");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			dealRes = Constants.RESULT_FAIL ;
		}
		DealSuccOrFail.dealApp(request,response,dealRes,resJson);
		mLogger.info("=====Now end executing LayerLayEggReqController.queryLayerCurve");
	}
	
	@RequestMapping("queryEggWeightCurve")
    public void queryEggWeightCurve(HttpServletResponse response,HttpServletRequest request){
		mLogger.info("=====Now start executing LayerLayEggReqController.queryEggWeightCurve");
		JSONObject resJson = new JSONObject();
		String dealRes = null ;
		try {
			String paraStr = PubFun.getRequestPara(request);
			mLogger.info("saveHouse.para=" + paraStr);
			JSONObject jsonObject = new JSONObject(paraStr);
			int userId = jsonObject.optInt("id_spa");
			sSDUserOperationService.insert(SDUserOperationService.MENU_DATAANALYSIS_EGGWEIGHT_CURVE, SDUserOperationService.OPERATION_SELECT, userId);
			mLogger.info("========蛋重操作信息：查询，导入完毕");
			mLogger.info("jsonObject=" + jsonObject.toString());
			//** 业务处理开始，查询、增加、修改、或删除 **//
			JSONObject tHouseJson = jsonObject.getJSONObject("params");
			int FarmBreedId = tHouseJson.getInt("FarmBreedId");
			int FarmId = tHouseJson.getInt("FarmId");
			String ViewType = tHouseJson.getString("ViewType");
			//01周显示    02 日显示
			List<HashMap<String,Object>>  ff  =null;
			String SQL = null;
			if(ViewType.equals("02")){
				 SQL =" SELECT day_age as agw , house_id ,s_f_getHouseName(house_id) AS HouseName , ROUND((cur_lay_weight/cur_lay_num)*1000 ,0) as layrate"
						+ " FROM s_b_layer_breed_detail AS bd LEFT JOIN s_b_layer_house_breed AS hb ON bd.`house_breed_id` = hb.`id` "
						+ " WHERE  hb.`farm_breed_id`="+FarmBreedId+" AND  growth_date > DATE_SUB(CURDATE(),INTERVAL 60 DAY) AND  growth_date <= CURDATE() "
						+ " UNION ALL SELECT day_age as agw , IFNULL(null, '01')   AS  house_id, "
						+ " IFNULL(null,'全场平均') AS HouseName , "
						+ " ROUND((SUM(cur_lay_weight)/SUM(cur_lay_num))*1000, 0) as layrate "
						+ " FROM s_b_layer_breed_detail AS bd LEFT JOIN s_b_layer_house_breed AS hb ON bd.`house_breed_id` = hb.`id` "
						+ " WHERE  hb.`farm_breed_id`="+FarmBreedId+" AND  growth_date > DATE_SUB(CURDATE(),INTERVAL 60 DAY) AND  growth_date <= CURDATE() GROUP BY growth_date";
			}else if(ViewType.equals("01")){
				 SQL =" SELECT week_age as agw , house_id ,  s_f_getHouseName(house_id) AS HouseName ,"
						+ " ROUND((SUM(cur_lay_weight)/SUM(cur_lay_num))*1000, 0)AS layrate"
						+ " FROM s_b_layer_breed_detail AS bd LEFT JOIN s_b_layer_house_breed AS hb ON bd.`house_breed_id` = hb.`id` "
						+ " WHERE  hb.`farm_breed_id`="+FarmBreedId+" AND   growth_date <= CURDATE()"
						+ " GROUP BY week_age , house_id  UNION ALL SELECT week_age as agw , IFNULL(NULL, '01') AS house_id,  "
						+ " IFNULL(NULL,'全场平均') AS HouseName ,ROUND((SUM(cur_lay_weight)/SUM(cur_lay_num))*1000, 0) AS layrate  "
						+ " FROM s_b_layer_breed_detail AS bd LEFT JOIN s_b_layer_house_breed AS hb ON bd.`house_breed_id` = hb.`id` "
						+ " WHERE  hb.`farm_breed_id`="+FarmBreedId+"  AND   growth_date <= CURDATE() GROUP BY week_age  ORDER BY house_id , agw ";
			}
			 mLogger.info("==========LayerLayEggReqController.queryEggWeightCurve.SQL=" + SQL);
			 ff = tBaseQueryService.selectMapByAny(SQL);
		     if(ff.size()>0){
		    	JSONArray  EggAvgWeight = new JSONArray();
		    	JSONArray  HouseDatas = new JSONArray();
		    	JSONObject tJSONObject = new JSONObject();
		    	JSONArray  xAxis = new JSONArray();
		    	int i=0;
		    	boolean xboolean =true;
		    	for (HashMap<String, Object> hashMap : ff) {
		    		Object agw = hashMap.get("agw");
		   		    Object layrate = hashMap.get("layrate");
		   		    if(layrate==null){
		   		    	layrate = 0;
		   		    }
		   		    Object house_id = hashMap.get("house_id");
		    		Object HouseName = hashMap.get("HouseName");
		    		xAxis.put(agw);
		    		HouseDatas.put(layrate);
		    		boolean outcome =false;
		    		try {
		    			 outcome = !house_id.equals(ff.get(i+1).get("house_id"));
					} catch (Exception e) {
						 outcome = true;
					}
		   		    if(outcome){
			   		     tJSONObject =new JSONObject();  
			   		     tJSONObject.put("house_id", house_id);
			   		     tJSONObject.put("HouseName", HouseName);
			   		     tJSONObject.put("HouseDatas", HouseDatas);
			   		     EggAvgWeight.put(tJSONObject);
			   		     HouseDatas = new JSONArray();
		   		    }
		   		    if(outcome&&xboolean){
		    			 resJson.put("xAxis",xAxis);
		    			 xAxis = new JSONArray();
		    			 xboolean =false;
			   		  }
		   		    i++;
		    	}
		    	resJson.put("Result","Success");	
		    	resJson.put("FarmBreedId",FarmBreedId);	
		    	resJson.put("FarmId",FarmId);	
		    	resJson.put("ViewType",ViewType);	
		    	resJson.put("EggAvgWeight",EggAvgWeight);	
		    }else{
		    	resJson.put("Result","Fail");
		    }
			dealRes = Constants.RESULT_SUCCESS ;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resJson = new JSONObject();
				resJson.put("Exception", "数据异常");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			dealRes = Constants.RESULT_FAIL ;
		}
		DealSuccOrFail.dealApp(request,response,dealRes,resJson);
		mLogger.info("=====Now end executing LayerLayEggReqController.queryEggWeightCurve");
	}
}
