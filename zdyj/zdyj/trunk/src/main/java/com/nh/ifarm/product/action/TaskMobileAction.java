package com.nh.ifarm.product.action;

import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.product.service.FarmTaskService;
import com.nh.ifarm.product.service.TaskService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.DealSuccOrFail;
import com.nh.ifarm.util.common.PageData;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.xml.internal.ws.api.pipe.Tube;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.TagUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Seymour on 2016/12/6.
 */
@Controller
@RequestMapping("/taskMobile")
public class TaskMobileAction extends BaseAction {


    @Autowired
    private FarmTaskService farmTaskService;

    @RequestMapping("/taskQuery")
    public void taskQuery(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        PageData pd = new PageData();
        pd = this.getPageData();
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        JSONObject jsonObject = new JSONObject(aa);

        int userId = jsonObject.optInt("id_spa");
        JSONObject tUserJson = jsonObject.getJSONObject("params");
        String RemindDate = tUserJson.optString("RemindDate");
        int HouseId = tUserJson.optInt("HouseId");
        int FarmId = tUserJson.optInt("FarmId");


        pd.put("FarmId", FarmId);
        pd.put("HouseId", HouseId);
        pd.put("RemindDate", RemindDate);
        pd.put("UserId", userId);

        resJson = farmTaskService.selectForMobile(pd);
        dealRes = resJson.get("dealRes").toString();
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }

    @RequestMapping("/taskDeal")
    public void taskDeal(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JSONObject resJson = new JSONObject();
        String dealRes = null;
        PageData pd = new PageData();
        pd = this.getPageData();
        String aa = pd.toString();
        aa = aa.substring(1, aa.length() - 2);
        JSONObject jsonObject = new JSONObject(aa);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        int userId = jsonObject.optInt("id_spa");
        JSONObject tUserJson = jsonObject.getJSONObject("params");
        int FarmId = tUserJson.optInt("FarmId");
        int HouseId = tUserJson.optInt("HouseId");
        int RemindId = tUserJson.optInt("RemindID");
        String RemindDate = tUserJson.optString("RemindDate");
        String TaskName = tUserJson.optString("TaskName");
        String dealStatus = tUserJson.optString("dealStatus");
        Date curDate = new Date();

        pd.put("HouseId", HouseId);
        pd.put("FarmId", FarmId);
        pd.put("id", RemindId);
        pd.put("RemindDate", RemindDate);
        pd.put("dealStatus", dealStatus);
        Date remindDate = sdf.parse(RemindDate);
        int flag = 0;
        if (RemindDate.equals(sdf.format(curDate))){
            flag = farmTaskService.updateCurStatusForMobile(pd);
        }else{
            flag = farmTaskService.updateHisStatusForMobile(pd);
        }
        if (flag == 0){
            resJson.put("Error", "系统不存在该任务！");
            resJson.put("Result", "Fail");
        }else if (flag < 0) {
            resJson.put("Error", "修改失败！");
            resJson.put("Result", "Fail");
        }else if (flag > 0) {
            resJson.put("Error", "");
            resJson.put("Result", "Success");
        }
        dealRes = Constants.RESULT_SUCCESS;
        DealSuccOrFail.dealApp(request, response, dealRes, resJson);
    }
}
