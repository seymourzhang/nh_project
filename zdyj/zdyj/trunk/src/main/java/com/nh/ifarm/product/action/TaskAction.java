package com.nh.ifarm.product.action;

import com.nh.ifarm.product.service.FarmTaskService;
import com.nh.ifarm.product.service.TaskService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.service.OrganService;
import com.nh.ifarm.monitor.action.MonitorAction;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Seymour on 2016/10/18.
 */
@Controller
@RequestMapping("/product")
public class TaskAction extends BaseAction {
    @Autowired
    private OrganService organService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FarmTaskService farmTaskService;

    @RequestMapping("/missionRemindView")
    public ModelAndView missionRemindView(Page page, HttpSession session) throws Exception {
        ModelAndView mav = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        SDUser user = (SDUser) session.getAttribute(Const.SESSION_USER);
        pd.put("user_id", user.getId());
        List<PageData> lpd = organService.selectOrgByUser(pd);
        List<PageData> tasks = getTasks(pd);
        JSONArray a = new JSONArray();
        for (PageData task : tasks) {
            a.put(task);
        }
        mav.addObject("org_name", lpd.size() == 0 ? "暂无数据！" : lpd.get(0).get("name_cn"));
        mav.addObject("task_type", getTaskTypeName());
        mav.addObject("task_code", getTaskCodeName(pd));
        mav.addObject("date_type", getDateType());
        mav.addObject("tasks", a);
        mav.setViewName("modules/product/missionRemind");
        return mav;
    }

    @RequestMapping("/saveAdd")
    public void saveAdd(HttpServletResponse response, HttpSession session) throws Exception {
        Json j = new Json();
        PageData pd = this.getPageData();
        SDUser user = (SDUser) session.getAttribute(Const.SESSION_USER);
        PageData pageData = new PageData();
        pageData.put("user_id", user.getId());
        List<PageData> lpd = organService.selectOrgByUser(pageData);
        pageData.put("task_id", pd.get("taskCode"));
        List<PageData> code = getTaskCodeName(pageData);
        pageData.put("orgIds", lpd.get(0).get("id"));
        List<PageData> tempT = farmTaskService.selectByTashId(pageData);
        if (tempT.size() == 0) {
            for (int i = 0; i < lpd.size(); ++i) {
                PageData temp = new PageData();
                temp.put("org_id", lpd.get(i).get("id"));
                temp.put("org_name", lpd.get(i).get("name_cn"));
                temp.put("task_id", pd.get("taskCode"));
                temp.put("task_name", code.get(0).get("task_name"));
                temp.put("task_type", pd.get("taskType"));
                temp.put("task_status", "Y");
                temp.put("week_age_type", "null");
                temp.put("date_type", pd.get("taskWD"));
                temp.put("date_values", pd.get("dateValues"));
                temp.put("week_group", pd.get("weeks"));
                temp.put("create_person", user.getId());
                temp.put("create_date", new Date());
                temp.put("create_time", new Date());
                temp.put("modify_person", user.getId());
                temp.put("modify_date", new Date());
                temp.put("modify_time", new Date());
                farmTaskService.insert(temp);
            }
            List<PageData> tasks = getTasks(pageData);
            j.setMsg("1");
            j.setObj(tasks);
            j.setSuccess(true);
        }else{
            j.setMsg("您设定的任务，与之前任务相似，请重新设定！");
            j.setSuccess(false);
        }
        super.writeJson(j, response);
    }

    @RequestMapping("/deleteTask")
    public void deleteTask(HttpSession session, HttpServletResponse response) throws Exception {
        Json j = new Json();
        PageData pd = this.getPageData();
        SDUser user = (SDUser) session.getAttribute(Const.SESSION_USER);
        int num = farmTaskService.updateTaskStatus(pd);
        pd.put("user_id", user.getId());
        List<PageData> tasks = getTasks(pd);
        j.setObj(tasks);
        if (num <= 0) {
            j.setMsg("此次更新失败！");
        } else {
            j.setMsg("1");
        }
        super.writeJson(j, response);
    }

    @RequestMapping("/queryTask")
    public void queryTask(HttpSession session, HttpServletResponse response) throws Exception {
        Json j = new Json();
        PageData pd = this.getPageData();
        List<PageData> code = getTaskCodeName(pd);
        j.setMsg("1");
        j.setObj(code);
        super.writeJson(j, response);
    }


    public List<PageData> getTaskTypeName() throws Exception {
        return taskService.getTaskTypeName();
    }

    public List<PageData> getTaskCodeName(PageData pd) throws Exception {
        return taskService.getTaskCodeName(pd);
    }

    public List<PageData> getDateType() throws Exception {
        return taskService.getDateTypeName();
    }

    public List<PageData> getTasks(PageData pd) throws Exception {
        List<PageData> lpd = organService.selectOrgByUser(pd);
        List temp = new ArrayList();
        for (PageData pageData : lpd) {
            temp.add(pageData.get("id").toString());
        }
        String orgIds = MonitorAction.listToString(temp);
        pd.put("orgIds", orgIds);
        List<PageData> tasks = farmTaskService.selectByUserIdOrStatus(pd);
        for (PageData task : tasks) {
            String weekGroup = task.get("week_group").toString();
            String[] weeks = weekGroup.split(",");
            List temp1 = new ArrayList();
            for (String week : weeks) {
                if (week.equals("1")){
                    temp1.add("周一");
                }else if (week.equals("2")){
                    temp1.add("周二");
                }else if (week.equals("3")){
                    temp1.add("周三");
                }else if (week.equals("4")){
                    temp1.add("周四");
                }else if (week.equals("5")){
                    temp1.add("周五");
                }else if (week.equals("6")){
                    temp1.add("周六");
                }else if (week.equals("7")){
                    temp1.add("周七");
                }
            }
            task.put("week_group", temp1.toString().replace("[", "").replace("]", ""));
        }
        return tasks;
    }
}
