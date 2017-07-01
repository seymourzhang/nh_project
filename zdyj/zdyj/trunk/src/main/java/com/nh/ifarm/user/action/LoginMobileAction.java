package com.nh.ifarm.user.action;


import com.alibaba.fastjson.JSONException;
import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.farm.service.FarmService;
import com.nh.ifarm.monitor.service.AutioSettingService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.user.service.SBUserImeiService;
import com.nh.ifarm.user.service.SDUserService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.*;
import com.nh.ifarm.util.service.ModuleService;
import com.nh.ifarm.util.service.OrganService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/loginMobile")
public class LoginMobileAction extends BaseAction{
	
	@Autowired
	private SDUserService userService;
	
//	@Autowired
//	private SDMenuService sdMenuService;
	
	@Autowired
	private ModuleService moduleService;

	@Autowired
	private SBUserImeiService sbUserImeiService;

	@Autowired
	private OrganService organService;

    @Autowired
    private BatchManageService batchManageService;

    @Autowired
    private FarmService farmService;

    @Autowired
    private AutioSettingService autioSettingService;


	/**
	 * 移动端登陆
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/login")
	public void login(HttpServletRequest request,HttpServletResponse response)throws Exception {
        //shiro管理的session
        String dealRes = null;
        JSONObject resJson = new JSONObject();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();

            PageData pd = new PageData();
            pd = this.getPageData();
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);

            JSONObject tUserJson = jsonObject.getJSONObject("params");
            String userName = tUserJson.optString("userCode");
            String password = tUserJson.optString("passWord");
            String passwd = new SimpleHash("SHA-1", userName, password).toString();    //密码加密
            pd.put("user_code", userName);
            pd.put("user_password", passwd);
		    pd.put("user_status", '1');

            String loginApp = tUserJson.optString("loginApp");
            String AndroidImei = tUserJson.optString("AndroidImei");
            String uuid = tUserJson.optString("uuid");
            String model = tUserJson.optString("model");
            String sysVersion = tUserJson.optString("sysVersion");
            String platForm = tUserJson.optString("platForm");

            pd = userService.getUserBylogin(pd);

            if (pd != null) {
                String alarmStatus = "";
                PageData pageData = new PageData();
                pageData.put("imei_no", AndroidImei);
                PageData imei = autioSettingService.queryImeiInfo(pageData);
                if (!PubFun.isNull(imei)){
                    if (!PubFun.isNull(imei.get("bak1")) && ("1".equals(imei.get("bak1")) || "0".equals(imei.get("bak1")))){
                        alarmStatus = imei.get("bak1").toString();
                    } else {
                        alarmStatus = "1";
                    }
                }
                if (!"".equals(AndroidImei) && AndroidImei != null) {
                    pageData.put("imei_no", AndroidImei);
                    pageData.put("user_id", pd.getInteger("id"));
                    pageData.put("user_code", userName);
                    pageData.put("uuid", uuid);
                    pageData.put("model", model);
                    pageData.put("sys_version", sysVersion);
                    pageData.put("platform", platForm);
                    pageData.put("bak1", alarmStatus);
                    pageData.put("create_person", pd.getInteger("id"));
                    pageData.put("create_date", new Date());
                    pageData.put("create_time", new Date());
                    int i = sbUserImeiService.insert(pageData);
                    pageData.put("modify_date", new Date());
                    pageData.put("modify_time", new Date());
                    int a = sbUserImeiService.insertLog(pageData);
                }
                SDUser user = new SDUser();
                user.setId(pd.getInteger("id"));
                user.setUser_code(pd.getString("user_code"));
                user.setUser_password(pd.getString("user_password"));
                user.setUser_real_name(pd.getString("user_real_name"));
                user.setUser_real_name_en(pd.getString("user_real_name_en"));
                user.setUser_mobile_1(pd.getString("user_mobile_1"));
                user.setUser_status(pd.getString("user_status"));
                user.setFreeze_status(pd.getString("freeze_status"));
                session.setAttribute(Const.SESSION_USER, user);
                JSONObject userInfo = getUserInfo(user);
                PageData userPar = new PageData();
                userPar.put("user_id", user.getId());
                PageData temp = organService.getUserRole(userPar);
                if (temp != null){
                    userInfo.put("userId", temp.get("id"));
                    userInfo.put("userName", temp.get("user_real_name"));
                    userInfo.put("userCode", temp.get("user_code"));
                    userInfo.put("roleId", temp.get("role_temp_id"));
                    userInfo.put("roleName", temp.get("role_temp_name"));
                    userInfo.put("tele", temp.get("user_mobile_1"));
                }
                resJson.put("userinfo", userInfo);
                //shiro加入身份验证
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(userName, passwd);
                try {
                    subject.login(token);
                } catch (AuthenticationException e) {
                    resJson.put("Result", "Fail");
                    resJson.put("Error", "身份验证失败！");
                    dealRes = Constants.RESULT_SUCCESS;
                }
                if (!pd.getString("user_status").equals("1")) {
                    resJson.put("Result", "Fail");
                    resJson.put("LoginResult", 2);
                    resJson.put("Error", "账户异常！");
                    dealRes = Constants.RESULT_SUCCESS;
                } else if (!pd.getString("freeze_status").equals("0")) {
                    resJson.put("Result", "Fail");
                    resJson.put("LoginResult", 2);
                    resJson.put("Error", "账户已被删除！");
                    dealRes = Constants.RESULT_SUCCESS;
                } else {
                    pd.put("user_id", pd.getInteger("id"));
                    List<PageData> lpd = organService.getFarmListByUserId(pd);
                    JSONArray farms = new JSONArray();
                    for (PageData data : lpd) {
                        JSONObject farm = new JSONObject();
                        farm.put("farmId", data.get("org_id"));
                        farm.put("farmName", data.get("org_name"));
                        farms.put(farm);
                    }
                    resJson.put("FarmList", farms);
                    resJson.put("Result", "Success");
                    resJson.put("LoginResult", 1);
                    dealRes = Constants.RESULT_SUCCESS;
                }
            } else {
                resJson.put("Result", "Fail");
                resJson.put("LoginResult", 3);
                resJson.put("Error", "用户名或密码有误！");
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

    @RequestMapping("/queryDetail")
    public void queryDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //shiro管理的session
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();

            PageData pd = new PageData();
            pd = this.getPageData();
            String aa = pd.toString();
            aa = aa.substring(1, aa.length() - 2);
            JSONObject jsonObject = new JSONObject(aa);

            JSONObject tUserJson = jsonObject.getJSONObject("params");
            Integer userId = tUserJson.optInt("userId");
            Integer farmId = tUserJson.optInt("farmId");

            pd.put("id", userId);
            PageData ui = userService.findUserInfo(pd);

            List houses = new ArrayList();
            pd.put("user_id", userId);
            pd.put("parent_id", farmId);
            PageData pageData1 = new PageData();
            pageData1.put("parent_id", farmId);
            List<PageData> ll = organService.getOrgListById(pageData1);
            pd.put("farm_id", farmId);
            JSONArray ja = new JSONArray();
            for (PageData pageData : ll) {
//                pd.put("user_id", userId);
                pd.put("house_code", pageData.get("id"));
                pd.put("house_id", pageData.get("organization_id"));
                List<PageData> device = farmService.findDevice(pd);
                PageData lpd = batchManageService.selectBatchDataForMobile(pd);
                JSONObject o = new JSONObject();
                o.put("id", pageData.get("id"));
                o.put("house_org_id", pageData.get("organization_id"));
                o.put("name", pageData.get("name_cn"));
                if (lpd == null) {
                    o.put("deviceCode", "");
                    o.put("BreedBatchId", 0);
                    o.put("BreedBatchStatus", 0);
                } else {
                    o.put("BreedBatchNo", lpd.get("batch_no") == null ? 0 : lpd.get("batch_no"));
                    o.put("BreedBatchId", lpd.get("batch_id") == null ? 0 : lpd.get("batch_id"));
                    o.put("BreedBatchStatus", lpd.get("status").equals(0) ? 2 : 1);
                }
                o.put("houseTypeName", pageData.get("code_name"));
                o.put("houseType", pageData.get("house_type"));
                o.put("deviceCode", device.size() == 0 ? "" : device.get(0).get("device_code"));
                ja.put(o);
            }
            resJson.put("HouseInfos", ja);

            JSONArray housesList = new JSONArray();
            PageData userHouse = new PageData();
            userHouse.put("obj_type", 2);
            userHouse.put("user_id", userId);
            userHouse.put("farm_id", farmId);
            List<PageData> userHouses = userService.getUserHouses(userHouse);
            for (PageData house : userHouses) {
                JSONObject UserHouse = new JSONObject();
                UserHouse.put("id", house.get("house_id"));
                UserHouse.put("name", house.get("name_cn"));
                housesList.put(UserHouse);
            }
            resJson.put("UserHouse", housesList);

            JSONArray EmploeeInfos = new JSONArray();
            List<PageData> emploee = organService.getFarmUser(pd);
            if (emploee.size() != 0) {
                for (PageData pageData : emploee) {
                    JSONArray UserHouses = new JSONArray();
                    JSONObject jo = new JSONObject();
                    jo.put("userId", pageData.get("id"));
                    jo.put("userName", pageData.get("user_real_name"));
                    jo.put("userEnName", pageData.get("user_real_name_en"));
                    jo.put("userCode", pageData.get("user_code"));
                    jo.put("roleId", pageData.get("role_temp_id"));
                    jo.put("roleName", pageData.get("role_temp_name"));
                    jo.put("tele", pageData.get("user_mobile_1"));
                    PageData pp = new PageData();
                    pp.put("obj_type", 2);
                    pp.put("user_id", pageData.get("id"));
                    pp.put("farm_id", farmId);
                    List<PageData> vUser = userService.getUserHouses(pp);
                    for (PageData data : vUser) {
                        JSONObject UserHouse = new JSONObject();
                        UserHouse.put("id", data.get("house_id"));
                        UserHouse.put("name", data.get("name_cn"));
                        UserHouses.put(UserHouse);
                    }
                    jo.put("UserHouse", UserHouses);
                    EmploeeInfos.put(jo);
                }
            }
            resJson.put("EmploeeInfos", EmploeeInfos);

            PageData pa = new PageData();
            pa.put("id", farmId);
            List<PageData> llpd = organService.getOrgListById(pa);
            if (llpd.size() != 0) {
                JSONObject farmInfo = new JSONObject();
                farmInfo.put("id", llpd.get(0).get("id"));
                farmInfo.put("name", llpd.get(0).get("name_cn"));
                String feedMethod = llpd.get(0).getString("feed_method");
                String feedType = llpd.get(0).getString("feed_type");
                String farm_type = llpd.get(0).getString("farm_type");
                String farm_type_name = llpd.get(0).getString("farm_type_name");
                PageData temp = new PageData();
                temp.put("code_type", "FEED_METHOD");
                temp.put("biz_code", feedMethod);
                List<PageData> temp1 = farmService.findCode(temp);
                farmInfo.put("feedMethod", temp1.get(0).get("code_name"));
                temp.put("code_type", "FEED_TYPE");
                temp.put("biz_code", feedType);
                List<PageData> temp2 = farmService.findCode(temp);
                farmInfo.put("feedType", temp2.get(0).get("code_name"));
                pa.put("id", llpd.get(0).get("parent_id"));
                List<PageData> llpd_parent = organService.getOrgListById(pa);
                farmInfo.put("CompanyId", llpd_parent.get(0).get("id"));
                farmInfo.put("CompanyName", llpd_parent.get(0).get("name_cn"));
                farmInfo.put("address1", llpd.get(0).get("level1"));
                farmInfo.put("address2", llpd.get(0).get("level2"));
                farmInfo.put("address3", llpd.get(0).get("level3"));
                farmInfo.put("pinyin", llpd.get(0).get("pinyin_name"));
                farmInfo.put("farm_type", farm_type_name);
                farmInfo.put("farm_type_id", farm_type);
                resJson.put("FarmInfo", farmInfo);
            }

            JSONObject info = new JSONObject();
            List<PageData> AuthorityInfo = organService.getAuthorityInfo(pd);
            for (PageData data : AuthorityInfo) {
                info.put(data.getString("app_menu_code"), data.getString("authority_value"));
            }
            resJson.put("AuthorityInfo", info);
            resJson.put("Result", "Success");
            resJson.put("Error", "");
        }catch (Exception e){
            e.printStackTrace();
            resJson.put("Result", "Fail");
            resJson.put("Error", "程序处理错误，请联系管理员！");
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    private JSONObject getUserInfo(SDUser user) throws JSONException {
        JSONObject userInfo = null;
        if (user != null) {
            userInfo = new JSONObject();
            try {
                userInfo.put("id", user.getId());
                userInfo.put("name", user.getUser_code());
                userInfo.put("tele", user.getUser_mobile_1());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userInfo;
    }

	/**
	 * 进入tab标签
	 * @return
	 */
	@RequestMapping("/tab")
	public ModelAndView tab(){
		ModelAndView mav = new ModelAndView("framework/tab");
		return mav;
	}
	
	/**
	 * 进入首页后的默认页面
	 * @return
	 */
	@RequestMapping("/login_default")
	public ModelAndView defaultPage(){
		ModelAndView mav = new ModelAndView("framework/default");
		return mav;
	}



	/**
	 * 退出系统
	 * @param request
	 * @return
	 */
	@RequestMapping("/outLogin")
	public ModelAndView outLogin(HttpServletRequest request) {
		request.getSession().setAttribute(Const.SESSION_USER, null);
		request.getSession().setAttribute(Const.SESSION_allmenuList, null);
		ModelAndView mav = new ModelAndView("/modules/user/login");
		return mav;
	}

}
