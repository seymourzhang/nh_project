package com.nh.ifarm.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.nh.ifarm.user.service.SDUserService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

@Service
public class SDUserServiceImpl implements SDUserService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/** 登录判断 **/
	public PageData getUserBylogin(PageData pd) throws Exception {
		return (PageData) dao.findForObject("SDUserMapper.getUserInfo", pd);
	}
	
	@Override
	public List<PageData> getUserInfo(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("SDUserMapper.getUserList", pd);
	}
	@Override
	public List<PageData> getUserInfo2(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("SDUserMapper.getUserList2", pd);
	}
	@Override
	public int saveUser(PageData pd) throws Exception {
		return (Integer) dao.save("SDUserMapper.saveUser", pd);
	}
	
	@Override
	public void saveUserFarm(PageData pd) throws Exception {
		dao.save("SDUserMapper.saveUserFarm", pd);
	}
	
	@Override
	public void editUser(PageData pd) throws Exception {
		dao.update("SDUserMapper.editUser", pd);
		
	}
	@Override
	public void delUserFarm(PageData pd) throws Exception {
		dao.delete("SDUserMapper.delUserFarm", pd);
	}
	
	@Override
	public void delUserHouse(PageData pd) throws Exception {
		dao.delete("SDUserMapper.delUserHouse", pd);
	}
	
	
	@Override
	public void saveUserHouse(PageData pd) throws Exception {
		dao.save("SDUserMapper.saveUserHouse", pd);
	}
	@Override
	public PageData findUserInfo(PageData pd) throws Exception {
		return (PageData) dao.findForObject("SDUserMapper.findUserInfo", pd);
	}
	
	/**
	 * 根据用户id查询用户栋舍 
	 */
	@Override
	public List<PageData> findUserHouseCode(PageData pd) throws Exception {
		return  (List<PageData>) dao.findForList("SDUserMapper.findUserHouseCode", pd);
	}

	/**
	 * 根据用户id查询用户权限
	 */
	@Override
	public List<PageData> getUserRights(PageData pd) throws Exception {
		return  (List<PageData>) dao.findForList("SDUserMapper.getUserRights", pd);
	}

	/**
	 * 根据用户id查询栋舍权限
	 * */
	@Override
	public List<PageData> getUserHouses(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDUserMapper.getUserHouses", pd);
	}

	public List<PageData> getUserList(PageData pd)  throws Exception {
		List<PageData> list= getUserInfo(pd);		
		List<PageData> userlist=new ArrayList<PageData>();
		for (PageData pageData : list) {
			PageData paDate = new PageData();
			paDate.put("user_id", pageData.getInteger("id"));
			paDate.put("farm_id", pageData.getInteger("farm_id"));
			List<PageData> houseList=findUserHouseCode(paDate);
			String houseID ="";
			String houseName ="";
			for (int i = 0; i < houseList.size(); i++) {
				if((i+1)==houseList.size()){
					houseID+=houseList.get(i).getString("house_code");
					houseName+=houseList.get(i).getString("house_name");
				}else{
					houseID+=houseList.get(i).getString("house_code")+",";
					houseName+=houseList.get(i).getString("house_name")+",";
				}
			}
			pageData.put("house_code", houseID);
			pageData.put("house_name", houseName);
			userlist.add(pageData);
		}

		return userlist;
	}
	
	public List<PageData> getUserList2(PageData pd)  throws Exception {
		List<PageData> list;
//		System.out.println(pd.get("farm_add1"));
//		System.out.println(!pd.get("farm_add1").toString().equals(null));
//		System.out.println(!pd.get("farm_add1").toString().equals(""));
		if(!pd.get("farm_add1").toString().equals(null) && !pd.get("farm_add1").toString().equals("")){
			if(Integer.parseInt(pd.getString("farm_add1"))==1){
			list= getUserInfo2(pd);
			}else{
				list= getUserInfo(pd);
			}
		}else{
		 list= getUserInfo(pd);
		}
		List<PageData> userlist=new ArrayList<PageData>();
		for (PageData pageData : list) {
			PageData paDate = new PageData();
			paDate.put("user_id", pageData.getInteger("id"));
			paDate.put("farm_id", pageData.getInteger("farm_id"));
			List<PageData> houseList=findUserHouseCode(paDate);
			String houseID ="";
			String houseName ="";
			for (int i = 0; i < houseList.size(); i++) {
				if((i+1)==houseList.size()){
					houseID+=houseList.get(i).getString("house_code");
					houseName+=houseList.get(i).getString("house_name");
				}else{
					houseID+=houseList.get(i).getString("house_code")+",";
					houseName+=houseList.get(i).getString("house_name")+",";
				}
			}
			pageData.put("house_code", houseID);
			pageData.put("house_name", houseName);
			userlist.add(pageData);
		}

		return userlist;
	}
	
}
