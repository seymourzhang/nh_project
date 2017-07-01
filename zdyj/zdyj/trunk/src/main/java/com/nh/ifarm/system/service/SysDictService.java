package com.nh.ifarm.system.service;

import java.util.List;

import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;


public interface SysDictService {
     
	/**
     * 新增
     * @param PageDate HashMap集合
     * @return
     * @author zl
     */
	void save(PageData pd)throws Exception;
	/**
     * 删除
     * @param PageDate HashMap集合
     * @return
     * @author zl
     */
	void delete(PageData pd)throws Exception;
	/**
     * 修改
     * @param PageDate HashMap集合
     * @return
     * @author zl
     */
	void edit(PageData pd)throws Exception;
	 /**
     * 列表
     * @param PageDate HashMap集合
     * @return	List
     * @author zl
     */
	List<PageData> sysDictlistPage(Page page)throws Exception;
	
	/**
     * 通过ID查找
     * @param PageDate HashMap集合
     * @return	PageDate
     * @author zl
     */
	public PageData findDictInfo(PageData pd)throws Exception;
	/**
     * 批量删除
     * @param String[] ArrayDATA_IDS 
     * @return
     * @author zl
     */
//	void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}
