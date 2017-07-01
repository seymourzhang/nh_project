package com.nh.ifarm.goods.service;

import java.math.BigDecimal;
import java.util.List;

import com.nh.ifarm.util.common.PageData;

public interface GoogsService {
	
	public List<PageData> getGoodsList(PageData pd) throws Exception;
	
	public List<PageData> getGoodsList2(PageData pd) throws Exception;
	
	public List<PageData> getCorporationGood(PageData pd) throws Exception;
	
	public List<PageData> getSpec(PageData pd) throws Exception;
	
	public List<PageData> getUnit(PageData pd) throws Exception;
	
	public List<PageData> getCorporation(PageData pd) throws Exception;
	
	public List<PageData> getCorporation2(PageData pd) throws Exception;
	
	public List<PageData> getCorporation3(PageData pd) throws Exception;
	
	public List<PageData> getFactory(PageData pd) throws Exception;
	
	public List<PageData> getFactory2(PageData pd) throws Exception;
	
	public List<PageData> getFactory3(PageData pd) throws Exception;
	
	public int saveStock(PageData pd)throws Exception;
	
	public int saveStockcChange(PageData pd)throws Exception;
	
	public List<PageData> getStockChange(PageData pd) throws Exception;
	
	public List<PageData> getStock(PageData pd) throws Exception;
	
	public BigDecimal getSumCount(PageData pd) throws Exception;
	
	public int editStock(PageData pd)throws Exception;
	
	public List<PageData> getStockSum(PageData pd) throws Exception;

	public List<PageData> getStockApproval(PageData pd) throws Exception;
	
	public List<PageData> getStockApproval2(PageData pd) throws Exception;

	public void approvalStockChange(List<PageData> list) throws Exception;
	
	public void approvalStockChange2(List<PageData> list) throws Exception;

	public int updateRemindData(PageData pd) throws Exception;
	
	public int saveCorporation(PageData pd)throws Exception;
	
	public int saveFactory(PageData pd)throws Exception;
	
	public int saveGoods(PageData pd)throws Exception;
	
	public int saveCorporationGoods(PageData pd)throws Exception;
	
	public int editCorporation(PageData pd)throws Exception;
	
	public int editFactory(PageData pd)throws Exception;
	
	public int editGoods(PageData pd)throws Exception;
	
	public int editCorporationGood(PageData pd)throws Exception;
	
	public void updateStock(List<PageData> list) throws Exception;

	/**
	 * 执行存储过程
	 * @param procName 存储过程名
	 * @throws Exception
	 */
	void execProc(String procName) throws Exception;

	/**
	 * 获取库存相关提醒信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> getRemindMsg(PageData pd) throws Exception;
	
	/**
	 * 删除供应商物资关系
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	void deleteCorporationGoods(PageData pd) throws Exception;
}
