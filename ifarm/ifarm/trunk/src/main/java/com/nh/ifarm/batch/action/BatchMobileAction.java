package com.nh.ifarm.batch.action;

import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Seymour on 2017/6/13.
 */
@Controller
@RequestMapping("/batchMobile")
public class BatchMobileAction extends BaseAction {

    @Autowired
    private BatchManageService batchManageService;

    @RequestMapping("/placeChildQurey")
    public void placeChildQurey(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        try {
            JSONObject jsonObject = new JSONObject(aa);
            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            int FarmId = tUserJson.optInt("farm_id");
            String SendType = tUserJson.optString("send_type");

            pd.put("user_id", userId);
            pd.put("farm_id", FarmId);
            String errorMsg = "NULL";
            JSONObject childInfo = new JSONObject();
            JSONArray childInfos = new JSONArray();
            if ("01".equals(SendType)) {
                pd.put("mobileFlag", "app1");
                List<PageData> list2 = batchManageService.getFaChuData(pd);
                pd.put("mobileFlag", "app2");
                List<PageData> list = batchManageService.getFaChuData(pd);
                for (PageData data : list2) {
                    list.add(data);
                }
                if (list.size() == 0) {
                    errorMsg =  "暂无发雏信息！";
                } else {
                    for (PageData data : list) {
                        childInfo = new JSONObject();
                        childInfo.put("childBatchNo", data.get("child_batch_no"));
                        childInfo.put("farmName", data.get("farm_name"));
                        childInfo.put("variety", data.get("variety"));
                        childInfo.put("corporation", data.get("corporation"));
                        childInfo.put("srcBatchNo", data.get("src_batch_no"));
                        childInfo.put("sendDate", data.get("operation_date"));
                        childInfo.put("sendNum", data.get("send_female_num"));
                        childInfo.put("checkNum", data.get("check_female_num") == null ? "" : data.get("check_female_num"));
                        childInfo.put("checkDate", data.get("check_date") == null ? "" : data.get("check_date"));
                        childInfo.put("sendStatus", data.get("check_date") == null && data.get("check_female_num") == null ? 1 : 0);
                        childInfos.put(childInfo);
                    }
                }
            }else if ("02".equals(SendType)){
                pd.put("mobileFlag", "app1");
                List<PageData> list2 = batchManageService.getFaLiaoData(pd);
                pd.put("mobileFlag", "app2");
                List<PageData> list = batchManageService.getFaLiaoData(pd);
                for (PageData data : list2) {
                    list.add(data);
                }
                if (list.size() == 0) {
                    errorMsg = "暂无发料信息！";
                } else {
                    for (PageData data : list) {
                        childInfo = new JSONObject();
                        childInfo.put("send_id", data.get("send_ID"));
                        childInfo.put("childBatchNo", data.get("send_batch_no"));
                        childInfo.put("farmName", data.get("farm_name"));
                        childInfo.put("goodType", data.get("good_type").toString().equals("3") ? "饲料" : "药品");
                        childInfo.put("goodName", data.get("goods_name"));
                        childInfo.put("sendDate", data.get("operation_date"));
                        childInfo.put("sendNum", data.get("send_num"));
                        childInfo.put("checkNum", data.get("check_num") == null ? "" : data.get("check_num"));
                        childInfo.put("checkDate", data.get("check_date") == null ? "" : data.get("check_date"));
                        childInfo.put("sendStatus", data.get("check_date") == null && data.get("check_num") == null ? 1 : 0);
                        childInfos.put(childInfo);
                    }
                }
            }else if ("03".equals(SendType)){
                pd.put("mobileFlag", "app1");
                List<PageData> list2 = batchManageService.getFaYaoData(pd);
                pd.put("mobileFlag", "app2");
                List<PageData> list = batchManageService.getFaYaoData(pd);
                for (PageData data : list2) {
                    list.add(data);
                }
                if (list.size() == 0) {
                    errorMsg = "暂无发药信息！";
                } else {
                    for (PageData data : list) {
                        childInfo = new JSONObject();
                        childInfo.put("send_id", data.get("send_ID"));
                        childInfo.put("childBatchNo", data.get("send_batch_no"));
                        childInfo.put("farmName", data.get("farm_name"));
                        childInfo.put("goodType", data.get("good_type").toString().equals("6") ? "饲料" : data.get("good_type").toString().equals("2") || data.get("good_type").toString().equals("3") ? "药品" : "其他");
                        childInfo.put("goodName", data.get("goods_name"));
                        childInfo.put("sendDate", data.get("operation_date"));
                        childInfo.put("sendNum", data.get("send_num"));
                        childInfo.put("checkNum", data.get("check_num") == null ? "" : data.get("check_num"));
                        childInfo.put("checkDate", data.get("check_date") == null ? "" : data.get("check_date"));
                        childInfo.put("sendStatus", data.get("check_date") == null && data.get("check_num") == null ? 1 : 0);
                        childInfos.put(childInfo);
                    }
                }
            }
            if (!"NULL".equals(errorMsg)){
                resJson.put("Result", "Fail");
                resJson.put("Error", errorMsg);
            }else{
                resJson.put("ChildInfos", childInfos);
                resJson.put("Result", "Success");
                resJson.put("Error", "");
            }
        }catch (Exception e) {
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/placeChildSave")
    public void placeChildSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        try {
            JSONObject jsonObject = new JSONObject(aa);
            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            int FarmId = tUserJson.optInt("farmId");
            String SendType = tUserJson.optString("sendType");
            String ChildBatchNo = tUserJson.optString("childBatchNo");
            int CheckNum = tUserJson.optInt("checkNum");
            String CheckDate = tUserJson.optString("checkDate");
            String SendId = tUserJson.optString("send_id");
            if ("01".equals(SendType)) {
                PageData pageData = new PageData();
                pageData.put("farm_id", FarmId);
                pageData.put("check_female_num", CheckNum);
                pageData.put("check_date", CheckDate);
                pageData.put("child_batch_no", ChildBatchNo);
                pageData.put("mobileCheckFlag", "app");
                PageData result = batchManageService.editFaChuData(pageData);
                if ((boolean) result.get("result")) {
                    resJson.put("Error", "");
                    resJson.put("Result", "Success");
                } else {
                    resJson.put("Error", result.get("msg").toString());
                    resJson.put("Result", "Fail");
                }
            } else if ("02".equals(SendType) || "03".equals(SendType)) {
                PageData pageData = new PageData();
                pageData.put("farm_id", FarmId);
                pageData.put("send_ID", SendId);
                pageData.put("check_num", CheckNum);
                pageData.put("check_date", CheckDate);
                pageData.put("mobileCheckFlag", "app");
                PageData result = batchManageService.editFaLiaoData(pageData);
                if ((boolean) result.get("result")) {
                    resJson.put("Error", "");
                    resJson.put("Result", "Success");
                } else {
                    resJson.put("Error", result.get("msg").toString());
                    resJson.put("Result", "Fail");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/placeYouthQurey")
    public void placeYouthQurey(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        try {
            JSONObject jsonObject = new JSONObject(aa);
            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            int FarmId = tUserJson.optInt("FarmId");
            int HouseId = tUserJson.optInt("HouseId");

            pd.put("farm_id", FarmId);
            pd.put("house_code", HouseId);
            List<PageData> list = batchManageService.getFaChuData(pd);
            List<PageData> lpd = batchManageService.getCreateBatchData(pd);
            if (lpd.size() == 0) {
                JSONObject youth = new JSONObject();
                resJson.put("Result", "Fail");
                youth.put("childBatchNo", list.size() == 0 ? "暂无发雏信息" : list.get(0).get("child_batch_no"));
                resJson.put("youthInfos", youth);
                resJson.put("Error", "暂无进鸡批次信息！");
            } else {
                JSONObject youth = new JSONObject();
                youth.put("childBatchNo", list.size() == 0 ? "暂无发雏信息" : list.get(0).get("child_batch_no"));
                youth.put("youthBatchNo", lpd.get(0).get("batchNo"));
                youth.put("variety_id", lpd.get(0).get("variety_id"));
                youth.put("variety", lpd.get(0).get("variety"));
                youth.put("corporation_id", lpd.get(0).get("corporation_id"));
                youth.put("corporation", lpd.get(0).get("corporation"));
                youth.put("growthAge", lpd.get(0).get("grow_age"));
                youth.put("placeDate", lpd.get(0).get("operation_date"));
                youth.put("placeNum", lpd.get(0).get("female_count"));
                resJson.put("youthInfos", youth);
                resJson.put("Result", "Success");
                resJson.put("Error", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/placeYouthSave")
    public void placeYouthSave(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        try {
            JSONObject jsonObject = new JSONObject(aa);
            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            String FarmId = tUserJson.optString("farmId");
            String FarmName = tUserJson.optString("farmName");
            String FarmType = tUserJson.optString("farmType");
            int HouseId = tUserJson.optInt("houseId");
            String HouseName = tUserJson.optString("houseName");
            String YouthBatchNo = tUserJson.optString("youthBatchNo");
            String VarietyId = tUserJson.optString("varietyId");
            String Variety = tUserJson.optString("variety");
            int CorporationId = tUserJson.optInt("corporationId");
            String Corporation = tUserJson.optString("corporation");
            int GrowthAge = tUserJson.optInt("growthAge");
            String PlaceDate = tUserJson.optString("placeDate");
            int IsMix = tUserJson.optInt("isMix");
            int PlaceMaleNum = tUserJson.optInt("placeMaleNum");
            int PlaceFemaleNum = tUserJson.optInt("placeFemaleNum");

            pd.put("farm_id", FarmId + "");
            pd.put("farm_name", FarmName);
            pd.put("house_code", HouseId + "");
            pd.put("house_type", FarmType);
            pd.put("batch_no", YouthBatchNo);
            pd.put("house_name", HouseName);
            pd.put("operation_date", PlaceDate);
            pd.put("grow_age", GrowthAge);
            pd.put("variety_id", VarietyId);
            pd.put("variety", Variety);
            pd.put("corporation_id", CorporationId);
            pd.put("corporation", Corporation);
            pd.put("user_id", userId);
            pd.put("male_count", PlaceMaleNum);
            pd.put("female_count", PlaceFemaleNum);
            pd.put("is_mix", IsMix);
            PageData pageData = new PageData();
            pageData.put("farm_id", FarmId);
            pageData.put("houseBatchStatus", "1");
            PageData checkDate = batchManageService.selectBatchDataForMobile(pageData);
            PageData result = new PageData();
            if (checkDate == null) {
                result = batchManageService.saveCreateBatchData(pd);
                if ((boolean) result.get("result")){
                    resJson.put("Result", "Success");
                    resJson.put("Error", "");
                } else {
                    resJson.put("Result", "Fail");
                    resJson.put("Error", result.get("msg"));
                }
            } else {
                if (YouthBatchNo.equals("")){
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "必须指定批次号！");
                } else if (PlaceDate.equals("")){
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "必须指定进鸡日！");
                } else if (VarietyId.equals("")){
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "必须指定品种！");
                }else if (PlaceFemaleNum <= 0){
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "进鸡数必须大于0！");
                } else if (GrowthAge <= 0){
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "生长日龄必须大于0！");
                } else {
                    String batchNoOld = checkDate.get("batch_no").toString();
                    if (YouthBatchNo.equals(batchNoOld)) {
                        PageData pageData1 = new PageData();
                        pageData1.put("farm_id", FarmId);
                        pageData1.put("house_code", HouseId);
                        pageData1.put("batch_no", batchNoOld);
                        checkDate = batchManageService.selectBatchDataForMobile(pageData1);
                        if (checkDate == null) {
                            result = batchManageService.saveCreateBatchData(pd);
                            if ((boolean) result.get("result")) {
                                resJson.put("Result", "Success");
                                resJson.put("Error", "");
                            } else {
                                resJson.put("Result", "Fail");
                                resJson.put("Error", result.get("msg"));
                            }
                        } else {
                            resJson.put("Result", "Fail");
                            resJson.put("Error", "该栋舍已进鸡！");
                        }
                    } else {
                        resJson.put("Result", "Fail");
                        resJson.put("Error", "农场内该 " + batchNoOld + " 批次未结束，不允许新建批次！");
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/marketQuery")
    public void marketQuery(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        try {
            JSONObject jsonObject = new JSONObject(aa);
            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            int FarmId = tUserJson.optInt("FarmId");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            pd.put("farm_id", FarmId);
            pd.put("mobileFlag", "app");
            List<PageData> lpd = batchManageService.getOverBatchData(pd);
            if (lpd.size() == 0){
                resJson.put("Result", "Fail");
                resJson.put("Error", "暂无可出栏数据！");
            } else {
                JSONArray batchs = new JSONArray();
                for (PageData data : lpd) {
                    JSONObject batch = new JSONObject();
                    batch.put("status", data.get("status"));
                    batch.put("houseId", data.get("houseId"));
                    batch.put("houseName", data.get("house"));
                    batch.put("marketDate", data.get("market_operation_date") == null ? "" : sdf.format(sdf.parse(data.get("market_operation_date").toString())));
                    batch.put("marketNum", data.get("over_batch_count"));
                    batch.put("perFemaleWeight", data.get("female_weight"));
                    batch.put("growthAge", data.get("grow_age"));
                    batch.put("batchNo", data.get("batch_no"));
                    batchs.put(batch);
                }
                resJson.put("matketInfos", batchs);
                resJson.put("Result", "Success");
                resJson.put("Error", "");
            }
        }catch (Exception e) {
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/marketSave")
    public void marketSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        try {
            JSONObject jsonObject = new JSONObject(aa);
            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            int FarmId = tUserJson.optInt("farmId");
            int HouseId = tUserJson.optInt("houseId");
            String HouseName = tUserJson.optString("houseName");
            String MarketDate = tUserJson.optString("marketDate");
            BigDecimal PerFemaleWeight = new BigDecimal(tUserJson.opt("perFemaleWeight").toString());
            int GrowthAge = tUserJson.optInt("growthAge");
            int marketNum = tUserJson.optInt("marketNum");
            String BatchNo = tUserJson.optString("batchNo");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            PageData pageData = new PageData();
            pageData.put("farm_id", FarmId);
            pageData.put("house_code", HouseId);
            pageData.put("mobileFlag", "app");
            List<PageData> lpd = batchManageService.getOverBatchData(pageData);
            if (lpd.size() == 0) {
                resJson.put("Result", "Fail");
                resJson.put("Error", "数据错误，请联系管理员！");
            } else if (lpd.get(0).get("status").toString().equals("0")) {
                resJson.put("Result", "Fail");
                resJson.put("Error", "该栋舍已出栏！");
            } else {
                PageData pp = batchManageService.selectBatchDataForMobile(pageData);
                if (pp == null) {
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "数据错误，请联系管理员！");
                } else {
                    Date placeDate = sdf.parse(pp.get("operation_date").toString());
                    Date mardetDate = sdf.parse(MarketDate);
                    if (mardetDate.before(placeDate)) {
                        resJson.put("Result", "Fail");
                        resJson.put("Error", "出栏日期在进鸡日期之前，请重新填写！");
                    } else {
                        pd.put("farm_id", FarmId + "");
                        pd.put("batch_no", BatchNo);
                        pd.put("house_id", HouseId);
                        pd.put("house_code", HouseId + "");
                        pd.put("house_name", HouseName);
                        pd.put("male_count", 0);
                        pd.put("female_count", marketNum);
                        pd.put("female_weight", PerFemaleWeight);
                        pd.put("user_id", userId);
                        pd.put("operation_date", MarketDate);
                        pd.put("grow_age", GrowthAge + "");
                        PageData result = batchManageService.saveOverBatchData(pd);
                        if ((boolean) result.get("result")) {
                            resJson.put("Result", "Success");
                            resJson.put("Error", "");
                        } else {
                            resJson.put("Result", "Fail");
                            resJson.put("Error", result.get("msg"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
