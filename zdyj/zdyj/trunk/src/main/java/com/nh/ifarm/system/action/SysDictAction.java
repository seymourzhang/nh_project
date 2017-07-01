package com.nh.ifarm.system.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nh.ifarm.system.service.SysDictService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;

@Controller
@RequestMapping(value = "/sysDict")
public class SysDictAction extends BaseAction {

	@Autowired
	private SysDictService sysDictService;

	@RequestMapping(value = "/showSysDict")
	public ModelAndView showSysDict(Page page, HttpServletRequest request)throws Exception{

		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		
		String codeDesc = pd.getString("codeDesc");
		if(codeDesc != null && !"".equals(codeDesc)){
			codeDesc = codeDesc.trim();
			pd.put("codeDesc", codeDesc);
		}
		
		page.setPd(pd);
		List<PageData> dictList = sysDictService.sysDictlistPage(page);		
		page.setPd(pd);	
		mv.setViewName("modules/system/sysDict");
		mv.addObject("pd", pd);
		mv.addObject("dictList", dictList);
		return mv;
	}

	/**
	 * 去新增页面
	 * 
	 * @param
	 * @author zl
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/goAdd")
	public ModelAndView goAdd() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("modules/system/addSysDict");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 新增
	 * 
	 * @param request
	 * @author Administrator
	 * @return ModelAndVier
	 */
	@RequestMapping(value = "/save")
	public void save(HttpServletRequest request, HttpServletResponse response) {
		Json j = new Json();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			sysDictService.save(pd);
			j.setMsg("1");
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
		}
		super.writeJson(j, response);
	}
	
    /**
     * 去编辑页面
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData dict = sysDictService.findDictInfo(pd);
		mv.addObject("pd",pd);
		mv.addObject("dictList", dict);
		mv.setViewName("/modules/system/editSysDict");
		return mv;
	}

	/**
	 * 编辑
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	public void edit(HttpServletResponse response,HttpServletRequest request){
		Json j = new Json();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			sysDictService.edit(pd);
			j.setMsg("1");
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("2");
		}
		super.writeJson(j, response);
	}

	/**
	 * 删除
	 * @param response
	 * @param request
	 */
	@RequestMapping("/delDict")
	public void delDict(HttpServletResponse response, HttpServletRequest request) {
		Json j = new Json();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			sysDictService.findDictInfo(pd);
			sysDictService.delete(pd);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("2");
		}

		super.writeJson(j, response);
	}
	
	@RequestMapping("/isRepeat")
	public void isRepeat(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		PageData mcl=  sysDictService.findDictInfo(pd);
		if(mcl!=null){
			j.setMsg("1");//批次号已经存在
			j.setSuccess(true);
		}else{
			j.setMsg("2");// 批次号不存在
		}
		super.writeJson(j, response);
	}
}
