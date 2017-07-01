package com.nh.ifarm.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.nh.ifarm.system.entity.SDMenu;
import com.nh.ifarm.system.service.SDMenuService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;



@Service
public class SDMenuServiceImpl implements SDMenuService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	public List<SDMenu>  listSubMenuByParentId(PageData pd) throws Exception {
		return (List<SDMenu>)dao.findForList("SDMenuMapper.listSubMenuByParentId", pd);
	}
	
	public List<SDMenu> listAllParentMenu(PageData pd) throws Exception {
		return (List<SDMenu>) dao.findForList("SDMenuMapper.listAllParentMenu",pd);
	}

	public List<SDMenu> listAllMenu(PageData pd) throws Exception {
		List<SDMenu> rl = this.listAllParentMenu(pd);
		for (SDMenu menu : rl) {
			pd.put("MENU_PID", menu.getMENU_ID());   
			List<SDMenu> subList=this.listSubMenuByParentId(pd);
			menu.setSubMenu(subList);
		}
		return rl;
	}

	
}
