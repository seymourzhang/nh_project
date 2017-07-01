package com.mtc.mapper.app;

import com.mtc.entity.app.SBUserRole;

import java.util.List;

public interface SBUserRoleMapperCustom {
	
	public int insertBatch(List<SBUserRole> items);
	
	public int update(SBUserRole tSBUserRole);
	 
	public int deleteByUserId(int id);
	
	public SBUserRole selectByUserId(int user_id);
	
	
}