package com.nh.ifarm.Alidayu.entity.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface BaseQueryMapper {
    
    public String selectStringByAny(@Param("sql") String sql);
    
    public Integer selectIntergerByAny(@Param("sql") String sql);
    
    public List<HashMap<String,Object>> selectMapByAny(@Param("sql") String sql);
    
    public List<LinkedHashMap<String,Object>> selectMapByAny2(@Param("sql") String sql);
    
    public int updateIntergerByAny(@Param("sql") String sql);
    
    public int deleteByAny(@Param("sql") String sql);
}
