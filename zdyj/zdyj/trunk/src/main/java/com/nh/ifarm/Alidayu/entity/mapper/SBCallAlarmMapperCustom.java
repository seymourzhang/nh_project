package com.nh.ifarm.Alidayu.entity.mapper;

import com.nh.ifarm.Alidayu.entity.SBCallAlarm;

import java.util.List;

public interface SBCallAlarmMapperCustom {
	int insertCallAlarmBatch(List<SBCallAlarm> list);
}