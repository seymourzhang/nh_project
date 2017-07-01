package com.nh.ifarm.util.service;

import java.util.List;

import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;

public interface OrganService {
	List<PageData> getOrgLevelList(PageData pd) throws Exception;
	List<PageData> getOrgList(PageData pd) throws Exception;
	List<PageData> getOrgListByRoleId(PageData pd) throws Exception;
	List<PageData> getCompanyByUserId(PageData pd) throws Exception;
	List<PageData> getFarmByUserId(PageData pd) throws Exception;
	List<PageData> getHouseListByUserId(PageData pd) throws Exception;

	/**
	 * 按user_id查询
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> selectOrgByUser(PageData pd) throws Exception;

    /***
     * 按user_id查询其下所有农场
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> getFarmListByUserId(PageData pd) throws Exception;
	List<PageData> getOrgListById(PageData pd) throws Exception;

	List<PageData> getHouseType(PageData pd) throws Exception;

	List<PageData> getOrganizationList(PageData pd) throws Exception;

	List<PageData> getMaxOrgLevelId(PageData pd) throws Exception;

	int insertOrg(PageData pd) throws Exception;

	int updateOrg(PageData pd) throws Exception;

	int deleteOrg(PageData pd) throws Exception;

	int setFarmMapping(PageData pd) throws Exception;
	int setHouseMapping(PageData pd) throws Exception;

	List<PageData> getFarmForMapping(PageData pd) throws Exception;

	List<PageData> getFarmUser(PageData pd) throws Exception;

	List<PageData> getAuthorityInfo(PageData pd) throws Exception;

	PageData getUserRole(PageData pd) throws Exception;

	PageData saveFarmMapping(PageData pd) throws Exception;
}
