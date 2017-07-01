package com.nh.ifarm.user.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nh.ifarm.user.entity.SDUser;
import com.nh.ifarm.util.common.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nh.ifarm.user.service.RoleService;
import com.nh.ifarm.util.action.BaseAction;
import com.nh.ifarm.util.common.Json;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.service.ModuleService;

@Controller
@RequestMapping(value="/role")
public class RoleAction extends BaseAction {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ModuleService moduleService;
	
	
	/**
	 * 通过角色获取组织架构
	 * @return
	 */
	@RequestMapping("/getOrgByRoleId")
	public void getOrgByRoleId(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws Exception{
		Json j=new Json();
		PageData pd = new PageData();
		SDUser user=(SDUser)session.getAttribute(Const.SESSION_USER);

		pd = this.getPageData();
		List<PageData> orgList= moduleService.service("organServiceImpl", "getOrgListByRoleId", new Object[]{pd});

		PageData pdate = new PageData();
		pdate.put("user_id", user.getId());
		List<PageData> orgListAll= moduleService.service("organServiceImpl", "getOrgListByRoleId", new Object[]{pdate});

		List<PageData> list=new ArrayList<PageData>();
		for (PageData pageData : orgListAll) {
			PageData data=new PageData();
			data.put("id", pageData.getInteger("id") + "");
			data.put("pId", pageData.getInteger("parent_id")+ "");
			data.put("name", pageData.getString("name_cn") + "");
			data.put("open", "true");
			data.put("chkDisabled", "false");

			String flag = pd.getString("checkedFlag");
			data.put("checked", "false");

			if(data.getString("id").equals("147")){
				String a = "1";
			}

				for (PageData pageData2 : orgList) {
					if(null != flag && "1".equals(flag)) {
						int id2 = pageData2.getInteger("id");
						int id = pageData.getInteger("id");
						if (null != pageData2.get("role_level") && id2 == id) {
							data.put("checked", "true");
						}
					}
				}
			list.add(data);
		}

		j.setSuccess(true);
		j.setObj(list);
		super.writeJson(j, response);
	}
	
}
