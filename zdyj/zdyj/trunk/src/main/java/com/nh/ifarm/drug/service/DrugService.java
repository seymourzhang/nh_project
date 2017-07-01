package com.nh.ifarm.drug.service;

import java.util.List;

import com.nh.ifarm.util.common.PageData;

public interface DrugService {

	 /**
    * 按条件查询
    * @param pd
    * @return List<Drug>
    * @throws Exception
    */
	List<PageData> selectDrugPlan(PageData pd) throws Exception;
	
	
	/**
    * 查询药品疫苗使用表
    * @return List<Drug>
    * @throws Exception
    */
	List<PageData> selectDrugFact(PageData pd) throws Exception ;
	
	/**
	 * 新增药品疫苗计划
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	void saveDrugPlan(PageData pd) throws Exception;
	
	/**
	 * 删除药品疫苗计划
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	void deleteDrugPlan(PageData pd) throws Exception;
	
	/**
	 * 新增药品疫苗使用
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	void saveDrugFact(PageData pd) throws Exception;
	
	/**
	 * 删除药品疫苗使用
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	void deleteDrugFact(PageData pd) throws Exception;
	
	/**
	 * 查询用户所在农场
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	PageData selectFarm(PageData pd) throws Exception;
	
	/**
	 * 查询负责人集合
	 * @return pd
	 * @throws Exception
	 */
	List<PageData> selectUser(PageData pd) throws Exception;
	
	/**
	 * 查询物资
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectGoods(PageData pd) throws Exception;
	
	/**
	 * 查询物资
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectCode(PageData pd) throws Exception;
	
	/**
	 * 查询工厂
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectFactory(PageData pd) throws Exception;
	
}
