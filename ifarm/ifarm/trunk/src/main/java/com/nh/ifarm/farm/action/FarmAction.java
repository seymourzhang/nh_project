package com.nh.ifarm.farm.action;

import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.farm.service.FarmService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.service.ModuleService;
import com.nh.ifarm.util.service.OrganService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName: FarmAction
 * @Date 2016-7-26
 * @author Varro
 * 农场管理控制类
 */
@Controller
@RequestMapping("/farm")
public class FarmAction extends BaseAction{
    @Autowired 
	private FarmService farmService;

	@Autowired
	private OrganService organService;
    
    @Autowired
	private ModuleService moduleService;
    
    @Autowired
    private BatchManageService batchManageService;

	@RequestMapping("/houseView")
	public ModelAndView houseView(Page page,HttpSession session) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		pd.put("user_id",user.getId());
//		/**栋舍信息-Varro-2016-7-26-**/
//		List<PageData> hoList= farmService.findHouse(pd);
//		List<PageData> houseList=new ArrayList<PageData>();
//		for (PageData pageData : hoList) {
//			PageData paDate = new PageData();
//			paDate.put("house_id", pageData.getInteger("id"));
//			List<PageData> houseDev=farmService.findHouseDevice(paDate);
//			String deviceID ="";
//			String deviceName ="";
//			for (int i = 0; i < houseDev.size(); i++) {
//				if((i+1)==houseDev.size()){
//					deviceID+=houseDev.get(i).getString("device_code");
//					deviceName+=houseDev.get(i).getString("device_factory")+"（设备号:"+houseDev.get(i).getString("device_code")+", 端口号:"+houseDev.get(i).getString("port_id")+"）";
//				}else{
//					deviceID+=houseDev.get(i).getString("device_code")+",";
//					deviceName+=houseDev.get(i).getString("device_factory")+"（设备号:"+houseDev.get(i).getString("device_code")+", 端口号:"+houseDev.get(i).getString("port_id")+"）,";
//				}
//			}
//			pageData.put("deviceID", deviceID);
//			pageData.put("deviceName", deviceName);
//			houseList.add(pageData);
//
//		}
//		mv.addObject("SDHouseList",houseList);
		if(!StringUtils.isBlank(pd.getString("write_read"))){
			session.setAttribute("write_read", pd.getString("write_read"));
		}else{
			pd.put("write_read", session.getAttribute("write_read"));
		}
		mv.addObject("pd",pd);
		mv.setViewName("/modules/farm/houseView");
		return mv;
	}
	
	@RequestMapping("/searchHouse")
	public void searchHouse(HttpServletResponse response,HttpSession session) throws Exception{
		Json j=new Json();
		PageData pd = new PageData();
		pd = this.getPageData();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		pd.put("user_id",user.getId());
		/**栋舍信息**/
		List<PageData> hoList= farmService.findHouse(pd);
		List<PageData> houseList=new ArrayList<PageData>();
		for (PageData pageData : hoList) {
			PageData paDate = new PageData();
			paDate.put("house_id", pageData.getInteger("id"));
			List<PageData> houseDev=farmService.findHouseDevice(paDate);
			String deviceID ="";
			String deviceName ="";
			for (int i = 0; i < houseDev.size(); i++) {
				if((i+1)==houseDev.size()){
					deviceID+=houseDev.get(i).getString("device_code");
					deviceName+=houseDev.get(i).getString("device_factory")+"（设备号:"+houseDev.get(i).getString("device_code")+", 端口号:"+houseDev.get(i).getString("port_id")+"）";
				}else{
					deviceID+=houseDev.get(i).getString("device_code")+",";
					deviceName+=houseDev.get(i).getString("device_factory")+"（设备号:"+houseDev.get(i).getString("device_code")+", 端口号:"+houseDev.get(i).getString("port_id")+"）,";
				}
			}
			pageData.put("deviceID", deviceID);
			pageData.put("deviceName", deviceName);
			houseList.add(pageData);

		}
		j.setSuccess(true);
		j.setObj(houseList);
		super.writeJson(j, response);
	}

	@RequestMapping("/farmView")
	public ModelAndView farmView(Page page,HttpSession session) throws Exception {
		ModelAndView mv = this.getModelAndView();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id",user.getId());
		/**农场信息-Varro-2016-7-26-**/
//		List<PageData> farmList= farmService.findFarm(pd);
//		mv.addObject("SDFarmList",farmList);
		

//
//		/**批次信息-Varro-2016-7-26-**/
//		page.setPd(pd);
//		List<PageData> balist = farmService.findBatchlistPage(page);
//		List<PageData> batchList=new ArrayList<PageData>();
//		for (PageData pageData : balist) {
//			PageData paDate = new PageData();
//			paDate.put("operation_type", "1");
//			paDate.put("batch_no", pageData.getString("batch_no"));
//			paDate.put("farmId",pageData.get("farm_id"));
//			paDate.put("houseId", pageData.get("house_code"));
//			List<PageData> bblist= farmService.selectBatchByCondition(paDate);
//			if(bblist.size()!=0){
//				pageData.put("batch_flag", "1");
//			}
//			if(bblist.size()!=0 && Integer.parseInt(pageData.get("operation_type").toString())==2){
//			   continue;
//			}
//			batchList.add(pageData);
//		}
		if(!StringUtils.isBlank(pd.getString("write_read"))){
			session.setAttribute("write_read", pd.getString("write_read"));
		}else{
			pd.put("write_read", session.getAttribute("write_read"));
		}
		mv.addObject("pd",pd);
//		mv.addObject("SDBatchList",batchList);
		
		mv.setViewName("/modules/farm/farmView");
		return mv;
	}
	
	@RequestMapping("/searchFarm")
	public void searchFarm(HttpServletResponse response,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id",user.getId());
		/**农场信息**/
		List<PageData> farmList= farmService.findFarm(pd);	
		j.setSuccess(true);
		j.setObj(farmList);
		super.writeJson(j, response);
	}
	
	/**
	 * 跳转到新增农场信息页面
	 * varro 2016-7-9
	 * @return
	 */
	@RequestMapping(value="/addFarmUrl")
	public ModelAndView addFarmUrl(HttpServletResponse response,HttpServletRequest request,HttpSession session)throws Exception{
		ModelAndView mv = this.getModelAndView();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		/***查询饲养方式***/
		pd.put("code_type", "FEED_METHOD");
		List<PageData> feedMethod= farmService.findCode(pd);
		
		/***查询饲养品种***/
		pd.put("code_type", "FEED_TYPE");
		List<PageData> feedType= farmService.findCode(pd);
		
		/***查询农场类型***/
		pd.put("code_type", "HOUSE_TYPE");
		List<PageData> farmType= farmService.findCode(pd);
		
		/******查询省份**********/
		pd.put("parent_id", 0);
		pd.put("level", 1);
		List<PageData> prlist= farmService.findAreaChina(pd);
		mv.addObject("pd",pd);
		mv.addObject("feedMethod",feedMethod);
		mv.addObject("feedType",feedType);
		mv.addObject("farmType",farmType);
		mv.addObject("prlist",prlist);
		pd.put("level_id", 1);
		pd.put("user_id",user.getId());
		List<PageData> corporationList= farmService.selectOganizationList(pd);
		mv.addObject("corporationList",corporationList);
		mv.setViewName("modules/farm/addFarm");
		return mv;
	}
	
	/**
	 * 跳转到修改农场页面
	 * varro 2016-7-9
	 * @return
	 */
	@RequestMapping(value="/editFarmUrl")
	public ModelAndView editFarmUrl(HttpServletResponse response,HttpServletRequest request,HttpSession session)throws Exception{
		ModelAndView mv = this.getModelAndView();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> farmList=farmService.findFarm(pd);
		PageData farm=farmList.get(0);
		String farmName = farm.getString("farm_name_chs");
		int r = farmName.indexOf("(");
		if(r!=-1){
		farm.put("farm_name_body",farmName.substring(0,farmName.indexOf("(")));
		farm.put("farm_name_extend",farmName.substring(farmName.indexOf("("),farmName.length()));
		}else{
			farm.put("farm_name_body",farmName);
			farm.put("farm_name_extend","");
		}
		/***查询饲养方式***/
		pd.put("code_type", "FEED_METHOD");
		List<PageData> feedMethod= farmService.findCode(pd);
		
		/***查询饲养品种***/
		pd.put("code_type", "FEED_TYPE");
		List<PageData> feedType= farmService.findCode(pd);
		
		/***查询农场类型***/
		pd.put("code_type", "HOUSE_TYPE");
		List<PageData> farmType= farmService.findCode(pd);
		
		PageData paramPd = new PageData();
		paramPd.put("farm_id", farm.get("id"));
		List<PageData> batchCurrList = batchManageService.getCreateBatchData(paramPd);
		if(batchCurrList.size() != 0){
			farm.put("dis", 1);
		}else{
			farm.put("dis", 0);
		}
		
		/******查询省份**********/
		pd.put("parent_id", 0);
		pd.put("level", 1);
		List<PageData> prlist= farmService.findAreaChina(pd);
		mv.addObject("pd",pd);
		mv.addObject("farmData",farm);
		mv.addObject("feedMethod",feedMethod);
		mv.addObject("feedType",feedType);
		mv.addObject("farmType",farmType);
		mv.addObject("prlist",prlist);
		pd.put("level_id", 1);
		pd.put("user_id",user.getId());
		List<PageData> corporationList= farmService.selectOganizationList(pd);
		mv.addObject("corporationList",corporationList);
		mv.setViewName("modules/farm/editFarm");
		return mv;
	}
	
	
	@RequestMapping("/getAreaChina")
	public void getAreaChina(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> prlist= farmService.findAreaChina(pd);
		j.setSuccess(true);
		j.setObj(prlist);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/isFarmNameNull")
	public void isFarmNameNull(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl = farmService.selectByCondition(pd);
		if(mcl!=null&&mcl.size()!=0){
			j.setMsg("1");//用户名已经存在
			j.setSuccess(true);
		}else{
			j.setMsg("2");// 用户名不存在
		}
		super.writeJson(j, response);
	}
	
	@RequestMapping("/isBatchNoNull")
	public void isBatchNoNull(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl= farmService.selectBatchByCondition(pd);
		if(mcl!=null&&mcl.size()!=0){
			j.setMsg("1");//批次号已经存在
			j.setSuccess(true);
		}else{
			j.setMsg("2");// 批次号不存在
		}
		super.writeJson(j, response);
	}
	@RequestMapping("/isBatchHouseNull")
	public void isBatchHouseNull(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		PageData mcl= farmService.isBatchHouseNull(pd);
		if(mcl!=null&& mcl.getString("operation_type").equals("2")){
			j.setMsg("1");//该栋舍存在未出栏的批次
			j.setSuccess(true);
		}else{
			j.setMsg("2");// 
		}
		super.writeJson(j, response);
	}
	
	
	
//
//	/**
//	 * 保存批次
//	 * @return
//	 */
//	@RequestMapping("/addBatch")
//	public void addBatch(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
//		Json j=new Json();
//		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		pd.put("operation_type","2");
//		pd.put("create_person",user.getId());
//		pd.put("create_date", new Date());
//		pd.put("create_time", new Date());
//		try {
//			if(pd.get("count").equals("")){
//				pd.put("count", null);
//			}
//			farmService.saveBatch(pd);
//			j.setMsg("1");
//			j.setSuccess(true);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			j.setMsg("2");
//		}
//		super.writeJson(j, response);
//	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping("/addFarm")
	public void addFarm(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		
		pd.put("id",0);
		pd.put("freeze_status",0);
		pd.put("create_person",user.getId());
		pd.put("create_date", new Date());	
		pd.put("create_time", new Date());
		pd.put("modify_person",user.getId());
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());

		try {
			String[] orgStr = pd.getString("organizationId").split(",");
			if(orgStr.length>1){
				String orgParentId = orgStr[0];
				String orgLevelId = orgStr[1];
				pd.put("parent_id",orgParentId);
				pd.put("level_id",orgLevelId);
				farmService.saveFarm(pd);
				//			pd.put("id",pd.getInteger("id"));
				//			pd.put("farm_code",pd.getInteger("id"));
				//			farmService.editFarm(pd);
				j.setMsg("1");
				j.setSuccess(true);
			} else{
				j.setMsg("2");
				j.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("2");
		}
		super.writeJson(j, response);
	}
	/**
	 * 修改农场
	 * @return
	 */
	@RequestMapping("/editFarm")
	public void editFarm(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();

		pd.put("freeze_status",0);
		pd.put("modify_person",user.getId());
		pd.put("modify_date", new Date());
		pd.put("modify_time", new Date());
		try {
			farmService.editFarm(pd);
			pd.put("org_id", pd.get("id"));
			pd.put("org_name_updated", pd.get("farm_name_chs")+"("+pd.get("id")+")");
			organService.updateOrg(pd);
			pd.put("farmId", pd.get("id"));
			List<PageData> houseList = getHouseList(pd);
			if(houseList.size() != 0){
				for(PageData house : houseList){
					house.put("house_type", pd.get("farm_type"));
					farmService.editHouse(house);
				}
			}
			j.setMsg("1");
			j.setSuccess(true);
			
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("2");
		}
		super.writeJson(j, response);
	}
	
	/**
	 * 删除农场
	 * @return
	 */
	@RequestMapping("/delFarm")
	public void delFarm(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("freeze_status",1);
		pd.put("modify_person",user.getId());
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());
		String farmId = pd.getString("id");
		PageData farmPd = new PageData();
		try {
				farmPd.put("farmId",farmId);
				List<PageData> houseList = farmService.findHouse(farmPd);
				if(houseList.size()>0){
					j.setMsg("请先删除该农场下的栋舍");
					j.setSuccess(false);
				} else{
					farmService.editFarm(pd);
					List<Integer> orgList = new ArrayList<Integer>();
					orgList.add(Integer.valueOf(farmId));
					pd.put("orgList",orgList);
					pd.put("freeze_status",1); //删除标志位
					int i = organService.deleteOrg(pd);
					if(i==1){
						j.setMsg("删除成功！");
						j.setSuccess(true);
					} else{
						j.setMsg("机构关系删除失败");
						j.setSuccess(false);
					}
				}

		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		super.writeJson(j, response);
	}
	/**
	 * 跳转到新增栋舍信息页面
	 * varro 2016-7-9
	 * @return
	 */
	@RequestMapping(value="/addHouseUrl")
	public ModelAndView addHouseUrl(HttpServletResponse response,HttpServletRequest request,HttpSession session)throws Exception{
		ModelAndView mv = this.getModelAndView();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id",user.getId());
		/***栋舍类型***/
		pd.put("code_type", "HOUSE_TYPE");
		List<PageData> houseType= farmService.findCode(pd);
//		List<PageData> device= farmService.findDevice(pd);
		mv.addObject("pd",pd);
		mv.addObject("houseType",houseType);
//		mv.addObject("device",device);
//		mv.addObject("farmList",getFarmList(pd));
		mv.addObject("farmList",farmService.findFarm(pd));
		mv.setViewName("modules/farm/addHouse");
		return mv;
	}
	
	/**
	 * 跳转到修改栋舍信息页面
	 * varro 2016-7-9
	 * @return
	 */
	@RequestMapping(value="/editHouseUrl")
	public ModelAndView editHouseUrl(HttpServletResponse response,HttpServletRequest request,HttpSession session)throws Exception{
		ModelAndView mv = this.getModelAndView();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id",user.getId());
		List<PageData> houselist=farmService.findHouse(pd);
		PageData houselData=houselist.get(0);
		
		/***栋舍类型***/
		pd.put("code_type", "HOUSE_TYPE");
		List<PageData> houseType= farmService.findCode(pd);
		pd.put("house_id", pd.getString("id"));
		List<PageData> device= farmService.findDevice(pd);
		mv.addObject("pd",pd);
		mv.addObject("houselData",houselData);
		mv.addObject("houseType",houseType);
		mv.addObject("device",device);
		mv.addObject("farmList",getFarmList(pd));
		mv.setViewName("modules/farm/editHouse");
		return mv;
	}


	/**
	 * 跳转到绑定栋舍与设备页面
	 * raymon 2016-11-28
	 * @return
	 */
	@RequestMapping(value="/setDeviceHouseRelation")
	public ModelAndView setDeviceHouseRelation()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		List<PageData> houselist=organService.getOrgListById(pd);
		PageData houseData=houselist.get(0);
		mv.addObject("houseData",houseData);
		mv.addObject("pd",pd);
		mv.setViewName("modules/farm/setDeviceHouseRelation");
		return mv;
	}

	/**
	 * 获取设备清单
	 * @return
	 */
	@RequestMapping("/getDeviceList")
	public void getDeviceList(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> deviceList = farmService.findDevice(pd);
		j.setSuccess(true);
		j.setObj(deviceList);
		super.writeJson(j, response);
	}

	/**
	 * 获取传感器清单
	 * @return
	 */
	@RequestMapping("/getSensorList")
	public void getSensorList(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		pd.put("sensor_code","1000");
		List<PageData> deviceList = farmService.findSensor(pd);
		j.setSuccess(true);
		j.setObj(deviceList);
		super.writeJson(j, response);
	}

	/**
	 * 获取传感器清单
	 * @return
	 */
	@RequestMapping("/delDevice")
	public void delDevice(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		int i = farmService.delDevice(pd);
		i =  farmService.delSensor(pd);
		j.setSuccess(true);
		super.writeJson(j, response);
	}

	/**
	 * 设定传感器
	 * @return
	 */
	@RequestMapping("/setSensorLocation")
	public void setSensorLocation(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		pd.put("sensor_code","1000");
		pd.put("create_person",user.getId());
		int i = farmService.delSensor(pd);
		i = farmService.insertSensor(pd);
		j.setSuccess(true);
		super.writeJson(j, response);
	}


	/**
	 * 绑定栋舍与设备
	 * @return
	 */
	@RequestMapping("/mappingDevice")
	public void mappingDevice(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		pd.put("sensor_code","1000");
		pd.put("create_person",user.getId());
		PageData paramPd = new PageData();
		paramPd.put("device_code",pd.get("device_code"));
		String msg ="";
		List<PageData> deviceList = farmService.findDevice(paramPd);
		if(deviceList.size()>0){
			j.setSuccess(false);
			msg="该设备已绑定栋舍";
		} else{
			int i = farmService.mappingDevice(pd);
			if(i ==1){
				j.setSuccess(true);
			} else{
				j.setSuccess(false);
				msg="无该编号的设备";
			}

		}
		j.setMsg(msg);
		super.writeJson(j, response);
	}

	/**
	 * 保存
	 * @return
	 */
	@RequestMapping("/addHouse")
	public void addHouse(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		
		pd.put("id",0);
		pd.put("house_code","0");
		pd.put("freeze_status","0");
		pd.put("create_person",user.getId());
		pd.put("create_date", new Date());	
		pd.put("create_time", new Date());
		pd.put("modify_person",user.getId());
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());
		try {
			farmService.saveHouse(pd);
			int house_id=pd.getInteger("id");
			pd.put("id",house_id);
			pd.put("house_code",house_id);
			pd.put("user_id",user.getId());

			List<PageData> maxLevelList = organService.getMaxOrgLevelId(null);
			int maxOrgLevelId = Integer.valueOf(String.valueOf(maxLevelList.get(0).get("max_level_id")));
			pd.put("level_id",maxOrgLevelId+1);

			int k = organService.setHouseMapping(pd);
			if(k > 0){
					PageData paramPd = new PageData();
					Integer objId = (Integer)pd.get("id");
					paramPd.put("obj_id",objId);
					paramPd.put("obj_type",2);
					paramPd.put("create_person",user.getId());
					paramPd.put("user_id",user.getId());
					moduleService.service("roleServiceImpl","insertRightsObj",new Object[]{paramPd});

					pd.put("id",pd.get("house_code"));
					farmService.editHouse(pd);
					/**
					 * 新增栋舍警报
					 */
					for (int i = 0; i < 4; i++) {
						pd.put("house_id",pd.getInteger("id"));
						pd.put("alarm_type",i+1);
						farmService.saveHouseAlarm(pd);
					}

					j.setMsg("1");
					j.setSuccess(true);
			} else{
				j.setSuccess(false);
				j.setMsg("系统内部错误");
			}



//			/**
//			 * 新增栋舍和设备关系
//			 */
//			if(!StringUtils.isBlank(pd.getString("deviceKey"))){
//				String [] arr=pd.getString("deviceKey").split(",");
//				for (int k = 0; k < arr.length; k++) {
//					pd.put("device_code", arr[k]);
//					List<PageData> device= farmService.findDevice(pd);
//					PageData da=device.get(0);
//					pd.put("device_code", da.getString("device_code"));
//					pd.put("device_type", da.getString("device_type"));
//					pd.put("port_id", da.getString("port_id"));
//					pd.put("house_id", house_id);
//					farmService.saveDeviHouse(pd);
//				}
//			}

			
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("2");
		}
		super.writeJson(j, response);
	}
	
		
	
	/**
	 * 修改栋舍
	 * @return
	 */
	@RequestMapping("/editHouse")
	public void editHouse(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		
		pd.put("modify_person",user.getId());
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());
		try {
			farmService.editHouse(pd);
			pd.put("org_id", pd.get("id"));
			pd.put("org_name_updated", pd.get("house_name"));
			organService.updateOrg(pd);
//			/**
//			 * 修改栋舍和设备关系
//			 */
//			pd.put("house_id", pd.getString("id"));
//			pd.put("farm_id",pd.getString("farmId"));
//			farmService.delDeviHouse(pd);
//			if(!StringUtils.isBlank(pd.getString("deviceKey"))){
//				String [] arr=pd.getString("deviceKey").split(",");
//				for (int k = 0; k < arr.length; k++) {
//					pd.put("device_code", arr[k]);
//					List<PageData> device= farmService.findDevice(pd);
//					PageData da=device.get(0);
//					pd.put("create_person",user.getId());
//					pd.put("create_date", new Date());
//					pd.put("create_time", new Date());
//					pd.put("device_code", da.getString("device_code"));
//					pd.put("device_type", da.getString("device_type"));
//					pd.put("port_id", da.getString("port_id"));
//					farmService.saveDeviHouse(pd);
//				}
//			}
			j.setMsg("1");
			j.setSuccess(true);
			
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("2");
		}
		super.writeJson(j, response);
	}
	
	@RequestMapping("/isHouseNameNull")
	public void isHouseNameNull(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl= farmService.selectHouseById(pd);
		if(mcl!=null&&mcl.size()!=0){
			j.setMsg("1");//批次号已经存在
			j.setSuccess(true);
		}else{
			j.setMsg("2");// 批次号不存在
		}
		super.writeJson(j, response);
	}
	
	
	/**
	 * 删除栋舍
	 * @return
	 */
	@RequestMapping("/delHouse")
	public void delHouse(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		
		PageData paramPd = new PageData();
		paramPd.put("farm_id", pd.get("farm_id"));
		paramPd.put("house_code", pd.get("id"));
		List<PageData> batchCurrList = batchManageService.getCreateBatchData(paramPd);
		if(batchCurrList.size() == 0){
		List<Integer> orgList = new ArrayList<>();
		orgList.add(Integer.valueOf(String.valueOf(pd.get("id"))));
		pd = this.getPageData();
		pd.put("freeze_status",1);
		pd.put("modify_person",user.getId());
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());
		pd.put("freeze_status","1");
		pd.put("orgList",orgList);
		try {
			farmService.editHouse(pd);
			j.setMsg("删除成功！");
			j.setSuccess(true);
			
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		}else{
			j.setMsg("存在相应的批次，无法删除！");
			j.setSuccess(false);
		}
		super.writeJson(j, response);
	}
	
