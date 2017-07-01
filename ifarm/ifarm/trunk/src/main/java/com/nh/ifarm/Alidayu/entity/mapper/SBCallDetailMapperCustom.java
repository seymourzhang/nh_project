package com.nh.ifarm.Alidayu.entity.mapper;

import com.nh.ifarm.Alidayu.entity.SBCallDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SBCallDetailMapperCustom {

	int insertCallDetails(List<SBCallDetail> list);

	SBCallDetail selectCallDetail(@Param("mainCode") String mainCode, @Param("callResult") String callResult);
}