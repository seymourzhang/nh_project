package com.nh.ifarm.goods.service.impl;

import com.nh.ifarm.goods.service.GoogsService;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class GoogsServiceImpl implements GoogsService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	public List<PageData> getGoodsList(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getGoodsList", pd);
	}
	
	public List<PageData> getGoodsList2(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getGoodsList2", pd);
	}

	public List<PageData> getGoodsList3(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getGoodsList3", pd);
	}
	
	public List<PageData> getCorporationGood(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getCorporationGood", pd);
	}
	
	public List<PageData> getSpec(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getSpec", pd);
	}
	
	public List<PageData> getUnit(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getUnit", pd);
	}
	
	public List<PageData> getCorporation(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getCorporation", pd);
	}
	
	public List<PageData> getCorporation2(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getCorporation2", pd);
	}
	
	public List<PageData> getCorporation3(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getCorporation3", pd);
	}
	
	public List<PageData> getFactory(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getFactory", pd);
	}
	public List<PageData> getFactory2(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getFactory2", pd);
	}
	public List<PageData> getFactory3(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getFactory3", pd);
	}
	public int saveStock(PageData pd) throws Exception {
		return (Integer) dao.save("SDGoodsMapper.saveStock", pd);
	}
	public int saveStockcChange(PageData pd) throws Exception {
		return (Integer) dao.save("SDGoodsMapper.saveStockcChange", pd);
	}
	
	public List<PageData> getStockChange(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getStockChange", pd);
	}
	public List<PageData> getStock(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getStock", pd);
	}
	public BigDecimal getSumCount(PageData pd) throws Exception {
		return (BigDecimal)dao.findForObject("SDGoodsMapper.getSumCount", pd);
	}
	
	public int editStock(PageData pd) throws Exception {
		return (Integer)dao.update("SDGoodsMapper.editStock", pd);
	}
	
	public List<PageData> getStockSum(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getStockSum", pd);
	}

	public List<PageData> getStockApproval(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getStockApproval", pd);
	}
	
	public List<PageData> getStockApproval2(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDGoodsMapper.getStockApproval2", pd);
	}

	public void approvalStockChange(List<PageData> list) throws Exception{
		dao.batchSave("SDGoodsMapper.approvalStockChange", list);
	}
	
	public void approvalStockChange2(List<PageData> list) throws Exception{
		dao.batchSave("SDGoodsMapper.approvalStockChange2", list);
	}

	public int updateRemindData(PageData pd) throws Exception{
		return (Integer) dao.save("SDGoodsMapper.exec_SP_STOCK_REMIND", pd);
	}
	
	public int saveCorporation(PageData pd) throws Exception {
		return (Integer) dao.save("SDGoodsMapper.saveCorporation", pd);
	}
	
	public int saveFactory(PageData pd) throws Exception {
		return (Integer) dao.save("SDGoodsMapper.saveFactory", pd);
	}
	
	public int saveGoods(PageData pd) throws Exception {
		return (Integer) dao.save("SDGoodsMapper.saveGoods", pd);
	}
	
	public int saveCorporationGoods(PageData pd) throws Exception {
		return (Integer) dao.save("SDGoodsMapper.saveCorporationGoods", pd);
	}
	
	public int editCorporation(PageData pd) throws Exception {
		return (Integer)dao.update("SDGoodsMapper.editCorporation", pd);
	}
	
	public int editFactory(PageData pd) throws Exception {
		return (Integer)dao.update("SDGoodsMapper.editFactory", pd);
	}
	
	public int editGoods(PageData pd) throws Exception {
		return (Integer)dao.update("SDGoodsMapper.editGoods", pd);
	}
	
	public int editCorporationGood(PageData pd) throws Exception {
		return (Integer)dao.update("SDGoodsMapper.editCorporationGood", pd);
	}
	
	public void updateStock(List<PageData> list) throws Exception{
		   dao.batchSave("SDGoodsMapper.updateStock", list);
	}

	/**
	 * 执行存储过程
	 * @param procName 存储过程名
	 * @throws Exception
	 */
	public void execProc(String procName) throws Exception{
		dao.findForObject("SDGoodsMapper." + procName, null);
	}

	/**
	 * 获取库存相关提醒信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getRemindMsg(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDGoodsMapper.getRemindMsg", pd);
	}
	
	public void deleteCorporationGoods(PageData pd) throws Exception{
		   dao.delete("SDGoodsMapper.deleteCorporationGoods", pd);
	}

}
