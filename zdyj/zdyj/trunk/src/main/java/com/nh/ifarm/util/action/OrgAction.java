package com.nh.ifarm.util.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.nh.ifarm.user.service.RoleService;
import com.nh.ifarm.util.service.ModuleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.common.Const;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.service.OrganService;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/org")
public class OrgAction extends BaseAction{
	
	@Autowired
	private OrganService  organService;

	@Autowired
	private ModuleService moduleService;

	/**
	 * 跳转到批次管理页面
	 * raymon 2016-10-18
	 * @return
	 */
	@RequestMapping(value="/orgManage")
	public ModelAndView showOrgManage()throws Exception{
		PageData pd = this.getPageData();
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("modules/util/orgManage");
		mv.addObject("pd",pd);
		return mv;
	}

	@RequestMapping("/getOrganizationList")
	public void getOrganizationList(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);
		pd.put("user_id",user.getId());
		List<PageData> organizationList = organService.getOrganizationList(pd);
		j.setSuccess(true);
		j.setObj(organizationList);
		super.writeJson(j, response);
	}

	@RequestMapping("/getFarmWithHouseName")
	public void getFarmWithHouseName(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		int level_id = Integer.valueOf((String)pd.get("level_id"));
		int pId = Integer.valueOf((String)pd.get("id"));

		List<PageData> tmpPdList = organService.getMaxOrgLevelId(null);
		PageData tmpPd = tmpPdList.get(0);
		int max_level_id = Integer.valueOf(String.valueOf(tmpPd.get("max_level_id")));

		List<PageData> organizationList = new ArrayList<>();
		List<Integer> parentOrgIdList = new ArrayList<>();
		tmpPd = new PageData();

		List<PageData> tmpRtList = new ArrayList<>();
		//获取农场清单
		for(int i=(level_id+1); i < max_level_id+1; i++){

			tmpPd.clear();
			if(i == (level_id+1)){
				tmpPd.put("level_id",i);
				tmpPd.put("parent_id",pId);
			} else{
				tmpPd.put("parentOrgIdList",parentOrgIdList);
			}
			organizationList = organService.getOrgListById(tmpPd);

			for(PageData tmpOrg : organizationList){
				parentOrgIdList.add(tmpOrg.getInteger("id"));
				PageData org = new PageData();
				org.put("id",-1);
				org.put("name_cn","");
				org.put("parent_id",tmpOrg.getInteger("id"));
				org.put("parent_name",tmpOrg.getString("name_cn"));
				org.put("level_id",level_id-2);
				org.put("level_name","");
				tmpRtList.add(org);
			}

			if( (max_level_id-1) == i){
				break;
			}
		}

		List<PageData> rtList = new ArrayList<>();
		PageData rtPd = new PageData();
		tmpPd.clear();
		tmpPd.put("parentOrgIdList",parentOrgIdList);
		if(parentOrgIdList.size()>0)
			organizationList = organService.getOrgListById(tmpPd);
		else
			organizationList = new ArrayList<>();

		//格式化返回结果集
		for(PageData tmpOrg : organizationList){
			int flag = -1;
			int k =0;
			for(PageData t : rtList){
				int a = t.getInteger("parent_id").intValue();
				int b = tmpOrg.getInteger("parent_id").intValue();
				if(a == b){
					flag = k;
				}
				k += 1;
			}

			if(flag == -1) {
				rtList.add(tmpOrg);
			} else{
				String tmpName = rtList.get(flag).getString("name_cn");
				rtList.get(flag).put("name_cn", tmpName + "," + tmpOrg.getString("name_cn"));
			}

		}

		if(rtList.size() == 0){
			rtList.addAll(tmpRtList);
		}

		j.setSuccess(true);
		j.setObj(rtList);
		super.writeJson(j, response);
	}

	@RequestMapping("/deleteOrg")
	public void deleteOrg(HttpServletResponse response) throws Exception{
		Json j=new Json();
		PageData pd = this.getPageData();
		String orgStr = pd.getString("org");
		List<Integer> orgList = new ArrayList<>();

		if(orgStr.length()>1){
			String[] tmpOrgs = orgStr.substring(0,orgStr.length()-1).split(",");
			for(String tmp : tmpOrgs){
				orgList.add(Integer.valueOf(tmp));
			}

			pd.put("orgList",orgList);
			pd.put("freeze_status",1); //删除标志位
			int i = organService.deleteOrg(pd);
			if(i==1){
				j.setSuccess(true);
			} else{
				j.setSuccess(false);
			}
		} else{
			j.setSuccess(false);
			j.setMsg("没有选中需要被删除的机构");
		}
		super.writeJson(j, response);
	}

	@RequestMapping("/updateOrg")
	public void updateOrg(HttpServletResponse response) throws Exception{
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);
		Json j=new Json();
		PageData pd = this.getPageData();
		int i = organService.updateOrg(pd);
		if(i==1){
			j.setSuccess(true);
		} else{
			j.setSuccess(false);
		}
		super.writeJson(j, response);
	}

	@RequestMapping("/addOrg")
	public void addOrg(HttpServletResponse response) throws Exception{
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);

		Json j=new Json();
		PageData pd = this.getPageData();
		String orgName = pd.getString("org_name");
		String orgLevelName = pd.getString("org_level_name");
		Integer orgLevelId = Integer.valueOf((String)pd.get("org_level_id"));
		String parentOrgName = pd.getString("parent_org_name");
		Integer parentOrgId = Integer.valueOf((String)pd.get("parent_org_id"));
		Integer parentOrgLevelId = Integer.valueOf((String)pd.get("parent_org_level_id"));
		PageData paramPd = new PageData();
		boolean flag = true;
		String msg ="";

		List<PageData> maxPdList = organService.getMaxOrgLevelId(null);
		PageData maxPd = maxPdList.get(0);

		List<PageData> organizationList = organService.getOrganizationList(null);
		if(organizationList.size()==0){
			paramPd.put("organization_id","101");
			paramPd.put("name_cn",orgName);
			paramPd.put("parent_id",0);
			paramPd.put("level_id",1);
			paramPd.put("level_name",orgLevelName);
			paramPd.put("freeze_status",0);
			paramPd.put("create_person", user.getId());
		} else{
			int maxOrgLevel = Integer.valueOf(String.valueOf(maxPd.get("max_level_id"))) ;
			int maxOrganizationId = 0;
			for(PageData tmpPd : organizationList){
				if(maxOrgLevel < tmpPd.getInteger("org_level") ){
					maxOrgLevel = tmpPd.getInteger("org_level");
				}

				if(orgLevelId == tmpPd.getInteger("org_level") ){
					if(orgName.equals(tmpPd.getString("name_cn"))){
						flag = false;
						msg = "机构名称重复";
						break;
					}
					if(maxOrganizationId < tmpPd.getInteger("org_code") ){
						maxOrganizationId = tmpPd.getInteger("org_code");
					}
					if(parentOrgId == tmpPd.getInteger("parent_id") ){
						orgLevelName = tmpPd.getString("level_name");
					}
				}
			}
			if(orgLevelId >= (maxOrgLevel) && maxOrgLevel != 0 ){
				flag = false;
				msg = "不能新建机构层级为" + orgLevelId.toString() + "的机构";
			} else{
				paramPd.put("organization_id",maxOrganizationId+1);
				paramPd.put("name_cn",orgName);
				paramPd.put("parent_id",parentOrgId);
				paramPd.put("level_id",orgLevelId);
				paramPd.put("level_name",orgLevelName);
				paramPd.put("freeze_status",0);
				paramPd.put("create_person", user.getId());
			}
		}


		if(flag){
			int i = organService.insertOrg(paramPd);
			Long objId = (Long)paramPd.get("id");
			paramPd.clear();
			paramPd.put("obj_id",objId);
			paramPd.put("obj_type",2);
			paramPd.put("create_person",user.getId());
			paramPd.put("user_id",user.getId());
			moduleService.service("roleServiceImpl","insertRightsObj",new Object[]{paramPd});
			if(i==1){
				j.setSuccess(true);
			} else{
				j.setSuccess(false);
			}
		} else{
			j.setSuccess(false);
			j.setMsg(msg);
		}

		super.writeJson(j, response);
	}

	@RequestMapping("/getOrgList")
	public void getOrgList(HttpServletResponse response) throws Exception{
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);
		Json j=new Json();
		PageData pd = this.getPageData();
		pd.put("user_id", user.getId());
		List<PageData> orgLevelList = organService.getOrgLevelList(null);
		List<PageData> orglist = organService.getOrgList(pd);
		j.setSuccess(true);
		j.setObj(orgLevelList);
		j.setObj1(orglist);
		super.writeJson(j, response);
	}

	@RequestMapping("/getOrg")
	public void getOrg(HttpServletResponse response) throws Exception{
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);
		
		Json j=new Json();
		PageData pd = this.getPageData();
		pd.put("user_id", user.getId());
		List<PageData> orglist = organService.getOrgList(pd);
		int count=0;
		for(PageData tmpPd : orglist){
			if(count<=tmpPd.getInteger("level_id")){
				count = tmpPd.getInteger("level_id");
			}
		}
