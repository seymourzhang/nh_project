package com.mtc.mapper.app;

import com.mtc.entity.app.SBYincommMain;
import java.util.List;

public interface SBYincommMainMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_yincomm_main
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String mmSn);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_yincomm_main
     *
     * @mbggenerated
     */
    int insert(SBYincommMain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_yincomm_main
     *
     * @mbggenerated
     */
    SBYincommMain selectByPrimaryKey(String mmSn);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_yincomm_main
     *
     * @mbggenerated
     */
    List<SBYincommMain> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_yincomm_main
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SBYincommMain record);
}