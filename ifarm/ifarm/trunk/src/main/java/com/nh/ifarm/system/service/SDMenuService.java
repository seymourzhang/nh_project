package com.nh.ifarm.system.service;

import com.nh.ifarm.system.entity.SDMenu;
import com.nh.ifarm.util.common.PageData;

import java.util.List;

public interface SDMenuService {

	public boolean checkMenuRight(PageData pageData) throws Exception;
	
	public List<SDMenu> listAllMenu(PageData pd) throws Exception;
	
	/**
	 * 查询主菜单
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> selectParentMenuList(PageData pd) throws Exception;
	
	/**
	 * 根据条件查询菜单
	 * @param pd
	 * @return
	 * @throws Exception
	 */
    public List<PageData> selectMenuList(PageData pd)  throws Exception ;
	
    /**
     * 保存菜单
     * @param pd
     * @return
     * @throws Exception
     */
	public PageData saveMenu(PageData pd)throws Exception;
	
	/**
	 * 修改菜单
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData updateMenu(PageData pd)throws Exception;
	
	/**
	 * 逻辑删除菜单
	 * @param pd
	 * @throws Exception
	 */
	public void deleteMenu(PageData pd)throws Exception;

}