//
//	/**
//	 * 跳转到新增批次信息页面
//	 * varro 2016-7-9
//	 * @return
//	 */
//	@RequestMapping(value="/addBatchUrl")
//	public ModelAndView addBatchUrl(HttpServletResponse response,HttpServletRequest request,HttpSession session)throws Exception{
//		ModelAndView mv = this.getModelAndView();
//		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		pd.put("user_id",user.getId());
//		/***出入拦方式方式***/
////		pd.put("code_type", "OPERATION_TYPE");
////		List<PageData> operationType= farmService.findCode(pd);
//
//		mv.addObject("pd",pd);
////		mv.addObject("operationType",operationType);
//		mv.addObject("farmList",getFarmList(pd));
//		mv.addObject("houseList",getHouseList(pd));
//		mv.setViewName("modules/farm/addBatch");
//		return mv;
//	}
	
//
//
//	/**
//	 * 跳转到修改批次信息页面
//	 * varro 2016-7-9
//	 * @return
//	 */
//	@RequestMapping(value="/editBatchUrl")
//	public ModelAndView editBatchUrl()throws Exception{
//		ModelAndView mv = this.getModelAndView();
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		Page page=new Page();
//		page.setPd(pd);
//		List<PageData> balist = farmService.findBatchlistPage(page);
//		PageData batch=balist.get(0);
//		/***出入拦方式方式***/
////		pd.put("code_type", "OPERATION_TYPE");
////		List<PageData> operationType= farmService.findCode(pd);
//
//		mv.addObject("pd",pd);
//		mv.addObject("batchData",batch);
//		mv.addObject("farmList",getFarmList());
//		mv.addObject("houseList",getHouseList(pd));
//		mv.setViewName("modules/farm/editBatch");
//		return mv;
//	}
//	/**
//	 * 修改批次
//	 * @return
//	 */
//	@RequestMapping("/editBatch")
//	public void editBatch(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
//		Json j=new Json();
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		try {
//			farmService.editBatch(pd);
//			j.setMsg("1");
//			j.setSuccess(true);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			j.setMsg("2");
//		}
//		super.writeJson(j, response);
//	}
//
//	/**
//	 * 跳转到出栏信息页面
//	 * varro 2016-7-9
//	 * @return
//	 */
//	@RequestMapping(value="/laiBatchUrl")
//	public ModelAndView laiBatchUrl()throws Exception{
//		ModelAndView mv = this.getModelAndView();
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		Page page=new Page();
//		page.setPd(pd);
//		List<PageData> balist = farmService.findBatchlistPage(page);
//		PageData batch=balist.get(0);
//		mv.addObject("pd",pd);
//		mv.addObject("batchData",batch);
//		mv.addObject("farmList",getFarmList());
//		mv.addObject("houseList",getHouseList(pd));
//		mv.setViewName("modules/farm/laiBatch");
//		return mv;
//	}
//
//	/**
//	 * 批次出栏
//	 * @return
//	 */
//	@RequestMapping("/laiBatch")
//	public void laiBatch(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
//		Json j=new Json();
//		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		Page page=new Page();
//		page.setPd(pd);
//		List<PageData> balist = farmService.findBatchlistPage(page);
//		PageData batch=balist.get(0);
//		pd.put("house_code",batch.getString("house_code"));
//		pd.put("house_name",batch.getString("house_name"));
//		pd.put("farm_id",batch.getInteger("farm_id"));
//		pd.put("batch_no",batch.getString("batch_no"));
//		pd.put("operation_type","1");
//		pd.put("create_person",user.getId());
//		pd.put("create_date", new Date());
//		pd.put("create_time", new Date());
//		try {
//			if(pd.get("count").equals("")){
//				pd.put("count", null);
//			}
//			farmService.saveBatch(pd);
//			j.setMsg("1");
//			j.setSuccess(true);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			j.setMsg("2");
//		}
//		super.writeJson(j, response);
//	}
	
	
	
	
	@RequestMapping("/getHouse")
	public void getHouse(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl = getHouseList(pd);
		j.setSuccess(true);
		j.setObj(mcl);
		super.writeJson(j, response);
	}
	
	/**
	 * 获取农场信息
	 * @return 数据列表
     */
	List<PageData> getFarmList(PageData pd) throws Exception {
		List<PageData> mcl;
//		mcl = moduleService.service("farmServiceImpl", "selectByCondition", new Object[]{pd});//farmService.selectAll();
		mcl = farmService.selectByCondition(pd);
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
	
	/**
	 * 获取养殖品种信息
	 * @param pd 数据对象
	 * @return 数据列表
     */
	@RequestMapping(value="/getFeedTypeList")
	public void getFeedTypeList(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		pd.put("code_type", "FEED_TYPE");
		List<PageData> feedType= moduleService.service("codeServiceImpl", "getCodeList", new Object[]{pd});
		j.setSuccess(true);
		j.setObj(feedType);
		super.writeJson(j, response);
	}
	
	/**
	 * 获取养殖方式信息
	 * @param pd 数据对象
	 * @return 数据列表
     */
	@RequestMapping(value="/getFeedMethodList")
	public void getFeedMethodList(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		pd.put("code_type", "FEED_METHOD");
		List<PageData> feedMethod= moduleService.service("codeServiceImpl", "getCodeList", new Object[]{pd});
		j.setSuccess(true);
		j.setObj(feedMethod);
		super.writeJson(j, response);
	}
	
	/**
	 * 获取栋舍类型
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/setHouseType")
	public void setHouseType(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> mcl = getFarmList(pd);
		j.setSuccess(true);
		j.setObj(mcl.get(0).get("farm_type"));
		super.writeJson(j, response);
	}

}
