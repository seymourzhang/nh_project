package com.nh.ifarm.drug.action;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nh.ifarm.drug.service.DrugService;
import com.nh.ifarm.goods.service.GoogsService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.user.service.SDUserService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.DateUtil;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.service.ModuleService;
import com.mysql.fabric.xmlrpc.base.Array;

/**
 * Created by yoven on 10/31/2016.
 * drug控制类
 */
@Controller
@RequestMapping("/drug")
public class DrugAction extends BaseAction {
     
	@Autowired
	private DrugService drugService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private SDUserService userService;
	
	@RequestMapping("/showDrug")
	public ModelAndView showDrug(HttpSession session) throws Exception {
		ModelAndView mv = this.getModelAndView();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id", user.getId());
		List<PageData> company = moduleService.service("organServiceImpl", "getCompanyByUserId", new Object[]{pd});
		pd.put("parent_id", company.get(0).get("org_id"));
		pd.put("company", company.get(0).get("org_name"));
		List<PageData> farm = moduleService.service("organServiceImpl", "getFarmByUserId", new Object[]{pd});
		pd.put("farm_id", farm.get(0).get("org_id"));
		pd.put("farm_name", farm.get(0).get("org_name"));
		mv.addObject("houseList",moduleService.service("organServiceImpl", "getHouseListByUserId", new Object[]{pd}));
		pd.put("good_type", null);
		mv.addObject("goodsList",drugService.selectGoods(pd));	
		pd.put("code_desc", "使用方法");
		mv.addObject("useTypeList",drugService.selectCode(pd));
		pd.put("code_desc", "物资类别");
		pd.put("bak1", "s_b_use_drug");
		mv.addObject("goodTypeList",drugService.selectCode(pd));
//		List<PageData> drug = drugService.selectDrugPlan(pd);
		mv.setViewName("/modules/drug/drugView");
		mv.addObject("pd",pd);	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		mv.addObject("systemDate",df.format(new Date()).toString());
		//负责人
		pd.put("id",user.getId());
		pd.put("obj_type",2);
		pd.put("user_status",1);
		pd.put("freeze_status",0);
		pd.put("listFlag",1);
		mv.addObject("userList",userService.getUserList(pd));
		return mv;
	}
	
	@RequestMapping("/drugEdit")
	public ModelAndView drugEdit(HttpSession session) throws Exception {
		ModelAndView mv = this.getModelAndView();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id", user.getId());
		List<PageData> company = moduleService.service("organServiceImpl", "getCompanyByUserId", new Object[]{pd});
        pd.put("parent_id", (company.size()>0)?company.get(0).get("org_id"):"");
        pd.put("company", (company.size()>0)?company.get(0).get("org_name"):"");

		List<PageData> farmList = moduleService.service("organServiceImpl", "getFarmListByUserId", new Object[]{pd});
		mv.addObject("farmList", farmList);
		pd.put("farmId", (farmList.size()>0)?farmList.get(0).get("org_id"):"");
		List<PageData> houseList = moduleService.service("organServiceImpl", "getHouseType", new Object[]{pd});
		mv.addObject("house_type", (houseList.size()>0)?houseList.get(0).get("house_type"):"");
		pd.put("good_type", null);
		mv.addObject("goodsList",drugService.selectGoods(pd));	
		pd.put("code_desc", "使用方法");
		mv.addObject("useTypeList",drugService.selectCode(pd));
		pd.put("code_desc", "物资类别");
		pd.put("bak1", "s_b_use_drug");
		mv.addObject("goodTypeList",drugService.selectCode(pd));
		mv.setViewName("/modules/drug/drugEdit");
		mv.addObject("pd",pd);		
		return mv;
	}
	
	/**
     * 查询计划数据
     * yoven 2016-10-31
     * @return
     */
    @RequestMapping(value="/searchData")
    public void searchData(HttpServletResponse response)throws Exception{
        Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> drug;
        String ps = pd.get("paramTypeSelectValue").toString();
        if(ps.equals("plan")){
//	        String tr = (String) pd.get("grow_week_age");        
//	        String[] grow_week_age = tr.split(",");
//	        List<String> gw = java.util.Arrays.asList(grow_week_age);
//	        if(gw.size()==1 && gw.get(0).toString().equals("")){
//	        	pd.put("grow_week_age", null);	
//	        }else{
//	        pd.put("grow_week_age", gw);
//	        }       
        	drug = drugService.selectDrugPlan(pd);
        }else if(ps.equals("fact")){
        	drug = drugService.selectDrugFact(pd);
        }else{
        	drug = drugService.selectDrugPlan(pd);
        }
          j.setSuccess(true);
          j.setObj(drug);
        super.writeJson(j, response);
    }   
    
