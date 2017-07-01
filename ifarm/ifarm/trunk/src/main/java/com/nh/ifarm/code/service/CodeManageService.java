package com.nh.ifarm.code.service;

import java.util.List;

import com.nh.ifarm.util.common.PageData;

/**
 * 2017-05-17
 * @author yoven
 * 编码业务处理
 */
public interface CodeManageService {
	
	/**
	 * 获取编码信息 
	 * 返回list
	 * @throws Exception
	 */
	public PageData findCodeInfo(PageData pd)throws Exception;
	
	List<PageData> getCodeList(PageData pd)  throws Exception ;
	
	public PageData saveCode(PageData pd)throws Exception;
	
	public PageData updateCode(PageData pd)throws Exception;
	
	public void deleteCode(PageData pd)throws Exception;
	
	List<PageData> getCodeType()  throws Exception ;

}
