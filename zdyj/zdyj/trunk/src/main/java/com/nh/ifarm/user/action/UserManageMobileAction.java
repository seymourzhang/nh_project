package com.nh.ifarm.user.action;

import com.nh.ifarm.user.service.SDUserService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.common.PubFun;
import com.nh.ifarm.util.service.ModuleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.json.JSONArray;
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

import static com.nh.ifarm.user.action.UserManageAction.getHttpResponse;


/**
 * Created by Seymour on 2017/4/24.
 */

@Controller
@RequestMapping("userMobile/")
public class UserManageMobileAction extends BaseAction {

    @Autowired
    private SDUserService tSDUserService;

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
            int CompanyId = tUserJson.optInt("CompanyId");
            int FarmId = tUserJson.optInt("farmId");
            String userName = tUserJson.optString("user_code");
            String chName = tUserJson.optString("ch_name");
            String enName = tUserJson.optString("en_name");
            String mobileNum = tUserJson.optString("mobile_num");
            String roleTempId = tUserJson.optString("role_temp_id");
            JSONArray houseArray = tUserJson.optJSONArray("UserHouse");

            PageData pageData = new PageData();

            String password = userName + "123";
            String passwd = new SimpleHash("SHA-1", userName, password).toString();    //密码加密

            pageData.put("id", 0);
            pageData.put("user_code", userName);
            pageData.put("user_password", passwd);
            pageData.put("user_real_name", chName);
            pageData.put("user_real_name_en", enName);
            pageData.put("user_mobile_1", mobileNum);
            pageData.put("user_status", 1);
            pageData.put("freeze_status", 0);
            pageData.put("create_person", user);
            pageData.put("create_date", new Date());
            pageData.put("create_time", new Date());
            pageData.put("modify_person", user);
            pageData.put("modify_date", new Date());
            pageData.put("modify_time", new Date());


