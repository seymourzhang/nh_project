package com.mtc.mapper.app;

import com.mtc.entity.app.SDDevice;
import java.util.List;

public interface SDDeviceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_d_device
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String deviceCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_d_device
     *
     * @mbggenerated
     */
    int insert(SDDevice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_d_device
     *
     * @mbggenerated
     */
    SDDevice selectByPrimaryKey(String deviceCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_d_device
     *
     * @mbggenerated
     */
    List<SDDevice> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_d_device
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SDDevice record);
}