//		if(orglist!=null && orglist.size()!=0){
//			count = orglist.get(orglist.size()-1).getInteger("level_id");
//		}
		List<PageData> list=new ArrayList<PageData>();
		for (int i = 1; i <= count; i++) {
			for (PageData pageData : orglist) {
				if(i==pageData.getInteger("level_id")){
					PageData paData=new PageData();
					BeanUtils.copyProperties(pageData,paData);
					paData.put("level_id", pageData.getInteger("level_id"));
					paData.put("parent_id", pageData.getInteger("parent_id"));
					paData.put("level_name", pageData.getString("level_name"));
					paData.put("role_level", pageData.getInteger("role_level"));
					list.add(paData);
					break;
				}
				
			}
		}
		
//		j.setMsg(conut);
		j.setSuccess(true);
		j.setObj(list);
		j.setObj1(orglist);
		super.writeJson(j, response);
	}
	@RequestMapping("/getOrgByPid")
	public void getOrgByPid(HttpServletResponse response) throws Exception{
		Json j=new Json();
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		pd.put("user_id", user.getId());
		if("".equals(pd.getString("parent_id"))){
			pd.put("parent_id",null);
		}
		List<PageData> orglist = organService.getOrgList(pd);
		j.setSuccess(true);
		j.setObj(orglist);
		super.writeJson(j, response);
	}

	@RequestMapping("/getOrgByUser")
	public void getOrgByUser(HttpServletResponse response) throws Exception{
		Json j=new Json();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		pd.put("user_id", user.getId());
		pd.put("flag",1);
		List<PageData> orglist = organService.selectOrgByUser(pd);
		j.setSuccess(true);
		j.setObj(orglist);
		super.writeJson(j, response);
	}

	@RequestMapping("/getFarmForMapping")
	public void getFarmForMapping(HttpServletResponse response) throws Exception{
		Json j=new Json();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		pd.put("user_id", user.getId());
		List<PageData> orgList = organService.getFarmForMapping(null);
		if(orgList.size()>0){
			j.setSuccess(true);
			j.setObj(orgList);
		} else{
			j.setSuccess(false);
		}
		super.writeJson(j, response);
	}

	/**
	 * 弃用，仅做其他代码参考之用
	 * */
	@RequestMapping("/setFarmMapping")
	public void setFarmMapping(HttpServletResponse response) throws Exception{
		Json j=new Json();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);
		PageData pd = this.getPageData();
		pd.put("user_id", user.getId());


