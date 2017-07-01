package com.nh.ifarm.batch.action;

import com.nh.ifarm.batch.service.BatchManageService;
import com.nh.ifarm.breed.service.SDFileService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.Office;
import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.common.PubFun;
import com.nh.ifarm.util.service.ModuleService;
import com.sun.tools.internal.xjc.generator.bean.ImplStructureStrategy.Result;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by LeLe on 10/25/2016.
 * 批次管理控制类
 */
@Controller
@RequestMapping(value="/batch")
public class BatchManageAction extends BaseAction {
    @Autowired
    ModuleService moduleService;

    @Autowired
    BatchManageService batchManageService;
    
    @Autowired
    private SDFileService sdFileService;

    /**
     * 跳转到批次管理(种)页面
     * raymon 2016-10-18
     * @return
     */
    @RequestMapping(value="/batchManage")
    public ModelAndView batchManage()throws Exception{
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        //获取机构
        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
        pd.put("farm_id",orgList.get(0).get("org_id").toString());
        pd.put("farm_name",orgList.get(0).getString("org_name"));
        pd.put("parent_id",orgList.get(0).get("org_id").toString());
        List<PageData> orgList3 = moduleService.service("organServiceImpl","getCompanyByUserId",new Object[]{pd});
        pd.put("company_name",orgList3.get(0).get("org_name").toString());
        pd.put("level_name",orgList3.get(0).get("level_name").toString());
        List<PageData> orgList2 = moduleService.service("organServiceImpl","getOrgList",new Object[]{pd});
        pd.put("length", orgList2.size());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/batch/batchManage");
        String mi = PubFun.getPropertyValue("Batch.Index");
		mv.addObject("batchIndex",mi.replace(" ","").toString());
        mv.addObject("pd",pd);
        mv.addObject("companyList", orgList3);
        return mv;
    }
    
    /**
     * 跳转到批次管理(肉)页面
     * raymon 2016-10-18
     * @return
     */
    @RequestMapping(value="/batchManage2")
    public ModelAndView batchManage2()throws Exception{
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        //获取机构
        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
        pd.put("farm_id",orgList.get(0).get("org_id").toString());
        pd.put("farm_name",orgList.get(0).getString("org_name"));
        pd.put("parent_id",orgList.get(0).get("org_id").toString());
        List<PageData> orgList3 = moduleService.service("organServiceImpl","getCompanyByUserId",new Object[]{pd});
        pd.put("company_name",orgList3.get(0).get("org_name").toString());
        pd.put("level_name",orgList3.get(0).get("level_name").toString());
        List<PageData> orgList2 = moduleService.service("organServiceImpl","getOrgList",new Object[]{pd});
        pd.put("length", orgList2.size());
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("modules/batch/batchManage");
        String mi = PubFun.getPropertyValue("Batch2.Index");
		mv.addObject("batchIndex",mi.replace(" ","").toString());
        mv.addObject("pd",pd);
        mv.addObject("companyList", orgList3);
        return mv;
    }
    
    /**
     * 获取发雏数据
     * @param response
     */
    @RequestMapping(value="/getFaChuData")
    public void getFaChuData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        //获取用户信息
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        pd.put("jinjiFlag", 1);
        //获取数据
        List<PageData> list = batchManageService.getFaChuData(pd);
//        if(pd.get("farm_id")==null){
//        //获取机构
//        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
//        pd.put("farm_id",orgList.get(0).get("org_id").toString());
//        pd.put("farm_code",orgList.get(0).get("org_code").toString());
//        pd.put("farm_name",orgList.get(0).getString("org_name"));
//        pd.put("feed_type", orgList.get(0).get("feed_type"));
//        }
        //获取数据
        List<PageData> list2 = batchManageService.getCreateBatchData(pd);

