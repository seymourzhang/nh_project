package com.nh.ifarm.system.action;

import com.nh.ifarm.system.service.RepMobileService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Seymour on 2017/5/31.
 */
@Controller
@RequestMapping("repMobile")
public class RepMobileAction extends BaseAction {

    @Autowired
    private RepMobileService repMobileService;

    @RequestMapping("/queryRepList")
    public void queryRepList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        PageData pd = new PageData();
        try {
            List<PageData> lpd = repMobileService.getRepMobile();
            if (lpd.size() == 0) {
                resJson.put("Result", "Fail");
                resJson.put("Error", "暂无数据！");
            } else {
                JSONArray reps = new JSONArray();
                for (PageData pageData : lpd) {
                    JSONObject rep = new JSONObject();
                    rep.put("RepId", pageData.get("rep_id"));
                    rep.put("RepName", pageData.get("rep_name"));
                    rep.put("RepUrl", pageData.get("rep_url"));
                    reps.put(rep);
                }
                resJson.put("RepInfo", reps);
                resJson.put("Result", "Success");
            }
            dealRes = Constants.RESULT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (JSONException ee) {
                ee.printStackTrace();
            }
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/weekRepList")
    public void weekRepList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);

            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            int FarmId = tUserJson.optInt("FarmId");
            pd.put("farmId", FarmId);
            List<PageData> pageData = repMobileService.getWeekDate(pd);
            JSONArray weeks = new JSONArray();
            if (pageData != null && pageData.size() != 0) {
                for (PageData data : pageData) {
                    JSONObject week = new JSONObject();
                    week.put(data.get("week_code").toString(), data.get("week_name"));
                    weeks.put(week);
                }
                resJson.put("FarmId", FarmId);
                resJson.put("weekList", weeks);
                resJson.put("Result", "Success");
            }else{
                resJson.put("Result", "Fail");
                resJson.put("Error", "暂无数据！");
            }
            dealRes = Constants.RESULT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (JSONException ee) {
                ee.printStackTrace();
            }
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/queryParityPerformance")
    public void queryParityPerformance(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);

