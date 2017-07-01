package com.nh.ifarm.batch.service;

import com.nh.ifarm.util.common.PageData;

import java.util.HashMap;
import java.util.List;

/**
 * Created by LeLe on 11/2/2016.
 * 批次管理服务接口
 */
public interface BatchManageService {
	/**
     * 获取发雏数据
     * @return
     * @throws Exception
     */
    List<PageData> getFaChuData(PageData pd) throws Exception;
    
    /**
     * 获取发料数据
     * @return
     * @throws Exception
     */
    List<PageData> getFaLiaoData(PageData pd) throws Exception;
    
    /**
     * 获取发药数据
     * @return
     * @throws Exception
     */
    List<PageData> getFaYaoData(PageData pd) throws Exception;
    
    /**
     * 获取创建批次数据
     * @return
     * @throws Exception
     */
    List<PageData> getCreateBatchData(PageData pd) throws Exception;
    
    /**
     * 获取出栏日龄
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> getOverBatchAge(PageData pd) throws Exception;
    
    /**
     * 获取调出数量
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> getBatchDataCount(PageData pd) throws Exception;
    
    /**
     * 保存发雏数据
     * @return
     * @throws Exception
     */
    PageData saveFaChuData(PageData pd) throws Exception;
    
    /**
     * 保存发料/发药数据
     * @return
     * @throws Exception
     */
    PageData saveFaLiaoData(PageData pd) throws Exception;

    /**
     * 保存创建批次数据
     * @return
     * @throws Exception
     */
    PageData saveCreateBatchData(PageData pd) throws Exception;
    
    /**
     * 修改发雏数据
     * @param pd
     * @return
     * @throws Exception
     */
    PageData editFaChuData(PageData pd) throws Exception;
    
    /**
     * 删除发雏数据
     * @param pd
     * @return
     * @throws Exception
     */
    PageData delFaChuData(PageData pd) throws Exception;
    
    /**
     * 修改发料/发药数据
     * @param pd
     * @return
     * @throws Exception
     */
    PageData editFaLiaoData(PageData pd) throws Exception;
    
    /**
     * 删除发料/发药数据
     * @param pd
     * @return
     * @throws Exception
     */
    PageData delFaLiaoData(PageData pd) throws Exception;

    /**
     * 获取修改批次数据
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> getEditBatchData(PageData pd) throws Exception;

    /**
     * 保存修改批次数据
     * @return
     * @throws Exception
     */
    PageData saveEditBatchData(PageData pd) throws Exception;
    
    /**
     * 获取淘汰批次数据
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> getTaoTaiData(PageData pd) throws Exception;
    
    /**
     * 保存淘汰批次数据
     * @return
     * @throws Exception
     */
    PageData saveTaoTaiData(PageData pd) throws Exception;

    /**
     * 获取出栏批次数据
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> getOverBatchData(PageData pd) throws Exception;

    /**
     * 保存出栏批次数据
     * @return
     * @throws Exception
     */
    PageData saveOverBatchData(PageData pd) throws Exception;
    
    /**
     * 获取结算数据
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> getSettleData(PageData pd) throws Exception;
    List<PageData> getSettleData2(PageData pd) throws Exception;
    
    /**
     * 保存结算数据
     * @return
     * @throws Exception
     */
    int saveSettleData(PageData pd) throws Exception;
    
    /**
     * 修改结算数据
     * @return
     * @throws Exception
     */
    int editSettleData(PageData pd) throws Exception;
    
    /**
     * 获取结算子数据
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> getSettleSubData(PageData pd) throws Exception;
    
    /**
     * 保存结算子数据
     * @return
     * @throws Exception
     */
    int saveSettleSubData(PageData pd) throws Exception;
    
    /**
     * 修改结算子数据
     * @return
     * @throws Exception
     */
    int editSettleSubData(PageData pd) throws Exception;
    
    /**
	 * 删除结算子数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	void deleteSettleSubData(PageData pd) throws Exception;
    
    /**
     * 结算总金额
     * @return
     * @throws Exception
     */
    PageData priceCount(PageData pd) throws Exception;
    
    /**
     * 结算总金额
     * @return
     * @throws Exception
     */
    List<PageData> priceCount2(PageData pd) throws Exception;
    
    /**
     * 结算总数量和总重量
     * @return
     * @throws Exception
     */
    PageData dataSum(PageData pd) throws Exception;

    /***
     *
     * 查询批次信息for mobile
     * @param pd
     * @return
     * @throws Exception
     */
    PageData selectBatchDataForMobile(PageData pd) throws Exception;
    
    List<PageData> selectSBCode(PageData pd)throws Exception;
    List<PageData> getGoodsList(PageData pd) throws Exception;
    List<PageData> getGoodId(PageData pd) throws Exception;
    
    PageData selectSumjimiao(PageData pd) throws Exception;
    List<PageData> selectSumByGoodId(PageData pd) throws Exception;
    PageData selectSumsiliao(PageData pd) throws Exception;
    PageData selectSummiaoyao(PageData pd) throws Exception;
    
    /*** 养殖主数据    */
	List<PageData> getGoodsList2(PageData pd) throws Exception;
	
	PageData saveGoods(PageData pd)throws Exception;
	
	PageData updateGoods(PageData pd)throws Exception;
	
	List<PageData> getCorporationGood(PageData pd) throws Exception;
	
	int saveCorporationGoods(PageData pd)throws Exception;
	
	int editCorporationGood(PageData pd)throws Exception;
	
	/**
	 * 删除供应商物资关系
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	void deleteCorporationGoods(PageData pd) throws Exception;
	
    List<PageData> getSpec(PageData pd) throws Exception;
	
	List<PageData> getUnit(PageData pd) throws Exception;
	
	List<PageData> getCorporation(PageData pd) throws Exception;
	
	List<PageData> getCorporation2(PageData pd) throws Exception;
	
	List<PageData> getCorporation3(PageData pd) throws Exception;
	
	List<PageData> getFactory(PageData pd) throws Exception;
	
	List<PageData> getFactory2(PageData pd) throws Exception;
	
	List<PageData> getFactory3(PageData pd) throws Exception;

    /**
     * 获取所有物资的map集合
     * @return
     * @throws Exception
     */
    HashMap<String,Integer> getGoodsMap() throws Exception;
}
