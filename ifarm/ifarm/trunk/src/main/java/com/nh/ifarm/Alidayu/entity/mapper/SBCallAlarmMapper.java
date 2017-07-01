package com.nh.ifarm.Alidayu.entity.mapper;

import com.nh.ifarm.Alidayu.entity.SBCallAlarm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SBCallAlarmMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_call_alarm
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(@Param("mainCode") String mainCode, @Param("alarmId") Integer alarmId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_call_alarm
     *
     * @mbggenerated
     */
    int insert(SBCallAlarm record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_call_alarm
     *
     * @mbggenerated
     */
    SBCallAlarm selectByPrimaryKey(@Param("mainCode") String mainCode, @Param("alarmId") Integer alarmId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_call_alarm
     *
     * @mbggenerated
     */
    List<SBCallAlarm> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_call_alarm
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SBCallAlarm record);
}