package com.nh.ifarm.farm.action;

import com.nh.ifarm.farm.service.FarmService;
import com.nh.ifarm.farm.service.impl.FarmServiceImpl;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.common.PubFun;
import com.nh.ifarm.util.service.OrganService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by Seymour on 2016/12/21.
 */
@Controller
@RequestMapping("/farmMobile")
public class FarmMobileAction extends BaseAction {

    @Autowired
    private FarmService farmService;

    @RequestMapping("/deviceQuery")
    public void deviceQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            int HouseId = tUserJson.optInt("house_id");

            pd.put("house_id", HouseId);

            JSONArray sensorInfos = new JSONArray();
            String DeviceCode = "";
            String mainId = "";
            List<PageData> deviceList = farmService.findDevice(pd);
            if (deviceList.size() != 0){
                DeviceCode = deviceList.get(0).get("device_code").toString();
                mainId = deviceList.get(0).get("main_id").toString();

                pd.put("main_id", mainId);
                pd.put("sensor_code", "1000");
                List<PageData> sensorList = farmService.findSensor(pd);
                for (PageData pageData : sensorList) {
                    JSONObject sensorInfo = new JSONObject();
                    sensorInfo.put("sensor_no", pageData.get("sensor_no") == null ? "" : pageData.get("sensor_no"));
                    sensorInfo.put("sensor_code", pageData.get("sensor_type") == null ? "" : pageData.get("sensor_type"));
                    sensorInfo.put("show_column", pageData.get("location_code"));
                    sensorInfos.put(sensorInfo);
                }
            }
            resJson.put("device_code", DeviceCode);
            resJson.put("Result", "Success");
            resJson.put("sensorInfo", sensorInfos);
            dealRes = Constants.RESULT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/deviceSave")
    public void deviceSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            int HouseId = tUserJson.optInt("house_id");
            String DeviceCode = tUserJson.optString("device_code");
            JSONArray SensorInfo = tUserJson.optJSONArray("sensorInfo");

            if (!"".equals(DeviceCode)) {
                //绑定设备
                pd.put("device_code", DeviceCode);
                PageData deviceSpe = farmService.findDevice_v2(pd);
                if (PubFun.isNull(deviceSpe)) {
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "无该编号的设备");
                } else {
                    List<PageData> deviceList = farmService.findDeviceIsExist(pd);
                    if (deviceList.size() > 0){
                        resJson.put("Result", "Fail");
                        resJson.put("Error", "该设备已绑定栋舍");
                    } else {
                        String mainId = "";
                        String deviceType = "";
                        deviceType = deviceSpe.getString("device_type");
                        PageData pageData = new PageData();
                        pageData.put("house_id", HouseId);
                        PageData tt = farmService.findDeviceForOut(pageData);
                        if (!PubFun.isNull(tt)) {
                            mainId = deviceType + "-" + tt.get("device_code").toString() + "-x";
                            int ii = farmService.delDevice(pageData);
                            pageData.put("device_code", tt.get("device_code"));
                            for (int i = 0; i < SensorInfo.length(); ++i) {
                                pageData.put("main_id", mainId);
                                pageData.put("show_column", SensorInfo.getJSONObject(i).get("show_column"));
                                pageData.put("sensor_code", "1000");
                                int o = farmService.delSensor(pageData);
                            }
                        }
                        pd.put("farm_id", FarmId);
                        pd.put("house_id", HouseId);
                        pd.put("device_code", DeviceCode);
                        pd.put("device_type", deviceType);
                        pd.put("create_person", userId);
                        pd.put("create_date", new Date());
                        pd.put("create_time", new Date());
                        pd.put("modify_person", userId);
                        pd.put("modify_date", new Date());
                        pd.put("modify_time", new Date());
                        int j = farmService.mappingDevice(pd);
                        if (j == 1) {
                            resJson.put("Result", "Success");
                            resJson.put("Error", "");
                            mainId = deviceType + "-" + DeviceCode + "-x";
                            for (int i = 0; i < SensorInfo.length(); ++i) {
                                PageData pageData1 = new PageData();
                                pageData1.put("farm_id", FarmId);
                                pageData1.put("house_id", HouseId);
                                pageData1.put("device_code", DeviceCode);
                                pageData1.put("main_id", mainId);
                                pageData1.put("show_column", SensorInfo.getJSONObject(i).get("show_column"));
                                pageData1.put("sensor_code", "1000");
                                pageData1.put("sensor_no", SensorInfo.getJSONObject(i).get("sensor_no"));
                                pageData1.put("create_person", userId);
                                int a = farmService.insertSensor(pageData1);
                            }
                        }
                    }
                }
            } else {
                //解除设备绑定
                PageData pageData = new PageData();
                pageData.put("house_id", HouseId);
                PageData deviceSpe = farmService.findDeviceForOut(pageData);
                if (PubFun.isNull(deviceSpe)) {
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "暂无设备绑定！");
                } else {
                    int ii = farmService.delDevice(pageData);
                    for (int i = 0; i < SensorInfo.length(); ++i) {
                        pageData.put("main_id", deviceSpe.get("main_id"));
                        pageData.put("show_column", SensorInfo.getJSONObject(i).get("show_column"));
                        pageData.put("sensor_code", "1000");
                        int o = farmService.delSensor(pageData);
                    }
                    resJson.put("Result", "Success");
                    resJson.put("Error", "");
                }
            }
            dealRes = Constants.RESULT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
            dealRes = Constants.RESULT_SUCCESS;
        }
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
