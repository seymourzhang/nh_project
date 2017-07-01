package com.nh.ifarm.code.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.nh.ifarm.util.common.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nh.ifarm.code.service.CodeManageService;
import com.nh.ifarm.system.entity.SDMenu;
import com.nh.ifarm.system.service.SDMenuService;
import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.action.BaseAction;

@Controller
@RequestMapping(value="/system")
public class CodeManageAction extends BaseAction {
	

	@Autowired
	private SDMenuService sDMenuService;
	
	@Autowired
	private CodeManageService codeManageService;
	
	/**
	 * 编码管理
	 * @return
	 */
	@RequestMapping(value="/codeManage")
	public ModelAndView codeManage(Page page, HttpSession session)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		pd.put("id",user.getId());

		/******查询编码**********/
//		List<PageData> codelist= codeManageService.getCodeList(pd);
//		mv.addObject("codelist",codelist);
		mv.setViewName("modules/code/codeManage");
		List<PageData> codeType = codeManageService.getCodeType();
		mv.addObject("codeTypelist",codeType);
		mv.addObject("pd",pd);
		return mv;
	}	
	
	/**
	 * 菜单管理
	 * @return
	 */
	@RequestMapping(value="/menuManage")
	public ModelAndView menuManage(Page page, HttpSession session)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		pd.put("id",user.getId());
		mv.setViewName("modules/menu/menuManage");
		List<PageData> menuList = sDMenuService.selectParentMenuList(pd);
		mv.addObject("menuPidlist",menuList);
		mv.addObject("pd",pd);
		return mv;
	}	
	
	@RequestMapping("/getCodeList")
	public void getCodeList(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> codeList = codeManageService.getCodeList(pd);
		j.setSuccess(true);
		j.setObj(codeList);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getCodeType")
	public void getCodeType(HttpServletResponse response) throws Exception{
		Json j=new Json();
		List<PageData> codeType = codeManageService.getCodeType();
		j.setSuccess(true);
		j.setObj(codeType);
		super.writeJson(j, response);
	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping("/addCode")
	public void addCode(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id", user.getId());
			PageData result = codeManageService.saveCode(pd);
            if(Integer.valueOf(result.get("msg").toString()).intValue()==1){
            	j.setMsg("保存成功");
    			j.setSuccess(true);
            }else{
            	j.setMsg("数据重复");
    			j.setSuccess(false);
            }
		super.writeJson(j, response);
	}
	
	
	/**
	 * 修改编码
	 * @return
	 */
	@RequestMapping("/editCode")
	public void editCode(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id", user.getId());
			PageData result = codeManageService.updateCode(pd);
			if(Integer.valueOf(result.get("msg").toString()).intValue()==1){
            	j.setMsg("修改成功");
    			j.setSuccess(true);
            }else{
            	j.setMsg("数据重复");
    			j.setSuccess(false);
            }
		super.writeJson(j, response);
	}
	
	/**
	 * 删除编码
	 * @return
	 */
	@RequestMapping("/delCode")
	public void delCode(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
			try {
				codeManageService.deleteCode(pd);
				j.setMsg("删除成功");
				j.setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
				j.setMsg(e.getMessage());
				j.setSuccess(false);
			}
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getMenuList")
	public void getMenuList(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> menuList = sDMenuService.selectMenuList(pd);
		j.setSuccess(true);
		j.setObj(menuList);
		super.writeJson(j, response);
	}
	
	@RequestMapping("/getMenuPidList")
	public void getMenuPidList(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		List<PageData> menuList = sDMenuService.selectParentMenuList(pd); 
		j.setSuccess(true);
		j.setObj(menuList);
		super.writeJson(j, response);
	}
	
	/**
	 * 保存菜单
	 * @return
	 */
	@RequestMapping("/addMenu")
	public void addMenu(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id", user.getId());
			PageData result = sDMenuService.saveMenu(pd);
            if(Integer.valueOf(result.get("msg").toString()).intValue()==1){
            	j.setMsg("保存成功");
    			j.setSuccess(true);
            }else{
            	j.setMsg("数据重复");
    			j.setSuccess(false);
            }
		super.writeJson(j, response);
	}
	
	
	/**
	 * 修改菜单
	 * @return
	 */
	@RequestMapping("/editMenu")
	public void editMenu(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("user_id", user.getId());
			PageData result = sDMenuService.updateMenu(pd);
			if(Integer.valueOf(result.get("msg").toString()).intValue()==1){
            	j.setMsg("修改成功");
    			j.setSuccess(true);
            }else{
            	j.setMsg("数据重复");
    			j.setSuccess(false);
            }
		super.writeJson(j, response);
	}
	
	/**
	 * 删除菜单
	 * @return
	 */
	@RequestMapping("/delMenu")
	public void delMenu(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		SDUser user = (SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = new PageData();
		pd = this.getPageData();
			try {
				sDMenuService.deleteMenu(pd);
				j.setMsg("删除成功");
				j.setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
				j.setMsg(e.getMessage());
				j.setSuccess(false);
			}
		super.writeJson(j, response);
	}
	

}
