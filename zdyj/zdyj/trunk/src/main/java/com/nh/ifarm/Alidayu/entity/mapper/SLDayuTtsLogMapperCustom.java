package com.nh.ifarm.Alidayu.entity.mapper;


import com.nh.ifarm.Alidayu.entity.SLDayuTtsLog;

public interface SLDayuTtsLogMapperCustom {

    int updateByBizId(SLDayuTtsLog record);

	SLDayuTtsLog getSLDayuTtsLogByBizId(String bizId);
}