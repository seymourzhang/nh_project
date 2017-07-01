package com.nh.ifarm.report.action;

import com.nh.ifarm.report.service.RepCurveMobileService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Seymour on 2017/3/25.
 */
@Controller
@RequestMapping("/reportCurveMobile")
public class ReportCurveMobileAction extends BaseAction {

    @Autowired
    private RepCurveMobileService repCurveMobileService;

    public void reportCurve(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
            String CurveType = tUserJson.optString("CurveType");
            String BreedBatchId = tUserJson.optString("FarmBreedId");
            int FarmId = tUserJson.optInt("FarmId");
            String DateType = tUserJson.optString("DateType");
            String FarmType = tUserJson.optString("FarmType");

            PageData pageData = new PageData();
            pageData.put("FarmId", FarmId);
            pageData.put("UserId", userId);
            pageData.put("FarmType", FarmType);
            List<PageData> lpd = new ArrayList<>();
            if ("02".equals(DateType)) {
                lpd = repCurveMobileService.getCurveData(pageData);
            } else if ("01".equals(DateType)){
                lpd = repCurveMobileService.getCurveDataDaily(pageData);
            }
            if (lpd.size() == 0){
                resJson.put("Error", "暂无数据！");
                resJson.put("Result", "Fail");
            }else{
                int houseTemp = -1;
                int flag = 1;
                JSONArray DCRate = new JSONArray();
                List curveData = new ArrayList();
                List standarList = new ArrayList();
                List<Integer> xAxis = new ArrayList<>();
                List<Integer> houses = new ArrayList<>();
                JSONObject jo = new JSONObject();
                JSONObject standar = new JSONObject();
                net.sf.json.JSONObject houseAge = new net.sf.json.JSONObject();
                List<Integer> housesAge = new ArrayList<>();
                for (PageData data : lpd) {
                    int houseId = Integer.parseInt(data.get("house_code").toString());
                    if (lpd.size() == flag){
                        if ("01".equals(CurveType) || "03".equals(CurveType) || "05".equals(CurveType) || "06".equals(CurveType) || "07".equals(CurveType)  || "08".equals(CurveType) || "09".equals(CurveType)) {
                            if (!xAxis.contains(data.getInteger("growth_age")))
                                xAxis.add(data.getInteger("growth_age"));
                            housesAge.add(data.getInteger("growth_age"));
                        } else if ("02".equals(CurveType) || "04".equals(CurveType)) {
                            int layAge = Integer.parseInt(data.get("laying_age").toString());
                            if (!xAxis.contains(layAge))
                                xAxis.add(layAge);
                            housesAge.add(layAge);
                        }
                    }
                    if (houseTemp != houseId || lpd.size() == flag) {
                        if ("01".equals(CurveType) || "03".equals(CurveType) || "05".equals(CurveType) || "06".equals(CurveType) || "07".equals(CurveType)  || "08".equals(CurveType) || "09".equals(CurveType)) {
                            if (houseTemp != -1)
                                houseAge.put("" + houseTemp, housesAge.toString());
                        } else if ("02".equals(CurveType) || "04".equals(CurveType)) {
                            if (houseTemp != -1)
                                houseAge.put("" + houseTemp, housesAge.toString());
                        }
                        if (lpd.size() != flag)
                            houses.add(houseId);
                        houseTemp = houseId;
                        housesAge = new ArrayList<>();
                    }
                    if ("01".equals(CurveType) || "03".equals(CurveType) || "05".equals(CurveType) || "06".equals(CurveType) || "07".equals(CurveType)  || "08".equals(CurveType) || "09".equals(CurveType)) {
                        if (!xAxis.contains(data.getInteger("growth_age")))
                            xAxis.add(data.getInteger("growth_age"));
                        housesAge.add(data.getInteger("growth_age"));
                    } else if ("02".equals(CurveType) || "04".equals(CurveType)) {
                        int layAge = Integer.parseInt(data.get("laying_age").toString());
                        if (!xAxis.contains(layAge))
                            xAxis.add(layAge);
                        housesAge.add(layAge);
                    }
                    flag++;
                }
                Collections.sort(xAxis);
                if (xAxis.size() > 60){
                    int size = xAxis.size();
                    for (int i = 0; i < size - 60; ++i){
                        xAxis.remove(0);
                    }
                }
                for (Integer house : houses) {
                    jo = new JSONObject();
                    standar = new JSONObject();
                    curveData = new ArrayList();
                    standarList = new ArrayList();
                    String houseName = "";
                    for (Integer xAxi : xAxis) {
                        for (PageData data : lpd) {
                            int weekAge = 0;
                            int houseId = Integer.parseInt(data.get("house_code").toString());
                            if ("01".equals(CurveType) || "03".equals(CurveType) || "05".equals(CurveType) || "06".equals(CurveType) || "07".equals(CurveType)  || "08".equals(CurveType) || "09".equals(CurveType)) {
                                weekAge = Integer.parseInt(data.get("growth_age").toString());
                            } else if ("02".equals(CurveType) || "04".equals(CurveType)) {
                                weekAge = Integer.parseInt(data.get("laying_age").toString());
                            }
                            if (house == houseId) {
                                houseName = data.getString("house_name");
//                                String[] temp = houseAge.opt("" + house).toString().replace("[", "").replace("]", "").split(",");
                                List<Integer> temp1 = net.sf.json.JSONArray.toList(houseAge.getJSONArray("" + house));
                                if (temp1.contains(xAxi)) {
                                    if (xAxi == weekAge) {
                                        if ("01".equals(CurveType)) {
                                            standarList.add(data.get("母鸡标准累计死淘率").toString());
                                            if ("02".equals(DateType))
                                                curveData.add(data.get("母鸡累计死淘率").toString());
                                        } else if ("02".equals(CurveType)) {
                                            standarList.add(data.get("标准产蛋率").toString());
                                            curveData.add(data.get("实际产蛋率").toString());
                                        } else if ("03".equals(CurveType)) {
                                            standarList.add(data.get("标准母均重").toString());
                                            curveData.add(data.get("实际母均重"));
                                        } else if ("04".equals(CurveType)) {
                                            curveData.add(data.get("蛋重"));
                                        } else if ("05".equals(CurveType)) {
                                            curveData.add(data.get("实际日耗水"));
                                        } else if ("06".equals(CurveType)) {
                                            standarList.add(data.get("标准日耗料").toString());
                                            curveData.add(data.get("实际日耗料"));
                                        } else if ("07".equals(CurveType)) {
                                            curveData.add(data.get("水料比"));
                                        } else if ("08".equals(CurveType)) {
                                            curveData.add(data.get("存活率"));
                                            if ("02".equals(DateType))
                                                standarList.add(data.get("标准存活率"));
                                        } else if ("09".equals(CurveType)) {
                                            curveData.add(data.get("死淘率"));
                                        }
                                    }else{
                                        continue;
                                    }
                                }else{
                                    standarList.add("-");
                                    curveData.add("-");
                                    break;
                                }
                            }
                        }
                    }
                    jo.put("HouseDatas", curveData);
                    jo.put("HouseId", house);
                    jo.put("HouseName", houseName);
                    DCRate.put(jo);
                    if (!"04".equals(CurveType) && !"05".equals(CurveType) && !"07".equals(CurveType) && !"09".equals(CurveType)) {
                        standar.put("HouseName", "标准");
                        standar.put("HouseDatas", standarList);
                        standarList = new ArrayList();
                    }
                }
                if (!"04".equals(CurveType) && !"05".equals(CurveType) && !"07".equals(CurveType) && !"09".equals(CurveType)) {
                    DCRate.put(standar);
                }
                resJson.put("FarmId", FarmId);
                resJson.put("CurveType", CurveType);
                resJson.put("DCRate", DCRate);
                resJson.put("xAxis", xAxis);
                resJson.put("Result", "Success");
                resJson.put("Error", "");
            }
            dealRes = Constants.RESULT_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            resJson.put("Error", "程序处理错误，请联系管理员！");
            resJson.put("Result", "Fail");
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
