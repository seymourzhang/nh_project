package com.mtc.app.controller;

import com.mtc.app.service.BaseQueryService;
import com.mtc.common.util.DealSuccOrFail;
import com.mtc.common.util.PubFun;
import com.mtc.common.util.constants.Constants;
import org.apache.log4j.Logger;
import org.json.JSONArray;
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
 * Created by Seymour on 2017/5/26.
 */
@Controller
@RequestMapping("/rep")
public class RepWaterFeedReqController {

    private static Logger mLogger = Logger.getLogger(RepWaterFeedReqController.class);

    @Autowired
    private BaseQueryService mBaseQueryService;

    @RequestMapping("/queryWaterFeed")
    public void queryWaterFeed(HttpServletRequest request, HttpServletResponse response) {
        mLogger.info("=======Now start executing RepWaterFeedReqController.queryWaterFeed");
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        try {
            String paraStr = PubFun.getRequestPara(request);
            mLogger.info("updateFarm.para=" + paraStr);
            JSONObject jsonobject = new JSONObject(paraStr);
            int userId = jsonobject.optInt("id_spa");
            mLogger.info("jsonObject=" + jsonobject.toString());
            String tErrorContent = "Null";
            /** 业务处理开始，查询、增加、修改、或删除 **/
            JSONObject params = jsonobject.getJSONObject("params");
            int FarmBreedId = params.optInt("FarmBreedId");
            int HouseId = params.optInt("HouseId");
            String CompareType = params.optString("CompareType");
            String SQL = "";
            if ("01".equals(CompareType)) {
                SQL = "SELECT " +
                        "  age                                                 AS x_axis, " +
                        "  house_id, " +
                        "  s_f_getHouseName(house_id)                          AS HouseName, " +
                        "  ifnull(ROUND((SUM(bd.num_bak1) / SUM(cur_feed)), 1), 0) AS waterFeed, " +
                        "  hb.farm_id " +
                        "FROM s_b_breed_detail AS bd LEFT JOIN s_b_house_breed AS hb ON bd.`house_breed_id` = hb.`id` " +
                        "WHERE hb.`farm_breed_id` = " + FarmBreedId +
                        "      AND growth_date <= CURDATE() " +
                        "GROUP BY house_id, x_axis";
            } else if ("02".equals(CompareType)){
                SQL = "SELECT  " +
                        "  age                                                 AS x_axis,  " +
                        "  house_id,  " +
                        "  s_f_getHouseName(house_id)                          AS HouseName,  " +
                        "  ifnull(ROUND((SUM(bd.num_bak1) / SUM(cur_feed)), 1), 0) AS waterFeed,  " +
                        "  hb.farm_breed_id,  " +
                        "  fb.batch_code, " +
                        "  hb.farm_id " +
                        "FROM s_b_breed_detail AS bd " +
                        "LEFT JOIN s_b_house_breed AS hb ON bd.`house_breed_id` = hb.`id`  " +
                        "LEFT JOIN s_b_farm_breed AS fb ON fb.id = hb.farm_breed_id " +
                        "WHERE hb.`house_id` = " + HouseId +
                        "      AND growth_date <= CURDATE()  " +
                        "GROUP BY farm_breed_id, x_axis";
            }
            mLogger.info("=========RepWaterFeedReqController.queryWaterFeed.SQL: "+SQL);

            List<HashMap<String, Object>> listMap = mBaseQueryService.selectMapByAny(SQL);
            if (listMap.size() > 0){
                List xAxis = new ArrayList();
                int lastHouseID = 0, lastBreedID = 0, dealCount = 0, farmId = 0;
                JSONObject tJSONObject = new JSONObject();
                JSONArray waterFeedArray = new JSONArray();
                JSONArray TempDatas = new JSONArray();
                for (HashMap<String, Object> map : listMap) {
                    dealCount ++;
                    if ("01".equals(CompareType)){
                        int houseId = (Integer) map.get("house_id");
                        farmId = (Integer) map.get("farm_id");
                        Object x_axis = map.get("x_axis");
                        if (x_axis == null) {
                            tErrorContent = "批次数据错误。";
                            break;
                        }
                        if (!xAxis.contains(x_axis)) {
                            xAxis.add(x_axis);
                        }

                        String HouseName = map.get("HouseName").toString();
                        if (lastHouseID != houseId) {
                            if (waterFeedArray.length() != 0) {
                                tJSONObject.put("HouseDatas", waterFeedArray);
                                TempDatas.put(tJSONObject);

                                tJSONObject = new JSONObject();
                                waterFeedArray = new JSONArray();
                            }
                            tJSONObject.put("housename", HouseName + "栋");
                            tJSONObject.put("HouseId", houseId);
                        }
                        waterFeedArray.put(map.get("waterFeed").toString());
                        if (dealCount == listMap.size() && waterFeedArray.length() != 0){
                            tJSONObject.put("HouseDatas", waterFeedArray);
                            TempDatas.put(tJSONObject);
                        }
                        lastHouseID = houseId;
                        resJson.put("FarmId", farmId);
                        resJson.put("FarmBreedId", FarmBreedId);
                        resJson.put("Result", "Success");
                        resJson.put("xAxis", xAxis);
                        resJson.put("WaterFeedRate", TempDatas);
                    } else if ("02".equals(CompareType)){
                        int farmBreedId = (Integer) map.get("farm_breed_id");
                        farmId = (Integer) map.get("farm_id");
                        Object x_axis = map.get("x_axis");
                        if (x_axis == null) {
                            tErrorContent = "批次数据错误。";
                            break;
                        }
                        if (!xAxis.contains(x_axis)) {
                            xAxis.add(x_axis);
                        }

                        String batchCode = map.get("batch_code").toString();
                        if (lastBreedID != farmBreedId) {
                            if (waterFeedArray.length() != 0) {
                                tJSONObject.put("HouseDatas", waterFeedArray);
                                TempDatas.put(tJSONObject);

                                tJSONObject = new JSONObject();
                                waterFeedArray = new JSONArray();
                            }
                            tJSONObject.put("FBBatchCode", batchCode);
                            tJSONObject.put("FarmBreedId", farmBreedId);
                        }
                        waterFeedArray.put(map.get("waterFeed").toString());
                        if (dealCount == listMap.size() && waterFeedArray.length() != 0){
                            tJSONObject.put("HouseDatas", waterFeedArray);
                            TempDatas.put(tJSONObject);
                        }
                        lastBreedID = farmBreedId;
                        resJson.put("FarmId", farmId);
                        resJson.put("HouseId", HouseId);
                        resJson.put("Result", "Success");
                        resJson.put("xAxis", xAxis);
                        resJson.put("WaterFeedRate", TempDatas);
                    }
                }
            }else{
                resJson.put("Result", "Fail");
                resJson.put("Error", "暂无入雏信息！");
            }
            dealRes = Constants.RESULT_SUCCESS;
            /** 业务处理结束 **/
        } catch (Exception e) {
            e.printStackTrace();
            try {
                dealRes = Constants.RESULT_FAIL;
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
        mLogger.info("=====Now end executing RepWaterFeedReqController.queryWaterFeed");
    }
}
