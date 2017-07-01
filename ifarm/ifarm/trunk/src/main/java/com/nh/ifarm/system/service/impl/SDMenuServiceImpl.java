package com.nh.ifarm.system.service.impl;

import com.nh.ifarm.farm.service.FarmService;
import com.nh.ifarm.system.entity.SDMenu;
import com.nh.ifarm.system.service.SDMenuService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;



@Service
public class SDMenuServiceImpl implements SDMenuService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Autowired
	private FarmService farmService;

	public boolean checkMenuRight(PageData pageData) throws Exception {
		try{
			List<SDMenu> menuList = (List<SDMenu>)dao.findForList("SDMenuMapper.checkMenuRight", pageData);
			if(!(null == menuList || menuList.size()==0)){
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return false;
	}

	public List<SDMenu>  listSubMenuByParentId(PageData pd) throws Exception {
		return (List<SDMenu>)dao.findForList("SDMenuMapper.listSubMenuByParentId", pd);
	}
	
	public List<SDMenu> listAllParentMenu(PageData pd) throws Exception {
		return (List<SDMenu>) dao.findForList("SDMenuMapper.listAllParentMenu",pd);
	}

	public List<SDMenu> listAllMenu(PageData pd) throws Exception {
		List<SDMenu> rl = this.listAllParentMenu(pd);
		List<SDMenu> rt = new ArrayList<>();
		List<PageData> showHouseTypeList = farmService.selectShowHouseType(pd);

		for (SDMenu menu : rl) {
			pd.put("MENU_PID", menu.getMENU_ID());
			List<SDMenu> tmpSubList=this.listSubMenuByParentId(pd);
			List<SDMenu> subList = new ArrayList<>();
			for(SDMenu sdMenu : tmpSubList){
				if(sdMenu.getSHOW_HOUSE_TYPE().equals("*") || checkShowHouseType(sdMenu, showHouseTypeList)){
					subList.add(sdMenu);
				}
			}
			menu.setSubMenu(subList);
			rt.add(menu);
		}
		return rt;
	}

	Boolean checkShowHouseType(SDMenu menu, List<PageData> showHouseTypeList){
		Boolean rt = false;
		for(PageData pd : showHouseTypeList){
			if(menu.getSHOW_HOUSE_TYPE().indexOf(pd.getString("SHOW_HOUSE_TYPE"))>=0){
				rt = true;
			}
		}
		return rt;
	}
	
	public List<PageData> selectMenuList(PageData pd)  throws Exception{
		return (List<PageData>)dao.findForList("SDMenuMapper.selectMenuList", pd);
	}
	
	public List<PageData> selectParentMenuList(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("SDMenuMapper.selectParentMenuList", pd);
	}
	
	public PageData saveMenu(PageData pd)throws Exception{
		PageData rt = new PageData();
    	rt.put("result",true);
        rt.put("msg",1);
    	int i = 0;
    	try{
    	i = (Integer) dao.save("SDMenuMapper.saveMenu", pd);
    	pd.put("rights_id", pd.get("menu_id"));
    	pd.put("obj_id", pd.get("menu_id"));
    	i *= (Integer) dao.save("SDMenuMapper.saveRights", pd);
    	pd.put("rights_id", pd.get("menu_id"));
    	List<PageData> roleRightsList = (List<PageData>)dao.findForList("SDMenuMapper.selectRoleList", pd);
    	for(int j =0;j<roleRightsList.size();j++){
    		pd.put("role_id", roleRightsList.get(j).get("role_id"));
    	i *= (Integer) dao.save("SDMenuMapper.saveRoleRights", pd);
    	}
    	List<PageData> roleTempList = (List<PageData>)dao.findForList("SDMenuMapper.selectRoleTempList", pd);
    	for(int j =0;j<roleTempList.size();j++){
    		pd.put("role_temp_id", roleTempList.get(j).get("role_temp_id"));
    		pd.put("role_temp_name", roleTempList.get(j).get("role_temp_name"));
    	i *= (Integer) dao.save("SDMenuMapper.saveRoleTempRights", pd);
    	}
    	
    	if(i != 1){
    		rt.put("result",false);
            rt.put("msg",0);
    	}
    	}catch(Exception e){
			e.printStackTrace();
			rt.put("result",false);
            rt.put("msg",0);
		}
    	return rt;
	}
	
	public PageData updateMenu(PageData pd)throws Exception{
		PageData rt = new PageData();
    	rt.put("result",true);
        rt.put("msg",1);
    	int i = 0;
    	i = (Integer) dao.update("SDMenuMapper.editMenu", pd);
    	if(i != 1){
    		rt.put("result",false);
            rt.put("msg",0);
    	}
    	return rt;
	}
	
	public void deleteMenu(PageData pd)throws Exception{
		dao.delete("SDMenuMapper.delMenu", pd);
	}
	
	
}
