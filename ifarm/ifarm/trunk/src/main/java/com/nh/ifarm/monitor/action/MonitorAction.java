package com.nh.ifarm.monitor.action;

import com.nh.ifarm.monitor.service.MonitorService;
import com.nh.ifarm.monitor.service.OperationService;
import com.nh.ifarm.monitor.service.UserFilterService;
import com.nh.ifarm.monitor.service.VideoMonitorService;
import com.nh.ifarm.system.service.CodeService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.*;
import com.nh.ifarm.util.service.ModuleService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: MonitorAction
 * @Date 2016-6-30
 * @author Raymon
 * 监控模块控制类
 */
@Controller
@RequestMapping("/monitor")
public class MonitorAction extends BaseAction{
	@Autowired
	private CodeService codeService;
    @Autowired 
	private MonitorService monitorService;

	@Autowired
	private VideoMonitorService VideoMonitorService;

	@Autowired
	private UserFilterService userFilterService;

    @Autowired
    private OperationService operationService;

	static final String monitorCurList="monitorCurList";

	@Autowired
	private ModuleService moduleService;

	@RequestMapping("/showMonitor")
	public ModelAndView showMonitor(String id,String pid) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		List<PageData> mcl = getList(pd);
		mv.setViewName("/modules/monitor/monitor");
		mv.addObject(monitorCurList,mcl);
		mv.addObject("farmList",getFarmList());
		mv.addObject("houseList",getHouseList(pd));
		String mi = PubFun.getPropertyValue("Monitor.Index");
		mv.addObject("monitorIndex",mi.replace(" ","").toString());
		mv.addObject("pd",pd);
		return mv;
	}
	
	@RequestMapping("/showMonitorBatch")
	public ModelAndView showMonitorBatch(HttpSession session) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		String tabId = pd.getString("tabId");
		pd.put("user_id",user.getId());
		mv.setViewName("/modules/monitor/monitorBatch");
		List<PageData> categoryTypeList = monitorService.selectCategoryType(pd);
		String categoryType= "";
		for(int i =0;i<categoryTypeList.size();i++){
			pd.put("categoryType"+categoryTypeList.get(i).get("biz_code").toString(),categoryTypeList.get(i).get("biz_code").toString());
//			categoryType = categoryType+ categoryTypeList.get(i).get("biz_code").toString();
//			if(i+1<categoryTypeList.size()){
//				categoryType = categoryType+",";
//			}
		}
//		mv.addObject("categoryTypeList",categoryType);

//		//获取tab页面与对应代码之间的匹配关系,用于计算显示
//		String tabCodeMapString = "父母代育成:1,父母代产蛋:2,商品代育成:3,商品代产蛋:4,白羽肉鸡:5,黄羽肉鸡:6,肉杂鸡:7,育成:8,产蛋:9,肉鸡:10";//PubFun.getPropertyValue("tab-code-map");
//		//数据处理:将得到的字符串数据转换成需要的map数据结构
//		HashMap<String, PageData> tabCodeMap = PubFun.getConstantMap(tabCodeMapString);
		//获取当前页面需要显示的tab配置
		PageData pdCodeFeedType = codeService.getCodeForFeedType(pd);
		if(pdCodeFeedType == null){
			mv.setViewName("modules/util/addfarm");
			return mv;
		}
		String mi = pdCodeFeedType.getString("feed_type");
		String biz_code = pdCodeFeedType.getString("mapping_type");
		List<String> bizCodeList = PubFun.getConstantList(biz_code);
		String parentIdString = pdCodeFeedType.getString("tabParentId");
		List<String> parentIdList = PubFun.getConstantList(parentIdString);
		String productionReportShowTabString = mi;//PubFun.getPropertyValue("Batch.Feed.Type");
		List<String> tabNameList = PubFun.getConstantList(pdCodeFeedType.getString("tabName"));
		//数据处理:将得到的字符串数据转换成需要的list数据结构
		List<String> productionReportShowTabList = PubFun.getConstantList(productionReportShowTabString);
		//处理数据:得到需要显示的tab也的具体信息,返回到前端页面
		List<PageData> tabList = new ArrayList<PageData>();
		for (int i = 0; i < productionReportShowTabList.size(); i++) {
			String tabCode = productionReportShowTabList.get(i);
			String tabName = tabNameList.get(i);
			String bizCode = bizCodeList.get(i);
			String parentId = parentIdList.get(i);
			PageData tab = new PageData();
			tab.put("name", tabName);
			tab.put("code", tabCode);
			tab.put("bizCode", bizCode);
			tab.put("parentId", parentId);
			tabList.add(tab);
		}
		pd.put("tabList", tabList);

		//是否页面跳转,传递tabID
		if("".equals(tabId)){
			//如果不是页面跳转,那么判断页面tab是否存在肉鸡,如果存在肉鸡,则优先显示肉鸡,否则默认显示第一个
			if(mi.contains("10")){
				pd.put("tabId", "10");
			}else{
				pd.put("tabId", tabList.get(0).getString("code"));
			}
		}else{
			if(!mi.contains(tabId)){
				pd.put("tabId", tabList.get(0).getString("code"));
			}
		}
		//如果存在tabID参数,则需要传入页面初始化需要的其他两个参数:种类:bizCode,大类:parentId
