package com.nh.ifarm.user.service;

import java.util.List;

import com.nh.ifarm.util.common.PageData;

/**
 * 2016-7-6
 * @author varro
 * 用户业务处理
 */
public interface SDUserService {
	
	/**
	 * 登录判断  
	 * 返回 PageData  
	 */
	public PageData getUserBylogin(PageData pd)throws Exception;
	
	/**
	 * 获取用户信息 
	 * 返回list
	 * @throws Exception
	 */
	public List<PageData>  getUserInfo(PageData pd)throws Exception;
	
	public List<PageData>  getUserInfo2(PageData pd)throws Exception;
	
	public int saveUser(PageData pd)throws Exception;
	
	public void saveUserFarm(PageData pd)throws Exception;
	
	public void editUser(PageData pd)throws Exception;
	
	public void delUserFarm(PageData pd)throws Exception;
	
	public void delUserHouse(PageData pd)throws Exception;
	
	public void saveUserHouse(PageData pd)throws Exception;
	
	/**
	 * 查询用户信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	PageData findUserInfo(PageData pd)throws Exception;
	
	/**
	 * 根据用户id查询用户栋舍 
	 */
	List<PageData> findUserHouseCode(PageData pd) throws Exception;

	List<PageData> getUserHouses(PageData pd) throws Exception;

	/**
	 * 根据用户id查询用户权限
	 */
	List<PageData> getUserRights(PageData pd) throws Exception;

	List<PageData> getUserList(PageData pd)  throws Exception ;
	
	List<PageData> getUserList2(PageData pd)  throws Exception ;

}
