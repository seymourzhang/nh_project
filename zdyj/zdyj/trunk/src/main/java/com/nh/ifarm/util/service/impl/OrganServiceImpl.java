package com.nh.ifarm.util.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.nh.ifarm.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;
import com.nh.ifarm.util.service.OrganService;

@Service
public class OrganServiceImpl implements OrganService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Autowired
	private RoleService roleService;

	public List<PageData> getOrgLevelList(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getOrgLevelList", pd);
	}

	public List<PageData> getOrgList(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getOrgList", pd);
	}
	
	public List<PageData> getOrgListByRoleId(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getOrgListByRoleId", pd);
	}
	
	public List<PageData> getOrgListById(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getOrgListById", pd);
	}
	
	public List<PageData> getCompanyByUserId(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getCompanyByUserId", pd);
	};

	public List<PageData> getFarmByUserId(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getFarmByUserId", pd);
	};

	public List<PageData> getHouseListByUserId(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getHouseListByUserId", pd);
	};

	public List<PageData> selectOrgByUser(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getOrgListByUser", pd);
	}

    public List<PageData> getFarmListByUserId(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("SDOrganizationMapper.getFarmListByUserId", pd);
    };

    public List<PageData> getHouseType(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getHouseType",pd);
	}

	public List<PageData> getOrganizationList(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getOrganizationList", pd);
	}

	public List<PageData> getMaxOrgLevelId(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getMaxOrgLevelId", pd);
	}

	public int insertOrg(PageData pd) throws Exception {
		return (Integer) dao.save("SDOrganizationMapper.insertOrg", pd);
	}

	public int updateOrg(PageData pd) throws Exception {
		return (Integer) dao.update("SDOrganizationMapper.updateOrg", pd);
	}

	public int deleteOrg(PageData pd) throws Exception {
		return (Integer) dao.delete("SDOrganizationMapper.deleteOrg", pd);
	}

	public List<PageData> getFarmForMapping(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getFarmForMapping", pd);
	}

	public int setFarmMapping(PageData pd) throws Exception {
		return (Integer) dao.save("SDOrganizationMapper.setFarmMapping", pd);
	}

	public PageData saveFarmMapping(PageData pd) throws Exception {
		PageData rt = new PageData();
		int orgLevelId = Integer.valueOf(pd.getString("level_id"));
		String success ="success";
		String msg ="msg";

		List<PageData> maxLevelList = getMaxOrgLevelId(null);
		int maxOrgLevelId = Integer.valueOf(String.valueOf(maxLevelList.get(0).get("max_level_id")))  ;

		if((maxOrgLevelId-1) == orgLevelId){
			String orgStr = pd.getString("org");
			List<Integer> orgList = new ArrayList<>();

			if(orgStr.length()>1) {
				String[] tmpOrgs = orgStr.substring(0, orgStr.length()).split(",");
				for (String tmp : tmpOrgs) {
					orgList.add(Integer.valueOf(tmp));
				}
				pd.put("orgList",orgList);
				int i = setFarmMapping(pd);

				if(i > 0){
					for(int k=0; k<i; k++){
						PageData paramPd = new PageData();
//						Long objId = (Long)pd.get("id");
						paramPd.put("obj_id",orgList.get(k));
						paramPd.put("obj_type",2);
						paramPd.put("create_person",pd.get("user_id"));
						paramPd.put("user_id",pd.get("user_id"));
						roleService.insertRightsObj(paramPd);
					}
					rt.put(success,true);
					rt.put(msg,"");
				} else{
					rt.put(success,false);
					rt.put(msg,"系统内部错误");
				}
			} else{
				rt.put(success,false);
				rt.put(msg,"没有选择需要绑定的农场");
			}
		} else{
			rt.put(success,false);
			rt.put(msg,"该机构下不能直接绑定农场");
		}


		return rt;
	}

	public int setHouseMapping(PageData pd) throws Exception {
		return (Integer) dao.save("SDOrganizationMapper.setHouseMapping", pd);
	}

	public List<PageData> getFarmUser(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getFarmUser", pd);
	}

	public List<PageData> getAuthorityInfo(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("SDOrganizationMapper.getAuthorityInfo", pd);
	}
	public PageData getUserRole(PageData pd) throws Exception{
		return (PageData) dao.findForObject("SDOrganizationMapper.getUserRole", pd);
	}
}