            int userId = jsonObject.optInt("id_spa");
            JSONObject tUserJson = jsonObject.getJSONObject("params");
            int FarmId = tUserJson.optInt("FarmId");
            String ReportType = tUserJson.optString("ReportType");
            String ViewType = tUserJson.optString("ViewType");
            String WeekDate = tUserJson.optString("WeekDate");
            if (WeekDate.length() > 15){
                resJson.put("Error", "日期格式错误！");
                resJson.put("Result", "Fail");
            }else {
                pd.put("farmId", FarmId);
                String[] temp = WeekDate.replaceAll("\\)", "").split("\\(");
                String weekAge = temp[1];
                pd.put("weekAge", weekAge);
                pd.put("weekDate", temp[0]);
                JSONArray performance = new JSONArray();
                if ("01".equals(ReportType)) {
                    if ("01".equals(ViewType)) {
                        List<PageData> lpd = repMobileService.getItemData(pd);
                        List<PageData> lpd_total = repMobileService.getItemDataTotal(pd);
                        List<PageData> lpd_avg = repMobileService.getItemDataAvg(pd);
                        List<PageData> lpd_target = repMobileService.getItemDataTarget(pd);
                        if (lpd.size() != 0 && lpd_total.size() != 0 && lpd_avg.size() != 0 && lpd_target.size() != 0) {
                            JSONObject json1 = new JSONObject();
                            json1.put("itemName", "总配种头数");
                            json1.put("itemValue", lpd.get(0).get("total_count"));
                            json1.put("totalValue", lpd_total.get(0).get("total_count"));
                            json1.put("targetValue", lpd_target.get(0).get("total_count"));
                            json1.put("averageValue", lpd_avg.get(0).get("total_count"));

                            JSONObject json2 = new JSONObject();
                            json2.put("itemName", "首次配种头数");
                            json2.put("itemValue", lpd.get(0).get("first_count"));
                            json2.put("totalValue", lpd_total.get(0).get("first_count"));
                            json2.put("targetValue", lpd_target.get(0).get("first_count"));
                            json2.put("averageValue", lpd_avg.get(0).get("first_count"));

                            JSONObject json3 = new JSONObject();
                            json3.put("itemName", "重复头数");
                            json3.put("itemValue", lpd.get(0).get("repeat_count"));
                            json3.put("totalValue", lpd_total.get(0).get("repeat_count"));
                            json3.put("targetValue", lpd_target.get(0).get("repeat_count"));
                            json3.put("averageValue", lpd_avg.get(0).get("repeat_count"));

                            JSONObject json4 = new JSONObject();
                            json4.put("itemName", "重复比率(%)");
                            json4.put("itemValue", new BigDecimal(lpd.get(0).get("repeat_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("totalValue", new BigDecimal(lpd_total.get(0).get("repeat_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("targetValue", new BigDecimal(lpd_target.get(0).get("repeat_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("averageValue", new BigDecimal(lpd_avg.get(0).get("repeat_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json5 = new JSONObject();
                            json5.put("itemName", "多重配种头数");
                            json5.put("itemValue", lpd.get(0).get("more_count"));
                            json5.put("totalValue", lpd_total.get(0).get("more_count"));
                            json5.put("targetValue", lpd_target.get(0).get("more_count"));
                            json5.put("averageValue", lpd_avg.get(0).get("more_count"));

                            JSONObject json6 = new JSONObject();
                            json6.put("itemName", "多重配种比率(%)");
                            json6.put("itemValue", new BigDecimal(lpd.get(0).get("more_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("totalValue", new BigDecimal(lpd_total.get(0).get("more_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("targetValue", new BigDecimal(lpd_target.get(0).get("more_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("averageValue", new BigDecimal(lpd_avg.get(0).get("more_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json7 = new JSONObject();
                            json7.put("itemName", "配种次均授精数");
                            json7.put("itemValue", lpd.get(0).get("insemination_num"));
                            json7.put("totalValue", lpd_total.get(0).get("insemination_num"));
                            json7.put("targetValue", lpd_target.get(0).get("insemination_num"));
                            json7.put("averageValue", lpd_avg.get(0).get("insemination_num"));

                            JSONObject json8 = new JSONObject();
                            json8.put("itemName", "人工授精配种头数");
                            json8.put("itemValue", lpd.get(0).get("artificial_insemination_count"));
                            json8.put("totalValue", lpd_total.get(0).get("artificial_insemination_count"));
                            json8.put("targetValue", lpd_target.get(0).get("artificial_insemination_count"));
                            json8.put("averageValue", lpd_avg.get(0).get("artificial_insemination_count"));

                            JSONObject json9 = new JSONObject();
                            json9.put("itemName", "人工授精配种比率(%)");
                            json9.put("itemValue", new BigDecimal(lpd.get(0).get("artificial_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json9.put("totalValue", new BigDecimal(lpd_total.get(0).get("artificial_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json9.put("targetValue", new BigDecimal(lpd_target.get(0).get("artificial_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json9.put("averageValue", new BigDecimal(lpd_avg.get(0).get("artificial_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json10 = new JSONObject();
                            json10.put("itemName", "自然授精配种比率(%)");
                            json10.put("itemValue", new BigDecimal(lpd.get(0).get("natural_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json10.put("totalValue", new BigDecimal(lpd_total.get(0).get("natural_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json10.put("targetValue", new BigDecimal(lpd_target.get(0).get("natural_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json10.put("averageValue", new BigDecimal(lpd_avg.get(0).get("natural_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json11 = new JSONObject();
                            json11.put("itemName", "其他方式授精配种头数");
                            json11.put("itemValue", lpd.get(0).get("other_insemination_count"));
                            json11.put("totalValue", lpd_total.get(0).get("other_insemination_count"));
                            json11.put("targetValue", lpd_target.get(0).get("other_insemination_count"));
                            json11.put("averageValue", lpd_avg.get(0).get("other_insemination_count"));

                            JSONObject json12 = new JSONObject();
                            json12.put("itemName", "其他方式授精配种比率(%)");
                            json12.put("itemValue", new BigDecimal(lpd.get(0).get("other_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json12.put("totalValue", new BigDecimal(lpd_total.get(0).get("other_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json12.put("targetValue", new BigDecimal(lpd_target.get(0).get("other_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json12.put("averageValue", new BigDecimal(lpd_avg.get(0).get("other_insemination_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json13 = new JSONObject();
                            json13.put("itemName", "同精配种头数");
                            json13.put("itemValue", lpd.get(0).get("same_semen_count"));
                            json13.put("totalValue", lpd_total.get(0).get("same_semen_count"));
                            json13.put("targetValue", lpd_target.get(0).get("same_semen_count"));
                            json13.put("averageValue", lpd_avg.get(0).get("same_semen_count"));

                            JSONObject json14 = new JSONObject();
                            json14.put("itemName", "同精配种比率(%)");
                            json14.put("itemValue", new BigDecimal(lpd.get(0).get("same_semen_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json14.put("totalValue", new BigDecimal(lpd_total.get(0).get("same_semen_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json14.put("targetValue", new BigDecimal(lpd_target.get(0).get("same_semen_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json14.put("averageValue", new BigDecimal(lpd_avg.get(0).get("same_semen_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json15 = new JSONObject();
                            json15.put("itemName", "进场日龄");
                            json15.put("itemValue", new BigDecimal(lpd.get(0).get("in_farm_dayage").toString()).divide(new BigDecimal(1), 1, 4));
                            json15.put("totalValue", new BigDecimal(lpd_total.get(0).get("in_farm_dayage").toString()).divide(new BigDecimal(1), 1, 4));
                            json15.put("targetValue", new BigDecimal(lpd_target.get(0).get("in_farm_dayage").toString()).divide(new BigDecimal(1), 1, 4));
                            json15.put("averageValue", new BigDecimal(lpd_avg.get(0).get("in_farm_dayage").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json16 = new JSONObject();
                            json16.put("itemName", "进群日龄");
                            json16.put("itemValue", new BigDecimal(lpd.get(0).get("in_group_dayage").toString()).divide(new BigDecimal(1), 1, 4));
                            json16.put("totalValue", new BigDecimal(lpd_total.get(0).get("in_group_dayage").toString()).divide(new BigDecimal(1), 1, 4));
                            json16.put("targetValue", new BigDecimal(lpd_target.get(0).get("in_group_dayage").toString()).divide(new BigDecimal(1), 1, 4));
                            json16.put("averageValue", new BigDecimal(lpd_avg.get(0).get("in_group_dayage").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json17 = new JSONObject();
                            json17.put("itemName", "第一次配种日龄");
                            json17.put("itemValue", new BigDecimal(lpd.get(0).get("first_dayage").toString()).divide(new BigDecimal(1), 1, 4));
                            json17.put("totalValue", new BigDecimal(lpd_total.get(0).get("first_dayage").toString()).divide(new BigDecimal(1), 1, 4));
                            json17.put("targetValue", new BigDecimal(lpd_target.get(0).get("first_dayage").toString()).divide(new BigDecimal(1), 1, 4));
                            json17.put("averageValue", new BigDecimal(lpd_avg.get(0).get("first_dayage").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json18 = new JSONObject();
                            json18.put("itemName", "进场后第一次配种母猪数");
                            json18.put("itemValue", lpd.get(0).get("first_female_count"));
                            json18.put("totalValue", lpd_total.get(0).get("first_female_count"));
                            json18.put("targetValue", lpd_target.get(0).get("first_female_count"));
                            json18.put("averageValue", lpd_avg.get(0).get("first_female_count"));

                            JSONObject json19 = new JSONObject();
                            json19.put("itemName", "进场至第一次配种时间间隔");
                            json19.put("itemValue", new BigDecimal(lpd.get(0).get("first_time_interval").toString()).divide(new BigDecimal(1), 1, 4));
                            json19.put("totalValue", new BigDecimal(lpd_total.get(0).get("first_time_interval").toString()).divide(new BigDecimal(1), 1, 4));
                            json19.put("targetValue", new BigDecimal(lpd_target.get(0).get("first_time_interval").toString()).divide(new BigDecimal(1), 1, 4));
                            json19.put("averageValue", new BigDecimal(lpd_avg.get(0).get("first_time_interval").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json20 = new JSONObject();
                            json20.put("itemName", "断奶至配种间距");
                            json20.put("itemValue", new BigDecimal(lpd.get(0).get("weaning_to_breeding").toString()).divide(new BigDecimal(1), 1, 4));
                            json20.put("totalValue", new BigDecimal(lpd_total.get(0).get("weaning_to_breeding").toString()).divide(new BigDecimal(1), 1, 4));
                            json20.put("targetValue", new BigDecimal(lpd_target.get(0).get("weaning_to_breeding").toString()).divide(new BigDecimal(1), 1, 4));
                            json20.put("averageValue", new BigDecimal(lpd_avg.get(0).get("weaning_to_breeding").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json21 = new JSONObject();
                            json21.put("itemName", "本期配种断奶7天内配种比率(%)");
                            json21.put("itemValue", new BigDecimal(lpd.get(0).get("seven_day_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json21.put("totalValue", new BigDecimal(lpd_total.get(0).get("seven_day_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json21.put("targetValue", new BigDecimal(lpd_target.get(0).get("seven_day_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json21.put("averageValue", new BigDecimal(lpd_avg.get(0).get("seven_day_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json22 = new JSONObject();
                            json22.put("itemName", "断奶后第一次配种母猪数");
                            json22.put("itemValue", lpd.get(0).get("after_weaning_female_count"));
                            json22.put("totalValue", lpd_total.get(0).get("after_weaning_female_count"));
                            json22.put("targetValue", lpd_target.get(0).get("after_weaning_female_count"));
                            json22.put("averageValue", lpd_avg.get(0).get("after_weaning_female_count"));

                            performance.put(json1);
                            performance.put(json2);
                            performance.put(json3);
                            performance.put(json4);
                            performance.put(json5);
                            performance.put(json6);
                            performance.put(json7);
                            performance.put(json8);
                            performance.put(json9);
                            performance.put(json10);
                            performance.put(json11);
                            performance.put(json12);
                            performance.put(json13);
                            performance.put(json14);
                            performance.put(json15);
                            performance.put(json16);
                            performance.put(json17);
                            performance.put(json18);
                            performance.put(json19);
                            performance.put(json20);
                            performance.put(json21);
                            performance.put(json22);
                            resJson.put("Performance", performance);
                            resJson.put("Result", "Success");
                        }else{
                            resJson.put("Result", "Fail");
                            resJson.put("Error", "暂无数据！");
                        }
                    } else if ("02".equals(ViewType)) {
                        List<PageData> lpd = repMobileService.getDeliveryData(pd);
                        List<PageData> lpd_total = repMobileService.getDeliveryDataTotal(pd);
                        List<PageData> lpd_avg = repMobileService.getDeliveryDataAvg(pd);
                        List<PageData> lpd_target = repMobileService.getDeliveryDataTarget(pd);
                        if (lpd.size() != 0 && lpd_total.size() != 0 && lpd_avg.size() != 0 && lpd_target.size() != 0) {
                            JSONObject json1 = new JSONObject();
                            json1.put("itemName", "分娩头数");
                            json1.put("itemValue", lpd.get(0).get("childbirth_count"));
                            json1.put("totalValue", lpd_total.get(0).get("childbirth_count"));
                            json1.put("targetValue", lpd_target.get(0).get("childbirth_count"));
                            json1.put("averageValue", lpd_avg.get(0).get("childbirth_count"));

                            JSONObject json2 = new JSONObject();
                            json2.put("itemName", "活仔数少于7的比率(%)");
                            json2.put("itemValue", new BigDecimal(lpd.get(0).get("life_less_seven_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("totalValue", new BigDecimal(lpd_total.get(0).get("life_less_seven_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("targetValue", new BigDecimal(lpd_target.get(0).get("life_less_seven_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("averageValue", new BigDecimal(lpd_avg.get(0).get("life_less_seven_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json3 = new JSONObject();
                            json3.put("itemName", "分娩母猪平均胎次");
                            json3.put("itemValue", new BigDecimal(lpd.get(0).get("avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json4 = new JSONObject();
                            json4.put("itemName", "平均窝总产仔数");
                            json4.put("itemValue", new BigDecimal(lpd.get(0).get("avg_childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json5 = new JSONObject();
                            json5.put("itemName", "总产仔数");
                            json5.put("itemValue", new BigDecimal(lpd.get(0).get("total_childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("totalValue", new BigDecimal(lpd_total.get(0).get("total_childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("targetValue", new BigDecimal(lpd_target.get(0).get("total_childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("averageValue", new BigDecimal(lpd_avg.get(0).get("total_childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json6 = new JSONObject();
                            json6.put("itemName", "窝均健仔数");
                            json6.put("itemValue", new BigDecimal(lpd.get(0).get("avg_health_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_health_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_health_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_health_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json7 = new JSONObject();
                            json7.put("itemName", "总健仔数");
                            json7.put("itemValue", lpd.get(0).get("toal_health_count"));
                            json7.put("totalValue", lpd_total.get(0).get("toal_health_count"));
                            json7.put("targetValue", lpd_target.get(0).get("toal_health_count"));
                            json7.put("averageValue", lpd_avg.get(0).get("toal_health_count"));

                            JSONObject json8 = new JSONObject();
                            json8.put("itemName", "窝均死胎数");
                            json8.put("itemValue", new BigDecimal(lpd.get(0).get("avg_dead_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json8.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_dead_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json8.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_dead_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json8.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_dead_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json9 = new JSONObject();
                            json9.put("itemName", "死胎率(%)");
                            json9.put("itemValue", new BigDecimal(lpd.get(0).get("dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json9.put("totalValue", new BigDecimal(lpd_total.get(0).get("dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json9.put("targetValue", new BigDecimal(lpd_target.get(0).get("dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json9.put("averageValue", new BigDecimal(lpd_avg.get(0).get("dead_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json10 = new JSONObject();
                            json10.put("itemName", "窝均木乃伊数");
                            json10.put("itemValue", new BigDecimal(lpd.get(0).get("avg_mummy_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json10.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_mummy_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json10.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_mummy_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json10.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_mummy_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json11 = new JSONObject();
                            json11.put("itemName", "木乃伊率(%)");
                            json11.put("itemValue", new BigDecimal(lpd.get(0).get("mummy_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json11.put("totalValue", new BigDecimal(lpd_total.get(0).get("mummy_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json11.put("targetValue", new BigDecimal(lpd_target.get(0).get("mummy_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json11.put("averageValue", new BigDecimal(lpd_avg.get(0).get("mummy_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json12 = new JSONObject();
                            json12.put("itemName", "分娩率(%)");
                            json12.put("itemValue", new BigDecimal(lpd.get(0).get("childbirth_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json12.put("totalValue", new BigDecimal(lpd_total.get(0).get("childbirth_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json12.put("targetValue", new BigDecimal(lpd_target.get(0).get("childbirth_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json12.put("averageValue", new BigDecimal(lpd_avg.get(0).get("childbirth_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json13 = new JSONObject();
                            json13.put("itemName", "调整分娩率(%)");
                            json13.put("itemValue", new BigDecimal(lpd.get(0).get("adjust_childbirth_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json13.put("totalValue", new BigDecimal(lpd_total.get(0).get("adjust_childbirth_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json13.put("targetValue", new BigDecimal(lpd_target.get(0).get("adjust_childbirth_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json13.put("averageValue", new BigDecimal(lpd_avg.get(0).get("adjust_childbirth_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json14 = new JSONObject();
                            json14.put("itemName", "平均怀孕天数");
                            json14.put("itemValue", new BigDecimal(lpd.get(0).get("avg_pregnant_day").toString()).divide(new BigDecimal(1), 1, 4));
                            json14.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_pregnant_day").toString()).divide(new BigDecimal(1), 1, 4));
                            json14.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_pregnant_day").toString()).divide(new BigDecimal(1), 1, 4));
                            json14.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_pregnant_day").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json15 = new JSONObject();
                            json15.put("itemName", "活仔平均出生重");
                            json15.put("itemValue", new BigDecimal(lpd.get(0).get("avg_life_weight").toString()).divide(new BigDecimal(1), 1, 4));
                            json15.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_life_weight").toString()).divide(new BigDecimal(1), 1, 4));
                            json15.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_life_weight").toString()).divide(new BigDecimal(1), 1, 4));
                            json15.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_life_weight").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json16 = new JSONObject();
                            json16.put("itemName", "分娩间距");
                            json16.put("itemValue", new BigDecimal(lpd.get(0).get("pregnant_interval").toString()).divide(new BigDecimal(1), 1, 4));
                            json16.put("totalValue", new BigDecimal(lpd_total.get(0).get("pregnant_interval").toString()).divide(new BigDecimal(1), 1, 4));
                            json16.put("targetValue", new BigDecimal(lpd_target.get(0).get("pregnant_interval").toString()).divide(new BigDecimal(1), 1, 4));
                            json16.put("averageValue", new BigDecimal(lpd_avg.get(0).get("pregnant_interval").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json17 = new JSONObject();
                            json17.put("itemName", "返情流产空怀总头数");
                            json17.put("itemValue", lpd.get(0).get("no_pregnant_count"));
                            json17.put("totalValue", lpd_total.get(0).get("no_pregnant_count"));
                            json17.put("targetValue", lpd_target.get(0).get("no_pregnant_count"));
                            json17.put("averageValue", lpd_avg.get(0).get("no_pregnant_count"));

                            JSONObject json18 = new JSONObject();
                            json18.put("itemName", "断奶前死亡率（按批次）");
                            json18.put("itemValue", new BigDecimal(lpd.get(0).get("bef_weaning_dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json18.put("totalValue", new BigDecimal(lpd_total.get(0).get("bef_weaning_dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json18.put("targetValue", new BigDecimal(lpd_target.get(0).get("bef_weaning_dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json18.put("averageValue", new BigDecimal(lpd_avg.get(0).get("bef_weaning_dead_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json19 = new JSONObject();
                            json19.put("itemName", "已分娩并且断奶母猪头数");
                            json19.put("itemValue", lpd.get(0).get("childbirth_weaning_female"));
                            json19.put("totalValue", lpd_total.get(0).get("childbirth_weaning_female"));
                            json19.put("targetValue", lpd_target.get(0).get("childbirth_weaning_female"));
                            json19.put("averageValue", lpd_avg.get(0).get("childbirth_weaning_female"));

                            JSONObject json20 = new JSONObject();
                            json20.put("itemName", "窝数/配种母猪/年");
                            json20.put("itemValue", new BigDecimal(lpd.get(0).get("childbirth_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json20.put("totalValue", new BigDecimal(lpd_total.get(0).get("childbirth_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json20.put("targetValue", new BigDecimal(lpd_target.get(0).get("childbirth_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json20.put("averageValue", new BigDecimal(lpd_avg.get(0).get("childbirth_year_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json21 = new JSONObject();
                            json21.put("itemName", "窝数/ 母猪 /年");
                            json21.put("itemValue", new BigDecimal(lpd.get(0).get("female_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json21.put("totalValue", new BigDecimal(lpd_total.get(0).get("female_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json21.put("targetValue", new BigDecimal(lpd_target.get(0).get("female_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json21.put("averageValue", new BigDecimal(lpd_avg.get(0).get("female_year_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json22 = new JSONObject();
                            json22.put("itemName", "健仔数/配种母猪/年");
                            json22.put("itemValue", new BigDecimal(lpd.get(0).get("health_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json22.put("totalValue", new BigDecimal(lpd_total.get(0).get("health_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json22.put("targetValue", new BigDecimal(lpd_target.get(0).get("health_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json22.put("averageValue", new BigDecimal(lpd_avg.get(0).get("health_year_count").toString()).divide(new BigDecimal(1), 1, 4));

                            performance.put(json1);
                            performance.put(json2);
                            performance.put(json3);
                            performance.put(json4);
                            performance.put(json5);
                            performance.put(json6);
                            performance.put(json7);
                            performance.put(json8);
                            performance.put(json9);
                            performance.put(json10);
                            performance.put(json11);
                            performance.put(json12);
                            performance.put(json13);
                            performance.put(json14);
                            performance.put(json15);
                            performance.put(json16);
                            performance.put(json17);
                            performance.put(json18);
                            performance.put(json19);
                            performance.put(json20);
                            performance.put(json21);
                            performance.put(json22);
                            resJson.put("Performance", performance);
                            resJson.put("Result", "Success");
                        } else {
                            resJson.put("Result", "Fail");
                            resJson.put("Error", "暂无数据！");
                        }
                    } else if ("03".equals(ViewType)) {
                        List<PageData> lpd = repMobileService.getCutFeedData(pd);
                        List<PageData> lpd_total = repMobileService.getCutFeedDataTotal(pd);
                        List<PageData> lpd_avg = repMobileService.getCutFeedDataAvg(pd);
                        List<PageData> lpd_target = repMobileService.getCutFeedDataTarget(pd);
                        if (lpd.size() != 0 && lpd_total.size() != 0 && lpd_avg.size() != 0 && lpd_target.size() != 0) {
                            JSONObject json1 = new JSONObject();
                            json1.put("itemName", "断奶母猪数");
                            json1.put("itemValue", lpd.get(0).get("weaning_female_count"));
                            json1.put("totalValue", lpd_total.get(0).get("weaning_female_count"));
                            json1.put("targetValue", lpd_target.get(0).get("weaning_female_count"));
                            json1.put("averageValue", lpd_avg.get(0).get("weaning_female_count"));

                            JSONObject json2 = new JSONObject();
                            json2.put("itemName", "断奶仔猪数");
                            json2.put("itemValue", new BigDecimal(lpd.get(0).get("weaning_piglet_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("totalValue", new BigDecimal(lpd_total.get(0).get("weaning_piglet_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("targetValue", new BigDecimal(lpd_target.get(0).get("weaning_piglet_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("averageValue", new BigDecimal(lpd_avg.get(0).get("weaning_piglet_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json3 = new JSONObject();
                            json3.put("itemName", "窝均断奶仔猪数");
                            json3.put("itemValue", new BigDecimal(lpd.get(0).get("avg_weaning_piglet").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_weaning_piglet").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_weaning_piglet").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_weaning_piglet").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json4 = new JSONObject();
                            json4.put("itemName", "母猪已断奶或整窝寄出");
                            json4.put("itemValue", lpd.get(0).get("leave_nest_female"));
                            json4.put("totalValue", lpd_total.get(0).get("leave_nest_female"));
                            json4.put("targetValue", lpd_target.get(0).get("leave_nest_female"));
                            json4.put("averageValue", lpd_avg.get(0).get("leave_nest_female"));

                            JSONObject json5 = new JSONObject();
                            json5.put("itemName", "每头母猪断奶仔猪头数");
                            json5.put("itemValue", new BigDecimal(lpd.get(0).get("female_weaned_piglet").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("totalValue", new BigDecimal(lpd_total.get(0).get("female_weaned_piglet").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("targetValue", new BigDecimal(lpd_target.get(0).get("female_weaned_piglet").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("averageValue", new BigDecimal(lpd_avg.get(0).get("female_weaned_piglet").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json6 = new JSONObject();
                            json6.put("itemName", "断奶母猪7天内再次配种比率");
                            json6.put("itemValue", new BigDecimal(lpd.get(0).get("repeat_seven_days_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("totalValue", new BigDecimal(lpd_total.get(0).get("repeat_seven_days_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("targetValue", new BigDecimal(lpd_target.get(0).get("repeat_seven_days_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("averageValue", new BigDecimal(lpd_avg.get(0).get("repeat_seven_days_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json7 = new JSONObject();
                            json7.put("itemName", "净寄养头数");
                            json7.put("itemValue", lpd.get(0).get("net_foster_count"));
                            json7.put("totalValue", lpd_total.get(0).get("net_foster_count"));
                            json7.put("targetValue", lpd_target.get(0).get("net_foster_count"));
                            json7.put("averageValue", lpd_avg.get(0).get("net_foster_count"));

                            JSONObject json8 = new JSONObject();
                            json8.put("itemName", "平均断奶日龄");
                            json8.put("itemValue", new BigDecimal(lpd.get(0).get("avg_weaning_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json8.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_weaning_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json8.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_weaning_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json8.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_weaning_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json9 = new JSONObject();
                            json9.put("itemName", "平均断奶体重");
                            json9.put("itemValue", new BigDecimal(lpd.get(0).get("avg_weaning_weiht").toString()).divide(new BigDecimal(1), 1, 4));
                            json9.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_weaning_weiht").toString()).divide(new BigDecimal(1), 1, 4));
                            json9.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_weaning_weiht").toString()).divide(new BigDecimal(1), 1, 4));
                            json9.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_weaning_weiht").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json10 = new JSONObject();
                            json10.put("itemName", "断奶前死亡仔猪数");
                            json10.put("itemValue", lpd.get(0).get("bef_weaned_dead_count"));
                            json10.put("totalValue", lpd_total.get(0).get("bef_weaned_dead_count"));
                            json10.put("targetValue", lpd_target.get(0).get("bef_weaned_dead_count"));
                            json10.put("averageValue", lpd_avg.get(0).get("bef_weaned_dead_count"));

                            JSONObject json11 = new JSONObject();
                            json11.put("itemName", "断奶前死亡率(%)");
                            json11.put("itemValue", new BigDecimal(lpd.get(0).get("bef_weaned_dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json11.put("totalValue", new BigDecimal(lpd_total.get(0).get("bef_weaned_dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json11.put("targetValue", new BigDecimal(lpd_target.get(0).get("bef_weaned_dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json11.put("averageValue", new BigDecimal(lpd_avg.get(0).get("bef_weaned_dead_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json12 = new JSONObject();
                            json12.put("itemName", "断奶仔猪数/配种母猪/年");
                            json12.put("itemValue", new BigDecimal(lpd.get(0).get("weaned_childbirth").toString()).divide(new BigDecimal(1), 1, 4));
                            json12.put("totalValue", new BigDecimal(lpd_total.get(0).get("weaned_childbirth").toString()).divide(new BigDecimal(1), 1, 4));
                            json12.put("targetValue", new BigDecimal(lpd_target.get(0).get("weaned_childbirth").toString()).divide(new BigDecimal(1), 1, 4));
                            json12.put("averageValue", new BigDecimal(lpd_avg.get(0).get("weaned_childbirth").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json13 = new JSONObject();
                            json13.put("itemName", "断奶仔猪数/母猪/年");
                            json13.put("itemValue", new BigDecimal(lpd.get(0).get("weaned_female").toString()).divide(new BigDecimal(1), 1, 4));
                            json13.put("totalValue", new BigDecimal(lpd_total.get(0).get("weaned_female").toString()).divide(new BigDecimal(1), 1, 4));
                            json13.put("targetValue", new BigDecimal(lpd_target.get(0).get("weaned_female").toString()).divide(new BigDecimal(1), 1, 4));
                            json13.put("averageValue", new BigDecimal(lpd_avg.get(0).get("weaned_female").toString()).divide(new BigDecimal(1), 1, 4));

                            performance.put(json1);
                            performance.put(json2);
                            performance.put(json3);
                            performance.put(json4);
                            performance.put(json5);
                            performance.put(json6);
                            performance.put(json7);
                            performance.put(json8);
                            performance.put(json9);
                            performance.put(json10);
                            performance.put(json11);
                            performance.put(json12);
                            performance.put(json13);
                            resJson.put("Performance", performance);
                            resJson.put("Result", "Success");
                        } else {
                            resJson.put("Result", "Fail");
                            resJson.put("Error", "暂无数据！");
                        }
                    } else if ("04".equals(ViewType)) {
                        List<PageData> lpd = repMobileService.getBreedData(pd);
                        List<PageData> lpd_total = repMobileService.getBreedDataTotal(pd);
                        List<PageData> lpd_avg = repMobileService.getBreedDataAvg(pd);
                        List<PageData> lpd_target = repMobileService.getBreedDataTarget(pd);
                        if (lpd.size() != 0 && lpd_total.size() != 0 && lpd_avg.size() != 0 && lpd_target.size() != 0) {
                            JSONObject json1 = new JSONObject();
                            json1.put("itemName", "期末母猪存栏头数");
                            json1.put("itemValue", lpd.get(0).get("female_count"));
                            json1.put("totalValue", lpd_total.get(0).get("female_count"));
                            json1.put("targetValue", lpd_target.get(0).get("female_count"));
                            json1.put("averageValue", lpd_avg.get(0).get("female_count"));

                            JSONObject json2 = new JSONObject();
                            json2.put("itemName", "期末未配种母猪存栏头数");
                            json2.put("itemValue", lpd.get(0).get("no_breeding_female_count"));
                            json2.put("totalValue", lpd_total.get(0).get("no_breeding_female_count"));
                            json2.put("targetValue", lpd_target.get(0).get("no_breeding_female_count"));
                            json2.put("averageValue", lpd_avg.get(0).get("no_breeding_female_count"));

                            JSONObject json3 = new JSONObject();
                            json3.put("itemName", "期末配种母猪存栏头数");
                            json3.put("itemValue", lpd.get(0).get("breeding_female_count"));
                            json3.put("totalValue", lpd_total.get(0).get("breeding_female_count"));
                            json3.put("targetValue", lpd_target.get(0).get("breeding_female_count"));
                            json3.put("averageValue", lpd_avg.get(0).get("breeding_female_count"));

                            JSONObject json4 = new JSONObject();
                            json4.put("itemName", "期末公猪存栏头数");
                            json4.put("itemValue", lpd.get(0).get("male_count"));
                            json4.put("totalValue", lpd_total.get(0).get("male_count"));
                            json4.put("targetValue", lpd_target.get(0).get("male_count"));
                            json4.put("averageValue", lpd_avg.get(0).get("male_count"));

                            JSONObject json5 = new JSONObject();
                            json5.put("itemName", "平均母猪存栏");
                            json5.put("itemValue", new BigDecimal(lpd.get(0).get("avg_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_female_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json6 = new JSONObject();
                            json6.put("itemName", "未配种母猪平均存栏");
                            json6.put("itemValue", new BigDecimal(lpd.get(0).get("avg_no_breeding_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_no_breeding_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_no_breeding_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_no_breeding_female_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json7 = new JSONObject();
                            json7.put("itemName", "配种母猪平均存栏");
                            json7.put("itemValue", new BigDecimal(lpd.get(0).get("avg_breeding_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json7.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_breeding_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json7.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_breeding_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json7.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_breeding_female_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json8 = new JSONObject();
                            json8.put("itemName", "平均胎次");
                            json8.put("itemValue", new BigDecimal(lpd.get(0).get("avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));
                            json8.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));
                            json8.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));
                            json8.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json9 = new JSONObject();
                            json9.put("itemName", "进场母猪头数");
                            json9.put("itemValue", lpd.get(0).get("in_farm_female_count"));
                            json9.put("totalValue", lpd_total.get(0).get("in_farm_female_count"));
                            json9.put("targetValue", lpd_target.get(0).get("in_farm_female_count"));
                            json9.put("averageValue", lpd_avg.get(0).get("in_farm_female_count"));

                            JSONObject json10 = new JSONObject();
                            json10.put("itemName", "迁入母猪头数");
                            json10.put("itemValue", lpd.get(0).get("head_female_count"));
                            json10.put("totalValue", lpd_total.get(0).get("head_female_count"));
                            json10.put("targetValue", lpd_target.get(0).get("head_female_count"));
                            json10.put("averageValue", lpd_avg.get(0).get("head_female_count"));

                            JSONObject json11 = new JSONObject();
                            json11.put("itemName", "总计增加母猪头数");
                            json11.put("itemValue", lpd.get(0).get("total_add_female_count"));
                            json11.put("totalValue", lpd_total.get(0).get("total_add_female_count"));
                            json11.put("targetValue", lpd_target.get(0).get("total_add_female_count"));
                            json11.put("averageValue", lpd_avg.get(0).get("total_add_female_count"));

                            JSONObject json12 = new JSONObject();
                            json12.put("itemName", "淘汰母猪");
                            json12.put("itemValue", lpd.get(0).get("eliminate_female_count"));
                            json12.put("totalValue", lpd_total.get(0).get("eliminate_female_count"));
                            json12.put("targetValue", lpd_target.get(0).get("eliminate_female_count"));
                            json12.put("averageValue", lpd_avg.get(0).get("eliminate_female_count"));

                            JSONObject json13 = new JSONObject();
                            json13.put("itemName", "母猪死亡头数");
                            json13.put("itemValue", lpd.get(0).get("dead_female_count"));
                            json13.put("totalValue", lpd_total.get(0).get("dead_female_count"));
                            json13.put("targetValue", lpd_target.get(0).get("dead_female_count"));
                            json13.put("averageValue", lpd_avg.get(0).get("dead_female_count"));

                            JSONObject json14 = new JSONObject();
                            json14.put("itemName", "迁出母猪头数");
                            json14.put("itemValue", lpd.get(0).get("out_female_count"));
                            json14.put("totalValue", lpd_total.get(0).get("out_female_count"));
                            json14.put("targetValue", lpd_target.get(0).get("out_female_count"));
                            json14.put("averageValue", lpd_avg.get(0).get("out_female_count"));

                            JSONObject json15 = new JSONObject();
                            json15.put("itemName", "出售母猪头数");
                            json15.put("itemValue", lpd.get(0).get("sale_female_count"));
                            json15.put("totalValue", lpd_total.get(0).get("sale_female_count"));
                            json15.put("targetValue", lpd_target.get(0).get("sale_female_count"));
                            json15.put("averageValue", lpd_avg.get(0).get("sale_female_count"));

                            JSONObject json16 = new JSONObject();
                            json16.put("itemName", "更新率(%)");
                            json16.put("itemValue", new BigDecimal(lpd.get(0).get("update_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json16.put("totalValue", new BigDecimal(lpd_total.get(0).get("update_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json16.put("targetValue", new BigDecimal(lpd_target.get(0).get("update_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json16.put("averageValue", new BigDecimal(lpd_avg.get(0).get("update_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json17 = new JSONObject();
                            json17.put("itemName", "总计减少母猪头数");
                            json17.put("itemValue", lpd.get(0).get("total_lessen_female_count"));
                            json17.put("totalValue", lpd_total.get(0).get("total_lessen_female_count"));
                            json17.put("targetValue", lpd_target.get(0).get("total_lessen_female_count"));
                            json17.put("averageValue", lpd_avg.get(0).get("total_lessen_female_count"));

                            JSONObject json18 = new JSONObject();
                            json18.put("itemName", "死亡率(%)");
                            json18.put("itemValue", new BigDecimal(lpd.get(0).get("dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json18.put("totalValue", new BigDecimal(lpd_total.get(0).get("dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json18.put("targetValue", new BigDecimal(lpd_target.get(0).get("dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json18.put("averageValue", new BigDecimal(lpd_avg.get(0).get("dead_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json19 = new JSONObject();
                            json19.put("itemName", "淘汰率(%)");
                            json19.put("itemValue", new BigDecimal(lpd.get(0).get("eliminate_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json19.put("totalValue", new BigDecimal(lpd_total.get(0).get("eliminate_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json19.put("targetValue", new BigDecimal(lpd_target.get(0).get("eliminate_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json19.put("averageValue", new BigDecimal(lpd_avg.get(0).get("eliminate_rate").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json20 = new JSONObject();
                            json20.put("itemName", "淘汰母猪平均胎次");
                            json20.put("itemValue", new BigDecimal(lpd.get(0).get("eliminate_female_avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));
                            json20.put("totalValue", new BigDecimal(lpd_total.get(0).get("eliminate_female_avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));
                            json20.put("targetValue", new BigDecimal(lpd_target.get(0).get("eliminate_female_avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));
                            json20.put("averageValue", new BigDecimal(lpd_avg.get(0).get("eliminate_female_avg_pregnant_no").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json21 = new JSONObject();
                            json21.put("itemName", "平均非生产天数/ 母猪 /年");
                            json21.put("itemValue", new BigDecimal(lpd.get(0).get("avg_procreate_day").toString()).divide(new BigDecimal(1), 1, 4));
                            json21.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_procreate_day").toString()).divide(new BigDecimal(1), 1, 4));
                            json21.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_procreate_day").toString()).divide(new BigDecimal(1), 1, 4));
                            json21.put("averageValue", new BigDecimal(lpd_avg.get(0).get("avg_procreate_day").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json22 = new JSONObject();
                            json22.put("itemName", "死淘猪一生平均提供活仔数");
                            json22.put("itemValue", new BigDecimal(lpd.get(0).get("eliminate_procreate_life_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json22.put("totalValue", new BigDecimal(lpd_total.get(0).get("eliminate_procreate_life_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json22.put("targetValue", new BigDecimal(lpd_target.get(0).get("eliminate_procreate_life_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json22.put("averageValue", new BigDecimal(lpd_avg.get(0).get("eliminate_procreate_life_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json23 = new JSONObject();
                            json23.put("itemName", "死淘猪一生提供断奶猪个数");
                            json23.put("itemValue", lpd.get(0).get("eliminate_procreate_breeding_count"));
                            json23.put("totalValue", lpd_total.get(0).get("eliminate_procreate_breeding_count"));
                            json23.put("targetValue", lpd_target.get(0).get("eliminate_procreate_breeding_count"));
                            json23.put("averageValue", lpd_avg.get(0).get("eliminate_procreate_breeding_count"));

                            JSONObject json24 = new JSONObject();
                            json24.put("itemName", "期末后备舍母猪存栏头数");
                            json24.put("itemValue", new BigDecimal(lpd.get(0).get("bak_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json24.put("totalValue", new BigDecimal(lpd_total.get(0).get("bak_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json24.put("targetValue", new BigDecimal(lpd_target.get(0).get("bak_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json24.put("averageValue", new BigDecimal(lpd_avg.get(0).get("bak_female_count").toString()).divide(new BigDecimal(1), 1, 4));

                            JSONObject json25 = new JSONObject();
                            json25.put("itemName", "期末后备舍在产母猪存栏头数");
                            json25.put("itemValue", lpd.get(0).get("bak_procreate_female_count"));
                            json25.put("totalValue", lpd_total.get(0).get("bak_procreate_female_count"));
                            json25.put("targetValue", lpd_target.get(0).get("bak_procreate_female_count"));
                            json25.put("averageValue", lpd_avg.get(0).get("bak_procreate_female_count"));

                            JSONObject json26 = new JSONObject();
                            json26.put("itemName", "期末后备舍进群母猪存栏头数");
                            json26.put("itemValue", lpd.get(0).get("bak_in_group_female_count"));
                            json26.put("totalValue", lpd_total.get(0).get("bak_in_group_female_count"));
                            json26.put("targetValue", lpd_target.get(0).get("bak_in_group_female_count"));
                            json26.put("averageValue", lpd_avg.get(0).get("bak_in_group_female_count"));

                            JSONObject json27 = new JSONObject();
                            json27.put("itemName", "期末配种舍母猪存栏头数");
                            json27.put("itemValue", lpd.get(0).get("bh_female_count"));
                            json27.put("totalValue", lpd_total.get(0).get("bh_female_count"));
                            json27.put("targetValue", lpd_target.get(0).get("bh_female_count"));
                            json27.put("averageValue", lpd_avg.get(0).get("bh_female_count"));

                            JSONObject json28 = new JSONObject();
                            json28.put("itemName", "期末配种舍在产母猪存栏头数");
                            json28.put("itemValue", lpd.get(0).get("bh_procreate_female_count"));
                            json28.put("totalValue", lpd_total.get(0).get("bh_procreate_female_count"));
                            json28.put("targetValue", lpd_target.get(0).get("bh_procreate_female_count"));
                            json28.put("averageValue", lpd_avg.get(0).get("bh_procreate_female_count"));

                            JSONObject json29 = new JSONObject();
                            json29.put("itemName", "期末配种舍进群母猪存栏头数");
                            json29.put("itemValue", lpd.get(0).get("bh_in_group_female_count"));
                            json29.put("totalValue", lpd_total.get(0).get("bh_in_group_female_count"));
                            json29.put("targetValue", lpd_target.get(0).get("bh_in_group_female_count"));
                            json29.put("averageValue", lpd_avg.get(0).get("bh_in_group_female_count"));

                            JSONObject json30 = new JSONObject();
                            json30.put("itemName", "期末分娩舍仔猪存栏头数");
                            json30.put("itemValue", lpd.get(0).get("ch_piglet_count"));
                            json30.put("totalValue", lpd_total.get(0).get("ch_piglet_count"));
                            json30.put("targetValue", lpd_target.get(0).get("ch_piglet_count"));
                            json30.put("averageValue", lpd_avg.get(0).get("ch_piglet_count"));

                            JSONObject json31 = new JSONObject();
                            json31.put("itemName", "期末育成舍仔猪存栏头数");
                            json31.put("itemValue", lpd.get(0).get("gh_piglet_count"));
                            json31.put("totalValue", lpd_total.get(0).get("gh_piglet_count"));
                            json31.put("targetValue", lpd_target.get(0).get("gh_piglet_count"));
                            json31.put("averageValue", lpd_avg.get(0).get("gh_piglet_count"));

                            JSONObject json32 = new JSONObject();
                            json32.put("itemName", "期末育肥舍仔猪存栏头数");
                            json32.put("itemValue", lpd.get(0).get("fh_piglet_count"));
                            json32.put("totalValue", lpd_total.get(0).get("fh_piglet_count"));
                            json32.put("targetValue", lpd_target.get(0).get("fh_piglet_count"));
                            json32.put("averageValue", lpd_avg.get(0).get("fh_piglet_count"));

                            JSONObject json33 = new JSONObject();
                            json33.put("itemName", "期末选种舍仔猪存栏头数");
                            json33.put("itemValue", lpd.get(0).get("sh_piglet_count"));
                            json33.put("totalValue", lpd_total.get(0).get("sh_piglet_count"));
                            json33.put("targetValue", lpd_target.get(0).get("sh_piglet_count"));
                            json33.put("averageValue", lpd_avg.get(0).get("sh_piglet_count"));

                            JSONObject json34 = new JSONObject();
                            json34.put("itemName", "本期妊娠状态母猪头数");
                            json34.put("itemValue", lpd.get(0).get("gestation_female_count"));
                            json34.put("totalValue", lpd_total.get(0).get("gestation_female_count"));
                            json34.put("targetValue", lpd_target.get(0).get("gestation_female_count"));
                            json34.put("averageValue", lpd_avg.get(0).get("gestation_female_count"));

                            JSONObject json35 = new JSONObject();
                            json35.put("itemName", "本期分娩状态母猪头数");
                            json35.put("itemValue", lpd.get(0).get("childbirth_female_count"));
                            json35.put("totalValue", lpd_total.get(0).get("childbirth_female_count"));
                            json35.put("targetValue", lpd_target.get(0).get("childbirth_female_count"));
                            json35.put("averageValue", lpd_avg.get(0).get("childbirth_female_count"));

                            JSONObject json36 = new JSONObject();
                            json36.put("itemName", "本期断奶状态母猪头数");
                            json36.put("itemValue", lpd.get(0).get("weaning_female_count"));
                            json36.put("totalValue", lpd_total.get(0).get("weaning_female_count"));
                            json36.put("targetValue", lpd_target.get(0).get("weaning_female_count"));
                            json36.put("averageValue", lpd_avg.get(0).get("weaning_female_count"));

                            JSONObject json37 = new JSONObject();
                            json37.put("itemName", "本期反情状态母猪头数");
                            json37.put("itemValue", lpd.get(0).get("no_gestation_female_count"));
                            json37.put("totalValue", lpd_total.get(0).get("no_gestation_female_count"));
                            json37.put("targetValue", lpd_target.get(0).get("no_gestation_female_count"));
                            json37.put("averageValue", lpd_avg.get(0).get("no_gestation_female_count"));

                            JSONObject json38 = new JSONObject();
                            json38.put("itemName", "本期转出断奶仔猪头数");
                            json38.put("itemValue", lpd.get(0).get("out_piglet_count"));
                            json38.put("totalValue", lpd_total.get(0).get("out_piglet_count"));
                            json38.put("targetValue", lpd_target.get(0).get("out_piglet_count"));
                            json38.put("averageValue", lpd_avg.get(0).get("out_piglet_count"));

                            performance.put(json1);
                            performance.put(json2);
                            performance.put(json3);
                            performance.put(json4);
                            performance.put(json5);
                            performance.put(json6);
                            performance.put(json7);
                            performance.put(json8);
                            performance.put(json9);
                            performance.put(json10);
                            performance.put(json11);
                            performance.put(json12);
                            performance.put(json13);
                            performance.put(json14);
                            performance.put(json15);
                            performance.put(json16);
                            performance.put(json17);
                            performance.put(json18);
                            performance.put(json19);
                            performance.put(json20);
                            performance.put(json21);
                            performance.put(json22);
                            performance.put(json23);
                            performance.put(json24);
                            performance.put(json25);
                            performance.put(json26);
                            performance.put(json27);
                            performance.put(json28);
                            performance.put(json29);
                            performance.put(json30);
                            performance.put(json31);
                            performance.put(json32);
                            performance.put(json33);
                            performance.put(json34);
                            performance.put(json35);
                            performance.put(json36);
                            performance.put(json37);
                            performance.put(json38);
                            resJson.put("Performance", performance);
                            resJson.put("Result", "Success");
                        } else {
                            resJson.put("Result", "Fail");
                            resJson.put("Error", "暂无数据！");
                        }
                    }
                } else if ("02".equals(ReportType)) {
                    if ("01".equals(ViewType)){
                        List<PageData> lpd = repMobileService.getItemData(pd);
                        List<PageData> lpd_total = repMobileService.getItemDataTotal(pd);
                        List<PageData> lpd_target = repMobileService.getItemDataTarget(pd);
                        if (lpd.size() != 0 && lpd_total.size() != 0 && lpd_target.size() != 0) {
                            JSONObject json1 = new JSONObject();
                            json1.put("itemName", "总配种头数");
                            json1.put("itemValue", lpd.get(0).get("total_count"));
                            json1.put("totalValue", lpd_total.get(0).get("total_count"));
                            json1.put("targetValue", lpd_target.get(0).get("total_count"));
                            json1.put("reachRate", new BigDecimal(lpd_target.get(0).get("total_count").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("total_count").toString()).divide(new BigDecimal(lpd_target.get(0).get("total_count").toString()), 3, 4).multiply(new BigDecimal(100)));

                            JSONObject json2 = new JSONObject();
                            json2.put("itemName", "首次配种头数");
                            json2.put("itemValue", new BigDecimal(lpd.get(0).get("first_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("totalValue", new BigDecimal(lpd_total.get(0).get("first_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("targetValue", new BigDecimal(lpd_target.get(0).get("first_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("reachRate", new BigDecimal(lpd_target.get(0).get("first_count").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("first_count").toString()).divide(new BigDecimal(lpd_target.get(0).get("first_count").toString()), 3, 4).multiply(new BigDecimal(100)));
                            performance.put(json1);
                            performance.put(json2);
                            resJson.put("Result", "Success");
                            resJson.put("Performance", performance);
                        } else {
                            resJson.put("Result", "Fail");
                            resJson.put("Error", "暂无数据！");
                        }
                    } else if ("02".equals(ViewType)){
                        List<PageData> lpd = repMobileService.getDeliveryData(pd);
                        List<PageData> lpd_total = repMobileService.getDeliveryDataTotal(pd);
                        List<PageData> lpd_target = repMobileService.getDeliveryDataTarget(pd);
                        if (lpd.size() != 0 && lpd_total.size() != 0 && lpd_target.size() != 0) {
                            JSONObject json1 = new JSONObject();
                            json1.put("itemName", "分娩头数");
                            json1.put("itemValue", new BigDecimal(lpd.get(0).get("childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json1.put("totalValue", new BigDecimal(lpd_total.get(0).get("childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json1.put("targetValue", new BigDecimal(lpd_target.get(0).get("childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json1.put("reachRate", new BigDecimal(lpd_target.get(0).get("childbirth_count").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("childbirth_count").toString()).divide(new BigDecimal(lpd_target.get(0).get("childbirth_count").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json2 = new JSONObject();
                            json2.put("itemName", "分娩窝数");
                            json2.put("itemValue", new BigDecimal(lpd.get(0).get("childbirth_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("totalValue", new BigDecimal(lpd_total.get(0).get("childbirth_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("targetValue", new BigDecimal(lpd_target.get(0).get("childbirth_year_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("reachRate", new BigDecimal(lpd_target.get(0).get("childbirth_year_count").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("childbirth_year_count").toString()).divide(new BigDecimal(lpd_target.get(0).get("childbirth_year_count").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json3 = new JSONObject();
                            json3.put("itemName", "活仔平均出生重");
                            json3.put("itemValue", new BigDecimal(lpd.get(0).get("avg_life_weight").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_life_weight").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_life_weight").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("reachRate", new BigDecimal(lpd_target.get(0).get("avg_life_weight").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("avg_life_weight").toString()).divide(new BigDecimal(lpd_target.get(0).get("avg_life_weight").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json4 = new JSONObject();
                            json4.put("itemName", "平均窝总产仔数");
                            json4.put("itemValue", new BigDecimal(lpd.get(0).get("avg_childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_childbirth_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("reachRate", new BigDecimal(lpd_target.get(0).get("avg_childbirth_count").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("avg_childbirth_count").toString()).divide(new BigDecimal(lpd_target.get(0).get("avg_childbirth_count").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json5 = new JSONObject();
                            json5.put("itemName", "窝均健仔数");
                            json5.put("itemValue", new BigDecimal(lpd.get(0).get("avg_health_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_health_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_health_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("reachRate", new BigDecimal(lpd_target.get(0).get("avg_health_count").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("avg_health_count").toString()).divide(new BigDecimal(lpd_target.get(0).get("avg_health_count").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json6  = new JSONObject();
                            json6.put("itemName", "分娩率(%)");
                            json6.put("itemValue", new BigDecimal(lpd.get(0).get("childbirth_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("totalValue", new BigDecimal(lpd_total.get(0).get("childbirth_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("targetValue", new BigDecimal(lpd_target.get(0).get("childbirth_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("reachRate", new BigDecimal(lpd_target.get(0).get("childbirth_rate").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("childbirth_year_count").toString()).divide(new BigDecimal(lpd_target.get(0).get("childbirth_year_count").toString()), 3, 4).multiply(new BigDecimal(100)));
                            performance.put(json1);
                            performance.put(json2);
                            performance.put(json3);
                            performance.put(json4);
                            performance.put(json5);
                            performance.put(json6);
                            resJson.put("Result", "Success");
                            resJson.put("Performance", performance);
                        } else {
                            resJson.put("Result", "Fail");
                            resJson.put("Error", "暂无数据！");
                        }

                    } else if ("03".equals(ViewType)){
                        List<PageData> lpd = repMobileService.getCutFeedData(pd);
                        List<PageData> lpd_total = repMobileService.getCutFeedDataTotal(pd);
                        List<PageData> lpd_target = repMobileService.getCutFeedDataTarget(pd);
                        if (lpd.size() != 0 && lpd_total.size() != 0 && lpd_target.size() != 0) {
                            JSONObject json1 = new JSONObject();
                            json1.put("itemName", "断奶仔猪数");
                            json1.put("itemValue", new BigDecimal(lpd.get(0).get("weaning_piglet_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json1.put("totalValue", new BigDecimal(lpd_total.get(0).get("weaning_piglet_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json1.put("targetValue", new BigDecimal(lpd_target.get(0).get("weaning_piglet_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json1.put("reachRate", new BigDecimal(lpd_target.get(0).get("weaning_piglet_count").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("weaning_piglet_count").toString()).divide(new BigDecimal(lpd_target.get(0).get("weaning_piglet_count").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json2 = new JSONObject();
                            json2.put("itemName", "窝均断奶仔猪数");
                            json2.put("itemValue", new BigDecimal(lpd.get(0).get("avg_weaning_piglet").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_weaning_piglet").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_weaning_piglet").toString()).divide(new BigDecimal(1), 1, 4));
                            json2.put("reachRate", new BigDecimal(lpd_target.get(0).get("avg_weaning_piglet").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("avg_weaning_piglet").toString()).divide(new BigDecimal(lpd_target.get(0).get("avg_weaning_piglet").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json3 = new JSONObject();
                            json3.put("itemName", "平均断奶体重");
                            json3.put("itemValue", new BigDecimal(lpd.get(0).get("avg_weaning_weiht").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_weaning_weiht").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_weaning_weiht").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("reachRate", new BigDecimal(lpd_target.get(0).get("avg_weaning_weiht").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("avg_weaning_weiht").toString()).divide(new BigDecimal(lpd_target.get(0).get("avg_weaning_weiht").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json4 = new JSONObject();
                            json4.put("itemName", "平均断奶日龄");
                            json4.put("itemValue", new BigDecimal(lpd.get(0).get("avg_weaning_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("totalValue", new BigDecimal(lpd_total.get(0).get("avg_weaning_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("targetValue", new BigDecimal(lpd_target.get(0).get("avg_weaning_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("reachRate", new BigDecimal(lpd_target.get(0).get("avg_weaning_rate").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("avg_weaning_rate").toString()).divide(new BigDecimal(lpd_target.get(0).get("avg_weaning_rate").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json5 = new JSONObject();
                            json5.put("itemName", "断奶前死亡率(%)");
                            json5.put("itemValue", new BigDecimal(lpd.get(0).get("bef_weaned_dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("totalValue", new BigDecimal(lpd_total.get(0).get("bef_weaned_dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("targetValue", new BigDecimal(lpd_target.get(0).get("bef_weaned_dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("reachRate", new BigDecimal(lpd_target.get(0).get("bef_weaned_dead_rate").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("bef_weaned_dead_rate").toString()).divide(new BigDecimal(lpd_target.get(0).get("bef_weaned_dead_rate").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json6 = new JSONObject();
                            json6.put("itemName", "PSY");
                            json6.put("itemValue", new BigDecimal(lpd.get(0).get("weaned_female").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("totalValue", new BigDecimal(lpd_total.get(0).get("weaned_female").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("targetValue", new BigDecimal(lpd_target.get(0).get("weaned_female").toString()).divide(new BigDecimal(1), 1, 4));
                            json6.put("reachRate", new BigDecimal(lpd_target.get(0).get("weaned_female").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("weaned_female").toString()).divide(new BigDecimal(lpd_target.get(0).get("weaned_female").toString()), 3, 4).multiply(new BigDecimal(100)));
                            performance.put(json1);
                            performance.put(json2);
                            performance.put(json3);
                            performance.put(json4);
                            performance.put(json5);
                            performance.put(json6);
                            resJson.put("Result", "Success");
                            resJson.put("Performance", performance);
                        } else {
                            resJson.put("Result", "Fail");
                            resJson.put("Error", "暂无数据！");
                        }

                    } else if ("04".equals(ViewType)){
                        List<PageData> lpd = repMobileService.getBreedData(pd);
                        List<PageData> lpd_total = repMobileService.getBreedDataTotal(pd);
                        List<PageData> lpd_target = repMobileService.getBreedDataTarget(pd);
                        if (lpd.size() != 0 && lpd_total.size() != 0 && lpd_target.size() != 0) {
                            JSONObject json1 = new JSONObject();
                            json1.put("itemName", "期末母猪存栏头数");
                            json1.put("itemValue", lpd.get(0).get("female_count"));
                            json1.put("totalValue", lpd_total.get(0).get("female_count"));
                            json1.put("targetValue", lpd_target.get(0).get("female_count"));
                            json1.put("reachRate", new BigDecimal(lpd_target.get(0).get("female_count").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("female_count").toString()).divide(new BigDecimal(lpd_target.get(0).get("female_count").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json2 = new JSONObject();
                            json2.put("itemName", "本期分娩状态母猪头数");
                            json2.put("itemValue", lpd.get(0).get("childbirth_female_count"));
                            json2.put("totalValue", lpd_total.get(0).get("childbirth_female_count"));
                            json2.put("targetValue", lpd_target.get(0).get("childbirth_female_count"));
                            json2.put("reachRate", new BigDecimal(lpd_target.get(0).get("childbirth_female_count").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("childbirth_female_count").toString()).divide(new BigDecimal(lpd_target.get(0).get("childbirth_female_count").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json3 = new JSONObject();
                            json3.put("itemName", "期末后备舍母猪存栏头数");
                            json3.put("itemValue", new BigDecimal(lpd.get(0).get("bak_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("totalValue", new BigDecimal(lpd_total.get(0).get("bak_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("targetValue", new BigDecimal(lpd_target.get(0).get("bak_female_count").toString()).divide(new BigDecimal(1), 1, 4));
                            json3.put("reachRate", new BigDecimal(lpd_target.get(0).get("bak_female_count").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("bak_female_count").toString()).divide(new BigDecimal(lpd_target.get(0).get("bak_female_count").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json4 = new JSONObject();
                            json4.put("itemName", "死亡率(%)");
                            json4.put("itemValue", new BigDecimal(lpd.get(0).get("dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("totalValue", new BigDecimal(lpd_total.get(0).get("dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("targetValue", new BigDecimal(lpd_target.get(0).get("dead_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json4.put("reachRate", new BigDecimal(lpd_target.get(0).get("dead_rate").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("dead_rate").toString()).divide(new BigDecimal(lpd_target.get(0).get("dead_rate").toString()), 3, 4).multiply(new BigDecimal(100)));
                            JSONObject json5 = new JSONObject();
                            json5.put("itemName", "淘汰率(%)");
                            json5.put("itemValue", new BigDecimal(lpd.get(0).get("eliminate_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("totalValue", new BigDecimal(lpd_total.get(0).get("eliminate_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("targetValue", new BigDecimal(lpd_target.get(0).get("eliminate_rate").toString()).divide(new BigDecimal(1), 1, 4));
                            json5.put("reachRate", new BigDecimal(lpd_target.get(0).get("eliminate_rate").toString()).compareTo(new BigDecimal(0)) == 0 ? "-" : new BigDecimal(lpd.get(0).get("eliminate_rate").toString()).divide(new BigDecimal(lpd_target.get(0).get("eliminate_rate").toString()), 3, 4).multiply(new BigDecimal(100)));
                            performance.put(json1);
                            performance.put(json2);
                            performance.put(json3);
                            performance.put(json4);
                            performance.put(json5);
                            resJson.put("Result", "Success");
                            resJson.put("Performance", performance);
                        } else {
                            resJson.put("Result", "Fail");
                            resJson.put("Error", "暂无数据！");
                        }

                    }
                }
                pd.put("farmId", FarmId);
            }
            dealRes = Constants.RESULT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (JSONException ee) {
                ee.printStackTrace();
            }
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    public static void main(String[] args) throws Exception{
        PageData json = new PageData();
        json.put("aaaa", "bbbb");
        String ss = "2017(05)";
        String[] aa = ss.replaceAll("\\)", "").split("\\(");
        System.out.println("now excute here:" + aa[1]);
    }
}