//		int orgLevelId = Integer.valueOf(pd.getString("level_id"));
//
//		List<PageData> maxLevelList = organService.getMaxOrgLevelId(null);
//		int maxOrgLevelId = Integer.valueOf(String.valueOf(maxLevelList.get(0).get("max_level_id")))  ;
//
//		if((maxOrgLevelId-1) == orgLevelId){
//			String orgStr = pd.getString("org");
//			List<Integer> orgList = new ArrayList<>();
//
//			if(orgStr.length()>1) {
//				String[] tmpOrgs = orgStr.substring(0, orgStr.length() - 1).split(",");
//				for (String tmp : tmpOrgs) {
//					orgList.add(Integer.valueOf(tmp));
//				}
//				pd.put("orgList",orgList);
//				int i = organService.setFarmMapping(pd);
//
//				if(i > 0){
//					for(int k=0; k<i; k++){
//						PageData paramPd = new PageData();
////						Long objId = (Long)pd.get("id");
//						paramPd.put("obj_id",orgList.get(k));
//						paramPd.put("obj_type",2);
//						paramPd.put("create_person",user.getId());
//						moduleService.service("roleServiceImpl","insertRightsObj",new Object[]{paramPd});
//					}
//
//					j.setSuccess(true);
//				} else{
//					j.setSuccess(false);
//					j.setMsg("系统内部错误");
//				}
//			} else{
//				j.setSuccess(false);
//				j.setMsg("没有选择需要绑定的农场");
//			}
//		} else{
//			j.setSuccess(false);
//			j.setMsg("该机构下不能直接绑定农场");
//		}

		super.writeJson(j, response);
	}

}
