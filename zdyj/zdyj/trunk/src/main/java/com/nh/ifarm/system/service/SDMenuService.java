package com.nh.ifarm.system.service;

import java.util.List;


import com.nh.ifarm.system.entity.SDMenu;
import com.nh.ifarm.util.common.PageData;

public interface SDMenuService {
	
	public List<SDMenu> listAllMenu(PageData pd) throws Exception;

}
