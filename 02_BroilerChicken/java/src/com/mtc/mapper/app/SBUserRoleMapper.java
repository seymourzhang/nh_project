package com.mtc.mapper.app;

import com.mtc.entity.app.SBUserRole;
import java.util.List;

public interface SBUserRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_user_role
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_user_role
     *
     * @mbggenerated
     */
    int insert(SBUserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_user_role
     *
     * @mbggenerated
     */
    SBUserRole selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_user_role
     *
     * @mbggenerated
     */
    List<SBUserRole> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_b_user_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SBUserRole record);
}