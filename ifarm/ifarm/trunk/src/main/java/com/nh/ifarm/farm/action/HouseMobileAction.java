package com.nh.ifarm.farm.action;


import com.nh.ifarm.farm.service.FarmService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.service.ModuleService;
import com.nh.ifarm.util.service.OrganService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Seymour on 2017/4/25.
 */
@Controller
@RequestMapping("/houseMobile")
public class HouseMobileAction extends BaseAction {

    @Autowired
    private FarmService farmService;

    @Autowired
    private OrganService organService;

    @Autowired
    private ModuleService moduleService;

    @RequestMapping("/add")
    public void add(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);
            int user = jsonObject.optInt("id_spa");

            JSONObject tUserJson = jsonObject.optJSONObject("params");
            int FarmId = tUserJson.optInt("farm_id");
            String HouseName = tUserJson.optString("house_name");
            String HouseType = tUserJson.optString("house_type");
            pd.put("id", 0);
            pd.put("house_code", "0");
            pd.put("house_name", HouseName);
            pd.put("house_type", HouseType);
            pd.put("farm_id", FarmId);
            pd.put("freeze_status", "0");
            pd.put("create_person", user);
            pd.put("create_date", new Date());
            pd.put("create_time", new Date());
            pd.put("modify_person", user);
            pd.put("modify_date", new Date());
            pd.put("modify_time", new Date());
            farmService.saveHouse(pd);
            int house_id = pd.getInteger("id");
            pd.put("id", house_id);
            pd.put("house_code", house_id);
            pd.put("user_id", user);

            List<PageData> maxLevelList = organService.getMaxOrgLevelId(null);
            int maxOrgLevelId = Integer.valueOf(String.valueOf(maxLevelList.get(0).get("max_level_id")));
            pd.put("level_id", maxOrgLevelId + 1);

            int k = organService.setHouseMapping(pd);
            if (k > 0) {
                PageData para = new PageData();
                para.put("farm_id", FarmId);
                para.put("insertHouseFlag", "insertFlag");
                List<PageData> emploee = organService.getFarmUser(para);
                PageData paramPd = new PageData();
                Integer objId = (Integer) pd.get("id");

                //该农场下对应的有效用户赋权限
                for (PageData data : emploee) {
                    paramPd.put("user_id", data.get("id"));
                    paramPd.put("obj_id", objId);
                    paramPd.put("obj_type", 2);
                    paramPd.put("create_person", user);
                    moduleService.service("roleServiceImpl", "insertRightsObj", new Object[]{paramPd});
                }
                pd.put("id", pd.get("house_code"));
                farmService.editHouse(pd);
                /**
                 * 新增栋舍警报
                 */
                for (int i = 0; i < 4; i++) {
                    pd.put("house_id", pd.getInteger("id"));
                    pd.put("alarm_type", i + 1);
                    farmService.saveHouseAlarm(pd);
                }
                resJson.put("house_id", house_id);
                resJson.put("farm_id", FarmId);
                resJson.put("house_name", HouseName);
                resJson.put("Result", "Success");
                resJson.put("Error", "");
            } else {
                resJson.put("Result", "Fail");
                resJson.put("Error", "系统内部错误！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/update")
    public void update(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);
            int user = jsonObject.optInt("id_spa");

            JSONObject tUserJson = jsonObject.optJSONObject("params");
            int HouseId = tUserJson.optInt("house_id");
            String HouseName = tUserJson.optString("house_name");

            pd.put("modify_person", user);
            pd.put("modify_date", new Date());
            pd.put("modify_time", new Date());
            farmService.editHouse(pd);
            pd.put("org_id", HouseId);
            pd.put("org_name_updated", HouseName);
            organService.updateOrg(pd);
            /**
             * 修改栋舍和设备关系
             */
            pd.put("house_id", pd.getString("id"));
            pd.put("farm_id", pd.getString("farmId"));
            farmService.delDeviHouse(pd);
            if (!StringUtils.isBlank(pd.getString("deviceKey"))) {
                String[] arr = pd.getString("deviceKey").split(",");
                for (int k = 0; k < arr.length; k++) {
                    pd.put("device_code", arr[k]);
                    List<PageData> device = farmService.findDevice(pd);
                    PageData da = device.get(0);
                    pd.put("create_person", user);
                    pd.put("create_date", new Date());
                    pd.put("create_time", new Date());
                    pd.put("device_code", da.getString("device_code"));
                    pd.put("device_type", da.getString("device_type"));
                    pd.put("port_id", da.getString("port_id"));
                    farmService.saveDeviHouse(pd);
                }
            }
            resJson.put("Result", "Success");
            resJson.put("Error", "");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);
            int user = jsonObject.optInt("id_spa");

            JSONObject tUserJson = jsonObject.optJSONObject("params");
            int HouseId = tUserJson.optInt("house_id");
            List orgList = new ArrayList();
            orgList.add(HouseId);
            pd.put("id", HouseId);
            pd.put("freeze_status", 1);
            pd.put("modify_person", user);
            pd.put("modify_date", new Date());
            pd.put("modify_time", new Date());
            pd.put("orgList", orgList);
            farmService.editHouse(pd);
            resJson.put("Result", "Success");
            resJson.put("Error", "");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (JSONException ee) {
                ee.printStackTrace();
            }
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
