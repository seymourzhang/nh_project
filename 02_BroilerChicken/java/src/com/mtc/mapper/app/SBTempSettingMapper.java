package com.mtc.mapper.app;

import com.mtc.entity.app.SBTempSetting;
import java.util.List;

public interface SBTempSettingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_temp_setting
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_temp_setting
     *
     * @mbggenerated
     */
    int insert(SBTempSetting record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_temp_setting
     *
     * @mbggenerated
     */
    SBTempSetting selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_temp_setting
     *
     * @mbggenerated
     */
    List<SBTempSetting> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_temp_setting
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SBTempSetting record);
}