            PageData checkUserPd = new PageData();
            checkUserPd.put("user_code", userName);
            checkUserPd.put("freeze_status", 0);
            checkUserPd.put("user_status", 1);
            PageData lUser = tSDUserService.findUserInfo(checkUserPd);
            if (lUser == null) {
                String url = PubFun.getPropertyValue("Check.User.Url") + userName;
                String json = getHttpResponse(url);
                com.alibaba.fastjson.JSONObject jsStr = com.alibaba.fastjson.JSONObject.parseObject(json);

                if (Integer.parseInt(jsStr.getString("obj")) == 0) {
                    tSDUserService.saveUser(pageData);
                    int userId = pageData.getInteger("id");

                    if (userId > 0) {
                        List orgs = new ArrayList();
                        orgs.add(CompanyId);
                        orgs.add(FarmId);
                        for (int i = 0; i < houseArray.length(); ++i) {
                            orgs.add(houseArray.getJSONObject(i).get("house_id"));
                        }
                        String orgsStr = orgs.toString().replace("[", "").replace("]", ",");
                        pd.put("org_str", orgsStr);
                        pd.put("user_id", userId);
                        pd.put("role_id", roleTempId);
                        pd.put("role_temp_id", roleTempId);
                        pd.put("is_main_rela", "1");
                        pd.put("create_person", user);
                        moduleService.service("roleServiceImpl", "saveUserRole", new Object[]{pd});
                        PageData pp = new PageData();
                        pp.put("obj_type", 2);
                        pp.put("user_id", userId);
                        List<PageData> vUser = tSDUserService.getUserHouses(pp);
                        JSONArray UserHouses = new JSONArray();
                        for (PageData data : vUser) {
                            JSONObject UserHouse = new JSONObject();
                            UserHouse.put("id", data.get("house_id"));
                            UserHouse.put("name", data.get("name_cn"));
                            UserHouse.put("deviceCode", data.get("device_code"));
                            UserHouse.put("houseTypeName", data.get("code_name"));
                            UserHouse.put("BreedBatchId", data.get("batch_id"));
                            UserHouse.put("BreedBatchStatus", data.get("status"));
                            UserHouses.put(UserHouse);
                        }
                        resJson.put("user_id", userId);
                        resJson.put("user_code", userName);
                        resJson.put("ch_name", chName);
                        resJson.put("en_name", enName);
                        resJson.put("mobile_num", mobileNum);
                        resJson.put("role_temp_id", roleTempId);
                        resJson.put("role_id", vUser.get(0).get("role_id"));
                        resJson.put("UserHouse", UserHouses);
                        resJson.put("Result", "Success");
                    } else {
                        resJson.put("Result", "Fail");
                        resJson.put("Error", "未知错误！");
                    }
                } else {
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "用户已存在，请重新添加！");
                }
            } else {
                resJson.put("Result", "Fail");
                resJson.put("Error", "用户已存在，请重新添加！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (Exception e1) {
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
            int CompanyId = tUserJson.optInt("CompanyId");
            int FarmId = tUserJson.optInt("farmId");
            int UserId = tUserJson.optInt("user_id");
            String chName = tUserJson.optString("ch_name");
            String enName = tUserJson.optString("en_name");
            String mobileNum = tUserJson.optString("mobile_num");
            String roleTempId = tUserJson.optString("role_temp_id");
            JSONArray orgArray = tUserJson.optJSONArray("UserHouse");

            PageData pageData = new PageData();
            pageData.put("id", UserId);
            pageData.put("user_real_name", chName);
            pageData.put("user_real_name_en", enName);
            pageData.put("user_mobile_1", mobileNum);
            pageData.put("create_person", user);
            pageData.put("modify_person", user);
            pageData.put("modify_date", new Date());
            pageData.put("modify_time", new Date());
            try {
                tSDUserService.editUser(pageData);
                if (!StringUtils.isBlank(roleTempId)) {
                    List orgs = new ArrayList();
                    orgs.add(CompanyId);
                    orgs.add(FarmId);
                    for (int i = 0; i < orgArray.length(); ++i){
                        orgs.add(orgArray.getJSONObject(i).get("house_id"));
                    }
                    String orgsStr = orgs.toString().replace("[", "").replace("]", ",");
                    pd.put("org_str", orgsStr);
                    pd.put("user_id", pageData.get("id"));
                    pd.put("role_id", roleTempId);
                    pd.put("is_main_rela", 1);
                    pd.put("create_person", user);
                    pd.put("modify_person", user);
                    pd.put("modify_date", new Date());
                    pd.put("modify_time", new Date());

                    moduleService.service("roleServiceImpl", "editUserRole", new Object[]{pd});
                }
                PageData pp = new PageData();
                pp.put("obj_type", 2);
                pp.put("user_id", pageData.get("id"));
                List<PageData> vUser = tSDUserService.getUserHouses(pp);
                JSONArray UserHouses = new JSONArray();
                for (PageData data : vUser) {
                    JSONObject UserHouse = new JSONObject();
                    UserHouse.put("id", data.get("house_id"));
                    UserHouse.put("name", data.get("name_cn"));
                    UserHouses.put(UserHouse);
                }
                resJson.put("user_id", pageData.get("id"));
                resJson.put("user_code", vUser.get(0).get("user_code"));
                resJson.put("ch_name", chName);
                resJson.put("en_name", enName);
                resJson.put("mobile_num", mobileNum);
                resJson.put("role_temp_id", roleTempId);
                resJson.put("role_id", vUser.get(0).get("role_id"));
                resJson.put("UserHouse", UserHouses);
                resJson.put("Result", "Success");
                resJson.put("Error", "");
            } catch (Exception e) {
                e.printStackTrace();
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (Exception e1) {
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
            int UserId = tUserJson.optInt("user_id");
            pd.put("id", UserId);
            pd.put("user_status","0");
            pd.put("freeze_status","1");
            pd.put("modify_person",UserId);
            pd.put("modify_date", new Date());
            pd.put("modify_time", new Date());
            tSDUserService.editUser(pd);
            resJson.put("Result", "Success");
            resJson.put("Error", "");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/resetPassword")
    public void resetPassword(HttpServletRequest request, HttpServletResponse response) {
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
            int UserId = tUserJson.optInt("user_id");
            String UserCode = tUserJson.optString("user_code");
            String password = UserCode + "123";
            String passwd = new SimpleHash("SHA-1", UserCode, password).toString();    //密码加密
            pd.put("id", UserId);
            pd.put("user_password", passwd);
            pd.put("modify_person", UserId);
            pd.put("modify_date", new Date());
            pd.put("modify_time", new Date());
            tSDUserService.editUser(pd);
            resJson.put("Result", "Success");
            resJson.put("Error", "");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resJson.put("Result", "Fail");
                resJson.put("Error", "程序处理错误，请联系管理员！");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/updatePassword")
    public void updatePassword(HttpServletRequest request, HttpServletResponse response) {
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

            int UserId = tUserJson.optInt("user_id");
            String UserCode = tUserJson.optString("user_code");
            String OldPwd = tUserJson.optString("old_pw");
            String NewPwd = tUserJson.optString("new_pw");

            String passwd = new SimpleHash("SHA-1", UserCode, OldPwd).toString();    //密码加密
            pd.put("user_code", UserCode);
            pd.put("user_password", passwd);
            pd.put("user_status", '1');
            pd = tSDUserService.getUserBylogin(pd);
            if (pd != null) {
                PageData pageData = new PageData();
                String newwd = new SimpleHash("SHA-1", UserCode, NewPwd).toString();    //密码加密
                pageData.put("id", UserId);
                pageData.put("user_password", newwd);
                pageData.put("modify_person", UserId);
                pageData.put("modify_date", new Date());
                pageData.put("modify_time", new Date());
                tSDUserService.editUser(pageData);
                resJson.put("Result", "Success");
                resJson.put("Error", "");
            } else {
                resJson.put("Result", "Fail");
                resJson.put("LoginResult", 3);
                resJson.put("Error", "用户名或密码有误！");
            }
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
