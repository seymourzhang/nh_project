package com.mtc.mapper.app;

import com.mtc.entity.app.SBYincommSub;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SBYincommSubMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_yincomm_sub
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(@Param("mmSn") String mmSn, @Param("sensorCode") String sensorCode, @Param("sensorSn") Integer sensorSn);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_yincomm_sub
     *
     * @mbggenerated
     */
    int insert(SBYincommSub record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_yincomm_sub
     *
     * @mbggenerated
     */
    SBYincommSub selectByPrimaryKey(@Param("mmSn") String mmSn, @Param("sensorCode") String sensorCode, @Param("sensorSn") Integer sensorSn);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_yincomm_sub
     *
     * @mbggenerated
     */
    List<SBYincommSub> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_yincomm_sub
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SBYincommSub record);
}