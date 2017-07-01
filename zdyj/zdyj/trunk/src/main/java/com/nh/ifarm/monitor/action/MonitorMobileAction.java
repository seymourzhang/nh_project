package com.nh.ifarm.monitor.action;

import com.nh.ifarm.monitor.service.MonitorService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.service.OrganService;
//import com.sun.xml.internal.xsom.impl.scd.SCDImpl;
import com.nh.ifarm.util.common.Constants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seymour on 2016/11/22.
 */
@Controller
@RequestMapping("/monitorMobile")
public class MonitorMobileAction extends BaseAction {

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private OrganService organService;

    @RequestMapping("/monitoring")
    public void monitoring(HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*Json j=new Json();
        PageData pd = this.getPageData();
        PageData pageData = new PageData();*/
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        JSONObject jsonObject = new JSONObject(aa);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
//        Date curDate = new Date();

        int userId = jsonObject.optInt("id_spa");
        JSONObject tUserJson = jsonObject.getJSONObject("params");
        int FarmId = tUserJson.optInt("FarmId");
        List<PageData> mcl = new ArrayList<>();
        List farm = new ArrayList();
        farm.add(FarmId);
        pd.put("farmId", farm);
        pd.put("parent_id", FarmId);
        pd.put("user_id", userId);
        List<PageData> datas = organService.getOrgList(pd);
        JSONArray monitor = new JSONArray();
        for (PageData a : datas) {
            List house = new ArrayList();
            house.add(a.getInteger("id"));
            pd.put("houseId", house);
            PageData hou = new PageData();
            hou.put("farmId", FarmId);
            hou.put("houseId", a.getInteger("id"));
            PageData temp = monitorService.selectAlarmCounts(hou);
            pd.put("appFlag", "app");
            mcl = monitorService.selectByCondition(pd);
            if (mcl.size() != 0) {
                for (PageData data : mcl) {
                    /*long accident = Long.parseLong(data.get("accidentTime").toString());
                    Date collectDate = sdf.parse(data.get("collect_datetime2").toString());
                    long dateDiff = (curDate.getTime() - collectDate.getTime()) / (1000 * 60);//转换成分钟差*/
                    String alarmCode = data.get("biz_code").toString();
                    if ("1".equals(data.get("show_flag").toString()) && !"1".equals(alarmCode)) {
                        JSONObject jo = new JSONObject();
                        jo.put("houseName", a.get("name_cn"));
                        jo.put("dayAge", data.get("date_age"));
                        jo.put("data_time", sdf1.format(sdf1.parse(data.get("collect_datetime").toString())));
                        jo.put("out_temp", "-".equals(data.get("outside_temp").toString()) ? "-" : data.get("outside_temp").toString() + "℃");
                        jo.put("tempLeft1", "-".equals(data.get("inside_temp1").toString()) ? "-" : data.get("inside_temp1").toString() + "℃");
                        jo.put("tempLeft2", "-".equals(data.get("inside_temp2").toString()) ? "-" : data.get("inside_temp2").toString() + "℃");
                        jo.put("tempMiddle1", "-".equals(data.get("inside_temp10").toString()) ? "-" : data.get("inside_temp10").toString() + "℃");
                        jo.put("tempMiddle2", "-".equals(data.get("inside_temp11").toString()) ? "-" : data.get("inside_temp11").toString() + "℃");
                        jo.put("tempRight1", "-".equals(data.get("inside_temp19").toString()) ? "-" : data.get("inside_temp19").toString() + "℃");
                        jo.put("tempRight2", "-".equals(data.get("inside_temp20").toString()) ? "-" : data.get("inside_temp20").toString() + "℃");
                        jo.put("tar_temp", "-".equals(data.get("inside_set_temp").toString()) ? "-" : data.get("inside_set_temp").toString() + "℃");
                        jo.put("avg_temp", "-".equals(data.get("inside_avg_temp").toString()) ? "-" : data.get("inside_avg_temp").toString() + "℃");
                        jo.put("H_temp", "-".equals(data.get("high_alarm_temp").toString()) ? "-" : data.get("high_alarm_temp").toString() + "℃");
                        jo.put("L_temp", "-".equals(data.get("low_alarm_temp").toString()) ? "-" : data.get("low_alarm_temp").toString() + "℃");
                        jo.put("point_temp", "-".equals(data.get("point_temp_diff").toString()) ? "-" : data.get("point_temp_diff").toString() + "℃");
                        jo.put("humi", "-".equals(data.get("inside_humidity").toString()) ? "-" : data.get("inside_humidity").toString() + "%");
                        jo.put("CO2", "-".equals(data.get("co2").toString()) ? "-" : data.get("co2"));
                        jo.put("illumination", "-".equals(data.get("lux").toString()) ? "-" : data.get("lux"));
                        jo.put("power_status", "-".equals(data.get("power_status")) ? "-" : (data.get("power_status").equals("1") ? "断电" : "正常"));
                        jo.put("temp_in1_alarm", data.get("temp_in1_alarm"));
                        jo.put("temp_in2_alarm", data.get("temp_in2_alarm"));
                        jo.put("temp_in3_alarm", data.get("temp_in3_alarm"));
                        jo.put("temp_avg_alarm", data.get("temp_avg_alarm"));
                        jo.put("point_temp_alarm", data.get("point_temp_alarm"));
                        jo.put("power_status_alarm", data.get("power_status_alarm"));
                        jo.put("title_ted", Integer.parseInt(temp.get("num").toString()) > 0 ? 1 : 0);
                        jo.put("co2_alarm", data.get("co2_alarm"));
                        jo.put("lux_alarm", data.get("lux_alarm"));
                        jo.put("ammonia_1", data.get("ammonia_1").toString());
                        jo.put("water_consumption", data.get("water_consumption").toString());
                        monitor.put(jo);
                    } else {
                        JSONObject show_n = new JSONObject();
                        show_n.put("houseName", a.get("name_cn"));
                        show_n.put("dayAge", "-");
                        show_n.put("data_time", "-");
                        show_n.put("out_temp", "-");
                        show_n.put("tempLeft1", "-");
                        show_n.put("tempLeft2", "-");
                        show_n.put("tempMiddle1", "-");
                        show_n.put("tempMiddle2", "-");
                        show_n.put("tempRight1", "-");
                        show_n.put("tempRight2", "-");
                        show_n.put("tar_temp", "-");
                        show_n.put("avg_temp", "-");
                        show_n.put("H_temp", "-");
                        show_n.put("L_temp", "-");
                        show_n.put("point_temp", "-");
                        show_n.put("humi", "-");
                        show_n.put("CO2", "-");
                        show_n.put("illumination", "-");
                        show_n.put("power_status", "-");
                        show_n.put("temp_in1_alarm", "-");
                        show_n.put("temp_in2_alarm", "-");
                        show_n.put("temp_in3_alarm", "-");
                        show_n.put("temp_avg_alarm", "-");
                        show_n.put("point_temp_alarm", "-");
                        show_n.put("power_status_alarm", "-");
                        show_n.put("title_red", Integer.parseInt(temp.get("num").toString()) > 0 ? 1 : 0);
                        show_n.put("co2_alarm", "-");
                        show_n.put("lux_alarm", "-");
                        show_n.put("ammonia_1", "-");
                        show_n.put("water_consumption", "-");
                        monitor.put(show_n);
                    }
                }
            } else {
                JSONObject jo = new JSONObject();
                jo.put("houseName", a.get("name_cn"));
                jo.put("dayAge", "-");
                jo.put("data_time", "-");
                jo.put("out_temp", "-");
                jo.put("tempLeft1", "-");
                jo.put("tempLeft2", "-");
                jo.put("tempMiddle1", "-");
                jo.put("tempMiddle2", "-");
                jo.put("tempRight1", "-");
                jo.put("tempRight2", "-");
                jo.put("tar_temp", "-");
                jo.put("avg_temp", "-");
                jo.put("H_temp", "-");
                jo.put("L_temp", "-");
                jo.put("point_temp", "-");
                jo.put("humi", "-");
                jo.put("CO2", "-");
                jo.put("illumination", "-");
                jo.put("power_status", "-");
                jo.put("temp_in1_alarm", "-");
                jo.put("temp_in2_alarm", "-");
                jo.put("temp_in3_alarm", "-");
                jo.put("temp_avg_alarm", "-");
                jo.put("point_temp_alarm", "-");
                jo.put("power_status_alarm", "-");
                jo.put("title_ted", Integer.parseInt(temp.get("num").toString()) > 0 ? 1 : 0);
                jo.put("co2_alarm", "-");
                jo.put("lux_alarm", "-");
                jo.put("ammonia_1", "-");
                jo.put("water_consumption", "-");
                monitor.put(jo);
            }
        }
        resJson.put("Result", "Success");
        resJson.put("MonitorData", monitor);
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/needAlarm")
    public void needAlarm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject resJson = new JSONObject();
        PageData pd = new PageData();
        pd = this.getPageData();
        String dealRes = null;
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        JSONObject jsonObject = new JSONObject(aa);

        int userId = jsonObject.optInt("id_spa");
        JSONObject tUserJson = jsonObject.getJSONObject("params");
        int FarmId = tUserJson.optInt("FarmId");

        List farm = new ArrayList();
        List house = new ArrayList();
        farm.add(FarmId + "");
        pd.put("farmId", MonitorAction.listToString(farm));
        pd.put("user_id", userId);
        pd.put("parent_id", FarmId);
        List<PageData> ll = organService.getOrgList(pd);
        for (PageData pageData : ll) {
            house.add(pageData.get("id").toString());
        }
        if(house.size() == 0){
            pd.put("houseId", 0);
        }else{
            pd.put("houseId", MonitorAction.listToString(house));
        }

        pd.put("deal_status", "01");
        PageData mcl = monitorService.selectAlarmCounts(pd);
        if ("0".equals(mcl.get("num").toString())){
            resJson.put("AlarmStatus", "N");
        }else{
            resJson.put("AlarmStatus", "Y");
        }
        resJson.put("Result", "Success");
        resJson.put("Error", "");
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