    /**
     * 保存计划数据
     * yoven 2016-10-31
     * @return
     */
    @RequestMapping(value="/savePlanData")
    public void savePlanData(HttpServletResponse response, HttpServletRequest request, HttpSession session)throws Exception{
        Json j=new Json();
        SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("create_person",user.getId());
        pd.put("modify_person", user.getId());
        String tr = (String) pd.get("grow_week_age");        
        String[] grow_week_age = tr.split(",");
        List<String> gw = java.util.Arrays.asList(grow_week_age);
        if(gw.size()==1 && gw.get(0).toString().equals("")){
        	pd.put("grow_week_age", null);	
        }else{
        pd.put("grow_week_age", gw);
        }
        drugService.saveDrugPlan(pd);
//        PageData pd2 = new PageData();
        List<PageData> drug = drugService.selectDrugPlan(pd);
                j.setObj(drug);
                j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    /**
     * 删除计划数据
     * yoven 2016-10-31
     * @return
     */
    @RequestMapping(value="/deletePlanData")
    public void deletePlanData(HttpServletResponse response)throws Exception{
        Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        String tr = (String) pd.get("deleteRow");
        String[] drugs = tr.split(";");
        for(String dr:drugs){
        	int tt = Integer.parseInt(dr);
        	pd.put("id", tt);
        	drugService.deleteDrugPlan(pd);
        }
//        PageData pd2 = new PageData();
        List<PageData> drug = drugService.selectDrugPlan(pd);
                j.setObj(drug);
                j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    /**
     * 保存数据
     * yoven 2016-10-31
     * @return
     */
    @RequestMapping(value="/saveData")
    public void saveData(HttpServletResponse response, HttpServletRequest request, HttpSession session)throws Exception{
        Json j=new Json();
        SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("create_person",user.getId());
        pd.put("modify_person", user.getId());
        drugService.saveDrugFact(pd);
        pd.put("good_type1", pd.get("good_type"));
        List<PageData> drug = drugService.selectDrugFact(pd);
                j.setObj(drug);
                j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    /**
     * 删除实际表数据
     * yoven 2016-10-31
     * @return
     */
    @RequestMapping(value="/deleteData")
    public void deleteData(HttpServletResponse response)throws Exception{
        Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        String tr = (String) pd.get("deleteRow");
        String[] drugs = tr.split(";");
        for(String dr:drugs){
        	int tt = Integer.parseInt(dr);
        	pd.put("id", tt);
        	drugService.deleteDrugFact(pd);
        }
        
        List<PageData> drug = drugService.selectDrugFact(pd);
                j.setObj(drug);
                j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    /**
     * 类型联动名称
     * yoven 2016-10-31
     * @return
     */
    @RequestMapping(value="/getGoods")
    public void getGoods(HttpServletResponse response)throws Exception{
        Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> goods = drugService.selectGoods(pd);
                j.setObj(goods);
                j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    /**
     * 药品联动工厂
     * yoven 2016-10-31
     * @return
     */
    @RequestMapping(value="/getFactory")
    public void getFactory(HttpServletResponse response)throws Exception{
        Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> factory = drugService.selectFactory(pd);
                j.setObj(factory);
                j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    /**
     * 负责人
     * yoven 2016-10-31
     * @return
     */
    @RequestMapping(value="/getUser")
    public void getUser(HttpServletResponse response,HttpSession session)throws Exception{
        Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		pd.put("user_id", user.getId());
        List<PageData> userList = drugService.selectUser(pd);
                j.setObj(userList);
                j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    /**
     * 获取栋舍类型
     * yoven 2017-02-23
     * @return
     */
    @RequestMapping(value="/getHouseType")
    public void getHouseType(HttpServletResponse response)throws Exception{
        Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> houseList = moduleService.service("organServiceImpl", "getHouseType", new Object[]{pd});
                j.setObj(houseList);
                j.setSuccess(true);
        super.writeJson(j, response);
    }
 
	
}