        //返回前端数据
        j.setSuccess(true);
        j.setObj(list);
        j.setObj1(pd);
        j.setObj2(list2);
        super.writeJson(j, response);
    }
    
    /**
     * 获取发雏数据
     * @param response
     */
    @RequestMapping(value="/getFaChuData2")
    public void getFaChuData2(HttpServletResponse response) throws Exception{
        Json j=new Json();
        //获取用户信息
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        if(pd.get("farm_id").toString().equals("")){
        //获取机构
        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
        pd.put("farm_id",orgList.get(0).get("org_id").toString());
        pd.put("farm_code",orgList.get(0).get("org_code").toString());
        pd.put("farm_name",orgList.get(0).getString("org_name"));
        pd.put("feed_type", orgList.get(0).get("feed_type"));
        }
        //获取数据
        List<PageData> list = batchManageService.getFaChuData(pd);
        
        //获取数据
        List<PageData> list2 = batchManageService.getCreateBatchData(pd);

        //返回前端数据
        j.setSuccess(true);
        j.setObj(list);
        j.setObj1(pd);
        j.setObj2(list2);
        super.writeJson(j, response);
    }
    
    /**
	 * 新增发雏
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/addFaChu")
	public void addFaChu(HttpServletResponse response){
		Json j=new Json();
		PageData pd = this.getPageData();
		int userId = getUserId();
		pd.put("user_id", userId);
		try {
			    pd.put("is_enable",1);
				pd.put("create_person",userId);
				pd.put("create_date", new Date());	
				pd.put("create_time", new Date());
				pd.put("modify_person",userId);
				pd.put("modify_date", new Date());	
				pd.put("modify_time", new Date());
				PageData pd2 = batchManageService.saveFaChuData(pd);
				pd.put("child_batch_no", null);
				pd.put("jinjiFlag", 1);
				pd.put("farm_id", null);
				List<PageData> mcl = batchManageService.getFaChuData(pd);
				j.setMsg(pd2.getString("msg"));
				j.setObj(mcl);	
				j.setSuccess((boolean) pd2.get("result"));			
		} catch (Exception e) {
			e.printStackTrace();
			j.setObj(1);	
			j.setSuccess(false);
			j.setMsg("养殖批次已存在，请重新输入！");
		}
		
		super.writeJson(j, response);
	}
	
	/**
	 * 修改发雏信息
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/updateFaChu")
	public void updateFaChu(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		int userId = getUserId();
		pd.put("user_id", userId);
        try{
        pd.put("modify_person",userId);
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());
		PageData pd2 = batchManageService.editFaChuData(pd);
		pd.put("child_batch_no", null);
		pd.put("jinjiFlag", 1);
		pd.put("farm_id", null);
	    List<PageData> mcl = batchManageService.getFaChuData(pd);
	    j.setMsg(pd2.getString("msg"));
	    j.setSuccess((boolean) pd2.get("result"));	
		j.setObj(mcl);
        
		
	} catch (Exception e) {
		j.setMsg("0");
		e.printStackTrace();
	}
//        List<PageData> mc = googsService.getCorporation2(null);
		super.writeJson(j, response);
	}
	
	/**
	 * 删除发雏信息
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/deleteFaChu")
	public void deleteFaChu(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		int userId = getUserId();
		pd.put("user_id", userId);
        try{
		PageData pd2 = batchManageService.delFaChuData(pd);
		pd.put("child_batch_no", null);
		pd.put("jinjiFlag", 1);
	    List<PageData> mcl = batchManageService.getFaChuData(pd);
	    j.setMsg(pd2.getString("msg"));
	    j.setSuccess((boolean) pd2.get("result"));	
		j.setObj(mcl);
        
		
	} catch (Exception e) {
		j.setMsg("0");
		e.printStackTrace();
	}
//        List<PageData> mc = googsService.getCorporation2(null);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/editFileUrl")
    public ModelAndView editFileUrl(Page page, HttpSession session) throws Exception {
        ModelAndView mav = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mav.setViewName("modules/batch/editFile");
        return mav;
    }
	
	/**
     * 获取发料数据
     * @param response
     */
    @RequestMapping(value="/getFaLiaoData")
    public void getFaLiaoData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        //获取用户信息
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        if(pd.get("farm_id")==null){
          //获取机构
          List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
          pd.put("farm_id",orgList.get(0).get("org_id").toString());
          pd.put("farm_code",orgList.get(0).get("org_code").toString());
          pd.put("farm_name",orgList.get(0).getString("org_name"));
          pd.put("feed_type", orgList.get(0).get("feed_type"));
          }
        //获取数据
        List<PageData> list = batchManageService.getFaLiaoData(pd);
        //获取数据
        List<PageData> list2 = batchManageService.getCreateBatchData(pd);

        //返回前端数据
        j.setSuccess(true);
        j.setObj(list);
        j.setObj1(pd);
        j.setObj2(list2);
        super.writeJson(j, response);
    }
    
    /**
     * 获取发药数据
     * @param response
     */
    @RequestMapping(value="/getFaYaoData")
    public void getFaYaoData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        //获取用户信息
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        if(pd.get("farm_id")==null){
            //获取机构
            List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
            pd.put("farm_id",orgList.get(0).get("org_id").toString());
            pd.put("farm_code",orgList.get(0).get("org_code").toString());
            pd.put("farm_name",orgList.get(0).getString("org_name"));
            pd.put("feed_type", orgList.get(0).get("feed_type"));
            }
        //获取数据
        List<PageData> list = batchManageService.getFaYaoData(pd);
        //获取数据
        List<PageData> list2 = batchManageService.getCreateBatchData(pd);

        //返回前端数据
        j.setSuccess(true);
        j.setObj(list);
        j.setObj1(pd);
        j.setObj2(list2);
        super.writeJson(j, response);
    }
    
    /**
	 * 新增发料/发药
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/addFaLiao")
	public void addFaLiao(HttpServletResponse response){
		Json j=new Json();
		PageData pd = this.getPageData();
		int userId = getUserId();
		pd.put("user_id", userId);
		try {
			    pd.put("is_enable",1);
				pd.put("create_person",userId);
				pd.put("create_date", new Date());	
				pd.put("create_time", new Date());
				pd.put("modify_person",userId);
				pd.put("modify_date", new Date());	
				pd.put("modify_time", new Date());
				PageData pd2 = batchManageService.saveFaLiaoData(pd);
				List<PageData> mcl;
				if(pd.get("tabName").toString().equals("faLiao")){
					mcl = batchManageService.getFaLiaoData(pd);
				}else{
					mcl = batchManageService.getFaYaoData(pd);
				}
				
				j.setMsg(pd2.getString("msg"));
				j.setObj(mcl);	
				j.setSuccess((boolean) pd2.get("result"));			
		} catch (Exception e) {
			e.printStackTrace();
			j.setObj(1);	
			j.setSuccess(false);
			j.setMsg("分发批次已存在，请重新输入！");
		}
		
		super.writeJson(j, response);
	}
	
	/**
	 * 修改发料/发药信息
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/updateFaLiao")
	public void updateFaLiao(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		int userId = getUserId();
		pd.put("user_id", userId);
        try{
        pd.put("modify_person",userId);
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());
		PageData pd2 = batchManageService.editFaLiaoData(pd);
		List<PageData> mcl;
		if(pd.get("tabName").toString().equals("faLiao")){
			mcl = batchManageService.getFaLiaoData(pd);
		}else{
			mcl = batchManageService.getFaYaoData(pd);
		}
	    j.setMsg(pd2.getString("msg"));
	    j.setSuccess((boolean) pd2.get("result"));	
		j.setObj(mcl);
        
		
	} catch (Exception e) {
		j.setMsg("0");
		e.printStackTrace();
	}
//        List<PageData> mc = googsService.getCorporation2(null);
		super.writeJson(j, response);
	}
	
	/**
	 * 删除发料/发药信息
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/deleteFaLiao")
	public void deleteFaLiao(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		int userId = getUserId();
		pd.put("user_id", userId);
        try{
		PageData pd2 = batchManageService.delFaLiaoData(pd);
		List<PageData> mcl;
		if(pd.get("tabName").toString().equals("faLiao")){
			mcl = batchManageService.getFaLiaoData(pd);
		}else{
			mcl = batchManageService.getFaYaoData(pd);
		}
	    j.setMsg(pd2.getString("msg"));
	    j.setSuccess((boolean) pd2.get("result"));	
		j.setObj(mcl);
	} catch (Exception e) {
		j.setMsg("0");
		e.printStackTrace();
	}
//        List<PageData> mc = googsService.getCorporation2(null);
		super.writeJson(j, response);
	}

    /**
     * 获取创建批次数据
     * @param response
     */
    @RequestMapping(value="/getCreateBatchData")
    public void getCreateBatchData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        //获取用户信息
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        if(pd.get("farm_id").toString().equals("")){
        //获取机构
        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
        pd.put("farm_id",orgList.get(0).get("org_id").toString());
        pd.put("farm_code",orgList.get(0).get("org_code").toString());
        pd.put("farm_name",orgList.get(0).getString("org_name"));
        pd.put("feed_type", orgList.get(0).get("feed_type"));
        }
        //获取数据
        List<PageData> list = batchManageService.getCreateBatchData(pd);
        List<PageData> list2 = batchManageService.getCreateBatchData(pd);

        //返回前端数据
        j.setSuccess(true);
        j.setObj(list);
        j.setObj1(pd);
        j.setObj2(list2);
        super.writeJson(j, response);
    }

    /**
     * 保存创建批次数据
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/saveCreateBatchData")
    public void saveCreateBatchData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());

        //获取农场类型
        PageData tmp = new PageData();
        tmp.put("farmId", pd.getString("farm_id"));
        tmp.put("houseId", pd.getString("house_code"));
        List<PageData> houseList = moduleService.service("organServiceImpl","getHouseType", new Object[]{tmp});
        if(houseList.toArray().length == 1){
            pd.put("house_type", houseList.get(0).getString("house_type")) ;
        } else{
            j.setSuccess(false);
            j.setMsg("请检查栋舍类型");
        }

        PageData pd2 = new PageData();
        pd2.put("user_id", getUserId());
        
        //获取机构
        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
        pd2.put("farm_id",orgList.get(0).get("org_id").toString());
        pd2.put("farm_code",orgList.get(0).get("org_code").toString());
        pd2.put("farm_name",orgList.get(0).getString("org_name"));
        
        

        //获取数据
        List<PageData> list = batchManageService.getOverBatchData(pd2);
        //获取进鸡数据
//        List<PageData> list2 = batchManageService.getCreateBatchData(pd2);
//        int jinji= Integer.valueOf(pd.get("female_count").toString()).intValue();
//        for(int i=0;i<list2.size();i++){
//        	jinji = jinji+ Integer.valueOf(list2.get(i).get("female_count").toString()).intValue();
//        }
        //获取当前栋舍发雏数据
//        List<PageData> list3 = batchManageService.getFaChuData(pd);
//        System.out.println(list.get(0).get("batchNo2").toString().equals(pd.getString("batch_no")));
//        System.out.println(!pd.getString("house_code").equals(list.get(0).getString("house_code")));
//        System.out.println(Integer.parseInt(list.get(0).get("is_mix").toString()) != Integer.parseInt(pd.get("is_mix").toString()));
        if(list.size()!=0 && list.get(0).get("batchNo2").toString().equals(pd.getString("batch_no")) && !pd.getString("house_code").equals(list.get(0).getString("house_code"))
        		&& Integer.parseInt(list.get(0).get("is_mix").toString()) != Integer.parseInt(pd.get("is_mix").toString())){
        	j.setSuccess(false);
            j.setMsg("同批次在已有栋舍出栏的情况下，新进鸡的相同批次混合状态必须相同！");
        }
//        else if(list3.size()>0 && Integer.valueOf(list3.get(0).get("send_female_num").toString()).intValue() < jinji){
//        	j.setSuccess(false);
//            j.setMsg("同批次进鸡总数不能大于发雏数量！");
//        }
        else{
        j.setSuccess(false);
        j.setMsg("新建批次失败，请联系管理员！");
        PageData result = batchManageService.saveCreateBatchData(pd);
        Boolean flag = (Boolean)(result.get("result"));
        j.setSuccess(flag);
        j.setMsg(result.getString("msg"));
        }
        super.writeJson(j, response);
    }


    /**
     * 获取修改批次数据
     * @param response
     */
    @RequestMapping(value="/getEditBatchData")
    public void getEditBatchData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        //获取用户信息
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        if(pd.get("farm_id").toString().equals("")){
        //获取机构
        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
        pd.put("farm_id",orgList.get(0).get("org_id").toString());
        pd.put("farm_code",orgList.get(0).get("org_code").toString());
        pd.put("farm_name",orgList.get(0).getString("org_name"));
        }
        //获取数据
        List<PageData> list = batchManageService.getEditBatchData(pd);
        List<PageData> list2 = batchManageService.getCreateBatchData(pd);
        //返回前端数据
        j.setSuccess(true);
        j.setObj(list);
        j.setObj1(pd);
        j.setObj2(list2);
        super.writeJson(j, response);
    }

    /**
     * 保存修改批次数据
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/saveEditBatchData")
    public void saveEditBatchData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());

        j.setSuccess(false);
        j.setMsg("修改批次失败，请联系管理员！");
        PageData result = batchManageService.saveEditBatchData(pd);
        Boolean flag = (Boolean)(result.get("result"));
        j.setSuccess(flag);
        j.setMsg(result.getString("msg"));
        super.writeJson(j, response);
    }
    
    /**
     * 获取淘汰批次数据
     * @param response
     */
    @RequestMapping(value="/getTaoTaiData")
    public void getTaoTaiData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        //获取用户信息
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        if(pd.get("farm_id").toString().equals("")){
        //获取机构
        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
        pd.put("farm_id",orgList.get(0).get("org_id").toString());
        pd.put("farm_code",orgList.get(0).get("org_code").toString());
        pd.put("farm_name",orgList.get(0).getString("org_name"));
        }
        //获取数据
        List<PageData> list = batchManageService.getTaoTaiData(pd);
        List<PageData> list2 = batchManageService.getCreateBatchData(pd);
        //返回前端数据
        j.setSuccess(true);
        j.setObj(list);
        j.setObj1(pd);
        j.setObj2(list2);
        super.writeJson(j, response);
    }
    
    /**
     * 保存淘汰批次数据
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/saveTaoTaiData")
    public void saveTaoTaiData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());

        j.setSuccess(false);
        j.setMsg("批次淘汰失败，请联系管理员！");
        PageData result = batchManageService.saveTaoTaiData(pd);
        Boolean flag = (Boolean)(result.get("result"));
        j.setSuccess(flag);
        j.setMsg(result.getString("msg"));
        super.writeJson(j, response);
    }

    /**
     * 获取出栏批次数据
     * @param response
     */
    @RequestMapping(value="/getOverBatchData")
    public void getOverBatchData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        //获取用户信息
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        if(pd.get("farm_id").toString().equals("")){
        //获取机构
        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
        pd.put("farm_id",orgList.get(0).get("org_id").toString());
        pd.put("farm_code",orgList.get(0).get("org_code").toString());
        pd.put("farm_name",orgList.get(0).getString("org_name"));
        }
        //获取数据
        List<PageData> list = batchManageService.getOverBatchData(pd);
        List<PageData> list2 = batchManageService.getCreateBatchData(pd);
        //返回前端数据
        j.setSuccess(true);
        j.setObj(list);
        j.setObj1(pd);
        j.setObj2(list2);
        super.writeJson(j, response);
    }

    /**
     * 保存出栏批次数据
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/saveOverBatchData")
    public void saveOverBatchData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());

        //获取农场类型
        PageData tmp = new PageData();
        tmp.put("farmId", pd.getString("farm_id"));
        tmp.put("houseId", pd.getString("house_code"));
        List<PageData> houseList = moduleService.service("organServiceImpl","getHouseType", new Object[]{tmp});
        if(houseList.toArray().length == 1){
            pd.put("house_type", houseList.get(0).getString("house_type")) ;
        } else{
            j.setSuccess(false);
            j.setMsg("请检查栋舍类型");
        }

        j.setSuccess(false);
        j.setMsg("批次出栏失败，请联系管理员！");
        PageData result = batchManageService.saveOverBatchData(pd);
        Boolean flag = (Boolean)(result.get("result"));
        j.setSuccess(flag);
        j.setMsg(result.getString("msg"));
        super.writeJson(j, response);
    }
    
    /**
     * 获取结算数据
     * @param response
     */
    @RequestMapping(value="/getSettleData")
    public void getSettleData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        //获取用户信息
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        if(pd.get("farm_id").toString().equals("")){
        //获取机构
        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
        pd.put("farm_id",orgList.get(0).get("org_id").toString());
        pd.put("farm_code",orgList.get(0).get("org_code").toString());
        pd.put("farm_name",orgList.get(0).getString("org_name"));
        }
        //获取数据
        List<PageData> list = batchManageService.getSettleData(pd);
        List<PageData> list2 = batchManageService.getCreateBatchData(pd);
        //返回前端数据
        j.setSuccess(true);
        j.setObj(list);
        j.setObj1(pd);
        j.setObj2(list2);
        super.writeJson(j, response);
    }
    
    /**
     * 修改结算数据
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/updateSettleData")
    public void updateSettleData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
        pd.put("settle_id", pd.get("id"));
        List<PageData> pdlist = batchManageService.priceCount2(pd);
        float gross_chicken_amount=(float) 0.0,chicken_amount=(float) 0.0,feed_amount=(float) 0.0,
        		drug_amount=(float) 0.0,gross_chicken_number=(float) 0.0,gross_chicken_weight=(float) 0.0;
        for(int i=0;i<pdlist.size();i++){
        	if(Integer.valueOf(pdlist.get(i).get("settle_type").toString()).intValue()==1){
        		gross_chicken_amount = Float.valueOf(pdlist.get(i).get("priceCount").toString()).floatValue();
        		gross_chicken_number = Float.valueOf(pdlist.get(i).get("munberCount").toString()).floatValue();
        		gross_chicken_weight = Float.valueOf(pdlist.get(i).get("weightCount").toString()).floatValue();
        		pd.put("gross_chicken_amount", gross_chicken_amount);
        		pd.put("gross_chicken_number", gross_chicken_number);
        		pd.put("gross_chicken_weight", gross_chicken_weight);
        	}else if(Integer.valueOf(pdlist.get(i).get("settle_type").toString()).intValue()==2){
        		chicken_amount = Float.valueOf(pdlist.get(i).get("priceCount").toString()).floatValue();
        		pd.put("chicken_amount", chicken_amount);
        	}else if(Integer.valueOf(pdlist.get(i).get("settle_type").toString()).intValue()==3){
        		feed_amount = Float.valueOf(pdlist.get(i).get("priceCount").toString()).floatValue();
        		pd.put("feed_amount", feed_amount);
        	}else if(Integer.valueOf(pdlist.get(i).get("settle_type").toString()).intValue()==4){
        		drug_amount = Float.valueOf(pdlist.get(i).get("priceCount").toString()).floatValue();
        		pd.put("drug_amount", drug_amount);
        	}
        }
        
        j.setSuccess(false);
        j.setMsg("批次淘汰失败，请联系管理员！");
        int i = batchManageService.editSettleData(pd);
        if(i==1){
        	j.setSuccess(true);
            j.setMsg("修改成功！");
        }else{
        	j.setSuccess(false);
            j.setMsg("修改失败！");
        }
        List<PageData> list = batchManageService.getSettleData(null);
        //返回前端数据
        j.setObj(list);
        super.writeJson(j, response);
    }
    
    /**
     * 获取结算子数据
     * @param response
     */
    @RequestMapping(value="/getSettleSubData")
    public void getSettleSubData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        //获取用户信息
        PageData pd = this.getPageData();
        //获取非毛鸡
        List<PageData> list = batchManageService.getSettleSubData(pd);
        float acount = (float) 0.0;
        PageData pageData1 = batchManageService.priceCount(pd);
        if(pageData1 !=null){
        acount = Float.valueOf(pageData1.get("priceCount").toString()).floatValue();
        }
        pd.put("settle_type", 1);
        //获取毛鸡
        List<PageData> list2 = batchManageService.getSettleSubData(pd);
        float acount2 = (float) 0.0,acount3 = (float) 0.0;
        PageData pageData2 = batchManageService.dataSum(pd);
        if(pageData2 !=null){
        acount2 = Float.valueOf(pageData2.get("weightCount").toString()).floatValue();
        acount3 = Float.valueOf(pageData2.get("munberCount").toString()).floatValue();
        DecimalFormat df = new DecimalFormat("0.00");
        
        pd.put("weightCount", df.format(acount/acount2));
        pd.put("munberCount", df.format(acount/acount3));
        }else{
        	pd.put("weightCount", 0);
            pd.put("munberCount", 0);
        }
        pd.put("priceCount", acount);
        //返回前端数据
        j.setSuccess(true);
        j.setObj(list);
        j.setObj1(pd);
        j.setObj2(list2);
        super.writeJson(j, response);
    }
    
    /**
     * 新增结算子数据
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/saveSettleSubData")
    public void saveSettleSubData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
//        String[] goods = pd.get("good_id").toString().split(",");
//		pd.put("good_id", goods[0]);
//		pd.put("good_name", goods[1]);
        j.setSuccess(false);
        j.setMsg("批次淘汰失败，请联系管理员！");
//        if(Integer.valueOf(pd.get("settle_type").toString()).intValue()==1){
//        	pd.put("settle_type", null);
//        }
        int i = batchManageService.saveSettleSubData(pd);
        if(i==1){
        	j.setSuccess(true);
            j.setMsg("修改成功！");
        }else{
        	j.setSuccess(false);
            j.setMsg("修改失败！");
        }
        List<PageData> list;
        if(Integer.valueOf(pd.get("settle_type").toString()).intValue()==1){
        	pd.put("settle_type", 1);
            //获取毛鸡
            list = batchManageService.getSettleSubData(pd);
        }else{
        	//获取非毛鸡
            list = batchManageService.getSettleSubData(pd);
        }
        //返回前端数据
        j.setObj(list);
        super.writeJson(j, response);
    }
    
    /**
     * 修改结算子数据
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/updateSettleSubData")
    public void updateSettleSubData(HttpServletResponse response) throws Exception{
        Json j=new Json();
        PageData pd = this.getPageData();
        pd.put("user_id", getUserId());
//        String[] goods = pd.get("good_id").toString().split(",");
//		pd.put("good_id", goods[0]);
//		pd.put("good_name", goods[1]);
        j.setSuccess(false);
        j.setMsg("批次淘汰失败，请联系管理员！");
//        if(Integer.valueOf(pd.get("settle_type").toString()).intValue()==1){
//        	pd.put("settle_type", null);
//        }
        int i = batchManageService.editSettleSubData(pd);
        if(i==1){
        	j.setSuccess(true);
            j.setMsg("修改成功！");
        }else{
        	j.setSuccess(false);
            j.setMsg("修改失败！");
        }
        List<PageData> list;
        if(Integer.valueOf(pd.get("settle_type").toString()).intValue()==1){
        	pd.put("settle_type", 1);
            //获取毛鸡
            list = batchManageService.getSettleSubData(pd);
        }else{
        	//获取非毛鸡
            list = batchManageService.getSettleSubData(pd);
        }
        //返回前端数据
        j.setObj(list);
        super.writeJson(j, response);
    }
    
    /**
     * 删除供结算子数据
     * yoven 2017-05-04
     * @return
     */
    @RequestMapping(value="/deleteSettleSubData")
    public void deleteSettleSubData(HttpServletResponse response)throws Exception{
        Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        batchManageService.deleteSettleSubData(pd);
        List<PageData> settleSub = batchManageService.getSettleSubData(pd);
                j.setObj(settleSub);
                j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    @RequestMapping(value="/getCount")
    public void getCount(HttpServletResponse response) throws Exception{
    	Json j=new Json();
    	PageData pd = this.getPageData();
    	pd.put("user_id", getUserId());
        List<PageData> outList = batchManageService.getBatchDataCount(pd);
        j.setObj(outList);
        j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    @RequestMapping(value="/getOverBatchAge")
    public void getOverBatchAge(HttpServletResponse response) throws Exception{
    	Json j=new Json();
    	PageData pd = this.getPageData();
        List<PageData> outList = batchManageService.getOverBatchAge(pd);
        List<PageData> outList3 = batchManageService.getCreateBatchData(pd);
        j.setObj(outList);
        j.setObj2(outList3);
        j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    @RequestMapping(value="/getFarm")
    public void getFarm(HttpServletResponse response) throws Exception{
    	Json j=new Json();
    	PageData pd = this.getPageData();
    	pd.put("user_id", getUserId());
    	//获取机构
        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmByUserId",new Object[]{pd});
        j.setObj(orgList);
        j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    @RequestMapping(value="/getFarm2")
    public void getFarm2(HttpServletResponse response) throws Exception{
    	Json j=new Json();
    	PageData pd = this.getPageData();
    	pd.put("user_id", getUserId());
    	//获取机构
        List<PageData> orgList = moduleService.service("organServiceImpl","getFarmListByUserId",new Object[]{pd});
        j.setObj(orgList);
        j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    @RequestMapping("/getGoods")
	public void getGoods(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		pd.put("code_type", "settle_type");
		pd.put("biz_code", pd.get("settle_type"));
		List<PageData> codeList = batchManageService.selectSBCode(pd);
		pd.put("good_type", codeList.get(0).get("bak1"));
		List<PageData> goodsList = batchManageService.getGoodsList(pd);
		j.setSuccess(true);
		j.setObj(goodsList);
		super.writeJson(j, response);
	}
    
    @RequestMapping("/getGoods2")
	public void getGoods2(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> goodsList = batchManageService.getGoodsList2(pd);
		j.setSuccess(true);
		j.setObj(goodsList);
		super.writeJson(j, response);
	}
    
    @RequestMapping("/getGoods3")
   	public void getGoods3(HttpServletResponse response) throws Exception{
   		Json j=new Json();
   		PageData pd = this.getPageData();
   		List<PageData> goodsList = batchManageService.getGoodsList(pd);
   		j.setSuccess(true);
   		j.setObj(goodsList);
   		super.writeJson(j, response);
   	}
    
    @RequestMapping("/getGoodId")
	public void getGoodId(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> goodsList = batchManageService.getGoodId(pd);
		j.setSuccess(true);
		j.setObj(goodsList);
		super.writeJson(j, response);
	}
    
    @RequestMapping("/getSumjimiao")
	public void getSumjimiao(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		PageData sumJimiao = batchManageService.selectSumjimiao(pd);
		j.setSuccess(true);
		j.setObj(sumJimiao);
		super.writeJson(j, response);
	}
    
    @RequestMapping("/getSumsiliao")
	public void getSumsiliao(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		PageData sumSiliao = batchManageService.selectSumsiliao(pd);
		j.setSuccess(true);
		j.setObj(sumSiliao);
		super.writeJson(j, response);
	}
    
    @RequestMapping("/getSummiaoyao")
	public void getSummiaoyao(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		PageData sumMiaoyao = batchManageService.selectSummiaoyao(pd);
		j.setSuccess(true);
		j.setObj(sumMiaoyao);
		super.writeJson(j, response);
	}
    
    @RequestMapping(value="/feedMasterDataManage")
	public ModelAndView googsManage()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("modules/batch/feedManage");
		PageData pd = new PageData();
		pd = this.getPageData();

		List<PageData> farmList = getFarm();
		if(farmList.size()>0){
			mv.addObject("farmId",farmList.get(0).get("id"));
			mv.addObject("farm",farmList.get(0).get("name_cn"));
		}
		mv.addObject("pd",pd);
		return mv;
		
	}
    
    /**
	 * 新增物资
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/addGoods")
	public void addGoods(HttpServletResponse response,HttpSession session) throws Exception{
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		Json j=new Json();
		PageData pd = this.getPageData();
			List<PageData> stock = batchManageService.getGoodsList(pd);
			if(stock.size()>0){
				j.setMsg("0");
				j.setSuccess(false);
			}else{
				pd.put("create_person",user.getId());
				pd.put("create_date", new Date());	
				pd.put("create_time", new Date());
				pd.put("modify_person",user.getId());
				pd.put("modify_date", new Date());	
				pd.put("modify_time", new Date());
				PageData result = batchManageService.saveGoods(pd);
				List<PageData> mcl = batchManageService.getGoodsList2(null);
				j.setMsg(result.get("msg").toString());
				j.setObj(mcl);
				j.setSuccess((boolean) result.get("result"));
			}
			
		super.writeJson(j, response);
	}
	
	/**
	 * 修改物资
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/updateGoods")
	public void updateGoods(HttpServletResponse response) throws Exception{
//		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);
		Json j=new Json();
		PageData pd = this.getPageData();
        pd.put("good_id", pd.get("good_id"));
        pd.put("good_code", pd.get("good_code"));
        pd.put("good_name", pd.get("good_name"));
        pd.put("bak", pd.get("bak"));
        pd.put("modify_person",user.getId());
		pd.put("modify_date", new Date());	
		pd.put("modify_time", new Date());
//		PageData result = batchManageService.updateGoods(pd);
	    List<PageData> mcl = batchManageService.getGoodsList2(null);
//		j.setMsg(result.get("msg").toString());
		j.setObj(mcl);  
//        List<PageData> mc = googsService.getCorporation2(null);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getCorporationGood")
	public void getCorporationGood(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> goodsList = batchManageService.getCorporationGood(pd);
		j.setSuccess(true);
		j.setObj(goodsList);
		super.writeJson(j, response);
	}
	
	/**
	 * 新增物资关系
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/addCorporationGood")
	public void addCorporationGood(HttpServletResponse response,HttpSession session){
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		Json j=new Json();
		PageData pd = this.getPageData();
		String[] corporation = pd.get("corporation").toString().split(",");
		pd.put("corporation_id", corporation[0]);
		String[] factory = pd.get("factory").toString().split(",");
		pd.put("factory_id", factory[0]);
		String[] goods = pd.get("goods").toString().split(",");
		pd.put("good_id", goods[0]);
		try {
			List<PageData> stock = batchManageService.getCorporationGood(pd);
			if(stock.size()>0){
				j.setMsg("0");
			}else{
				pd.put("create_person",user.getId());
				pd.put("create_date", new Date());	
				pd.put("create_time", new Date());
				pd.put("modify_person",user.getId());
				pd.put("modify_date", new Date());	
				pd.put("modify_time", new Date());
				pd.put("corporation_name", corporation[1]);
				pd.put("factory_name", factory[1]);
				pd.put("good_name", goods[1]);
				batchManageService.saveCorporationGoods(pd);
				List<PageData> mcl = batchManageService.getCorporationGood(null);
				j.setMsg("1");
				j.setObj(mcl);
			}

			j.setSuccess(true);
			
		} catch (Exception e) {
			j.setMsg("0");
			e.printStackTrace();
		}
		super.writeJson(j, response);
	}
	
	/**
     * 删除供应商物资关系
     * yoven 2017-05-03
     * @return
     */
    @RequestMapping(value="/deleteCorporationGoods")
    public void deleteCorporationGoods(HttpServletResponse response)throws Exception{
        Json j=new Json();
        PageData pd = new PageData();
        pd = this.getPageData();
        String tr = (String) pd.get("deleteRow");
        String[] corporationGoods = tr.split(";");
        for(String dr:corporationGoods){
        	int tt = Integer.parseInt(dr);
        	pd.put("id", tt);
        	batchManageService.deleteCorporationGoods(pd);
        }
        List<PageData> corporationGood = batchManageService.getCorporationGood(pd);
                j.setObj(corporationGood);
                j.setSuccess(true);
        super.writeJson(j, response);
    }
    
    /**
     * 上传文件
     * @return
     */
    @RequestMapping(value="/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "eFiles", required = false) MultipartFile file)throws Exception{
        Json j = new Json();
        PageData pd = this.getPageData();
        String filePath = null;
        try{
            ResourceBundle conf = ResourceBundle.getBundle("pro/excelFormat_batchSend");
            filePath = sdFileService.uploadFile(request, response, file, pd.getString("upload_file_type"), j);
            Office of = new Office();
            Workbook wb = of.openFile(filePath);
            pd.putAll(of.checkExcelDataVaild(wb, conf, pd.getString("upload_file_type")));
            pd.put("error_type",7);
            List<PageData> list = (List<PageData>) pd.get("data");
            if(pd.getString("upload_file_type").equals("1")){
	            for(PageData pageData :list){
	            	if(pageData.get(0).toString().equals("false")){
	            		System.out.println(pageData.get("farm_name").toString());
	            		//获取机构
	                    List<PageData> orgList = moduleService.service("organServiceImpl","getFarmListByFarmName",new Object[]{pageData});
	                    pageData.put("farm_id", orgList.get(0).get("org_id"));
//	                    pageData.put("src_batch_no", "0");
	                    pageData.put("good_name", pageData.get("variety"));
	                    List<PageData> goodList = batchManageService.getGoodsList(pageData);
	//                    if(goodList.size()>0){
	                    	pageData.put("variety_id", goodList.get(0).get("good_id"));
	//                    }else{
	//                    	pageData.put("variety_id", 0);
	//                    }
	                    
	                    List<PageData> corporationList = batchManageService.getCorporation2(pageData);
	                    pageData.put("corporation_id", corporationList.get(0).get("corporation_id"));
	                    pageData.put("bak", "0");
	                    pageData.put("is_enable", 1);
	                    pageData.put("modify_person", 0);
	                    pageData.put("modify_date", new Date());
	                    pageData.put("modify_time", new Date());
	                    pageData.put("create_person", 0);
	                    pageData.put("create_date", new Date());
	                    pageData.put("create_time", new Date());
	                    batchManageService.saveFaChuData(pageData);
	            	}
	            }
            }else{
            	for(PageData pageData :list){
	            	if(pageData.get(0).toString().equals("false")){
	            		//获取机构
	                    List<PageData> orgList = moduleService.service("organServiceImpl","getFarmListByFarmName",new Object[]{pageData});
	                    pageData.put("farm_id", orgList.get(0).get("org_id"));
//	                    pageData.put("src_batch_no", "0");
	                    PageData pageData2 = new PageData();
	                    pageData2.put("code_type", "good_type");
	                    pageData2.put("code_name", pageData.get("good_type"));
	            		List<PageData> goodType= moduleService.service("codeServiceImpl", "getCodeList", new Object[]{pageData2});
	            		pageData.put("good_type", goodType.get(0).get("biz_code"));
	            		
	                    pageData.put("good_name", pageData.get("variety"));
	                    List<PageData> goodList = batchManageService.getGoodsList(pageData);
	                    	pageData.put("goods_id", goodList.get(0).get("good_id"));
	                    
	                    List<PageData> corporationList = batchManageService.getCorporation2(pageData);
	                    pageData.put("corporation_id", corporationList.get(0).get("corporation_id"));
	                    pageData.put("bak", "0");
	                    pageData.put("is_enable", 1);
	                    pageData.put("modify_person", 0);
	                    pageData.put("modify_date", new Date());
	                    pageData.put("modify_time", new Date());
	                    pageData.put("create_person", 0);
	                    pageData.put("create_date", new Date());
	                    pageData.put("create_time", new Date());
	                    batchManageService.saveFaLiaoData(pageData);
	            	}
	            }
            }
            pd.put("error_type",-1);
            j.setSuccess((boolean) pd.get("result"));
            j.setObj(pd);
        } catch (Exception e){
            logger.error(e.getMessage());
            j.setSuccess(false);
            j.setObj(pd);
        } finally {
            super.writeJson(j, response);
        }
    }
    
    @RequestMapping("/download")
    public void download(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception {

        String fileName = "模板文件.xlsx";
        String dir = request.getSession().getServletContext().getRealPath("")+"modules/file/upload/模板文件.xlsx";
//        String filePath =  path + "modules/file/" + dirName + "/" + fileName;
        dir = StringUtils.replace(StringUtils.replace(dir, "\\\\", "\\"), "\\]", "]");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        InputStream fis = null;
        File uploadFile = new File(dir);
        fis = new FileInputStream(uploadFile);
        bis = new BufferedInputStream(fis);
        response.reset();
        fos = response.getOutputStream();
        bos = new BufferedOutputStream(fos);

        fileName = new String(fileName.getBytes("gbk"), "iso8859-1");
        response.setContentType("octets/stream");
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

//        response.setContentType("text/plain;charset=UTF-8");
//        response.setHeader("Content-disposition","attachment; filename=\""+uploadFile.getName() + "\"");
        FileCopyUtils.copy(fis, bos);//spring工具类直接流拷贝
        bos.flush();
        fis.close();
        bis.close();
        fos.close();
        bos.close();
    }
    
    /**
	 * 获取农场信息
	 * @return 数据列表
     */
	List<PageData> getFarm() throws Exception {
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		pd.put("user_id", user.getId());
		List<PageData> orglist=moduleService.service("organServiceImpl", "getFarmByUserId", new Object[]{pd});//farmService.selectAll();
		int count=0;
		List<PageData> list=new ArrayList<PageData>();
		if(orglist!=null&&orglist.size()!=0){
			for (PageData pageData : orglist) {
					PageData paData=new PageData();
					paData.put("id", pageData.getInteger("org_id"));
					paData.put("organization_id", pageData.getInteger("org_code"));
					paData.put("name_cn", pageData.getString("org_name"));
					paData.put("parent_id", pageData.getInteger("org_parent_id"));
					paData.put("level_id", pageData.getInteger("org_level_id"));
					list.add(paData);
			}
		}
		return list;
	}
	
	@RequestMapping("/getGoodType")
	public void getGoodType(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		pd.put("code_type", "good_type");
		List<PageData> goodType= moduleService.service("codeServiceImpl", "getCodeList", new Object[]{pd});
		List<PageData> goodType2 = new ArrayList<PageData>();
		for(PageData goodType1: goodType){
			if(goodType1.get("biz_code").toString().equals("1")){
				goodType2.add(goodType1);
			}
		}
		j.setSuccess(true);
		j.setObj(goodType2);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getSpec")
	public void getSpec(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		pd.put("code_type", "spec");
		List<PageData> spec= moduleService.service("codeServiceImpl", "getCodeList", new Object[]{pd});
		j.setSuccess(true);
		j.setObj(spec);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getSpec2")
	public void getSpec2(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> spec = batchManageService.getSpec(pd);
		j.setSuccess(true);
		j.setObj(spec);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getUnit")
	public void getUnit(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		pd.put("code_type", "unit");
		List<PageData> unit= moduleService.service("codeServiceImpl", "getCodeList", new Object[]{pd});
		j.setSuccess(true);
		j.setObj(unit);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getUnit2")
	public void getUnit2(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> unit = batchManageService.getUnit(pd);
		j.setSuccess(true);
		j.setObj(unit);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getCorporation")
	public void getCorporation(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> corporation = batchManageService.getCorporation(pd);
		j.setSuccess(true);
		j.setObj(corporation);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getCorporation2")
	public void getCorporation2(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> corporation = batchManageService.getCorporation2(pd);
		j.setSuccess(true);
		j.setObj(corporation);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getFactory")
	public void getFactory(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> factory = batchManageService.getFactory(pd);
		j.setSuccess(true);
		j.setObj(factory);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getFactory2")
	public void getFactory2(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> factory = batchManageService.getFactory2(pd);
		j.setSuccess(true);
		j.setObj(factory);
		super.writeJson(j, response);
	}
    
}
