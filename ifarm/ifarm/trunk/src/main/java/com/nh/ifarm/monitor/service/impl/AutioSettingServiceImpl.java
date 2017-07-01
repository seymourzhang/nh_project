package com.nh.ifarm.monitor.service.impl;

import com.nh.ifarm.monitor.service.AutioSettingService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Seymour on 2017/1/11.
 */
@Service
public class AutioSettingServiceImpl implements AutioSettingService {
    @SuppressWarnings("restriction")
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    public PageData saveInfo(PageData pd) throws Exception{
        PageData i = new PageData();
        try{
            JSONObject temp = new JSONObject(pd);
            JSONArray alarmers = temp.optJSONArray("alarmers");
            for (int j = 0; j < alarmers.length(); ++j){
                JSONObject aTmp = alarmers.getJSONObject(j);
                PageData a = new PageData();
                a.put("farmId", pd.get("farmId"));
                a.put("houseId", pd.get("houseId"));
                a.put("remindMethod", pd.get("remindMethod"));
                a.put("userId", aTmp.get("userId"));
                a.put("userOrder", aTmp.get("userOrder"));
                a.put("userType", aTmp.get("userType"));
                dao.delete("AutioSettingMapper.delInfoReminder", a);
                dao.save("AutioSettingMapper.saveInfoReminder", a);
            }
            pd.put("switch_rele_house", "Y");
            pd.put("person_rele_house", "Y");
            pd.put("alarm_rele_house", "N");
            dao.save("AutioSettingMapper.saveInfoSetting", pd);
            dao.save("AutioSettingMapper.saveInfoSwitch", pd);
            dao.delete("AutioSettingMapper.delInfoAlarmCode", pd);
            dao.save("AutioSettingMapper.saveInfoAlarmCode", pd);

            i.put("Result", "Success");
            i.put("Error", "");
        }catch(Exception e){
            e.printStackTrace();
            i.put("Result", "Fail");
            i.put("Error", "程序处理错误，请联系管理员！");
        }
        return i;
    }

    public PageData queryInfo(PageData pd) throws Exception{
        PageData i = new PageData();
        try{
            JSONArray houseAlarmSetting = new JSONArray();
            List<PageData> lpd = (List<PageData>) dao.findForList("AutioSettingMapper.queryInfoSetting", pd);
            if (lpd.size() != 0) {
                for (PageData data : lpd) {
                    JSONArray alarmers = new JSONArray();
                    JSONObject alarmer = new JSONObject();
                    int HouseId = data.getInteger("house_id");
                    pd.put("houseId", HouseId);
                    PageData switchs = (PageData) dao.findForObject("AutioSettingMapper.queryInfoSwitch", pd);
                    if (switchs != null) {
                        alarmer.put("status", switchs.get("status"));
                    } else {
                        alarmer.put("status", "");
                    }
                    List<PageData> users = (List<PageData>) dao.findForList("AutioSettingMapper.queryInfoReminder", pd);
                    if (users.size() != 0) {
                        for (PageData pageData : users) {
                            JSONObject user = new JSONObject();
                            user.put("userOrder", pageData.get("user_order"));
                            user.put("userId", pageData.get("user_id"));
                            user.put("userType", pageData.get("user_type"));
                            alarmers.put(user);
                        }
                    } else {
                        for (int aa = 1; aa < 4; ++aa){
                            JSONObject user = new JSONObject();
                            user.put("userOrder", aa);
                            user.put("userId", 0);
                            user.put("userType", 0);
                            alarmers.put(user);
                        }
                    }
                    alarmer.put("HouseName", data.get("name_cn"));
                    alarmer.put("HouseId", data.get("house_id"));
                    alarmer.put("alarmers", alarmers);
                    houseAlarmSetting.put(alarmer);
                }
                i.put("Result", "Success");
                i.put("Error", "");
            }else{
                /*JSONObject alarmer = new JSONObject();
                for (int aa = 1; aa < 4; ++aa){
                    JSONObject user = new JSONObject();
                    user.put("userOrder", aa);
                    user.put("userId", 0);
                    user.put("userType", 0);
                    alarmers.put(user);
                }
                alarmer.put("HouseName", "");
                alarmer.put("HouseId", 0);
                alarmer.put("status", "");
                alarmer.put("alarmers", alarmers);
                houseAlarmSetting.put(alarmer);*/
                i.put("Result", "Fail");
                i.put("Error", "暂无栋舍信息！");
            }
            i.put("FarmId", pd.get("FarmId"));
            i.put("houseAlarmSetting", houseAlarmSetting);
        }catch(Exception e){
            e.printStackTrace();
            i.put("Result", "Fail");
            i.put("Error", "程序处理错误，请联系管理员！");
        }
        return i;
    }

    public PageData queryImeiInfo(PageData pd) throws Exception {
        return (PageData) dao.findForObject("AutioSettingMapper.queryImeiInfo", pd);
    }
}
