package com.mtc.mapper.app;

import com.mtc.entity.app.SLUserLogon;
import java.util.List;

public interface SLUserLogonMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_l_user_logon
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_l_user_logon
     *
     * @mbggenerated
     */
    int insert(SLUserLogon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_l_user_logon
     *
     * @mbggenerated
     */
    SLUserLogon selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_l_user_logon
     *
     * @mbggenerated
     */
    List<SLUserLogon> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_l_user_logon
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SLUserLogon record);
}