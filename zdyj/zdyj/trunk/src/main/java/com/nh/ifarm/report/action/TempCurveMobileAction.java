package com.nh.ifarm.report.action;

import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.report.service.TemProfileService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.common.PubFun;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Seymour on 2016/12/12.
 */
@Controller
@RequestMapping("/tempMobile")
public class TempCurveMobileAction extends BaseAction {

    @Autowired
    private TemProfileService temProfileService;

    @Autowired
    private BatchManageService batchManageService;

    @RequestMapping("/tempCurveReq")
    public void tempCurveReq(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        try {
            String ErrorMsg = "Null";
            JSONObject jsonObject = new JSONObject(aa);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            pd.put("BreedBatchId", tUserJson.optString("FarmBreedId"));
            pd.put("FarmId", tUserJson.optInt("FarmId"));
            pd.put("HouseId", tUserJson.optInt("HouseId"));
            pd.put("DataType", tUserJson.optString("DataType"));
            pd.put("ReqFlag", tUserJson.optString("ReqFlag"));

            int HouseId = tUserJson.getInt("HouseId");
            String ReqFlag = tUserJson.optString("ReqFlag");
            String DataType = tUserJson.optString("DataType");
            String DataRange = tUserJson.optString("DataRange");
            String BreedBatchId = tUserJson.optString("FarmBreedId");
            String data_age = "";
            String data_date = "";
            String SpecialDate = "";
            List<PageData> lcp = new ArrayList<>();

            if ("0".equals(BreedBatchId)) {
                ErrorMsg = "暂无批次信息";
            }
            if ("02".equals(DataType) && "Y".equals(ReqFlag)) {
                if (DataRange.length() != 10) {
                    ErrorMsg = "日期参数错误，请联系管理员！";
                }
            }
            if ("03".equals(DataType)) {
                if("Y".equals(ReqFlag) && DataRange.length() != 16){
                    ErrorMsg = "日期参数错误，请联系管理员！";
                }
                if("N".equals(ReqFlag) && DataRange.length() != 10){
                    ErrorMsg = "日期参数错误，请联系管理员！";
                }
            }

            if("Null".equals(ErrorMsg)){
                if ("01".equals(DataType) || ("02".equals(DataType) && "N".equals(ReqFlag))) {
                    PageData temp = new PageData();
                    temp.put("farm_id", pd.get("FarmId"));
                    temp.put("house_code", pd.get("HouseId"));
                    temp = batchManageService.selectBatchDataForMobile(temp);
                    if (temp != null) {
                        DataRange = sdf.format("1".equals(temp.get("status").toString()) ? new Date() : sdf.parse(temp.get("market_date").toString()));
                        SpecialDate = DataRange;
                    }
                }
                if ("01".equals(DataType)) {
                    pd.put("SpecialDate", SpecialDate);
                    lcp = temProfileService.selectTemForMobileDay(pd);
                } else if ("02".equals(DataType)) {
                    pd.put("DataRange", DataRange);
                    lcp = temProfileService.selectTemForMobileHour(pd);
                } else if ("03".equals(DataType)) {
                    pd.put("DataRange", DataRange);
                    lcp = temProfileService.selectTemForMobileMinute(pd);
                }
                if (lcp.size() == 0 ) {
                    ErrorMsg = "该批次暂无数据！";
                } else {
                    JSONArray TempDatas = new JSONArray();
                    JSONArray avgtempLeft1 = new JSONArray();
                    JSONArray avgtempLeft2 = new JSONArray();
                    JSONArray avgtempMiddle1 = new JSONArray();
                    JSONArray avgtempMiddle2 = new JSONArray();
                    JSONArray avgtempRight1 = new JSONArray();
                    JSONArray avgtempRight2 = new JSONArray();
                    JSONArray avgoutsideTemp = new JSONArray();
                    JSONArray highAlarmTempArray = new JSONArray();
                    JSONArray lowAlarmTempArray = new JSONArray();
                    JSONArray insideSetTempArray = new JSONArray();
                    JSONArray insideHumidityArray = new JSONArray();
                    JSONArray xAxis = new JSONArray();
                    for (PageData hashMap : lcp) {
                        Object x_axis = hashMap.get("x_axis");
                        if (x_axis == null) {
                            x_axis = 0;
                        }

                        if (x_axis.toString().endsWith("60")) {
                            int tHor = Integer.parseInt(x_axis.toString().substring(0, 2).replace(":", "")) + 1;
                            x_axis = PubFun.fillLeftChar(tHor, '0', 2) + ":00";
                        }

                        Object avgtempLeft11 = hashMap.get("avgtempLeft1");
                        if (avgtempLeft11 == null) {
                            avgtempLeft11 = 0;
                        }
                        Object avgtempLeft22 = hashMap.get("avgtempLeft2");
                        if (avgtempLeft22 == null) {
                            avgtempLeft22 = 0;
                        }
                        Object avgtempMiddle11 = hashMap.get("avgtempMiddle1");
                        if (avgtempMiddle11 == null) {
                            avgtempMiddle11 = 0;
                        }
                        Object avgtempMiddle22 = hashMap.get("avgtempMiddle2");
                        if (avgtempMiddle22 == null) {
                            avgtempMiddle22 = 0;
                        }
                        Object avgtempRight11 = hashMap.get("avgtempRight1");
                        if (avgtempRight11 == null) {
                            avgtempRight11 = 0;
                        }
                        Object avgtempRight22 = hashMap.get("avgtempRight2");
                        if (avgtempRight22 == null) {
                            avgtempRight22 = 0;
                        }
                        Object avgoutsideTemp1 = hashMap.get("avgoutsidetemp");
                        if (avgoutsideTemp1 == null) {
                            avgoutsideTemp1 = 0;
                        }
                        Object highAlarmTemp = hashMap.get("highAlarmTemp");
                        if (highAlarmTemp == null) {
                            highAlarmTemp = 0;
                        }
                        Object lowAlarmTemp = hashMap.get("lowAlarmTemp");
                        if (lowAlarmTemp == null) {
                            lowAlarmTemp = 0;
                        }
                        Object insideSetTemp = hashMap.get("insideSetTemp");
                        if (insideSetTemp == null) {
                            insideSetTemp = 0;
                        }
                        Object insideHumidity = hashMap.get("insideHumidity");
                        if (insideHumidity == null) {
                            insideHumidity = 0;
                        }
                        xAxis.put(x_axis);
                        if (hashMap.get("dataflag").equals("N")) {
                            continue;
                        }
                        avgtempLeft1.put("-99.0".equals(avgtempLeft11.toString()) ? "0" : avgtempLeft11.toString());
                        avgtempLeft2.put("-99.0".equals(avgtempLeft22.toString()) ? "0" : avgtempLeft22.toString());
                        avgtempMiddle1.put("-99.0".equals(avgtempMiddle11.toString()) ? "0" : avgtempMiddle11.toString());
                        avgtempMiddle2.put("-99.0".equals(avgtempMiddle22.toString()) ? "0" : avgtempMiddle22.toString());
                        avgtempRight1.put("-99.0".equals(avgtempRight11.toString()) ? "0" : avgtempRight11.toString());
                        avgtempRight2.put("-99.0".equals(avgtempRight22.toString()) ? "0" : avgtempRight22.toString());
                        avgoutsideTemp.put("-99.0".equals(avgoutsideTemp1.toString()) ? "0" : avgoutsideTemp1.toString());
                        highAlarmTempArray.put("-99.0".equals(highAlarmTemp.toString()) ? "0" : highAlarmTemp.toString());
                        lowAlarmTempArray.put("-99.0".equals(lowAlarmTemp.toString()) ? "0" : lowAlarmTemp.toString());
                        insideSetTempArray.put("-99.0".equals(insideSetTemp.toString()) ? "0" : insideSetTemp.toString());
                        insideHumidityArray.put("-99.0".equals(insideHumidity.toString()) ? "0" : insideHumidity.toString());
                        if (DataType.equals("02")) {
                            data_date = hashMap.get("data_date") != null ? hashMap.get("data_date").toString() : "Null";
                        } else if (DataType.equals("01")) {
                            data_date = hashMap.get("date_date") != null ? hashMap.get("data_date").toString() : "Null";
                        }
                        if (hashMap.get("data_age") != null) {
                            data_age = hashMap.get("data_age").toString();
                        }
                    }

                    resJson.put("xAxis", xAxis);
                    JSONObject tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "tempLeft1");
                    tJSONObject.put("TempAreaName", "T1");
                    tJSONObject.put("TempCurve", avgtempLeft1);
                    TempDatas.put(tJSONObject);
                    tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "tempLeft2");
                    tJSONObject.put("TempAreaName", "T2");
                    tJSONObject.put("TempCurve", avgtempLeft2);
                    TempDatas.put(tJSONObject);
                    tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "tempMiddle1");
                    tJSONObject.put("TempAreaName", "T3");
                    tJSONObject.put("TempCurve", avgtempMiddle1);
                    TempDatas.put(tJSONObject);
                    // tJSONObject = new JSONObject();
                    // tJSONObject.put("TempAreaCode", "tempMiddle2");
                    // tJSONObject.put("TempAreaName", "中区温度2");
                    // tJSONObject.put("TempCurve", avgtempMiddle2);
                    // TempDatas.put(tJSONObject);
                    tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "tempRight1");
                    tJSONObject.put("TempAreaName", "T4");
                    tJSONObject.put("TempCurve", avgtempRight1);
                    TempDatas.put(tJSONObject);
                    tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "tempRight2");
                    tJSONObject.put("TempAreaName", "T5");
                    tJSONObject.put("TempCurve", avgtempRight2);
                    TempDatas.put(tJSONObject);
                    tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "out_temp");
                    tJSONObject.put("TempAreaName", "室外");
                    tJSONObject.put("TempCurve", avgoutsideTemp);
                    TempDatas.put(tJSONObject);
                    tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "highAlarmTemp");
                    tJSONObject.put("TempAreaName", "高报");
                    tJSONObject.put("TempCurve", highAlarmTempArray);
                    TempDatas.put(tJSONObject);
                    tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "lowAlarmTemp");
                    tJSONObject.put("TempAreaName", "低报");
                    tJSONObject.put("TempCurve", lowAlarmTempArray);
                    TempDatas.put(tJSONObject);
                    tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "insideSetTemp");
                    tJSONObject.put("TempAreaName", "目标");
                    tJSONObject.put("TempCurve", insideSetTempArray);
                    TempDatas.put(tJSONObject);
