package com.nh.ifarm.analyze.action;

import com.nh.ifarm.analyze.service.DailyService;
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
import java.util.Date;
import java.util.List;

/**
 * Created by Seymour on 2017/3/23.
 */
@Controller
@RequestMapping("/dailyReportMobile")
public class DailyReportMobileAction extends BaseAction {

    @Autowired
    private DailyService dailyService;

    @RequestMapping("/dailyReport")
    public void dailyReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);
            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");

            String HouseBreedId = tUserJson.optString("HouseBreedId");
            int HouseId = tUserJson.optInt("HouseId");
            int WeekAgeBegin = tUserJson.optInt("WeekAgeBegin");
            int WeekAgeEnd = tUserJson.optInt("WeekAgeEnd");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();

            PageData pageData = new PageData();
            pageData.put("BreedBatchId", HouseBreedId);
            pageData.put("HouseId", HouseId);
            PageData specialDates = dailyService.selectDate(pageData);
            if (specialDates == null) {
                resJson.put("Result", "Fail");
                resJson.put("Error", "暂无入雏信息！");
            } else {
                String sDate = specialDates.getString("marketed_date");
                Date special = sdf.parse(sDate);
                PageData lpd = new PageData();
                if (now.after(special)) {
                    pageData.put("SpecialDate", sDate);
                    lpd = dailyService.selectBySpecialDate(pageData);
                } else {
                    pageData.put("SpecialDate", sdf.format(now));
                    lpd = dailyService.selectBySpecialDate(pageData);
                }
                if (lpd != null) {
                    resJson.put("HouseBreedId", HouseBreedId);
                    resJson.put("HouseId", HouseId);
                    resJson.put("cur_date", sdf.format(now));
                    resJson.put("growth_age", lpd.get("age"));
                    resJson.put("layer_age", lpd.get("lay_day_age"));
                    resJson.put("survival_rate", lpd.get("survival_rate").toString());
                }
                if (WeekAgeBegin == 0 && WeekAgeEnd == 0){
                    pageData.put("RecentThirtyDays", 100);
                }else {
                    pageData.put("WeekAgeBegin", WeekAgeBegin);
                    pageData.put("WeekAgeEnd", WeekAgeEnd);
                }
                List<PageData> dataInfo = dailyService.selectDailyReport(pageData);
                if (dataInfo.size() != 0) {
                    JSONArray datas = new JSONArray();
                    for (PageData data : dataInfo) {
                        JSONObject dd = new JSONObject();
                        dd.put("day_age", data.get("age"));
                        dd.put("male_cd_num", data.get("male_cur_cd").toString());
                        dd.put("male_cd_rate", data.get("male_cur_cd_rate").toString());
                        dd.put("female_cd_num", data.get("female_cur_cd").toString());
                        dd.put("female_cd_rate", data.get("female_cur_cd_rate").toString());
                        dd.put("intake_sig", data.get("act_feed_daliy").toString());
                        dd.put("water_sig", data.get("cur_water_daily").toString());
                        dd.put("male_body_weight", data.get("male_cur_weight").toString());
                        dd.put("female_body_weight", data.get("female_cur_weight").toString());
                        dd.put("layer_num", data.get("laying_cur_amount").toString());
                        dd.put("layer_rate", data.get("lay_rate").toString());
                        dd.put("female_cur_amount", data.get("female_cur_amount").toString());
                        dd.put("male_cur_amount", data.get("male_cur_amount").toString());
                        dd.put("uniformity", data.get("act_evenness").toString());
                        datas.put(dd);
                    }
                    resJson.put("dataInfo", datas);
                    resJson.put("Result", "Success");
                    resJson.put("Error", "");
                }else{
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "暂无数据！");
                }
            }
            dealRes = Constants.RESULT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            resJson.put("Error", "程序处理错误，请联系管理员！");
            resJson.put("Result", "Fail");
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/weeklyReport")
    public void weeklyReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);
            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");

            String HouseBreedId = tUserJson.optString("HouseBreedId");
            int HouseId = tUserJson.optInt("HouseId");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();

            PageData pageData = new PageData();
            pageData.put("BreedBatchId", HouseBreedId);
            pageData.put("HouseId", HouseId);
            PageData specialDates = dailyService.selectDate(pageData);
            if (specialDates == null) {
                resJson.put("Result", "Fail");
                resJson.put("Error", "暂无入雏信息！");
            } else {
                String sDate = specialDates.getString("marketed_date");
                Date special = sdf.parse(sDate);
                PageData lpd = new PageData();
                if (now.after(special)) {
                    pageData.put("SpecialDate", sDate);
                    lpd = dailyService.selectBySpecialDate(pageData);
                } else {
                    pageData.put("SpecialDate", sdf.format(now));
                    lpd = dailyService.selectBySpecialDate(pageData);
                }
                if (lpd != null) {
                    resJson.put("HouseBreedId", HouseBreedId);
                    resJson.put("HouseId", HouseId);
                    resJson.put("cur_date", sdf.format(now));
                    resJson.put("growth_week_age", lpd.get("growth_week_age"));
                    resJson.put("layer_week_age", lpd.get("layWeekAge"));
                    resJson.put("survival_rate", lpd.get("survival_week_rate").toString());
                }
                pageData.put("UserId", userId);
                List<PageData> dataInfo = dailyService.selectWeeklyReport(pageData);
                if (dataInfo.size() != 0) {
                    JSONArray datas = new JSONArray();
                    for (PageData data : dataInfo) {
                        JSONObject dd = new JSONObject();
                        dd.put("growth_week_age", data.get("growth_week_age").toString());
                        dd.put("week_cd_num", data.get("cur_cd").toString());
                        dd.put("week_cd_rate", data.get("cur_cd_rate").toString());
                        dd.put("intake_acc", data.get("intake_acc").toString());
                        dd.put("intake_sig", data.get("intake_sig").toString());
                        dd.put("water_week", data.get("water_week").toString());
                        dd.put("water_sig", data.get("water_sig").toString());
                        dd.put("week_layer_num", data.get("week_layer_num").toString());
                        dd.put("week_layer_rate", data.get("week_layer_rate").toString());
                        dd.put("male_body_weight", data.get("male_body_weight").toString());
                        dd.put("female_body_weight", data.get("female_body_weight").toString());
                        dd.put("female_week_amount", data.get("female_week_amount").toString());
                        dd.put("male_week_amount", data.get("male_week_amount").toString());
                        dd.put("uniformity", data.get("female_act_evenness").toString());
                        datas.put(dd);
                    }
                    resJson.put("dataInfo", datas);
                    resJson.put("Result", "Success");
                    resJson.put("Error", "");
                }
            }
            dealRes = Constants.RESULT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            resJson.put("Error", "程序处理错误，请联系管理员！");
            resJson.put("Result", "Fail");
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
