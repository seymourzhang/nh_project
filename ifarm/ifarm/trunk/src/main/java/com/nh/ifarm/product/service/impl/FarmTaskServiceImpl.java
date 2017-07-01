package com.nh.ifarm.product.service.impl;

import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.product.service.FarmTaskService;
import com.nh.ifarm.util.common.Constants;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Seymour on 2016/10/24.
 */
@Service
public class FarmTaskServiceImpl implements FarmTaskService {
    @Resource(name="daoSupport")
    private DaoSupport dao;

    @Autowired
    private BatchManageService batchManageService;


    public void insert(PageData pd) throws Exception{
        dao.save("SBFarmTaskMapper.insertBatch", pd);
    }

    public List<PageData> selectByUserIdOrStatus(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("SBFarmTaskMapper.selectByUserIdOrStatus", pd);
    }

    public int updateTaskStatus(PageData pd) throws Exception{
        return (Integer) dao.update("SBFarmTaskMapper.updateTaskStatus", pd);
    }

    public List<PageData> selectByTashId(PageData pd) throws Exception{
        return (List<PageData>) dao.findForList("SBFarmTaskMapper.selectByTashId", pd);
    }

    public JSONObject selectForMobile(PageData pd) throws Exception{
        PageData putData = new PageData();
        JSONObject resJson = new JSONObject();
        Date curDate = new Date();
        String dealRes = null;
        int FarmId = pd.getInteger("FarmId");
        int HouseId = pd.getInteger("HouseId");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String rdate = pd.get("RemindDate").toString();
        Date remindDate = sdf.parse(rdate);
        pd.put("farm_id", FarmId);
        pd.put("house_code", HouseId);
        PageData batch = batchManageService.selectBatchDataForMobile(pd);

        PageData counts = new PageData();
        if (batch == null){
            resJson.put("Error", "暂无批次信息！");
            resJson.put("Result", "Fail");
        }else {
            Date batchStartDate = sdf.parse(batch.get("operation_date").toString());
            pd.put("remindDate", sdf.format(remindDate));
            List<PageData> temp = new ArrayList<>();
            counts = new PageData();
            if (rdate.equals(sdf.format(curDate))) {
                temp = (List<PageData>) dao.findForList("SBFarmTaskMapper.selectCurrForMobile", pd);
            } else {
                temp = (List<PageData>) dao.findForList("SBFarmTaskMapper.selectHistForMobile", pd);
            }
            JSONArray taskInfos = new JSONArray();
            if (temp.size() != 0) {
                for (PageData data : temp) {
                    JSONObject taskInfo = new JSONObject();
                    taskInfo.put("RemindID", data.get("id"));
                    taskInfo.put("TskId", data.get("task_id"));
                    taskInfo.put("TskType", data.get("task_type"));
                    taskInfo.put("TskName", data.get("task_name"));
                    taskInfo.put("dealStatus", data.get("deal_status"));
                    taskInfos.put(taskInfo);
                }
                resJson.put("Error", "");
                resJson.put("Result", "Success");
                resJson.put("TskInfo", taskInfos);
            } else {
                resJson.put("TskInfo", taskInfos);
                resJson.put("Error", "暂无数据！");
                resJson.put("Result", "Fail");
            }

            pd.put("operationDate", sdf.format(batchStartDate));
            counts = (PageData) dao.findForObject("SBFarmTaskMapper.selectCountForMobile", pd);
        }

        resJson.put("HouseId", HouseId);
        resJson.put("UnCompleteTaskNum", counts == null ? 0 : counts.get("unCompletes"));
        resJson.put("delayCount", counts == null ? 0 : counts.get("delays"));
        resJson.put("RemindDate", rdate);
        dealRes = Constants.RESULT_SUCCESS;
        resJson.put("dealRes", dealRes);
        return resJson;
    }

    public int updateCurStatusForMobile(PageData pd) throws Exception{
        return (Integer) dao.update("SBFarmTaskMapper.updateCurStatusForMobile", pd);
    }

    public int updateHisStatusForMobile(PageData pd) throws Exception{
        return (Integer) dao.update("SBFarmTaskMapper.updateHisStatusForMobile", pd);
    }
}