//                if ("03".equals(DataType)) {
                    tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "insideHumidity");
                    tJSONObject.put("TempAreaName", "湿度");
                    tJSONObject.put("TempCurve", insideHumidityArray);
                    TempDatas.put(tJSONObject);
//                }
                    resJson.put("TempDatas", TempDatas);
                    resJson.put("HouseId", HouseId);
                    resJson.put("DataDate", DataType.equals("03") ? sdf.format(sdf.parse(DataRange)) : data_date);
                    resJson.put("data_age", data_age);
                    resJson.put("FarmBreedId", BreedBatchId);
                    resJson.put("Result", "Success");
                    resJson.put("Error", "");
                    dealRes = Constants.RESULT_SUCCESS;
                }
            }
            if(!"Null".equals(ErrorMsg)){
                resJson.put("Error", ErrorMsg);
                resJson.put("Result", "Fail");
                dealRes = Constants.RESULT_SUCCESS;
            }
        } catch (Exception e){
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/lcCurveReq")
    public void lcCurveReq(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        try {
            String ErrorMsg = "Null";
            JSONObject jsonObject = new JSONObject(aa);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            pd.put("BreedBatchId", tUserJson.get("FarmBreedId"));
            pd.put("FarmId", tUserJson.get("FarmId"));
            pd.put("HouseId", tUserJson.get("HouseId"));
            pd.put("DataType", tUserJson.get("DataType"));
            pd.put("ReqFlag", tUserJson.get("ReqFlag"));
            pd.put("DataRange", tUserJson.get("DataRange"));

            int HouseId = tUserJson.getInt("HouseId");
            String ReqFlag = tUserJson.optString("ReqFlag");
            String DataType = tUserJson.optString("DataType");
            String DataRange = tUserJson.optString("DataRange");
            String BreedBatchId = tUserJson.optString("FarmBreedId");
            String data_age = "";
            String data_date = "";
            String SpecialDate = "";
            List<PageData> lcp = new ArrayList<>();
            if ("0".equals(BreedBatchId)) {
                ErrorMsg = "暂无批次信息";
            }
            if ("02".equals(DataType) && "Y".equals(ReqFlag)) {
                if (DataRange.length() != 10) {
                    ErrorMsg = "日期参数错误，请联系管理员！";
                }
            }
            if ("03".equals(DataType)) {
                if ("Y".equals(ReqFlag) && DataRange.length() != 16) {
                    ErrorMsg = "日期参数错误，请联系管理员！";
                }
                if ("N".equals(ReqFlag) && DataRange.length() != 10) {
                    ErrorMsg = "日期参数错误，请联系管理员！";
                }
            }

            if ("Null".equals(ErrorMsg)) {
                if ("01".equals(DataType) || ("02".equals(DataType) && "N".equals(ReqFlag))) {
                    PageData temp = new PageData();
                    temp.put("farm_id", pd.get("FarmId"));
                    temp.put("house_code", pd.get("HouseId"));
                    temp = batchManageService.selectBatchDataForMobile(temp);
                    if (temp != null) {
                        DataRange = sdf.format("1".equals(temp.get("status").toString()) ? new Date() : sdf.parse(temp.get("market_date").toString()));
                        SpecialDate = DataRange;
                    }
                }
                if ("01".equals(DataType)) {
                    pd.put("SpecialDate", SpecialDate);
                    lcp = temProfileService.selectLCForMobileDay(pd);
                } else if ("02".equals(DataType)) {
                    pd.put("DataRange", DataRange);
                    lcp = temProfileService.selectLCForMobileHour(pd);
                } else if ("03".equals(DataType)) {
                    pd.put("DataRange", DataRange);
                    lcp = temProfileService.selectLCForMobileMinute(pd);
                }
                if (lcp.size() == 0) {
                    ErrorMsg = "该批次暂无数据！";
                } else {
                    JSONArray TempDatas = new JSONArray();
                    JSONArray co2 = new JSONArray();
                    JSONArray lux = new JSONArray();
                    JSONArray xAxis = new JSONArray();
                    for (PageData hashMap : lcp) {
                        Object x_axis = hashMap.get("x_axis");
                        if (x_axis == null) {
                            x_axis = 0;
                        }

                        if (x_axis.toString().endsWith("60")) {
                            int tHor = Integer.parseInt(x_axis.toString().substring(0, 2).replace(":", "")) + 1;
                            x_axis = PubFun.fillLeftChar(tHor, '0', 2) + ":00";
                        }

                        Object co2Obj = hashMap.get("co2");
                        if (co2Obj == null) {
                            co2Obj = 0;
                        }
                        Object luxObj = hashMap.get("lux");
                        if (luxObj == null) {
                            luxObj = 0;
                        }
                        xAxis.put(x_axis);
                        if (hashMap.get("dataflag").equals("N")) {
                            continue;
                        }
                        co2.put("-99.0".equals(co2Obj.toString()) ? "0" : co2Obj.toString());
                        lux.put("-99.0".equals(luxObj.toString()) ? "0" : luxObj.toString());
                        if (DataType.equals("02")) {
                            data_date = hashMap.get("data_date") != null ? hashMap.get("data_date").toString() : "Null";
                        } else if (DataType.equals("01")) {
                            data_date = hashMap.get("date_date") != null ? hashMap.get("data_date").toString() : "Null";
                        }
                        if (hashMap.get("data_age") != null) {
                            data_age = hashMap.get("data_age").toString();
                        }
                    }

                    resJson.put("xAxis", xAxis);
                    JSONObject tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "tempLeft1");
                    tJSONObject.put("TempAreaName", "二氧化碳");
                    tJSONObject.put("TempCurve", co2);
                    TempDatas.put(tJSONObject);
                    tJSONObject = new JSONObject();
                    tJSONObject.put("TempAreaCode", "tempLeft2");
                    tJSONObject.put("TempAreaName", "光照");
                    tJSONObject.put("TempCurve", lux);
                    TempDatas.put(tJSONObject);
                    resJson.put("TempDatas", TempDatas);
                    resJson.put("HouseId", HouseId);
                    resJson.put("DataDate", DataType.equals("03") ? sdf.format(sdf.parse(DataRange)) : data_date);
                    resJson.put("data_age", data_age);
                    resJson.put("FarmBreedId", BreedBatchId);
                    resJson.put("Result", "Success");
                    resJson.put("Error", "");
                    dealRes = Constants.RESULT_SUCCESS;
                }
            } else {
                resJson.put("Error", ErrorMsg);
                resJson.put("Result", "Fail");
                dealRes = Constants.RESULT_SUCCESS;
            }
        } catch (JSONException JSONe) {
            JSONe.printStackTrace();
            dealRes = Constants.RESULT_FAIL;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