//		if(null != pd.get("tabId")){
			String tabIdIndex = pd.getString("tabId");
			for (int i = 0; i < tabList.size(); i++) {
				if(tabIdIndex.equals(tabList.get(i).getString("code"))){
					pd.put("bizCode",tabList.get(i).getString("bizCode"));
					pd.put("parentId",tabList.get(i).getString("parentId"));
				}
			}
//		}

		mv.addObject("pd",pd);
		return mv;
	}

	@RequestMapping("/reflushMonitor")
	public void reflushMonitor(HttpServletResponse response, HttpSession session) throws Exception{
		Json j=new Json();
		//			Gson gson = new Gson();
//			String str = gson.toJson(MonitorCurrList);
//			arg1.getWriter().print(str);
		PageData pd = this.getPageData();
		PageData pageData = new PageData();
		List<PageData> mcl = new ArrayList<PageData>();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		pd.put("user_id",user.getId());
		if ("true".equals(pd.get("checked"))) {
			pageData.put("user_code", user.getId());
			pd = getMonitorSet(pageData);
			String farmIdGroup = pd.get("farm_id_group").toString();
			String houseIdGroup = pd.get("house_id_group").toString();
			pd = new PageData();
			List tempFarm = Arrays.asList(farmIdGroup.split(","));
			List tempHouse = Arrays.asList(houseIdGroup.split(","));
			pd.put("farmId", tempFarm);
			pd.put("houseId", tempHouse);
			mcl = getList(pd);
		} else if ("false".equals(pd.get("checked"))) {
			List tempFarm = new ArrayList();
			List tempHouse = new ArrayList();
			if (pd.get("farmId") != null) {
				tempFarm.add(pd.get("farmId"));
				pd.clear();
				pd.put("farmId", tempFarm);
			} else if (pd.get("houseId") != null){
				tempHouse.add(pd.get("houseId"));
				pd.clear();
				pd.put("houseId", tempHouse);
			} else if (pd.get("farmId") != null && pd.get("houseId") != null){
				tempHouse.add(pd.get("houseId"));
				tempFarm.add(pd.get("farmId"));
				pd.clear();
				pd.put("houseId", tempHouse);
				pd.put("farmId", tempFarm);
			}
			mcl = getList(pd);
		}
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}

	@RequestMapping("/reflushMonitor2")
	public void reflushMonitor2(HttpServletResponse response) throws Exception{
		Json j=new Json();
		//			Gson gson = new Gson();
//			String str = gson.toJson(MonitorCurrList);
//			arg1.getWriter().print(str);
		PageData pd = this.getPageData();
		List<PageData> mcl = getHouseList(pd);
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/reflushMonitorBatch")
	public void reflushMonitorBatch(HttpServletResponse response, HttpSession session) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		pd.put("user_id",user.getId());
		List<PageData> mcl = monitorService.selectMonitorBatch(pd);

		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}


	@RequestMapping("/responseall")
	public void responseAll(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String dealRes = null ;
		JSONObject jfm=new JSONObject();
		List<PageData> mcl = monitorService.selectAllForMobile();
		if(mcl!=null){
			jfm.put("monitor", mcl);
			jfm.put("Result", "Success");
			dealRes = Constants.RESULT_SUCCESS;
		}else{
			dealRes = Constants.RESULT_FAIL ;
			jfm.put("ErrorMsg","未查询到数据！");
		}
		DealSuccOrFail.dealApp(request,response,dealRes,jfm);
	}

	@RequestMapping("/videoMonitor")
	public ModelAndView videoMonitor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		List<PageData> mcl = getList(pd);
		mv.setViewName("/modules/monitor/videoMonitor");
		mv.addObject(monitorCurList,mcl);
		mv.addObject("pd", pd);
		mv.addObject("farmList",getFarmList());
		mv.addObject("houseList",getHouseList(pd));
		return mv;
	}

	@RequestMapping("/getVideoHouse")
	public void getVideoHouse(HttpServletRequest request, HttpServletResponse response) throws  Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl = VideoMonitorService.selectByCondition(pd);
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}

	@RequestMapping("/getHouse")
	public void getHouse(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl = getHouseList(pd);
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}

	@RequestMapping("/monitorSet")
	public ModelAndView monitorSet(HttpSession session) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();

		mv.setViewName("/modules/monitor/monitorSet");
		mv.addObject("pd", pd);
		return mv;
	}

    @RequestMapping("/getOrgBySetted")
    public void getOrgBySetted(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
        Json j=new Json();
        PageData pd = new PageData();
        SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
        pd = this.getPageData();
        /*List<PageData> orglist= moduleService.service("organServiceImpl", "getOrgListByRoleId", new Object[]{pd});*/
        //获取架构全部
        PageData pd2 = new PageData();
        pd2.put("user_id", user.getId());
        List<PageData> orgAll= operationService.selectAll(pd2);
        //获取设定id
        pd2.put("user_code", user.getId());
        PageData mcl = getMonitorSet(pd2);
        List boss = new ArrayList();
        if(mcl!=null){
        List<String> houseArrays = Arrays.asList(mcl.get("house_id_group").toString().split(","));
        pd = new PageData();
        pd.put("level_id", 3);
        pd.put("groupId", houseArrays);
        List<PageData> houseId = operationService.selectByCondition(pd);
        List<String> farmArrays = Arrays.asList(mcl.get("farm_id_group").toString().split(","));
        pd = new PageData();
        pd.put("level_id", 2);
        pd.put("groupId", farmArrays);
        List<PageData> farmId = operationService.selectByCondition(pd);
        
        boss.addAll(farmId);
        boss.addAll(houseId);
        //获取农场以上层级id
        List<PageData> temps = modifySet(mcl);
        boss.addAll(temps);
        }
        //处理组织架构数据
        List<PageData> list = new ArrayList<PageData>();
        for (PageData pageData : orgAll) {
            PageData data=new PageData();
            data.put("id", pageData.getInteger("id") + "");
            data.put("pId", pageData.getInteger("parent_id")+ "");
            data.put("name", pageData.getString("name_cn") + "");
            data.put("open", "true");
//			data.put("chkDisabled", "true");
            int tmp = 0;
            if(mcl != null){
            List<PageData> ss = new ArrayList<PageData>(boss);
            for (PageData p2 : ss) {
                if(p2.get("id").equals(pageData.getInteger("id"))){
                    tmp = 1;
                    break;
                }
            }
            if (tmp == 1) {
                data.put("checked", "true");
            } else {
                data.put("checked", "false");
            }
            }else{
            	data.put("checked", "false");
            }
            list.add(data);
        }
        j.setSuccess(true);
        j.setObj(list);
        super.writeJson(j, response);
    }

	@RequestMapping("/saveSet")
	public void saveSet(HttpServletResponse response, HttpSession session) throws Exception {
        Json j = new Json();
        SDUser user = (SDUser) session.getAttribute(Const.SESSION_USER);
        PageData pd = new PageData();
        pd = this.getPageData();

        pd.put("user_code", user.getId());
        pd.put("create_date", new Date());
        pd.put("create_time", new Date());
        try {
            //根据id查询organization_id存入
            String temp = pd.get("groupId").toString();
            List<String> ids = Arrays.asList(temp.split(","));
            PageData pdTemp = new PageData();
            pdTemp.put("groupId", ids);
            List<PageData> list = operationService.selectByConditionMy(pdTemp);
            List<String> farmIdGroup = new ArrayList<String>();
            List<String> houseIdGroup = new ArrayList<String>();
            for (PageData pageData : list) {
                if ("3".equals(pageData.get("level_id").toString())) {
                    farmIdGroup.add(pageData.get("organization_id").toString());
                } else if ("4".equals(pageData.get("level_id").toString())) {
                    houseIdGroup.add(pageData.get("organization_id").toString());
                }
            }
            //存入设定
//            PageData end = new PageData();
            pd.put("farm_id_group", listToString(farmIdGroup));
            pd.put("house_id_group", listToString(houseIdGroup));

            PageData mcl = userFilterService.selectByCondition(pd);
            if (mcl.size() == 0) {
                userFilterService.saveSet(pd);
            } else {
                userFilterService.updateSet(pd);
            }
            j.setMsg("1");
            j.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg("2");
        }
        super.writeJson(j, response);
    }

	PageData getMonitorSet(PageData pd) throws Exception{
		PageData mcl;
		mcl = userFilterService.selectByCondition(pd);
		return mcl;
	}

	/**
	 * 获取数据列表
	 * @param pd 数据对象
	 * @return 数据列表
     */
	List<PageData> getList(PageData pd) throws Exception {
		List<PageData> mcl;
		if(null != pd.get("org_id") || pd.get("farmId")!=null || pd.get("houseId")!=null) {
			mcl = monitorService.selectByCondition(pd);
		} else {
			mcl = monitorService.selectAll(pd);
		}
		return mcl;
	}

	/**
	 * 查询组织架构method
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	/*List<PageData> getStructure(PageData pd) throws Exception {
		List<PageData> mcl;
		mcl = userFilterService.selectByConditionForSelect(pd);
		return mcl;
	}*/

	/***
	 * 查询编辑设定
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> modifySet(PageData pd) throws Exception {
		String msd = pd.get("farm_id_group").toString();
		List msdArray = Arrays.asList(msd.split(","));
		PageData temp = new PageData();
		temp.put("farmId", msdArray);
		List<PageData> lpd = operationService.selectForUp(temp);
		return lpd;
	}

	/**
	 * 获取农场信息
	 * @param
	 * @return 数据列表
     */
	List<PageData> getFarmList() throws Exception {
		List<PageData> mcl;
			mcl = moduleService.service("farmServiceImpl", "selectAll", null);//farmService.selectAll();
		return mcl;
	}
	
	/**
	 * 获取栋舍信息
	 * @param pd 数据对象
	 * @return 数据列表
     */
	List<PageData> getHouseList(PageData pd) throws Exception {
		List<PageData> mcl;
			mcl = moduleService.service("farmServiceImpl", "selectHouseByCondition", new Object[]{pd});//farmService.selectHouseAll();
		return mcl;
	}

	/***
	 * list转string
	 * @param stringList
	 * @return
	 */
	public static String listToString(List<String> stringList){
        if (stringList==null) {
			return null;
		}
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
			if (flag) {
				result.append(",");
			} else {
				flag = true;
			}
			result.append(string);
		}
        return result.toString();
    }

}
