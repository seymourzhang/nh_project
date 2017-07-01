package com.nh.ifarm.analyze.action;

import com.nh.ifarm.analyze.service.DailyService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Seymour on 2016/11/30.
 */
@Controller
@RequestMapping("/dailyMobile")
public class DailyQueryMobileAction extends BaseAction {

    @Autowired
    private DailyService dailyService;

    @RequestMapping("/dailySave")
    public void dailySave(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

            String BreedBatchId = tUserJson.optString("BreedBatchId");
            int HouseId = tUserJson.optInt("HouseId");
            int FarmId = tUserJson.optInt("FarmId");
            int DayAge = tUserJson.optInt("DayAge");
            String HouseName = tUserJson.optString("HouseName");
            int death_num_male = tUserJson.optInt("death_num_male");
            int death_num_female = tUserJson.optInt("death_num_female");
            int culling_num_male = tUserJson.optInt("culling_num_male");
            int culling_num_female = tUserJson.optInt("culling_num_female");
            String body_weight_male = tUserJson.optString("body_weight_male");
            String body_weight_female = tUserJson.optString("body_weight_female");
            int gender_error_male = tUserJson.optInt("gender_error_male");
            int gender_error_female = tUserJson.optInt("gender_error_female");
            String feed_code_female = tUserJson.optString("feed_code_female");
            String feed_weight_female = tUserJson.optString("feed_weight_female");
            String water_capacity_female = tUserJson.optString("water_capacity_female");
            int layer_amount = tUserJson.optInt("layer_amount");
            int layer_qual_amount = tUserJson.optInt("layer_qual_amount");
            String layer_qual_weight = tUserJson.optString("layer_qual_weight");
            String uniformity = tUserJson.optString("uniformity");

            pd.put("BreedBatchId", BreedBatchId);
            pd.put("FarmId", FarmId);
            pd.put("HouseId", HouseId);
            pd.put("DayAge", DayAge);
            pd.put("HouseName", HouseName);
            pd.put("death_num_male", death_num_male);
            pd.put("death_num_female", death_num_female);
            pd.put("culling_num_male", culling_num_male);
            pd.put("culling_num_female", culling_num_female);
            pd.put("body_weight_male", body_weight_male);
            pd.put("body_weight_female", body_weight_female);
            pd.put("gender_error_male", gender_error_male);
            pd.put("gender_error_female", gender_error_female);
            pd.put("feed_code_female", feed_code_female);
            pd.put("feed_weight_female", feed_weight_female);
            pd.put("water_capacity_female", water_capacity_female);
            pd.put("layer_amount", layer_amount);
            pd.put("layer_qual_weight", layer_qual_weight);
            pd.put("layer_qual_amount", layer_qual_amount);
            pd.put("uniformity", uniformity);
            pd.put("user_id", userId);

            int flag = dailyService.dailySave(pd);

            if (flag == -1) {
                resJson.put("Result", "Fail");
                resJson.put("Error", "死淘数超过入栏数！");
            } else if (flag == -2) {
                resJson.put("Result", "Fail");
                resJson.put("Error", "日期超过当前时间，请重新输入！");
            } else if (flag == -3) {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } else {
                resJson.put("Result", "Success");
                resJson.put("Error", "");
            }
        }catch (Exception e){
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/dailyQuery")
    public void dailyQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        PageData pd = new PageData();
        pd = this.getPageData();
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        JSONObject jsonObject = new JSONObject(aa);
        int userId = jsonObject.optInt("id_spa");
        JSONObject tUserJson = jsonObject.getJSONObject("params");

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        int HouseId = tUserJson.optInt("HouseId");
        String BreedBatchId = tUserJson.optString("BreedBatchId");
        String SpecialFlag = tUserJson.optString("SpecialFlag");
        String SpecialDate = tUserJson.optString("SpecialDate");
        pd.put("HouseId", HouseId);
        pd.put("BreedBatchId", BreedBatchId);

        PageData date = dailyService.selectDate(pd);
        pd.put("SpecialDate", SpecialDate);
        PageData data = new PageData();
        if (!"0".equals(BreedBatchId)) {
            if ("Y".equals(SpecialFlag)) {
                data = dailyService.selectBySpecialDate(pd);
                if (data == null) {
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "暂无数据！");
                }
            } else if ("N".equals(SpecialFlag)) {
                Date curDate = new Date();
                pd.put("SpecialDate", sdf1.format(curDate));
                data = dailyService.selectBySpecialDate(pd);
                Date lairage = sdf1.parse(date.get("lairage").toString());
                Date marketedDate = sdf1.parse(date.get("marketed_date").toString());
                if (data == null) {
                    if (curDate.before(lairage)) {
                        pd.put("SpecialDate", sdf1.format(lairage));
                        data = dailyService.selectBySpecialDate(pd);
                    } else if (curDate.after(marketedDate)) {
                        pd.put("SpecialDate", sdf1.format(marketedDate));
                        data = dailyService.selectBySpecialDate(pd);
                    }
                }
            }
            if (data != null) {
                JSONObject dataInfo = new JSONObject();
                dataInfo.put("BreedBatchId", data.get("batch_id"));
                dataInfo.put("HouseId", data.get("house_code"));
                dataInfo.put("GrowthDate", sdf1.format(sdf1.parse(data.get("growth_date").toString())));
                dataInfo.put("DayAge", data.get("age"));
                dataInfo.put("GrowthWeekAge", data.get("growth_week_age"));
                dataInfo.put("LayerWeekAge", data.get("layWeekAge"));
                dataInfo.put("death_num_male", data.get("male_death_pm"));
                dataInfo.put("death_num_female", data.get("female_death_pm"));
                dataInfo.put("culling_num_male", data.get("male_culling_pm"));
                dataInfo.put("culling_num_female", data.get("female_culling_pm"));
                dataInfo.put("body_weight_male", data.get("male_cur_weight"));
                dataInfo.put("body_weight_female", data.get("female_cur_weight"));
                dataInfo.put("gender_error_male", data.get("male_mistake"));
                dataInfo.put("gender_error_female", data.get("female_mistake"));
                dataInfo.put("feed_code_female", data.get("feed_name") == null ? "" : data.get("feed_name"));
                dataInfo.put("feed_weight_female", data.get("female_cur_feed"));
                dataInfo.put("water_capacity_female", data.get("female_cur_water"));
                dataInfo.put("layer_amount", data.get("laying_cur_amount"));

                dataInfo.put("layer_qual_amount", data.get("laying_qual_cur_amount"));
                dataInfo.put("layer_qual_weight", data.get("laying_qual_cur_wight"));

                dataInfo.put("uniformity", data.get("female_cur_evenness"));
                resJson.put("DataInfo", dataInfo);
                resJson.put("Error", "");
                resJson.put("Result", "Success");
            } else {
                resJson.put("Result", "Fail");
                resJson.put("Error", "暂无数据！");
            }
        }else{
            resJson.put("Result", "Fail");
            resJson.put("Error", "暂无数据！");
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
